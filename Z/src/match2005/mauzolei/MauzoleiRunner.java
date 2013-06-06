package match2005.mauzolei;

public class MauzoleiRunner {
	
	public static void main(String[] args) {
		MauzoleiField field = new MauzoleiField();
		field.setSize(8, 8, 4, MauzoleiForm.FILTER);
		
		PentaFigures figures = new PentaFigures();
		figures.createPlacements(field);
		
		MauzoleiPacking packing = new MauzoleiPacking();
		packing.setField(field);
		packing.setPlacements(figures.placements);
		packing.solve();
		
	}

}
