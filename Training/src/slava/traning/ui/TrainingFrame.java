package slava.traning.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import slava.traning.enging.TrainingEngine;
import slava.traning.enging.TrainingListener;
import slava.traning.model.TrainingModel;

public class TrainingFrame implements TrainingListener {
	TrainingEngine engine = new TrainingEngine();
	TrainingModel model;
	JFrame frame;
	JPanel mainpanel = new JPanel(new BorderLayout());
	JPanel table = new JPanel();
	JLabel labelKeyName = new JLabel();
	JLabel labelKeyText = new JLabel();
	JLabel labelValueName = new JLabel();
	JLabel labelValueText = new JLabel();
	JLabel count = new JLabel();
	JLabel timer = new JLabel();
	JTextField text = new JTextField();
	JButton button = new JButton();

	public TrainingFrame() {}

	public void setModel(TrainingModel model) {
		this.model = model;
	}
	
    public void open() {
        frame = new JFrame();
        frame.setTitle("Training"); 
       
        BoxLayout b = new BoxLayout(table, BoxLayout.Y_AXIS);
        table.setLayout(b);
        layoutTwoLabels(labelKeyName, labelKeyText, model.getKeyName());
        layoutTwoLabels(labelValueName, labelValueText, model.getValueName());
        table.add(Box.createVerticalStrut(10));
        updateCount();
        layoutStatus();
        table.add(Box.createVerticalGlue());
        table.add(text);
        
        mainpanel.add(table, BorderLayout.CENTER);
        
        button.setText("Next");
        mainpanel.add(button, BorderLayout.SOUTH);
        frame.setContentPane(mainpanel);
        frame.addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent e) {
                text.requestFocus();
            }
        });
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            	new ExitAction().actionPerformed(null);
            }
        });
        text.addKeyListener(new KeyAdapter() {
        	public void keyPressed(KeyEvent e) {
        		if(e.getKeyCode() == 13 || e.getKeyCode() == 10) {
        			execute();
        		}
        	}
		});
        button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				execute();
			}
		});
        frame.pack();
        frame.setSize(400, 300);
        center(frame);
        frame.setVisible(true);
        
        engine = new TrainingEngine();
        engine.setModel(model);
        engine.setListener(this);
    }

    void execute() {
		if(engine.isRunning()) {
			button.setText("Next");
			engine.checkNow();
		} else {
			button.setText("Check");
			engine.start();
		}
		text.requestFocus();
    }

    void layoutTwoLabels(JLabel c1, JLabel c2, String name) {
    	JPanel p = new JPanel(new BorderLayout());
    	c1.setText(name);
    	p.add(c1, BorderLayout.WEST);
    	p.add(new JLabel(), BorderLayout.CENTER);
    	p.add(c2, BorderLayout.EAST);
    	table.add(p);
    }

    void layoutStatus() {
    	JPanel p = new JPanel(new BorderLayout());
    	p.add(timer, BorderLayout.WEST);
    	p.add(new JLabel(), BorderLayout.CENTER);
    	p.add(count, BorderLayout.EAST);
    	table.add(p);
    }

    class ExitAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Frame f = frame;
            frame = null;
            if(f == null) return;
//            saveFrameBounds(f);
//            CrosswordPreference p = CrosswordPreference.getInstance();
//            p.save();
            f.setVisible(false);
            Runtime.getRuntime().exit(0);
        }
    }

    public static void center(Window w) {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension f = w.getSize();
        w.setLocation((d.width - f.width) / 2, (d.height - f.height) / 2);
    }

	@Override
    public void started() {
    	
    }

	@Override
	public void setPair(final String key, final String value) {
		try {
		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				text.setText("");
				labelKeyText.setText(key);
				labelValueText.setText(value);
			}
		});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void checkValue(final String value) {
		try {
		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				labelValueText.setText(value);
				if(value.equals(text.getText())) {
					engine.success();
				} else {
					engine.failure();
				}
				text.setText("");
				updateCount();
				timer.setText("");
				button.setText("Next");
			}
		});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setTimer(final int sec) {
		try {
		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				timer.setText("Time left: " + sec);
			}
		});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void updateCount() {
		count.setText("Count: " + engine.getSuccessCount() + ":" + engine.getFailureCount());
	}
}
