/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject;

import dungeonescape.space.Position;

/**
 *
 * @author Andrew
 */
public class DungeonObjectTrack {
    
    private final Position position;
    private final String dungeonSpaceSymbol;
    
    public DungeonObjectTrack(Position position, String dungeonSpaceSymbol) {
        this.position = position;
        this.dungeonSpaceSymbol = dungeonSpaceSymbol;
    }

    public Position getPosition() {
        return position;
    }

    public String getDungeonSpaceSymbol() {
        return dungeonSpaceSymbol;
    }  

    @Override
    public String toString() {
        return "DungeonObjectTrack{" + "position=" + position + ", dungeonSpaceSymbol=" + dungeonSpaceSymbol + '}';
    }
}
