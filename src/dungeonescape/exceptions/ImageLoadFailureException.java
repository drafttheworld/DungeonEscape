/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeonescape.exceptions;

/**
 *
 * @author Andrew
 */
public class ImageLoadFailureException extends RuntimeException {
    
    public ImageLoadFailureException(String message) {
        super(message);
    }
    
    public ImageLoadFailureException(String message, Throwable t) {
        super(message, t);
    }
    
}
