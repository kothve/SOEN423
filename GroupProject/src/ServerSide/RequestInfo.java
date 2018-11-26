package ServerSide;
import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import ServerCenterApp.ServerCenterIDLPOA;

import java.util.Properties;
import java.io.IOException;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.rmi.*;
import java.rmi.server.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Iterator;



/**
 * This class implements the remote interface SomeInterface.
 */

public class RequestInfo  extends ServerCenterIDLPOA  {
	boolean serverCrash = false;
	DatagramSocket serverSocketCrash;	
	private static  DatagramSocket socket;
	private   InetAddress group;
	private   byte[] buf; 
	int crashIndex = 2;
	int serverType; // 0 is software failure , 1 is replica is down	
	String RMlocation = null;		
	String info;
	String methodName;
	String manager_ID;
	String firstName;
	String lastName;
	 int employeeID;
	 String mailID;
	 String projectID;
	 String clientName;
	 String projectName;
	 String location;
	 String remoteCenterServer;
	 String recordID;
	 String fieldName;
	 String newValue;
	 int missingRM = 0;
	 int port = 2222;
	 String IP_address;
	 DatagramSocket serverSocket;
	 
	 
		
	private ORB orb;
	
	RequestInfo(int serverType) {
		
		this.serverType = serverType;
	}
	
	 public void setORB(ORB orb_val) {
		    orb = orb_val; 
		  }

	
	public String createMRecord(String methodName, String manager_ID, String firstName, String lastName, int employeeID,
			String mailID, String projectID, String clientName, String projectName, String location) {
		
		String returnMessage = null;
		
		//get initial time
		long initialTime = System.currentTimeMillis();
		
		
		this.methodName = methodName;
		this.manager_ID = manager_ID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.employeeID = employeeID;
		this.mailID = mailID;
		this.projectID = projectID;
		this.clientName = clientName;
		this.projectName = projectName;
		this.location = location;
		
		
		info = makeJSON();
		
		
		to_Sequencer_UDP(1000, info );
		
		if(serverType == 0) {
		 returnMessage =  receiveFromReplica(2222);  
		}
		else if(serverType ==1) {
			
			 returnMessage =  checkForCrash(2222, initialTime);  	
			
		}
		
		
		
		return returnMessage;
	}

	
	public String getRecordCounts(String methodName, String manager_ID) {
		
		String returnMessage = null;
		
		//get initial time
		long initialTime = System.currentTimeMillis();
		
		
		
		
		this.methodName = methodName;
		this.manager_ID = manager_ID;
		
		
		info = makeJSON();
		// TODO Auto-generated method stub
		
		//sends info to sequencer
		
		
		
		
		to_Sequencer_UDP(1000, info );
		
		if(serverType == 0) {
		 returnMessage =  receiveFromReplica(2222);  
		}
		else if(serverType ==1) {
			
			 returnMessage =  checkForCrash(2222, initialTime);  	
			
		}
		
		
		
		return returnMessage;
	}

	
	public String createERecord(String methodName, String manager_ID, String firstName, String lastName, int employeeID,
			String mailID, String projectID) {
		
		String returnMessage = null;
		//get initial time
		long initialTime = System.currentTimeMillis();
		
		
		
		
		this.methodName = methodName;
		this.manager_ID = manager_ID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.employeeID = employeeID;
		this.mailID = mailID;
		this.projectID = projectID;
		
		
		
		
		
		to_Sequencer_UDP(1000, info );
		
		if(serverType == 0) {
		 returnMessage =  receiveFromReplica(2222);  
		}
		else if(serverType ==1) {
			
			 returnMessage =  checkForCrash(2222, initialTime);  	
			
		}
		
		
		
		return returnMessage;
	}

	
	public String transferRecord(String methodName, String manager_ID, String recordID, String remoteCenterServer) {
		// TODO Auto-generated method stub
		String returnMessage = null;		
		//get initial time
		long initialTime = System.currentTimeMillis();
		
		
		
		
		this.methodName = methodName;
		this.manager_ID = manager_ID;
		this.recordID = recordID;
		this.remoteCenterServer = remoteCenterServer;
		
		
		
		
		to_Sequencer_UDP(1000, info );
		
		if(serverType == 0) {
		 returnMessage =  receiveFromReplica(2222);  
		}
		else if(serverType ==1) {
			
			 returnMessage =  checkForCrash(2222, initialTime);  	
			
		}
		
		
		
		return returnMessage;
	}

	
	public String editRecord(String methodName, String manager_ID, String recordID, String fieldName, String newValue) {
		
		String returnMessage = null;
		//get initial time
		long initialTime = System.currentTimeMillis();
		
		
		// TODO Auto-generated method stub
		this.methodName = methodName;
		this.manager_ID = manager_ID;
		this.recordID = recordID;
		this.fieldName = fieldName;
		this.newValue = newValue;
		
		
		
		
		
		
		to_Sequencer_UDP(1000, info );
		
		if(serverType == 0) {
		 returnMessage =  receiveFromReplica(2222);  
		}
		else if(serverType ==1) {
			
			 returnMessage =  checkForCrash(2222, initialTime);  	
			
		}
		
		
		
		return returnMessage;
	}
	
	
	
	
	//TRANSFORMS THE ARGUMENTS INTO A JSON OBJ
	public String makeJSON() {
		
		
	String jsonString = "";	
	String method = this.methodName;
	
	
	if(method.equals("createMRecord")) {
		
	
		jsonString = Json.createObjectBuilder()
	            .add("methodName", this.methodName)
	            .add("manager_ID", this.manager_ID)
	            .add("args", Json.createObjectBuilder().add("firstName", this.firstName)
														.add("firstName", this.firstName)            
														.add("lastName", this.lastName)
														.add("employeeID", this.employeeID)
														.add("mailID", this.mailID)
														.add("projectID", this.projectID)	           
														.add("clientName", this.clientName)
														.add("projectName", this.projectName)
														.add("location", this.location))
	            .add("port", this.port)
	            .add("ip", getIP())
	            .build()
	            .toString();	
		
		
		
		
			
		
		
		
		
		
	}
		
	else if(method.equals("createERecord")) {
		
		jsonString = Json.createObjectBuilder()
	            .add("methodName", this.methodName)	            
	            .add("manager_ID", this.manager_ID)
	            .add("args", Json.createObjectBuilder().add("firstName", this.firstName)
	            										.add("firstName", this.firstName)            
	            										.add("lastName", this.lastName)
	            										.add("employeeID", this.employeeID)
	            										.add("mailID", this.mailID)
	            										.add("projectID", this.projectID))
	            .add("port", this.port)
	            .add("ip", getIP())
	            .build()
	            .toString();		
		
	}	
	else if(method.equals("getRecordCounts")) {
		
		
		jsonString =  Json.createObjectBuilder()
	            //.add("methodName", this.methodName)
				.add("methodName", "getRecordCounts")
	            .add("manager_ID", "CA10001")
				//.add("args", Json.createObjectBuilder().add("firstName", "Bob"))
	            .add("port", this.port)
	            .add("ip", getIP())
	            .build()
	            .toString();	
		
		
		
	}		
	else if(method.equals("editRecord")) {
		jsonString = Json.createObjectBuilder()
		.add("methodName", this.methodName)
        .add("manager_ID", this.manager_ID)
        .add("args", Json.createObjectBuilder().add("recordID", this.recordID)	       
        										.add("fieldName", this.fieldName)
        										.add("newValue", this.newValue))
        .add("port", this.port)
        .add("ip", getIP())
        .build()
        .toString();	
		
		
	}		
	else if(method.equals("transferRecords")) {
		
		jsonString = Json.createObjectBuilder()
	            .add("methodName", this.methodName)
	            .add("manager_ID", this.manager_ID)
	            .add("args", Json.createObjectBuilder().add("recordID", this.recordID)	       
	            										.add("remoteCenterServer", this.remoteCenterServer))
	            .add("port", this.port)
	            .add("ip", getIP())
	            .build()
	            .toString();
		
		
		
	}
	
	
	
	
	return jsonString;		
		
	}//END METHOD MAKEJSON
	
	
	public void to_Sequencer_UDP(int port, String message) {
		
		
		String messageToClient = null;
		
			
			try {
				DatagramSocket clientSocket = new DatagramSocket();
			      InetAddress IPAddress = InetAddress.getByName("localhost");
			      byte[] sendData = new byte[1024];
			      byte[] receiveData = new byte[1024];
			      
			      sendData = message.getBytes();
			      System.out.println("Sending to sequencer : "+ message );
			      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 1000);
			      clientSocket.send(sendPacket);
		    	           
			      
			      clientSocket.close();
		     
		 	          	      
		 } // end try
		  catch (Exception ex) {
		    ex.printStackTrace( );
		   
		  } //end catch
	}
	
	
	
	
	//////////////////////////CHECKING FOR SERVER CRASH///////////////////////////
	
	
	public synchronized  String checkForCrash(int port, long time) {
		
		serverCrash = false;		
		int index = 0;			
		int rm = 0;
		int[] RMsToMSG = {10, 10, 10};
		String message = null;
		String[] receiveMsg =  {"", "", "empty"};
		long[] timeArray = {0,0,0};
		int missingRM = 0;
		String replica_Str = null;
		String sequence = null;
		

		
      try {
    	
    	serverSocketCrash = new DatagramSocket(port);
        byte[] receiveData = new byte[1024];
        
        System.out.printf("Listening on udp",
                InetAddress.getLocalHost().getHostAddress(), port);      
            
        DatagramPacket receivePacket = new DatagramPacket(receiveData,
                           receiveData.length);
        
        
        //////START TIMER

        while(true)
        {
        	
        	 serverSocketCrash.receive(receivePacket);
        	 
              String sentence = new String( receivePacket.getData(), 0,
                                 receivePacket.getLength() );
              replica_Str = sentence;             
              System.out.println("RECEIVED: " + sentence);
              JsonObject obj = jsonFromString(replica_Str);
              System.out.println("index is " + index);
              //IF we want to look for different values (servertype 0)
              //Checks if message received is a message sent from a replica in the right json format
              System.out.println("server type is " + serverType);
              
              
        
              if(serverType == 1 && obj.containsKey("rm")){ //
            	  
	  
            	  System.out.println(index);
            	  //System.out.println(timeArray.length);
            	  //            	  
            	  //if(index==0) {
            	  RMlocation = obj.getString("manager_ID").substring(0,2);
            	  rm = obj.getInt("rm");
            	  sequence = obj.getString("sequenceNumber");
            	  message = obj.getString("message");
            	             	 
            	  receiveMsg[index] = message; 
            	  RMsToMSG[index] = rm;
            	  
            	  timeArray[index] = System.currentTimeMillis() - time;
            	  index++;
            	  System.out.println("check 2 index : " + index);
            	  }
              
       		
              if(index == 2) { 		  
         		  System.out.println( "Starting timer with time : " + timeArray[1]*2 );
    		  
         		  
         		  Thread t = new Thread(new Runnable() {
         			  
         			  public void run() {
         				  
         				 timerCheck(receiveMsg, timeArray[1]*2); 
         				  
         			  }
         		  });
         		  
         		  t.start();
         		 
           	 }  
  
          	  if(index == 3 ) {
          		try {
    		        serverSocketCrash.close();
    		      
    		        System.out.println("The server is shut down!");
    		    } catch (Exception e) { }
            	  }  
           
              
        }
      } catch (IOException e) {
              System.out.println(e);
      } 
      
      if(serverCrash) {
    	  
    	  if(RMsToMSG[0] == 1 && RMsToMSG[1] == 2 ) {
 			  
 			  missingRM = 3;
 		  }
 		  
 		  else if(RMsToMSG[0] == 2 && RMsToMSG[1] == 3 ) {
 			  
 			  missingRM = 1;
 		  }
 		 else if(RMsToMSG[0] == 1 && RMsToMSG[1] == 3 ) {
			  
			  missingRM = 2;
		  }
	  
    	  try {
			multicast(RMlocation, missingRM, "crash");
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      }
      
      System.out.println("1 : " + receiveMsg[0]);
      System.out.println("2 : " + receiveMsg[1]);
      System.out.println("3 : " + receiveMsg[2]);
      
    
      //compare messages
     
      
     if(receiveMsg[0].equals(receiveMsg[1])) {
	
		 message = receiveMsg[0];
	 }  
     
     
   
     
     
     
     System.out.println("Sending to Client : "+ message);
     
     crashIndex = 2;
     return message; 
						
			}
//////////////////////////END CHECKING FOR SERVER CRASH///////////////////////////
	
	
	public void timerCheck(String[] receiveMsg, long time) {
		
			Timer timer = new Timer();	 
    		  System.out.println( "Starting timer with time : " + time );
    		  
      		
      		 
      		TimerTask task = new TimerTask() {
      			  
				@Override
      			  public void run() {
      				  	if(receiveMsg[2].equals("empty")) {
      			         System.out.println("EXECUTING TIMER TASK");       				             				
      					 serverCrash = true;
      					 serverSocketCrash.close();}
      						
      			  }      					
								   
      			  
      			}; //timeArray[firstTwoReception[1]] );
      			
      			
      			
      			timer.schedule(task, time);
      			
	
	}
	
	
	
	
	//////////////////CHECKS FOR WRONG VALUE FROM A REPLICA//////////////////////////////////////////
	
	//receives json string from replica and compares results	
	public synchronized  String receiveFromReplica(int port) { 
		
		String location = null;
		int index = 0;		
		int rm = 0;
		String message = null;
		String[] receiveMsg = new String[3];
		String replica_Str = null;
		String sequence = null;
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
              replica_Str = sentence;             
              System.out.println("RECEIVED: " + sentence);
              JsonObject obj = jsonFromString(replica_Str);
              
              //IF we want to look for different values (servertype 0)
              //Checks if message received is a message sent from a replica in the right json format
              if(serverType == 0 && obj.containsKey("sequenceNumber")){ //
            	  
            	  
            	  // 
            	  
            	  //if(index==0) {
            	  location = obj.getString("manager_ID").substring(0,2);
            	  rm = obj.getInt("rm");
            	  sequence = obj.getString("sequenceNumber");
            	  message = obj.getString("message");
            	  receiveMsg[rm-1] = message;
            	  index++;
            	 
            	  
            	 // }
            	  /*
            	  else if(location.equals(obj.getString("manager_ID").substring(0,2)) && sequence.equals(obj.getString("sequenceNumber")) ) {
           		 
                	  message = obj.getString("message");
                	  receiveMsg[rm-1] = message;
                	  index++;  
                	 
                	  System.out.println("RECEIVED: " + sentence ); 
            	  }*/
            	             	            	  
            	  
            	  if(index == 3 ) {
            		  
            		  try {
            		        serverSocket.close();
            		        System.out.println("The server is shut down!");
            		    } catch (Exception e) { /* failed */ }
            	  }
            	                                                	
              
                        
              }
              
        }
      } catch (IOException e) {
              System.out.println(e);
      } 
      
      
      System.out.println("1 : " + receiveMsg[0]);
      System.out.println("2 : " + receiveMsg[1]);
      System.out.println("3 : " + receiveMsg[2]);
      
     //CHECK FOR SOFTWARE FAILURE 
    
      //compare messages
     boolean[] areEqual = {false, false, false}; //i0 is comparing msg1 to msg2, i1  is comparing msg1 to msg3, and i3 is comparing msg2 to msg3
      
     if(receiveMsg[0].equals(receiveMsg[1])) {
		 areEqual[0] = true;
		 message = receiveMsg[0];
	 }  
     if(receiveMsg[0].equals(receiveMsg[2])) {
		 areEqual[1] = true;
		 message = receiveMsg[0];
	 }  
     if(receiveMsg[1].equals(receiveMsg[2])) {
		 areEqual[2] = true;	
		 message = receiveMsg[1];
	 }  
     
     //rm1 is faulty
     if( areEqual[0] == false && areEqual[1] == false &&  areEqual[2] == true ) {
    	 
    	
    	 try {
			multicast(location, 1, "wrongValue");
			System.out.println("rm1 is faulty");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }
     
     
   //rm2 is faulty
     else if( areEqual[0] == false && areEqual[2] == false &&  areEqual[1] == true ) {
    	 
    	 try {
			multicast(location, 2, "wrongValue");
			System.out.println("rm2 is faulty");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }
     
   //rm3 is faulty
     else if( areEqual[1] == false && areEqual[2] == false &&  areEqual[0] == true ) {
    	 
    	 try {
			multicast(location, 3, "wrongValue");
			
			System.out.println("rm3 is faulty");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }
     
     
     
     System.out.print("Sending to Client : "+ message);
     
     
     return message; 
    	 
     
     }
      
      
      
      
      
      
      
      
      
    
	
	
	
	
	
		
	public String getIP() {
		try {
			serverSocket = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return serverSocket.getLocalAddress().getHostAddress();
	}
		
	
	public long timeMillisecond() {
		
		
		
		
		
		return System.currentTimeMillis();
	}
		
	private  JsonObject jsonFromString(String jsonObjectStr) {

	    JsonReader jsonReader = Json.createReader(new StringReader(jsonObjectStr));
	    JsonObject object = jsonReader.readObject();
	    jsonReader.close();

	    return object;
	}
	
	//mutlicast to RM's which Replica and the error type
	public synchronized void  multicast(String location, int rm, String error) throws IOException {
		
		
		String jsonString =  Json.createObjectBuilder()
	            //.add("methodName", this.methodName)
				.add("location", location)
				.add("rm",rm)				
	            .add("errorType", error)
				//.add("args", Json.createObjectBuilder().add("firstName", "Bob"))	            
	            .build()
	            .toString();	
		
		
		
		
        socket = new DatagramSocket();
        group = InetAddress.getByName("230.0.0.1");//which ever we decide on 
        buf = jsonString.getBytes();
 
        DatagramPacket packet  = new DatagramPacket(buf, buf.length, group, 3001);
        socket.send(packet);
        socket.close();
        
        System.out.println("mutlicasting error : " +jsonString);
        
        
        
    }
	

	
	 public void shutdown() {
	     orb.shutdown(false);
	   }
}
