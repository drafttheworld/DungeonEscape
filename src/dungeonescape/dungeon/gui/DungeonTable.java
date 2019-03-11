/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon.gui;

import dungeonescape.dungeon.notifications.PlayerNotFoundNotification;
import dungeonescape.dungeonobject.DungeonObjectTrack;
import dungeonescape.play.GameSession;
import dungeonescape.space.DungeonSpaceType;
import java.awt.Rectangle;
import java.util.Enumeration;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author Andrew
 */
public class DungeonTable extends JTable {

    public static final int CELL_SIZE = 30;

    final int msBetweenIterations = 10;

    private final GameSession gameSession;

    private final TableCellRenderer imageTableCellRenderer;

    private int mapCenterX;
    private int mapCenterY;

    public DungeonTable(GameSession gameSession) {
        this.gameSession = gameSession;
        imageTableCellRenderer = new DungeonTableCellRenderer();
        initTable();
    }

    private void initTable() {
        String player = gameSession.getDungeonConfiguration().getPlayerNames().get(0);
        int dungeonSize = gameSession.getDungeonConfiguration().getDungeonWidth();

        String[][] rows = populateMapRows(gameSession.getPlayerMap(player), dungeonSize);
        for (int row = 0; row < dungeonSize; row++) {
            for (int col = 0; col < dungeonSize; col++) {
                if (DungeonSpaceType.PLAYER.getValueString().equals(rows[row][col])) {
                    mapCenterX = col;
                    mapCenterY = row;
                }
            }
        }

        String[] columns = new String[dungeonSize];
        for (int index = 0; index < columns.length; index++) {
            columns[index] = "";
        }

        this.setModel(new DefaultTableModel(
                rows,
                columns
        ) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });

        Enumeration<TableColumn> tableColumns = this.getColumnModel().getColumns();
        while (tableColumns.hasMoreElements()) {
            TableColumn tableColumn = tableColumns.nextElement();
            tableColumn.setResizable(false);
            tableColumn.setMinWidth(CELL_SIZE);
            tableColumn.setMaxWidth(CELL_SIZE);
            tableColumn.setPreferredWidth(CELL_SIZE);
            tableColumn.setWidth(CELL_SIZE);
            tableColumn.setCellRenderer(imageTableCellRenderer);
        }

        this.setRowHeight(CELL_SIZE);
        this.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        this.setFocusable(false);
        this.setRowSelectionAllowed(false);
        this.setCellSelectionEnabled(false);
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

    protected void updateMap(List<DungeonObjectTrack> dungeonObjectTracks) {
        dungeonObjectTracks.forEach(dungeonObjectTrack -> {

            String mapSymbol;
            int row, col;
            if (dungeonObjectTrack.getPreviousPosition() != null) {
                mapSymbol = dungeonObjectTrack.getPreviousPositionSymbol();
                row = dungeonObjectTrack.getPreviousPosition().getPositionY();
                col = dungeonObjectTrack.getPreviousPosition().getPositionX();
                this.setValueAt(mapSymbol, row, col);
            }

            mapSymbol = dungeonObjectTrack.getCurrentPositionSymbol();
            row = dungeonObjectTrack.getCurrentPosition().getPositionY();
            col = dungeonObjectTrack.getCurrentPosition().getPositionX();
            this.setValueAt(mapSymbol, row, col);

            if (DungeonSpaceType.PLAYER.getValueString().equals(mapSymbol)) {
                mapCenterX = col;
                mapCenterY = row;
            }
        });

        centerOnPlayer("Andrew");
    }

    public void centerOnPlayer(String player) {
        Rectangle currentlyVisible = this.getVisibleRect();
        Rectangle nextVisible = new Rectangle(currentlyVisible);
        int totalRowHeight = mapCenterY * CELL_SIZE;
        int totalColWidth = mapCenterX * CELL_SIZE;
        nextVisible.y = (int) (totalRowHeight - (currentlyVisible.getHeight() / 2));
        nextVisible.x = (int) (totalColWidth - (currentlyVisible.getWidth() / 2));
        this.scrollRectToVisible(nextVisible);
    }

//    public void centerOnPlayer(String player) {
//        Rectangle currentlyVisible = this.getVisibleRect();
//        Rectangle nextVisible = new Rectangle(currentlyVisible);
//        int totalRowHeight = mapCenterY * CELL_SIZE;
//        int totalColWidth = mapCenterX * CELL_SIZE;
//        nextVisible.y = (int) (totalRowHeight - (currentlyVisible.getHeight() / 2));
//        nextVisible.x = (int) (totalColWidth - (currentlyVisible.getWidth() / 2));
//        int deltaY = nextVisible.y - currentlyVisible.y;
//        int deltaX = nextVisible.x - currentlyVisible.x;
//        
//        final DungeonTable dungeonTable = this;
//
//        Timer scrollTimer = new Timer(msBetweenIterations, new ActionListener() {
//            int currentIteration = 0;
//            final long animationTime = 150; // milliseconds
//            final long nsBetweenIterations = msBetweenIterations * 1000000; // nanoseconds
//            final long startTime = System.nanoTime() - nsBetweenIterations; // Make the animation move on the first iteration
//            final long targetCompletionTime = startTime + animationTime * 1000000;
//            final long targetElapsedTime = targetCompletionTime - startTime;
//            
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                long timeSinceStart = System.nanoTime() - startTime;
//                double percentComplete = Math.min(1.0, (double) timeSinceStart / targetElapsedTime);
//
//                double factor = getFactor(percentComplete);
//                nextVisible.y = (int) Math.round(dungeonTable.getVisibleRect().y + deltaY * factor);
//                nextVisible.x = (int) Math.round(dungeonTable.getVisibleRect().x + deltaX * factor);
//                dungeonTable.scrollRectToVisible(nextVisible);
//                if (timeSinceStart >= targetElapsedTime) {
//                    ((Timer) e.getSource()).stop();
//                }
//            }
//        });
//        scrollTimer.setInitialDelay(0);
//        scrollTimer.start();
//        
//        
//        this.scrollRectToVisible(nextVisible);
//    }
//
//    private double getFactor(double percentComplete) {
//        return percentComplete;
//    }
    public int getMapCenterX() {
        return mapCenterX;
    }

    public int getMapCenterY() {
        return mapCenterY;
    }

}
