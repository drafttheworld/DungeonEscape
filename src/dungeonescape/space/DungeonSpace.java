/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.space;

import dungeonescape.dungeonobject.DungeonObject;

/**
 *
 * @author Andrew
 */
public class DungeonSpace {

    private Position position;
    private DungeonObject dungeonObject;

    public DungeonSpace(Position position) {
        this.position = position;
    }
    
    public DungeonSpace(Position position, DungeonObject dungeonObject) {
        this.position = position;
        
        if (dungeonObject != null && dungeonObject.getPosition() == null) {
            dungeonObject.setPosition(position);
        }
        this.dungeonObject = dungeonObject;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
        
        if (dungeonObject != null) {
            dungeonObject.setPosition(position);
        }
    }

    public DungeonObject getDungeonObject() {
        return dungeonObject;
    }

    public void setDungeonObject(DungeonObject dungeonObject) {
        if (dungeonObject != null) {
            dungeonObject.setPosition(position);
            this.dungeonObject = dungeonObject;
        }
    }

    public DungeonSpace dungeonObject(DungeonObject dungeonObject) {
        setDungeonObject(dungeonObject);
        return this;
    }

    public DungeonSpaceType getDungeonSpaceType() {
        if (dungeonObject == null) {
            return DungeonSpaceType.OPEN_SPACE;
        }
        return dungeonObject.getDungeonSpaceType();
    }

    public char getSpaceSymbol() {
        if (dungeonObject == null) {
            return DungeonSpaceType.OPEN_SPACE.getValue();
        }
        return dungeonObject.getDungeonSpaceType().getValue();
    }

}
