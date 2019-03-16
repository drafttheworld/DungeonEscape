/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject;

import dungeonescape.space.Position;

/**
 *
 * @author Andrew
 */
public class DungeonObjectTrack {
    
    private Position previousPosition;
    private String previousPositionSymbol;
    private String currentPositionSymbol;
    private DungeonObject dungeonObject;
    
    public DungeonObjectTrack(DungeonObject dungeonObject, String currentPositionSymbol) {
        this.dungeonObject = dungeonObject;
        this.currentPositionSymbol = currentPositionSymbol;
    }

    public Position getPreviousPosition() {
        return previousPosition;
    }
    
    public void setPreviousPosition(Position previousPosition) {
        this.previousPosition = previousPosition;
    }

    public void setPreviousPositionSymbol(String previousPositionSymbol) {
        this.previousPositionSymbol = previousPositionSymbol;
    }
    
    public String getPreviousPositionSymbol() {
        return previousPositionSymbol;
    }
    
    public String getCurrentPositionSymbol() {
        return currentPositionSymbol;
    }

    public void setCurrentPositionSymbol(String currentPositionSymbol) {
        this.currentPositionSymbol = currentPositionSymbol;
    }

    public Position getCurrentPosition() {
        return dungeonObject.getPosition();
    }

    public DungeonObject getDungeonObject() {
        return dungeonObject;
    }

    public void setDungeonObject(DungeonObject dungeonObject) {
        this.dungeonObject = dungeonObject;
    }
    
    public boolean isMoved() {
        if (previousPosition == null) {
            return true;
        }
        return !previousPosition.equals(dungeonObject.getPosition());
    }
    
}
