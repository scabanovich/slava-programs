package com.slava.drops;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.slava.common.RectangularField;

public class DropState {
    DropState parent;
    int move;
    int[] state;
    
    public DropState(DropState parent, int[] state, int move) {
        this.parent = parent;
        this.state = state;
        this.move = move;
    }
    
    public int getVolume() {
        int c = 0;
        for (int p = 0; p < state.length; p++) c += state[p];
        return c;
    }
    
    public DropState hit(RectangularField f, int p) {
        if(state[p] == 0) return null;
        int[] _state = (int[])state.clone();
        Set bullets = new HashSet();
        _state[p] = 0;
        for (int d = 0; d < 4; d++) bullets.add(new int[]{p, d});
        Set places = new HashSet();
        while(bullets.size() > 0) {
            places.clear();
            Iterator it = bullets.iterator();
            while(it.hasNext()) {
                int[] b = (int[])it.next();
                if(_state[b[0]] > 0) {
                    it.remove();
                    places.add(new Integer(b[0]));
                } else {
                    b[0] = f.jump(b[0], b[1]);
                    if(b[0] < 0) {
                        it.remove();
                    }
                }
            }
            Iterator it1 = places.iterator();
            while(it1.hasNext()) {
                int q = ((Integer)it1.next()).intValue();
                _state[q]--;
                if(_state[q] == 0) {
                    for (int d = 0; d < 4; d++) bullets.add(new int[]{q, d});
                }
                
            }
        }
        
        return new DropState(this, _state, p);
    }
    
    public void printState(RectangularField f) {
        for (int p = 0; p < state.length; p++) {
            System.out.print(" " + state[p]);
            if(f.isRightBorder(p)) System.out.println("");
        }
        System.out.println("");
        System.out.println("Deficit = " + DropEstimate.simpleEstimate(f, state));
    }
    
    public String getSolution() {
        StringBuffer sb = new StringBuffer();
        DropState s = this;
        while(s.parent != null) {
            sb.insert(0, s.move + " ");
            s = s.parent;
        }
        return sb.toString();
    }
    
}
