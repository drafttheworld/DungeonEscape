/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.characters.pathfinder;

import dungeonescape.dungeonobject.characters.DungeonCharacter;
import dungeonescape.dungeonobject.characters.Ghost;
import dungeonescape.dungeonobject.characters.Player;
import dungeonescape.dungeon.space.DungeonSpace;
import dungeonescape.dungeon.space.DungeonSpaceType;
import dungeonescape.dungeon.space.Position;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author Andrew
 */
public class EnemyPathfinder {

    public static final int PATH_AREA_BUFFER = 5;

    /**
     *
     * @param dungeon
     * @param enemy
     * @param player
     * @return
     */
    public static List<DungeonSpace> findShortestPathForEnemy(DungeonSpace[][] dungeon, DungeonCharacter enemy, Player player) {
        DungeonSpace[][] dungeonArea = narrowDungeonArea(dungeon, enemy.getPosition(), player.getPosition());
        return findShortestPathUsingBFS(dungeonArea, enemy);
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

        DungeonSpace[][] dungeonArea = new DungeonSpace[dungeonAreaSouthY - dungeonAreaNorthY + 1][dungeonAreaEastX - dungeonAreaWestX + 1];
        int dungeonAreaRow = 0;
        for (int dungeonRow = dungeonAreaNorthY; dungeonRow <= dungeonAreaSouthY; dungeonRow++) {
            int dungeonAreaCol = 0;
            for (int dungeonCol = dungeonAreaWestX; dungeonCol <= dungeonAreaEastX; dungeonCol++) {
                dungeonArea[dungeonAreaRow][dungeonAreaCol] = dungeon[dungeonRow][dungeonCol];
                dungeonAreaCol++;
            }
            dungeonAreaRow++;
        }
        return dungeonArea;
    }

    private static List<DungeonSpace> findShortestPathUsingBFS(DungeonSpace[][] dungeonArea, DungeonCharacter enemy) {
        boolean[][] visited = new boolean[dungeonArea.length][dungeonArea[0].length];
        PathNode startNode = null;
        for (int row = 0; row < dungeonArea.length; row++) {
            for (int col = 0; col < dungeonArea[row].length; col++) {
                if (enemy instanceof Ghost) {
                    visited[row][col] = false;
                } else {
                    visited[row][col] = !(dungeonArea[row][col].isEmpty() || containsOnlyGhostsOrPlayers(dungeonArea[row][col]));
                }
                if (dungeonArea[row][col].getDungeonObjects().contains(enemy)) {
                    startNode = new PathNode(row, col, dungeonArea[row][col], null);
                }
            }
        }

        if (startNode == null) {
            throw new IllegalArgumentException("Enemy was not found within dungeonArea.");
        }

        Queue<PathNode> pathQueue = new LinkedList();
        pathQueue.add(startNode);

        while (!pathQueue.isEmpty()) {
            PathNode pathNode = pathQueue.remove();

            if (dungeonSpaceContainsPlayer(pathNode.getDungeonSpace())) {
                return pathNode.getPathToDungeonSpace();
            }

            int row = pathNode.getRow();
            int col = pathNode.getCol();

            // moving north
            addNextPathNode(row - 1, col, dungeonArea, visited, pathNode, pathQueue);

            // moving south
            addNextPathNode(row + 1, col, dungeonArea, visited, pathNode, pathQueue);

            // moving east
            addNextPathNode(row, col + 1, dungeonArea, visited, pathNode, pathQueue);

            // moving west
            addNextPathNode(row, col - 1, dungeonArea, visited, pathNode, pathQueue);
        }

        return Collections.emptyList();
    }

    private static void printDungeonArea(DungeonSpace[][] dungeonArea) {
        int playerCol = -1;
        int playerRow = -1;
        StringBuilder sb = new StringBuilder("dungeonArea: \n");
        for (int row = 0; row < dungeonArea.length; row++) {
            for (int col = 0; col < dungeonArea[row].length; col++) {
                char visibleSpaceSymbol
                    = dungeonArea[row][col].getVisibleDungeonObject().getDungeonSpaceType().getValue();
                sb.append(visibleSpaceSymbol);
                if (visibleSpaceSymbol == DungeonSpaceType.PLAYER.getValue()) {
                    playerCol = col;
                    playerRow = row;
                }
            }
            sb.append("\n");
        }
        sb.append("Player found at [").append(playerCol).append(",").append(playerRow).append("]");
        System.out.println(sb.toString());
    }

    private static void addNextPathNode(int row, int col, DungeonSpace[][] dungeonArea,
        boolean[][] visited, PathNode previousPathNode, Queue<PathNode> pathQueue) {
        try {
            if (col >= 0 && row >= 0 && row < dungeonArea.length && col < dungeonArea[row].length && !visited[row][col]) {
                pathQueue.add(new PathNode(row, col, dungeonArea[row][col], previousPathNode));
                visited[row][col] = true;
            }
        } catch (RuntimeException e) {
            System.out.println("Error adding node at: [" + col + "," + row + "]");
            throw e;
        }
    }

    private static boolean dungeonSpaceContainsPlayer(DungeonSpace dungeonSpace) {
        return dungeonSpace.getDungeonObjects().stream()
            .anyMatch(dungeonSpaceObject -> dungeonSpaceObject instanceof Player);
    }

    private static boolean containsOnlyGhostsOrPlayers(DungeonSpace dungeonSpace) {
        return dungeonSpace.getDungeonObjects().stream()
            .allMatch(dungeonObject -> dungeonObject instanceof Ghost || dungeonObject instanceof Player);
    }

    private static class PathNode {

        private final int row;
        private final int col;
        private final DungeonSpace dungeonSpace;
        private final PathNode previousPathNode;

        public PathNode(int row, int col, DungeonSpace dungeonSpace, PathNode previousPathNode) {
            this.row = row;
            this.col = col;
            this.dungeonSpace = dungeonSpace;
            this.previousPathNode = previousPathNode;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        public DungeonSpace getDungeonSpace() {
            return dungeonSpace;
        }

        public PathNode getPreviousPathNode() {
            return previousPathNode;
        }

        public List<DungeonSpace> getPathToDungeonSpace() {
            List<DungeonSpace> pathToDungeonSpace = new ArrayList<>();
            PathNode pathNode = this;
            while (pathNode != null) {
                pathToDungeonSpace.add(pathNode.getDungeonSpace());
                pathNode = pathNode.getPreviousPathNode();
            }
            Collections.reverse(pathToDungeonSpace);
            return pathToDungeonSpace;
        }

    }

}
