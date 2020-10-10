package Teachers;

public class Teacher {
	private String name; //name of teacher
	private int noOfLessons; // no of Lessons in the day
	private boolean headOfDepartment; //whether the teacher is head of department or not
	private int hoursWorked; // no of hours they worked
	private boolean admin; //whether they are admin or not
	
	public Teacher() {}
	public Teacher(String name, int noOfLessons, boolean headOfDepartement, int hoursWorked, boolean admin) {
		this.setName(name);
		this.setNoOfLessons(noOfLessons);
		this.setHeadOfDepartment(headOfDepartement);
		this.setHoursWorked(hoursWorked);
		this.setAdmin(admin);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNoOfLessons() {
		return noOfLessons;
	}
	public void setNoOfLessons(int noOfLessons) {
		this.noOfLessons = noOfLessons;
	}
	public boolean isHeadOfDepartment() {
		return headOfDepartment;
	}
	public void setHeadOfDepartment(boolean headOfDepartment) {
		this.headOfDepartment = headOfDepartment;
	}
	public int getHoursWorked() {
		return hoursWorked;
	}
	public void setHoursWorked(int hoursWorked) {
		this.hoursWorked = hoursWorked;
	}
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
}
