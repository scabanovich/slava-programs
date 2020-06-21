package miner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.slava.common.RectangularField;

public class MinerSolver2 implements MinerConstants {
	RectangularField f = new RectangularField();
	boolean reportProgress = true;

	int[] condition; // HIDDEN or >= 0
	int[] state;     //UNKNOWN  SAFE  MINE
	
	class Cells {
		Set<Integer> set = new HashSet<>();
		int mines;
		int place;

		public Cells (int place, int mines) {
			this.place = place;
			this.mines = mines;
		}
		
		public int size() {
			return set.size();
		}
		
		public void validate() {
			if (mines < 0 || mines > size()) {
				throw new RuntimeException("Error at " + place + " mines=" + mines + " size=" + size());
			}
		}
		
		public boolean reduce(Cells c, Map<Integer, Set<Cells>> all) {
			if (c == this) {
				return false;
			}
			if (size() >= c.size() && set.containsAll(c.set)) {
				set.removeAll(c.set);
				mines -= c.mines;
				for (int p: c.set) {
					if (all.containsKey(p)) {
						all.get(p).remove(this);
					}
				}
				validate();
				return true;
			}
			return false;
		}

		public boolean reduce2(int[] state, Cells c, Map<Integer, Set<Cells>> all) {
			if (c == this || c.mines > mines) {
				return false;
			}
			Set<Integer> s1 = new HashSet<>(set);
			Set<Integer> s2 = new HashSet<>(c.set);
			Set<Integer> s12 = new HashSet<>(s1);
			s12.retainAll(s2); //intersection
			if (s12.isEmpty()) {
				return false;
			}
			s1.removeAll(s12);
			s2.removeAll(s12);
			int lim = findLimit(s1, all);
			if (lim + c.mines == mines) {
				c.setValueInSet(state, all, s2, SAFE); // cannot be in s2
				if (lim == s1.size()) {
					setValueInSet(state, all, s1, MINE); // s1 is full
					reduce(c, all);
				} else if (reportProgress) {
					System.out.println("Reduced 3");
				}
				return true;
			}
			return false;
		}

		public int resolve(int[] state, Map<Integer, Set<Cells>> all) {
			int changes = 0;
			int v = (mines == set.size()) ? MINE : (mines == 0) ? SAFE : UNKNOWN;
			if (v != UNKNOWN) {
				changes = size();
				setValueInSet(state, all, set, v);
			}
			return changes;
		}
		
		void setValueInSet(int[] state, Map<Integer, Set<Cells>> all, Set<Integer> set, int v) {
			for (int p: set) {
				state[p] = v;
				if (v == MINE) {
					mines--;
				}
				all.get(p).remove(this);
				for (Cells c: all.get(p)) {
					c.set.remove(p);
					if (v == MINE) {
						c.mines--;
					}
					c.validate();
				}
				all.remove(p);
			}
			this.set.removeAll(set);
			validate();
		}

		@Override
		public int hashCode() {
			return place;
		}

		public boolean equals(Object o) {
			Cells c = (Cells)o;
			return this.place == c.place;
		}
	}
	
	public MinerSolver2() {
		f.setOrts(RectangularField.DIAGONAL_ORTS);
	}

	public void setReportProgress(boolean b) {
		this.reportProgress = b;
	}

	public void setSize(int width, int height) {
		f.setSize(width, height);
	}

	public void setCondition(int[] condition) {
		if (condition.length != f.getSize()) {
			throw new RuntimeException();
		}
		this.condition = condition;
		state = new int[condition.length];
		for (int p = 0; p < state.length; p++) {
			if (condition[p] > HIDDEN) {
				state[p] = SAFE;
			} else if (condition[p] < HIDDEN) {
				throw new RuntimeException();
			} else {
				state[p] = UNKNOWN;
			}
		}
	}

	public int getConditionSize() {
		int res = 0;
		for (int p = 0; p < condition.length; p++) {
			if (condition[p] > HIDDEN) {
				res++;
			}
		}
		return res;
	}
	
	int findLimit(Set<Integer> set, Map<Integer, Set<Cells>> all) {
		int result = set.size();
		if (result < 2) {
			return result;
		}
		int p = set.iterator().next();
		if (!all.containsKey(p)) {
			return result;
		}
		Set<Cells> cs = all.get(p);
		for (Cells c: cs) {
			Set<Integer> intersection = new HashSet<>(set);
			intersection.retainAll(c.set);
			int q = c.mines > intersection.size() ? intersection.size() : c.mines;
			if (q >= result) continue;
			Set<Integer> diff = new HashSet<>(set);
			diff.removeAll(intersection);
			q += findLimit(diff, all);
			if (q < result) {
				result = q;
			}
		}
		
		return result;
	}
	
	Map<Integer, Set<Cells>> buildSets() {
		Map<Integer, Set<Cells>> result = new HashMap<>();
		for (int p = 0; p < state.length; p++) {
			if (condition[p] > HIDDEN) {
				Cells c = new Cells(p, condition[p]);
				for (int d = 0; d < 8; d++) {
					int q = f.jump(p, d);
					if (q >= 0 && state[q] == UNKNOWN) {
						c.set.add(q);
						if (!result.containsKey(q)) {
							result.put(q, new HashSet<>());
						}
						result.get(q).add(c);
					}
				}
				c.validate();
			}

		}
		return result;
	}

	//returns changes
	int round(Map<Integer, Set<Cells>> all) {
		int changes = 0;
		Integer[] ps = all.keySet().toArray(new Integer[0]);
		for (int p: ps) {
			if (all.containsKey(p)) {
				Cells[] cs = all.get(p).toArray(new Cells[0]);
				for (Cells c: cs) {
					if (c.size() > 0) {
						changes += c.resolve(state, all);
					}
				}
			}
		}
		return changes;
	}

	boolean round2(Map<Integer, Set<Cells>> all) {
		for (int p: all.keySet()) {
			for (Cells c1: all.get(p)) {
				for (Cells c2: all.get(p)) {
					if (c1.reduce(c2, all)) {
						if (reportProgress) {
							System.out.println("Reduced");
						}
						return true;
					}
				}
			}
		}
		return false;
	}

	boolean round3(Map<Integer, Set<Cells>> all) {
		for (int p: all.keySet()) {
			for (Cells c1: all.get(p)) {
				for (Cells c2: all.get(p)) {
					if (c1.reduce2(state, c2, all)) {
						if (reportProgress) {
							System.out.println("Reduced 2");
						}
						return true;
					}
				}
			}
		}
		return false;
	}
	

	public void solve() {
		Map<Integer, Set<Cells>> all = buildSets();
		while (true) {
			int changes = round(all);

			if (reportProgress) {
				System.out.println(changes);
			}
			if (changes == 0 && !round2(all) && !round3(all)) {
				break;
			}
		}
	}

	public boolean isSolved() {
		for (int p = 0; p < state.length; p++) {
			if (state[p] == UNKNOWN) return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		int width = 6;
		int height = 6;
		int mines = 6;

		int attempt = 0;
		while (true) {
			attempt++;
			System.out.println("Attempt " + attempt);
			int[] condition = MinerGenerator.generateAndReduce(width, height, mines);
			MinerSolver2 s = new MinerSolver2();
			s.setSize(width, height);
			s.setCondition(condition);
			if (s.getConditionSize() > 150) continue;
			System.out.println("Numbers = " + s.getConditionSize());
			s.solve();
			if (s.isSolved()) {
				MinerGenerator.print(s.state, s.f);
				break;
			}
		}
	}

}
