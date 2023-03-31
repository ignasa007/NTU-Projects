package STARS;

import java.time.*;

/**
 * Used for implementing the methods requested in the interface
 * @author Group 3
 */

public class TimeTableMgr {


	/**
	 * N_STUDY_WEEKS - number of study weeks set to 13
	 */

	private static final int N_STUDY_WEEKS = 13;

	/**
	 * Method to create an exam slot
	 * @return created exam slot as required
	 */

	public static ExamSlot createExam() {
		
		String venue = TimeTableInterface.inputVenue();
		System.out.println("Enter date of exam:");
		LocalDate date = TimeTableInterface.inputDate();
		LocalTime[] time = new LocalTime[2];
		while(true) {
			System.out.print("Starting time of exam - ");
			time[0] = TimeTableInterface.inputLocalTime();
			System.out.print("Ending time of exam - ");
			time[1] = TimeTableInterface.inputLocalTime();
			if (time[1].compareTo(time[0])<0) {
				System.out.println("Error: starting time is after ending time. Please try again.");
			}
			else {
				break;
			}
		}
		ExamSlot exam = new ExamSlot(time, date, venue);
		exam.printInfo();
		return exam;

	}

	/**
	 * Method to create a timetable for a new index
	 * @return timetable created for the index
	 */

	public static TimeTable createTimeTable() {

		// Call this function to create a new timetable for a new index
		TimeTable t = new TimeTable();
		do {
			WorkSlot w;
			while(true) {
				w = createWorkSlot();
				TimeTable tester = new TimeTable();
				tester.add(w);
				if (t.isClashing(tester)!=null) {
					System.out.println("Lesson clashes with existing lessons in this timetable. Please try again.");
					tester.remove(w);
				}
				else {
					break;
				}
			}
			t.add(w);
		} while (TimeTableInterface.inputAnother());
		if (TimeTableInterface.haveFinal()) {
			t.setExam(createExam());
		}
		return t;

	}

	/**
	 * Method to check if timetables of two indices clash
	 * @param tt1 - first timetable object
	 * @param tt2 - second timetable object
	 * @return boolean value indicating if the two timetables are clashing
	 */

	public static boolean isClashing(TimeTable tt1, TimeTable tt2) {

		//Check if 2 time tables of 2 different indices clashes. Print out error message if clash
		if (tt1.isClashing(tt2) == null) {
			return false;
		}
		System.out.println("Clash detected");
		TimeSlot[] clash_info = tt1.isClashing(tt2);
		clash_info[0].printInfo();
		clash_info[1].printInfo();
		return true;

	} 

	/**
	 * Method to check if an index's timetable clashes with a student's timetable
	 * @param stt - student's timetable to check clash against
	 * @param tt - index's timetable to check clash for
	 * @return boolean value indicating if there's a clash or not
	 */

	public static boolean isClashing(StudentTimeTable stt, TimeTable tt) {

		return stt.isClashing(tt);

	}

	/**
	 * Method to create access period for a student
	 * @return created access period object for the student
	 */

	public static AccessPeriod createAccessPeriod() {

		LocalDate[] date = new LocalDate[2];
		LocalTime[] time = new LocalTime[2];
		while (true) {
			System.out.print("Enter starting date of access period - ");
			date[0] = TimeTableInterface.inputDate();
			System.out.print("Enter starting time of access period - ");
			time[0] = TimeTableInterface.inputLocalTime();
			System.out.print("Enter ending date of access period - ");
			date[1] = TimeTableInterface.inputDate();
			System.out.print("Enter ending time of access period - ");
			time[1] = TimeTableInterface.inputLocalTime();
			LocalDateTime t_start = LocalDateTime.of(date[0], time[0]);
			LocalDateTime t_end = LocalDateTime.of(date[1], time[1]);
			if (t_end.isBefore(t_start)) {
				System.out.println("Error: starting time is after ending time. Please try again.");
			}
			else {
				break;
			}
		}
		return new AccessPeriod(date, time);

	}

	/**
	 * Method to create a work slot 
	 * @return created workslot
	 */

	public static WorkSlot createWorkSlot() {

		Type tt = TimeTableInterface.inputType();
		DayOfWeek day = TimeTableInterface.inputDayOfWeek();
		LocalTime[] time = new LocalTime[2];
		while(true) {
			System.out.print("Starting time - ");
			time[0] = TimeTableInterface.inputLocalTime();
			System.out.print("Ending time - ");
			time[1] = TimeTableInterface.inputLocalTime();
			if (time[1].compareTo(time[0])<0) {
				System.out.println("Error: starting time is after ending time. Please try again.");
			}
			else {
				break;
			}
		}
		boolean[] workWeek = TimeTableInterface.inputWorkWeeks(N_STUDY_WEEKS);
		String venue = TimeTableInterface.inputVenue();
		WorkSlot slot = new WorkSlot(time, workWeek, day, tt, venue);
		slot.printInfo();
		return slot;

	}


}