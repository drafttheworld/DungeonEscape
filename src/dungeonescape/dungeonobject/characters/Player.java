/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.characters;

import dungeonescape.dungeon.Dungeon;
import dungeonescape.dungeon.notifications.ActionNotAllowedNotification;
import dungeonescape.dungeon.notifications.NotificationManager;
import dungeonescape.dungeon.notifications.WinNotification;
import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.dungeonobject.DungeonObjectTrack;
import dungeonescape.dungeonobject.FreezeTime;
import dungeonescape.dungeonobject.construction.Construction;
import dungeonescape.play.Direction;
import dungeonescape.space.DungeonSpace;
import dungeonescape.space.DungeonSpaceType;
import dungeonescape.space.Position;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Andrew
 */
public class Player extends DungeonCharacter {

    private final Dungeon dungeon;
    private final String playerName;
    private final int playerVisibility;
    private int frozenTurnsRemaining;
    private boolean won;
    private boolean lost;

    public Player(String playerName, int playerVisibility, Dungeon dungeon) {
        this.playerName = playerName;
        this.playerVisibility = playerVisibility;
        this.dungeon = dungeon;
        super.setActive(true);
        frozenTurnsRemaining = 0;
        won = false;
        lost = false;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getPlayerVisibility() {
        return playerVisibility;
    }

    public boolean isFrozen() {
        return frozenTurnsRemaining > 0;
    }

    public FreezeTime getFrozenTurnsRemaining() {
        return new FreezeTime(frozenTurnsRemaining);
    }

    public void addFrozenTurns(FreezeTime forzenTurns) {
        frozenTurnsRemaining += forzenTurns.getTurns();
    }

    public void decrementFrozenTurnsRemaining(long turns) {
        frozenTurnsRemaining -= turns;
    }

    public boolean hasWon() {
        return won;
    }

    public void setWon(boolean won) {
        this.won = won;
    }

    public boolean hasLost() {
        return lost;
    }

    public void setLost(boolean lost) {
        this.lost = lost;
    }

    @Override
    public List<DungeonObjectTrack> interact(DungeonObject dungeonObject) {
        List<DungeonObjectTrack> objectTracks = new ArrayList<>();
        if (dungeonObject instanceof DungeonMaster) {
            objectTracks.addAll(((DungeonMaster) dungeonObject).interact(this));
        } else if (dungeonObject instanceof Guard) {
            objectTracks.addAll(((Guard) dungeonObject).interact(this));
        } else if (dungeonObject instanceof Ghost) {
            objectTracks.addAll(((Ghost) dungeonObject).interact(this));
        }

        return objectTracks;
    }

    @Override
    public DungeonSpaceType getDungeonSpaceType() {
        return DungeonSpaceType.PLAYER;
    }

    @Override
    public List<DungeonObjectTrack> move(Direction direction, DungeonSpace[][] dungeon) {

        DungeonSpace currentDungeonSpace = getDungeonSpace();
        Position nextPosition = determineNextPosition(direction);

        if (nextPosition.getPositionX() < 0 || nextPosition.getPositionX() >= dungeon.length
            || nextPosition.getPositionY() < 0 || nextPosition.getPositionY() >= dungeon.length) {
            NotificationManager.notify(new WinNotification());
            return Collections.emptyList();
        }
        DungeonSpace nextDungeonSpace = dungeon[nextPosition.getPositionY()][nextPosition.getPositionX()];

        if (nextDungeonSpace.containsWall()) {
            return Collections.emptyList();
        }

        List<DungeonObjectTrack> objectTracks = new ArrayList<>();
        objectTracks.addAll(nextDungeonSpace.addDungeonObject(this));
        setPreviousDungeonSpace(currentDungeonSpace);
        currentDungeonSpace.removeDungeonObject(this);

        objectTracks.add(new DungeonObjectTrack(getPreviousDungeonSpace().getPosition(),
            getPreviousDungeonSpace().getVisibleDungeonSpaceType().getValueString()));
        objectTracks.add(new DungeonObjectTrack(getPosition(), getDungeonSpaceType().getValueString()));
        objectTracks.addAll(revealMapForMove(direction, dungeon));

        return objectTracks;
    }

    private Position determineNextPosition(Direction direction) {
        Position currentPlayerPosition = getPosition();
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
                NotificationManager.notify(new ActionNotAllowedNotification(errorMessage));
        }
        return null;
    }

    private List<DungeonObjectTrack> revealMapForMove(Direction direction, DungeonSpace[][] dungeon) {
        List<DungeonObjectTrack> revealedDungeonSpaces = new ArrayList<>();
        int exposeRow, exposeCol, fromCol, toCol, fromRow, toRow;
        switch (direction) {
            case NORTH:
                //reveal northern row
                exposeRow = getPosition().getPositionY() - playerVisibility;
                if (exposeRow < 0) {
                    break;
                }

                fromCol = getPosition().getPositionX() - playerVisibility;
                toCol = getPosition().getPositionX() + playerVisibility;
                for (int col = fromCol; col <= toCol; col++) {
                    if (col >= 0 && col < dungeon.length) {
                        dungeon[exposeRow][col].setVisible(true);
                        revealedDungeonSpaces.add(new DungeonObjectTrack(new Position(col, exposeRow),
                            dungeon[exposeRow][col].getVisibleDungeonSpaceType().getValueString()));
                    }
                }
                break;
            case SOUTH:
                //reveal southern row
                exposeRow = getPosition().getPositionY() + playerVisibility;
                if (exposeRow >= dungeon.length) {
                    break;
                }

                fromCol = getPosition().getPositionX() - playerVisibility;
                toCol = getPosition().getPositionX() + playerVisibility;
                for (int col = fromCol; col <= toCol; col++) {
                    if (col >= 0 && col < dungeon.length) {
                        dungeon[exposeRow][col].setVisible(true);
                        revealedDungeonSpaces.add(new DungeonObjectTrack(new Position(col, exposeRow),
                            dungeon[exposeRow][col].getVisibleDungeonSpaceType().getValueString()));
                    }
                }
                break;
            case EAST:
                //reveal eastern row
                exposeCol = getPosition().getPositionX() + playerVisibility;
                if (exposeCol >= dungeon.length) {
                    break;
                }

                fromRow = getPosition().getPositionY() - playerVisibility;
                toRow = getPosition().getPositionY() + playerVisibility;
                for (int row = fromRow; row <= toRow; row++) {
                    if (row >= 0 && row < dungeon.length) {
                        dungeon[row][exposeCol].setVisible(true);
                        revealedDungeonSpaces.add(new DungeonObjectTrack(new Position(exposeCol, row),
                            dungeon[row][exposeCol].getVisibleDungeonSpaceType().getValueString()));
                    }
                }
                break;
            case WEST:
                //reveal western row
                exposeCol = getPosition().getPositionX() - playerVisibility;
                if (exposeCol < 0) {
                    break;
                }

                fromRow = getPosition().getPositionY() - playerVisibility;
                toRow = getPosition().getPositionY() + playerVisibility;
                for (int row = fromRow; row <= toRow; row++) {
                    if (row >= 0 && row < dungeon.length) {
                        dungeon[row][exposeCol].setVisible(true);
                        revealedDungeonSpaces.add(new DungeonObjectTrack(new Position(exposeCol, row),
                            dungeon[row][exposeCol].getVisibleDungeonSpaceType().getValueString()));
                    }
                }
                break;
            default:
                break;
        }

        return revealedDungeonSpaces;
    }

    public List<DungeonObjectTrack> revealCurrentMapArea() {
        int northStart = getPosition().getPositionY() - playerVisibility;
        if (northStart < 0) {
            northStart = 0;
        }

        int westStart = getPosition().getPositionX() - playerVisibility;
        if (westStart < 0) {
            westStart = 0;
        }

        int southEnd = getPosition().getPositionY() + playerVisibility;
        if (southEnd >= dungeon.getDungeon().length) {
            southEnd = dungeon.getDungeon().length - 1;
        }

        int eastEnd = getPosition().getPositionX() + playerVisibility;
        if (eastEnd >= dungeon.getDungeon().length) {
            eastEnd = dungeon.getDungeon().length - 1;
        }

        List<DungeonObjectTrack> objectTracks = new ArrayList<>();
        for (int row = northStart; row <= southEnd; row++) {
            for (int col = westStart; col <= eastEnd; col++) {
                DungeonSpace dungeonSpace = dungeon.getDungeon()[row][col];
                if (!dungeonSpace.isVisible()) {
                    dungeonSpace.setVisible(true);
                    objectTracks.add(new DungeonObjectTrack(dungeonSpace.getPosition(),
                        dungeonSpace.getVisibleDungeonSpaceType().getValueString()));
                }
            }
        }

        return objectTracks;
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
