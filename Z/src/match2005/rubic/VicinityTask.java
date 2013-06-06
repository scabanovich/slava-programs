package match2005.rubic;

public abstract class VicinityTask {
	String file;
	int module;
	boolean complete;
	
	public VicinityTask(String file, int module, boolean complete) {
		this.file = file;
		this.module = module;
		this.complete = complete;
	}
	
	public abstract int getStateCode(RubicState state);

}
