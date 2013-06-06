package com.slava.supaplex.model;

public class GravityPlace {
	private int place;
	
	byte[] bytes = new byte[6];
	
	public static GravityPlace create(byte[] bs, int off) {
		if(bs.length <= off + 6) return null;
		GravityPlace p = new GravityPlace();
		System.arraycopy(bs, off, p.bytes, 0, 6);
		int i1 = bs[off + 0];
		if(i1 < 0) i1 +=256;
		int i2 = bs[off + 1];
		if(i2 < 0) i2 +=256;
		p.place = (i1 * 256 + i2) / 2;
		return p;
	}
	
	public void apply(byte[] bs, int off) {
		System.arraycopy(bytes, 0, bs, off, 6);
	}
	
	public int getPlace() {
		return place;
	}
	
	public int getMode() {
		return (int)bytes[2];
	}
	
	public void setPlace(int place) {
		if(this.place == place) return;
		this.place = place;
		int p = place * 2;
		bytes[0] = (byte)(p / 256);
		bytes[1] = (byte)(p % 256);
	}

}
