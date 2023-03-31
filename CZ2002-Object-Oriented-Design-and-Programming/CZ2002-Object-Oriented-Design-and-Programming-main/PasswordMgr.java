package STARS; 

import java.io.* ;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/**
 * Used for working with user password
 * @author Group 3
 */

public class PasswordMgr {


    /**
     * Method to encode a password string using hashing
     * @param str - string to encode
     * @return encoded string
     */

    public static String hash(String str) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA3-256");
            byte[] hashBytes = digest.digest(str.getBytes(StandardCharsets.UTF_8));
            return new String(hashBytes);
        }
        catch (NoSuchAlgorithmException e) {
            System.err.println("Invalid hash type");
        }
        return "Error";

    }

    /**
     * Method to check if the user's password input is correct or not
     * @param email - email ID of the user
     * @param password - password entered by the user
     * @param domain - user domain ("admin" or "student")
     * @return boolean value indicating if login was successful or not
     */

    public static boolean checkPassword(String email, String password, String domain){
        
        String hashed = hash(password);
        String fileName = domain + "Passwords.dat";
        if (!hashed.equals("error")) {
            try {
                FileInputStream fiStream = new FileInputStream(fileName);
                BufferedInputStream biStream = new BufferedInputStream(fiStream);
                ObjectInputStream diStream = new ObjectInputStream(biStream);
                try {
                    while (true) {
                        String iEmail = diStream.readUTF();
                        String iHashed = diStream.readUTF();
                        if (iEmail.equals(email)) {
                            diStream.close();
                            return iHashed.equals(hashed);
                        }
                    }
                }
                catch (EOFException e) {
                    System.out.println("Email does not exist!");
                    diStream.close();
                    return false;
                }
            }
            catch (FileNotFoundException e) {
                System.out.println("IOError: File not found!" + fileName);
                System.exit(0);
            }
            catch (IOException e) {
                System.out.println("File IO Error!" + e.getMessage());
                System.exit(0);
            }
        }
        return false;

    }

    /**
     * Method to save a new user's password
     * @param email - email ID of the user
     * @param password - password entered by the user
     * @param domain - user domain ("admin" or "student")
     */

    public static void storePassword (String email, String password, String domain) {

        String fileName = domain + "Passwords.dat";
        String hashed = hash(password);
        try {
            File file = new File(fileName);
            boolean flag = false;
            if (file.exists()) {
                flag = true;
            }
            file.createNewFile();  // handles exception when file doesn't exist
            FileOutputStream foStream = new FileOutputStream(file, true);
            BufferedOutputStream boStream = new BufferedOutputStream(foStream);
            ObjectOutputStream doStream;
            //handling cases where we're appending to file, prevents rewriting file header.
            if (flag) {
                doStream = new AppendableObjectOutputStream(boStream);
            }
            else {
                doStream = new ObjectOutputStream(boStream);
            }
            doStream.writeUTF(email);
            doStream.writeUTF(hashed);
            doStream.close();
        }
        catch ( IOException e ) {
            System.out.println( "File IO Error!" + e.getMessage() );
            System.exit( 0 );
        }

    }

    /**
     * Static class necessary to prevent ObjectOutputHeader from rewriting file header each time
     */

    private static class AppendableObjectOutputStream extends ObjectOutputStream {

        public AppendableObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }
        @Override
        protected void writeStreamHeader() throws IOException {

        }

    }

    /**
     * Used to initialize admin passwords before MySTARS can be run.
     * @param args - command line arguments as a string array
     */

    public static void main(String[] args) { 

        Scanner sc = new Scanner(System.in);
        Console console = System.console();
        System.out.println("1. Store password");
        System.out.println("2. Check password");
        int choice;
        while(true) {
            try {
                choice = Integer.parseInt(sc.next());
                break;
            }
            catch (NumberFormatException e) {
                System.out.println("Incorrect input type, please enter an integer choice:");
            }
        }
        if (choice!=1 && choice!=2) {
            System.out.println("Incorrect choice!");
        }
        System.out.println("Enter domain:");
        String domain = sc.next();
        System.out.println("Enter email:");
        String email = sc.next();
        String password;
        if (console == null) {
            System.out.println("No console available, password text will not be hidden.");
            System.out.println("Enter password:");
            password = sc.next();
        }
        else {
            System.out.println("Enter password (will be hidden):");
            char[] pwd = console.readPassword();
            password = new String(pwd);
        }
        if (choice==1) {
            storePassword(email, password, domain);
        }
        else if (choice==2) {
            boolean check = checkPassword(email, password, domain);
            if (check) {
                System.out.println("User found!");
            }
            else {
                System.out.println("User not found!");
            }
        }

    }


}