package greencity.exception.exceptions;

/**
 * Exception that we get when we send a request (for example, to findById) and there is
 * no Notification record with this id, then we get {@link NotificationNotFoundException}.
 *
 * @author AI Assistant
 * @version 1.0
 */
public class NotificationNotFoundException extends RuntimeException {
    /**
     * Constructor for NotificationNotFoundException.
     *
     * @param message - giving message.
     */
    public NotificationNotFoundException(String message) {
        super(message);
    }
}