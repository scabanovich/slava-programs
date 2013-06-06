package packing3d;

public class Packing3dRunner {
	
	public void run(Problem3D problem) {
		CubeField cubeField = new CubeField();
		cubeField.setSize(problem.xSize, problem.ySize, problem.zSize);
		Figures3D fgs = new Figures3D();
		fgs.createPlacements(problem.figures, cubeField);
		PackingAnalysis analysis = new PackingAnalysis();
		analysis.setPlacements(fgs.placements);
		analysis.setCubeField(cubeField);
		analysis.setUsageLimits(problem.usageLimit);
		analysis.solve();
	}

	public static void main(String[] args) {
		Problem3D problem = new Problem3D_a();
		new Packing3dRunner().run(problem);
	}
}
