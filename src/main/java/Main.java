import client.ClientController;
import consolehandler.*;

public class Main {

    public static void main(String[] args) {

        ClientController.connect();
        System.out.print("\n");

        CommandController cmd = new CommandController();
        cmd.start(new ClientInterpreter());
        cmd.stop();
    }
}
