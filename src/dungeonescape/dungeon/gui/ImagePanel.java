/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author Andrew
 */
public class ImagePanel extends JPanel {

    private Image img;

    public ImagePanel(String img) {
        this(new ImageIcon(img).getImage());
    }

    public ImagePanel(Image img) {
        this.img = img;
        init();
    }

    private void init() {
        Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
        super.setPreferredSize(size);
        super.setMinimumSize(size);
        super.setMaximumSize(size);
        super.setSize(size);
        super.setLayout(null);
        super.setBackground(Color.white);
    }

    @Override
    public void paintComponent(Graphics g) {
//        int x = getWidth() / 2 - img.getWidth(this) / 2;
//        int y = getHeight() / 2 - img.getHeight(this) / 2;
        g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
    }

}
