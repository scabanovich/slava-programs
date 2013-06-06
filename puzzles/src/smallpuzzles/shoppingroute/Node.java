package smallpuzzles.shoppingroute;

public class Node {
    private int id;
    private int kind;  // 0 - nothing, 1 - parking lot, 2 - market
    private Transition[] transitions = new Transition[0];

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

    public void addTransition(Transition t) {
         Transition[] ts = new  Transition[transitions.length + 1];
         System.arraycopy(transitions, 0, ts, 0, transitions.length);
         ts[transitions.length] = t;
         transitions = ts;
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
