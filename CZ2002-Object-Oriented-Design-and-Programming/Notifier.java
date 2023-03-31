package STARS;

/**
 * Used to notify a student of changes via email
 * @author Group 3
 */

public interface Notifier {

    /**
     * Method to send a notification to students via email
     * @param ids - email IDs of the people to send the notification to
     * @param subject - subject of the email
     * @param notification - body of the message
     */

    void sendNotification(String[] ids, String subject, String notification);

}
