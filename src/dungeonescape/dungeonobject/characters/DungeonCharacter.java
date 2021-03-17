/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.characters;

import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.dungeonobject.DungeonObjectTrack;
import dungeonescape.play.Direction;
import dungeonescape.space.DungeonSpace;
import java.util.List;

/**
 *
 * @author Andrew
 */
public abstract class DungeonCharacter extends DungeonObject {

    private final Direction defaultFacingDirection = Direction.WEST;

    private Player player;
    private int numberOfSpacesToMoveWhenPatrolling;
    private int numberOfSpacesToMoveWhenHunting;
    private DungeonSpace previousDungeonSpace;
    private Direction currentFacingDirection;
    private boolean active;

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

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

    public abstract List<DungeonSpace> move(Direction direction);

    public abstract boolean canOccupySpace(DungeonSpace dungeonSpace);

}
