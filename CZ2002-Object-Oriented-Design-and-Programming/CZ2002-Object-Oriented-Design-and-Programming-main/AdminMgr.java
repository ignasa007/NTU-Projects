package STARS;

/**
 * Used for implementing changes made by the admin
 * @author Group 3
 */

public class AdminMgr {


    /**
     * Method for adding a student to the database.
     * @param name - name of the student
     * @param email - email ID of the student
     * @param matricNo - matric number of the student
     * @param gender - gender of the student
     * @param nationality - nationality of the student
     * @param password - password for student's login to STARS
     * @return boolean value indicating if a new object was created or not
     */

    public static boolean addStudent(String name, String email, String matricNo, char gender, String nationality, String password) {
        
        if (StudentMgr.findStudent(email).getName().equals("")) {  // student doesn't already exist
            PasswordMgr.storePassword(email, password, "student");
            StudentMgr.createStudent(name, email, matricNo, gender, nationality);
            return true;
        }
        return false;

    }

    /**
     * Method for adding a course to the database.
     * @param code - course code
     * @param name - course name
     * @param school - school offering the course
     * @param AUs - number of AUs associated with the course
     * @return boolean value indicating if a new course was added to the database
     */

    public static boolean addCourse(String code, String name, String school, int AUs) { 

        if (!courseExists(code)) {  // course doesn't already exist
            CourseMgr.createCourse(code, name, school, AUs);
            return true;
        }
        return false;

    }

    /**
     * Method for updating access period of a student.
     * @param email - email ID of the student (used as an identifier)
     * @return boolean value indicating if a new course was added to the database
     */

    public static boolean updateAccess(String email) {

        Student student = StudentMgr.findStudent(email);
        if (!student.getName().equals("")) {
            student.setAccessPeriod(TimeTableMgr.createAccessPeriod());
            return true;
        }
        return false;

    }

    /**
     * Method to print the list of students enrolled in a course.
     * @param courseCode - code of the course for which the list of students is to be printed
     * @return string giving the number of students enrolled in the course and their information
     */

    public static String printStudentsByCourse(String courseCode){
        String result = "";
        int counter = 0;
        Course course = CourseMgr.findCourse(courseCode);
        if (course.getCode().equals("-1")) {
            return "No such course.";
        }
        for (Index index: course.getIndices()) {
            for(Student st: index.getStudents()) {
                result += st.printInfo() + "\n";
                counter += 1;
            }
        }
        return (counter+" students in course.\n"+result);

    }

    /**
     * Method to print the list of students enrolled in an index of a course.
     * @param indexNo - index number for which the list of students is to be printed
     * @return string giving the number of students enrolled in the index and their information
     */

    public static String printStudentsByIndex(int indexNo) {

        String result = "";
        int counter = 0;
        Index index = CourseMgr.findCourseByIndex(indexNo).findIndex(indexNo);
        if(index.getIndexNo()==-1){
            return "No such index.";
        }
        for(Student st: index.getStudents()){
            result += st.printInfo() + "\n";
            counter += 1;
        }
        return (counter+" students in index.\n"+result);

    }

    /**
     * Method to check if a course exists or not.
     * @param code - code of the course
     * @return boolean value indicating if the course exists or not
     */

    public static boolean courseExists(String code) {

        return !CourseMgr.findCourse(code).getCode().equals("-1");

    }

    /**
     * Method to change the code for a course
     * @param oldCode - old course code, which is to be changed
     * @param newCode - new course code, to which the code is to be changed
     */

    public static void changeCode(String oldCode, String newCode) {

        Course c = CourseMgr.findCourse(oldCode);
        c.setCode(newCode);

    }

    /**
     * Method to change the name of a course
     * @param code - code of the course
     * @param newName - new title for the course
     */

    public static void changeCourseName(String code, String newName) {

        Course c = CourseMgr.findCourse(code);
        c.setName(newName);

    }

    /**
     * Method to change the school to which a course belongs to
     * @param code - code of the course
     * @param newSchool - new school to which the course belongs now
     */

    public static void changeSchool(String code, String newSchool) {

        Course c = CourseMgr.findCourse(code);
        c.setSchool(newSchool);

    }

    /**
     * Method to change the number of AUs associated with a course
     * @param code - code of the course
     * @param newAUs - new number of AUs to be assigned to the course
     */

    public static void changeAUs(String code, int newAUs) {

        Course c = CourseMgr.findCourse(code);
        c.setAUs(newAUs);

    }

    /**
     * Method to add an index number to the list of indices of a course
     * @param code - course code of the index
     * @param indexNo - index number of the code
     * @param vacancy - vacancy available in the index
     */

    public static void addIndex(String code, int indexNo, int vacancy) {

        Course c = CourseMgr.findCourse(code);
        c.addIndex(indexNo, vacancy, TimeTableMgr.createTimeTable());

    }

    /**
     * Method to change the vacancy available for an index
     * @param code - course code of the index
     * @param indexNo - index number of the code
     * @param vacancy - vacancy available in the index
     * @return boolean value indicating if the course code exists 
     */

    public static boolean changeVacancy(String code, int indexNo, int vacancy) {

        Course c = CourseMgr.findCourseByIndex(indexNo);
        if (c.getCode().equals(code)) {  // index is part of course
            Index index = c.findIndex(indexNo);
            index.setVacancy(vacancy);
            return true;
        }
        return false;

    }

    /**
     * Method to print the list of all the students in the University
     * @return string giving the information of all the students
     */

    public static String printAllStudents(){
        String result = "";
        int counter = 0;
        for(Student st: StudentMgr.getStudentList()){
            result += st.printInfo() + "\n";
            counter += 1;
        }
        return (counter+" students in database.\n"+result);
    }

    public static String printAllCourses(){
        return CourseMgr.printCourseDetails();
    }
}
