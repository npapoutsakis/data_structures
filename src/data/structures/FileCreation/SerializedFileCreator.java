package data.structures.FileCreation;

import java.util.Random;
import data.structure.FileCreator;
import data.structures.FileManager.*;
import data.structures.Nodes.DataNode;
import data.structures.Nodes.IndexNode;
import data.structures.Nodes.SimpleNode;
/**
 * SerializedFileCreator implements FileCreator and creates serialized files of
 * type A and B.
 * @author Nick-PC
 *
 */
public class SerializedFileCreator implements FileCreator {
	
	private SimpleNode[] dataNodes;
	private IndexNode[] indexNodes;
	
	private int[] keys;
	
	private FileManager f = null;
	private int pageOfNode = 0;
	
	public SerializedFileCreator() {
		dataNodes = createDataNodes();
		indexNodes = new IndexNode[numberOfNodes];		
		f = new FileManager();
	}	
	
/////////////////////set-get/////////////////////////////
	public FileManager getFileManager() {
		return this.f;
	}
	
	public int[] getKeys() {
		return keys;
	}

	public 	SimpleNode[] getDataNodes() {
		return this.dataNodes;
	}

	public IndexNode[] getIndexNodes() {
		return indexNodes;
	}

	public int getPageOfNodes() {
		return this.pageOfNode;
	}

/////////////////////methods/////////////////////
	/**
	 * Creating file with random nodes. 
	 * Also calculating index node and store them in an array
	 */
	@Override
	public int createFile(String name) {
		byte[] buffer = new byte[DataNodeSize];
		byte[] result = new byte[DataPageSize];
		int pos = 0;
		
		if(name == null)
			return 0;
		
		f.createFile(name);
		for(int i=0; i < dataNodes.length; i++) {
			
			buffer = dataNodes[i].makeByteArray();
			
			indexNodes[i] = new IndexNode(dataNodes[i].getKey(), pageOfNode);
			
			System.arraycopy(buffer, 0, result, pos, buffer.length);
			pos += buffer.length;
			
			if(pos == DataPageSize) {
				this.pageOfNode += 1;
				f.appendBlock(result);
				pos = 0;
			}
		}
		f.closeFile();
		return 1;
	}
	
	/**
	 * Creating Index File 
	 * @fileName is the name of the indexFile.
	 */
	@Override
	public int createIndexFile(String fileName) {
		
		byte[] buffer = new byte[IndexNodeSize];
		byte[] result = new byte[DataPageSize];
		int pos = 0;
		
		if(fileName == null)
			return 0;
		
		f.createFile(fileName);
		for(int j = 0; j < indexNodes.length; j++) {
			
			buffer = indexNodes[j].makeByteArrayFromIndexNode();
			
			System.arraycopy(buffer, 0, result, pos, buffer.length);
			
			pos += buffer.length;
			
			if(pos == DataPageSize) {
				f.appendBlock(result);
				pos = 0;
			}
		}
		f.closeFile();
		return 1;
	}
	
	
	/**
	 * This function create Random nodes with keys form 1 to 10^6 + 1.
	 * @returns the array of nodes created
	 */ 
	private SimpleNode[] createDataNodes() {
		SimpleNode[] random_nodes = new SimpleNode[numberOfNodes];
		Random randomGenerator = new Random();
		
		int START_INT = 1;
		int END_INT = 1000001;
		
		int[] randomInts = randomGenerator.ints(START_INT, END_INT).distinct().limit(numberOfNodes).toArray();
		
		this.keys = randomInts;
		
		for (int countRandom = 0; countRandom < numberOfNodes; countRandom++) {
			random_nodes[countRandom] = new DataNode(randomInts[countRandom]);
		}
		return random_nodes;
	}
	
	/**
	 * @returns the already existing keys. This method will help us in search.
	 */
	public int[] getArrayRandomInts() {
		return this.keys;
	}
}
