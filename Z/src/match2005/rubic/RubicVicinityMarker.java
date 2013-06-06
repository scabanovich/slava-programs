package match2005.rubic;

import java.io.File;
import java.io.FileOutputStream;

public class RubicVicinityMarker {
	static VicinityTask RUBIC_CORNERS_TASK = 
		new VicinityTask("rubicCorners.txt", 2187 * 40320, true) {
		public int getStateCode(RubicState state) {
			return state.cornerCode();
		}		
	};
	static VicinityTask RUBIC_CUBE_RS_TASK = 
		new VicinityTask("rubicCubeRs.txt", 2187 * 2048, true) {
		public int getStateCode(RubicState state) {
			return state.rotationCode();
		}		
	};
	static VicinityTask RUBIC_EDGES_TASK = 
		new VicinityTask("rubicEdges.txt", RubicState.FACTORIALS[11], false) {
		public int getStateCode(RubicState state) {
			return state.edgeCode(module);
		}		
	};
	static VicinityTask RUBIC_PARTIAL_A_TASK = 
		new VicinityTask("rubicPartialA.txt", 24 * 24 * 24 * 24, false) {
		public int getStateCode(RubicState state) {
			return state.partialCode();
		}		
	};
	static VicinityTask RUBIC_COLOR_TASK = 
		new VicinityTask("rubicColor.txt", 8000000, false) {
		public int getStateCode(RubicState state) {
			return state.coloringCode();
		}		
	};

	static byte MAXIMUM = (byte)255;
	
	VicinityTask task = RUBIC_EDGES_TASK;
	int tMax = 8;

	byte[] states = new byte[task.module];
	
	public RubicVicinityMarker() {
		for (int i = 0; i < states.length; i++) states[i] = MAXIMUM;
	}
	
	int delta = 0;

	class RM extends RubicMover {
		protected void onSolutionFound() {
			int code = task.getStateCode(state);
			if(states[code] == MAXIMUM) {
				delta++;
				states[code] = (byte)t;
			}
		}
		protected boolean canContinue() {
			if(task.complete) {
				int code = task.getStateCode(state);
				if(states[code] != MAXIMUM && states[code] < t) return false;
			}
			return super.canContinue();
		}
		
	}

	public void execute() {
		RubicState origin = RubicState.getOrigin();
		RubicMover mover = new RM();
		states[task.getStateCode(origin)] = 0;
		for (int t = 1; t < tMax; t++) {
			delta = 0;
			mover.setTimeLimit(t);
			mover.setState(RubicState.getOrigin());
			mover.solve();
			System.out.println("t=" + t + " states=" + delta);
		}
		delta = 0;
		for (int i = 0; i < states.length; i++) {
			if(states[i] == MAXIMUM) {
				states[i] = (byte)tMax;
				delta++;
			}
		}
		System.out.println("t=" + tMax + " states=" + delta);
		save();
	}
	
	private void save() {
		try {
			FileOutputStream os = new FileOutputStream(new File(task.file));
			os.write(states);
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
