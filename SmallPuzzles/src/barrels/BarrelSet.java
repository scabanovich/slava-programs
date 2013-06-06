package barrels;

public class BarrelSet {
	Barrel[] barrels;
	
	public BarrelSet(Barrel[] barrels) {
		this.barrels = barrels;
	}
	
	public void pour(int from, int to, int amount) {
		barrels[from].pourTo(barrels[to], amount);
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < barrels.length; i++) {
			sb.append(" ").append(barrels[i].toString());
		}
		return sb.toString();
	}
	
	public void run(int[][] alg) {
		for (int i = 0; i < alg.length; i++) {
			pour(alg[i][0], alg[i][1], alg[i][2]);
			System.out.println(toString());
		}
	}
	
	static int W = 0;
	static int F = 1;
	static int A = 2;
	static int B = 3;
	static int C = 4;
	
	static int[][] ALG = {
		{F,C,4}, //2
		{F,A,3},{W,A,1},{A,C,1},{A,F,2}, //3
		{W,A,2},{A,F,2}, //4
		{W,A,1},{A,F,1}, //5
		{W,A,1},{A,C,1}, //6
		{W,A,1},{A,F,1}, //7
		{W,A,1},{A,C,1}, //8
		{W,A,2},{A,F,1},{A,B,1}, //9
		{W,A,2},{A,B,2}, //10
		{W,A,1},{A,C,1}, //11
		{W,A,2},{A,B,2}, //12
		{W,A,2},{A,B,2}, //13
		{W,A,2},{A,C,2}, //14
		{W,A,2},{A,B,2}, //15
		{W,A,1},{A,B,1}, //16
		{W,A,1},{A,C,1}, //17
		{W,A,2},{A,B,2}, //18
		{W,A,2},{A,B,2}, //19
		{W,A,2},{A,B,1},{A,C,1}, //20
		{W,A,2},{A,B,2}, //21
		{W,A,1},{A,B,1}, //22
		{W,A,1},{A,B,1}, //23
		{W,A,1},{A,C,1}, //24	
		{W,A,1},{A,B,1}, //25
		{W,A,1},{A,C,1}, //26	
		{W,A,2},{A,C,1}, //27
		{W,A,3},{A,C,1}, //28
		{W,A,8},{A,C,1}, //29
		{W,A,7},{A,C,1}, //30
		{W,C,2}          //31
	};

	static int[][] PRELUCKII = {
		{F,A,2},{W,A,7}, //1
		{F,B,1},{A,B,2},{W,B,3}, //2
		{F,C,1},{W,C,4}, //3
		{C,A,3}, //4
		{F,C,3}, {W,C,9}, //5
		{C,B,3}
	};

	public static void main(String[] args) {
		Barrel[] bs = new Barrel[]{
			new Barrel(100, 100),
			new Barrel(20, 0),
			new Barrel(0, 0),
			new Barrel(0, 0),
			new Barrel(0, 0),
		};
		BarrelSet set = new BarrelSet(bs);
		//set.run(ALG);
		set.run(PRELUCKII);
		
	}

}
