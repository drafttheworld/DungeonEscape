/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.play;

import dungeonescape.dungeon.Dungeon;
import dungeonescape.dungeon.DungeonConfiguration;
import dungeonescape.dungeonobject.FreezeTime;
import dungeonescape.dungeon.space.DungeonSpace;
import java.util.Set;
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

    public Set<DungeonSpace> movePlayerGui(Direction direction) {
        movePlayer(direction);
        return dungeon.getDungeonSpacesToUpdate();
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

        int dungeonTimeElapsed = dungeon.getDungeonTimeElapsed();
        return new StringBuilder()
            .append("Dungeon time elapsed: ").append(dungeonTimeElapsed).append(" turns").append("\n")
            .append(dungeon.getPlayer().getPlayerStats()).toString();
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    public String getPlayerMap() {
        return dungeon.generatePlayerMap();
    }

}
