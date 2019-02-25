/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.play;

import dungeonescape.dungeon.Dungeon;
import dungeonescape.dungeon.DungeonConfiguration;
import dungeonescape.dungeonobject.actions.move.Direction;
import java.util.UUID;

/**
 *
 * @author Andrew
 */
public class GameSession {
    
    private final String sessionId = UUID.randomUUID().toString();
    
    private final String playerName;
    private final DungeonConfiguration dugeonConfiguration;
    
    private final Dungeon dungeon;
    
    public GameSession(String playerName, DungeonConfiguration dugeonConfiguration) {
        this.playerName = playerName;
        this.dugeonConfiguration = dugeonConfiguration;
        
        dungeon = new Dungeon(this);
    }

    public String getPlayerName() {
        return playerName;
    }

    public DungeonConfiguration getDugeonConfiguration() {
        return dugeonConfiguration;
    }
    
    public String getSessionId() {
        return sessionId;
    }
    
    public GameState movePlayer(Direction direction) {
        return dungeon.movePlayer(direction);
    }
    
}
