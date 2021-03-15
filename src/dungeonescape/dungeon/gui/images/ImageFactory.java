/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon.gui.images;

import dungeonescape.play.Direction;
import dungeonescape.space.DungeonSpaceType;
import javax.swing.ImageIcon;

/**
 *
 * @author Andrew
 */
public class ImageFactory {

    private static final ImageIcon defaultDungeonMasterImageIcon = Images.DUNGEON_MASTER_FACING_RIGHT.getImageIcon();
    private static final ImageIcon defaultGhostImageIcon = Images.GHOST_FACING_RIGHT.getImageIcon();
    private static final ImageIcon defaultGuardImageIcon = Images.GUARD_FACING_RIGHT.getImageIcon();
    private static final ImageIcon defaultPlayerImageIcon = Images.PLAYER_FACING_RIGHT.getImageIcon();

    public static ImageIcon getImageIcon(DungeonSpaceType dungeonSpaceType,
        Direction previousFacingDirection, Direction currentFacingDirection) {

        switch (dungeonSpaceType) {
            case COIN:
                return Images.COIN.getImageIcon();
            case DUNGEON_MASTER:
                return getDungeonMasterImageIcon(previousFacingDirection, currentFacingDirection);
            case FREEZE_MINE:
                return Images.FREEZE_MINE.getImageIcon();
            case GHOST:
                return getGhostImageIcon(previousFacingDirection, currentFacingDirection);
            case GUARD:
                return getGuardImageIcon(previousFacingDirection, currentFacingDirection);
            case MYSTERY_BOX:
                return Images.MYSTERY_BOX.getImageIcon();
            case NON_VISIBLE_SPACE:
                return Images.NON_VISIBLE_SPACE.getImageIcon();
            case OPEN_SPACE:
                return Images.OPEN_SPACE.getImageIcon();
            case PLAYER:
                return getPlayerImageIcon(previousFacingDirection, currentFacingDirection);
            case TELEPORT_MINE:
                return Images.TELEPORT_MINE.getImageIcon();
            case WALL:
                return Images.STONE_WALL.getImageIcon();
            default:
                throw new IllegalArgumentException("Unrecognized dungeon space type: "
                    + dungeonSpaceType.getValue());
        }
    }

    private static ImageIcon getDungeonMasterImageIcon(Direction previousFacingDirection, Direction currentFacingDirection) {

        switch (currentFacingDirection) {
            case EAST:
                return Images.DUNGEON_MASTER_FACING_LEFT.getImageIcon();
            case WEST:
                return Images.DUNGEON_MASTER_FACING_RIGHT.getImageIcon();
            default:
                switch (previousFacingDirection) {
                    case EAST:
                        return Images.DUNGEON_MASTER_FACING_LEFT.getImageIcon();
                    case WEST:
                        return Images.DUNGEON_MASTER_FACING_RIGHT.getImageIcon();
                    default:
                        return defaultDungeonMasterImageIcon;
                }
        }
    }

    private static ImageIcon getGhostImageIcon(Direction previousFacingDirection, Direction currentFacingDirection) {

        switch (currentFacingDirection) {
            case EAST:
                return Images.GHOST_FACING_LEFT.getImageIcon();
            case WEST:
                return Images.GHOST_FACING_RIGHT.getImageIcon();
            default:
                switch (previousFacingDirection) {
                    case EAST:
                        return Images.GHOST_FACING_LEFT.getImageIcon();
                    case WEST:
                        return Images.GHOST_FACING_RIGHT.getImageIcon();
                    default:
                        return defaultGhostImageIcon;
                }
        }
    }

    private static ImageIcon getGuardImageIcon(Direction previousFacingDirection, Direction currentFacingDirection) {

        switch (currentFacingDirection) {
            case EAST:
                return Images.GUARD_FACING_LEFT.getImageIcon();
            case WEST:
                return Images.GUARD_FACING_RIGHT.getImageIcon();
            default:
                switch (previousFacingDirection) {
                    case EAST:
                        return Images.GUARD_FACING_LEFT.getImageIcon();
                    case WEST:
                        return Images.GUARD_FACING_RIGHT.getImageIcon();
                    default:
                        return defaultGuardImageIcon;
                }
        }
    }

    private static ImageIcon getPlayerImageIcon(Direction previousFacingDirection, Direction currentFacingDirection) {

        switch (currentFacingDirection) {
            case EAST:
                return Images.PLAYER_FACING_LEFT.getImageIcon();
            case WEST:
                return Images.PLAYER_FACING_RIGHT.getImageIcon();
            default:
                switch (previousFacingDirection) {
                    case EAST:
                        return Images.PLAYER_FACING_LEFT.getImageIcon();
                    case WEST:
                        return Images.PLAYER_FACING_RIGHT.getImageIcon();
                    default:
                        return defaultPlayerImageIcon;
                }
        }
    }
}
