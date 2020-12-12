package Teachers;

public class Lesson {
	public boolean one; //Whether teacher has lesson one
	public boolean two;//Whether teacher has lesson two
	public boolean three;//Whether teacher has lesson three
	public boolean four;//Whether teacher has lesson four
	public boolean five;//Whether teacher has lesson five
	public boolean six;//Whether teacher has lesson six
	public boolean lunch; //Whether teacher has a lesson in lunch or not
	
	public Lesson(boolean a, boolean b, boolean c, boolean d, boolean e, boolean f, boolean g) { //Constructor
		this.one = a;
		this.two = b;
		this.three = c;
		this.four = d;
		this.lunch = e;
		this.five = f;
		this.six = g;
	}
	
	public String yesNo (boolean a) { //returns either yes or no
		if (a) {
			return "Yes"; //if true then there is a lesson
		}
		return "No"; //if false then that means there is no lesson
	}
	public String toString() { //returns all lessons in yes no form;
		String k = "";
		k = k + this.yesNo(one);
		k = k + " " + this.yesNo(two);
		k = k + " " + this.yesNo(three);
		k = k + " " + this.yesNo(four);
		k = k + " " + this.yesNo(lunch);
		k = k + " " + this.yesNo(five);
		k = k + " " + this.yesNo(six);
		return k;
	}
	
}
