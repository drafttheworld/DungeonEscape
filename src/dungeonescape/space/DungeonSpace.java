/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.space;

import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.dungeonobject.DungeonObjectTrack;
import dungeonescape.dungeonobject.TeleportObject;
import dungeonescape.dungeonobject.characters.DungeonCharacter;
import dungeonescape.dungeonobject.characters.Guard;
import dungeonescape.dungeonobject.characters.Player;
import dungeonescape.dungeonobject.construction.Construction;
import dungeonescape.dungeonobject.mine.Mine;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Andrew
 */
public class DungeonSpace {

    private Position position;
    private boolean visible;
    private final List<DungeonObject> dungeonObjects;

    public DungeonSpace(Position position) {
        this.position = position;
        visible = false;
        dungeonObjects = new ArrayList<>();
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;

        dungeonObjects.forEach(dungeonObject -> {
            dungeonObject.setDungeonSpace(this);
        });
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public List<DungeonObject> getDungeonObjects() {
        return Collections.unmodifiableList(dungeonObjects);
    }

    public List<DungeonObjectTrack> addDungeonObject(DungeonObject dungeonObject) {
        List<DungeonObjectTrack> objectTracks = new ArrayList<>();

        boolean isTeleported = false;
        List<DungeonObject> existingDungeonObjects = new ArrayList<>(dungeonObjects);
        for (DungeonObject existingDungeonObject : existingDungeonObjects) {
            if ((existingDungeonObject instanceof TeleportObject
                    && dungeonObject instanceof Player)
                    || (existingDungeonObject instanceof Player
                    && dungeonObject instanceof TeleportObject)) {
                isTeleported = true;
            }

            objectTracks.addAll(existingDungeonObject.interact(dungeonObject));

            if (dungeonObject instanceof Player && existingDungeonObject instanceof Mine) {
                dungeonObjects.remove(existingDungeonObject);
            }
        }

        if (!isTeleported) {
            dungeonObject.setDungeonSpace(this);
            if (!this.isEmpty()) {
                DungeonObject tailObject = this.getDungeonObjects()
                        .get(this.getDungeonObjects().size() - 1);
                if (tailObject instanceof Player) {
                    dungeonObjects.add(this.getDungeonObjects().size() - 1, dungeonObject);
                } else {
                    dungeonObjects.add(dungeonObject);
                }
            } else {
                dungeonObjects.add(dungeonObject);
            }

        }

        return objectTracks;
    }

    public void removeDungeonObject(DungeonObject dungeonObject) {
        dungeonObjects.remove(dungeonObject);
    }

    public void clearDungeonObjects() {
        dungeonObjects.clear();
    }

    public DungeonSpace dungeonObject(DungeonObject dungeonObject) {
        addDungeonObject(dungeonObject);
        return this;
    }

    public DungeonSpaceType getVisibleDungeonSpaceType() {
        if (!visible) {
            return DungeonSpaceType.NON_VISIBLE_SPACE;
        } else if (dungeonObjects.isEmpty()) {
            return DungeonSpaceType.OPEN_SPACE;
        }

        DungeonSpaceType visibleDungeonSpaceType = DungeonSpaceType.OPEN_SPACE;
        for (int index = dungeonObjects.size() - 1; index >= 0; index--) {
            DungeonObject dungeonObject = dungeonObjects.get(index);
            if (dungeonObject instanceof DungeonCharacter
                    && !((DungeonCharacter) dungeonObject).isActive()) {
                continue;
            } else if (dungeonObject instanceof Mine
                    && !((Mine) dungeonObject).isActive()) {
                continue;
            }

            visibleDungeonSpaceType = dungeonObject.getDungeonSpaceType();
            break;
        }
        return visibleDungeonSpaceType;
    }

    public boolean containsDungeonSpaceType(DungeonSpaceType dungeonSpaceType) {
        return !dungeonObjects.stream()
                .noneMatch(obj -> obj.getDungeonSpaceType() == dungeonSpaceType);
    }

    public char getVisibleSpaceSymbol() {
        return getVisibleDungeonSpaceType().getValue();
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

}
