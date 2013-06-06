package slava.puzzle.template.model;

public class PuzzleModel {
	protected PuzzleLoader loader;
	private boolean isRunning;
	protected String solutionInfo = null;
	
	public void setLoader(PuzzleLoader loader) {
		this.loader = loader;
		loader.setModel(this);
	}
	
	public PuzzleLoader getLoader() {
		return loader;
	}

	public boolean isRunning() {
		return isRunning;
	}
	
	public void setRunning(boolean b) {
		isRunning = b;
	}
	
	public void setSolutionInfo(String info) {
		solutionInfo = info;
	}
	
	public String getStatusText() {
		if(isRunning()) return "Wait...";
		if(solutionInfo != null) return solutionInfo;
		return "";
	}
	
	public boolean isShowingSolution() {
		return solutionInfo != null;
	}
	
}
