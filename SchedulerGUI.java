package project;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Provides a comprehensive graphical user interface for the Operation Scheduler application
 * with calendar view and resource management.
 */
public class SchedulerGUI {
    private Scheduler scheduler;
    private JFrame mainFrame;
    private JTextArea outputArea;
    private JTabbedPane tabbedPane;

    /**
     * Constructs a new SchedulerGUI with the specified scheduler.
     *
     * @param scheduler The scheduler instance to manage operations
     */
    public SchedulerGUI(Scheduler scheduler) {
        this.scheduler = scheduler;
        setLookAndFeel();
        initializeGUI();
    }

    /**
     * Sets the Numbs Look and Feel for a modern UI appearance, if available.
     */
    private void setLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            
        }
    }

    /**
     * Initialises and displays the main application window.
     */
    private void initializeGUI() {
        mainFrame = new JFrame("Operation Scheduler");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1000, 700);
        mainFrame.setLayout(new BorderLayout(10,10));

        // Add padding around the content
        ((JComponent) mainFrame.getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        createMenuBar();
        createMainContent();

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    /**
     * Creates the application menu bar.
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveItem = new JMenuItem("Save Data");
        JMenuItem loadItem = new JMenuItem("Load Data");
        JMenuItem exitItem = new JMenuItem("Exit");

        saveItem.addActionListener(e -> saveData());
        loadItem.addActionListener(e -> loadData());
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // Edit menu
        JMenu editMenu = new JMenu("Edit");
        JMenuItem undoItem = new JMenuItem("Undo");
        undoItem.addActionListener(e -> undoLastOperation());
        editMenu.add(undoItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        mainFrame.setJMenuBar(menuBar);
    }

    /**
     * Creates the main content area with tabs and output panel.
     */
    private void createMainContent() {
        tabbedPane = new JTabbedPane();

        // Create tabs
        JPanel professionalsPanel = createProfessionalsPanel();
        JPanel appointmentsPanel = createAppointmentsPanel();
        JPanel tasksPanel = createTasksPanel();
        JPanel resourcesPanel = createResourcesPanel();
        JPanel calendarPanel = createCalendarPanel();

        tabbedPane.addTab("Health Professionals", professionalsPanel);
        tabbedPane.addTab("Appointments", appointmentsPanel);
        tabbedPane.addTab("Tasks", tasksPanel);
        tabbedPane.addTab("Resources", resourcesPanel);
        tabbedPane.addTab("Calendar", calendarPanel);

        // Output area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        JScrollPane outputScrollPane = new JScrollPane(outputArea);
        outputScrollPane.setPreferredSize(new Dimension(1000, 180));
        outputScrollPane.setBorder(BorderFactory.createTitledBorder("Output"));

        mainFrame.add(tabbedPane, BorderLayout.CENTER);
        mainFrame.add(outputScrollPane, BorderLayout.SOUTH);
    }

    /**
     * Creates the Health Professionals management panel.
     *
     * @return Configured JPanel for professionals management
     */
    private JPanel createProfessionalsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        JButton addButton = new JButton("Add Professional");
        JButton listButton = new JButton("List Professionals");
        styleButton(addButton);
        styleButton(listButton);

        addButton.addActionListener(e -> addHealthProfessional());
        listButton.addActionListener(e -> listHealthProfessionals());

        buttonPanel.add(addButton);
        buttonPanel.add(listButton);

        // List display
        JTextArea listArea = new JTextArea();
        listArea.setEditable(false);
        listArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(listArea);
        scrollPane.setPreferredSize(new Dimension(960, 480));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates the Appointments management panel.
     *
     * @return Configured JPanel for appointments management
     */
    private JPanel createAppointmentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        JButton addButton = new JButton("Add Appointment");
        JButton addRecurringButton = new JButton("Add Recurring Appointment");
        JButton listButton = new JButton("List Appointments");
        JButton searchButton = new JButton("Search Available Slots");
        styleButton(addButton);
        styleButton(addRecurringButton);
        styleButton(listButton);
        styleButton(searchButton);

        addButton.addActionListener(e -> addAppointment());
        addRecurringButton.addActionListener(e -> addRecurringAppointment());
        listButton.addActionListener(e -> listAppointments());
        searchButton.addActionListener(e -> searchAvailableSlots());

        buttonPanel.add(addButton);
        buttonPanel.add(addRecurringButton);
        buttonPanel.add(listButton);
        buttonPanel.add(searchButton);

        // List display
        JTextArea listArea = new JTextArea();
        listArea.setEditable(false);
        listArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(listArea);
        scrollPane.setPreferredSize(new Dimension(960, 480));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates the Tasks management panel.
     *
     * @return Configured JPanel for tasks management
     */
    private JPanel createTasksPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15,10));
        JButton addButton = new JButton("Add Task");
        JButton listButton = new JButton("List Tasks");
        styleButton(addButton);
        styleButton(listButton);

        addButton.addActionListener(e -> addTask());
        listButton.addActionListener(e -> listTasks());

        buttonPanel.add(addButton);
        buttonPanel.add(listButton);

        // List display
        JTextArea listArea = new JTextArea();
        listArea.setEditable(false);
        listArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(listArea);
        scrollPane.setPreferredSize(new Dimension(960, 480));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates the Resources management panel.
     *
     * @return Configured JPanel for resources management
     */
    private JPanel createResourcesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        JButton addButton = new JButton("Add Resource");
        JButton listButton = new JButton("List Resources");
        styleButton(addButton);
        styleButton(listButton);

        addButton.addActionListener(e -> addResource());
        listButton.addActionListener(e -> listResources());

        buttonPanel.add(addButton);
        buttonPanel.add(listButton);

        // List display
        JTextArea listArea = new JTextArea();
        listArea.setEditable(false);
        listArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(listArea);
        scrollPane.setPreferredSize(new Dimension(960, 480));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates the Calendar view panel.
     *
     * @return Configured JPanel for calendar view
     */
    private JPanel createCalendarPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Control panel
        JPanel controlPanel = new JPanel();
        JComboBox<Integer> yearCombo = new JComboBox<>();
        JComboBox<String> monthCombo = new JComboBox<>(new String[]{
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        });
        JButton refreshButton = new JButton("Refresh");
        
        // Populate years (current year ±5 years)
        int currentYear = LocalDate.now().getYear();
        for (int year = currentYear - 5; year <= currentYear + 5; year++) {
            yearCombo.addItem(year);
        }
        yearCombo.setSelectedItem(currentYear);
        monthCombo.setSelectedIndex(LocalDate.now().getMonthValue() - 1);
        
        controlPanel.add(new JLabel("Year:"));
        controlPanel.add(yearCombo);
        controlPanel.add(new JLabel("Month:"));
        controlPanel.add(monthCombo);
        controlPanel.add(refreshButton);
        
        // Calendar panel
        JPanel calendarPanel = new JPanel(new GridLayout(0, 7, 5, 5));
        calendarPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        refreshButton.addActionListener(e -> updateCalendarView(
            (Integer) yearCombo.getSelectedItem(), 
            monthCombo.getSelectedIndex() + 1, 
            calendarPanel));
        
        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(calendarPanel), BorderLayout.CENTER);
        
        // Initial calendar update
        updateCalendarView(currentYear, LocalDate.now().getMonthValue(), calendarPanel);
        return panel;
    }

    /**
     * Updates the calendar view for the specified year and month.
     * 
     * @param year the year to display
     * @param month the month to display (1-12)
     * @param calendarPanel the panel to update
     */
    private void updateCalendarView(int year, int month, JPanel calendarPanel) {
        calendarPanel.removeAll();
        
        // Add day headers
        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : days) {
            JLabel label = new JLabel(day, SwingConstants.CENTER);
            label.setFont(new Font("SansSerif", Font.BOLD, 14));
            calendarPanel.add(label);
        }
        
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate first = yearMonth.atDay(1);
        int startOffset = first.getDayOfWeek().getValue() % 7; // Sunday = 0
        
        // Add empty cells for offset
        for (int i = 0; i < startOffset; i++) {
            calendarPanel.add(new JLabel(""));
        }
        
        // Add day cells
        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            JButton dayButton = new JButton(String.valueOf(day));
            dayButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
            LocalDate date = LocalDate.of(year, month, day);
            dayButton.addActionListener(e -> showAppointmentsOnDate(date));
            calendarPanel.add(dayButton);
        }
        
        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

    /**
     * Shows appointments on a specific date in the output area.
     * 
     * @param date the date to show appointments for
     */
    private void showAppointmentsOnDate(LocalDate date) {
        StringBuilder sb = new StringBuilder();
        sb.append("Appointments on ").append(date).append(":\n");
        
        for (HealthProfessional hp : scheduler.getAllHealthProfessionals()) {
            List<Appointment> appointments = scheduler.getDiary(hp).getAppointmentsOnDate(date);
            if (!appointments.isEmpty()) {
                sb.append(hp.getName()).append(":\n");
                for (Appointment appt : appointments) {
                    sb.append(" - ").append(appt.getStartTime()).append(" to ")
                      .append(appt.getEndTime()).append(": ")
                      .append(appt.getPatientName()).append(" (")
                      .append(appt.getTreatmentType()).append(")");
                    if (appt.getResource() != null) {
                        sb.append(" [Resource: ").append(appt.getResource().getName()).append("]");
                    }
                    sb.append("\n");
                }
            }
        }
        
        outputArea.append(sb.toString());
    }

    /**
     * Applies uniform styling to buttons.
     * @param button JButton to style.
     */
    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(new Color(70, 130, 180)); // Steel Blue
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(200, 35));
    }

    /**
     * Shows a dialog to add a new health professional.
     */
    private void addHealthProfessional() {
        JTextField nameField = new JTextField(20);
        JTextField professionField = new JTextField(20);
        JTextField officeField = new JTextField(20);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10,10));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Profession:"));
        panel.add(professionField);
        panel.add(new JLabel("Office Location:"));
        panel.add(officeField);

        int result = JOptionPane.showConfirmDialog(
                mainFrame,
                panel,
                "Add Health Professional",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String profession = professionField.getText().trim();
            String office = officeField.getText().trim();

            if (name.isEmpty() || profession.isEmpty() || office.isEmpty()) {
                showError("All fields must be filled");
                return;
            }

            HealthProfessional hp = new HealthProfessional(name, profession, office);
            scheduler.addHealthProfessional(hp);
            outputArea.append("Added health professional: " + hp + "\n");
        }
    }

    /**
     * Lists all health professionals in the output area.
     */
    private void listHealthProfessionals() {
        List<HealthProfessional> professionals = scheduler.getAllHealthProfessionals();
        if (professionals.isEmpty()) {
            outputArea.append("No health professionals registered.\n");
            return;
        }

        outputArea.append("=== Health Professionals ===\n");
        for (HealthProfessional hp : professionals) {
            outputArea.append(hp.toString() + "\n");
        }
    }

    /**
     * Shows a dialog to add a new appointment.
     */
    private void addAppointment() {
        List<HealthProfessional> professionals = scheduler.getAllHealthProfessionals();
        if (professionals.isEmpty()) {
            showError("No health professionals available. Please add some first.");
            return;
        }

        JComboBox<HealthProfessional> professionalCombo = new JComboBox<>(professionals.toArray(new HealthProfessional[0]));
        JTextField dateField = new JTextField(LocalDate.now().toString(), 10);
        JTextField startTimeField = new JTextField("09:00", 5);
        JTextField endTimeField = new JTextField("10:00", 5);
        JTextField treatmentField = new JTextField(20);
        JTextField patientField = new JTextField(20);
        JComboBox<Resource> resourceCombo = new JComboBox<>();
        resourceCombo.addItem(null); // No resource
        for (Resource res : scheduler.getAllSharedResources()) {
            resourceCombo.addItem(res);
        }

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("Health Professional:"));
        panel.add(professionalCombo);
        panel.add(new JLabel("Date (YYYY-MM-DD):"));
        panel.add(dateField);
        panel.add(new JLabel("Start Time (HH:MM):"));
        panel.add(startTimeField);
        panel.add(new JLabel("End Time (HH:MM):"));
        panel.add(endTimeField);
        panel.add(new JLabel("Treatment Type:"));
        panel.add(treatmentField);
        panel.add(new JLabel("Patient Name:"));
        panel.add(patientField);
        panel.add(new JLabel("Resource:"));
        panel.add(resourceCombo);

        int result = JOptionPane.showConfirmDialog(
                mainFrame,
                panel,
                "Add Appointment",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                LocalDate date = LocalDate.parse(dateField.getText());
                LocalTime startTime = LocalTime.parse(startTimeField.getText());
                LocalTime endTime = LocalTime.parse(endTimeField.getText());
                String treatment = treatmentField.getText().trim();
                String patient = patientField.getText().trim();
                Resource resource = (Resource) resourceCombo.getSelectedItem();

                if (treatment.isEmpty() || patient.isEmpty()) {
                    showError("Treatment type and patient name are required");
                    return;
                }

                if (endTime.isBefore(startTime) || endTime.equals(startTime)) {
                    showError("End time must be after start time");
                    return;
                }

                HealthProfessional selected = (HealthProfessional) professionalCombo.getSelectedItem();
                Appointment appt = new Appointment(date, startTime, endTime, treatment, patient, false, resource);

                if (scheduler.bookAppointment(List.of(selected), appt)) {
                    outputArea.append("Appointment booked successfully for " + selected.getName() + "\n");
                } else {
                    showError("Failed to book appointment - time slot not available");
                }
            } catch (DateTimeParseException e) {
                showError("Invalid date or time format. Please use YYYY-MM-DD for date and HH:MM for time.");
            }
        }
    }

    /**
     * Shows a dialog to add a new recurring appointment.
     */
    private void addRecurringAppointment() {
        List<HealthProfessional> professionals = scheduler.getAllHealthProfessionals();
        if (professionals.isEmpty()) {
            showError("No health professionals available. Please add some first.");
            return;
        }

        JComboBox<HealthProfessional> professionalCombo = new JComboBox<>(professionals.toArray(new HealthProfessional[0]));
        JTextField dateField = new JTextField(LocalDate.now().toString(), 10);
        JTextField startTimeField = new JTextField("09:00", 5);
        JTextField endTimeField = new JTextField("10:00", 5);
        JTextField treatmentField = new JTextField(20);
        JTextField patientField = new JTextField(20);
        JComboBox<Resource> resourceCombo = new JComboBox<>();
        resourceCombo.addItem(null); // No resource
        for (Resource res : scheduler.getAllSharedResources()) {
            resourceCombo.addItem(res);
        }
        JSpinner recurrenceSpinner = new JSpinner(new SpinnerNumberModel(7, 1, 30, 1));
        JSpinner occurrencesSpinner = new JSpinner(new SpinnerNumberModel(4, 1, 52, 1));

        JPanel panel = new JPanel(new GridLayout(9, 2, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("Health Professional:"));
        panel.add(professionalCombo);
        panel.add(new JLabel("Start Date (YYYY-MM-DD):"));
        panel.add(dateField);
        panel.add(new JLabel("Start Time (HH:MM):"));
        panel.add(startTimeField);
        panel.add(new JLabel("End Time (HH:MM):"));
        panel.add(endTimeField);
        panel.add(new JLabel("Treatment Type:"));
        panel.add(treatmentField);
        panel.add(new JLabel("Patient Name:"));
        panel.add(patientField);
        panel.add(new JLabel("Resource:"));
        panel.add(resourceCombo);
        panel.add(new JLabel("Recurrence (days):"));
        panel.add(recurrenceSpinner);
        panel.add(new JLabel("Number of Occurrences:"));
        panel.add(occurrencesSpinner);

        int result = JOptionPane.showConfirmDialog(
                mainFrame,
                panel,
                "Add Recurring Appointment",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                LocalDate date = LocalDate.parse(dateField.getText());
                LocalTime startTime = LocalTime.parse(startTimeField.getText());
                LocalTime endTime = LocalTime.parse(endTimeField.getText());
                String treatment = treatmentField.getText().trim();
                String patient = patientField.getText().trim();
                Resource resource = (Resource) resourceCombo.getSelectedItem();
                int recurrenceDays = (Integer) recurrenceSpinner.getValue();
                int occurrences = (Integer) occurrencesSpinner.getValue();

                if (treatment.isEmpty() || patient.isEmpty()) {
                    showError("Treatment type and patient name are required");
                    return;
                }

                if (endTime.isBefore(startTime) || endTime.equals(startTime)) {
                    showError("End time must be after start time");
                    return;
                }

                HealthProfessional selected = (HealthProfessional) professionalCombo.getSelectedItem();
                Appointment appt = new Appointment(date, startTime, endTime, treatment, patient, true, resource);

                if (scheduler.bookRecurringAppointment(List.of(selected), appt, recurrenceDays, occurrences)) {
                    outputArea.append("Recurring appointment booked successfully for " + selected.getName() + "\n");
                } else {
                    showError("Failed to book recurring appointment - time slot not available");
                }
            } catch (DateTimeParseException e) {
                showError("Invalid date or time format. Please use YYYY-MM-DD for date and HH:MM for time.");
            }
        }
    }

    /**
     * Shows a dialog to search for available time slots.
     */
    private void searchAvailableSlots() {
        List<HealthProfessional> professionals = scheduler.getAllHealthProfessionals();
        if (professionals.isEmpty()) {
            showError("No health professionals available. Please add some first.");
            return;
        }

        // Professional selection
        JPanel professionalPanel = new JPanel(new GridLayout(0, 1));
        JList<HealthProfessional> professionalList = new JList<>(professionals.toArray(new HealthProfessional[0]));
        professionalList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        professionalPanel.add(new JLabel("Select Professionals:"));
        professionalPanel.add(new JScrollPane(professionalList));

        // Resource selection
        JPanel resourcePanel = new JPanel(new GridLayout(0, 1));
        List<Resource> allResources = scheduler.getAllSharedResources();
        JList<Resource> resourceList = new JList<>(allResources.toArray(new Resource[0]));
        resourceList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        resourcePanel.add(new JLabel("Select Resources:"));
        resourcePanel.add(new JScrollPane(resourceList));

        // Date and duration
        JTextField startDateField = new JTextField(LocalDate.now().toString(), 10);
        JTextField endDateField = new JTextField(LocalDate.now().plusWeeks(1).toString(), 10);
        JTextField durationField = new JTextField("60", 5);

        JPanel datePanel = new JPanel(new GridLayout(3, 2, 10, 10));
        datePanel.add(new JLabel("Start Date (YYYY-MM-DD):"));
        datePanel.add(startDateField);
        datePanel.add(new JLabel("End Date (YYYY-MM-DD):"));
        datePanel.add(endDateField);
        datePanel.add(new JLabel("Duration (minutes):"));
        datePanel.add(durationField);

        JTabbedPane searchTabbedPane = new JTabbedPane();
        searchTabbedPane.addTab("Professionals", professionalPanel);
        searchTabbedPane.addTab("Resources", resourcePanel);
        searchTabbedPane.addTab("Dates", datePanel);

        int result = JOptionPane.showConfirmDialog(
                mainFrame,
                searchTabbedPane,
                "Search Available Slots",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                LocalDate startDate = LocalDate.parse(startDateField.getText());
                LocalDate endDate = LocalDate.parse(endDateField.getText());
                int duration = Integer.parseInt(durationField.getText());
                
                List<HealthProfessional> selectedProfessionals = professionalList.getSelectedValuesList();
                List<Resource> selectedResources = resourceList.getSelectedValuesList();
                
                if (selectedProfessionals.isEmpty()) {
                    showError("At least one professional must be selected");
                    return;
                }
                
                List<TimeSlot> availableSlots = scheduler.findAvailableSlots(
                    selectedProfessionals, 
                    selectedResources,
                    startDate, 
                    endDate, 
                    duration
                );
                
                outputArea.append("=== Available Slots ===\n");
                outputArea.append("Search took: " + scheduler.getLastSearchDuration() + "ms\n");
                if (availableSlots.isEmpty()) {
                    outputArea.append("No available slots found\n");
                } else {
                    for (TimeSlot slot : availableSlots) {
                        outputArea.append("- " + slot + "\n");
                    }
                }
            } catch (DateTimeParseException e) {
                showError("Invalid date format. Please use YYYY-MM-DD.");
            } catch (NumberFormatException e) {
                showError("Duration must be a valid number");
            }
        }
    }

    /**
     * Shows a dialog to add a new task.
     */
    private void addTask() {
        List<HealthProfessional> professionals = scheduler.getAllHealthProfessionals();
        if (professionals.isEmpty()) {
            showError("No health professionals available. Please add some first.");
            return;
        }

        JComboBox<HealthProfessional> professionalCombo = new JComboBox<>(professionals.toArray(new HealthProfessional[0]));
        JTextField descriptionField = new JTextField(20);
        JComboBox<String> priorityCombo = new JComboBox<>(new String[]{"High", "Medium", "Low"});

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("Health Professional:"));
        panel.add(professionalCombo);
        panel.add(new JLabel("Task Description:"));
        panel.add(descriptionField);
        panel.add(new JLabel("Priority:"));
        panel.add(priorityCombo);

        int result = JOptionPane.showConfirmDialog(
                mainFrame,
                panel,
                "Add Task",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            String description = descriptionField.getText().trim();
            if (description.isEmpty()) {
                showError("Task description is required");
                return;
            }

            HealthProfessional selected = (HealthProfessional) professionalCombo.getSelectedItem();
            String priority = (String) priorityCombo.getSelectedItem();

            Task task = new Task(description, priority);
            scheduler.getDiary(selected).addTask(task);
            outputArea.append("Task added successfully for " + selected.getName() + "\n");
        }
    }

    /**
     * Lists all appointments in the output area.
     */
    private void listAppointments() {
        List<HealthProfessional> professionals = scheduler.getAllHealthProfessionals();
        if (professionals.isEmpty()) {
            outputArea.append("No health professionals available.\n");
            return;
        }

        outputArea.append("=== Appointments ===\n");
        for (HealthProfessional hp : professionals) {
            List<Appointment> appointments = scheduler.getDiary(hp).getAllAppointments();
            if (!appointments.isEmpty()) {
                outputArea.append("Appointments for " + hp.getName() + ":\n");
                for (Appointment appt : appointments) {
                    outputArea.append("- " + appt + "\n");
                }
            }
        }
    }

    /**
     * Lists all tasks in the output area.
     */
    private void listTasks() {
        List<HealthProfessional> professionals = scheduler.getAllHealthProfessionals();
        if (professionals.isEmpty()) {
            outputArea.append("No health professionals available.\n");
            return;
        }

        outputArea.append("=== Tasks ===\n");
        for (HealthProfessional hp : professionals) {
            List<Task> tasks = scheduler.getDiary(hp).getAllTasks();
            if (!tasks.isEmpty()) {
                outputArea.append("Tasks for " + hp.getName() + ":\n");
                for (Task task : tasks) {
                    outputArea.append("- " + task + "\n");
                }
            }
        }
    }

    /**
     * Shows a dialog to add a new resource.
     */
    private void addResource() {
        JTextField nameField = new JTextField(20);
        JTextField typeField = new JTextField(20);
        JTextField locationField = new JTextField(20);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("Resource Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Resource Type:"));
        panel.add(typeField);
        panel.add(new JLabel("Location:"));
        panel.add(locationField);

        int result = JOptionPane.showConfirmDialog(
                mainFrame,
                panel,
                "Add Resource",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String type = typeField.getText().trim();
            String location = locationField.getText().trim();

            if (name.isEmpty() || type.isEmpty() || location.isEmpty()) {
                showError("All fields must be filled");
                return;
            }

            Resource resource = new Resource(name, type, location);
            scheduler.addSharedResource(resource);
            outputArea.append("Added resource: " + resource + "\n");
        }
    }

    /**
     * Lists all resources in the output area.
     */
    private void listResources() {
        List<Resource> resources = scheduler.getAllSharedResources();
        if (resources.isEmpty()) {
            outputArea.append("No resources available.\n");
            return;
        }

        outputArea.append("=== Resources ===\n");
        for (Resource res : resources) {
            outputArea.append("- " + res + "\n");
        }
    }

    /**
     * Saves the scheduler data to a file.
     */
    private void saveData() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
            try {
                FileManager.saveToFile(scheduler, fileChooser.getSelectedFile().getPath());
                outputArea.append("Data saved successfully to " + fileChooser.getSelectedFile().getPath() + "\n");
            } catch (Exception ex) {
                showError("Error saving data: " + ex.getMessage());
            }
        }
    }

    /**
     * Loads scheduler data from a file.
     */
    private void loadData() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
            try {
                scheduler = FileManager.loadFromFile(fileChooser.getSelectedFile().getPath());
                outputArea.append("Data loaded successfully from " + fileChooser.getSelectedFile().getPath() + "\n");
            } catch (Exception ex) {
                showError("Error loading data: " + ex.getMessage());
            }
        }
    }

    /**
     * Undoes the last operation.
     */
    private void undoLastOperation() {
        if (scheduler.undo()) {
            outputArea.append("Undo successful");
        } else {
            outputArea.append("Nothing to undo");
        }
    }

    /**
     * Displays an error message dialog.
     *
     * @param message The error message to display
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(mainFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}