/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon.space;

import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.dungeonobject.NonVisibleSpace;
import dungeonescape.dungeonobject.OpenSpace;
import dungeonescape.dungeonobject.TeleportObject;
import dungeonescape.dungeonobject.characters.DungeonCharacter;
import dungeonescape.dungeonobject.characters.Ghost;
import dungeonescape.dungeonobject.characters.NonPlayerCharacter;
import dungeonescape.dungeonobject.characters.Player;
import dungeonescape.dungeonobject.construction.Construction;
import dungeonescape.dungeonobject.mine.Mine;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Andrew
 */
public class DungeonSpace implements Comparable<DungeonSpace> {

    private final long comparisonId = ThreadLocalRandom.current().nextLong();

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

    public List<DungeonSpace> addDungeonObject(DungeonObject dungeonObject) {

        List<DungeonSpace> dungeonSpaces = new ArrayList<>();

        boolean isTeleported = false;
        List<DungeonObject> existingDungeonObjects = new ArrayList<>(dungeonObjects);
        for (DungeonObject existingDungeonObject : existingDungeonObjects) {
            if ((existingDungeonObject instanceof TeleportObject && dungeonObject instanceof Player)
                || (existingDungeonObject instanceof Player && dungeonObject instanceof TeleportObject)
                || (existingDungeonObject instanceof Player && dungeonObject instanceof Ghost)) {

                isTeleported = true;
            }

            dungeonSpaces.addAll(existingDungeonObject.interact(dungeonObject));

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

        return dungeonSpaces;
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

    public DungeonObject getVisibleDungeonObject() {

        if (!visible) {
            return new NonVisibleSpace();
        } else if (dungeonObjects.isEmpty()) {
            return new OpenSpace();
        }

        DungeonObject visibleDungeonObject = null;
        for (int index = dungeonObjects.size() - 1; index >= 0; index--) {
            DungeonObject dungeonObject = dungeonObjects.get(index);
            if (dungeonObject instanceof Mine && !((Mine) dungeonObject).isActive()
                || (dungeonObject instanceof NonPlayerCharacter
                && !isNonPlayerCharacterVisible((NonPlayerCharacter) dungeonObject))) {
                continue;
            }

            visibleDungeonObject = dungeonObject;
            break;
        }

        if (visibleDungeonObject == null) {
            visibleDungeonObject = new OpenSpace();
        }

        return visibleDungeonObject;
    }

    private boolean isNonPlayerCharacterVisible(NonPlayerCharacter nonPlayerCharacter) {

        if (!nonPlayerCharacter.isActive()) {
            return false;
        }

        Player player = nonPlayerCharacter.getPlayer();
        int playerLineOfSightDistance = player.getPlayerLineOfSightDistance();
        int playerPositionX = player.getPosition().getPositionX();
        int playerPositionY = player.getPosition().getPositionY();
        int npcPositionX = nonPlayerCharacter.getPosition().getPositionX();
        int npcPositionY = nonPlayerCharacter.getPosition().getPositionY();

        if (playerPositionX == npcPositionX) {
            return Math.abs(playerPositionY - npcPositionY) <= playerLineOfSightDistance;
        } else if (playerPositionY == npcPositionY) {
            return Math.abs(playerPositionX - npcPositionX) <= playerLineOfSightDistance;
        } else {
            int diffX = Math.abs(playerPositionX - npcPositionX);
            int diffY = Math.abs(playerPositionY - npcPositionY);
            return (int) Math.sqrt(diffX * diffX + diffY * diffY) <= playerLineOfSightDistance;
        }
    }

    public boolean containsDungeonSpaceType(DungeonSpaceType dungeonSpaceType) {
        return !dungeonObjects.stream()
            .noneMatch(obj -> obj.getDungeonSpaceType() == dungeonSpaceType);
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

    @Override
    public int compareTo(DungeonSpace other) {
        return Long.compare(this.comparisonId, other.comparisonId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, visible);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        } else if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final DungeonSpace other = (DungeonSpace) obj;
        return this.visible != other.visible
            && Objects.equals(this.position, other.position)
            && Objects.equals(this.dungeonObjects, other.dungeonObjects);
    }

    @Override
    public String toString() {
        return "DungeonSpace{" + "position=" + position + ", visible=" + visible + ", dungeonObjects=" + dungeonObjects + '}';
    }
}
