/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.powerups;

import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.dungeon.space.DungeonSpace;
import dungeonescape.dungeon.space.DungeonSpaceType;
import java.util.List;

/**
 *
 * @author Andrew
 */
public class PowerUpBox extends DungeonObject {

    private final PowerUp powerUp;

    public PowerUpBox(PowerUp powerUp) {
        this.powerUp = powerUp;
    }

    @Override
    public List<DungeonSpace> interact(DungeonObject dungeonObject) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DungeonSpaceType getDungeonSpaceType() {
        return DungeonSpaceType.POWER_UP_BOX;
    }

}
