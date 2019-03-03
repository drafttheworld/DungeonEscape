/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.characters;

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
                for (DungeonObject dungeonObject : dungeon[col][row].getDungeonObjects()) {
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
        List<DungeonSpace> path = EnemyPathfinder.findShortestPathForEnemy(dungeon, enemy, player);
        int nextDungeonSpaceIndex = path.size() < numberOfMoves ? path.size() - 1 : numberOfMoves - 1;
        DungeonSpace nextDungeonSpace = path.get(nextDungeonSpaceIndex);
        
        //exit the current space before entering the next space
        enemy.getDungeonSpace().removeDungeonObject(enemy);
        nextDungeonSpace.addDungeonObject(enemy);
    }

    /**
     * Move one random adjacent space, excluding the previously occupied space.
     * @param dungeon
     * @param enemy 
     */
    private static void patrol(DungeonSpace[][] dungeon, DungeonCharacter enemy) throws GameNotification {
        DungeonSpace nextDungeonSpace = determineNextPatrolSpace(dungeon, enemy);
        enemy.setPreviousDungeonSpace(enemy.getDungeonSpace());
        enemy.getDungeonSpace().removeDungeonObject(enemy);
        nextDungeonSpace.addDungeonObject(enemy);
    }
    
    private static DungeonSpace determineNextPatrolSpace(DungeonSpace[][] dungeon, DungeonCharacter enemy) {
        
        List<DungeonSpace> availableDungeonSpaces = new ArrayList<>();
        
        int enemyPosX = enemy.getPosition().getPositionX();
        int enemyPosY = enemy.getPosition().getPositionY();
        //north
        if (enemy.getPosition().getPositionY() > 0
                && enemy.canOccupySpace(dungeon[enemyPosX][enemyPosY - 1])
                && !Objects.equals(enemy.getPreviousDungeonSpace(), dungeon[enemyPosX][enemyPosY - 1])) {
            availableDungeonSpaces.add(dungeon[enemyPosX][enemyPosY - 1]);
        }
        //south
        if (enemy.getPosition().getPositionY() < dungeon.length - 1
                && enemy.canOccupySpace(dungeon[enemyPosX][enemyPosY + 1])
                && !Objects.equals(enemy.getPreviousDungeonSpace(), dungeon[enemyPosX][enemyPosY + 1])) {
            availableDungeonSpaces.add(dungeon[enemyPosX][enemyPosY + 1]);
        }
        //east
        if (enemy.getPosition().getPositionX() < dungeon.length - 1
                && enemy.canOccupySpace(dungeon[enemyPosX + 1][enemyPosY])
                && !Objects.equals(enemy.getPreviousDungeonSpace(), dungeon[enemyPosX + 1][enemyPosY])) {
            availableDungeonSpaces.add(dungeon[enemyPosX + 1][enemyPosY]);
        }
        //west
        if (enemy.getPosition().getPositionX() > 0
                && enemy.canOccupySpace(dungeon[enemyPosX - 1][enemyPosY])
                && !Objects.equals(enemy.getPreviousDungeonSpace(), dungeon[enemyPosX - 1][enemyPosY])) {
            availableDungeonSpaces.add(dungeon[enemyPosX - 1][enemyPosY]);
        }
        
        Integer nextSpaceIndex = ThreadLocalRandom.current().nextInt(0, availableDungeonSpaces.size());
        
        return availableDungeonSpaces.get(nextSpaceIndex);
    }

}
