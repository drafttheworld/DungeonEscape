/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape;

import dungeonescape.dungeon.DungeonConfiguration;
import dungeonescape.dungeonobject.mine.FreezeTime;
import dungeonescape.play.GameSession;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Andrew
 */
public class DungeonEscapeApplication {

    private GameSession gameSession;

    public void startNewGame(String playerName, DungeonConfiguration dungeonConfiguration) {
        gameSession = new GameSession(playerName, dungeonConfiguration);
    }
    
    public static void main(String[] args) {
        DungeonEscapeApplication dungeonEscapeApplication = new DungeonEscapeApplication();

        DungeonConfiguration dungeonConfiguration
                = new DungeonConfiguration()
                        .width(1000)
                        .exitCount(5)
                        .maxFreezeTime(new FreezeTime(5, TimeUnit.MINUTES))
                        .numberOfFreezeMines(5000)
                        .numberOfTeleportMines(500);

        dungeonEscapeApplication.startNewGame("Andrew", dungeonConfiguration);
    }

}
