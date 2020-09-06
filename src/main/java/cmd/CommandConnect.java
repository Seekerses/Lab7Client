package cmd;

import client.ClientController;

import java.io.IOException;

public class CommandConnect implements Command, Local{

    private static final long serialVersionUID = 1337000003L;

    @Override
    public String execute(String[] args) throws IOException {
        if (args == null ||  args.length != 2){
            return("Please invoke this command with 2 arguments (IP, port).");
        }
        else {
            ClientController.setDestIP(args[0]);
            ClientController.setDestPort(Integer.parseInt(args[1]));
            return null;
        }
    }
}
