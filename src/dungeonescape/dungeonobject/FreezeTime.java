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

    private final int turns;

    public FreezeTime(int turns) {
        this.turns = turns;
    }

    public int getTurns() {
        return turns;
    }

    @Override
    public String toString() {
        return "FreezeTime{" + "turns=" + turns + '}';
    }

}
