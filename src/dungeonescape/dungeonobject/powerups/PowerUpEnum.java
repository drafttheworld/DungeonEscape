/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeonobject.powerups;

/**
 *
 * @author Andrew
 */
public enum PowerUpEnum {

    INVINCIBILITY("Invincibility"),
    INVISIBILITY("Invisibility"),
    REPELLENT("Repellent"),
    TERMINATOR("Terminator");

    private final String title;

    private PowerUpEnum(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
