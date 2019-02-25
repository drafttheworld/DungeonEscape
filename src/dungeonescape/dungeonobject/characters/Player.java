/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.characters;

import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.play.GameSession;
import dungeonescape.space.DungeonSpaceType;

/**
 *
 * @author Andrew
 */
public class Player extends DungeonObject {
    
    private final String playerName;
    
    public Player(String playerName) {
        this.playerName = playerName;
    }
    
    public String getPlayerName() {
        return playerName;
    }

    @Override
    public void act(GameSession session) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DungeonSpaceType getDungeonSpaceType() {
        return DungeonSpaceType.PLAYER;
    }
    
}
