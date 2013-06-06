package champ2006;

public class PathSegment {
	int index;
	int[] path;
	
	public PathSegment(int[] path, int index) {
		this.path = path;
		this.index = index;
	}
	
	public static String getCode(int[] path) {
		int[] path1 = reduce(path);
		int[] path2 = reduce(revert(path));
		path = (compare(path1, path2) > 0) ? path2 : path1;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < path.length; i++) {
			sb.append(path[i]);
		}
		return sb.toString();		
	}
	
	public static int[] revert(int[] path) {
		int[] p = new int[path.length];
		for (int i = 0; i < p.length; i++) {
			p[i] = path[p.length - 1 - i];
		}
		return p;
	}

	public static int[] reduce(int[] path) {
		int[] p = new int[path.length];
		int delta = path[0];
		for (int i = 0; i < p.length; i++) {
			p[i] = path[i] - delta;
			if(p[i] < 0) p[i] += 4;
		}
		int f = 0;
		for (int i = 0; i < p.length && f == 0; i++) {
			f = p[i];
		}
		if(f == 3) {
			for (int i = 0; i < p.length; i++) {
				if(p[i] == 3) p[i] = 1; else if(p[i] == 1) p[i] = 3;
			}
		}
		return p;
	}
	
	public static int compare(int[] path1, int[] path2) {
		if(path1.length < path2.length) return -1;
		if(path1.length > path2.length) return 1;
		for (int i = 0; i < path1.length; i++) {
			if(path1[i] < path2[i]) return -1;
			if(path1[i] > path2[i]) return 1;
		}
		return 0;
	}

}
