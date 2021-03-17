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
import dungeonescape.play.Direction;
import dungeonescape.space.DungeonSpace;
import dungeonescape.space.DungeonSpaceType;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Andrew
 */
public class Ghost extends NonPersonDungeonCharacter {

    public static final FreezeTime DEFAULT_FREEZE_TIME = new FreezeTime(30);
    public static int DEFAULT_MOVES_WHEN_PATROLLING = 2;
    public static int DEFAULT_MOVES_WHEN_HUNTING = 3;
    public static int DEFAULT_DETECTION_DISTANCE = 10;

    private final DungeonSpace[][] dungeon;
    private final FreezeTime freezeTime;
    private int detectionDistance;

    public Ghost(DungeonSpace[][] dungeon, FreezeTime freezeTime) {
        this.dungeon = dungeon;
        this.freezeTime = freezeTime;
        super.setActive(true);
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
            ((Player) dungeonObject).addFrozenTurns(freezeTime);
            super.setActive(false);
            NotificationManager.notify(
                new InteractionNotification("A ghost has attacked you and frozen you in fear for "
                    + freezeTime.getTurns() + " turns."));
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

    @Override
    public List<DungeonSpace> move(Direction direction) {
        throw new UnsupportedOperationException("This method is not supported for a NonPersonDungeonCharacter.");
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
}
