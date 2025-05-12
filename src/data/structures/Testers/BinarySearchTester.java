package data.structures.Testers;

import java.io.IOException;
import data.structure.Tester;
import data.structures.FileCreation.SortedFileCreator;
import data.structures.Search.BinarySearch;

public class BinarySearchTester implements Tester{
	
	private int average;
	private String FileName;				//File Name made by A
	private String IndexFileName;			//Index File Name made by B
	private int[] performanceCounters;		//Counters that will count disk accesses of External Sorting Algorithm
	
	public BinarySearchTester(String FileName, String indexFile) {
		this.FileName = FileName;
		this.IndexFileName = indexFile;
		this.performanceCounters = new int[2];
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
	
	public void setPerformanceC(int performance) {
		this.performanceCounters[0] = performance;
	}
	
	public void setPerformanceD(int performance) {
		this.performanceCounters[1] = performance;
	}
	
	public int getPerformanceCountersC() {
		return this.performanceCounters[0];
	}
	
	public int getPerformanceCountersD() {
		return this.performanceCounters[1];
	}
	
//////////////////////methods//////////////////////
	
	/**
	 * doTest() method will make 20 searches and count average
	 * @throws IOException
	 */
	@Override
	public void doTest() throws IOException {
		BinarySearch binaryTest = new BinarySearch();
		SortedFileCreator sortFile = new SortedFileCreator();
		
		System.out.println("Searching files made by C:");
		sortFile.createFile(FileName);							//Creating sort data file
		
		setPerformanceC(sortFile.getPerformanceCounterC());		//setting PerformanceCounterC
			
		int total_disk_accesses = 0;
		
		for(int index = 0; index < searchNumber; index++) {		//searching 20 times and take average of disk accesses
			
			binaryTest.search(sortFile.getFileManager().getName(), 0, sortFile.getFileManager().getNumberOfPages() - 1 , sortFile.getArrayRandomKey());
			
			total_disk_accesses += binaryTest.getCounterSearch();
			
			binaryTest.resetCounterSearch();
		}
		
		average = total_disk_accesses / searchNumber;
		
		System.out.println("Disk Access Average: "+ average +" times");
		System.out.println();
		average = 0; 
		
		System.out.println("Searching files made by D: ");
		sortFile.createIndexFile(IndexFileName);				//Creating sort index file
		
		setPerformanceD(sortFile.getPerformanceCounterD());		//setting PerformanceCounterC
		
		total_disk_accesses = 0;
		
		for(int index = 0; index < searchNumber; index++) {		//searching 20 times and take average of disk accesses
			
			binaryTest.searchUsingIndex(FileName, sortFile.getFileManager().getName(), 0, sortFile.getFileManager().getNumberOfPages() - 1, sortFile.getArrayRandomKey());
			
			total_disk_accesses += binaryTest.getCounterUsingIndex();
			
			binaryTest.resetCounterUsingIndex();
		}
		
		average = total_disk_accesses / searchNumber;
		
		System.out.println("Disk Access Average: "+ average +" times");
		System.out.println();
		
		return;
	}
	
}
