/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon.gui;

import dungeonescape.dungeon.gui.images.ImageFactory;
import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.dungeonobject.characters.DungeonCharacter;
import dungeonescape.play.Direction;
import dungeonescape.dungeon.space.DungeonSpace;
import dungeonescape.dungeon.space.DungeonSpaceType;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Andrew
 */
public class DungeonTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public void setValue(Object value) {

        if (value == null) {
            throw new IllegalArgumentException("setValue value is null.");
        } else if (!(value instanceof DungeonSpace)) {
            throw new IllegalArgumentException("setValue expected value of type: " + DungeonSpace.class
                + ", but found value of type: " + value.getClass() + " [" + value + "]");
        }

        DungeonSpace dungeonSpace = (DungeonSpace) value;
        DungeonObject dungeonObject = dungeonSpace.getVisibleDungeonObject();
        DungeonSpaceType dungeonSpaceType = dungeonObject.getDungeonSpaceType();
        Direction previousFacingDirection = null;
        Direction currentFacingDirection = null;
        if (dungeonObject instanceof DungeonCharacter) {
            DungeonCharacter dungeonCharacter = (DungeonCharacter) dungeonObject;
            previousFacingDirection = dungeonCharacter.getPreviousFacingDirection();
            currentFacingDirection = dungeonCharacter.getCurrentFacingDirection();
        }

        setIcon(ImageFactory.getImageIcon(dungeonSpaceType, previousFacingDirection, currentFacingDirection));
    }
}
