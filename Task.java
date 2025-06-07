package project;

import java.io.Serializable;

/**
 * Represents a task for a health professional.
 */
public class Task implements Serializable {
    
	private static final long serialVersionUID = 1078756307900380910L;
	private String description;
    private String priority;

    /**
     * Constructs a new Task.
     * @param description the task description
     * @param priority the task priority
     */
    public Task(String description, String priority) {
        this.description = description;
        this.priority = priority;
    }

    // Getters and setters methods 
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
    
    @Override
    public String toString() {
        return description + " (Priority: " + priority + ")";
    }
}