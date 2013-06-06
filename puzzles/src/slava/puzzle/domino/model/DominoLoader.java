package slava.puzzle.domino.model;

import java.io.*;
import java.util.*;
import slava.puzzle.template.model.PuzzleLoader;

public class DominoLoader extends PuzzleLoader {
	
	public DominoLoader() {
		setRoot("/data/domino");
		initName("domino");
	}

	public DominoModel getDominoModel() {
		return (DominoModel)getModel();
	}

	public void load() throws Exception {
		File f = getFile();
		if(!f.isFile()) throw new Exception("File does not exist.");
		Properties p = new Properties();
		p.load(new FileInputStream(f));
		DominoModel model = getDominoModel();
		int width = Integer.parseInt(p.getProperty("width"));
		int height = Integer.parseInt(p.getProperty("height"));
		model.setSize(width, height);
		String field = p.getProperty("field");
		model.field = deserialize(field);
		String values = p.getProperty("values");
		String hOutline = p.getProperty("hOutline");
		String vOutline = p.getProperty("vOutline");
		if(values != null) {
			model.setProblem(deserialize(values), deserialize(hOutline), deserialize(vOutline));
		}
	}
	
	public void save() throws Exception {
		DominoModel model = getDominoModel();
		Properties p = new Properties();
		p.setProperty("width", "" + model.getWidth());
		p.setProperty("height", "" + model.getHeight());
		p.setProperty("field", serialize(model.getFieldCopy()));
		if(model.isShowingSolution()) {
			p.setProperty("values", serialize(model.values));
			p.setProperty("hOutline", serialize(model.getHRestrictionOutline()));
			p.setProperty("vOutline", serialize(model.getVRestrictionOutline()));
		}
		FileOutputStream os = new FileOutputStream(getFile());
		p.store(os, null);
		os.close();
	}	

}
