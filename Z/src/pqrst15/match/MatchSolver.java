package pqrst15.match;

import java.util.ArrayList;

public class MatchSolver {
	static Match[] NO_MATCHS = new Match[0];
	static Node[] NO_NODES = new Node[0];

	MatchField field;
	
	int t;
	int[] wayCount;
	int[] way;
	Match[][][] ways;
	
	Node[][] burnedNodes;
	Match[][] burnedMatchs;
	
	int matchsToBurn;
	int nodesToBurn;
	int twoMatchMoveCount;
	int twoMatchMoveCountLimit = 3;
	
	
	public MatchSolver() {}
	
	public void setField(MatchField field) {
		this.field = field;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		matchsToBurn = field.matchs.length;
		nodesToBurn = field.nodes.length;
		wayCount = new int[nodesToBurn + 1];
		way = new int[nodesToBurn + 1];
		ways = new Match[nodesToBurn][400][0];
		burnedNodes = new Node[nodesToBurn][0];
		burnedMatchs = new Match[nodesToBurn][0];
		t = 0;

		burnedNodes[0] = new Node[]{field.nodes[9]};
		burnedNodes[0][0].burningTime = 0;
		nodesToBurn--;
		twoMatchMoveCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(matchsToBurn == 0) return;
		if(nodesToBurn * 3 + 3 < matchsToBurn) return;
		if(burnedNodes[t].length == 0) return;
		Match[] required = getRequiredMatchs();
		if(required.length > 3) return;
		if(hasWrongNode()) return;
		if(required.length == 3) {
			if(matchsToBurn > required.length) return;
			wayCount[t] = 1;
			ways[t][0] = required;
			return;
		}
		Match[] available = getAvailableMatchs();
		if(available.length + required.length < 2) return;
		if(available.length + required.length == 2) {
			if(twoMatchMoveCount >= twoMatchMoveCountLimit) return;
			Match[] pr = prepare(required, 2);
			System.arraycopy(available, 0, pr, 0, available.length);
			wayCount[t] = 1;
			ways[t][0] = pr;
			return;
		}
		if(available.length + required.length == 3) {
			Match[] pr = prepare(required, 3);
			System.arraycopy(available, 0, pr, 0, available.length);
			wayCount[t] = 1;
			ways[t][0] = pr;
			if(required.length == 1 && twoMatchMoveCount < twoMatchMoveCountLimit) {
				for (int i = 0; i < available.length; i++) {
					pr = prepare(required, 2);
					pr[0] = available[i];
					ways[t][wayCount[t]] = pr;
					wayCount[t]++;
				}
			}
		} else if(required.length == 2) {
			if(matchsToBurn == required.length && twoMatchMoveCount < twoMatchMoveCountLimit) {
				wayCount[t] = 1;
				ways[t][0] = required;
			}
			for (int i = 0; i < available.length; i++) {
				Match[] pr = prepare(required, 3);
				pr[0] = available[i];
				ways[t][wayCount[t]] = pr;
				wayCount[t]++;
			}
		} else if(required.length == 1) {
			for (int i1 = 0; i1 < available.length; i1++) {
				for (int i2 = i1 + 1; i2 < available.length; i2++) {
					Match[] pr = prepare(required, 3);
					pr[0] = available[i1];
					pr[1] = available[i2];
					ways[t][wayCount[t]] = pr;
					wayCount[t]++;
				}
			}
			if(twoMatchMoveCount < twoMatchMoveCountLimit) {
				for (int i = 0; i < available.length; i++) {
					Match[] pr = prepare(required, 2);
					pr[0] = available[i];
					ways[t][wayCount[t]] = pr;
					wayCount[t]++;
				}
			}
		} else {
			if(available.length > 13) return;
			for (int i1 = 0; i1 < available.length; i1++) {
				for (int i2 = i1 + 1; i2 < available.length; i2++) {
					for (int i3 = i2 + 1; i3 < available.length; i3++) {
						Match[] pr = new Match[3];
						pr[0] = available[i1];
						pr[1] = available[i2];
						pr[2] = available[i3];
						ways[t][wayCount[t]] = pr;
						wayCount[t]++;
					}
				}
			}
			if(twoMatchMoveCount < twoMatchMoveCountLimit) {
				for (int i1 = 0; i1 < available.length; i1++) {
					for (int i2 = i1 + 1; i2 < available.length; i2++) {
						Match[] pr = prepare(required, 2);
						pr[0] = available[i1];
						pr[1] = available[i2];
						ways[t][wayCount[t]] = pr;
						wayCount[t]++;
					}
				}
			}
		}
	}
	
	boolean hasWrongNode() {
		for (int n = 0; n < field.nodes.length; n++) {
			Node node = field.nodes[n];
			if(node.burningTime >= 0) continue;
			int q = 0;
			for (int k = 0; k < node.matchs.length; k++) {
				Match m = node.matchs[k];
				if(m.burningTime >= 0) continue;
				Node other = m.node1 == node ? m.node2 : m.node1;
				if(other.burningTime < t && other.burningTime >= 0) ++q;
			}
			if(q >= 4) return true;
		}
		return false;
	}
	
	Match[] prepare(Match[] required, int dim) {
		Match[] res = new Match[dim];
		System.arraycopy(required, 0, res, res.length - required.length, required.length);
		return res;
	}
	
	Match[] getRequiredMatchs() {
		ArrayList l = new ArrayList();
		for (int i = 0; i < burnedNodes[t].length; i++) {
			Node n = burnedNodes[t][i];
			for (int k = 0; k < n.matchs.length; k++) {
				Match m = n.matchs[k];
				if(m.burningTime >= 0 || l.contains(m)) continue;
				Node other = (m.node1 == n) ? m.node2 : m.node1;
				if(other.burningTime >= 0) l.add(m);
			}
		}
		return (Match[])l.toArray(NO_MATCHS);
	}
	
	Match[] getAvailableMatchs() {
		ArrayList l = new ArrayList();
		for (int i = 0; i < burnedNodes[t].length; i++) {
			Node n = burnedNodes[t][i];
			for (int k = 0; k < n.matchs.length; k++) {
				Match m = n.matchs[k];
				if(m.burningTime >= 0 || l.contains(m)) continue;
				Node other = (m.node1 == n) ? m.node2 : m.node1;
				if(other.burningTime < 0) l.add(m);
			}
		}
		return (Match[])l.toArray(NO_MATCHS);
	}
	
	void move() {
		Match[] ms = ways[t][way[t]];
		if(ms.length == 2) twoMatchMoveCount++;
		burnedMatchs[t] = ms;
		for (int i = 0; i < ms.length; i++) {
			ms[i].burningTime = t;
			matchsToBurn--;
		}
		ArrayList l = new ArrayList();
		for (int i = 0; i < ms.length; i++) {
			if(ms[i].node1.burningTime < 0 && !l.contains(ms[i].node1)) l.add(ms[i].node1);
			if(ms[i].node2.burningTime < 0 && !l.contains(ms[i].node2)) l.add(ms[i].node2);
		}
		Node[] ns = (Node[])l.toArray(NO_NODES);
		burnedNodes[t + 1] = ns;
		for (int i = 0; i < ns.length; i++) {
			ns[i].burningTime = t + 1;
			nodesToBurn--;
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		Node[] ns = burnedNodes[t + 1];
		Match[] ms = ways[t][way[t]];
		if(ms.length == 2) twoMatchMoveCount--;
		for (int i = 0; i < ns.length; i++) {
			ns[i].burningTime = -1;
			nodesToBurn++;
		}
		for (int i = 0; i < ms.length; i++) {
			ms[i].burningTime = -1;
			matchsToBurn++;
		}
	}
	
	void anal() {
		srch();
		way[t] = -1;
		int mbm = matchsToBurn; 
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(matchsToBurn < mbm) {
				mbm = matchsToBurn;
				System.out.println("-->" + matchsToBurn);
			}
			if(matchsToBurn == 0) {
				System.out.println("Solution found!");
				printSolution();
				return;
			}
		}
	}
	
	void printSolution() {
		for (int i = 0; i < field.size; i++) {
			int n = field.indexToNode[i];
			if(n < 0) {
				System.out.print(" " + "+");
			} else {
				int tt = field.nodes[n].burningTime;
				char c = (char)(tt >= 26 ? 65 + tt - 26 : 97 + tt);
				System.out.print(" " + c);
			}
			if(field.x[i] == field.width - 1) {
				System.out.println("");
			}
		}
	}

}
