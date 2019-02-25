/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.mine;

import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.play.GameSession;
import dungeonescape.space.DungeonSpaceType;

/**
 *
 * @author Andrew
 */
public class TeleportDestination extends DungeonObject {

    @Override
    public void act(GameSession session) {
    }

    @Override
    public DungeonSpaceType getDungeonSpaceType() {
        return DungeonSpaceType.TELEPORT_LOCATION;
    }
    
}
