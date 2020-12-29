package Teachers;

import duties.Time;

public class AssignedTimes extends Time { //class to help organize duties based on times
	public boolean [] assignedOnThisTime = new boolean[5]; //if the teacher has been assigned at this time on the day. Make it public as it is easier to access 
	public AssignedTimes() {
		// TODO Auto-generated constructor stub
	}

	public AssignedTimes(int m) {
		super(m);
		// TODO Auto-generated constructor stub
	}

	public AssignedTimes(int hours, int minutes, boolean [] assignedOnThisTime ) {
		super(hours, minutes);
		this.assignedOnThisTime = assignedOnThisTime;
		// TODO Auto-generated constructor stub
	}
	
	public AssignedTimes(Time StartTime, boolean []assignedOnThisTime) {
		super(StartTime.getHours(), StartTime.getMinutes());
		this.assignedOnThisTime = assignedOnThisTime; 
	}

}
