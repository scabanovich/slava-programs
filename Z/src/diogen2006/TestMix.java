package diogen2006;

public class TestMix {
	
	static void test1() {
		Mix.computeBestRatios(Math.PI);
		Mix m1 = new Mix(9, 3, 1);
		Mix m2 = new Mix(12, 10, 3);
		Mix mLimit = m1.mix(m2);
		System.out.println("Limit=" + mLimit);
		int[] ns = {4,3};
		for (int i = 0; i < ns.length; i++) {
			int q = ns[i];
			m2 = m2.mix(m1.take(q));
			System.out.println("m2: " + m2);
			m1 = m1.deduct(q);
			
			m1 = m1.mix(m2.take(q));
			m2 = m2.deduct(q);
			System.out.println("m1: " + m1);
		}
	}
	
	static void test2() {
		RelationsSearcer s = new RelationsSearcer();
		s.search(Math.PI, 20, 1E-8);
	}

	public static void main(String[] args) {
		//test1();
		test2();
	}

}

/**
		Mix m1 = new Mix(9, 3, 1);
		Mix m2 = new Mix(12, 10, 3);
		Mix mLimit = m1.mix(m2);
		System.out.println("Limit=" + mLimit);
		int[] ns = {4,3,1,1};
		for (int i = 0; i < 4; i++) {
			int q = ns[i];
			m2 = m2.mix(m1.take(q));
			System.out.println("m2: " + m2);
			m1 = m1.deduct(q);
			
			m1 = m1.mix(m2.take(q));
			m2 = m2.deduct(q);
			System.out.println("m1: " + m1);
		}
Limit=amount=21(277/87)=3.1839080459770117
m2: amount=16(159/49)=3.2448979591836733
m1: amount=9(59/19)=3.1052631578947367
m2: amount=15(119/37)=3.2162162162162162
m1: amount=9(355/113)=3.1415929203539825  !!!

m2: amount=13(4639/1445)=3.2103806228373704
m1: amount=9(13853/4399)=3.149124801091157
m2: amount=13(180857/56419)=3.205604494939648
m1: amount=9(540523/171305)=3.15532529698491


Target 103993/33102

                
                  
       1   1     1   2 2 
     4 4 4 4   3 3 3   2 
     1     3 3 3   2 2 2  
                
*/