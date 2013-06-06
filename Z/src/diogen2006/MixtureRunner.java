package diogen2006;

public class MixtureRunner {
	
	public static MixtureSet generateInitialSet() {
		MixtureSet s = new MixtureSet(3);
		s.mixtures[0] = new Mixture(8, 6d); 
		s.mixtures[1] = new Mixture(8, 6d); 
		s.mixtures[2] = new Mixture(9, 7d);  


		//limit=10
		s.move(1, 2, 1);
		s.move(1, 0, 1);
		s.move(2, 1, 1);
		s.move(0, 1, 1);
		s.move(1, 0, 1);
		s.move(2, 1, 1);

		s.move(1, 0, 1);
		s.move(1, 2, 1);
		s.move(0, 2, 1);
		s.move(2, 1, 1);
		s.move(0, 1, 2);
		s.move(2, 0, 1);
		s.move(0, 1, 1);
		s.move(2, 0, 1);
		s.move(0, 2, 1);

		s.move(1, 2, 1);

//--------
		s.move(1, 2, 1);
		s.move(2, 0, 1);
		s.move(2, 1, 1);
		s.move(1, 0, 1);
		s.move(0, 2, 1);

		return s;
	}
	
	static void run() {
		MixtureSet s = generateInitialSet();
		MixtureSolver solver = new MixtureSolver();
		solver.setInitialSet(s);
		solver.setTargetRation(Math.PI);
		solver.setMixtureLimit(10);
		
		solver.solve();
	}
	
	public static void main(String[] args) {
		run();
	}

}

/**
		s.mixtures[0] = new Mixture(8, 6d); 
		s.mixtures[1] = new Mixture(8, 6d); 
		s.mixtures[2] = new Mixture(9, 7d);  
		s.move(1, 2, 1);
		s.move(1, 0, 1);
		s.move(2, 1, 1);
		s.move(0, 1, 1);
		s.move(1, 0, 1);
		s.move(2, 1, 1);

		s.move(1, 0, 1);
		s.move(1, 2, 1);
		s.move(0, 2, 1);
		s.move(2, 1, 1);
		s.move(0, 1, 2);
		s.move(2, 0, 1);
		s.move(0, 1, 1);
		s.move(2, 0, 1);
		s.move(0, 2, 1);

		s.move(1, 2, 1);
		s.move(0, 2, 1);???
 
1.233679824963474E-12
 1->2, 2->0, 2->1, 1->0, 0->2, 2->1, 0->2, 0->2, 2->1, 1->2, 2->0, 0->1, 2->0, 1->2, 0->1, 1->0,
 
		s.mixtures[0] = new Mixture(16, 12d); 
		s.mixtures[1] = new Mixture(16, 13d); 
		s.mixtures[2] = new Mixture(20, 15d);  
		s.move(1, 0, 2);
		s.move(0, 1, 1);
		s.move(2, 0, 1);
		s.move(2, 1, 1);//
		s.move(0, 1, 3);
		s.move(1, 0, 1);
		s.move(2, 0, 4);
		s.move(2, 1, 1);
		s.move(0, 2, 1);
		s.move(1, 2, 1);
		s.move(0, 1, 1);

		s.move(1, 2, 1);
		s.move(0, 1, 1);
		s.move(0, 2, 2);
		s.move(2, 0, 1);
		s.move(1, 2, 1);
1.467714838554457E-12
 0->1, 2->0, 0->2, 1->2, 2->1, 2->0, 2->0, 2->0, 0->2, 1->2, 2->0, 2->0, 2->1, 0->2, 2->0, 0->2, 2->0, 1->2, 0->1, 2->0,
 * 
		s.mixtures[0] = new Mixture(16, 12d); 
		s.mixtures[1] = new Mixture(16, 13d); 
		s.mixtures[2] = new Mixture(20, 15d);  
		s.move(1, 0, 2);
		s.move(0, 1, 1);
		s.move(2, 0, 1);
		s.move(2, 1, 1);
		s.move(0, 1, 3);
		s.move(1, 0, 1);
		s.move(2, 0, 4);
		s.move(2, 1, 1);
		s.move(0, 2, 1);
		s.move(1, 2, 1);
		s.move(0, 1, 1);
3.5695890687748033E-12
 1->2, 0->1, 0->2, 0->2, 2->0, 1->0, 0->2, 1->0, 0->1, 0->1, 2->0, 2->1, 0->2, 0->2, 1->0, 1->2, 2->0, 2->0, 2->1, 0->2,

 ***************************************** 
	s.mixtures[0] = new Mixture(18, 14d); 
	s.mixtures[1] = new Mixture(20, 15d); 
	s.mixtures[2] = new Mixture(19, 15d); 
1.2542411553795318E-11
 0->2, 1->0, 2->0, 1->2, 2->1, 2->0, 0->2, 2->1, 1->2, 2->1, 0->2, 1->0, 2->0, 2->1, 0->2, 1->0, 0->2, 0->1, 0->2, 2->0, 1->2, 0->1,
1.2544187910634719E-11
 0->2, 1->0, 2->0, 2->1, 1->2, 2->0, 1->2, 2->1, 0->2, 0->2, 2->0, 2->1, 1->0, 2->1, 0->2, 1->0, 0->2, 0->2, 0->1, 2->0, 1->2, 0->1,
2.984190672350451E-11
 0->2, 1->0, 2->0, 1->0, 0->2, 1->0, 0->1, 2->1, 1->0, 1->2, 0->1, 0->1, 2->0, 2->1, 1->0, 0->1, 0->2, 0->2, 1->0, 2->0, 1->2, 0->1,

*/