/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.powerups;

import dungeonescape.dungeonobject.DungeonObject;

/**
 * Enemies cannot detect the player. Enemies may still land on the player's space (due to random movement) but does not
 * affect the player.
 *
 * @author Andrew
 */
public class Invisibility implements PowerUp<Invisibility> {

    @Override
    public void applyPowerUp(DungeonObject dungeonObject) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Invisibility newInstance() {
        return new Invisibility();
    }

}
