package slava.puzzle.doublepath.model;

import java.io.*;
import java.util.Properties;
import slava.puzzle.template.model.PuzzleLoader;

public class DoublePathLoader extends PuzzleLoader {

	public DoublePathLoader() {
		init();
	}
	protected void init() {
		setRoot("/data/doublepath");
		initName("doublepath_");
	}
	
	public DoublePathModel getDoublePathModel() {
		return (DoublePathModel)getModel();
	}

	public void load() throws Exception {
		File f = getFile();
		if(!f.isFile()) throw new Exception("File does not exist.");
		Properties p = new Properties();
		p.load(new FileInputStream(f));
		DoublePathModel model = getDoublePathModel();
		int width = Integer.parseInt(p.getProperty("width"));
		int height = Integer.parseInt(p.getProperty("height"));
		model.setSize(width, height);
		//DoublePathField field = model.getField();
		String states = p.getProperty("field");
		model.getField().setStates(deserialize(states));
		//String values = p.getProperty("values");
	}
	
	public void save() throws Exception {
		DoublePathModel model = getDoublePathModel();
		DoublePathField field = model.getField();
		Properties p = new Properties();
		p.setProperty("width", "" + field.getWidth());
		p.setProperty("height", "" + field.getHeight());
		p.setProperty("field", serialize(field.getStateCopy()));
		FileOutputStream os = new FileOutputStream(getFile());
		p.store(os, null);
		os.close();
	}	

}
