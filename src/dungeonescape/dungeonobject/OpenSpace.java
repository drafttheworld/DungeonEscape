/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject;

import dungeonescape.dungeon.space.DungeonSpace;
import dungeonescape.dungeon.space.DungeonSpaceType;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Andrew
 */
public class OpenSpace extends DungeonObject {

    @Override
    public List<DungeonSpace> interact(DungeonObject dungeonObject) {
        return Collections.emptyList();
    }

    @Override
    public DungeonSpaceType getDungeonSpaceType() {
        return DungeonSpaceType.OPEN_SPACE;
    }

}
