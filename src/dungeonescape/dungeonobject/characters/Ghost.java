/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.characters;

import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.dungeonobject.FreezeTime;
import dungeonescape.play.Direction;
import dungeonescape.space.DungeonSpace;
import dungeonescape.space.DungeonSpaceType;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Andrew
 */
public class Ghost extends DungeonCharacter {
    
    public static final FreezeTime DEFAULT_FREEZE_TIME = new FreezeTime(30, TimeUnit.MINUTES);
    public static int DEFAULT_MOVES_WHEN_PATROLLING = 2;
    public static int DEFAULT_MOVES_WHEN_HUNTING = 3;
    public static int DEFAULT_DETECTION_DISTANCE = 10;
    
    private final FreezeTime freezeTime;
    private int detectionDistance;
    
    public Ghost(FreezeTime freezeTime) {
        this.freezeTime = freezeTime;
        super.setActive(true);
    }
    
    public FreezeTime getFreezeTime() {
        return freezeTime;
    }

    @Override
    public int getNumberOfSpacesToMoveWhenPatrolling() {
        return super.getNumberOfSpacesToMoveWhenPatrolling() == 0 ? 
                DEFAULT_MOVES_WHEN_PATROLLING : super.getNumberOfSpacesToMoveWhenPatrolling();
    }
    
    public Ghost numberOfSpacesToMoveWhenPatrolling(int numberOfMovesWhenPatrolling) {
        setNumberOfSpacesToMoveWhenPatrolling(numberOfMovesWhenPatrolling);
        return this;
    }

    @Override
    public int getNumberOfSpacesToMoveWhenHunting() {
        return super.getNumberOfSpacesToMoveWhenHunting() == 0 ? 
                DEFAULT_MOVES_WHEN_HUNTING : super.getNumberOfSpacesToMoveWhenHunting();
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
    public void interact(DungeonObject dungeonObject) {
        if (dungeonObject instanceof Player) {
            ((Player) dungeonObject).addFrozenTime(freezeTime);
            System.out.println("You were attacked by a ghost, added frozen time.");
            getDungeonSpace().removeDungeonObject(this);
            super.setActive(false);
        }
    }

    @Override
    public DungeonSpaceType getDungeonSpaceType() {
        return DungeonSpaceType.GHOST;
    }

    @Override
    public void move(Direction direction, DungeonSpace[][] dungeon) {
        CharacterActionUtil.moveEnemy(dungeon, this, getNumberOfSpacesToMoveWhenPatrolling(), 
                getNumberOfSpacesToMoveWhenHunting(), getDetectionDistance());
    }

    @Override
    public boolean canOccupySpace(DungeonSpace dungeonSpace) {
        return true;
    }
    
}
