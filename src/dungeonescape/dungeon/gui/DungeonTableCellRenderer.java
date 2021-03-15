/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon.gui;

import dungeonescape.dungeon.gui.images.ImageFactory;
import dungeonescape.dungeon.gui.images.Images;
import dungeonescape.dungeonobject.DungeonObjectTrack;
import dungeonescape.play.Direction;
import dungeonescape.space.DungeonSpaceType;
import java.util.Objects;
import javax.swing.ImageIcon;
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
        } else if (!Objects.equals(DungeonObjectTrack.class, value.getClass())) {
            throw new IllegalArgumentException("setValue expected value of type: " + DungeonObjectTrack.class
                + ", but found value of type: " + value.getClass());
        }

        DungeonObjectTrack dungeonObjectTrack = (DungeonObjectTrack) value;
        DungeonSpaceType dungeonSpaceType
            = DungeonSpaceType.getDungeonSpaceType(dungeonObjectTrack.getDungeonSpaceSymbol());

        if (dungeonSpaceType == null) {
            System.out.println("Unknown dungeon space type character: " + ((String) value));
            return;
        }

        setIcon(ImageFactory.getImageIcon(dungeonSpaceType, dungeonObjectTrack.getPreviousFacingDirection(),
            dungeonObjectTrack.getCurrentFacingDirection()));
    }
}
