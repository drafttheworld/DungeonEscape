/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.characters;

import dungeonescape.space.DungeonSpace;
import java.util.List;

/**
 *
 * @author Andrew
 */
public abstract class NonPersonDungeonCharacter extends DungeonCharacter {

    public abstract List<DungeonSpace> move(DungeonSpace[][] dungeon, Player player);

}
