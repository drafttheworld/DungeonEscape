/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.characters;

import dungeonescape.dungeon.notifications.GameNotification;
import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.dungeonobject.FreezeTime;
import dungeonescape.play.Direction;
import dungeonescape.space.DungeonSpace;
import dungeonescape.space.DungeonSpaceType;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Andrew
 */
public class Ghost extends DungeonCharacter {
    
    public static final FreezeTime DEFAULT_FREEZE_TIME = new FreezeTime(30, TimeUnit.MINUTES);
    
    private final FreezeTime freezeTime;
    
    public Ghost(FreezeTime freezeTime) {
        this.freezeTime = freezeTime;
    }

    @Override
    public void interact(DungeonObject dungeonObject) throws GameNotification {
        if (dungeonObject instanceof Player) {
            ((Player) dungeonObject).addFrozenTime(freezeTime);
        }
    }

    @Override
    public DungeonSpaceType getDungeonSpaceType() {
        return DungeonSpaceType.GHOST;
    }

    @Override
    public void move(Direction direction, DungeonSpace[][] dungeon) throws GameNotification {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
