package ServerSide;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.*;

public class MulticastSocketServer {

    final static String INET_ADDR = "230.0.0.1";
    final static int PORT = 3001;

    public static void main(String[] args) throws UnknownHostException, InterruptedException {

        //Asking SPECIFIC RM to send Replacement for Replica
        //**********************************************************************
        String askingForReplacement;
        JSONObject replaceJson = new JSONObject();
        try {
        replaceJson.put("rm", "2");
        replaceJson.put("from", "1");
        replaceJson.put("replace", "true");
        replaceJson.put("locationToChange", "CA");
        } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        askingForReplacement = replaceJson.toString();
        //**********************************************************************


        //Testing Regular Error Messages
        //**********************************************************************
        InetAddress addr = InetAddress.getByName(INET_ADDR);
        String message;
        JSONObject json = new JSONObject();
        try {
			json.put("location", "US");
			json.put("rm", "3");
			json.put("errorType", "wrongValue");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        message = json.toString();
        //**********************************************************************

        // Open a new DatagramSocket, which will be used to send the data.
        try  {
        	DatagramSocket serverSocket = new DatagramSocket();
                // Create a packet that will contain the data
                // (in the form of bytes) and send it.
                DatagramPacket msgPacket = new DatagramPacket(message.getBytes(),
                        message.getBytes().length, addr, PORT);
                serverSocket.send(msgPacket);

                System.out.println("Server sent packet with msg: " + message);
                Thread.sleep(500);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}