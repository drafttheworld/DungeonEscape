/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.mine;

import dungeonescape.space.Position;
import dungeonescape.play.GameSession;
import dungeonescape.space.DungeonSpace;
import dungeonescape.space.DungeonSpaceType;

/**
 *
 * @author Andrew
 */
public class TeleportMine extends Mine {
    
    private DungeonSpace teleportSpace;
    
    public TeleportMine(DungeonSpace teleportSpace) {
        this.teleportSpace = teleportSpace;
    }
    
    public void setTeleportPosition(DungeonSpace teleportSpace) {
        this.teleportSpace = teleportSpace;
    }
    
    public TeleportMine teleportPosition(DungeonSpace teleportSpace) {
        setTeleportPosition(teleportSpace);
        return this;
    }
    
    public DungeonSpace getTeleportPosition() {
        return teleportSpace;
    }

    @Override
    public void act(GameSession session) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DungeonSpaceType getDungeonSpaceType() {
        return DungeonSpaceType.TELEPORT_MINE;
    }
    
}
