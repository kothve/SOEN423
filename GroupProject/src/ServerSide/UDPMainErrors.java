package ServerSide;

import java.util.Scanner;


///////////////TEST SCENARIO FOR SOFTWARE ISSUES//////////////////////REPLICAS OF 1 RM WILL BE RETURNING WRONG VALUES

public class UDPMainErrors {
	public static void main(String args[]) {
		
		Scanner in = new Scanner(System.in);
		
		
			System.out.println("Select Replicas to make mistakes[1, 2 or 3]");
			System.out.println("Crash one replica either [1, 2 or 3]");
		 
	      int num = in.nextInt();
		
        //Starting Servers for First RM set of Replicas
        //***********************************************************
        UDPServer firstServer = new UDPServer(6050, "CA",num );
        firstServer.start();

        
        UDPServer secondServer = new UDPServer(6051, "US", num);
        secondServer.start();

        UDPServer thirdServer = new UDPServer(6052, "UK",num);
        thirdServer.start();
        //***********************************************************

        //Starting Servers for Second RM set of Replicas
        //***********************************************************
        UDPServer firstServer2 = new UDPServer(6053, "CA",num );
        firstServer2.start();

        UDPServer secondServer2 = new UDPServer(6054, "US",num );
        secondServer2.start();

        UDPServer thirdServer2 = new UDPServer(6055, "UK",num );
        thirdServer2.start();
        //***********************************************************

        //Starting Servers for Third RM set of Replicas
        //***********************************************************
        UDPServer firstServer3 = new UDPServer(6056, "CA",num);
        firstServer3.start();

        UDPServer secondServer3 = new UDPServer(6057, "US",num);
        secondServer3.start();

        UDPServer thirdServer3 = new UDPServer(6058, "UK",num);
        thirdServer3.start();
        //***********************************************************

        //Starting All RM's
        //***********************************************************
        
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
