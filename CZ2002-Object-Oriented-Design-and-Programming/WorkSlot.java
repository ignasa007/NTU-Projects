package STARS;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Used for making an object for each work slot
 * @author Group 3
 */

public class WorkSlot extends TimeSlot {


	/**
	 * N_STUDY_WEEKS - number of study weeks in a semester set to constant 13
	 * day - day of the week for the work associated with the work slot
	 * workWeek - list of weeks on which the work slot is applicable
	 * venue - venue of the work associated with the work slot
	 */

	private static final int N_STUDY_WEEKS = 13;
	private DayOfWeek day;
	private boolean [] workWeek;
	private String venue;

	/**
	 * Constructor method for the work slot object
	 * @param time - time of the day for the work slot
	 * @param workWeek - list of weeks on which the work slot is applicable
	 * @param day - ay of the week for the work associated with the work slot
	 * @param type - type of work associated with the work slot
	 * @param venue - venue of the work associated with the work slot
	 */

	public WorkSlot(LocalTime[] time, boolean[] workWeek, DayOfWeek day, Type type, String venue) {

		super(time, type);
		this.day = day;
		this.workWeek = workWeek;
		this.venue = venue;

	}

	/**
	 * Method to print the information for a work slot
	 * @return string containing the information
	 */

	public String printInfo() {

		String info = "";
		info += String.format("%s \t %s - %s \n", day.getDisplayName(TextStyle.FULL, new Locale("en")), this.getTime()[0].toString(), this.getTime()[1].toString());
		info += "Type - " + this.getType().toString() + "\n";
		info += "Venue - " + venue + "\n";
		return info;

	}

	/**
	 * Method to check if a work slot is clashing with the slot of the object
	 * @param ws - work slot to check against
	 * @return boolean value indicating if there's a clash between the two slots
	 */

	public boolean isClashing(WorkSlot ws) {

		if (this.day.equals(ws.getDay())) {  //IF in the same day
			for (int i=0; i<13; i++) {
				if (this.workWeek[i] && ws.getWorkWeek()[i]) { // if in the same week
					return super.isClashing(ws);    // if at the same time in the day
				}
			}
		}
		return false;

	}

	/**
	 * Accessor method to get the list of weeks on which the work slot is applicable
	 * @return list of weeks for the work slot
	 */

	public boolean[] getWorkWeek() {

		return workWeek;

	}

	/**
	 * Accessor method to get the day of the week for the work slot
	 * @return day of the week for the slot
	 */

	public DayOfWeek getDay() {

		return day;

	}

	/**
	 * Accessor method for retrieving the venue of the works associated with the work slot
	 * @return venue of the work slot
	 */

	public String getVenue() {

		return venue;

	}

}
