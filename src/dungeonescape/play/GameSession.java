/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.play;

import dungeonescape.dungeon.Dungeon;
import dungeonescape.dungeon.DungeonConfiguration;
import dungeonescape.dungeonobject.DungeonObjectTrack;
import dungeonescape.dungeonobject.FreezeTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Andrew
 */
public class GameSession {

    private final String sessionId = UUID.randomUUID().toString();
    private final DungeonConfiguration dungeonConfiguration;
    private final Dungeon dungeon;

    //Game status
    private Map<String, Integer> turnCounts;

    public GameSession(DungeonConfiguration dungeonConfiguration) {
        this.dungeonConfiguration = dungeonConfiguration;

        dungeon = new Dungeon(dungeonConfiguration);
        turnCounts = new HashMap<>();
        dungeonConfiguration.getPlayerNames().forEach((pName) -> {
            turnCounts.put(pName, 0);
        });

    }

    public DungeonConfiguration getDungeonConfiguration() {
        return dungeonConfiguration;
    }

    public String getSessionId() {
        return sessionId;
    }

    public List<DungeonObjectTrack> movePlayerGui(Direction direction, String playerName) {
        movePlayer(direction, playerName);
        return dungeon.getDungeonObjectTracks();
    }

    public String movePlayerHeadless(Direction direction, String playerName) {
        movePlayer(direction, playerName);
        return dungeon.generatePlayerMap();
    }

    private void movePlayer(Direction direction, String playerName) {
        dungeon.movePlayer(direction, playerName);

        Integer turnCount = turnCounts.get(playerName) + 1;
        turnCounts.put(playerName, turnCount);

        if (turnCount == dungeonConfiguration.getSpawnDungeonMastersTurnCount()) {
            dungeon.spawnDungeonMasters();
        }
    }

    public String getPlayerStats(String playerName) {
        StringBuilder playerStats = new StringBuilder("Stats for ").append(playerName).append(":\n")
                .append("Dungeon time: ").append(dungeon.getDungeonTimeElapsed()).append("\n")
                .append("Frozen time remaining: ");
        FreezeTime freezeTime = dungeon.getPlayer(playerName).getFrozenTimeRemaining();
        playerStats.append(freezeTime.getFreezeTimeForTimeUnit(TimeUnit.MINUTES))
                .append(" ").append(TimeUnit.MINUTES)
                .append("\n");
        return playerStats.toString();
    }

    public String getPlayerMap() {
        String miniMap = dungeon.generatePlayerMap();
//        System.out.println(miniMap);
        return miniMap;
    }

}
