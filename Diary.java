package project;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages appointments and tasks for a single health professional, including
 * resource bookings.
 */
public class Diary {
	private HealthProfessional owner;
	private List<Appointment> appointments;
	private List<Task> tasks;
	private List<Resource> resources;

	/**
	 * Constructs a new Diary for a health professional.
	 * 
	 * @param owner the health professional who owns this diary
	 */
	public Diary(HealthProfessional owner) {
		this.owner = owner;
		this.appointments = new ArrayList<>();
		this.tasks = new ArrayList<>();
		this.resources = new ArrayList<>();
	}

	/**
	 * Adds an appointment to the diary.
	 * 
	 * @param appointment the appointment to add
	 * @return true if added successfully, false if there was a conflict
	 */
	public boolean addAppointment(Appointment appointment) {
		if (!isSlotAvailable(appointment.getDate(), appointment.getStartTime(), appointment.getEndTime(),
				appointment.getResource())) {
			return false;
		}
		appointments.add(appointment);
		return true;
	}

	/**
	 * Adds a recurring appointment to the diary.
	 * 
	 * @param appointment    the base appointment to add
	 * @param recurrenceDays the number of days between each occurrence
	 * @param occurrences    the number of times to repeat the appointment
	 * @return true if added successfully, false if there was a conflict
	 */
	public boolean addRecurringAppointment(Appointment appointment, int recurrenceDays, int occurrences) {
		List<Appointment> appointmentsToAdd = new ArrayList<>();

		// First check all appointments can be added
		for (int i = 0; i < occurrences; i++) {
			LocalDate newDate = appointment.getDate().plusDays(i * recurrenceDays);
			Appointment newAppointment = new Appointment(newDate, appointment.getStartTime(), appointment.getEndTime(),
					appointment.getTreatmentType(), appointment.getPatientName(), true, appointment.getResource());

			if (!isSlotAvailable(newDate, newAppointment.getStartTime(), newAppointment.getEndTime(),
					newAppointment.getResource())) {
				return false;
			}
			appointmentsToAdd.add(newAppointment);
		}

		// If all are available, add them
		appointments.addAll(appointmentsToAdd);
		return true;
	}

	/**
	 * Removes recurring appointments from the diary (for rollback).
	 * 
	 * @param baseAppointment the base appointment to remove
	 * @param recurrenceDays  the recurrence interval
	 * @param occurrences     the number of occurrences
	 */
	public void rollbackRecurringAppointments(Appointment baseAppointment, int recurrenceDays, int occurrences) {
		List<Appointment> toRemove = new ArrayList<>();
		for (int i = 0; i < occurrences; i++) {
			LocalDate newDate = baseAppointment.getDate().plusDays(i * recurrenceDays);
			for (Appointment appt : appointments) {
				if (appt.getDate().equals(newDate) && appt.getStartTime().equals(baseAppointment.getStartTime())
						&& appt.getEndTime().equals(baseAppointment.getEndTime())
						&& appt.getPatientName().equals(baseAppointment.getPatientName())) {
					toRemove.add(appt);
				}
			}
		}
		appointments.removeAll(toRemove);
	}

	/**
	 * Removes an appointment from the diary.
	 * 
	 * @param appointment the appointment to remove
	 * @return true if removed successfully, false if not found
	 */
	public boolean removeAppointment(Appointment appointment) {
		return appointments.remove(appointment);
	}

	/**
	 * Gets all appointments for a specific date.
	 * 
	 * @param date the date to filter by
	 * @return list of appointments on that date
	 */
	public List<Appointment> getAppointmentsOnDate(LocalDate date) {
		List<Appointment> result = new ArrayList<>();
		for (Appointment appt : appointments) {
			if (appt.getDate().equals(date)) {
				result.add(appt);
			}
		}
		return result;
	}

	/**
	 * Checks if a time slot is available considering resource conflicts.
	 * 
	 * @param date      the date to check
	 * @param startTime the proposed start time
	 * @param endTime   the proposed end time
	 * @param resource  the resource to check for conflicts
	 * @return true if the slot is available, false if booked
	 */
	public boolean isSlotAvailable(LocalDate date, LocalTime startTime, LocalTime endTime, Resource resource) {
		Appointment temp = new Appointment(date, startTime, endTime, "TEMP", "TEMP", false, resource);
		for (Appointment existing : appointments) {
			if (existing.overlapsWith(temp)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Adds a task to the diary.
	 * 
	 * @param task the task to add
	 */
	public void addTask(Task task) {
		tasks.add(task);
	}

	/**
	 * Gets all tasks in this diary.
	 * 
	 * @return list of tasks
	 */
	public List<Task> getAllTasks() {
		return new ArrayList<>(tasks);
	}

	/**
	 * Gets all appointments in this diary.
	 * 
	 * @return list of appointments
	 */
	public List<Appointment> getAllAppointments() {
		return new ArrayList<>(appointments);
	}

	/**
	 * Adds a resource to the diary.
	 * 
	 * @param resource the resource to add
	 */
	public void addResource(Resource resource) {
		resources.add(resource);
	}

	/**
	 * Gets all resources in this diary.
	 * 
	 * @return list of resources
	 */
	public List<Resource> getAllResources() {
		return new ArrayList<>(resources);
	}

	/**
	 * Gets the owner of this diary.
	 * 
	 * @return the health professional who owns this diary
	 */
	public HealthProfessional getOwner() {
		return owner;
	}
}