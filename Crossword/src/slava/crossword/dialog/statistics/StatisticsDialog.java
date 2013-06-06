package slava.crossword.dialog.statistics;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import slava.ui.action.*;
import slava.ui.dialog.*;
import slava.crossword.runtime.*;
import slava.crossword.ui.*;

public class StatisticsDialog extends AbstractQueryWizard {
    protected StatisticsProvider provider = new StatisticsProvider();
    protected DefaultTableModel tablemodel;
    protected JTable table;

    public StatisticsDialog() {
        title = "Word Statistics";
    }

    public void execute(int[][] jp, int[] net, byte[] content) {
        WordBase.instance.update();
        provider.init(jp, net, content);
        tablemodel.setDataVector(provider.getStatistics(), provider.getHeaders());
        super.execute(CrosswordFrame.frame().getContentPane());
    }

    protected CommandBar createCommandBar() {
        CommandBar bar = new CommandBar();
        bar.setCommands(new String[]{"Close"});
        bar.registerKeyboardAction("Close", KeyEvent.VK_ENTER);
        bar.registerKeyboardAction("Close", KeyEvent.VK_ESCAPE);
        return bar;
    }

    public void action(String command) {
        if("Close".equals(command)) dispose();
    }

    protected JComponent createInputPanel() {
        JPanel p = new JPanel(new BorderLayout());
        tablemodel = new DefaultTableModel();
        table = new JTable(tablemodel);
        JScrollPane sp = new JScrollPane(table);
        p.add(sp, BorderLayout.CENTER);
        sp.setPreferredSize(new Dimension(600, 400));
        return p;
    }

    protected void onWindowOpened() {
        bar.getComponent().requestFocus();
    }
}
