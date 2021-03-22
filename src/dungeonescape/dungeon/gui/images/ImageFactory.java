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
            case POWER_UP_BOX:
                return Images.MYSTERY_BOX.getImageIcon();
            case NON_VISIBLE_SPACE:
                return Images.NON_VISIBLE_SPACE.getImageIcon();
            case TELEPORT_LOCATION:
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
                    + dungeonSpaceType);
        }
    }

    private static ImageIcon getDungeonMasterImageIcon(Direction previousFacingDirection, 
        Direction currentFacingDirection) {
        
        if (currentFacingDirection == null) {
            return Images.DUNGEON_MASTER_FACING_RIGHT.getImageIcon();
        }

        switch (currentFacingDirection) {
            case EAST:
            case WEST:
                return getDungeonMasterImageIcon(currentFacingDirection);
            default:
                return getDungeonMasterImageIcon(previousFacingDirection);
        }
    }
    
    private static ImageIcon getDungeonMasterImageIcon(Direction direction) {
        
        if (direction == null) {
            return Images.DUNGEON_MASTER_FACING_RIGHT.getImageIcon();
        }
        
        switch (direction) {
            case EAST:
                return Images.DUNGEON_MASTER_FACING_RIGHT.getImageIcon();
            case WEST:
                return Images.DUNGEON_MASTER_FACING_LEFT.getImageIcon();
            default:
                throw new IllegalArgumentException("Unsupported dungeon master direction: " + direction);
        }
    }

    private static ImageIcon getGhostImageIcon(Direction previousFacingDirection, Direction currentFacingDirection) {
        
        if (currentFacingDirection == null) {
            return Images.GHOST_FACING_RIGHT.getImageIcon();
        }

        switch (currentFacingDirection) {
            case EAST:
            case WEST:
                return getGhostImageIcon(currentFacingDirection);
            default:
                return getGhostImageIcon(previousFacingDirection);
        }
    }
    
    private static ImageIcon getGhostImageIcon(Direction direction) {
        
        if (direction == null) {
            return Images.GHOST_FACING_RIGHT.getImageIcon();
        }
        
        switch (direction) {
            case EAST:
                return Images.GHOST_FACING_RIGHT.getImageIcon();
            case WEST:
                return Images.GHOST_FACING_LEFT.getImageIcon();
            default:
                throw new IllegalArgumentException("Unsupported ghost direction: " + direction);
        }
    }

    private static ImageIcon getGuardImageIcon(Direction previousFacingDirection, Direction currentFacingDirection) {
        
        if (currentFacingDirection == null) {
            return Images.GUARD_FACING_RIGHT.getImageIcon();
        }

        switch (currentFacingDirection) {
            case EAST:
            case WEST:
                return getGuardImageIcon(currentFacingDirection);
            default:
                return getGuardImageIcon(previousFacingDirection);
        }
    }
    
    private static ImageIcon getGuardImageIcon(Direction direction) {
        
        if (direction == null) {
            return Images.GUARD_FACING_RIGHT.getImageIcon();
        }
        
        switch (direction) {
            case EAST:
                return Images.GUARD_FACING_RIGHT.getImageIcon();
            case WEST:
                return Images.GUARD_FACING_LEFT.getImageIcon();
            default:
                throw new IllegalArgumentException("Unsupported guard direction: " + direction);
        }
    }

    private static ImageIcon getPlayerImageIcon(Direction previousFacingDirection, Direction currentFacingDirection) {
        
        if (currentFacingDirection == null) {
            return Images.PLAYER_FACING_RIGHT.getImageIcon();
        }

        switch (currentFacingDirection) {
            case EAST:
            case WEST:
                return getPlayerImageIcon(currentFacingDirection);
            default:
                return getPlayerImageIcon(previousFacingDirection);
        }
    }
    
    private static ImageIcon getPlayerImageIcon(Direction direction) {
        
        if (direction == null) {
            return Images.PLAYER_FACING_RIGHT.getImageIcon();
        }
        
        switch (direction) {
            case EAST:
                return Images.PLAYER_FACING_RIGHT.getImageIcon();
            case WEST:
                return Images.PLAYER_FACING_LEFT.getImageIcon();
            default:
                throw new IllegalArgumentException("Unsupported player direction: " + direction);
        }
    }
}
