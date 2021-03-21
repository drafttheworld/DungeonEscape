/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.powerups;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Andrew
 */
public class PowerFactoryTest {

    public static void main(String[] args) {

        Map<Class<?>, Integer> powerUps = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            PowerUp powerUp = PowerUpFactory.generatePowerUp();
            Integer count = powerUps.computeIfAbsent(powerUp.getClass(), k -> (Integer) 1);
            if (count != null) {
                powerUps.put(powerUp.getClass(), count + 1);
            }
        }

        for (Entry<Class<?>, Integer> entry : powerUps.entrySet()) {
            System.out.println("Class: " + entry.getKey() + ", Count: " + entry.getValue());
        }

    }

}
