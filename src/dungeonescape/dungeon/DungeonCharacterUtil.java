/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon;

import dungeonescape.dungeonobject.FreezeTime;
import dungeonescape.dungeonobject.characters.DungeonCharacter;
import dungeonescape.dungeonobject.characters.DungeonMaster;
import dungeonescape.dungeonobject.characters.Ghost;
import dungeonescape.dungeonobject.characters.Guard;
import dungeonescape.dungeon.space.DungeonSpace;
import dungeonescape.dungeonobject.characters.Player;
import java.util.ArrayList;
import java.util.Collections;
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
     * Guards start at random dungeon entrances. If there are more guards than entrances then some entrances will have
     * multiple guards. Guards patrol 2 random spaces per turn along the open spaces and will chase the player when
     * within 10 spaces (including the guard's current space). Guards are unaffected by mines but cannot walk through
     * walls.
     *
     * @param dungeon
     * @param numberOfGuardsToPlace
     * @param player
     * @return
     * @throws dungeonescape.dungeon.notifications.GameNotification
     */
    protected static List<Guard> placeGuards(DungeonSpace[][] dungeon, int numberOfGuardsToPlace, Player player) {

        if (numberOfGuardsToPlace < 1) {
            return Collections.emptyList();
        }

        List<Guard> guards = new ArrayList<>();
        List<DungeonSpace> dungeonExits = DungeonConstructionUtil.determineDungeonExits(dungeon);
        DungeonSpace jailCellSpace = dungeon[dungeon.length / 2][dungeon.length / 2];
        //place guards at the exits
        Set<Integer> usedSpaces = new HashSet<>();
        for (int guardNumber = 0; guardNumber < numberOfGuardsToPlace; guardNumber++) {
            Integer dungeonExitNumber = ThreadLocalRandom.current().nextInt(0, dungeonExits.size());
            while (usedSpaces.contains(dungeonExitNumber)) {
                dungeonExitNumber = ThreadLocalRandom.current().nextInt(0, dungeonExits.size());
            }

            DungeonSpace dungeonExitSpace = dungeonExits.get(dungeonExitNumber);
            Guard guard = new Guard(jailCellSpace, dungeon, player);
            dungeonExitSpace.addDungeonObject(guard);
            guards.add(guard);

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
                Guard guard = new Guard(jailCellSpace, dungeon, player);
                DungeonSpace dungeonSpace = emptyDungeonSpaces.get(emptyDungeonSpaceNumber);
                dungeonSpace.addDungeonObject(guard);
                guards.add(guard);
                usedSpaces.add(emptyDungeonSpaceNumber);
            }
        }

        return guards;
    }

    /**
     * Ghosts are distributed evenly along the outside of the map and are allowed to wander through walls. Ghosts are
     * unaffected by all dungeon objects and will chase the player when within 50 spaces (includes the current space of
     * the ghost).
     *
     * @param dungeon
     * @param numberOfGhostsToPlace
     * @param ghostMinFreezeTime
     * @param ghostMaxFreezeTime
     * @param offsetFromBorder
     * @param player
     * @return
     * @throws dungeonescape.dungeon.notifications.GameNotification
     */
    protected static List<Ghost> placeGhosts(DungeonSpace[][] dungeon, int numberOfGhostsToPlace,
        FreezeTime ghostMinFreezeTime, FreezeTime ghostMaxFreezeTime, int offsetFromBorder, Player player) {

        if (numberOfGhostsToPlace < 1) {
            return Collections.emptyList();
        }

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

            switch (borderNumber) {
                case 0://northern border
                    ghosts = placeGhosts(offsetFromBorder, null, dungeon, numberOfGhostsToPlaceOnThisBorder,
                        ghostMinFreezeTime, ghostMaxFreezeTime, ghosts, player);
                    break;
                case 1://eastern border
                    col = (dungeon.length - 1) - offsetFromBorder;
                    ghosts = placeGhosts(null, col, dungeon, numberOfGhostsToPlaceOnThisBorder,
                        ghostMinFreezeTime, ghostMaxFreezeTime, ghosts, player);
                    break;
                case 2://southern border
                    row = (dungeon.length - 1) - offsetFromBorder;
                    ghosts = placeGhosts(row, null, dungeon, numberOfGhostsToPlaceOnThisBorder,
                        ghostMinFreezeTime, ghostMaxFreezeTime, ghosts, player);
                    break;
                case 3://western border
                    ghosts = placeGhosts(null, offsetFromBorder, dungeon, numberOfGhostsToPlaceOnThisBorder,
                        ghostMinFreezeTime, ghostMaxFreezeTime, ghosts, player);
                    break;
            }

            if (ghosts.size() == numberOfGhostsToPlace) {
                break;
            }
        }

        return ghosts;
    }

    private static List<Ghost> placeGhosts(Integer row, Integer col, DungeonSpace[][] dungeon,
        int numberOfGhostsToPlaceOnThisBorder, FreezeTime ghostMinFreezeTime, FreezeTime ghostMaxFreezeTime,
        List<Ghost> ghosts, Player player) {

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
            int freezeTime
                = ThreadLocalRandom.current().nextInt(ghostMinFreezeTime.getTurns(), ghostMaxFreezeTime.getTurns() + 1);
            Ghost ghost = new Ghost(new FreezeTime(freezeTime), dungeon, player);
            dungeonBorderSpace.addDungeonObject(ghost);
            ghosts.add(ghost);
        }
        return ghosts;
    }

    private static boolean dungeonBoardSpaceIsOccupiedByAnotherCharacter(DungeonSpace dungeonSpace) {
        return !dungeonSpace.getDungeonObjects().stream()
            .noneMatch(dungeonObject -> dungeonObject instanceof DungeonCharacter);
    }

    protected static List<DungeonMaster> placeDungeonMasters(DungeonSpace[][] dungeon,
        int numberOfDungeonMasters, Player player) {

        if (numberOfDungeonMasters < 1) {
            return Collections.emptyList();
        }

        int center = dungeon.length / 2;

        List<DungeonMaster> dungeonMasters = new ArrayList<>();

        //First dungeon master will spawn at the center of the map
        DungeonMaster dungeonMaster = new DungeonMaster(player);
        dungeon[center][center].addDungeonObject(dungeonMaster);
        dungeonMasters.add(dungeonMaster);

        //The remainder of the dungeon masters will be placed randomly throughout the map
        int numberOfRemainingDungeonMasters = numberOfDungeonMasters - 1;
        List<DungeonSpace> availableDungeonSpaces = DungeonConstructionUtil.getOpenSpaces(dungeon);
        Set<Integer> usedSpaces = new HashSet<>();
        for (int remainingDungeonMaster = 0; remainingDungeonMaster < numberOfRemainingDungeonMasters; remainingDungeonMaster++) {
            Integer position = ThreadLocalRandom.current().nextInt(0, availableDungeonSpaces.size());
            while (usedSpaces.contains(position)) {
                position = ThreadLocalRandom.current().nextInt(0, availableDungeonSpaces.size());
            }
            dungeonMaster = new DungeonMaster(player);
            availableDungeonSpaces.get(position).addDungeonObject(dungeonMaster);
            dungeonMasters.add(dungeonMaster);
            usedSpaces.add(position);
        }

        return dungeonMasters;
    }

}
