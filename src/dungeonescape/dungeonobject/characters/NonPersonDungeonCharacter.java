/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.characters;

import dungeonescape.dungeonobject.DungeonObjectTrack;
import dungeonescape.space.DungeonSpace;

/**
 *
 * @author Andrew
 */
public abstract class NonPersonDungeonCharacter extends DungeonCharacter {

    public abstract DungeonObjectTrack move(DungeonSpace[][] dungeon, Player player);

}
