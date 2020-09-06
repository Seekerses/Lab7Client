package cmd;
import consolehandler.TableController;
import productdata.Product;

/**
 * get element with the shortest name
 *
 *
 */

public class Commandmin_by_name implements Command {

    private static final long serialVersionUID = 1337000011L;

    @Override
    public String execute(String[] args) {
        try {
            if (args.length == 1) {
                return("There is no args for this command!");
            }
        }catch (NullPointerException e) {
            String min = "";
            int i = 1;
            Product p = null;
            for (Product prod : TableController.getCurrentTable().getProducts()) {
                if (i == 1) {
                    min = prod.getName();
                }
                if (i == 0) {
                    if (min.compareTo(prod.getName()) <= 0) {
                        min = prod.getName();
                        p = prod;
                    }
                }
                i = 0;
            }
            if (p != null) {
                return (p.toString());
            } else {
                return ("Empty table");
            }
        }
        return null;
    }

    /**
     * get name of command
     *
     * @return String
     */

    public String toString() {
        return "min_by_name";
    }
}
