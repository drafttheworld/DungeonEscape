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
public class WinNotification extends GameNotification {
    
    public WinNotification() {
        this("Congratulations, you won!");
    }
    
    public WinNotification(String message) {
        super(message);
    }
    
}
