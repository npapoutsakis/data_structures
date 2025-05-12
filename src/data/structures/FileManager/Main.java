package data.structures.FileManager;

import java.io.IOException;

import data.structures.Testers.ArraySearchTester;
import data.structures.Testers.BinarySearchTester;

public class Main {
	
	public static final int DataPageSize = 128;
	public static final int NodeSize = 32;
	
	public static void main(String[] args) throws IOException {
			
		String fileName = "LabFileA";		
		String indexFileName = "LabIndexFile";		
		
		System.out.println("*****************************************************");
		System.out.println("----------------Array Search Test!------------------");
		System.out.println();
		
		ArraySearchTester arrayTester = new ArraySearchTester(fileName, indexFileName);
		arrayTester.doTest();
		
		System.out.println("-------------Array Search Test Succeed!-------------");
		System.out.println();
		
		System.out.println("*****************************************************");
		
		System.out.println();
		System.out.println("----------------Binary Search Test!------------------");
		System.out.println();
		
		BinarySearchTester binaryTester = new BinarySearchTester(fileName, indexFileName);
		binaryTester.doTest();
			
		System.out.println("-------------Binary Search Test Succeed!-------------");
		System.out.println();
		System.out.println("*****************************************************");
		System.out.println();
		
		System.out.println("------------External Sorting Performance!------------");
		System.out.println();
		
		System.out.println("Organization: ");
		System.out.println("	Files made by C: " + binaryTester.getPerformanceCountersC() + " times (Disk Access)");
		System.out.println("	Files made by D: " + binaryTester.getPerformanceCountersD()	+ " times (Disk Access)\n");
		
		System.out.println("*****************************************************");
		System.out.println();
		return;
	}
}
