/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.characters.pathfinder;

import dungeonescape.dungeonobject.characters.DungeonCharacter;
import dungeonescape.dungeonobject.characters.Ghost;
import dungeonescape.dungeonobject.characters.Player;
import dungeonescape.space.DungeonSpace;
import dungeonescape.space.Position;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 *
 * @author Andrew
 */
public class EnemyPathfinder {

    public static final int PATH_AREA_BUFFER = 20;

    /**
     *
     * @param dungeon
     * @param enemy
     * @param player
     * @return
     */
    public static List<DungeonSpace> findShortestPathForEnemy(DungeonSpace[][] dungeon, DungeonCharacter enemy, Player player) {
        if (enemy instanceof Ghost) {
            return findNextDungeonSpaceForGhost(dungeon, (Ghost) enemy, player);
        }

        DungeonSpace[][] dungeonArea = narrowDungeonArea(dungeon, enemy.getPosition(), player.getPosition());
        return findShortestPathUsingBFS(dungeonArea, enemy, player);

    }

    private static DungeonSpace[][] narrowDungeonArea(DungeonSpace[][] dungeon, Position enemyPosition, Position playerPosition) {

        int dungeonAreaNorthY;
        int dungeonAreaSouthY;
        if (enemyPosition.getPositionY() < playerPosition.getPositionY()) {
            dungeonAreaNorthY = enemyPosition.getPositionY();
            dungeonAreaSouthY = playerPosition.getPositionY();
        } else {
            dungeonAreaNorthY = playerPosition.getPositionY();
            dungeonAreaSouthY = enemyPosition.getPositionY();
        }
        dungeonAreaNorthY -= PATH_AREA_BUFFER;
        dungeonAreaNorthY = dungeonAreaNorthY < 0 ? 0 : dungeonAreaNorthY;
        dungeonAreaSouthY += PATH_AREA_BUFFER;
        dungeonAreaSouthY = dungeonAreaSouthY > dungeon.length - 1 ? dungeon.length - 1 : dungeonAreaSouthY;

        int dungeonAreaWestX;
        int dungeonAreaEastX;
        if (enemyPosition.getPositionX() < playerPosition.getPositionX()) {
            dungeonAreaWestX = enemyPosition.getPositionX();
            dungeonAreaEastX = playerPosition.getPositionX();
        } else {
            dungeonAreaWestX = playerPosition.getPositionX();
            dungeonAreaEastX = enemyPosition.getPositionX();
        }
        dungeonAreaWestX -= PATH_AREA_BUFFER;
        dungeonAreaWestX = dungeonAreaWestX < 0 ? 0 : dungeonAreaWestX;
        dungeonAreaEastX += PATH_AREA_BUFFER;
        dungeonAreaEastX = dungeonAreaEastX > dungeon.length - 1 ? dungeon.length - 1 : dungeonAreaEastX;

        DungeonSpace[][] dungeonArea = new DungeonSpace[dungeonAreaEastX - dungeonAreaWestX + 1][dungeonAreaSouthY - dungeonAreaNorthY + 1];

        int dungeonAreaRow = 0;
        for (int dungeonRow = dungeonAreaNorthY; dungeonRow <= dungeonAreaSouthY; dungeonRow++) {
            int dungeonAreaCol = 0;
            for (int dungeonCol = dungeonAreaWestX; dungeonCol < dungeonAreaEastX; dungeonCol++) {
                dungeonArea[dungeonAreaRow][dungeonAreaCol] = dungeon[dungeonRow][dungeonCol];
                dungeonAreaCol++;
            }
            dungeonAreaRow++;
        }

        return dungeonArea;
    }

    private static List<DungeonSpace> findShortestPathUsingBFS(DungeonSpace[][] dungeonArea, DungeonCharacter enemy, Player player) {
        boolean[][] visited = new boolean[dungeonArea.length][dungeonArea.length];
        PathNode startNode = null;
        PathNode endNode = null;
        for (int row = 0; row < dungeonArea.length; row++) {
            for (int col = 0; col < dungeonArea.length; col++) {
                visited[row][col] = !(dungeonArea[row][col].isEmpty() || containsOnlyGhosts(dungeonArea[row][col]));
                if (dungeonArea[row][col].getDungeonObjects().contains(enemy)) {
                    startNode = new PathNode(row, col, 0);
                } else if (dungeonArea[row][col].getDungeonObjects().contains(player)) {
                    endNode = new PathNode(row, col, -1);
                }
            }
        }

        if (startNode == null) {
            throw new IllegalArgumentException("Enemy was not found within dungeonArea.");
        } else if (endNode == null) {
            throw new IllegalArgumentException("Player was not found within dungeonArea.");
        }

        Queue<PathNode> pathQueue = new LinkedList();
        pathQueue.add(startNode);

        List<DungeonSpace> shortestPath = new ArrayList<>();
        while (!pathQueue.isEmpty()) {
            PathNode pathNode = pathQueue.remove();

            shortestPath.add(dungeonArea[pathNode.getCol()][pathNode.getRow()]);
            if (pathNode.getCol() == endNode.getCol() && pathNode.getRow() == endNode.getRow()) {
                break;
            }

            int row = pathNode.getRow();
            int col = pathNode.getCol();
            int distanceFromEnemy = pathNode.getDistanceFromEnemy() + 1;

            // moving north
            addNextPathNode(col, row - 1, distanceFromEnemy, dungeonArea.length, visited, pathQueue);

            // moving south
            addNextPathNode(col, row + 1, distanceFromEnemy, dungeonArea.length, visited, pathQueue);

            // moving east
            addNextPathNode(col + 1, row, distanceFromEnemy, dungeonArea.length, visited, pathQueue);

            // moving west
            addNextPathNode(col - 1, row, distanceFromEnemy, dungeonArea.length, visited, pathQueue);
        }

        return shortestPath;
    }

    private static void addNextPathNode(int col, int row, int distanceFromEnemy,
            int dungeonAreaLength, boolean[][] visited, Queue<PathNode> pathQueue) {
        if (col >= 0 && col < dungeonAreaLength && row >= 0 && row < dungeonAreaLength) {
            pathQueue.add(new PathNode(col, row, distanceFromEnemy));
            visited[row][col + 1] = true;
        }
    }

    private static boolean containsOnlyGhosts(DungeonSpace dungeonSpace) {
        return dungeonSpace.getDungeonObjects().stream()
                .allMatch(dungeonObject -> dungeonObject instanceof Ghost);
    }

    private static List<DungeonSpace> findNextDungeonSpaceForGhost(DungeonSpace[][] dungeon, Ghost enemy, Player player) {
        int deltaX = enemy.getPosition().getPositionX() - player.getPosition().getPositionX();
        int deltaY = enemy.getPosition().getPositionY() - player.getPosition().getPositionY();

        //randomly move either east-west or north-south
        boolean moveX = Math.random() < .5;

        List<DungeonSpace> nextDungeonSpace = new ArrayList<>();
        int currentPositionX = enemy.getPosition().getPositionX();
        int currentPositionY = enemy.getPosition().getPositionY();
        int nextX;
        int nextY;
        if (moveX) {
            //if deltaX is positive, move west, otherwise move east
            if (deltaX > 0) {
                nextX = currentPositionX - 1;
            } else {
                nextX = currentPositionX + 1;
            }
            nextY = currentPositionY;
        } else {
            //if deltaY is positive, move north, otherwise move south
            nextX = currentPositionX;
            if (deltaY > 0) {
                nextY = currentPositionY - 1;
            } else {
                nextY = currentPositionY - 1;
            }
        }

        if (nextX < 0 || nextY < 0) {
            throw new NoSuchElementException("Unable to find the next position for ghost at ["
                    + enemy.getPosition().getPositionX() + "," + enemy.getPosition().getPositionY() + "].");
        }

        nextDungeonSpace.add(dungeon[nextX][nextY]);

        return nextDungeonSpace;
    }

    private static class PathNode {

        private final int col;
        private final int row;
        private final int distanceFromEnemy;

        public PathNode(int col, int row, int distanceFromEnemy) {
            this.col = col;
            this.row = row;
            this.distanceFromEnemy = distanceFromEnemy;
        }

        public int getCol() {
            return col;
        }

        public int getRow() {
            return row;
        }

        public int getDistanceFromEnemy() {
            return distanceFromEnemy;
        }

    }

}
