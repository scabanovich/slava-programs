package com.slava.regions;

import java.util.ArrayList;

public class RegionsState {
	static byte[] EMPTY = new byte[0];
	byte[] lengths = EMPTY;
	byte[] numbers = EMPTY;
	RegionsState parent;
	
	public RegionsState() {}
	
	public RegionsState(byte[] lengths, byte[] numbers, RegionsState parent) {
		this.parent = parent;
		this.lengths = lengths;
		this.numbers = numbers;
	}
	
	public byte[] getLengths() {
		return lengths;
	}
	
	public byte[] getNumbers() {
		return numbers;
	}
	
	public boolean equals(RegionsState state) {
		if(state.lengths.length != lengths.length) return false;
		for (int i = 0; i < lengths.length; i++) {
			if(state.lengths[i] != lengths[i]) return false;
			if(state.numbers[i] != numbers[i]) return false;
		}
		return true;
	}
	
	public RegionsState[] getNeighbourStates() {
		ArrayList list = new ArrayList();
		for (int i = 0; i < lengths.length; i++) {
			if(numbers[i] == 0) continue;
			for (int j = 1; j < lengths[i]; j++) {
				byte[] ls = new byte[lengths.length + 1];
				byte[] ns = new byte[lengths.length + 1];
				for (int k = 0; k < i; k++) {
					ls[k] = lengths[k];
					ns[k] = numbers[k];
				}
				ls[i] = (byte)j;
				ns[i] = (byte)(numbers[i] / 2);
				ls[i + 1] = (byte)(lengths[i] - j);
				ns[i + 1] = (byte)(numbers[i] - ns[i]);
				for (int k = i + 1; k < lengths.length; k++) {
					ls[k + 1] = lengths[k];
					ns[k + 1] = numbers[k];
				}
				list.add(new RegionsState(ls, ns, this));
				if(ns[i] != ns[i + 1]) {
					ls = (byte[])ls.clone();
					ns = (byte[])ns.clone();
					int c = ns[i];
					ns[i] = ns[i + 1];
					ns[i + 1] = (byte)c;
					list.add(new RegionsState(ls, ns, this));
				}
			}
		}
		for (int i = 0; i < lengths.length - 1; i++) {
			byte[] ls = new byte[lengths.length - 1];
			byte[] ns = new byte[lengths.length - 1];
			for (int k = 0; k < i; k++) {
				ls[k] = lengths[k];
				ns[k] = numbers[k];
			}
			ls[i] = (byte)(lengths[i] + lengths[i + 1]);
			ns[i] = (byte)(numbers[i] + numbers[i + 1]);
			for (int k = i + 2; k < lengths.length; k++) {
				ls[k - 1] = lengths[k];
				ns[k - 1] = numbers[k];
			}
			list.add(new RegionsState(ls, ns, this));
		}
		return (RegionsState[])list.toArray(new RegionsState[0]);
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append('[');
		for (int i = 0; i < lengths.length; i++) {
			if(numbers[i] < 10) {
				sb.append('.');
			}
			sb.append(numbers[i]);
			for (int k = 1; k < lengths[i]; k++) sb.append("..");
		}
		sb.append(']');
		return sb.toString();
	}

}
