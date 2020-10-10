package cmd;

import client.ClientController;

import java.util.Arrays;

public class CommandChangePort implements Command, Local {

    private static final long serialVersionUID = 1337000001L;

    @Override
    public String execute(String[] args){
        if (args != null && args.length != 1) {
            ClientController.connect();
            return null;
        }
        else {
            return ("Wrong arguments");
        }
    }
}
