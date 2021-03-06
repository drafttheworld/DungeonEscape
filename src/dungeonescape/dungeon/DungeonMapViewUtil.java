/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon;

import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.dungeonobject.characters.Player;
import dungeonescape.dungeon.space.DungeonSpace;
import dungeonescape.dungeon.space.DungeonSpaceType;
import dungeonescape.dungeon.space.DungeonSpaceTypeFilters;

/**
 *
 * @author Andrew
 */
public class DungeonMapViewUtil {

    protected static String getFullDungeonAsString(DungeonSpace[][] dungeon, DungeonSpaceTypeFilters dungeonSpaceTypeFilters) {
        //Convert the maze to a string
        StringBuilder dungeonAsString = new StringBuilder();
        for (int row = 0; row < dungeon.length; row++) {
            for (int col = 0; col < dungeon.length; col++) {
                if (dungeon[row][col].isEmpty()) {
                    dungeonAsString.append(DungeonSpaceType.OPEN_SPACE.getValue());
                } else {
                    DungeonSpaceType dungeonSpaceType
                        = dungeon[row][col].getVisibleDungeonObject().getDungeonSpaceType();
                    if (dungeonSpaceTypeFilters == null) {
                        dungeonAsString.append(dungeonSpaceType.getValue());
                    } else if (dungeonSpaceTypeFilters.allow(dungeonSpaceType)) {
                        dungeonAsString.append(dungeonSpaceType.getValue());
                    } else {
                        dungeonAsString.append(DungeonSpaceType.OPEN_SPACE.getValue());
                    }
                }
                if (col == dungeon.length - 1 && row < dungeon.length - 1) {
                    dungeonAsString.append("\n");
                }
            }
        }

        return dungeonAsString.toString();
    }

    protected static String getPlayerMap(DungeonSpace[][] dungeon) {
        StringBuilder playerMap = new StringBuilder();
        for (int row = 0; row < dungeon.length; row++) {
            for (int col = 0; col < dungeon.length; col++) {
                char visibleSymbol;
                if (dungeon[row][col].isVisible()) {
                    visibleSymbol = dungeon[row][col].getVisibleDungeonObject().getDungeonSpaceType().getValue();
                } else {
                    visibleSymbol = DungeonSpaceType.NON_VISIBLE_SPACE.getValue();
                }
                playerMap.append(visibleSymbol);
            }
            if (row < dungeon.length - 1) {
                playerMap.append("\n");
            }
        }
        return playerMap.toString();
    }

}
