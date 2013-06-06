package champukr;

import java.util.HashMap;
import java.util.Map;

public class Tomatoes2 {
	Map map = new HashMap();
	
	double[] sums = new double[1000];
	
	String getCode(int index, int[] queue) {
		StringBuffer sb = new StringBuffer();
		sb.append(index).append(":");
		for (int i =  0; i < queue.length; i++) {
			sb.append(' ').append(queue[i]);
		}
		return sb.toString();
	}
	
	public double getProbability(int index, int[] queue) {
		String code = getCode(index, queue);
		Double d = (Double)map.get(code);
		if(d != null) return d.doubleValue();
		if(queue[index] >= queue.length) {
			d = new Double(1d);
		} else if(queue[index] == 1) {
			d = new Double(1d / queue.length);
		} else {
			double sum = 0d;
			for (int i = 0; i < queue.length; i++) {
				if(i == index) {
					sum += 1d;
				} else {
					int index1 = 0;
					int[] q1 = new int[queue.length];
					int length = 0;
					for (int j = 0; j < queue.length; j++) {
						if(j == i) continue;
						q1[length] = queue[j] - 1;
						if(q1[length] <= 0) continue;
						if(j == index) {
							index1 = length;
						}
						length++;
					}
					int[] queue1 = new int[length];
					System.arraycopy(q1, 0, queue1, 0, length);
					for (int k = 0; k < queue1.length; k++) {
						if(queue1[k] > queue1.length) queue1[k] = queue1.length;
					}
					sum += getProbability(index1, queue1);
				}
			}
			d = new Double(sum / queue.length);
		}
		map.put(code, d);
		return d.doubleValue();
	}
	
	public void compute(int n) {
		long f = 1;
		for (int i = 2; i <= n; i++) f *= i;
		long sum = 0;
		double dsum = 0d;
		int[] queue = new int[n];
		for (int i = 0; i < n; i++) queue[i] = i + 1;
		for (int i = 0; i < n; i++) {
			double di = getProbability(i, queue);
			dsum += di;
			long li = Math.round(di * f);
			sum += li;
//			System.out.println(i + " " + li);
//			if(Math.abs(di * f - li) >= 0.01d) {
//				System.out.println("big error");
//			}
		}
		sums[n] = dsum;
//		System.out.print("M(" + n + ")*" + n + "!=" + sum + "   ");
//		System.out.println("M(" + n + ")/" + n + "=" + (sum * 1d / f) / n);
		System.out.println("M(" + n + ")=" + dsum);
		if(n > 5) {
			double delta = sums[n] - sums[n - 1];
			System.out.println("delta=" + delta);
		}
	
	}


	public static void main(String[] args) {
		Tomatoes2 p = new Tomatoes2();
		for (int n = 5; n < 18; n++) p.compute(n);

	}

}
