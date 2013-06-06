package ic2005_3;

import java.util.ArrayList;

public class FlyOnPolyhedron {
	static int[][] TETRAHEDR_FACES = {
		{0,1,2}, {1,2,3}, {0,2,3}, {0,1,3}
	};
	static int[][] OCTAHEDR_FACES = {
		{0,1,2}, {0,2,3}, {0,3,4}, {0,1,4},
		{1,2,5}, {2,3,5}, {3,4,5}, {1,4,5}
	};
	static int[][] ICOSAHEDR_FACES = {
		{0,1,2}, {0,2,3}, {0,3,4}, {0,4,5}, {0,1,5},
		{1,2,9}, {2,3,10}, {3,4,6}, {4,5,7}, {1,5,8},
		{1,8,9}, {2,9,10}, {3,6,10}, {4,6,7}, {5,7,8},
		{6,7,11}, {7,8,11}, {8,9,11}, {9,10,11}, {6,10,11},
	};

	int[][] faces = ICOSAHEDR_FACES;

	int[] usedFaces = new int[faces.length];
	
	static double H = Math.sqrt(3d) / 2d;
	
	double cosinus = 0.5d;
	
	Triangle[] triangles;
	
	int getAdjacentFace(int faceIndex, int vertexC) {
		for (int i = 0; i < faces.length; i++) {
			if(isAdjacent(faceIndex, vertexC, i)) return i;
		}
		return -1;
	}
	
	boolean isAdjacent(int faceIndex1, int vertexC, int faceIndex2) {
		if(faceIndex2 == faceIndex1) return false;
		for (int i = 0; i < faces[faceIndex1].length; i++) {
			int v = faces[faceIndex1][i];
			if(v != vertexC && getVertexIndex(faceIndex2, v) < 0) return false;
		}
		return true;
	}
	
	int getVertexIndex(int faceIndex, int vertex) {
		for (int i = 0; i < faces[faceIndex].length; i++) {
			if(faces[faceIndex][i] == vertex) return i;
		}
		return -1;
	}
	
	Triangle getInitialTriangle() {
		Triangle t = new Triangle();
		t.faceIndex = 0;
		t.cVertexIndex = 2;
		t.points[0] = new Vector(0.5d, 0d);
		t.points[1] = new Vector(-0.5d, 0d);
		t.points[2] = new Vector(0d, H);
		return t;
	}
	
	Triangle getNextTriangle(Triangle t) {
		double[] cs = new double[3];
		for (int i = 0; i < 3; i++) {
			cs[i] = t.points[i].cos();
		}
		double csc = cs[t.cVertexIndex];
		int aVertexIndex = -1;
		if(csc > cosinus) {
			for (int i = 0; i < 3; i++) {
				if(i != t.cVertexIndex && cs[i] > cosinus) aVertexIndex = i;
			}
		} else {
			for (int i = 0; i < 3; i++) {
				if(i != t.cVertexIndex && cs[i] < cosinus) aVertexIndex = i;
			}
		}
		if(aVertexIndex < 0) {
			throw new RuntimeException("fuck 1");
		}
		int aVertex = faces[t.faceIndex][aVertexIndex];
		int nextFace = getAdjacentFace(t.faceIndex, aVertex);
		if(nextFace < 0) {
			throw new RuntimeException("fuck 2");
		}
		int ncVertexIndex = -1;
		for (int i = 0; i < 3; i++) {
			if(getVertexIndex(t.faceIndex, faces[nextFace][i]) < 0) ncVertexIndex = i;
		}
		if(ncVertexIndex < 0) {
			throw new RuntimeException("Cannot find next vertex");
		}

		Triangle t2 = new Triangle();
		t2.faceIndex = nextFace;
		t2.cVertexIndex = ncVertexIndex;
		Vector cv = new Vector(0d, 0d);
		for (int i = 0; i < 3; i++) {
			if(i != aVertexIndex) {
				cv.x += t.points[i].x;
				cv.y += t.points[i].y;
			} else {
				cv.x -= t.points[i].x;
				cv.y -= t.points[i].y;
			}
		}
		for (int i = 0; i < 3; i++) {
			int vi = getVertexIndex(nextFace, faces[t.faceIndex][i]);
			if(vi >= 0) {
				t2.points[vi] = t.points[i];
			}
		}
		t2.points[t2.cVertexIndex] = cv;
		for (int i = 0; i < 3; i++) {
			if(t2.points[i] == null) throw new RuntimeException("Vector not assigned");
		}		
		return t2;
	}
	
	int facesCount;
	
	public double run() {
		facesCount = 1;
		usedFaces = new int[faces.length];
		Triangle t = getInitialTriangle();
		usedFaces[t.faceIndex] = 1;
		double distance = 0d;
		ArrayList l = new ArrayList();
		l.add(t);
		while(true) {
			distance = t.points[t.cVertexIndex].length();
			Triangle t1 = getNextTriangle(t);
			if(usedFaces[t1.faceIndex] == 1) break;
			usedFaces[t1.faceIndex] = 1;
			t = t1;
			l.add(t);
			facesCount++;
		}
		triangles = (Triangle[])l.toArray(new Triangle[0]);
		return distance;
		//System.out.println(cosinus + ":" + distance);
	}
	
	void printTriangles() {
		for (int i = 0; i < triangles.length; i++) {
			Triangle t = triangles[i];
			int[] tp = faces[t.faceIndex];
			System.out.println(t.faceIndex + " : " + tp[0] + " " + tp[1] + " " + tp[2]);
		}
		System.out.println();
	}
	
	public static void main(String[] args) {
//		System.out.println(Math.sqrt(109d) / 2); //cube
//		FlyOnPolyhedron p2 = new FlyOnPolyhedron();
//		p2.cosinus = 0.3593;
//		double d3 = p2.run();
//		System.out.println(d3);
		double bd = 0d;
		for (int i = 0; i < 999; i++) {
			FlyOnPolyhedron p = new FlyOnPolyhedron();
			p.cosinus = 0.000001d + 0.001d * i;
			double d = p.run();
			if(d > bd) {
				bd = d;
				System.out.println(d + " in " + p.facesCount + " at " + p.cosinus);
				p.printTriangles();
			}
		}
	}
	
}

class Vector {
	double x;
	double y;
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double length() {
		return Math.sqrt(x * x + y * y);
	}
	public double cos() {
		return x / length();
	}
}

class Triangle {
	int faceIndex;
	int cVertexIndex;
	Vector[] points = new Vector[3];
}

//A) 3.968626966596886 in 8 faces
//B) 6.763874629234338 in 14 faces [0.3593, 03973]