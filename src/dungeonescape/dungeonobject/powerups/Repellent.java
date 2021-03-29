/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.powerups;

import dungeonescape.dungeonobject.DungeonObject;

/**
 * Enemies within 10 spaces move away from the player. Enemies outside of 10 spaces cannot move within 10 spaces of the
 * player.
 *
 * @author Andrew
 */
public class Repellent implements PowerUp<Repellent> {

    @Override
    public void applyPowerUp(DungeonObject dungeonObject) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Repellent newInstance() {
        return new Repellent();
    }

    @Override
    public PowerUpEnum getCorrespondingPowerUpEnum() {
        return PowerUpEnum.REPELLENT;
    }

}
