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

    private static final String IMAGES_DIRECTORY = "images/";
    private static ImageIcon[] images;

    static {
        try {
            images = new ImageIcon[]{
                new ImageIcon(getImage(IMAGES_DIRECTORY + "wall.png")),
                new ImageIcon(getImage(IMAGES_DIRECTORY + "open_space.png")),
                new ImageIcon(getImage(IMAGES_DIRECTORY + "nonvisible_space.png")),
                new ImageIcon(getImage(IMAGES_DIRECTORY + "freeze_mine.png")),
                new ImageIcon(getImage(IMAGES_DIRECTORY + "teleport_mine.png")),
                new ImageIcon(getImage(IMAGES_DIRECTORY + "ghost.png")),
                new ImageIcon(getImage(IMAGES_DIRECTORY + "guard.png")),
                new ImageIcon(getImage(IMAGES_DIRECTORY + "player.png")),
                new ImageIcon(getImage(IMAGES_DIRECTORY + "dungeon_master.png"))};
        } catch (IOException ex) {
            Logger.getLogger(DungeonTableCellRenderer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void setValue(Object value
    ) {

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
            case WALL:
                setIcon(images[0]);
                break;
            case OPEN_SPACE:
                setIcon(images[1]);
                break;
            case NON_VISIBLE_SPACE:
                setIcon(images[2]);
                break;
            case FREEZE_MINE:
                setIcon(images[3]);
                break;
            case TELEPORT_MINE:
                setIcon(images[4]);
                break;
            case GHOST:
                setIcon(images[5]);
                break;
            case GUARD:
                setIcon(images[6]);
                break;
            case PLAYER:
                setIcon(images[7]);
                break;
            case DUNGEON_MASTER:
                setIcon(images[8]);
                break;
            default:
                setText("?");
                System.out.println("Unrecognized dungeon space type: " + dungeonSpaceType.getValue());
        }

    }

    private static Image getImage(String path) throws IOException {
        return getScaledImage(ImageIO.read(DungeonTableCellRenderer.class.getResource(path)),
                DungeonTable.CELL_SIZE, DungeonTable.CELL_SIZE);
    }

    private static Image getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }

}
