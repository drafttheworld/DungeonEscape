/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.play;

import dungeonescape.dungeon.DungeonConfiguration;

/**
 *
 * @author Andrew
 */
public enum GameDifficulty {
    
    EASY(EASY()),
    NORMAL(NORMAL()),
    HARD(HARD()),
    INSANE(INSANE());
    
    private final DungeonConfiguration dungeonConfiguration;
    
    private GameDifficulty(DungeonConfiguration dungeonConfiguration) {
        this.dungeonConfiguration = dungeonConfiguration;
    }
    
    public DungeonConfiguration getDungeonConfiguration() {
        return dungeonConfiguration;
    }
    
    private static DungeonConfiguration EASY() {
        return new DungeonConfiguration();
    }
    
    private static DungeonConfiguration NORMAL() {
        return new DungeonConfiguration();
    }
    
    private static DungeonConfiguration HARD() {
        return new DungeonConfiguration();
    }
    
    private static DungeonConfiguration INSANE() {
        return new DungeonConfiguration();
    }
    
}
