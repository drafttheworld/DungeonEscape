/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject;

import dungeonescape.play.Direction;
import dungeonescape.dungeon.space.DungeonSpace;
import java.util.Objects;

/**
 *
 * @author Andrew
 */
public class DungeonObjectTrack {

    private DungeonSpace dungeonSpace;
    private Direction facingDirection;

    public DungeonObjectTrack(DungeonSpace dungeonSpace, Direction facingDirection) {
        this.dungeonSpace = dungeonSpace;
        this.facingDirection = facingDirection;
    }

    public DungeonSpace getDungeonSpace() {
        return dungeonSpace;
    }

    public Direction getFacingDirection() {
        return facingDirection;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dungeonSpace, facingDirection);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        } else if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final DungeonObjectTrack other = (DungeonObjectTrack) obj;
        return Objects.equals(this.dungeonSpace, other.dungeonSpace)
            && this.facingDirection == other.facingDirection;
    }

    @Override
    public String toString() {
        return "DungeonObjectTrack{" + "dungeonSpace=" + dungeonSpace + ", facingDirection=" + facingDirection + '}';
    }
}
