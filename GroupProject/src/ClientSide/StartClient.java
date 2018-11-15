package ClientSide;




import ServerCenterApp.*;

import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;
import java.io.*;
import java.sql.Timestamp;
import java.util.*;
 
public class StartClient {
 
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      try {
    	  
    	String Manager_ID;
	    ORB orb = ORB.init(args, null);
	    org.omg.CORBA.Object objRef =   orb.resolve_initial_references("NameService");
	    NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
	    ServerCenterIDL request = (ServerCenterIDL) ServerCenterIDLHelper.narrow(ncRef.resolve_str("FrontEnd"));
	    
	    //ServerCenterIDL CA_Server = (ServerCenterIDL) ServerCenterIDLHelper.narrow(ncRef.resolve_str("CA"));
	    //ServerCenterIDL US_Server = (ServerCenterIDL) ServerCenterIDLHelper.narrow(ncRef.resolve_str("US"));
	    //ServerCenterIDL UK_Server = (ServerCenterIDL) ServerCenterIDLHelper.narrow(ncRef.resolve_str("UK"));
	    
	    
	    boolean exit = false;
		  
		  
		   
		   Scanner scanner = new Scanner( System.in );
		   
		   while(true) {
			   exit = false;   
			   
		   
		   
		   
		   System.out.println("Please input your manager ID");
		   
		   String manager_ID = scanner.next();
		   String manager_Location = manager_ID.substring(0, 2).toUpperCase();
		   
		   
		   
		   ServerCenterIDL h = request;
		   
		   
		   
		   
		   //check managerID see if it is valid
		   if(validate_ID(manager_Location)) {
			   Manager_ID = manager_ID; //if valid
			   
			   printToLogs(Manager_ID, "Log in");
			   
			   
			  
			   
			   
			  
			   
			   
			   
			   while(!exit) {
				   
				   
			   
			   
			   
			   
	             
			   String recordID;
			   String fieldName;
			   String newValue;
			   String firstName;
			   String lastName;
			   int employeeID;
			   String mailID;
			   String projectID;
			   String clientName;
			   String projectName;
			   String location;
			   System.out.println("Select one of the following options:\n");
			   System.out.println("1. Create a manager record.\n2. Create an employe record.\n3. Get the total number of records from all CenterServers.\n4. Edit a record.\n5. Transfer Record\n6. Re-enter Manager ID ");
			   int option = scanner.nextInt();
			   
			   
			   
			   switch (option) {
			   
			   
	           case 1:  
	        	   
	        	  
	        	   
	        	   
	        	   System.out.println("createMRecord(String firstName, String lastName, int employeeID, String mailID, String projectID, String clientName, String projectName, String location)");
	           
	           System.out.println("Insert firstName");
	           firstName = scanner.next();
	           System.out.println("Insert Last Name");
	           lastName = scanner.next();
	           System.out.println("Insert employee ID (Int)");
	           employeeID = scanner.nextInt();
	           System.out.println("Insert mailID");
	           mailID = scanner.next();
	           System.out.println("Insert projectID");
	           projectID = scanner.next();
	           System.out.println("Insert client name");
	           clientName = scanner.next();
	           System.out.println("Insert project name");
	           projectName = scanner.next();
	           //System.out.println("Insert location");
	           //location = scanner.next();
	           
	           if(isAlpha(firstName) && isAlpha(lastName))
	           {
	        	   printToLogs(manager_ID, h.createMRecord("createMRecord", Manager_ID, firstName, lastName, employeeID, mailID, projectID, clientName, projectName, manager_Location) ); //h.createMRecord(firstName, lastName, employeeID, mailID, projectID, clientName, projectName, location);
	 	          
				   
		            
	           }
	           
	           else {
	        	   
	        	   
	        	   System.out.println("INPUT ERROR - INVALID FIRSTNAME/LASTNAME");
	        	   
	           }
	           
	           
	           
	           
	           
	           
	           
	           
	                    break;
	                    
	           case 2:  
	        	   		
	        	   
	        	   
	        	   		System.out.println("createERecord(String firstName, String lastName, int employeeID, String mailID, String projectID)");
	                    
	                    
	                    
	                    System.out.println("Insert firstName");
	                    firstName = scanner.next();
	                    System.out.println("Insert Last Name");
	                    lastName = scanner.next();
	                    System.out.println("Insert employee ID (Int)");
	                    employeeID = scanner.nextInt();
	                    System.out.println("Insert mailID");
	                    mailID = scanner.next();
	                    System.out.println("Insert projectID");
	                    projectID = scanner.next();                   
	                    
	                    
	                    if(isAlpha(firstName) && isAlpha(lastName)) {
	                    printToLogs(manager_ID,h.createERecord("createERecord", Manager_ID, firstName, lastName, employeeID, mailID, projectID));
	                   
	         		   	//System.out.println(h.prinDataClient());}
	                    }
	                    else {
	     	        	   
	     	        	   
	     	        	   System.out.println("INPUT ERROR - INVALID FIRSTNAME/LASTNAME");
	     	        	   
	     	           }
	                    
	                    
	                    
	                    break;
	                    
	                    
	                    
	                    
	                    
	                    
	                    
	           case 3:  
	        	   String recordsCount = h.getRecordCounts("getRecordCounts", Manager_ID);
	        	   
	        	  
	        	   printToLogs(manager_ID, recordsCount);
	        	   
	        	   
	        	   
	                    break;
	           case 4:  
	        	   
	        	   
	    		   
	        	   
	        	   
	        	   
	        	   System.out.println("editRecord(String recordID, String fieldName, String newValue)");
	        	   System.out.println("Insert recordID");
	               recordID = scanner.next();
	               System.out.println("Insert fieldName (mailID, projectID, projectName, projectClient or location) FOR MR || Insert fieldName (mailID, projectID) FOR ER ");
	               fieldName = scanner.next();
	               System.out.println("Insert newValue");
	               newValue = scanner.next();
	               
	               printToLogs(manager_ID, h.editRecord("editRecord", Manager_ID,recordID, fieldName, newValue));
	               
	              
	    		   //System.out.println(h.prinDataClient());
	        	   
	               break;
			   
	           case 5: 
	        	   String server;
	        	   
	        	   System.out.println("TransferRecord(String RecordID, string centralServer)");
	        	   System.out.println("Insert recordID");
	        	   recordID = scanner.next();
	        	   System.out.println("Insert Sever name (CA, US, UK ");
	        	   server = scanner.next().toUpperCase();
	        	   
	        	   if(server.equals("CA") || server.equals("US") || server.equals("UK")) {	
	        	   
	        	   printToLogs(manager_ID, h.transferRecord("transferRecord", manager_ID, recordID, server));
	        	   
	        	  // System.out.println(h.prinDataClient());
	        	   }
	        	   
	        	   
	        	   
	        	   
	           break;   
			   
			   
		   case 6:  exit = true;
		   printToLogs(Manager_ID, "Log out");
	           break;
		   }}
	  
	  
	  }
			   		   
		   }
		  
		   
            
            	
            }
       
       catch (Exception e) {
          System.out.println("Client exception: " + e);
	  e.printStackTrace();
       }
 
    }
    
    public static boolean validate_ID(String ID) {
		   
 	   if(ID.equals("CA") || ID.equals("US") || ID.equals("UK")) {		  
 		  System.out.println("Manager ID is valid");
 		   return true;		   
 	   }			   
 	   else {		   
 		   System.out.println("Manager ID is invalid, please try again!");		   
 		   return false;
 	   }
 		
 	}
public synchronized static void printToLogs(String ManagerID, String info) {
		
		
		
		
		try
		{
			String filename = ManagerID+".txt";
			
			
			
			
		    
		    FileWriter fw = new FileWriter(filename,true); //the true will append the new data
		    fw.write("\n\n----------"+getTimeStamp()+"----------\n"+
		    		"ID: "+ ManagerID+"\n" +
		    		info +"\n"+
		    		"----------"+getTimeStamp()+"----------\n\n"
		    		
		    		
		    		);//appends the string to the file
		    
		    
		    
		    
		    
		    
		    
		    fw.close();
		    
		    System.out.println("\n\n----------"+getTimeStamp()+"----------\n"+
		    		"ID: "+ ManagerID+"\n" +
		    		info +"\n"+
		    		"----------"+getTimeStamp()+"----------\n\n");
		}
		catch(IOException ioe)
		{
		    System.err.println("IOException: " + ioe.getMessage());
		}	
		
	}
   
public static boolean isAlpha(String name) {
    char[] chars = name.toCharArray();

    for (char c : chars) {
        if(!Character.isLetter(c)) {
            return false;
        }
    }

    return true;
}
   
   
   
   
   
   public static String getTimeStamp()
	{
	String timeStamp;
	Date date= new Date();

	long time = date.getTime();
	Timestamp ts = new Timestamp(time);
	timeStamp = ts.toString();
			

	return timeStamp;
	}
    
    
    
    
 
}