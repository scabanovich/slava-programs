package slava.puzzle.sudoku.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.*;
import slava.puzzle.sudoku.solver.AbstractSudokuField;
import slava.puzzle.sudoku.solver.SudokuField;
import slava.puzzle.sudoku.solver.Symmetries;
import slava.puzzle.sudoku.solver.restriction.DifferenceNotGreaterThanNRestriction;
import slava.puzzle.sudoku.solver.variations.*;
import slava.puzzle.template.gui.PuzzleFrame;

public class SudokuVarGenerator {
	Map variations = new TreeMap();

	JFrame frame;
	JComboBox selector;
	JTextField stepsLimitField;
	JTextField narrowestField;
	JTextField quantityField;
	JTextArea resultArea;
	JComboBox symmetry;
	JButton runButton;

	public SudokuVarGenerator() {
		init();
	}

	public void init() {
		SudokuField standard = new SudokuField();
		standard.setWidth(9, false);
		variations.put("Standard", standard);

		SudokuField diagonal = new SudokuField();
		diagonal.setWidth(9, true);
		variations.put("Diagonal", diagonal);

		variations.put("Cube", new CubeSudokuField());
		variations.put("Double", new DoubleSudokuField());
		variations.put("Holes", new HoleSudokuField());
		variations.put("Kings", new KingsSudokuField());
		variations.put("Knight", new KnightSudokuField(null));
		variations.put("Knight-0", new KnightSudokuField(KnightSudokuRunner.EMPTY_DATA));
		variations.put("Layered", new LayeredSudokuField());
		variations.put("Parket", new ParketSudokuField());
		variations.put("Parket-1", new ParketSudokuField(1));
		variations.put("Parket-2", new ParketSudokuField(2));
		variations.put("Parket-3", new ParketSudokuField(3));
		variations.put("Parket-4", new ParketSudokuField(4));
		variations.put("Parket-5", new ParketSudokuField(5));
		variations.put("Pawn", new PawnSudokuField(null));
		variations.put("Pawn-0", new PawnSudokuField(KnightSudokuRunner.EMPTY_DATA));
		variations.put("Pills", new PillsSudokuField());
		variations.put("Ring3", new Ring3SudokuField());
		variations.put("Sextet", new SextetSudokuField());
		variations.put("Squares", new SquaresSudokuField());
		
		variations.put("Tridoku 'V raznye storony'", new CubeTridokuField(CubeTridokuField.V_RAZNYE_STORONY));
		variations.put("Tridoku Iny-Yany", new CubeTridokuField(CubeTridokuField.INY_YANY));

		variations.put("Sliced cube A", new CubeSlicedSudokuField(CubeSlicedSudokuField.A));
		variations.put("Sliced cube B", new CubeSlicedSudokuField(CubeSlicedSudokuField.B));
		variations.put("Sliced cube C", new CubeSlicedSudokuField(CubeSlicedSudokuField.C));
		variations.put("Sliced cube D", new CubeSlicedSudokuField(CubeSlicedSudokuField.D));
		variations.put("Sliced cube E", new CubeSlicedSudokuField(CubeSlicedSudokuField.E));
		variations.put("Sliced cube F", new CubeSlicedSudokuField(CubeSlicedSudokuField.F));

		SudokuField diffNotGreaterThan5 = new SudokuField();
		diffNotGreaterThan5.setWidth(9, false);
		DifferenceNotGreaterThanNRestriction r= new DifferenceNotGreaterThanNRestriction();
		r.setNeighboursDifferNotMoreThan(5);
		r.setField(diffNotGreaterThan5);
		diffNotGreaterThan5.addRestriction(r);
		variations.put("Differences <= 5", diffNotGreaterThan5);

	}

	public void run() {
		frame = new JFrame("Sudoku generator");
		Container c = frame.getContentPane();
		c.setLayout(new BorderLayout());
		JPanel panel = new JPanel();
		GridLayout layout = new GridLayout(5, 2);
		layout.setHgap(10);
		panel.setLayout(layout);
		c.add(panel, BorderLayout.NORTH);

		JLabel l1 = new JLabel("Select sudoku kind: ");
		l1.setPreferredSize(new Dimension(300, 20));
		panel.add(l1);
		selector = new JComboBox(variations.keySet().toArray(new String[0]));
		selector.setPreferredSize(new Dimension(300, 20));
		panel.add(selector);

		JLabel l2 = new JLabel("Maximum number of steps: ");
		l2.setPreferredSize(new Dimension(300, 20));
		panel.add(l2);
		stepsLimitField = new JTextField();
		stepsLimitField.setText("5");
		panel.add(stepsLimitField);

		JLabel l3 = new JLabel("Minimum number of opened cells at one step: ");
		l3.setPreferredSize(new Dimension(300, 20));
		panel.add(l3);
		narrowestField = new JTextField();
		narrowestField.setText("5");
		panel.add(narrowestField);

		JLabel l5 = new JLabel("Symmetry: ");
		l5.setPreferredSize(new Dimension(300, 20));
		panel.add(l5);
		symmetry = new JComboBox(Symmetries.SYMMETRIES);
		symmetry.setPreferredSize(new Dimension(300, 20));
		panel.add(symmetry);

		JLabel l4 = new JLabel("Quantity: ");
		l4.setPreferredSize(new Dimension(300, 20));
		panel.add(l4);
		quantityField = new JTextField();
		quantityField.setText("1");
		panel.add(quantityField);

		resultArea = new JTextArea();
		resultArea.setFont(new Font("Monospaced", 0, 12));
		JScrollPane pane = new JScrollPane(resultArea);
		c.add(pane, BorderLayout.CENTER);
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		runButton = new JButton("Run");
		p.add(runButton, BorderLayout.EAST);
		c.add(p, BorderLayout.SOUTH);

		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String variation = selector.getSelectedItem().toString();
				int stepsLimit = -1;
				try {
					stepsLimit = Integer.parseInt(stepsLimitField.getText());
				} catch (NumberFormatException exc) {
					System.out.println("Incorrect integer: " + stepsLimitField.getText());
					return;
				}
				int narrowest = -1;
				try {
					narrowest = Integer.parseInt(narrowestField.getText());
				} catch (NumberFormatException exc) {
					System.out.println("Incorrect integer: " + narrowestField.getText());
					return;
				}
				String sym = symmetry.getSelectedItem().toString();
				int quantity = 1;
				try {
					quantity = Integer.parseInt(quantityField.getText());
				} catch (NumberFormatException exc) {
					System.out.println("Incorrect integer: " + quantityField.getText());
					return;
				}
				for (int i = 0; i < quantity; i++) {
					run(variation, stepsLimit, narrowest, sym);
				}
			}
		});
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.pack();
		frame.setSize(560, 600);
		PuzzleFrame.center(frame);
		frame.setVisible(true);
	}


	void run(String variation, int stepsLimit, int narrowest, String symmetry) {
		final AbstractSudokuField f = (AbstractSudokuField)variations.get(variation);
		if(f == null) return;
		AbstractSudokuRunner runner = new AbstractSudokuRunner() {
			protected AbstractSudokuField createSudokeField() {
				return f;
			}
		};
		StringBuffer out = new StringBuffer();
		runner.generate(symmetry, stepsLimit, narrowest, out);
		if(out.length() > 0) {
			resultArea.append(out.toString() + "\n");
		}
	}

	public static void main(String[] args) {
		SudokuVarGenerator g = new SudokuVarGenerator();
		g.run();
	}

}
