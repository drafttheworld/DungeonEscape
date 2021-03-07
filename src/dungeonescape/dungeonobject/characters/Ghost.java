/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.characters;

import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.dungeonobject.DungeonObjectTrack;
import dungeonescape.dungeonobject.FreezeTime;
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
public class Ghost extends DungeonCharacter {

    public static final FreezeTime DEFAULT_FREEZE_TIME = new FreezeTime(30);
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
    public List<DungeonObjectTrack> interact(DungeonObject dungeonObject) {
        if (dungeonObject instanceof Player) {
            ((Player) dungeonObject).addFrozenTurns(freezeTime);
            super.setActive(false);
        }

        return Collections.emptyList();
    }

    @Override
    public DungeonSpaceType getDungeonSpaceType() {
        return DungeonSpaceType.GHOST;
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
        return true;
    }

}
