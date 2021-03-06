/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.characters;

/**
 *
 * @author Andrew
 */
public abstract class NonPlayerCharacter extends DungeonCharacter implements Runnable {

    private final Player player;

    protected NonPlayerCharacter(Player player) {
        this.player = player;
    }

    /**
     * Time in ms between movements of this character.
     */
    private long movementRateMs;

    public long getMovementRateMs() {
        return movementRateMs;
    }

    public void setMovementRateMs(long movementRateMs) {
        this.movementRateMs = movementRateMs;
    }

    public Player getPlayer() {
        return player;
    }

}
