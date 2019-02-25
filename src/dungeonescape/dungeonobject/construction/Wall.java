/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.construction;

import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.play.GameSession;
import dungeonescape.space.DungeonSpaceType;

/**
 *
 * @author Andrew
 */
public class Wall extends DungeonObject {

    @Override
    public void act(GameSession session) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DungeonSpaceType getDungeonSpaceType() {
        return DungeonSpaceType.WALL;
    }
    
}
