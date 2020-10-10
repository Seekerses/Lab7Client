package client;

import clientserverdata.Reply;
import clientserverdata.Request;
import consolehandler.ClientInterpreter;
import consolehandler.CommandController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Arrays;

public class ClientController {

    private static DatagramSocket clientSocket = null;
    private static InetAddress destIP;
    private static Integer destPort;
    private static Integer port = null;
    private static InetAddress tempIP = null;
    private static Integer tempPort;

    static Reply handleRequest(Request request){
        byte[] serializedRequest = Serializer.serialize(request);
        assert serializedRequest != null;
        byte[] reply = null;
        try {
            sendRequestingHandshake();
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
            if (port == null) {
                System.out.print("Please enter a port that you want bind to:\n>");
                port = changePort();
                clientSocket = new DatagramSocket(port);
                clientSocket.setSoTimeout(5000);
            }
            setDestIP("localhost");
            System.out.print("Please enter a port that you want connect to:\n>");
            setDestPort(changePort());
            System.out.println("Port has been successfully changed.");
             if(sendConnectingHandshake()){
                 System.out.println("Connection stabled.");
             }
             else {
                 System.out.println("Connection failed. Please choose another port.");
                 connect();
             }
            CommandController.registration(new ClientInterpreter());
        }
        catch (SocketTimeoutException ex){
            System.out.println("Chosen server is not responding. Please try again...\n");
            connect();
        }
        catch (BindException e){
            System.out.println("Your port already in use.");
            connect();
        }
        catch (SocketException e){
            System.out.println("Port is unavailable.");
            connect();
        } catch (IOException e) {
            System.out.println("Some IO errors occurs");
        }
    }

    private static boolean sendConnectingHandshake() throws IOException {
        byte[] buf = new byte[1024];
        buf[0] = 1;
        buf[1023] = 1;
        DatagramPacket packet = new DatagramPacket(buf, 0, 1024, ClientController.getDestIP(), ClientController.getDestPort());
        clientSocket.send(packet);
        DatagramPacket connect = new DatagramPacket(new byte[1024],1024);
        clientSocket.receive(connect);
        return Arrays.equals(buf, connect.getData());
    }
    private static void sendRequestingHandshake() throws IOException {
        byte[] buf = new byte[1024];
        buf[0] = 100;
        buf[1023] = 100;
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

    public static DatagramSocket getClientSocket(){
        return clientSocket;
    }

    static InetAddress getDestIP() {
        return destIP;
    }

    public static void setDestIP(String name){
        try {
            destIP = InetAddress.getByName(name);
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
    }

    static int getDestPort() {
        return destPort;
    }

    public static InetAddress getTempIP() {
        return tempIP;
    }

    public static void setTempIP(InetAddress tempIP) {
        ClientController.tempIP = tempIP;
    }

    public static int getTempPort() {
        return tempPort;
    }

    public static void setTempPort(int tempPort) {
        ClientController.tempPort = tempPort;
    }
}


