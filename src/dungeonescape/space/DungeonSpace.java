/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.space;

import dungeonescape.dungeon.notifications.GameNotification;
import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.dungeonobject.characters.Ghost;
import dungeonescape.dungeonobject.construction.Construction;
import dungeonescape.dungeonobject.mine.Mine;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Andrew
 */
public class DungeonSpace {

    private Position position;
    private List<DungeonObject> dungeonObjects;

    public DungeonSpace(Position position) {
        this.position = position;
    }

    public DungeonSpace(Position position, DungeonObject dungeonObject) {
        this.position = position;

        if (dungeonObject != null && dungeonObject.getPosition() == null) {
            dungeonObject.setPosition(position);
        }

        dungeonObjects = new ArrayList<>();
        dungeonObjects.add(dungeonObject);
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;

        dungeonObjects.forEach(dungeonObject -> {
            dungeonObject.setPosition(position);
        });
    }

    public List<DungeonObject> getDungeonObjects() {
        return Collections.unmodifiableList(dungeonObjects);
    }

    public void addDungeonObject(DungeonObject dungeonObject) throws GameNotification {
        if (dungeonObject != null) {
            for (DungeonObject existingDungeonObject : dungeonObjects) {
//                existingDungeonObject.interact(dungeonObject);
            }
            dungeonObjects.add(dungeonObject);
        }
    }

    public void removeDungeonObject(DungeonObject dungeonObject) {
        dungeonObjects.remove(dungeonObject);
    }
    
    public void clearDungeonObjects() {
        dungeonObjects.clear();
    }

    public DungeonSpace dungeonObject(DungeonObject dungeonObject) throws GameNotification {
        addDungeonObject(dungeonObject);
        return this;
    }

    public DungeonSpaceType getVisibleDungeonSpaceType() {
        DungeonSpaceType visibleDungeonSpaceType = DungeonSpaceType.OPEN_SPACE;
        if (!dungeonObjects.isEmpty()) {
            visibleDungeonSpaceType = dungeonObjects.get(dungeonObjects.size() - 1).getDungeonSpaceType();
        }

        return visibleDungeonSpaceType;
    }

    public boolean containsDungeonSpaceType(DungeonSpaceType dungeonSpaceType) {
        return !dungeonObjects.stream()
                .noneMatch(obj -> obj.getDungeonSpaceType() == dungeonSpaceType);
    }

    public char getVisibleSpaceSymbol() {
        char visibleSpaceSymbol = DungeonSpaceType.OPEN_SPACE.getValue();
        if (!dungeonObjects.isEmpty()) {
            visibleSpaceSymbol = dungeonObjects.get(dungeonObjects.size() - 1).getDungeonSpaceType().getValue();
        }

        return visibleSpaceSymbol;
    }

    public boolean containsWall() {
        boolean containsWall = false;
        for (DungeonObject dungeonObject : dungeonObjects) {
            if (dungeonObject.getDungeonSpaceType() == DungeonSpaceType.WALL) {
                containsWall = true;
                break;
            }
        }
        return containsWall;
    }

    public boolean isEmpty() {
        return dungeonObjects.isEmpty();
    }

    public boolean isNotPermanentlyOccupied() {
        return dungeonObjects.stream()
                .noneMatch(obj -> {
                    return obj instanceof Construction
                            || obj instanceof Mine;
                });
    }

    private boolean isAllowedToOccupySpace(DungeonObject dungeonObject) {
        if (dungeonObject instanceof Ghost) {
            return true;
        }

        if (isEmpty()) {
            DungeonObject occupant = dungeonObjects.stream()
                    .filter(dungeonObj -> !(dungeonObj instanceof Ghost))
                    .findAny()
                    .orElse(null);
            return occupant == null;
        }

        return false;
    }

}
