package STARS;

import java.io.Console;
import java.util.Scanner;

/**
 * Used for creating an interface to login to STARS
 * @author Group 3
 */

public class LoginInterface {


    /**
     * Interface for user login
     */

    public static void run() {

        try {
            System.out.println("Reading in data from database...");
            //initializes data using DatabaseManager from respective constructors
            new CourseMgr();
            new StudentMgr();
        }
        catch (Exception e){
            System.out.println("IOError: Exiting...");
        }
        Scanner sc = new Scanner(System.in);
        Console console = System.console();
        System.out.println("Welcome to STARS!");
        while (true) {
            System.out.println("Enter your domain:");
            System.out.println("1. Admin");
            System.out.println("2. Student");
            System.out.println("3. Exit");
            int choice;
            String domain;
            while (true) {
                while (true) {
                    try {
                        choice = Integer.parseInt(sc.next());
                        break;
                    }
                    catch (NumberFormatException e) {
                        System.out.println("Incorrect input type, please enter an integer choice:");
                    }
                }
                if (choice<1 || choice>3) {
                    System.out.println("Incorrect choice! Please try again:");
                } 
                else if (choice == 1) {
                    domain = "admin";
                    break;
                } 
                else if (choice == 2) {
                    domain = "student";
                    break;
                } 
                else {
                    return;
                }
            }
            System.out.println("Enter email:");
            String email = sc.next();
            String password;
            if (console == null) {
                System.out.println("Not running on terminal. Password will not be hidden.");
                System.out.println("Enter password:");
                password = sc.next();
            } 
            else {
                System.out.println("Running on terminal. Password will be hidden.");
                System.out.println("Enter password (will be hidden):");
                char[] pwd = console.readPassword();
                password = new String(pwd);
            }
            boolean check = PasswordMgr.checkPassword(email, password, domain);
            if (check) {
                System.out.println("Login successful!");
                // pass control to appropriate UI based on domain
                if (domain.equals("student")) {
                    StudentInterface.Run(email);
                }
                else {
                    AdminInterface.Run();
                }
                System.out.println("---------------------------------------------------------------------");
                DatabaseManager.updateDatabase();
                //return;  //if enabled, this exits the program after a single user logs out
            } 
            else {
                System.out.println("Login unsuccessful, try again!");
            }
        }

    }

    /**
     * Driver code for the login interface
     * @param args - command line arguments as a string array
     */
    
    public static void main(String[] args) {

        run();

    }


}
