package com.slava.drops;

import com.slava.common.RectangularField;

public class DropEstimate {

    public static int simpleEstimate(RectangularField f, int[] state) {
        // deficit
        int d = 0;
        int[] hd = new int[f.getHeight()];
        int[] vd = new int[f.getWidth()];
        
        //drop count
        int[] hc = new int[f.getHeight()];
        int[] vc = new int[f.getWidth()];
        
        for (int p = 0; p < state.length; p++) {
            if(state[p] == 0) continue;
            int x = f.getX(p);
            int y = f.getY(p);
            hd[y] += state[p];
            vd[x] += state[p];
            hc[y]++;
            vc[x]++;
            d += state[p];
        }
        System.out.println("d=" + d);
        for (int x = 0; x < vc.length; x++) {
            if(vd[x] == 0) continue;
            int dd = vc[x] < 2 ? 0 : vc[x] * 2 - 3;
            if(dd >= vd[x] - 1) dd = vd[x] - 1;
            d -= dd;
        }
        
        for (int y = 0; y < hc.length; y++) {
            if(hd[y] == 0) continue;
            int dd = hc[y] < 2 ? 0 : hc[y] * 2 - 3;
            if(dd >= hd[y] - 1) dd = hd[y] - 1;
            d -= dd;
        }
        
        return d;
    }
    
    
    
}
