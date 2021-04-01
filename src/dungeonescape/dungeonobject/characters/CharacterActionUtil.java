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
import dungeonescape.dungeon.space.DungeonSpace;
import dungeonescape.dungeon.space.Position;
import dungeonescape.dungeonobject.powerups.PowerUp;
import dungeonescape.dungeonobject.powerups.PowerUpEnum;
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
        Direction previousFacingDirection = player.getCurrentFacingDirection();
        if (previousFacingDirection == Direction.EAST || previousFacingDirection == Direction.WEST) {
            player.setPreviousFacingDirection(previousFacingDirection);
        }

        Position nextPosition = determineNextPosition(player.getPosition(), direction);

        if (nextPosition.getPositionX()
            < 0 || nextPosition.getPositionX() >= dungeon.length
            || nextPosition.getPositionY() < 0 || nextPosition.getPositionY() >= dungeon.length) {
            NotificationManager.notify(new WinNotification());
            return Collections.emptyList();
        }
        DungeonSpace nextDungeonSpace = dungeon[nextPosition.getPositionY()][nextPosition.getPositionX()];

        if (nextDungeonSpace.containsWall()) {
            return Collections.emptyList();
        }

        MovementAction movementAction = assignCharacterMovement(player, nextDungeonSpace);

        player.setCurrentFacingDirection(movementAction.getDirection());

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

        if (!enemy.isActive()) {
            return Collections.emptyList();
        }

        List<DungeonSpace> dungeonSpaces = new ArrayList<>();

        DungeonSpace startingDungeonSpace = enemy.getDungeonSpace();
        boolean startingSpaceIsVisible = startingDungeonSpace.isVisible();
        if (startingSpaceIsVisible) {
            dungeonSpaces.add(startingDungeonSpace);
        }

        Direction previousFacingDirection = enemy.getCurrentFacingDirection();
        if (previousFacingDirection == Direction.EAST || previousFacingDirection == Direction.WEST) {
            enemy.setPreviousFacingDirection(previousFacingDirection);
        }

        MovementAction movementAction = new MovementAction(Direction.UNKNOWN, Collections.emptyList());
        boolean isPlayerInvisible = isPlayerInvisibile(player);
        if (!isPlayerInvisible && isPlayerInView(enemy, player, detectionDistance)) {
            movementAction = hunt(dungeon, enemy, player, numberOfSpacesToMoveWhenHunting);
        } else {
            int movesExecuted = 0;
            for (int moveNumber = 0; moveNumber < numberOfSpacesToMoveWhenPatrolling; moveNumber++) {
                movementAction = patrol(dungeon, enemy);
                movesExecuted++;
                if (!isPlayerInvisible && isPlayerInView(enemy, player, detectionDistance)) {
                    int numberOfMovesRemaining = numberOfSpacesToMoveWhenHunting - movesExecuted;
                    if (numberOfMovesRemaining > 0) {
                        movementAction = hunt(dungeon, enemy, player, numberOfMovesRemaining);
                    }
                    break;
                }
            }
        }
        enemy.setCurrentFacingDirection(movementAction.getDirection());
        dungeonSpaces.addAll(movementAction.getDungeonSpaces());

        dungeonSpaces.removeIf(dungeonSpace -> !dungeonSpace.isVisible());

        return dungeonSpaces;
    }

    private static boolean isPlayerInvisibile(Player player) {
        PowerUp powerUp = player.getActivePowerUp();
        return powerUp != null
            && powerUp.getCorrespondingPowerUpEnum() == PowerUpEnum.INVISIBILITY;
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
    private synchronized static MovementAction hunt(DungeonSpace[][] dungeon, DungeonCharacter enemy,
        Player player, int numberOfMoves) {

        if (player.getActivePowerUp() != null
            && player.getActivePowerUp().getCorrespondingPowerUpEnum() == PowerUpEnum.REPELLENT) {

            return assignCharacterMovement(enemy, determineDungeonSpaceIfRepelled(dungeon, enemy, player));
        }

        List<DungeonSpace> path = EnemyPathfinder.findShortestPathForEnemy(dungeon, enemy, player);
        if (!path.isEmpty()) {
            int nextDungeonSpaceIndex = path.size() <= numberOfMoves ? path.size() - 1 : numberOfMoves;
            return assignCharacterMovement(enemy, path.get(nextDungeonSpaceIndex));
        } else {
            return patrol(dungeon, enemy);
        }
    }

    private static DungeonSpace determineDungeonSpaceIfRepelled(DungeonSpace[][] dungeon,
        DungeonCharacter enemy, Player player) {

        int enemyPosX = enemy.getPosition().getPositionX();
        int enemyPosY = enemy.getPosition().getPositionY();
        int playerPosX = player.getPosition().getPositionX();
        int playerPosY = player.getPosition().getPositionY();

        int nextPosX = enemyPosX;
        if (enemyPosX < playerPosX && enemyPosX > 0) { // player is west of enemy
            // move east
            DungeonSpace dungeonSpace = dungeon[enemyPosY][enemyPosX - 1];
            if (enemy.canOccupySpace(dungeonSpace)) {
                return dungeonSpace;
            }
        } else if (enemyPosX > playerPosX && enemyPosX < dungeon.length - 1) {
            // move west
            DungeonSpace dungeonSpace = dungeon[enemyPosY][enemyPosX + 1];
            if (enemy.canOccupySpace(dungeonSpace)) {
                return dungeonSpace;
            }
        }

        int nextPosY = enemyPosY;
        if (enemyPosY < playerPosY && enemyPosY > 0) {
            // move south
            DungeonSpace dungeonSpace = dungeon[enemyPosY - 1][enemyPosX];
            if (enemy.canOccupySpace(dungeonSpace)) {
                return dungeonSpace;
            }
        } else if (enemyPosY > playerPosY && enemyPosY < dungeon.length - 1) {
            // move north
            DungeonSpace dungeonSpace = dungeon[enemyPosY + 1][enemyPosX];
            if (enemy.canOccupySpace(dungeonSpace)) {
                return dungeonSpace;
            }
        }

        return dungeon[nextPosY][nextPosX];
    }

    /**
     * Move one random adjacent space, excluding the previously occupied space.
     *
     * @param dungeon
     * @param enemy
     */
    private static synchronized MovementAction patrol(DungeonSpace[][] dungeon, DungeonCharacter enemy) {

        DungeonSpace nextDungeonSpace = determineNextPatrolSpace(dungeon, enemy);
        if (nextDungeonSpace == null) {
            NotificationManager.notify(
                new ActionNotAllowedNotification("Unable to find next patrol space for "
                    + enemy.getClass().getSimpleName() + " at [" + enemy.getPosition().getPositionX() + ","
                    + enemy.getPosition().getPositionY() + "]"));
            return new MovementAction(Direction.UNKNOWN, Collections.emptyList());
        }

        return assignCharacterMovement(enemy, nextDungeonSpace);
    }

    private static MovementAction assignCharacterMovement(DungeonCharacter character, DungeonSpace nextDungeonSpace) {

        DungeonSpace currentDungeonSpace = character.getDungeonSpace();
        character.setPreviousDungeonSpace(currentDungeonSpace);
        currentDungeonSpace.removeDungeonObject(character);

        List<DungeonSpace> dungeonSpaces = nextDungeonSpace.addDungeonObject(character);

        if (dungeonSpaces.isEmpty()) {
            dungeonSpaces.add(nextDungeonSpace);
        }

        Direction direction = determineDirection(currentDungeonSpace, nextDungeonSpace);

        return new MovementAction(direction, dungeonSpaces);
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
            return Direction.EAST;
        } else if (diffX > 0) {
            return Direction.WEST;
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

    private static class MovementAction {

        private final Direction direction;
        private final List<DungeonSpace> dungeonSpaces;

        public MovementAction(Direction direction, List<DungeonSpace> dungeonSpaces) {
            this.direction = direction;
            this.dungeonSpaces = dungeonSpaces;
        }

        public Direction getDirection() {
            return direction;
        }

        public List<DungeonSpace> getDungeonSpaces() {
            return dungeonSpaces;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, dungeonSpaces);
        }

        @Override
        public boolean equals(Object obj) {

            if (this == obj) {
                return true;
            } else if (obj == null || getClass() != obj.getClass()) {
                return false;
            }

            final MovementAction other = (MovementAction) obj;
            return this.direction == other.direction
                && Objects.equals(this.dungeonSpaces, other.dungeonSpaces);
        }

        @Override
        public String toString() {
            return "MovementAction{" + "direction=" + direction + ", dungeonSpaces=" + dungeonSpaces + '}';
        }
    }

}
