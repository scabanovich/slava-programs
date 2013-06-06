package slava.puzzle.crossnumber.analysis.logic;

import java.util.ArrayList;

import slava.puzzle.crossnumber.CrossNumberField;

public class CrossNumberGenerator {
	CrossNumberField field;
	CrossNumberLogicSolver solver;
	
	int changesCount;
	
	public CrossNumberGenerator() {}
	
	public void init(CrossNumberField field) {
		this.field = field;
		solver = new CrossNumberLogicSolver();
		solver.setField(field);
		solver.doNotRebuildRegions = true;
		solver.solve();
	}
	
	public void generate() {
		generateOne();
		int attempt = 0;
		solver.solve();
		while(solver.getResultIndex() != 0 && attempt < 1000) {
			attempt++;
			init(field);
			generateOne();
		}
		System.out.println("Attempts=" + attempt);
	}
	
	public void generateOne() {
		changesCount = 0;
		if(solver.getResultIndex() == 2) return;
		while(true) {
			int p = findAppropriatePlace();
			if(p < 0) break;
			
			Region[] prs = solver.regionsByPoint[p];
			if(prs == null || prs.length == 0) break;
			RegionFiller[] fs = new RegionFiller[prs.length];
			boolean ok = true;
			for (int i = 0; i < prs.length; i++) {
				fs[i] = new RegionFiller();
				fs[i].setLimit = 100000;
				fs[i].generateSumDistFor = getIndex(prs[i], p);
				fs[i].init(prs[i].points.length, solver.values, solver.restrictions);
				fs[i].setRegion(prs[i]);
				fs[i].solve();
				if(fs[i].getFilter() == null) ok = false;
			}
			if(!ok) break;
			
			int[][] variants = findVariants(fs, p);
			if(variants.length == 0) break;
			int c = 0;
			while(!tryIt(variants, prs) && c < 10) c++;
//			System.out.println("-->" + c);
			if(c == 10) break;
		}
	}
	
	boolean tryIt(int[][] variants, Region[] prs) {
		int [] cs = new int[prs.length];
		for (int i = 0; i < prs.length; i++) {
			cs[i] = prs[i].sum;
		}
		int variant = (int)(variants.length * Math.random());
		for (int i = 0; i < variants[variant].length; i++) {
			if(prs[i].sum > 0) continue;
			prs[i].sum = variants[variant][i];//
			changesCount++;
		}
		solver.solve();
		if(solver.getResultIndex() == 2) {
			for (int i = 0; i < prs.length; i++) {
				prs[i].sum = cs[i];
			}
			return false;
		}
		return true;
	}	
	
	int getIndex(Region r, int p) {
		for (int i = 0; i < r.points.length; i++) {
			if(r.points[i] == p) return i;
		}
		return -1;
	}
	
	int[][] findVariants(RegionFiller[] fs, int p) {
		ArrayList l = new ArrayList();
		if(fs.length == 1) {
			int[][] sumDist = fs[0].getSumDistribution();
			for (int sum = 1; sum < 46; sum++) {
				if(getPossibleValueCount(sum, sumDist) == 1) l.add(new int[]{sum});
			}
		} else if(fs.length > 1) {
			// fs.length may be greater than 2
			// when "through" option is on 
			//
			int[][] sumDist1 = fs[0].getSumDistribution();
			int[][] sumDist2 = fs[1].getSumDistribution();
			for (int sum1 = 1; sum1 < 46; sum1++) {
				if(getPossibleValueCount(sum1, sumDist1) == 0) continue;
				for (int sum2 = 1; sum2 < 46; sum2++) {
					if(getPossibleValueCount(sum2, sumDist2) == 0) continue;
					if(getPossibleValueCount(sum1, sumDist1, sum2, sumDist2) == 1) {
						l.add(new int[]{sum1, sum2});
					}
				}
			}			
		} else {
			throw new RuntimeException("Wrong fillers number");
		}
		return (int[][])l.toArray(new int[0][]);
	}
	
	int getPossibleValueCount(int sum, int[][] sumDist) {
		int c = 0;
		for (int v = 0; v < sumDist[sum].length; v++) {
			if(sumDist[sum][v] > 0) ++c;
		}
		return c;
	}
	
	int getPossibleValueCount(int sum1, int[][] sumDist1, int sum2, int[][] sumDist2) {
		int c = 0;
		for (int v = 0; v < sumDist1[sum1].length; v++) {
			if(sumDist1[sum1][v] > 0 && sumDist2[sum2][v] > 0) ++c;
		}
		return c;
	}

	int[] WEIGHTS = {0, 1000, 200, 50, 10, 5, 4, 3, 2, 1, 0, 0};
	
	int findAppropriatePlace() {
		int[] weight = new int[field.size()];
		
		Region[] rs = solver.regions;
		for (int i = 0; i < rs.length; i++) {
			int k = 0;
			if(rs[i].sum > 0) continue;
			int[] ps = rs[i].points;
			for (int j = 0; j < ps.length; j++) {
				if(solver.values[ps[j]] >= 0) ++k;
			}
			for (int j = 0; j < ps.length; j++) {
				if(solver.values[ps[j]] >= 0) weight[ps[j]] += WEIGHTS[k];
			}
		}
		
		int w = -1;
		int pc = -1;
		int q = 0;
		for (int p = 0; p < weight.length; p++) {
			if(weight[p] > w) {
				w = weight[p];
				pc = p;
				q = 1;
			} else if(weight[p] == w) {
				q++;
			}
		}
		if(q > 1 && w > 0) {
			while(true) {
				pc = (int)(weight.length * Math.random());
				if(weight[pc] == w) break;
			}
		}
		return pc;	
	}
	
	public boolean isChanged() {
		return changesCount > 0;
	}
	
	public int[] getNewHSum() {
		int[] hsum = new int[field.getHSum().length];
		for (int i = 0; i < hsum.length; i++) hsum[i] = -2;
		Region[] rs = solver.regions;
		for (int i = 0; i < rs.length; i++) {
			if(rs[i].place >= 0 && rs[i].direction == 0) {
				hsum[rs[i].place] = rs[i].sum; 
			}
		}
		return hsum;
	}

	public int[] getNewVSum() {
		int[] vsum = new int[field.getVSum().length];
		for (int i = 0; i < vsum.length; i++) vsum[i] = -2;
		Region[] rs = solver.regions;
		for (int i = 0; i < rs.length; i++) {
			if(rs[i].place >= 0 && rs[i].direction == 1) {
				vsum[rs[i].place] = rs[i].sum; 
			}
		}
		return vsum;
	}

}
