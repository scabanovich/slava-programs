package com.slava.supaplex.ui.dnd;

import java.io.IOException;
import java.io.Serializable;

public class SupaplexLevelTransferData implements Serializable {
	String id;
	byte[] bytes;
	
	public SupaplexLevelTransferData(byte[] bytes, String id) {
		this.bytes = bytes;
		this.id = id;
	}
	
	public byte[] getBytes() {
		return bytes;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.write(bytes);
	}
	
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		id = "";
		bytes = new byte[60 * 24 + 96];
		int r = 0;
		while(r < bytes.length) {
			r += in.read(bytes, r, bytes.length - r);
		}
	}

}
