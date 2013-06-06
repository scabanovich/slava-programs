package com.slava.supaplex.ui.dnd;

import java.awt.datatransfer.DataFlavor;

public class SupaplexLevelDataFlavor extends DataFlavor {
	public static final SupaplexLevelDataFlavor instance = new SupaplexLevelDataFlavor();
	
	public SupaplexLevelDataFlavor() {
		super(SupaplexLevelTransferData.class, "Supaplex Level");
	}

}
