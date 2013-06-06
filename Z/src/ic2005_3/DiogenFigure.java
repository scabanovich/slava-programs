package ic2005_3;

public class DiogenFigure {
	int index;
	int[][] points;
	int[] hAreas;
	int[] vAreas;
	
	public DiogenFigure(int index, int[] form, int[] hAreas, int[] vAreas) {
		this.index = index;
		this.hAreas = hAreas;
		this.vAreas = vAreas;
		setForm(form);
	}
	
	void setForm(int[] form) {
		int size = 0;
		for (int i = 0; i < form.length; i++) if(form[i] == 1) size++;
		points = new int[size][2];
		int pi = 0;
		for (int i = 0; i < form.length; i++) if(form[i] == 1) {
			points[pi][0] = i % 4;
			points[pi][1] = i / 4;
			pi++;
		}
	}

}
