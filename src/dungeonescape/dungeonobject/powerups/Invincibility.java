/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.powerups;

import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.space.DungeonSpace;
import dungeonescape.space.DungeonSpaceType;
import java.util.List;

/**
 * Enemies attack but are then transported to a random location on the map without affecting the player.
 *
 * @author Andrew
 */
public class Invincibility extends DungeonObject {

    @Override
    public List<DungeonSpace> interact(DungeonObject dungeonObject) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DungeonSpaceType getDungeonSpaceType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
