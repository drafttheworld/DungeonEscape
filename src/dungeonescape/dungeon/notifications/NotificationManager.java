/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.dungeon.notifications;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andrew
 */
public class NotificationManager {
    private static final List<NotificationListener> notificationListeners
            = new ArrayList<>();
    
    public static void registerNotificationListener(NotificationListener notificationListener) {
        notificationListeners.add(notificationListener);
    }
    
    public static void notify(GameNotification gameNotification) {
        notificationListeners.forEach(notificationListener -> {
            notificationListener.processNotification(gameNotification);
        });
    }
    
}
