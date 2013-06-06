package forsmarts;

public class RoundAndRoundLoopFilter {
	int[] blackPoints;
	int[][] lineRestrictions;
	int blackPointsCount;
	
	public RoundAndRoundLoopFilter(int[] blackPoints, int[][] lineRestrictions) {
		this.blackPoints = blackPoints;
		this.lineRestrictions = lineRestrictions;
		blackPointsCount = 0;
		for (int i = 0 ; i < blackPoints.length; i++) {
			if(blackPoints[i] == 1) blackPointsCount++;
		}
	}
	
	public boolean check(RoundAndRoundLoop loop) {
		for (int d = 0; d < 3; d++) {
			for (int i = 0; i < lineRestrictions[d].length; i++) {
				int lc = lineRestrictions[d][i];
				if(lc >= 0 && loop.lines[d][i] > lc) return false;
			}
		}
		int b = 0;
		for (int t = 0; t < blackPoints.length; t++) {
			if(blackPoints[t] > 0 && loop.triangleState[t] > 0) ++b;
		}
		loop.blackPointCount = b;
			if(loop.blackPointCount > 3) return false;
		
		
		return b > 0 && loop.blackPointCount + loop.cornerCount <= blackPointsCount;
	}

}
