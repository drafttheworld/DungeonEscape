/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.characters;

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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andrew
 */
public class Player extends DungeonCharacter {

    private final DungeonSpace[][] dungeon;
    private final String playerName;
    private final int playerLineOfSightDistance;
    private int frozenTurnsRemaining;
    private boolean won;
    private boolean lost;

    public Player(String playerName, int playerLineOfSightDistance, DungeonSpace[][] dungeon) {
        this.playerName = playerName;
        this.playerLineOfSightDistance = playerLineOfSightDistance;
        this.dungeon = dungeon;
        super.setActive(true);
        frozenTurnsRemaining = 0;
        won = false;
        lost = false;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getPlayerLineOfSightDistance() {
        return playerLineOfSightDistance;
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
    public List<DungeonSpace> interact(DungeonObject dungeonObject) {

        List<DungeonSpace> objectTracks = new ArrayList<>();
        if (!isActive()) {
            return objectTracks;
        } else if (dungeonObject instanceof DungeonMaster) {
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
    public List<DungeonSpace> move(Direction direction) {
        List<DungeonSpace> objectTracks = new ArrayList<>();
        objectTracks.addAll(CharacterActionUtil.movePlayer(dungeon, this, direction));
        objectTracks.addAll(revealMapForMove(direction));
        return objectTracks;
    }

    public List<DungeonSpace> revealMapForMove(Direction direction) {

        List<DungeonSpace> revealedDungeonSpaces = new ArrayList<>();
        int exposeRow, exposeCol, fromCol, toCol, fromRow, toRow;
        switch (direction) {
            case NORTH:
                //reveal northern row
                exposeRow = getPosition().getPositionY() - playerLineOfSightDistance;
                if (exposeRow < 0) {
                    break;
                }

                fromCol = getPosition().getPositionX() - playerLineOfSightDistance;
                toCol = getPosition().getPositionX() + playerLineOfSightDistance;
                for (int col = fromCol; col <= toCol; col++) {
                    if (col >= 0 && col < dungeon.length) {
                        DungeonSpace dungeonSpace = dungeon[exposeRow][col];
                        if (dungeonSpace.isVisible()) {
                            continue;
                        }
                        dungeonSpace.setVisible(true);
                        revealedDungeonSpaces.add(dungeonSpace);
                    }
                }
                break;
            case SOUTH:
                //reveal southern row
                exposeRow = getPosition().getPositionY() + playerLineOfSightDistance;
                if (exposeRow >= dungeon.length) {
                    break;
                }

                fromCol = getPosition().getPositionX() - playerLineOfSightDistance;
                toCol = getPosition().getPositionX() + playerLineOfSightDistance;
                for (int col = fromCol; col <= toCol; col++) {
                    DungeonSpace dungeonSpace = dungeon[exposeRow][col];
                    if (dungeonSpace.isVisible()) {
                        continue;
                    }
                    dungeonSpace.setVisible(true);
                    revealedDungeonSpaces.add(dungeonSpace);
                }
                break;
            case EAST:
                //reveal eastern row
                exposeCol = getPosition().getPositionX() + playerLineOfSightDistance;
                if (exposeCol >= dungeon.length) {
                    break;
                }

                fromRow = getPosition().getPositionY() - playerLineOfSightDistance;
                toRow = getPosition().getPositionY() + playerLineOfSightDistance;
                for (int row = fromRow; row <= toRow; row++) {
                    if (row >= 0 && row < dungeon.length) {
                        DungeonSpace dungeonSpace = dungeon[row][exposeCol];
                        if (dungeonSpace.isVisible()) {
                            continue;
                        }
                        dungeonSpace.setVisible(true);
                        revealedDungeonSpaces.add(dungeonSpace);
                    }
                }
                break;
            case WEST:
                //reveal western row
                exposeCol = getPosition().getPositionX() - playerLineOfSightDistance;
                if (exposeCol < 0) {
                    break;
                }

                fromRow = getPosition().getPositionY() - playerLineOfSightDistance;
                toRow = getPosition().getPositionY() + playerLineOfSightDistance;
                for (int row = fromRow; row <= toRow; row++) {
                    if (row >= 0 && row < dungeon.length) {
                        DungeonSpace dungeonSpace = dungeon[row][exposeCol];
                        if (dungeonSpace.isVisible()) {
                            continue;
                        }
                        dungeonSpace.setVisible(true);
                        revealedDungeonSpaces.add(dungeonSpace);
                    }
                }
                break;
            default:
                break;
        }

        return revealedDungeonSpaces;
    }

    public List<DungeonSpace> revealCurrentMapArea() {
        int northStart = getPosition().getPositionY() - playerLineOfSightDistance;
        if (northStart < 0) {
            northStart = 0;
        }

        int westStart = getPosition().getPositionX() - playerLineOfSightDistance;
        if (westStart < 0) {
            westStart = 0;
        }

        int southEnd = getPosition().getPositionY() + playerLineOfSightDistance;
        if (southEnd >= dungeon.length) {
            southEnd = dungeon.length - 1;
        }

        int eastEnd = getPosition().getPositionX() + playerLineOfSightDistance;
        if (eastEnd >= dungeon.length) {
            eastEnd = dungeon.length - 1;
        }

        List<DungeonSpace> revealedDungeonSpaces = new ArrayList<>();
        for (int row = northStart; row <= southEnd; row++) {
            for (int col = westStart; col <= eastEnd; col++) {
                DungeonSpace dungeonSpace = dungeon[row][col];
                if (dungeonSpace.isVisible()) {
                    continue;
                }
                dungeonSpace.setVisible(true);
                revealedDungeonSpaces.add(dungeonSpace);
            }
        }

        return revealedDungeonSpaces;
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
