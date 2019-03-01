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
public class FreezeMine extends Mine {
    
    FreezeTime freezeTime;
    
    public FreezeMine(FreezeTime freezeTime) {
        this.freezeTime = freezeTime;
    }
    
    public void setFreezeDuration(FreezeTime freezeTime) {
        this.freezeTime = freezeTime;
    }
    
    public FreezeMine freezeDuration(FreezeTime freezeTime) {
        setFreezeDuration(freezeTime);
        return this;
    }
    
    public FreezeTime getFreezeDuration() {
        return freezeTime;
    }

    @Override
    public void interact(DungeonObject dungeonObject) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DungeonSpaceType getDungeonSpaceType() {
        return DungeonSpaceType.FREEZE_MINE;
    }
    
}
