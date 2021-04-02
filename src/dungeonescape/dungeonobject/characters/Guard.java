/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.characters;

import dungeonescape.dungeon.notifications.ActionNotAllowedNotification;
import dungeonescape.dungeon.notifications.InteractionNotification;
import dungeonescape.dungeon.notifications.NotificationManager;
import dungeonescape.dungeon.space.DungeonSpace;
import dungeonescape.dungeon.space.DungeonSpaceType;
import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.dungeonobject.TeleportObject;
import dungeonescape.dungeonobject.construction.Construction;
import dungeonescape.dungeonobject.powerups.PowerUp;
import dungeonescape.dungeonobject.powerups.PowerUpEnum;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Andrew
 */
public class Guard extends NonPlayerCharacter implements TeleportObject {

    public static int DEFAULT_MOVES_WHEN_PATROLLING = 5;
    public static int DEFAULT_MOVES_WHEN_HUNTING = 4;
    public static int DEFAULT_DETECTION_DISTANCE = 5;

    private final DungeonSpace jailCellSpace;
    private final DungeonSpace[][] dungeon;
    private int detectionDistance;

    public Guard(DungeonSpace jailCellSpace, DungeonSpace[][] dungeon, Player player) {
        super(player);
        this.jailCellSpace = jailCellSpace;
        this.dungeon = dungeon;
    }

    @Override
    public int getNumberOfSpacesToMoveWhenPatrolling() {
        return super.getNumberOfSpacesToMoveWhenPatrolling() == 0
            ? DEFAULT_MOVES_WHEN_PATROLLING : super.getNumberOfSpacesToMoveWhenPatrolling();
    }

    public Guard numberOfMovesWhenPatrolling(int numberOfMovesWhenPatrolling) {
        setNumberOfSpacesToMoveWhenPatrolling(numberOfMovesWhenPatrolling);
        return this;
    }

    @Override
    public int getNumberOfSpacesToMoveWhenHunting() {
        return super.getNumberOfSpacesToMoveWhenHunting() == 0
            ? DEFAULT_MOVES_WHEN_HUNTING : super.getNumberOfSpacesToMoveWhenHunting();
    }

    public Guard numberOfMovesWhenHunting(int numberOfMovesWhenHunting) {
        setNumberOfSpacesToMoveWhenHunting(numberOfMovesWhenHunting);
        return this;
    }

    public int getDetectionDistance() {
        return detectionDistance == 0 ? DEFAULT_DETECTION_DISTANCE : detectionDistance;
    }

    public void setDetectionDistance(int detectionDistance) {
        this.detectionDistance = detectionDistance;
    }

    public Guard detectionDistance(int detectionDistance) {
        setDetectionDistance(detectionDistance);
        return this;
    }

    @Override
    public List<DungeonSpace> interact(DungeonObject dungeonObject) {

        if (!isActive()) {
            return Collections.emptyList();
        } else if (dungeonObject instanceof Construction) {
            NotificationManager.notify(
                new ActionNotAllowedNotification("Guards cannot move through obstacles."));
        } else if (dungeonObject instanceof DungeonMaster) {
            NotificationManager.notify(
                new ActionNotAllowedNotification("Guards cannot occupy the same space as a dungeon master."));
        } else if (dungeonObject instanceof Player) {
            Player player = (Player) dungeonObject;
            PowerUp activePowerUp = player.getActivePowerUp();
            boolean isAttackable = activePowerUp == null
                || (activePowerUp.getCorrespondingPowerUpEnum() != PowerUpEnum.INVINCIBILITY
                && activePowerUp.getCorrespondingPowerUpEnum() != PowerUpEnum.INVISIBILITY
                && activePowerUp.getCorrespondingPowerUpEnum() != PowerUpEnum.TERMINATOR);
            if (isAttackable) {
                NotificationManager.notify(
                    new InteractionNotification("A guard has caught you and moved you back to the center of the map."));
                player.incrementGuardAttacks();
                player.clearCoinsInInventory();
                player.clearPowerUpInventory();
                List<DungeonSpace> dungeonSpaces = teleport(dungeonObject);
                DungeonSpace nextDungeonSpace = teleportGuard();
                setPreviousDungeonSpace(getDungeonSpace());
                getDungeonSpace().removeDungeonObject(this);
                nextDungeonSpace.addDungeonObject(this);
                setDungeonSpace(nextDungeonSpace);
                return dungeonSpaces;
            } else if (activePowerUp != null
                && activePowerUp.getCorrespondingPowerUpEnum() == PowerUpEnum.TERMINATOR) {
                setActive(false);
            }
        }

        return Collections.emptyList();
    }

    @Override
    public DungeonSpaceType getDungeonSpaceType() {
        return DungeonSpaceType.GUARD;
    }

    @Override
    public boolean canOccupySpace(DungeonSpace dungeonSpace) {
        return dungeonSpace.getDungeonObjects().stream()
            .noneMatch(dungeonObject -> {
                return dungeonObject instanceof Construction || dungeonObject instanceof Ghost;
            });
    }

    /**
     * Use the player's position to determine whether they are in range rather than searching the surrounding tiles.
     *
     * @param dungeon
     * @param player
     * @return
     */
    @Override
    public List<DungeonSpace> move(DungeonSpace[][] dungeon, Player player) {

        return CharacterActionUtil.moveEnemy(dungeon, this, getNumberOfSpacesToMoveWhenPatrolling(),
            getNumberOfSpacesToMoveWhenHunting(), getDetectionDistance(), player);
    }

    @Override
    public List<DungeonSpace> teleport(DungeonObject dungeonObject) {

        Player player = (Player) dungeonObject;

        //move the player and then remove the player from the previous location
        DungeonSpace startingSpace = player.getDungeonSpace();
        startingSpace.removeDungeonObject(player);
        jailCellSpace.addDungeonObject(player);

        List<DungeonSpace> dungeonSpaces = Arrays.asList(startingSpace, jailCellSpace);
        return dungeonSpaces;
    }

    private DungeonSpace teleportGuard() {

        int direction = ThreadLocalRandom.current().nextInt(4);
        int startingRow;
        int startingCol;
        switch (direction) {
            case 0:// North
                startingRow = dungeon.length / 4;
                for (int row = startingRow; row < dungeon.length; row++) {
                    for (int col = 0; col < dungeon.length; col++) {
                        DungeonSpace dungeonSpace = dungeon[row][col];
                        if (dungeonSpace.isEmpty()) {
                            return dungeonSpace;
                        }
                    }
                }
                break;
            case 1:// South
                startingRow = dungeon.length - dungeon.length / 4;
                for (int row = startingRow; row >= 0; row--) {
                    for (int col = 0; col < dungeon.length; col++) {
                        DungeonSpace dungeonSpace = dungeon[row][col];
                        if (dungeonSpace.isEmpty()) {
                            return dungeonSpace;
                        }
                    }
                }
                break;
            case 2: // East
                startingCol = dungeon.length - dungeon.length / 4;
                for (int col = startingCol; col >= 0; col--) {
                    for (int row = 0; row < dungeon.length; row++) {
                        DungeonSpace dungeonSpace = dungeon[row][col];
                        if (dungeonSpace.isEmpty()) {
                            return dungeonSpace;
                        }
                    }
                }
                break;
            case 3: // West
                startingCol = dungeon.length / 4;
                for (int col = startingCol; col < dungeon.length; col++) {
                    for (int row = 0; row < dungeon.length; row++) {
                        DungeonSpace dungeonSpace = dungeon[row][col];
                        if (dungeonSpace.isEmpty()) {
                            return dungeonSpace;
                        }
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported direction: " + direction);
        }

        throw new RuntimeException("Unable to find next location for guard.");
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + getDungeonSpace().getPosition().hashCode();
        hash = 37 * hash + this.detectionDistance;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        } else if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final Guard other = (Guard) obj;
        return this.detectionDistance == other.detectionDistance
            && this.getDungeonSpace().getPosition().equals(other.getDungeonSpace().getPosition());
    }

    @Override
    public String toString() {
        return "Guard{" + "detectionDistance=" + detectionDistance + '}';
    }
}
