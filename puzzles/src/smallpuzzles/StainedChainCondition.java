package smallpuzzles;

public class StainedChainCondition {
    int[] stainsOnChain = new int[]{ 0, 1, 15, 24};
    int[] stainsInField = new int[]{ 0, 5, 10, 15};

    int[] field;
    int[] chain;

    public StainedChainCondition() {}

    public void setSize(int size) {
        chain = new int[size];
        for (int i = 0; i < size; i++) chain[i] = 0;
        for (int i = 0; i < stainsOnChain.length; i++) {
            chain[stainsOnChain[i]] = 1;
        }
        field = new int[size];
        for (int i = 0; i < size; i++) field[i] = 0;
        for (int i = 0; i < stainsInField.length; i++) {
            field[stainsInField[i]] = 1;
        }
    }

    public boolean accept(int t, int p) {
        return (chain[t] == field[p]);
    }

}
