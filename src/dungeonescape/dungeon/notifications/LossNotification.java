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
public class LossNotification extends GameNotification {
    
    public LossNotification() {
        this("Sorry, you did not escape the dungeon.");
    }
    
    public LossNotification(String message) {
        super(message);
    }
    
}
