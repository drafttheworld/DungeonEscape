/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon.gui;

import dungeonescape.dungeon.gui.images.Images;
import dungeonescape.space.DungeonSpaceType;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Andrew
 */
public class DungeonTableCellRenderer extends DefaultTableCellRenderer {
    
    @Override
    public void setValue(Object value) {

        if (value == null) {
            System.out.println("setValue value is null.");
            return;
        }

        DungeonSpaceType dungeonSpaceType = DungeonSpaceType.getDungeonSpaceType((String) value);

        if (dungeonSpaceType == null) {
            System.out.println("Unknown dungeon space type character: " + ((String) value));
            return;
        }

        switch (dungeonSpaceType) {
            case COIN:
                setIcon(Images.COIN.getImageIcon());
                break;
            case DUNGEON_MASTER:
                setIcon(Images.DUNGEON_MASTER.getImageIcon());
                break;
            case FREEZE_MINE:
                setIcon(Images.FREEZE_MINE.getImageIcon());
                break;
            case GHOST:
                setIcon(Images.GHOST.getImageIcon());
                break;
            case GUARD:
                setIcon(Images.GUARD.getImageIcon());
                break;
            case MYSTERY_BOX:
                setIcon(Images.MYSTERY_BOX.getImageIcon());
                break;
            case NON_VISIBLE_SPACE:
                setIcon(Images.NON_VISIBLE_SPACE.getImageIcon());
                break;
            case OPEN_SPACE:
                setIcon(Images.OPEN_SPACE.getImageIcon());
                break;
            case PLAYER:
                setIcon(Images.PLAYER.getImageIcon());
                break;
            case TELEPORT_MINE:
                setIcon(Images.TELEPORT_MINE.getImageIcon());
                break;
            case WALL:
                setIcon(Images.STONE_WALL.getImageIcon());
                break;

            default:
                throw new IllegalArgumentException("Unrecognized dungeon space type: "
                    + dungeonSpaceType.getValue());
        }
    }
}
