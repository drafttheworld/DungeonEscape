/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.mine;

import dungeonescape.dungeon.notifications.GameNotification;
import dungeonescape.dungeon.notifications.InteractionNotification;
import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.dungeonobject.TeleportObject;
import dungeonescape.dungeonobject.characters.Player;
import dungeonescape.space.DungeonSpace;
import dungeonescape.space.DungeonSpaceType;

/**
 *
 * @author Andrew
 */
public class TeleportMine extends Mine implements TeleportObject {

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
            teleport(dungeonObject);
            setActive(false);
            throw new InteractionNotification("You stepped on a teleport mine and have been transported to ["
                + getPosition().getPositionX() + "," + getPosition().getPositionY() + "]");
        }
    }

    @Override
    public DungeonSpaceType getDungeonSpaceType() {
        return DungeonSpaceType.TELEPORT_MINE;
    }

    @Override
    public void teleport(DungeonObject dungeonObject) throws GameNotification {
        Player player = (Player) dungeonObject;
        
        //move the player to the next location
        teleportSpace.addDungeonObject(player);
    }

}
