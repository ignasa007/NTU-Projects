package STARS;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Used for defining the time interval in which a student is allowed to access STARS
 * @author Group 3
 */

public class AccessPeriod extends TimeSlot {


	/**
	 * date - Date on which access period is granted to a student
	 */

	private LocalDate[] date;

	/**
	 * Constructor method for AccessPeriod class. Constructor of the super class - TimeSlot - is also called.
	 * @param date - sets the date attribute of the class
	 * @param time - gives the time of the day for access period
	 */

	public AccessPeriod(LocalDate[] date, LocalTime[] time) { 

		super(time, Type.ACCESS_PERIOD);
		this.date = date;

	}

	/**
	 * Returns false by default since access periods never clash. Needed to override isClashing method from super class.
	 * @param t - access time 
	 * @return false
	 */

	public boolean isClashing(TimeSlot t) {

		return false;

	}

	/**
	 * Checks if STARS is accessible at a particular time.
	 * @return boolean variable indicating if the system is accessible or not
	 */

	public boolean isAvailable() {

		LocalDateTime t_start = LocalDateTime.of(date[0], this.getTime()[0]);
		LocalDateTime t_end = LocalDateTime.of(date[1], this.getTime()[1]);
		LocalDateTime t_now = LocalDateTime.now();
		return (t_start.isBefore(t_now)) && (t_now.isBefore(t_end));

	}

	/**
	 * Used to retrieve the access period for a student 
	 * @return access period as a string in "(start time) to (end time)" format
	 */

	public String printInfo(){
		LocalDateTime t_start = LocalDateTime.of(date[0], this.getTime()[0]);
		LocalDateTime t_end =LocalDateTime.of(date[1], this.getTime()[1]);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String start = t_start.format(formatter);
		String end = t_end.format(formatter);
		return start + " to " + end;
	}

	
}