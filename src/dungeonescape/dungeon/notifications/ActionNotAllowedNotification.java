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
public class ActionNotAllowedNotification extends GameNotification {
    
    private static final String NOTIFICATION = "Action not allowed: ";
    
    public ActionNotAllowedNotification(String message) {
        super(message);
    }
    
    @Override
    public String getNotificationMessage() {
        return NOTIFICATION + super.getNotificationMessage();
    }
    
}
