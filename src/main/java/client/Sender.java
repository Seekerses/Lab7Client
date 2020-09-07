package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.Arrays;

class Sender {

    static void send(byte[] data) throws IOException{
        DatagramPacket commandPacket;
        DatagramPacket handle;
        byte[] done = new byte[1024]; //std buffer for exchanging done reply
        done[0] = 33;
        boolean last = false;

        while(!last) {
            if (data.length > 1012) {
                commandPacket = new DatagramPacket(PacketUtils.formatData(Arrays.copyOfRange(data, 0, 1012)),
                        1024, ClientController.getDestIP(), ClientController.getDestPort());
            }
            else {
                commandPacket = new DatagramPacket(PacketUtils.formatData(Arrays.copyOf(data,1012)),
                        1024, ClientController.getDestIP(), ClientController.getDestPort());
                last = true;
            }
            ClientController.getClientSocket().send(commandPacket);

            handle = new DatagramPacket(new byte[1024],1024);
            ClientController.getClientSocket().receive(handle);

            if ( handle.getData()[0] == 111 ){
                if ( data.length > 1012 ) {
                    data = Arrays.copyOfRange(data, 1012, data.length);
                }
            }
            else {
                last = false;
            }
        }

        commandPacket = new DatagramPacket(done,1024, ClientController.getDestIP(), ClientController.getDestPort());
        ClientController.getClientSocket().send(commandPacket);
    }
}
