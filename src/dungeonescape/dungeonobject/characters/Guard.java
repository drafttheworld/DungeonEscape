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
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Andrew
 */
public class Guard extends DungeonCharacter implements TeleportObject {

    public static int DEFAULT_MOVES_WHEN_PATROLLING = 5;
    public static int DEFAULT_MOVES_WHEN_HUNTING = 4;
    public static int DEFAULT_DETECTION_DISTANCE = 5;

    private final DungeonSpace jailCellSpace;
    private int detectionDistance;

    public Guard(DungeonSpace jailCellSpace) {
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
        if (dungeonObject instanceof Construction) {
            NotificationManager.notify(
                    new ActionNotAllowedNotification("Guards cannot move through obstacles."));
        } else if (dungeonObject instanceof DungeonMaster) {
            NotificationManager.notify(
                    new ActionNotAllowedNotification("Guards cannot occupy the same space as a dungeon master."));
        } else if (dungeonObject instanceof Player) {
            objectTracks.addAll(teleport(dungeonObject));
            getDungeonSpace().removeDungeonObject(this);
            super.setActive(false);
            NotificationManager.notify(
                    new InteractionNotification("A guard has caught you and moved you back to your cell."));
        }
        
        return Collections.emptyList();
    }

    @Override
    public DungeonSpaceType getDungeonSpaceType() {
        return DungeonSpaceType.GUARD;
    }

    @Override
    public List<DungeonObjectTrack> move(Direction direction, DungeonSpace[][] dungeon) {
        
        DungeonSpace previousDungeonSpace = getDungeonSpace();
        
        CharacterActionUtil.moveEnemy(dungeon, this, getNumberOfSpacesToMoveWhenPatrolling(),
                getNumberOfSpacesToMoveWhenHunting(), getDetectionDistance());

        List<DungeonObjectTrack> objectTracks = new ArrayList<>();
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
    public boolean canOccupySpace(DungeonSpace dungeonSpace) {
        return dungeonSpace.getDungeonObjects().stream()
                .noneMatch(dungeonObject -> {
                    return dungeonObject instanceof Construction
                            || (dungeonObject instanceof DungeonCharacter
                            && !(dungeonObject instanceof Ghost));
                });
    }

    @Override
    public List<DungeonObjectTrack> teleport(DungeonObject dungeonObject) {
        Player player = (Player) dungeonObject;
        
        DungeonObjectTrack oldPlayerLocation = new DungeonObjectTrack(player.getPosition(), 
                player.getDungeonSpace().getVisibleDungeonSpaceType().getValueString());

        //move the player and then remove the player from the previous location
        DungeonSpace currentSpace = player.getDungeonSpace();
        jailCellSpace.addDungeonObject(player);
        currentSpace.removeDungeonObject(player);
        
        DungeonObjectTrack newPlayerLocation = new DungeonObjectTrack(player.getPosition(), 
                player.getDungeonSpace().getVisibleDungeonSpaceType().getValueString());
        
        List<DungeonObjectTrack> objectTracks = new ArrayList<>();
        objectTracks.add(oldPlayerLocation);
        objectTracks.add(newPlayerLocation);
        return objectTracks;
    }

}
