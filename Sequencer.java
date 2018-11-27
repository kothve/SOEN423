package ServerSide;

import java.io.IOException;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.security.KeyStore.Entry;
import java.util.concurrent.atomic.AtomicLong;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;

public class Sequencer implements Runnable {

private static  DatagramSocket socket;

private   InetAddress group;
private   byte[] buf; 
private   int sequenceNumber; //unique ID


 private Sequencer() {
	 this.sequenceNumber = 0;
	 	 
 }


 



	public static void main(String args[]) {
		
		Sequencer sequencer = new Sequencer();
		
		
		//receives message for other sequencers 
		Thread thread = new Thread() {
		      public void run(){		       
		    	  try {
					sequencer.receiveUDPMessage("230.0.0.0", 3000);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		      }
		   };
		
		
		
				
		
		
		//receives message for other seqeuncers + and multicasts message to replicas
		sequencer.run();
		
		
		
		
	}
	
	
	
	
	public synchronized int getSequenceNum() {
		
		
		return this.sequenceNumber;
	}
	
	
	
	
	
	
	//multicast message to group
	public synchronized void  multicast(String multicastMessage) throws IOException {
		
			
		
		        socket = new DatagramSocket();
		        group = InetAddress.getByName("230.0.0.0");//which ever we decide on 
		        buf = multicastMessage.getBytes();
		 
		        DatagramPacket packet  = new DatagramPacket(buf, buf.length, group, 3000);
		        socket.send(packet);
		        socket.close();
		        incrementSeqNum();
		        
		        
		        
		        
		    }
	
	public synchronized   void incrementSeqNum() {
		
		sequenceNumber++;
	}
	
		
	
	
	
	//receive request from FRONT END
	public synchronized   void receiveFE(int port) { 
		
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
	              
	            	if(!FE_Str_seq.equals(null)) {
	    				
	    				try {
	    					multicast(FE_Str_seq); //send messsage as multicast
	    					//multicast(testHoldQueue()); // sends message with seq number of 10 to test hold queue
	    					System.out.println("sending : "+FE_Str_seq);
	    					System.out.println("sequenceNumber is: "+ getSequenceNum() );
	    							
	    				} catch (IOException e) {
	    					// TODO Auto-generated catch block
	    					e.printStackTrace();
	    				}
	    								
	    			}
	                
	              }
	              
	        }
	      } catch (IOException e) {
	              System.out.println(e);
	      }
	      finally {if(serverSocket != null) serverSocket.close();}
	      
	      
	
	      
	      
	     
	      // should close serverSocket in finally block
	    }
	
	
	//takes a json format String and adds a key, value (In this case adds a sequence number)
	public  String addSequenceToJson(String message) {
		
		
		JsonObject object = jsonFromString(message);
		
		JsonObjectBuilder job = Json.createObjectBuilder();

	    for (java.util.Map.Entry<String, JsonValue> entry : object.entrySet()) {
	        job.add(entry.getKey(), entry.getValue());
	    }
		
	    JsonObject newObject = job.add("sequenceNumber", Integer.toString(sequenceNumber)).build();
		
		
		
		
		
		
		return newObject.toString();
		
	}
	
	
	
	//takes a string in json format and converts it to Json Object
	private  JsonObject jsonFromString(String jsonObjectStr) {

	    JsonReader jsonReader = Json.createReader(new StringReader(jsonObjectStr));
	    JsonObject object = jsonReader.readObject();
	    jsonReader.close();

	    return object;
	}
	
	
	private  static String testHoldQueue(){
		
		String test_str = "{\"methodName\":\"getRecordCounts\",\"manager_ID\":\"CA10001\",\"sequenceNumber\":\"10\"}";
		
		return test_str;
	}
	
	
	
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
	     					if(Integer.parseInt(message.getString("sequenceNumber")) >= this.sequenceNumber   ) {
	     						
	     						this.sequenceNumber = Integer.parseInt(message.getString("sequenceNumber")) + 1;
     						
	     					}
      
	      }        
	      }
      
	   }

	public void run() {
		receiveFE(1000);
	}
	
	
	
	
	

}
