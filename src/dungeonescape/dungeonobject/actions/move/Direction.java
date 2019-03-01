/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.actions.move;

/**
 *
 * @author Andrew
 */
public enum Direction {
    
    NORTH("north"),
    SOUTH("south"),
    EAST("east"),
    WEST("west");
    
    private final String value;
    
    private Direction(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
}
