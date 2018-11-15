package ServerSide;

import java.io.IOException;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.KeyStore.Entry;
import java.util.concurrent.atomic.AtomicLong;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;

public class Sequencer {

private DatagramSocket socket;
private int sequenceNumber =  0;
private InetAddress group;
private byte[] buf; 
	
	public static void main(String args[]) {
		
		
		
		Json FE_Request;
		String FE_String;
		
					
		
		FE_String = run(1000);
		
		System.out.println( System.currentTimeMillis());
		
	
		
		
		
		
		
		
		
		
		
	}
	
	
	
	
	public int getSequenceNum() {
		
		
		return this.sequenceNumber;
	}
	
	
	
	
	
	
	//multicast message to group
	public void multicast(String multicastMessage) throws IOException {
		
		
		
		
		
		
		
		
		
		
		
		
		        socket = new DatagramSocket();
		        group = InetAddress.getByName("230.0.0.0");//which ever we decide on 
		        buf = multicastMessage.getBytes();
		 
		        DatagramPacket packet  = new DatagramPacket(buf, buf.length, group, 4446);
		        socket.send(packet);
		        socket.close();
		        
		        
		        //increments the sequence number
		        incrementSeqNum();
		        
		        
		    }
	
	public synchronized void incrementSeqNum() {
		
		this.sequenceNumber= this.sequenceNumber + 1;
	}
	
		
	
	
	
	//receive request from FRONT END
	public static  String run(int port) { 
		
			String FE_Str = null;
			DatagramSocket serverSocket = null;
	      try {
	    	
	    	 serverSocket = new DatagramSocket(port);
	        byte[] receiveData = new byte[8];
	        
	       

	        System.out.printf("Listening on udp",
	                InetAddress.getLocalHost().getHostAddress(), port);     
	        DatagramPacket receivePacket = new DatagramPacket(receiveData,
	                           receiveData.length);

	        while(true)
	        {
	              serverSocket.receive(receivePacket);
	              String sentence = new String( receivePacket.getData(), 0,
	                                 receivePacket.getLength() );
	              FE_Str = sentence;
	              System.out.println("RECEIVED: " + sentence);
	              // now send acknowledgement packet back to sender     
	              
	              
	              return  sentence;   
	              
	              
	        }
	      } catch (IOException e) {
	              System.out.println(e);
	      }
	      finally {if(serverSocket != null) serverSocket.close();}
	      
	      
		return FE_Str;
	      
	      
	     
	      // should close serverSocket in finally block
	    }
	
	
	//takes a json format String and adds a key, value
	public String addSequenceToJson(String message) {
		
		
		JsonObject object = jsonFromString(message);
		
		JsonObjectBuilder job = Json.createObjectBuilder();

	    for (java.util.Map.Entry<String, JsonValue> entry : object.entrySet()) {
	        job.add(entry.getKey(), entry.getValue());
	    }
		
	    JsonObject newObject = job.add("sequenceNumber", Integer.toString(this.sequenceNumber)).build();
		
		
		
		
		
		
		return newObject.toString();
		
	}
	
	
	
	//takes a string in json format and converts it to Json Object
	private static JsonObject jsonFromString(String jsonObjectStr) {

	    JsonReader jsonReader = Json.createReader(new StringReader(jsonObjectStr));
	    JsonObject object = jsonReader.readObject();
	    jsonReader.close();

	    return object;
	}
	
	

}
