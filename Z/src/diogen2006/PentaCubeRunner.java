package diogen2006;

import forsmarts.fitting2d.Packing2dRunner;

public class PentaCubeRunner {
	
	static PentaCubeForm FORM_1 = new PentaCubeForm(
		new int[] {
			1,0,1,1,  //ix2 = 0
			1,0,0,1,  //ix2 = 1
			1,1,0,1,  //ix2 = 2;
			0,1,0,1,  //ix2 = 3;
		},
		4, 4, 15, "NL"
	);

	static PentaCubeForm FORM_2 = new PentaCubeForm(
		new int[] {
			0,1,1,0,1,1,  //ix2 = 0
			1,1,0,0,1,0,  //ix2 = 1
			0,1,0,1,1,0,  //ix2 = 2;
		},
		6, 3, 12, "FZ"
	);

	static PentaCubeForm FORM_AA = new PentaCubeForm(
			new int[] {
				1,1,1,1,0,1,1,1, //ix2 = 0
				0,1,0,0,0,1,0,1  //ix2 = 1
			},
			8, 2, 14, "YU"
		);

	static PentaCubeForm FORM_3 = new PentaCubeForm(
		new int[] {
			1,1,1,0,0,0,0,  //ix2 = 0
			0,1,0,0,1,1,1,  //ix2 = 1
			0,1,0,1,1,0,0,  //ix2 = 2;
		},
		7, 3, 11, "NY"
	);

	public static void main(String[] args) {
		PentaCubeForm cubeForm = FORM_2;
		PentaCubeAnalysis solver = new PentaCubeAnalysis();
		solver.setShowSolutionLimit(10);
		solver.setCubeForm(cubeForm);
		PentaCube2DProblem problem = new PentaCube2DProblem(cubeForm);
		new Packing2dRunner().run(problem, solver);
		System.out.println("solutionCount=" + solver.getSolutionCount());
	}

}