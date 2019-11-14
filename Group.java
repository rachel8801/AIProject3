package objects;

import java.util.ArrayList;

public class Group {
	public ArrayList<TestObject> arr;
	public int type;

	public Group(int type, ArrayList<TestObject> obj_arr){
		this.type = type;
		arr = obj_arr;
	}
}
