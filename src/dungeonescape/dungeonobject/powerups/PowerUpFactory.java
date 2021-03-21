/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.powerups;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Andrew
 */
public class PowerUpFactory {

    private static final PowerUp[] powerUps
        = {new Invincibility(), new Invisibility(), new Repellent(), new Terminator()};

    private final int probabilitySum;
    private final int[] probabilities;

    public PowerUpFactory(int invincibilityProbability, int invisibilityProbability,
        int repellentProbability, int terminatorProbability) {

        probabilitySum = invincibilityProbability + invisibilityProbability
            + repellentProbability + terminatorProbability;
        probabilities = new int[]{invincibilityProbability, invisibilityProbability,
            repellentProbability, terminatorProbability};
    }

    public PowerUp generatePowerUp() {

        int index = ThreadLocalRandom.current().nextInt(probabilitySum) + 1;
        int sum = 0;
        int i = 0;
        while (sum < index) {
            sum += probabilities[i++];
        }

        return powerUps[i - 1];
    }

}
