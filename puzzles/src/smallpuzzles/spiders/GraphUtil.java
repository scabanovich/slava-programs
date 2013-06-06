package smallpuzzles.spiders;

public class GraphUtil {

    //ignore unbound vertices
    public static boolean isConnectedGraph(int[][] graph) {
        int[] vertices = new int[graph.length + 1];
        int[] occupied = new int[graph.length + 1];
        for (int i = 0; i < occupied.length; i++) occupied[i] = 0;
        int current = getFirstBoundVertix(graph);
        if(current < 0) return true;
        int total = 1;
        int index = 0;
        vertices[index] = current;
        occupied[current] = 1;
        while(index < total) {
            current = vertices[index];
            for (int i = 0; i < graph.length; i++) {
                if(graph[current][i] == 0 || occupied[i] == 1) continue;
                occupied[i] = 1;
                vertices[total] = i;
                ++total;
            }
            index++;
        }
        return total == getBoundVertixCount(graph);
    }

    public static int getBindCount(int[][] graph, int vertix) {
        int c = 0;
        for (int i = 0; i < graph.length; i++)
          if(graph[vertix][i] == 1) ++c;
        return c;
    }

    public static int getFirstBoundVertix(int[][] graph) {
        for (int i = 0; i < graph.length; i++)
          if(getBindCount(graph, i) > 0) return i;
        return -1;
    }

    public static int getBoundVertixCount(int[][] graph) {
        int c = 0;
        for (int i = 0; i < graph.length; i++)
          if(getBindCount(graph, i) > 0) ++c;
        return c;
    }

    public static void main(String[] args) {
        int[][] graph = new int[][]{
            {0, 1, 0, 0, 0},
            {1, 0, 0, 0, 1},
            {0, 0, 0, 0, 1},
            {0, 0, 0, 0, 0},
            {0, 1, 1, 0, 0}
        };
        System.out.println(isConnectedGraph(graph));
    }
}