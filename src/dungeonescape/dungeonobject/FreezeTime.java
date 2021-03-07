/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject;

/**
 *
 * @author Andrew
 */
public class FreezeTime {

    private final long turns;

    public FreezeTime(long turns) {
        this.turns = turns;
    }

    public long getTurns() {
        return turns;
    }

    @Override
    public String toString() {
        return "FreezeTime{" + "turns=" + turns + '}';
    }

}
