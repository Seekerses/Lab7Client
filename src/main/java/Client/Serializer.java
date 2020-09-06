package Client;

import cliser.Reply;

import java.io.*;
import java.util.Arrays;

public class Serializer {

    protected static <T> byte[] serialize(T obj){
        byte[] buff;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(baos);
            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();
            buff = baos.toByteArray();
            return buff;
        }
        catch (IOException e){
            //make
            return null;
        }
    }

    protected static Reply deserialize(byte[] data) {
        try{
            if (data.length>0) {
                ObjectInput ois = null;
                Reply answ = null;
                try (ByteArrayInputStream bais = new ByteArrayInputStream(data)) {
                    ois = new ObjectInputStream(bais);
                    answ = (Reply) ois.readObject();
                }catch (EOFException e){
                    assert ois != null;
                    ois.close();
                }
                ois.close();
                return answ;
            }
        }
        catch (IOException ioException) {
            System.out.println("Oh no, some IO exception occurs.");
            ioException.printStackTrace();
        }
        catch (ClassNotFoundException classNotFoundException){
            System.out.println("An unknown format response was received from the server, " +
                    "please change the connection to the correct server.");
        }
        return null;
    }
    
}
