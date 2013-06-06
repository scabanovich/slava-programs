package com.slava.supaplex.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import com.slava.supaplex.model.*;

public class SupaplexLevelComponent extends JComponent {
	SupaplexImages images = new SupaplexImages();
	SupaplexModel model;
	int cellSize = 18;
	SupaplexLevel level;
	int maxX, maxY;
	
	public void setModel(SupaplexModel model) {
		this.model = model;
		setPreferredSize(new Dimension(maxX = cellSize * model.getField().getWidth(), maxY = cellSize * model.getField().getHeight()));
		addMouseListener(new ML());
		addMouseMotionListener(new MML());
	}
	
	public void setLevel(SupaplexLevel level) {
		this.level = level;
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		Rectangle clip = g.getClipBounds();
		if(level == null) {
			g.setColor(Color.GRAY);
			g.fillRect(0, 0, maxX, maxY);
		} else {
			SupaplexField f = model.getField();
			byte[] b = level.getBytes();
			for (int i = 0; i < f.getSize(); i++) {
				int x = cellSize * f.x(i);
				int y = cellSize * f.y(i);
				if(x > clip.width + clip.x) continue;
				if(y > clip.height + clip.y) continue;
				if(x + cellSize < clip.x) continue;
				if(y + cellSize < clip.y) continue;
				g.setColor(images.getColor(b[i]));
				g.fillRect(x + 1, y + 1, cellSize - 2, cellSize - 2);
				Image image = images.getImage(b[i]);
				if(image != null) g.drawImage(image, x + 1, y + 1, this);
			}
			GravityPlace[] gs = level.getGravityPlaces().getGravityPlaces();
			for (int i = 0; i < gs.length; i++) {
				int p = gs[i].getPlace();
				int x = cellSize * f.x(p);
				int y = cellSize * f.y(p);
				if(x > clip.width + clip.x) continue;
				if(y > clip.height + clip.y) continue;
				if(x + cellSize < clip.x) continue;
				if(y + cellSize < clip.y) continue;
				x = x + (cellSize / 2);
				y = y + (cellSize / 2);
				if(gs[i].getMode() == 0) {
					g.setColor(Color.WHITE);
				} else {
					g.setColor(Color.PINK);
				}
				g.fillOval(x - 3, y - 3, 6, 6);
			}
		}
	}
	
	boolean isDragging = false;
	
	class ML extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			isDragging = true;
		}

	    public void mouseReleased(MouseEvent e) {
	    	isDragging = false;
	    	applyTool(e.getPoint());
	    }		
	}
	
	class MML extends MouseMotionAdapter {
		public void mouseDragged(MouseEvent e) {
			if(isDragging) applyTool(e.getPoint());
		}
	}
	
	private void applyTool(Point p) {
		SupaplexField f = model.getField();
		int ix = p.x / cellSize;
		int iy = p.y / cellSize;
		int i = iy * f.getWidth() + ix;
		model.applySelectedToolItem(i);
		int x = cellSize * f.x(i);
		int y = cellSize * f.y(i);
		repaint(x, y, cellSize, cellSize);
	}
	
}
