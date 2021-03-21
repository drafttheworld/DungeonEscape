/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon.gui;

import dungeonescape.dungeon.DungeonConfiguration;
import dungeonescape.dungeonobject.FreezeTime;
import dungeonescape.play.GameDifficulty;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Andrew
 */
public class CustomDungeonConfigurationPanel extends JPanel {

    private JTextField dungeonWidthTextField;
    private JTextField playerVisibilityTextField;
    private JTextField numberOfDungeonExitsTextField;
    private JTextField dungeonMasterCountTextField;
    private JTextField dungeonMasterPatrolMovesTextField;
    private JTextField dungeonMasterHuntingMovesTextField;
    private JTextField dungeonMasterDetectionRangeTextField;
    private JTextField dungeonMasterSpawnTurnTextField;
    private JTextField guardCountTextField;
    private JTextField guardPatrolMovesTextField;
    private JTextField guardHungtingMovesTextField;
    private JTextField guardDetectionDistanceTextField;
    private JTextField ghostCountTextField;
    private JTextField ghostPatrolMovesTextField;
    private JTextField ghostHungtingMovesTextField;
    private JTextField ghostDetectionDistanceTextField;
    private JTextField ghostMinimumFreezeTimeTextField;
    private JTextField ghostMaximumFreezeTimeTextField;
    private JTextField freezeMineCountTextField;
    private JTextField freezeMineMinFreezeTimeTextField;
    private JTextField freezeMineMaxFreezeTimeTextField;
    private JTextField teleportMineCountTextField;
    private JTextField powerUpBoxCountTextField;
    private JTextField invincibilityProbabilityTextField;
    private JTextField invisibilityProbabilityTextField;
    private JTextField repellentProbabilityTextField;
    private JTextField terminatorProbabilityTextField;
    private JTextField coinCoveragePercentageTextField;
    private JTextField coinCountOverrideTextField;

    public CustomDungeonConfigurationPanel() {
        init();
    }

    public DungeonConfiguration getDungeonConfiguration() {

        DungeonConfiguration dungeonConfiguration = GameDifficulty.CUSTOM.getDungeonConfiguration();
        return dungeonConfiguration.dungeonWidth(Integer.parseInt(dungeonWidthTextField.getText()))
            .playerVisibility(Integer.parseInt(playerVisibilityTextField.getText()))
            .dungeonExitCount(Integer.parseInt(numberOfDungeonExitsTextField.getText()))
            .dungeonMasterCount(Integer.parseInt(dungeonMasterCountTextField.getText()))
            .dungeonMasterNumberOfMovesWhenPatrolling(Integer.parseInt(dungeonMasterPatrolMovesTextField.getText()))
            .dungeonMasterNumberOfMovesWhenHunting(Integer.parseInt(dungeonMasterHuntingMovesTextField.getText()))
            .dungeonMasterDetectionDistance(Integer.parseInt(dungeonMasterDetectionRangeTextField.getText()))
            .spawnDungeonMastersTurnCount(Integer.parseInt(dungeonMasterSpawnTurnTextField.getText()))
            .guardCount(Integer.parseInt(guardCountTextField.getText()))
            .guardNumberOfMovesWhenPatrolling(Integer.parseInt(guardPatrolMovesTextField.getText()))
            .guardNumberOfMovesWhenHunting(Integer.parseInt(guardHungtingMovesTextField.getText()))
            .guardDetectionDistance(Integer.parseInt(guardDetectionDistanceTextField.getText()))
            .ghostCount(Integer.parseInt(ghostCountTextField.getText()))
            .ghostNumberOfMovesWhenPatrolling(Integer.parseInt(ghostPatrolMovesTextField.getText()))
            .ghostNumberOfMovesWhenHunting(Integer.parseInt(ghostHungtingMovesTextField.getText()))
            .ghostDetectionDistance(Integer.parseInt(ghostDetectionDistanceTextField.getText()))
            .ghostMinFreezeTime(new FreezeTime(Integer.parseInt(ghostMinimumFreezeTimeTextField.getText())))
            .ghostMaxFreezeTime(new FreezeTime(Integer.parseInt(ghostMaximumFreezeTimeTextField.getText())))
            .freezeMineCount(Integer.parseInt(freezeMineCountTextField.getText()))
            .freezeMineMinFreezeTime(new FreezeTime(Integer.parseInt(freezeMineMinFreezeTimeTextField.getText())))
            .freezeMineMaxFreezeTime(new FreezeTime(Integer.parseInt(freezeMineMaxFreezeTimeTextField.getText())))
            .teleportMineCount(Integer.parseInt(teleportMineCountTextField.getText()))
            .powerUpBoxCount(Integer.parseInt(powerUpBoxCountTextField.getText()))
            .invincibilityProbability(Integer.parseInt(invincibilityProbabilityTextField.getText()))
            .invisibilityProbability(Integer.parseInt(invisibilityProbabilityTextField.getText()))
            .repellentProbability(Integer.parseInt(repellentProbabilityTextField.getText()))
            .terminatorProbability(Integer.parseInt(terminatorProbabilityTextField.getText()))
            .coinCoveragePercentOfOpenSpaces(Integer.parseInt(coinCoveragePercentageTextField.getText()))
            .coinCountOverride(Integer.parseInt(coinCountOverrideTextField.getText()));
    }

    private void init() {

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        JLabel titleLabel = new JLabel("Fill out the following dungeon characteristics:");
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        titlePanel.add(titleLabel);
        this.add(titlePanel);

        this.add(new JLabel("Physical Dungeon Characteristics:"));
        dungeonWidthTextField = createAndAddUserField("Dungeon width (# of spaces):");
        playerVisibilityTextField = createAndAddUserField("Player visibility (# of spaces):");
        numberOfDungeonExitsTextField = createAndAddUserField("Number of dungeon exits:");
        this.add(new JLabel("Enemy Characteristics:"));
        dungeonMasterCountTextField = createAndAddUserField("Dungeon master count:");
        dungeonMasterPatrolMovesTextField = createAndAddUserField("Dungeon master patrol moves:");
        dungeonMasterHuntingMovesTextField = createAndAddUserField("Dungeon master hunting moves:");
        dungeonMasterDetectionRangeTextField = createAndAddUserField("Dungeon master detection range:");
        dungeonMasterSpawnTurnTextField = createAndAddUserField("Dungeon master spawn at turn:");
        guardCountTextField = createAndAddUserField("Guard count:");
        guardPatrolMovesTextField = createAndAddUserField("Guard patrol moves:");
        guardHungtingMovesTextField = createAndAddUserField("Guard hunting moves:");
        guardDetectionDistanceTextField = createAndAddUserField("Guard detection range:");
        ghostCountTextField = createAndAddUserField("Ghost count:");
        ghostPatrolMovesTextField = createAndAddUserField("Ghost patrol moves:");
        ghostHungtingMovesTextField = createAndAddUserField("Ghost hunting moves:");
        ghostDetectionDistanceTextField = createAndAddUserField("Ghost detection range:");
        ghostMinimumFreezeTimeTextField = createAndAddUserField("Ghost minimum freeze time:");
        ghostMaximumFreezeTimeTextField = createAndAddUserField("Ghost maximum freeze time:");
        this.add(new JLabel("Mine Characteristics"));
        freezeMineCountTextField = createAndAddUserField("Freeze mine count:");
        freezeMineMinFreezeTimeTextField = createAndAddUserField("Freeze mine min freeze time:");
        freezeMineMaxFreezeTimeTextField = createAndAddUserField("Freeze mine max freeze time:");
        teleportMineCountTextField = createAndAddUserField("Teleport mine count:");
        this.add(new JLabel("Power-up Characteristics"));
        powerUpBoxCountTextField = createAndAddUserField("Number of power-up boxes:");
        invincibilityProbabilityTextField = createAndAddUserField("Probability of invincibility (0-5):");
        invisibilityProbabilityTextField = createAndAddUserField("Probability of invisibility (0-5):");
        repellentProbabilityTextField = createAndAddUserField("Probability of repellent (0-5):");
        terminatorProbabilityTextField = createAndAddUserField("Probability of terminator (0-5):");
        this.add(new JLabel("Coin Characteristics"));

    }

    private void createCoinField() {

        JPanel coinCharacteristicsPanel = new JPanel();
        coinCharacteristicsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JPanel coinCharacteristicsFieldPanel = new JPanel();
        coinCharacteristicsFieldPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        coinCharacteristicsPanel.add(coinCharacteristicsFieldPanel);

        JCheckBox overrideCoinPercentageCheckBox = new JCheckBox("Override with specific number of coins");
        coinCharacteristicsPanel.add(overrideCoinPercentageCheckBox);

        JPanel percentageOfOpenSpacesToPlaceCoinsPanel = createFieldPanel("Percentage of open spaces to place coins (0-100):");
        coinCoveragePercentageTextField = getJTextField(percentageOfOpenSpacesToPlaceCoinsPanel);
        coinCharacteristicsFieldPanel.add(percentageOfOpenSpacesToPlaceCoinsPanel);

        JPanel numberOfCoinsToPlacePanel = createFieldPanel("Number of coins to place:");
        coinCountOverrideTextField = getJTextField(numberOfCoinsToPlacePanel);

        overrideCoinPercentageCheckBox.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                coinCharacteristicsFieldPanel.removeAll();
                coinCharacteristicsFieldPanel.add(numberOfCoinsToPlacePanel);
            } else {
                coinCharacteristicsFieldPanel.removeAll();
                coinCharacteristicsFieldPanel.add(percentageOfOpenSpacesToPlaceCoinsPanel);
            }
            coinCharacteristicsPanel.revalidate();
            coinCharacteristicsPanel.repaint();
        });
        this.add(coinCharacteristicsPanel);
    }

    private JTextField createAndAddUserField(String fieldTitle) {

        JPanel jPanel = createFieldPanel(fieldTitle);
        this.add(jPanel);

        return getJTextField(jPanel);
    }

    private JPanel createFieldPanel(String fieldTitle) {

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel jLabel = new JLabel(fieldTitle);
        JTextField jTextField = new JTextField();
        jTextField.setColumns(10);
        jPanel.add(jLabel);
        jPanel.add(jTextField);

        return jPanel;
    }

    private JTextField getJTextField(JPanel jPanel) {

        for (Component component : jPanel.getComponents()) {
            if (component instanceof JTextField) {
                return (JTextField) component;
            }
        }

        return null;
    }

}
