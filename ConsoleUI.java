package project;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Provides a text-based user interface for the Operation Scheduler with
 * resource management and calendar view functionality. This class handles all
 * user interactions and displays information from the Scheduler system.
 */
public class ConsoleUI {
	/** The scheduler instance this UI interacts with */
	private Scheduler scheduler;

	/** Scanner for reading user input */
	private Scanner scanner;

	/**
	 * Constructs a new ConsoleUI for a scheduler.
	 * 
	 * @param scheduler the scheduler to interact with
	 */
	public ConsoleUI(Scheduler scheduler) {
		this.scheduler = scheduler;
		this.scanner = new Scanner(System.in);
	}

	/**
	 * Starts the main menu loop and handles user input. Continues running until the
	 * user chooses to exit.
	 */
	public void start() {
		boolean running = true;
		while (running) {
			displayMainMenu();
			int choice = getIntInput(1, 14);

			switch (choice) {
			case 1:
				addHealthProfessional();
				break;
			case 2:
				listHealthProfessionals();
				break;
			case 3:
				addAppointment();
				break;
			case 4:
				addRecurringAppointment();
				break;
			case 5:
				addTask();
				break;
			case 6:
				listAppointmentsForProfessional();
				break;
			case 7:
				listTasksForProfessional();
				break;
			case 8:
				manageResources();
				break;
			case 9:
				searchAvailableSlots();
				break;
			case 10:
				showCalendarView();
				break;
			case 11:
				saveData();
				break;
			case 12:
				loadData();
				break;
			case 13:
				undoLastOperation();
				break;
			case 14:
				running = false;
				System.out.println("Exiting...");
				break;
			}
		}
	}

	/**
	 * Displays the main menu options to the user.
	 */
	private void displayMainMenu() {
		System.out.println("=== Operation Scheduler ===");
		System.out.println("1. Add Health Professional");
		System.out.println("2. List Health Professionals");
		System.out.println("3. Add Appointment");
		System.out.println("4. Add Recurring Appointment");
		System.out.println("5. Add Task");
		System.out.println("6. List Appointments for Professional");
		System.out.println("7. List Tasks for Professional");
		System.out.println("8. Manage Resources");
		System.out.println("9. Search Available Slots");
		System.out.println("10. Calendar View");
		System.out.println("11. Save Data");
		System.out.println("12. Load Data");
		System.out.println("13. Undo Last Operation");
		System.out.println("14. Exit");
		System.out.print("Choose an option: ");
	}

	/**
	 * Adds a new health professional to the system. Prompts user for name,
	 * profession, and office location.
	 */
	private void addHealthProfessional() {
		System.out.println("--- Add Health Professional ---");
		System.out.print("Name: ");
		String name = scanner.nextLine();
		System.out.print("Profession: ");
		String profession = scanner.nextLine();
		System.out.print("Office Location: ");
		String office = scanner.nextLine();

		HealthProfessional hp = new HealthProfessional(name, profession, office);
		scheduler.addHealthProfessional(hp);
		System.out.println("Added: " + hp);
	}

	/**
	 * Lists all health professionals in the system.
	 */
	private void listHealthProfessionals() {
		System.out.println("--- Health Professionals ---");
		List<HealthProfessional> hps = scheduler.getAllHealthProfessionals();
		if (hps.isEmpty()) {
			System.out.println("No health professionals registered.");
		} else {
			for (int i = 0; i < hps.size(); i++) {
				System.out.println((i + 1) + ". " + hps.get(i));
			}
		}
	}

	/**
	 * Adds a new appointment to the system. Handles selection of professionals,
	 * resources, and appointment details.
	 */
	private void addAppointment() {
		System.out.println("\n--- Add Appointment ---");
		List<HealthProfessional> hps = scheduler.getAllHealthProfessionals();
		if (hps.isEmpty()) {
			System.out.println("No health professionals available. Please add some first.");
			return;
		}

		// Select professionals
		List<HealthProfessional> selected = selectProfessionals(hps);
		if (selected.isEmpty()) {
			System.out.println("No professionals selected.");
			return;
		}

		// Select resources
		List<Resource> selectedResources = selectResources();

		// Get appointment details
		LocalDate date = getDateInput("Date (YYYY-MM-DD): ");
		LocalTime startTime = getTimeInput("Start time (HH:MM): ");
		LocalTime endTime = getTimeInput("End time (HH:MM): ");
		System.out.print("Treatment type: ");
		String treatment = scanner.nextLine();
		System.out.print("Patient name: ");
		String patient = scanner.nextLine();

		// Create appointment with resource
		Resource resource = selectedResources.isEmpty() ? null : selectedResources.get(0);
		Appointment appt = new Appointment(date, startTime, endTime, treatment, patient, false, resource);

		if (scheduler.bookAppointment(selected, appt)) {
			System.out.println("Appointment booked successfully.");
		} else {
			System.out.println("Failed to book appointment - time slot not available for all professionals.");
		}
	}

	/**
	 * Adds a recurring appointment to the system. Similar to addAppointment but
	 * with recurrence parameters.
	 */
	private void addRecurringAppointment() {
		System.out.println("\n--- Add Recurring Appointment ---");
		List<HealthProfessional> hps = scheduler.getAllHealthProfessionals();
		if (hps.isEmpty()) {
			System.out.println("No health professionals available. Please add some first.");
			return;
		}

		// Select professionals
		List<HealthProfessional> selected = selectProfessionals(hps);
		if (selected.isEmpty()) {
			System.out.println("No professionals selected.");
			return;
		}

		// Select resources
		List<Resource> selectedResources = selectResources();

		// Get appointment details
		LocalDate date = getDateInput("Date (YYYY-MM-DD): ");
		LocalTime startTime = getTimeInput("Start time (HH:MM): ");
		LocalTime endTime = getTimeInput("End time (HH:MM): ");
		System.out.print("Treatment type: ");
		String treatment = scanner.nextLine();
		System.out.print("Patient name: ");
		String patient = scanner.nextLine();
		System.out.print("Recurrence days: ");
		int recurrenceDays = getIntInput(1, 30);
		System.out.print("Number of occurrences: ");
		int occurrences = getIntInput(1, 100);

		// Create appointment with resource
		Resource resource = selectedResources.isEmpty() ? null : selectedResources.get(0);
		Appointment appt = new Appointment(date, startTime, endTime, treatment, patient, true, resource);

		if (scheduler.bookRecurringAppointment(selected, appt, recurrenceDays, occurrences)) {
			System.out.println("Recurring appointment booked successfully.");
		} else {
			System.out.println("Failed to book recurring appointment - time slot not available for all professionals.");
		}
	}

	/**
	 * Adds a new task for a health professional.
	 */
	private void addTask() {
		System.out.println("\n--- Add Task ---");
		List<HealthProfessional> hps = scheduler.getAllHealthProfessionals();
		if (hps.isEmpty()) {
			System.out.println("No health professionals available. Please add some first.");
			return;
		}

		// Select professional
		HealthProfessional hp = selectSingleProfessional(hps);

		// Get task details
		System.out.print("Task description: ");
		String description = scanner.nextLine();
		System.out.print("Task priority (High, Medium, Low): ");
		String priority = scanner.nextLine();

		Task task = new Task(description, priority);
		scheduler.getDiary(hp).addTask(task);
		System.out.println("Task added successfully.");
	}

	/**
	 * Lists appointments for a selected health professional.
	 */
	private void listAppointmentsForProfessional() {
		System.out.println("\n--- Appointments for Professional ---");
		List<HealthProfessional> hps = scheduler.getAllHealthProfessionals();
		if (hps.isEmpty()) {
			System.out.println("No health professionals available.");
			return;
		}

		HealthProfessional hp = selectSingleProfessional(hps);
		Diary diary = scheduler.getDiary(hp);
		List<Appointment> appointments = diary.getAllAppointments();

		System.out.println("\nAppointments for " + hp.getName() + ":");
		if (appointments.isEmpty()) {
			System.out.println("No appointments scheduled.");
		} else {
			for (Appointment appt : appointments) {
				System.out.println("- " + appt);
			}
		}
	}

	/**
	 * Lists tasks for a selected health professional.
	 */
	private void listTasksForProfessional() {
		System.out.println("\n--- Tasks for Professional ---");
		List<HealthProfessional> hps = scheduler.getAllHealthProfessionals();
		if (hps.isEmpty()) {
			System.out.println("No health professionals available.");
			return;
		}

		HealthProfessional hp = selectSingleProfessional(hps);
		Diary diary = scheduler.getDiary(hp);
		List<Task> tasks = diary.getAllTasks();

		System.out.println("\nTasks for " + hp.getName() + ":");
		if (tasks.isEmpty()) {
			System.out.println("No tasks scheduled.");
		} else {
			for (Task task : tasks) {
				System.out.println("- " + task);
			}
		}
	}

	/**
	 * Manages resource operations (add/list).
	 */
	private void manageResources() {
		System.out.println("\n--- Resource Management ---");
		System.out.println("1. Add Resource");
		System.out.println("2. List Resources");
		System.out.print("Choose an option: ");

		int choice = getIntInput(1, 2);
		switch (choice) {
		case 1:
			addResource();
			break;
		case 2:
			listResources();
			break;
		}
	}

	/**
	 * Adds a new resource to the system.
	 */
	private void addResource() {
		System.out.print("\nResource Name: ");
		String name = scanner.nextLine();
		System.out.print("Resource Type: ");
		String type = scanner.nextLine();
		System.out.print("Location: ");
		String location = scanner.nextLine();

		Resource resource = new Resource(name, type, location);
		scheduler.addSharedResource(resource);
		System.out.println("Added resource: " + resource);
	}

	/**
	 * Lists all resources in the system.
	 */
	private void listResources() {
		List<Resource> resources = scheduler.getAllSharedResources();
		if (resources.isEmpty()) {
			System.out.println("No resources available.");
		} else {
			System.out.println("\n=== Resources ===");
			for (Resource res : resources) {
				System.out.println("- " + res);
			}
		}
	}

	/**
	 * Searches for available time slots based on criteria.
	 */
	private void searchAvailableSlots() {
		System.out.println("\n--- Search Available Slots ---");
		List<HealthProfessional> hps = scheduler.getAllHealthProfessionals();
		if (hps.isEmpty()) {
			System.out.println("No health professionals available. Please add some first.");
			return;
		}

		// Select professionals
		List<HealthProfessional> selectedProfessionals = selectProfessionals(hps);
		if (selectedProfessionals.isEmpty()) {
			System.out.println("No professionals selected.");
			return;
		}

		// Select resources
		List<Resource> selectedResources = selectResources();

		// Get date range
		LocalDate startDate = getDateInput("Start date (YYYY-MM-DD): ");
		LocalDate endDate = getDateInput("End date (YYYY-MM-DD): ");
		System.out.print("Duration in minutes: ");
		int duration = getIntInput(1, 480);

		// Search for available slots
		List<TimeSlot> availableSlots = scheduler.findAvailableSlots(selectedProfessionals, selectedResources,
				startDate, endDate, duration);

		System.out.println("\n=== Available Slots ===");
		System.out.println("Search took: " + scheduler.getLastSearchDuration() + "ms");
		if (availableSlots.isEmpty()) {
			System.out.println("No available slots found.");
		} else {
			for (int i = 0; i < availableSlots.size(); i++) {
				System.out.println((i + 1) + ". " + availableSlots.get(i));
			}
		}
	}

	/**
	 * Displays a calendar view for a specific month and year.
	 */
	private void showCalendarView() {
		System.out.println("\n--- Calendar View ---");
		System.out.print("Enter year (YYYY): ");
		int year = getIntInput(1900, 2100);
		System.out.print("Enter month (1-12): ");
		int month = getIntInput(1, 12);

		YearMonth yearMonth = YearMonth.of(year, month);
		LocalDate first = yearMonth.atDay(1);
		int startOffset = first.getDayOfWeek().getValue() % 7; // Sunday = 0

		// Print calendar header
		System.out.println("\n   " + yearMonth.getMonth() + " " + year);
		System.out.println("Su Mo Tu We Th Fr Sa");

		// Print leading spaces
		for (int i = 0; i < startOffset; i++) {
			System.out.print("   ");
		}

		// Print days
		for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
			LocalDate date = LocalDate.of(year, month, day);
			System.out.printf("%2d", day);

			// Highlight today
			if (date.equals(LocalDate.now())) {
				System.out.print("*");
			} else {
				System.out.print(" ");
			}

			// New line at end of week
			if ((day + startOffset) % 7 == 0) {
				System.out.println();
			}
		}
		System.out.println("\n* = Today");

		// Show appointments for a specific date
		System.out.print("\nEnter a date to view appointments (YYYY-MM-DD) or 0 to return: ");
		String input = scanner.nextLine();
		if (!input.equals("0")) {
			try {
				LocalDate date = LocalDate.parse(input);
				showAppointmentsOnDate(date);
			} catch (DateTimeParseException e) {
				System.out.println("Invalid date format.");
			}
		}
	}

	/**
	 * Shows appointments on a specific date.
	 * 
	 * @param date the date to show appointments for
	 */
	private void showAppointmentsOnDate(LocalDate date) {
		System.out.println("\nAppointments on " + date + ":");
		boolean found = false;

		for (HealthProfessional hp : scheduler.getAllHealthProfessionals()) {
			List<Appointment> appointments = scheduler.getDiary(hp).getAppointmentsOnDate(date);
			if (!appointments.isEmpty()) {
				found = true;
				System.out.println("\n" + hp.getName() + ":");
				for (Appointment appt : appointments) {
					String resourceInfo = appt.getResource() != null
							? " [Resource: " + appt.getResource().getName() + "]"
							: "";
					System.out.println(" - " + appt.getStartTime() + " to " + appt.getEndTime() + ": "
							+ appt.getPatientName() + " (" + appt.getTreatmentType() + ")" + resourceInfo);
				}
			}
		}

		if (!found) {
			System.out.println("No appointments scheduled for this date.");
		}
	}

	/**
	 * Saves scheduler data to a file.
	 */
	private void saveData() {
		System.out.print("\nEnter filename to save to: ");
		String filename = scanner.nextLine();
		try {
			FileManager.saveToFile(scheduler, filename);
			System.out.println("Data saved successfully.");
		} catch (IOException e) {
			System.out.println("Error saving data: " + e.getMessage());
		}
	}

	/**
	 * Loads scheduler data from a file.
	 */
	private void loadData() {
		System.out.print("\nEnter filename to load from: ");
		String filename = scanner.nextLine();
		try {
			Scheduler newScheduler = FileManager.loadFromFile(filename);
			this.scheduler = newScheduler;
			System.out.println("Data loaded successfully.");
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Error loading data: " + e.getMessage());
		}
	}

	/**
	 * Undoes the last operation performed on the scheduler.
	 */
	private void undoLastOperation() {
		if (scheduler.undo()) {
			System.out.println("Undo successful.");
		} else {
			System.out.println("Nothing to undo.");
		}
	}

	// Helper methods

	/**
	 * Gets integer input from user within specified range.
	 * 
	 * @param min minimum allowed value
	 * @param max maximum allowed value
	 * @return valid user input
	 */
	private int getIntInput(int min, int max) {
		while (true) {
			try {
				int input = Integer.parseInt(scanner.nextLine());
				if (input >= min && input <= max) {
					return input;
				}
				System.out.print("Please enter a number between " + min + " and " + max + ": ");
			} catch (NumberFormatException e) {
				System.out.print("Invalid input. Please enter a number: ");
			}
		}
	}

	/**
	 * Gets date input from user in YYYY-MM-DD format.
	 * 
	 * @param prompt the prompt to display to user
	 * @return parsed LocalDate
	 */
	private LocalDate getDateInput(String prompt) {
		while (true) {
			System.out.print(prompt);
			try {
				return LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE);
			} catch (DateTimeParseException e) {
				System.out.println("Invalid date format. Please use YYYY-MM-DD.");
			}
		}
	}

	/**
	 * Gets time input from user in HH:MM format.
	 * 
	 * @param prompt the prompt to display to user
	 * @return parsed LocalTime
	 */
	private LocalTime getTimeInput(String prompt) {
		while (true) {
			System.out.print(prompt);
			try {
				return LocalTime.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_TIME);
			} catch (DateTimeParseException e) {
				System.out.println("Invalid time format. Please use HH:MM.");
			}
		}
	}

	/**
	 * Selects multiple health professionals from a list.
	 * 
	 * @param all list of all available professionals
	 * @return list of selected professionals
	 */
	private List<HealthProfessional> selectProfessionals(List<HealthProfessional> all) {
		System.out.println("Select professionals (enter numbers, comma separated):");
		listHealthProfessionals();
		System.out.print("Your selection: ");
		return selectMultiple(all);
	}

	/**
	 * Selects a single health professional from a list.
	 * 
	 * @param hps list of health professionals
	 * @return selected professional
	 */
	private HealthProfessional selectSingleProfessional(List<HealthProfessional> hps) {
		System.out.println("Select professional:");
		listHealthProfessionals();
		System.out.print("Your selection: ");
		int choice = getIntInput(1, hps.size()) - 1;
		return hps.get(choice);
	}

	/**
	 * Selects multiple items from a list using comma-separated indices.
	 * 
	 * @param all list of all available items
	 * @param <T> type of items in list
	 * @return list of selected items
	 */
	private <T> List<T> selectMultiple(List<T> all) {
		List<T> selected = new ArrayList<>();
		String input = scanner.nextLine();
		String[] parts = input.split(",");

		for (String part : parts) {
			try {
				int index = Integer.parseInt(part.trim()) - 1;
				if (index >= 0 && index < all.size()) {
					selected.add(all.get(index));
				}
			} catch (NumberFormatException e) {
				// Ignore invalid entries
			}
		}

		return selected;
	}

	/**
	 * Selects resources from available resources.
	 * 
	 * @return list of selected resources
	 */
	private List<Resource> selectResources() {
		List<Resource> allResources = scheduler.getAllSharedResources();
		List<Resource> selectedResources = new ArrayList<>();
		if (!allResources.isEmpty()) {
			System.out.println("Select resources (comma separated, 0 for none):");
			for (int i = 0; i < allResources.size(); i++) {
				System.out.println((i + 1) + ". " + allResources.get(i));
			}
			System.out.print("Your selection: ");
			selectedResources = selectMultipleResources(allResources);
		}
		return selectedResources;
	}

	/**
	 * Selects multiple resources from a list.
	 * 
	 * @param all list of all available resources
	 * @return list of selected resources
	 */
	private List<Resource> selectMultipleResources(List<Resource> all) {
		List<Resource> selected = new ArrayList<>();
		String input = scanner.nextLine();
		if (input.trim().equals("0"))
			return selected;

		String[] parts = input.split(",");
		for (String part : parts) {
			try {
				int index = Integer.parseInt(part.trim()) - 1;
				if (index >= 0 && index < all.size()) {
					selected.add(all.get(index));
				}
			} catch (NumberFormatException e) {
				// Ignore invalid entries
			}
		}
		return selected;
	}
}