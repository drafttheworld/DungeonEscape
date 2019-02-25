/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon;

import dungeonescape.dungeonobject.mine.Mine;
import dungeonescape.dungeonobject.mine.TargetBoundaries;
import dungeonescape.dungeonobject.mine.TargetBoundary;
import dungeonescape.space.DungeonSpace;
import dungeonescape.space.DungeonSpaceType;
import dungeonescape.space.DungeonSpaceTypeFilters;
import dungeonescape.space.Position;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Andrew
 */
public class DungeonUtil {

    protected static List<DungeonSpace> getOpenSpaces(DungeonSpace[][] dungeon) {

        List<DungeonSpace> openSpaces = new ArrayList<>();
        for (int row = 0; row < dungeon.length; row++) {
            for (int col = 0; col < dungeon.length; col++) {
                if (dungeon[col][row].getDungeonSpaceType() == DungeonSpaceType.OPEN_SPACE) {
                    openSpaces.add(dungeon[col][row]);
                }
            }
        }

        return openSpaces;
    }

    protected static String getFullDungeonAsString(DungeonSpace[][] dungeon, DungeonSpaceTypeFilters dungeonSpaceTypeFilters) {
        //Convert the maze to a string
        StringBuilder dungeonAsString = new StringBuilder();
        for (int h = 0; h < dungeon.length; h++) {
            for (int w = 0; w < dungeon.length; w++) {
                if (dungeonSpaceTypeFilters == null) {
                    dungeonAsString.append(dungeon[w][h].getSpaceSymbol());
                } else if (dungeonSpaceTypeFilters.allow(dungeon[w][h].getDungeonSpaceType())) {
                    dungeonAsString.append(dungeon[w][h].getSpaceSymbol());
                } else {
                    dungeonAsString.append(DungeonSpaceType.OPEN_SPACE.getValue());
                }
                if (w == dungeon.length - 1 && h < dungeon.length - 1) {
                    dungeonAsString.append("\n");
                }
            }
        }

        return dungeonAsString.toString();
    }

    protected static String getFullPlayerDungeonAsString(DungeonSpace[][] dungeon, Position playerPosition, int playerVisibility) {
        DungeonSpaceTypeFilters dungeonSpaceTypeFilters = new DungeonSpaceTypeFilters();
        dungeonSpaceTypeFilters.setPlayerVisibility(playerPosition, playerVisibility);
        dungeonSpaceTypeFilters.addExclusionType(DungeonSpaceType.DUNGEON_MASTER);
        dungeonSpaceTypeFilters.addExclusionType(DungeonSpaceType.FREEZE_MINE);
        dungeonSpaceTypeFilters.addExclusionType(DungeonSpaceType.GHOST);
        dungeonSpaceTypeFilters.addExclusionType(DungeonSpaceType.TELEPORT_MINE);

        return getFullDungeonAsString(dungeon, dungeonSpaceTypeFilters);
    }

    protected static DungeonSpace[][] cutExitPaths(DungeonSpace[][] dungeon, int exitCount) {
        int width = dungeon.length;
        int height = width;

        int startX = width / 2;
        int startY = startX;

        for (int pathNumber = 0; pathNumber < exitCount; pathNumber++) {

            int[] position = {startX, startY};

            while (position[0] > 0
                    && position[0] < width - 1
                    && position[1] < height - 1
                    && position[1] > 0) {

                //1 = up
                //2 = right
                //3 = down
                //4 = left
                Integer movementDirection = ThreadLocalRandom.current().nextInt(1, 5);
                Integer numberOfMoves = ThreadLocalRandom.current().nextInt(3, 11);

                for (int i = 0; i < numberOfMoves; i++) {
                    switch (movementDirection) {
                        case 1:
                            position[1] = position[1] - 1;
                            break;
                        case 2:
                            position[0] = position[0] + 1;
                            break;
                        case 3:
                            position[1] = position[1] + 1;
                            break;
                        default:
                            position[0] = position[0] - 1;
                            break;
                    }

                    DungeonSpace dungeonSpace = dungeon[position[0]][position[1]];
                    if (dungeonSpace.getDungeonSpaceType() != DungeonSpaceType.PLAYER) {
                        dungeon[position[0]][position[1]] = new DungeonSpace(dungeonSpace.getPosition());
                    }

                    if (position[0] == 0
                            || position[0] == width - 1
                            || position[1] == height - 1
                            || position[1] == 0) {
                        System.out.println("Exit at: " + Arrays.toString(position));
                        break;
                    }
                }

            }
        }

        return dungeon;
    }

    protected static DungeonSpace[][] deployMines(DungeonSpace[][] dungeon, List<Mine> mines, TargetBoundaries targetBoundaries) {
        Map<Double, List<DungeonSpace>> targetAreas = new HashMap<>();

        List<TargetBoundary> targeBoundaryAreas = targetBoundaries.getTargetBoundaries();

        targeBoundaryAreas.forEach((targetBoundary) -> {
            List<DungeonSpace> emptyDungeonSpacesInTargetArea = new ArrayList<>();
            for (int row = 0; row < dungeon.length; row++) {
                for (int col = 0; col < dungeon.length; col++) {
                    if (inTargetBoundary(row, col, targetBoundary.getMinDistanceFromCenter(), targetBoundary.getMaxDistanceFromCenter(), dungeon.length)
                            && dungeon[col][row].getDungeonSpaceType() == DungeonSpaceType.OPEN_SPACE) {
                        emptyDungeonSpacesInTargetArea.add(dungeon[col][row]);
                    }
                }
            }
            if (emptyDungeonSpacesInTargetArea.isEmpty()) {
                System.out.println("There are no available spaces available for target area, "
                        + "min: "+targetBoundary.getMinDistanceFromCenter() + " max: "+targetBoundary.getMaxDistanceFromCenter());
            } else {
            targetAreas.put(targetBoundary.getTargetPercentage(), emptyDungeonSpacesInTargetArea);
            }
        });

        int mineIndex = 0;
        int minesPlaced = 0;
        for (Map.Entry<Double, List<DungeonSpace>> entry : targetAreas.entrySet()) {
            if (entry.getValue().isEmpty()) {
                continue;
            }

            Set<Integer> usedIndices = new HashSet<>();
            int numberOfMinesToPlace = (int) (mines.size() * entry.getKey());
            for (int i = 0; i < numberOfMinesToPlace; i++) {
                int index = ThreadLocalRandom.current().nextInt(0, entry.getValue().size());
                while (usedIndices.contains(index)) {
                    index = ThreadLocalRandom.current().nextInt(0, entry.getValue().size());
                }
                entry.getValue().get(index).setDungeonObject(mines.get(mineIndex++));
                minesPlaced++;
                usedIndices.add(index);
            }
        }
        System.out.println("Placed " + minesPlaced + " mines of type: " + mines.get(0).getDungeonSpaceType().name());
        return dungeon;
    }

    private static boolean inTargetBoundary(int row, int col, int minTargetBoundary, int maxTargetBoundary, int dungeonWidth) {
        int center = dungeonWidth / 2;
        int minNorthBoundary = center - minTargetBoundary;
        int maxNorthBoundary = center - maxTargetBoundary;
        int minEastBoundary = center + minTargetBoundary;
        int maxEastBoundary = center + maxTargetBoundary;
        int minSouthBoundary = center + minTargetBoundary;
        int maxSouthBoundary = center + maxTargetBoundary;
        int minWestBoundary = center - minTargetBoundary;
        int maxWestBoundary = center - maxTargetBoundary;
        
        boolean rowInRange = (row <= minNorthBoundary && row >= maxNorthBoundary)
                || (row >= minSouthBoundary && row <= maxSouthBoundary);
        boolean colInRange = (col >= minEastBoundary && col <= maxEastBoundary)
                || (col <= minWestBoundary && col >= maxWestBoundary);
        
        return rowInRange || colInRange;
    }

}
