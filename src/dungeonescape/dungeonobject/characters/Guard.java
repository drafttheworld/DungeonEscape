/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.characters;

import dungeonescape.dungeon.notifications.ActionNotAllowedNotification;
import dungeonescape.dungeon.notifications.InteractionNotification;
import dungeonescape.dungeon.notifications.NotificationManager;
import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.dungeonobject.DungeonObjectTrack;
import dungeonescape.dungeonobject.TeleportObject;
import dungeonescape.dungeonobject.construction.Construction;
import dungeonescape.play.Direction;
import dungeonescape.space.DungeonSpace;
import dungeonescape.space.DungeonSpaceType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andrew
 */
public class Guard extends NonPersonDungeonCharacter implements TeleportObject {

    public static int DEFAULT_MOVES_WHEN_PATROLLING = 5;
    public static int DEFAULT_MOVES_WHEN_HUNTING = 4;
    public static int DEFAULT_DETECTION_DISTANCE = 5;

    private final DungeonSpace[][] dungeon;
    private final DungeonSpace jailCellSpace;
    private int detectionDistance;

    public Guard(DungeonSpace[][] dungeon, DungeonSpace jailCellSpace) {
        this.dungeon = dungeon;
        this.jailCellSpace = jailCellSpace;
        super.setActive(true);
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
    public List<DungeonObjectTrack> interact(DungeonObject dungeonObject) {

        List<DungeonObjectTrack> objectTracks = new ArrayList<>();
        if (!isActive()) {
            return objectTracks;
        } else if (dungeonObject instanceof Construction) {
            NotificationManager.notify(
                new ActionNotAllowedNotification("Guards cannot move through obstacles."));
        } else if (dungeonObject instanceof DungeonMaster) {
            NotificationManager.notify(
                new ActionNotAllowedNotification("Guards cannot occupy the same space as a dungeon master."));
        } else if (dungeonObject instanceof Player) {
            objectTracks.addAll(teleport(dungeonObject));
            super.setActive(false);
            NotificationManager.notify(
                new InteractionNotification("A guard has caught you and moved you back to the center of the map."));
        }

        return objectTracks;
    }

    @Override
    public DungeonSpaceType getDungeonSpaceType() {
        return DungeonSpaceType.GUARD;
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

    @Override
    public List<DungeonObjectTrack> move(Direction direction) {
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
    public List<DungeonObjectTrack> move(DungeonSpace[][] dungeon, Player player) {

        DungeonSpace previousDungeonSpace = getDungeonSpace();

        List<DungeonObjectTrack> movementTracks
            = CharacterActionUtil.moveEnemy(dungeon, this, getNumberOfSpacesToMoveWhenPatrolling(),
                getNumberOfSpacesToMoveWhenHunting(), getDetectionDistance(), player);

        List<DungeonObjectTrack> objectTracks = new ArrayList<>();
        objectTracks.addAll(movementTracks);

        if (previousDungeonSpace.isVisible()) {
            objectTracks.add(new DungeonObjectTrack(previousDungeonSpace.getPosition(),
                previousDungeonSpace.getVisibleDungeonSpaceType().getValueString()));
        }
        if (getDungeonSpace().isVisible()) {
            objectTracks.add(new DungeonObjectTrack(getPosition(), getDungeonSpaceType().getValueString()));
        }
        return objectTracks;
    }

    @Override
    public List<DungeonObjectTrack> teleport(DungeonObject dungeonObject) {
        Player player = (Player) dungeonObject;

        //move the player and then remove the player from the previous location
        DungeonSpace currentSpace = player.getDungeonSpace();
        jailCellSpace.addDungeonObject(player);
        currentSpace.removeDungeonObject(player);

        DungeonObjectTrack oldPlayerLocation = new DungeonObjectTrack(currentSpace.getPosition(),
            currentSpace.getVisibleDungeonSpaceType().getValueString());
        DungeonObjectTrack newPlayerLocation = new DungeonObjectTrack(player.getPosition(),
            player.getDungeonSpace().getVisibleDungeonSpaceType().getValueString());

        List<DungeonObjectTrack> objectTracks = new ArrayList<>();
        objectTracks.add(oldPlayerLocation);
        objectTracks.add(newPlayerLocation);
        return objectTracks;
    }
}
