package data.structures.FileManager;

import java.io.*;

public class FileManager {
		
	private static final int DataPageSize = 128;	//Default data page size
	
	private String name;							//info that will be stored in fileHandle
	private int numberOfPages;				
	private int currentPosition;
	private RandomAccessFile file;
	
	public FileManager() {	
		this.file = null;							//initialize values
		this.name = null;
		this.currentPosition = 0;
		this.numberOfPages = 0;
	}
	
///////////////getters and setters/////////////////////////////
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumberOfPages() {
		return numberOfPages;
	}

	public void setNumberOfPages(int numberOfPages) {
		this.numberOfPages = numberOfPages;
	}

	public int getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(int currentPosition) {
		this.currentPosition = currentPosition;
	}

	public RandomAccessFile getFile() {
		return file;
	}

	public void setFile(RandomAccessFile file) {
		this.file = file;
	}

	public static int getDatapagesize() {
		return DataPageSize;
	}
	
///////////////////////////methods///////////////////////////////
	
	/**
	 * fileHandle keeps the variables of the class updated whenever we create or close a file.
	 * In the first 128 bytes we store the File name (40 characters), current page position of file pointer and
	 * the number of total pages of the file.
	 * @param file
	 * @param fileName
	 * @param currentPos
	 * @param num
	 * @returns 1 if success 0 otherwise
	 */
	public int fileHandle(RandomAccessFile file, String fileName, int currentPos, int num) {
		
		byte[] data = new byte[DataPageSize];
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		
		byte[] name = fileName.getBytes();				
		byte[] fixedArrayForName = new byte[40];
		System.arraycopy(name, 0, fixedArrayForName, 0, name.length);
		
		this.file = file;											//storing data
		this.name = fileName;
		this.numberOfPages = num;
		this.currentPosition = currentPos;
		
		try{
			
			dos.write(fixedArrayForName, 0, 40);
			dos.writeInt(currentPos);
			dos.writeInt(num);
			dos.close();
			
			byte[] buffer = bos.toByteArray();
			System.arraycopy(buffer, 0, data, 0, buffer.length);	//i have got the serialized data, and i'm ready to write them in file
			bos.close();
			
			file.seek(0);
			file.write(data);
			
			return 1;
		}
		catch (IOException e){
			System.out.println("File_Handle_Error");
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * Creating a new File, with name fileName for reading and writing
	 * Updating FileHandle
	 * @param fileName
	 * @return
	 */
	public int createFile(String fileName) {
		try {
			RandomAccessFile file = new RandomAccessFile(fileName, "rw");
			
			fileHandle(file, fileName, 0, 0);
			
			return 1;
		}
		catch (IOException e) {
			System.out.println("Creating_File_Failed");
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * OpenFile with name fileName for reading and writing
	 * Updating class variables
	 * @param fileName
	 * @returns the Number of total pages without taking into account FileHandle info page.
	 */
	public int openFile(String fileName) {
		
		File f = new File(fileName);
		int sizeOfFile;
		
		if(f.exists()) {											//checking if file exists
			try {
				
				this.file = new RandomAccessFile(fileName, "rw");
				this.name = fileName;
				
				sizeOfFile = (int)file.length() - DataPageSize; 	 //decrease by 128 bytes because of FileHandle info page
				this.numberOfPages = sizeOfFile/DataPageSize;
				
				return this.numberOfPages;
				
			} catch (IOException e) {
				System.out.println("Opening_File_Failed");
				e.printStackTrace();
				return 0;
			}
		}
		System.out.println("Can't find File");
		return 0;
	}
	
	/**
	 * ReadBlock has 2 parameters pos and an a byte[].
	 * This method is used to read a block of 128 (DataPageSize) bytes from file
	 * and bring them into main memory (buffer) for processing
	 * @param pos
	 * @param dataRead
	 * @return
	 */
	public int readBlock(int pos, byte[] dataRead) {
		
		byte[] buffer = new byte[DataPageSize];
		this.currentPosition = pos;
		
		ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
		DataInputStream dis = new DataInputStream(bis);
		
		try {
			if((pos + 1) * DataPageSize < file.length()) { //check if we can access this space of file
				
				file.seek((pos + 1) * DataPageSize);
				file.read(buffer);
				
				this.currentPosition = (int)file.getFilePointer()/DataPageSize - 1;  //reducing 1 because of the FileHadle
				
				dis.read(dataRead);
				dis.close();
				bis.close();
				return 1;
			}
			System.out.println("Can't read this page");
			return 0;
		}
		catch (IOException e) {
			System.out.println("Block_Reading_Failed");
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * Reading Next 128 bytes of the file, using readBlock
	 * @param dataRead
	 * @return
	 */
	public int readNextBlock(byte[] dataRead) {
		
		int result = readBlock(currentPosition, dataRead);
		
		if(result == 1)
			return 1;
		
		System.out.println("Reading_Next_Block_Failed");
		return 0;
	}
	
	/**
	 * WriteBlock has 2 parameters pos and an a byte[].
	 * This method is used to write a block of 128 (DataPageSize) bytes to a page in file.
	 * @param pos
	 * @param writeData
	 * @return
	 */
	public int writeBlock(int pos, byte[] writeData) { /**Add Counter for accessing disk**/
		
		byte[] data = new byte[DataPageSize];
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		
		try{
			if((pos + 1) * DataPageSize <= file.length() + DataPageSize) {
				
				dos.write(writeData, 0, writeData.length); 		//writing data
				dos.close();
				
				byte[] buffer = bos.toByteArray();
				System.arraycopy(buffer, 0, data, 0, buffer.length);
				bos.close();
				
				file.seek(pos*DataPageSize);
				file.write(data);
				
				this.currentPosition =(int)file.getFilePointer()/DataPageSize - 1;
				
				return 1;
			}
			System.out.println("Can't place pointer there!");
			return 0;
		}
		catch(IOException e) {
			System.out.println("Writing_Block_Failed");
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * Writing the next 128 bytes of the file using WriteBlock.
	 * @param writeData
	 * @return
	 */
	public int writeBlockNext(byte[] writeData) {
		
		int result = writeBlock(currentPosition, writeData);
		
		if(result == 1)
			return 1;
			
		System.out.println("Writing_Next_Block_Failed");
		return 0;
	}
	
	/**
	 * AppendBlock writes data from buffer at the end of the file
	 * Number of pages increased
	 * @param writeData
	 * @return
	 */
	public int appendBlock(byte[] writeData) {
		this.numberOfPages += 1;							//increase number of pages
		int result = writeBlock(numberOfPages, writeData);	//write at the end of the file
		
		if(result == 1)
			return 1;
		
		System.out.println("Appending_Block_Failed");
		return 0;
		
	}
	
	/**
	 * DeleteBlock() deletes a specific block of 128 bytes from file.
	 * @param pos
	 * @return
	 */
	public int deleteBlock(int pos) {
		
		byte[] buffer = new byte[DataPageSize];
		
		try {
			file.seek(numberOfPages * DataPageSize);
			file.read(buffer);
			
			writeBlock(pos+ 1, buffer);
				
			this.numberOfPages--;
			
			return 1;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * closeFile() closes an open file and updates File Handle Info.
	 * @return
	 */
	public int closeFile() {
		fileHandle(this.getFile(), this.getName(), this.getCurrentPosition(), this.getNumberOfPages());
		try {
			this.file.close();
			return 1;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}	
	
	public void printFileHanldeInfo() {
		System.out.println("================FileHandle================");
		System.out.println("File: "+ this.name);
		System.out.println("Pointer Position: "+ this.currentPosition);
		System.out.println("Pages: " + this.numberOfPages);
		System.out.println("================FileHandle================");
	}

}
