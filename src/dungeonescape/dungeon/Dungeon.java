/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon;

import dungeonescape.dungeon.notifications.GameNotification;
import dungeonescape.dungeonobject.actions.move.Direction;
import dungeonescape.dungeonobject.characters.Ghost;
import dungeonescape.dungeonobject.characters.Guard;
import dungeonescape.dungeonobject.characters.Player;
import dungeonescape.dungeonobject.construction.Wall;
import dungeonescape.dungeonobject.mine.TargetBoundaries;
import dungeonescape.dungeonobject.mine.TargetBoundary;
import dungeonescape.play.GameSession;
import dungeonescape.space.DungeonSpace;
import dungeonescape.space.Position;
import java.util.List;

/**
 *
 * @author Andrew
 */
public class Dungeon {

    private Player player;
    private DungeonSpace[][] dungeon;
    private List<Guard> guards;
    private List<Ghost> ghosts;

    private final GameSession gameSession;

    public Dungeon(GameSession gameSession) throws GameNotification {
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
    private DungeonSpace[][] generateDungeon() throws GameNotification {

        int width = gameSession.getDugeonConfiguration().getDungeonWidth();
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

        DungeonConfiguration dungeonConfiguration = gameSession.getDugeonConfiguration();

        //place the player at the start of the maze
        Position startPosition = new Position(startX, startY);
        player = new Player(gameSession.getPlayerName(), dungeonConfiguration.getPlayerVisibility());
        dungeon[startPosition.getPositionX()][startPosition.getPositionY()] = new DungeonSpace(startPosition, player);

        //Cut out the exit paths
        dungeon = DungeonConstructionUtil.cutExitPaths(dungeon, dungeonConfiguration.getDungeonExitCount());

        //Define the target boundaries for the mines
        TargetBoundaries targetBoundaries = new TargetBoundaries();
        targetBoundaries.addTargetBoundary(new TargetBoundary(1, width / 4, .6));
        targetBoundaries.addTargetBoundary(new TargetBoundary(width / 4 + 1, width / 2, .4));

        //lay the freeze mines
        dungeon = DungeonConstructionUtil.placeFreezeMines(dungeon, 
                dungeonConfiguration.getNumberOfFreezeMines(), dungeonConfiguration.getMaxFreezeTime(), targetBoundaries);

        //lay the teleport mines
        dungeon = DungeonConstructionUtil.placeTeleportMines(dungeon, 
                dungeonConfiguration.getNumberOfTeleportMines(), targetBoundaries);
        
        //place the guards
        guards = DungeonCharacterUtil.placeGuards(dungeon, dungeonConfiguration.getNumberOfGuards());

        //place the ghosts, offset 1/4 of the total dungeon width from the border
        int offset = dungeon.length / 4;
        ghosts = DungeonCharacterUtil.placeGhosts(dungeon, dungeonConfiguration.getNumberOfGhosts(), offset);

        System.out.println(DungeonMapViewUtil.getFullDungeonAsString(dungeon, null));
        return dungeon;
    }

    

    public String movePlayer(Direction direction) throws GameNotification {

        player.move(direction);
        
        int playerVisibility = gameSession.getDugeonConfiguration().getPlayerVisibility();
        return DungeonMapViewUtil.getPlayerMiniMap(dungeon, player, playerVisibility);
    }

}
