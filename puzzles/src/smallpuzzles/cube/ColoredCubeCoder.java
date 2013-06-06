package smallpuzzles.cube;

public class ColoredCubeCoder {
    int faceCount = 6;
    //0 - tob, 1 - bottom, 2 - face, 3 - back, 4 - left, 5 - right
    int[][] corners = new int[][]{
        {0, 2, 4}, {0, 4, 3}, {0, 3, 5}, {0, 5, 2},
        {1, 4, 2}, {1, 3, 4}, {1, 5, 3}, {1, 2, 5}
    };

    int[][] cornerStates = new int[40][];
    int[][] cubeStates = new int[30][8];
    int[][] cubeColors = new int[30][6];

    public ColoredCubeCoder() {
        fillCornerStates();
        fillCubeStates();
    }

    private void fillCornerStates() {
        int q = 0;
        for (int i1 = 0; i1 < faceCount; i1++) {
          for (int i2 = i1 + 1; i2 < faceCount; i2++) {
            for (int i3 = i1 + 1; i3 < faceCount; i3++) {
                if(i2 == i3) continue;
                cornerStates[q] = new int[]{i1, i2, i3};
                ++q;
            }
          }
        }
    }

    private void fillCubeStates() {
        int[] faces = new int[faceCount];
        int[] colors = new int[faceCount];
        for (int c = 0; c < faceCount; c++) colors[c] = 0;
        faces[0] = 0;
        colors[faces[0]] = 1;
        int count = 0;
        for (int c1 = 1; c1 < faceCount; c1++) {
            faces[1] = c1;
            colors[faces[1]] = 1;
            faces[2] = (c1 == 1) ? 2 : 1;
            colors[faces[2]] = 1;
            for (int c3 = 1; c3 < faceCount; c3++) {
                if(colors[c3] != 0) continue;
                faces[3] = c3;
                colors[faces[3]] = 1;
                for (int c4 = 1; c4 < faceCount; c4++) {
                    if(colors[c4] != 0) continue;
                    faces[4] = c4;
                    colors[faces[4]] = 1;
                    for (int c5 = 1; c5 < faceCount; c5++) {
                        if (colors[c5] != 0) continue;
                        faces[5] = c5;
                        makeCubeState(count, faces);
                        for (int b = 0; b < faceCount; b++) cubeColors[count][b] = faces[b];
                        ++count;
                    }
                    colors[faces[4]] = 0;
                }
                colors[faces[3]] = 0;
            }
            colors[faces[2]] = 0;
            colors[faces[1]] = 0;
        }
    }

    private void makeCubeState(int count, int[] faces) {
        int[] colors = new int[3];
        for (int c = 0; c < corners.length; c++) {
            for (int i = 0; i < 3; i++) colors[i] = faces[corners[c][i]];
            cubeStates[count][c] = findCornerIndex(colors);
        }
    }

    private int findCornerIndex(int[] colors) {
        normalizeCorner(colors);
        for (int i = 0; i < cornerStates.length; i++)
            if(areEqualArrays(cornerStates[i], colors)) return i;
        return -1;
    }

    private int findCubeIndex(int[] colors) {
      for (int i = 0; i < cornerStates.length; i++)
          if(areEqualArrays(cubeColors[i], colors)) return i;
      return -1;
    }

    private void normalizeCorner(int[] colors) {
        if(colors[0] < colors[1] && colors[0] < colors[2]) return;
        if(colors[1] < colors[0] && colors[1] < colors[2]) {
            int q = colors[0];
            colors[0] = colors[1];
            colors[1] = colors[2];
            colors[2] = q;
        } else if(colors[2] < colors[0] && colors[2] < colors[1]) {
            int q = colors[2];
            colors[2] = colors[1];
            colors[1] = colors[0];
            colors[0] = q;
        }
    }

    private boolean areEqualArrays(int[] a1, int[] a2) {
        for (int i = 0; i < a1.length; i++) if(a1[i] != a2[i]) return false;
        return true;
    }

    public int packCubes(SetsSifter sifter, int[][] eightCubes) {
        int solutionCount = 0;
        int[] solution = null;
        for (int i = 0; i < cubeStates.length; i++) {
            sifter.setData(cubeStates[i], eightCubes);
            solutionCount += sifter.getSolutionCount();
            if(solutionCount == 1) solution = sifter.getSolution();
        }
        if(solutionCount % 2 == 1) {
          System.out.println("solutionCount=" + solutionCount);
          if (solution != null)
            System.out.println("solution available");
        }
        return solutionCount;
    }

    public int packCubesVerbose(SetsSifter sifter, int[][] eightCubes) {
        int solutionCount = 0;
        int[] solution = null;
        for (int i = 0; i < cubeStates.length; i++) {
            sifter.setData(cubeStates[i], eightCubes);
            int sc = sifter.getSolutionCount();
            solutionCount += sc;
            if(sc > 0) {
                System.out.print("Result cube: ");
                for (int b = 0; b < faceCount; b++) System.out.print(" " + cubeColors[i][b]);
                System.out.println("  Pack count=" + sc);
                solution = sifter.getSolution();
                System.out.print("  First pack: ");
                for (int b = 0; b < 8; b++) System.out.print(" " + solution[b]);
                System.out.println("");
            }
        }
        return solutionCount;
    }


    public static void main(String[] args) {
      solveCubeSet();
    }

    private static void solveCubeSet() {
        SetsSifter sifter = new SetsSifter();
        ColoredCubeCoder c = new ColoredCubeCoder();
        int[][] eightCubeColors = new int[][]{
            {0, 4, 1, 3, 2, 5},
            {0, 3, 1, 2, 4, 5},
            {0, 1, 2, 4, 3, 5},
            {0, 2, 1, 3, 4, 5},
            {0, 4, 1, 5, 3, 2},
            {0, 2, 1, 4, 3, 5},
            {0, 1, 2, 3, 4, 5},
            {0, 3, 1, 4, 2, 5}
        };
        int[][] eightCubes = new int[8][8];
        for (int i = 0; i < 8; i++) {
            int k = c.findCubeIndex(eightCubeColors[i]);
            eightCubes[i] = c.cubeStates[k];
        }
        c.packCubesVerbose(sifter, eightCubes);
    }

    private static void analyzeEverything() {
        SetsSifter sifter = new SetsSifter();
        ColoredCubeCoder c = new ColoredCubeCoder();
        int[][] eightCubes = new int[8][8];
        //int[] set = new int[]{2, 5, 12, 17, 22, 24, 27, 28};
        long v = 0;
        long v1 = 30 * 29 / 2 * 28 / 3 * 27 / 4 * 26 / 5 * 25 / 6 * 24 / 7 * 23 / 8;
        System.out.println(v1);
        int[] distribution = new int[5];
        for (int i1 = 0; i1 < 30; i1++) {
          for (int i2 = i1 + 1; i2 < 30; i2++) {
            for (int i3 = i2 + 1; i3 < 30; i3++) {
              for (int i4 = i3 + 1; i4 < 30; i4++) {
                for (int i5 = i4 + 1; i5 < 30; i5++) {
                  for (int i6 = i5 + 1; i6 < 30; i6++) {
                    for (int i7 = i6 + 1; i7 < 30; i7++) {
                      for (int i8 = i7 + 1; i8 < 30; i8++) {
                          eightCubes[0] = c.cubeStates[i1];
                          eightCubes[1] = c.cubeStates[i2];
                          eightCubes[2] = c.cubeStates[i3];
                          eightCubes[3] = c.cubeStates[i4];
                          eightCubes[4] = c.cubeStates[i5];
                          eightCubes[5] = c.cubeStates[i6];
                          eightCubes[6] = c.cubeStates[i7];
                          eightCubes[7] = c.cubeStates[i8];
                          int solutionCount = c.packCubes(sifter, eightCubes);
                          if(solutionCount < 5) ++distribution[solutionCount];
                          ++v;
                          if(v % 10000 == 9999) {
                            System.out.println(":" + distribution[0] + ":" + distribution[1] + ":" + distribution[2] + ":" + distribution[4] + ":");
                          }
                      }
                    }
                  }
                }
              }
            }
          }
        }
    }

}
