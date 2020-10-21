package Teachers;

public class Teacher {
	private String name; //name of teacher
	private int noOfLessonsinDay; // no of Lessons in the day
	private boolean headOfDepartment; //whether the teacher is head of department or not
	private int hoursWorked; // no of hours they worked
	private boolean admin; //whether they are admin or not
	private String meetingDay;
	int points;
	
	public Teacher() {}
	public Teacher(String name, int noOfLessonsinDay, boolean headOfDepartement, int hoursWorked, boolean admin, String meetingDay) {
		this.setName(name);
		this.setNoOfLessonsinDay(noOfLessonsinDay
				);
		this.setHeadOfDepartment(headOfDepartement);
		this.setHoursWorked(hoursWorked);
		this.setAdmin(admin);
		this.setMeetingDay(meetingDay);
		setPoints();
	}
	public String getMeetingDay() {
		return meetingDay;
	}
	public void setMeetingDay(String meetingDay) {
		this.meetingDay = meetingDay;
	}

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNoOfLessonsinDay() {
		return noOfLessonsinDay;
	}
	public void setNoOfLessonsinDay(int noOfLessons) {
		this.noOfLessonsinDay = noOfLessons;
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
	
	public String isAdmintoString() {
		if(this.admin) {
			return "yes";
		}
		return "no";
	}
	public String isHeadOfDepartmenttoString() {
		if(this.headOfDepartment) {
			return "yes";
		}
		return "no";
	}
	
	private void setPoints() {
		if(this.admin) {
			this.points = 0;
			return;
		}
		
	}
	
	
	public String toString() {
		String returningString = "Teacher Name : " + this.name + "\n";
		returningString = returningString + "No of lessons in a week : " + this.noOfLessons + "\n";
		returningString = returningString + "Hours worked: " + this.hoursWorked + "\n";
		returningString = returningString + "HeadofDepartment: " + this.isHeadOfDepartmenttoString() + "\n";
		returningString = returningString + "Admin: " + this.isAdmintoString() + "\n";
		return returningString;
	}
	
}
