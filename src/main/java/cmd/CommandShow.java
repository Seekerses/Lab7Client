package cmd;
import consolehandler.TableController;

/**
 * show all elements in String format
 *
 *
 */

public class CommandShow implements Command {

    private static final long serialVersionUID = 1337000015L;

    @Override
    public String execute(String[] args) {
        try {
            if (args.length == 1) {
                return ("There is no args for this command!");
            }
        }catch (NullPointerException e) {
            if (TableController.getCurrentTable().getSize() == 0) {
                return ("Collection is empty!");
            } else {
                return TableController.getCurrentTable().show();
            }
        }
        return null;
    }

    /**
     * get name of command
     *
     * @return String
     */

    public String toString(){
        return "show";
    }
}
