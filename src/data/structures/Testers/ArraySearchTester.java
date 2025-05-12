package data.structures.Testers;

import java.io.IOException;
import data.structure.Tester;
import data.structures.FileCreation.SerializedFileCreator;
import data.structures.Search.ArraySearch;

public class ArraySearchTester implements Tester{
	
	private int average;
	private String FileName;			//File Name made by A
	private String IndexFileName;		//Index File Name made by B
	
	public ArraySearchTester(String fileName, String IndexFileName) {
		this.FileName = fileName;
		this.IndexFileName = IndexFileName;
	}
	
///////////set-get for class variables///////////	
	
	public int getAverage() {
		return this.average;
	}
	
	public String getFileName() {
		return this.FileName;
	}
	
	public String getIndexFileName() {
		return IndexFileName;
	}
	
//////////////////////methods//////////////////////
	
	/**
	 * doTest() method will make 20 searches and count average
	 * @throws IOException
	 */
	@Override
	public void doTest() throws IOException {
		ArraySearch arrayTest = new ArraySearch();
		SerializedFileCreator serFile = new SerializedFileCreator();
		
		System.out.println("Searching files made by A: ");
		serFile.createFile(FileName);					//Creating serialized data file with random nodes
		
		int total_disk_accesses = 0;
		for(int index = 0; index < searchNumber; index++) {		//searching 20 times and take average of disk accesses
			
			arrayTest.search(FileName, arrayTest.getRandomExistingKey(serFile.getKeys()));
			
			total_disk_accesses += arrayTest.getCounterSearch();
			
			arrayTest.resetCounterSearch();
		}
		
		average = total_disk_accesses / searchNumber;
		
		System.out.println("Disk Access Average: "+ average +" times");
		System.out.println();
		
		System.out.println("Searching files made by B: ");
		serFile.createIndexFile(IndexFileName);			//Creating serialized index file(not sorted)
				
		total_disk_accesses = 0;
		for(int index = 0; index < searchNumber; index++) {		//searching 20 times and take average of disk accesses
			
			arrayTest.searchUsingIndex(FileName, IndexFileName, arrayTest.getRandomExistingKey(serFile.getKeys()));
			
			total_disk_accesses += arrayTest.getCounterUsingIndex();
			
			arrayTest.resetCounterUsingIndex();
		}
		
		average = total_disk_accesses / searchNumber;
		
		System.out.println("Disk Access Average: "+ average +" times");
		System.out.println();
		
		arrayTest.resetCounterSearch();
		arrayTest.resetCounterUsingIndex();
		
		return;
	}
	
}
