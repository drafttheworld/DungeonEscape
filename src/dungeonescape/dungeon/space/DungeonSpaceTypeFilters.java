/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon.space;

import dungeonescape.dungeon.space.DungeonSpaceType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andrew
 */
public class DungeonSpaceTypeFilters {
    
    private Position playerPosition;
    private int playerVisibility = 0;
    private int mapVisibility = Integer.MAX_VALUE;
    
    private final List<DungeonSpaceType> dungeonSpaceTypeExclusions = new ArrayList<>();

    public Position getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(Position playerPosition) {
        this.playerPosition = playerPosition;
    }
    
    public int getPlayerVisibility() {
        return playerVisibility;
    }
    
    public void setPlayerVisibility(int playerVisibility) {
        this.playerVisibility = playerVisibility;
    }
    
    public int getMapVisibility() {
        return mapVisibility;
    }
    
    public void setMapVisibility(int mapVisibility) {
        this.mapVisibility = mapVisibility;
    }
    
    public void addExclusionType(DungeonSpaceType dungeonSpaceType) {
        dungeonSpaceTypeExclusions.add(dungeonSpaceType);
    }
    
    public void removeExclusionType(DungeonSpaceType dungeonSpaceType) {
        dungeonSpaceTypeExclusions.remove(dungeonSpaceType);
    }
    
    public void purgeExclusionTypes() {
        dungeonSpaceTypeExclusions.clear();
    }
    
    public boolean allow(DungeonSpaceType dungeonSpaceType) {
        return !dungeonSpaceTypeExclusions.contains(dungeonSpaceType);
    }
    
    public boolean deny(DungeonSpaceType dungeonSpaceType) {
        return dungeonSpaceTypeExclusions.contains(dungeonSpaceType);
    }
    
}
