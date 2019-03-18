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
import dungeonescape.dungeonobject.DungeonObjectTrack;
import dungeonescape.dungeonobject.construction.Construction;
import dungeonescape.play.Direction;
import dungeonescape.space.DungeonSpace;
import dungeonescape.space.DungeonSpaceType;
import dungeonescape.space.Position;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Andrew
 */
public class DungeonMaster extends DungeonCharacter {

    public static int DEFAULT_MOVES_WHEN_PATROLLING = 3;
    public static int DEFAULT_MOVES_WHEN_HUNTING = 4;
    public static int DEFAULT_DETECTION_DISTANCE = 8;

    public static final String CAPTURE_NOTIFICATION
            = "You have been caught and executed by a DUNGEON MASTER!";

    private int detectionDistance;

    public DungeonMaster() {
        super.setActive(true);
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
     * The dungeon master does not interact with any other object in the dungeon
     * other than the player. If the player is caught by the dungeon master the
     * game is over.
     *
     * @param dungeonObject
     * @throws GameNotification
     */
    @Override
    public List<DungeonObjectTrack> interact(DungeonObject dungeonObject) {
        if (dungeonObject instanceof Construction) {
            NotificationManager.notify(
                    new ActionNotAllowedNotification("A dungeon master cannot move "
                            + "through obstacles."));
        } else if (dungeonObject instanceof DungeonMaster) {
            NotificationManager.notify(
                    new ActionNotAllowedNotification("A dungeon master cannot occupy the "
                            + "same space as another dungeon master."));
        } else if (dungeonObject instanceof Player) {
            NotificationManager.notify(
                    new LossNotification(CAPTURE_NOTIFICATION));
        }
        
        return Collections.emptyList();
    }

    /**
     * Moves 3 spaces per turn. Can only occupy empty dungeon spaces. Movement
     * direction is random unless the player is within line of sight. When the
     * player is in the dungeon master's line of sight movement direction will
     * be toward the player.
     *
     * @param direction
     * @param dungeon
     * @return
     * @throws GameNotification
     */
    @Override
    public List<DungeonObjectTrack> move(Direction direction, DungeonSpace[][] dungeon) {

        DungeonSpace previousDungeonSpace = getDungeonSpace();
        
        CharacterActionUtil.moveEnemy(dungeon, this, getNumberOfSpacesToMoveWhenPatrolling(),
                getNumberOfSpacesToMoveWhenHunting(), getDetectionDistance());

        List<DungeonObjectTrack> objectTracks = new ArrayList<>();
        if (getPreviousDungeonSpace().isVisible()) {
            objectTracks.add(new DungeonObjectTrack(previousDungeonSpace.getPosition(),
                    previousDungeonSpace.getVisibleDungeonSpaceType().getValueString()));
        }
        if (getDungeonSpace().isVisible()) {
            objectTracks.add(new DungeonObjectTrack(getPosition(), getDungeonSpaceType().getValueString()));
        }
        return objectTracks;
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

}
