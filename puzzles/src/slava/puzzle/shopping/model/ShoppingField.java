package slava.puzzle.shopping.model;

public class ShoppingField {
	int width;
	int height;
	int size;
	int[] x;
	int[] y;
	int[][] jp;
	Node[] nodes;
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		size = width * height;
		x = new int[size];
		y = new int[size];
		for (int i = 0; i < size; i++) {
			x[i] = (i % width);
			y[i] = (i / width);
		}
		jp = new int[4][size];
		for (int i = 0; i < size; i++) {
			jp[0][i] = (x[i] == width - 1) ? -1 : i + 1;
			jp[1][i] = (y[i] == height - 1) ? -1 : i + width;
			jp[2][i] = (x[i] == 0) ? -1 : i - 1;
			jp[3][i] = (y[i] == 0) ? -1 : i - width;
		}
		nodes = new Node[size];
		for (int i = 0; i < size; i++) {
			nodes[i] = new Node(i);
			nodes[i].setEnabled(false);
		}
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getSize() {
		return size;
	}
	
	public int x(int i) {
		return (i < 0 || i >= size) ? -1 : x[i];
	}

	public int y(int i) {
		return (i < 0 || i >= size) ? -1 : y[i];
	}
	
	public Node getNeighbour(Node n, int d) {
		int i = n.getId();
		i = jp[d][i];
		return (i < 0) ? null : nodes[i]; 
	}
	
	public Node getNode(int i) {
		return (i < 0 || i >= size) ? null : nodes[i];
	}
	
	public Node[] getNodes() {
		return nodes;
	}

}
