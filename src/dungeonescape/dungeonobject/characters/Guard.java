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
import dungeonescape.dungeonobject.TeleportObject;
import dungeonescape.dungeonobject.construction.Construction;
import dungeonescape.play.Direction;
import dungeonescape.dungeon.space.DungeonSpace;
import dungeonescape.dungeon.space.DungeonSpaceType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
            super.setActive(false);
            NotificationManager.notify(
                new InteractionNotification("A guard has caught you and moved you back to the center of the map."));
            return teleport(dungeonObject);
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
    public List<DungeonSpace> teleport(DungeonObject dungeonObject) {

        Player player = (Player) dungeonObject;

        //move the player and then remove the player from the previous location
        DungeonSpace startingSpace = player.getDungeonSpace();
        jailCellSpace.addDungeonObject(player);
        startingSpace.removeDungeonObject(player);

        return Arrays.asList(startingSpace, jailCellSpace);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.jailCellSpace);
        hash = 67 * hash + this.detectionDistance;
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
        final Guard other = (Guard) obj;
        if (this.detectionDistance != other.detectionDistance) {
            return false;
        }
        if (!Objects.equals(this.jailCellSpace, other.jailCellSpace)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Guard{" + "jailCellSpace=" + jailCellSpace + ", detectionDistance=" + detectionDistance + '}';
    }
}
