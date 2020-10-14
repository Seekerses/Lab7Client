package client;

import clientserverdata.Reply;
import consolehandler.TableController;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;

public class UpdateController implements Runnable {

    private DatagramSocket updaterSocket;

    public  UpdateController() throws SocketException {
        updaterSocket = new DatagramSocket(ClientController.getPort()+1);
        updaterSocket.setSoTimeout(0);
    }
    @Override
    public void run() {

        try {

            while(true) {

                DatagramPacket fromServer = new DatagramPacket(new byte[1024], 1024);

                updaterSocket.receive(fromServer);
                System.out.println(Arrays.toString(fromServer.getData()));

                Receiver receiver = new Receiver();
                Reply update = Serializer.deserialize(receiver.getReply(updaterSocket));

                TableController.getCurrentTable().setTable(update.getProducts());
                System.out.println("Table updated");
            }
        } catch (SocketException e) {
            System.out.println("Troubles with updater.");
            run();
        } catch (IOException e) {
            e.printStackTrace();
            run();
        }
        catch (NullPointerException e){
            System.out.println("Restarting updater");
            run();
        }
    }

}
