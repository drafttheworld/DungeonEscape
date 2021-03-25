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
                return Image.COIN.getImageIcon();
            case DUNGEON_MASTER:
                return getDungeonMasterImageIcon(previousFacingDirection, currentFacingDirection);
            case FREEZE_MINE:
                return Image.FREEZE_MINE.getImageIcon();
            case GHOST:
                return getGhostImageIcon(previousFacingDirection, currentFacingDirection);
            case GUARD:
                return getGuardImageIcon(previousFacingDirection, currentFacingDirection);
            case POWER_UP_BOX:
                return Image.MYSTERY_BOX.getImageIcon();
            case NON_VISIBLE_SPACE:
                return Image.NON_VISIBLE_SPACE.getImageIcon();
            case TELEPORT_LOCATION:
            case OPEN_SPACE:
                return Image.OPEN_SPACE.getImageIcon();
            case PLAYER:
                return getPlayerImageIcon(previousFacingDirection, currentFacingDirection);
            case TELEPORT_MINE:
                return Image.TELEPORT_MINE.getImageIcon();
            case WALL:
                return Image.STONE_WALL.getImageIcon();
            default:
                throw new IllegalArgumentException("Unrecognized dungeon space type: "
                    + dungeonSpaceType);
        }
    }

    private static ImageIcon getDungeonMasterImageIcon(Direction previousFacingDirection, 
        Direction currentFacingDirection) {
        
        if (currentFacingDirection == null) {
            return Image.DUNGEON_MASTER_FACING_RIGHT.getImageIcon();
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
            return Image.DUNGEON_MASTER_FACING_RIGHT.getImageIcon();
        }
        
        switch (direction) {
            case EAST:
                return Image.DUNGEON_MASTER_FACING_RIGHT.getImageIcon();
            case WEST:
                return Image.DUNGEON_MASTER_FACING_LEFT.getImageIcon();
            default:
                throw new IllegalArgumentException("Unsupported dungeon master direction: " + direction);
        }
    }

    private static ImageIcon getGhostImageIcon(Direction previousFacingDirection, Direction currentFacingDirection) {
        
        if (currentFacingDirection == null) {
            return Image.GHOST_FACING_RIGHT.getImageIcon();
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
            return Image.GHOST_FACING_RIGHT.getImageIcon();
        }
        
        switch (direction) {
            case EAST:
                return Image.GHOST_FACING_RIGHT.getImageIcon();
            case WEST:
                return Image.GHOST_FACING_LEFT.getImageIcon();
            default:
                throw new IllegalArgumentException("Unsupported ghost direction: " + direction);
        }
    }

    private static ImageIcon getGuardImageIcon(Direction previousFacingDirection, Direction currentFacingDirection) {
        
        if (currentFacingDirection == null) {
            return Image.GUARD_FACING_RIGHT.getImageIcon();
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
            return Image.GUARD_FACING_RIGHT.getImageIcon();
        }
        
        switch (direction) {
            case EAST:
                return Image.GUARD_FACING_RIGHT.getImageIcon();
            case WEST:
                return Image.GUARD_FACING_LEFT.getImageIcon();
            default:
                throw new IllegalArgumentException("Unsupported guard direction: " + direction);
        }
    }

    private static ImageIcon getPlayerImageIcon(Direction previousFacingDirection, Direction currentFacingDirection) {
        
        if (currentFacingDirection == null) {
            return Image.PLAYER_FACING_RIGHT.getImageIcon();
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
            return Image.PLAYER_FACING_RIGHT.getImageIcon();
        }
        
        switch (direction) {
            case EAST:
                return Image.PLAYER_FACING_RIGHT.getImageIcon();
            case WEST:
                return Image.PLAYER_FACING_LEFT.getImageIcon();
            default:
                throw new IllegalArgumentException("Unsupported player direction: " + direction);
        }
    }
}
