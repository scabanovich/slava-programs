package pqrst14;

import com.slava.common.RectangularField;

public class NoTouchingRunner {
	
	static int[] FILTER = new int[]{
		1,1,1,1,0,0,0,0,0,0,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,0,0,0,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,
		0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,
		0,0,0,1,1,1,1,1,1,1,1,1,1,1,0,0,0,
		0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,
		1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,0,0,
		1,1,1,1,1,1,0,1,1,1,1,1,1,1,0,0,0
	};
	
	public static void main(String[] args) {
		RectangularField field = new RectangularField();
		field.setSize(17, 16);
		NoTouching analyzer = new NoTouching();
		analyzer.setField(field);
		analyzer.setFilter(FILTER);
		analyzer.solve();		
		
	}

}
