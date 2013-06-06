package slava.puzzles.pusher.solve;

public class PusherState {
	int pusher;
	int[] boxes;

	PusherState parent;

	public PusherState() {}

	public int getPusher() {
		return pusher;
	}

	public int[] getBoxes() {
		return boxes;
	}

	public PusherState copy() {
		PusherState s = new PusherState();
		s.pusher = pusher;
		s.boxes = new int[this.boxes.length];
		System.arraycopy(this.boxes, 0, s.boxes, 0, this.boxes.length);
		s.parent = this;
		return s;
	}

	public String code() {
		StringBuilder sb = new StringBuilder();
		sb.append(pusher);
		for (int i = 0; i < boxes.length; i++) {
			if(boxes[i] > 0) sb.append(":").append(i);
		}
		return sb.toString();
	}

	public PusherState pushToCopy(PusherField f, int d) {
		PusherState copy = copy();
		boolean result = copy.push(f, d);
		return result ? copy : null;
	}

	public boolean push(PusherField f, int d) {
		int p = f.getField().jump(pusher, d);
		if(p < 0 || f.isWall(p)) return false;
		if(boxes[p] == 1) {
			int q = f.getField().jump(p, d);
			if(q < 0 || f.isWall(q) || boxes[q] == 1) return false;
			boxes[q] = 1;
			boxes[p] = 0;
		}
		pusher = p;
		return true;
	}

	public boolean canPush(PusherField f, int d) {
		int p = f.getField().jump(pusher, d);
		if(p < 0 || f.isWall(p)) return false;
		if(boxes[p] == 1) {
			int q = f.getField().jump(p, d);
			if(q < 0 || f.isWall(q) || boxes[q] == 1) return false;
		}
		return true;
	}

	public PusherState pullToCopy(PusherField f, int d, boolean withBox) {
		PusherState copy = copy();
		boolean result = copy.pull(f, d, withBox);
		return result ? copy : null;
	}

	public boolean pull(PusherField f, int d, boolean withBox) {
		int p = f.getField().jump(pusher, d);
		if(p < 0 || f.isWall(p) || boxes[p] == 1) return false;
		if(!withBox) {
			pusher = p;
			return true;
		}
		d += 2;
		if(d >= 4) d -= 4;
		int q = f.getField().jump(pusher, d);
		if(q >= 0 && boxes[q] == 1) {
			boxes[q] = 0;
			boxes[pusher] = 1;
			pusher = p;
			return true;
		}
		return false;
	}

	public boolean isHopeless(PusherField f, int[] finalBoxes) {
		for (int p = 0; p < finalBoxes.length; p++) {
			if(boxes[p] == 0 || finalBoxes[p] == 1) continue;
			//TODO
		}
		return false;
	}

}
