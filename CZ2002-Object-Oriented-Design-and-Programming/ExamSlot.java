package STARS;

import java.time.*;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Used to represent an exam slot for a course
 * @author Group 3
 */

public class ExamSlot extends TimeSlot {


	/**
	 * date - date of the exam
	 * venue - venue of the exam
	 */

	private LocalDate date;
	private String venue;

	/**
	 * Constructor for the class.
	 * @param time - time of the exam (attributed inherited from parent class - TimeSlot)
	 * @param date - date of the exam
	 * @param venue - venue of the exam
	 */

	public ExamSlot(LocalTime[] time, LocalDate date, String venue) {
		super(time, Type.EXAM);
		this.date = date;
		this.venue = venue;
	}

	/**
	 * Method to print the information about the examination - its date, time and venue.
	 * @return formatted string containing this information 
	 */

	public String printInfo() {

		String info = "";
		info += "Exam date information -\n";
		info += date.toString() + "\t" + this.getTime()[0] + " - " + this.getTime()[1] + "\n";
		info += "Venue - " + venue + "\n";
		return info;

	}

	/**
	 * Method to check for clashes with another exam slot.
	 * @param es - another exam slot to check for clashes against
	 * @return boolean value indicating if there's a clash with the other exam
	 */

	public boolean isClashing(ExamSlot es) {

		if (es==null) {
			return false;
		}
		if (date.equals(es.getDate())) {	// If dates are the same
			return super.isClashing(es);
		}
		return false;

	}

	/**
	 * Accessor method to retrieve the date of the exam.
	 * @return date of the exam
	 */

	public LocalDate getDate() {

		return date;

	}

	/**
	 * Accessor method to retrieve the venue of the exam.
	 * @return venue of the exam
	 */

	public String getVenue() {

		return venue;
		
	}


}