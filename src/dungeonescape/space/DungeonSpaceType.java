/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.space;

import java.util.Arrays;

/**
 *
 * @author Andrew
 */
public enum DungeonSpaceType {

    WALL('#'),
    NON_VISIBLE_SPACE('X'),
    OPEN_SPACE(' '),
    FREEZE_MINE('*'),
    TELEPORT_MINE('@'),
    TELEPORT_LOCATION(' '),
    PLAYER('P'),
    GHOST('~'),
    GUARD('G'),
    DUNGEON_MASTER('M');

    private final char value;

    private DungeonSpaceType(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }

    public String getValueString() {
        return "" + value;
    }

    public static DungeonSpaceType getDungeonSpaceType(String symbol) {
        return Arrays.asList(DungeonSpaceType.values()).stream()
                .filter(dst -> dst.getValue() == symbol.charAt(0))
                .findFirst()
                .orElse(null);
    }

}
