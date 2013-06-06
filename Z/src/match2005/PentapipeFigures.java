package match2005;

import java.util.ArrayList;

public class PentapipeFigures {
	static char[] letters = {'i', 'p', 'z', 'w', 'x', 'v', 't', 'y', 'f', 'l', 's', 'u'};
	int[][] forms = {
		//I
		{0,0,0,0,0,0,0,
		 0,0,0,0,0,0,0,
		 2,2,2,2,2,2,2,
		 0,1,1,1,1,1,0,
		 2,2,2,2,2,2,2,
		 0,0,0,0,0,0,0,
		 0,0,0,0,0,0,0		
		},
		//P
		{0,0,0,0,0,0,0,
		 0,0,0,0,0,0,0,
		 0,0,2,2,2,2,0,
		 0,2,2,1,1,2,0,
		 0,0,1,1,1,2,0,
		 0,2,2,2,2,2,0,
		 0,0,0,0,0,0,0		
		},
		//Z
		{0,0,0,0,0,0,0,
		 0,0,0,2,0,2,0,
		 0,2,2,2,1,2,0,
		 0,2,1,1,1,2,0,
		 0,2,1,2,2,2,0,
		 0,2,0,2,0,0,0,
		 0,0,0,0,0,0,0		
		},
		//W
		{0,0,0,0,0,0,0,
		 0,0,2,2,2,2,0,
		 0,2,2,1,1,0,0,
		 0,2,1,1,2,2,0,
		 0,2,1,2,2,0,0,
		 0,2,0,2,0,0,0,
		 0,0,0,0,0,0,0		
		},
		//X
		{0,0,0,0,0,0,0,
		 0,0,2,0,2,0,0,
		 0,2,2,1,2,2,0,
		 0,0,1,1,1,0,0,
		 0,2,2,1,2,2,0,
		 0,0,2,0,2,0,0,
		 0,0,0,0,0,0,0		
		},
		//V
		{0,0,0,0,0,0,0,
		 0,0,0,2,0,2,0,
		 0,0,0,2,1,2,0,
		 0,2,2,2,1,2,0,
		 0,0,1,1,1,2,0,
		 0,2,2,2,2,2,0,
		 0,0,0,0,0,0,0		
		},
		//T
		{0,0,0,0,0,0,0,
		 0,0,2,0,2,0,0,
		 0,0,2,1,2,0,0,
		 0,2,2,1,2,2,0,
		 0,0,1,1,1,0,0,
		 0,2,2,2,2,2,0,
		 0,0,0,0,0,0,0		
		},
		//Y
		{0,0,0,0,0,0,0,
		 0,0,0,0,0,0,0,
		 0,0,0,2,0,2,0,
		 0,2,2,2,1,2,2,
		 0,0,1,1,1,1,0,
		 0,2,2,2,2,2,2,
		 0,0,0,0,0,0,0		
		},
		//F
		{0,0,0,0,0,0,0,
		 0,0,2,0,2,0,0,
		 0,2,2,1,2,0,0,
		 0,0,1,1,2,2,0,
		 0,2,2,1,1,0,0,
		 0,0,2,2,2,2,0,
		 0,0,0,0,0,0,0		
		},
		//L
		{0,0,0,0,0,0,0,
		 0,2,0,2,0,0,0,
		 0,2,1,2,2,2,2,
		 0,2,1,1,1,1,0,
		 0,2,2,2,2,2,2,
		 0,0,0,0,0,0,0,
		 0,0,0,0,0,0,0		
		},
		//S
		{0,0,0,0,0,0,0,
		 0,0,0,0,0,0,0,
		 0,0,0,2,2,2,2,
		 0,2,2,2,1,1,0,
		 0,0,1,1,1,2,2,
		 0,2,2,2,2,2,0,
		 0,0,0,0,0,0,0		
		},
		//U
		{0,0,0,0,0,0,0,
		 0,2,0,2,0,2,0,
		 0,2,1,2,1,2,0,
		 0,2,1,1,1,2,0,
		 0,2,2,2,2,2,0,
		 0,0,0,0,0,0,0,
		 0,0,0,0,0,0,0		
		},	
	};
	
	PentapipeFigure[] figures;
	
	public void init() {
		ArrayList list = new ArrayList();
		PentapipeField f = new PentapipeField();
		f.setSize(7);
		for (int i = 0; i < forms.length; i++) {
			PentapipeFigure figure = new PentapipeFigure(i, letters[i], forms[i], f);
			list.add(figure);
			int[] form = transformForm(forms[i], f.rotation());
			for (int k = 0; k < 4; k++) {
				figure = new PentapipeFigure(i, letters[i], form, f);
				if(contains(list, figure)) break;
				list.add(figure);
				form = transformForm(form, f.rotation());
			}
			form = transformForm(forms[i], f.reflection());
			for (int k = 0; k < 4; k++) {
				figure = new PentapipeFigure(i, letters[i], form, f);
				if(contains(list, figure)) break;
				list.add(figure);
				form = transformForm(form, f.rotation());
			}
		}		
		figures = (PentapipeFigure[])list.toArray(new PentapipeFigure[0]);
	}
	
	boolean contains(ArrayList list, PentapipeFigure f) {
		for (int i = 0; i < list.size(); i++) {
			PentapipeFigure fi = (PentapipeFigure)list.get(i);
			if(fi.equals(f)) return true;
		}
		return false;
	}
	
	int[] transformForm(int[] form, int[] transform) {
		int[] f = new int[form.length];
		for (int i = 0; i < f.length; i++) {
			f[i] = form[transform[i]];
		}
		return f;
	}

}
