package ServerSide;

import java.io.IOException;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;



public class Replica implements Runnable {
	
	
	private HashMap<Integer, String> holdbackqueue;
	//UDP_Connect UDP_Server;
	int totalNumberOfRecords = 0;	
	int numberOfRecords;
    //int counter;
	 HashMap<Character, ArrayList<Records>> database;
	private int sequenceNumber = 0;
	String location;
	
	
	
	
	public Replica(String location, HashMap<Integer, String> holdbackqueue, HashMap<Character, ArrayList<Records>> database  ) {
		
		this.location = location;
		this.holdbackqueue = holdbackqueue;
		this.database = database;
		
		
	}
	
	
	
	
	


		   

	public void receiveUDPMessage(String ip, int port) throws IOException {
		      
			byte[] buffer=new byte[1024];
		     MulticastSocket socket=new MulticastSocket(1111);
		     InetAddress group=InetAddress.getByName("230.0.0.0");
		     socket.joinGroup(group);
		     
		     		while(true){
		     			
		     			System.out.println("Waiting for multicast message...");
		     			DatagramPacket packet=new DatagramPacket(buffer,
		     					buffer.length);
		     			socket.receive(packet);
		     			String msg = new String(packet.getData(),
		     					packet.getOffset(),packet.getLength());
		     			System.out.println("[Multicast UDP message received>> "+msg);
		         
		     			JsonObject message = jsonFromString(msg);
		         
		     				if(message.containsKey("sequenceNumber")){
		         
		        	 
		     					if(Integer.parseInt(message.getString("sequenceNumber")) == this.sequenceNumber   ) {
		     						
		     						incrementSeqNum();
		         		
		     						//delivers message
		     						
		     					}
		        		 
		     					else if(Integer.parseInt(message.getString("sequenceNumber")) > this.sequenceNumber) {
		        		 
		     						//adds to hold back queue
		     						
		     					this.holdbackqueue.put(Integer.parseInt(message.getString("sequenceNumber")), message.toString());	
		        		 
		     					}
		        	 
		        	 
		        	 
		         
		         
		      }
		         
		         
		         
		      }
		     
		      
		   }
		
		   
	public void	delivers(String message) {
		
		
		
		
		
		
		
		
	}
	
	


	
	private JsonObject jsonFromString(String jsonObjectStr) {

	    JsonReader jsonReader = Json.createReader(new StringReader(jsonObjectStr));
	    JsonObject object = jsonReader.readObject();
	    jsonReader.close();

	    return object;
	}


public synchronized void incrementSeqNum() {
		
		this.sequenceNumber= this.sequenceNumber + 1;
	}




	@Override
	public void run() {
		
		try {
			receiveUDPMessage("230.0.0.0", 4321);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	

}
