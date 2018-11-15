package ServerSide;

import java.util.ArrayList;
import java.util.HashMap;

public class ReplicaTesting {
	
	
public static void main(String[] args) {
	    
		HashMap<Integer, String> holdbackqueue = new HashMap <Integer, String>();
		HashMap<Character, ArrayList<Records>> database = new HashMap <Character, ArrayList<Records>>();
		
		
		
		
		
		Replica CA = new Replica("CA",holdbackqueue, database );
		
		CA.run();
		
		
	      
	      while(true) {
	    	  
	    	  
	    	  
	    	  
	    	  
	    	  
	      }
	      
	      
	      
	      
		
		System.out.println("dasd");
	   }
	
	

}
