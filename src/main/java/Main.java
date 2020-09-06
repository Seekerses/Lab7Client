import Client.ClientController;
import Control.*;

import java.io.*;
import java.util.Hashtable;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
//        TableManager prodTable = new TableManager("products");
//        TableController.setCurrentTable(prodTable);
//
//        try {
//            if( new File("saved.csv").createNewFile()){
//                System.out.println("Save file created.");
//            }
//        }
//        catch (Exception e ){
//            System.out.println("Could not create default save file, please specify it manually\n");
//        }
//
//        if(args.length != 0) {
//            Initializer.init(prodTable, new File(args[0]));
//        }
//        else {
//            try {
//                Initializer.init(prodTable, new File("saved.csv").exists() ? new File("saved.csv") : null);
//            } catch (NullPointerException e) {
//                Initializer.init(prodTable, null);
//            }
//        }

        ClientController.connect(1339);
        System.out.print("\n");

        CommandController cmd = new CommandController();
        System.out.println("Enter Command " +
                "or Help to display a list of commands:");
        System.out.print(">");
        cmd.start(new ClientInterpreter());
        cmd.stop();
    }
}
