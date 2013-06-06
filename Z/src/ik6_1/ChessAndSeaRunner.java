package ik6_1;

public class ChessAndSeaRunner {

	public static void main(String[] args) {
		ChessAndSeaField f = new ChessAndSeaField();
		f.setSize(9, 9);
		ChessAndSeaSolver solver = new ChessAndSeaSolver();
		solver.setField(f);
		solver.solve();
//		solver.dump();
	}

}

/*
Max Weight=64
 N B N + x x x + x
 + N + + + N + + x
 x + x + x x + + N
 + N + + + N + x N
 N + N + x + + + N
 + N + + + N + + x
 N + N + x x x + x
 + + + R + + + + +
 N + N + x x x x N
Code=Rd8, Bb1, Na1, Nc1, Nb2, Nf2, Ni3, Nb4, Nf4, Ni4, Na5, Nc5, Ni5, Nb6, Nf6, Na7, Nc7, Na9, Nc9, Ni9
Max Weight=64
 x x N + x x x + N
 + N + + + N + + B
 x + x + x x + + N
 + N + + + N + x N
 N + N + x + + + N
 + N + + + N + + x
 N + N + x x x + x
 + + + R + + + + +
 N + N + x x x x N
Max Weight=61
 N + x x x + x N x
 x + + N + + x N x
 N + + B x + + + +
 N + + N + + + x +
 x + + x x + N N N
 + + + N + + + N +
 N + x x x + + + +
 + R + + + + + + +
 N + x x x x N N N
Max Weight=60
 N + x x x + N N N
 x + + N + + + N +
 N + x x x + + + +
 + R + + + + + + +
 N + x x x x N N N
 x + + + + + + x +
 N + + x x + + + +
 N + + N N + x N x
 N + + x x + + N x
Max Weight=60
 N N x x x x N N N
 x + + + + + + x +
 + + + x x + + + +
 N + + + N + x N x
 N N + x x + + N x
 N + + + N + + + +
 + + + x x x + + +
 N + + + N + + x N
 N N + x x x + N N
Max Weight=58
 x x x x N N x x N
 + + + + + + + + +
 x x + + + + + + x
 N N + x N N + N N
 x x + + N + + + N
 + + + + + + + + +
 x + x + x + + + N
 x N B N x N + N N
 x + x + x + + + N
Max Weight=58
 N x x x x N x N x
 + + + + + + x + x
 x + + + + + + + +
 N N + N N x + x N
 N + + + N + + x N
 + + + + + + + + +
 N + + + x + x + x
 N N + N x N B N x
 N + + + x + x + x
Max Weight=57
 N x x x x N x N x
 + + + + + + x + x
 x + + + + + + + +
 N N + N N x + x N
 N + + + N + + x N
 + + + + + + + + +
 N + N + x + x + x
 + N + N + N x N x
 N + N + x + x + x
Max Weight=51
 N N + x x x + N N
 N x + + N + + x x
 + + + x x + + + +
 x + + + + + + x +
 N N x x x x N x N
 x + + + + + + x +
 + + + + + + + + +
 N + + N N + + x x
 x + + N N + + N N
Max Weight=51
 x + + N N + + x x
 N + + x x + + N N
 x + + + + + + x x
 N + x x x x + + +
 + R + + + + + + +
 N + x x x + x N N
 x + + N + + + + +
 x + x + + + + + +
 x + + N + N N N N
Max Weight=50
 N N N x x N + N N
 + x + + + + + + N
 + + + + + K + + +
 x x x x + + + + x
 + + + + + + R + +
 + + + x x x + + +
 x x + + N + + + x
 N N + x x x + N N
 x x + + N + + x N

Min Weight=19
 x x + + + + + + x
 + + + K + + K + x
 K + + + + + + + x
 + + x x x x + + +
 x + + + + + + K +
 + + + x + + + + +
 x x N N N x x + K
 + + + x + + + + +
 x + + + + + x x x

Min Weight=19
 + x x x x + x + x
 R + + + + + + + +
 + x + + + x N x x
 + x + K + + + + +
 + x + + + + K + x
 + + + x x + + + +
 + K + + + + + K +
 + + + + K + + + +
 + x x + + + x x x
Min Weight=19
 x x x x + x + x +
 + + + + + + + + R
 x + + + x N x x +
 x + K + + + + + +
 x + + + + K + x +
 + + x x + + + + +
 K + + + + + K + +
 + + + K + + + + +
 x x + + + x x x +
Min Weight=19
 x + x x x + K + x
 x + + + + + + + +
 x + + K + x + K +
 x + + + + x + + +
 + R + + + + + + +
 x + x + x x + + +
 + + x + + + + K +
 x + N + K + + + +
 + + x + + + x x x
Min Weight=19
 x + + + + + x x x
 + + + x + + + + +
 x x N N N x x + K
 + + + x + + + + +
 x + + + + + x x x
 + + + + + + + + +
 + K + + K + + K +
 + + + + + + + + +
 x x + K + x x x x
Code=Nc3, Nd3, Ne3, Ki3, Kb7, Ke7, Kh7, Kd9
*/
