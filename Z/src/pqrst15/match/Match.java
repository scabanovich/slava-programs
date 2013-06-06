package pqrst15.match;

public class Match {
	int number;
	Node node1;
	Node node2;

	int burningTime = -1;

	public Match(int number, Node n1, Node n2) {
		this.number = number;
		node1 = n1;
		node2 = n2;
	}

}
