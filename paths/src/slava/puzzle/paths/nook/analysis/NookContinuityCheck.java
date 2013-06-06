package slava.puzzle.paths.nook.analysis;

public class NookContinuityCheck {

	public static boolean check(NookField f, int[] state, int[] usage) {
		int[] visited = new int[state.length];
		int[] stack = new int[state.length];
		for (int p = 0; p < state.length; p++) {
			if(state[p] >= 0 || visited[p] > 0 || f.filter[p] == 1) continue;
			boolean access = false;
			int vc = 0;
			int vv = 1;
			stack[0] = p;
			visited[p] = 1;
			while(vc < vv) {
				int q = stack[vc];
				for (int d = 0; d < 8; d++) {
					int s = f.jump(q, d);
					if(s < 0 || visited[s] > 0) continue;
					if(state[s] >= 0) {
						int n = state[s];
						if(n > 0 && usage[n - 1] < 0) {
							access = true;
						} else if(n < f.getFilteredSize() - 1 && usage[n + 1] < 0) {
							access = true;
						}
					} else {
						visited[s] = 1;
						stack[vv] = s;
						vv++;
					}
				}
				vc++;
			}
			if(!access) return false;
		}
		return true;
	}
	
	public static int[] getUniqueMove(NookField f, int[] state, int[] usage) {
		int[] vs = new int[16];
		for (int p = 0; p < state.length; p++) {
			if(state[p] >= 0 || f.filter[p] == 1) continue;
			int vsl = 0;
			int c = 0;
			
			for (int d = 0; d < 8; d++) {
				int q = f.jump(p, d);
				if(q < 0 || f.filter[q] == 1) continue;
				if(state[q] < 0) {
					c++;
				} else {
					int n = state[q];
					if(n > 0 && usage[n - 1] < 0) {
						vs[vsl] = n - 1;
						vsl++;
					}
					if(n < f.getFilteredSize() - 1 && usage[n + 1] < 0) {
						vs[vsl] = n + 1;
						vsl++;
					}
				}
			}
			if(c > 1) continue;
			if(c < 2 && vsl == 0) {
				if(c == 1 && usage[0] >= 0 && usage[f.getFilteredSize() - 1] < 0) {
					return new int[]{p, f.getFilteredSize() - 1};
				}
				return new int[]{p, -1};
			}
			if(c == 1) {
				boolean severalWays = false;
				for (int i = 1; i < vsl; i++) {
					if(vs[i] != vs[0]) severalWays = true;
				}
				if(severalWays) continue;
				
				int v = vs[0];
				int v1 = v - 1;
				int delta = 1;
				while(v1 >= 0 && usage[v1] < 0) {
					v1--;
					delta++;
				}
				if(v1 >= 0 && f.getDistance(p, usage[v1]) > delta) {
					return new int[]{p, -1};
				}
				v1 = v + 1;
				delta = 1;
				while(v1 < f.getFilteredSize() && usage[v1] < 0) {
					v1++;
					delta++;
				}
				if(v1 < f.getFilteredSize() && f.getDistance(p, usage[v1]) > delta) {
					return new int[]{p, -1};
				}
				
				return new int[]{p, v};
			} else if(c == 0) {
				if(vsl == 1) {
					if(vs[0] == 0 || vs[0] == f.getFilteredSize() - 1) return new int[]{p, vs[0]};
				}
				
				if(vsl < 2) {
					if(vs[0] == f.getFilteredSize() - 1) {
						return new int[]{p, vs[0]};
					}
					return new int[]{p, -1};
				}
				if(vsl == 2) {
					if(vs[0] == f.getFilteredSize() - 1 || vs[1] == f.getFilteredSize() - 1) {
						return new int[]{p, f.getFilteredSize() - 1};
					}
					if(vs[0] != vs[1]) return new int[]{p, -1};
					return new int[]{p, vs[0]};
				}
				//TODO
			}
		}		
		return null;
	}

}
