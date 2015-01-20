package slava.puzzles.checkers;

import java.util.ArrayList;
import java.util.List;

import slava.puzzles.game.GameConstants;

public class MoveSearcher implements CheckersConstants, GameConstants {
	CheckersPosition position;
	
	public MoveSearcher() {}
	
	public void setPosition(CheckersPosition position) {
		this.position = position;
	}

	public CheckersField getField() {
		return position.getField();
	}

	public List<Move> getMoves(int turn) {
		List<Move> result = new ArrayList<Move>();
		for (byte p = 0; p < position.getField().getSize(); p++) {
			byte v = position.getValue(p);
			if((turn == WHITE && v > EMPTY) || (turn == BLACK && v < EMPTY)) {
				result.addAll(getBeatingMoves(p));
			}
		}
		if(result.isEmpty()) {
			for (byte p = 0; p < position.getField().getSize(); p++) {
				byte v = position.getValue(p);
				if((turn == WHITE && v > EMPTY) || (turn == BLACK && v < EMPTY)) {
					result.addAll(getNonBeatingMoves(p));
				}
			}
		}
		return result;
	}

	public List<Move> getNonBeatingMoves(byte p) {
		List<Move> result = new ArrayList<Move>();
		byte value = position.getValue(p);
		if(value == WHITE_PAWN) {
			for (int d = 0; d < 2; d++) {
				byte q = getField().jump(p, d);
				if(q >= 0 && position.getValue(q) == EMPTY) {
					result.add(new Move(position, p, value, q, getNewValue(value, q)));
				}
			}
		} else if(value == BLACK_PAWN) {
			for (byte d = 2; d < 4; d++) {
				byte q = getField().jump(p, d);
				if(q >= 0 && position.getValue(q) == EMPTY) {
					result.add(new Move(position, p, value, q, getNewValue(value, q)));
				}
			}
		} else if(value == WHITE_KING || value == BLACK_KING) {
			for (byte d = 0; d < 4; d++) {
				byte q = getField().jump(p, d);
				while(q >= 0 && position.getValue(q) == EMPTY) {
					result.add(new Move(position, p, value, q, value));
					q = getField().jump(q, d);
				}
			}
			
		}
		return result;
	}

	public List<Move> getBeatingMoves(byte p) {
		List<Move> result = new ArrayList<Move>();
		byte value = position.getValue(p);
		position.setValue(p, EMPTY);
		for (byte d = 0; d < 4; d++) {
			byte q = findPrey(value, null, p, d);
			if(q >= 0) {
				byte q2 = getField().jump(q, d);
				byte newValue = getNewValue(value, q2);
				collectMoves(p, value, q2, d, newValue, new ChainedPlace(q), result);
			}
		}
		position.setValue(p, value);
		return result;
	}

	byte getNewValue(byte value, byte p) {
		if(value == WHITE_PAWN && getField().y[p] == 7) {
			return WHITE_KING;
		} else if(value == BLACK_PAWN && getField().y[p] == 0) {
			return BLACK_KING;
		} else {
			return value;
		}
	}

	byte findPrey(byte value, ChainedPlace beaten, byte p, byte d) {
		byte q = (value == WHITE_PAWN || value == BLACK_PAWN)
				? findPawnPrey(value, p, d) : (value == WHITE_KING || value == BLACK_KING)
				? findKingPrey(value, p, d) : -1;
		if(q >= 0 && beaten != null && beaten.contains(q)) {
			return -1;
		}
		return q;
	}

	byte findPawnPrey(byte value, byte p, byte d) {
		p = getField().jump(p, d);
		if(p >= 0 && value * position.getValue(p) < 0) {
			byte q = getField().jump(p, d);
			return (q >= 0 && position.getValue(q) == EMPTY) ? p : -1;
		}
		return -1;
	}

	byte findKingPrey(byte value, byte p, byte d) {
		p = getField().jump(p, d);
		while(p >= 0) {
			int vv = value * position.getValue(p);
			if(vv > 0) {
				return -1;
			} else if(vv < 0) {
				byte q = getField().jump(p, d);
				return (q >= 0 && position.getValue(q) == EMPTY) ? p : -1;
			}
			p = getField().jump(p, d);			
		}
		return -1;
	}

	void collectMoves(byte start, byte startValue, byte p, byte d, byte value, ChainedPlace beaten, List<Move> result) {
		byte[] di = {
			(d == 3) ? (byte)0 :(byte)(d + 1),
			(d == 0) ? (byte)3 : (byte)(d - 1)
		};
		int size = result.size();
		{
			byte q = findPrey(value, beaten, p, d);
			if(q >= 0 && !beaten.contains(q)) {
				byte q2 = getField().jump(q, d);
				byte newValue = getNewValue(value, q2);
				collectMoves(start, startValue, q2, d, newValue, beaten.addNext(q), result);
				beaten.dropNext();
			}
		}		
		byte pi = p;
		while(pi >= 0 && position.getValue(pi) == EMPTY) {
			for (int i = 0; i < 2; i++) {
				byte q = findPrey(value, beaten, pi, di[i]);
				if(q >= 0) {
					byte q2 = getField().jump(q, di[i]);
					byte newValue = getNewValue(value, q2);
					collectMoves(start, startValue, q2, di[i], newValue, beaten.addNext(q), result);
					beaten.dropNext();
				}
			}
			if(value != WHITE_KING && value != BLACK_KING) {
				break;
			}
			pi = getField().jump(pi, d);
		}
		if(result.size() == size) {
			pi = p;
			while(pi >= 0 && position.getValue(pi) == EMPTY) {
				Move move = new Move(position, start, startValue, pi, value);
				move.setBeaten(beaten.toArray());
				result.add(move);
				if(value != WHITE_KING && value != BLACK_KING) {
					break;
				}
				pi = getField().jump(pi, d);
			}
		}
	}

}
