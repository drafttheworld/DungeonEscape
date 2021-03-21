/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape;

import dungeonescape.dungeon.DungeonConfiguration;
import dungeonescape.dungeon.gui.DungeonEscapeGUI;
import dungeonescape.dungeon.notifications.ExecutionErrorNotification;
import dungeonescape.dungeon.notifications.NotificationManager;
import dungeonescape.play.GameDifficulty;
import dungeonescape.play.GameSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Andrew
 */
public class DungeonEscapeApplication {

    private final Map<String, GameSession> gameSessions = new HashMap<>();

    public GameSession startNewGame(DungeonConfiguration dungeonConfiguration) {
        GameSession gameSession = new GameSession(dungeonConfiguration);
        gameSessions.put(gameSession.getSessionId(), gameSession);
        return gameSession;
    }

    public void startGameGUI() throws IOException {
        new DungeonEscapeGUI(this).setVisible(true);
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

    public static void main(String[] args) throws IOException {

        new DungeonEscapeApplication().startGameGUI();
    }

}
