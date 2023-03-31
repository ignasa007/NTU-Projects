package STARS;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents a student
 * @author Group 3
 */

public class Student /*extends User*/ implements Serializable {


	/**
	 * name - name of the student
	 * email - email ID of the student
	 * matricNo - matric number of the student
	 * gender - gender of the student ("M" or "F")
	 * nationality - nationality of the student
	 * indices - list of indices in which the student is enrolled
	 * accessPeriod - period of time for the student to access the STARS system
	 * AUs - number of academic units being studied by the student 
	 * timetable - timetable of the student
	 * AU_LIMIT - maximum number of academic units any student can register for
	 */
	
	private final String name;
	private final String email;
	private final String matricNo;
	private final char gender;
	private final String nationality;
	private ArrayList<Index> indices;
	private AccessPeriod accessPeriod;
	private int AUs;
	private StudentTimeTable timetable;
	public static final int AU_LIMIT = 21;

	/**
	 * Constructor method for the Student object
	 * @param name - name of the student
	 * @param email - email ID of the student
	 * @param matric - matric number of the student
	 * @param gender - gender of the student
	 * @param nationality - nationality of the student
	 */
	
	protected Student(String name, String email, String matric, char gender, String nationality) {
		
		this.name = name;
		this.email = email;
		matricNo = matric;
		this.gender = gender;
		this.nationality = nationality;
		timetable = new StudentTimeTable();
		indices = new ArrayList<Index>();
		accessPeriod = null;

	}

	/**
	 * Empty constructor for theStudent object
	 */

	public Student() {

		this("", "",  "", 'M', "");

	}

	/**
	 * Method to add a course to a student's course load
	 * @param course - Course object for the course to be added
	 * @param index - Index object for the index of the course added
	 */

	public void addCourse(Course course, Index index) {

		// add index to indices
		indices.add(index);
		// add course.GetAUs to AUs
		AUs += course.getAUs();
		// change timetable
		timetable.add(index.getTimeTable());

	}

	/**
	 * Method to remove a course from a student's course load
	 * @param course - Course object for the course to be removed
	 * @param index - Index object for the index of the course to be removed
	 */
	
	public void removeCourse(Course course, Index index) {

		// remove index from indices
		indices.remove(index);
		// remove course.GetAUs to AUs
		AUs -= course.getAUs();
		// change timetable
		timetable.remove(index.getTimeTable());

	}

	/**
	 * Method to get a student's information
	 * @return string containing the relevant information of the student
	 */

	public String printInfo() {

		return ("Name: " + name + "\tEmail: " + email + "\tMatric number: " + matricNo);

	}

	/**
	 * Accessor method to get a student's name
	 * @return student's name 
	 */
	
	public String getName() {

		return name;

	}

	/**
	 * Accessor method to get a student's gender
	 * @return student's gender
	 */

	public char getGender() {

		return gender;

	}

	/**
	 * Accessor method to get a student's matric number
	 * @return student's matric number
	 */

	public String getMatricNo() {

		return matricNo;

	}

	/**
	 * Accessor method to get the student's registered number of AUs
	 * @return number of AUs the student is registered for
	 */

	public int getAUs() {

		return AUs;

	}

	/**
	 * Accessor method to get a student's nationality
	 * @return student's nationality
	 */

	public String getNationality() {

		return nationality;

	}

	/**
	 * Accessor method to get the list of indices a student is enrolled for
	 * @return indices list for the student
	 */

	public ArrayList<Index> getIndices() {

		return indices;

	}

	/**
	 * Accessor method to get a student's access period to STARS
	 * @return student's access period
	 */

	public AccessPeriod getAccessPeriod() {

		return accessPeriod;

	}

	/**
	 * Accessor method to get a student's email ID
	 * @return student's email ID
	 */

	public String getEmail() {

		return email;

	}

	/**
	 * Accessor method to get student's timetable
	 * @return student's timetable
	 */

	public StudentTimeTable getTimeTable() {

		return timetable;

	}

	/**
	 * Method to change the index for some course
	 * @param currIndex - current index for the course
	 * @param newIndexObj - Index object to replace the old index
	 */

	public void setIndex(int currIndex, Index newIndexObj) { // newIndex to change

		for (int i=0; i<indices.size(); i++) {
			if (indices.get(i).getIndexNo()==currIndex) {
				timetable.remove(indices.get(i).getTimeTable());
				indices.set(i, newIndexObj);
				timetable.add(newIndexObj.getTimeTable());
				break;
			}
		}

	}

	/**
	 * Mutator method to set the access period for the student
	 * @param accessPeriod - new AccessPeriod object for the student 
	 */

	public void setAccessPeriod(AccessPeriod accessPeriod) {

		this.accessPeriod = accessPeriod;

	}

	/**
	 * Overrides Object superclass' equals function to prevent duplicates while reading, writing and changing files.
	 * @param o - Object to check for equality
	 * @return boolean value indicating equal or not
	 */

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Student)) return false;
		Student student = (Student) o;
		return getEmail().equals(student.getEmail());
	}

}
