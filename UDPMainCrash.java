package ServerSide;

import java.util.Scanner;


///////////////TEST SCENARIO FOR SOFTWARE ISSUES//////////////////////REPLICAS OF 1 RM WILL BE RETURNING WRONG VALUES

public class UDPMainCrash {
	public static void main(String args[]) {
		
		Scanner in = new Scanner(System.in);
		
		
	      
	      System.out.println("Select replica to crash [1 to 9]");
	      int replica = in.nextInt();
	      
	     
	      
	      
	      
		
        //Starting Servers for First RM set of Replicas
        //***********************************************************
        UDPServer firstServer = new UDPServer(6050, "CA");
       

        
        UDPServer secondServer = new UDPServer(6051, "US");
       

        UDPServer thirdServer = new UDPServer(6052, "UK");
       
        //***********************************************************

        //Starting Servers for Second RM set of Replicas
        //***********************************************************
        UDPServer firstServer2 = new UDPServer(6053, "CA");
        

        UDPServer secondServer2 = new UDPServer(6054, "US" );
       

        UDPServer thirdServer2 = new UDPServer(6055, "UK" );
       
        //***********************************************************

        //Starting Servers for Third RM set of Replicas
        //***********************************************************
        UDPServer firstServer3 = new UDPServer(6056, "CA");
        

        UDPServer secondServer3 = new UDPServer(6057, "US");
       
        UDPServer thirdServer3 = new UDPServer(6058, "UK");
       
        //***********************************************************

        //Starting All RM's
        //***********************************************************
        
        if(replica ==1) {
        	
        	
            secondServer.start();
            thirdServer.start();
        	firstServer2.start();
            secondServer2.start();
            thirdServer2.start();
            firstServer3.start();
            secondServer3.start();
            thirdServer3.start();	
        	
        	
        	
        }
        if(replica == 2) {
        	
        	firstServer.start();
        	
            thirdServer.start();
        	firstServer2.start();
            secondServer2.start();
            thirdServer2.start();
            firstServer3.start();
            secondServer3.start();
            thirdServer3.start();		
        	
        	
        	
        }
        if(replica == 3) {
        	
        	firstServer.start();
        	secondServer.start();
            
        	firstServer2.start();
            secondServer2.start();
            thirdServer2.start();
            firstServer3.start();
            secondServer3.start();
            thirdServer3.start();
        	
        	
        	
        }
        
        if(replica == 4) {
        	
        	firstServer.start();
        	secondServer.start();
            thirdServer.start();
        	
            secondServer2.start();
            thirdServer2.start();
            firstServer3.start();
            secondServer3.start();
            thirdServer3.start();
        	
        	
        	
        }
        
        if(replica == 5) {
        	
        	firstServer.start();
        	secondServer.start();
            thirdServer.start();
        	firstServer2.start();
            
            thirdServer2.start();
            firstServer3.start();
            secondServer3.start();
            thirdServer3.start();
        	
        	
        	
        }
        if(replica == 6) {
        	
        	firstServer.start();
        	secondServer.start();
            thirdServer.start();
        	firstServer2.start();
            secondServer2.start();
            
            firstServer3.start();
            secondServer3.start();
            thirdServer3.start();
        	
        	
        	
        }
        if(replica == 7) {
        	
        	firstServer.start();
        	secondServer.start();
            thirdServer.start();
        	firstServer2.start();
            secondServer2.start();
            thirdServer2.start();
           
            secondServer3.start();
            thirdServer3.start();
        	
        	
        	
        }
        if(replica == 8) {
        	
        	firstServer.start();
        	secondServer.start();
            thirdServer.start();
        	firstServer2.start();
            secondServer2.start();
            thirdServer2.start();
            firstServer3.start();
           
            thirdServer3.start();
        	
        	
        	
        }
        if(replica == 9) {
        	
        	firstServer.start();
        	secondServer.start();
            thirdServer.start();
        	firstServer2.start();
            secondServer2.start();
            thirdServer2.start();
            firstServer3.start();
            secondServer3.start();
           
        	
        	
        	
        }
        
        
        
        
        replicaManager firstRM = new replicaManager(firstServer, secondServer, thirdServer, 1,8888);
        Thread t = new Thread(firstRM);
        t.start();

        replicaManager secondRM = new replicaManager(firstServer2, secondServer2, thirdServer2, 2,8888);
        Thread t2 = new Thread(secondRM);
        t2.start();

        replicaManager thirdRM = new replicaManager(firstServer3, secondServer3, thirdServer3, 3,8888);
        Thread t3 = new Thread(thirdRM);
        t3.start();

        //***********************************************************
        
        ///////////////////////testing execution of methods/////////////////////////////////////
        /*
        System.out.println(firstServer.createERecord("CA1001", "Leo", "Mototo", 1231, "leo@gmail.com", "P1001"));
        System.out.println(secondServer.createERecord("US1001", "Leo", "Mototo", 1231, "leo@gmail.com", "P1001"));
        System.out.println(firstServer.getRecordCounts("CA10001"));
        System.out.println(firstServer.editRecord("CA10001" ,"MR10001", "mailID", "123@gmail.com"));
        System.out.println(secondServer.getRecordCounts("US10001"));
        */
        ///////////////////////END testing/////////////////////////////////////
    }

}
