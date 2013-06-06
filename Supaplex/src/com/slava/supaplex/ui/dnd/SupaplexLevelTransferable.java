package com.slava.supaplex.ui.dnd;

import java.awt.datatransfer.*;
import java.io.IOException;

import com.slava.supaplex.model.SupaplexLevel;

public class SupaplexLevelTransferable implements Transferable {
	DataFlavor[] FLAVORS = new DataFlavor[]{SupaplexLevelDataFlavor.instance};
	SupaplexLevelTransferData data;
	
	public SupaplexLevelTransferable(SupaplexLevel level) {
		data = new SupaplexLevelTransferData(level.getBytes(), "" + level.getModel());
	}

	public DataFlavor[] getTransferDataFlavors() {
		return FLAVORS;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		for (int i = 0; i < FLAVORS.length; i++) {
			if(FLAVORS[i].equals(flavor)) return true;
		}
		return false;
	}

	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		for (int i = 0; i < FLAVORS.length; i++) {
			if(FLAVORS[i].equals(flavor)) return data;
		}
		return null;
	}

}
