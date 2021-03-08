/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon;

import dungeonescape.dungeon.notifications.ActionNotAllowedNotification;
import dungeonescape.dungeon.notifications.ExecutionErrorNotification;
import dungeonescape.dungeon.notifications.GameOverNotification;
import dungeonescape.dungeon.notifications.NotificationManager;
import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.dungeonobject.DungeonObjectTrack;
import dungeonescape.dungeonobject.characters.DungeonCharacter;
import dungeonescape.dungeonobject.characters.DungeonMaster;
import dungeonescape.dungeonobject.characters.Ghost;
import dungeonescape.dungeonobject.characters.Guard;
import dungeonescape.dungeonobject.characters.NonPersonDungeonCharacter;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andrew
 */
public class Dungeon {

    private final ExecutorService executorService =
        Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private final List<DungeonCharacter> nonPlayerCharacters;
    private final List<DungeonObject> moveableDungeonObjects;
    private final List<DungeonObjectTrack> dungeonObjectTracks;
    private final DungeonConfiguration dungeonConfiguration;

    private Player player;
    private int dungeonTimeElapsed;
    private DungeonSpace[][] dungeon;
    private int numberOfOpenDungeonSpaces;

    public Dungeon(DungeonConfiguration dungeonConfiguration) {
        this.dungeonConfiguration = dungeonConfiguration;

        nonPlayerCharacters = new ArrayList<>();
        moveableDungeonObjects = new ArrayList<>();
        dungeonObjectTracks = Collections.synchronizedList(new ArrayList<>());
        dungeon = generateDungeon();
        dungeonTimeElapsed = 0;
    }

    /**
     * Maze will be a square with height equal to width.
     *
     * @return
     */
    private DungeonSpace[][] generateDungeon() {

        int width = dungeonConfiguration.getDungeonWidth();
        int height = width;

        dungeon = new DungeonSpace[width][width];

        int centerX = width / 2;
        int centerY = height / 2;

        // Fill the maze
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                dungeon[row][col] = new DungeonSpace(new Position(col, row));
                dungeon[row][col].addDungeonObject(new Wall());
            }
        }

        // Cut out the exit paths
        dungeon = DungeonConstructionUtil.cutExitPaths(dungeon, dungeonConfiguration.getDungeonExitCount());

        // Create and place the player at the center of the dungeon
        dungeon[centerX][centerY].clearDungeonObjects();
        String playerName = dungeonConfiguration.getPlayerName();
        player = new Player(playerName, dungeonConfiguration.getPlayerVisibility(), dungeon);
        dungeon[centerX][centerY].addDungeonObject(player);
        player.revealCurrentMapArea();
        moveableDungeonObjects.add(player);

        numberOfOpenDungeonSpaces = DungeonConstructionUtil.getOpenSpaces(dungeon).size();
        System.out.println("Number of open dungeon spaces: " + numberOfOpenDungeonSpaces);

        // Define the target boundaries for the mines
        TargetBoundaries targetBoundaries = new TargetBoundaries();
        targetBoundaries.addTargetBoundary(new TargetBoundary(1, width / 4, .6));
        targetBoundaries.addTargetBoundary(new TargetBoundary(width / 4 + 1, width / 2, .4));

        // Lay the freeze mines
        List<Mine> freezeMines = DungeonConstructionUtil.placeFreezeMines(dungeon, dungeonConfiguration.getFreezeMineCount(),
            dungeonConfiguration.getFreezeMineMaxFreezeTime(), targetBoundaries);
        moveableDungeonObjects.addAll(freezeMines);
        System.out.println("Layed " + freezeMines.size() + " freeze mines.");

        // Lay the teleport mines
        List<Mine> teleportMines = DungeonConstructionUtil.placeTeleportMines(dungeon,
            dungeonConfiguration.getTeleportMineCount(), targetBoundaries);
        moveableDungeonObjects.addAll(teleportMines);
        System.out.println("Layed " + teleportMines.size() + " teleport mines.");

        // Place the guards
        List<Guard> guards = DungeonCharacterUtil.placeGuards(dungeon, dungeonConfiguration.getGuardCount());
        guards.forEach(guard -> {
            guard.setDetectionDistance(dungeonConfiguration.getGuardDetectionDistance());
            guard.setNumberOfSpacesToMoveWhenHunting(dungeonConfiguration.getGuardNumberOfMovesWhenHunting());
            guard.setNumberOfSpacesToMoveWhenPatrolling(dungeonConfiguration.getGuardNumberOfMovesWhenPatrolling());
        });
        nonPlayerCharacters.addAll(guards);
        moveableDungeonObjects.addAll(guards);
        System.out.println("Added " + guards.size() + " guards.");

        // Place the ghosts, offset 1/4 of the total dungeon width from the border
        int borderOffset = dungeon.length / 4;
        List<Ghost> ghosts = DungeonCharacterUtil.placeGhosts(dungeon,
            dungeonConfiguration.getGhostCount(), dungeonConfiguration.getGhostFreezeTime(), borderOffset);
        ghosts.forEach(ghost -> {
            ghost.setDetectionDistance(dungeonConfiguration.getGhostDetectionDistance());
            ghost.setNumberOfSpacesToMoveWhenHunting(dungeonConfiguration.getGhostNumberOfMovesWhenHunting());
            ghost.setNumberOfSpacesToMoveWhenPatrolling(dungeonConfiguration.getGhostNumberOfMovesWhenPatrolling());
        });
        nonPlayerCharacters.addAll(ghosts);
        moveableDungeonObjects.addAll(ghosts);
        System.out.println("Added " + ghosts.size() + " ghosts.");

        moveableDungeonObjects.forEach(dungeonObject -> {
            dungeonObjectTracks.add(new DungeonObjectTrack(dungeonObject.getPosition(),
                dungeonObject.getDungeonSpace().getVisibleDungeonSpaceType().getValueString()));
        });

        return dungeon;
    }

    public void movePlayer(Direction direction) {

        //check to see if the player has been caught by a dungeon master
        if (player.hasWon() || player.hasLost()) {
            String gameResult = player.hasWon() ? "You Won!" : "You lost.";
            NotificationManager.notify(
                new GameOverNotification("Cannot move player after game has ended (" + gameResult + ")."));
            return;
        }

        cleanupExpendedItems();

        if (!player.isFrozen()) {
            try {
                List<DungeonObjectTrack> playerTracks = player.move(direction);
                if (playerTracks.isEmpty()) {
                    return;
                }
                dungeonObjectTracks.addAll(playerTracks);
            } catch (UnsupportedOperationException e) {
                e.printStackTrace();
                NotificationManager.notify(new ActionNotAllowedNotification(e.getMessage()));
                return;
            }
            moveNonPlayerCharacters();
            dungeonTimeElapsed++;
        } else if (player.getFrozenTurnsRemaining().getTurns() > 0) {
            player.decrementFrozenTurnsRemaining(1);
            moveNonPlayerCharacters();
            dungeonTimeElapsed++;
        }

        cleanupDungeonTracks();
    }

    private void cleanupDungeonTracks() {

        Map<Position, DungeonObjectTrack> dotMap = new HashMap<>();
        for (DungeonObjectTrack dot : dungeonObjectTracks) {
            Position dotPosition = dot.getPosition();
            String dungeonSpaceCharacter = dungeon[dotPosition.getPositionY()][dotPosition.getPositionX()]
                .getVisibleDungeonSpaceType().getValueString();
            dotMap.putIfAbsent(dotPosition, new DungeonObjectTrack(dotPosition, dungeonSpaceCharacter));
        }
        dungeonObjectTracks.clear();
        dotMap.values().forEach(dot -> {
            dungeonObjectTracks.add(dot);
        });
    }

    private void cleanupExpendedItems() {

        Iterator<DungeonCharacter> npcIterator = nonPlayerCharacters.iterator();
        while (npcIterator.hasNext()) {
            DungeonCharacter npc = npcIterator.next();
            if (!npc.isActive()) {
                npcIterator.remove();
            }
        }

        dungeonObjectTracks.clear();
    }

    private void moveNonPlayerCharacters() {

        List<Future<?>> futures = new ArrayList<>();
        for (DungeonCharacter dungeonCharacter : nonPlayerCharacters) {
            NonPersonDungeonCharacter npc = (NonPersonDungeonCharacter) dungeonCharacter;
            try {
                Future future =
                    executorService.submit(() -> dungeonObjectTracks.addAll(npc.move(dungeon, player)));
                futures.add(future);
            } catch (RuntimeException e) {
                NotificationManager.notify(new ExecutionErrorNotification(e.getMessage()));
            }
        }

        futures.parallelStream().forEach(future -> {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    public void spawnDungeonMasters() {

        List<DungeonMaster> dungeonMasters =
            DungeonCharacterUtil.placeDungeonMasters(dungeon, dungeonConfiguration.getDungeonMasterCount());
        nonPlayerCharacters.addAll(dungeonMasters);
        moveableDungeonObjects.addAll(dungeonMasters);
        dungeonMasters.forEach(dungeonMaster -> {
            dungeonObjectTracks.add(new DungeonObjectTrack(dungeonMaster.getPosition(),
                dungeonMaster.getDungeonSpace().getVisibleDungeonSpaceType().getValueString()));
        });
    }

    public String generatePlayerMap() {
        return DungeonMapViewUtil.getPlayerMap(dungeon);

        // use the below line to a full view of the exposed dungeon
//        return DungeonMapViewUtil.getFullDungeonAsString(dungeon, null);
    }

    public List<DungeonObjectTrack> getDungeonObjectTracks() {
        return Collections.unmodifiableList(dungeonObjectTracks);
    }

    public DungeonSpace[][] getDungeon() {
        return dungeon;
    }

    public Player getPlayer() {
        return player;
    }

    public int getDungeonTimeElapsed() {
        return dungeonTimeElapsed;
    }
}
