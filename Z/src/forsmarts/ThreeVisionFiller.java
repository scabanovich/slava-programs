/*
*  Copyright
*      Copyright (c) Kabira Technologies Inc. 2005
*      All rights reserved.
*  
*  History
*      $Source:$
*      $Revision:$ $Date:$
*/
package forsmarts;

/**
 * @author glory
 *
 */
public class ThreeVisionFiller extends ThreeVisionSolver {
	
	int pieceCount;
	
	public ThreeVisionFiller() {
		setSolutionLimit(1);
	}
	
	public void setPieceCount(int pieceCount) {
		this.pieceCount = pieceCount;
	}

	void srch() {
		wayCount[t] = 0;
		for (int k = 0; k < field.placements.length; k++) {
			if(!canAdd(k)) continue;
			ways[t][wayCount[t]] = k;
			wayCount[t]++;
		}
		randomize();
	}
	
	void randomize() {
		for (int i = 0; i < wayCount[t]; i++) {
			int j = (int)((wayCount[t] - i) * Math.random()) + i;
			int c = ways[t][i];
			ways[t][i] = ways[t][j];
			ways[t][j] = c;
		}
		if(wayCount[t] > 10) wayCount[t] = 10;		
	}
	
	protected void onSolutionFound() {
		if(solutionCount == 1) {
			createDots();
		}
	}
	
	boolean isSolution() {
		return pieceIndex == pieceCount;
	}

	void createDots() {
		for (int p = 0; p < field.getSize(); p++) {
			field.dots[p] = -1;
			if(state[p] >= 0) {
				field.dots[p] = getNumber(p, state);
			}
		}
	}
}
