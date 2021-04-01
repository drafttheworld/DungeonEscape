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
    DEMO,
    EASY,
    NORMAL,
    HARD,
    CUSTOM;

    public DungeonConfiguration getDungeonConfiguration() {
        switch (this) {
            case DEMO:
                return demo();
            case EASY:
                return easy();
            case NORMAL:
                return normal();
            case HARD:
                return hard();
            case CUSTOM:
                return new DungeonConfiguration();
        }
        return null;
    }

    private static DungeonConfiguration demo() {
        return new DungeonConfiguration()
            .dungeonWidth(25)
            .playerVisibility(15)
            .dungeonExitCount(8)
            .dungeonMasterCount(0)
            .spawnDungeonMastersTurnCount(25)
            .dungeonMasterDetectionDistance(5)
            .dungeonMasterNumberOfMovesWhenPatrolling(2)
            .dungeonMasterNumberOfMovesWhenHunting(1)
            .dungeonMasterMovementRateMs(1000)
            .guardCount(0)
            .guardDetectionDistance(5)
            .guardNumberOfMovesWhenPatrolling(2)
            .guardNumberOfMovesWhenHunting(1)
            .guardMovementRateMs(1000)
            .ghostCount(1)
            .ghostMinFreezeTime(new FreezeTime(10))
            .ghostMaxFreezeTime(new FreezeTime(10))
            .ghostDetectionDistance(5)
            .ghostNumberOfMovesWhenPatrolling(3)
            .ghostNumberOfMovesWhenHunting(1)
            .ghostMovementRateMs(1000)
            .freezeMineCount(0)
            .freezeMineMinFreezeTime(new FreezeTime(5))
            .freezeMineMaxFreezeTime(new FreezeTime(5))
            .teleportMineCount(0)
            .powerUpBoxCount(1)
            .invincibilityProbability(1)
            .invisibilityProbability(1)
            .repellentProbability(1)
            .terminatorProbability(1)
            .powerUpDurationTurns(10)
            .coinCoveragePercentOfOpenSpaces(0);
    }

    /**
     * (100 X 100) 10,000 space dungeon
     *
     * @return
     */
    private static DungeonConfiguration easy() {
        return new DungeonConfiguration()
            .dungeonWidth(DungeonSize.SMALL.getDungeonWidth())
            .playerVisibility(15)
            .dungeonExitCount(8)
            .dungeonMasterCount(1)
            .spawnDungeonMastersTurnCount(25)
            .dungeonMasterDetectionDistance(5)
            .dungeonMasterNumberOfMovesWhenPatrolling(2)
            .dungeonMasterNumberOfMovesWhenHunting(1)
            .dungeonMasterMovementRateMs(1000)
            .guardCount(5)
            .guardDetectionDistance(5)
            .guardNumberOfMovesWhenPatrolling(2)
            .guardNumberOfMovesWhenHunting(1)
            .guardMovementRateMs(1000)
            .ghostCount(8)
            .ghostMinFreezeTime(new FreezeTime(10))
            .ghostMaxFreezeTime(new FreezeTime(10))
            .ghostDetectionDistance(5)
            .ghostNumberOfMovesWhenPatrolling(3)
            .ghostNumberOfMovesWhenHunting(1)
            .ghostMovementRateMs(1000)
            .freezeMineCount(25)
            .freezeMineMinFreezeTime(new FreezeTime(5))
            .freezeMineMaxFreezeTime(new FreezeTime(5))
            .teleportMineCount(10)
            .powerUpBoxCount(10)
            .invincibilityProbability(1)
            .invisibilityProbability(1)
            .repellentProbability(1)
            .terminatorProbability(1)
            .powerUpDurationTurns(10)
            .coinCoveragePercentOfOpenSpaces(10);
    }

    /**
     * (500 X 500) 250,000 space dungeon
     *
     * @return
     */
    private static DungeonConfiguration normal() {
        return new DungeonConfiguration()
            .dungeonWidth(DungeonSize.MEDIUM.getDungeonWidth())
            .playerVisibility(12)
            .dungeonExitCount(5)
            .dungeonMasterCount(3)
            .spawnDungeonMastersTurnCount(25)
            .dungeonMasterDetectionDistance(6)
            .dungeonMasterNumberOfMovesWhenPatrolling(3)
            .dungeonMasterNumberOfMovesWhenHunting(3)
            .dungeonMasterMovementRateMs(500)
            .guardCount(100)
            .guardDetectionDistance(6)
            .guardNumberOfMovesWhenPatrolling(2)
            .guardNumberOfMovesWhenHunting(2)
            .guardMovementRateMs(500)
            .ghostCount(200)
            .ghostMinFreezeTime(new FreezeTime(10))
            .ghostMaxFreezeTime(new FreezeTime(20))
            .ghostDetectionDistance(6)
            .ghostNumberOfMovesWhenPatrolling(5)
            .ghostNumberOfMovesWhenHunting(3)
            .ghostMovementRateMs(500)
            .freezeMineCount(1000)
            .freezeMineMinFreezeTime(new FreezeTime(5))
            .freezeMineMaxFreezeTime(new FreezeTime(15))
            .teleportMineCount(500)
            .powerUpBoxCount(20)
            .invincibilityProbability(1)
            .invisibilityProbability(1)
            .repellentProbability(1)
            .terminatorProbability(1)
            .powerUpDurationTurns(10)
            .coinCoveragePercentOfOpenSpaces(5);
    }

    /**
     * (1000 X 1000) 1,000,000 space dungeon
     *
     * @return
     */
    private static DungeonConfiguration hard() {
        return new DungeonConfiguration()
            .dungeonWidth(DungeonSize.LARGE.getDungeonWidth())
            .playerVisibility(10)
            .dungeonExitCount(3)
            .dungeonMasterCount(10)
            .spawnDungeonMastersTurnCount(25)
            .dungeonMasterDetectionDistance(7)
            .dungeonMasterNumberOfMovesWhenPatrolling(3)
            .dungeonMasterNumberOfMovesWhenHunting(3)
            .dungeonMasterMovementRateMs(400)
            .guardCount(150)
            .guardDetectionDistance(7)
            .guardNumberOfMovesWhenPatrolling(3)
            .guardNumberOfMovesWhenHunting(3)
            .guardMovementRateMs(400)
            .ghostCount(300)
            .ghostMinFreezeTime(new FreezeTime(15))
            .ghostMaxFreezeTime(new FreezeTime(25))
            .ghostDetectionDistance(7)
            .ghostNumberOfMovesWhenPatrolling(5)
            .ghostNumberOfMovesWhenHunting(3)
            .ghostMovementRateMs(400)
            .freezeMineCount(5000)
            .freezeMineMinFreezeTime(new FreezeTime(10))
            .freezeMineMaxFreezeTime(new FreezeTime(20))
            .teleportMineCount(1000)
            .powerUpBoxCount(30)
            .invincibilityProbability(1)
            .invisibilityProbability(1)
            .repellentProbability(1)
            .terminatorProbability(1)
            .powerUpDurationTurns(10)
            .coinCoveragePercentOfOpenSpaces(1);
    }
}
