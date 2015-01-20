package slava.puzzles.game;

import java.io.File;
import java.util.ArrayList;
//import java.util.HashMap;
import java.util.List;
//import java.util.Map;

public class Solver implements GameConstants {
	IPosition position;

//	Map<String, Result> results = new HashMap<String, Result>();
	ResultStorage results = new ResultStorage();

	public Solver() {
		String[] draws = {"D.5", "D:5", "D.H", "D:H", "Y.H", "Y:H", "5.Y", "5:Y",
						  "5.D", "5:D", "H.D", "H:D", "H.Y", "H:Y", "Y.5", "Y:5",
//						  "w5:D", "05:D", "t5:D", "Ht:D", "Hw:D", "H0:D",
//						  "H0:Y", "Hw:Y", "Ht:Y", "Df:5", "Dj:5", "Dm:5",
//						  "Dm:H", "Dj:H", "Df:H", "fY:H", "mY:H", "jY:H",
//						  "mY:5", "jY:5", "fY:5",
//						  "efY:5", "efY:H", "Def:H",
//						  "eW.oU", "e^.oU", "e3.oU", "e^.oU", "eW.oU", "eP.oU", "eJ.oU", "eP.oY",
//						  "be^.oY", "be3.oY", "beW.oY", "beP.oY", "beJ.oY",
//						  "be7.Do", "be9.Do", "be^.Do", "be1.Do", "be3.Do", "beW.Do", "beJ.Do", "beI.Do", "beM.Do",
//						  "be^.oU", "be3.oU", "beW.oU", "beP.oU", "beJ.oU",
//						  
//						  "g7.Mr", "g7.Er"
						  };
		for (String s: draws) addResult(s, DRAW, null, 0);
	}

	public void loadStorage(String fileName) {
		StateReader s = new StateReader();
		s.setFile(new File(fileName));
		while(s.hasNext()) {
			s.next();
			String code = s.getCurrentCode();
			Result r = s.getCurrentResult();
			results.put(code, r.getResult(), r.getBestMove(), r.getDepth());
		}
	}

	int storageSize = 100000;
	byte addResult(String code, byte result, Object bestMove, int depth) {
		result = results.put(code, result, bestMove, depth);
//		if(result == DRAW) {
//			System.out.println(code + " draw");
//		}
		if(results.size() == storageSize) {
			storageSize += 100000;
			System.out.println(results.size());
		}
		return result;
	}

	public void setPosition(IPosition position) {
		this.position = position;
	}

	public Object getBestMove() {
		Result r = results.get(position.getCode());
		if(r != null && r.getBestMove() != null) {
			return r.getBestMove();
		}
		return null;
	}

	public ResultStorage getResults() {
		return results;
	}

	public int solve() {
		storageSize = getResults().size() + 100000;
		storageSize -= (storageSize % 100000);
		int result = position.getResult();
		if(result != INDEFINITE) {
			return result;
		}
		byte turnToWin = position.getTurn();
		for (int depth = 40; depth < 600; depth += 10) {
//			System.out.println("Depth=" + depth);
			int r = solve(depth, turnToWin);
			if(r != INDEFINITE) {
				return r;
			}
			turnToWin = (byte)(1 - turnToWin);
		}		
		return INDEFINITE;
	}

	byte solve(int depth, byte turnToWin) {
		byte result = position.getResult();
		if(result != INDEFINITE) {
			return result;
		}
		String code = position.getCode();
		Result r0 = results.get(code);
		if(r0 != null) {
			if(r0.getResult() != INDEFINITE) {
				return r0.getResult();
			} else if(depth <= r0.getDepth()) {
				return INDEFINITE;
			}
		}
		int turn = position.getTurn();
		Result rdraw = null;
		boolean indefinite = false;
		List<? extends IMove> moves = position.getMoves();
		if(moves.size() == 0) throw new RuntimeException();
		List<IMove> imoves = new ArrayList<IMove>();
		for (IMove move: moves) {
			move.apply();
			String c = position.getCode();
			move.back();
			Result r = results.get(c);
			if(r == null) {
				imoves.add(move);
			} else if(r.getResult() == WHITE_WIN) {
				if(turn == WHITE) {
					return addResult(code, WHITE_WIN, move.getInfo(), 0);
				}
			} else if(r.getResult() == BLACK_WIN) {
				if(turn == BLACK) {
					return addResult(code, BLACK_WIN, move.getInfo(), 0);
				}
			} else if(r.getResult() == DRAW) {
				if(rdraw == null) {
					rdraw = new Result(DRAW, move.getInfo(), 0);
				}
			} else if(r.getResult() == INDEFINITE) {
				if(r.getDepth() < depth) {
					imoves.add(move);
				} else {
					indefinite = true;					
				}
			}
		}
		if(imoves.isEmpty()) {
			if(indefinite) {
				//do nothing
			} else if(rdraw != null) {
				return addResult(code, rdraw.getResult(), rdraw.getBestMove(), rdraw.getDepth());
			} else {
				byte r = turn == BLACK ? WHITE_WIN : BLACK_WIN;;
				return addResult(code, r, null, 0);
			}
		}

		if(turnToWin != turn && indefinite) {
			return addResult(code, INDEFINITE, null, depth);
		}

		int nextDepth = depth - WEIGHTS[imoves.size()] - 1;
		
		if(nextDepth < 0) {
			return addResult(code, INDEFINITE, null, depth);
		}

		boolean first10 = WEIGHTS[imoves.size()] > 10;
		for (IMove move: imoves) {
			move.apply();
			int nd = nextDepth;
			if(first10) {
				nd = depth - 10;
			}
			byte r = solve(nd, turnToWin);
			move.back();
			if(r == WHITE_WIN) {
				if(turn == WHITE) {
					return addResult(code, WHITE_WIN, move.getInfo(), 0);
				}
			} else if(r == BLACK_WIN) {
				if(turn == BLACK) {
					return addResult(code, BLACK_WIN, move.getInfo(), 0);
				}
			} else if(r == DRAW) {
				if(rdraw == null) {
					rdraw = new Result(DRAW, move.getInfo(), 0);
				}
			} else if(r == INDEFINITE) {
				indefinite = true;
				first10 = false;
			}
			if(turnToWin != turn && indefinite) {
				return addResult(code, INDEFINITE, null, depth);
			}
		}
		if(indefinite) {
			return addResult(code, INDEFINITE, null, depth);
		} else if(rdraw != null) {
			return addResult(code, rdraw.getResult(), rdraw.getBestMove(), rdraw.getDepth());
		} else {
			byte r = turn == BLACK ? WHITE_WIN : BLACK_WIN;;
			return addResult(code, r, null, 0);
		}
	}

}
