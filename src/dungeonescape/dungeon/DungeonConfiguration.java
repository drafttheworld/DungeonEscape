/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon;

import dungeonescape.dungeonobject.mine.FreezeTime;

/**
 *
 * @author Andrew
 */
public class DungeonConfiguration {
    
    //Player settings
    private int playerVisibility;
    private int miniMapVisibility;
    
    //Dungeon settings
    private int dungeonWidth;
    private int dungeonExitCount;
    
    //Enemy settings
    private int numberOfDungeonMasters;
    private int numberOfGuards;
    private int numberOfGhosts;
    
    //Mine settings
    private int numberOfFreezeMines;
    private FreezeTime maxFreezeTime;
    
    private int numberOfTeleportMines;

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

    public FreezeTime getMaxFreezeTime() {
        return maxFreezeTime;
    }

    public void setMaxFreezeTime(FreezeTime maxFreezeTime) {
        this.maxFreezeTime = maxFreezeTime;
    }
    
    public DungeonConfiguration maxFreezeTime(FreezeTime maxFreezeTime) {
        setMaxFreezeTime(maxFreezeTime);
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
