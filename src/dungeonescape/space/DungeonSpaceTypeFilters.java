/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.space;

import dungeonescape.space.DungeonSpaceType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andrew
 */
public class DungeonSpaceTypeFilters {
    
    private Position playerPosition;
    private int visibilityDistance;
    
    private final List<DungeonSpaceType> dungeonSpaceTypeExclusions = new ArrayList<>();
    
    public void setPlayerVisibility(Position playerPosition, int visibilityDistance) {
        this.playerPosition = playerPosition;
    }
    
    public void clearPlayerVisibility() {
        playerPosition = null;
        visibilityDistance = Integer.MAX_VALUE;
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
