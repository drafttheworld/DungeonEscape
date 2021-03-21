/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon.gui.images;

import dungeonescape.play.Direction;
import dungeonescape.dungeon.space.DungeonSpaceType;
import javax.swing.ImageIcon;

/**
 *
 * @author Andrew
 */
public class ImageFactory {

    public static ImageIcon getImageIcon(DungeonSpaceType dungeonSpaceType, Direction currentFacingDirection) {

        switch (dungeonSpaceType) {
            case COIN:
                return Images.COIN.getImageIcon();
            case DUNGEON_MASTER:
                return getDungeonMasterImageIcon(currentFacingDirection);
            case FREEZE_MINE:
                return Images.FREEZE_MINE.getImageIcon();
            case GHOST:
                return getGhostImageIcon(currentFacingDirection);
            case GUARD:
                return getGuardImageIcon(currentFacingDirection);
            case POWER_UP_BOX:
                return Images.MYSTERY_BOX.getImageIcon();
            case NON_VISIBLE_SPACE:
                return Images.NON_VISIBLE_SPACE.getImageIcon();
            case TELEPORT_LOCATION:
            case OPEN_SPACE:
                return Images.OPEN_SPACE.getImageIcon();
            case PLAYER:
                return getPlayerImageIcon(currentFacingDirection);
            case TELEPORT_MINE:
                return Images.TELEPORT_MINE.getImageIcon();
            case WALL:
                return Images.STONE_WALL.getImageIcon();
            default:
                throw new IllegalArgumentException("Unrecognized dungeon space type: "
                    + dungeonSpaceType);
        }
    }

    private static ImageIcon getDungeonMasterImageIcon(Direction currentFacingDirection) {

        switch (currentFacingDirection) {
            case EAST:
                return Images.DUNGEON_MASTER_FACING_LEFT.getImageIcon();
            default:
                return Images.DUNGEON_MASTER_FACING_RIGHT.getImageIcon();
        }
    }

    private static ImageIcon getGhostImageIcon(Direction currentFacingDirection) {

        switch (currentFacingDirection) {
            case EAST:
                return Images.GHOST_FACING_LEFT.getImageIcon();
            default:
                return Images.GHOST_FACING_RIGHT.getImageIcon();
        }
    }

    private static ImageIcon getGuardImageIcon(Direction currentFacingDirection) {

        switch (currentFacingDirection) {
            case EAST:
                return Images.GUARD_FACING_LEFT.getImageIcon();
            default:
                return Images.GUARD_FACING_RIGHT.getImageIcon();
        }
    }

    private static ImageIcon getPlayerImageIcon(Direction currentFacingDirection) {

        switch (currentFacingDirection) {
            case EAST:
                return Images.PLAYER_FACING_LEFT.getImageIcon();
            default:
                return Images.PLAYER_FACING_RIGHT.getImageIcon();
        }
    }
}
