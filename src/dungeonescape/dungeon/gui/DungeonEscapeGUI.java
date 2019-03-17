/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon.gui;

import dungeonescape.DungeonEscapeApplication;
import dungeonescape.dungeon.notifications.GameNotification;
import dungeonescape.dungeon.notifications.LossNotification;
import dungeonescape.dungeon.notifications.NotificationListener;
import dungeonescape.dungeon.notifications.NotificationManager;
import dungeonescape.dungeon.notifications.WinNotification;
import dungeonescape.dungeonobject.DungeonObjectTrack;
import dungeonescape.play.Direction;
import dungeonescape.play.DungeonSize;
import dungeonescape.play.GameDifficulty;
import dungeonescape.play.GameSession;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Andrew
 */
public class DungeonEscapeGUI extends JFrame implements NotificationListener {

    private static final int NORTH_KEY_CODE = 38;
    private static final int SOUTH_KEY_CODE = 40;
    private static final int EAST_KEY_CODE = 39;
    private static final int WEST_KEY_CODE = 37;

    private static final String APP_IMAGE = "images/hero_vs_dungeon_master.png";
    private static final String LOSS_IMAGE = "images/notifications/game_lost.png";
    private static final String WIN_IMAGE = "images/notifications/game_won.png";

    private static final String PLAYER_NAME = "Andrew";

    private JPanel applicationPane;
    private JLabel loadingLabel;
    private JButton startButton;
    private JButton recenterButton;
    private List<DungeonTable> dungeonTables;
    private DungeonTable activeDungeonTable;
    private JComponent gamePane;

    DungeonEscapeApplication dungeonEscapeApplication;
    private GameSession gameSession;

    public DungeonEscapeGUI() throws IOException {
        initComponents();
    }

    private void initComponents() throws IOException {

        applicationPane = new JPanel();
        loadingLabel = new JLabel();
        startButton = new JButton();
        recenterButton = new JButton();
        dungeonTables = new ArrayList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                //do nothing
            }

            @Override
            public void keyPressed(KeyEvent e) {
                //do nothing
            }

            @Override
            public void keyReleased(KeyEvent e) {
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

        });

        dungeonEscapeApplication = new DungeonEscapeApplication();

        startButton.setText("Start New Game");
        startButton.addActionListener((ActionEvent e) -> {

            applicationPane.remove(gamePane);
            applicationPane.remove(startButton);
            gamePane = new GameConfigurationPane(this);
            applicationPane.add(gamePane, BorderLayout.CENTER);
            refresh();
        });

        BufferedImage backgroundImage = ImageIO.read(DungeonEscapeGUI.class.getResource(APP_IMAGE));
        gamePane = new ImagePanel(backgroundImage);

        applicationPane.setLayout(new BorderLayout());
        applicationPane.add(startButton, BorderLayout.NORTH);
        applicationPane.add(gamePane, BorderLayout.CENTER);

        this.setLayout(new BorderLayout());
        this.add(applicationPane);
        this.setTitle("Dungeon Escape");
        pack();

        NotificationManager.registerNotificationListener(this);
    }

    protected void startNewGame(GameDifficulty gameDifficulty, DungeonSize dungeonSize) {
        applicationPane.remove(gamePane);
        loadingLabel.setText("Loading map...");
        applicationPane.add(loadingLabel, BorderLayout.CENTER);
        loadingLabel.setVisible(true);
        SwingUtilities.invokeLater(() -> {
            //Create the game session
            gameSession = new DungeonEscapeApplication().startNewGame(PLAYER_NAME, gameDifficulty, dungeonSize);
            activeDungeonTable = new DungeonTable(gameSession);
            dungeonTables.add(activeDungeonTable);

            //Once ready remove the loading label and add the map
            gamePane = buildDungeonScrollPane();
            applicationPane.remove(loadingLabel);
            applicationPane.add(gamePane, BorderLayout.CENTER);

            //Add the recenter button
            applicationPane.add(recenterButton, BorderLayout.SOUTH);
            recenterButton.setVisible(true);
            recenterButton.setText("Recenter Map");
            recenterButton.addActionListener((ActionEvent ev) -> {
                gamePane.requestFocus();
                activeDungeonTable.centerOnPlayer(PLAYER_NAME);
                refresh();
            });

            //Refresh the map
            refresh();
            activeDungeonTable.centerOnPlayer(PLAYER_NAME);
        });
    }

    private void movePlayer(Direction direction) {
        List<DungeonObjectTrack> dungeonObjectTracks = gameSession.movePlayerGui(direction, PLAYER_NAME);
        activeDungeonTable.updateMap(dungeonObjectTracks);
    }

    @Override
    public void processNotification(GameNotification gameNotification) {
        try {
            if (gameNotification instanceof LossNotification) {
                displayNotificationPane(LOSS_IMAGE);
            } else if (gameNotification instanceof WinNotification) {
                displayNotificationPane(WIN_IMAGE);
            }
        } catch (IOException ex) {
            Logger.getLogger(DungeonEscapeGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void displayNotificationPane(String notificationImage) throws IOException {
        BufferedImage backgroundImage = ImageIO.read(DungeonEscapeGUI.class.getResource(notificationImage));
        applicationPane.remove(gamePane);
        gamePane = new ImagePanel(backgroundImage);
        applicationPane.remove(recenterButton);
        applicationPane.add(gamePane, BorderLayout.CENTER);
        refresh();
    }

    private void refresh() {
        this.revalidate();
        this.repaint();
        this.requestFocus();
    }

    private JComponent buildDungeonScrollPane() {
        JScrollPane dungeonScrollPane = new JScrollPane();
        dungeonScrollPane.setColumnHeader(null);
        dungeonScrollPane.setPreferredSize(new Dimension(480, 480));
        dungeonScrollPane.setViewportView(activeDungeonTable);
        dungeonScrollPane.setVisible(true);
        dungeonScrollPane.setFocusable(false);

        dungeonScrollPane.addComponentListener(new ComponentListener() {

            @Override
            public void componentResized(ComponentEvent e) {
                activeDungeonTable.centerOnPlayer(PLAYER_NAME);
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                //do nothing
            }

            @Override
            public void componentShown(ComponentEvent e) {
                //do nothing
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                //do nothing
            }

        });
        return dungeonScrollPane;
    }

}
