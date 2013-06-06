package slava.puzzle.crossnumber;

import slava.puzzle.template.model.PuzzleModel;

public class CrossNumberModel extends PuzzleModel {
	private CrossNumberField field;
	private int[][] hdistribution = null;
	private int[][] vdistribution = null;
	
	public CrossNumberModel() {
		field = new CrossNumberField();
		setLoader(new CrossNumberLoader());
	}
	
	public CrossNumberField getField() {
		return field;
	}
	
	public void flip(int place) {
		field.setMask(place, 1 - field.getMask(place));
	}
	
	public void setSolutionInfo(String info) {
		solutionInfo = info;
		if(info == null) setDistribution(null, null);
	}
	
	public void setDistribution(int[][] h, int[][] v) {
		hdistribution = h;
		vdistribution = v;
	}
	
	public String getStatusText() {
		if(isRunning()) return "Wait...";
		if(solutionInfo != null) return solutionInfo;
		return "";
	}
	
	public String getSolutionDistributionInfo(int place, boolean horisontal) {
		if(hdistribution == null || vdistribution == null || place < 0) return null;
		StringBuffer sb = new StringBuffer();
		sb.append("<html><body><table>");
		int[] d = (horisontal) ? hdistribution[place] : vdistribution[place];
		boolean f = false;
		for (int i = 0; i < d.length; i++) {
			if(d[i] > 0) {
				f = true;
				sb.append("<tr>" + "<td>"+ i + "</td><td>" + d[i] + "</td>" + "</tr>");
			}
		}
		sb.append("</table></body></html>");
		return (!f) ? null : sb.toString();
	}
	
	public boolean isThroughRow() {
		return field.isThroughRow();
	}
	
	public void setThroughRow(boolean b) {
		field.setThroughRow(b);
	}

}
