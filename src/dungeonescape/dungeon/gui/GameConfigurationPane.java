/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon.gui;

import dungeonescape.dungeon.DungeonConfiguration;
import dungeonescape.play.DungeonSize;
import dungeonescape.play.GameDifficulty;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.Box;
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
    
    private static final String DUNGEON_SIZE_PREFIX = "Dungeon width: ";
    
    DungeonEscapeGUI application;

    JComboBox gameDifficultyComboBox;
    JComboBox dungeonSizeComboBox;

    JScrollPane dungeonSettingsScrollPane;
    JLabel dungeonSizeValue;

    public GameConfigurationPane(DungeonEscapeGUI application) {
        this.application = application;
        init();
    }

    private void init() {

        setLayout(new BorderLayout(5, 5));

        JPanel gameDifficultyPanel = new JPanel();
        gameDifficultyPanel.setLayout(new BorderLayout());
        gameDifficultyComboBox = new JComboBox(GameDifficulty.values());
        gameDifficultyComboBox.addItemListener((ItemEvent e) -> {
            gameDifficultyPanel.remove(dungeonSettingsScrollPane);
            dungeonSettingsScrollPane
                    = new JScrollPane(generateGameDifficultySettingsPanel((GameDifficulty) e.getItem()));
            gameDifficultyPanel.add(dungeonSettingsScrollPane, BorderLayout.CENTER);
            gameDifficultyPanel.revalidate();
            gameDifficultyPanel.repaint();
        });

        gameDifficultyPanel.add(gameDifficultyComboBox, BorderLayout.NORTH);

        GameDifficulty gameDifficulty = (GameDifficulty) gameDifficultyComboBox.getSelectedItem();
        dungeonSettingsScrollPane
                = new JScrollPane(generateGameDifficultySettingsPanel(gameDifficulty));

        gameDifficultyPanel.add(dungeonSettingsScrollPane, BorderLayout.CENTER);

        JPanel dungeonSizePanel = new JPanel();
        dungeonSizePanel.setLayout(new BorderLayout());
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.PAGE_AXIS));
        dungeonSizeComboBox = new JComboBox(DungeonSize.values());
        dungeonSizeComboBox.addItemListener((ItemEvent e) -> {
            dungeonSizeValue.setText(DUNGEON_SIZE_PREFIX + ((DungeonSize) e.getItem()).getDungeonWidth());
        });
        northPanel.add(dungeonSizeComboBox);

        dungeonSizeValue = new JLabel(DUNGEON_SIZE_PREFIX + ((DungeonSize) dungeonSizeComboBox.getSelectedItem()).getDungeonWidth());
        northPanel.add(dungeonSizeValue);
        dungeonSizePanel.add(northPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.LINE_AXIS));
        centerPanel.add(gameDifficultyPanel);
        centerPanel.add(Box.createHorizontalStrut(5));
        centerPanel.add(dungeonSizePanel);

        JButton startGameButton = new JButton("Start Game");
        startGameButton.addActionListener((ActionEvent e) -> {
            application.startNewGame((GameDifficulty) gameDifficultyComboBox.getSelectedItem(), 
                    (DungeonSize) dungeonSizeComboBox.getSelectedItem());
        });
        
        add(centerPanel, BorderLayout.CENTER);
        add(startGameButton, BorderLayout.SOUTH);
    }

    private JPanel generateGameDifficultySettingsPanel(GameDifficulty gameDifficulty) {
        DungeonConfiguration dungeonConfiguration = gameDifficulty.getDungeonConfiguration();
        JLabel dungeon = new JLabel("Dungeon:");
        JLabel playerVisibility = new JLabel("Player visibility: " + dungeonConfiguration.getPlayerVisibility());
        JLabel dungeonExitCount = new JLabel("Dungeon exits: " + dungeonConfiguration.getDungeonExitCount());
        JLabel enemies = new JLabel("Enemies:");
        JLabel dungeonMasterPercentage = new JLabel("Dungeon master percentage: " + dungeonConfiguration.getDungeonMasterPercentage());
        JLabel dungeonMasterNumberOfMovesWhenPatrolling = new JLabel("Dungeon master patrol moves: " + dungeonConfiguration.getDungeonMasterNumberOfMovesWhenPatrolling());
        JLabel dungeonMasterNumberOfMovesWhenHunting = new JLabel("Dungeon master hunting moves: " + dungeonConfiguration.getDungeonMasterNumberOfMovesWhenHunting());
        JLabel dungeonMasterDetectionDistance = new JLabel("Dungeon master detection range: " + dungeonConfiguration.getDungeonMasterDetectionDistance());
        JLabel spawnDungeonMastersTurnCount = new JLabel("Dungeon master spawn at turn: " + dungeonConfiguration.getSpawnDungeonMastersTurnCount());
        JLabel guardPercentage = new JLabel("Guard percentage: " + dungeonConfiguration.getGuardPercentage());
        JLabel guardNumberOfMovesWhenPatrolling = new JLabel("Guard patrol moves: " + dungeonConfiguration.getGuardNumberOfMovesWhenPatrolling());
        JLabel guardNumberOfMovesWhenHunting = new JLabel("Guard hunting moves: " + dungeonConfiguration.getGuardNumberOfMovesWhenHunting());
        JLabel guardDetectionDistance = new JLabel("Guard detection range: " + dungeonConfiguration.getGuardDetectionDistance());
        JLabel ghostPercentage = new JLabel("Ghost percentage: " + dungeonConfiguration.getGhostPercentage());
        JLabel ghostNumberOfMovesWhenPatrolling = new JLabel("Ghost patrol moves: " + dungeonConfiguration.getGhostNumberOfMovesWhenPatrolling());
        JLabel ghostNumberOfMovesWhenHunting = new JLabel("Ghost hunting moves: " + dungeonConfiguration.getGhostNumberOfMovesWhenHunting());
        JLabel ghostDetectionDistance = new JLabel("Ghost detection range: " + dungeonConfiguration.getGhostDetectionDistance());
        JLabel ghostFreezeTime = new JLabel("Ghost " + dungeonConfiguration.getGhostFreezeTime().toString());
        JLabel mines = new JLabel("Mines:");
        JLabel freezeMinePercentage = new JLabel("Freeze mine percentage: " + dungeonConfiguration.getFreezeMinePercentage());
        JLabel maxFreezeMineTime = new JLabel("Freeze mine max " + dungeonConfiguration.getMaxFreezeMineTime());
        JLabel teleportMinePercentage = new JLabel("Teleport mine percentage: " + dungeonConfiguration.getTeleportMinePercentage());

        JPanel configurationPanel = new JPanel();
        configurationPanel.setLayout(new BoxLayout(configurationPanel, BoxLayout.PAGE_AXIS));

        configurationPanel.add(dungeon);
        configurationPanel.add(playerVisibility);
        configurationPanel.add(dungeonExitCount);
        configurationPanel.add(enemies);
        configurationPanel.add(dungeonMasterPercentage);
        configurationPanel.add(dungeonMasterNumberOfMovesWhenPatrolling);
        configurationPanel.add(dungeonMasterNumberOfMovesWhenHunting);
        configurationPanel.add(dungeonMasterDetectionDistance);
        configurationPanel.add(spawnDungeonMastersTurnCount);
        configurationPanel.add(guardPercentage);
        configurationPanel.add(guardNumberOfMovesWhenPatrolling);
        configurationPanel.add(guardNumberOfMovesWhenHunting);
        configurationPanel.add(guardDetectionDistance);
        configurationPanel.add(ghostPercentage);
        configurationPanel.add(ghostNumberOfMovesWhenPatrolling);
        configurationPanel.add(ghostNumberOfMovesWhenHunting);
        configurationPanel.add(ghostDetectionDistance);
        configurationPanel.add(ghostFreezeTime);
        configurationPanel.add(mines);
        configurationPanel.add(freezeMinePercentage);
        configurationPanel.add(maxFreezeMineTime);
        configurationPanel.add(teleportMinePercentage);

        return configurationPanel;
    }

}
