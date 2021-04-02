/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author Andrew
 */
public class FinalScoreWindow extends JFrame {

    public void createAndShowFinalScoreWindow(String finalScoreText) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(600, 400));

        JLabel title = new JLabel("Final Score");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        panel.add(title, BorderLayout.NORTH);

        JTextArea textArea = new JTextArea(finalScoreText);
        panel.add(textArea, BorderLayout.CENTER);

        JButton okButton = new JButton("OK");
        okButton.addActionListener((ActionEvent e) -> {
            this.dispose();
        });

        panel.add(okButton, BorderLayout.SOUTH);

        this.add(panel);

        this.pack();
        this.setVisible(true);
    }

}
