/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.mine;

import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.dungeonobject.DungeonObjectTrack;
import dungeonescape.space.DungeonSpaceType;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Andrew
 */
public class TeleportDestination extends DungeonObject {

    @Override
    public List<DungeonObjectTrack> interact(DungeonObject dungeonObject) {
        return Collections.emptyList();
    }

    @Override
    public DungeonSpaceType getDungeonSpaceType() {
        return DungeonSpaceType.TELEPORT_LOCATION;
    }
    
}
