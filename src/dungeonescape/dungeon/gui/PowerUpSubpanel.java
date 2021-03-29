/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon.gui;

import dungeonescape.dungeonobject.powerups.PowerUpEnum;
import dungeonescape.dungeonobject.powerups.PowerUpListener;
import dungeonescape.dungeonobject.powerups.PowerUpService;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Andrew
 */
public class PowerUpSubpanel extends JPanel implements PowerUpListener {

    private final PowerUpEnum powerUpEnum;
    private final PowerUpService powerUpService;
    private final JFrame dungeonEscapeGUI;
    private int inventory;
    private JLabel label;
    private JButton button;

    public PowerUpSubpanel(PowerUpEnum powerUpEnum, PowerUpService powerUpService, JFrame dungeonEscapeGUI) {
        this.powerUpEnum = powerUpEnum;
        this.powerUpService = powerUpService;
        this.dungeonEscapeGUI = dungeonEscapeGUI;
        inventory = 0;

        initComponents();
    }

    private void initComponents() {

        setAlignmentX(Component.RIGHT_ALIGNMENT);
        setAlignmentY(Component.BOTTOM_ALIGNMENT);
        setLayout(new FlowLayout(FlowLayout.LEADING));
        setMaximumSize(new Dimension(300, 40));

        label = new JLabel(buildLabelText());
        add(label);

        button = new JButton("Use");
        button.setEnabled(false);
        button.addActionListener((ActionEvent e) -> {
            powerUpService.usePowerUp(powerUpEnum);
            decrementInventory(1);
            dungeonEscapeGUI.requestFocus();
        });
        add(button);

        powerUpService.registerPowerUpClient(this);
    }

    private void incrementInventory() {
        inventory++;
        label.setText(buildLabelText());
        if (inventory > 0) {
            button.setEnabled(true);
        }
        this.revalidate();
        this.repaint();
    }

    public void decrementInventory(int amount) {
        inventory -= amount;
        label.setText(buildLabelText());
        if (inventory <= 0) {
            button.setEnabled(false);
        }
        this.revalidate();
        this.repaint();
    }

    public int getInventory() {
        return inventory;
    }

    public PowerUpEnum getPowerUp() {
        return powerUpEnum;
    }

    private String buildLabelText() {
        return powerUpEnum.getTitle() + ": " + inventory;
    }

    @Override
    public void notifyPowerUpAdded(PowerUpEnum powerUpEnum) {
        if (powerUpEnum != null
            && this.powerUpEnum == powerUpEnum) {

            incrementInventory();
        }
    }

    @Override
    public void notifyPowerUpUsed(PowerUpEnum powerUpEnum) {
        // do nothing, not used.
    }
}
