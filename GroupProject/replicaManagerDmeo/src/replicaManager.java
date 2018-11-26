import org.json.JSONArray;
import org.json.JSONObject;
import java.net.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.*;

import org.json.*;

public class replicaManager implements Runnable {

    final static String INET_ADDR = "224.0.0.3";
    int PORT;

    UDPServer CAReplica;
    UDPServer USReplica;
    UDPServer UKReplica;
    int rmNumber;
    int CAcount = 0;
    int UScount = 0;
    int UKcount = 0;

    //***************************************************************************
    //Simple constructor which associates the replicas with a Replica Manager
    //***************************************************************************

    public replicaManager(UDPServer CAport, UDPServer USport, UDPServer UKport, int rmNum, int portNum) {
        this.CAReplica = CAport;
        this.USReplica = USport;
        this.UKReplica = UKport;
        this.rmNumber = rmNum;
        this.PORT = portNum;
    }

    //***************************************************************************

    //***************************************************************************
    //Method which runs in order to receive multicast message from FE an other Replica Managers
    //***************************************************************************
    public void receiveUDPMessage() throws IOException
    {

        // Get the address that we are going to connect to.
        InetAddress address = InetAddress.getByName(INET_ADDR);
        byte[] buf = new byte[1024];

        // Create a new Multicast socket
        //***************************************************************************
        try (MulticastSocket clientSocket = new MulticastSocket(PORT)){
            //Joint the Multicast group.
            clientSocket.joinGroup(address);
       //***************************************************************************

            //Starting to listen for incoming Multicast UDP Messages
            //*******************************************************************
            while (true) {

                // Receive the information and print it.
                //**********************************************************************
                DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
                clientSocket.receive(msgPacket);
                String msg = new String(buf, 0, buf.length);
                JSONObject incomingMessage = new JSONObject(msg);
                //**********************************************************************


                //Getting Target RM Integer Value
                //**********************************************************************
                String intendedRM = incomingMessage.getString("rm");
                int intValueRM = Integer.valueOf(intendedRM);
                //**********************************************************************


                //Parsing Incoming Message
                //**********************************************************************

                //Check if the Multicast Message is for this specific RM
                if (intValueRM == this.rmNumber)
                {
                    //**********************************************************************
                    //Regular Error, Increment Count
                    //**********************************************************************
                    if (incomingMessage.has("location")) {

                        String location = incomingMessage.getString("location");
                        String errorType = incomingMessage.getString("errorType");

                        if (errorType.equals("wrongValue"))
                        {
                            System.out.println("Wrong Value Error at RM#" + this.rmNumber + " for " + location);

                            if(location.equals("CA"))
                            {
                                this.CAcount++;

                                if(this.CAcount == 3)
                                {
                                    askForReplacement("CA");
                                    this.CAcount = 0;
                                }
                            }
                            else if(location.equals("US"))
                            {
                                this.UScount++;
                                if(UScount == 3)
                                {
                                    askForReplacement("US");
                                    UScount = 0;
                                }
                            }
                            else
                            {
                                this.UKcount++;
                                if(UKcount == 3)
                                {
                                    askForReplacement("UK");
                                    UKcount = 0;
                                }
                            }
                        }
                        //**********************************************************************
                        //Crash Error Immediately Replace this RM's Location
                        //**********************************************************************
                        if (errorType.equals("crash"))
                        {
                            System.out.println("There was an immediate crash at RM#" + this.rmNumber + " for " + location);

                            askForReplacement(location);
                        }

                    }
                    //**********************************************************************

                    //**********************************************************************
                    // Receiving Server from Another RM
                    //**********************************************************************
                    if (incomingMessage.has("server")) {

                        String replicaToReplaceLocation = incomingMessage.getString("serverLoc");
                        HashMap<Character, ArrayList<Records>> replacmentReplica  = new HashMap();
                        JSONObject server = incomingMessage.getJSONObject("server");

                        System.out.println(this.rmNumber + " RM is doing a Replica Replacement For " + replicaToReplaceLocation);

                        //Getting All the nested key Values
                        //**********************************************************************
                        Iterator keysToCopyIterator = server.keys();
                        List<String> keysList = new ArrayList<String>();
                        while(keysToCopyIterator.hasNext()) {
                            String key = (String) keysToCopyIterator.next();
                            keysList.add(key);
                        }
                        //**********************************************************************

                        //Reproducing the HashMap with the provided Json
                        //**********************************************************************
                        for (String element : keysList) {

                            ArrayList<Records> newList = new ArrayList<Records>();

                            JSONArray arr = incomingMessage.getJSONObject("server").getJSONArray(element);
                            for (int i = 0; i < arr.length(); i++)
                            {
                                String firstName = arr.getJSONObject(i).getString("firstName");
                                String lastName = arr.getJSONObject(i).getString("lastName");
                                String recordID = arr.getJSONObject(i).getString("recordsID");
                                String location = arr.getJSONObject(i).getString("location");
                                int employeeID = arr.getJSONObject(i).getInt("employeeID");
                                String mailID = arr.getJSONObject(i).getString("mailID");
                                String projectClient = arr.getJSONObject(i).getString("projectClient");
                                String projectName = arr.getJSONObject(i).getString("projectName");
                                String projectID = arr.getJSONObject(i).getString("projectID");

                                Records currentRecord = new Records(firstName,lastName,employeeID,mailID,projectID,projectClient,projectName,location,recordID);
                                newList.add(currentRecord);
                            }

                            replacmentReplica.put(element.charAt(0),newList);
                        }
                        //**********************************************************************

                        //Removing Broken Replica and Replacing
                        //**********************************************************************
                        int oldPort;

                        if(replicaToReplaceLocation.equals("CA"))
                        {
                            oldPort = CAReplica.getPort();
                            CAReplica.interrupt();
                            CAReplica = new UDPServer(oldPort,replicaToReplaceLocation,replacmentReplica);
                        }
                        else if(replicaToReplaceLocation.equals("US"))
                        {
                            oldPort = USReplica.getPort();
                            USReplica.interrupt();
                            USReplica = new UDPServer(oldPort,replicaToReplaceLocation,replacmentReplica);
                        }
                        else
                        {
                            oldPort = UKReplica.getPort();
                            UKReplica.interrupt();
                            UKReplica = new UDPServer(oldPort,replicaToReplaceLocation,replacmentReplica);
                        }

                        System.out.println("Successful Replacment for RM #" + this.rmNumber + " with location " + replicaToReplaceLocation);

                        UDPServer newReplica = new UDPServer(oldPort,replicaToReplaceLocation,replacmentReplica);
                        newReplica.prinData();
                        //**********************************************************************
                    }
                    //**********************************************************************

                    //***************************************************************************
                    //Sending a replacement for another RM
                    //***************************************************************************
                    if (incomingMessage.has("replace")) {

                        String locationToSend = incomingMessage.getString("locationToChange");
                        String rmToSendTo = incomingMessage.getString("from");

                        sendReplacement(locationToSend,rmToSendTo);
                    }
                    //**********************************************************************
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //***************************************************************************

    @Override
    public void run() {
        try {
            receiveUDPMessage();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void askForReplacement(String location) throws UnknownHostException, InterruptedException
    {
        InetAddress addr = InetAddress.getByName(INET_ADDR);
        String toRM;

        System.out.println("RM #" + this.rmNumber + " is asking for a replacement for " + location);

        //Determining which RM to ask for Replacement Replica
        //*********************************************************************
        if(this.rmNumber == 1)
        {
            toRM = "2";
        }
        else if(this.rmNumber == 2)
        {
            toRM = "1";
        }
        else
        {
            toRM = "1";
        }
        //**********************************************************************

        //Asking SPECIFIC RM to send Replacement for Replica
        //**********************************************************************
        String askingForReplacement;
        JSONObject replaceJson = new JSONObject();
        replaceJson.put("rm", toRM);
        replaceJson.put("from", String.valueOf(this.rmNumber));
        replaceJson.put("replace", "true");
        replaceJson.put("locationToChange", location);
        askingForReplacement = replaceJson.toString();
        //**********************************************************************

        // Datagram Socket Used to Send Packet
        //**********************************************************************
        try (DatagramSocket serverSocket = new DatagramSocket()) {

                DatagramPacket msgPacket = new DatagramPacket(askingForReplacement.getBytes(),
                        askingForReplacement.getBytes().length, addr, PORT);
                serverSocket.send(msgPacket);
                Thread.sleep(500);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //**********************************************************************
    }

    public void sendReplacement(String locationToSend, String rmTarget) throws UnknownHostException, InterruptedException
    {
        System.out.println("RM#" + this.rmNumber + " is sending a replacement replica for RM#" + rmTarget + " at location " + locationToSend );

        HashMap<Character, ArrayList<Records>> database  = new HashMap();

        //Getting Database to Send to Other RM
        //**********************************************************************
        if(locationToSend.equals("CA"))
        {
            database = this.CAReplica.getDatabase();
        }
        else if(locationToSend.equals("US"))
        {
            database = this.USReplica.getDatabase();
        }
        else
        {
            database = this.UKReplica.getDatabase();
        }
        //**********************************************************************

        JSONObject sendJson = new JSONObject();
        sendJson.put("server",database);
        sendJson.put("rm", rmTarget);
        sendJson.put("serverLoc", locationToSend);

        String sendServerData = sendJson.toString();
        InetAddress addr = InetAddress.getByName(INET_ADDR);


        // Open a new DatagramSocket, which will be used to send the data.
        try (DatagramSocket serverSocket = new DatagramSocket()) {

                // Create a packet that will contain the data
                // (in the form of bytes) and send it.
                DatagramPacket msgPacket = new DatagramPacket(sendServerData.getBytes(),
                        sendServerData.getBytes().length, addr, PORT);
                serverSocket.send(msgPacket);
                Thread.sleep(500);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
