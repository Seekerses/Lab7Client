package cmd;

import Client.ClientController;

public class CommandChangePort implements Command, Local {

    private static final long serialVersionUID = 1337000001L;

    @Override
    public String execute(String[] args){
        if (args == null || args.length != 1) {
            ClientController.connect(Integer.parseInt(args[0]));
            return null;
        }
        else {
            return ("Wrong arguments");
        }
    }
}
