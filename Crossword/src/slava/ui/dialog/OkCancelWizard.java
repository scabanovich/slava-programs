package slava.ui.dialog;

public abstract class OkCancelWizard extends AbstractQueryWizard {

    public void action(String command) {
        if("OK".equals(command)) {
            returnType = 0;
            ok();
            dispose();
        } else if("Cancel".equals(command)) {
            cancel();
            dispose();
        }
    }

    protected boolean ok() {
        return true;
    }

    protected boolean cancel() {
        return true;
    }

}
