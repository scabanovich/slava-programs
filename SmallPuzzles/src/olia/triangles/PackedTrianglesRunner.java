package olia.triangles;

import com.slava.common.RectangularField;

public class PackedTrianglesRunner {
	
	public static void generate(int size) {
		PackedTriangleGenerator g = new PackedTriangleGenerator();
		RectangularField f = new RectangularField();
		f.setSize(size, size);
		g.setField(f);
		g.generate();
	}
	
	public static void main(String[] args) {
		generate(7);
	}

}
