package forsmarts.fitting2d;

public class Problem2D {
	public int xSize;
	public int ySize;
	public int[][][] figures;
	public boolean isReflectionAllowed = false;
	public int[] usageLimit;
	public char[] designations;
	public int shapeMode = PackingAnalysis.HV_SHAPE;
	public int[] fieldRestriction = null;
	public int showSolutionLimit = -1;
	public int solutionLimit = -1;
}
