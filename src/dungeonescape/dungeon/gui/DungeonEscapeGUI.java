/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon.gui;

import dungeonescape.DungeonEscapeApplication;
import dungeonescape.dungeon.DungeonConfiguration;
import dungeonescape.dungeon.gui.images.Image;
import dungeonescape.dungeon.gui.soundfx.AudioPlayer;
import dungeonescape.dungeon.gui.soundfx.AudioTrack;
import dungeonescape.dungeon.notifications.GameNotification;
import dungeonescape.dungeon.notifications.LossNotification;
import dungeonescape.dungeon.notifications.NotificationListener;
import dungeonescape.dungeon.notifications.NotificationManager;
import dungeonescape.dungeon.notifications.WinNotification;
import dungeonescape.play.Direction;
import dungeonescape.play.GameSession;
import dungeonescape.dungeon.space.DungeonSpace;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
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

    private static final int STARTING_HEIGHT = 600;
    private static final int STARTING_WIDTH = 900;

    private static final String PLAYER_NAME = "Hero";

    private static final ExecutorService backgroundMusicExecutorService = Executors.newSingleThreadExecutor();

    private boolean gameOver = true;

    private JPanel applicationPanel;
    private JLabel loadingLabel;
    private JButton startButton;
    private JButton recenterButton;
    private List<DungeonTable> dungeonTables;
    private DungeonTable activeDungeonTable;
    private JComponent gameConfigurationPane;
    private JPanel informationPanel;
    private TextArea playerInformationTextArea;
    private TextArea playerNotificationsTextArea;

    private DungeonEscapeApplication dungeonEscapeApplication;
    private GameSession gameSession;
    private Future backgroundAudioTrack;

    public DungeonEscapeGUI(DungeonEscapeApplication dungeonEscapeApplication) throws IOException {
        this.dungeonEscapeApplication = dungeonEscapeApplication;
        initComponents();
    }

    private void initComponents() throws IOException {

        applicationPanel = new JPanel();
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

                if (gameOver) {
                    return;
                }

                switch (e.getKeyCode()) {
                    case NORTH_KEY_CODE:
                        movePlayer(Direction.NORTH);
                        updateStats();
                        break;
                    case SOUTH_KEY_CODE:
                        movePlayer(Direction.SOUTH);
                        updateStats();
                        break;
                    case EAST_KEY_CODE:
                        movePlayer(Direction.EAST);
                        updateStats();
                        break;
                    case WEST_KEY_CODE:
                        movePlayer(Direction.WEST);
                        updateStats();
                        break;
                    default:
                        //do nothing
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // do nothing
            }

        });

        this.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                applicationPanel.revalidate();
                applicationPanel.repaint();
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

        startButton.setText("Start New Game");
        startButton.addActionListener((ActionEvent e) -> {
            if (backgroundAudioTrack != null) {
                System.out.println("cancelled audio track.");
                backgroundAudioTrack.cancel(true);
            }
            applicationPanel.remove(gameConfigurationPane);
            applicationPanel.remove(recenterButton);
            applicationPanel.remove(startButton);
            if (informationPanel != null) {
                applicationPanel.remove(informationPanel);
            }
            gameConfigurationPane = new GameConfigurationPane(this);
            applicationPanel.add(gameConfigurationPane, BorderLayout.CENTER);

            if (playerInformationTextArea != null) {
                playerInformationTextArea.setText(gameSession.getPlayerStats());
            }

            if (playerNotificationsTextArea != null) {
                playerNotificationsTextArea.setText("");
            }

            refresh();
        });

        gameConfigurationPane = new ImagePanel(Image.START_SCREEN_BACKGROUND.getBufferedImage());

        applicationPanel.setLayout(new BorderLayout());
        applicationPanel.setPreferredSize(new Dimension(STARTING_WIDTH, STARTING_HEIGHT));
        applicationPanel.add(startButton, BorderLayout.NORTH);
        applicationPanel.add(gameConfigurationPane, BorderLayout.CENTER);

        this.setLayout(new BorderLayout());
        this.add(applicationPanel);
        this.setTitle("Dungeon Escape");
        centerApplication();
        pack();

        NotificationManager.registerNotificationListener(this);
    }

    private void centerApplication() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - STARTING_WIDTH) / 2);
        int y = (int) ((dimension.getHeight() - STARTING_HEIGHT) / 2);
        this.setLocation(x, y);
    }

    protected void startNewGame(DungeonConfiguration dungeonConfiguration) {
        applicationPanel.remove(gameConfigurationPane);
        loadingLabel.setText("Loading map...");
        applicationPanel.add(loadingLabel, BorderLayout.CENTER);
        loadingLabel.setVisible(true);
        SwingUtilities.invokeLater(() -> {
            //Create the game session
            dungeonConfiguration.setPlayerName(PLAYER_NAME);
            gameSession = new DungeonEscapeApplication().startNewGame(dungeonConfiguration);
            activeDungeonTable = new DungeonTable(gameSession);
            dungeonTables.add(activeDungeonTable);

            //Once ready remove the loading label and add the map
            gameConfigurationPane = buildDungeonScrollPane();
            applicationPanel.remove(loadingLabel);
            applicationPanel.add(startButton, BorderLayout.NORTH);
            applicationPanel.add(gameConfigurationPane, BorderLayout.CENTER);
            if (informationPanel == null) {
                informationPanel = buildInformationPane();
            } else {
                updateStats();
            }
            applicationPanel.add(informationPanel, BorderLayout.EAST);

            //Add the recenter button
            applicationPanel.add(recenterButton, BorderLayout.SOUTH);
            recenterButton.setVisible(true);
            recenterButton.setText("Recenter Map");
            recenterButton.addActionListener((ActionEvent ev) -> {
                gameConfigurationPane.requestFocus();
                activeDungeonTable.centerOnPlayer();
                refresh();
            });

            //Refresh the map
            refresh();
            activeDungeonTable.centerOnPlayer();
            gameOver = false;
            backgroundAudioTrack
                = backgroundMusicExecutorService.submit(new AudioPlayer(AudioTrack.BACKGROUND_EPIC));
        });
    }

    private void movePlayer(Direction direction) {
        Set<DungeonSpace> dungeonSpaces = gameSession.movePlayerGui(direction);
        activeDungeonTable.updateMap(dungeonSpaces);
    }

    @Override
    public void processNotification(GameNotification gameNotification) {
        try {
            if (gameNotification instanceof LossNotification) {
                if (backgroundAudioTrack != null) {
                    backgroundAudioTrack.cancel(true);
                }
                displayNotificationPane(Image.GAME_LOST.getBufferedImage());
                gameOver = true;
            } else if (gameNotification instanceof WinNotification) {
                if (backgroundAudioTrack != null) {
                    backgroundAudioTrack.cancel(true);
                }
                displayNotificationPane(Image.GAME_WON.getBufferedImage());
                gameOver = true;
            } else {
                playerNotificationsTextArea.setText(gameNotification.getNotificationMessage());
            }
        } catch (IOException ex) {
            Logger.getLogger(DungeonEscapeGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void displayNotificationPane(BufferedImage backgroundImage) throws IOException {
        applicationPanel.remove(gameConfigurationPane);
        applicationPanel.remove(informationPanel);
        gameConfigurationPane = new ImagePanel(backgroundImage);
        applicationPanel.remove(recenterButton);
        applicationPanel.add(startButton, BorderLayout.NORTH);
        applicationPanel.add(gameConfigurationPane, BorderLayout.CENTER);
        refresh();
    }

    private void refresh() {
        this.revalidate();
        this.repaint();
        this.requestFocus();
    }

    private JPanel buildInformationPane() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.PAGE_AXIS));
        infoPanel.setFocusable(false);

        JLabel playerInformationLabel = new JLabel("Player Stats");
        playerInformationLabel.setFont(new Font(Font.SERIF, Font.BOLD, 12));

        JPanel playerInformation = new JPanel();
        playerInformationTextArea = new TextArea(gameSession.getPlayerStats());
        playerInformationTextArea.setPreferredSize(new Dimension(300, 200));
        playerInformationTextArea.setFocusable(false);
        playerInformationTextArea.setEditable(false);

        playerInformation.add(playerInformationLabel);
        playerInformation.add(playerInformationTextArea);

        JLabel playerNotificationsLabel = new JLabel("Player Notifications");
        playerInformationLabel.setFont(new Font(Font.SERIF, Font.BOLD, 12));

        JPanel playerNotifications = new JPanel();
        playerNotificationsTextArea = new TextArea();
        playerNotificationsTextArea.setPreferredSize(new Dimension(300, 200));
        playerNotificationsTextArea.setFocusable(false);
        playerNotificationsTextArea.setEditable(false);

        playerNotifications.add(playerNotificationsLabel);
        playerNotifications.add(playerNotificationsTextArea);

        infoPanel.add(playerInformation);
        infoPanel.add(playerNotifications);

        infoPanel.setPreferredSize(new Dimension(300, 400));

        infoPanel.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                infoPanel.revalidate();
                infoPanel.repaint();
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
        return infoPanel;
    }

    private JComponent buildDungeonScrollPane() {
        JScrollPane dungeonScrollPane = new JScrollPane();
        dungeonScrollPane.setColumnHeader(null);
        dungeonScrollPane.setPreferredSize(new Dimension(900, 600));
        dungeonScrollPane.setViewportView(activeDungeonTable);
        dungeonScrollPane.setVisible(true);
        dungeonScrollPane.setFocusable(false);

        dungeonScrollPane.addComponentListener(new ComponentListener() {

            @Override
            public void componentResized(ComponentEvent e) {
                activeDungeonTable.centerOnPlayer();
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

    private void updateStats() {
        String playerStats = gameSession.getPlayerStats();
        playerInformationTextArea.setText(playerStats);
        playerInformationTextArea.revalidate();
        playerInformationTextArea.repaint();
    }

}
