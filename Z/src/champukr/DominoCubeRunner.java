package champukr;

public class DominoCubeRunner {
	
	static class DominoCubeFillerListener implements DominoCubeFiller.Listener {
		DominoCubeDivider divider;
		public DominoCubeFillerListener(DominoCubeDivider divider) {
			this.divider = divider;
		}
		public void onSolutionFound(int[] state) {
			divider.setNumbers(state);
			divider.solve();
//			System.out.println("sc = " + divider.solutionCount + " " + " tc=" + divider.treeCount);
			if(divider.solutionCount == 1) {
				System.out.println("Solution " + " treeCount=" + divider.treeCount);
				for (int i = 0; i < state.length; i++) {
					System.out.print(" " + state[i]);
				}
				System.out.println("");
				int[] solution = divider.solution;
				for (int i = 0; i < state.length; i++) {
					char c = (char)(97 + solution[i]);
					System.out.print(" " + c);
				}
				System.out.println("");
			}
			if(divider.solutionCount > 0) {
//				System.out.println("Solution count=" + divider.solutionCount + " Fixed domino count=" + divider.getFixedDominoCount());
			}
		}
	}
	
	static void enumerate() {
		DominoCubeField f = new DominoCubeField();
		f.setSize(4);
		DominoSet dominoes = new DominoSet();
		dominoes.setSize(7);
		DominoCubeDivider divider = new DominoCubeDivider();
		divider.setDominoes(dominoes);
		divider.setField(f);
		DominoCubeFillerListener listener = new DominoCubeFillerListener(divider);
		DominoCubeFiller p = new DominoCubeFiller();
		p.setField(f);
		p.setListener(listener);
		p.solve();
	}
	
	static void solve(int[] state) {
		DominoCubeField f = new DominoCubeField();
		f.setSize(4);
		DominoSet dominoes = new DominoSet();
		dominoes.setSize(7);
		DominoCubeDivider divider = new DominoCubeDivider();
		divider.setDominoes(dominoes);
		divider.setField(f);
		divider.setNumbers(state);
		divider.solve();
		System.out.println("Solution count=" + divider.solutionCount);
		System.out.println("Fixed domino count=" + divider.getFixedDominoCount());
		if(divider.solutionCount > 0) {
			int[] solution = divider.solution;
			for (int i = 0; i < state.length; i++) {
				char c = (char)(96 + solution[i]);
				System.out.print(" " + c);
			}
			System.out.println("");
		}
	}
	
	static int[] EXAMPLE = {
		6,2,2,2, 6,1,5,0, 0,5,1,6, 0,4,4,4,
		0,1,5,6, 1,    3, 5,    3, 6,3,3,0,
		4,3,3,2, 5,    3, 1,    3, 2,1,5,4,
		2,6,2,2, 0,1,5,6, 6,1,5,0, 4,4,0,4,
	};
	static int[] MY_SOLUTION = {
		5,0,6,1, 3,0,4,5, 3,6,2,1, 1,6,0,5,
		2,1,3,6, 4,    2, 4,    0, 2,4,2,4,
		2,5,3,2, 4,    4, 0,    6, 6,2,4,0,
		3,6,0,3, 1,5,5,1, 5,1,1,5, 3,0,6,3,
	};

	public static void main(String[] args) {
		enumerate();
		///solve(MY_SOLUTION);
	}

}

/*
Solution  treeCount=1 with only Uniqueness moves
 5 1 5 1  3 0 4 5  3 6 2 1  1 5 1 5   3 2 6 1  3 4  3 2  3 4 0 5   2 3 1 6  6 1  0 5  4 3 5 0   2 6 0 4  0 6 4 2  6 0 2 4  4 0 6 2
 { n t c  x n t r  x q s r  y q s h   { | m c  w f  o j  y z i h   d | m l  w f  o j  g z i e   d k p l  } k p v  } b u v  g b u e


Solution  treeCount=1
 5 0 6 1  3 0 4 5  3 6 2 1  1 6 0 5   2 1 3 6  4 2  4 0  2 4 2 4   2 5 3 2  4 4  0 6  6 2 4 0   3 6 0 3  1 5 5 1  5 1 1 5  3 0 6 3
 f b q p  f b q l  t v w d  g v w k   e i h p  s l  t d  g { { k   e i h o  s r  j n  z z | |   x u u o  x m m r  j c c n  y y } }
Solution  treeCount=1
 5 0 6 1 3 0 4 5 3 6 2 1 1 6 0 5 4 2 4 2 2 6 2 4 4 5 3 0 0 4 2 6 6 0 2 2 4 1 3 4 3 6 0 3 1 5 5 1 5 1 1 5 3 0 6 3
 k b x f j b x l i h w e i h w v k { { f j l m e d o n v z z | | p q m s d o n u y y } } p g g q r c c s r t t u
Solution  treeCount=1
 5 0 6 1 1 2 6 3 5 4 0 3 1 6 0 5 4 2 4 2 0 4 2 4 6 3 1 2 0 4 2 6 6 0 4 4 2 3 5 2 3 6 0 3 5 1 1 5 1 5 5 1 3 0 6 3
 l x w g d x w t m q b f p q b f l { { g d t m s p h i e z z | | j k r s o h i e y y } } j c c k r n n u o v v u
Solution  treeCount=1
 5 0 6 1 1 2 6 3 5 4 0 3 1 6 0 5 0 3 5 4 4 2 6 2 2 4 2 4 4 3 1 4 2 2 0 6 6 2 4 0 3 6 0 3 5 1 1 5 1 5 5 1 3 0 6 3
 t u h i e u h i l v b j f v b k t n o d e m l j f { { k s n o d w m x r z z | | s q q p w c c p x g g r y y } }
Solution  treeCount=1
 5 0 6 1 5 0 3 4 0 6 3 3 2 6 0 4 1 4 1 6 1 4 6 2 4 3 5 0 2 6 1 3 0 4 5 2 5 2 2 3 4 2 4 2 6 3 3 0 1 6 0 5 1 1 5 5
 i b s r i b s { h g u { h g u z p q q r c | f n e m y z p o d d c | f n e m y l k o j j k t t x v w w x v } } l
Solution  treeCount=1
 5 0 6 1 2 3 6 1 3 3 0 6 2 6 0 4 0 5 2 5 2 5 4 0 6 1 3 2 3 5 0 4 2 6 4 1 3 4 4 1 4 2 4 2 6 3 3 0 1 6 0 5 1 1 5 5
 r s j k y s j k y u b e x u b e r q q p z i n c x { m d f f o p z i n c l { m d g g o h w t t h w v v | l } } |
Solution  treeCount=1
 5 0 6 1 2 1 3 6 2 5 3 2 3 6 0 3 3 0 4 5 4 2 4 4 1 5 5 1 3 6 2 1 4 0 0 6 5 1 1 5 1 6 0 5 2 4 2 4 6 2 4 0 3 0 6 3
 f b q p e i h p e i h o s r r o f b q n t n t u s k k u v w x d v d j l j c c l g w x m g { { m z z | | y y } }
Solution  treeCount=1
 5 0 6 1 4 2 4 2 0 4 2 6 3 6 0 3 3 0 4 5 2 6 6 0 1 5 5 1 3 6 2 1 2 4 2 2 5 1 1 5 1 6 0 5 4 5 3 0 4 1 3 4 3 0 6 3
 k b { f k z z f y y | | } } x x j b { l j l p q p g g q i h w e m e m s r c c s i h w v d o n v d o n u r t t u
Solution  treeCount=1
 5 0 6 1 4 2 4 2 0 4 2 6 3 6 0 3 1 2 6 3 0 4 6 0 5 1 1 5 5 4 0 3 2 4 4 4 1 5 5 1 1 6 0 5 6 3 1 2 2 3 5 2 3 0 6 3
 l | w g l { { g y y z z x x } } d | w r d r j k j c c k m u b f m q p q p n n s t u b f t h i e o h i e o v v s
Solution  treeCount=1
 5 0 6 1 0 3 5 4 4 3 1 4 3 6 0 3 1 2 6 3 4 2 2 2 5 1 1 5 5 4 0 3 6 2 0 6 1 5 5 1 1 6 0 5 2 4 2 4 6 2 4 0 3 0 6 3
 s t h i s n o d r n o d r q q p e t h i e k u k u c c p m v b j m j w x w g g x f v b l f { { l z z | | y y } }
Solution  treeCount=1
 5 0 6 1 1 4 1 6 2 6 1 3 4 2 4 2 5 0 3 4 1 4 0 4 6 3 3 0 0 6 3 3 6 2 5 2 1 6 0 5 2 6 0 4 4 3 5 0 5 2 2 3 1 1 5 5
 k b r q o p p q o n d d j n i i k b r z c y c y j s s x h g t z f u f u v w w x h g t { e m | { e m | l v } } l
Solution  treeCount=1
 5 0 6 1 0 5 2 5 3 5 0 4 4 2 4 2 2 3 6 1 2 5 2 6 6 3 3 0 3 3 0 6 4 0 4 1 1 6 0 5 2 6 0 4 6 1 3 2 3 4 4 1 1 1 5 5
 q r j k q p p o f f n o g g n h y r j k x i x i w s s h y t b e u c u c w v v z { t b e { | m d l | m d l } } z
Solution  treeCount=1
 5 1 5 1 3 0 4 5 3 6 2 1 1 5 1 5 3 2 6 1 3 4 3 2 3 4 0 5 2 3 1 6 6 1 0 5 4 3 5 0 2 6 0 4 0 6 4 2 6 0 2 4 4 0 6 2
 { q v c z q v w z y x w s y x h { t u c | f r l s j i h d t u p | f r l g j i e d n o p } n o m } b k m g b k e
Solution  treeCount=1
 5 1 5 1 0 2 4 6 6 6 0 0 1 3 3 5 4 0 2 6 2 3 4 1 2 4 4 2 0 6 4 2 4 3 2 5 6 0 4 2 3 5 1 3 6 1 5 0 0 1 5 6 3 5 1 3
 m d n p w w q q v v b b g t f f m d n p z h z i g t s e y j r o y h { i { l s e x j r o x c k u | c k u | l } }
Solution  treeCount=1
 5 1 5 1 0 2 4 6 6 6 0 0 1 3 3 5 0 4 6 2 3 4 5 2 4 2 2 4 4 2 0 6 3 2 1 4 4 2 6 0 3 5 1 3 6 1 5 0 0 1 5 6 3 5 1 3
 v e l f w w x x h h b b i i j k v e l f n { o { d m j k u r p z n z o | d m t | u r p y s c g y s c g } q q t }
Solution  treeCount=1
 5 1 5 1 1 2 4 5 5 6 0 1 1 3 3 5 5 2 4 1 0 6 4 2 3 3 3 3 0 5 1 6 5 1 3 3 4 0 6 2 2 4 2 4 6 2 4 0 0 0 6 6 4 6 0 2
 h v v c w w u u x x q q s y y z h l f c i t j { s r | z e l f p i t j { g r | d e m m p k k o o b b n n g } } d
Solution  treeCount=1
 5 1 5 1 1 2 6 3 5 4 0 3 1 5 1 5 5 0 4 3 2 3 4 3 1 6 2 3 0 5 3 4 5 0 1 6 6 1 3 2 2 6 0 4 4 2 0 6 2 4 6 0 4 0 6 2
 h w x s v w x y v u q y c u q z h i j s l r f { c t | z e i j g l r f { p t | d e k b g m k b } m o n } p o n d
Solution  treeCount=1
 5 1 5 1 5 0 4 3 0 5 3 4 2 6 0 4 1 2 6 3 2 3 5 0 4 2 0 6 5 4 0 3 4 3 1 6 2 4 6 0 1 5 1 5 1 6 2 3 6 1 3 2 4 0 6 2
 h w v s h i j s e i j g e k b g x w v y l r l r m k b t x z q y f u f u m o n t c z q { c | } { p | } d p o n d
Solution  treeCount=1
 5 1 5 1 5 2 4 1 0 5 1 6 2 4 2 4 1 2 4 5 0 6 5 1 6 2 4 0 5 6 0 1 4 2 3 3 0 0 6 6 1 3 3 5 3 3 3 3 4 0 6 2 4 6 0 2
 h v v c h l f c e l f p e m m p w w u u i t i t j j o o x x q q k y k y b b n n s z z { s r | { g r | d g } } d
Solution  treeCount=1
 5 1 5 1 3 2 6 1 2 3 1 6 2 6 0 4 3 0 4 5 3 4 6 1 0 6 4 2 3 6 2 1 3 2 0 5 6 0 2 4 1 5 1 5 3 4 0 5 4 3 5 0 4 0 6 2
 y q v c y x w c d x w p d n o p z q v { t f t f s n o m z u | { r l r l s b k m } u | h } j i h g j i e g b k e

*/