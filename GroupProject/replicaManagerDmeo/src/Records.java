import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.sql.Timestamp;


public class Records implements Serializable {

	//static int recordNumber =10000;
	
	String recordsID = null;
	
	String ER_projectID;
	String firstName;
	String lastName;
	int employeeID;
	String mailID;
	//Projects project;
	String location;//US, CA, UK
	 String projectID;
	 String projectName;
	 String projectClient;

	 public Records(String firstname, String lastName, int employeeID, String mailID, String projectID,String projectClient,String projectName, String location, String recordID)
	 {
		 this.projectID =  projectID;
		 this.projectName = projectName;
		 this.projectClient = projectClient;

		 this.firstName = firstname;
		 this.lastName = lastName;
		 this.employeeID = employeeID;
		 this.mailID = mailID;
		 this.recordsID = recordID;

		 if(location.equals("CA") || location.equals("US") ||  location.equals("UK")){

			 this.location = location;

		 }
	 }

	public Records(String recordPrefix ,String firstname, String lastName, int employeeID, String mailID, String projectID,String projectClient,String projectName, String location) {
		
		//a way to get recordNumber;
		
		int size = recordPrefix.length();
		
		
		if(size == 3) {
			
			recordsID = recordPrefix.substring(0, 2) +"1000"+recordPrefix.substring(2, 3);	
			
		}
		if(size == 4) {
			
			recordsID = recordPrefix.substring(0, 2) +"100"+recordPrefix.substring(2, 4);
			
		} 

		this.projectID =  projectID;
		 this.projectName = projectName;
		 this.projectClient = projectClient;
		
		this.firstName = firstname;
		this.lastName = lastName;
		this.employeeID = employeeID;
		this.mailID = mailID;
		
		if(location.equals("CA") || location.equals("US") ||  location.equals("UK")){
			
			this.location = location;	
			
		}
		else {
			
			this.location = "ERROR";
			
		}
	}
	
	
public Records(String recordPrefix ,String firstname, String lastName, int employeeID, String mailID, String projectID) {
		
		//a way to get recordNumber;
	int size = recordPrefix.length();
	if(size == 3) {
		
		recordsID = recordPrefix.substring(0, 2) +"1000"+recordPrefix.substring(2, 3);	
		
	}
	if(size == 4) {
		
		recordsID = recordPrefix.substring(0, 2) +"100"+recordPrefix.substring(2, 4);
		
	}

	
	this.projectID =  projectID;
	 this.projectName = "";
	 this.projectClient = "";
	
	
	this.firstName = firstname;
	this.lastName = lastName;
	this.employeeID = employeeID;
	this.mailID = mailID;
	//this.ER_projectID = projectID;
	
	
	this.location = "";
		
		
	}


 public void display() {
		
		System.out.println(	recordsID +"  " + firstName +"  " +	 lastName +"  " +	 employeeID +"  " + mailID +"  " + projectID +"  "+ projectClient+ " " + projectName +"  " + location);
		
		
	}

	public String display_String() {
		
		return	recordsID +"  " + firstName +"  " +	 lastName +"  " +	 employeeID +"  " + mailID +"  " + projectID +"  "+ projectClient+ " " + projectName +"  " + location;
		
		
	}


	public String getTimeStamp()
	{
	String timeStamp;
	Date date= new Date();

	long time = date.getTime();
	Timestamp ts = new Timestamp(time);
	timeStamp = ts.toString();
			

	return timeStamp;
	}
 

	
public String ER_projectID() {
	
	return ER_projectID;
	
}

public void ER_projectID(String ER_projectID) {
	this.ER_projectID = ER_projectID;
}


public String getRecordsID() {
	return recordsID;
} 


public String getFirstName() {
	return firstName;
}


public void setFirstName(String firstName) {
	this.firstName = firstName;
}


public String getLastName() {
	return lastName;
}


public void setLastName(String lastName) {
	this.lastName = lastName;
}


public int getEmployeeID() {
	return employeeID;
}


public void setEmployeeID(int employeeID) {
	this.employeeID = employeeID;
}


public String getMailID() {
	return mailID;
}


public void setMailID(String mailID) {
	this.mailID = mailID;
}

/*
public Projects getProject() {
	return project;
}


public void setProject(Projects project) {
	this.project = project;
}
*/

public String getProjectID() {
	return projectID;
}

public void setProjectID(String projectID) {
	this.projectID = projectID;
}


public String getProjectClient() {
	return projectClient;
}

public void setProjectClient(String projectClient) {
	this.projectClient = projectClient;
}
public String getProjectName() {
	return projectName;
}

public void setProjectName(String projectName) {
	this.projectName = projectName;
}


public String getLocation() {
	return location;
}


public void setLocation(String location) {
	this.location = location;
}






}
