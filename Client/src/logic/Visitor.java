package logic;

public class Visitor {
	private String id;
	//private boolean haveOrder;
	
	public Visitor() {}
	
	public Visitor(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String toString() {
		return String.format("%s\n", id);
	}
}
