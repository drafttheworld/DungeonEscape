/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.characters;

import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.play.Direction;
import dungeonescape.dungeon.space.DungeonSpace;
import java.util.List;

/**
 *
 * @author Andrew
 */
public abstract class DungeonCharacter extends DungeonObject {

    private final Direction defaultFacingDirection = Direction.WEST;

    private int numberOfSpacesToMoveWhenPatrolling;
    private int numberOfSpacesToMoveWhenHunting;
    private DungeonSpace previousDungeonSpace;
    private Direction previousFacingDirection;
    private Direction currentFacingDirection;
    private boolean active = true;

    public int getNumberOfSpacesToMoveWhenPatrolling() {
        return numberOfSpacesToMoveWhenPatrolling;
    }

    public void setNumberOfSpacesToMoveWhenPatrolling(int numberOfSpacesToMoveWhenPatrolling) {
        this.numberOfSpacesToMoveWhenPatrolling = numberOfSpacesToMoveWhenPatrolling;
    }

    public int getNumberOfSpacesToMoveWhenHunting() {
        return numberOfSpacesToMoveWhenHunting;
    }

    public void setNumberOfSpacesToMoveWhenHunting(int numberOfSpacesToMoveWhenHunting) {
        this.numberOfSpacesToMoveWhenHunting = numberOfSpacesToMoveWhenHunting;
    }

    public Direction getPreviousFacingDirection() {
        return previousFacingDirection;
    }

    public void setPreviousFacingDirection(Direction previousFacingDirection) {
        this.previousFacingDirection = previousFacingDirection;
    }

    public DungeonSpace getPreviousDungeonSpace() {
        return previousDungeonSpace;
    }

    public void setPreviousDungeonSpace(DungeonSpace previousDungeonSpace) {
        this.previousDungeonSpace = previousDungeonSpace;
    }

    public Direction getCurrentFacingDirection() {
        if (currentFacingDirection == null) {
            return defaultFacingDirection;
        }
        return currentFacingDirection;
    }

    public void setCurrentFacingDirection(Direction currentFacingDirection) {
        this.currentFacingDirection = currentFacingDirection;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public abstract List<DungeonSpace> move(DungeonSpace[][] dungeon, Player player);

    public abstract boolean canOccupySpace(DungeonSpace dungeonSpace);

}
