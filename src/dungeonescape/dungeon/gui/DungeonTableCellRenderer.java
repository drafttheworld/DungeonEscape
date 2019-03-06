/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon.gui;

import dungeonescape.space.DungeonSpaceType;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Andrew
 */
public class DungeonTableCellRenderer extends DefaultTableCellRenderer {

    private static final String IMAGES_DIRECTORY = "images/";//"src/dungeonescape/dungeon/gui/images/"
    private static ImageIcon[] images;
    
    static {
        try {
            images = new ImageIcon[]{
                new ImageIcon(getScaledImage(ImageIO.read(DungeonTableCellRenderer.class.getResource(IMAGES_DIRECTORY + "wall.png")), DungeonEscapeGUI.CELL_SIZE, DungeonEscapeGUI.CELL_SIZE)),
                new ImageIcon(getScaledImage(ImageIO.read(DungeonTableCellRenderer.class.getResource(IMAGES_DIRECTORY + "open_space.png")), DungeonEscapeGUI.CELL_SIZE, DungeonEscapeGUI.CELL_SIZE)),
                new ImageIcon(getScaledImage(ImageIO.read(DungeonTableCellRenderer.class.getResource(IMAGES_DIRECTORY + "nonvisible_space.png")), DungeonEscapeGUI.CELL_SIZE, DungeonEscapeGUI.CELL_SIZE)),
                new ImageIcon(getScaledImage(ImageIO.read(DungeonTableCellRenderer.class.getResource(IMAGES_DIRECTORY + "freeze_mine.png")), DungeonEscapeGUI.CELL_SIZE, DungeonEscapeGUI.CELL_SIZE)),
                new ImageIcon(getScaledImage(ImageIO.read(DungeonTableCellRenderer.class.getResource(IMAGES_DIRECTORY + "teleport_mine.png")), DungeonEscapeGUI.CELL_SIZE, DungeonEscapeGUI.CELL_SIZE)),
                new ImageIcon(getScaledImage(ImageIO.read(DungeonTableCellRenderer.class.getResource(IMAGES_DIRECTORY + "ghost.png")), DungeonEscapeGUI.CELL_SIZE, DungeonEscapeGUI.CELL_SIZE)),
                new ImageIcon(getScaledImage(ImageIO.read(DungeonTableCellRenderer.class.getResource(IMAGES_DIRECTORY + "guard.png")), DungeonEscapeGUI.CELL_SIZE, DungeonEscapeGUI.CELL_SIZE)),
                new ImageIcon(getScaledImage(ImageIO.read(DungeonTableCellRenderer.class.getResource(IMAGES_DIRECTORY + "player.png")), DungeonEscapeGUI.CELL_SIZE, DungeonEscapeGUI.CELL_SIZE)),
                new ImageIcon(getScaledImage(ImageIO.read(DungeonTableCellRenderer.class.getResource(IMAGES_DIRECTORY + "dungeon_master.png")), DungeonEscapeGUI.CELL_SIZE, DungeonEscapeGUI.CELL_SIZE))
            };  } catch (IOException ex) {
                images = null;
            Logger.getLogger(DungeonTableCellRenderer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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

        String imagePath;
        switch (dungeonSpaceType) {
            case WALL:
                setIcon(images[0]);
//                imagePath = IMAGES_DIRECTORY + "wall.png";
                break;
            case OPEN_SPACE:
                setIcon(images[1]);
//                imagePath = IMAGES_DIRECTORY + "open_space.png";
                break;
            case NON_VISIBLE_SPACE:
                setIcon(images[2]);
//                imagePath = IMAGES_DIRECTORY + "nonvisibile_space.png";
                break;
            case FREEZE_MINE:
                setIcon(images[3]);
//                imagePath = IMAGES_DIRECTORY + "freeze_mine.png";
                break;
            case TELEPORT_MINE:
                setIcon(images[4]);
//                imagePath = IMAGES_DIRECTORY + "teleport_mine.png";
                break;
            case GHOST:
                setIcon(images[5]);
//                imagePath = IMAGES_DIRECTORY + "ghost.png";
                break;
            case GUARD:
                setIcon(images[6]);
//                imagePath = IMAGES_DIRECTORY + "guard.png";
                break;
            case PLAYER:
                setIcon(images[7]);
//                imagePath = IMAGES_DIRECTORY + "player.png";
                break;
            case DUNGEON_MASTER:
                setIcon(images[8]);
//                imagePath = IMAGES_DIRECTORY + "dungeon_master.png";
                break;
            default:
                setText("?");
                System.out.println("Unrecognized dungeon space type: " + dungeonSpaceType.getValue());
                return;
        }

//        try {
//            BufferedImage image = ImageIO.read(getClass().getResource(imagePath));
//            setIcon(new ImageIcon(getScaledImage(image, 25, 25)));
//        } catch (IOException ex) {
//            setText("?");
//        }
        
    }
    
    private static Image getScaledImage(Image srcImg, int w, int h){
    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = resizedImg.createGraphics();

    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g2.drawImage(srcImg, 0, 0, w, h, null);
    g2.dispose();

    return resizedImg;
}

}
