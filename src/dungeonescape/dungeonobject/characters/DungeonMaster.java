/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.characters;

import dungeonescape.dungeon.notifications.ActionNotAllowedNotification;
import dungeonescape.dungeon.notifications.GameNotification;
import dungeonescape.dungeon.notifications.LossNotification;
import dungeonescape.dungeon.notifications.NotificationManager;
import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.dungeonobject.construction.Construction;
import dungeonescape.dungeon.space.DungeonSpace;
import dungeonescape.dungeon.space.DungeonSpaceType;
import dungeonescape.dungeonobject.powerups.PowerUp;
import dungeonescape.dungeonobject.powerups.PowerUpEnum;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Andrew
 */
public class DungeonMaster extends NonPlayerCharacter {

    public static int DEFAULT_MOVES_WHEN_PATROLLING = 3;
    public static int DEFAULT_MOVES_WHEN_HUNTING = 4;
    public static int DEFAULT_DETECTION_DISTANCE = 8;

    public static final String CAPTURE_NOTIFICATION
        = "You have been caught and executed by a DUNGEON MASTER!";

    private int detectionDistance;

    public DungeonMaster(Player player) {
        super(player);
    }

    @Override
    public int getNumberOfSpacesToMoveWhenPatrolling() {
        return super.getNumberOfSpacesToMoveWhenPatrolling() == 0
            ? DEFAULT_MOVES_WHEN_PATROLLING : super.getNumberOfSpacesToMoveWhenPatrolling();
    }

    public DungeonMaster numberOfMovesWhenPatrolling(int numberOfMovesWhenPatrolling) {
        setNumberOfSpacesToMoveWhenPatrolling(numberOfMovesWhenPatrolling);
        return this;
    }

    @Override
    public int getNumberOfSpacesToMoveWhenHunting() {
        return super.getNumberOfSpacesToMoveWhenHunting() == 0
            ? DEFAULT_MOVES_WHEN_HUNTING : super.getNumberOfSpacesToMoveWhenHunting();
    }

    public DungeonMaster numberOfMovesWhenHunting(int numberOfMovesWhenHunting) {
        setNumberOfSpacesToMoveWhenHunting(numberOfMovesWhenHunting);
        return this;
    }

    public int getDetectionDistance() {
        return detectionDistance == 0 ? DEFAULT_DETECTION_DISTANCE : detectionDistance;
    }

    public void setDetectionDistance(int detectionDistance) {
        this.detectionDistance = detectionDistance;
    }

    public DungeonMaster detectionDistance(int detectionDistance) {
        setDetectionDistance(detectionDistance);
        return this;
    }

    /**
     * The dungeon master does not interact with any other object in the dungeon other than the player. If the player is
     * caught by the dungeon master the game is over.
     *
     * @param dungeonObject
     * @return
     * @throws GameNotification
     */
    @Override
    public List<DungeonSpace> interact(DungeonObject dungeonObject) {

        if (!isActive()) {
            return Collections.emptyList();
        } else if (dungeonObject instanceof Construction) {
            NotificationManager.notify(
                new ActionNotAllowedNotification("A dungeon master cannot move "
                    + "through obstacles."));
        } else if (dungeonObject instanceof DungeonMaster) {
            NotificationManager.notify(
                new ActionNotAllowedNotification("A dungeon master cannot occupy the "
                    + "same space as another dungeon master."));
        } else if (dungeonObject instanceof Player) {
            PowerUp activePowerUp = getPlayer().getActivePowerUp();
            boolean isAttackable = activePowerUp == null
                || (activePowerUp.getCorrespondingPowerUpEnum() != PowerUpEnum.INVINCIBILITY
                && activePowerUp.getCorrespondingPowerUpEnum() != PowerUpEnum.INVISIBILITY
                && activePowerUp.getCorrespondingPowerUpEnum() != PowerUpEnum.TERMINATOR);
            if (isAttackable) {
                NotificationManager.notify(
                    new LossNotification(CAPTURE_NOTIFICATION));
            }
        }

        return Collections.emptyList();
    }

    @Override
    public DungeonSpaceType getDungeonSpaceType() {
        return DungeonSpaceType.DUNGEON_MASTER;
    }

    @Override
    public boolean canOccupySpace(DungeonSpace dungeonSpace) {
        return dungeonSpace.getDungeonObjects().stream()
            .noneMatch(dungeonObject -> {
                return dungeonObject instanceof Construction
                    || (dungeonObject instanceof DungeonCharacter
                    && !(dungeonObject instanceof Ghost));
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
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + this.detectionDistance;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DungeonMaster other = (DungeonMaster) obj;
        if (this.detectionDistance != other.detectionDistance) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DungeonMaster{" + "detectionDistance=" + detectionDistance + '}';
    }
}
