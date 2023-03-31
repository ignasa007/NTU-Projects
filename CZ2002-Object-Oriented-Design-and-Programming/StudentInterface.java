package STARS;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Used to create an interface for the student to make changes
 * @author Group 3
 */

public class StudentInterface {


	/**
	 * input - Scanner object for the interface
	 * console - Console object
	 */

	private static Scanner input = new Scanner(System.in);
	private static Console console = System.console();

	/**
	 * Method to confirm the changes made by the user
	 * @return boolean value indicating if the changes were confirmed
	 */
	
	public static boolean confirmChanges() {

		//confirm if user wants to enter:
		System.out.println("1: Confirm changes\n" + "2: Exit");
		int ans;
		while (true) {
			try {
				ans = Integer.parseInt(input.next());
				break;
			}
			catch (NumberFormatException e) {
				System.out.println("Incorrect input type. Please enter an integer choice:");
			}
		}
		if (ans==1) {
			return true;
		}
		return false;

	}

	/**
	 * Method to add a course to waitlist for a student
	 * @param st - Student object requesting to add a course
	 */
	
	public static void addCourse (Student st) {  // only waitlists; student doesn't have permission to add
		
		System.out.println("Enter -1 to abort at any time.");
	    System.out.print("Index No - ");
	    int index_no;
		while (true) {
			try {
				index_no = Integer.parseInt(input.next());
				if (index_no<-1 || index_no==0) {
					System.out.println("Error. Please provide valid input:");
				}
				else
					break;
			}
			catch (NumberFormatException e) {
				System.out.println("Incorrect input type. Please enter an integer choice:");
			}
		}
	    if (index_no ==-1) {
			System.out.println("Aborting...");
	        return;
	    }
	    if (confirmChanges()) {
			System.out.println(StudentMgr.addCourse(st, index_no));
			return;
		}
		System.out.println("Aborting...");

	}

	/**
	 * Method to remove a course from the list of registered ones
	 * @param st - Student object requesting to make the change
	 * @return boolean value indicating if the course was successfully deregistered for the student
	 */

	public static boolean removeCourse(Student st) {

		System.out.println("Enter -1 to abort at any time.");
		System.out.println("List of courses registered:\n");
		checkCourses(st);  //generate list of options 
		System.out.print("Enter course option to remove: ");
		int option;
		while(true) {
			try {
				option = Integer.parseInt(input.next());
				if(option<-1 || option==0) {
					System.out.println("Error. Please provide valid input:");
				}
				else
					break;
			}
			catch (NumberFormatException e) {
				System.out.println("Incorrect input type. Please enter an integer choice:");
			}
		}
		option -= 1;
		if (option==-2) {
			return false;
		}
		if (confirmChanges()) {
			return StudentMgr.removeCourse(st, option);
		}
		return false;

	}

	/**
	 * Method to print all the courses registered for a student
	 * @param st - Student object requesting to print these courses
	 */

	public static void checkCourses(Student st) {

		System.out.println("Registered courses:");
		ArrayList<String> stringArray = StudentMgr.checkCourses(st);
		for (int i=0; i<stringArray.size(); i++){
			System.out.println(stringArray.get(i));
		}

	}

	/**
	 * Method to display the vacancy for indices requested
	 */

	public static void displayVacancy() {

		int index_no = 0;
		while(index_no != -1) {
			System.out.println("Enter -1 to abort at any time.");
			System.out.print("Index No - ");
			while(true) {
				try {
					index_no = Integer.parseInt(input.next());
					if(index_no<-1 || index_no == 0){
						System.out.println("Error: index number cannot be negative. Please provide valid input:");
					}
					else
						break;
				}
				catch (NumberFormatException e){
					System.out.println("Incorrect input type, please enter an integer choice:");
				}
			}
			System.out.println(CourseMgr.getVacancy(index_no));
		}

	}

	/**
	 * Method to change the idnex registered for in a course
	 * @param st - Student object requesting the change of index
	 * @return boolean value indicating if the change of index was successful
	 */

	public static boolean changeIndex(Student st) {

		System.out.println("Enter -1 to abort at any time.");
        System.out.print("Current Index No - ");
        int currIndex;
		while(true) {
			try {
				currIndex = Integer.parseInt(input.next());
				if (currIndex<-1 || currIndex==0) {
					System.out.println("Error: index number cannot be negative. Please provide valid input:");
				}
				else if (!StudentMgr.hasIndex(st, currIndex) && currIndex!=-1) {
					System.out.println("Error: You do not have this index number. Enter a valid index:");
				}
				else
					break;
			}
			catch (NumberFormatException e) {
				System.out.println("Incorrect input type, please enter an integer choice:");
			}
		}
		if (currIndex==-1) {
			return false;
		}
        System.out.print("New Index No - ");
        int newIndex;
		while (true) {
			try {
				newIndex = Integer.parseInt(input.next());
				if(newIndex<-1 || newIndex == 0){
					System.out.println("Error: index number cannot be negative. Please provide valid input:");
				}
				else
					break;
			}
			catch (NumberFormatException e){
				System.out.println("Incorrect input type, please enter an integer choice:");
			}
		}
        if (newIndex==-1) {
            return false;
		}
		System.out.println("Changing from "+currIndex+" to "+newIndex);
		if (confirmChanges()) {
			if (StudentMgr.changeIndex(st, currIndex, newIndex)){
				System.out.println("Index number "+currIndex+" has been changed to "+newIndex);
				return true;
			}
		}
		System.out.println("Unable to change index. Please check if the new index is for the same course and has vacancies.");
		return false;

	}

	/**
	 * Method to swap the index with a peer
	 * @param st - Student object requesting for index swap
	 * @return boolean variable indicating if the swap was successful 
	 */

	public static boolean swopIndex(Student st){
		System.out.println("Name: " + st.getName());
		System.out.println("Matriculation No: "+st.getMatricNo());
		System.out.println("Enter -1 to abort at any time.");
		System.out.print("Enter your index number - ");
	    int currIndex;
		while(true) {
			try {
				currIndex = Integer.parseInt(input.next());
				if(currIndex<-1 || currIndex == 0){
					System.out.println("Error: index number cannot be negative. Please provide valid input:");
				}
				else if (!StudentMgr.hasIndex(st, currIndex) && currIndex!=-1){
					System.out.println("Error: You do not have this index number. Input a valid index:");
				}
				else
					break;
			}
			catch (NumberFormatException e){
				System.out.println("Incorrect input type, please enter an integer choice:");
			}
		}
		if(currIndex==-1){
			return false;
		}
	    System.out.print("Enter peer's email - ");
	    String email = input.next();
	    String password;
		if (console == null) {
			System.out.println("No console available, password text will not be hidden.");
			System.out.println("Enter peer's password:");
			password = input.next();
		}
		else{
			System.out.println("Enter peer's password (will be hidden):");
			char[] pwd = console.readPassword();
			password = new String(pwd);
		}
		if(PasswordMgr.checkPassword(email, password, "student")){;} //verify credentials - do nothing
		else{
			System.out.println("Incorrect login details");
			return false;
		}
		Student st2 = StudentMgr.findStudent(email);
        System.out.print("Enter peer's index no - ");
        int newIndex;
		while(true) {
			try {
				newIndex = Integer.parseInt(input.next());
				if(newIndex<-1 || newIndex == 0){
					System.out.println("Error: index number cannot be negative. Please provide valid input:");
				}
				else if (!StudentMgr.hasIndex(st2, newIndex) && newIndex != -1){
					System.out.println("Error: You do not have this index number. Input a valid index:");
				}
				else if(currIndex == newIndex){
					System.out.println("Error: Both students have the same index. Aborting...");
					newIndex = -1;
					break;
				}
				else
					break;
			}
			catch (NumberFormatException e){
				System.out.println("Incorrect input type, please enter an integer choice:");
			}
		}
        if (newIndex==-1) {
            return false;
		}
		System.out.println("Swopping your index "+currIndex+" with index " + newIndex+" of "+st2.getName());
		if (confirmChanges()){
			if (StudentMgr.swopIndex(st, st2, currIndex, newIndex)){ 
				//update
				System.out.println(st.getMatricNo() + " - Index no " + currIndex +
								" has been successfully swopped with " +
								st2.getMatricNo() + " - Index no " + newIndex);
				return true;			
			}
		}
		System.out.println("Unable to swop index");
		return false;

	}

	/**
	 * Driver code for the class
	 * @param email - email ID of the student logged in to STARS
	 */
		
	public static void Run(String email) {

		int choice;
		Student st = StudentMgr.findStudent(email);
		if (!st.getName().equals("")) { //check if valid
			if(st.getAccessPeriod()==null){
				System.out.println("Error: you have not yet been assigned an access period.\n"+
						"Please contact the MySTARS Administrator.");
				return;
			}
			if(!st.getAccessPeriod().isAvailable()){
				System.out.println("Accessed denied: please log in during your scheduled access period: "+
						st.getAccessPeriod().printInfo());
				return;
			}
			do {
				System.out.println("Select an option:");
				System.out.println("1. Add Course\r\n" + 
						"2. Drop Course\r\n" + 
						"3. Check/Print Courses Registered\r\n" + 
						"4. Check Vacancies Available\r\n" + 
						"5. Change Index Number of Course\r\n" + 
						"6. Swop Index Number with Another Student\r\n" +
						"7. View Timetable (including waitlisted)\r\n" +
						"8. View all courses available\n" +
						"9. Log out and exit");
				Scanner input = new Scanner(System.in);
				while(true) {
					try {
						choice = Integer.parseInt(input.next());
						break;
					}
					catch (NumberFormatException e){
						System.out.println("Incorrect input type, please enter an integer choice:");
					}
				}	
				switch(choice) {
				
					case 1:
						addCourse(st);
						break;

					case 2:
						if (removeCourse(st)) { //if true - course removed
							System.out.println("Course has been successfully removed!");
						}
						else {
							System.out.println("Error: Incorrect choice of index.");
						}
						break;

					case 3:
						checkCourses(st);
						break;

					case 4:
						displayVacancy();
						break;

					case 5:
						if (changeIndex(st)){
							System.out.println("Index successfully changed");
						}
						break;

					case 6:
						swopIndex(st);
						break;
					case 7:
						System.out.println(StudentMgr.viewTimetable(st));
						break;
					case 8:
						System.out.println(StudentMgr.printCourseDetails());
						break;
					case 9:
						System.out.println("Logging out. Have a nice day!");
						break;
					default:
						System.out.println("Request not recognized.");
				}
			} while (choice<9);
		}
		else {
			System.out.println("Error: Student details do not exist.");
		}

	}

	
}
