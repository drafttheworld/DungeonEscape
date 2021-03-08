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

    COIN('$'),
    DUNGEON_MASTER('M'),
    FREEZE_MINE('*'),
    GHOST('~'),
    GUARD('G'),
    MYSTERY_BOX('?'),
    NON_VISIBLE_SPACE('X'),
    OPEN_SPACE(' '),
    PLAYER('P'),
    TELEPORT_LOCATION(' '),
    TELEPORT_MINE('@'),
    WALL('#');

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
