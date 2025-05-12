package data.structures.Nodes;

public class IndexNode extends SimpleNode{
	
	private int index;			//index shows the page of the original DataNode
	
	public IndexNode(int key, int index) {
		super(key);
		this.index = index;
	}
///////////set-get for class variables///////////
	public int getIndex() {
		return index;
	}
}
	