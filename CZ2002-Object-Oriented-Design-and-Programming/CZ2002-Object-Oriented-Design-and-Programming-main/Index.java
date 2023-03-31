package STARS;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Used to represent an index in a course
 * @author Group 3
 */

public class Index implements Serializable {


	/**
	 * indexNo - index number of the index object
	 * courseCode - code of the course to which the index belongs
	 * students - list of students in the course
	 * vacancy - available vacancy count for the index
	 * waitlist - waitlist for the index
	 * timetable - semester timetable for the index
	 */

	private final int indexNo;
	private final String courseCode;
	private ArrayList<Student> students;
	private int vacancy;
	private LinkedList<Student> waitlist;
	private TimeTable timetable;

	/**
	 * Constructor method for the index class.
	 * @param indexNo - index number of the index object
	 * @param vacancy - available vacancy count for the index
	 * @param courseCode - course code of the course to which the index belongs
	 * @param timetable - semester timetable of the index
	 */

	public Index(int indexNo, int vacancy, String courseCode, TimeTable timetable) {

		this.indexNo = indexNo;
		this.vacancy = vacancy;
		this.courseCode = courseCode;
		this.students = new ArrayList<Student>();
		this.waitlist = new LinkedList<Student>();
		this.timetable = timetable;

	}

	/**
	 * Empty constructor for the index object.
	 */

	public Index() {

		this(-1, -1, "-1", null);

	}
	
	/**
	 * Method to register a student for an index.
	 * @param student - object for the student to be registered for the index
	 */

	public void registerStudent(Student student) {

		students.add(student);
		vacancy -= 1;

	}

	/**
	 * Method to deregister a student from an index.
	 * @param student - object for the student to be deregistered from the index
	 */
	
	public void deRegister(Student student) {

		if (students.remove(student)) {
			vacancy += 1;
		}
		waitlist.remove(student);

	}

	/**
	 * Method to add a student to the waitlist.
	 * @param student - object for the student to be added to the waitlist
	 * @return boolean value indicating if the student was successfully registered for the course or not
	 */
	
	public boolean addToWaitlist(Student student) {

		if (waitlist.contains(student) || students.contains(student)) {
			return false;
		}
		waitlist.addLast(student);
		return true;

	}

	/**
	 * Method to register the index for the students on the top of the waitlist
	 * @return list of students added to the index
	 */

	public LinkedList<Student> flushWaitlist() {

		LinkedList<Student> list = new LinkedList<Student>();
		Student temp;
		while (!waitlist.isEmpty() && vacancy>0) {
			temp = waitlist.pop();
			if(temp.getAUs()+CourseMgr.findCourse(courseCode).getAUs()<=Student.AU_LIMIT) {
				registerStudent(temp);
				list.add(temp);
			}
		}
		Notifier notifier = new NotifyByEmail();
		String subject = "Successful Registration of Course";
		String body = "Dear student,\n\nYou have been successfully registered for the course " + courseCode +
				" under index number " + indexNo + ". Log in to STARS to view your updated timetable." +
				"\n\nBest regards,\nSTARS Course Registration Team";
		String[] emails = new String[list.size()];
		for(int i=0; i<list.size(); i++) {
			emails[i] = list.get(i).getEmail();
		}
		notifier.sendNotification(emails, subject, body);
		return list;

	}

	/**
	 * Method to check if a student is registered for this index
	 * @param student - object for the student to check for
	 * @return boolean value indicating if the student is registered for the course
	 */

	public boolean isRegistered(Student student) {
		 
		return students.contains(student);
		 
	}

	/**
	 * Method to print information for an index
	 * @return string containing the information of an index
	 */

	public String printInfo() {

		return ("Index number: "+indexNo+"\tVacancies: "+vacancy+"\tWaitlisted: "+waitlist.size());
	
	}

	/**
	 * Accessor method to retrieve the vacancy count of the index
	 * @return vacancy in the index
	 */

	public int getVacancy() {

		return vacancy;

	}

	/**
	 * Accessor method to retrieve the index number of the index
	 * @return index number of the index
	 */

	public int getIndexNo() {

		return indexNo;

	}

	/**
	 * Accessor method to retrieve the code of the course the index belongs to
	 * @return code of the corresponding course
	 */

	public String getCourseCode() {

		return courseCode;

	}

	/**
	 * Accessor method to retrieve the student list for the index
	 * @return list of students enrolled in the course
	 */

	protected ArrayList<Student> getStudents() {

		return students;

	}

	/**
	 * Accessor method to retrieve the timetable for the index
	 * @return index timetable
	 */

	public TimeTable getTimeTable() {

		return timetable;

	}

	/**
	 * Accessor method to retrieve the waitlist of the index
	 * @return index's waitlist
	 */

	public LinkedList<Student> getWaitlist() {

		return waitlist;

	}

	/**
	 * Mutator method to set the vacancy count of the index
	 * @param vacancy - value to set for vacancy count of the index
	 */

	public void setVacancy(int vacancy) {

		this.vacancy = vacancy;

	}

	/**
	 * Mutator method to set the timetable count of the index
	 * @param tt - object to set as timetable for the index 
	 */

	public void setTimeTable(TimeTable tt) {

		timetable = tt;

	}

	/**
	 * Overrides Object superclass' equals function to prevent duplicates while reading, writing and changing files.
	 * @param o - Object to check for equality
	 * @return boolean value indicating equal or not
	 */

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Index)) return false;
		Index index = (Index) o;
		return getIndexNo() == index.getIndexNo() &&
				getCourseCode().equals(index.getCourseCode());
	}

}

