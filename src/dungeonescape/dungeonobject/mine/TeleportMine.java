/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.mine;

import dungeonescape.dungeon.notifications.InteractionNotification;
import dungeonescape.dungeon.notifications.NotificationManager;
import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.dungeonobject.TeleportObject;
import dungeonescape.dungeonobject.characters.Player;
import dungeonescape.space.DungeonSpace;
import dungeonescape.space.DungeonSpaceType;
import java.util.Collections;
import java.util.List;

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
    public List<DungeonSpace> interact(DungeonObject dungeonObject) {

        if (dungeonObject instanceof Player) {
            setActive(false);
            NotificationManager.notify(
                new InteractionNotification(
                    "You stepped on a teleport mine and have been transported to another part of the dungeon."));
            return teleport(dungeonObject);
        }

        return Collections.emptyList();
    }

    @Override
    public DungeonSpaceType getDungeonSpaceType() {
        return DungeonSpaceType.TELEPORT_MINE;
    }

    @Override
    public List<DungeonSpace> teleport(DungeonObject dungeonObject) {
        Player player = (Player) dungeonObject;

        //move the player to the next location
        player.setPreviousDungeonSpace(player.getDungeonSpace());
        player.getDungeonSpace().removeDungeonObject(player);
        teleportSpace.addDungeonObject(player);
        return player.revealCurrentMapArea();
    }

}
