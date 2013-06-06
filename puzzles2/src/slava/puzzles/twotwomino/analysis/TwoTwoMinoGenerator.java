package slava.puzzles.twotwomino.analysis;

import java.util.*;
import slava.puzzles.twotwomino.model.*;

public class TwoTwoMinoGenerator {
	TwoTwoMinoField field;
	TwoTwoMinoDoubleField doubleField;
	int[][][] figures;
	TwoTwoMinoPacking packing = new TwoTwoMinoPacking();
	int[] restrictions;
	
	public void setField(TwoTwoMinoField field) {
		this.field = field;
		doubleField = new TwoTwoMinoDoubleField(field);
	}
	
	public void setFigures(int[][][] figures) {
		this.figures = figures;
	}
	
	public void generate() {
		packing.setField(field);
		packing.setFigures(figures);
		int attemptCount = 0;
		int solutionLimitCount = 8;
		while(true) {
			attemptCount++;
			///generateAny();
			///restrictions = createRestrictions();
			restrictions = createRandomRestrictions();
			List solutions = solve(solutionLimitCount);
			if(solutions != null /*&& solutions.size() < solutionLimitCount*/ && solutions.size() > 0) {
				System.out.println("solutionCount=" + solutions.size() + " attemptCount=" + attemptCount);
				if(solutions.size() == 1) break;
			}
		}
	}
	
	void generateAny() {
		packing.setPrintSolutionLimit(0);
		packing.setRandomizung(true);
		packing.setRestrictions(null);
		packing.setSolutionCountLimit(1);
		while(true) {
			packing.solve();
			if(packing.getSolutionCount() > 0) break;
		}		
	}
	
	public int[] createRestrictions() {
		int[] r = new int[field.getSize()];
		int[] values = (int[])packing.getSolutions().get(0);
		for (int i = 0; i < field.getSize(); i++) {
			int p0 = doubleField.convertFromOriginalField(i, 0, 0);
			int px = doubleField.convertFromOriginalField(i, 1, 0);
			int py = doubleField.convertFromOriginalField(i, 0, 1);
			if(values[p0] != values[px]) {
				r[i] = 2;
			} else if(values[p0] != values[py]) {
				r[i] = 1;
			} else {
				r[i] = 0;
			}
		}
		return r;
	}
	
	public int[] createRandomRestrictions() {
		int[] r = new int[field.getSize()];
		int q = field.getSize() / 3;
		for (int t = 0; t < q; t++) {
			int j = (int)(field.getSize() * Math.random());
			while(r[j] > 0) {
				j = (int)(field.getSize() * Math.random());
			}
			r[j] = (int)(2 * Math.random()) + 1;
		}
		return r;		
	}
	
	void printRestrictions() {
		for (int i = 0; i < field.getSize(); i++) {
			char c = restrictions[i] == 1 ? 'h' :
			restrictions[i] == 2 ? 'v' : 'o';
			System.out.print(" " + c);
			if(field.x(i) == field.getWidth() - 1) System.out.println("");
		}
		System.out.println("");
	}
	
	List solve(int solutionLimitCount) {
		packing.cleanValues();
			packing.setPrintSolutionLimit(0);
		packing.setRandomizung(false);
		packing.setRestrictions(restrictions);
		packing.setSolutionCountLimit(solutionLimitCount);
		packing.solve();
		List s = packing.getSolutions();
		if(s.size() == 1) {
			printRestrictions();
			packing.printSolution(packing.doubleField, (int[])s.get(0));
		}
		return s;
	}
	
	public static int WIDTH = 6;
	public static int HEIGHT = 7;

	public static void main(String[] args) {
		TwoTwoMinoFigures f = new TwoTwoMinoFigures();
		f.makeFigures();
		f.makeExtendedFigures();
		TwoTwoMinoField field = new TwoTwoMinoField();
		field.setSize(WIDTH, HEIGHT);
		TwoTwoMinoGenerator g = new TwoTwoMinoGenerator();
		g.setField(field);
		g.setFigures(f.getExtendedFigures());
		g.generate();
	}

}

/*
 o v o o o v o o
 o o o o o v h o
 h o h o v h o v
 o v v o o o o o
 o o o o h o h o
 o v o o v o o o
 o v o o h o h o
 o o v o v o o o
 v o o o v h o h

 a a a b b b c c c c c e e e e e
 a a a b b b c c c c c e e e e e
 a a p p b b k k k k c e n n n n
 a a p p b b k k k k c e m m n n
 a a p p b b o o l k k k m m m n
 q q p p o o o o l k l l m m m n
 q q q p p o o o l l l l m m n n
 q q q p p o o o l l l l m m n n
 q q r r r r j j h h h h h h h h
 q q r r r r j j j j h h d d h h
 t t t r s s s s s j j j d d d d
 t t t r s s s s s j j j d d d d
 t t t r w w w w s s i i d d f f
 t t t r w w w w w w i i f f f f
 u u u u u v x x x w i i g g f f
 u u u u u v x x x w i i g g f f
 u v v v v v x x x i i i g g f f
 u v v v v v x x x i g g g g g g

 o o o o v o o h
 o o v o h o o o
 v o h o o o o v
 h h o v o o o o
 o o o o o o o v
 v o h h o h v h
 o o o h o o o v
 o v o o o h v o
 v o v o o o o o

 b b h h h h i i c d d d d d d d
 b b h h h h i i c d d d d d e e
 b b k k h i i i c c c c e e e e
 b b k k h i i i i i c c e e e e
 b k k k h h j j j j c c f f f e
 b k k k j j j j j j c c f f f e
 b b k k t t j l l l a a n n f f
 u u t t t t j l l l a a n n f f
 u u u u t t l l m m a a n n n f
 u u u u t t l l m m a a n n n f
 u v v v t t l l m m a a a s n n
 u v v v p p m m m m m m a s r r
 w w v v p p p p p p s s s s s r
 w w v v p p g g p p s s s s s r
 w w w v x x g g o o o o o q r r
 w w w v x x g g o o q q o q r r
 w x x x x g g g o o q q q q r r
 w x x x x g g g o o q q q q r r
solutionCount=1 attemptCount=16566

 h o o h o v o o
 v o o v o h h o
 o o h o h o o o
 o o o o o o o h
 o o v o o v o o
 o o h o v o o o
 v o h v h o o v
 o v o o h o o v
 o h o o h o o o

 d d d d s s s s s s s h h h h h
 c c d d s s t t s s s h h h h h
 c d d d u u u t r r h h g g g g
 c d d d u u u t r r r r r r g g
 c c u u u u t t q q q q r r g g
 c c u u b b t t p p q q r r g g
 c c b b b b t t p p q q e e g g
 c c b b b b t t p p q q e e e e
 a a a a a b o o p p q e e e f f
 a a a a a b o o p p q e e e f f
 v v x x a a o o p i i i i i f f
 v v x x o o o o p i i i i i f f
 v x x x o o m l i i l l l l k f
 v x x x x x m l l l l l l l k f
 v v v w w w m m m m m m k k k f
 v v v w w w m m j j m m k k k f
 w w w w n n n n j j j j j j k k
 w w n n n n n n n n j j j j k k
solutionCount=1 attemptCount=60008

 h o h o o h o o
 o h v o o h h o
 o o o o o h o o
 o o o o o v o o
 o v o v o o o o
 v o o v o o v v
 v o o o o v o v
 h o h o o h o h
 o o o v o v o o

 h h h h h h h h q q q q q q j j
 g g h h n n h h q q p p q q j j
 g g g g o n p p p p p p q q j j
 g g o o o n p p p p j j j j j j
 g g o o n n n n l l l l k k k k
 g g o o n n n n l l k k k k k k
 d d o o r r m m l l l k i i e e
 d d o o r r m m l l l k i i e e
 d d s r r r r m m m u u i i e e
 d d s r r r r m m m u u i i e e
 d s s s s s t m v v u u u i i e
 d s s s s s t m v v u u u i i e
 d c c c c c t t v v v u w w w e
 d c c c c c t t v v v u w w w e
 c c a a a a t t x x v v w w w w
 a a a a b b t t x x x x w w f f
 a a b b b b b t x x x f f f f f
 a a b b b b b t x x x f f f f f
solutionCount=1 attemptCount=159483

*/