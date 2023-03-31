package STARS;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Used for implementing changes made to a course
 * @author Group 3
 */

public class CourseMgr {


    /**
     * courseList - a list of all the courses
     */

    private static ArrayList<Course> courseList = new ArrayList<Course>();  // array of all courses

    /**
     * Constructor method for the class - initializes the courseList.
     */

    public CourseMgr() {

        courseList = DatabaseManager.initializeCmgr();
        
    }

    /**
     * Accessor method to retrieve list of all courses.
     * @return courseList attribute of the class
     */
    
    public static ArrayList<Course> getCourseList() {

        return courseList;

    }

    /**
     * Method to create a course object, and add it to the course list.
     * @param code - course code
     * @param name - course title
     * @param school - name of the school offering the course
     * @param AUs - number of AUs associated with the course 
     */

    public static void createCourse(String code, String name, String school, int AUs) {

        Course course = new Course(code, name, school, AUs);
        courseList.add(course);

    }

    /**
     * Accessor method to retrieve a course object.
     * @param code - course code which uniquely identifies a course object
     * @return corresponding course object, and if the course code does not exist, an object with default constructor is returned
     */

    public static Course findCourse(String code) {

        Course course;
        ListIterator<Course> courses = courseList.listIterator();
        while(courses.hasNext()) {
            course = courses.next();
            if (course.getCode().equals(code)) {
                return course;
            }
        }
        return new Course(); // blank name, needs to be checked for when using this function

    }

    /**
     * Retrieve course object corresponding to a specific index number.
     * @param indexNo - index number uniquely identifying the index
     * @return corresponding course object, and if the course code does not exist, an object with default constructor is returned
     */
    
    public static Course findCourseByIndex(int indexNo) { //to find course with given index

    	Course course;
    	LinkedList<Index> indicesList;
    	for (int i=0; i<courseList.size(); i++) { // for every course
    		course = courseList.get(i);
    		indicesList = course.getIndices();
    		for (int j=0; j<indicesList.size(); j++) { // for every index in course
    			Index indexObj = indicesList.get(j);
    			if (indexObj.getIndexNo()==indexNo) {
    				return course;
    			}
            }
    	}
        return new Course(); //need to check
        
    }

    /**
     * Method to get the vacancy available in an index.
     * @param indexNo - index number
     * @return string with the information about the index and its vacancy count
     */

    public static String getVacancy(int indexNo) {

        Course course = findCourseByIndex(indexNo);
        if (course.getCode().equals("")) {
            return ("Index number not found.");
        }
        if (course.getAUs()!=-1) { // check if valid
            Index indexObj = course.findIndex(indexNo); // find index obj that index belongs to
            if (indexObj.getIndexNo()!=-1) { // check if valid
                return ("Index: " + indexNo + "\tVacancy: " + indexObj.getVacancy() + "\tWaitlisted: " + indexObj.getWaitlist().size());
            }
            else {
                return ("Index number not found.");
            }
        }
        else {
            return ("Course code not found.");
        }

    }

    /**
     * Method to register a student to a course.
     * @param student - Student object to add course to
     * @param course - Course object to register for a student
     * @param indexNo - indexNo of the course to register
     * @return boolean value indicating if the student was successful in registering for the course or not
     */

    public static boolean registerStudent(Student student, Course course, int indexNo) {

        if (student.getAUs()+course.getAUs()>Student.AU_LIMIT) {
            System.out.println("AU limit of " + Student.AU_LIMIT + " reached for " + student.getName() +".");
            return false;
        }
        else {
            Index index = course.findIndex(indexNo);
            if (index.getIndexNo()==-1) {
                System.out.println("Index does not exist.");
                return false;
            }
            else if (index.getVacancy()==0) {
                System.out.println("Index has no vacancy.");
                return false;
            }
            else {
                index.registerStudent(student);
                return true;
            }
        }

    }

    /**
     * Method to deregister a student from a course.
     * @param student - Student object for which the course is to be deregistered
     * @param course - Course object to be deregister the student from 
     * @param indexNo - index number of the course to deregister the student from
     * @return boolean value indicating if the student was successfully deregistered from the course or not
     */

    public static boolean deRegisterStudent(Student student, Course course, int indexNo) {

        Index index = course.findIndex(indexNo);
        if(index.getIndexNo()==-1){
            System.out.println("Index doesn't exist");
            return false;
        }
        else{
            index.deRegister(student);
            return true;
        }

    }

    /**
     * Method to register the index for the students on the top of the waitlist
     * @param index - index object to flush the waitlist for
     * @return list of students added to the index
     */
    
    public static LinkedList<Student> flushWaitlist(Index index) {

        return index.flushWaitlist();
        
    }

    /**
     * Method to add a student to the waitlist of an index
     * @param indexNo - index number of the index
     * @param student - Student object for the student to be added to the waitlist
     * @return boolean value indicating if the student was successfully added to the waitlist
     */

    public static boolean waitlistStudent(int indexNo, Student student) {

        Index index = findCourseByIndex(indexNo).findIndex(indexNo);
        if (index.addToWaitlist(student)) {
            flushWaitlist(index);
            return true;
        }
        return false;

    }

    /**
     * Method to check if a student is registered for an index
     * @param index - Index object for the index to check in
     * @param student - Student object for the student to check for
     * @return boolean value indicating if the student is registered for the index
     */

    public static boolean isRegistered(Index index, Student student) {

        return index.isRegistered(student);
        
    }

    /**
     * Method to print the details of all the indices of all the courses
     * @return string containing the information about all the indices in all the courses
     */

    public static String printCourseDetails() {

        String result = "";
        int counter = 0;
        for(Course course: CourseMgr.getCourseList()){
            result += course.printInfo() + "\n";
            System.out.println("List of indexes available (" + course.getIndices().size() + " total):\n");
            for(Index index: course.getIndices()){
                result += index.printInfo()+"\n";
            }
            result += "--------------------------\n";
            counter += 1;
        }
        return counter+" courses in database.\n"+result;

    }


}
