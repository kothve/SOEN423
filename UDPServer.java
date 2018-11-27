package ServerSide;

import java.io.*;
import java.net.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import org.json.JSONException;
import org.json.JSONObject;



public class UDPServer extends Thread {

    int totalNumberOfRecords = 6;
    int rm;
    String location;
    int numberOfRecords;
    private Map<Integer, String> holdingQueue;
    HashMap<Character, ArrayList<Records>> database;
    private int sequenceNumber = 0;
    UDP_Connect UDP_Connect_to_other_replicas;
    int RMError;
    String errorType;
    boolean makeError = false;
    

    int desiredPort;
    String currentLocation;

    public UDPServer(int port, String location,  HashMap<Character, ArrayList<Records>> newReplica, int rm, int seq, boolean makeError ) {
        this.desiredPort = port;
        this.currentLocation = location;
        this.database = newReplica;
        this.rm = rm;
         RMError = 0;
         this.sequenceNumber = seq+1;
         
         this.makeError = makeError;
         UDP_Connect_to_other_replicas = new UDP_Connect(port, newReplica, getHashSize(newReplica)); 
         UDP_Connect_to_other_replicas.start();
         
         
    }

    public UDPServer(int port, String location, boolean makeError) {
    	
    	 this.desiredPort = port;
         this.currentLocation = location;
         
        
        	 
        this.makeError = makeError;
         
        
         HashMap<Character, ArrayList<Records>> database  = new HashMap();
         
         if(port == 6050 || port == 6051 || port == 6052) {
         	
         	rm = 1;
     	
         }
         else if(port == 6053 || port == 6054 || port == 6055){
         	
         	rm = 2;
         }
         else if(port == 6056 || port == 6057 || port == 6058){
         	
         	rm = 3;
         }
         
         
         
         
         if(currentLocation.equals("CA")) {

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
             UDP_Connect_to_other_replicas = new UDP_Connect(port, database, numberOfRecords); 
             UDP_Connect_to_other_replicas.start();

         }

         else if(currentLocation.equals("US")) {

             ArrayList<Records> addNewArray1 = new ArrayList<Records>();
             database.put('o', addNewArray1);
             database.get('o').add(new Records( "MR4" , " Dave ", 	" Oboto ",	 321321  , " obotoz@live.ca ", " P1654452 ", " Canopy ", " A1 " ,"US"));

             this.numberOfRecords = 1;

             this.database = database;
             UDP_Connect_to_other_replicas = new UDP_Connect(port, database, numberOfRecords); 
             UDP_Connect_to_other_replicas.start();
         }
         else if(currentLocation.equals("UK")) {

             ArrayList<Records> addNewArray1 = new ArrayList<Records>();
             ArrayList<Records> addNewArray2 = new ArrayList<Records>();
             database.put('a', addNewArray1);
             database.get('a').add(new Records( "MR5" , " Alexia ", 	" Alena ",	 432412  , " a.alena@live.ca ", " P1632 ", " ACB ", " A1 " ,"UK"));
             database.put('c', addNewArray2);
             database.get('c').add(new Records( "ER6" , " Bob ", 	" Cotton ",	 5432412  , " b.c@live.ca ", " P163223 ", " Namaste tech ", " A1 " ,"UK"));

             numberOfRecords = 2;
             this.database = database;
             UDP_Connect_to_other_replicas = new UDP_Connect(port, database, numberOfRecords); 
             UDP_Connect_to_other_replicas.start();
         }

         prinData();
    	
    	
    	
    }

    //Constructor
    public UDPServer(int port, String location)
    {	
    	this.makeError = false;
        this.desiredPort = port;
        this.currentLocation = location;

        HashMap<Character, ArrayList<Records>> database  = new HashMap();
        RMError = 0;
        if(port == 6050 || port == 6051 || port == 6052) {
        	
        	rm = 1;
    	
        }
        else if(port == 6053 || port == 6054 || port == 6055){
        	
        	rm = 2;
        }
        else if(port == 6056 || port == 6057 || port == 6058){
        	
        	rm = 3;
        }
        
        
        
        if(currentLocation.equals("CA")) {

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
            UDP_Connect_to_other_replicas = new UDP_Connect(port, database, numberOfRecords); 
            UDP_Connect_to_other_replicas.start();

        }

        else if(currentLocation.equals("US")) {

            ArrayList<Records> addNewArray1 = new ArrayList<Records>();
            database.put('o', addNewArray1);
            database.get('o').add(new Records( "MR4" , " Dave ", 	" Oboto ",	 321321  , " obotoz@live.ca ", " P1654452 ", " Canopy ", " A1 " ,"US"));

            this.numberOfRecords = 1;

            this.database = database;
            UDP_Connect_to_other_replicas = new UDP_Connect(port, database, numberOfRecords); 
            UDP_Connect_to_other_replicas.start();
        }
        else if(currentLocation.equals("UK")) {

            ArrayList<Records> addNewArray1 = new ArrayList<Records>();
            ArrayList<Records> addNewArray2 = new ArrayList<Records>();
            database.put('a', addNewArray1);
            database.get('a').add(new Records( "MR5" , " Alexia ", 	" Alena ",	 432412  , " a.alena@live.ca ", " P1632 ", " ACB ", " A1 " ,"UK"));
            database.put('c', addNewArray2);
            database.get('c').add(new Records( "ER6" , " Bob ", 	" Cotton ",	 5432412  , " b.c@live.ca ", " P163223 ", " Namaste tech ", " A1 " ,"UK"));

            numberOfRecords = 2;
            this.database = database;
            UDP_Connect_to_other_replicas = new UDP_Connect(port, database, numberOfRecords); 
            UDP_Connect_to_other_replicas.start();
        }

        prinData();

    }

  
	public void run()
    {

		 byte[] buffer = new byte[1024];
	        String msg = "";
	        MulticastSocket socket = null;
	        InetAddress group = null;

	        try {
	            socket = new MulticastSocket(3000);
	            group = InetAddress.getByName("230.0.0.0");
	            socket.joinGroup(group);
	            
	           

	            while (true) {
	                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
	                socket.receive(packet);
	                msg = new String(packet.getData(), packet.getOffset(), packet.getLength());
	                
	                
	                JsonObject jsonMsg = jsonFromString(msg);

	                if (jsonMsg.containsKey("sequenceNumber")) {
	                    if (Integer.parseInt(jsonMsg.getString("sequenceNumber")) == this.sequenceNumber) {
	                        this.sequenceNumber++;
	                        
	                        if(jsonMsg.getString("manager_ID").substring(0, 2).equals(currentLocation)) {	
	                        	 System.out.println("Received : " +msg);
	                        	 
	                        	 try {
									execute(jsonMsg);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
	                        }
	                        
	                    } else if (Integer.parseInt(jsonMsg.getString("sequenceNumber")) > this.sequenceNumber) {
	                        this.holdingQueue.put(Integer.parseInt(jsonMsg.getString("sequenceNumber")), jsonMsg.toString());

	                        if (jsonMsg.containsKey("manager_ID")) {
	                            String managerId = jsonMsg.getString("manager_ID");

	                        }
	                    }
	                }
	            }
	        } catch (UnknownHostException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
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

    
    private synchronized String execute(JsonObject jsonOld) throws JSONException {
    	String message = null;
    	
    	String jsonSTR = jsonOld.toString();
    	JSONObject json = new JSONObject(jsonSTR);
    	
        if (json.has("methodName")) {
        	
            String methodName = json.getString("methodName");
            if (methodName.equals("createMRecord")) {
           	 
            	
                if (json.has("args")) {
                	
                    JSONObject methodArgs = json.getJSONObject("args");
                    if (
                            
                                    methodArgs.has("firstName") &&
                                    methodArgs.has("lastName") &&
                                    methodArgs.has("employeeID") &&
                                    methodArgs.has("mailID") &&
                                    methodArgs.has("projectID") &&
                                    methodArgs.has("clientName") &&
                                    methodArgs.has("projectName") &&
                                    methodArgs.has("location")
                    ) {
                    	System.out.println("Executing method create");
                    	message = createMRecord(
                                json.getString("manager_ID"),
                                methodArgs.getString("firstName"),
                                methodArgs.getString("lastName"),
                                methodArgs.getInt("employeeID"),
                                methodArgs.getString("mailID"),
                                methodArgs.getString("projectID"),
                                methodArgs.getString("clientName"),
                                methodArgs.getString("projectName"),
                                methodArgs.getString("location")
                        );
                    }

                }
            }else if (methodName.equals("createERecord")) {
            	JSONObject methodArgs = json.getJSONObject("args");
                if (
                       
                                methodArgs.has("firstName") &&
                                methodArgs.has("lastName") &&
                                methodArgs.has("employeeID") &&
                                methodArgs.has("mailID") &&
                                methodArgs.has("projectID")
                ) {
                	message =  createERecord(
                            json.getString("manager_ID"),
                            methodArgs.getString("firstName"),
                            methodArgs.getString("lastName"),
                            methodArgs.getInt("employeeID"),
                            methodArgs.getString("mailID"),
                            methodArgs.getString("projectID")
                    );
                }

            } else if (methodName.equals("getRecordCounts")) {
            	
                //JsonObject methodArgs = json.getJsonObject("args");
                
               // if (methodArgs.containsKey("manager_ID")) {
                	message = getRecordCounts(json.getString("manager_ID"));
                	System.out.println(getRecordCounts(json.getString("manager_ID")));
               // }
            } else if (methodName.equals("editRecord")) {
            	JSONObject methodArgs = json.getJSONObject("args");
                if (
                       
                                methodArgs.has("recordID") &&
                                methodArgs.has("fieldName") &&
                                methodArgs.has("newValue")
                ) {
                	message =  editRecord(
                            json.getString("manager_ID"),
                            methodArgs.getString("recordID"),
                            methodArgs.getString("fieldName"),
                            methodArgs.getString("newValue")
                    );
                }
            } else if (methodName.equals("transferRecord")) {
            	JSONObject methodArgs = json.getJSONObject("args");
                if (
                       
                                methodArgs.has("recordID") &&
                                methodArgs.has("remoteCenterServer")
                ) {
                	message = transferRecord(
                            json.getString("manager_ID"),
                            methodArgs.getString("recordID"),
                            methodArgs.getString("remoteCenterServer")
                    );
                }
            }

        }
        
        
        System.out.println("make error is " + makeError);
        to_FE_UDP(message, json.getString("manager_ID"));
       
        return message;
    }
    
    ////////////////////////////////////////A2 METHODS//////////////////////////////////////////////
    


    /////////////////////////CREATE MR RECORD////////////////////////////////////////////////////////
   
 	   


	/////////////////////////////////////////////////////////////////
 	  public synchronized String createMRecord(String Manager_ID, String firstName, String lastName, int employeeID, String mailID, String projectID, String clientName, String projectName, String location)   {
 		 
 		   String result = "Success";
 		   
 		  // numberOfRecords++;
 		      
 	       //UDP_Server.numberOfRecords++;
 		   char dbCharacter = Character.toLowerCase(lastName.charAt(0));

 		  
 		   try {
 				totalNumberOfRecords = getAllRecordCounts()+1;
 			} catch (Exception e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
 		    //totalNumberOfRecords++;
 		    String number = Integer.toString(totalNumberOfRecords);
 			   
 			   
 			  System.out.println("TOTAL NUMBER OF RECORDS" +number);  
 		   
 		   
 		   
 		   
 		   
 			   Records recordCreated = new Records("MR"+number, firstName, lastName, employeeID,  mailID, projectID,  clientName,projectName, location);
 		   
 			   Set<Character> keySet = this.database.keySet();
 			   
 			   java.util.Iterator<Character> keySetIterator = keySet.iterator();
 			   
 			   
 			   
 			   if(this.database.containsKey(dbCharacter)) {
 				   
 				   
 			   
 			   while (keySetIterator.hasNext()) {
 				   
 				   
 				   Character key = keySetIterator.next();
 				   if (dbCharacter == key) {
 					   
 					   this.database.get(key).add(recordCreated);
 					   
 					   
 					    
 				   }
 				   
 			   }
 	   }else {
 		   
 		   ArrayList<Records> addNewArray = new ArrayList<Records>();
 		   
 		   this.database.put(dbCharacter, addNewArray);
 		   this.database.get(dbCharacter).add(recordCreated);
 		
 		   
 		   
 	   }
 		   		   		   	   
 		   
 			    
 			  
  			  UDP_Connect_to_other_replicas.numberOfRecords = getHashSize(database); 
  			  numberOfRecords =  getHashSize(database);	   
 			   
 			   
 	       
 	       prinData(); 
 	    printToLogs("Manager Record has been added succesfully\n" + recordCreated.display_String());            	   	   
 		return "Manager Record has been added succesfully\n" + recordCreated.display_String();	   	
 	   }
 	   
 	 /////////////////////////////////////////////////////////////////////////////////////  
 	   
 	
 		
 	
 	   ////////////////////CREATE ERECORD///////////////////////
 	   
 	   public synchronized String createERecord(String Manager_ID, String firstName, String lastName, int employeeID, String mailID, String projectID)  {
 		   
 		    
 		   
 		   //numberOfRecords++;
 		  // UDP_Server.numberOfRecords++;
 		   try {
 				totalNumberOfRecords = getAllRecordCounts()+1;
 			} catch (Exception e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
 		    
 		   String result = "Success";
 		   
 		   String number = Integer.toString(totalNumberOfRecords);
 		   
 		   //totalNumberOfRecords++;
 		   System.out.println("TOTAL NUMBER OF RECORDS" +number);
 		   
 		   
 		   
 		   
 		   
 		   
 		   char dbCharacter = Character.toLowerCase(lastName.charAt(0));
 		   
 		   	
 		   
 			   Records recordCreated = new Records("ER"+number, firstName, lastName, employeeID,  mailID, projectID);
 		   
 			   Set<Character> keySet = this.database.keySet();
 			   
 			   java.util.Iterator<Character> keySetIterator = keySet.iterator();
 			   
 			   
 			   
 			   if(this.database.containsKey(dbCharacter)) {
 				   
 				   
 			   
 			   while (keySetIterator.hasNext()) {
 				   
 				   
 				   Character key = keySetIterator.next();
 				   if (dbCharacter == key) {
 					   
 					   this.database.get(key).add(recordCreated);
 					   
 					   
 					    
 				   }
 				   
 			   }
 	   }else {
 		   
 		   ArrayList<Records> addNewArray = new ArrayList<Records>();
 		   
 		   this.database.put(dbCharacter, addNewArray);
 		   this.database.get(dbCharacter).add(recordCreated);
 		
 		   
 		   
 	   }
 		   		   		   	   
 		   
 	       //counter = numberOfRecords;
 			  
 			   
 			  UDP_Connect_to_other_replicas.numberOfRecords = getHashSize(database); 
 			  numberOfRecords =  getHashSize(database);
 		   
 		   
 		   
 		   
 	       
 	       prinData();   
 	    printToLogs("Employee Record has been added succesfully\n" + recordCreated.display_String());            	   	   
 		return "Employee Record has been added succesfully\n" + recordCreated.display_String();	   	
 		
 		   }
 	   
 	   /////////////////////////////////////////////////////////////////////////////
 	   
////////////////////////////////////////////////GET RECORD COUNTS//////////////////////////////////
 	   public synchronized String getRecordCounts(String Manager_ID)  {
 		   
 		   
 		  String CA = null;
 		  String US = null;
 		  String UK = null;
 		  String all_Records = "Error";
 		   
 		   
 		   if(currentLocation.equals("CA")) {
 			  CA = Integer.toString(getHashSize(database)); 
 			  
 			  if(rm == 1 ) {
 				 US = UDP_Connect_to_other_replicas .getRecords(6051);
 	 			 
 	 			 UK = UDP_Connect_to_other_replicas .getRecords(6052);
 	 			  
 	 			
 				  
 			  }
 			  else if(rm == 2) {
 				 US = UDP_Connect_to_other_replicas .getRecords(6054);
 	 			 
 	 			 UK = UDP_Connect_to_other_replicas .getRecords(6055);  
 				  
 				  
 			  }
 			  
 			  else if(rm == 3) {
 				  
 				 US = UDP_Connect_to_other_replicas .getRecords(6057);
 	 			 
 	 			 UK = UDP_Connect_to_other_replicas .getRecords(6058);
 	 			  
 	 			
 				  
 				  
 				  
 			  }
	  
 			  all_Records = "CA "+CA+", US "+US+", UK "+UK;
 			 
 			   
 		   }
 		   else if(currentLocation.equals("US")) {
 			   
 			   	US  = Integer.toString(getHashSize(database)); 
 				
 			   	
 			   if(rm == 1 ) {
 			   	CA = UDP_Connect_to_other_replicas.getRecords(6050);
			
 				UK = UDP_Connect_to_other_replicas.getRecords(6052);}
 			   
 			  else if(rm == 2) {
  				 CA = UDP_Connect_to_other_replicas.getRecords(6053);
  	 			 
  	 			 UK = UDP_Connect_to_other_replicas.getRecords(6055);  
  				  
  				  
  			  }
 			   
 			 else if(rm == 3) {
  				 CA = UDP_Connect_to_other_replicas.getRecords(6056);
  	 			 
  	 			 UK = UDP_Connect_to_other_replicas.getRecords(6058);  
  				  
  				  
  			  }
 			   
 				  all_Records = "CA "+CA+",US "+US+", UK "+UK;
 				  
 		   }
 		   
 		   else if(currentLocation.equals("UK")) {
 			   	UK =  Integer.toString(getHashSize(database)); 
 			   	
 			   	
 			   	
 			   if(rm == 1 ) {
 	 			   	CA = UDP_Connect_to_other_replicas .getRecords(6050);
 				
 	 				US = UDP_Connect_to_other_replicas .getRecords(6051);}
 	 			   
 	 			  else if(rm == 2) {
 	 				CA = UDP_Connect_to_other_replicas.getRecords(6053);
 	  	 			 
 	  	 			 US = UDP_Connect_to_other_replicas .getRecords(6054);  
 	  				  
 	  				  
 	  			  }
 	 			   
 	 			 else if(rm == 3) {
 	  				 US = UDP_Connect_to_other_replicas .getRecords(6057);
 	  	 			 
 	  	 			 CA = UDP_Connect_to_other_replicas .getRecords(6056);  
 	  				  
 	  				  
 	  			  }
 	 			   
 	 				 
 			   
 				  all_Records = "CA "+CA+",US "+US+", UK "+UK;
 				  
 		   }
 		   
 		     
 		   
 		  printToLogs(all_Records);  
 		return all_Records ;
 	      // code to be supplied
 	   }

 	   
 	  ////////////////////////////////////////////////////////////////////////////////////////////////
 	   
 	   
 	   
 	   
 	   
 	   public synchronized String editRecord(String Manager_ID ,String recordID, String fieldName, String newValue) {
 		   
 		   int loopCounter = 0;
 		   //find recordID
 		   String type = recordID.substring(0, 2);
 		   String get;
 		   String message = "INVALID REQUEST";
 		   
 		   //if(type.equals("ER")) {
 			   
 			   
 		   
 		   
 		   
 		   
 		   
 		   Iterator it =  this.database.entrySet().iterator();
 		    while (it.hasNext()) {
 		        Map.Entry pair = (Map.Entry)it.next();

 		        ArrayList<Records> value = (ArrayList<Records>) pair.getValue();
 		        //System.out.println("[" + pair.getKey() + "] = ");
 		        for(int i = 0; i < value.size(); i++) {
 		        	
 		        	
 		        	
 		        	if(value.get(i).getRecordsID().equals(recordID)) {
 		        		
 		        		loopCounter++;
 		        		
 		        		if(type.equals("MR")) {
 		        				        				        				        				        			        			        		
 		        		
 		        		
 		        		if(fieldName.equals("mailID")) {
 		        			
 		        			value.get(i).setMailID(newValue);
 		        			message = "RECORD ID : ["+recordID+"] changed mailID to ["+newValue+"]" ; 
 		        		}
 		        		
 		        		else if(fieldName.equals("projectID")) {
 		        			
 		        			value.get(i).setProjectID(newValue);
 		        			message = "RECORD ID : ["+recordID+"] changed project ID to ["+newValue+"]" ;
 		        			
 		        		}
 		        		else if(fieldName.equals("projectClient")) {
 		        			
 		        			value.get(i).setProjectClient(newValue);
 		        			message = "RECORD ID : ["+recordID+"] changed project client to ["+newValue+"]" ;
 		        		}
 		        		else if(fieldName.equals("projectName")) {
 		        			
 		        			value.get(i).setProjectName(newValue);
 		        			message = "RECORD ID : ["+recordID+"] changed project name to ["+newValue+"]" ;
 		        		}
 		        		
 		        		else if(fieldName.equals("currentLocation") && (newValue.equals("CA") || newValue.equals("US") || newValue.equals("UK"))) {
 		        			
 		        			value.get(i).setLocation(newValue);
 		        			message = "RECORD ID : ["+recordID+"] changed location to ["+newValue+"]" ;
 		        		}
 		        		
 		        		else {
 		        			message = "INVALID REQUEST" ;
 		        		}
 		        			
 		        			
 		        		}
 		        	
 		        		else if(type.equals("ER")) {
 		        			
 		        			if(fieldName.equals("mailID")) {
 			        			
 			        			value.get(i).setMailID(newValue);
 			        			message = "RECORD ID : ["+recordID+"] changed mailID to ["+newValue+"]" ; 
 			        		}	
 		        			else if(fieldName.equals("projectID")) {
 			        			
 			        			value.get(i).setProjectID(newValue);
 			        			message = "RECORD ID : ["+recordID+"] changed project ID to ["+newValue+"]" ;
 			        			
 			        		}
 		        			else {
 		        				message = "INVALID REQUEST" ;
 		        			}
 		        			
 		        			
 		        			
 		        		}//end type MR	
 		        		
 		        		
 		        		
 		        		}//end if RECORDSrecords_ID
 		        		
 		        		
 		        		
 		        		
 		        	else if(loopCounter == 0) {
 			        	
 		        		message = "INVALID RECORD ID";
 			        	
 			        }	
 		        		
 		        	}//end for
 		        
 		        	
 		        	
 		        	
 		        	
 		        	
 		        	//projectID + " " + this.projectClient + "" + this.projectName
 		        }//end while
 		        
 		        
 		       
 		        
 		    
 	  
 	  
 		   
 		    prinData();   
 		   printToLogs(message);
 		   return message;
 		   
 	   }
 	   
 	   public synchronized  int getAllRecordCounts() {
 		    
 			  int CA =0;
 			  int US =0;
 			  int UK =0;
 			  int all_Records = 0;
 			 
 			   
 			   
 			   if(currentLocation.equals("CA")) {
 				   
 				   
 				   
 				   
 				  CA = getHashSize(database); 
 				  	 
 				  
 					
 	 			   if(rm == 1 ) {
 	 	 			   	US = Integer.parseInt(UDP_Connect_to_other_replicas.getRecords(6051));
 	 				
 	 	 				UK = Integer.parseInt(UDP_Connect_to_other_replicas.getRecords(6052));
 	 			   }
 	 	 			  else if(rm == 2) {
 	 	 				US = Integer.parseInt(UDP_Connect_to_other_replicas.getRecords(6054));
 	 	  	 			 
 	 	 				UK = Integer.parseInt(UDP_Connect_to_other_replicas.getRecords(6055)); 
 	 	  				  
 	 	  				  
 	 	  			  }
 	 	 			   
 	 	 			 else if(rm == 3) {
 	 	 				US = Integer.parseInt(UDP_Connect_to_other_replicas.getRecords(6057));
	 	  	 			 
 	 	 				UK = Integer.parseInt(UDP_Connect_to_other_replicas.getRecords(6058)); 
 	 	  				  
 	 	  				  
 	 	  			  }
 				  
			  
 				  all_Records = CA+US+UK;
 				 
 				   
 			   }
 			   else if(currentLocation.equals("US")) {
 				  US = getHashSize(database); 
 				  if(rm == 1 ) {
	 	 			   	CA = Integer.parseInt(UDP_Connect_to_other_replicas.getRecords(6050));
	 				
	 	 				UK = Integer.parseInt(UDP_Connect_to_other_replicas.getRecords(6052));
	 			   }
	 	 			  else if(rm == 2) {
	 	 				CA = Integer.parseInt(UDP_Connect_to_other_replicas.getRecords(6053));
	 	  	 			 
	 	 				UK = Integer.parseInt(UDP_Connect_to_other_replicas.getRecords(6055)); 
	 	  				  
	 	  				  
	 	  			  }
	 	 			   
	 	 			 else if(rm == 3) {
	 	 				CA = Integer.parseInt(UDP_Connect_to_other_replicas.getRecords(6056));
	 	  	 			 
	 	 				UK = Integer.parseInt(UDP_Connect_to_other_replicas.getRecords(6058)); 
	 	  				  
	 	  				  
	 	  			  }
 				   
 					all_Records = CA+US+UK;
 					  
 			   }
 			   
 			   else if(currentLocation.equals("UK")) {
 				  UK = getHashSize(database); 
 				  if(rm == 1 ) {
	 	 			   	CA = Integer.parseInt(UDP_Connect_to_other_replicas.getRecords(6050));
	 				
	 	 				US = Integer.parseInt(UDP_Connect_to_other_replicas.getRecords(6051));
	 			   }
	 	 			  else if(rm == 2) {
	 	 				CA = Integer.parseInt(UDP_Connect_to_other_replicas.getRecords(6053));
	 	  	 			 
	 	 				US = Integer.parseInt(UDP_Connect_to_other_replicas.getRecords(6054));
	 	  				  
	 	  				  
	 	  			  }
	 	 			   
	 	 			 else if(rm == 3) {
	 	 				CA = Integer.parseInt(UDP_Connect_to_other_replicas.getRecords(6056));
	 	  	 			 
	 	 				US = Integer.parseInt(UDP_Connect_to_other_replicas.getRecords(6057));
	 	  				  
	 	  				  
	 	  			  }
 				   
 					all_Records = CA+US+UK;
 					  
 			   }
 			   
 			     
 			   //System.out.println("ALL RECORDS : CA "+CA +", US "+US+", UK "+UK);
 			
 			return all_Records;
 		      // code to be supplied
 		   }
 	   
 	   
 	   
 	   
 	   public int getHashSize(HashMap<Character, ArrayList<Records>> database) {
 		   int counter = 0;
 		   
 		   Iterator it =  this.database.entrySet().iterator();
 		    while (it.hasNext()) {
 		        Map.Entry pair = (Map.Entry)it.next();

 		        ArrayList<Records> value = (ArrayList<Records>) pair.getValue();
 		       
 		        counter+=value.size();
 		       // System.out.println(counter);
 		        }
 		   
 		   
 		   
 		   return counter;
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


 	   public void printToLogs(String info) {
 			
 			
 			
 			
 			try
 			{
 				String filename = rm+this.currentLocation+".txt";
 				
 				
 				
 				
 			    
 			    FileWriter fw = new FileWriter(filename,true); //the true will append the new data
 			    fw.write("\n\n----------"+getTimeStamp()+"----------\n"+
 			    		"SERVER: "+ " RM"+rm + currentLocation+"\n" +
 			    		info +"\n"+
 			    		"----------"+getTimeStamp()+"----------\n\n"
 			    		
 			    		
 			    		);//appends the string to the file
 			    
 			    
 			    
 			    
 			    
 			    
 			    
 			    fw.close();
 			}
 			catch(IOException ioe)
 			{
 			    System.err.println("IOException: " + ioe.getMessage());
 			}	
 			
 		}

 	

 	
 	public synchronized void prinData() {
 		//System.out.println("Size of HashMap : " + database.size());
 		
 		
 		//ArrayList<Records> test1 = new ArrayList<Records>();
 		//test1 = database.get('m');
 		
 		System.out.println("[--------------------------------------------RM"+ rm +" "+ currentLocation+"-----------------------------------------------]");
 		
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
 	

 	
 	public synchronized  String prinDataClient() {
 		String display =currentLocation + "\n";
 		
 		Iterator it =  this.database.entrySet().iterator();
 	    while (it.hasNext()) {
 	        Map.Entry pair = (Map.Entry)it.next();

 	        ArrayList<Records> value = (ArrayList<Records>) pair.getValue();
 	        display+= "[" +  pair.getKey() + "] =\n ";
 	        
 	        
 	        for(int i = 0; i < value.size(); i++) {
 	        	
 	        	display+= (i+1)+" : " + value.get(i).display_String() + "\n";
 	        	
 	        	
 	        }
 	        
 	        
 	        display+="\n";
 	        
 	    }


 	   return display;
 	}



 	public synchronized String transferRecord(String Manager_ID, String recordID, String remoteCenterServer) {
 		int sendToPort = 0;
 		String result = "Error - Transfer Request Could Not Be Completed!";
 		String response = null;
 		Records record;
 		
 		if(recordExists(recordID)) {
 			
 			
 			if(remoteCenterServer.equals("CA")) {
 				
 				if(rm==1) {
 					response = UDP_Connect_to_other_replicas.askRecordExists(6050, recordID);
 	 				sendToPort = 6050;
 					
 				}
 				else if(rm==2) {
 					response = UDP_Connect_to_other_replicas.askRecordExists(6053, recordID);
 	 				sendToPort = 6053;
 					
 				}
 				else if(rm==3) {
 					response = UDP_Connect_to_other_replicas.askRecordExists(6056, recordID);
 	 				sendToPort = 6056;
 					
 				}
 				
 			}
 			
 			else if(remoteCenterServer.equals("US")) {
 				
 				if(rm==1) {
 					response = UDP_Connect_to_other_replicas.askRecordExists(6051, recordID);
 	 				sendToPort = 6051;
 					
 				}
 				else if(rm==2) {
 					response = UDP_Connect_to_other_replicas.askRecordExists(6054, recordID);
 	 				sendToPort = 6054;
 					
 				}
 				else if(rm==3) {
 					response = UDP_Connect_to_other_replicas.askRecordExists(6057, recordID);
 	 				sendToPort = 6057;
 					
 				}
 				
 			}
 			
 			
 			else{//for UK
 				
 				if(rm==1) {
 					response = UDP_Connect_to_other_replicas.askRecordExists(6052, recordID);
 	 				sendToPort = 6052;
 					
 				}
 				else if(rm==2) {
 					response = UDP_Connect_to_other_replicas.askRecordExists(6055, recordID);
 	 				sendToPort = 6055;
 					
 				}
 				else if(rm==3) {
 					response = UDP_Connect_to_other_replicas.askRecordExists(6058, recordID);
 	 				sendToPort = 6058;
 					
 				}
 				
 			}
 			
 			
 			if(response.equals("n")) {// delete record on this server and transfer
 			
 			record = getRecord(recordID);	
 			
 			
 			
 			try {
 				UDP_Connect_to_other_replicas.sendRecord(record, sendToPort);
 				
 			} catch (IOException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}	
 				
 				
 				
 			deleteRecord(recordID);
 			numberOfRecords = getHashSize(database);
 			UDP_Connect_to_other_replicas.numberOfRecords = getHashSize(database);
 			
 			
 			
 			
 			
 			result = "Success : Record "+recordID+" has been removed from database and sent to server "+remoteCenterServer;	
 				
 			
 			
 			}
 			
 			else {
 				
 				result = "ERROR - Record already exists at "+ remoteCenterServer + " server!"; 	
 				
 			}
 			
 			
 			
 		}//end if records exists
 		
 		
 		 try {
 				totalNumberOfRecords = getAllRecordCounts();
 			} catch (Exception e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
 		    
 		  
 		   
 		   String number = Integer.toString(totalNumberOfRecords);
 		   
 		   //totalNumberOfRecords++;
 		   System.out.println("TOTAL NUMBER OF RECORDS" +number);
 		
 		
 		
 		
 		
 		
 		
 		
 		printToLogs(result);
 		 prinData(); 
 		
 		
 		return result;
 	}


 	public boolean recordExists(String recordID) {
 		
 		boolean exists = false;
 		
 		
 			
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
 		
 			
 				
 		return exists;
 		
 		
 		
 		
 		
 		
 	}

 	public synchronized void deleteRecord(String recordID) {
 		
 		Iterator it =  this.database.entrySet().iterator();
 	    while (it.hasNext()) {
 	        Map.Entry pair = (Map.Entry)it.next();

 	        ArrayList<Records> value = (ArrayList<Records>) pair.getValue();
 	        //System.out.println("[" + pair.getKey() + "] = ");
 	        for(int i = 0; i < value.size(); i++) {
 	        	
 	        	
 	        	
 	        	if(value.get(i).getRecordsID().equals(recordID)) {
 		
 	        		value.remove(i);
 	        		
 	        		if(value.size() == 0) {//remove map key if no more records in Array list
 	        			
 	        			it.remove();
 	        			
 	        		}
 	        		
 	        		
 	        	}//
 	        	
 	}}
 		
 	   
 		
 	}
 	public Records getRecord(String recordID) {
 		
 		Records record = null;
 		Iterator it =  this.database.entrySet().iterator();
 	    while (it.hasNext()) {
 	        Map.Entry pair = (Map.Entry)it.next();

 	        ArrayList<Records> value = (ArrayList<Records>) pair.getValue();
 	        //System.out.println("[" + pair.getKey() + "] = ");
 	        for(int i = 0; i < value.size(); i++) {
 	        	
 	        	
 	        	
 	        	if(value.get(i).getRecordsID().equals(recordID)) {
 		
 	        		record = value.get(i);
 	        	}//
 	        	
 	}}
 		return record;
 		
 	   
 		
 	}
 	
 	
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
	//SENDS TO FE BY UDP
		public void to_FE_UDP(String message, String manager_ID) {
			
			
			if(makeError) {
	        	
	        	message = "randomMSG";
	        }
			
			String jsonString = Json.createObjectBuilder()
		            .add("manager_ID", manager_ID)
		            .add("rm", rm)		            
		            .add("message", message)	   
		            .add("sequenceNumer", this.sequenceNumber)
		            .build()
		            .toString();
			
	
			
			
			String messageToClient = null;
			
				
				try {
					DatagramSocket clientSocket = new DatagramSocket();
				      InetAddress IPAddress = InetAddress.getByName("localhost");
				      byte[] sendData = new byte[1024];
				      byte[] receiveData = new byte[1024];
				      
				      sendData = jsonString.getBytes();
				      System.out.println("Sending to FE : "+ jsonString );
				      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 2222);
				      clientSocket.send(sendPacket);
			    	           
				      
				      clientSocket.close();
			     
			 	          	      
			 } // end try
			  catch (Exception ex) {
			    ex.printStackTrace( );
			   
			  } //end catch
		}
		/////////////////////////////////////////////////////////
 	
 	
		public int getSeqNumber() {
			
			return this.sequenceNumber;
			
		}




 	  
    
    
    
    
    
    
    
    


}

