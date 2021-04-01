/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.play;

import dungeonescape.dungeon.Dungeon;
import dungeonescape.dungeon.DungeonConfiguration;
import dungeonescape.dungeon.space.DungeonSpace;
import dungeonescape.dungeonobject.characters.Player;
import dungeonescape.dungeonobject.powerups.PowerUpService;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author Andrew
 */
public class GameSession {

    private static final int TURN_DELTA_BONUS = 2;

    private final String sessionId = UUID.randomUUID().toString();
    private final DungeonConfiguration dungeonConfiguration;
    private final Dungeon dungeon;
    private final PowerUpService powerUpService;

    public GameSession(DungeonConfiguration dungeonConfiguration) {
        this.dungeonConfiguration = dungeonConfiguration;

        powerUpService = new PowerUpService();
        dungeon = new Dungeon(dungeonConfiguration, powerUpService);
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

        Set<DungeonSpace> dungeonSpacesToUpdate = dungeon.movePlayer(direction);

        if (dungeon.getPlayer().getTurnCount() == dungeonConfiguration.getSpawnDungeonMastersTurnCount()) {
            dungeonSpacesToUpdate.addAll(dungeon.spawnDungeonMasters());
        }

        return dungeonSpacesToUpdate;
    }

    public String getPlayerStats(boolean isFinalStats) {

        //Stats:
        //Number of coins collected
        //Number of power-ups collected
        //Number of ghost attacks
        //Number of guard attacks
        //Number of Freeze mines tripped
        //Number of Teleport mines tripped
        //Number of turns
        //Total play time
        //Score
        Player player = dungeon.getPlayer();

        int coinsInInventory = player.getCoinsInInventory();
        int powerUpsCollected = player.getPowerUpsCollected();

        int score = coinsInInventory + (powerUpsCollected * 10);

        if (isFinalStats) {
            int turnDelta = dungeon.getNumberOfOpenSpaces() - player.getTurnCount();
            score += (turnDelta * TURN_DELTA_BONUS);
        }

        StringBuilder sb = new StringBuilder()
            .append("Dungeon time elapsed: ").append(player.getTurnCount()).append(" turns\n")
            .append("Coins in inventory: ").append(player.getCoinsInInventory()).append(" coins\n");

        if (isFinalStats) {
            sb.append("Coins collected: ").append(player.getCoinsCollected()).append(" coins\n");
        }

        sb.append("Active power-up turns remaining: ").append(player.getActivePowerUpTurnsRemaining()).append(" turns\n")
            .append("Frozen turns remaining: ").append(player.getFrozenTurnsRemaining().getTurns()).append(" turns");

        if (isFinalStats) {
            sb.append("\nPower-ups collected: ").append(player.getPowerUpsCollected()).append(" power-ups\n")
                .append("Ghost attacks: ").append(player.getGhostAttacks()).append(" attacks\n")
                .append("Guard attacks: ").append(player.getGuardAttacks()).append(" attacks\n")
                .append("Freeze mines tripped: ").append(player.getFreezeMinesTripped()).append(" mines\n")
                .append("Teleport mines tripped: ").append(player.getTeleportMinesTripped()).append(" mines\n\n")
                .append("Score: ").append(score);
        }

        return sb.toString();
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
