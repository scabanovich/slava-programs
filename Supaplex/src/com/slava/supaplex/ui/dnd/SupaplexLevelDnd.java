package com.slava.supaplex.ui.dnd;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import com.slava.supaplex.model.SupaplexLevel;
import com.slava.supaplex.ui.SupaplexLevelList;

public class SupaplexLevelDnd implements DragGestureListener, DragSourceListener, DropTargetListener {
	DropTarget dropTarget;
	CustomDragSource dragSource;
	SupaplexLevelList list;
	
	public SupaplexLevelDnd(SupaplexLevelList list) {
		this.list = list;
	}
	
	public void activate() {
		dragSource = new CustomDragSource();
		dragSource.createDefaultDragGestureRecognizer(list.getList(), DnDConstants.ACTION_COPY_OR_MOVE, this);
		dropTarget = new DropTarget(list.getList(), DnDConstants.ACTION_COPY_OR_MOVE, this);
		dropTarget.setActive(true);
	}

	public void dragOver(DropTargetDragEvent dtde) {
		DataFlavor[] fs = dtde.getCurrentDataFlavors();
		for (int i = 0; i < fs.length; i++) {
			if(fs[i].equals(SupaplexLevelDataFlavor.instance)) {
				dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
				return;
			}
		}
		dtde.rejectDrag();
	}

	public void dropActionChanged(DropTargetDragEvent dtde) {}

	public void drop(DropTargetDropEvent dtde) {
		Transferable t = dtde.getTransferable();
		//DataFlavor[] fs = dtde.getCurrentDataFlavors();
		int index = list.getIndex(dtde.getLocation());
		if(t.isDataFlavorSupported(SupaplexLevelDataFlavor.instance)) {
			try {
				SupaplexLevelTransferData data = (SupaplexLevelTransferData)t.getTransferData(SupaplexLevelDataFlavor.instance);
				SupaplexLevel level = new SupaplexLevel();
				level.setModel(list.getModel());
				level.loadFromBytes(data.getBytes());
				if(index >= 0) {
					list.getModel().insertLevel(level, index);
				} else {
					list.getModel().addLevel(level);
				}
				dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
			} catch (Exception e) {
				e.printStackTrace();
				dtde.rejectDrop();
			}
			return;
		}
		dtde.rejectDrop();
	}

	public void dragExit(DropTargetEvent dte) {}

	public void dragGestureRecognized(DragGestureEvent dge) {
		Point p = dge.getDragOrigin();
		int index = list.getIndex(p);
		if(index < 0) return;
		Cursor c = ("1.3.1_01".equals(System.getProperty("java.version")))
			? DragSource.DefaultCopyDrop : DragSource.DefaultCopyNoDrop;
		Transferable t = getTransferable(index);
		if(t != null) dragSource.startDrag(dge, c, t, this);
	}
	
	private Transferable getTransferable(int i) {
		SupaplexLevel level = list.getModel().getLevel(i);
		return (level == null) ? null : new SupaplexLevelTransferable(level);
	}

	public void dragEnter(DragSourceDragEvent dsde) {}
	public void dragOver(DragSourceDragEvent dsde) {}
	public void dropActionChanged(DragSourceDragEvent dsde) {}
	public void dragDropEnd(DragSourceDropEvent dsde) {}
	public void dragExit(DragSourceEvent dse) {}
	public void dragEnter(DropTargetDragEvent dtde) {}

}
