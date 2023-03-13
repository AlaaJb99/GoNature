package logic;

public class Authorized {
	
	private String firstName;
	private String lastName;
	private String workerNumber;
	private String email;
	private String role;
	private String parkNum;
	
	public Authorized(String firstName, String lastName, String workerNumber, String email, String role, String parkNum) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.workerNumber = workerNumber;
		this.email = email;
		this.role = role;
		this.parkNum = parkNum;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getWorkerNumber() {
		return workerNumber;
	}

	public void setWorkerNumber(String workerNumber) {
		this.workerNumber = workerNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getParkNum() {
		return parkNum;
	}

	public void setParkNum(String parkNum) {
		this.parkNum = parkNum;
	}

	@Override
	public String toString() {
		return String.format("%s %s %s %s %s %s\n", firstName,lastName,workerNumber,email,role,parkNum);
	}
}
