package STARS;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Used for notifying the students via email
 * @author Group 3
 */

public class NotifyByEmail implements Notifier {

    /**
     * username - dummy account to send the email
     * password - password for the account
     */

    final static private String username = "oodp.stars.group3";
    final static private String password = "j@v@1sfun";

    /**
     * Helper function to join the lsit of email addresses into a single string
     * @param addresses - list of email addresses
     * @return string made by joining email addresses
     */

    private static String returnAddressList(String[] addresses) {

        String result = "";
        for(int i=0; i< addresses.length; i++){
            result += addresses[i];
            if (i!=addresses.length-1) {
                result+=",";
            }
        }
        return result;

    }

    /**
     * Method for sending notification to students
     * @param addressList - list of email IDs
     * @param subject - subject of the email
     * @param notification - body of the email
     */

    public void sendNotification(String[] addressList, String subject, String notification) {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        String addresses = returnAddressList(addressList);
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("oodp.stars.group3@gmail.com"));
            message.addRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(addresses)); // recipient email
            message.addRecipients(Message.RecipientType.CC,
                    InternetAddress.parse("varunsri001@e.ntu.edu.sg")); //cc email, add yourself
            message.setSubject(subject);
            message.setText(notification);
            Transport.send(message);
            System.out.println("Successfully notified students.");
        }
        catch(AddressException e) {
            System.out.println("Could not parse address!");
        }
        catch (Exception e) {
            System.out.println("Error: invalid email among registering students, aborting mailing sequence...");;
        }

    }

    /**
     * Driver code for the login interface
     * @param args - command line arguments as a string array
     */

    public static void main(String[] args){
        //System.out.println(returnAddressList(new String[]{"varunsri001@e.ntu.edu.sg", "iyengar.varun@gmail.com"}));
        Notifier notifier = new NotifyByEmail();
        notifier.sendNotification(new String[]{"iyengar.varun@gmail.com"}, "Tester mail", "Hello there");
    }
}