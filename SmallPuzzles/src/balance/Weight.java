package balance;

import java.util.ArrayList;

public class Weight {
	static Weight[] EMPTY = new Weight[0];

	Weight parent = null;
	int leg = 0;
	Weight[] children = EMPTY;
	int mass = -1;
	
	public Weight() {}
	
	public Weight(Weight parent, int leg) {
		this.parent = parent;
		this.leg = leg;
		if(parent != null) parent.addChild(this);
	}
	
	public void addChild(Weight c) {
		if(children.length == 0) {
			children = new Weight[]{c};
		} else if(children.length == 1) {
			children = new Weight[]{children[0], c};
		} else {
			Weight[] cs = new Weight[children.length + 1];
			System.arraycopy(children, 0, cs, 0, children.length);
			cs[children.length] = c;
			children = cs;
		}
	}
	
	public void setMass(int m) {
		this.mass = m;
		if(parent == null) return;
		if(m < 0) {
			parent.setMass(-1);
		} else {
			parent.computeMass();
		}
	}
	
	public void computeMass() {
		if(children.length == 0) return;
		int s = 0;
		for (int i = 0; i < children.length; i++) {
			int dm = children[i].mass;
			if(dm < 0) return;
			s += dm;
		}
		mass = s;
		if(parent != null) parent.computeMass();		
	}
	
	public boolean checkBalance() {
		if(mass < 0) return true; //not defined
		int c = 0;
		for (int i = 0; i < children.length; i++) {
			c += children[i].leg * children[i].mass;
		}
		if(c != 0) return false;
		return (parent == null) || parent.checkBalance(); 
	}
	
	public int getLeafCount() {
		if(children.length == 0) return 1;
		int c = 0;
		for (int i = 0; i < children.length; i++) {
			c += children[i].getLeafCount();
		}
		return c;		
	}
	
	public void collectLeaves(ArrayList list) {
		if(children.length == 0) {
			list.add(this);
		} else {
			for (int i = 0; i < children.length; i++) {
				children[i].collectLeaves(list);
			}
		}
	}
	
	public int getLevel() {
		return parent == null ? 0 : parent.getLevel() + 1;
	}
	
	public String toString() {
		int level = getLevel();
		StringBuffer sb = new StringBuffer();
		if(children.length == 0) {
			for (int i = 0; i < level; i++) sb.append("  ");
			sb.append(leg).append(':');
			sb.append("(" + mass + ")\n");
		} else {
			for (int i = 0; i < level; i++) sb.append("  ");
			sb.append(leg).append(':');
			sb.append("{\n");
			for (int i = 0; i < children.length; i++) {
				sb.append(children[i].toString());				
			}
			for (int i = 0; i < level; i++) sb.append("  ");
			sb.append("}\n");
		}
		return sb.toString();
	}

}
