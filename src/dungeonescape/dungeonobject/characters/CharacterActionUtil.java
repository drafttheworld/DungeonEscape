/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.characters;

import dungeonescape.dungeon.notifications.ActionNotAllowedNotification;
import dungeonescape.dungeon.notifications.GameNotification;
import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.dungeonobject.characters.pathfinder.EnemyPathfinder;
import dungeonescape.space.DungeonSpace;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Andrew
 */
public class CharacterActionUtil {

    /**
     * When the player is not in view of the enemy the enemy will patrol
     * randomly (but not moving back to the previous space). When the player is
     * in view of the enemy, the enemy will "hunt", moving toward the player. If
     * during the move the enemy moves in view of the player, the enemy will
     * change from patrol mode to hunt mode, or vice versa.
     *
     * @param dungeon
     * @param enemy
     * @param numberOfSpacesToMoveWhenPatrolling When the player is not in view
     * the enemy will move this many spaces.
     * @param numberOfSpacesToMoveWhenHunting When the player is in view at the
     * the character will move this many spaces.
     * @param detectionDistance
     * @throws dungeonescape.dungeon.notifications.GameNotification
     */
    protected static void moveEnemy(DungeonSpace[][] dungeon, DungeonCharacter enemy,
            int numberOfSpacesToMoveWhenPatrolling, int numberOfSpacesToMoveWhenHunting, int detectionDistance) throws GameNotification {
//        System.out.println("Moving " + enemy.getClass().getSimpleName());
        Player player = findPlayerInView(dungeon, enemy, detectionDistance);
        if (player != null) {
            hunt(dungeon, enemy, player, numberOfSpacesToMoveWhenHunting);
        } else {
            int movesExecuted = 0;
            for (int moveNumber = 0; moveNumber < numberOfSpacesToMoveWhenPatrolling; moveNumber++) {
                patrol(dungeon, enemy);
                player = findPlayerInView(dungeon, enemy, detectionDistance);
                movesExecuted++;
                if (player != null) {
                    break;
                }
            }
            if (player != null) {
                int numberOfMovesRemaining = numberOfSpacesToMoveWhenHunting - movesExecuted;
                hunt(dungeon, enemy, player, numberOfMovesRemaining);
            }
//            System.out.println("Moved " + enemy.getClass().getSimpleName() + " " + movesExecuted + " times.");
        }
    }

    /**
     * Player is in view if the straight line distance is within the
     * detectionDistance using the Pathagorean equation.
     *
     * @param dungeon
     * @param enemy
     * @param detectionDistance
     * @return
     */
    protected static Player findPlayerInView(DungeonSpace[][] dungeon, DungeonCharacter enemy, int detectionDistance) {

        int enemyPosX = enemy.getPosition().getPositionX();
        int enemyPosY = enemy.getPosition().getPositionY();

        int northWestX = enemyPosX - detectionDistance;
        northWestX = northWestX < 0 ? 0 : northWestX;
        int northWestY = enemyPosY - detectionDistance;
        northWestY = northWestY < 0 ? 0 : northWestY;
        int southEastX = enemyPosX + detectionDistance;
        southEastX = southEastX > dungeon.length - 1 ? dungeon.length - 1 : southEastX;
        int southWestY = enemyPosY + detectionDistance;
        southWestY = southWestY > dungeon.length - 1 ? dungeon.length - 1 : southWestY;

        for (int row = northWestY; row <= southWestY; row++) {
            for (int col = northWestX; col <= southEastX; col++) {
                for (DungeonObject dungeonObject : dungeon[row][col].getDungeonObjects()) {
                    if (dungeonObject instanceof Player) {
                        return (Player) dungeonObject;
                    }
                }

            }
        }

        return null;
    }

    /**
     * Moves the enemy toward the player.
     *
     * @param dungeon
     * @param enemy
     * @param player
     * @param numberOfMoves
     * @throws GameNotification
     */
    private static void hunt(DungeonSpace[][] dungeon, DungeonCharacter enemy, Player player, int numberOfMoves) throws GameNotification {
        //determine the shortest path to the player and move down that path.
//        System.out.println("Hunting, number of spaces to move: "+numberOfMoves);
        List<DungeonSpace> path = EnemyPathfinder.findShortestPathForEnemy(dungeon, enemy, player);
        printPath(path);
        if (!path.isEmpty()) {
        int nextDungeonSpaceIndex = path.size() < numberOfMoves ? path.size() - 1 : numberOfMoves - 1;
        DungeonSpace nextDungeonSpace = path.get(nextDungeonSpaceIndex);
//        System.out.println("Player is at ["+player.getPosition().getPositionX()+","+player.getPosition().getPositionY()+"]");
//        System.out.println("Moving enemy from ["+enemy.getPosition().getPositionX()+","+enemy.getPosition().getPositionY()+"] to ["+nextDungeonSpace.getPosition().getPositionX()+","+nextDungeonSpace.getPosition().getPositionY()+"]");
        DungeonSpace currentDungeonSpace = enemy.getDungeonSpace();
        enemy.getDungeonSpace().removeDungeonObject(enemy);
        enemy.setPreviousDungeonSpace(currentDungeonSpace);
        nextDungeonSpace.addDungeonObject(enemy);
        } else {
            System.out.println("Unable to find path to player.");
            patrol(dungeon, enemy);
        }
    }

    private static void printPath(List<DungeonSpace> shortestPathToPlayer) {
        StringBuilder sb = new StringBuilder("Path to player: ");
        for (DungeonSpace dungeonSpace : shortestPathToPlayer) {
            sb.append("[").append(dungeonSpace.getPosition().getPositionX()).append(",").append(dungeonSpace.getPosition().getPositionY()).append("]");
            if (dungeonSpace != shortestPathToPlayer.get(shortestPathToPlayer.size() - 1)) {
                sb.append(" -> ");
            }
        }
        System.out.println(sb.toString());
    }

    /**
     * Move one random adjacent space, excluding the previously occupied space.
     *
     * @param dungeon
     * @param enemy
     */
    private static void patrol(DungeonSpace[][] dungeon, DungeonCharacter enemy) throws GameNotification {
        DungeonSpace nextDungeonSpace = determineNextPatrolSpace(dungeon, enemy);
        if (nextDungeonSpace == null) {
            throw new ActionNotAllowedNotification("Unable to find next patrol space for "
                    + enemy.getClass().getSimpleName() + " at [" + enemy.getPosition().getPositionX() + ","
                    + enemy.getPosition().getPositionY() + "]");
        }

        DungeonSpace currentDungeonSpace = enemy.getDungeonSpace();
        enemy.getDungeonSpace().removeDungeonObject(enemy);
        enemy.setPreviousDungeonSpace(currentDungeonSpace);
        nextDungeonSpace.addDungeonObject(enemy);
    }

    private static DungeonSpace determineNextPatrolSpace(DungeonSpace[][] dungeon, DungeonCharacter enemy) {
//        System.out.println("Determining patrol spaces for enemy at [" + enemy.getPosition().getPositionX() + "," + enemy.getPosition().getPositionY() + "].");
        List<DungeonSpace> availableDungeonSpaces = new ArrayList<>();

        int enemyPosX = enemy.getPosition().getPositionX();
        int enemyPosY = enemy.getPosition().getPositionY();
        //north
        if (enemy.getPosition().getPositionY() > 0
                && enemy.canOccupySpace(dungeon[enemyPosY - 1][enemyPosX])) {
            availableDungeonSpaces.add(dungeon[enemyPosY - 1][enemyPosX]);
        }
        //south
        if (enemy.getPosition().getPositionY() < dungeon.length - 1
                && enemy.canOccupySpace(dungeon[enemyPosY + 1][enemyPosX])) {
            availableDungeonSpaces.add(dungeon[enemyPosY + 1][enemyPosX]);
        }
        //east
        if (enemy.getPosition().getPositionX() < dungeon.length - 1
                && enemy.canOccupySpace(dungeon[enemyPosY][enemyPosX + 1])) {
            availableDungeonSpaces.add(dungeon[enemyPosY][enemyPosX + 1]);
        }
        //west
        if (enemy.getPosition().getPositionX() > 0
                && enemy.canOccupySpace(dungeon[enemyPosY][enemyPosX - 1])) {
            availableDungeonSpaces.add(dungeon[enemyPosY][enemyPosX - 1]);
        }

        DungeonSpace nextDungeonSpace = null;
        if (availableDungeonSpaces.size() > 1) {
            Integer nextSpaceIndex = ThreadLocalRandom.current().nextInt(0, availableDungeonSpaces.size());
            nextDungeonSpace = availableDungeonSpaces.get(nextSpaceIndex);
            while (Objects.equals(enemy.getPreviousDungeonSpace(), nextDungeonSpace)) {
                nextSpaceIndex = ThreadLocalRandom.current().nextInt(0, availableDungeonSpaces.size());
                nextDungeonSpace = availableDungeonSpaces.get(nextSpaceIndex);
            }
        } else if (availableDungeonSpaces.size() == 1) {
            nextDungeonSpace = availableDungeonSpaces.get(0);
        }

        return nextDungeonSpace;
    }

}
