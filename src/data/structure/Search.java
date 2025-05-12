package data.structure;
/**
 * @author Nick-PC
 */
public interface Search {
	
	public static final int numberOfNodes = 10000;
	public static final int DataPageSize = 128;
	public static final int NodeSize = 32;
	public static final int IndexNodeSize = 8;
		
	//Declaring counters for Disk Accesses
	public int[] counter = new int[2]; 
	
	//Get counters
	
	//counter that will count disk accesses only when  searching Node file(SimpleNode) 
	public int getCounterSearch();  		
	
	public int getCounterUsingIndex();
	
	//reset Counter
	
	//counter that will count disk accesses only when searching using index Node file
	public void resetCounterSearch();
	
	public void resetCounterUsingIndex(); 
	
}