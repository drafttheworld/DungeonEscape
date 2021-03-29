/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.powerups;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andrew
 */
public class PowerUpService {

    List<PowerUpListener> powerUpListeners = new ArrayList<>();

    public void registerPowerUpClient(PowerUpListener powerUpListener) {
        if (powerUpListener != null) {
            powerUpListeners.add(powerUpListener);
        }
    }

    public void usePowerUp(PowerUpEnum powerUpEnum) {
        powerUpListeners.forEach(powerUpListener -> powerUpListener.notifyPowerUpUsed(powerUpEnum));
    }

    public void addPowerUp(PowerUpEnum powerUpEnum) {
        powerUpListeners.forEach(powerUpListener -> powerUpListener.notifyPowerUpAdded(powerUpEnum));
    }

}
