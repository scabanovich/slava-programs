package slava.puzzle.paths.nook.analysis;

import com.slava.common.RectangularField;

import slava.puzzle.paths.nook.model.NookPathsPuzzle;

public class NookPathsPuzzleCheck {

	public static String getPuzzleError(NookPathsPuzzle puzzle) {
		RectangularField f = puzzle.getModel().getField();
		int[] filter = puzzle.getFilter();
		int[] data = puzzle.getData();
		int b = 0;
		int w = 0;
		boolean start = false;
		int maxValue = 0;
		int[] used = new int[f.getSize()];
		int oddity = -1;
		for (int p = 0; p < data.length; p++) {
			if(filter[p] == 1) continue;
			int c = (f.getX(p) + f.getY(p)) % 2;
			if(c == 0) b++; else w++;
			int v = data[p];
			if(v <= 0) continue;
			if(v > maxValue) maxValue = v; 
			if(used[v - 1] > 0) {
				return "Value " + v + " is used more than once.";
			}
			used[v - 1]++;
			if(v == 1) start = true;
			int odd = (c + v) % 2;
			if(oddity < 0) {
				oddity = odd;
			} else if(oddity != odd) {
				return "Value " + v + " breaks evenness rule.";
			}
		}
		if(maxValue > b + w) {
			return "Maximum value must be " + (b + w);
		}
		if(Math.abs(b - w) > 1) {
			return "Number of black and white cells cannot differ more than 1.";
		}
		if(!start) {
			return "Path start point must be set.";
		}
		if(maxValue < b + w) {
			return "Path end point (" + (b + w) + ") must be set.";
		}
		return null;
	}

	public static String getGenerationError(NookPathsPuzzle puzzle) {
		RectangularField f = puzzle.getModel().getField();
		int[] filter = puzzle.getFilter();
		int[] data = puzzle.getData();
		int b = 0;
		int w = 0;
		int pb = -1;
		int pe = -1;
		int maxValue = 0;
		for (int p = 0; p < data.length; p++) {
			if(filter[p] == 1) continue;
			int c = (f.getX(p) + f.getY(p)) % 2;
			if(c == 0) b++; else w++;
			int v = data[p];
			if(v <= 0) continue;
			if(v == 1) {
				pb = p;
			}
			if(v > maxValue) {
				pe = p;
				maxValue = v;
			}
		}
		if(Math.abs(b - w) > 1) {
			return "Number of black and white cells cannot differ more than 1.";
		}
		if(b + w != maxValue) {
			pe = -1;
		}
		int ob = (pb < 0) ? -1 : (f.getX(pb) + f.getY(pb)) % 2;
		int oe = (pe < 0) ? -1 : (f.getX(pe) + f.getY(pe)) % 2;
		if(b == w) {
			if(ob >= 0 && oe >= 0 & ob == oe) {
				return "Start and end points must be in diffrent colored cells.";
			}
		} else if(b > w) {
			if(ob == 1 || oe == 1) {
				return "Start and end points must be in black cells.";
			}
		} else if(b < w) {
			if(ob == 0 || oe == 0) {
				return "Start and end points must be in white cells.";
			}
		}
		return null;
	}
	
	public static void setStartAndEndValues(NookPathsPuzzle puzzle, int[] initdata) {
		RectangularField f = puzzle.getModel().getField();
		int[] filter = puzzle.getFilter();
		int[] data = puzzle.getData();
		int b = 0;
		int w = 0;
		int pb = -1;
		int pe = -1;
		int maxValue = 0;
		for (int p = 0; p < data.length; p++) {
			if(filter[p] == 1) continue;
			int c = (f.getX(p) + f.getY(p)) % 2;
			if(c == 0) b++; else w++;
			int v = data[p];
			if(v <= 0) continue;
			if(v == 1) {
				pb = p;
			}
			if(v > maxValue) {
				pe = p;
				maxValue = v;
			}
		}
		if(b + w != maxValue) {
			pe = -1;
		}
		int ob = (pb < 0) ? -1 : (f.getX(pb) + f.getY(pb)) % 2;
		int oe = (pe < 0) ? -1 : (f.getX(pe) + f.getY(pe)) % 2;
		if(pb < 0 && pe < 0) {
			if(b == w) {
				pb = generatePlace(puzzle, -1, -1);
			} else if(b > w) {
				pb = generatePlace(puzzle, -1, 0);
			} else if(b < w) {
				pb = generatePlace(puzzle, -1, 1);
			}
			ob = (f.getX(pb) + f.getY(pb)) % 2;
			oe = (b == w) ? 1 - ob : ob;
			pe = generatePlace(puzzle, pb, oe);
		} else if(pe < 0) {
			oe = (b == w) ? 1 - ob : ob;
			pe = generatePlace(puzzle, pb, oe);
		} else if(pb < 0) {
			ob = (b == w) ? 1 - oe : oe;
			pb = generatePlace(puzzle, pe, ob);
		}
		
		initdata[pb] = 0;
		initdata[pe] = b + w - 1;
	}
	
	static int generatePlace(NookPathsPuzzle puzzle, int other, int evenness) {
		RectangularField f = puzzle.getModel().getField();
		while(true) {
			int p = (int)(Math.random() * f.getSize());
			if(puzzle.getFilter()[p] == 1 || p == other) continue;
			int op = (f.getX(p) + f.getY(p)) % 2;
			if(evenness >= 0 && op != evenness) continue;
			return p;
		}
	}

}
 