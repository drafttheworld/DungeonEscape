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
import dungeonescape.dungeonobject.characters.Player;
import java.util.HashMap;
import java.util.List;
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
    private int turnCount;

    public GameSession(DungeonConfiguration dungeonConfiguration) {
        this.dungeonConfiguration = dungeonConfiguration;

        dungeon = new Dungeon(dungeonConfiguration);
        turnCount = 0;
    }

    public DungeonConfiguration getDungeonConfiguration() {
        return dungeonConfiguration;
    }

    public String getSessionId() {
        return sessionId;
    }

    public List<DungeonObjectTrack> movePlayerGui(Direction direction) {
        movePlayer(direction);
        return dungeon.getDungeonObjectTracks();
    }

    public String movePlayerHeadless(Direction direction) {
        movePlayer(direction);
        return dungeon.generatePlayerMap();
    }

    private void movePlayer(Direction direction) {
        dungeon.movePlayer(direction);

        turnCount++;

        if (turnCount == dungeonConfiguration.getSpawnDungeonMastersTurnCount()) {
            dungeon.spawnDungeonMasters();
        }
    }

    public String getPlayerStats() {
        Player player = dungeon.getPlayer();
        StringBuilder playerStats = new StringBuilder("Stats for ").append(player.getPlayerName()).append(":\n")
            .append("Dungeon time: ").append(dungeon.getDungeonTimeElapsed()).append("\n")
            .append("Frozen time remaining: ");
        FreezeTime freezeTime = player.getFrozenTurnsRemaining();
        playerStats.append(freezeTime.getTurns())
            .append(" ").append("Turns")
            .append("\n");
        return playerStats.toString();
    }

    public String getPlayerMap() {
        String miniMap = dungeon.generatePlayerMap();
//        System.out.println(miniMap);
        return miniMap;
    }

}
