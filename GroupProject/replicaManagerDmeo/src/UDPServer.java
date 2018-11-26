import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



public class UDPServer extends Thread {

    int totalNumberOfRecords = 6;
    String location;
    int numberOfRecords;

    HashMap<Character, ArrayList<Records>> database;



    int desiredPort;
    String currentLocation;

    public UDPServer(int port, String location,  HashMap<Character, ArrayList<Records>> newReplica ) {
        this.desiredPort = port;
        this.currentLocation = location;
        this.database = newReplica;
    }


    //Constructor
    public UDPServer(int port, String location )
    {
        this.desiredPort = port;
        this.currentLocation = location;

        HashMap<Character, ArrayList<Records>> database  = new HashMap();

        if(location.equals("CA")) {

            //put 3 records inside database
            ArrayList<Records> addNewArray1 = new ArrayList<Records>();
            ArrayList<Records> addNewArray2 = new ArrayList<Records>();

            database.put('m', addNewArray1);
            database.get('m').add(new Records( "MR1" , " Olivier ", 	" Mercier-Peetz ",	 27181000  , " oliviermercierpeetz@live.ca ", " P1232132 ", " UofConcordia ", " A1 " ,"CA"));
            database.get('m').add(new Records( "MR2" , " Karen ", 	" Marquez ",	 21232123  , " km@live.ca ", " P123213 ", " UofConcordia ", " A1 " ,"CA"));
            database.put('p', addNewArray2);
            database.get('p').add(new Records( "ER3" , " Uwe ", 	" Peetz ",	 2122323  , " uwe-p@live.ca ", "P10001"));
            //3 records inside hashmap

            this.numberOfRecords = 3;

            //UDP_Server.setNumberOfRecords(numberOfRecords);
            this.database = database;

        }

        else if(location.equals("US")) {

            ArrayList<Records> addNewArray1 = new ArrayList<Records>();
            database.put('o', addNewArray1);
            database.get('o').add(new Records( "MR4" , " Dave ", 	" Oboto ",	 321321  , " obotoz@live.ca ", " P1654452 ", " Canopy ", " A1 " ,"US"));

            this.numberOfRecords = 1;

            this.database = database;

        }
        else if(location.equals("UK")) {

            ArrayList<Records> addNewArray1 = new ArrayList<Records>();
            ArrayList<Records> addNewArray2 = new ArrayList<Records>();
            database.put('a', addNewArray1);
            database.get('a').add(new Records( "MR5" , " Alexia ", 	" Alena ",	 432412  , " a.alena@live.ca ", " P1632 ", " ACB ", " A1 " ,"UK"));
            database.put('c', addNewArray2);
            database.get('c').add(new Records( "ER6" , " Bob ", 	" Cotton ",	 5432412  , " b.c@live.ca ", " P163223 ", " Namaste tech ", " A1 " ,"UK"));

            numberOfRecords = 2;
            this.database = database;
        }

        prinData();

    }

    public void run()
    {

        DatagramSocket serverSocket = null;
        try {
            serverSocket = new DatagramSocket(desiredPort);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];
        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            try {
                serverSocket.receive(receivePacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String sentence = new String(receivePacket.getData());
            System.out.println("RECEIVED: " + sentence);
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            String capitalizedSentence = sentence.toUpperCase();
            sendData = capitalizedSentence.getBytes();
            DatagramPacket sendPacket =
                    new DatagramPacket(sendData, sendData.length, IPAddress, port);
            try {
                serverSocket.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void prinData() {

        System.out.println("[--------------------------------------------"+this.currentLocation+"-----------------------------------------------]");

        Iterator it =  this.database.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();

            ArrayList<Records> value = (ArrayList<Records>) pair.getValue();
            System.out.println("[" + pair.getKey() + "] = ");
            for(int i = 0; i < value.size(); i++) {

                System.out.println(i+" : " + value.get(i).display_String() + "");


            }

            System.out.println("");


        }
        System.out.println("[-------------------------------------------------------------------------------------------]");

    }

    public void setFirstDatabase(HashMap<Character, ArrayList<Records>> database)
    {
        this.database = database;
    }

    public HashMap<Character, ArrayList<Records>> getDatabase()
    {

        return this.database;
    }

    public int getPort()
    {
        return this.desiredPort;
    }



}

