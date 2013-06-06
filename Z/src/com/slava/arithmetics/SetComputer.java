package com.slava.arithmetics;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class SetComputer {
	NumberSet initial;
	Set<NumberSet> set;
	Set<Integer> allresults;

	public SetComputer() {}

	public void compute() {
		set = new HashSet<NumberSet>();
		allresults = new TreeSet<Integer>();
		set.add(initial);
		while(!set.isEmpty()) {
			set = iterate();
			NumberSet q = find(24);
			if(q != null) {
				q.printWithHistory();
			}
		}
		System.out.println(allresults.contains(24));
	}

	NumberSet find(int number) {
		for (NumberSet s: set) {
			if(s.getNumbers().length == 1 && s.getNumbers()[0] == number) {
				return s;
			}
		}
		return null;
	}

	Set<NumberSet> iterate() {
		Set<NumberSet> next = new HashSet<NumberSet>();
		for (NumberSet s: set) {
			for (int i: s.getNumbers()) {
				allresults.add(i);
			}
			next.addAll(s.applyAction());
		}
		return next;
	}

	
	public static void main(String[] args) {
		SetComputer c = new SetComputer();
		NumberSet initial = new NumberSet(new int[]{1,3,4,6});
		c.initial = initial;
		c.compute();
	}

}
