/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.mine;

import dungeonescape.dungeon.notifications.GameNotification;
import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.dungeonobject.characters.Player;
import dungeonescape.play.Direction;
import dungeonescape.space.DungeonSpace;
import dungeonescape.space.DungeonSpaceType;

/**
 *
 * @author Andrew
 */
public class TeleportMine extends Mine {
    
    private DungeonSpace teleportSpace;
    
    public TeleportMine(DungeonSpace teleportSpace) {
        this.teleportSpace = teleportSpace;
    }
    
    public void setTeleportPosition(DungeonSpace teleportSpace) {
        this.teleportSpace = teleportSpace;
    }
    
    public TeleportMine teleportPosition(DungeonSpace teleportSpace) {
        setTeleportPosition(teleportSpace);
        return this;
    }
    
    public DungeonSpace getTeleportPosition() {
        return teleportSpace;
    }

    @Override
    public void interact(DungeonObject dungeonObject) throws GameNotification {
        if (dungeonObject instanceof Player) {
            Player player = (Player) dungeonObject;
            player.getDungeonSpace().removeDungeonObject(player);
            teleportSpace.addDungeonObject(player);
        }
    }

    @Override
    public DungeonSpaceType getDungeonSpaceType() {
        return DungeonSpaceType.TELEPORT_MINE;
    }
    
}
