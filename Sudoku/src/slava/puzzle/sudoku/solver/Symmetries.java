package slava.puzzle.sudoku.solver;

public interface Symmetries {
	public String NONE = "None";
	public String VERTICAL = "Vertical";
	public String HORIZONTAL = "Horizontal";
	public String DIAGONAL = "Diagonal";
	public String CENTRAL = "Central";
	
	public String[] SYMMETRIES = {NONE, VERTICAL, HORIZONTAL, DIAGONAL, CENTRAL};

}
