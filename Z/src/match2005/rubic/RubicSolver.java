package match2005.rubic;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class RubicSolver implements RubicConstants {
	static Vicinity[] vicinities = new Vicinity[]{
		new Vicinity(RubicVicinityMarker.RUBIC_CORNERS_TASK),
		new Vicinity(RubicVicinityMarker.RUBIC_CUBE_RS_TASK),
		new Vicinity(RubicVicinityMarker.RUBIC_EDGES_TASK),
	};
	
	RubicState origin = RubicState.getOrigin();
	RubicState state;
	int maximumTime = 20;
	
	String outFileName;
	
	public RubicSolver() {
	}
	
	public void setState(RubicState state) {
		this.state = state;
	}
	
	public void setMaximumTime(int t) {
		maximumTime = t;
	}
	
	public void setOutFileName(String f) {
		outFileName = f;
	}
	
	public String execute() {
		int time = 0;
		for (int i = 0; i < vicinities.length; i++) {
			int code = vicinities[i].task.getStateCode(state);
			if(time < vicinities[i].states[code]) time = vicinities[i].states[code];
		}
		
		RMS solver1 = new RMS();
		long t = System.currentTimeMillis();
		while(time <= maximumTime && solver1.solution == null) {
			long dt = System.currentTimeMillis() - t;
			dt = dt/1000;
			String m = "Moves=" + time + " dt=" + (dt / 60) + "m" + (dt % 60) + "s";
			System.out.println(m);
			solver1.setState(state.copy());
			solver1.setTimeLimit(time);
			solver1.solve();
			++time;
		}
		String message = "Solution=" + solver1.solution + "\n";
		System.out.print(message);
		if(outFileName != null) {
			try {
				File f = new File(outFileName);
				if(!f.exists()) f.createNewFile();
				FileOutputStream os = new FileOutputStream(f, true);
				BufferedOutputStream bs = new BufferedOutputStream(os);
				bs.write(state.getStates().getBytes());
				bs.write("\n".getBytes());
				bs.write(message.getBytes());
				bs.close();
				os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return solver1.solution;
	}
	
}

class RMS extends RubicMover {
	RubicState origin = RubicState.getOrigin();
	String solution;
	
	protected boolean canContinue() {
		if(solution != null) return false;
		if(t >= timeLimit) return false;
		for (int i = 0; i < RubicSolver.vicinities.length; i++) {
			int code = RubicSolver.vicinities[i].task.getStateCode(state);
			int dt = RubicSolver.vicinities[i].states[code];
			if(t + dt > timeLimit) return false;
		}
		return true;
	}
	
	protected boolean isSolution() {
		if(t < timeLimit) return false;
		return origin.equals(state);
	}
	
	protected void onSolutionFound() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < t; i++) {
			int r = ways[i][way[i]];
			String ds = ROTATIONS_NOTATION[r];
			if(sb.length() > 0) sb.append(" ");
			sb.append(ds);
		}
		solution = sb.toString();
	}
	
}
