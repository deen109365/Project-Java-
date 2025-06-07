package project;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages health professionals and their diaries, providing scheduling
 * functionality with resource support.
 */
public class Scheduler {
	private Map<HealthProfessional, Diary> diaries;
	private UndoManager undoManager;
	private List<Resource> sharedResources;
	private long lastSearchDuration;

	/**
	 * Constructs a new Scheduler.
	 */
	public Scheduler() {
		this.diaries = new HashMap<>();
		this.undoManager = new UndoManager(this);
		this.sharedResources = new ArrayList<>();
		
		  // Predefined resources
	    addSharedResource(new Resource("Operating Theatre 1", "Operating Theatre", "Main Hospital"));
	    addSharedResource(new Resource("Operating Theatre 2", "Operating Theatre", "Main Hospital"));
	    addSharedResource(new Resource("MRI Scanner 1", "MRI Scanner", "Radiology Department"));
	    addSharedResource(new Resource("MRI Scanner 2", "MRI Scanner", "Radiology Department"));
	    addSharedResource(new Resource("X-Ray Machine", "X-Ray", "Radiology Department"));
	}

	/**
	 * Adds a health professional to the scheduler.
	 * 
	 * @param professional the professional to add
	 */
	public void addHealthProfessional(HealthProfessional professional) {
		if (!diaries.containsKey(professional)) {
			diaries.put(professional, new Diary(professional));
			undoManager.saveState();
		}
	}

	/**
	 * Removes a health professional from the scheduler.
	 * 
	 * @param professional the professional to remove
	 */
	public void removeHealthProfessional(HealthProfessional professional) {
		if (diaries.remove(professional) != null) {
			undoManager.saveState();
		}
	}

	/**
	 * Gets the diary for a specific health professional.
	 * 
	 * @param professional the professional whose diary to get
	 * @return the diary, or null if not found
	 */
	public Diary getDiary(HealthProfessional professional) {
		return diaries.get(professional);
	}

	/**
	 * Adds a shared resource to the scheduler.
	 * 
	 * @param resource the resource to add
	 */
	public void addSharedResource(Resource resource) {
		sharedResources.add(resource);
		undoManager.saveState();
	}

	/**
	 * Gets all shared resources.
	 * 
	 * @return list of shared resources
	 */
	public List<Resource> getAllSharedResources() {
		return new ArrayList<>(sharedResources);
	}

	/**
	 * Finds available time slots that work for all specified professionals and
	 * resources.
	 * 
	 * @param professionals   the list of professionals who need to attend
	 * @param resources       the list of resources that need to be booked
	 * @param startDate       the first date to consider
	 * @param endDate         the last date to consider
	 * @param durationMinutes the duration needed for the appointment in minutes
	 * @return list of available time slots
	 */
	public List<TimeSlot> findAvailableSlots(List<HealthProfessional> professionals, List<Resource> resources,
			LocalDate startDate, LocalDate endDate, int durationMinutes) {
		long startTime = System.currentTimeMillis();
		List<TimeSlot> availableSlots = new ArrayList<>();

		// Check each day in the range
		for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
			// Standard working hours (9am-5pm)
			LocalTime startTimeOfDay = LocalTime.of(9, 0);
			LocalTime endTimeOfDay = LocalTime.of(17, 0);

			// Check every 30 minutes within working hours
			for (LocalTime slotStart = startTimeOfDay; slotStart.isBefore(endTimeOfDay)
					&& slotStart.plusMinutes(durationMinutes)
							.isBefore(endTimeOfDay.plusMinutes(1)); slotStart = slotStart.plusMinutes(30)) {

				LocalTime slotEnd = slotStart.plusMinutes(durationMinutes);
				boolean allAvailable = true;

				// Check professional availability
				for (HealthProfessional hp : professionals) {
					Diary diary = diaries.get(hp);
					if (diary == null || !diary.isSlotAvailable(date, slotStart, slotEnd, null)) {
						allAvailable = false;
						break;
					}
				}

				// Check resource availability if professionals are available
				if (allAvailable && resources != null) {
					for (Resource res : resources) {
						boolean resourceAvailable = true;
						for (Diary diary : diaries.values()) {
							if (!diary.isSlotAvailable(date, slotStart, slotEnd, res)) {
								resourceAvailable = false;
								break;
							}
						}
						if (!resourceAvailable) {
							allAvailable = false;
							break;
						}
					}
				}

				if (allAvailable) {
					availableSlots.add(new TimeSlot(date, slotStart, slotEnd));
				}
			}
		}

		lastSearchDuration = System.currentTimeMillis() - startTime;
		return availableSlots;
	}

	/**
	 * Books an appointment for multiple professionals.
	 * 
	 * @param professionals the professionals to book for
	 * @param appointment   the appointment details
	 * @return true if successfully booked, false if there were conflicts
	 */
	public boolean bookAppointment(List<HealthProfessional> professionals, Appointment appointment) {
		// First check if all are available
		for (HealthProfessional hp : professionals) {
			Diary diary = diaries.get(hp);
			if (diary == null || !diary.isSlotAvailable(appointment.getDate(), appointment.getStartTime(),
					appointment.getEndTime(), appointment.getResource())) {
				return false;
			}
		}

		// Save state for undo
		undoManager.saveState();

		// If all available, book for each
		for (HealthProfessional hp : professionals) {
			diaries.get(hp).addAppointment(appointment);
		}

		return true;
	}

	/**
	 * Books a recurring appointment for multiple professionals.
	 * 
	 * @param professionals  the professionals to book for
	 * @param appointment    the appointment details
	 * @param recurrenceDays the number of days between each occurrence
	 * @param occurrences    the number of times to repeat the appointment
	 * @return true if successfully booked, false if there were conflicts
	 */
	public boolean bookRecurringAppointment(List<HealthProfessional> professionals, Appointment appointment,
			int recurrenceDays, int occurrences) {
		// Save state for undo
		undoManager.saveState();

		for (HealthProfessional hp : professionals) {
			Diary diary = diaries.get(hp);
			if (!diary.addRecurringAppointment(appointment, recurrenceDays, occurrences)) {
				// Rollback previous diaries
				for (HealthProfessional prevHp : professionals) {
					if (prevHp.equals(hp))
						break; // Stop when we reach current
					diaries.get(prevHp).rollbackRecurringAppointments(appointment, recurrenceDays, occurrences);
				}
				return false;
			}
		}
		return true;
	}

	/**
	 * Gets the duration of the last slot search operation.
	 * 
	 * @return search duration in milliseconds
	 */
	public long getLastSearchDuration() {
		return lastSearchDuration;
	}

	/**
	 * Undoes the last operation.
	 * 
	 * @return true if undo was successful, false if nothing to undo
	 */
	public boolean undo() {
		return undoManager.undo();
	}

	/**
	 * Gets all health professionals in the scheduler.
	 * 
	 * @return list of health professionals
	 */
	public List<HealthProfessional> getAllHealthProfessionals() {
		return new ArrayList<>(diaries.keySet());
	}

	/**
	 * Creates a memento of the current state for undo operations.
	 * 
	 * @return the memento object
	 */
	public SchedulerMemento createMemento() {
		return new SchedulerMemento(new HashMap<>(diaries));
	}

	/**
	 * Restores the scheduler state from a memento.
	 * 
	 * @param memento the memento to restore from
	 */
	public void restoreFromMemento(SchedulerMemento memento) {
		this.diaries = new HashMap<>(memento.getState());
	}
}