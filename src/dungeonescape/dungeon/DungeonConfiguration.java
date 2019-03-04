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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Andrew
 */
public class DungeonConfiguration {

    //Player settings
    private final List<String> playerNames;
    private int playerVisibility;
    private int miniMapVisibility;

    //Dungeon settings
    private int dungeonWidth;
    private int dungeonExitCount;

    //Enemy settings
    private int numberOfDungeonMasters;
    private int dungeonMasterNumberOfMovesWhenPatrolling;
    private int dungeonMasterNumberOfMovesWhenHunting;
    private int dungeonMasterDetectionDistance;
    private int spawnDungeonMastersTurnCount;
    private int numberOfGuards;
    private int guardNumberOfMovesWhenPatrolling;
    private int guardNumberOfMovesWhenHunting;
    private int guardDetectionDistance;
    private int numberOfGhosts;
    private int ghostNumberOfMovesWhenPatrolling;
    private int ghostNumberOfMovesWhenHunting;
    private int ghostDetectionDistance;
    private FreezeTime ghostFreezeTime;

    //Mine settings
    private int numberOfFreezeMines;
    private FreezeTime maxFreezeMineTime;

    private int numberOfTeleportMines;

    public DungeonConfiguration() {
        playerNames = new ArrayList<>();
    }

    public List<String> getPlayerNames() {
        return Collections.unmodifiableList(playerNames);
    }

    public void addPlayerName(String playerName) {
        if (playerName == null || "".equals(playerName)) {
            throw new IllegalArgumentException("Player name cannot be blank.");
        } else if (playerNames.contains(playerName)) {
            throw new IllegalArgumentException("Player " + playerName + " already exists. "
                    + "Please provide a unique name.");
        }
        playerNames.add(playerName);
    }

    public DungeonConfiguration playerName(String playerName) {
        addPlayerName(playerName);
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

    public int getMiniMapVisibility() {
        return miniMapVisibility;
    }

    public void setMiniMapVisibility(int miniMapVisibility) {
        this.miniMapVisibility = miniMapVisibility;
    }

    public DungeonConfiguration miniMapVisibility(int miniMapVisibility) {
        setMiniMapVisibility(miniMapVisibility);
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

    public int getNumberOfDungeonMasters() {
        return numberOfDungeonMasters;
    }

    public void setNumberOfDungeonMasters(int numberOfDungeonMasters) {
        this.numberOfDungeonMasters = numberOfDungeonMasters;
    }

    public DungeonConfiguration numberOfDungeonMasters(int numberOfDungeonMasters) {
        setNumberOfDungeonMasters(numberOfDungeonMasters);
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

    public int getNumberOfGuards() {
        return numberOfGuards;
    }

    public void setNumberOfGuards(int numberOfGuards) {
        this.numberOfGuards = numberOfGuards;
    }

    public DungeonConfiguration numberOfGuards(int numberOfGuards) {
        setNumberOfGuards(numberOfGuards);
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

    public int getNumberOfGhosts() {
        return numberOfGhosts;
    }

    public void setNumberOfGhosts(int numberOfGhosts) {
        this.numberOfGhosts = numberOfGhosts;
    }

    public DungeonConfiguration numberOfGhosts(int numberOfGhosts) {
        setNumberOfGhosts(numberOfGhosts);
        return this;
    }

    public FreezeTime getGhostFreezeTime() {
        if (ghostFreezeTime == null) {
            return Ghost.DEFAULT_FREEZE_TIME;
        }
        return ghostFreezeTime;
    }

    public void setGhostFreezeTime(FreezeTime ghostFreezeTime) {
        this.ghostFreezeTime = ghostFreezeTime;
    }

    public DungeonConfiguration ghostFreezeTime(FreezeTime freezeTime) {
        setGhostFreezeTime(freezeTime);
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

    public int getNumberOfFreezeMines() {
        return numberOfFreezeMines;
    }

    public void setNumberOfFreezeMines(int numberOfFreezeMines) {
        this.numberOfFreezeMines = numberOfFreezeMines;
    }

    public DungeonConfiguration numberOfFreezeMines(int numberOfFreezeMines) {
        setNumberOfFreezeMines(numberOfFreezeMines);
        return this;
    }

    public FreezeTime getMaxFreezeMineTime() {
        if (maxFreezeMineTime == null) {
            return FreezeMine.DEFAULT_MAX_FREEZE_TIME;
        }
        return maxFreezeMineTime;
    }

    public void setMaxFreezeMineTime(FreezeTime maxFreezeTime) {
        this.maxFreezeMineTime = maxFreezeTime;
    }

    public DungeonConfiguration maxFreezeMineTime(FreezeTime maxFreezeTime) {
        setMaxFreezeMineTime(maxFreezeTime);
        return this;
    }

    public int getNumberOfTeleportMines() {
        return numberOfTeleportMines;
    }

    public void setNumberOfTeleportMines(int numberOfTeleportMines) {
        this.numberOfTeleportMines = numberOfTeleportMines;
    }

    public DungeonConfiguration numberOfTeleportMines(int numberOfTeleportMines) {
        setNumberOfTeleportMines(numberOfTeleportMines);
        return this;
    }

}
