package slava.puzzle.template.model;

import java.util.*;
import java.io.File;

public class PuzzleLoader {
	protected PuzzleModel model;
	protected String root = "";
	protected String name = "shop";
	protected String extension = "txt";
	
	public PuzzleLoader() {}

	//like "/data/shopping"	
	public void setRoot(String relativePath) {
		try {
			root = new File(".").getAbsoluteFile().getParent() + relativePath;
			new File(root).mkdirs();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initName(String template) {
		this.name = template; 
	}
	
	public void setModel(PuzzleModel model) {
		this.model = model;
	}
	
	public PuzzleModel getModel() {
		return model;
	}
	
	public void load() throws Exception {
	}
	
	public void save() throws Exception {
	}
	
	protected File getFile() {
		return new File(root + "/" + name + "." + extension);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}


	public static String serialize(int[] array) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			sb.append(' ').append(array[i]);
		}
		return sb.toString();
	}
	
	public static int[] deserialize(String s) {
		StringTokenizer st = new StringTokenizer(s, " ");
		List l = new ArrayList();
		while(st.hasMoreTokens()) {
			l.add(new Integer(st.nextToken()));
		}
		Integer[] is = (Integer[])l.toArray(new Integer[0]);
		int[] array = new int[is.length];
		for (int i = 0; i < is.length; i++) array[i] = is[i].intValue();
		return array;
	}

}
