package Teachers;

public class Lesson {
	boolean one; //Whether teacher has lesson one
	boolean two;//Whether teacher has lesson two
	boolean three;//Whether teacher has lesson three
	boolean four;//Whether teacher has lesson four
	boolean five;//Whether teacher has lesson five
	boolean six;//Whether teacher has lesson six
	
	public boolean setLesson(String s) { //Method for setting lesson
		if(s.equals(null)) { //If there is no input, just return false
			return false;
		}
		return true;
	}
}
