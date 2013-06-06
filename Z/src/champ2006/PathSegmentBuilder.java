package champ2006;

import java.util.*;
import com.slava.common.RectangularField;

public class PathSegmentBuilder {
	PathSegment[] segments;
	RectangularField testField = new RectangularField();
	
	int minLength;
	int maxLength;
	
	int[] state;
	
	int t;
	int[] place;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	Map codes = new HashMap();
	
	ArrayList list = new ArrayList();
	
	public void build(int minLength, int maxLength) {
		this.maxLength = maxLength;
		this.minLength = minLength;
		testField.setSize(20, 20);
		solve();
	}
	
	void solve() {
		init();
		anal();
		segments = (PathSegment[])list.toArray(new PathSegment[0]);
		System.out.println(segments.length + " " + codes.size());
	}
	
	void init() {
		state = new int[testField.getSize()];
		wayCount = new int[10];
		way = new int[10];
		ways = new int[10][4];
		place = new int[10];
		t = 0;
		list.clear();
		codes.clear();
		place[0] = testField.getIndex(10, 10);
		state[place[0]] = 1;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == maxLength) return;
		for (int d = 0; d < 4; d++) {
			int q = testField.jump(place[t], d);
			if(state[q] > 0) continue;
			ways[t][wayCount[t]] = d;
			wayCount[t]++;
		}
	}
	
	void move() {
		int d = ways[t][way[t]];
		int p = testField.jump(place[t], d);
		place[t + 1] = p;
		state[p] = 1;
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int d = ways[t][way[t]];
		int p = testField.jump(place[t], d);
		state[p] = 0;
	}
	
	void anal() {
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(t >= minLength && t <= maxLength) {
				addSegment();
			}
		}		
	}
	
	void addSegment() {
		int[] path = new int[t];
		for (int i = 0; i < t; i++) {
			path[i] = ways[i][way[i]];
		}
		String code = PathSegment.getCode(path);
		if(!codes.containsKey(code)) {
			Integer n = new Integer(codes.size());
			codes.put(code, n);
		}
		int index = ((Integer)codes.get(code)).intValue();
		PathSegment s = new PathSegment(path, index);
		list.add(s);
	}
	
	void printSegments() {
		for (int i = 0; i < segments.length; i++) {
			int[] path = segments[i].path;
			for (int j = 0; j < path.length; j++) {
				System.out.print(path[j]);
			}
			System.out.println(" index=" + segments[i].index + " code=" + PathSegment.getCode(path));
		}
	}

}
