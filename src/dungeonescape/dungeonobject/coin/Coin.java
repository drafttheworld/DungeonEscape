/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.coin;

import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.dungeonobject.characters.Player;
import dungeonescape.dungeon.space.DungeonSpace;
import dungeonescape.dungeon.space.DungeonSpaceType;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Andrew
 */
public class Coin extends DungeonObject {

    @Override
    public List<DungeonSpace> interact(DungeonObject dungeonObject) {

        if (dungeonObject instanceof Player) {
            ((Player) dungeonObject).addCoinsCollected(1);
            DungeonSpace dungeonSpace = getDungeonSpace();
            dungeonSpace.removeDungeonObject(this);
            return Collections.singletonList(dungeonSpace);
        }

        return Collections.emptyList();
    }

    @Override
    public DungeonSpaceType getDungeonSpaceType() {
        return DungeonSpaceType.COIN;
    }

}
