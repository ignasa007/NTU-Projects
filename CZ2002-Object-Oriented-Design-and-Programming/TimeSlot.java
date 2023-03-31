package STARS;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * Abstract class used for providing a skeleton for other slot classes
 * @author Group 3
 */

public abstract class TimeSlot implements Serializable {


	/**
	 * time - size 2 array containing the start and end time of the slot
	 * TYPE - type of the time slot (LEC, TUT, LAB, SEM, EXAM, ACCESS_PERIOD)
	 */

	private LocalTime[] time;
	private Type TYPE;
	
	/**
	 * Constructor method for the class
	 * @param time - size 2 array containing start and end time of the slot
	 * @param type - type of the time slot
	 */
	
	public TimeSlot(LocalTime[] time, Type type) {

		// Initialize a new time slot object
		// time must be of size 2
		this.time = time;
		this.TYPE = type;

	}

	/**
	 * Abstract method to print the time slot's info
	 * @return string containing the time slot's information
	 */
		
	public abstract String printInfo();

	/**
	 * Accessor method to retrieve the start and end time of the slot
	 * @return size 2 array attribute with the start and end time of the slot
	 */
	
	public LocalTime[] getTime() {

		return time;

	}

	/**
	 * Accessor method to get the type of the slot
	 * @return type of the slot
	 */

	public Type getType() {

		return TYPE;

	}
	
	/**
	 * Method to check if a timeslot is clashing with the object's timeslot
	 * @param ts - timeslot to check against
	 * @return boolean value indicating if there's a clash
	 */

	public boolean isClashing(TimeSlot ts) {

		// Check if 2 time slots clash
		if(this.time[0].compareTo(ts.getTime()[0])==0){
			return true;
		}
		if(this.time[1].compareTo(ts.getTime()[1])==0){
			return true;
		}
		return (this.time[0].isBefore(ts.getTime()[1]) && this.time[1].isAfter(ts.getTime()[0]));
	}


}