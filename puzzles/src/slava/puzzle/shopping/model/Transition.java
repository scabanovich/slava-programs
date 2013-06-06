package slava.puzzle.shopping.model;

public class Transition {
	private Node source;
	private Node target;
	int kind;
	boolean enabled = true;
	boolean enablementReadOnly = false;

	//runtime
	int solutions = 0;

	public Transition(Node source, Node target) {
		this.source = source;
		this.target = target;
		source.addTransition(this);
	}

	public Node getSource() {
		return source;
	}

	public Node getTarget() {
		return target;
	}

	public void setKind(int kind) {
		this.kind = kind;
	}

	public int getKind() {
		return kind;
	}

	public void initRuntime() {
		solutions = 0;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean b) {
		enabled = b;
	}

	public boolean isEnablementReadOnly() {
		return enablementReadOnly;
	}

	public void setEnablementReadOnly(boolean b) {
		enablementReadOnly = b;
	}

	public void solutionFound() {
		++solutions;
	}

	public int getSolutions() {
		return solutions;
	}
	
	Transition backward;
	
	public Transition getBackwardTransition() {
		if(backward == null) {
			backward = target.findTransition(source.getId());
		}
		return backward; 
	}
	
	public void dispose() {
		getBackwardTransition();
		source.removeTransition(this);
		target.removeTransition(backward);
	}

}
