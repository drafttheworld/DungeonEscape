/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon;

import dungeonescape.dungeon.notifications.ActionNotAllowedNotification;
import dungeonescape.dungeon.notifications.GameNotification;
import dungeonescape.dungeon.notifications.PlayerNotFoundNotification;
import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.dungeonobject.DungeonObjectTrack;
import dungeonescape.dungeonobject.characters.DungeonCharacter;
import dungeonescape.dungeonobject.characters.DungeonMaster;
import dungeonescape.dungeonobject.characters.Ghost;
import dungeonescape.dungeonobject.characters.Guard;
import dungeonescape.dungeonobject.characters.Player;
import dungeonescape.dungeonobject.construction.Wall;
import dungeonescape.dungeonobject.mine.Mine;
import dungeonescape.dungeonobject.mine.TargetBoundaries;
import dungeonescape.dungeonobject.mine.TargetBoundary;
import dungeonescape.play.Direction;
import dungeonescape.space.DungeonSpace;
import dungeonescape.space.Position;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Andrew
 */
public class Dungeon {

    private final List<Player> players;
    private int dungeonTimeElapsed;
    private DungeonSpace[][] dungeon;
    private final List<DungeonCharacter> nonPlayerCharacters;
    private final List<DungeonObject> moveableDungeonObjects;
    private final List<DungeonObjectTrack> dungeonObjectTracks;

    private final DungeonConfiguration dungeonConfiguration;

    public Dungeon(DungeonConfiguration dungeonConfiguration) throws GameNotification {
        this.dungeonConfiguration = dungeonConfiguration;

        players = new ArrayList<>();
        nonPlayerCharacters = new ArrayList<>();
        moveableDungeonObjects = new ArrayList<>();
        dungeonObjectTracks = new ArrayList<>();
        dungeon = generateDungeon();
        dungeonTimeElapsed = 0;
    }

    /**
     * Maze will be a square with height equal to width.
     *
     * @return
     */
    private DungeonSpace[][] generateDungeon() throws GameNotification {

        int width = dungeonConfiguration.getDungeonWidth();
        int height = width;

        dungeon = new DungeonSpace[width][width];

        int centerX = width / 2;
        int centerY = height / 2;

        //Fill the maze
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                dungeon[row][col] = new DungeonSpace(new Position(col, row));
                dungeon[row][col].addDungeonObject(new Wall());
            }
        }

        //Cut out the exit paths
        dungeon = DungeonConstructionUtil.cutExitPaths(dungeon, dungeonConfiguration.getDungeonExitCount());

        //place the player at the center of the dungeon
        //Currently only support one player, TODO add support for multiple players
        dungeon[centerX][centerY].clearDungeonObjects();
        for (String playerName : dungeonConfiguration.getPlayerNames()) {
            Player player = new Player(playerName, dungeonConfiguration.getPlayerVisibility());
            dungeon[centerX][centerY].addDungeonObject(player);
            players.add(player);
            moveableDungeonObjects.add(player);
        }

        //Define the target boundaries for the mines
        TargetBoundaries targetBoundaries = new TargetBoundaries();
        targetBoundaries.addTargetBoundary(new TargetBoundary(1, width / 4, .6));
        targetBoundaries.addTargetBoundary(new TargetBoundary(width / 4 + 1, width / 2, .4));

        //lay the freeze mines
        List<Mine> freezeMines = DungeonConstructionUtil.placeFreezeMines(dungeon,
                dungeonConfiguration.getNumberOfFreezeMines(), dungeonConfiguration.getMaxFreezeMineTime(), targetBoundaries);
        moveableDungeonObjects.addAll(freezeMines);

        //lay the teleport mines
        List<Mine> teleportMines = DungeonConstructionUtil.placeTeleportMines(dungeon,
                dungeonConfiguration.getNumberOfTeleportMines(), targetBoundaries);
        moveableDungeonObjects.addAll(teleportMines);

        //place the guards
        List<Guard> guards = DungeonCharacterUtil.placeGuards(dungeon, dungeonConfiguration.getNumberOfGuards());
        guards.forEach(guard -> {
            guard.setDetectionDistance(dungeonConfiguration.getGuardDetectionDistance());
            guard.setNumberOfSpacesToMoveWhenHunting(dungeonConfiguration.getGuardNumberOfMovesWhenHunting());
            guard.setNumberOfSpacesToMoveWhenPatrolling(dungeonConfiguration.getGuardNumberOfMovesWhenPatrolling());
        });
        nonPlayerCharacters.addAll(guards);
        moveableDungeonObjects.addAll(guards);

        //place the ghosts, offset 1/4 of the total dungeon width from the border
        int borderOffset = dungeon.length / 4;
        List<Ghost> ghosts = DungeonCharacterUtil.placeGhosts(dungeon,
                dungeonConfiguration.getNumberOfGhosts(), dungeonConfiguration.getGhostFreezeTime(), borderOffset);
        ghosts.forEach(ghost -> {
            ghost.setDetectionDistance(dungeonConfiguration.getGhostDetectionDistance());
            ghost.setNumberOfSpacesToMoveWhenHunting(dungeonConfiguration.getGhostNumberOfMovesWhenHunting());
            ghost.setNumberOfSpacesToMoveWhenPatrolling(dungeonConfiguration.getGhostNumberOfMovesWhenPatrolling());
        });
        nonPlayerCharacters.addAll(ghosts);
        moveableDungeonObjects.addAll(ghosts);

        moveableDungeonObjects.forEach(dungeonObject -> {
            dungeonObjectTracks.add(new DungeonObjectTrack(dungeonObject, 
                    dungeonObject.getDungeonSpace().getVisibleDungeonSpaceType().getValueString()));
        });

        return dungeon;
    }

    public void movePlayer(Direction direction, String playerName) throws GameNotification {
        Player player = getPlayer(playerName);

        updateMoveableDungeonObjectTracksPreviousPositions();
        if (!player.isFrozen()) {
            try {
                players.get(0).move(direction, dungeon);
            } catch (ActionNotAllowedNotification n) {
                return;
            }
            moveNonPlayerCharacters();
            dungeonTimeElapsed++;
        } else if (player.getFrozenTimeRemaining().getFreezeTimeForTimeUnit(TimeUnit.MINUTES) > 0) {
            player.decrementFrozenTimeRemaining(1, TimeUnit.MINUTES);
            moveNonPlayerCharacters();
            dungeonTimeElapsed++;
        }

        updateMoveableDungeonObjectTracksNewPositions();
    }

    private void updateMoveableDungeonObjectTracksPreviousPositions() {
        dungeonObjectTracks.forEach(dungeonObjectTrack -> {
            int indexOfMoveableObject = moveableDungeonObjects.indexOf(dungeonObjectTrack.getDungeonObject());
            dungeonObjectTrack.setPreviousPosition(moveableDungeonObjects.get(indexOfMoveableObject).getPosition());
        });
    }

    private void updateMoveableDungeonObjectTracksNewPositions() {
        dungeonObjectTracks.forEach(dungeonObjectTrack -> {
            //Set the previous position and dungeon object
            int index = moveableDungeonObjects.indexOf(dungeonObjectTrack.getDungeonObject());
            Position prevPosition = dungeonObjectTrack.getPreviousPosition();
            DungeonSpace previousDungeonSpace = dungeon[prevPosition.getPositionY()][prevPosition.getPositionX()];
            dungeonObjectTrack.setPreviousPositionSymbol(previousDungeonSpace.getVisibleDungeonSpaceType().getValueString());
        });
    }

    private void moveNonPlayerCharacters() throws GameNotification {
        try {
            for (DungeonCharacter npc : nonPlayerCharacters) {
                npc.move(null, dungeon);
            }
        } catch (GameNotification | RuntimeException e) {
            System.out.println("Caught exception: " + e.getMessage());
            throw e;
        }
    }

    public void spawnDungeonMasters() throws GameNotification {
        int numberOfDungeonMasters = dungeonConfiguration.getNumberOfDungeonMasters();
        List<DungeonMaster> dungeonMasters = DungeonCharacterUtil.placeDungeonMasters(dungeon, numberOfDungeonMasters);
        nonPlayerCharacters.addAll(dungeonMasters);
        moveableDungeonObjects.addAll(dungeonMasters);
        dungeonMasters.forEach(dungeonMaster -> {
            dungeonObjectTracks.add(new DungeonObjectTrack(dungeonMaster, 
                    dungeonMaster.getDungeonSpace().getVisibleDungeonSpaceType().getValueString()));
        });
    }

    public String generatePlayerMiniMap(String playerName) throws PlayerNotFoundNotification {
//        return DungeonMapViewUtil.getPlayerMiniMap(dungeon, getPlayer(playerName), dungeonConfiguration.getMiniMapVisibility());
        return DungeonMapViewUtil.getFullDungeonAsString(dungeon, null);
    }

    public List<DungeonObjectTrack> getDungeonObjectTracks() {
        return Collections.unmodifiableList(dungeonObjectTracks);
    }

    private Player getPlayer(String playerName) throws PlayerNotFoundNotification {
        return players.stream()
                .filter(playerN -> playerName.equals(playerN.getPlayerName()))
                .findFirst()
                .orElseThrow(() -> new PlayerNotFoundNotification("Player " + playerName + " not found."));
    }
    
    public int getDungeonTimeElapsed() {
        return dungeonTimeElapsed;
    }

}
