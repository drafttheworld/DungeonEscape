/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.mine;

import dungeonescape.dungeonobject.FreezeTime;
import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.dungeonobject.characters.Player;
import dungeonescape.dungeon.space.DungeonSpace;
import dungeonescape.dungeon.space.DungeonSpaceType;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Andrew
 */
public class FreezeMine extends Mine {

    public static final FreezeTime DEFAULT_MAX_FREEZE_TIME = new FreezeTime(5);

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
    public List<DungeonSpace> interact(DungeonObject dungeonObject) {
        if (dungeonObject instanceof Player) {
            Player player = (Player) dungeonObject;
            player.addFrozenTurns(freezeTime);
            player.incrementFreezeMinesTripped();
            setActive(false);
        }
        return Collections.emptyList();
    }

    @Override
    public DungeonSpaceType getDungeonSpaceType() {
        return DungeonSpaceType.FREEZE_MINE;
    }

}
