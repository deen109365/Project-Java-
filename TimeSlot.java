package project;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents an available time slot for scheduling.
 */
public class TimeSlot {
	private LocalDate date;
	private LocalTime startTime;
	private LocalTime endTime;

	/**
	 * Constructs a new TimeSlot.
	 * 
	 * @param date      the date of the slot
	 * @param startTime the starting time
	 * @param endTime   the ending time
	 */
	public TimeSlot(LocalDate date, LocalTime startTime, LocalTime endTime) {
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	/**
	 * Returns the date of the scheduled operation.
	 *
	 * @return the date as a {@link java.time.LocalDate} object
	 */
	public LocalDate getDate() {
	    return date;
	}

	/**
	 * Returns the start time of the scheduled operation.
	 *
	 * @return the start time as a {@link java.time.LocalTime} object
	 */
	public LocalTime getStartTime() {
	    return startTime;
	}

	/**
	 * Returns the end time of the scheduled operation.
	 *
	 * @return the end time as a {@link java.time.LocalTime} object
	 */
	public LocalTime getEndTime() {
	    return endTime;
	}


	@Override
	public String toString() {
		return date + " from " + startTime + " to " + endTime;
	}
}