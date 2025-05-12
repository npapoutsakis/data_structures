package data.structures.Search;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Random;

import data.structure.Search;
import data.structures.FileManager.FileManager;

public class ArraySearch implements Search {
	
	public ArraySearch() {
		counter[0] = 0;			//Declaring counters for counting disk accesses
		counter[1] = 0;			//counter[0] is for search with only DataNodes while
	}							//counter[1] is for search using IndexNodes
	 
	
///////////set-get for class variables///////////
	
	public int getCounterSearch() {
		return counter[0];
	}
		
	public int getCounterUsingIndex() {
		return counter[1];
	}
	
	public void resetCounterSearch() {
		counter[0] = 0;
	}
	
	public void resetCounterUsingIndex() {
		counter[1] = 0;
	}
	
//////////////////////methods//////////////////////
	
	/**
	 * @param name sorted file made by A
	 * @param key random generated, key to search
	 * @return 1 if node key found
	 * @throws IOException
	 */
	public int search(String name, int key) throws IOException {
		if(name == null) {
			return 0;		//check for empty name
		}
		
		byte[] buffer = new byte[DataPageSize];
		FileManager f = new FileManager();
		f.openFile(name);
		
		for(int arrayIndex = 0; arrayIndex < f.getNumberOfPages(); arrayIndex++) {
			
			f.readBlock(arrayIndex, buffer);		//Begin Array Search from 1st page of the file until i find key
			counter[0] += 1;						//while counting disk accesses each time i seek in file
			
			ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
			DataInputStream din = new DataInputStream(bis);
			
			for (int i = 0; i < DataPageSize/NodeSize; i++) {		//for loop to find if 1 of 4 keys in the page match with key
								
				int node_key = din.readInt();
													
				if(node_key == key) {
					f.closeFile();
					return 1;
				}
				
				byte[] bytesForDataString = new byte[28];
				din.read(bytesForDataString, 0, 28);		//move file pointer to next node
			}
			din.close();
			bis.close();
		}
		System.out.println("Not Found!");
		return 0;
	}
	
	
	/**
	 * Search a file using an index file.
	 * @param fileName the name of the file to search
	 * @param indexFileName the name of the index file
	 * @param key the key to search
	 * @return 1 if the key is found, 0 otherwise
	 * @throws IOException
	 */
	public int searchUsingIndex(String fileName, String indexFileName, int key) throws IOException {
		
		if(fileName == null || indexFileName == null) {
			return 0;			//check for empty names
		}					
		
		byte[] buffer = new byte[DataPageSize];
		int node_key, node_page;
		
		FileManager f = new FileManager();
		f.openFile(indexFileName);				//opening Index File			 
		
		for(int index = 0; index < f.getNumberOfPages(); index++) {		//search all pages and find key, then read page and read on the other file on that page
			
			f.readBlock(index, buffer);			//Begin Array Search from 1st page of the file until i find key
			counter[1] += 1;					//while counting disk accesses each time i seek in file
			
			ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
			DataInputStream din = new DataInputStream(bis);
			
			for(int i=0; i < DataPageSize/IndexNodeSize; i++) {
				node_key = din.readInt();
				
				if(node_key == key) {				//for loop to find if 1 of 16 keys in the page match with key
													//if found 
					node_page = din.readInt();		//read node page 
					f.closeFile();					//close file 
					
					f.openFile(fileName);					//open data file
					f.readBlock(node_page, buffer);			//read on page with position node_page from the start
					counter[1] += 1;
					
					ByteArrayInputStream bs = new ByteArrayInputStream(buffer);
					DataInputStream dn = new DataInputStream(bs);
					
					for(int node = 0; node < DataPageSize/NodeSize; node++) {		//for loop to find if 1 of 4 keys in the page match with key
						int DataNodeKey = dn.readInt();
						
						if(DataNodeKey == key) {			//if found return 1; (Success)
							f.closeFile();
							return 1;
						}
						
						byte[] bytesForDataString = new byte[28];		//else read the next 28bytes and
						dn.read(bytesForDataString, 0, 28);				//move file pointer to next node
					}	
					dn.close();
					bs.close();
					System.out.println("Reading Failed!");
					return 0;
				}
				din.readInt();				//if node key not found in index file then read int(the page of the node) 
			}								//and move file pointer to next index node
			din.close();
			bis.close();
		}
		return 0;
	}
	
	/**
	 * Method used to give an already random existing key
	 * @param keys
	 * @return
	 */
	public int getRandomExistingKey(int[] keys) {
		Random random = new Random();
		
		int randomKey = keys[random.nextInt(keys.length)];
				
		return randomKey;
	}
	
}
