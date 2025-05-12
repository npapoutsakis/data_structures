package data.structures.Nodes;

public class DataNode extends SimpleNode {
	
	private static final int StringSize = 28; 			//Default NodeSize is 32 bytes 
	private String name;
	
	public DataNode(int key) {
		super(key);										//
		this.name = getAlphaNumericString(StringSize);  //every time i make a new node, a string is generated as Info of Node 
	}

///////////set-get for class variables///////////

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
////////////////////////methods/////////////////////////
	
	//Random string creator
	public static String getAlphaNumericString(int length) 
	{ 
		// chose a Character random from this String 
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
				            + "0123456789"
				            + "abcdefghijklmnopqrstuvxyz"; 

		// create StringBuffer size of AlphaNumericString 
		StringBuilder sb = new StringBuilder(length); 
		
		for (int i = 0; i < length; i++) { 
		    // generate a random number between 
		    // 0 to AlphaNumericString variable length 
		    int index 
			= (int)(AlphaNumericString.length() 
				* Math.random()); 

		    // add Character one by one in end of sb 
		    sb.append(AlphaNumericString 
				  .charAt(index)); 
		} 
		return sb.toString(); 
	}
	
}
