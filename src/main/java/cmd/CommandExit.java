package cmd;

import Client.ClientController;
import Client.Sender;

import java.io.IOException;

/**
 * break the programm
 *
 *
 */

public class CommandExit implements Command, Local{

    @Override
    public String execute(String[] args) throws IOException {
        try {
            if (args.length == 1) {
                System.out.println("There is no args for this command!");
            }
        }catch (NullPointerException e) {
            ClientController.getClientSocket().close();
            System.out.println("Program completion...");
            System.exit(0);
        }
        return null;
    }

    /**
     * get name of command
     *
     * @return String
     */

    @Override
    public String toString() {
        return "exit";
    }
}
