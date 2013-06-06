package smallpuzzles.shoppingroute;

import java.io.*;
import java.util.*;

public class GraphLoader {

    public GraphLoader() {
    }

    public Node[] load() {
        try {
            InputStream is = GraphLoader.class.getClassLoader().getResourceAsStream(
                "smallpuzzles/shoppingroute/GraphData.properties");
            Properties p = new Properties();
            p.load(is);
            return load(p);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Graph was not loaded.");
        }
    }

    private Node[] load(Properties p) throws Exception {
        int size = Integer.parseInt(p.getProperty("size"));
        Node[] ns = new Node[size];
        for (int i = 0; i < size; i++) ns[i] = new Node(i);
        String transitions = p.getProperty("transitions");
        StringTokenizer st = new StringTokenizer(transitions, ",; ");
        Set tset = new HashSet();
        while(st.hasMoreTokens()) {
            String t = st.nextToken();
            if(t.length() != 2) throw new Exception("Wrong transition description:" + t);
            if(t.charAt(0) > t.charAt(1)) t = "" + t.charAt(1) + "" + t.charAt(0);
            tset.add(t);
        }
        String[] tarr = (String[])tset.toArray(new String[0]);
        for (int i = 0; i < tarr.length; i++) {
            int src = getSource(tarr[i], 0, ns.length);
            int trg = getSource(tarr[i], 1, ns.length);
            new Transition(ns[src], ns[trg]);
            new Transition(ns[trg], ns[src]);
        }
        loadObjects(ns, p.getProperty("parkings"), 1);
        loadObjects(ns, p.getProperty("markets"), 2);
        loadRestrictions(ns, p.getProperty("restrictions"));
        return ns;
    }

    void loadObjects(Node[] ns, String s, int kind) throws Exception {
        StringTokenizer st = new StringTokenizer(s, ",; ");
        while(st.hasMoreTokens()) {
            String t = st.nextToken();
            if(t.length() != 1 && t.length() != 2) throw new Exception("Wrong node or transition description:" + t);
            if(t.length() == 1) {
                int src = getSource(t, 0, ns.length);
                ns[src].setKind(kind);
            } else if(t.length() == 2) {
                int src = getSource(t, 0, ns.length);
                int trg = getSource(t, 1, ns.length);
                Transition tr = ns[src].findTransition(trg);
                if(tr == null) throw new Exception("Transition " + t + " does not exist.");
                tr.setKind(kind);
                ns[trg].findTransition(src).setKind(kind);
            }
        }
    }

    private void loadRestrictions(Node[] ns, String s) throws Exception {
      if(s == null) return;
      StringTokenizer st = new StringTokenizer(s, ",; ");
      while(st.hasMoreTokens()) {
          String t = st.nextToken();
          if(t.length() != 2) throw new Exception("Wrong restriction description:" + t);
          int src = getSource(t, 0, ns.length);
          int trg = getSource(t, 1, ns.length);
          Transition tr = ns[trg].findTransition(src);
          if(tr == null) throw new Exception("Transition " + t + " does not exist.");
          tr.setEnabled(false);
          tr.setEnablementReadOnly(true);
      }
    }

    int getSource(String s, int index, int size) throws Exception {
        char ch = s.charAt(index);
        int src = ((int)s.charAt(index)) - 97;
        if(src < 0 || src >= size) {
          String msg = "Source index " + ch + " is out of range.";
          throw new Exception(msg);
        }
        return src;
    }

    public static void main(String[] args) {
        GraphLoader g = new GraphLoader();
        g.load();
    }

}
