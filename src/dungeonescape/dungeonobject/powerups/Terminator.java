/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.powerups;

import dungeonescape.dungeonobject.DungeonObject;

/**
 * Enemies (except for the dungeon master) are killed (do not respawn) when attacking the player. The dungeon master
 * will be transported to the center of the map if it attacks the player.
 *
 * @author Andrew
 */
public class Terminator implements PowerUp<Terminator> {

    @Override
    public void applyPowerUp(DungeonObject dungeonObject) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Terminator newInstance() {
        return new Terminator();
    }

    @Override
    public PowerUpEnum getCorrespondingPowerUpEnum() {
        return PowerUpEnum.TERMINATOR;
    }

}
