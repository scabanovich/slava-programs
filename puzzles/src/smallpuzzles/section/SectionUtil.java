package smallpuzzles.section;

public class SectionUtil {

  public static final int[] backTransform = new int[]{0, 1, 6, 3, 4, 5, 2, 7};

  public static int getPoint(int p, int origin, int tm, int origin2, Field field) {
      int dx1 = field.x[p] - field.x[origin];
      int dy1 = field.y[p] - field.y[origin];
      int dx2 = dx1, dy2 = dy1;
      switch (tm) {
          case 0: dx2 = dx1;  dy2 = dy1;  break;
          case 1: dx2 = dx1;  dy2 = -dy1; break;
          case 2: dx2 = -dy1; dy2 = dx1;  break;
          case 3: dx2 = -dy1; dy2 = -dx1; break;
          case 4: dx2 = -dx1; dy2 = -dy1; break;
          case 5: dx2 = -dx1; dy2 = dy1;  break;
          case 6: dx2 = dy1;  dy2 = -dx1; break;
          case 7: dx2 = dy1;  dy2 = dx1;  break;
          default:
      }
      int x2 = field.x[origin2] + dx2;
      int y2 = field.y[origin2] + dy2;
      return field.point(x2, y2);
  }

  public static void printSolution(Field field) {
      for (int i = 0; i < field.size; i++) {
          int v = field.values[i];
          char c = (v < 0) ? '-' : (v == 0) ? '?' : (v == 1) ? 'a' : (v == 2) ? 'b' : (v == 3) ? 'c' : 'x';
          System.out.print("" + c + ' ');
          if(field.x[i] == field.width - 1) System.out.println("");
      }
      System.out.println("");
  }

  public static boolean isConnectedPart(Field field) {
      int pA = -1;
      for (int i = 0; i < field.size; i++) {
          if(field.values[i] != 1) continue;
          pA = i;
          break;
      }
      int[] stack = new int[field.size];
      int v = 1;
      int c = 0;
      stack[0] = pA;
      field.values[pA] = 100;
      while(c < v) {
          pA = stack[c];
          for (int d = 0; d < 4; d++) {
              int pB = field.jp[d][pA];
              if(pB < 0 || field.values[pB] != 1) continue;
              stack[v] = pB;
              field.values[pB] = 100;
              ++v;
          }
          ++c;
      }
      boolean result = true;
      for (int i = 0; i < field.size; i++) {
          if(field.values[i] == 100) field.values[i] = 1;
          else if(field.values[i] == 1) result = false;
      }
      return result;
  }


}