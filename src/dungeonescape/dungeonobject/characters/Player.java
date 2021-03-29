/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.characters;

import dungeonescape.dungeonobject.DungeonObject;
import dungeonescape.dungeonobject.FreezeTime;
import dungeonescape.dungeonobject.construction.Construction;
import dungeonescape.play.Direction;
import dungeonescape.dungeon.space.DungeonSpace;
import dungeonescape.dungeon.space.DungeonSpaceType;
import dungeonescape.dungeon.space.Position;
import dungeonescape.dungeonobject.coin.Coin;
import dungeonescape.dungeonobject.powerups.PowerUp;
import dungeonescape.dungeonobject.powerups.PowerUpBox;
import dungeonescape.dungeonobject.powerups.PowerUpEnum;
import dungeonescape.dungeonobject.powerups.PowerUpListener;
import dungeonescape.dungeonobject.powerups.PowerUpService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author Andrew
 */
public class Player extends DungeonCharacter implements PowerUpListener {

    private final DungeonSpace[][] dungeon;
    private final String playerName;
    private final int playerLineOfSightDistance;
    private final PowerUpService powerUpService;
    private int frozenTurnsRemaining;
    private final Map<PowerUpEnum, List<PowerUp>> powerUps;
    private PowerUp activePowerUp;
    private int coinsCollected;
    private boolean won;
    private boolean lost;
    private boolean teleported;

    public Player(String playerName, int playerLineOfSightDistance, DungeonSpace[][] dungeon,
        PowerUpService powerUpService) {

        this.playerName = playerName;
        this.playerLineOfSightDistance = playerLineOfSightDistance;
        this.dungeon = dungeon;
        this.powerUpService = powerUpService;
        this.powerUpService.registerPowerUpClient(this);
        frozenTurnsRemaining = 0;
        powerUps = new EnumMap<>(PowerUpEnum.class);
        coinsCollected = 0;
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

    public PowerUp getActivePowerUp() {
        return activePowerUp;
    }

    public int getCoinsCollected() {
        return coinsCollected;
    }

    public void addCoinsCollected(int coinsCollected) {

        if (coinsCollected >= 0) {
            if (Integer.MAX_VALUE - coinsCollected <= this.coinsCollected) {
                this.coinsCollected = Integer.MAX_VALUE;
            } else {
                this.coinsCollected += coinsCollected;
            }
        }
    }

    public void removeCoinsCollected(int coins) {

        if (coins >= 0) {
            if (this.coinsCollected - coins < 0) {
                this.coinsCollected = 0;
            } else {
                this.coinsCollected -= coins;
            }
        }
    }

    public void addPowerUp(PowerUp powerUp) {

        if (powerUp != null) {
            List<PowerUp> powerUpList
                = powerUps.computeIfAbsent(powerUp.getCorrespondingPowerUpEnum(), k -> new ArrayList<>());
            powerUpList.add(powerUp);
            powerUpService.addPowerUp(powerUp.getCorrespondingPowerUpEnum());
        }
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

    public boolean isTeleported() {
        return teleported;
    }

    public void setTeleported(boolean teleported) {
        this.teleported = teleported;
    }

    @Override
    public List<DungeonSpace> interact(DungeonObject dungeonObject) {

        List<DungeonSpace> dungeonSpaces = new ArrayList<>();
        if (!isActive()) {
            return dungeonSpaces;
        } else if (dungeonObject instanceof Coin
            || dungeonObject instanceof PowerUpBox
            || dungeonObject instanceof DungeonMaster
            || dungeonObject instanceof Guard
            || dungeonObject instanceof Ghost) {

            dungeonSpaces.addAll(dungeonObject.interact(this));
        }

        return dungeonSpaces;
    }

    @Override
    public DungeonSpaceType getDungeonSpaceType() {
        return DungeonSpaceType.PLAYER;
    }

    @Override
    public List<DungeonSpace> move(DungeonSpace[][] dungeon, Player player) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public List<DungeonSpace> move(Direction direction) {

        teleported = false;// reset the teleported flag

        List<DungeonSpace> dungeonSpaces = new ArrayList<>();
        dungeonSpaces.addAll(CharacterActionUtil.movePlayer(dungeon, this, direction));
        if (teleported) {
            dungeonSpaces.addAll(revealCurrentMapArea());
        } else {
            dungeonSpaces.addAll(revealMapForMove(direction));
        }

        return dungeonSpaces;
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
                if (fromCol < 0) {
                    fromCol = 0;
                }

                toCol = getPosition().getPositionX() + playerLineOfSightDistance;
                if (toCol >= dungeon.length) {
                    toCol = dungeon.length - 1;
                }
                for (int col = fromCol; col <= toCol; col++) {

                    if (col < 0) {
                        continue;
                    } else if (col >= dungeon.length) {
                        break;
                    }

                    DungeonSpace dungeonSpace = dungeon[exposeRow][col];
                    if (dungeonSpace.isVisible()) {
                        continue;
                    }
                    dungeonSpace.setVisible(true);
                    revealedDungeonSpaces.add(dungeonSpace);
                }
                break;
            case SOUTH:
                //reveal southern row
                exposeRow = getPosition().getPositionY() + playerLineOfSightDistance;
                if (exposeRow >= dungeon.length) {
                    break;
                }

                fromCol = getPosition().getPositionX() - playerLineOfSightDistance;
                if (fromCol < 0) {
                    fromCol = 0;
                }

                toCol = getPosition().getPositionX() + playerLineOfSightDistance;
                if (toCol >= dungeon.length) {
                    toCol = dungeon.length - 1;
                }
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
                //reveal eastern column
                exposeCol = getPosition().getPositionX() + playerLineOfSightDistance;
                if (exposeCol >= dungeon.length) {
                    break;
                }

                fromRow = getPosition().getPositionY() - playerLineOfSightDistance;
                if (fromRow < 0) {
                    fromRow = 0;
                }

                toRow = getPosition().getPositionY() + playerLineOfSightDistance;
                if (toRow >= dungeon.length) {
                    toRow = dungeon.length - 1;
                }
                for (int row = fromRow; row <= toRow; row++) {
                    DungeonSpace dungeonSpace = dungeon[row][exposeCol];
                    if (dungeonSpace.isVisible()) {
                        continue;
                    }
                    dungeonSpace.setVisible(true);
                    revealedDungeonSpaces.add(dungeonSpace);
                }
                break;
            case WEST:
                //reveal western row
                exposeCol = getPosition().getPositionX() - playerLineOfSightDistance;
                if (exposeCol < 0) {
                    break;
                }

                fromRow = getPosition().getPositionY() - playerLineOfSightDistance;
                if (fromRow < 0) {
                    fromRow = 0;
                }

                toRow = getPosition().getPositionY() + playerLineOfSightDistance;
                if (toRow >= dungeon.length) {
                    toRow = dungeon.length - 1;
                }
                for (int row = fromRow; row <= toRow; row++) {
                    DungeonSpace dungeonSpace = dungeon[row][exposeCol];
                    if (dungeonSpace.isVisible()) {
                        continue;
                    }
                    dungeonSpace.setVisible(true);
                    revealedDungeonSpaces.add(dungeonSpace);
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
                if (!dungeonSpace.isVisible()) {
                    dungeonSpace.setVisible(true);
                }
                revealedDungeonSpaces.add(dungeonSpace);
            }
        }

        return revealedDungeonSpaces;
    }

    public String getPlayerStats() {
        return new StringBuilder().append("Coins collected: ").append(coinsCollected).append("\n")
            .append("Frozen turns remaining: ").append(frozenTurnsRemaining)
            .toString();
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

    @Override
    public int hashCode() {
        return Objects.hash(dungeon, playerName, playerLineOfSightDistance, frozenTurnsRemaining,
            powerUps, coinsCollected, won, lost, teleported);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        } else if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final Player other = (Player) obj;
        return this.playerLineOfSightDistance == other.playerLineOfSightDistance
            && this.frozenTurnsRemaining == other.frozenTurnsRemaining
            && this.coinsCollected == other.coinsCollected
            && Objects.equals(this.playerName, other.playerName)
            && Arrays.deepEquals(this.dungeon, other.dungeon)
            && Objects.equals(this.powerUps, other.powerUps);
    }

    @Override
    public void notifyPowerUpAdded(PowerUpEnum powerUpEnum) {
        // do nothing, not used.
    }

    @Override
    public void notifyPowerUpUsed(PowerUpEnum powerUpEnum) {
        if (powerUpEnum != null) {
            List<PowerUp> powerUpList
                = powerUps.computeIfAbsent(powerUpEnum, k -> new ArrayList<>());
            if (!powerUpList.isEmpty()) {
                activePowerUp = powerUpList.remove(powerUpList.size() - 1);
            } else {
                System.out.println("There are no available " + powerUpEnum + " power-ups.");
            }
        }
    }
}
