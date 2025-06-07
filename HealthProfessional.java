package project;

import java.util.Objects;

/**
 * Represents a health professional with personal information.
 */
public class HealthProfessional {
	private String name;
	private String profession;
	private String officeLocation;

	/**
	 * Constructs a new HealthProfessional.
	 * 
	 * @param name           the name of the professional
	 * @param profession     their profession (e.g., "Doctor", "Nurse")
	 * @param officeLocation their work location
	 */
	public HealthProfessional(String name, String profession, String officeLocation) {
		this.name = name;
		this.profession = profession;
		this.officeLocation = officeLocation;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name of the individual
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the profession.
	 * 
	 * @return the profession of the individual
	 */
	public String getProfession() {
		return profession;
	}

	/**
	 * Sets the profession.
	 * 
	 * @param profession the profession to set
	 */
	public void setProfession(String profession) {
		this.profession = profession;
	}

	/**
	 * Gets the office location.
	 * 
	 * @return the location of the office
	 */
	public String getOfficeLocation() {
		return officeLocation;
	}

	/**
	 * Sets the office location.
	 * 
	 * @param officeLocation the location of the office to set
	 */
	public void setOfficeLocation(String officeLocation) {
		this.officeLocation = officeLocation;
	}

	@Override
	public String toString() {
		return name + " (" + profession + ") - " + officeLocation;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		HealthProfessional that = (HealthProfessional) obj;
		return name.equals(that.name) && profession.equals(that.profession)
				&& officeLocation.equals(that.officeLocation);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, profession, officeLocation);
	}
}