package smallpuzzles.shoppingroute;

public class Util {
    public static char getPresentation(int i) {
        return (char)(i + 97);
    }

    public static String getPresentation(Node n) {
        return "" + getPresentation(n.getId());
    }

    public static String getPresentation(Transition t) {
        return "" + getPresentation(t.getSource().getId()) + "" + getPresentation(t.getTarget().getId());
    }

}