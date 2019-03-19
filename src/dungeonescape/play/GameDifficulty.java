/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.play;

import dungeonescape.dungeon.DungeonConfiguration;
import dungeonescape.dungeonobject.FreezeTime;
import java.util.concurrent.TimeUnit;

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

    /**
     * (100 X 100) 10,000 space dungeon
     * 
     * @return 
     */
    private static DungeonConfiguration EASY() {
        return new DungeonConfiguration()
                .dungeonWidth(DungeonSize.SMALL.getDungeonWidth())
                .playerVisibility(15)
                .dungeonExitCount(10)
                .dungeonMasterCount(1)
                .spawnDungeonMastersTurnCount(25)
                .dungeonMasterDetectionDistance(5)
                .dungeonMasterNumberOfMovesWhenPatrolling(2)
                .dungeonMasterNumberOfMovesWhenHunting(2)
                .guardCount(5)
                .guardDetectionDistance(5)
                .guardNumberOfMovesWhenPatrolling(2)
                .guardNumberOfMovesWhenHunting(2)
                .ghostCount(8)
                .ghostFreezeTime(new FreezeTime(20, TimeUnit.MINUTES))
                .ghostDetectionDistance(10)
                .ghostNumberOfMovesWhenPatrolling(5)
                .ghostNumberOfMovesWhenHunting(5)
                .freezeMineCount(25)
                .maxFreezeMineTime(new FreezeTime(3, TimeUnit.MINUTES))
                .teleportMineCount(10);
    }

    /**
     * (500 X 500) 250,000 space dungeon
     * 
     * @return 
     */
    private static DungeonConfiguration NORMAL() {
        return new DungeonConfiguration()
                .dungeonWidth(DungeonSize.MEDIUM.getDungeonWidth())
                .playerVisibility(12)
                .dungeonExitCount(5)
                .dungeonMasterCount(3)
                .spawnDungeonMastersTurnCount(25)
                .dungeonMasterDetectionDistance(6)
                .dungeonMasterNumberOfMovesWhenPatrolling(3)
                .dungeonMasterNumberOfMovesWhenHunting(3)
                .guardCount(200)
                .guardDetectionDistance(5)
                .guardNumberOfMovesWhenPatrolling(2)
                .guardNumberOfMovesWhenHunting(3)
                .ghostCount(500)
                .ghostFreezeTime(new FreezeTime(30, TimeUnit.MINUTES))
                .ghostDetectionDistance(10)
                .ghostNumberOfMovesWhenPatrolling(5)
                .ghostNumberOfMovesWhenHunting(5)
                .freezeMineCount(2000)
                .maxFreezeMineTime(new FreezeTime(5, TimeUnit.MINUTES))
                .teleportMineCount(1000);
    }

    /**
     * (1000 X 1000) 1,000,000 space dungeon
     * 
     * @return 
     */
    private static DungeonConfiguration HARD() {
        return new DungeonConfiguration()
                .dungeonWidth(DungeonSize.LARGE.getDungeonWidth())
                .playerVisibility(10)
                .dungeonExitCount(3)
                .dungeonMasterCount(10)
                .spawnDungeonMastersTurnCount(25)
                .dungeonMasterDetectionDistance(8)
                .dungeonMasterNumberOfMovesWhenPatrolling(3)
                .dungeonMasterNumberOfMovesWhenHunting(4)
                .guardCount(150)
                .guardDetectionDistance(8)
                .guardNumberOfMovesWhenPatrolling(3)
                .guardNumberOfMovesWhenHunting(3)
                .ghostCount(300)
                .ghostFreezeTime(new FreezeTime(40, TimeUnit.MINUTES))
                .ghostDetectionDistance(10)
                .ghostNumberOfMovesWhenPatrolling(5)
                .ghostNumberOfMovesWhenHunting(8)
                .freezeMineCount(5000)
                .maxFreezeMineTime(new FreezeTime(10, TimeUnit.MINUTES))
                .teleportMineCount(1000);
    }

    /**
     * (5000 X 5000) 25,000,000 space dungeon
     * 
     * @return 
     */
    private static DungeonConfiguration INSANE() {
        return new DungeonConfiguration()
                .dungeonWidth(DungeonSize.EPIC.getDungeonWidth())
                .playerVisibility(8)
                .dungeonExitCount(2)
                .dungeonMasterCount(500)
                .spawnDungeonMastersTurnCount(20)
                .dungeonMasterDetectionDistance(10)
                .dungeonMasterNumberOfMovesWhenPatrolling(4)
                .dungeonMasterNumberOfMovesWhenHunting(5)
                .guardCount(10000)
                .guardDetectionDistance(8)
                .guardNumberOfMovesWhenPatrolling(3)
                .guardNumberOfMovesWhenHunting(4)
                .ghostCount(40000)
                .ghostFreezeTime(new FreezeTime(60, TimeUnit.MINUTES))
                .ghostDetectionDistance(50)
                .ghostNumberOfMovesWhenPatrolling(8)
                .ghostNumberOfMovesWhenHunting(10)
                .freezeMineCount(500000)
                .maxFreezeMineTime(new FreezeTime(15, TimeUnit.MINUTES))
                .teleportMineCount(200000);
    }

}
