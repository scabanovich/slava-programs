package words;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import words.ProverbsSorter.ProverbEntry;

public class ProverbsVisualizer {
	protected JFrame frame = new JFrame("");
	JTextField textField = new JTextField();
	JTextArea textArea = new JTextArea();

	List<ProverbEntry> proverbs;
	
	public ProverbsVisualizer() {
		frame.setTitle("Proverbs");
		initData();
		init();
		setComponent();
		run();
	}

	public static void center(Window window) {
		Dimension dw = window.getSize();
		Dimension ds = Toolkit.getDefaultToolkit().getScreenSize();
		window.setLocation((ds.width - dw.width) / 2, (ds.height - dw.height) / 2);
	}

	private void initData() {
		ProverbsSorter p = new ProverbsSorter();
		p.addFile("/home/slava/Private/English/proverbs.txt");
		proverbs = p.proverbs;
	}

	private void init() {
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
	
	public void setComponent() {
		BorderLayout layout = new BorderLayout();
		layout.setHgap(10);
		layout.setVgap(10);
		frame.getContentPane().setLayout(layout);
		frame.getContentPane().add(textField, BorderLayout.NORTH);
		JScrollPane pane = new JScrollPane();
//		pane.setLayout(layout);
		pane.setViewportView(textArea);
		frame.getContentPane().add(pane);
		Insets i = new Insets(10, 10, 10, 10);
		textArea.setMargin(i);
		pane.setPreferredSize(new Dimension(400, 600));
		textField.addKeyListener(new KeyAdapter() {
		    public void keyReleased(KeyEvent e) {
		    	reload();
		    }
		});
	}

	void reload() {
		StringBuilder sb = new StringBuilder();
		String filter = textField.getText().trim();
		if(filter.length() > 2) {
			for (ProverbEntry e: proverbs) {
				if(e.matchesEnglish(filter) || e.matchesRussian(filter)) {
					e.printTo(sb);
					sb.append("\n");
				}
			}			
		}
		textArea.setText(sb.toString());
	}

	
	
	public void run() {
		frame.pack();
		center(frame);
		frame.setVisible(true);		 
	}
	
}
