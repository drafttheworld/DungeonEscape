/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon.notifications;

/**
 *
 * @author Andrew
 */
public abstract class GameNotification extends Exception {
    
    public GameNotification(String message) {
        super(message);
    }
    
}