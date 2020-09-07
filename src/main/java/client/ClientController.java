package client;

import clientserverdata.Reply;
import clientserverdata.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Arrays;

public class ClientController {

    private static DatagramSocket clientSocket = null;
    private static InetAddress destIP;
    private static int destPort;

    static Reply handleRequest(Request request){
        byte[] serializedRequest = Serializer.serialize(request);
        assert serializedRequest != null;
        byte[] reply = null;
        try {
            sendConnectingHandshake();
            Sender.send(serializedRequest);
            reply = Receiver.getReply();
        }
        catch(SocketTimeoutException e){
            System.out.println("Server is not responding, please, try again later or change connection.");
            return null;
        }
        catch (IOException e){
            System.out.print("Oh no, some IO Exception occurs, please, try again.");
        }
        assert reply != null;
        return Serializer.deserialize(reply);
    }

    public static void connect(){
        try {
            System.out.print("Please enter a port that you want bind to:\n>");
            clientSocket = new DatagramSocket(changePort());
            clientSocket.setSoTimeout(10000);
            setDestIP("localhost");
            System.out.print("Please enter a port that you want connect to:\n>");
            setDestPort(changePort());
            System.out.println("Port has been successfully changed.");
        }
        catch (BindException e){
            System.out.println("Your port already in use, please choose another port:");
            int port = changePort();
            connect(port);
        }
        catch (SocketException e){
            System.out.println("Port is unavailable, please choose another port:");
            int port = changePort();
            connect(port);
        }
    }

    static void sendConnectingHandshake() throws IOException {
        byte[] buf = new byte[1024];
        buf[0] = 1;
        buf[1023] = 1;
        DatagramPacket packet = new DatagramPacket(buf, 0, 1024, ClientController.getDestIP(), ClientController.getDestPort());
        clientSocket.send(packet);
    }

    private static int changePort() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int port;
        while (true) {
            try {
                String p = reader.readLine();

                port = Integer.parseInt(p);
                if (port < 65535) {
                    break;
                }
                else {
                    throw new NumberFormatException();
                }
            } catch (IOException | NumberFormatException ex) {
                System.out.println("Unexpectedly port number, please, enter correct port number:");
            }
        }
        return port;
    }

    public static void connect(int number){
        try {
            clientSocket = new DatagramSocket(number);
            clientSocket.setSoTimeout(1000);
            System.out.println("Port has been successfully changed.");
        }
        catch (BindException e){
            System.out.println("Your port already in use, please choose another port:");
            int port = changePort();
            connect(port);
        }
        catch (SocketException e) {
            System.out.println("Port is unavailable, please choose another port:");
            int port = changePort();
            connect(port);
        }
    }

    public static DatagramSocket getClientSocket(){
        return clientSocket;
    }

    static InetAddress getDestIP() {
        return destIP;
    }

    public static void setDestIP(String name){
        try {
            destIP = InetAddress.getByName(name);
            System.out.println("IP has been successfully changed.");
        }
        catch (UnknownHostException e){
            System.out.println("IP address is not determined, please enter correct IP address:");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String IP;
            while (true) {
                try {
                    IP = reader.readLine();
                    break;
                } catch (IOException ex) {
                    System.out.println("Please enter a correct IP:");
                }
            }
            setDestIP(IP);
        }
    }

    public static void setDestPort(int destPort) {
        ClientController.destPort = destPort;
        System.out.println("Port has been successfully changed.");
    }

    static int getDestPort() {
        return destPort;
    }
}


