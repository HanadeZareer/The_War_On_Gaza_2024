public class Martyr {
	// Declaration of variables
	private String martyrName;
	private String dateOfMartyrdom;

	// no-argument constructor
	public Martyr() {
	}	
	/**
	 * main argument constructor
	 * 
	 * @param martyrName
	 * @param dateOfMartyrdom
	 */
	public Martyr(String martyrName, String dateOfMartyrdom) {
		this.martyrName = martyrName;
		this.dateOfMartyrdom = dateOfMartyrdom;
	}

	// Setter and getter methods

	// return the martyr's name
	public String getMartyrName() {
		return martyrName;
	}

	/**
	 * set the martyr's name
	 * 
	 * @param martyrName
	 */
	public void setMartyrName(String martyrName) {
		this.martyrName = martyrName;
	}

	// return the date of martyrdom
	public String getDateOfMartyrdom() {
		return dateOfMartyrdom;
	}

	/**
	 * set the date of martyrdom
	 * 
	 * @param dateOfMartyrdom
	 */
	public void setDateOfMartyrdom(String dateOfMartyrdom) {
		this.dateOfMartyrdom = dateOfMartyrdom;
	}

	// the information of the martyr
	@Override
	public String toString() {
		return "Martyr [martyr name: " + martyrName + ", date of martyrdom: " + dateOfMartyrdom + "]";
	}

}
