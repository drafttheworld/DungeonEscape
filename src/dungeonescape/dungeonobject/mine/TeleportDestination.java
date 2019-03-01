/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.mine;

import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.space.DungeonSpaceType;

/**
 *
 * @author Andrew
 */
public class TeleportDestination extends DungeonObject {

    @Override
    public void interact(DungeonObject dungeonObject) {
        //Do nothing I'm simply a destination
    }

    @Override
    public DungeonSpaceType getDungeonSpaceType() {
        return DungeonSpaceType.TELEPORT_LOCATION;
    }
    
}
