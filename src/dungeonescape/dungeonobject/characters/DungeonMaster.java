/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.characters;

import dungeonescape.dungeon.notifications.ActionNotAllowedNotification;
import dungeonescape.dungeon.notifications.GameNotification;
import dungeonescape.dungeon.notifications.LossNotification;
import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.dungeonobject.construction.Construction;
import dungeonescape.play.Direction;
import dungeonescape.space.DungeonSpace;
import dungeonescape.space.DungeonSpaceType;

/**
 *
 * @author Andrew
 */
public class DungeonMaster extends DungeonCharacter {

    public static int DEFAULT_MOVES_WHEN_PATROLLING = 3;
    public static int DEFAULT_MOVES_WHEN_HUNTING = 4;
    public static int DEFAULT_DETECTION_DISTANCE = 10;
    
    public static final String CAPTURE_NOTIFICATION = "You have been caught and executed by a DUNGEON MASTER!";
    
    private int numberOfMovesWhenPatrolling;
    private int numberOfMovesWhenHunting;
    private int detectionDistance;

    public int getNumberOfMovesWhenPatrolling() {
        return numberOfMovesWhenPatrolling == 0 ? DEFAULT_MOVES_WHEN_PATROLLING : numberOfMovesWhenPatrolling;
    }

    public void setNumberOfMovesWhenPatrolling(int numberOfMovesWhenPatrolling) {
        this.numberOfMovesWhenPatrolling = numberOfMovesWhenPatrolling;
    }
    
    public DungeonMaster numberOfMovesWhenPatrolling(int numberOfMovesWhenPatrolling) {
        setNumberOfMovesWhenPatrolling(numberOfMovesWhenPatrolling);
        return this;
    }

    public int getNumberOfMovesWhenHunting() {
        return numberOfMovesWhenHunting == 0 ? DEFAULT_MOVES_WHEN_HUNTING : numberOfMovesWhenHunting;
    }

    public void setNumberOfMovesWhenHunting(int numberOfMovesWhenHunting) {
        this.numberOfMovesWhenHunting = numberOfMovesWhenHunting;
    }
    
    public DungeonMaster numberOfMovesWhenHunting(int numberOfMovesWhenHunting) {
        setNumberOfMovesWhenHunting(numberOfMovesWhenHunting);
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
    public void interact(DungeonObject dungeonObject) throws GameNotification {
        if (dungeonObject instanceof Construction) {
            throw new ActionNotAllowedNotification("A dungeon master cannot move through obstacles.");
        } else if (dungeonObject instanceof DungeonMaster) {
            throw new ActionNotAllowedNotification("A dungeon master cannot occupy the same space as a guard.");
        } else if (dungeonObject instanceof Player) {
            throw new LossNotification(CAPTURE_NOTIFICATION);
        }
    }

    /**
     * Moves 3 spaces per turn. Can only occupy empty dungeon spaces. Movement
     * direction is random unless the player is within line of sight. When the
     * player is in the dungeon master's line of sight movement direction will
     * be toward the player.
     *
     * @param direction
     * @param dungeon
     * @throws GameNotification
     */
    @Override
    public void move(Direction direction, DungeonSpace[][] dungeon) throws GameNotification {
        CharacterActionUtil.moveEnemy(dungeon, this, getDetectionDistance(), getNumberOfSpacesToMoveWhenPatrolling(), getNumberOfSpacesToMoveWhenHunting());
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
