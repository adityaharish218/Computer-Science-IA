package duties;


public class Time {
	private int hours;
	private int minutes;

	public Time() {
	}
	public void setTime(int hours, int minutes) {
		this.setHours(hours);
		this.setMinutes(minutes);
	}
	
	public Time(int m) {
		this.setMinutes(m);
	}

	public Time(int hours, int minutes) {
		this.setHours(hours);
		this.setMinutes(minutes);
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		hours = Math.abs(hours);
		hours = hours % 24;
		this.hours = hours;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		minutes = Math.abs(minutes);
		int hours = minutes / 60;
		this.setHours(this.hours + hours);
		minutes = minutes % 60;
		this.minutes = minutes;
	}

	public int toMinutes() {
		int minutes = this.hours * 60;
		return minutes + this.minutes;
	}

	public boolean equals(Time another) {
		return this.minutes == another.getMinutes() && this.hours == another.getHours();
	}

	public int compareTo(Time another) {
		return this.toMinutes() - another.toMinutes();
	}

	public String toString() {
		String returningString = "";

		if (this.hours < 10) {
			returningString = returningString + "0" + this.hours + ":";
		} else {
			returningString = returningString + this.hours + ":";
		}

		if (this.minutes < 10) {
			returningString = returningString + "0" + this.minutes + "";
		} else {
			returningString = returningString + this.minutes;
		}
		return returningString;
	}

	public int diff(Time another) {
		if (this.hours > another.getHours()) {
			return 1440 - Math.abs(this.compareTo(another));
		}
		return Math.abs(this.compareTo(another));
	}

	public void add(Time another) {
		this.setHours(this.hours + another.getHours());
		this.setMinutes(this.minutes + another.getMinutes());
	}

}
