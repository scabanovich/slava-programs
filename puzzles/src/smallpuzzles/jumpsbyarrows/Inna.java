package smallpuzzles.jumpsbyarrows;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Inna {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int x = 6767 - 365*15031996;
		
		JFrame f = new JFrame("Inna");
		JLabel label = new JLabel();
		label.setText("x=" + x);
		label.setPreferredSize(new Dimension(200, 100));
		f.getContentPane().add(label);

		f.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent event) {
				System.exit(0);
			}
		});

		f.pack();
		f.setVisible(true);

	}

}
