package STARS;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Objects;

/**
 * Represents courses offered in the university
 * @author Group 3
 */

public class Course implements Serializable {


	/**
	 * code - course code, which uniquely identifies it
	 * name - course title
	 * school - name of the school offering the course
	 * indices - list of indices available for the course
	 * AUs - AU count associated with the course
	 */

	private String code;
	private String name;
	private String school;
	private LinkedList<Index> indices;
	private int AUs;

	/**
	 * Class constructor - creates a course object with given parameter list
	 * @param code - course code, which uniquely identifies it
	 * @param name - school name offering the course
	 * @param school - name of the school offering the course
	 * @param AUs - AU count associated with the course
	 */

	public Course(String code, String name, String school, int AUs) {

		this.code = code;
		this.name = name;
		this.AUs = AUs;
		this.school = school;
		indices = new LinkedList<Index>();

	}

	/**
	 * Class constructor - in the case where no parameters are provided, creates a default object 
	 */

	public Course() {
		this("-1", "-1", "-1", -1);
	}

	/**
	 * Accessor method to retrieve the number of AUs offered by a course
	 * @return AU count of the course
	 */

	public int getAUs() {

		return AUs;

	}

	/**
	 * Accessor method to retrieve the code for the course
	 * @return course code
	 */

	public String getCode() {

		return code;

	}

	/**
	 * Accessor method to retrieve the title of the course
	 * @return course title
	 */

	public String getName() {

		return name;

	}

	/**
	 * Accessor method to retrieve the name of the school offering the course
	 * @return school name
	 */

	public String getSchool() {

		return school;

	}

	/**
	 * Accessor method to retrieve the list of indices available for the course
	 * @return indices list
	 */

	public LinkedList<Index> getIndices() {

		return indices;

	}

	/**
	 * Method to add an index to the indices list for the course
	 * @param num - index number
	 * @param vacancy - vacancy available for the index
	 * @param timeTable - timetable for the index
	 */

	public void addIndex(int num, int vacancy, TimeTable timeTable) {

		indices.add(new Index(num, vacancy, this.code, timeTable));

	}

	/**
	 * Method to retrieve an index object from the list of indices available for a course
	 * @param indexNo - index number of the object required
	 * @return index object corresponding to given indexNo
	 */
	
	public Index findIndex(int indexNo) {

        Index index;
        ListIterator<Index> indicesIterator = indices.listIterator();
        while(indicesIterator.hasNext()) {
            index = indicesIterator.next();
            if (index.getIndexNo()==indexNo) {
                return index;
            }
        }
		return new Index();
		
	}
	
	/**
	 * Mutator method to set course code
	 * @param code - course code to set 
	 */

	public void setCode(String code) {

		this.code = code;

	}

	/**
	 * Mutator method to set course name
	 * @param name - course name to set
	 */

	public void setName(String name) {

		this.name = name;

	}

	/**
	 * Mutator method to set school name offering the course
	 * @param school - school name to set 
	 */

	public void setSchool(String school) {

		this.school = school;

	}

	/**
	 * Mutator method to set the number of AUs
	 * @param AUs - number of AUs to set 
	 */

	public void setAUs(int AUs) {

		this.AUs = AUs;

	}

	/**
	 * Method to retrieve course information and display it
	 * @return string containing course information
	 */

	public String printInfo() {

		return ("Course code: "+code+"\tName: "+name+"\tSchool: "+school+"\tAUs: "+AUs);

	}

	/**
	 * Overrides Object superclass' equals function to prevent duplicates while reading, writing and changing files.
	 * @param o - Object to check for equality
	 * @return boolean value indicating equal or not
	 */

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Course)) return false;
		Course course = (Course) o;
		return getCode().equals(course.getCode());
	}

}
