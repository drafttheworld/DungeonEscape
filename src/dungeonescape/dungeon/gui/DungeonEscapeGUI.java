/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon.gui;

import dungeonescape.dungeon.notifications.GameNotification;
import dungeonescape.play.Direction;
import dungeonescape.play.GameSession;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author Andrew
 */
public class DungeonEscapeGUI extends JFrame {
    
    private static final int NORTH_KEY_CODE = 38;
    private static final int SOUTH_KEY_CODE = 40;
    private static final int EAST_KEY_CODE = 39;
    private static final int WEST_KEY_CODE = 37;

    private JButton buttonMoveWest;
    private JButton buttonMoveNorth;
    private JButton buttonMoveSouth;
    private JButton buttonMoveEast;
    private JPanel jPanel1;
    private JScrollPane mapScrollPane;
    private JTable mapTable;

    private final GameSession gameSession;
    private int mapCenterX;
    private int mapCenterY;

    public DungeonEscapeGUI(GameSession gameSession) {
        this.gameSession = gameSession;
        initComponents(gameSession.getDungeonConfiguration().getDungeonWidth());
    }

    private void initComponents(int dungeonSize) {
        mapScrollPane = new JScrollPane();
        mapTable = new JTable();
        jPanel1 = new JPanel();
        buttonMoveWest = new JButton();
        buttonMoveNorth = new JButton();
        buttonMoveSouth = new JButton();
        buttonMoveEast = new JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mapTable.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                //do nothing
            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {
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

        buildMapTable(dungeonMap, dungeonSize, mapTable);
        mapScrollPane.setViewportView(mapTable);

        buttonMoveWest.setText("West");
        buttonMoveWest.addActionListener((ActionEvent e) -> {
            movePlayer(Direction.WEST);
        });

        buttonMoveNorth.setText("North");
        buttonMoveNorth.addActionListener((ActionEvent e) -> {
            movePlayer(Direction.NORTH);
        });

        buttonMoveSouth.setText("South");
        buttonMoveSouth.addActionListener((ActionEvent e) -> {
            movePlayer(Direction.SOUTH);
        });

        buttonMoveEast.setText("East");
        buttonMoveEast.addActionListener((ActionEvent e) -> {
            movePlayer(Direction.EAST);
        });

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(364, 364, 364)
                                .addComponent(buttonMoveWest)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonMoveNorth)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonMoveSouth)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonMoveEast)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.CENTER)//BASELINE
                                        .addComponent(buttonMoveWest)
                                        .addComponent(buttonMoveNorth)
                                        .addComponent(buttonMoveSouth)
                                        .addComponent(buttonMoveEast)))
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(mapScrollPane)
                .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1)
                        .addGap(89, 89, 89))
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(mapScrollPane)
                        .addGap(20, 20, 20)
                        .addComponent(jPanel1)
                        .addContainerGap(19, Short.MAX_VALUE))
        );

        pack();

        //scroll to the center of the map
        scroll(mapTable);
    }

    private void movePlayer(Direction direction) {
        try {
            String mapString = gameSession.movePlayer(direction, "Andrew");
            SwingUtilities.invokeLater(() -> {
                buildMapTable(mapString, gameSession.getDungeonConfiguration().getDungeonWidth(), mapTable);
                scroll(mapTable);
            });
        } catch (GameNotification ex) {
            Logger.getLogger(DungeonEscapeGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void buildMapTable(String map, int dungeonSize, JTable mapTable) {

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

        int cellSize = 15;
        mapTable.setRowHeight(cellSize);
        Enumeration<TableColumn> tableColumns = mapTable.getColumnModel().getColumns();
        while (tableColumns.hasMoreElements()) {
            TableColumn tableColumn = tableColumns.nextElement();
            tableColumn.setResizable(false);
            tableColumn.setMinWidth(cellSize);
            tableColumn.setMaxWidth(cellSize);
            tableColumn.setPreferredWidth(cellSize);
            tableColumn.setWidth(cellSize);
        }

        mapTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        mapTable.setRowSelectionAllowed(false);
        mapTable.setCellSelectionEnabled(false);
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

    public static void main(String[] args) {
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(DungeonAppGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(DungeonAppGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(DungeonAppGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(DungeonAppGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new DungeonEscapeGUI().setVisible(true);
//            }
//        });
    }

}
