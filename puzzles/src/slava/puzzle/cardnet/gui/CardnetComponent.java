package slava.puzzle.cardnet.gui;

import java.awt.*;
import java.awt.event.*;
import slava.puzzle.cardnet.model.*;
import slava.puzzle.template.gui.*;
import slava.puzzle.template.model.PuzzleModel;

public class CardnetComponent extends PuzzleComponent {
	CardnetField field;
	CardSet cards;
	CardnetPuzzleInfo puzzle;
	int cellSize = 30;
	int halfCellSize = (cellSize / 2);
	int infoCellSize = 15;
	int valueInfoMargin = 7 * infoCellSize;
	int suitInfoMargin = 5 * infoCellSize;
	int statusLineHeight = 20;
	Dimension componentSize;
	CardnetPuzzleInputHelper inputHelper = new CardnetPuzzleInputHelper();
	CardnetGeneratorInfoInputHelper generatorInfoInputHelper = new CardnetGeneratorInfoInputHelper();

	public CardnetComponent() {
		ML listener = new ML();
		addMouseListener(listener);
		addMouseMotionListener(listener);
		inputHelper.setComponent(this);
		generatorInfoInputHelper.setComponent(this);
	}
	
	public void setModel(PuzzleModel model) {
		super.setModel(model);
		field = getCardnetModel().getField();
		cards = getCardnetModel().getCardSet();
		puzzle = getCardnetModel().getPuzzleInfo();
		int w = field.getWidth() * cellSize + valueInfoMargin + suitInfoMargin; 
		int h = field.getHeight() * cellSize + valueInfoMargin + suitInfoMargin + statusLineHeight;
		componentSize = new Dimension(w, h);
		setPreferredSize(componentSize);
		inputHelper.setModel(getCardnetModel());
		generatorInfoInputHelper.setModel(getCardnetModel());
	}
	
	public CardnetModel getCardnetModel() {
		return (CardnetModel)model;
	}

	public void paint(Graphics g) {
		super.paint(g);
		paintFieldBorder(g);
		paintCells(g);
		paintProblemSettingMode(g);
		paintInfos(g);
		paintStatusLine(g);
	}
	
	void paintFieldBorder(Graphics g) {
		g.setColor(Color.BLACK);
		int x = valueInfoMargin, y = valueInfoMargin;
		int w = field.getWidth() * cellSize;
		int h = field.getHeight() * cellSize;
		g.drawRect(x, y, w, h);
	}

	void paintCells(Graphics g) {
		for (int i = 0; i < field.getSize(); i++) {
			int fv = puzzle.getFilterValue(i);
			if(fv == 1) {
				g.setColor(Color.WHITE);
			} else {
				g.setColor(Color.BLACK);
			}
			int x = valueInfoMargin + cellSize * (field.x(i));
			int y = valueInfoMargin + cellSize * (field.y(i));
			g.fillRect(x + 1, y + 1, cellSize - 1, cellSize - 1);

			if(generatorInfoInputHelper.isEnabled() && generatorInfoInputHelper.getPosition() == i) {
				g.setColor(Color.ORANGE);
				g.drawArc(x + 5, y + 5, 20, 20, 0, 360);
			}
			g.setColor(Color.BLACK);
			if(puzzle.hasSolution()) {
				int v = puzzle.getValue(i);
				String s = cards.toString(v);
				if(s.length() >= 0) g.drawString(s, x + halfCellSize - 4 * s.length(), y + halfCellSize + 4);
			} else {
				String s = "";
				int n = generatorInfoInputHelper.getGeneratorInfo().getNumbers()[i];
				int c = generatorInfoInputHelper.getGeneratorInfo().getSuits()[i];
				if(n >= 0) s += cards.getNumberAsString(n);
				if(c >= 0) s += cards.getSuitAsString(c);
				if(s.length() >= 0) g.drawString(s, x + halfCellSize - 4 * s.length(), y + halfCellSize + 4);
			}
		}
	}
	
	void paintInfos(Graphics g) {
		if(!puzzle.hasProblem()) return;
		g.setColor(Color.BLACK);
		for (int h = 0; h < field.getHeight(); h++) {
			if(!puzzle.isHNumberSet(h)) continue;
			int y = valueInfoMargin + cellSize * h + halfCellSize + 4;
			int x = valueInfoMargin - infoCellSize;
			for (int i = cards.getNumberCount() - 1; i >= 0; i--) {
				int n = puzzle.getHNumbers(h)[i];
				if(n != 1) continue;
				String s = "" + cards.getNumberAsString(i);
				if(s.length() >= 0) g.drawString(s, x - 4 * s.length(), y);
				x -= infoCellSize;
			}
		}
		for (int h = 0; h < field.getHeight(); h++) {
			if(!puzzle.isHSuitSet(h)) continue;
			int y = valueInfoMargin + cellSize * h + halfCellSize + 4;
			int x = componentSize.width - suitInfoMargin + 8;
			for (int i = 0; i < cards.getSuitCount(); i++) {
				int n = puzzle.getHSuits(h)[i];
				if(n != 1) continue;
				String s = "" + cards.getSuitAsString(i);
				if(s.length() >= 0) g.drawString(s, x - 4 * s.length(), y);
				x += infoCellSize;
			}			
		}

		for (int v = 0; v < field.getWidth(); v++) {
			if(!puzzle.isVNumberSet(v)) continue;
			int x = valueInfoMargin + cellSize * v + halfCellSize;
			int y = valueInfoMargin - infoCellSize;
			for (int i = cards.getNumberCount() - 1; i >= 0; i--) {
				int n = puzzle.getVNumbers(v)[i];
				if(n != 1) continue;
				String s = "" + cards.getNumberAsString(i);
				if(s.length() >= 0) g.drawString(s, x - 4 * s.length(), y);
				y -= infoCellSize;
			}
		}
		for (int v = 0; v < field.getWidth(); v++) {
			if(!puzzle.isVSuitSet(v)) continue;
			int x = valueInfoMargin + cellSize * v + halfCellSize;
			int y = componentSize.height - suitInfoMargin + 10 - statusLineHeight;
			for (int i = 0; i < cards.getSuitCount(); i++) {
				int n = puzzle.getVSuits(v)[i];
				if(n != 1) continue;
				String s = "" + cards.getSuitAsString(i);
				if(s.length() >= 0) g.drawString(s, x - 4 * s.length(), y);
				y += infoCellSize;
			}
		}
	}

	void paintProblemSettingMode(Graphics g) {
		if(!inputHelper.isEnabled()) return;
		g.setColor(Color.CYAN);
		if(inputHelper.isSettingNumber && inputHelper.isSettingHLine) {
			int x = 0, y = valueInfoMargin + cellSize * inputHelper.rowIndex;
			g.fillRect(x, y, valueInfoMargin, cellSize);
		} else if(inputHelper.isSettingNumber && !inputHelper.isSettingHLine) {
			int y = 0, x = valueInfoMargin + cellSize * inputHelper.rowIndex;
			g.fillRect(x, y, cellSize, valueInfoMargin);
		} else if(!inputHelper.isSettingNumber && inputHelper.isSettingHLine) {
			int x = componentSize.width - suitInfoMargin, y = valueInfoMargin + cellSize * inputHelper.rowIndex;
			g.fillRect(x, y, suitInfoMargin, cellSize);
		} else if(!inputHelper.isSettingNumber && !inputHelper.isSettingHLine) {
			int y = componentSize.height - suitInfoMargin - statusLineHeight, x = valueInfoMargin + cellSize * inputHelper.rowIndex;
			g.fillRect(x, y, cellSize, suitInfoMargin);
		}
	}

	void paintStatusLine(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawString(model.getStatusText(), 5, componentSize.height - 10);
	}

	class ML extends MouseAdapter implements MouseMotionListener {
		int filterState = -1;

		public void mousePressed(MouseEvent e) {
			if(!isInField(e.getPoint())) return;
			getCardnetModel().setSettingPuzzleModeOn(false);
			getCardnetModel().setSettingGeneratorInfoModeOn(false);
			int p = getCell(e.getPoint());
			if(p < 0) return;
			filterState = 1 - puzzle.getFilterValue(p);
			puzzle.clearProblem();
			puzzle.setFilterValue(p, filterState);
			repaint();
		}

		public void mouseReleased(MouseEvent e) {
			filterState = -1;
		}

		public void mouseDragged(MouseEvent e) {
			if(filterState < 0 || !isInField(e.getPoint())) return;
			int p = getCell(e.getPoint());
			if(p < 0 || p >= field.getSize() || filterState == puzzle.getFilterValue(p)) return;
			puzzle.setFilterValue(p, filterState);
			repaint();
		}

		public void mouseMoved(MouseEvent e) {}
	}

	boolean isInField(Point p) {
		return p.x > valueInfoMargin && 
		       p.y > valueInfoMargin && 
		       p.x < componentSize.width - suitInfoMargin && 
		       p.y < componentSize.height - suitInfoMargin - statusLineHeight;
	}
	
	int getCell(Point p) {
		if(!isInField(p)) return -1;
		int x = (p.x - valueInfoMargin) / cellSize;
		int y = (p.y - valueInfoMargin) / cellSize;
		return x + y * field.getWidth();
	}
	
}
