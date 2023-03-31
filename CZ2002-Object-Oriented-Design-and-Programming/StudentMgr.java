package STARS;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Used for implementing the changes requested by a student
 * @author Group 3
 */

public class StudentMgr {


	/**
	 * StudentList - list of all the students enrolled in the university
	 */

    private static ArrayList<Student> StudentList = new ArrayList<Student>();  // array of all students

	/**
	 * Constructor method for the class
	 */
	
	 public StudentMgr() {

		StudentList = DatabaseManager.initializeSmgr();

	}

	/**
	 * Accessor method to retrieve the list of students
	 * @return list of all the students 
	 */

    public static ArrayList<Student> getStudentList() {

		return StudentList;
		
	}
	
	/**
	 * Method to create a student object in the database
	 * @param name - name of the student
	 * @param email - email ID of the student
	 * @param matric - matric number of the student
	 * @param gender - gender of the student
	 * @param nationality - nationality of the student
	 */

    public static void createStudent(String name, String email, String matric, char gender, String nationality) {

        Student st = new Student(name, email, matric, gender, nationality);
		StudentList.add(st);
		
	}
	
	/**
	 * Method to find a student object using email ID
	 * @param email - email ID of the student
	 * @return Student object corresponding to the student being queried
	 */

    public static Student findStudent(String email) {

        Student st;
        ListIterator<Student> i = StudentList.listIterator();
        while(i.hasNext())
        {
            st = i.next();
            if(st.getEmail().equals(email)){
                return st;
            }
        }
        return new Student(); // blank name, needs to be checked for when using this function
	
	}

	/**
	 * Method to add course for a student
	 * @param st - student for which the course is to be added
	 * @param index_no - index number to be registered
	 * @return string message indicating the outcome of the attempt at adding
	 */
    
    public static String addCourse(Student st, int index_no) {

    	Course c = CourseMgr.findCourseByIndex(index_no);
        if (c.getAUs() != -1) { //check if valid
        	Index indexObj = c.findIndex(index_no);
        	if (indexObj.getIndexNo()!=1){ //check if valid
        		if(TimeTableMgr.isClashing(st.getTimeTable(), indexObj.getTimeTable())){
        			return "Error: clashes with previously registered/waitlisted courses.";
				}
				if (CourseMgr.waitlistStudent(index_no, st)){
		        	st.addCourse(c, indexObj); // add course to student obj
		        	return "Course waitlisted successfully. If assigned, you will receive an email.";
		        }
				else {
					return "Error: already applied for this course.";
				}
        	}
		}
	    return "Error: invalid index number.";
	
	}

	/**
	 * Method to remove a course from a student's course load
	 * @param st - student object to deregister course for
	 * @param option - choice number of index being removed
	 * @return boolean value indicating if deregistration was successful
	 */
      
    public static boolean removeCourse(Student st, int option) {

		if (st.getIndices().size() - 1 < option) { //selected option
			return false;
		}
		Index indexObj = st.getIndices().get(option); //index object of selected option
		int indexNo = indexObj.getIndexNo();
		Course c = CourseMgr.findCourseByIndex(indexNo);
		if (c.getAUs() != -1) { //check if valid
			if (CourseMgr.deRegisterStudent(st, c, indexNo)) {
				st.removeCourse(c, indexObj);
				CourseMgr.flushWaitlist(indexObj);
				return true;
			}
		}
		return false;

	}
	
	/**
	 * Method to retrieve all the courses registered for by a student
	 * @param st - student object for which the list of courses is to be retrieved
	 * @return list of strings, one for each course and the corresponding index
	 */
        
	public static ArrayList<String> checkCourses(Student st) {

		ArrayList<String> stringArray = new ArrayList<>();
		if (st.getIndices().size() == 0) {
			stringArray.add("No courses registered.");
		}
		else {
			for (int j=0; j<st.getIndices().size(); j++) {
				Index indexObj = st.getIndices().get(j);
				if (indexObj.isRegistered(st)) {
					stringArray.add(j + 1 + ". Course: " + indexObj.getCourseCode() + " Index: " + indexObj.getIndexNo());
				}    //j+1 used as this is used as option number in StudentInterface.removeCourses() fn
			}
		}
		return stringArray;

	}

	/**
	 * Method to change the index for a course opted by the student
	 * @param st - student object for which the index is to be changed
	 * @param currIndex - current index number registered for
	 * @param newIndex - new index number to be changed to 
	 */
	
	public static boolean changeIndex(Student st, int currIndex, int newIndex) {

        //check if they belong to same course
        Course c = CourseMgr.findCourseByIndex(currIndex);
        Course c2 = CourseMgr.findCourseByIndex(newIndex);
        if (!c.getCode().equals(c2.getCode())) {
        	return false;
        }
		//find index obj of new index
		Index newIndexObj = c.findIndex(newIndex);
		//deregister currIndex and register newIndex in CourseMgr
		if (newIndexObj.getVacancy()>0 && CourseMgr.deRegisterStudent(st, c, currIndex)) {
			CourseMgr.registerStudent(st, c, newIndex);
			//change list of indices for student object
			st.setIndex(currIndex, newIndexObj);
			return true;
		}
		return false;  
		
	}

	/**
	 * Method to swap index between two students
	 * @param st - first student in the swap
	 * @param st2 - second student in the swap
	 * @param currIndex - index opted for by the first student
	 * @param newIndex - index opted for by the second student
	 */
	
	public static boolean swopIndex(Student st, Student st2, int currIndex, int newIndex) {
		
		//check if they belong to same course
        Course c = CourseMgr.findCourseByIndex(currIndex);
        Course c2 = CourseMgr.findCourseByIndex(newIndex);
        if (!c.getCode().equals(c2.getCode())) {
        	return false;
        }
        else {
        	//find index obj of new index
        	Index newIndexObj =c.findIndex(newIndex);
        	//find index obj of current index
        	Index currIndexObj = c.findIndex(currIndex);
			if (!(CourseMgr.isRegistered(currIndexObj, st) && CourseMgr.isRegistered(newIndexObj, st2))) {
        		// This will check if either of the two haven't registered for the course
        		return false;
        	}
			//deregister currIndex and register newIndex in CourseMgr
			if (CourseMgr.deRegisterStudent(st, c, currIndex)) {
				if (CourseMgr.deRegisterStudent(st2, c, newIndex)) {
					st.getTimeTable().remove(currIndexObj.getTimeTable());
					st2.getTimeTable().remove(newIndexObj.getTimeTable());
					if (!(TimeTableMgr.isClashing(st.getTimeTable(), newIndexObj.getTimeTable())||TimeTableMgr.isClashing(st2.getTimeTable(), currIndexObj.getTimeTable()))) {
						st.setIndex(currIndex, newIndexObj);
						
						st2.setIndex(newIndex, currIndexObj);
						CourseMgr.registerStudent(st, c, newIndex);
						CourseMgr.registerStudent(st2, c, currIndex);
						return true;
					}
					else {
						CourseMgr.registerStudent(st, c, currIndex);
						CourseMgr.registerStudent(st2, c, newIndex);
						st.getTimeTable().add(currIndexObj.getTimeTable());
						st2.getTimeTable().add(newIndexObj.getTimeTable());
					}
				}
				else { // student 2 failed to deregister, may not have had course
					CourseMgr.registerStudent(st, c, currIndex);
				}
	        }
 		}
		return false;
		
	}

	/**
	 * Method to view the timetable of a student
	 * @param st - student object for which the timetable is to be displayed
	 * @return string object describing the timetable
	 */

	public static String viewTimetable(Student st){
		if(st.getTimeTable().getTimeTable().size()==0){
			return "No courses registered or waitlisted.";
		}
		String result = "";
		for(int i=0; i<st.getTimeTable().getTimeTable().size(); i++){
			result += "Course: "+st.getIndices().get(i).getCourseCode()+"\tIndex: "+st.getIndices().get(i).getIndexNo()+"\n";
			TimeTable table = st.getTimeTable().getTimeTable().get(i);
			for (WorkSlot workSlot: table.getSlots()){ //for every WorkSlot
				result += workSlot.printInfo() + "\n";
			}
			try {
				ExamSlot eslot = table.getExam(); //examslot
				result += eslot.printInfo() + "\n";
			}
			catch (NullPointerException e){  // triggers when no exam
				result += "No exam for this course.\n";
			}
			result += "---------------------------------------------\n";
		}
		return result;
	}

	/**
	 * Method to print the details for all the courses
	 * @return string containing the information about all the courses
	 */

	public static String printCourseDetails() {

		return CourseMgr.printCourseDetails();

	}

	/**
	 * Method to check if the student is enrolled for an index
	 * @param st - Student object associated with the student
	 * @param indexNo - index number to check for
	 * @return boolean value indicating if a student is register for the index
	 */

	public static boolean hasIndex(Student st, int indexNo) {

		for (Index index : st.getIndices()) { 
			if (index.getIndexNo() == indexNo) {
				return true;
			}
		}
		return false;

	}


}

