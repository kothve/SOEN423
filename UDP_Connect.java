package ServerSide;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.io.*;


public class UDP_Connect extends Thread {
	
	int port;
	String location;
	int numberOfRecords;
	 HashMap<Character, ArrayList<Records>> database;
	 
	 
	
	 
	 
	 
	public UDP_Connect(int port, HashMap<Character, ArrayList<Records>> database, int numberOfRecords) {
		
		this.port = port;
		this.database = database;
		this.numberOfRecords = numberOfRecords;
		
		if(port == 6050 || port == 6053 || port == 6056) {
			
			this.location = "CA";
			
			
		}
		else if(port == 6051 || port == 6054 || port == 6057) {
			
			this.location = "US";
			
			
		}
		else if(port == 6052 || port == 6055 || port == 6058) {
			
			this.location = "UK";
			
			
		}
		
		
		
		
		
	}
	
	
	
	
	
	public void run() {
		
		
		
			try {
				start_server();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
    }
	
	
	
	//server that replies with number of records
	public void start_server() throws ClassNotFoundException {
		
		System.out.println("UPD SERVER WITH PORT "+port +" IS STARTING AND HAS "+ numberOfRecords+" records.");
		
		DatagramSocket aSocket = null;
		try{
			
	    	aSocket = new DatagramSocket(port);
					// create socket at agreed port
			byte[] buffer = new byte[1024];
			
			
 			while(true){
 				
 				String response;
 				
 				
 				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
  				aSocket.receive(request);
  				
  				
  				String option = new String(request.getData(),0,request.getLength());
  				
  				byte[] buf = new byte[1024];
  				
  				
  				if(option.equals("number")) { //if message received is "number", reply with the current number of records
  					
  					
  	  			
  				response = Integer.toString(numberOfRecords);
  				buf = response.getBytes(); 
  				
    			
  	  				
  	  				
  					
  					
  				}
  				
  				else if(option.substring(0,2).equals("ER") || (option.substring(0,2).equals("MR"))) {//find record
  					
  					
  				
  				if(recordExists(option)) {
  					
  						response = "y";//record exists in this database
		  				//do nothing
  						 buf = response.getBytes();
  				}
  				else {
  					
  					response = "n";//record does not exists in this database
  					///initiate transfer
  					 buf = response.getBytes();
  					
  					
  					
  					
  					
  					
  					
  					
  				}} //end else if record exists
  				
  				
  				else {// Receive Record Object/ de-serialiaze
  					ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(request.getData()));
  					Records record = (Records) iStream.readObject();
  					iStream.close();
  					
  					System.out.println(record.getRecordsID());
  					
  					char dbCharacter;
  					if(record.getRecordsID().substring(0,2).equals("MR")) {
  						
  						
  						record.setLocation(location);
  					}
  					//add record to database
  					dbCharacter = Character.toLowerCase(record.getLastName().charAt(1));
  					Set<Character> keySet = this.database.keySet();
  				   
  				   java.util.Iterator<Character> keySetIterator = keySet.iterator();
  				   
  				 
  				  System.out.println("CHARACHER IS " + dbCharacter); 
  				   
  				   if(this.database.containsKey(dbCharacter)) {
  					   
  					   
  				   
  				   while (keySetIterator.hasNext()) {
  					   
  					   
  					   Character key = keySetIterator.next();
  					   if (dbCharacter == key) {
  						   
  						   this.database.get(key).add(record);
  						   
  						   
  						    
  					   }
  					   
  				   }
  		   }else {
  			   
  			   ArrayList<Records> addNewArray = new ArrayList<Records>();
  			   
  			   this.database.put(dbCharacter, addNewArray);
  			   this.database.get(dbCharacter).add(record);
  			
  			   
  			   
  		   }

  					
  					numberOfRecords = getHashSize(database);
  					
  					
  					response ="Record Succesfully Received";
  					 buf = response.getBytes(); 
  					
  				}
  				
  				
  				  
  				  
  				  
  				
  				
  				DatagramPacket reply = new DatagramPacket(buf, buf.length, 
  	    		request.getAddress(), request.getPort());
  	    		aSocket.send(reply);
  				
    		}
		}catch (SocketException e){System.out.println("Socket in start_server: " + e.getMessage());
		}catch (IOException e) {System.out.println("IO: " + e.getMessage());
		}finally {if(aSocket != null) aSocket.close();}
		
		
		
	}
	

	
	public void setNumberOfRecords(int n) {
		
		this.numberOfRecords = n;
		
	}
	
public void setDatabase(HashMap<Character, ArrayList<Records>> database) {
		
		this.database = database;
		
	}

public HashMap<Character, ArrayList<Records>> getDatabase(HashMap<Character, ArrayList<Records>> database) {
	
	return this.database;
	
}

	
	
public synchronized String getRecords(int port) {
	
	String record = null;
	
	try {
		DatagramSocket clientSocket = new DatagramSocket();
	      InetAddress IPAddress = InetAddress.getByName("localhost");
	      byte[] sendData = new byte[1024];
	      byte[] receiveData = new byte[1024];
	      String sentence = "number";
	      sendData = sentence.getBytes();
	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
	      clientSocket.send(sendPacket);
	      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	      clientSocket.receive(receivePacket);
	      record = new String(receivePacket.getData(),0,receivePacket.getLength());
	     
	    	           
	      
	      clientSocket.close();
	     	      	     	      	     
	      
 	          	      
 } // end try
  catch (Exception ex) {
    ex.printStackTrace( );
    return "Getting Record Error";
  } //end catch
		
	
	
	
	
	 return record;
	
	}//end get records
		
public boolean recordExists(String recordID) {
	
	boolean exists =false;
	
	
		
		Iterator it =  this.database.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();

	        ArrayList<Records> value = (ArrayList<Records>) pair.getValue();
	        //System.out.println("[" + pair.getKey() + "] = ");
	        for(int i = 0; i < value.size(); i++) {
	        	
	        	
	        	
	        	if(value.get(i).getRecordsID().equals(recordID)) {
		
	        		exists = true;
	        		
		
	        	}//
	        	
	}}
	
		
			
	return false;
	
	
	
	
	
	
}	

public synchronized void sendRecord(Records record, int port) throws IOException {
	
	
	
	DatagramSocket clientSocket = new DatagramSocket();
    InetAddress IPAddress = InetAddress.getByName("localhost");
    
	
	
	
	
	
	ByteArrayOutputStream bStream = new ByteArrayOutputStream();
	ObjectOutput oo = new ObjectOutputStream(bStream); 
	oo.writeObject(record);
	oo.close();

	byte[] serializedRecord = bStream.toByteArray();
	
	DatagramPacket sendPacket = new DatagramPacket(serializedRecord, serializedRecord.length, IPAddress, port);
    clientSocket.send(sendPacket);
	
	
	
	
	
	
	
}

public int getHashSize(HashMap<Character, ArrayList<Records>> database) {
	   int counter = 0;
	   
	   Iterator it =  this.database.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();

	        ArrayList<Records> value = (ArrayList<Records>) pair.getValue();
	        
	        counter+=value.size();
	        }
	   
	   
	   
	   return counter;
}





public synchronized String askRecordExists(int port, String recordID) {
	
	String response = null;
	
	try {
		DatagramSocket clientSocket = new DatagramSocket();
	      InetAddress IPAddress = InetAddress.getByName("localhost");
	      byte[] sendData = new byte[1024];
	      byte[] receiveData = new byte[1024];
	      String sentence = recordID;
	      sendData = sentence.getBytes();
	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
	      clientSocket.send(sendPacket);
	      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	      clientSocket.receive(receivePacket);
	      response = new String(receivePacket.getData(),0,receivePacket.getLength());
	     
	    	           
	      
	      clientSocket.close();
	     	      	     	      	     
	      
 	          	      
 } // end try
  catch (Exception ex) {
    ex.printStackTrace( );
    return "Getting Record Error";
  } //end catch
		
	
	
	
	
	 return response;
	
	}//end get records






	}
	

	
	
	
	
	
		
		
	
	
	


