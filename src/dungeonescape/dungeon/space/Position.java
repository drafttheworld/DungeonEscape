/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon.space;

/**
 *
 * @author Andrew
 */
public class Position {

    int positionX;
    int positionY;

    public Position() {

    }

    public Position(int positionX, int positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public Position positionX(int positionX) {
        setPositionX(positionX);
        return this;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public Position positionY(int positionY) {
        setPositionY(positionY);
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + this.positionX;
        hash = 79 * hash + this.positionY;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        } else if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final Position other = (Position) obj;
        return this.positionX == other.positionX
            && this.positionY == other.positionY;
    }

    @Override
    public String toString() {
        return "Position{" + "positionX=" + positionX + ", positionY=" + positionY + '}';
    }

}
