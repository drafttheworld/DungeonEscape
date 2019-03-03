/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon;

import dungeonescape.dungeon.notifications.GameNotification;
import dungeonescape.dungeon.notifications.PlayerNotFoundNotification;
import dungeonescape.play.Direction;
import dungeonescape.dungeonobject.characters.DungeonCharacter;
import dungeonescape.dungeonobject.characters.Player;
import dungeonescape.dungeonobject.construction.Wall;
import dungeonescape.dungeonobject.mine.TargetBoundaries;
import dungeonescape.dungeonobject.mine.TargetBoundary;
import dungeonescape.space.DungeonSpace;
import dungeonescape.space.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Andrew
 */
public class Dungeon {

    private List<Player> players;
    private int dungeonTimeElapsed;
    private DungeonSpace[][] dungeon;
    private final List<DungeonCharacter> nonPlayerCharacters;

    private final DungeonConfiguration dungeonConfiguration;

    public Dungeon(DungeonConfiguration dungeonConfiguration) throws GameNotification {
        this.dungeonConfiguration = dungeonConfiguration;

        nonPlayerCharacters = new ArrayList<>();
        dungeon = generateDungeon();
        dungeonTimeElapsed = 0;
    }

    public Player getPlayer() {
        return players.get(0);
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
                dungeon[col][row] = new DungeonSpace(new Position(col, row));
                if (col != centerX && row != centerY) {
                    dungeon[col][row].addDungeonObject(new Wall());
                }
            }
        }

        //place the player at the center of the dungeon
        //Currently only support one player, TODO add support for multiple players
        Position startPosition = new Position(centerX, centerY);
        for (String playerName : dungeonConfiguration.getPlayerNames()) {
            Player player = new Player(playerName, dungeonConfiguration.getPlayerVisibility());
            dungeon[startPosition.getPositionX()][startPosition.getPositionY()] = new DungeonSpace(startPosition);
            dungeon[startPosition.getPositionX()][startPosition.getPositionY()].addDungeonObject(player);
            players.add(player);
        }

        //Cut out the exit paths
        dungeon = DungeonConstructionUtil.cutExitPaths(dungeon, dungeonConfiguration.getDungeonExitCount());

        //Define the target boundaries for the mines
        TargetBoundaries targetBoundaries = new TargetBoundaries();
        targetBoundaries.addTargetBoundary(new TargetBoundary(1, width / 4, .6));
        targetBoundaries.addTargetBoundary(new TargetBoundary(width / 4 + 1, width / 2, .4));

        //lay the freeze mines
        dungeon = DungeonConstructionUtil.placeFreezeMines(dungeon,
                dungeonConfiguration.getNumberOfFreezeMines(), dungeonConfiguration.getMaxFreezeMineTime(), targetBoundaries);

        //lay the teleport mines
        dungeon = DungeonConstructionUtil.placeTeleportMines(dungeon,
                dungeonConfiguration.getNumberOfTeleportMines(), targetBoundaries);

        //place the guards
        nonPlayerCharacters.addAll(DungeonCharacterUtil.placeGuards(dungeon, dungeonConfiguration.getNumberOfGuards()));

        //place the ghosts, offset 1/4 of the total dungeon width from the border
        int borderOffset = dungeon.length / 4;
        nonPlayerCharacters.addAll(DungeonCharacterUtil.placeGhosts(dungeon,
                dungeonConfiguration.getNumberOfGhosts(), dungeonConfiguration.getGhostFreezeTime(), borderOffset));

        System.out.println(DungeonMapViewUtil.getFullDungeonAsString(dungeon, null));
        return dungeon;
    }

    public void movePlayer(Direction direction, String playerName) throws GameNotification {
        Player player = getPlayer(playerName);
        
        players.get(0).move(direction, dungeon);
        moveNonPlayerCharacters();
        dungeonTimeElapsed++;

        while (player.isFrozen()) {
            moveNonPlayerCharacters();
            player.decrementFrozenTimeRemaining(1, TimeUnit.MILLISECONDS);
            dungeonTimeElapsed++;
        }

    }

    private void moveNonPlayerCharacters() throws GameNotification {
        for (DungeonCharacter npc : nonPlayerCharacters) {
            npc.move(null, dungeon);
        }
    }

    public String generatePlayerMiniMap(String playerName) throws PlayerNotFoundNotification {
        return DungeonMapViewUtil.getPlayerMiniMap(dungeon, getPlayer(playerName), dungeonConfiguration.getMiniMapVisibility());
    }
    
    private Player getPlayer(String playerName) throws PlayerNotFoundNotification {
        return players.stream()
                .filter(playerN -> playerName.equals(playerN.getPlayerName()))
                .findFirst()
                .orElseThrow(() -> new PlayerNotFoundNotification("Player " + playerName + " not found."));
    }

}
