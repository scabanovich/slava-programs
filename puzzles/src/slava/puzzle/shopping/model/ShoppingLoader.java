package slava.puzzle.shopping.model;

import java.util.*;
import java.io.*;
import slava.puzzle.template.model.*;

public class ShoppingLoader extends PuzzleLoader {
	
	public ShoppingLoader() {
		setRoot("/data/shopping");
		initName("shop");
	}
	
	public ShoppingModel getShoppingModel() {
		return (ShoppingModel)model;
	}
	
	public void load() throws Exception {
		File f = getFile();
		if(!f.isFile()) throw new Exception("File does not exist.");
		ShoppingField field = getShoppingModel().getField();
		Node[] ns = field.getNodes();
		Properties p = new Properties();
		p.load(new FileInputStream(f));
		String nodes = p.getProperty("nodes");
		if(nodes != null) deserializeNodes(ns, nodes);
		String transitions = p.getProperty("transitions");
		if(transitions != null) deserializeTransitions(ns, transitions);
	}
	
	public void save() throws Exception {
		ShoppingField field = getShoppingModel().getField();
		Node[] ns = field.getNodes();
		Properties p = new Properties();
		p.setProperty("nodes", serializeNodes(ns));
		p.setProperty("transitions", serializeTransitions(ns));
		FileOutputStream os = new FileOutputStream(getFile());
		p.store(os, null);
		os.close();
	}
	
	private String serializeNodes(Node[] ns) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ns.length; i++) {
			int q = (!ns[i].isEnabled()) ? -1 : ns[i].getKind();
			sb.append(' ').append(q);
		}
		return sb.toString();
	}

	private String serializeTransitions(Node[] ns) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ns.length; i++) {
			Transition[] ts = ns[i].getTransitions();
			for (int j = 0; j < ts.length; j++) {
				sb.append(' ').append(ts[j].getSource().getId());
				sb.append(' ').append(ts[j].getTarget().getId());
				sb.append(' ').append(!ts[j].isEnabled() ? -1 : ts[j].getKind());
			}
		}
		return sb.toString();
	}

	private void deserializeNodes(Node[] ns, String s) {
		for (int i = 0; i < ns.length; i++) {
			ns[i].setEnabled(false);
		}
		int[] q = deserialize(s);
		for (int i = 0; i < ns.length; i++) {
			boolean enabled = q[i] >= 0;
			ns[i].setEnabled(enabled);
			ns[i].setKind(enabled ? q[i] : 0);
		}
	}

	private void deserializeTransitions(Node[] ns, String s) {
		int[] q = deserialize(s);
		for (int i = 0; i < q.length; i += 3) {
			int sourceId = q[i];
			int targetId = q[i + 1];
			boolean enabled = (q[i + 2] >= 0);
			Transition t = new Transition(ns[sourceId], ns[targetId]);
			t.setEnabled(enabled);
			if(enabled) t.setKind(q[i + 2]);
		}
	}
	
}
