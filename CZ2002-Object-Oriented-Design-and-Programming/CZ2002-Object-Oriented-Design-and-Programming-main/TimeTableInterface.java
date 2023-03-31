package STARS;

import java.util.Scanner;
import java.time.*;

/**
 * Creates an interface for making a timetable
 * @author Group 3
 */

public class TimeTableInterface {


    /**
     * input - scanner object for the interface
     */

    private static Scanner input = new Scanner(System.in);

    /**
     * Method to create an interface for inputting date of a work slot
     * @return date inputted by the user
     */

    public static LocalDate inputDate(){
        int year, month, day;
        while(true) {
            System.out.println("Year: ");
            while(true) {
                try {
                    year = Integer.parseInt(input.next());
                    break;
                }
                catch (NumberFormatException e){
                    System.out.println("Incorrect input type, please enter an integer choice:");
                }
            }
            System.out.println("Month: ");
            while(true) {
                try {
                    month = Integer.parseInt(input.next());
                    break;
                }
                catch (NumberFormatException e){
                    System.out.println("Incorrect input type, please enter an integer choice:");
                }
            }
            System.out.println("Day: ");
            while(true) {
                try {
                    day = Integer.parseInt(input.next());
                    break;
                }
                catch (NumberFormatException e){
                    System.out.println("Incorrect input type, please enter an integer choice:");
                }
            }

            try {
                return LocalDate.of(year, month, day);
            }
            catch (Exception e) {
                System.out.println("Incorrect date! Try again.");
            }
        }
    }

    /**
     * Method to get the day of the week
     * @return day of the week inputted by the user
     */

    public static DayOfWeek inputDayOfWeek()
    {
        System.out.println("Select working day: ");
        System.out.println("1. Monday");
        System.out.println("2. Tuesday");
        System.out.println("3. Wednesday");
        System.out.println("4. Thursday");
        System.out.println("5. Friday");
        System.out.println("6. Saturday");
        while (true) {
            try {
                return DayOfWeek.of(input.nextInt());
            }
            catch (Exception e){
                System.out.println("Incorrect choice! Please try again.");
                System.out.println("Select a working day from above: ");
            }
        }
    }

    /**
     * Method to get the timing for the work slot
     * @return time inputted by the user
     */

    public static LocalTime inputLocalTime()
    {
        int hour, minute;
        System.out.print("\t --:-- \n");

        System.out.println("Select hour: ");
        while (true) {
            try {
                hour = Integer.parseInt(input.next());
                if(hour<0 || hour>23){
                    System.out.println("Incorrect input for hour! Please try again in range 0 to 23.");
                    System.out.println("Select hour: ");
                }
                else
                    break;
            }
            catch (NumberFormatException e) {
                System.out.println("Incorrect input type, please enter an integer choice:");
            }
        }
        System.out.printf("\t %2d:-- \n", hour);

        while (true) {
            try {
                minute = Integer.parseInt(input.next());
                if(!(minute == 0 || minute == 15 || minute==30 || minute == 45)){
                    System.out.println("Incorrect input for minutes! Please try again from above values.");
                    System.out.println("Select hour: ");
                }
                else
                    break;
            }
            catch (NumberFormatException e) {
                System.out.println("Incorrect input type, please enter an integer choice:");
            }
        }
        System.out.printf("\t %2d:%2d \n", hour, minute);

        return LocalTime.of(hour, minute);
    }

    /**
     * Method to get the type of work in the work slot
     * @return type of work
     */

    public static Type inputType(){
        System.out.println("Input type of class: ");
        System.out.println("1. Lecture");
        System.out.println("2. Tutorial");
        System.out.println("3. Lab");
        System.out.println("4. Seminar");
        int choice;
        while (true) {
            try {
                choice = Integer.parseInt(input.next());
                if(choice < 0 || choice > 4){
                    System.out.println("Error: please provide a valid choice.");
                }
                else
                    break;
            }
            catch (NumberFormatException e) {
                System.out.println("Incorrect input type, please enter an integer choice:");
            }
        }
        switch(choice)
        {
            case 1:
                return Type.LEC;
            case 2:
                return Type.TUT;
            case 3:
                return Type.LAB;
            case 4:
                return Type.SEM;
            default:
                return null;
        }
    }

    /**
     * Method to get the input for list of weeks on which the work slot is applicable
     * @param N_STUDY_WEEKS - number of weeks in the semester
     * @return boolean array with true at indices for weeks where the slot is applicable
     */

    public static boolean [] inputWorkWeeks(int N_STUDY_WEEKS)
    {
        int choice, i;
        boolean [] workWeek = new boolean[N_STUDY_WEEKS];
        System.out.println("Select a choice for study schedule");
        System.out.println("1. Odd weeks");
        System.out.println("2. Even weeks");
        System.out.println("3. All weeks");

        while(true) {
            try {
                choice = Integer.parseInt(input.next());
                if(choice<1 || choice > 3){
                    System.out.println("Error: Please provide valid choice:");
                }
                else
                    break;
            }
            catch (NumberFormatException e){
                System.out.println("Incorrect input type, please enter an integer choice:");
            }
        }
        switch(choice)
        {
            case 1:
                for(i = 0; i < N_STUDY_WEEKS; i+=2)
                {
                    workWeek[i] = true;
                }
                break;
            case 2:
                for(i = 1; i < N_STUDY_WEEKS; i+=2)
                {
                    workWeek[i] = true;
                }
                break;
            case 3:
                for(i = 0; i < N_STUDY_WEEKS; i+=1)
                {
                    workWeek[i] = true;
                }
                break;
            default:
                break;
        }
        return workWeek;
    }

    /**
     * Method to make an interface to get the venue
     * @return returns venue as a string
     */

    public static String inputVenue(){
        input.nextLine();
        System.out.println("Enter venue: ");
        return input.nextLine();
    }

    /**
     * Method to add another lesson to the timetable for a course
     * @return boolean value indicating if another lesson was added
     */

    public static boolean inputAnother(){
        System.out.println("Add another class to the course timetable? y/n");
        char choice;
        while(true) {
            choice = Character.toLowerCase(input.next().charAt(0));
            if (choice == 'y') {
                return true;
            } else if (choice == 'n') {
                System.out.println("Final timetable created!");
                return false;
            }
            else{
                System.out.println("Incorrect input, try again (enter y or n):");
            }
        }
    }

    /**
     * Method to create the timetable for final exam
     * @return boolean value indicating if the course has a final exam
     */

    public static boolean haveFinal(){
        System.out.println("Does this course have a final exam? y/n");
        char choice;
        while(true) {
            choice = Character.toLowerCase(input.next().charAt(0));
            if (choice == 'y') {
                return true;
            } else if (choice == 'n') {
                System.out.println("No final exam added. Course schedule creation complete.");
                return false;
            }
            else{
                System.out.println("Incorrect input, try again (enter y or n):");
            }
        }
    }
}
