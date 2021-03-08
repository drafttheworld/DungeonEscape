/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon.gui.images;

import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 *
 * @author Andrew
 */
public enum Images {

    COIN("items/coin.png"),
    DUNGEON_MASTER("characters/dungeon_master.png"),
    FREEZE_MINE("mines/freeze_mine.png"),
    GAME_LOST("notifications/game_lost.png"),
    GAME_WON("notifications/game_won.png"),
    GHOST("characters/ghost.png"),
    GUARD("characters/guard.png"),
    MYSTERY_BOX("items/mystery_box.png"),
    NON_VISIBLE_SPACE("construction/nonvisible_space.png"),
    OPEN_SPACE("construction/open_space.png"),
    PLAYER("characters/player.png"),
    START_SCREEN_BACKGROUND("hero_vs_dungeon_master.png"),
    STONE_WALL("construction/stone_wall.png"),
    TELEPORT_MINE("mines/teleport_mine.png");

    LoadedImage loadedImage;

    private Images(String path) {
        loadedImage = new LoadedImage(path);
    }

    public URL getUrl() {
        return loadedImage.getUrl();
    }
    
    public ImageIcon getImageIcon() {
        return loadedImage.getImageIcon();
    }
    
    public BufferedImage getBufferedImage() {
        return loadedImage.getBufferedImage();
    }
}
