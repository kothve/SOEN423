package ServerSide;

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

    final static String INET_ADDR = "230.0.0.1";
    int PORT = 3001;

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
        InetAddress address = InetAddress.getByName("230.0.0.1");
        byte[] buf = new byte[1024];

        // Create a new Multicast socket
        //***************************************************************************
        try {
        	MulticastSocket clientSocket = new MulticastSocket(3001);
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
                JSONObject incomingMessage = null;
				try {
					incomingMessage = new JSONObject(msg);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                //**********************************************************************


                //Getting Target RM Integer Value
                //**********************************************************************
                int intendedRM = 0;
				try {
					intendedRM = incomingMessage.getInt("rm");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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

                        String location = null;
						try {
							location = incomingMessage.getString("location");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                        String errorType = null;
						try {
							errorType = incomingMessage.getString("errorType");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

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

                        String replicaToReplaceLocation = null;
						try {
							replicaToReplaceLocation = incomingMessage.getString("serverLoc");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                        HashMap<Character, ArrayList<Records>> replacmentReplica  = new HashMap();
                        JSONObject server = null;
						try {
							server = incomingMessage.getJSONObject("server");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

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

                            JSONArray arr = null;
							try {
								arr = incomingMessage.getJSONObject("server").getJSONArray(element);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                            for (int i = 0; i < arr.length(); i++)
                            {
                            	try {
                            	String recordID = arr.getJSONObject(i).getString("recordsID");	
                                String firstName = arr.getJSONObject(i).getString("firstName");
                                String lastName = arr.getJSONObject(i).getString("lastName");                                
                                String location = arr.getJSONObject(i).getString("location");
                                int employeeID = arr.getJSONObject(i).getInt("employeeID");
                                String mailID = arr.getJSONObject(i).getString("mailID");
                                String projectClient = arr.getJSONObject(i).getString("projectClient");
                                String projectName = arr.getJSONObject(i).getString("projectName");
                                String projectID = arr.getJSONObject(i).getString("projectID");
                                
                                if(recordID.substring(0,2).equals("MR") && recordID.charAt(5) == '0'){
                                	recordID = "MR"+recordID.charAt(6);
                                }
                                else if(recordID.substring(0, 2).equals("MR") && recordID.charAt(5) != '0') {
                                	recordID = "MR"+recordID.substring(5, 7);
                                }
                                if(recordID.substring(0,2).equals("ER") && recordID.charAt(5) == '0'){
                                	recordID = "ER"+recordID.charAt(6);
                                }
                                else if(recordID.substring(0, 2).equals("ER") && recordID.charAt(5) != '0') {
                                	recordID = "ER"+recordID.substring(5, 7);
                                }
                                
                                if(recordID.substring(0,2).equals("MR")){
                                Records currentRecord = new Records(recordID, firstName,lastName,employeeID,mailID,projectID,projectClient,projectName,location);
                                newList.add(currentRecord);}
                                
                                else if(recordID.substring(0,2).equals("ER")) {
                                	
                                	Records currentRecord = new Records(recordID, firstName,lastName,employeeID,mailID,projectID);	
                                	newList.add(currentRecord);
                                	
                                }
                                
                                
                            	} catch (JSONException e) {
    								// TODO Auto-generated catch block
    								e.printStackTrace();
    							}
                                
                                
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
                            CAReplica = new UDPServer(oldPort,replicaToReplaceLocation,replacmentReplica,this.rmNumber);
                        }
                        else if(replicaToReplaceLocation.equals("US"))
                        {
                            oldPort = USReplica.getPort();
                            USReplica.interrupt();
                            USReplica = new UDPServer(oldPort,replicaToReplaceLocation,replacmentReplica,this.rmNumber);
                        }
                        else
                        {
                            oldPort = UKReplica.getPort();
                            UKReplica.interrupt();
                            UKReplica = new UDPServer(oldPort,replicaToReplaceLocation,replacmentReplica, this.rmNumber);
                        }

                        System.out.println("Successful Replacment for RM #" + this.rmNumber + " with location " + replicaToReplaceLocation);

                        UDPServer newReplica = new UDPServer(oldPort,replicaToReplaceLocation,replacmentReplica, this.rmNumber);
                        newReplica.prinData();
                        //**********************************************************************
                    }
                    //**********************************************************************

                    //***************************************************************************
                    //Sending a replacement for another RM
                    //***************************************************************************
                    if (incomingMessage.has("replace")) {
                    	try {
                        String locationToSend = incomingMessage.getString("locationToChange");
                        String rmToSendTo = incomingMessage.getString("from");
                        System.out.println("Sending Replacement");
                        sendReplacement(locationToSend,rmToSendTo);
                    	} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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
        //InetAddress addr = InetAddress.getByName("230.0.0.1");
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
        String askingForReplacement = null;
        JSONObject replaceJson = new JSONObject();
        try {
        replaceJson.put("rm", toRM);
        replaceJson.put("from", String.valueOf(this.rmNumber));
        replaceJson.put("replace", "true");
        replaceJson.put("locationToChange", location);
        askingForReplacement = replaceJson.toString();
        
       
        } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //**********************************************************************

        // Datagram Socket Used to Send Packet
        //**********************************************************************
        try  {
        	DatagramSocket serverSocket = new DatagramSocket();
                /*DatagramPacket msgPacket = new DatagramPacket(askingForReplacement.getBytes(),
                        askingForReplacement.getBytes().length, addr, PORT);
                serverSocket.send(msgPacket);*/
        	InetAddress group = InetAddress.getByName("230.0.0.1");//which ever we decide on 
        	byte[] buf = askingForReplacement.getBytes();
        	
	        DatagramPacket packet  = new DatagramPacket(buf, buf.length, group, 3001);
	        serverSocket.send(packet);
	        System.out.println("Server sent packet with msg: " + askingForReplacement);
        	
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
        try {
        sendJson.put("server",database);
        sendJson.put("rm", rmTarget);
        sendJson.put("serverLoc", locationToSend);
        } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        String sendServerData = sendJson.toString();
        InetAddress addr = InetAddress.getByName("230.0.0.1");


        // Open a new DatagramSocket, which will be used to send the data.
        try (DatagramSocket serverSocket = new DatagramSocket()) {

                // Create a packet that will contain the data
                // (in the form of bytes) and send it.
                DatagramPacket msgPacket = new DatagramPacket(sendServerData.getBytes(),
                        sendServerData.getBytes().length, addr, 3001);
                serverSocket.send(msgPacket);
                Thread.sleep(500);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
