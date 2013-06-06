package pqrst15;

import java.util.*;

public class ChocolateBar {
	Piece piece;
	
	Section[] sections;
	
	public void setPiece(Piece piece) {
		this.piece = piece;
		sections = new Section[]{new Section(new Piece[]{piece})};
	}
	
	public void execute() {
		for (int i = 0; i < 3; i++) makeCut();
		Set set = new TreeSet();
		for (int i = 0; i < sections.length; i++) {
			if(sections[i].isOfDifferentPieces()) {
				set.add(sections[i].getKey());
			}
		}
		System.out.println("Size=" + set.size());
		Iterator it = set.iterator();
		while(it.hasNext()) System.out.println(it.next());
	}
	
	void makeCut() {
		ArrayList list = new ArrayList();
		for (int i = 0; i < sections.length; i++) {
			Section s = sections[i];
			for (int j = 0; j < s.pieces.length; j++) {
				Piece p = s.pieces[j];
				for (int k = 1; k <= p.length / 2; k++) {
					Piece[] ps = new Piece[s.pieces.length + 1];
					System.arraycopy(s.pieces, 0, ps, 0, s.pieces.length);
					ps[j] = new Piece(k, p.width);
					ps[s.pieces.length] = new Piece(p.length - k, p.width);
					list.add(new Section(ps));
				}
				for (int k = 1; k <= p.width / 2; k++) {
					Piece[] ps = new Piece[s.pieces.length + 1];
					System.arraycopy(s.pieces, 0, ps, 0, s.pieces.length);
					ps[j] = new Piece(p.length, k);
					ps[s.pieces.length] = new Piece(p.length, p.width - k);
					list.add(new Section(ps));
				}
			}
		}
		sections = (Section[])list.toArray(new Section[0]);
	}
	
	public static void main(String[] args) {
		ChocolateBar bar = new ChocolateBar();
		Piece p = new Piece(5, 9);
		bar.setPiece(p);
		bar.execute();
	}

// f(9,5) = 99
}

class Section {
	Piece[] pieces;
	
	public Section(Piece[] pieces) {
		this.pieces = pieces;
	}
	
	public int getPieceCount() {
		return pieces.length;
	}
	
	public int[] getSortedSquares() {
		int[] s = new int[pieces.length];
		for (int i = 0; i < pieces.length; i++) {
			s[i] = pieces[i].square();
		}
		Arrays.sort(s);
		return s;
	}
	
	public boolean isOfDifferentPieces() {
		int[] s = getSortedSquares();
		for (int i = 1; i < s.length; i++) {
			if(s[i] == s[i - 1]) return false;
		}
		return true;
	}
	
	public String getKey() {
		int[] s = getSortedSquares();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length; i++) {
			sb.append(" " + s[i]);
		}
		return sb.toString();
	}

}

class Piece {
	int length;
	int width;
	
	public Piece(int length, int width) {
		this.width = width;
		this.length = length;
	}
	
	public int square() {
		return width * length;
	}
}
