package mozaika;

import mozaika.validator.ColoredPillsValidator;
import mozaika.validator.RegionsValidator;
import mozaika.validator.SnakeValidator;
import mozaika.validator.TetraminoValidator;

import com.slava.common.RectangularField;

public class MozaikaRunner {

	public static void run(int width, int height, int[] settingState, IMozaikaValidator validator) {
		MozaikaSolver solver = new MozaikaSolver();
		RectangularField f = new RectangularField();
		f.setSize(width, height);
		solver.setField(f);
		solver.setSettingState(settingState);
		validator.setField(f);
		solver.setValidator(validator);
		solver.solve();
		int sc = solver.solutionCount;
		System.out.println("Solution count=" + sc);
		System.out.println("Tree count=" + solver.treeCount);
	}

	public static void runColoredPills() {
		run(9, 9, MozaikaExamples.COLORED_PILLS_EXAMPLE, new ColoredPillsValidator());
	}

	public static void runSnake() {
		run(9, 9, MozaikaExamples.SNAKE_EXAMPLE_2, new SnakeValidator());
	}

	public static void runRegions() {
		run(9, 9, MozaikaExamples.REGIONS_EXAMPLE, new RegionsValidator());
	}

	public static void runTetramino() {
		run(12, 12, MozaikaExamples.TETRAMINO_EXAMPLE, new TetraminoValidator());
	}

	static final int COLORED_PILLS = 0;
	static final int SNAKE = 1;
	static final int THREE_REGIONS = 2;
	static final int TETRAMINO = 3;

	public static void main(String[] args) {
		int program = COLORED_PILLS;
		switch (program) {
			case COLORED_PILLS: runColoredPills(); break;
			case SNAKE:			runSnake();	break;
			case THREE_REGIONS: runRegions(); break;
			case TETRAMINO: 	runTetramino();	break;
			//
		}
	}

}
