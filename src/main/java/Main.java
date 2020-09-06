import client.ClientController;
import consolehandler.*;

public class Main {

    public static void main(String[] args) {

        ClientController.connect();
        System.out.print("\n");

        CommandController cmd = new CommandController();
        System.out.println("Enter Command " +
                "or Help to display a list of commands:");
        System.out.print(">");
        cmd.start(new ClientInterpreter());
        cmd.stop();
    }
}
