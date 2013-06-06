package match2005.rubic;

public class RubicRandomStateGenerator implements RubicConstants {
	RubicState state;
	String path;

	public RubicState generateState(int length) {
		state = RubicState.getOrigin();
		StringBuffer sb = new StringBuffer();
		int rp = -1;
		for (int i = 0; i < length; i++) {
			int r = (int)(ROTATIONS.length * Math.random());
			while(rp >= 0 && ROTATIONS[r].axe == ROTATIONS[rp].axe &&
					ROTATIONS[r].range == ROTATIONS[rp].range) {
				r = (int)(ROTATIONS.length * Math.random());
			}
			rp = r;
			ROTATIONS[r].execute(state);
			if(i > 0) sb.append(" ");
			sb.append(ROTATIONS_NOTATION[r]);
		}
		path = sb.toString();
		return state;
	}
	
	public String getPath() {
		return path;
	}


}
