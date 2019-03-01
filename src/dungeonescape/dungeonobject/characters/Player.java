/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.characters;

import dungeonescape.dungeon.notifications.GameNotification;
import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.dungeonobject.actions.move.Direction;
import dungeonescape.space.DungeonSpace;
import dungeonescape.space.DungeonSpaceType;

/**
 *
 * @author Andrew
 */
public class Player extends DungeonCharacter {
    
    private final String playerName;
    private final int playerVisibility;
    
    public Player(String playerName, int playerVisibility) {
        this.playerName = playerName;
        this.playerVisibility = playerVisibility;
    }
    
    public String getPlayerName() {
        return playerName;
    }

    public int getPlayerVisibility() {
        return playerVisibility;
    }
    
    public void move(Direction direction) {
        
    }

    @Override
    public void interact(DungeonObject dungeonObject) throws GameNotification {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DungeonSpaceType getDungeonSpaceType() {
        return DungeonSpaceType.PLAYER;
    }

    @Override
    public void move(Direction direction, DungeonSpace[][] dungeon) throws GameNotification {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
