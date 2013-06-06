package slava.crossword.ui;

import java.awt.event.*;
import javax.swing.*;
import slava.crossword.runtime.*;
import slava.ui.action.*;
import slava.ui.dialog.*;

public class CrosswordSolveDialog extends AbstractQueryWizard implements Runnable {
    Thread thread = null;
    CrosswordSolver solver;

    public CrosswordSolveDialog() {
        title = "Run";
    }

    protected JComponent createInputPanel() {
        return new JPanel();
    }

    protected CommandBar createCommandBar() {
        CommandBar b = new CommandBar();
        b.setCommands(new String[]{"Stop"});
        b.registerKeyboardAction("Stop", KeyEvent.VK_ESCAPE);
        return b;
    }

    public void execute(int[][] jp, int[] net, byte[] content) {
        WordBase.instance.update();
        solver = new CrosswordSolver();
		IWordFilter filter = WordBase.instance.getWordFilter();
		if(filter instanceof ISolutionEstimate) {
        	solver.setEstimator((ISolutionEstimate)filter);
		}
        solver.init(jp, net, content);
        thread = new Thread(this);
        thread.start();
        super.execute(CrosswordFrame.frame.getContentPane());
    }

    public void action(String command) {
        if("Stop".equals(command)) {
            interrupt();
        }
    }

    protected void onWindowClosed() {
        interrupt();
    }

    private void interrupt() {
        solver.interrupt();
    }

    public void run() {
        try { Thread.sleep(500); } catch (Exception e) {}
        solver.analyse();
        dispose();
    }

    public CrosswordSolver getSolver() {
        return solver;
    }

}
