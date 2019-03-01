/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon;

import dungeonescape.dungeon.notifications.GameNotification;
import dungeonescape.dungeonobject.mine.FreezeMine;
import dungeonescape.dungeonobject.mine.FreezeTime;
import dungeonescape.dungeonobject.mine.Mine;
import dungeonescape.dungeonobject.mine.TargetBoundaries;
import dungeonescape.dungeonobject.mine.TargetBoundary;
import dungeonescape.dungeonobject.mine.TeleportDestination;
import dungeonescape.dungeonobject.mine.TeleportMine;
import dungeonescape.space.DungeonSpace;
import java.util.ArrayList;
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
public class DungeonConstructionUtil {

    protected static List<DungeonSpace> getOpenSpaces(DungeonSpace[][] dungeon) {

        List<DungeonSpace> openSpaces = new ArrayList<>();
        for (int row = 0; row < dungeon.length; row++) {
            for (int col = 0; col < dungeon.length; col++) {
                if (dungeon[col][row].isEmpty()) {
                    openSpaces.add(dungeon[col][row]);
                }
            }
        }

        return openSpaces;
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

                    dungeon[position[0]][position[1]].clearDungeonObjects();

                    if (position[0] == 0
                            || position[0] == width - 1
                            || position[1] == height - 1
                            || position[1] == 0) {
                        break;
                    }
                }

            }
        }

        return dungeon;
    }
    
    protected static DungeonSpace[][] placeFreezeMines(DungeonSpace[][] dungeon, int numberOfFreezeMines, FreezeTime maxFreezeTime, TargetBoundaries targetBoundaries) throws GameNotification {

        List<Mine> freezeMines = new ArrayList<>();
        for (int i = 0; i < numberOfFreezeMines; i++) {
            int freezeTime = ThreadLocalRandom.current().nextInt(1, maxFreezeTime.getTime() + 1);
            freezeMines.add(new FreezeMine(new FreezeTime(freezeTime, maxFreezeTime.getTimeUnit())));
        }

        return deployMines(dungeon, freezeMines, targetBoundaries);
    }

    protected static DungeonSpace[][] placeTeleportMines(DungeonSpace[][] dungeon, int numberOfTeleportMines, TargetBoundaries targetBoundaries) throws GameNotification {

        List<Mine> teleportMines = new ArrayList<>();
        List<DungeonSpace> openSpaces = DungeonConstructionUtil.getOpenSpaces(dungeon);
        Set<Integer> usedOpenSpaces = new HashSet<>();
        for (int i = 0; i < numberOfTeleportMines; i++) {
            Integer teleportIndex = ThreadLocalRandom.current().nextInt(0, openSpaces.size());
            while (usedOpenSpaces.contains(teleportIndex)) {
                teleportIndex = ThreadLocalRandom.current().nextInt(0, openSpaces.size());
            }

            //provide a teleport location
            DungeonSpace teleportSpace = openSpaces.get(teleportIndex);
            teleportSpace.addDungeonObject(new TeleportDestination());
            teleportMines.add(new TeleportMine(teleportSpace));
            usedOpenSpaces.add(teleportIndex);
        }

        return deployMines(dungeon, teleportMines, targetBoundaries);
    }

    protected static DungeonSpace[][] deployMines(DungeonSpace[][] dungeon, List<Mine> mines, TargetBoundaries targetBoundaries) throws GameNotification {
        Map<Double, List<DungeonSpace>> targetAreas = new HashMap<>();

        List<TargetBoundary> targeBoundaryAreas = targetBoundaries.getTargetBoundaries();

        targeBoundaryAreas.forEach((targetBoundary) -> {
            List<DungeonSpace> emptyDungeonSpacesInTargetArea = new ArrayList<>();
            for (int row = 0; row < dungeon.length; row++) {
                for (int col = 0; col < dungeon.length; col++) {
                    if (inTargetBoundary(row, col, targetBoundary.getMinDistanceFromCenter(), targetBoundary.getMaxDistanceFromCenter(), dungeon.length)
                            && dungeon[col][row].isNotPermanentlyOccupied()) {
                        emptyDungeonSpacesInTargetArea.add(dungeon[col][row]);
                    }
                }
            }
            if (emptyDungeonSpacesInTargetArea.isEmpty()) {
                System.out.println("There are no available spaces available for target area, "
                        + "min: " + targetBoundary.getMinDistanceFromCenter() + " max: " + targetBoundary.getMaxDistanceFromCenter());
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
                entry.getValue().get(index).addDungeonObject(mines.get(mineIndex++));
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

    protected static List<DungeonSpace> determineDungeonExits(DungeonSpace[][] dungeon) {

        List<DungeonSpace> dungeonExits = new ArrayList<>();
        //check the northern boundary
        for (int column = 0; column < dungeon.length; column++) {
            if (dungeon[column][0].isNotPermanentlyOccupied()) {
                dungeonExits.add(dungeon[column][0]);
            }
        }

        //check the eastern boundary
        for (int row = 0; row < dungeon.length; row++) {
            if (dungeon[dungeon.length - 1][row].isNotPermanentlyOccupied()) {
                dungeonExits.add(dungeon[dungeon.length - 1][row]);
            }
        }

        //check the southern boundary
        for (int column = 0; column < dungeon.length; column++) {
            if (dungeon[column][dungeon.length - 1].isNotPermanentlyOccupied()) {
                dungeonExits.add(dungeon[column][dungeon.length - 1]);
            }
        }

        //check the western boundary
        for (int row = 0; row < dungeon.length; row++) {
            if (dungeon[0][row].isNotPermanentlyOccupied()) {
                dungeonExits.add(dungeon[0][row]);
            }
        }

        return dungeonExits;
    }

}
