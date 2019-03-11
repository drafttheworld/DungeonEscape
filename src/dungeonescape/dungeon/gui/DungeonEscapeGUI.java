/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon.gui;

import dungeonescape.DungeonEscapeApplication;
import dungeonescape.dungeon.notifications.GameNotification;
import dungeonescape.dungeon.notifications.NotificationListener;
import dungeonescape.dungeonobject.DungeonObjectTrack;
import dungeonescape.play.Direction;
import dungeonescape.play.DungeonSize;
import dungeonescape.play.GameDifficulty;
import dungeonescape.play.GameSession;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
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

    private JPanel applicationPane;
    private JLabel loadingLabel;
    private JButton startButton;
    private JButton recenterButton;
//    private JScrollPane mapScrollPane;
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
//        mapScrollPane = new JScrollPane();
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

        startButton.setText("Start New Game");
        startButton.addActionListener((ActionEvent e) -> {

            applicationPane.remove(gamePane);
            loadingLabel.setText("Loading map...");
            applicationPane.add(loadingLabel, BorderLayout.CENTER);
            loadingLabel.setVisible(true);
            refresh();
            SwingUtilities.invokeLater(() -> {
                //Create the game session
                gameSession = new DungeonEscapeApplication().startNewGame("Andrew", GameDifficulty.HARD, DungeonSize.XLARGE);
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
                    activeDungeonTable.centerOnPlayer("Andrew");
                    refresh();
                });

                //Refresh the map
                refresh();
                activeDungeonTable.centerOnPlayer("Andrew");
            });

        });

        BufferedImage backgroundImage = ImageIO.read(DungeonTableCellRenderer.class.getResource("images/HeroVsDungeonMaster.png"));
        gamePane = new ImagePanel(backgroundImage);

        applicationPane.setLayout(new BorderLayout());
        applicationPane.add(startButton, BorderLayout.NORTH);
        applicationPane.add(gamePane, BorderLayout.CENTER);

        this.setLayout(new BorderLayout());
        this.add(applicationPane);
        this.setTitle("Dungeon Escape");
        pack();
    }

    private void movePlayer(Direction direction) {
        List<DungeonObjectTrack> dungeonObjectTracks = gameSession.movePlayerGui(direction, "Andrew");
        SwingUtilities.invokeLater(() -> {
            activeDungeonTable.updateMap(dungeonObjectTracks);
        });
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

    private JComponent buildDungeonScrollPane() {
        JScrollPane dungeonScrollPane = new JScrollPane();
        dungeonScrollPane.setColumnHeader(null);
        dungeonScrollPane.setPreferredSize(new Dimension(480, 480));
        dungeonScrollPane.setViewportView(activeDungeonTable);
        dungeonScrollPane.setVisible(true);
        dungeonScrollPane.setFocusable(false);
        return dungeonScrollPane;
    }

}
