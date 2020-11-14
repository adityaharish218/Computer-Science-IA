package Teachers;

public class Lesson {
	public boolean one; //Whether teacher has lesson one
	public boolean two;//Whether teacher has lesson two
	public boolean three;//Whether teacher has lesson three
	public boolean four;//Whether teacher has lesson four
	public boolean five;//Whether teacher has lesson five
	public boolean six;//Whether teacher has lesson six
	public boolean homebase; // whether the teacher has a homebase or not 
	
	public static boolean setLesson(String s) { //Method for setting lesson
		if(s.equals(null)) { //If there is no input, just return false
			return false;
		}
		return true;
	}
}
