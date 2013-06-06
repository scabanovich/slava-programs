package slava.ui.dialog;

import java.awt.*;

public class UIUtil {

    public static void center(Window w) {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension f = w.getSize();
        w.setLocation((d.width - f.width) / 2, (d.height - f.height) / 2);
    }

}
