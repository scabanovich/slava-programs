package forsmarts.fitting2d;

public class Problem2D_TreePointCut extends Problem2D {
	static int[] DOTS_A = {
		0,1,1,1,0,1,
		0,1,0,0,0,1,
		1,0,0,0,0,1,
		1,0,0,0,0,1,
		0,1,0,0,0,0,
		1,1,1,1,0,0,		
	};
	static int[] DOTS_B = {
		1,0,0,1,0,0,
		0,0,1,1,0,0,
		0,0,1,1,1,0,
		0,0,1,1,1,0,
		0,1,0,0,0,0,
		0,1,1,0,1,1,
	};
	
	public static void run() {
		while(true) {
			Problem2D_TreePointCut problem = new Problem2D_TreePointCut();
			TreePointCutPackingAnalysis analysis = problem.createAnalysis();
			new Packing2dRunner().run(problem, analysis);
			if(analysis.getSolutionCount() > 0) break;
//			System.out.println(analysis.presolutionCount);
		}
		
	}
	
	int[] dots;	
	
	public Problem2D_TreePointCut() {
		init();
	}
	
	void init() {
		xSize = 6;
		ySize = 6;
		figures = TetraFigures.FIGURES;
		isReflectionAllowed = true;
		usageLimit = new int[]{1,1,1,1,1};
		for (int i = 0; i < 4; i++) {
			int k = (int)(5 * Math.random());
			usageLimit[k]++;
		}
		designations = TetraFigures.DESIGNATIONS;
		shapeMode = PackingAnalysis.NO_SHAPE;
		dots = DOTS_A;
	}
	
	public TreePointCutPackingAnalysis createAnalysis() {
		TreePointCutPackingAnalysis p = new TreePointCutPackingAnalysis();
		p.dots = this.dots;
		return p;
	}
	
}

class TreePointCutPackingAnalysis extends PackingAnalysis {
	int[] dots;
	int presolutionCount = 0;
	
	protected boolean canPlace(int pi) {
		if(!super.canPlace(pi)) return false;
		int[] ps = placements[figure[t]][pi].getPoints();
		int g = 0;
		for (int i = 0; i < ps.length; i++) {
			if(dots[ps[i]] == 1) ++g;
		}
		return g > 0;
	}
	
	protected boolean isIllegalState() {
		int[] xs = new int[usageLimits.length];
		for (int i = 0; i < field.getSize(); i++) {
			if(state[i] < 0) continue;
			if(dots[i] == 1) xs[state[i]]++;
		}
//		for (int i = 0; i < xs.length; i++) {
//			System.out.print(" " + xs[i]);
//		}
//		printSolution();
		for (int i = 0; i < xs.length; i++) {
			if(usage[i] >= usageLimits[i] && xs[i] != 3) return true;
			if(xs[i] > 3) return true;
		}
		return false;
	}

	protected boolean checkSolution() {
		++presolutionCount;
		int[] xs = new int[usageLimits.length];
		for (int i = 0; i < field.getSize(); i++) {
			if(dots[i] == 1) xs[state[i]]++;
		}
//		for (int i = 0; i < xs.length; i++) {
//			System.out.print(" " + xs[i]);
//		}
//		printSolution();
		for (int i = 0; i < xs.length; i++) {
			if(xs[i] != 3) return false;
		}
		return true;
	}

	
	
}

/*
A)
LIIIIS,
LLLLSS,
LLLLST,
LLLSTT,
OOSSTT,
OOSTTT,

LLLSTT
 
B)
LOOLLL,
LOOSSL,
LLSSOO,
TTTTOO,
TTTLLL,
TIIIIL,

LOSTLL

*/
