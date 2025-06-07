package project;

import java.util.Scanner;

/**
 * Entry point for the Operation Scheduler application.
 * 
 * This application allows users to choose between a Console-based UI and a GUI-based interface
 * for managing scheduled operations.
 */
public class OperationScheduler {

    /**
     * Main method that starts the Operation Scheduler program.
     * 
     * Prompts the user to select the interface (Console or GUI), initializes the scheduler,
     * and starts the chosen interface.
     * 
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose interface:");
        System.out.println("1. Console UI");
        System.out.println("2. GUI");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        Scheduler scheduler = new Scheduler();

        if (choice == 1) {
            // Start the console-based user interface
            ConsoleUI consoleUI = new ConsoleUI(scheduler);
            consoleUI.start();
        } else if (choice == 2) {
            // Launch the GUI using Swing
            javax.swing.SwingUtilities.invokeLater(() -> {
                new SchedulerGUI(scheduler);
            });
        } else {
            // Handle invalid input
            System.out.println("Invalid choice. Exiting.");
        }

        scanner.close();
    }
}
