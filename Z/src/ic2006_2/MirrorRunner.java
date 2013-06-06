package ic2006_2;

public class MirrorRunner {
	
	static int[] H_RESTRICTIONS = {
		-1,-1,-1,-1,-1,2,3,-1,-1,-1
//		-1,-1,-1,-1,-1,-1,-1,-1,-1,-1
	};

	static int[] V_RESTRICTIONS = {
		1,-1,-1,3,2,-1,2,-1,4,-1
//		-1,-1,-1,3,2,-1,2,-1,-1,-1
	};

	public static void main(String[] args) {
		MirrorQuorterField f = new MirrorQuorterField();
		f.setSize(5);
		MirrorQuarterEnumerator enumerator = new MirrorQuarterEnumerator();
		enumerator.setField(f);
		MirrorSeaBattle listener = new MirrorSeaBattle();
		listener.setHVRestrictions(H_RESTRICTIONS, V_RESTRICTIONS);
		enumerator.setListener(listener);
		enumerator.solve();
		System.out.println(enumerator.stateCount);
	}

}
