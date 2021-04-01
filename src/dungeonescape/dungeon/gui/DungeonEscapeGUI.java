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
import dungeonescape.dungeonobject.powerups.PowerUpEnum;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
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
    private JButton startNewGameButton;
    private JButton recenterButton;
    private List<DungeonTable> dungeonTables;
    private DungeonTable activeDungeonTable;
    private JComponent gameConfigurationPanel;
    private JPanel introMenuPanel;
    private JPanel informationPanel;
    private JTextArea playerInformationTextArea;
    private JTextArea playerNotificationsTextArea;

    private GameSession gameSession;
    private Future backgroundAudioTrack;

    public DungeonEscapeGUI() throws IOException {
        initComponents();
    }

    private void initComponents() throws IOException {

        applicationPanel = new JPanel();
        applicationPanel.setLayout(new BorderLayout());
        applicationPanel.setPreferredSize(new Dimension(STARTING_WIDTH, STARTING_HEIGHT));

        recenterButton = new JButton();
        dungeonTables = new ArrayList<>();

        addMovementKeyListener();

        addResizeListener();

        gameConfigurationPanel = new ImagePanel(Image.START_SCREEN_BACKGROUND.getBufferedImage());
        applicationPanel.add(gameConfigurationPanel, BorderLayout.CENTER);

        introMenuPanel = buildIntroMenuPanel(applicationPanel);
        applicationPanel.add(introMenuPanel, BorderLayout.EAST);

        this.setLayout(new BorderLayout());
        this.add(applicationPanel);
        this.setTitle("Dungeon Escape");
        centerApplication();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        pack();

        NotificationManager.registerNotificationListener(this);
    }

    private JButton createStartNewGameButton() {

        JButton button = new JButton();
        button.setText("Start New Game");
        button.addActionListener((ActionEvent e) -> {
            if (backgroundAudioTrack != null) {
                backgroundAudioTrack.cancel(true);
            }
            applicationPanel.remove(gameConfigurationPanel);
            applicationPanel.remove(introMenuPanel);
            applicationPanel.remove(recenterButton);
            applicationPanel.remove(button);
            if (informationPanel != null) {
                applicationPanel.remove(informationPanel);
            }
            informationPanel = null;
            gameConfigurationPanel = new GameConfigurationPane(this);
            applicationPanel.add(gameConfigurationPanel, BorderLayout.CENTER);

            if (playerInformationTextArea != null) {
                playerInformationTextArea.setText(gameSession.getPlayerStats(false));
            }

            if (playerNotificationsTextArea != null) {
                playerNotificationsTextArea.setText("");
            }

            refresh();
        });

        return button;
    }

    private void centerApplication() {

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - STARTING_WIDTH) / 2);
        int y = (int) ((dimension.getHeight() - STARTING_HEIGHT) / 2);
        this.setLocation(x, y);
    }

    protected void startNewGame(DungeonConfiguration dungeonConfiguration) {

        applicationPanel.remove(gameConfigurationPanel);
        SwingUtilities.invokeLater(() -> {
            //Create the game session
            dungeonConfiguration.setPlayerName(PLAYER_NAME);
            gameSession = new DungeonEscapeApplication().startNewGame(dungeonConfiguration);
            activeDungeonTable = new DungeonTable(gameSession);
            dungeonTables.add(activeDungeonTable);

            //Once ready remove the loading label and add the map
            gameConfigurationPanel = buildDungeonScrollPane();
            applicationPanel.add(startNewGameButton, BorderLayout.NORTH);
            applicationPanel.add(gameConfigurationPanel, BorderLayout.CENTER);
            if (informationPanel == null) {
                informationPanel = buildPlayerInformationPane();
            } else {
                updateStats();
            }
            applicationPanel.add(informationPanel, BorderLayout.EAST);

            //Add the recenter button
            applicationPanel.add(recenterButton, BorderLayout.SOUTH);
            recenterButton.setVisible(true);
            recenterButton.setText("Recenter Map");
            recenterButton.addActionListener((ActionEvent ev) -> {
                gameConfigurationPanel.requestFocus();
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

        applicationPanel.remove(gameConfigurationPanel);
        applicationPanel.remove(informationPanel);
        gameConfigurationPanel = new ImagePanel(backgroundImage);
        applicationPanel.remove(recenterButton);
        applicationPanel.add(startNewGameButton, BorderLayout.NORTH);
        applicationPanel.add(gameConfigurationPanel, BorderLayout.CENTER);
        refresh();
    }

    private void refresh(JComponent component) {
        component.revalidate();
        component.repaint();
    }

    private void refresh() {
        this.revalidate();
        this.repaint();
        this.requestFocus();
    }

    private JPanel buildIntroMenuPanel(JPanel parent) {

        JPanel panel = new JPanel();

        int panelWidth = 150;
        panel.setPreferredSize(new Dimension(panelWidth, STARTING_HEIGHT));
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        JButton storyButton = new JButton("The Story");
        storyButton.setMinimumSize(new Dimension(panelWidth, 30));
        storyButton.setMaximumSize(new Dimension(panelWidth, 30));
        storyButton.setPreferredSize(new Dimension(panelWidth, 30));

        storyButton.addActionListener((ActionEvent e) -> {
            ImagePanel storyImage = new ImagePanel(Image.STORY.getBufferedImage());
            storyImage.addComponentListener(new ComponentListener() {
                @Override
                public void componentResized(ComponentEvent e) {
                    storyImage.revalidate();
                    storyImage.repaint();
                }

                @Override
                public void componentMoved(ComponentEvent e) {
                    // not used
                }

                @Override
                public void componentShown(ComponentEvent e) {
                    // not used
                }

                @Override
                public void componentHidden(ComponentEvent e) {
                    // not used
                }
            });
            parent.remove(gameConfigurationPanel);
            parent.add(storyImage, BorderLayout.CENTER);
            gameConfigurationPanel = storyImage;
            refresh(parent);
        });
        panel.add(storyButton);

        JButton instructionsButton = new JButton("Instructions");
        instructionsButton.setMinimumSize(new Dimension(panelWidth, 30));
        instructionsButton.setMaximumSize(new Dimension(panelWidth, 30));
        instructionsButton.setPreferredSize(new Dimension(panelWidth, 30));
        panel.add(instructionsButton);

        startNewGameButton = createStartNewGameButton();
        startNewGameButton.setMinimumSize(new Dimension(panelWidth, 30));
        startNewGameButton.setMaximumSize(new Dimension(panelWidth, 30));
        startNewGameButton.setPreferredSize(new Dimension(panelWidth, 30));
        panel.add(startNewGameButton);

        return panel;
    }

    private JPanel buildPlayerInformationPane() {

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.PAGE_AXIS));
        infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.setFocusable(false);

        JLabel playerInformationLabel = new JLabel("Player Stats");
        playerInformationLabel.setFont(new Font(Font.SERIF, Font.BOLD, 12));

        JPanel playerInformation = new JPanel();
        playerInformation.setPreferredSize(new Dimension(300, 300));
        playerInformation.setMaximumSize(new Dimension(300, 300));
        playerInformationTextArea = new JTextArea(gameSession.getPlayerStats(false));
        playerInformationTextArea.setPreferredSize(new Dimension(300, 200));
        playerInformationTextArea.setMaximumSize(new Dimension(300, 200));
        playerInformationTextArea.setFocusable(false);
        playerInformationTextArea.setEditable(false);
        playerInformationTextArea.setBorder(BorderFactory.createLineBorder(Color.black));

        playerInformation.add(playerInformationLabel);
        playerInformation.add(playerInformationTextArea);

        JPanel playerNotifications = new JPanel();
        playerNotifications.setPreferredSize(new Dimension(300, 75));
        playerNotifications.setMaximumSize(new Dimension(300, 75));

        JLabel playerNotificationsLabel = new JLabel("Player Notifications");
        playerNotificationsLabel.setFont(new Font(Font.SERIF, Font.BOLD, 12));

        playerNotificationsTextArea = new JTextArea();
        playerNotificationsTextArea.setPreferredSize(new Dimension(300, 100));
        playerNotificationsTextArea.setMaximumSize(new Dimension(300, 100));
        playerNotificationsTextArea.setFocusable(false);
        playerNotificationsTextArea.setEditable(false);
        playerNotificationsTextArea.setLineWrap(true);
        playerNotificationsTextArea.setBorder(BorderFactory.createLineBorder(Color.black));

        playerNotifications.add(playerNotificationsLabel);
        playerNotifications.add(playerNotificationsTextArea);

        JPanel powerUpParentPanel = new JPanel();
        powerUpParentPanel.setPreferredSize(new Dimension(300, 300));
        powerUpParentPanel.setMaximumSize(new Dimension(300, 300));

        JLabel playerPowerUpsLabel = new JLabel("Player Power-ups");
        playerPowerUpsLabel.setFont(new Font(Font.SERIF, Font.BOLD, 12));
        playerPowerUpsLabel.setPreferredSize(new Dimension(300, 20));
        playerPowerUpsLabel.setMaximumSize(new Dimension(300, 20));
        playerPowerUpsLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        playerPowerUpsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        playerPowerUpsLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        powerUpParentPanel.add(playerPowerUpsLabel);

        JPanel playerPowerUpsPanel = new JPanel();
        playerPowerUpsPanel.setPreferredSize(new Dimension(300, 200));
        playerPowerUpsPanel.setMaximumSize(new Dimension(300, 200));
        playerPowerUpsPanel.setLayout(new BoxLayout(playerPowerUpsPanel, BoxLayout.PAGE_AXIS));

        playerPowerUpsPanel.add(new PowerUpSubpanel(PowerUpEnum.INVINCIBILITY, gameSession.getPowerUpService(), this));
        playerPowerUpsPanel.add(new PowerUpSubpanel(PowerUpEnum.INVISIBILITY, gameSession.getPowerUpService(), this));
        playerPowerUpsPanel.add(new PowerUpSubpanel(PowerUpEnum.REPELLENT, gameSession.getPowerUpService(), this));
        playerPowerUpsPanel.add(new PowerUpSubpanel(PowerUpEnum.TERMINATOR, gameSession.getPowerUpService(), this));
        powerUpParentPanel.add(playerPowerUpsPanel);

        infoPanel.add(playerInformation);
        infoPanel.add(playerNotifications);
        infoPanel.add(playerPowerUpsLabel);
        infoPanel.add(powerUpParentPanel);

        infoPanel.setPreferredSize(new Dimension(310, 400));
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

    protected void updateStats() {

        String playerStats = gameSession.getPlayerStats(false);
        playerInformationTextArea.setText(playerStats);
        playerInformationTextArea.revalidate();
        playerInformationTextArea.repaint();
    }

    private void addMovementKeyListener() {

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

                if (playerNotificationsTextArea != null) {
                    playerNotificationsTextArea.setText("");
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
    }

    private void addResizeListener() {

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
    }

}
