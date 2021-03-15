/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.characters;

import dungeonescape.dungeonobject.DungeonObjectTrack;
import java.util.Objects;

/**
 *
 * @author Andrew
 */
public class CharacterMovement {

    private final DungeonObjectTrack startingDungeonTrack;
    private final DungeonObjectTrack endingDungeonTrack;

    public CharacterMovement(DungeonObjectTrack startingDungeonTrack, DungeonObjectTrack endingDungeonTrack) {
        this.startingDungeonTrack = startingDungeonTrack;
        this.endingDungeonTrack = endingDungeonTrack;
    }

    public DungeonObjectTrack getStartingDungeonTrack() {
        return startingDungeonTrack;
    }

    public DungeonObjectTrack getEndingDungeonTrack() {
        return endingDungeonTrack;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startingDungeonTrack, endingDungeonTrack);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        } else if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final CharacterMovement other = (CharacterMovement) obj;
        return Objects.equals(this.startingDungeonTrack, other.startingDungeonTrack)
            && Objects.equals(this.endingDungeonTrack, other.endingDungeonTrack);
    }

    @Override
    public String toString() {
        return "CharacterMovement{" + "startingDungeonTrack=" + startingDungeonTrack + ", endingDungeonTrack=" + endingDungeonTrack + '}';
    }

}
