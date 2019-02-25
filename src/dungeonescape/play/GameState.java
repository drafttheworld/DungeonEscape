/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.play;

/**
 *
 * @author Andrew
 */
public class GameState {
    
    private final boolean gameLost;
    private final boolean gameWon;
    private final String playerMap;
    
    public GameState(boolean gameLost, boolean gameWon, String playerMap) {
        this.gameLost = gameLost;
        this.gameWon = gameWon;
        this.playerMap = playerMap;
    }

    public boolean isGameLost() {
        return gameLost;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public String getPlayerMap() {
        return playerMap;
    }
    
}
