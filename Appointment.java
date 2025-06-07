package project;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a medical appointment with date, time, treatment details, and resource.
 */
public class Appointment {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String treatmentType;
    private String patientName;
    private boolean isRecurring;
    private Resource resource;

    /**
     * Constructs a new Appointment.
     * 
     * @param date the date of the appointment
     * @param startTime the starting time
     * @param endTime the ending time
     * @param treatmentType the type of treatment
     * @param patientName the name of the patient
     * @param isRecurring whether the appointment is recurring
     * @param resource the resource booked for this appointment
     */
    public Appointment(LocalDate date, LocalTime startTime, LocalTime endTime, 
                      String treatmentType, String patientName, 
                      boolean isRecurring, Resource resource) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.treatmentType = treatmentType;
        this.patientName = patientName;
        this.isRecurring = isRecurring;
        this.resource = resource;
    }

    /**
     * Gets the date of the appointment.
     * 
     * @return the appointment date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date of the appointment.
     * 
     * @param date the new appointment date
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Gets the start time of the appointment.
     * 
     * @return the start time
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time of the appointment.
     * 
     * @param startTime the new start time
     */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the end time of the appointment.
     * 
     * @return the end time
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time of the appointment.
     * 
     * @param endTime the new end time
     */
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets the treatment type.
     * 
     * @return the treatment type
     */
    public String getTreatmentType() {
        return treatmentType;
    }

    /**
     * Sets the treatment type.
     * 
     * @param treatmentType the new treatment type
     */
    public void setTreatmentType(String treatmentType) {
        this.treatmentType = treatmentType;
    }

    /**
     * Gets the patient name.
     * 
     * @return the patient name
     */
    public String getPatientName() {
        return patientName;
    }

    /**
     * Sets the patient name.
     * 
     * @param patientName the new patient name
     */
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    /**
     * Checks if the appointment is recurring.
     * 
     * @return true if recurring, false otherwise
     */
    public boolean isRecurring() {
        return isRecurring;
    }

    /**
     * Sets whether the appointment is recurring.
     * 
     * @param recurring true for recurring, false for single occurrence
     */
    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }

    /**
     * Gets the resource booked for this appointment.
     * 
     * @return the resource, or null if not booked
     */
    public Resource getResource() {
        return resource;
    }

    /**
     * Sets the resource for this appointment.
     * 
     * @param resource the resource to book
     */
    public void setResource(Resource resource) {
        this.resource = resource;
    }

    @Override
    public String toString() {
        String resourceInfo = (resource != null) ? " using " + resource.getName() : "";
        return "Appointment for " + patientName + " on " + date + 
               " from " + startTime + " to " + endTime + 
               " (" + treatmentType + ")" + resourceInfo;
    }

    /**
     * Checks if this appointment overlaps with another appointment.
     * 
     * @param other the other appointment to check against
     * @return true if the appointments overlap, false otherwise
     */
    public boolean overlapsWith(Appointment other) {
        if (!this.date.equals(other.date)) return false;
        
        // Check time overlap
        boolean timeOverlap = this.startTime.isBefore(other.endTime) && 
                             this.endTime.isAfter(other.startTime);
        
        // Check resource conflict if both use the same resource
        boolean resourceConflict = this.resource != null && 
                                  other.resource != null &&
                                  this.resource.equals(other.resource);
        
        return timeOverlap && resourceConflict;
    }
}