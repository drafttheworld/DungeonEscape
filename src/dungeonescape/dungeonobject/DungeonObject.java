/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject;

import dungeonescape.dungeon.notifications.GameNotification;
import dungeonescape.dungeonobject.actions.Action;
import dungeonescape.space.DungeonSpaceType;
import dungeonescape.space.Position;

/**
 *
 * @author Andrew
 */
public abstract class DungeonObject {
    
    Position position;
    Action action;
    
    public void setPosition(Position position) {
        this.position = position;
    }
    
    public Position getPosition() {
        return position;
    }
    
    public DungeonObject position(Position position) {
        setPosition(position);
        return this;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
    
    public DungeonObject action(Action action) {
        setAction(action);
        return this;
    }
    
    public abstract void interact(DungeonObject dungeonObject) throws GameNotification;
    public abstract DungeonSpaceType getDungeonSpaceType();    
    
}
