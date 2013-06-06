package forsmarts.csudoku;

import com.slava.sudoku.*;

public class ConstructedSudokuSolver {
	ISudokuField field;
	Piece[] pieces;
	SudokuState state = new SudokuState();
	int[] initialData;
	
	int[] usedPieces;
	int[] occupied;

	int t;
	int[] wayCount;
	int[] way;
	int[][] waysA; //piece index
	int[][] waysB; //placement index
	
	int[] distribution;
	
	int solutionCount;
	
	public ConstructedSudokuSolver() {}
	
	public void setPieces(Piece[] pieces) {
		this.pieces = pieces;
	}
	
	public void setField(ISudokuField f) {
		field = f;		
		state.setField(f);
	}
	
	public void setInitialData(int[] initialData) {
		this.initialData = initialData;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state.init();
		for (int p = 0; p < initialData.length; p++) {
			if(initialData[p] >= 0)	state.add(p, initialData[p]);
		}

		usedPieces = new int[pieces.length];
		occupied = new int[field.getSize()];
		for (int i = 0; i < occupied.length; i++) occupied[i] = -1;
		
		wayCount = new int[pieces.length + 1];
		way = new int[pieces.length + 1];
		waysA = new int[pieces.length + 1][300];
		waysB = new int[pieces.length + 1][300];
		
		distribution = new int[field.getSize()];
		
		t = 0; 
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == pieces.length) return;
		
		makeDistribution();
		int p = getNarrowestPlace();
		if(p < 0 || distribution[p] == 0) return;
		
		for (int i = 0; i < pieces.length; i++) {
			if(usedPieces[pieces[i].index] > 0) continue;
			for (int j = 0; j < pieces[i].placements.length; j++) {
				int[] placement = pieces[i].placements[j];
				int[] values = pieces[i].values;
				if(canAdd(placement, values) && contains(placement, p)) {
					waysA[t][wayCount[t]] = i;
					waysB[t][wayCount[t]] = j;
					wayCount[t]++;
				}
			}
		}
	}
	
	void makeDistribution() {
		for (int i = 0; i < distribution.length; i++) distribution[i] = 0;
		for (int i = 0; i < pieces.length; i++) {
			if(usedPieces[pieces[i].index] > 0) continue;
			for (int j = 0; j < pieces[i].placements.length; j++) {
				int[] placement = pieces[i].placements[j];
				int[] values = pieces[i].values;
				if(canAdd(placement, values)) {
					for (int k = 0; k < placement.length; k++) {
						distribution[placement[k]]++;
					}
				}
			}
		}
	}
	
	int getNarrowestPlace() {
		int pc = -1;
		int min = 10000;
		for (int p = 0; p < distribution.length; p++) {
			if(occupied[p] >= 0) continue;
			if(distribution[p] < min) {
				min = distribution[p];
				pc = p;
			}
		}		
		return pc;
	}
	
	boolean contains(int[] placement, int p) {
		for (int i = 0; i < placement.length; i++) {
			if(placement[i] == p) return true;
		}
		return false;
	}
	
	boolean canAdd(int[] placement, int[] values) {
		for (int i = 0; i < placement.length; i++) {
			int p = placement[i];
			if(occupied[p] >= 0) return false;
			if(values[i] >= 0 && state.getValue(p) >= 0) return false;
		}
		boolean result = true;
		int j = 0;
		for (int i = 0; i < placement.length && result; i++) {
			int v = values[i];
			if(v < 0) continue;
			int p = placement[i];
			result = state.canAdd(p, v);
			if(!result) break;
			state.add(p, v);
			j = i + 1;
		}
		for (int i = 0; i < j; i++) {
			int v = values[i];
			if(v < 0) continue;
			int p = placement[i];
			if(v == state.getValue(p)) state.remove(p, v);
		}
		return result;
	}
	
	void move() {
		Piece piece = pieces[waysA[t][way[t]]];
		int[] placement = piece.placements[waysB[t][way[t]]];
		usedPieces[piece.index]++;
		add(placement, piece.values);		
		++t;
		srch();
		way[t] = -1;
	}
	
	void add(int[] placement, int[] values) {
		for (int i = 0; i < placement.length; i++) {
			int p = placement[i];
			int v = values[i];
			occupied[p] = (t + 1);
			if(v >= 0) state.add(p, v);
		}
	}
	
	void back() {
		--t;
		Piece piece = pieces[waysA[t][way[t]]];
		int[] placement = piece.placements[waysB[t][way[t]]];
		usedPieces[piece.index]--;
		remove(placement, piece.values);
	}
	
	void remove(int[] placement, int[] values) {
		for (int i = 0; i < placement.length; i++) {
			int p = placement[i];
			int v = values[i];
			occupied[p] = -1;
			if(v >= 0) state.remove(p, v);
		}
	}
	
	void anal() {
		srch();
		way[t] = -1;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(t == pieces.length) {
				onSolutionFound();
			}
		}		
	}
	
	void onSolutionFound() {
		int sc = SudokuRunner.solve((AbstractSudokuField)field, (int[])state.getValues().clone());
		if(sc == 0) return;
		++solutionCount;
		System.out.println(solutionCount);
		String s = field.printSolution(state.getValues());
		System.out.println(s);
		printOccupation();
	}
	
	void printOccupation() {
		System.out.println("occupation");
		for (int p = 0; p < field.getSize(); p++) {
			char c = (char)(96 + occupied[p]);
			System.out.print(" " + c);
			if(pieces[0].field.isRightBorder(p)) System.out.println("");
		}
		System.out.println("");
	}
	
}
