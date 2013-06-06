package champ2006;

import com.slava.common.RectangularField;

public class SegmentedLineRunner {
	static int[] FIRST_FORM = {
		1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,0,
	};

	static int[] SECOND_FORM = {
		0,0,0,0,0,0,0,0,0,0,1,
		0,0,0,0,0,0,0,0,0,0,1,
		1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,
		1,1,1,1,1,1,1,1,1,1,1,
		0,1,1,1,1,1,1,1,1,1,1,
	};

	static void runFirstPart() {
		PathSegmentBuilder segments = new PathSegmentBuilder();
		segments.build(1, 4);
		//segments.printSegments();
		
		RectangularField f = new RectangularField();
		f.setSize(11, 5);
		SegmentedLinePaker paker = new SegmentedLinePaker();
		paker.setField(f);
		paker.setForm(FIRST_FORM);
		paker.setSegments(segments.segments);
		int start = 0;
		int end = f.getIndex(10, 3);
		paker.setStartAndEnd(start, end);
		paker.solve();
	}
	
	static void runSecondPart() {
		PathSegmentBuilder segments = new PathSegmentBuilder();
		segments.build(5, 5);
		//segments.printSegments();
		
		RectangularField f = new RectangularField();
		f.setSize(11, 7);
		SegmentedLinePaker paker = new SegmentedLinePaker();
		paker.setField(f);
		paker.setForm(SECOND_FORM);
		paker.setSegments(segments.segments);
		int start = f.getIndex(10, 0);
		int end = f.getIndex(0, 5);
		paker.setStartAndEnd(start, end);
		paker.solve();
	}

	public static void main(String[] args) {
		//runFirstPart();
		runSecondPart();
	}

/*
 a b c c d d d m m n n
 h h h e e e e l m n o
 h g g f f l l l p o o
 i g g f f k k p p q q
 i i i j j j j p q q +
 
 + + + + + + + + + + a
 + + + + + + + + + + b
 f f f f e e e d d d b
 f g g e e i i i j d b
 g g g h h i i j j d b
 l l l k h h h j c c b
 + l l k k k k j c c c
 
 */
}
