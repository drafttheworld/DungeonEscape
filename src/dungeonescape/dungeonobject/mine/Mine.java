/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.mine;

import dungeonescape.dungeonobject.DungeonObject;

/**
 *
 * @author Andrew
 */
public abstract class Mine extends DungeonObject {
    
    
    Boolean active;
    
    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
    
    public Mine active(Boolean active) {
        setActive(active);
        return this;
    }
    
}
