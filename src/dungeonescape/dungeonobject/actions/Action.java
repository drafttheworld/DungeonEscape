/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.actions;

import dungeonescape.dungeonobject.DungeonObject;

/**
 *
 * @author Andrew
 */
public interface Action {
    
    public DungeonObject actOn(DungeonObject dungeonObject);
    
}
