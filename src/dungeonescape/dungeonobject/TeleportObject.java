/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject;

import dungeonescape.dungeon.space.DungeonSpace;
import java.util.List;

/**
 *
 * @author Andrew
 */
public interface TeleportObject {

    public List<DungeonSpace> teleport(DungeonObject dungeonObject);

}
