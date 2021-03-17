/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.characters;

import dungeonescape.dungeon.notifications.ActionNotAllowedNotification;
import dungeonescape.dungeon.notifications.GameNotification;
import dungeonescape.dungeon.notifications.NotificationManager;
import dungeonescape.dungeon.notifications.WinNotification;
import dungeonescape.dungeonobject.characters.pathfinder.EnemyPathfinder;
import dungeonescape.play.Direction;
import dungeonescape.space.DungeonSpace;
import dungeonescape.space.Position;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Andrew
 */
public class CharacterActionUtil {

    protected static List<DungeonSpace> movePlayer(DungeonSpace[][] dungeon, Player player, Direction direction) {

        List<DungeonSpace> dungeonSpaces = new ArrayList<>();
        dungeonSpaces.add(player.getDungeonSpace());

        Position nextPosition = determineNextPosition(player.getPosition(), direction);

        if (nextPosition.getPositionX() < 0 || nextPosition.getPositionX() >= dungeon.length
            || nextPosition.getPositionY() < 0 || nextPosition.getPositionY() >= dungeon.length) {
            NotificationManager.notify(new WinNotification());
            return Collections.emptyList();
        }
        DungeonSpace nextDungeonSpace = dungeon[nextPosition.getPositionY()][nextPosition.getPositionX()];

        if (nextDungeonSpace.containsWall()) {
            return Collections.emptyList();
        }

        Direction nextFacingDirection = assignCharacterMovement(player, nextDungeonSpace);
        player.setCurrentFacingDirection(nextFacingDirection);

        dungeonSpaces.add(nextDungeonSpace);

        return dungeonSpaces;
    }

    private static Position determineNextPosition(Position currentPlayerPosition, Direction direction) {

        switch (direction) {
            case NORTH:
                return new Position(currentPlayerPosition.getPositionX(), currentPlayerPosition.getPositionY() - 1);
            case SOUTH:
                return new Position(currentPlayerPosition.getPositionX(), currentPlayerPosition.getPositionY() + 1);
            case EAST:
                return new Position(currentPlayerPosition.getPositionX() + 1, currentPlayerPosition.getPositionY());
            case WEST:
                return new Position(currentPlayerPosition.getPositionX() - 1, currentPlayerPosition.getPositionY());
            default:
                String errorMessage = direction + " is not a valid direction. Allowed directions are: "
                    + Arrays.asList(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);
                NotificationManager.notify(new ActionNotAllowedNotification(errorMessage));
        }
        return null;
    }

    /**
     * When the player is not in view of the enemy the enemy will patrol randomly (but not moving back to the previous
     * space). When the player is in view of the enemy, the enemy will "hunt", moving toward the player. If during the
     * move the enemy moves in view of the player, the enemy will change from patrol mode to hunt mode, or vice versa.
     *
     * @param dungeon
     * @param enemy
     * @param numberOfSpacesToMoveWhenPatrolling When the player is not in view the enemy will move this many spaces.
     * @param numberOfSpacesToMoveWhenHunting When the player is in view at the the character will move this many
     * spaces.
     * @param detectionDistance
     * @param player
     * @return
     * @throws dungeonescape.dungeon.notifications.GameNotification
     */
    protected static List<DungeonSpace> moveEnemy(DungeonSpace[][] dungeon, DungeonCharacter enemy,
        int numberOfSpacesToMoveWhenPatrolling, int numberOfSpacesToMoveWhenHunting, int detectionDistance,
        Player player) {

        List<DungeonSpace> dungeonSpaces = new ArrayList<>();

        DungeonSpace startingDungeonSpace = enemy.getDungeonSpace();
        boolean startingSpaceIsVisible = startingDungeonSpace.isVisible();
        if (startingSpaceIsVisible) {
            dungeonSpaces.add(startingDungeonSpace);
        }

        Direction movementDirection = Direction.UNKNOWN;
        if (isPlayerInView(enemy, player, detectionDistance)) {
            movementDirection = hunt(dungeon, enemy, player, numberOfSpacesToMoveWhenHunting);
        } else {
            int movesExecuted = 0;
            for (int moveNumber = 0; moveNumber < numberOfSpacesToMoveWhenPatrolling; moveNumber++) {
                movementDirection = patrol(dungeon, enemy);
                movesExecuted++;
                if (isPlayerInView(enemy, player, detectionDistance)) {
                    int numberOfMovesRemaining = numberOfSpacesToMoveWhenHunting - movesExecuted;
                    if (numberOfMovesRemaining > 0) {
                        movementDirection = hunt(dungeon, enemy, player, numberOfMovesRemaining);
                    }
                    break;
                }
            }
        }
        enemy.setCurrentFacingDirection(movementDirection);

        boolean currentDungeonSpaceIsVisible = enemy.getDungeonSpace().isVisible();
        if (!startingSpaceIsVisible && !currentDungeonSpaceIsVisible) {
            return Collections.emptyList();
        } else if (currentDungeonSpaceIsVisible) {
            dungeonSpaces.add(enemy.getDungeonSpace());
        }

        return dungeonSpaces;
    }

    private static boolean isPlayerInView(DungeonCharacter enemy, Player player, int detectionDistance) {

        int enemyPosX = enemy.getPosition().getPositionX();
        int enemyPosY = enemy.getPosition().getPositionY();

        int playerPosX = player.getPosition().getPositionX();
        int playerPosY = player.getPosition().getPositionY();

        double distanceX = enemyPosX - playerPosX;
        double distanceY = enemyPosY - playerPosY;

        int distanceFromPlayer = (int) Math.ceil(Math.sqrt((distanceX * distanceX) + (distanceY * distanceY)));

        return distanceFromPlayer <= detectionDistance;
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
    private synchronized static Direction hunt(DungeonSpace[][] dungeon, DungeonCharacter enemy,
        Player player, int numberOfMoves) {

        List<DungeonSpace> path = EnemyPathfinder.findShortestPathForEnemy(dungeon, enemy, player);
        if (!path.isEmpty()) {
            try {
                int nextDungeonSpaceIndex = path.size() <= numberOfMoves ? path.size() - 1 : numberOfMoves;
                return assignCharacterMovement(enemy, path.get(nextDungeonSpaceIndex));
            } catch (Exception e) {
                System.out.println("path.size(): " + path.size() + ", numberOfMoves: " + numberOfMoves);
                throw e;
            }
        } else {
            System.out.println("Unable to find path to player.");
            return patrol(dungeon, enemy);
        }
    }

    private static void printPath(List<DungeonSpace> shortestPathToPlayer) {

        StringBuilder sb = new StringBuilder("Path to player: ");
        for (DungeonSpace dungeonSpace : shortestPathToPlayer) {
            sb.append("[").append(dungeonSpace.getPosition().getPositionX()).append(",")
                .append(dungeonSpace.getPosition().getPositionY()).append("]");
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
    private static synchronized Direction patrol(DungeonSpace[][] dungeon, DungeonCharacter enemy) {

        DungeonSpace nextDungeonSpace = determineNextPatrolSpace(dungeon, enemy);
        if (nextDungeonSpace == null) {
            NotificationManager.notify(
                new ActionNotAllowedNotification("Unable to find next patrol space for "
                    + enemy.getClass().getSimpleName() + " at [" + enemy.getPosition().getPositionX() + ","
                    + enemy.getPosition().getPositionY() + "]"));
            return Direction.UNKNOWN;
        }

        return assignCharacterMovement(enemy, nextDungeonSpace);
    }

    private static Direction assignCharacterMovement(DungeonCharacter character, DungeonSpace nextDungeonSpace) {

        DungeonSpace currentDungeonSpace = character.getDungeonSpace();
        character.setPreviousDungeonSpace(currentDungeonSpace);
        currentDungeonSpace.removeDungeonObject(character);
        nextDungeonSpace.addDungeonObject(character);

        return determineDirection(currentDungeonSpace, nextDungeonSpace);
    }

    private static Direction determineDirection(DungeonSpace startingDungeonSpace, DungeonSpace endingDungeonSpace) {

        int startingPositionX = startingDungeonSpace.getPosition().getPositionX();
        int endingPositionX = endingDungeonSpace.getPosition().getPositionX();
        int diffX = startingPositionX - endingPositionX;

        if (diffX == 0) {
            int startingPositionY = startingDungeonSpace.getPosition().getPositionY();
            int endingPositionY = endingDungeonSpace.getPosition().getPositionY();
            int diffY = startingPositionY - endingPositionY;

            if (diffY < 0) {
                return Direction.SOUTH;
            } else if (diffY > 0) {
                return Direction.NORTH;
            } else {
                return Direction.UNKNOWN;
            }
        } else if (diffX < 0) {
            return Direction.WEST;
        } else if (diffX > 0) {
            return Direction.EAST;
        } else {
            return Direction.UNKNOWN;
        }
    }

    private static DungeonSpace determineNextPatrolSpace(DungeonSpace[][] dungeon, DungeonCharacter enemy) {

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
