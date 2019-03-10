/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape;

import dungeonescape.dungeon.DungeonConfiguration;
import dungeonescape.dungeon.gui.DungeonEscapeGUI;
import dungeonescape.dungeon.notifications.GameNotification;
import dungeonescape.play.DungeonSize;
import dungeonescape.play.GameDifficulty;
import dungeonescape.play.GameSession;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Andrew
 */
public class DungeonEscapeApplication {

    private final Map<String, GameSession> gameSessions = new HashMap<>();

    public GameSession startNewCustomGame(DungeonConfiguration dungeonConfiguration) throws GameNotification {
        GameSession gameSession = new GameSession(dungeonConfiguration);
        gameSessions.put(gameSession.getSessionId(), gameSession);
        return gameSession;
    }

    //TODO: Create specific configurations
    public GameSession startNewGame(String playerName, GameDifficulty gameDifficulty,
            DungeonSize dungeonSize) throws GameNotification {
        
        DungeonConfiguration dungeonConfiguration = gameDifficulty.getDungeonConfiguration()
                .playerName(playerName)
                .dungeonWidth(dungeonSize.getDungeonWidth());
        GameSession gameSession = new GameSession(dungeonConfiguration);
        gameSessions.put(gameSession.getSessionId(), gameSession);
        return gameSession;
    }

    public GameSession getGameSession(String gameSessionId) {
        return gameSessions.get(gameSessionId);
    }

    public void deleteGameSession(String gameSessionId) {
        gameSessions.remove(gameSessionId);
    }

    public void deleteAllGameSessions() {
        gameSessions.clear();
    }

    public static void main(String[] args) throws GameNotification {
        new DungeonEscapeGUI().setVisible(true);
    }

}
