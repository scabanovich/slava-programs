package ic2006_1;

import forsmarts.distances.DistancesField;

public class RobotRunner {
	static int[] H_RESTR = {
		0,1,0,0,0,0,0,1,0,
		0,0,0,0,0,0,1,0,0,
		0,0,0,0,1,0,1,0,0,
		0,0,0,0,0,1,0,0,0,
		0,0,0,0,0,1,0,0,0,
		0,0,1,0,0,1,0,0,0,
		0,0,1,0,0,0,0,0,0,
		0,1,0,0,0,0,0,0,0,
		0,1,0,0,0,0,1,0,0,
	};
	
	static int[] V_RESTR = {
		0,0,0,0,0,0,0,1,0,
		0,0,1,0,0,0,0,0,1,
		1,0,0,0,0,0,0,1,0,
		0,0,0,0,0,0,0,0,0,
		0,0,1,1,0,1,0,1,1,
		0,0,0,0,0,1,0,0,0,
		0,0,0,0,0,0,0,0,0,
		0,1,0,0,0,0,0,0,0,
		0,0,0,0,0,0,0,0,0
	};
	
	DistancesField field;
	
	int[] program;
	
	public void setField(DistancesField field) {
		this.field = field;
	}
	
	public void setRestrictions(int[] hRestrictions, int[] vRestrictions) {
		for (int p = 0; p < field.getSize(); p++) {
			if(hRestrictions[p] == 1) {
				int q = field.jump(p, 0);
				field.prohibitJump(p, 0);
				if(q >= 0) field.prohibitJump(q, 2);
			}
			if(vRestrictions[p] == 1) {
				int q = field.jump(p, 1);
				field.prohibitJump(p, 1);
				if(q >= 0) field.prohibitJump(q, 3);
			}
		}
	}
	
	public boolean run(int start, int end) {
		int[][] usage = new int[field.getSize()][program.length];
		usage[start][0]++;
		int t = 0;
		int p = start;
		while(true) {
			int d = program[t];
			int q = field.jump(p, d);
			if(q == end) return true;
			if(q >= 0) p = q;
			if(usage[p][t] == 1) return false;
			usage[p][t]++;
			++t;
			if(t == program.length) t = 0;
		}
	}
	
	static char[] LETTERS = {'R', 'D', 'L', 'U'};
	
	public void runPrograms(int length, int start, int end) {
		program = new int[length];
		for (int i = 0; i < length; i++) program[i] = 0;
		while(true) {
			boolean b = run(start, end);
			if(b) {
				System.out.println("Solution");
				printProgram();
//				break;
			}
			if(!nextProgram()) break;
		}
		
	}
	
	boolean nextProgram() {
		for (int i = program.length - 1; i >= 0; i--) {
			program[i]++;
			if(program[i] < 4) return true;
			program[i] = 0;
		}
		return false;
	}
	
	void printProgram() {
		for (int i = 0; i < program.length; i++) {
			System.out.print(LETTERS[program[i]]);
		}
		System.out.println("");
	}	
	
	public static void main(String[] args) {
		RobotRunner runner = new RobotRunner();
		DistancesField f = new DistancesField();
		f.setSize(9, 9);
		runner.setField(f);
		runner.setRestrictions(H_RESTR, V_RESTR);
		int start = 8 * 9;
		int end = 8;
		runner.runPrograms(9, start, end);
	}
	
}
//UULURDRDR