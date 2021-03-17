/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.construction;

import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.space.DungeonSpace;
import dungeonescape.space.DungeonSpaceType;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Andrew
 */
public class Wall extends Construction {

    @Override
    public List<DungeonSpace> interact(DungeonObject dungeonObject) {
        return Collections.emptyList();
    }

    @Override
    public DungeonSpaceType getDungeonSpaceType() {
        return DungeonSpaceType.WALL;
    }

}
