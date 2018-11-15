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

private static  DatagramSocket socket;

private static  InetAddress group;
private static  byte[] buf; 
private static  int sequenceNumber =  0;




	public static void main(String args[]) {
		
		
		
		
		
		Json FE_Request;
		String FE_String;
		
		
		
		
		
		
		while(true) {
			
							
			
			FE_String = run(1000);
			
			
			if(!FE_String.equals(null)) {
				
				try {
					multicast(FE_String); //send messsage as multicast
					multicast(testHoldQueue()); // sends message with seq number of 10 to test hold queue
					System.out.println("sequenceNumber is: "+sequenceNumber );
							
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
								
			}
							
		}				
		
	}
	
	
	
	
	public synchronized int getSequenceNum() {
		
		
		return sequenceNumber;
	}
	
	
	
	
	
	
	//multicast message to group
	public synchronized static void  multicast(String multicastMessage) throws IOException {
		
			
		
		        socket = new DatagramSocket();
		        group = InetAddress.getByName("230.0.0.0");//which ever we decide on 
		        buf = multicastMessage.getBytes();
		 
		        DatagramPacket packet  = new DatagramPacket(buf, buf.length, group, 3000);
		        socket.send(packet);
		        socket.close();
		        
		        
		        //increments the sequence number
		        incrementSeqNum();
		        
		        
		    }
	
	public synchronized static  void incrementSeqNum() {
		
		sequenceNumber++;
	}
	
		
	
	
	
	//receive request from FRONT END
	public synchronized static  String run(int port) { 
		
			String FE_Str = null;
			DatagramSocket serverSocket = null;
	      try {
	    	
	    	 serverSocket = new DatagramSocket(port);
	        byte[] receiveData = new byte[1024];
	        
	       

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
	              
	              
	              JsonObject obj = jsonFromString(FE_Str);
	              
	              //Checks if message received is a valid request in the right json format
	              if(obj.containsKey("methodName")){
	              
	              
	              
	            	String FE_Str_seq =  addSequenceToJson(FE_Str);
	              
	              
	              return  FE_Str_seq;   
	              }
	              
	        }
	      } catch (IOException e) {
	              System.out.println(e);
	      }
	      finally {if(serverSocket != null) serverSocket.close();}
	      
	      
		return FE_Str;
	      
	      
	     
	      // should close serverSocket in finally block
	    }
	
	
	//takes a json format String and adds a key, value (In this case adds a sequence number)
	public static String addSequenceToJson(String message) {
		
		
		JsonObject object = jsonFromString(message);
		
		JsonObjectBuilder job = Json.createObjectBuilder();

	    for (java.util.Map.Entry<String, JsonValue> entry : object.entrySet()) {
	        job.add(entry.getKey(), entry.getValue());
	    }
		
	    JsonObject newObject = job.add("sequenceNumber", Integer.toString(sequenceNumber)).build();
		
		
		
		
		
		
		return newObject.toString();
		
	}
	
	
	
	//takes a string in json format and converts it to Json Object
	private static JsonObject jsonFromString(String jsonObjectStr) {

	    JsonReader jsonReader = Json.createReader(new StringReader(jsonObjectStr));
	    JsonObject object = jsonReader.readObject();
	    jsonReader.close();

	    return object;
	}
	
	
	private  static String testHoldQueue(){
		
		String test_str = "{\"methodName\":\"getRecordCounts\",\"manager_ID\":\"CA10001\",\"sequenceNumber\":\"10\"}";
		
		return test_str;
	}
	
	
	
	
	

}
