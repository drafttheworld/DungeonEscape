/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon;

import dungeonescape.dungeonobject.FreezeTime;
import dungeonescape.dungeonobject.characters.DungeonMaster;
import dungeonescape.dungeonobject.characters.Ghost;
import dungeonescape.dungeonobject.characters.Guard;
import dungeonescape.dungeonobject.mine.FreezeMine;

/**
 *
 * @author Andrew
 */
public class DungeonConfiguration {

    //Player settings
    private String playerName;
    private int playerVisibility;

    //Dungeon settings
    private int dungeonWidth;
    private int dungeonExitCount;

    //Enemy settings
    private int dungeonMasterCount;
    private int dungeonMasterNumberOfMovesWhenPatrolling;
    private int dungeonMasterNumberOfMovesWhenHunting;
    private long dungeonMasterMovementRateMs;
    private int dungeonMasterDetectionDistance;
    private int spawnDungeonMastersTurnCount;
    private int guardCount;
    private int guardNumberOfMovesWhenPatrolling;
    private int guardNumberOfMovesWhenHunting;
    private long guardMovementRateMs;
    private int guardDetectionDistance;
    private int ghostCount;
    private int ghostNumberOfMovesWhenPatrolling;
    private int ghostNumberOfMovesWhenHunting;
    private long ghostMovementRateMs;
    private int ghostDetectionDistance;
    private FreezeTime ghostMinFreezeTime;
    private FreezeTime ghostMaxFreezeTime;

    //Mine settings
    private int freezeMineCount;
    private FreezeTime freezeMineMinFreezeTime;
    private FreezeTime freezeMineMaxFreezeTime;
    private int teleportMineCount;

    //Power Up settings
    private int powerUpBoxCount;
    private int invincibilityProbability;
    private int invisibilityProbability;
    private int repellentProbability;
    private int terminatorProbability;
    private int powerUpDurationTurns;

    //Coin settings
    private Integer coinCoveragePercentOfOpenSpaces;//null to disable, 0-100 to populate a percent of open spaces.
    private Integer coinCountOverride;//null to disable, 0-n to populate specific number of coins.

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        if (playerName == null || "".equals(playerName)) {
            throw new IllegalArgumentException("Player name cannot be blank.");
        }
        this.playerName = playerName;
    }

    public DungeonConfiguration playerName(String playerName) {
        setPlayerName(playerName);
        return this;
    }

    public int getPlayerVisibility() {
        return playerVisibility;
    }

    public void setPlayerVisibility(int playerVisibility) {
        this.playerVisibility = playerVisibility;
    }

    public DungeonConfiguration playerVisibility(int playerVisibility) {
        setPlayerVisibility(playerVisibility);
        return this;
    }

    public int getDungeonWidth() {
        return dungeonWidth;
    }

    public void setDungeonWidth(int width) {
        this.dungeonWidth = width;
    }

    public DungeonConfiguration dungeonWidth(int width) {
        setDungeonWidth(width);
        return this;
    }

    public int getDungeonExitCount() {
        return dungeonExitCount;
    }

    public void setDungeonExitCount(int exitCount) {
        this.dungeonExitCount = exitCount;
    }

    public DungeonConfiguration dungeonExitCount(int exitCount) {
        setDungeonExitCount(exitCount);
        return this;
    }

    public int getDungeonMasterCount() {
        return dungeonMasterCount;
    }

    public void setDungeonMasterCount(int dungeonMasterCount) {
        this.dungeonMasterCount = dungeonMasterCount;
    }

    public DungeonConfiguration dungeonMasterCount(int dungeonMasterCount) {
        setDungeonMasterCount(dungeonMasterCount);
        return this;
    }

    public int getDungeonMasterNumberOfMovesWhenPatrolling() {
        if (dungeonMasterNumberOfMovesWhenPatrolling == 0) {
            return DungeonMaster.DEFAULT_MOVES_WHEN_PATROLLING;
        }
        return dungeonMasterNumberOfMovesWhenPatrolling;
    }

    public void setDungeonMasterNumberOfMovesWhenPatrolling(int dungeonMasterNumberOfMovesWhenPatrolling) {
        this.dungeonMasterNumberOfMovesWhenPatrolling = dungeonMasterNumberOfMovesWhenPatrolling;
    }

    public DungeonConfiguration dungeonMasterNumberOfMovesWhenPatrolling(int dungeonMasterNumberOfMovesWhenPatrolling) {
        setDungeonMasterNumberOfMovesWhenPatrolling(dungeonMasterNumberOfMovesWhenPatrolling);
        return this;
    }

    public int getDungeonMasterNumberOfMovesWhenHunting() {
        if (dungeonMasterNumberOfMovesWhenHunting == 0) {
            return DungeonMaster.DEFAULT_MOVES_WHEN_HUNTING;
        }
        return dungeonMasterNumberOfMovesWhenHunting;
    }

    public void setDungeonMasterNumberOfMovesWhenHunting(int dungeonMasterNumberOfMovesWhenHunting) {
        this.dungeonMasterNumberOfMovesWhenHunting = dungeonMasterNumberOfMovesWhenHunting;
    }

    public DungeonConfiguration dungeonMasterNumberOfMovesWhenHunting(int dungeonMasterNumberOfMovesWhenHunting) {
        setDungeonMasterNumberOfMovesWhenHunting(dungeonMasterNumberOfMovesWhenPatrolling);
        return this;
    }

    public long getDungeonMasterMovementRateMs() {
        return dungeonMasterMovementRateMs;
    }

    public void setDungeonMasterMovementRateMs(long dungeonMasterMovementRateMs) {
        this.dungeonMasterMovementRateMs = dungeonMasterMovementRateMs;
    }

    public DungeonConfiguration dungeonMasterMovementRateMs(long dungeonMasterMovementRateMs) {
        setDungeonMasterMovementRateMs(dungeonMasterMovementRateMs);
        return this;
    }

    public int getDungeonMasterDetectionDistance() {
        if (dungeonMasterDetectionDistance == 0) {
            return DungeonMaster.DEFAULT_DETECTION_DISTANCE;
        }
        return dungeonMasterDetectionDistance;
    }

    public void setDungeonMasterDetectionDistance(int dungeonMasterDetectionDistance) {
        this.dungeonMasterDetectionDistance = dungeonMasterDetectionDistance;
    }

    public DungeonConfiguration dungeonMasterDetectionDistance(int dungeonMasterDetectionDistance) {
        setDungeonMasterDetectionDistance(dungeonMasterDetectionDistance);
        return this;
    }

    public int getSpawnDungeonMastersTurnCount() {
        return spawnDungeonMastersTurnCount;
    }

    public void setSpawnDungeonMastersTurnCount(int spawnDungeonMastersTurnCount) {
        this.spawnDungeonMastersTurnCount = spawnDungeonMastersTurnCount;
    }

    public DungeonConfiguration spawnDungeonMastersTurnCount(int spawnDungeonMastersTurnCount) {
        setSpawnDungeonMastersTurnCount(spawnDungeonMastersTurnCount);
        return this;
    }

    public int getGuardCount() {
        return guardCount;
    }

    public void setGuardCount(int guardCount) {
        this.guardCount = guardCount;
    }

    public DungeonConfiguration guardCount(int guardCount) {
        setGuardCount(guardCount);
        return this;
    }

    public int getGuardNumberOfMovesWhenPatrolling() {
        if (guardNumberOfMovesWhenPatrolling == 0) {
            return Guard.DEFAULT_MOVES_WHEN_PATROLLING;
        }
        return guardNumberOfMovesWhenPatrolling;
    }

    public void setGuardNumberOfMovesWhenPatrolling(int guardNumberOfMovesWhenPatrolling) {
        this.guardNumberOfMovesWhenPatrolling = guardNumberOfMovesWhenPatrolling;
    }

    public DungeonConfiguration guardNumberOfMovesWhenPatrolling(int guardNumberOfMovesWhenPatrolling) {
        setGuardNumberOfMovesWhenPatrolling(guardNumberOfMovesWhenPatrolling);
        return this;
    }

    public int getGuardNumberOfMovesWhenHunting() {
        if (guardNumberOfMovesWhenHunting == 0) {
            return Guard.DEFAULT_MOVES_WHEN_HUNTING;
        }
        return guardNumberOfMovesWhenHunting;
    }

    public void setGuardNumberOfMovesWhenHunting(int guardNumberOfMovesWhenHunting) {
        this.guardNumberOfMovesWhenHunting = guardNumberOfMovesWhenHunting;
    }

    public DungeonConfiguration guardNumberOfMovesWhenHunting(int guardNumberOfMovesWhenHunting) {
        setGuardNumberOfMovesWhenHunting(guardNumberOfMovesWhenHunting);
        return this;
    }

    public long getGuardMovementRateMs() {
        return guardMovementRateMs;
    }

    public void setGuardMovementRateMs(long guardMovementRateMs) {
        this.guardMovementRateMs = guardMovementRateMs;
    }

    public DungeonConfiguration guardMovementRateMs(long guardMovementRateMs) {
        setGuardMovementRateMs(guardMovementRateMs);
        return this;
    }

    public int getGuardDetectionDistance() {
        if (guardDetectionDistance == 0) {
            return Guard.DEFAULT_DETECTION_DISTANCE;
        }
        return guardDetectionDistance;
    }

    public void setGuardDetectionDistance(int guardDetectionDistance) {
        this.guardDetectionDistance = guardDetectionDistance;
    }

    public DungeonConfiguration guardDetectionDistance(int guardDetectionDistance) {
        setGuardDetectionDistance(guardDetectionDistance);
        return this;
    }

    public int getGhostCount() {
        return ghostCount;
    }

    public void setGhostCount(int ghostCount) {
        this.ghostCount = ghostCount;
    }

    public DungeonConfiguration ghostCount(int ghostCount) {
        setGhostCount(ghostCount);
        return this;
    }

    public FreezeTime getGhostMinFreezeTime() {
        if (ghostMinFreezeTime == null) {
            return Ghost.DEFAULT_FREEZE_TIME;
        }
        return ghostMinFreezeTime;
    }

    public void setGhostMinFreezeTime(FreezeTime ghostFreezeTime) {
        this.ghostMinFreezeTime = ghostFreezeTime;
    }

    public DungeonConfiguration ghostMinFreezeTime(FreezeTime freezeTime) {
        setGhostMinFreezeTime(freezeTime);
        return this;
    }

    public FreezeTime getGhostMaxFreezeTime() {
        if (ghostMaxFreezeTime == null) {
            return Ghost.DEFAULT_FREEZE_TIME;
        }
        return ghostMaxFreezeTime;
    }

    public void setGhostMaxFreezeTime(FreezeTime ghostFreezeTime) {
        this.ghostMaxFreezeTime = ghostFreezeTime;
    }

    public DungeonConfiguration ghostMaxFreezeTime(FreezeTime freezeTime) {
        setGhostMaxFreezeTime(freezeTime);
        return this;
    }

    public int getGhostNumberOfMovesWhenPatrolling() {
        if (ghostNumberOfMovesWhenPatrolling == 0) {
            return Ghost.DEFAULT_MOVES_WHEN_PATROLLING;
        }
        return ghostNumberOfMovesWhenPatrolling;
    }

    public void setGhostNumberOfMovesWhenPatrolling(int ghostNumberOfMovesWhenPatrolling) {
        this.ghostNumberOfMovesWhenPatrolling = ghostNumberOfMovesWhenPatrolling;
    }

    public DungeonConfiguration ghostNumberOfMovesWhenPatrolling(int ghostNumberOfMovesWhenPatrolling) {
        setGhostNumberOfMovesWhenPatrolling(ghostNumberOfMovesWhenPatrolling);
        return this;
    }

    public int getGhostNumberOfMovesWhenHunting() {
        if (ghostNumberOfMovesWhenHunting == 0) {
            return Ghost.DEFAULT_MOVES_WHEN_HUNTING;
        }
        return ghostNumberOfMovesWhenHunting;
    }

    public void setGhostNumberOfMovesWhenHunting(int ghostNumberOfMovesWhenHunting) {
        this.ghostNumberOfMovesWhenHunting = ghostNumberOfMovesWhenHunting;
    }

    public DungeonConfiguration ghostNumberOfMovesWhenHunting(int ghostNumberOfMovesWhenHunting) {
        setGhostNumberOfMovesWhenHunting(ghostNumberOfMovesWhenHunting);
        return this;
    }

    public long getGhostMovementRateMs() {
        return ghostMovementRateMs;
    }

    public void setGhostMovementRateMs(long ghostMovementRateMs) {
        this.ghostMovementRateMs = ghostMovementRateMs;
    }

    public DungeonConfiguration ghostMovementRateMs(long ghostMovementRateMs) {
        setGhostMovementRateMs(ghostMovementRateMs);
        return this;
    }

    public int getGhostDetectionDistance() {
        if (ghostDetectionDistance == 0) {
            return Ghost.DEFAULT_DETECTION_DISTANCE;
        }
        return ghostDetectionDistance;
    }

    public void setGhostDetectionDistance(int ghostDetectionDistance) {
        this.ghostDetectionDistance = ghostDetectionDistance;
    }

    public DungeonConfiguration ghostDetectionDistance(int ghostDetectionDistance) {
        setGhostDetectionDistance(ghostDetectionDistance);
        return this;
    }

    public int getFreezeMineCount() {
        return freezeMineCount;
    }

    public void setFreezeMineCount(int freezeMineCount) {
        this.freezeMineCount = freezeMineCount;
    }

    public DungeonConfiguration freezeMineCount(int freezeMineCount) {
        setFreezeMineCount(freezeMineCount);
        return this;
    }

    public FreezeTime getFreezeMineMinFreezeTime() {
        if (freezeMineMinFreezeTime == null) {
            return FreezeMine.DEFAULT_MAX_FREEZE_TIME;
        }
        return freezeMineMinFreezeTime;
    }

    public void setFreezeMineMinFreezeTime(FreezeTime freezeMineMinFreezeTime) {
        this.freezeMineMinFreezeTime = freezeMineMinFreezeTime;
    }

    public DungeonConfiguration freezeMineMinFreezeTime(FreezeTime freezeMineMinFreezeTime) {
        setFreezeMineMinFreezeTime(freezeMineMinFreezeTime);
        return this;
    }

    public FreezeTime getFreezeMineMaxFreezeTime() {
        if (freezeMineMaxFreezeTime == null) {
            return FreezeMine.DEFAULT_MAX_FREEZE_TIME;
        }
        return freezeMineMaxFreezeTime;
    }

    public void setFreezeMineMaxFreezeTime(FreezeTime freezeMineMaxFreezeTime) {
        this.freezeMineMaxFreezeTime = freezeMineMaxFreezeTime;
    }

    public DungeonConfiguration freezeMineMaxFreezeTime(FreezeTime freezeMineMaxFreezeTime) {
        setFreezeMineMaxFreezeTime(freezeMineMaxFreezeTime);
        return this;
    }

    public int getTeleportMineCount() {
        return teleportMineCount;
    }

    public void setTeleportMineCount(int teleportMineCount) {
        this.teleportMineCount = teleportMineCount;
    }

    public DungeonConfiguration teleportMineCount(int teleportMineCount) {
        setTeleportMineCount(teleportMineCount);
        return this;
    }

    public int getPowerUpBoxCount() {
        return powerUpBoxCount;
    }

    public void setPowerUpBoxCount(int powerUpBoxCount) {
        this.powerUpBoxCount = powerUpBoxCount;
    }

    public DungeonConfiguration powerUpBoxCount(int powerUpBoxCount) {
        setPowerUpBoxCount(powerUpBoxCount);
        return this;
    }

    public int getInvincibilityProbability() {
        return invincibilityProbability;
    }

    public void setInvincibilityProbability(int invincibilityProbability) {
        this.invincibilityProbability = invincibilityProbability;
    }

    public DungeonConfiguration invincibilityProbability(int invincibilityProbability) {
        setInvincibilityProbability(invincibilityProbability);
        return this;
    }

    public int getInvisibilityProbability() {
        return invisibilityProbability;
    }

    public void setInvisibilityProbability(int invisibilityProbability) {
        this.invisibilityProbability = invisibilityProbability;
    }

    public DungeonConfiguration invisibilityProbability(int invisibilityProbability) {
        setInvisibilityProbability(invisibilityProbability);
        return this;
    }

    public int getRepellentProbability() {
        return repellentProbability;
    }

    public void setRepellentProbability(int repellentProbability) {
        this.repellentProbability = repellentProbability;
    }

    public DungeonConfiguration repellentProbability(int repellentProbability) {
        setRepellentProbability(repellentProbability);
        return this;
    }

    public int getTerminatorProbability() {
        return terminatorProbability;
    }

    public void setTerminatorProbability(int terminatorProbability) {
        this.terminatorProbability = terminatorProbability;
    }

    public DungeonConfiguration terminatorProbability(int terminatorProbability) {
        setTerminatorProbability(terminatorProbability);
        return this;
    }

    public int getPowerUpDurationTurns() {
        return powerUpDurationTurns;
    }

    public void setPowerUpDurationTurns(int powerUpDurationTurns) {
        this.powerUpDurationTurns = powerUpDurationTurns;
    }

    public DungeonConfiguration powerUpDurationTurns(int powerUpDurationTurns) {
        setPowerUpDurationTurns(powerUpDurationTurns);
        return this;
    }

    public Integer getCoinCoveragePercentOfOpenSpaces() {
        return coinCoveragePercentOfOpenSpaces;
    }

    public void setCoinCoveragePercentOfOpenSpaces(Integer coinCoveragePercentOfOpenSpaces) {
        this.coinCoveragePercentOfOpenSpaces = coinCoveragePercentOfOpenSpaces;
    }

    public DungeonConfiguration coinCoveragePercentOfOpenSpaces(Integer coinCoveragePercentOfOpenSpaces) {
        setCoinCoveragePercentOfOpenSpaces(coinCoveragePercentOfOpenSpaces);
        return this;
    }

    public Integer getCoinCountOverride() {
        return coinCountOverride;
    }

    public void setCoinCountOverride(Integer coinCountOverride) {
        this.coinCountOverride = coinCountOverride;
    }

    public DungeonConfiguration coinCountOverride(Integer coinCountOverride) {
        setCoinCountOverride(coinCountOverride);
        return this;
    }

    public boolean enemyAndMinePercentagesAreValid() {
        return Math.ceil(getDungeonMasterCount()
            + getGuardCount()
            + getGhostCount()
            + getFreezeMineCount()
            + getTeleportMineCount())
            <= 100;
    }

}
