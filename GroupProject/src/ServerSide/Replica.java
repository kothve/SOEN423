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
	private int sequenceNumber;
	//UDP_Connect UDP_Server;
	int totalNumberOfRecords = 0;	
	int numberOfRecords;
	HashMap<Character, ArrayList<Records>> database;
	String location;
	
	
	
	
	public Replica(String location, HashMap<Integer, String> holdbackqueue, HashMap<Character, ArrayList<Records>> database  ) {
		this.sequenceNumber = 0;
		this.location = location;
		this.holdbackqueue = holdbackqueue;
		this.database = database;
		
		
	}
	
	
	
	
	


		   
	//RECEIVE MESSAGE FROM MULTICAST
	public  synchronized void receiveUDPMessage(String ip, int port) throws IOException {
		      
			byte[] buffer=new byte[1024];
		     MulticastSocket socket=new MulticastSocket(3000);
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
		         
		     			
		     			
		     			
		     				//determines if message received is not some random message
		     				if(message.containsKey("sequenceNumber")){
		         
		        	 
		     					//determines if the message received is should be delivered
		     					if(Integer.parseInt(message.getString("sequenceNumber")) == this.sequenceNumber   ) {
		     						
		     						incrementSeqNum();
		         		
		     						deliver(msg);
		     						
		     					}
		     					
		     					//determines if the message received is not to be delivered and added to queue
		     					else if(Integer.parseInt(message.getString("sequenceNumber")) > this.sequenceNumber) {
		        		 
		     						//adds to hold back queue
		     						
		     					this.holdbackqueue.put(Integer.parseInt(message.getString("sequenceNumber")), message.toString());	
		     					
		     					System.out.println("Message has been put on hold "+message.toString());
		     					}
		        	 
		        	 
		        	 
		         
		         
		      }
		         
		         
		         
		      }
		     
		      
		   }
		
	//MESSAGE HAS PASSED SEQUENCE NUMBER CHECK AND NOW HAS TO BE EXECUTED	   
	public String deliver(String message) {
		
		
		//testing//
		System.out.println("Message has been delivered :"+ message );		
		
		//execute method CreateERecord, CreateMRecord, etc... To be added
		
		
		return message;//Message to return to the FE
						
				
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

	public int getSequenceNumber() {
		return this.sequenceNumber;
	}
	
	///////////////////////testing replica//////////////////////////////////
public static void main(String[] args) {
	    
		HashMap<Integer, String> holdbackqueue = new HashMap <Integer, String>();
		HashMap<Character, ArrayList<Records>> database = new HashMap <Character, ArrayList<Records>>();
		
		
		
		
		
		Replica CA = new Replica("CA",holdbackqueue, database );
		
		CA.run();
		
		
		
		
	      //CHECK HOLDING QUEUE FOR NEXT MESSAGE TO DELIVER
	      while(true) {
	    	  
	    	  int seq = CA.getSequenceNumber();
	    	  
	    	  
	    	  // If the replica Sequence number is equal to one of the sequence numbers in Queue, deliver the message associated to that seq number
	    	  if(CA.holdbackqueue.containsKey(seq)) {
	    		  
	    		  
	    		  CA.deliver(CA.holdbackqueue.get(seq));
	    		  CA.incrementSeqNum();
	    		  
	    		  
	    	  }
	    	  
	    	  
	    	 
	    	  
	    	  
	    	  
	      }
	      
	      
	      
	      
		
		
	   }
	
	
	
/////////////////////// END testing replica//////////////////////////////////
	

	

}
