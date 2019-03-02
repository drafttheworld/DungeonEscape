/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.characters;

import dungeonescape.dungeon.notifications.ActionNotAllowedNotification;
import dungeonescape.dungeon.notifications.GameNotification;
import dungeonescape.dungeon.notifications.LossNotification;
import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.dungeonobject.construction.Construction;
import dungeonescape.play.Direction;
import dungeonescape.space.DungeonSpace;
import dungeonescape.space.DungeonSpaceType;

/**
 *
 * @author Andrew
 */
public class DungeonMaster extends DungeonCharacter {

    /**
     * The dungeon master does not interact with any other object in the dungeon
     * other than the player. If the player is caught by the dungeon master the
     * game is over.
     *
     * @param dungeonObject
     * @throws GameNotification
     */
    @Override
    public void interact(DungeonObject dungeonObject) throws GameNotification {
        if (dungeonObject instanceof Construction) {
            throw new ActionNotAllowedNotification("A dungeon master cannot move through obstacles.");
        } else if (dungeonObject instanceof DungeonMaster) {
            throw new ActionNotAllowedNotification("A dungeon master cannot occupy the same space as a guard.");
        } else if (dungeonObject instanceof Player) {
            throw new LossNotification("A dungeon master has executed you! Game over.");
        }
    }

    /**
     * Moves 3 spaces per turn. Can only occupy empty dungeon spaces. Movement
     * direction is random unless the player is within line of sight. When the
     * player is in the dungeon master's line of sight movement direction will
     * be toward the player.
     *
     * @param direction
     * @param dungeon
     * @throws GameNotification
     */
    @Override
    public void move(Direction direction, DungeonSpace[][] dungeon) throws GameNotification {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public DungeonSpaceType getDungeonSpaceType() {
        return DungeonSpaceType.DUNGEON_MASTER;
    }

}
