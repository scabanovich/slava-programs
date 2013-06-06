package smallpuzzles.section;

public class ThreePartSection {
    Field field;

    int originA;
    int originB;
    int transformB;
    int originC;
    int transformC;

    int solutionCount;

    int t;
    int partSize;
    int[][] ways;
    int[] place;
    int[] wayCount;
    int[] way;

  public ThreePartSection() {}

  public void setField(Field field) {
      this.field = field;
  }

  public void solve() {
      solutionCount = 0;
      if(!computePartSize()) return;
      for (int i = 0; i < field.size; i++) {
          if(field.values[i] < 0) continue;
          originA = i;
          break;
      }
      for (int i = 0; shouldContinue() && i < field.size; i++) {
          if(field.values[i] < 0 || i == originA) continue;
          originB = i;
          for (transformB = 0; shouldContinue() && transformB < 8; transformB++) {
              for (int j = originB + 1; shouldContinue() && j < field.size; j++) {
                  if (field.values[j] < 0 || j == originA) continue;
                  originC = j;
                  for (transformC = 0; shouldContinue() && transformC < 8; transformC++) {
                      solveForPlacedOrigins();
                  }
              }
          }
      }
      System.out.println(solutionCount);
  }

  private boolean shouldContinue() {
      return true; // solutionCount == 0;
  }

  boolean computePartSize() {
    partSize = 0;
    for (int i = 0; i < field.size; i++) {
        if(field.values[i] >= 0) ++partSize;
    }
    System.out.println("partSize=" + partSize);
    if(partSize % 3 != 0) return false;
    partSize = partSize / 3;
    wayCount = new int[partSize + 1];
    ways = new int[partSize + 1][3];
    way = new int[partSize + 1];
    place = new int[partSize + 1];
    return true;
  }

  void solveForPlacedOrigins() {
      for (int i = 0; i < field.size; i++) {
          if(field.values[i] > 0) field.values[i] = 0;
      }
      field.values[originA] = 1;
      field.values[originB] = 2;
      field.values[originC] = 3;
      t = 0;
      anal();
  }

  void srch() {
      wayCount[t] = 0;
      int q = 4;
      for (int i = 0; i < field.size; i++) {
          if(field.values[i] != 0) continue;
          boolean aA = isAcceptedPartA(i);
          boolean aB = isAcceptedPartB(i);
          boolean aC = isAcceptedPartC(i);
          int q1 = 0;
          if(aA) ++q1;
          if(aB) ++q1;
          if(aC) ++q1;
          if(q <= q1) continue;
          q = q1;
          place[t] = i;
          if(q1 == 0) {
              return;
          } else if(q1 == 1) {
              ways[t][0] = (aA) ? 1 : (aB) ? 2 : 3;
          } else if(q1 == 2) {
              ways[t][0] = (aA) ? 1 : 2;
              ways[t][1] = (aC) ? 3 : 2;
          } else {
              ways[t][0] = 1;
              ways[t][1] = 2;
              ways[t][2] = 3;
          }
      }
      if(q < 4) wayCount[t] = q;
  }

  void move() {
      int w = ways[t][way[t]];
      int pA, pB, pC;
      if(w == 1) {
          pA = place[t];
          pB = getPartBPoint(pA);
          pC = getPartCPoint(pA);
      } else if(w == 2) {
          pB = place[t];
          pA = getPartAPointByPartB(pB);
          pC = getPartCPoint(pA);
      } else {
          pC = place[t];
          pA = getPartAPointByPartC(pC);
          pB = getPartBPoint(pA);
      }
      field.values[pA] = 1;
      field.values[pB] = 2;
      field.values[pC] = 3;
      ++t;
      srch();
      way[t] = -1;
  }

  void back() {
      --t;
      int w = ways[t][way[t]];
      int pA, pB, pC;
      if(w == 1) {
          pA = place[t];
          pB = getPartBPoint(pA);
          pC = getPartCPoint(pA);
      } else if(w == 2) {
          pB = place[t];
          pA = getPartAPointByPartB(pB);
          pC = getPartCPoint(pA);
      } else {
          pC = place[t];
          pA = getPartAPointByPartC(pC);
          pB = getPartBPoint(pA);
      }
      field.values[pA] = 0;
      field.values[pB] = 0;
      field.values[pC] = 0;
  }

  void anal() {
      srch();
      way[t] = -1;
      while(true) {
          while(way[t] == wayCount[t] - 1) {
              if(t == 0) return;
              back();
          }
          ++way[t];
          move();
          if(t == partSize - 1 && SectionUtil.isConnectedPart(field)) {
              ++solutionCount;
              if(solutionCount == 1) {
                  //rememberSolution();
                  SectionUtil.printSolution(field);
///                    return;
              }
          }
      }
  }

  boolean isAcceptedPartA(int pA) {
      int pB = getPartBPoint(pA);
      if(pB < 0 || pB == pA || field.values[pB] != 0) return false;
      int pC = getPartCPoint(pA);
      if(pC < 0 || pC == pA || field.values[pC] != 0) return false;
      if(pB == pC) return false;
      return true;
  }

  boolean isAcceptedPartB(int pB) {
      int pA = getPartAPointByPartB(pB);
      if(pA < 0 || pA == pB || field.values[pA] != 0) return false;
      int pC = getPartCPoint(pA);
      if(pC < 0 || pC == pA || field.values[pC] != 0) return false;
      if(pB == pC) return false;
      return true;
  }

  boolean isAcceptedPartC(int pC) {
      int pA = getPartAPointByPartC(pC);
      if(pA < 0 || pA == pC || field.values[pA] != 0) return false;
      int pB = getPartBPoint(pA);
      if(pB < 0 || pB == pA || field.values[pB] != 0) return false;
      if(pB == pC) return false;
      return true;
  }

  int getPartBPoint(int p) {
      return SectionUtil.getPoint(p, originA, transformB, originB, field);
  }

  int getPartAPointByPartB(int p) {
      return SectionUtil.getPoint(p, originB, SectionUtil.backTransform[transformB], originA, field);
  }

  int getPartCPoint(int p) {
      return SectionUtil.getPoint(p, originA, transformC, originC, field);
  }

  int getPartAPointByPartC(int p) {
      return SectionUtil.getPoint(p, originC, SectionUtil.backTransform[transformC], originA, field);
  }


}