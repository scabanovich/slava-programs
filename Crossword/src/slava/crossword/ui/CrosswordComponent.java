package slava.crossword.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import slava.crossword.undo.NetChange;
import slava.crossword.undo.UndoManager;
import slava.crossword.undo.UndoableChange;
import slava.crossword.undo.ValueChange;

import java.awt.image.*;

public class CrosswordComponent extends JComponent {
	private static final long serialVersionUID = 1L;
	CrosswordComponentModel model;
    int cellWidth = 20;
    int padding = 10;
    int selectedCell = -1;

    Image a;

    public CrosswordComponent() {
        this(new CrosswordComponentModel());
        int[] px = new int[144];
        for (int i = 0; i < 144; i++) px[i] = 0xffc0cf00;
        px[0] = 0x00000000;
        ImageProducer p = new MemoryImageSource(12, 12, px, 0, 12);
        JPanel pn = new JPanel();
        a = pn.createImage(p);
    }

    public CrosswordComponent(CrosswordComponentModel model) {
        this.model = model;
        model.addListener(this);
        ML ml = new ML();
        addMouseListener(ml);
        addMouseMotionListener(ml);
        addKeyListener(new KL());
    }

    public CrosswordComponentModel getModel() {
        return model;
    }

    public void updateSize() {
        int w = model.getWidth();
        int h = model.getHeight();
        setPreferredSize(new Dimension(w * cellWidth + 2 * padding, h * cellWidth + 2 * padding));
        repaint();
    }

    public void paint(Graphics g) {
        super.paint(g);
        if(model == null) return;
        //long t1 = System.currentTimeMillis();
        int w = model.getWidth();
        int h = model.getHeight();
        int xmin = padding, xmax = xmin + w * cellWidth, ymin = padding, ymax = ymin + h * cellWidth;
        for (int i = 0; i < w + 1; i++)
          g.drawLine(xmin + i * cellWidth, ymin, xmin + i * cellWidth, ymax);
        for (int i = 0; i < h + 1; i++)
          g.drawLine(xmin, ymin + i * cellWidth, xmax, ymin + i * cellWidth);
        int[] net = model.net;
        char[] content = model.getCharContent();
        for (int i = 0; i < net.length; i++) {
            int x = xmin + cellWidth * model.x[i], y = ymin + cellWidth * model.y[i];
            Color c = (net[i] == 0) ? Color.black.brighter() :
                      (net[i] == 2) ? Color.gray :
                      (i == selectedCell) ? Color.cyan.brighter() : Color.white;
            g.setColor(c);
            g.fillRect(x + 1, y + 1, cellWidth - 1, cellWidth - 1);
            if(content[i] != ' ') {
                g.setColor(Color.black);
                g.drawString("" + content[i], x + 5, y + 14);
            }
//            g.drawImage(a, x + 4, y + 4, 12, 12, null);
        }
    }

    public void paintCell(int i) {
        int x = padding + cellWidth * model.x[i], y = padding + cellWidth * model.y[i];
        repaint(x, y, cellWidth, cellWidth);
    }

    public int getCell(Point p) {
        int w = model.getWidth();
        int h = model.getHeight();
        int xmin = padding, xmax = xmin + w * cellWidth, ymin = padding, ymax = ymin + h * cellWidth;
        if(p.x <= xmin || p.y <= ymin || p.x >= xmax || p.y >= ymax) return -1;
        int x = (p.x - xmin) / cellWidth;
        int y = (p.y - ymin) / cellWidth;
        return x + y * w;
    }

    class ML extends MouseInputAdapter {
        int dragCell = -1;
        int dragValue = -1;
        public void mousePressed(MouseEvent e) {
            requestFocus();
            if(selectedCell >= 0) {
                int i = selectedCell;
                selectedCell = -1;
                paintCell(i);
            }
            dragCell = -1;
            dragValue = -1;
        }

        public void mouseDragged(MouseEvent e) {
            int dc = getCell(e.getPoint());
            if(dc < 0) return;
            int m = model.getColorMode();
            int c = model.getNetAt(dc);
            if(m == 1) {
				if(!model.canProcessKey(dc, ' ')) return;
				char oldValue = model.getCharContent()[dc];
//                model.processKey(dc, ' ');
                UndoableChange change = new ValueChange(model, dc, oldValue, ' ');
                change.redo();
                UndoManager.getInstance().addChange(change);
            } else if(dragCell == -1) {
                dragValue = (c == 1) ? m : (c == m) ? 1 : -1;
                dragCell = (dragValue < 0) ? -1 : dc;
                if(dragValue != -1) {
                	changeNet(dc);
                } 
            } else {
                if(c == 1 || c == m) {
					changeNet(dc);
                }
            }
        }
        
        private void changeNet(int dc) {
        	int oldValue = model.getNetAt(dc);
			if(oldValue == dragValue) return;
			UndoableChange change = new NetChange(model, dc, oldValue, dragValue);
			change.redo();
			UndoManager.getInstance().addChange(change);
        }

        public void mouseReleased(MouseEvent e) {
            int dc = getCell(e.getPoint());
            if(dc >= 0 && model.getNetAt(dc) == 1 && (dragCell < 0 || dc == dragCell)) {
                int i = selectedCell;
                selectedCell = dc;
                if(i >= 0) paintCell(i);
                paintCell(dc);
            }
            dragCell = -1;
            dragValue = -1;
        }

    }

    protected void processComponentKeyEvent(KeyEvent e) {
        if(e.getID() != KeyEvent.KEY_PRESSED) return;
            if(selectedCell >= 0 && model.getNetAt(selectedCell) == 1) {
            	char ch = e.getKeyChar();
            	if(!model.canProcessKey(selectedCell, ch)) return;
				char oldValue = model.getCharContent()[selectedCell];
//				  model.processKey(dc, ' ');
				UndoableChange change = new ValueChange(model, selectedCell, oldValue, ch);
				change.redo();
				UndoManager.getInstance().addChange(change);
            	
//                boolean b = model.processKey(selectedCell, e.getKeyChar());
//                if(!b) return;
                int i = selectedCell + 1;
                int[] net = model.net;
                while(i < net.length && net[i] != 1) ++i;
                if(i < net.length) {
                    selectedCell = i;
                    paintCell(i);
                }
            }
    }

    class KL extends KeyAdapter {
    }

}