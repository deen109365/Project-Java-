package project;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles saving and loading scheduler data to/from files with resource
 * support.
 */
public class FileManager {
	/**
	 * Saves the scheduler data to a file.
	 * 
	 * @param scheduler the scheduler to save
	 * @param filename  the file to save to
	 * @throws IOException if there's an error writing to the file
	 */
	public static void saveToFile(Scheduler scheduler, String filename) throws IOException {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
			
			// Convert to serializable data structure
			List<SerializableHealthProfessional> shps = new ArrayList<>();
			for (HealthProfessional hp : scheduler.getAllHealthProfessionals()) {
				Diary diary = scheduler.getDiary(hp);
				shps.add(new SerializableHealthProfessional(hp, diary));
			}

			// Save shared resources
			List<SerializableResource> sResources = new ArrayList<>();
			for (Resource res : scheduler.getAllSharedResources()) {
				sResources.add(new SerializableResource(res));
			}

			// Create container for all data
			SerializableSchedulerState state = new SerializableSchedulerState(shps, sResources);
			oos.writeObject(state);
		}
	}

	/**
	 * Loads scheduler data from a file.
	 * 
	 * @param filename the file to load from
	 * @return a new Scheduler with the loaded data
	 * @throws IOException            if there's an error reading the file
	 * @throws ClassNotFoundException if the file contains unexpected data
	 */
	public static Scheduler loadFromFile(String filename) throws IOException, ClassNotFoundException {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
			SerializableSchedulerState state = (SerializableSchedulerState) ois.readObject();

			Scheduler scheduler = new Scheduler();

			// Load health professionals and diaries
			for (SerializableHealthProfessional shp : state.healthProfessionals) {
				HealthProfessional hp = new HealthProfessional(shp.name, shp.profession, shp.officeLocation);
				scheduler.addHealthProfessional(hp);

				Diary diary = scheduler.getDiary(hp);
				for (SerializableAppointment sa : shp.appointments) {
					Resource resource = null;
					if (sa.resourceName != null) {
						resource = new Resource(sa.resourceName, sa.resourceType, sa.resourceLocation);
					}
					Appointment appt = new Appointment(LocalDate.parse(sa.date), LocalTime.parse(sa.startTime),
							LocalTime.parse(sa.endTime), sa.treatmentType, sa.patientName, sa.isRecurring, resource);
					diary.addAppointment(appt);
				}
				for (SerializableTask st : shp.tasks) {
					diary.addTask(new Task(st.description, st.priority));
				}
			}

			// Load shared resources
			for (SerializableResource sr : state.sharedResources) {
				scheduler.addSharedResource(new Resource(sr.name, sr.type, sr.location));
			}

			return scheduler;
		}
	}

	// Container for all scheduler state
	private static class SerializableSchedulerState implements Serializable {
	
		private static final long serialVersionUID = 7440301128940466199L;
		List<SerializableHealthProfessional> healthProfessionals;
		List<SerializableResource> sharedResources;

		SerializableSchedulerState(List<SerializableHealthProfessional> healthProfessionals,
				List<SerializableResource> sharedResources) {
			this.healthProfessionals = healthProfessionals;
			this.sharedResources = sharedResources;
		}
	}

	// Helper classes for serialization
	private static class SerializableHealthProfessional implements Serializable {
		
		private static final long serialVersionUID = 8279206694355240699L;
		String name;
		String profession;
		String officeLocation;
		List<SerializableAppointment> appointments;
		List<SerializableTask> tasks;

		SerializableHealthProfessional(HealthProfessional hp, Diary diary) {
			this.name = hp.getName();
			this.profession = hp.getProfession();
			this.officeLocation = hp.getOfficeLocation();
			this.appointments = new ArrayList<>();
			this.tasks = new ArrayList<>();

			for (Appointment appt : diary.getAllAppointments()) {
				appointments.add(new SerializableAppointment(appt));
			}
			for (Task task : diary.getAllTasks()) {
				tasks.add(new SerializableTask(task));
			}
		}
	}

	private static class SerializableAppointment implements Serializable {
		
		private static final long serialVersionUID = 6250099432315907402L;
		String date;
		String startTime;
		String endTime;
		String treatmentType;
		String patientName;
		boolean isRecurring;
		String resourceName;
		String resourceType;
		String resourceLocation;

		SerializableAppointment(Appointment appt) {
			this.date = appt.getDate().toString();
			this.startTime = appt.getStartTime().toString();
			this.endTime = appt.getEndTime().toString();
			this.treatmentType = appt.getTreatmentType();
			this.patientName = appt.getPatientName();
			this.isRecurring = appt.isRecurring();
			if (appt.getResource() != null) {
				this.resourceName = appt.getResource().getName();
				this.resourceType = appt.getResource().getType();
				this.resourceLocation = appt.getResource().getLocation();
			}
		}
	}

	private static class SerializableTask implements Serializable {
		
		private static final long serialVersionUID = 9200435498922861030L;
		String description;
		String priority;

		SerializableTask(Task task) {
			this.description = task.getDescription();
			this.priority = task.getPriority();
		}
	}

	private static class SerializableResource implements Serializable {
		
		private static final long serialVersionUID = 1298739326321821291L;
		String name;
		String type;
		String location;

		SerializableResource(Resource resource) {
			this.name = resource.getName();
			this.type = resource.getType();
			this.location = resource.getLocation();
		}
	}
}