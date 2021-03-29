/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.play;

import dungeonescape.dungeon.Dungeon;
import dungeonescape.dungeon.DungeonConfiguration;
import dungeonescape.dungeon.space.DungeonSpace;
import dungeonescape.dungeonobject.powerups.PowerUpService;
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
    private final PowerUpService powerUpService;

    //Game status
    private int turnCount;

    public GameSession(DungeonConfiguration dungeonConfiguration) {
        this.dungeonConfiguration = dungeonConfiguration;

        powerUpService = new PowerUpService();
        dungeon = new Dungeon(dungeonConfiguration, powerUpService);
        turnCount = 0;
    }

    public DungeonConfiguration getDungeonConfiguration() {
        return dungeonConfiguration;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Set<DungeonSpace> movePlayerGui(Direction direction) {
        return movePlayer(direction);
    }

    public String movePlayerHeadless(Direction direction) {
        movePlayer(direction);
        return dungeon.generatePlayerMap();
    }

    private Set<DungeonSpace> movePlayer(Direction direction) {

        turnCount++;

        Set<DungeonSpace> dungeonSpacesToUpdate = dungeon.movePlayer(direction);

        if (turnCount == dungeonConfiguration.getSpawnDungeonMastersTurnCount()) {
            dungeonSpacesToUpdate.addAll(dungeon.spawnDungeonMasters());
        }

        return dungeonSpacesToUpdate;
    }

    public String getPlayerStats() {

        return new StringBuilder()
            .append("Dungeon time elapsed: ").append(turnCount).append(" turns").append("\n")
            .append(dungeon.getPlayer().getPlayerStats()).toString();
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    public String getPlayerMap() {
        return dungeon.generatePlayerMap();
    }

    public PowerUpService getPowerUpService() {
        return powerUpService;
    }

}
