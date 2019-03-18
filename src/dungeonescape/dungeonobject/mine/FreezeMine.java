/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.mine;

import dungeonescape.dungeonobject.FreezeTime;
import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.dungeonobject.DungeonObjectTrack;
import dungeonescape.dungeonobject.characters.Player;
import dungeonescape.space.DungeonSpaceType;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Andrew
 */
public class FreezeMine extends Mine {
    
    public static final FreezeTime DEFAULT_MAX_FREEZE_TIME = new FreezeTime(5, TimeUnit.MINUTES);
    
    FreezeTime freezeTime;
    
    public FreezeMine(FreezeTime freezeTime) {
        this.freezeTime = freezeTime;
        super.setActive(true);
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
    public List<DungeonObjectTrack> interact(DungeonObject dungeonObject) {
        if (dungeonObject instanceof Player) {
            ((Player) dungeonObject).addFrozenTime(freezeTime);
            setActive(false);
        }
        return Collections.emptyList();
    }

    @Override
    public DungeonSpaceType getDungeonSpaceType() {
        return DungeonSpaceType.FREEZE_MINE;
    }
    
}
