/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon.gui;

import dungeonescape.DungeonEscapeApplication;
import dungeonescape.dungeon.notifications.GameNotification;
import dungeonescape.dungeon.notifications.NotificationManager;
import dungeonescape.dungeon.notifications.PlayerNotFoundNotification;
import dungeonescape.dungeonobject.DungeonObjectTrack;
import dungeonescape.play.Direction;
import dungeonescape.play.DungeonSize;
import dungeonescape.play.GameDifficulty;
import dungeonescape.play.GameSession;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Andrew
 */
public class DungeonEscapeGUI extends JFrame implements NotificationManager {

    private static final int NORTH_KEY_CODE = 38;
    private static final int SOUTH_KEY_CODE = 40;
    private static final int EAST_KEY_CODE = 39;
    private static final int WEST_KEY_CODE = 37;

    private JPanel applicationPane;
    private JLabel loadingLabel;
    private JButton startButton;
    private JButton recenterButton;
    private JScrollPane mapScrollPane;
    private List<DungeonTable> dungeonTables;
    private DungeonTable activeDungeonTable;

    DungeonEscapeApplication dungeonEscapeApplication;
    private GameSession gameSession;
    private int mapCenterX;
    private int mapCenterY;

    public DungeonEscapeGUI() {
        initComponents();
    }

    private void initComponents() {

        applicationPane = new JPanel();
        loadingLabel = new JLabel();
        startButton = new JButton();
        recenterButton = new JButton();
        mapScrollPane = new JScrollPane();
        dungeonTables = new ArrayList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                //do nothing
            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case NORTH_KEY_CODE:
                        movePlayer(Direction.NORTH);
                        break;
                    case SOUTH_KEY_CODE:
                        movePlayer(Direction.SOUTH);
                        break;
                    case EAST_KEY_CODE:
                        movePlayer(Direction.EAST);
                        break;
                    case WEST_KEY_CODE:
                        movePlayer(Direction.WEST);
                        break;
                    default:
                        //do nothing
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                
            }

        });

        dungeonEscapeApplication = new DungeonEscapeApplication();

        mapScrollPane.setColumnHeader(null);
        mapScrollPane.setPreferredSize(new Dimension(480, 480));
        mapScrollPane.setVisible(false);

        startButton.setText("Start New Game");
        startButton.addActionListener((ActionEvent e) -> {

            applicationPane.remove(mapScrollPane);
            loadingLabel.setText("Loading map...");
            applicationPane.add(loadingLabel, BorderLayout.CENTER);
            loadingLabel.setVisible(true);
            refresh();
            SwingUtilities.invokeLater(() -> {
                try {
                    //Create the game session
                    gameSession = new DungeonEscapeApplication().startNewGame("Andrew", GameDifficulty.HARD, DungeonSize.XLARGE);
                    activeDungeonTable = new DungeonTable(gameSession);
                    dungeonTables.add(activeDungeonTable);

                    //Once ready remove the loading label and add the map
                    applicationPane.remove(loadingLabel);
                    applicationPane.add(mapScrollPane, BorderLayout.CENTER);
                    mapScrollPane.setViewportView(activeDungeonTable);
                    mapScrollPane.setVisible(true);
                    mapScrollPane.setFocusable(false);

                    //Add the recenter button
                    applicationPane.add(recenterButton, BorderLayout.SOUTH);
                    recenterButton.setVisible(true);
                    recenterButton.setText("Recenter Map");
                    recenterButton.addActionListener((ActionEvent ev) -> {
                        mapScrollPane.requestFocus();
                        activeDungeonTable.centerOnPlayer("Andrew");
                        refresh();
                    });

                    //Refresh the map
                    refresh();
                    activeDungeonTable.centerOnPlayer("Andrew");
                    
                } catch (PlayerNotFoundNotification ex) {
                    Logger.getLogger(DungeonEscapeGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (GameNotification ex) {
                    Logger.getLogger(DungeonEscapeGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

        });

        applicationPane.setLayout(new BorderLayout());
        applicationPane.setPreferredSize(new Dimension(480, 480));
        applicationPane.add(startButton, BorderLayout.NORTH);

        this.setLayout(new BorderLayout());
        this.add(applicationPane);
        pack();
    }

    private void movePlayer(Direction direction) {
        try {
            List<DungeonObjectTrack> dungeonObjectTracks = gameSession.movePlayerGui(direction, "Andrew");
            SwingUtilities.invokeLater(() -> {
                activeDungeonTable.updateMap(dungeonObjectTracks);
            });
        } catch (GameNotification ex) {
            Logger.getLogger(DungeonEscapeGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void processNotification(GameNotification gameNotification) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void refresh() {
        this.revalidate();
        this.repaint();
        this.requestFocus();
    }

}
