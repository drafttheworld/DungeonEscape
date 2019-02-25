/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.space;

/**
 *
 * @author Andrew
 */
public enum DungeonSpaceType {

    WALL('#'),
    OPEN_SPACE(' '),
    FREEZE_MINE('*'),
    TELEPORT_MINE('@'),
    TELEPORT_LOCATION(' '),
    PLAYER('$'),
    GHOST('~'),
    DUNGEON_MASTER('X');

    private final char value;

    private DungeonSpaceType(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }

}
