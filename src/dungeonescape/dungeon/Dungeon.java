/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon;

import dungeonescape.dungeonobject.actions.move.Direction;
import dungeonescape.dungeonobject.characters.Player;
import dungeonescape.dungeonobject.construction.Wall;
import dungeonescape.dungeonobject.mine.FreezeMine;
import dungeonescape.dungeonobject.mine.FreezeTime;
import dungeonescape.dungeonobject.mine.Mine;
import dungeonescape.dungeonobject.mine.TargetBoundaries;
import dungeonescape.dungeonobject.mine.TargetBoundary;
import dungeonescape.dungeonobject.mine.TeleportDestination;
import dungeonescape.dungeonobject.mine.TeleportMine;
import dungeonescape.play.GameSession;
import dungeonescape.play.GameState;
import dungeonescape.space.DungeonSpace;
import dungeonescape.space.DungeonSpaceType;
import dungeonescape.space.Position;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Andrew
 */
public class Dungeon {
    
    private Player player;
    private DungeonSpace[][] dungeon;

    private final GameSession gameSession;
    
    public Dungeon(GameSession gameSession) {
        this.gameSession = gameSession;
                
        dungeon = generateDungeon();
    }
    
    protected DungeonSpace[][] getDungeon() {
        return dungeon;
    }
    
    protected GameSession getGameSession() {
        return gameSession;
    }

    /**
     * Maze will be a square with height equal to width.
     *
     * @return
     */
    private DungeonSpace[][] generateDungeon() {
        
        int width = gameSession.getDugeonConfiguration().getWidth();
        int height = width;
        
        dungeon = new DungeonSpace[width][width];

        //Fill the maze
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                dungeon[col][row] = new DungeonSpace(new Position(col, row), new Wall());
            }
        }

        int startX = width / 2;
        int startY = height / 2;

        //place a $ at the start of the maze
        Position startPosition = new Position(startX, startY);
        player = new Player(gameSession.getPlayerName());
        dungeon[startPosition.getPositionX()][startPosition.getPositionY()] = new DungeonSpace(startPosition, player);
        
        DungeonConfiguration dungeonConfiguration = gameSession.getDugeonConfiguration();

        //Cut out the exit paths
        dungeon = DungeonUtil.cutExitPaths(dungeon, dungeonConfiguration.getExitCount());
        
        //Define the target boundaries for the mines
        TargetBoundaries targetBoundaries = new TargetBoundaries();
        targetBoundaries.addTargetBoundary(new TargetBoundary(1, width / 4, .6));
        targetBoundaries.addTargetBoundary(new TargetBoundary(width / 4 + 1, width / 2, .4));

        //lay the freeze mines
        System.out.println("Placing freeze mines.");
        dungeon = placeFreezeMines(dungeon, dungeonConfiguration.getNumberOfFreezeMines(), dungeonConfiguration.getMaxFreezeTime(), targetBoundaries);

        //lay the teleport mines
        System.out.println("Placing teleport mines.");
        dungeon = placeTeleportMines(dungeon, dungeonConfiguration.getNumberOfTeleportMines(), targetBoundaries);

        System.out.println(DungeonUtil.getFullDungeonAsString(dungeon, null));

        return dungeon;
    }

    private DungeonSpace[][] placeFreezeMines(DungeonSpace[][] dungeon, int numberOfFreezeMines, FreezeTime maxFreezeTime, TargetBoundaries targetBoundaries) {

        List<Mine> freezeMines = new ArrayList<>();
        for (int i = 0; i < numberOfFreezeMines; i++) {
            int freezeTime = ThreadLocalRandom.current().nextInt(1, maxFreezeTime.getTime() + 1);
            freezeMines.add(new FreezeMine(new FreezeTime(freezeTime, maxFreezeTime.getTimeUnit())));
        }
        
        return DungeonUtil.deployMines(dungeon, freezeMines, targetBoundaries);
    }

    private DungeonSpace[][] placeTeleportMines(DungeonSpace[][] dungeon, int numberOfTeleportMines, TargetBoundaries targetBoundaries) {

        List<Mine> teleportMines = new ArrayList<>();
        List<DungeonSpace> openSpaces = DungeonUtil.getOpenSpaces(dungeon);
        Set<Integer> usedOpenSpaces = new HashSet<>();
        for (int i = 0; i < numberOfTeleportMines; i++) {
            Integer teleportIndex = ThreadLocalRandom.current().nextInt(0, openSpaces.size());
            while (usedOpenSpaces.contains(teleportIndex)) {
                teleportIndex = ThreadLocalRandom.current().nextInt(0, openSpaces.size());
            }
            
            //provide a teleport location
            DungeonSpace teleportSpace = openSpaces.get(teleportIndex);
            teleportSpace.setDungeonObject(new TeleportDestination());
            teleportMines.add(new TeleportMine(teleportSpace));
            usedOpenSpaces.add(teleportIndex);
        }
        
        return DungeonUtil.deployMines(dungeon, teleportMines, targetBoundaries);
    }
    
    public GameState movePlayer(Direction direction) {
        //do stuff
        boolean isLost = false;
        boolean isWon = false;
        Position playerPosition = player.getPosition();
        int playerVisibility = gameSession.getDugeonConfiguration().getPlayerVisibility();
        String playerBoard = DungeonUtil.getFullPlayerDungeonAsString(dungeon, playerPosition, playerVisibility);
        return new GameState(isLost, isWon, playerBoard);
    }

}
