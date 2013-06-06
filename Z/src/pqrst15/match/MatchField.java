package pqrst15.match;

import java.util.ArrayList;

public class MatchField {
	int width;
	int height;
	int size;
	
	int[] x,y;
	int[][] xy;
	
	int length;
	int[] form;

	int nodeSize;
	int[] indexToNode;
	int[] nodeToIndex;

	int[][] jp; // for nodes
	
	Node[] nodes;
	Match[] matchs;

	public MatchField() {}
	
	public void setSize(int width) {
		this.width = width;
		this.height = width;
		length = width / 2;
		size = width * height;
		x = new int[size];
		y = new int[size];
		xy = new int[width][height];
		for (int i = 0; i < size; i++) {
			x[i] = i % width;
			y[i] = i / width;
			xy[x[i]][y[i]] = i;
		}
		form = new int[size];
		
		nodeSize = 0;
		for (int i = 0; i < size; i++) {
			if(Math.abs(x[i] - y[i]) > length) {
				form[i] = 0;
			} else {
				form[i] = 1;
				nodeSize++;
			}
		}
		indexToNode = new int[size];
		nodeToIndex = new int[nodeSize];
		int k = 0;
		for (int i = 0; i < size; i++) {
			if(form[i] == 1) {
				nodeToIndex[k] = i;
				indexToNode[i] = k;
				++k;
			} else {
				indexToNode[i] = -1;
			}
		}
		jp = new int[6][nodeSize];
		for (int i = 0; i < size; i++) {
			int n = indexToNode[i];
			if(n < 0) continue;
			for (int d = 0; d < 6; d++) {
				int i2 = jumpIndex(i, VECTORS[d][0], VECTORS[d][1]);
				jp[d][n] = (i2 < 0) ? -1 : indexToNode[i2];
			}
		}
		
		nodes = new Node[nodeSize];
		for (int n = 0; n < nodeSize; n++) {
			nodes[n] = new Node(n, nodeToIndex[n]);
		}
		ArrayList ms = new ArrayList();
		for (int n = 0; n < nodeSize; n++) {
			for (int d = 0; d < 6; d++) {
				int n2 = jp[d][n];
				if(n2 < n) continue;
				Match m = new Match(ms.size(), nodes[n], nodes[n2]);
				ms.add(m);
			}
		}
		matchs = (Match[])ms.toArray(new Match[0]);
		for (int n = 0; n < nodeSize; n++) {
			ms = new ArrayList();
			for (int i = 0; i < matchs.length; i++) {
				if(matchs[i].node1.number == n || matchs[i].node2.number == n) {
					ms.add(matchs[i]);
				}
			}
			nodes[n].matchs = (Match[])ms.toArray(new Match[0]);
		}
	}
	
	static int[][] VECTORS = {{1,0},{1,1},{0,1},{-1,0},{-1,-1},{0,-1}};

	int jumpIndex(int p, int dx, int dy) {
		int ix = x[p] + dx;
		int iy = y[p] + dy;
		if(ix < 0 || ix >= width || iy < 0 || iy >= height) return -1;
		return xy[ix][iy];
	}

}
