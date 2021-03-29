/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.powerups;

import dungeonescape.dungeonobject.DungeonObject;

/**
 * Enemies attack but are then transported to a random location on the map without affecting the player.
 *
 * @author Andrew
 */
public class Invincibility implements PowerUp<Invincibility> {

    @Override
    public void applyPowerUp(DungeonObject dungeonObject) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Invincibility newInstance() {
        return new Invincibility();
    }

    @Override
    public PowerUpEnum getCorrespondingPowerUpEnum() {
        return PowerUpEnum.INVINCIBILITY;
    }

}
