/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon;

import dungeonescape.dungeon.notifications.GameNotification;
import dungeonescape.dungeonobject.characters.DungeonCharacter;
import dungeonescape.dungeonobject.characters.Ghost;
import dungeonescape.dungeonobject.characters.Guard;
import dungeonescape.space.DungeonSpace;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Andrew
 */
public class DungeonCharacterUtil {

    /**
     * Guards start at random dungeon entrances. If there are more guards than
     * entrances then some entrances will have multiple guards. Guards patrol 2
     * random spaces per turn along the open spaces and will chase the player
     * when within 10 spaces (including the guard's current space). Guards are
     * unaffected by mines but cannot walk through walls.
     *
     * @param dungeon
     * @param numberOfGuardsToPlace
     * @return
     * @throws dungeonescape.dungeon.notifications.GameNotification
     */
    protected static List<Guard> placeGuards(DungeonSpace[][] dungeon, int numberOfGuardsToPlace) throws GameNotification {

        List<Guard> guards = new ArrayList<>();
        List<DungeonSpace> dungeonExits = DungeonConstructionUtil.determineDungeonExits(dungeon);

        //place guards at the exits
        Set<Integer> usedSpaces = new HashSet<>();
        for (int guardNumber = 0; guardNumber < numberOfGuardsToPlace; guardNumber++) {
            Integer dungeonExitNumber = ThreadLocalRandom.current().nextInt(0, dungeonExits.size());
            while (usedSpaces.contains(dungeonExitNumber)) {
                dungeonExitNumber = ThreadLocalRandom.current().nextInt(0, dungeonExits.size());
            }

            DungeonSpace dungeonExitSpace = dungeonExits.get(dungeonExitNumber);

            Guard guard = new Guard();
            dungeonExitSpace.addDungeonObject(guard);
            guards.add(guard);
            System.out.println("Placed guard at ["
                    + dungeonExitSpace.getPosition().getPositionX() + ","
                    + dungeonExitSpace.getPosition().getPositionY() + "]");

            usedSpaces.add(dungeonExitNumber);

            if (guards.size() == dungeonExits.size()) {
                usedSpaces.clear();
                break;
            }

        }

        //place remaining guards on random empty map spaces
        if (guards.size() < numberOfGuardsToPlace) {

            List<DungeonSpace> emptyDungeonSpaces = DungeonConstructionUtil.getOpenSpaces(dungeon);
            Integer emptyDungeonSpaceNumber = ThreadLocalRandom.current().nextInt(0, emptyDungeonSpaces.size());
            while (guards.size() < numberOfGuardsToPlace) {
                while (usedSpaces.contains(emptyDungeonSpaceNumber)) {
                    emptyDungeonSpaceNumber = ThreadLocalRandom.current().nextInt(0, emptyDungeonSpaces.size());
                }
                Guard guard = new Guard();
                DungeonSpace dungeonSpace = emptyDungeonSpaces.get(emptyDungeonSpaceNumber);
                dungeonSpace.addDungeonObject(guard);
                guards.add(guard);
                usedSpaces.add(emptyDungeonSpaceNumber);
                System.out.println("Placed guard at ["
                        + dungeonSpace.getPosition().getPositionX() + ","
                        + dungeonSpace.getPosition().getPositionY() + "]");
            }
        }

        System.out.println("Placed " + guards.size() + " guards.");
        return guards;
    }

    /**
     * Ghosts are distributed evenly along the outside of the map and are
     * allowed to wander through walls. Ghosts are unaffected by all dungeon
     * objects and will chase the player when within 50 spaces (includes the
     * current space of the ghost).
     *
     * @param dungeon
     * @param numberOfGhostsToPlace
     * @return
     * @throws dungeonescape.dungeon.notifications.GameNotification
     */
    protected static List<Ghost> placeGhosts(DungeonSpace[][] dungeon,
            int numberOfGhostsToPlace, int offsetFromBorder) throws GameNotification {

        //place ghosts
        int numberOfGhostsPerBorder = numberOfGhostsToPlace / 4;

        //if numberOfGhostsPerBorder == 0 then there are less than 4 ghosts
        //Randomly place ghosts along each border starting at the northern
        //border and working clockwise. Excess ghosts will be placed along the
        //western border.
        List<Ghost> ghosts = new ArrayList<>();
        for (int borderNumber = 0; borderNumber < 4; borderNumber++) {

            int row, col;
            int numberOfGhostsToPlaceOnThisBorder;
            if (numberOfGhostsPerBorder == 0) {
                numberOfGhostsToPlaceOnThisBorder = numberOfGhostsToPlace - borderNumber;
            } else {
                if (borderNumber < 3) {
                    numberOfGhostsToPlaceOnThisBorder = numberOfGhostsPerBorder;
                } else {
                    numberOfGhostsToPlaceOnThisBorder = numberOfGhostsToPlace - ghosts.size();
                }
            }

            //northern border
            if (borderNumber == 0) {
                ghosts = placeGhosts(offsetFromBorder, null, dungeon, numberOfGhostsToPlaceOnThisBorder, ghosts);
            }

            //eastern border
            if (borderNumber == 1) {
                col = (dungeon.length - 1) - offsetFromBorder;
                ghosts = placeGhosts(null, col, dungeon, numberOfGhostsToPlaceOnThisBorder, ghosts);
            }

            //southern border
            if (borderNumber == 2) {
                row = (dungeon.length - 1) - offsetFromBorder;
                ghosts = placeGhosts(row, null, dungeon, numberOfGhostsToPlaceOnThisBorder, ghosts);
            }

            //western border
            if (borderNumber == 3) {
                ghosts = placeGhosts(null, offsetFromBorder, dungeon, numberOfGhostsToPlaceOnThisBorder, ghosts);
            }

            if (ghosts.size() == numberOfGhostsToPlace) {
                break;
            }
        }

        System.out.println("Placed " + ghosts.size() + " ghosts.");
        return ghosts;
    }

    private static List<Ghost> placeGhosts(Integer row, Integer col, DungeonSpace[][] dungeon,
            int numberOfGhostsToPlaceOnThisBorder, List<Ghost> ghosts) throws GameNotification {

        boolean selectRow = row == null;
        for (int ghostNumber = 0; ghostNumber < numberOfGhostsToPlaceOnThisBorder; ghostNumber++) {
            if (selectRow) {
                row = ThreadLocalRandom.current().nextInt(0, dungeon.length);
            } else {
                col = ThreadLocalRandom.current().nextInt(0, dungeon.length);
            }
            DungeonSpace dungeonBorderSpace = dungeon[col][row];
            while (dungeonBoardSpaceIsOccupiedByAnotherCharacter(dungeonBorderSpace)) {
                if (selectRow) {
                    row = ThreadLocalRandom.current().nextInt(0, dungeon.length);
                } else {
                    col = ThreadLocalRandom.current().nextInt(0, dungeon.length);
                }
                dungeonBorderSpace = dungeon[col][row];
            }
            Ghost ghost = new Ghost();
            dungeonBorderSpace.addDungeonObject(ghost);
            ghosts.add(ghost);
        }
        return ghosts;
    }

    private static boolean dungeonBoardSpaceIsOccupiedByAnotherCharacter(DungeonSpace dungeonSpace) {
        return !dungeonSpace.getDungeonObjects().stream()
                .noneMatch(dungeonObject -> dungeonObject instanceof DungeonCharacter);
    }

}
