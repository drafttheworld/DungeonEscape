/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject;

import dungeonescape.dungeon.notifications.GameNotification;

/**
 *
 * @author Andrew
 */
public interface TeleportObject {

    public void teleport(DungeonObject dungeonObject) throws GameNotification;

}
