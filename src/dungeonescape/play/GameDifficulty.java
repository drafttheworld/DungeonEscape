/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.play;

import dungeonescape.dungeon.DungeonConfiguration;
import dungeonescape.dungeonobject.FreezeTime;

/**
 *
 * @author Andrew
 */
public enum GameDifficulty {

    EASY,
    NORMAL,
    HARD,
    INSANE,
    CUSTOM;

    public DungeonConfiguration getDungeonConfiguration() {
        switch (this) {
            case EASY:
                return EASY();
            case NORMAL:
                return NORMAL();
            case HARD:
                return HARD();
            case INSANE:
                return INSANE();
            case CUSTOM:
                return new DungeonConfiguration();
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
            .dungeonExitCount(8)
            .dungeonMasterCount(1)
            .spawnDungeonMastersTurnCount(25)
            .dungeonMasterDetectionDistance(3)
            .dungeonMasterNumberOfMovesWhenPatrolling(2)
            .dungeonMasterNumberOfMovesWhenHunting(1)
            .guardCount(5)
            .guardDetectionDistance(3)
            .guardNumberOfMovesWhenPatrolling(2)
            .guardNumberOfMovesWhenHunting(1)
            .ghostCount(8)
            .ghostMinFreezeTime(new FreezeTime(10))
            .ghostMaxFreezeTime(new FreezeTime(10))
            .ghostDetectionDistance(5)
            .ghostNumberOfMovesWhenPatrolling(3)
            .ghostNumberOfMovesWhenHunting(1)
            .freezeMineCount(25)
            .freezeMineMinFreezeTime(new FreezeTime(5))
            .freezeMineMaxFreezeTime(new FreezeTime(5))
            .teleportMineCount(10)
            .powerUpBoxCount(10)
            .invincibilityProbability(1)
            .invisibilityProbability(1)
            .repellentProbability(1)
            .terminatorProbability(1)
            .coinCoveragePercentOfOpenSpaces(15);
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
            .dungeonMasterDetectionDistance(4)
            .dungeonMasterNumberOfMovesWhenPatrolling(3)
            .dungeonMasterNumberOfMovesWhenHunting(3)
            .guardCount(200)
            .guardDetectionDistance(4)
            .guardNumberOfMovesWhenPatrolling(2)
            .guardNumberOfMovesWhenHunting(2)
            .ghostCount(500)
            .ghostMinFreezeTime(new FreezeTime(10))
            .ghostMaxFreezeTime(new FreezeTime(20))
            .ghostDetectionDistance(5)
            .ghostNumberOfMovesWhenPatrolling(5)
            .ghostNumberOfMovesWhenHunting(3)
            .freezeMineCount(2000)
            .freezeMineMinFreezeTime(new FreezeTime(5))
            .freezeMineMaxFreezeTime(new FreezeTime(15))
            .teleportMineCount(1000)
            .powerUpBoxCount(20)
            .invincibilityProbability(1)
            .invisibilityProbability(1)
            .repellentProbability(1)
            .terminatorProbability(1)
            .coinCoveragePercentOfOpenSpaces(15);
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
            .dungeonMasterDetectionDistance(5)
            .dungeonMasterNumberOfMovesWhenPatrolling(3)
            .dungeonMasterNumberOfMovesWhenHunting(3)
            .guardCount(150)
            .guardDetectionDistance(5)
            .guardNumberOfMovesWhenPatrolling(3)
            .guardNumberOfMovesWhenHunting(3)
            .ghostCount(300)
            .ghostMinFreezeTime(new FreezeTime(15))
            .ghostMaxFreezeTime(new FreezeTime(25))
            .ghostDetectionDistance(5)
            .ghostNumberOfMovesWhenPatrolling(5)
            .ghostNumberOfMovesWhenHunting(3)
            .freezeMineCount(5000)
            .freezeMineMinFreezeTime(new FreezeTime(10))
            .freezeMineMaxFreezeTime(new FreezeTime(20))
            .teleportMineCount(1000)
            .powerUpBoxCount(30)
            .invincibilityProbability(1)
            .invisibilityProbability(1)
            .repellentProbability(1)
            .terminatorProbability(1)
            .coinCoveragePercentOfOpenSpaces(15);
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
            .dungeonMasterDetectionDistance(5)
            .dungeonMasterNumberOfMovesWhenPatrolling(3)
            .dungeonMasterNumberOfMovesWhenHunting(3)
            .guardCount(10000)
            .guardDetectionDistance(5)
            .guardNumberOfMovesWhenPatrolling(3)
            .guardNumberOfMovesWhenHunting(3)
            .ghostCount(40000)
            .ghostMinFreezeTime(new FreezeTime(20))
            .ghostMaxFreezeTime(new FreezeTime(30))
            .ghostDetectionDistance(8)
            .ghostNumberOfMovesWhenPatrolling(8)
            .ghostNumberOfMovesWhenHunting(5)
            .freezeMineCount(500000)
            .freezeMineMinFreezeTime(new FreezeTime(15))
            .freezeMineMaxFreezeTime(new FreezeTime(25))
            .teleportMineCount(200000)
            .powerUpBoxCount(40)
            .invincibilityProbability(1)
            .invisibilityProbability(1)
            .repellentProbability(1)
            .terminatorProbability(1)
            .coinCoveragePercentOfOpenSpaces(15);
    }

}
