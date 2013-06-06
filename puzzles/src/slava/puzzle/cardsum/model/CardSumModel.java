package slava.puzzle.cardsum.model;

import slava.puzzle.template.model.PuzzleModel;

public class CardSumModel extends PuzzleModel {
	CardSumField field;
	
	int[] solution;
	int[][] cardDistribution;
	int[][] sumDistribution;
	
	public CardSumModel() {
		field = new CardSumField();
		setLoader(new CardSumLoader());
	}
	
	public CardSumField getField() {
		return field;
	}
	
	public void setSolutionInfo(String info) {
		super.setSolutionInfo(info);
		if(info == null) {
			solution = null;
			cardDistribution = null;
			sumDistribution = null;
		}
	}

	public int getSolutionCardAt(int i) {
		return (solution == null) ? -1 : solution[i];
	}
	
	public String getCardDistributionTip(int place) {
		if(cardDistribution == null || cardDistribution == null || place < 0) return null;
		StringBuffer sb = new StringBuffer();
		sb.append("<html><body><table>");
		int[] d = cardDistribution[place];
		boolean f = false;
		for (int i = 0; i < d.length; i++) {
			if(d[i] > 0) {
				f = true;
				sb.append("<tr>" + "<td>"+ field.getVisual(i) + "</td><td>" + d[i] + "</td>" + "</tr>");
			}
		}
		sb.append("</table></body></html>");
		return (!f) ? null : sb.toString();
	}
	
	public String getSumDistributionTip(int column) {
		if(sumDistribution == null || sumDistribution == null || column < 0) return null;
		StringBuffer sb = new StringBuffer();
		sb.append("<html><body><table>");
		int[] d = sumDistribution[column];
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
	
	public void setCardAt(int i, String s) throws Exception {
		if(s.length() == 0) {
			field.setCard(i, -1);
		} else {
			for (int v = 0; v < field.getWidth(); v++) {
				if(field.getVisual(v).equals(s)) {
					field.setCard(i, v);
					return;					
				}
			}
			throw new Exception("Value is out of range."); 
		}
	}

	public void setSumAt(int column, String s) throws Exception {
		if(s.length() == 0) {
			field.setSum(column, -1);
		} else {
			int sum = Integer.parseInt(s);
			field.setSum(column, sum);
		}
	}
	
	public void setSolution(int[] solution) {
		this.solution = solution;
	}
	
	public void setDistribution(int[][] cardDistribution, int[][] sumDistribution) {
		this.cardDistribution = cardDistribution;
		this.sumDistribution = sumDistribution;
	}

}
