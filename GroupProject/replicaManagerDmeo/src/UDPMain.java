import java.io.BufferedReader;
import java.io.InputStreamReader;

public class UDPMain {

    public static void main(String args[]) {


        //Starting Servers for First RM set of Replicas
        //***********************************************************
        UDPServer firstServer = new UDPServer(9876, "CA");
        firstServer.start();

        UDPServer secondServer = new UDPServer(9877, "US");
        secondServer.start();

        UDPServer thirdServer = new UDPServer(9878, "UK");
        thirdServer.start();
        //***********************************************************

        //Starting Servers for Second RM set of Replicas
        //***********************************************************
        UDPServer firstServer2 = new UDPServer(9873, "CA");
        firstServer2.start();

        UDPServer secondServer2 = new UDPServer(9874, "US");
        secondServer2.start();

        UDPServer thirdServer2 = new UDPServer(9875, "UK");
        thirdServer2.start();
        //***********************************************************

        //Starting Servers for Third RM set of Replicas
        //***********************************************************
        UDPServer firstServer3 = new UDPServer(9870, "CA");
        firstServer3.start();

        UDPServer secondServer3 = new UDPServer(9871, "US");
        secondServer3.start();

        UDPServer thirdServer3 = new UDPServer(9872, "UK");
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
