package Teachers;

public class Teacher {
	private String name; //name of the teacher
	private int lessonsPerWeek; //number of lessons the teacher has per week
	private boolean admin; //whether the teacher is an administrator or not. This includes Subject area leaders and part timers
	private int id; //id of the teacher
	private int dutiesToBeAssigned; // the number of duties that should be assigned to the teacher
	private int dutiesAssigned;// the number of duties currently done by the teacher
	private int points; // the number of points the teacher has. Based on conditions
	private Lesson[] lessons; // contains lessons teachers have on the day
	private Subject[] subject; //contains the number of subjects the teacher has
	private boolean assignedToday; // if the teacher has been assigned today
	private boolean homebase; // whether the teacher has a homebase or not 
	//Setters and Getters 
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLessonsPerWeek() {
		return lessonsPerWeek;
	}
	public void setLessonsPerWeek(int lessonsPerWeek) {
		this.lessonsPerWeek = lessonsPerWeek;
	}
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getDutiesToBeAssigned() {
		return dutiesToBeAssigned;
	}
	public void setDutiesToBeAssigned() { //As this is being set internally, this does not take any input
		if(this.lessonsPerWeek == 21) { //If they have 21, they get 0 duties 
			this.dutiesToBeAssigned = 0;
			return;
		}
		if (this.admin == true || this.lessonsPerWeek == 20) { //if they have 20 or are admin, they get one duty
			this.dutiesToBeAssigned = 1;
			return;
		}
		
		this.dutiesToBeAssigned = 3; //default to 3 duties
		
	}
	public int getDutiesAssigned() {
		return dutiesAssigned;
	}
	public void setDutiesAssigned(int dutiesAssigned) {
		this.dutiesAssigned = dutiesAssigned;
	}
	public Lesson[] getLessons() {
		return lessons;
	}
	public void setLessons(Lesson[] lessons) {
		this.lessons = lessons;
	}
	public Subject[] getSubject() {
		return subject;
	}
	public void setSubject(Subject[] subject) {
		this.subject = subject;
	}
	public boolean getAssignedToday() {
		return assignedToday;
	}
	public void setAssignedToday(boolean assignedToday) {
		this.assignedToday = assignedToday;
	}
	public boolean isHomebase() {
		return homebase;
	}
	public void setHomebase(boolean homebase) {
		this.homebase = homebase;
	}
	
	public String toString() {
		String k = " ";
		k = k + "Teacher ID : " + this.getId();
		k = k + " Name : " + this.getName();
		k = k + " Admin : " + this.isAdmin();
		return k;
	}
	
	public String toStringWithLessons() {
		String k = this.toString() + " ";
		Lesson [] temp = this.getLessons();
		for (int i = 0; i < this.getLessons().length; i++) {
			k = k + "Day " + i + " " + temp[i].toString() + " ";
		}
		return k;
	}
	
}
