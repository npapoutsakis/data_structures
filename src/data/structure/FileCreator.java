package data.structure;

import java.io.IOException;
/**
 *	FileCreator is an interface with some methods that SortedFileCreator and 
 *	SerializedFileCreator will declare
 * @author Nick-PC
 */
public interface FileCreator {
	public static int numberOfNodes = 10000;
	public static int DataPageSize = 128;
	public static int DataNodeSize = 32;
	public static int IndexNodeSize = 8;
	
	/**
	 * CreateFile and CreateIndexFile will create files depending on the type of 
	 * organization we will choose
	 * 
	 * @param filename is the name of the file that we'll create random nodes 
	 * @return functions will return 1 if success 0 otherwise
	 */
	public int createFile(String filename) throws IOException;
	
	public int createIndexFile(String name) throws IOException;

}
