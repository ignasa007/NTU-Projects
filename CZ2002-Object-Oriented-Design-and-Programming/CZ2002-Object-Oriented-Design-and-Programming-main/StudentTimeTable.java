package STARS;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Used to store a timetable for the student
 * @author Group 3
 */

public class StudentTimeTable implements Serializable {


	/**
	 * timetable - stores the timetable in a list format
	 */

	private LinkedList<TimeTable> timetable;

	/**
	 * Constructor method for the class - sets the timetable attribute
	 */

	public StudentTimeTable() {

		timetable = new LinkedList<TimeTable>();

	}

	/**
	 * Method to add a lesson to the student's timetable
	 * @param tt - TimeTable object for the lesson to be added
	 */

	public void add(TimeTable tt) {

		if (!this.isClashing(tt)) {
			timetable.add(tt);
		}
		
	}

	/**
	 * Method to remove a lesson from the student's timetable
	 * @param tt - TimeTable object corresponding to the lesson to be removed
	 */

	public void remove(TimeTable tt) {

		timetable.remove(tt);

	}

	/**
	 * Method to check if the student's timetable is clashing with the a lesson's timetable
	 * @param tt - timetable of the lesson to check the clash for
	 * @return boolean value indicating if there's a clash or not
	 */

	public boolean isClashing(TimeTable tt) {

		ListIterator<TimeTable> i = timetable.listIterator();
		TimeTable cur;
		while(i.hasNext()) {
			cur = i.next();
			if(tt.isClashing(cur) != null) {
				return true;
			}
		}
		return false;
	
	}

	/**
	 * Accessor method to retrieve the student's timetable
	 * @return student's timetable
	 */

	public LinkedList<TimeTable> getTimeTable() {

		return timetable;

	}

	
}
