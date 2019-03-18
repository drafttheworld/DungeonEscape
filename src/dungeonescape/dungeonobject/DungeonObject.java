/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject;

import dungeonescape.space.DungeonSpace;
import dungeonescape.space.DungeonSpaceType;
import dungeonescape.space.Position;
import java.util.List;

/**
 *
 * @author Andrew
 */
public abstract class DungeonObject {
    
    private DungeonSpace dungeonSpace;

    public DungeonSpace getDungeonSpace() {
        return dungeonSpace;
    }

    public void setDungeonSpace(DungeonSpace dungeonSpace) {
        this.dungeonSpace = dungeonSpace;
    }
    
    public Position getPosition() {
        if (dungeonSpace == null) {
            return null;
        }
        
        return dungeonSpace.getPosition();
    }
    
    public abstract List<DungeonObjectTrack> interact(DungeonObject dungeonObject);
    public abstract DungeonSpaceType getDungeonSpaceType();    
    
}
