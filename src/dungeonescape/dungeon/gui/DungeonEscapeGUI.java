/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon.gui;

import dungeonescape.dungeon.notifications.GameNotification;
import dungeonescape.dungeonobject.DungeonObjectTrack;
import dungeonescape.play.Direction;
import dungeonescape.play.GameSession;
import dungeonescape.space.DungeonSpaceType;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author Andrew
 */
public class DungeonEscapeGUI extends JFrame {

    public static final int CELL_SIZE = 30;

    private static final int NORTH_KEY_CODE = 38;
    private static final int SOUTH_KEY_CODE = 40;
    private static final int EAST_KEY_CODE = 39;
    private static final int WEST_KEY_CODE = 37;

    private JPanel startPage;
    private JLabel loadingLabel;
    private JButton startButton;
    private JButton recenterButton;
    private JScrollPane mapScrollPane;
    private JTable mapTable;

    private TableCellRenderer imageTableCellRenderer;

    private final GameSession gameSession;
    private int mapCenterX;
    private int mapCenterY;

    public DungeonEscapeGUI(GameSession gameSession) {
        this.gameSession = gameSession;
        initComponents(gameSession.getDungeonConfiguration().getDungeonWidth());
    }

    private void initComponents(int dungeonSize) {

        startPage = new JPanel();
        loadingLabel = new JLabel();
        startButton = new JButton();
        recenterButton = new JButton();
        mapTable = new JTable();
        mapScrollPane = new JScrollPane();
        mapTable = new JTable();

        imageTableCellRenderer = new DungeonTableCellRenderer();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        mapScrollPane.addKeyListener(new KeyListener() {
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
                //do nothing
            }

        });

        mapScrollPane.setColumnHeader(null);

        String dungeonMap = null;
        try {
            dungeonMap = gameSession.getPlayerMap("Andrew");
        } catch (GameNotification gameNotification) {
            System.out.println("There was a problem: " + gameNotification.getMessage());
        }

        buildMapTable(dungeonMap, dungeonSize);
        mapScrollPane.setViewportView(mapTable);
        mapScrollPane.setPreferredSize(new Dimension(480, 480));
        mapScrollPane.setVisible(false);

        loadingLabel.setText("Loading map...");

        startButton.setText("Start New Game");
        startButton.addActionListener((ActionEvent e) -> {
            mapScrollPane.setVisible(true);
            recenterButton.setVisible(true);
            mapScrollPane.requestFocus();
            this.revalidate();
            this.repaint();
            scroll(mapTable);
        });

        recenterButton.setText("Recenter Map");
        recenterButton.setVisible(false);
        recenterButton.addActionListener((ActionEvent e) -> {
            mapScrollPane.requestFocus();
            scroll(mapTable);
        });
        startPage.setLayout(new BorderLayout());
        startPage.setPreferredSize(new Dimension(480, 480));
        startPage.add(startButton, BorderLayout.NORTH);
        startPage.add(mapScrollPane, BorderLayout.CENTER);
        startPage.add(recenterButton, BorderLayout.SOUTH);

        this.setLayout(new BorderLayout());
        this.add(startPage);
        pack();
    }

    private void movePlayer(Direction direction) {
        try {
            List<DungeonObjectTrack> dungeonObjectTracks = gameSession.movePlayerGui(direction, "Andrew");
            SwingUtilities.invokeLater(() -> {
                updateMapTable(dungeonObjectTracks);
            });
        } catch (GameNotification ex) {
            Logger.getLogger(DungeonEscapeGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void buildMapTable(String map, int dungeonSize) {

        String[][] rows = populateMapRows(map, dungeonSize);

        String[] columns = new String[dungeonSize];
        for (int index = 0; index < columns.length; index++) {
            columns[index] = "";
        }

        mapTable.setModel(new DefaultTableModel(
                rows,
                columns
        ) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });

        mapTable.setRowHeight(CELL_SIZE);
        Enumeration<TableColumn> tableColumns = mapTable.getColumnModel().getColumns();
        while (tableColumns.hasMoreElements()) {
            TableColumn tableColumn = tableColumns.nextElement();
            tableColumn.setResizable(false);
            tableColumn.setMinWidth(CELL_SIZE);
            tableColumn.setMaxWidth(CELL_SIZE);
            tableColumn.setPreferredWidth(CELL_SIZE);
            tableColumn.setWidth(CELL_SIZE);
            tableColumn.setCellRenderer(imageTableCellRenderer);
        }

        mapTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        mapTable.setFocusable(false);
        mapTable.setRowSelectionAllowed(false);
        mapTable.setCellSelectionEnabled(false);
    }

    private void updateMapTable(List<DungeonObjectTrack> dungeonObjectTracks) {
        dungeonObjectTracks.forEach(dungeonObjectTrack -> {
            String mapSymbol;
            int row, col;
            if (dungeonObjectTrack.getPreviousPosition() != null) {
                mapSymbol = dungeonObjectTrack.getPreviousPositionSymbol();
                row = dungeonObjectTrack.getPreviousPosition().getPositionY();
                col = dungeonObjectTrack.getPreviousPosition().getPositionX();
                mapTable.setValueAt(mapSymbol, row, col);
            }
            
            mapSymbol = dungeonObjectTrack.getCurrentPositionSymbol();
            row = dungeonObjectTrack.getCurrentPosition().getPositionY();
            col = dungeonObjectTrack.getCurrentPosition().getPositionX();
            mapTable.setValueAt(mapSymbol, row, col);

            if (DungeonSpaceType.PLAYER.getValueString().equals(mapSymbol)) {
                mapCenterX = col;
                mapCenterY = row;
            }
        });
    }

    private String[][] populateMapRows(String mapString, int dungeonSize) {

        String[][] rows = new String[dungeonSize][dungeonSize];
        char[] dungeonMap = mapString.toCharArray();
        int mapIndex = 0;
        for (int row = 0; row < dungeonSize; row++) {
            for (int col = 0; col < dungeonSize; col++) {
                char mapChar = dungeonMap == null ? '#' : dungeonMap[mapIndex++];
                if (mapChar == '\n') {
                    mapChar = dungeonMap[mapIndex++];
                } else if (mapChar == 'P') {
                    mapCenterX = col;
                    mapCenterY = row;
                }
                rows[row][col] = "" + mapChar;
            }
        }

        return rows;
    }

    /**
     * Scroll to specified location. e.g. <tt>scroll(component, LEFT,
     * BOTTOM);</tt>.
     *
     * @param c JComponent to scroll.
     */
    private void scroll(JTable c) {
        Rectangle visible = c.getVisibleRect();
        int totalRowHeight = mapCenterY * c.getRowHeight();
        int totalColWidth = mapCenterX * c.getColumnModel().getColumn(0).getWidth();
        visible.y = (int) (totalRowHeight - (visible.getHeight() / 2));
        visible.x = (int) (totalColWidth - (visible.getWidth() / 2));
        c.scrollRectToVisible(visible);
    }

}
