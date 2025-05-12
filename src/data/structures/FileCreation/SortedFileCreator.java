package data.structures.FileCreation;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import data.structure.FileCreator;
import data.structures.FileManager.FileManager;
import data.structures.Nodes.DataNode;
import data.structures.Nodes.IndexNode;
import data.structures.Nodes.SimpleNode;

import java.util.Random;
/**
 * SortedFileCreator implements FileCreator and creates sorted Files of
 * type C and D.
 * @author Nick-PC
 */
public class SortedFileCreator implements FileCreator {
	
	private SimpleNode[] dataNodes;
	private IndexNode[] indexNodes;
	private int[] performanceCounter;	//counter that will count disk accesses of external sorting
	
	private FileManager f = null;
	private int pageOfNode = 0;
	
	public SortedFileCreator() {
		this.dataNodes = new SimpleNode[numberOfNodes];			//Declaring Nodes
		this.indexNodes = new IndexNode[numberOfNodes];
		this.performanceCounter = new int[2];
		this.performanceCounter[0] = 0;
		this.performanceCounter[1] = 0;
		f = new FileManager();
	}	

/////////////////////set-get/////////////////////////////
	
	public FileManager getFileManager() {
		return this.f;
	}
	
	public SimpleNode[] getDataNodes() {
		return this.dataNodes;
	}
	
	public void setDataNodes(SimpleNode[] data) {
		this.dataNodes = data;
	}
	
	public int getPerformanceCounterC() {
		return this.performanceCounter[0];
	}
	
	public int getPerformanceCounterD() {
		return this.performanceCounter[1];
	}
	
	public int getPageOfNode() {
		return this.pageOfNode;
	}
	
/////////////////////////Methods/////////////////////////////
	
	/**
	 * 	CreateFile will create a new Sorted File and will take data from an already existing file
	 *  made by A organization.
	 *  
	 *  Specifically we read from file each page and we bring all data into main memory(buffer).
	 *  Then we create DataNodes with the info taken from file. 
	 *  
	 *  We end up with a SimpleNode[] array and using Arrays.sort() we sort them by key.
	 *  
	 *  At the end we create a new file and we store(write) the sorted array[] of nodes.
	 */
	@Override
	public int createFile(String name) throws IOException {
		byte[] buffer = new byte[DataPageSize];
		int pos = 0;
		int k = 0;
		if(name == null)
			return 0;
		
		f.openFile(name);
		
		for(int index = 0; index < f.getNumberOfPages(); index++) {
			
			f.readBlock(index, buffer);
			performanceCounter[0] += 1;
			
			ByteArrayInputStream bin = new ByteArrayInputStream(buffer);
			DataInputStream din = new DataInputStream(bin);
			
			for(int j = k; j < DataPageSize/DataNodeSize + k; j++) {
				
				int key = din.readInt();
				
				dataNodes[index + j] = new DataNode(key);
				
				byte[] bytesForDataString = new byte[28];
				
				din.read(bytesForDataString, 0, 28);
				String info = new String(bytesForDataString);
				
				((DataNode)dataNodes[index + j]).setName(info); //setting the name of the node. We put the string we read from page
			}
			k += DataPageSize/DataNodeSize - 1; 				//moving index + 3 times so to not overwrite array[] positions 
			din.close();
			bin.close();
		}
		f.closeFile();
		
		Arrays.sort(dataNodes);    //Sorting node array by key.
		
		byte[] node = new byte[DataNodeSize];
		
		f.createFile(name+"Sorted");
		for(int index = 0; index < dataNodes.length; index++) {
			
			node = dataNodes[index].makeByteArray();
			System.arraycopy(node, 0, buffer, pos, node.length);
			pos += node.length;
			
			if(pos == DataPageSize) {
				this.pageOfNode += 1;
				f.appendBlock(buffer);
				performanceCounter[0] += 1;
				pos = 0;
			}
		}
		f.closeFile();
		return 1;
	}
	
	
	/**
	 *	CreateIndexFile will do the same work but based on IndexNode Files.
	 * 	 1. Read
	 *	 2. Sort
	 *   3. Store
	 *   And we end up with a new file that contains sorted(by key) index nodes.
	 */
	@Override
	public int createIndexFile(String name) throws IOException {
		
		byte[] buffer = new byte[DataPageSize];
		byte[] index_size = new byte[IndexNodeSize];
		
		int pos = 0;
		int k = 0;
		
		if(name == null)
			return 0;
		
		f.openFile(name);
		for(int index = 0; index < f.getNumberOfPages(); index++) {
			
			f.readBlock(index, buffer);
			performanceCounter[1] += 1;
			
			ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
			DataInputStream din = new DataInputStream(bis);
			
			for(int i = k; i < DataPageSize/IndexNodeSize + k; i++) {
				int key = din.readInt();
				int page = din.readInt();
				indexNodes[index + i] = new IndexNode(key, page);
			}
			k += DataPageSize/IndexNodeSize - 1;
			din.close();
			bis.close();
		}
		f.closeFile();
		
		Arrays.sort(indexNodes);  //Sorting node array by key.
		
		f.createFile(name+"Sorted"); 
		for(int j = 0; j < indexNodes.length; j++) {
			
			index_size = indexNodes[j].makeByteArrayFromIndexNode();
			
			System.arraycopy(index_size, 0, buffer, pos, index_size.length);
			
			pos += index_size.length;
			
			if(pos == DataPageSize) {
				f.appendBlock(buffer);
				performanceCounter[1] += 1;
				pos = 0;
			}
		}
		f.closeFile();
		return 1;
	}
	
	
	/**
	 * @returns a random number from existing keys
	 */
	public int getArrayRandomKey() {
		Random random = new Random();
		return dataNodes[random.nextInt(dataNodes.length)].getKey();
	}
	
}
