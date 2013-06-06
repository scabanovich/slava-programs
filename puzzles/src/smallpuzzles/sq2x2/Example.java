package smallpuzzles.sq2x2;

public class Example {
	
	public static void main(String[] args) {
        int i;
        int s = 0;
        for (i = 0; i < 10; i++) {
        	s = s + i * i;
        }
		System.out.println(s);
		int[] t = new int[10];
		t[0] = 0;
		for (i = 1; i < 10; i++) {
			t[i] = t[i - 1] + i * i;
//			System.out.println("i=" + i + " t[i]=" + t[i]);
		}
		int c = 0;
		for (i = 1; i < 100000; i++) {
			double d = Math.random();
			int i1 = (int) (12 * d);
			if(i1 == 1) {
				c = c + 1;
			} 
		}
		System.out.println(c);
	}
	
	public static int method() {
		
		return 1;		
	}
}
