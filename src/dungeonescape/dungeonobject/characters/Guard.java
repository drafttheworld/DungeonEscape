/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.characters;

import dungeonescape.dungeon.notifications.ActionNotAllowedNotification;
import dungeonescape.dungeon.notifications.GameNotification;
import dungeonescape.dungeon.notifications.InteractionNotification;
import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.dungeonobject.construction.Construction;
import dungeonescape.play.Direction;
import dungeonescape.space.DungeonSpace;
import dungeonescape.space.DungeonSpaceType;

/**
 *
 * @author Andrew
 */
public class Guard extends DungeonCharacter {
    
    private final DungeonSpace jailCellSpace;
    
    public Guard(DungeonSpace jailCellSpace) {
        this.jailCellSpace = jailCellSpace;
    }

    @Override
    public void interact(DungeonObject dungeonObject) throws GameNotification {
        if (dungeonObject instanceof Construction) {
            throw new ActionNotAllowedNotification("Guards cannot move through obstacles.");
        } else if (dungeonObject instanceof DungeonMaster) {
            throw new ActionNotAllowedNotification("Guards cannot occupy the same space as a dungeon master.");
        } else if (dungeonObject instanceof Player) {
            Player player = (Player) dungeonObject;
            player.getDungeonSpace().removeDungeonObject(dungeonObject);
            jailCellSpace.addDungeonObject(player);
            throw new InteractionNotification("A guard has caught you and moved you back to your cell.");
        }
    }

    @Override
    public DungeonSpaceType getDungeonSpaceType() {
        return DungeonSpaceType.GUARD;
    }

    @Override
    public void move(Direction direction, DungeonSpace[][] dungeon) throws GameNotification {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean canOccupySpace(DungeonSpace dungeonSpace) {
        return dungeonSpace.getDungeonObjects().stream()
                .noneMatch(dungeonObject -> {
                    return dungeonObject instanceof Construction
                            || (dungeonObject instanceof DungeonCharacter
                            && !(dungeonObject instanceof Ghost));
                });
    }
    
}
