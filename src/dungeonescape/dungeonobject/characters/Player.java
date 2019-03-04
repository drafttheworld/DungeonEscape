/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.characters;

import dungeonescape.dungeon.notifications.ActionNotAllowedNotification;
import dungeonescape.dungeon.notifications.GameNotification;
import dungeonescape.dungeon.notifications.WinNotification;
import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.dungeonobject.FreezeTime;
import dungeonescape.dungeonobject.construction.Construction;
import dungeonescape.play.Direction;
import dungeonescape.space.DungeonSpace;
import dungeonescape.space.DungeonSpaceType;
import dungeonescape.space.Position;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Andrew
 */
public class Player extends DungeonCharacter {

    private final String playerName;
    private final int playerVisibility;
    private long frozenTimeRemainingInSeconds;

    public Player(String playerName, int playerVisibility) {
        this.playerName = playerName;
        this.playerVisibility = playerVisibility;

        frozenTimeRemainingInSeconds = 0L;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getPlayerVisibility() {
        return playerVisibility;
    }

    public boolean isFrozen() {
        return frozenTimeRemainingInSeconds > 0;
    }

    public FreezeTime getFrozenTimeRemaining() {
        return new FreezeTime(frozenTimeRemainingInSeconds, TimeUnit.SECONDS);
    }

    public void addFrozenTime(FreezeTime frozenTime) {
        frozenTimeRemainingInSeconds += frozenTime.getFreezeTimeForTimeUnit(TimeUnit.SECONDS);
    }

    public void decrementFrozenTimeRemaining(long time, TimeUnit timeUnit) {
        frozenTimeRemainingInSeconds -= timeUnit.toSeconds(time);
    }

    @Override
    public void interact(DungeonObject dungeonObject) throws GameNotification {
        if (dungeonObject instanceof DungeonMaster) {
            ((DungeonMaster) dungeonObject).interact(this);
        } else if (dungeonObject instanceof Guard) {
            ((Guard) dungeonObject).interact(this);
        } else if (dungeonObject instanceof Ghost) {
            ((Ghost) dungeonObject).interact(this);
        }
    }

    @Override
    public DungeonSpaceType getDungeonSpaceType() {
        return DungeonSpaceType.PLAYER;
    }

    @Override
    public void move(Direction direction, DungeonSpace[][] dungeon) throws GameNotification {

        DungeonSpace currentDungeonSpace
                = dungeon[getPosition().getPositionY()][getPosition().getPositionX()];
        Position nextPosition = determineNextPosition(getPosition(), direction);

        if (nextPosition.getPositionX() < 0 || nextPosition.getPositionY() < 0) {
            throw new WinNotification();
        }
        DungeonSpace nextDungeonSpace = dungeon[nextPosition.getPositionY()][nextPosition.getPositionX()];
        
        if (nextDungeonSpace.containsWall()) {
            throw new ActionNotAllowedNotification("Cannot move into a wall.");
        }
        
        nextDungeonSpace.addDungeonObject(this);
        currentDungeonSpace.removeDungeonObject(this);
    }

    private Position determineNextPosition(Position currentPlayerPosition, Direction direction) throws ActionNotAllowedNotification {
        switch (direction) {
            case NORTH:
                return new Position(currentPlayerPosition.getPositionX(), currentPlayerPosition.getPositionY() - 1);
            case SOUTH:
                return new Position(currentPlayerPosition.getPositionX(), currentPlayerPosition.getPositionY() + 1);
            case EAST:
                return new Position(currentPlayerPosition.getPositionX() + 1, currentPlayerPosition.getPositionY());
            case WEST:
                return new Position(currentPlayerPosition.getPositionX() - 1, currentPlayerPosition.getPositionY());
            default:
                String errorMessage = direction.getValue() + " is not a valid direction. Allowed directions are: "
                        + Arrays.toString(Direction.values());
                throw new ActionNotAllowedNotification(errorMessage);
        }
    }

    @Override
    public boolean canOccupySpace(DungeonSpace dungeonSpace) {
        return dungeonSpace.getDungeonObjects().stream()
                .noneMatch(dungeonObject -> {
                    return dungeonObject instanceof Construction
                            || (dungeonObject instanceof DungeonCharacter
                            && !(dungeonObject instanceof Ghost));
                });
    }

}
