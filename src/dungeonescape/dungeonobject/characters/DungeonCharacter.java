/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.characters;

import dungeonescape.dungeon.notifications.GameNotification;
import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.dungeonobject.actions.move.Direction;
import dungeonescape.space.DungeonSpace;

/**
 *
 * @author Andrew
 */
public abstract class DungeonCharacter extends DungeonObject {
    
    public abstract void move(Direction direction, DungeonSpace[][] dungeon) throws GameNotification;
    
}
