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
    
    //Dungeon settings
    private int width;
    private int exitCount;
    
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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    
    public DungeonConfiguration width(int width) {
        setWidth(width);
        return this;
    }

    public int getExitCount() {
        return exitCount;
    }

    public void setExitCount(int exitCount) {
        this.exitCount = exitCount;
    }
    
    public DungeonConfiguration exitCount(int exitCount) {
        setExitCount(exitCount);
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
