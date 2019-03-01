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
import dungeonescape.dungeonobject.actions.move.Direction;
import java.util.UUID;

/**
 *
 * @author Andrew
 */
public class GameSession {

    private final String sessionId = UUID.randomUUID().toString();

    private final String playerName;
    private final DungeonConfiguration dugeonConfiguration;

    private final Dungeon dungeon;

    //Game status
    boolean won = false;
    boolean lost = false;

    public GameSession(String playerName, DungeonConfiguration dugeonConfiguration) throws GameNotification {
        this.playerName = playerName;
        this.dugeonConfiguration = dugeonConfiguration;

        dungeon = new Dungeon(this);
    }

    public String getPlayerName() {
        return playerName;
    }

    public DungeonConfiguration getDugeonConfiguration() {
        return dugeonConfiguration;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String movePlayer(Direction direction) throws GameNotification {

        if (won || lost) {
            String gameResult = won ? "You Won!" : "You lost.";
            throw new GameOverNotification("Cannot move player after game has ended (" + gameResult + ").");
        }

        return dungeon.movePlayer(direction);
    }

}
