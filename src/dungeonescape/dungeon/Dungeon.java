/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon;

import dungeonescape.dungeon.notifications.ActionNotAllowedNotification;
import dungeonescape.dungeon.notifications.ExecutionErrorNotification;
import dungeonescape.dungeon.notifications.GameOverNotification;
import dungeonescape.dungeon.notifications.NotificationManager;
import dungeonescape.dungeonobject.characters.DungeonCharacter;
import dungeonescape.dungeonobject.characters.DungeonMaster;
import dungeonescape.dungeonobject.characters.Ghost;
import dungeonescape.dungeonobject.characters.Guard;
import dungeonescape.dungeonobject.characters.Player;
import dungeonescape.dungeonobject.construction.Wall;
import dungeonescape.dungeonobject.mine.Mine;
import dungeonescape.dungeonobject.mine.TargetBoundaries;
import dungeonescape.dungeonobject.mine.TargetBoundary;
import dungeonescape.play.Direction;
import dungeonescape.dungeon.space.DungeonSpace;
import dungeonescape.dungeon.space.Position;
import dungeonescape.dungeonobject.characters.NonPlayerCharacter;
import dungeonescape.dungeonobject.powerups.PowerUpService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 *
 * @author Andrew
 */
public class Dungeon {

    private final ExecutorService executorService
        = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private final List<NonPlayerCharacter> nonPlayerCharacters;
    private final DungeonConfiguration dungeonConfiguration;
    private final PowerUpService powerUpService;
    private final ScheduledExecutorService npcScheduledExecutorService
        = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    private Player player;
    private DungeonSpace[][] dungeon;
    private int numberOfOpenDungeonSpaces;
    private Instant playStartTime;

    public Dungeon(DungeonConfiguration dungeonConfiguration, PowerUpService powerUpService) {
        this.dungeonConfiguration = dungeonConfiguration;
        this.powerUpService = powerUpService;

        nonPlayerCharacters = new ArrayList<>();
        dungeon = generateDungeon();
    }

    /**
     * Maze will be a square with height equal to width.
     *
     * @return
     */
    private DungeonSpace[][] generateDungeon() {

        int width = dungeonConfiguration.getDungeonWidth();
        int height = width;

        dungeon = new DungeonSpace[width][width];

        int centerX = width / 2;
        int centerY = height / 2;

        // Fill the maze
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                dungeon[row][col] = new DungeonSpace(new Position(col, row));
                dungeon[row][col].addDungeonObject(new Wall());
            }
        }

        // Cut out the exit paths
        dungeon = DungeonConstructionUtil.cutExitPaths(dungeon, dungeonConfiguration.getDungeonExitCount());

        // Create and place the player at the center of the dungeon
        dungeon[centerX][centerY].clearDungeonObjects();
        String playerName = dungeonConfiguration.getPlayerName();
        player = new Player(playerName, dungeonConfiguration.getPlayerVisibility(), dungeon, powerUpService,
            dungeonConfiguration.getPowerUpDurationTurns());
        dungeon[centerX][centerY].addDungeonObject(player);
        player.revealCurrentMapArea();
        powerUpService.registerPowerUpClient(player);

        numberOfOpenDungeonSpaces = DungeonConstructionUtil.getOpenSpaces(dungeon).size();
        System.out.println("Number of open dungeon spaces: " + numberOfOpenDungeonSpaces);

        // Define the target boundaries for the mines
        // target boundaries provide a way to specify a percentage of the mines to
        // be placed at a certain distance ranges away from the center of the map.
        TargetBoundaries targetBoundaries = new TargetBoundaries();
        targetBoundaries.addTargetBoundary(new TargetBoundary(1, width / 4, .6));
        targetBoundaries.addTargetBoundary(new TargetBoundary(width / 4 + 1, width / 2, .4));

        // Lay the freeze mines
        List<Mine> freezeMines = DungeonConstructionUtil.placeFreezeMines(dungeon,
            dungeonConfiguration.getFreezeMineCount(), dungeonConfiguration.getFreezeMineMinFreezeTime(),
            dungeonConfiguration.getFreezeMineMaxFreezeTime(), targetBoundaries);
        System.out.println("Layed " + freezeMines.size() + " freeze mines.");

        // Lay the teleport mines
        List<Mine> teleportMines = DungeonConstructionUtil.placeTeleportMines(dungeon,
            dungeonConfiguration.getTeleportMineCount(), targetBoundaries);
        System.out.println("Layed " + teleportMines.size() + " teleport mines.");

        // Place the mystery boxes (should not occupy the same space as a mine)
        DungeonConstructionUtil.placePowerUpBoxes(dungeon, dungeonConfiguration.getPowerUpBoxCount(),
            dungeonConfiguration.getInvincibilityProbability(), dungeonConfiguration.getInvisibilityProbability(),
            dungeonConfiguration.getRepellentProbability(), dungeonConfiguration.getTerminatorProbability());

        // Place coins (should not occupy the same space as a mine)
        DungeonConstructionUtil.placeCoins(dungeon, dungeonConfiguration.getCoinCoveragePercentOfOpenSpaces(),
            dungeonConfiguration.getCoinCountOverride());

        // Place the guards
        List<Guard> guards = DungeonCharacterUtil.placeGuards(dungeon, dungeonConfiguration.getGuardCount(), player);
        guards.forEach(guard -> {
            guard.setDetectionDistance(dungeonConfiguration.getGuardDetectionDistance());
            guard.setNumberOfSpacesToMoveWhenHunting(dungeonConfiguration.getGuardNumberOfMovesWhenHunting());
            guard.setNumberOfSpacesToMoveWhenPatrolling(dungeonConfiguration.getGuardNumberOfMovesWhenPatrolling());
            guard.setMovementRateMs(dungeonConfiguration.getGuardMovementRateMs());
        });
        nonPlayerCharacters.addAll(guards);
        System.out.println("Added " + guards.size() + " guards.");

        // Place the ghosts, offset 1/4 of the total dungeon width from the border
        int borderOffset = dungeon.length / 4;
        List<Ghost> ghosts = DungeonCharacterUtil.placeGhosts(dungeon,
            dungeonConfiguration.getGhostCount(), dungeonConfiguration.getGhostMinFreezeTime(),
            dungeonConfiguration.getGhostMaxFreezeTime(), borderOffset, player);
        ghosts.forEach(ghost -> {
            ghost.setDetectionDistance(dungeonConfiguration.getGhostDetectionDistance());
            ghost.setNumberOfSpacesToMoveWhenHunting(dungeonConfiguration.getGhostNumberOfMovesWhenHunting());
            ghost.setNumberOfSpacesToMoveWhenPatrolling(dungeonConfiguration.getGhostNumberOfMovesWhenPatrolling());
            ghost.setMovementRateMs(dungeonConfiguration.getGhostMovementRateMs());
        });
        nonPlayerCharacters.addAll(ghosts);
        System.out.println("Added " + ghosts.size() + " ghosts.");

        npcScheduledExecutorService.scheduleWithFixedDelay(() -> cleanupInactiveDungeonObjects(), 1, 1, TimeUnit.MINUTES);

//        nonPlayerCharacters.forEach(nonPlayerCharacter -> {
//            long movementRateMs = nonPlayerCharacter.getMovementRateMs();
//            npcScheduledExecutorService.scheduleWithFixedDelay(nonPlayerCharacter,
//                movementRateMs, movementRateMs, TimeUnit.MILLISECONDS);
//        });
        return dungeon;
    }

    public Set<DungeonSpace> movePlayer(Direction direction) {

        if (playStartTime == null) {
            playStartTime = Instant.now();
        }

        //check to see if the player has been caught by a dungeon master
        if (player.hasWon() || player.hasLost()) {
            String gameResult = player.hasWon() ? "You Won!" : "You lost.";
            NotificationManager.notify(
                new GameOverNotification("Cannot move player after game has ended (" + gameResult + ")."));
            return Collections.emptySet();
        }

        Set<DungeonSpace> dungeonSpacesToUpdate = new HashSet<>(nonPlayerCharacters.size());

        if (!player.isFrozen()) {
            try {
                List<DungeonSpace> playerDungeonSpaces = player.move(direction);
                if (playerDungeonSpaces.isEmpty()) {
                    return Collections.emptySet();
                }

                dungeonSpacesToUpdate.addAll(playerDungeonSpaces);
            } catch (UnsupportedOperationException e) {
                e.printStackTrace();
                NotificationManager.notify(new ActionNotAllowedNotification(e.getMessage()));
                return Collections.emptySet();
            }
        } else if (player.getFrozenTurnsRemaining().getTurns() > 0) {
            player.decrementFrozenTurnsRemaining(1);
        }

        dungeonSpacesToUpdate.addAll(moveNonPlayerCharacters());

        return dungeonSpacesToUpdate;
    }

    private void cleanupInactiveDungeonObjects() {

        Iterator<NonPlayerCharacter> npcIterator = nonPlayerCharacters.iterator();
        while (npcIterator.hasNext()) {
            DungeonCharacter npc = npcIterator.next();
            if (!npc.isActive()) {
                npcIterator.remove();
            }
        }
    }

    private Set<DungeonSpace> moveNonPlayerCharacters() {

        List<Future<List<DungeonSpace>>> futures = new ArrayList<>();
        for (DungeonCharacter dungeonCharacter : nonPlayerCharacters) {
            try {
                Future<List<DungeonSpace>> future
                    = executorService.submit(() -> dungeonCharacter.move(dungeon, player));
                futures.add(future);
            } catch (RuntimeException e) {
                NotificationManager.notify(new ExecutionErrorNotification(e.getMessage()));
            }
        }

        return futures.parallelStream()
            .map((Future<List<DungeonSpace>> future) -> {
                try {
                    return future.get();
                } catch (InterruptedException | ExecutionException e) {
                    return Collections.emptyList();
                }
            })
            .flatMap(List::stream)
            .map(dungeonSpace -> (DungeonSpace) dungeonSpace)
            .filter(dungeonSpace -> dungeonSpace.isVisible())
            .collect(Collectors.toCollection(ConcurrentSkipListSet::new));
    }

    public List<DungeonSpace> spawnDungeonMasters() {

        List<DungeonMaster> dungeonMasters
            = DungeonCharacterUtil.placeDungeonMasters(dungeon, dungeonConfiguration.getDungeonMasterCount(), player);

        nonPlayerCharacters.addAll(dungeonMasters);

        return dungeonMasters.stream()
            .map(DungeonMaster::getDungeonSpace)
            .filter(DungeonSpace::isVisible)
            .collect(Collectors.toList());
    }

    public String generatePlayerMap() {
        return DungeonMapViewUtil.getPlayerMap(dungeon);

        // use the below line to a full view of the exposed dungeon
//        return DungeonMapViewUtil.getFullDungeonAsString(dungeon, null);
    }

    public DungeonSpace[][] getDungeon() {
        return dungeon;
    }

    public int getNumberOfOpenSpaces() {
        return numberOfOpenDungeonSpaces;
    }

    public Player getPlayer() {
        return player;
    }
}
