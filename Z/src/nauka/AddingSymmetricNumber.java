package nauka;

public class AddingSymmetricNumber {
	
	public void run(int[] number) {
		print(number);
		while(!isSymmetric(number)) {
			number = add(number);
			if(number.length % 10 == 0) System.out.println(number.length);
		}
		print(number);
	}
	
	int[] add(int[] number) {
		int[] rs = new int[number.length];
		for (int i = 0; i < number.length; i++) {
			rs[i] = number[i] + number[number.length - 1 - i];
		}
		for (int i = 0; i < rs.length - 1; i++) {
			if(rs[i] >= 10) {
				rs[i + 1] += (rs[i] / 10);
				rs[i] = rs[i] % 10;
			}
		}
		int q = rs[rs.length - 1];
		if(q >= 10) {
			int[] rs2 = new int[rs.length + 1];
			System.arraycopy(rs, 0, rs2, 0, rs.length);
			rs2[rs.length] = q / 10;
			rs2[rs.length - 1] = q % 10;
			rs = rs2;
		}
		return rs;		
	}
	
	public boolean isSymmetric(int[] number) {
		int m = (number.length - 1) / 2;
		for (int i = 0; i <= m; i++) {
			if(number[i] != number[number.length - 1 - i]) return false;
		}
		return true;		
	}
	
	public void print(int[] number) {
		for (int i = 0; i < number.length; i++) {
			System.out.print(" " + number[i]);
		}
		System.out.println(" ");
	}
	
	public int[] getRandom(int length) {
		int[] r = new int[length];
		for (int i = 0; i < length; i++) {
			r[i] = (int)(10 * Math.random());
		}
		if(r[length - 1] == 0) r[length - 1] = (int)(9 * Math.random()) + 1;
		return r;
	}
	
	public static void main(String[] args) {
		AddingSymmetricNumber p = new AddingSymmetricNumber();
		int[] number = p.getRandom(8);
		p.run(number);
	}

}
