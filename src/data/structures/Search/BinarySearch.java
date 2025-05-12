package data.structures.Search;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import data.structure.Search;
import data.structures.FileManager.FileManager;

public class BinarySearch implements Search{

	public BinarySearch() {
		counter[0] = 0;				//Declaring counters for counting disk accesses
		counter[1] = 0;				//counter[0] is for search with only DataNodes while
									//counter[1] is for search using IndexNodes 
	}
	
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
	 * @param datafile sorted file made by C
	 * @param leftPage	always 0 (start page of file)
	 * @param rightPage number of pages of the file
	 * @param key random generated, key to search
	 * @return 1 if node key found
	 * @throws IOException
	 */
	public int search(String datafile, int leftPage ,int rightPage, int key) throws IOException {
		
		if(datafile == null)
			return 0;			//check for empty name
		
		byte[] buffer = new byte[DataPageSize];
		FileManager f = new FileManager();
		
		f.openFile(datafile);				//openSortedFile made with C and perform binary search
		if(rightPage >= leftPage) {
			
			int mid = leftPage + (rightPage - leftPage) / 2; 
			
			f.readBlock(mid, buffer);					//search middle page	
			counter[0] += 1;
			
			ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
			DataInputStream din = new DataInputStream(bis);
			
			for (int i = 0; i < DataPageSize/NodeSize; i++) {
				
				int node_key = din.readInt();				//for loop to find if 1 of 4 keys in the page match with key
				
				if(node_key == key) {
					f.closeFile();							//return 1 if found
					return 1;
				}
				
				byte[] bytesForDataString = new byte[28];
				din.read(bytesForDataString, 0, 28);		//move file pointer to next node
				
				if(node_key > key) {
					return search(datafile, leftPage, mid - 1, key);
				}
				else if(i == (DataPageSize/NodeSize - 1) && node_key < key){		//if searched all 4 nodes
					return search(datafile,  mid + 1 , rightPage, key);
				}
			}
			System.out.println("Binary Search Error");
			din.close();
			bis.close();
		}
		return 0;
	}
	
		
	/**
	 * @param datafile sorted file made by A
	 * @param indexfile sorted index file made by D
	 * @param leftPage	always 0 (start page of file)
	 * @param rightPage number of pages of the file
	 * @param key random generated, key to search
	 * @return 1 if node key found
	 * @throws IOException
	 */
	public int searchUsingIndex(String datafile, String indexfile, int leftPage, int rightPage, int key) throws IOException {
			
		if(datafile == null || indexfile == null)
			return 0;								//check for empty name
		
		byte[] buffer = new byte[DataPageSize];
		byte[] node_data = new byte[DataPageSize];
		FileManager f = new FileManager();
		
		f.openFile(indexfile);			//openSortedIndexFile made with D and perform binary search
		
		if(rightPage >= leftPage) {
			
			int mid = leftPage + (rightPage - leftPage) / 2;
			
			f.readBlock(mid, buffer);			//search middle page
			counter[1] += 1;					//increase counter, read causes 1 disk access
			
			ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
			DataInputStream din = new DataInputStream(bis);
	
			for(int index = 0; index < DataPageSize/IndexNodeSize; index++) {	//for loop to find if 1 of 16 keys in the page match with key
				
				int index_node_key = din.readInt();
				
				if(index_node_key == key) {		//if found, we take the page of the node and search on the other file on that page.
					
					int page = din.readInt();
					f.closeFile();
					
					f.openFile(datafile);		//open file made by A.
						
					f.readBlock(page, node_data);		
					counter[1] += 1;
					
					ByteArrayInputStream bs = new ByteArrayInputStream(node_data);
					DataInputStream dn = new DataInputStream(bs);
					
					for(int node = 0; node < DataPageSize/NodeSize; node++) {
						
						int DataNodeKey = dn.readInt();
						
						if(DataNodeKey == key) {
							f.closeFile();
							return 1;
						}
						
						byte[] bytesForDataString = new byte[28];
						dn.read(bytesForDataString, 0, 28);		//move file pointer to next node
					
					}	
					dn.close();
					bs.close();
					System.out.println("Reading Failed!");
					return 0;
				}
				if(index_node_key > key) {
					
					return searchUsingIndex(datafile, indexfile, leftPage,  mid - 1, key);
				}	
				else if(index == (DataPageSize/IndexNodeSize - 1) && index_node_key < key){		//if searched all 16 nodes
					
					return searchUsingIndex(datafile, indexfile, mid + 1, rightPage, key);
				}
				din.readInt(); // if index_node_key != key then move to nextNode
			}
			System.out.println("Binary Search Error");
			din.close();
			bis.close();
		}
		return 0;
	}	
}
