package slava.puzzle.circuit;

import java.io.*;
import java.util.*;

public class CircuitData {
	Set nodes;
	Bind[] binds;
	Equation[] equations;
	
	public CircuitData() {
	}

	public void load(File f) {
		ArrayList list = new ArrayList();
		nodes = new HashSet();
		try {
			FileReader r = new FileReader(f);
			BufferedReader br = new BufferedReader(r);
			String s = null;
			Bind b = null;
			while((s = br.readLine()) != null) {
				if(s.startsWith("//") || s.trim().length() == 0) continue;
				b = parse(s);
				list.add(b);
				nodes.add(b.beginning);
				nodes.add(b.ending);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		binds = (Bind[])list.toArray(new Bind[0]);
//		for (int i = 0; i < binds.length; i++) {
//			System.out.println(binds[i].toString());
//		}
		initEquations();
	}
	
	private Bind parse(String s) throws Exception {
		int v = -1;
		String b = null;
		String e = null;
		int vi = s.indexOf(':');
		if(vi >= 0) {
			v = Integer.parseInt(s.substring(vi + 1).trim());
			s = s.substring(0, vi);
		}
		int i = s.indexOf('-');
		if(i < 0) throw new Exception("Separator '-' not found.");
		b = s.substring(0, i).trim();
		e = s.substring(i + 1).trim();
		if(b.equals(e)) throw new Exception("Bind cannot start and end at the same node.");
		return new Bind(b, e, v);
	}
	
	private void initEquations() {
		Iterator it = nodes.iterator();
		ArrayList list = new ArrayList();
		while(it.hasNext()) {
			String n = it.next().toString();
			Equation eq = new Equation();
			for (int i = 0; i < binds.length; i++) {
				if(binds[i].beginning.equals(n)) {
					if(!binds[i].isValueSet) {
						eq.addMember(binds[i], -1);
					} else {
						eq.addToConstant(-binds[i].value);
					}
				} else if(binds[i].ending.equals(n)) {
					if(!binds[i].isValueSet) {
						eq.addMember(binds[i], 1);
					} else {
						eq.addToConstant(binds[i].value);
					}
				}
			}
			if(!eq.isEmpty()) {
				list.add(eq);
			}
		}
		equations = (Equation[])list.toArray(new Equation[0]);
//		for (int i = 0; i < equations.length; i++) {
//			System.out.println(equations[i].toString());
//		}
	}

}
