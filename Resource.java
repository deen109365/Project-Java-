package project;

import java.util.Objects;

/**
 * Represents a bookable medical resource like an operating theatre or MRI
 * scanner.
 */
public class Resource {
	private String name;
	private String type;
	private String location;

	/**
	 * Constructs a new Resource.
	 * 
	 * @param name     the name of the resource
	 * @param type     the type of resource (e.g., "Operating Theatre", "MRI
	 *                 Scanner")
	 * @param location the location of the resource
	 */
	public Resource(String name, String type, String location) {
		this.name = name;
		this.type = type;
		this.location = location;
	}

	/**
	 * Gets the name of the resource.
	 * 
	 * @return the resource name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the resource.
	 * 
	 * @param name the new resource name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the type of the resource.
	 * 
	 * @return the resource type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type of the resource.
	 * 
	 * @param type the new resource type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets the location of the resource.
	 * 
	 * @return the resource location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Sets the location of the resource.
	 * 
	 * @param location the new resource location
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return name + " (" + type + ") - " + location;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Resource that = (Resource) obj;
		return name.equals(that.name) && type.equals(that.type) && location.equals(that.location);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, type, location);
	}
}