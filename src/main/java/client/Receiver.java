package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketTimeoutException;
import java.util.Arrays;

class Receiver {

    static byte[] getReply() throws IOException {

        byte[] buf = new byte[1024]; //buffer for coming bytes
        byte[] clear = new byte[1024]; //std buffer for "everything OK"  reply
        byte[] bad = new byte[1024]; //std buffer for "something went wrong" reply
        byte[] done = new byte[1024]; //std buffer for exchanging done reply
        clear[0] = 111; // Ok signal
        bad[0] = 22; // Error signal
        done[0] = 33;
        boolean repeater = false;
        int tries = 10;
        DatagramPacket fromServer = new DatagramPacket(buf, 1024);
        ClientController.getClientSocket().receive(fromServer);
        byte[] result = new byte[0];
        while (true) {
            try {
                ClientController.getClientSocket().receive(fromServer);
            }
            catch (SocketTimeoutException ex){
                tries--;
                if (tries > 0 ){
                    if (repeater){
                        DatagramPacket toServer = new DatagramPacket(clear,
                                1024, ClientController.getDestIP(), ClientController.getDestPort());
                        ClientController.getClientSocket().send(toServer);
                    }
                    else {
                        DatagramPacket toServer = new DatagramPacket(bad,
                                1024, ClientController.getDestIP(), ClientController.getDestPort());
                        ClientController.getClientSocket().send(toServer);
                    }
                }
                else throw ex;
            }

            if (Arrays.equals(fromServer.getData(), done)) {
                break;
            }
            if (PacketUtils.checkHash(fromServer.getData())) {
                DatagramPacket toServer = new DatagramPacket(clear,
                        1024, ClientController.getDestIP(), ClientController.getDestPort());
                ClientController.getClientSocket().send(toServer);
                repeater = true;
                result = PacketUtils.merge(result,Arrays.copyOfRange(fromServer.getData(),0,1012));
            }
            else {
                DatagramPacket toServer = new DatagramPacket(bad,
                        1024, ClientController.getDestIP(), ClientController.getDestPort());
                ClientController.getClientSocket().send(toServer);
                repeater = false;
            }
        }
        return result;
    }
}
