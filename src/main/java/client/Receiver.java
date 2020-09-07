package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.Arrays;

class Receiver {

    static byte[] getReply() throws IOException {

        byte[] buf = new byte[1024]; //buffer for coming bytes
        byte[] clear = new byte[1024]; //std buffer for "everything OK" and exchanging done reply
        byte[] bad = new byte[1024]; //std buffer for "something went wrong" reply
        clear[0] = 111; // Ok signal
        bad[0] = 22; // Error signal

        byte[] result = new byte[0];
        while (true) {
            DatagramPacket fromServer = new DatagramPacket(buf, 1024);
            ClientController.getClientSocket().receive(fromServer);

            if (Arrays.equals(fromServer.getData(), clear)) {
                break;
            }

            if (PacketUtils.checkHash(fromServer.getData())) {
                DatagramPacket toServer = new DatagramPacket(clear,
                        1024, fromServer.getAddress(), fromServer.getPort());
                ClientController.getClientSocket().send(toServer);
                result = PacketUtils.merge(result,Arrays.copyOfRange(fromServer.getData(),0,1012));
            }
            else {
                DatagramPacket toServer = new DatagramPacket(bad,
                        1024, fromServer.getAddress(), fromServer.getPort());
                ClientController.getClientSocket().send(toServer);
            }

        }
        System.out.println(Arrays.toString(result));
        return result;
    }
}
