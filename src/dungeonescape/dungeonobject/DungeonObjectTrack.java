/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject;

import dungeonescape.play.Direction;
import dungeonescape.space.Position;
import java.util.Objects;

/**
 *
 * @author Andrew
 */
public class DungeonObjectTrack {

    private Position previousPosition;
    private Direction previousFacingDirection;
    private String previousDungeonSpaceSymbol;
    private Position currentPosition;
    private Direction currentFacingDirection;
    private String currentDungeonSpaceSymbol;

    public Position getPreviousPosition() {
        return previousPosition;
    }

    public void setPreviousPosition(Position previousPosition) {
        this.previousPosition = previousPosition;
    }

    public DungeonObjectTrack previousPosition(Position previousPosition) {
        setPreviousPosition(previousPosition);
        return this;
    }

    public Direction getPreviousFacingDirection() {
        return previousFacingDirection;
    }

    public void setPreviousFacingDirection(Direction previousFacingDirection) {
        this.previousFacingDirection = previousFacingDirection;
    }

    public DungeonObjectTrack previousFacingDirection(Direction previousFacingDirection) {
        setPreviousFacingDirection(previousFacingDirection);
        return this;
    }

    public String getPreviousDungeonSpaceSymbol() {
        return previousDungeonSpaceSymbol;
    }

    public void setPreviousDungeonSpaceSymbol(String previousDungeonSpaceSymbol) {
        this.previousDungeonSpaceSymbol = previousDungeonSpaceSymbol;
    }

    public DungeonObjectTrack previousDungeonSpaceSymbol(String previousDungeonSpaceSymbol) {
        setPreviousDungeonSpaceSymbol(previousDungeonSpaceSymbol);
        return this;
    }

    public Position getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Position currentPosition) {
        this.currentPosition = currentPosition;
    }

    public DungeonObjectTrack currentPosition(Position currentPosition) {
        setCurrentPosition(currentPosition);
        return this;
    }

    public Direction getCurrentFacingDirection() {
        return currentFacingDirection;
    }

    public void setCurrentFacingDirection(Direction currentFacingDirection) {
        this.currentFacingDirection = currentFacingDirection;
    }

    public DungeonObjectTrack currentFacingDirection(Direction currentFacingDirection) {
        setCurrentFacingDirection(currentFacingDirection);
        return this;
    }

    public String getCurrentDungeonSpaceSymbol() {
        return currentDungeonSpaceSymbol;
    }

    public void setCurrentDungeonSpaceSymbol(String currentDungeonSpaceSymbol) {
        this.currentDungeonSpaceSymbol = currentDungeonSpaceSymbol;
    }

    public DungeonObjectTrack currentDungeonSpaceSymbol(String currentDungeonSpaceSymbol) {
        setCurrentDungeonSpaceSymbol(currentDungeonSpaceSymbol);
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(previousPosition, previousFacingDirection, previousDungeonSpaceSymbol, currentPosition,
            currentFacingDirection, currentDungeonSpaceSymbol);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        } else if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final DungeonObjectTrack other = (DungeonObjectTrack) obj;
        return Objects.equals(this.previousDungeonSpaceSymbol, other.previousDungeonSpaceSymbol)
            && Objects.equals(this.currentDungeonSpaceSymbol, other.currentDungeonSpaceSymbol)
            && Objects.equals(this.previousPosition, other.previousPosition)
            && this.previousFacingDirection == other.previousFacingDirection
            && Objects.equals(this.currentPosition, other.currentPosition)
            && this.currentFacingDirection == other.currentFacingDirection;
    }

    @Override
    public String toString() {
        return "DungeonObjectTrack{" + "previousPosition=" + previousPosition
            + ", previousFacingDirection=" + previousFacingDirection
            + ", previousDungeonSpaceSymbol=" + previousDungeonSpaceSymbol
            + ", currentPosition=" + currentPosition
            + ", currentFacingDirection=" + currentFacingDirection
            + ", currentDungeonSpaceSymbol=" + currentDungeonSpaceSymbol + '}';
    }
}
