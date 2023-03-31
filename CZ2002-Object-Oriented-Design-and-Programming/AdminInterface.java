package STARS;
import java.io.Console;
import java.util.Scanner;

/**
 * Used for creating an interface for the admin
 * @author Group 3
 */

public class AdminInterface {


	/**
	 * input - Scanner object for the interface
	 * console - Console object for methods to access character-based console device
	 */

	private static Scanner input = new Scanner(System.in);
	private static Console console = System.console();

	/**
	 * Driver code to create admin interface.
	 */

	public static void Run() {

		int choice;
		do {
			System.out.println("Select an option:");
			System.out.println("1. Add Student\n" +
					"2. Add Course\n" +
					"3. Update Course\n" + //contains add index and update vacancy as index cannot exist without course
					"4. Edit Student Access Period\n" +
					"5. Check Vacancies by Index Number\n" +
					"6. Print Student List by Course\n" +
					"7. Print Student List by Index Number\n" +
					"8. Log out and exit");
			while(true) {
				try {
					choice = Integer.parseInt(input.next());
					break;
				}
				catch (NumberFormatException e) {
					System.out.print("Incorrect input type. Please enter an integer choice - ");
				}
			}
			switch (choice) {
				case 1:
					//create student
					getStudentInput();
					break;
				case 2:
					//create course
					getCourseInput();
					break;
				case 3:
					//update course
					updateCourseMenu();
					break;
				case 4:
					//edit student access period
					System.out.print("Enter student email - ");
					if (AdminMgr.updateAccess(input.next())) {
						System.out.println("Access period updated successfully.");
					}
					else {
						System.out.println("Error: Student email not found.");
					}
					break;
				case 5:
					//check vacancies by index
					displayVacancy();
					break;
				case 6:
					//print student list by course
					System.out.println("Enter course code - ");
					System.out.println(AdminMgr.printStudentsByCourse(input.next()));
					break;
				case 7:
					//print student list by index number
					System.out.println("Enter index number - ");
					int indexNo;
					while (true) {
						try {
							indexNo = Integer.parseInt(input.next());
							if (indexNo<0) {
								System.out.print("Error: index number cannot be negative. Please provide valid input - ");
							}
							else
								break;
						}
						catch (NumberFormatException e) {
							System.out.print("Incorrect input type. Please enter an integer choice - ");
						}
					}
					System.out.println(AdminMgr.printStudentsByIndex(indexNo));
					break;
				case 8:
					System.out.println("Logging out. Have a nice day!");
					break;
				default:
					System.out.println("Request not recognized. Please try again.\n");
			}
		} while (choice != 8);
	}

	/**
	 * Method for displaying vacancies of requested indices.
	 */

	public static void displayVacancy() {

		int index_no;
		do {
			System.out.println("Enter -1 to abort at any time.");
			System.out.print("Index number - ");
			while (true) {
				try {
					index_no = Integer.parseInt(input.next());
					break;
				}
				catch (NumberFormatException e) {
					System.out.print("Incorrect input type. Please enter an integer choice - ");
				}
			}
			System.out.println(CourseMgr.getVacancy(index_no));
		} while (index_no != -1);

	}

	/**
	 * Interface for creating a student object in the database.
	 */

	public static void getStudentInput() {

		input.nextLine();  //reads \n from previous input
		System.out.print("Enter name - ");
		String name = input.nextLine();
		System.out.print("Enter email - ");
		String email = input.nextLine();
		System.out.print("Enter matric number - ");
		String matricNo = input.nextLine();
		while (matricNo.length()!= 9|| Character.isDigit(matricNo.charAt(0))|| Character.isDigit(matricNo.charAt(8))){
			System.out.println("Enter a valid matric number");
			matricNo = input.nextLine();
		}
		System.out.print("Enter gender - ");
		char gender = input.nextLine().charAt(0);
		while (gender!='M' && gender!='F') {
			System.out.print("Invalid input. Please choose from M and F - ");
			gender = input.nextLine().charAt(0);
		}
		System.out.print("Enter nationality - ");
		String nationality = input.nextLine();
		String password;
		if (console == null) {
			System.out.println("Not running on terminal. Password text will not be hidden.");
			System.out.print("Enter password - ");
			password = input.next();
		} 
		else {
			System.out.println("Running on terminal. Password text will be hidden.");
			System.out.print("Enter password - ");
			char[] pwd = console.readPassword();
			password = new String(pwd);
		}
		if (AdminMgr.addStudent(name, email, matricNo, gender, nationality, password)) {
			System.out.println("Student successfully added!");
			System.out.println("Add an access period for this student - ");
			if (AdminMgr.updateAccess(email)) {
				System.out.println("Access period assigned successfully.");
			}
			else {
				System.out.println("An error occurred in assigning access period. Please try again through the Update Access Period option.");
			}
			System.out.println(AdminMgr.printAllStudents());
		}
		else {
			System.out.println("Error: student email already exists.");
		}
	}

	/**
	 * Interface for creating a course object in the database.
	 */

	public static void getCourseInput() {

		input.nextLine();  //reads \n from previous input
		System.out.print("Enter course code - ");
		String code = input.nextLine();
		System.out.print("Enter course name - ");
		String name = input.nextLine();
		System.out.print("Enter school - ");
		String school = input.nextLine();
		System.out.print("Enter AUs - ");
		int AUs;
		while (true) {
			try {
				AUs = Integer.parseInt(input.next());
				if (AUs<0) {
					System.out.print("Error: AUs cannot be negative. Please provide a valid input - ");
				}
				else {
					break;
				}
			}
			catch (NumberFormatException e) {
				System.out.print("Incorrect input type. Please enter an integer choice - ");
			}
		}
		if (AdminMgr.addCourse(code, name, school, AUs)) {
			System.out.println("Course added successfully! Please use \"Update Course\" to add indices and timetables.");
			System.out.println(AdminMgr.printAllCourses());
		}
		else {
			System.out.println("Error: course code already exists.");
		}

	}

	/**
	 * Interface for updating a course object in the database.
	 */

	public static void updateCourseMenu(){

		input.nextLine();  //reads \n from previous input
		System.out.print("Enter a course code to modify - ");
		String code = input.next();
		while (!AdminMgr.courseExists(code)) {
			System.out.print("Error: course does not exist.\nEnter a valid course code - ");
			code = input.next();
		}
		int choice;
		do {
			System.out.println("Select an option:");
			System.out.println("1. Update course code\n" +
					"2. Update course name\n" +
					"3. Update school\n" +
					"4. Update AUs\n" +
					"5. Add index\n" +
					"6. Change vacancy for index\n" +
					"7. Back to main menu");
			while (true) {
				try {
					choice = Integer.parseInt(input.next());
					break;
				}
				catch (NumberFormatException e) {
					System.out.print("Incorrect input type. Please enter an integer choice - ");
				}
			}
			input.nextLine();
			switch (choice) {
				case 1:
					System.out.print("Enter a new course code (-1 to abort) - ");
					String newCode = input.nextLine();
					if(newCode.equals("-1")){
						break;
					}
					while (AdminMgr.courseExists(newCode)) {
						System.out.println("Error: code already in use.");
						newCode = input.nextLine();
					}
					AdminMgr.changeCode(code, newCode);
					code = newCode;  //updating code so that remaining functions update the correct course.
					break;
				case 2:
					System.out.print("Enter new course name (-1 to abort) - ");
					String newName = input.nextLine();
					if (newName.equals("-1")){
						break;
					}
					AdminMgr.changeCourseName(code, newName);
					break;
				case 3:
					System.out.print("Enter new school (-1 to abort) - ");
					String newSchool = input.nextLine();
					if(newSchool.equals("-1")){
						break;
					}
					AdminMgr.changeSchool(code, newSchool);
					break;
				case 4:
					System.out.print("Enter new number of AUs (-1 to abort) - ");
					int newAUs;
					while (true) {
						try {
							newAUs = Integer.parseInt(input.next());
							if (newAUs<-1) {
								System.out.print("Error: AUs cannot be negative. Please provide valid input - ");
							}
							else
								break;
						}
						catch (NumberFormatException e) {
							System.out.print("Incorrect input type. Please enter an integer choice - ");
						}
					}
					input.nextLine();
					if(newAUs==-1){
						break;
					}
					AdminMgr.changeAUs(code, newAUs);
					break;
				case 5:
					//add index
					System.out.print("Enter an index number (-1 to abort) - ");
					int indexNo;
					while (true) {
						try {
							indexNo = Integer.parseInt(input.next());
							if (indexNo<-1) {
								System.out.print("Error: index number cannot be negative. Please provide valid input - ");
							}
							else
								break;
						}
						catch (NumberFormatException e) {
							System.out.print("Incorrect input type. Please enter an integer choice - ");
						}
					}
					if(indexNo==-1){
						break;
					}
					System.out.print("Enter number of vacancies - ");
					int vac;
					while(true) {
						try {
							vac = Integer.parseInt(input.next());
							if (vac<0) {
								System.out.print("Error: vacancies cannot be negative. Please provide valid input - ");
							}
							else
								break;
						}
						catch (NumberFormatException e) {
							System.out.print("Incorrect input type. Please enter an integer choice - ");
						}
					}
					input.nextLine();
					System.out.println("Creating timetable - class slots will be added one at a time.");
					AdminMgr.addIndex(code, indexNo, vac);
					break;
				case 6:
					//change vacancy for index
					System.out.print("Enter an index number (-1 to abort) - ");
					int indexNo2;
					while (true) {
						try {
							indexNo2 = Integer.parseInt(input.next());
							if (indexNo2<-1) {
								System.out.print("Error: index number cannot be negative. Please provide valid input - ");
							}
							else
								break;
						}
						catch (NumberFormatException e) {
							System.out.println("Incorrect input type. Please enter an integer choice - ");
						}
					}
					if(indexNo2==-1){
						break;
					}
					System.out.print("Enter number of vacancies - ");
					int vac2;
					while (true) {
						try {
							vac2 = Integer.parseInt(input.next());
							if (vac2<0) {
								System.out.println("Error: vacancies cannot be negative. Please provide valid input - ");
							}
							else
								break;
						}
						catch (NumberFormatException e) {
							System.out.print("Incorrect input type. Please enter an integer choice - ");
						}
					}
					input.nextLine();
					if (AdminMgr.changeVacancy(code, indexNo2, vac2)) {
						System.out.println("Vacancy changed successfully!");
					}
					else {
						System.out.println("Error: index number not part of course.");
					}
					break;
				case 7:
					System.out.println("Returning to menu...");
				default:
					System.out.println("Request not recognized. Please try again.\n");
			}
		} while (choice != 7);

	}
	

}