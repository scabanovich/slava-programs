package com.slava.supaplex.model;

import java.util.Properties;

public class SupaplexLevel {
	static int FLAG_COUNT = 5;
	String name = "";
	byte[] flags = new byte[FLAG_COUNT];
	byte lastFlag = 0;
	int count;
	byte[] distribution = new byte[64];
	GravityPlaces gravityPlaces = new GravityPlaces();
	byte[] state;
	SupaplexModel model;
	
	public SupaplexLevel() {
		gravityPlaces.setLevel(this);
	}
	
	public void setModel(SupaplexModel model) {
		this.model = model;
		state = new byte[model.getField().getSize()];
		for (int i = 0; i < model.getField().getSize(); i++) {
			if(model.getField().isOnBorder(i)) state[i] = (byte)6;
		}
	}
	
	public SupaplexModel getModel() {
		return model;
	}
	
	public void loadFromBytes(byte[] bytes) {
		System.arraycopy(bytes, 0, state, 0, state.length);
		System.arraycopy(bytes, state.length, flags, 0, flags.length);
		String n = new String(bytes, state.length + flags.length + 1, 23);
		n = n.trim();
		while(n.startsWith("-")) n = n.substring(1);
		while(n.endsWith("-")) n = n.substring(0, n.length() - 1);
		name = n.trim();
		lastFlag = bytes[state.length + flags.length + 1 + 23];
		count = (int)bytes[state.length + flags.length + 1 + 23 + 1];
		if(count < 0) count += 256;
		System.arraycopy(bytes, bytes.length - 64, distribution, 0, 64); 

		for (int i = 0; i < model.getField().getSize(); i++) {
			if(model.getField().isOnBorder(i)) state[i] = (byte)6;
		}
		byte gc = bytes[state.length + flags.length + 1 + 23 + 2];
		gravityPlaces.load(gc, distribution);
		
	}
	
	public byte[] getBytes() {
		byte[] b = new byte[state.length + 96];
		System.arraycopy(state, 0, b, 0, state.length);
		System.arraycopy(flags, 0, b, state.length, flags.length);
		b[state.length + flags.length] = (byte)32;
		byte[] nb = ("-" + getFullName() + "-").getBytes();
		System.arraycopy(nb, 0, b, state.length + flags.length + 1, nb.length);
		b[state.length + flags.length + 1 + nb.length] = lastFlag;
		b[state.length + flags.length + 1 + nb.length + 1] = (byte)count; 
		b[state.length + flags.length + 1 + nb.length + 2] = (byte)gravityPlaces.getGravityPlaces().length; 
		System.arraycopy(distribution, 0, b, b.length - 64, 64); 
		return b;
	}
	
	public String getName() {
		return name;
	}
	
	public String getFullName() {
		String n = name;
		if(n.length() > 19) n = n.substring(0, 19);
		n = " " + n + " ";
		while(n.length() < 21) n = "-" + n + "-";
		if(n.length() == 22) n = n.substring(1);
		return n;
	}
	
	public int getCount() {
		return count;
	}
	
	public boolean isJumping() {
		return flags[4] == (byte)1;
	}

	public byte[] getState() {
		return state;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void editName(String name) {
		name = name.trim();
		this.name = name;
		Properties p = new Properties();
		p.setProperty("kind", "levelRenamed");
		model.fire(p);
	}
	
	public void editGravity(String s) {
		flags[4] = ("true".equals(s)) ? (byte)1 : (byte)0; 
	}
	
	public void editCount(String s) {
		int i = -1;
		try {
			i = Integer.parseInt(s);
		} catch (Exception e) {}
		if(i < 0) return;
		if(i > 254) i = 254;
		count = i;
	}

	public void paintAllWith(byte b) {
		for (int i = 0; i < model.getField().getSize(); i++) {
			if(!model.getField().isOnBorder(i)) state[i] = b;
		}
	}
	
	public GravityPlaces getGravityPlaces() {
		return gravityPlaces;
	}
	
	public void applySelectedToolItem(int place, byte tool, int gravityMode) {
		getState()[place] = tool;
		gravityPlaces.setGravityPlace(place, gravityMode);
		
	}
	
	public String getParameter2() {
		return "" + flags[1];
	}
	
	public String getParameter4() {
		return "" + flags[3];
	}
	
	public String getParameterLast() {
		return "" + lastFlag;
	}
	
	public void editParameter2(String v) {
		byte b = parseByte(v);
		if(b != (byte)255) flags[1] = b;
	}
	
	public void editParameter4(String v) {
		byte b = parseByte(v);
		if(b != (byte)255) flags[3] = b;
	}
	
	public void editParameterLast(String v) {
		byte b = parseByte(v);
		if(b != (byte)255) lastFlag = b;
	}
	
	static byte parseByte(String s) {
		byte i = (byte)255;
		try {
			i = Byte.parseByte(s);
		} catch (Exception e) {}
		return i;
	}
	
}
