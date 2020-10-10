package client;

import clientserverdata.Reply;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;

class Sender {

    static void send(byte[] data) throws IOException{


        byte[] done; //std buffer for exchanging done reply
        done = new byte[1024];
        done[0] = 33;
        boolean last = false;

        DatagramPacket handle = new DatagramPacket(new byte[1024],1024);
        DatagramPacket commandPacket;

        ClientController.getClientSocket().receive(handle);
        Reply newAdr = Serializer.deserialize(handle.getData());
        ClientController.setTempPort(ClientController.getDestPort());
        ClientController.setDestPort(Integer.parseInt(newAdr.getAnswer().split(":")[1]));

        while(!last) {
            if (data.length > 1024) {
                commandPacket = new DatagramPacket(Arrays.copyOfRange(data, 0, 1024),
                        1024, ClientController.getDestIP(), ClientController.getDestPort());
            }
            else {
                commandPacket = new DatagramPacket(Arrays.copyOf(data,1024),
                        1024, ClientController.getDestIP(), ClientController.getDestPort());
                last = true;
            }
                ClientController.getClientSocket().send(commandPacket);

            handle = new DatagramPacket(new byte[1024],1024);
            ClientController.getClientSocket().receive(handle);
            if ( handle.getData()[0] == 111 ){
                if ( data.length > 1024 ) {
                    data = Arrays.copyOfRange(data, 1024, data.length);
                }
            }
            else {
                last = false;
            }
        }

        commandPacket = new DatagramPacket(done,1024, ClientController.getDestIP(), ClientController.getDestPort());
        ClientController.getClientSocket().send(commandPacket);

        handle = new DatagramPacket(new byte[1024],1024);
        ClientController.getClientSocket().receive(handle);

        ClientController.setDestPort(ClientController.getTempPort());
    }
}
