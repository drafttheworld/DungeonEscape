/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon.gui;

import dungeonescape.dungeon.DungeonConfiguration;
import dungeonescape.exceptions.GameInitializationFailureException;
import dungeonescape.play.GameDifficulty;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author Andrew
 */
public class GameConfigurationPane extends JPanel {

    private final DungeonEscapeGUI applicationGUI;

    private JComboBox gameDifficultyComboBox;
    private JComboBox dungeonSizeComboBox;
    private CustomDungeonConfigurationPanel customDungeonConfigurationPanel;

    private JScrollPane dungeonSettingsScrollPane;
    private JLabel dungeonSizeValue;

    public GameConfigurationPane(DungeonEscapeGUI applicationGUI) {
        this.applicationGUI = applicationGUI;
        init();
    }

    private void init() {

        setLayout(new BorderLayout(5, 5));

        JPanel gameDifficultyPanel = new JPanel();
        gameDifficultyPanel.setLayout(new BorderLayout());

        gameDifficultyComboBox = new JComboBox(GameDifficulty.values());
        gameDifficultyComboBox.addItemListener((ItemEvent e) -> {
            GameDifficulty gameDifficulty = (GameDifficulty) e.getItem();
            gameDifficultyPanel.remove(dungeonSettingsScrollPane);
            if (gameDifficulty == GameDifficulty.CUSTOM) {
                customDungeonConfigurationPanel = new CustomDungeonConfigurationPanel();
                dungeonSettingsScrollPane
                    = new JScrollPane(customDungeonConfigurationPanel);
            } else {
                dungeonSettingsScrollPane
                    = new JScrollPane(generateStaticGameDifficultySettingsPanel(gameDifficulty));
            }

            gameDifficultyPanel.add(dungeonSettingsScrollPane, BorderLayout.CENTER);
            gameDifficultyPanel.revalidate();
            gameDifficultyPanel.repaint();
        });

        gameDifficultyPanel.add(gameDifficultyComboBox, BorderLayout.NORTH);

        GameDifficulty gameDifficulty = (GameDifficulty) gameDifficultyComboBox.getSelectedItem();
        dungeonSettingsScrollPane
            = new JScrollPane(generateStaticGameDifficultySettingsPanel(gameDifficulty));

        gameDifficultyPanel.add(dungeonSettingsScrollPane, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.LINE_AXIS));
        centerPanel.add(gameDifficultyPanel);

        JButton startGameButton = new JButton("Start Game");
        startGameButton.addActionListener((ActionEvent e) -> {
            GameDifficulty selectedGameDifficulty = (GameDifficulty) gameDifficultyComboBox.getSelectedItem();
            DungeonConfiguration dungeonConfiguration;
            if (selectedGameDifficulty == GameDifficulty.CUSTOM) {
                if (customDungeonConfigurationPanel == null) {
                    throw new GameInitializationFailureException("Could not find the request custom configuration.");
                }
                dungeonConfiguration = customDungeonConfigurationPanel.getDungeonConfiguration();
            } else {
                dungeonConfiguration = selectedGameDifficulty.getDungeonConfiguration();
            }

            applicationGUI.startNewGame(dungeonConfiguration);
        });

        add(centerPanel, BorderLayout.CENTER);
        add(startGameButton, BorderLayout.SOUTH);
    }

    private JPanel generateStaticGameDifficultySettingsPanel(GameDifficulty gameDifficulty) {
        DungeonConfiguration dungeonConfiguration = gameDifficulty.getDungeonConfiguration();
        JLabel dungeon = new JLabel("Dungeon:");
        int dungeonWidth = dungeonConfiguration.getDungeonWidth();
        String dungeonSpaces = NumberFormat.getNumberInstance(Locale.US).format(dungeonWidth * dungeonWidth);
        JLabel dungeonSize = new JLabel("Dungeon Size: " + dungeonWidth + " X " + dungeonWidth + " (" + dungeonSpaces + " spaces)");
        JLabel playerVisibility = new JLabel("Player visibility: " + dungeonConfiguration.getPlayerVisibility());
        JLabel dungeonExitCount = new JLabel("Dungeon exits: " + dungeonConfiguration.getDungeonExitCount());
        JLabel enemies = new JLabel("Enemies:");
        JLabel dungeonMasterCount = new JLabel("Dungeon master count: " + dungeonConfiguration.getDungeonMasterCount());
        JLabel dungeonMasterNumberOfMovesWhenPatrolling = new JLabel("Dungeon master patrol moves: " + dungeonConfiguration.getDungeonMasterNumberOfMovesWhenPatrolling());
        JLabel dungeonMasterNumberOfMovesWhenHunting = new JLabel("Dungeon master hunting moves: " + dungeonConfiguration.getDungeonMasterNumberOfMovesWhenHunting());
        JLabel dungeonMasterDetectionDistance = new JLabel("Dungeon master detection range: " + dungeonConfiguration.getDungeonMasterDetectionDistance());
        JLabel spawnDungeonMastersTurnCount = new JLabel("Dungeon master spawn at turn: " + dungeonConfiguration.getSpawnDungeonMastersTurnCount());
        JLabel guardCount = new JLabel("Guard count: " + dungeonConfiguration.getGuardCount());
        JLabel guardNumberOfMovesWhenPatrolling = new JLabel("Guard patrol moves: " + dungeonConfiguration.getGuardNumberOfMovesWhenPatrolling());
        JLabel guardNumberOfMovesWhenHunting = new JLabel("Guard hunting moves: " + dungeonConfiguration.getGuardNumberOfMovesWhenHunting());
        JLabel guardDetectionDistance = new JLabel("Guard detection range: " + dungeonConfiguration.getGuardDetectionDistance());
        JLabel ghostCount = new JLabel("Ghost count: " + dungeonConfiguration.getGhostCount());
        JLabel ghostNumberOfMovesWhenPatrolling = new JLabel("Ghost patrol moves: " + dungeonConfiguration.getGhostNumberOfMovesWhenPatrolling());
        JLabel ghostNumberOfMovesWhenHunting = new JLabel("Ghost hunting moves: " + dungeonConfiguration.getGhostNumberOfMovesWhenHunting());
        JLabel ghostDetectionDistance = new JLabel("Ghost detection range: " + dungeonConfiguration.getGhostDetectionDistance());
        JLabel ghostMinFreezeTime = new JLabel("Ghost min freeze time: " + dungeonConfiguration.getGhostMinFreezeTime().toString());
        JLabel ghostMaxFreezeTime = new JLabel("Ghost max freeze time: " + dungeonConfiguration.getGhostMaxFreezeTime().toString());
        JLabel mines = new JLabel("Mines:");
        JLabel freezeMineCount = new JLabel("Freeze mine count: " + dungeonConfiguration.getFreezeMineCount());
        JLabel freezeMineMaxFreezeTime = new JLabel("Freeze mine max " + dungeonConfiguration.getFreezeMineMaxFreezeTime());
        JLabel teleportMineCount = new JLabel("Teleport mine count: " + dungeonConfiguration.getTeleportMineCount());

        JPanel configurationPanel = new JPanel();
        configurationPanel.setLayout(new BoxLayout(configurationPanel, BoxLayout.PAGE_AXIS));

        configurationPanel.add(dungeon);
        configurationPanel.add(dungeonSize);
        configurationPanel.add(playerVisibility);
        configurationPanel.add(dungeonExitCount);
        configurationPanel.add(enemies);
        configurationPanel.add(dungeonMasterCount);
        configurationPanel.add(dungeonMasterNumberOfMovesWhenPatrolling);
        configurationPanel.add(dungeonMasterNumberOfMovesWhenHunting);
        configurationPanel.add(dungeonMasterDetectionDistance);
        configurationPanel.add(spawnDungeonMastersTurnCount);
        configurationPanel.add(guardCount);
        configurationPanel.add(guardNumberOfMovesWhenPatrolling);
        configurationPanel.add(guardNumberOfMovesWhenHunting);
        configurationPanel.add(guardDetectionDistance);
        configurationPanel.add(ghostCount);
        configurationPanel.add(ghostNumberOfMovesWhenPatrolling);
        configurationPanel.add(ghostNumberOfMovesWhenHunting);
        configurationPanel.add(ghostDetectionDistance);
        configurationPanel.add(ghostMinFreezeTime);
        configurationPanel.add(ghostMaxFreezeTime);
        configurationPanel.add(mines);
        configurationPanel.add(freezeMineCount);
        configurationPanel.add(freezeMineMaxFreezeTime);
        configurationPanel.add(teleportMineCount);

        return configurationPanel;
    }

}
