package forsmarts.snake;

public class SnakeSolver extends AbstractSnakeSolver {
	//data section
	int[] fieldData;
	int[] hData;
	int[] vData;
	
	//state section
	int[] hUsage;
	int[] vUsage;

	public SnakeSolver() {}
	
	public void setData(int[] fieldData, int[] hData, int[] vData, int snakeLength) {
		this.fieldData = fieldData;
		this.hData = hData;
		this.vData = vData;
		setSnakeLength(snakeLength);
	}
	
	protected void init() {
		super.init();
		hUsage = new int[field.getHeight()];
		vUsage = new int[field.getWidth()];
		place[0] = -1;
		for (int p = 0; p < fieldData.length; p++) {
			if(fieldData[p] == SnakeRunner.B) {
				place[0] = p;
				break;
			}
		}
		state[place[0]] = 1;
		hUsage[field.getY(place[0])]++;
		vUsage[field.getX(place[0])]++;
	}
	
	protected boolean canMove(int q) {
		if(fieldData[q] == SnakeRunner.S) return false;
		if((fieldData[q] == SnakeRunner.F) != (t == snakeLength - 1)) return false;
		int ix = field.getX(q);
		if(vData[ix] >= 0 && vData[ix] <= vUsage[ix]) return false;
		int iy = field.getY(q);
		if(hData[iy] >= 0 && hData[iy] <= hUsage[iy]) return false;
		return true;
	}
	
	protected boolean isOk() {
		for (int ix = 0; ix < field.getWidth(); ix++) if(!isColumnOk(ix)) return false;
		for (int iy = 0; iy < field.getHeight(); iy++) if(!isRowOk(iy)) return false;
		return true;
	}
	
	boolean isRowOk(int iy) {
		if(hData[iy] < 0) return true;
		if(hData[iy] < hUsage[iy]) return false;
		int c = hUsage[iy];
		for (int ix = 0; ix < field.getWidth(); ix++) {
			int p = field.getIndex(ix, iy);
			if(state[p] == 0 && restriction[p] == 0) ++c;
		}
		return c >= hData[iy];
	}
	
	boolean isColumnOk(int ix) {
		if(vData[ix] < 0) return true;
		if(vData[ix] < vUsage[ix]) return false;
		int c = vUsage[ix];
		for (int iy = 0; iy < field.getHeight(); iy++) {
			int p = field.getIndex(ix, iy);
			if(state[p] == 0 && restriction[p] == 0) ++c;
		}
		return c >= vData[ix];
	}

	protected void onMove(int p) {
		hUsage[field.getY(p)]++;
		vUsage[field.getX(p)]++;
	}
	
	protected void onBack(int p) {
		hUsage[field.getY(p)]--;
		vUsage[field.getX(p)]--;
	}

	protected boolean isSolution() {
		for (int ix = 0; ix < field.getWidth(); ix++) {
			if(vData[ix] < 0) continue;
			if(vData[ix] != vUsage[ix]) return false;
		}
		for (int iy = 0; iy < field.getWidth(); iy++) {
			if(hData[iy] < 0) continue;
			if(hData[iy] != hUsage[iy]) return false;
		}
		return true;
	}
	
}
