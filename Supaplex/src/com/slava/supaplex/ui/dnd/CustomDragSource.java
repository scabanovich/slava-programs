package com.slava.supaplex.ui.dnd;

import java.awt.dnd.peer.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.awt.*;

public class CustomDragSource extends DragSource {
	private static DragSource dflt;

	public static DragSource getDefaultDragSource() {
		if (dflt == null) dflt = new CustomDragSource();
		return dflt;
	}

	protected DragSourceContext createDragSourceContext(DragSourceContextPeer dscp,
				DragGestureEvent dgl, Cursor dragCursor, Image dragImage, Point imageOffset, Transferable t, DragSourceListener dsl) {
		return new CustomDragSourceContext(dscp, dgl, dragCursor, dragImage, imageOffset, t, dsl);
	}

}

class CustomDragSourceContext extends DragSourceContext {
	public CustomDragSourceContext(DragSourceContextPeer dscp, DragGestureEvent trigger, Cursor dragCursor, Image dragImage, Point offset, Transferable t, DragSourceListener dsl) {
		super(dscp,trigger,dragCursor,dragImage,offset,t,dsl);
	}

	protected void updateCurrentCursor(int dropOp, int targetAct, int status) {
		Cursor c = null;
		switch (status) {
		default:
			targetAct = DnDConstants.ACTION_NONE;
		case ENTER:
		case OVER:
		case CHANGED:
			int ra = dropOp & targetAct;
			if (ra == DnDConstants.ACTION_NONE) { // no drop possible
				if ((dropOp & DnDConstants.ACTION_LINK) == DnDConstants.ACTION_LINK)
					c = DragSource.DefaultLinkNoDrop;
				else if ((dropOp & DnDConstants.ACTION_MOVE) == DnDConstants.ACTION_MOVE)
					c = DragSource.DefaultMoveNoDrop;
				else
					c = DragSource.DefaultCopyNoDrop;
			} else { // drop possible
				if ((ra & DnDConstants.ACTION_LINK) == DnDConstants.ACTION_LINK)
					c = DragSource.DefaultLinkDrop;
				else if ((ra & DnDConstants.ACTION_MOVE) == DnDConstants.ACTION_MOVE)
					c = DragSource.DefaultMoveDrop;
				else
					c = DragSource.DefaultCopyDrop;
			}
		}
		setCursor(c);
	}

}
