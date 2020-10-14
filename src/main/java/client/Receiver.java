package client;

import clientserverdata.Reply;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

class Receiver {

    byte[] getReply(DatagramSocket socket) throws IOException {

        byte[] buf = new byte[1024]; //buffer for coming bytes
        byte[] clear = new byte[1024]; //std buffer for "everything OK"  reply
        byte[] done = new byte[1024]; //std buffer for exchanging done reply
        clear[0] = 111; // Ok signal
        done[0] = 33;

        DatagramPacket fromServer = new DatagramPacket(buf, 1024);

        socket.receive(fromServer);
        System.out.println("addr" + Arrays.toString(fromServer.getData()));
        Reply newAdr = Serializer.deserialize(fromServer.getData());

        int destPort = (Integer.parseInt(newAdr.getAnswer().split(":")[1]));

        DatagramPacket toServer = new DatagramPacket(clear,
                1024, ClientController.getDestIP(),destPort);


        byte[] result = new byte[0];
        while (true) {

            socket.receive(fromServer);
            System.out.println(Arrays.toString(fromServer.getData()));
            if (Arrays.equals(fromServer.getData(), done)) {
                socket.send(toServer);
                break;
            }
                socket.send(toServer);
                result = PacketUtils.merge(result,fromServer.getData());
        }
        return result;
    }
}
