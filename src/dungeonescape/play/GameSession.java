/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.play;

import dungeonescape.dungeon.Dungeon;
import dungeonescape.dungeon.DungeonConfiguration;
import dungeonescape.dungeon.notifications.GameNotification;
import dungeonescape.dungeon.notifications.GameOverNotification;
import dungeonescape.dungeon.notifications.LossNotification;
import dungeonescape.dungeon.notifications.PlayerNotFoundNotification;
import dungeonescape.dungeon.notifications.WinNotification;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
    private boolean won = false;
    private boolean lost = false;

    public GameSession(DungeonConfiguration dungeonConfiguration) throws GameNotification {
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

    public String movePlayer(Direction direction, String playerName) throws GameNotification {

        if (won || lost) {
            String gameResult = won ? "You Won!" : "You lost.";
            throw new GameOverNotification("Cannot move player after game has ended (" + gameResult + ").");
        } else if (!dungeonConfiguration.getPlayerNames().contains(playerName)) {
            throw new PlayerNotFoundNotification("Player " + playerName 
                    + " does not exist. Valid players: " + dungeonConfiguration.getPlayerNames().toString());
        }

        try {
            dungeon.movePlayer(direction, playerName);
            
            Integer turnCount = turnCounts.get(playerName) + 1;
            turnCounts.put(playerName, turnCount);
            
            if (turnCount == dungeonConfiguration.getSpawnDungeonMastersTurnCount()) {
                dungeon.spawnDungeonMasters();
            }
        } catch (GameNotification gameNotification) {
            if (gameNotification instanceof WinNotification) {
                won = true;
            } else if (gameNotification instanceof LossNotification) {
                lost = true;
            }
            throw gameNotification;
        }
        return dungeon.generatePlayerMiniMap(playerName);
    }
    
    public String getPlayerMap(String playerName) throws PlayerNotFoundNotification {
        String miniMap = dungeon.generatePlayerMiniMap(playerName);
//        System.out.println(miniMap);
        return miniMap;
    }

}
