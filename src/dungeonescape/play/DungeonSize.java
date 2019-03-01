/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.play;

/**
 *
 * @author Andrew
 */
public enum DungeonSize {
    
    SMALL(50),
    MEDIUM(100),
    LARGE(500),
    XLARGE(1000),
    EPIC(5000);
    
    private final int dungeonWidth;
    
    private DungeonSize(int dungeonWidth) {
        this.dungeonWidth = dungeonWidth;
    }
    
    public int getDungeonWidth() {
        return dungeonWidth;
    }
    
}
