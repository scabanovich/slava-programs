package match2013;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Equations {
	Map<String, Set<int[]>> map = new HashMap<String, Set<int[]>>();

	public void buildMap() {
		double p = 1;
		for (int i = 0; i < 4; i++) p *= runToMap(new int[]{2,1});
		p *= runToMap(new int[]{1,2});
		p *= runToMap(new int[]{1,2,1,1});
		for (int i = 0; i < 2; i++) p *= runToMap(new int[]{1,1,1});
		p *= runToMap(new int[]{1,1,1,1});
		p *= runToMap(new int[]{1,1,2,2});
		p *= runToMap(new int[]{1,1,3,3});
		p *= runToMap(new int[]{1,2,2});
		p *= runToMap(new int[]{1,2,3});
		p *= runToMap(new int[]{1,3,2});
		p *= runToMap(new int[]{1,3,1});
		p *= runToMap(new int[]{1,3,1,1});
		p *= runToMap(new int[]{1,4,1});
		p *= runToMap(new int[]{1,4,2});
		p *= runToMap(new int[]{2,1,1});
		p *= runToMap(new int[]{3,1,1});
		p *= runToMap(new int[]{3,1,2});
		for (int i = 0; i < 2; i++) p *= runToMap(new int[]{3,2,1});
		p *= runToMap(new int[]{5,3});
		System.out.println(p);
	}

	public long runToMap(int[] lengths) {
		Set<int[]> s = run(lengths);
		String c = "";
		for (int i = 0; i < lengths.length; i++) c += lengths[i];
		map.put(c, s);
		int l = 0;
		for (int i = 0; i < lengths.length; i++) l += lengths[i];
		long r = 1; for (int i = 0; i < l; i++) r *= 10;
		if(s.size() > 0) r /= s.size();
//		System.out.println(c + " " + r);
		return r;
	}
	
	public Set<int[]> run(int[] lengths) {
		Set<int[]> result = new HashSet<int[]>();
		int tm = 0;
		for (int i = 0; i < lengths.length; i++) {
			tm += lengths[i];
		}
		int t = 0;
		int[] used = new int[10];
		int[] ds = new int[tm + 1];
		for (int i = 0; i < ds.length; i++) ds[i] = -1;
		while(true) {
			while(ds[t] == 9 || t == tm) {
				if(t == 0) return result;
				t--;
				used[ds[t]]--;
			}
			ds[t]++;
			if(used[ds[t]] > 0) continue;
			used[ds[t]]++;
			t++;
			ds[t] = -1;
			if(t == tm) {
				int[] n = new int[lengths.length];
				int p = 0, k = 0, d = 0;
				boolean bad = false;
				for (int i = 0; i < tm; i++) {
					if(d == 0 && ds[i] == 0 && lengths[k] > 1) bad = true; //cannot start with 0
					p = p * 10 + ds[i];
					d++;
					if(d == lengths[k]) {
						n[k] = p;
						k++;
						d = 0;
						p = 0;
					}
				}
				String res = bad ? null : findEquation(n);
				if(res != null) {
					if(result.size() < 2) {
						for (int i = 0; i < n.length; i++) System.out.print(" " + n[i]);
						System.out.println(": " + res);
					}
					int[] dsc = new int[tm];
					System.arraycopy(ds, 0, dsc, 0, tm);
					result.add(dsc);
				}
			}
		}
	}

	public String findEquation(int[] n) {
		if(n.length < 2) return null;
		if(n.length == 2) {
			if(n[0] == n[1]) {
				return " " + n[0] + " = " + n[1] + " ";
			}
			if(isSquare(n[1]) && n[0] == (int)Math.sqrt(n[1])) return " " + n[0] + " = " + "sqrt( " + n[1] + " ) ";
			if(isSquare(n[0]) && n[1] == (int)Math.sqrt(n[0])) return " " + "sqrt( " + n[0] + " )" + " = " + n[1];
		} else {
			for (int i = 1; i < n.length; i++) {
				int[] m = new int[n.length - 1];
				System.arraycopy(n, 0, m, 0, i - 1);
				System.arraycopy(n, i + 1, m, i, n.length - i - 1);
				for (int w = 0; w < 4; w++) {
					if(w == 3 && (n[i] == 0 || (n[i - 1] % n[i] != 0))) continue;
					int q = n[i - 1];
					if(w == 0) q += n[i]; else if(w == 1) q -= n[i]; else if(w == 2) q *= n[i];
					                      else if(w == 3) q /= n[i];
					m[i - 1] = q;
					String res = findEquation(m);
					if(res != null) {
						String s = " " + q + " ";
						String[] c = new String[]{" + ", " - ", " * ", " / "};
						int k = res.indexOf(s);
						String result = res.substring(0, k + 1) + "( " + n[i - 1] + c[w] + n[i] + " )" + res.substring(k + s.length() - 1);
						return result;
					}
				}
			}
		}
		for (int i = 0; i < n.length; i++) {
			if(n[i] > 1 && isSquare(n[i])) {
				int c = n[i], q = (int)Math.sqrt(n[i]);
				n[i] = q;
				String res = findEquation(n);				
				n[i] = c;
				if(res != null) {
					String s = " " + q + " ";
					int k = res.indexOf(s);
					String result = res.substring(0, k + 1) + "sqrt( " + c + " )" + res.substring(k + s.length() - 1);
					return result;
				}
			}
		}
		
		return null;
	}

	boolean isSquare(int n) {
		int q = (int)Math.sqrt(n);
		return q * q == n;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Equations p = new Equations();
		long l = p.runToMap(new int[]{5,4,1});
		System.out.println("--->" + l);
//		p.buildMap();
//		Set<String> result = p.run(new int[]{1,1,3,3});
//		System.out.println(result.size());
		String result = p.findEquation(new int[]{8,4,650,712});
		System.out.println(result);

	}

}

/**
 5 3 + + 1 6 + + 7 + + +
 2 + + 1 6 + + + 0 + 4 5
 1 + + 8 + + 7 2 9 + 6 +
 9 + + + + + + + + + 8 1
 + + 4 + + + + 1 6 + + +
 + 9 0 + + 1 + + 8 3 + 6
 + + 9 + 5 0 3 + 4 + + 2
 7 + 6 + + 8 + + + + 3 4
 3 + + + + + + + + + 9 +
 4 + 8 3 7 + 2 9 + + + +
 6 + + 2 + + 5 + + 8 1 +
 8 + + 4 + 3 6 + 5 1 2 +
 
 1,1,1,1,1,0,0,0,0,1,1,1,
 0,0,0,0,0,0,1,1,0,0,0,1,
 0,0,1,0,0,1,1,0,0,1,0,1,
 0,1,1,1,0,0,1,0,1,1,0,0,
 0,0,1,0,0,0,0,0,1,1,0,0,
 1,0,0,0,1,0,0,0,0,0,0,1,
 1,0,0,1,1,0,0,0,0,0,1,1,
 1,0,0,0,1,0,1,1,0,1,1,0,
 1,1,0,0,1,0,0,1,0,0,0,0,
 0,0,0,0,0,0,0,1,1,0,1,0,
 1,1,1,0,1,1,0,0,0,0,1,0,
 1,0,1,0,0,1,1,1,0,1,1,1,

*/