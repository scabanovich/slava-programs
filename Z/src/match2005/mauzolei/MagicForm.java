package match2005.mauzolei;

import java.io.*;

public class MagicForm {
	MauzoleiField field;
	
	int[] form;

	public void setField(MauzoleiField field) {
		this.field = field;
		form = new int[field.size];
	}
	
	public void load() {
		File f = new File("./packings.txt");
		try {
			FileReader r = new FileReader(f);
			BufferedReader br = new BufferedReader(r);
			String s = null;
			int z = field.zSize - 1;
			int y = 0;
			while(true) {
				s = br.readLine();
				if(s == null) break;
				s = s.trim();
				if(s.length() == 0) continue;
				int x = 0;
				for (int i = 0; i < s.length(); i++) {
					char c = s.charAt(i);
					if(c == ' ') continue;
					if(c == 'z') {
						form[field.xyz[x][y][z]] = -1;
					} else {
						form[field.xyz[x][y][z]] = (int)c - 97;
					}
					++x;
				}
				++y;
				if(y >= field.ySize) {
					y = 0;
					--z;
					if(z < 0)  break;
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
//		printForm();
	}

	void printForm() {
		for (int i = 0; i < field.size; i++) {
			char c = form[i] < 0 ? '+' : (char)(97 + form[i]);
			System.out.print(" " + c);
			if(field.x[i] == field.xSize - 1) {
				System.out.println("");
				if(field.y[i] == field.ySize - 1) {
					System.out.println("");
				}
			}
		}
	}
	
}
