/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon.gui.images;

import dungeonescape.dungeon.gui.DungeonTable;
import dungeonescape.exceptions.ImageLoadFailureException;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author Andrew
 */
class LoadedImage {

    private BufferedImage bufferedImage;
    private ImageIcon imageIcon;
    private URL url;

    protected LoadedImage(String path) {

        url = LoadedImage.class.getResource(path);

        if (url == null) {
            throw new ImageLoadFailureException("Unable to find image at path: " + path);
        }

        try {
            bufferedImage = ImageIO.read(url);
            imageIcon = new ImageIcon(getImage(url));
        } catch (IOException e) {
            throw new ImageLoadFailureException("Unable to load image with path: " + path, e);
        }
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public ImageIcon getImageIcon() {
        return imageIcon;
    }

    public URL getUrl() {
        return url;
    }

    private static Image getImage(URL url) throws IOException {
        return getScaledImage(ImageIO.read(url),
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
