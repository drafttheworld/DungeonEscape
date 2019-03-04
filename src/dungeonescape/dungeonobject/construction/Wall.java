/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.construction;

import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.space.DungeonSpaceType;

/**
 *
 * @author Andrew
 */
public class Wall extends Construction {

    @Override
    public void interact(DungeonObject dungeonObject) {
        //Do nothing I'm a wall.
    }

    @Override
    public DungeonSpaceType getDungeonSpaceType() {
        return DungeonSpaceType.WALL;
    }
    
}