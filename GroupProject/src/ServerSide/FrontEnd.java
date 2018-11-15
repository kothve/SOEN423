package ServerSide;
import ServerCenterApp.*;

import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
import java.util.Properties;
 
public class FrontEnd {
 
	//STEP 1 : in cmd type -> start orbd -ORBInitialPort 1050   
	//STEP 2 : Run FronEnd.java with the following arguments  :    -ORBInitialPort 1050 -ORBInitialHost localhost
	//STEP 3 : RUN startClient.java with the following arguments : -ORBInitialPort 1050 -ORBInitialHost localhost
	
	
	
  public static void main(String args[]) {

///////////////////////////////////////CORBA SERVER INITIATION //////////////////////////////////////
    try{
      // create and initialize the ORB //// get reference to rootpoa &amp; activate the POAManager
      ORB orb = ORB.init(args, null);      
      POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
      rootpoa.the_POAManager().activate();
 
      // create servant and register it with the ORB
      RequestInfo request = new RequestInfo();
      request.setORB(orb); 


      
      // get object reference from the servant
      org.omg.CORBA.Object ref = rootpoa.servant_to_reference(request);
      ServerCenterIDL href = ServerCenterIDLHelper.narrow(ref);


      
      
      
      
 
      org.omg.CORBA.Object objRef =  orb.resolve_initial_references("NameService");
      NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
 
      NameComponent path[] = ncRef.to_name( "FrontEnd" );
      ncRef.rebind(path, href);
      
      
 
      System.out.println("Central Servers ready and waiting ...");
 
      // wait for invocations from clients
      for (;;){
	  orb.run();
      }
    } 
 
      catch (Exception e) {
        System.err.println("ERROR: " + e);
        e.printStackTrace(System.out);
      }
 
      System.out.println("Center Server operational ...");
      
///////////END CORBA INITIATION////////////////////////////////////////////////////////
      
      
      
      
      
      
      
      
      
      
      
      
      
 
  }

  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
}