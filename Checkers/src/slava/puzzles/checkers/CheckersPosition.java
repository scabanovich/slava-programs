package slava.puzzles.checkers;

import java.util.List;

import slava.puzzles.game.IPosition;

public class CheckersPosition implements CheckersConstants, IPosition {
	CheckersField field;

	MoveSearcher searcher = new MoveSearcher();

	byte turn = WHITE;

	byte[] state;

	public CheckersPosition() {
		searcher.setPosition(this);
	}

	public void setField(CheckersField field) {
		this.field = field;
		state = new byte[field.getSize()]; //empty
	}

	public void setPosition(String code) {
		int f = WHITE;
		for (int i = 0; i < state.length; i++) {
			state[i] = EMPTY;
		}
		for (int i = 0; i < code.length(); i++) {
			char ch = code.charAt(i);
			if(ch == '.') {
				setTurn(WHITE);
				f = BLACK;
			} else if(ch == ':') {
				setTurn(BLACK);
				f = BLACK;
			} else {
				for (byte k = 0; k < PAWN_NOTE.length; k++) {
					if(PAWN_NOTE[k] == ch) {
						if(f == WHITE) setValue(k, WHITE_PAWN); else setValue(k, BLACK_PAWN);
					} else if(KING_NOTE[k] == ch) {
						if(f == WHITE) setValue(k, WHITE_KING); else setValue(k, BLACK_KING);
					}
				}
			}
		}
		if(!getCode().equals(code)) {
			throw new RuntimeException("Set " + code + " but obtained " + getCode());
		}
	}

	public CheckersField getField() {
		return field;
	}

	@Override
	public byte getTurn() {
		return turn;
	}

	public void setTurn(byte t) {
		turn = t;
	}

	public byte getValue(byte p) {
		return state[p];
	}

	public void setValue(byte p, byte v) {
		state[p] = v;
	}

	@Override
	public byte getResult() {
		return getMoves().isEmpty() ? (turn == WHITE ? WHITE_WIN : BLACK_WIN) : INDEFINITE;
	}

	static char[] PAWN_NOTE = {
		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 
		'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
		'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
		'y', 'z', '0', '2', '4', '6', '8', '*'
	};

	static char[] KING_NOTE = {
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 
		'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
		'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
		'Y', 'Z', '1', '3', '5', '7', '9', '^'
	};

	@Override
	public String getCode() {
		StringBuilder sb = new StringBuilder();
		for (int p = 0; p < field.getSize(); p++) {
			if(state[p] == WHITE_PAWN) {
				sb.append(PAWN_NOTE[p]);
			} else if(state[p] == WHITE_KING) {
				sb.append(KING_NOTE[p]);
			}
		}
		if(turn == WHITE) sb.append('.'); else sb.append(':');
		for (int p = 0; p < field.getSize(); p++) {
			if(state[p] == BLACK_PAWN) {
				sb.append(PAWN_NOTE[p]);
			} else if(state[p] == BLACK_KING) {
				sb.append(KING_NOTE[p]);
			}
		}
		return sb.toString();
	}

	@Override
	public List<Move> getMoves() {
		return searcher.getMoves(turn);
	}
}
