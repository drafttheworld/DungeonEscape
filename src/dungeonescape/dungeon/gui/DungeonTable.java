/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon.gui;

import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.dungeonobject.characters.Player;
import dungeonescape.play.GameSession;
import dungeonescape.dungeon.space.DungeonSpace;
import java.awt.Rectangle;
import java.util.Enumeration;
import java.util.Set;
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

        int dungeonSize = gameSession.getDungeonConfiguration().getDungeonWidth();

        DungeonSpace[][] rows = gameSession.getDungeon().getDungeon();
        Player player = gameSession.getDungeon().getPlayer();
        mapCenterX = player.getPosition().getPositionX();
        mapCenterY = player.getPosition().getPositionY();

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

    protected void updateMap(Set<DungeonSpace> dungeonSpacesToUpdate) {

        dungeonSpacesToUpdate.parallelStream().forEach(dungeonSpace -> {

            int row = dungeonSpace.getPosition().getPositionY();
            int col = dungeonSpace.getPosition().getPositionX();
            this.setValueAt(dungeonSpace, row, col);

            DungeonObject dungeonObject = dungeonSpace.getVisibleDungeonObject();
            if (dungeonObject instanceof Player) {
                mapCenterX = col;
                mapCenterY = row;
            }
        });

        centerOnPlayer();
    }

    public void centerOnPlayer() {

        Rectangle currentlyVisible = this.getVisibleRect();
        Rectangle nextVisible = new Rectangle(currentlyVisible);
        int totalRowHeight = mapCenterY * CELL_SIZE;
        int totalColWidth = mapCenterX * CELL_SIZE;
        nextVisible.y = (int) (totalRowHeight - (currentlyVisible.getHeight() / 2));
        nextVisible.x = (int) (totalColWidth - (currentlyVisible.getWidth() / 2));
        this.scrollRectToVisible(nextVisible);
    }

    public int getMapCenterX() {
        return mapCenterX;
    }

    public int getMapCenterY() {
        return mapCenterY;
    }

}
