package client;

import clientserverdata.Reply;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.Arrays;

class Receiver {

    static byte[] getReply() throws IOException {

        byte[] buf = new byte[1024]; //buffer for coming bytes
        byte[] clear = new byte[1024]; //std buffer for "everything OK"  reply
        byte[] done = new byte[1024]; //std buffer for exchanging done reply
        clear[0] = 111; // Ok signal
        done[0] = 33;

        DatagramPacket fromServer = new DatagramPacket(buf, 1024);

        ClientController.getClientSocket().receive(fromServer);
        Reply newAdr = Serializer.deserialize(fromServer.getData());
        System.out.println(newAdr.getAnswer());
        ClientController.setTempPort(ClientController.getDestPort());
        ClientController.setDestPort(Integer.parseInt(newAdr.getAnswer().split(":")[1]));

        DatagramPacket toServer = new DatagramPacket(clear,
                1024, ClientController.getDestIP(), ClientController.getDestPort());


        byte[] result = new byte[0];
        while (true) {

            ClientController.getClientSocket().receive(fromServer);
            if (Arrays.equals(fromServer.getData(), done)) {
                ClientController.getClientSocket().send(toServer);
                break;
            }
                ClientController.getClientSocket().send(toServer);
                result = PacketUtils.merge(result,fromServer.getData());
        }
        ClientController.setDestPort(ClientController.getTempPort());
        return result;
    }
}
