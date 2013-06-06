package slava.puzzle.template.action;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;

public class PuzzleMenuBar {
	protected JMenuBar menuBar = new JMenuBar();
	protected Map menus = new HashMap();
	PuzzleActionManager manager;

	public void init(PuzzleActionManager manager) {
		this.manager = manager;
		createMenuItem("File", "Open", "open", KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		createMenuItem("File", "Save", "save", KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		createMenuItem("File", "Save As...", "saveAs", null);
		getMenu("File").add(new JSeparator());
		createMenuItem("File", "Exit", "exit", KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.ALT_MASK));

		createMenuItem("Run", "Generate", "generate", KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0));
		createMenuItem("Run", "Solve", "solve", KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0));
		createMenuItem("Run", "Stop", "stop", KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
	}
	
	public void createMenuItem(String menuName, String displayName, String actionName, KeyStroke accelerator) {
		JMenu menu = getMenu(menuName);
		ActionListener listener = manager.getAction(actionName);
		createMenuItem(menu, displayName, listener, accelerator);
	}

	protected void createMenuItem(JMenu menu, String name, ActionListener listener) {
		createMenuItem(menu, name, listener, null);
	}

	protected void createMenuItem(JMenu menu, String name, ActionListener listener, KeyStroke accelerator) {
		if(listener == null) return;
		final JMenuItem item = new JMenuItem(name);
		if(listener != null) item.addActionListener(listener);
		menu.add(item);

		item.setActionCommand(name);
		if(accelerator != null) item.setAccelerator(accelerator);
		if(listener instanceof PuzzleAction) {
			final PuzzleAction a = (PuzzleAction)listener;
			menu.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					item.setEnabled(a.isEnabled());
				}
			});
		}
	}
	
	public JMenuBar getComponent() {
		return menuBar;
	}
	
	public JMenu getMenu(String name) {
		JMenu menu = (JMenu)menus.get(name);
		if(menu == null) {
			menu = new JMenu(name);
			menuBar.add(menu);
			menus.put(name, menu);
		}
		return menu;
	}

}
