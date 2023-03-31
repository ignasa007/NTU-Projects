package STARS;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Used for making a timetable for a lesson
 * @author Group 3
 */

public class TimeTable implements Serializable {


	/**
	 * slots - list of work slots in the timetable
	 * exam - slot for exam, if the course has one
	 */
	
	private ArrayList<WorkSlot> slots;
	private ExamSlot exam;

	/**
	 * Constructor method for the class
	 */

	public TimeTable() {

		slots = new ArrayList<WorkSlot>();
		exam = null;

	}

	/**
	 * Accessor method to retrieve the work slots
	 * @return list of work slots in the timetable
	 */
	
	public ArrayList<WorkSlot> getSlots() {

		return slots;

	}

	/**
	 * Method to add a workslot in the timetable
	 * @param t - the workslot to be added
	 */

	public void add(WorkSlot t) {

		slots.add(t);

	}

	/**
	 * Method to remove a workslot from the timetable
	 * @param t - workslot to be removed
	 */

	public void remove(WorkSlot t) {

		slots.remove(t);

	}

	/**
	 * Method to check if there are any clashes between the workslots in a timetable
	 * @param t - timetable to check for clashes in
	 * @return a pair of workslots clashing with each other
	 */

	public TimeSlot [] isClashing(TimeTable t) {

		int i, j;
		for (i=0; i<slots.size(); i++) {
			for (j=0; j<t.getSlots().size(); j++) {
				if (slots.get(i).isClashing(t.getSlots().get(j))) {
					WorkSlot[] result = new WorkSlot[2];
					result[0] = slots.get(i);
					result[1] = t.getSlots().get(i);
					return result;
				}
			}
		}
		if (t.getExam()!=null) {
			if (exam.isClashing(t.getExam())) {
				ExamSlot[] result = new ExamSlot[2];
				result[0] = exam;
				result[1] = t.getExam();
				return result;
			}
		}
		return null;

	}

	/**
	 * Accessor method to retrieve the exam slot from a timetable
	 * @return exam slot
	 */

	public ExamSlot getExam() {

		return exam;

	}

	/**
	 * Mutator method to set the exam slot in a timetable
	 * @param e - exam slot to set
	 */

	public void setExam(ExamSlot e) {

		this.exam = e;

	}
	

}
