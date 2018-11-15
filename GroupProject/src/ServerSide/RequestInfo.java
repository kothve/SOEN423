package ServerSide;
import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import javax.json.Json;
import javax.json.JsonReader;
import ServerCenterApp.ServerCenterIDLPOA;

import java.util.Properties;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.rmi.*;
import java.rmi.server.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;



/**
 * This class implements the remote interface SomeInterface.
 */

public class RequestInfo  extends ServerCenterIDLPOA  {
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
		
	private ORB orb;
	
	RequestInfo() {
	}
	
	 public void setORB(ORB orb_val) {
		    orb = orb_val; 
		  }

	
	public String createMRecord(String methodName, String manager_ID, String firstName, String lastName, int employeeID,
			String mailID, String projectID, String clientName, String projectName, String location) {
		
		
		
		
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
		
		
		
		
		// TODO Auto-generated method stub
		return info;
	}

	
	public String getRecordCounts(String methodName, String manager_ID) {
		
		this.methodName = methodName;
		this.manager_ID = manager_ID;
		
		
		info = makeJSON();
		// TODO Auto-generated method stub
		
		//sends info to sequencer
		to_Sequencer_UDP(1000, info );
		
		
		
		return info;
	}

	
	public String createERecord(String methodName, String manager_ID, String firstName, String lastName, int employeeID,
			String mailID, String projectID) {
		
		this.methodName = methodName;
		this.manager_ID = manager_ID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.employeeID = employeeID;
		this.mailID = mailID;
		this.projectID = projectID;
		
		
		
		
		
		info = makeJSON();
		
		//sends info to sequencer
				to_Sequencer_UDP(1000, info );
		
		
		
		
		// TODO Auto-generated method stub
		return info;
	}

	
	public String transferRecord(String methodName, String manager_ID, String recordID, String remoteCenterServer) {
		// TODO Auto-generated method stub
		this.methodName = methodName;
		this.manager_ID = manager_ID;
		this.recordID = recordID;
		this.remoteCenterServer = remoteCenterServer;
		
		
		
		
		info = makeJSON();
		
		//sends info to sequencer
				to_Sequencer_UDP(1000, info );
		
		// TODO Auto-generated method stub
		return info;
	}

	
	public String editRecord(String methodName, String manager_ID, String recordID, String fieldName, String newValue) {
		// TODO Auto-generated method stub
		this.methodName = methodName;
		this.manager_ID = manager_ID;
		this.recordID = recordID;
		this.fieldName = fieldName;
		this.newValue = newValue;
		
		
		
		
		
		
		
		info = makeJSON();
		
		//sends info to sequencer
				to_Sequencer_UDP(1000, info );
		
		
		
		// TODO Auto-generated method stub
		return info;
	}
	
	
	
	
	//TRANSFORMS THE ARGUMENTS INTO A JSON OBJ
	public String makeJSON() {
		
		
	String jsonString = "";	
	String method = this.methodName;
	
	
	if(method.equals("createMRecord")) {
		
	
		jsonString = Json.createObjectBuilder()
	            .add("methodName", this.methodName)
	            .add("manager_ID", this.manager_ID)
	            .add("firstName", this.firstName)
	            .add("lastName", this.lastName)
	            .add("employeeID", this.employeeID)
	            .add("mailID", this.mailID)
	            .add("projectID", this.projectID)
	            .add("clientName", this.clientName)
	            .add("projectName", this.projectName)
	            .add("location", this.location)
	            .build()
	            .toString();	
		
		
		
		
			
		
		
		
		
		
	}
		
	else if(method.equals("createERecord")) {
		
		jsonString = Json.createObjectBuilder()
	            .add("methodName", this.methodName)
	            .add("manager_ID", this.manager_ID)
	            .add("firstName", this.firstName)
	            .add("lastName", this.lastName)
	            .add("employeeID", this.employeeID)
	            .add("mailID", this.mailID)
	            .add("projectID", this.projectID)	       
	            .build()
	            .toString();		
		
	}	
	else if(method.equals("getRecordCounts")) {
		
		
		jsonString = Json.createObjectBuilder()
	            //.add("methodName", this.methodName)
				.add("methodName", "getRecordCounts")
	            .add("manager_ID", "CA10001")
	            .build()
	            .toString();	
		
		
		
	}		
	else if(method.equals("editRecord")) {
		jsonString = Json.createObjectBuilder()
		.add("methodName", this.methodName)
        .add("manager_ID", this.manager_ID)
        .add("recordID", this.recordID)
        .add("fieldName", this.fieldName)
        .add("newValue", this.newValue)
        .build()
        .toString();	
		
		
	}		
	else if(method.equals("transferRecords")) {
		
		jsonString = Json.createObjectBuilder()
	            .add("methodName", this.methodName)
	            .add("manager_ID", this.manager_ID)
	            .add("recordID", this.recordID)
	            .add("remoteCenterServer", this.remoteCenterServer)
	            .build()
	            .toString();
		
		
		
	}
	
	
	
	
	return jsonString;		
		
	}//END METHOD MAKEJSON
	
	
	public void to_Sequencer_UDP(int port, String message) {
		
		
		
		
			
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
				
			
			
			
			
			
			
			}//end get records
		
		
		
		
		
		
		
	
	
	
	

	
	 public void shutdown() {
	     orb.shutdown(false);
	   }
}
