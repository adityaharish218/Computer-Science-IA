package Teachers;

import java.util.ArrayList;

import duties.Duty;

public class Teacher {
	private String name = ""; //name of the teacher, set it empty to avoid null pointer exception
	private int lessonsPerWeek; //number of lessons the teacher has per week
	private boolean admin; //whether the teacher is an administrator or not. This includes Subject area leaders and part timers
	private int id; //id of the teacher
	private int dutiesToBeAssigned; // the number of duties that should be assigned to the teacher
	private int dutiesAssigned;// the number of duties currently done by the teacher
	private int points; // the number of points the teacher has. Based on conditions
	private Lesson[] lessons = new Lesson[1]; // contains lessons teachers have on the day
	private Subject[] subject = new Subject[1]; //contains the number of subjects the teacher has
	private boolean homebase; // whether the teacher has a homebase or not 
	private boolean assignedAfterSchool = false; //whether a teacher has already been assigned an after school duty or not;
	private int howManyDutiescanBeAssigned = 0; // how many different duties the teacher can perform.
	private boolean alreadyReassigned = false; //if the teacher has already been reAssigned
	private ArrayList<Duty> assignedDuties = new ArrayList<Duty>(); //the duties assigned to the teacher
	public boolean isAlreadyReassigned() {
		return alreadyReassigned;
	}
	public void setAlreadyReassigned(boolean alreadyReassigned) {
		this.alreadyReassigned = alreadyReassigned;
	}

	//setters and getters
	public ArrayList<Duty> getAssignedDuties() {
		return assignedDuties;
	}
	public void setAssignedDuties(ArrayList<Duty> assignedDuties) {
		this.assignedDuties = assignedDuties;
	}
	public int getHowManyDutiescanBeAssigned() {
		return howManyDutiescanBeAssigned;
	}
	public void setHowManyDutiescanBeAssigned(int howManyDutiescanBeAssigned) {
		this.howManyDutiescanBeAssigned = howManyDutiescanBeAssigned;
	}
	public boolean isAssignedAfterSchool() {
		return assignedAfterSchool;
	}
	public void setAssignedAfterSchool(boolean assignedAfterSchool) {
		this.assignedAfterSchool = assignedAfterSchool;
	}
	
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
		if(this.lessonsPerWeek >= 21) { //If they have 21 or more, they get 0 duties 
			this.dutiesToBeAssigned = 0;
			return;
		}
		if (this.admin == true || this.lessonsPerWeek == 20) { //if they have 20 or are admin, they get one duty
			this.dutiesToBeAssigned = 1;
			return;
		}
		if(this.lessonsPerWeek < 10) { //if they work part time 
			this.dutiesToBeAssigned = 1; //they are assigned one duty
			return;
		}
		this.dutiesToBeAssigned = 3; //default to 3 duties
		
	}
	
	public void setDutiesToBeAssigned(int hoursForNone, int hoursForOne, int forAdmin, int hoursForPartTime, int forPartTimers, int forNormal) { //custom method
		if(this.lessonsPerWeek >= hoursForNone) { //check if the teacher has more than or is equal to these number of hours such that they should not be assigned a duty
			this.dutiesToBeAssigned = 0; //this teacher can do 0 duties
			return; //break out of the function
		}
		if(this.lessonsPerWeek >= hoursForOne) { //check if the teacher has more than or is equal to these number of hours such that they should only be assigned one duty
			this.dutiesToBeAssigned = 1; //this teacher can only do 1 duty
			return;//break out of the function
		}
		if(this.isAdmin()) { //if the teacher is admin
			this.dutiesToBeAssigned = forAdmin; //assign them the number of duties the user wishes to be assigned to admin
			return;
		}
		if(this.lessonsPerWeek <= hoursForPartTime) { //if the teacher is part time
			this.dutiesToBeAssigned = forPartTimers; //assign them the number of duties the user wishes to be assigned to partTimers
		}
		this.lessonsPerWeek = forNormal; //if they do not meet any of the other conditions, then they are normal so assign them the number of duties the user wishes to be assigned to normal.
		
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

	public boolean isHomebase() {
		return homebase;
	}
	public void setHomebase(boolean homebase) {
		this.homebase = homebase;
	}
	
	public String toString() { //To string method without lessons 
		String k = " ";
		k = k + "Teacher ID : " + this.getId();
		k = k + " Name : " + this.getName();
		k = k + " Admin : " + this.isAdmin();
		return k;
	}
	
	public String toStringWithLessons() { //To string method with lessons
		String k = this.toString() + " ";
		Lesson [] temp = this.getLessons();
		for (int i = 0; i < this.getLessons().length; i++) {
			k = k + "Day " + (i + 1) + " " + temp[i].toString() + " ";
		}
		return k;
	}
	public String toStringWithSubjects() { //print out teachers with subjects
		String k = this.toString() + " ";
		for(int i = 0; i < this.getSubject().length; i++) {
			Subject [] temp = this.getSubject();
			k = k + "Subject " + (i + 1) + ": " + temp[i].toString(); 
		}
		return k;
	}
	
	public String toStringWithNoOfDuties() { //print out teachers with the number of duties they can do
		String k = this.toString() + " ";
		k = k + "No of Duties teacher can do : " + this.getHowManyDutiescanBeAssigned();
		return k;
	}
	
	
	
	public void Clear() {
		this.name = null;
		this.lessonsPerWeek = 0;
		this.admin = false;
		this.id = 0;
		this.dutiesToBeAssigned = 0;
		this.dutiesAssigned = 0;
		this.lessons = null;
		this.subject = null;
		this.homebase = false;
	}
	
	public Teacher() {} //empty constructor
	public Teacher(String name, int ID, int lessonsPerWeek, boolean homebase, Lesson [] lessons) {
		this.name = name;
		this.id = ID;
		this.lessonsPerWeek = lessonsPerWeek;
		this.homebase = homebase;
		this.lessons = lessons;
		
	}
	
	public String toStringWithNumberOfDutiesToBeSet() {
		String k = this.toString();
		k = k + " No of Duties to be set " + this.getDutiesToBeAssigned();
		return k;
	}
	
	public int CompareForSort() { //used for sorting 
		if(this.isAdmin()) { //if the teacher is admin
			return 0; //return 0 signaling teacher is admin
		}
		return 1; //return 0 as teacher is not admin
	}
	
	public void setNoOfLessonsOnEachDay() { //method to set the number of lessons on each day for the teacher
		for(int i = 0; i < this.lessons.length; i++) { //loop through all of the teacher lessons
			this.lessons[i].setLessonsToday(); //uses the setLessonsToday method to set the lessons on each day
		}
	}
	
	public void clearForCustom() { //method to clear the teacher of all duties assigned
		this.getAssignedDuties().clear(); //clear the duties that have been assigned to this teacher
		this.setAssignedAfterSchool(false); //set to false that this teacher has been assigned after school
	}
	
	public String toStringWithLessonCount() {
		String k = this.toString();
		for(int i = 0; i < this.lessons.length; i++) {
			k = k + " Day " + (i+1) + " " + this.lessons[i].getLesosnsToday();
		}
		return k;
	}
	
	
	
}
