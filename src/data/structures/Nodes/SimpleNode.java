package data.structures.Nodes;

import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

import data.structure.Node;

public class SimpleNode implements Node {
	/**
	 * The key of the node
	 */
	private int key;

	/**
	 * The class constructor
	 * 
	 * @param item the value for the key of the node
	 */
	public SimpleNode(int item) {
		key = item;
	}

///////////set-get for class variables///////////
	
	/**	
	 * Returns the key of the Node.
	 * 
	 * @return the key
	 */
	public int getKey() {
		return this.key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	
////////////////methods////////////////
	@Override
	public int compareTo(Node otherNode) {
		if (this.getKey() == otherNode.getKey())
			return 0; // this == otherNode
		else if (this.getKey() > otherNode.getKey())
			return 1; // this > otherNode
		else
			return -1; // this < otherNode
	}
	
	/**
	 * 	makeByteArrayFromIndexNode and makeByteArray take a Node and 
	 *  convert him into a byte[] array
	 */
	public byte[] makeByteArrayFromIndexNode() {
		java.nio.ByteBuffer bb = java.nio.ByteBuffer.allocate(8); // allocate 8 bytes for the output
		bb.order(ByteOrder.BIG_ENDIAN);
		
		bb.putInt(this.getKey());
		bb.putInt(((IndexNode)this).getIndex());
		byte[] result =  bb.array();
		
		return result;
	}

	public byte[] makeByteArray() {
		java.nio.ByteBuffer bb = java.nio.ByteBuffer.allocate(32); // allocate 32 bytes for the output
		bb.order(ByteOrder.BIG_ENDIAN);
		
		bb.putInt(this.getKey());
		bb.put(((DataNode)this).getName().getBytes(StandardCharsets.US_ASCII));
		byte[] result =  bb.array();
		
		return result;
	}

}
