package project;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a saved state of the scheduler for undo functionality.
 */
public class SchedulerMemento {
	private Map<HealthProfessional, Diary> state;

	/**
	 * Constructs a new memento with the current state.
	 * 
	 * @param state the state to save
	 */
	public SchedulerMemento(Map<HealthProfessional, Diary> state) {
		
		this.state = new HashMap<>();
		for (Map.Entry<HealthProfessional, Diary> entry : state.entrySet()) {
			HealthProfessional hp = entry.getKey();
			Diary originalDiary = entry.getValue();

			// Creating  a copy of the health professional
			HealthProfessional hpCopy = new HealthProfessional(hp.getName(), hp.getProfession(),
					hp.getOfficeLocation());

			// Creating  a copy of the diary
			Diary diaryCopy = new Diary(hpCopy);
			for (Appointment appt : originalDiary.getAllAppointments()) {
				diaryCopy.addAppointment(new Appointment(appt.getDate(), appt.getStartTime(), appt.getEndTime(),
						appt.getTreatmentType(), appt.getPatientName(), appt.isRecurring(), appt.getResource()));
			}

			// Copy tasks
			for (Task task : originalDiary.getAllTasks()) {
				diaryCopy.addTask(new Task(task.getDescription(), task.getPriority()));
			}

			this.state.put(hpCopy, diaryCopy);
		}
	}

	/**
	 * Gets the saved state.
	 * 
	 * @return the saved state as a map of health professionals to their diaries
	 */
	public Map<HealthProfessional, Diary> getState() {
		return state;
	}
}