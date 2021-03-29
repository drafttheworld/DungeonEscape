/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.characters;

import dungeonescape.dungeon.notifications.InteractionNotification;
import dungeonescape.dungeon.notifications.NotificationManager;
import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.dungeonobject.FreezeTime;
import dungeonescape.dungeon.space.DungeonSpace;
import dungeonescape.dungeon.space.DungeonSpaceType;
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
public class Ghost extends NonPlayerCharacter {

    public static final FreezeTime DEFAULT_FREEZE_TIME = new FreezeTime(30);
    public static int DEFAULT_MOVES_WHEN_PATROLLING = 2;
    public static int DEFAULT_MOVES_WHEN_HUNTING = 3;
    public static int DEFAULT_DETECTION_DISTANCE = 10;

    private final FreezeTime freezeTime;
    private final DungeonSpace[][] dungeon;
    private int detectionDistance;

    public Ghost(FreezeTime freezeTime, DungeonSpace[][] dungeon, Player player) {
        this.freezeTime = freezeTime;
        this.dungeon = dungeon;
    }

    public FreezeTime getFreezeTime() {
        return freezeTime;
    }

    @Override
    public int getNumberOfSpacesToMoveWhenPatrolling() {
        return super.getNumberOfSpacesToMoveWhenPatrolling() == 0
            ? DEFAULT_MOVES_WHEN_PATROLLING : super.getNumberOfSpacesToMoveWhenPatrolling();
    }

    public Ghost numberOfSpacesToMoveWhenPatrolling(int numberOfMovesWhenPatrolling) {
        setNumberOfSpacesToMoveWhenPatrolling(numberOfMovesWhenPatrolling);
        return this;
    }

    @Override
    public int getNumberOfSpacesToMoveWhenHunting() {
        return super.getNumberOfSpacesToMoveWhenHunting() == 0
            ? DEFAULT_MOVES_WHEN_HUNTING : super.getNumberOfSpacesToMoveWhenHunting();
    }

    public Ghost numberOfSpacesToMoveWhenHunting(int numberOfMovesWhenHunting) {
        setNumberOfSpacesToMoveWhenHunting(numberOfMovesWhenHunting);
        return this;
    }

    public int getDetectionDistance() {
        return detectionDistance == 0 ? DEFAULT_DETECTION_DISTANCE : detectionDistance;
    }

    public void setDetectionDistance(int detectionDistance) {
        this.detectionDistance = detectionDistance;
    }

    public Ghost detectionDistance(int detectionDistance) {
        setDetectionDistance(detectionDistance);
        return this;
    }

    @Override
    public List<DungeonSpace> interact(DungeonObject dungeonObject) {

        if (isActive() && dungeonObject instanceof Player) {
            Player player = (Player) dungeonObject;
            PowerUp activePowerUp = player.getActivePowerUp();
            boolean isAttackable = activePowerUp == null
                || (activePowerUp.getCorrespondingPowerUpEnum() != PowerUpEnum.INVINCIBILITY
                && activePowerUp.getCorrespondingPowerUpEnum() != PowerUpEnum.INVISIBILITY
                && activePowerUp.getCorrespondingPowerUpEnum() != PowerUpEnum.TERMINATOR);
            if (isAttackable) {
                ((Player) dungeonObject).addFrozenTurns(freezeTime);
                NotificationManager.notify(
                    new InteractionNotification("A ghost has attacked you and frozen you in fear for "
                        + freezeTime.getTurns() + " turns."));
                // TODO teleport the ghost elsewhere in the dungeon
                int nextPosX = ThreadLocalRandom.current().nextInt(dungeon.length);
                int nextPosY = ThreadLocalRandom.current().nextInt(dungeon.length);
                DungeonSpace nextDungeonSpace = dungeon[nextPosY][nextPosX];
                setPreviousDungeonSpace(getDungeonSpace());
                getDungeonSpace().removeDungeonObject(this);
                nextDungeonSpace.addDungeonObject(this);
                setDungeonSpace(nextDungeonSpace);
                return Arrays.asList(getPreviousDungeonSpace(), nextDungeonSpace);
            } else if (activePowerUp != null
                && activePowerUp.getCorrespondingPowerUpEnum() == PowerUpEnum.TERMINATOR) {
                setActive(false);
            }
        }

        return Collections.emptyList();
    }

    @Override
    public DungeonSpaceType getDungeonSpaceType() {
        return DungeonSpaceType.GHOST;
    }

    @Override
    public boolean canOccupySpace(DungeonSpace dungeonSpace) {
        return true;
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
        hash = 29 * hash + Objects.hashCode(this.freezeTime);
        hash = 29 * hash + this.detectionDistance;
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
        final Ghost other = (Ghost) obj;
        if (this.detectionDistance != other.detectionDistance) {
            return false;
        }
        if (!Objects.equals(this.freezeTime, other.freezeTime)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Ghost{" + "freezeTime=" + freezeTime + ", detectionDistance=" + detectionDistance + '}';
    }
}
