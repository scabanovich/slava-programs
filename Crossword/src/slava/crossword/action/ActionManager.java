package slava.crossword.action;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.JOptionPane;

import slava.crossword.dialog.*;
import slava.crossword.runtime.*;
import slava.crossword.preference.*;
import slava.crossword.ui.*;
import slava.crossword.undo.CleanLettersChange;
import slava.crossword.undo.UndoManager;
import slava.crossword.undo.UndoableChange;

public class ActionManager implements ActionListener {
    public static String OPEN = "Open";
    public static String SAVE = "Save";
	public static String SAVE_ANSWERS = "SaveAnswers";
	public static String SAVE_AS_NUMBERED = "SaveAsNumbered";
    public static String EXIT = "Exit";
    public static String STATISTICS = "Statistics";
    public static String RUN = "Run";
    public static String SET_SIZE = "Set Size";
    public static String CLEAN = "Clean Letters";
    public static String EXCLUDE = "Exclude Current Words";
    public static String CLEAN_EXCLUDED = "Clean Exclusions";
    public static String LIMIT_WORDBASE = "Limit Word Base";
    public static String EDIT_SOURCES = "Edit Sources";
    public static String UNDO = "Undo";
	public static String REDO = "Redo";
    CrosswordComponentModel model;
    HashMap actionDelegates = new HashMap();

    public ActionManager() {}

    public void setModel(CrosswordComponentModel model) {
        this.model = model;
    }

    public CrosswordComponentModel getModel() {
        return model;
    }

    public void registerAction(String command, ActionListener l) {
        actionDelegates.put(command, l);
    }

    void runDelegateAction(String command, ActionEvent event) {
        ActionListener l = (ActionListener)actionDelegates.get(command);
        if(l != null) l.actionPerformed(event);
    }

    public void actionPerformed(ActionEvent e) {
        Component source = (Component)e.getSource();
        String command = e.getActionCommand();
        if(command.equals(STATISTICS)) {
            model.statistics();
        } else if(command.equals(RUN)) {
            model.run();
        } else if(command.equals(SET_SIZE)) {
            SetSizeDialog d = new SetSizeDialog();
            Point p = d.execute(source, new Point(model.getWidth(), model.getHeight()));
            if(p != null) {
                model.setDimensions(p.x, p.y);
                CrosswordPreference pref = CrosswordPreference.getInstance();
                pref.setInt("net.width", p.x);
                pref.setInt("net.height", p.y);
				UndoManager.getInstance().clean();
            }
        } else if(command.equals(CLEAN)) {
        	UndoableChange change = new CleanLettersChange(model);
        	change.redo();
			UndoManager.getInstance().addChange(change);
//            model.cleanLetters();
        } else if(command.equals(OPEN)) {
            FileDataDialog d = new FileDataDialog();
            Properties p = model.getStorage().getProperties();
            p.setProperty("title", OPEN);
            int i = d.execute(source, p);
            if(i < 0) return;
            model.getStorage().setProperties(p);
            model.getStorage().load(model);
            model.fireSizeChanged();
			UndoManager.getInstance().clean();
        } else if(command.equals(SAVE)) {
        	save(source, false);
        } else if(command.equals(SAVE_ANSWERS)) {
			save(source, true);
        } else if(command.equals(SAVE_AS_NUMBERED)) {
        	saveAsNumbered(source);
        } else if(command.equals(EXIT)) {
            runDelegateAction(EXIT, e);
        } else if(command.equals(EXCLUDE)) {
            model.excludeWords();
        } else if(command.equals(LIMIT_WORDBASE)) {
        	model.limitWordBase();
        } else if(command.equals(CLEAN_EXCLUDED)) {
            WordBase.instance.cleanExcludedPatterns();
        } else if(command.equals(EDIT_SOURCES)) {
            SourceListDialog d = new SourceListDialog();
            java.util.List list = WordBase.instance.getSourceList();
            d.setData(list);
            int i = d.execute(source);
            if(i < 0) return;
            WordBase.instance.setSourceList(list);
            WordBase.instance.saveSourceList();
        } else if(command.equals(UNDO)) {
        	UndoManager.getInstance().undo();
        } else if(command.equals(REDO)) {
			UndoManager.getInstance().redo();
        }
    }
    
    private void save(Component source, boolean saveAnswers) {
		FileDataDialog d = new FileDataDialog();
		Properties p = model.getStorage().getProperties();
		p.setProperty("title", SAVE);
		int i = d.execute(source, p);
		if(i < 0) return;
		model.getStorage().setProperties(p);
		model.getStorage().save(model, saveAnswers);
    }

    private void saveAsNumbered(Component source) {
		FileDataDialog d = new FileDataDialog();
		Properties p = model.getStorage().getProperties();
		p.setProperty("title", SAVE);
		int r = d.execute(source, p);
		if(r < 0) return;
		model.getStorage().setProperties(p);
    	byte[] b = model.getRuntimeCharacters();
    	int[] n = model.getRuntimeNet();
    	Set s = new HashSet();
    	for (int i = 0; i < b.length; i++) {
    		if(n[i] == 1 && b[i] != (byte)255) {
    			s.add(new Byte(b[i]));
    		}
    	}
    	String word = JOptionPane.showInputDialog("Enter word");
    	Map letterToNumber = new HashMap();
    	byte[] numberToLetter = new byte[s.size()];
    	for (int i = 0; i < numberToLetter.length; i++) numberToLetter[i] = (byte)255;
    	                         
    	LetterCoder lc = WordBase.instance.getLetterCoder();
    	for (int i = 0; i < word.length(); i++) {
    		char c = word.charAt(i);
    		byte wi = lc.getWindowsCharIndex(c);
    		Byte wb = new Byte(wi);
    		if(letterToNumber.containsKey(wb)) continue;
    		int q = letterToNumber.size();
    		letterToNumber.put(wb, new Integer(q));
    		numberToLetter[q] = wi;
    	}
    	int tipSize = letterToNumber.size();
    	Iterator it = s.iterator();
    	while(it.hasNext()) {
    		Byte wb = (Byte)it.next();
    		if(letterToNumber.containsKey(wb)) continue;
    		int q = (int)(numberToLetter.length * Math.random());
    		while(numberToLetter[q] != (byte)255) {
    			q = (int)(numberToLetter.length * Math.random());
    		}
    		letterToNumber.put(wb, new Integer(q));
    		numberToLetter[q] = wb.byteValue();
    	}
		model.getStorage().saveAsNumbers(model, letterToNumber, numberToLetter, tipSize);
    }

}