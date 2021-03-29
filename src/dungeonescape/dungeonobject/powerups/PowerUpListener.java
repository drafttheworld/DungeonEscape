/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.powerups;

/**
 *
 * @author Andrew
 */
public interface PowerUpListener {

    public void notifyPowerUpAdded(PowerUpEnum powerUpEnum);

    public void notifyPowerUpUsed(PowerUpEnum powerUpEnum);

}
