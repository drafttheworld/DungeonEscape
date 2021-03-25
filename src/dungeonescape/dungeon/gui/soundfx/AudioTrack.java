/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon.gui.soundfx;

/**
 *
 * @author Andrew
 */
public enum AudioTrack {

    BACKGROUND_EPIC("tracks/background/POL-random-encounter-short.wav");

    private final String path;

    private AudioTrack(String path) {
        this.path = AudioTrack.class.getResource(path).getFile();
    }

    public String getPath() {
        return path;
    }

}
