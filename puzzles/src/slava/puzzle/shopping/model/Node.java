package slava.puzzle.shopping.model;

public class Node {
	private int id;
	private int kind;  // 0 - nothing, 1 - parking lot, 2 - market
	private Transition[] transitions = new Transition[0];
	private boolean enabled;

	//runtime
	private boolean passed = false;

	public Node(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setKind(int kind) {
		this.kind = kind;
	}

	public int getKind() {
		return kind;
	}
	
	public void setEnabled(boolean b) {
		this.enabled = b;
		if(!b) {
			Transition[] t = transitions;
			for (int i = 0; i < t.length; i++) {
				t[i].dispose();
			}
		}
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public void addTransition(Transition t) {
		 Transition[] ts = new Transition[transitions.length + 1];
		 System.arraycopy(transitions, 0, ts, 0, transitions.length);
		 ts[transitions.length] = t;
		 transitions = ts;
	}
	
	public void removeTransition(Transition t) {
		int i = getTransitionIndex(t);
		if(i < 0) return;
		Transition[] ts = new Transition[transitions.length - 1];
		if(i > 0) {
			System.arraycopy(transitions, 0, ts, 0, i);
		}
		if(i < transitions.length - 1) {
			System.arraycopy(transitions, i + 1, ts, i, transitions.length - i - 1);
		}
		transitions = ts;
	}
	
	int getTransitionIndex(Transition t) {
		for (int i = 0; i < transitions.length; i++) if(transitions[i] == t) return i;
		return -1;
	}

	public Transition[] getTransitions() {
		return transitions;
	}

	public Transition findTransition(int targetId) {
		for (int i = 0; i < transitions.length; i++) {
			if(transitions[i].getTarget().getId() == targetId) return transitions[i];
		}
		return null;
	}

	public boolean isPassed() {
		return passed;
	}

	public void setPassed(boolean b) {
		passed = b;
	}

}
