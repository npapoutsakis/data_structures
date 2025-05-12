package data.structure;

import java.io.IOException;

public interface Tester {
	
	//We will search for 20 times a random key
	public final int searchNumber = 20; 
	
	/**
	 * Tester interface contains a method doTest()
	 * doTest() method will make 20 searches and count average
	 * Disk Accesses both when we ArraySearch and BinarySearch.
	 * @throws IOException
	 */
	public void doTest() throws IOException;
	
}
