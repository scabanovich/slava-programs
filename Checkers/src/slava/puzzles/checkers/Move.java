package slava.puzzles.checkers;

import slava.puzzles.game.IMove;

public class Move implements IMove, CheckersConstants {
	CheckersPosition position;

	byte start;
	byte startValue;
	byte end;
	private byte[] beaten;
	byte[] beatenValues;
	byte endValue;

	public Move(CheckersPosition position, byte start, byte startValue, byte end, byte endValue) {
		this.position = position;
		this.startValue = startValue;
		if(startValue == EMPTY) throw new RuntimeException();
		this.start = start;
		this.end = end;
		this.endValue = endValue;
		if(endValue == EMPTY) throw new RuntimeException();
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public void setBeaten(byte[] beaten) {
		this.beaten = beaten;
		for (int i = 0; i < beaten.length; i++) {
			if(position.getValue(beaten[i]) == EMPTY) {
				throw new RuntimeException();
			}
		}
	}

	public int getBeatenCount() {
		return beaten == null ? 0 : beaten.length;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(start).append("-->").append(end);
		sb.append("; ").append(endValue);
		sb.append("; ");
		if(beaten !=null) for (int i = 0; i < beaten.length; i++) {
			sb.append(" " + beaten[i]);
		}
		return sb.toString();
	}

	@Override
	public MoveInfo getInfo() {
		return new MoveInfo(start, end, beaten, endValue);
	}

	@Override
	public void apply() {
		position.setValue(start, EMPTY);
		position.setValue(end, endValue);
		if(beaten != null) {
			beatenValues = new byte[beaten.length];
			for (int i = 0; i < beaten.length; i++) {
				byte p = beaten[i];
				beatenValues[i] = position.getValue(p);
				if(beatenValues[i] == EMPTY) {
					System.out.println(getInfo());
					throw new RuntimeException();
				}
				position.setValue(p, EMPTY);
			}
		}
		position.turn = (byte)(1 - position.turn);
	}

	@Override
	public void back() {
		position.setValue(start, startValue);
		if(end != start) position.setValue(end, EMPTY);
		if(beaten != null) {
			for (int i = 0; i < beaten.length; i++) {
				byte p = beaten[i];
				position.setValue(p, beatenValues[i]);
			}
		}
		position.turn = (byte)(1 - position.turn);
	}
}

class MoveInfo {
	byte start;
	byte end;
	private byte[] beaten;
	byte endValue;

	MoveInfo(byte start, byte end, byte[] beaten, byte endValue) {
		this.start = start;
		this.end = end;
		this.beaten = beaten;
		this.endValue = endValue;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(start);
		if(beaten != null) for (int i = 0; i < beaten.length; i++) {
			sb.append(":" + beaten[i]);
		}
		sb.append(":").append(end);
		sb.append("=").append(endValue);
		return sb.toString();
	}
}
