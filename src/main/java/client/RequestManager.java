package client;

import clientserverdata.Reply;
import clientserverdata.Request;
import cmd.Command;
import cmd.Local;
import cmd.Preparable;
import cmd.Registerable;
import consolehandler.CommandController;
import consolehandler.CommandInterpreter;

import java.io.IOException;

public class RequestManager {

    public static void makeRequest(Command command, String[] args){

        Command cmd = null;
        try {
            cmd = command.getClass().newInstance();
        }
        catch (InstantiationException e){
            System.out.println("Command have not nullify constructor.");
        }
        catch (IllegalAccessException e){
            System.out.println("Why do we lost access to cmd ?");
        }
        if (command instanceof Local){
            try {
                String res = command.execute(args);
                if (res != null) {
                    System.out.println(res);
                }
            }
            catch (IOException e){
                System.out.println("Wrong arguments");
            }
            return;
        }
        if (command instanceof Preparable){
            if (cmd != null) {
                ((Preparable) cmd).prepare(args);
            }
        }
        Reply result = ClientController.handleRequest(new Request(cmd, args, UserData.login, Userdata.password));
        if (cmd instanceof Registerable){
            if (!"Approved".equals(result.getAnswer())){
                CommandController.registration(new CommandInterpreter());
            }
        }
        if (result != null) {
            if (result.getAnswer() != null) System.out.println(result.getAnswer());
            if (result.getProducts() != null){
                result.getProducts().forEach((k) -> {
                    if ((k == null)) {
                        System.out.print("");
                    } else {
                        System.out.println(k.toString());
                    }
                });
            }
        }

    }
}
