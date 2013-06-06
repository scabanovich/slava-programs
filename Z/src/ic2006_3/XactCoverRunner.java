package ic2006_3;

import com.slava.chess.ChessFigures;

public class XactCoverRunner {
	static int[] FORM = {
		1,0,0,0,0,1,0,0,0,0,
		0,1,1,1,0,0,0,1,1,1,
		0,1,0,0,1,0,1,0,0,1,
		0,1,0,0,1,0,1,0,0,1,
		0,0,1,1,1,0,1,1,1,0,
		0,0,0,0,0,1,0,0,0,0,
		0,0,0,0,1,0,1,0,0,0,
		0,0,0,1,0,0,0,1,0,0
	};

	public static void main(String[] args) {
		ChessFigures fgs = new ChessFigures(10, 8);
		XactCoverSolver solver = new XactCoverSolver();
		solver.setFigures(fgs);
		solver.setForm(FORM);
		solver.solve();
	}

}

/**
t=16
 1 N N N N 1 N N N N
 2 1 1 1 2 2 3 1 1 1
 3 1 3 2 1 2 1 3 2 1
 R 1 1 2 1 1 1 1 2 1
 2 N 1 1 1 1 1 1 1 B
 2 + 1 3 K 1 1 K 2 +
 2 N 1 2 1 1 1 3 2 1
 1 N + 1 + + 1 1 K 1

Nb8,Nc8,Nd8,Ne8,Ng8,Nh8,Ni8,Nj8,Ra5,Nb4,Bj4,Ke3,Kh3,Nb2,Nb1,Ki1

Ki1,Ke3,Kh3,Ra5,Nb1,Nb2,Nb4,Nb8,Nc8,Nd8,Ne8,Ng8,Nh8,Ni8,Nj8,Bj4
*/