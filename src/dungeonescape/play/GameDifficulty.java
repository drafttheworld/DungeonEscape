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

    EASY(),
    NORMAL(),
    HARD(),
    INSANE();

    public DungeonConfiguration getDungeonConfiguration() {
        switch(this) {
            case EASY:
                return EASY();
            case NORMAL:
                return NORMAL();
            case HARD:
                return HARD();
            case INSANE:
                return INSANE();
        }
        return null;
    }

    private static DungeonConfiguration EASY() {
        return new DungeonConfiguration();
    }

    private static DungeonConfiguration NORMAL() {
        return new DungeonConfiguration();
    }

    private static DungeonConfiguration HARD() {
        return new DungeonConfiguration()
                .playerVisibility(25)
                .dungeonExitCount(5)
                .dungeonMasterPercentage(.005)
                .spawnDungeonMastersTurnCount(25)
                .guardPercentage(.01)
                .ghostPercentage(.1)
                .freezeMinePercentage(1)
                .teleportMinePercentage(.5);
    }

    private static DungeonConfiguration INSANE() {
        return new DungeonConfiguration();
    }

}
