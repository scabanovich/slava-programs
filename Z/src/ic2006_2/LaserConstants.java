package ic2006_2;

public interface LaserConstants {
	public int[] REVERSE = {4,5,6,7,0,1,2,3};
	public int[] CODES = {1,2,4,8,16,32,64,128};

	public int A = CODES[3] + CODES[7];
	public int B = CODES[0] + CODES[2] + CODES[5];
	public int C = CODES[1] + CODES[4] + CODES[6];
	public int D = CODES[0] + CODES[2] + CODES[4] + CODES[6];
	public int E = CODES[1] + CODES[3] + CODES[5] + CODES[7];
	public int F = CODES[1] + CODES[5];

}
