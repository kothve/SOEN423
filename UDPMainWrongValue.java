package ServerSide;

import java.util.Scanner;

public class UDPMainWrongValue {

	 public static void main(String args[]) {

	    	Scanner in = new Scanner(System.in);
	//Starting Servers for First RM set of Replicas
    //***********************************************************
    UDPServer firstServer = new UDPServer(6050, "CA", true);
    firstServer.start();

    
    UDPServer secondServer = new UDPServer(6051, "US");
    secondServer.start();

    UDPServer thirdServer = new UDPServer(6052, "UK");
    thirdServer.start();
    
    //***********************************************************

    //Starting Servers for Second RM set of Replicas
    //***********************************************************
    UDPServer firstServer2 = new UDPServer(6053, "CA");
    firstServer2.start();

    UDPServer secondServer2 = new UDPServer(6054, "US");
    secondServer2.start();

    UDPServer thirdServer2 = new UDPServer(6055, "UK");
    thirdServer2.start();
    //***********************************************************

    //Starting Servers for Third RM set of Replicas
    //***********************************************************
    UDPServer firstServer3 = new UDPServer(6056, "CA");
    firstServer3.start();

    UDPServer secondServer3 = new UDPServer(6057, "US");
    secondServer3.start();

    UDPServer thirdServer3 = new UDPServer(6058, "UK");
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
    
    
	 }
	
	
	
}
