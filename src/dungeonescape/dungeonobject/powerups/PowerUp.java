/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.powerups;

import dungeonescape.dungeonobject.DungeonObject;

/**
 *
 * @author Andrew
 */
public interface PowerUp<T> {

    void applyPowerUp(DungeonObject dungeonObject);

    T newInstance();

}
