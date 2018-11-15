package ServerCenterApp;


/**
* ServerCenterApp/ServerCenterIDLPOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ServerCenterIDL.idl
* Tuesday, November 13, 2018 6:20:20 o'clock PM EST
*/

public abstract class ServerCenterIDLPOA extends org.omg.PortableServer.Servant
 implements ServerCenterApp.ServerCenterIDLOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("createMRecord", new java.lang.Integer (0));
    _methods.put ("getRecordCounts", new java.lang.Integer (1));
    _methods.put ("createERecord", new java.lang.Integer (2));
    _methods.put ("transferRecord", new java.lang.Integer (3));
    _methods.put ("editRecord", new java.lang.Integer (4));
    _methods.put ("shutdown", new java.lang.Integer (5));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // ServerCenterApp/ServerCenterIDL/createMRecord
       {
         String methodName = in.read_string ();
         String manager_ID = in.read_string ();
         String firstName = in.read_string ();
         String lastName = in.read_string ();
         int employeeID = in.read_long ();
         String mailID = in.read_string ();
         String projectID = in.read_string ();
         String clientName = in.read_string ();
         String projectName = in.read_string ();
         String location = in.read_string ();
         String $result = null;
         $result = this.createMRecord (methodName, manager_ID, firstName, lastName, employeeID, mailID, projectID, clientName, projectName, location);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }


  // signature of second remote method
       case 1:  // ServerCenterApp/ServerCenterIDL/getRecordCounts
       {
         String methodName = in.read_string ();
         String Manager_ID = in.read_string ();
         String $result = null;
         $result = this.getRecordCounts (methodName, Manager_ID);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 2:  // ServerCenterApp/ServerCenterIDL/createERecord
       {
         String methodName = in.read_string ();
         String Manager_ID = in.read_string ();
         String firstName = in.read_string ();
         String lastName = in.read_string ();
         int employeeID = in.read_long ();
         String mailID = in.read_string ();
         String projectID = in.read_string ();
         String $result = null;
         $result = this.createERecord (methodName, Manager_ID, firstName, lastName, employeeID, mailID, projectID);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 3:  // ServerCenterApp/ServerCenterIDL/transferRecord
       {
         String methodName = in.read_string ();
         String Manager_ID = in.read_string ();
         String recordID = in.read_string ();
         String remoteCenterServer = in.read_string ();
         String $result = null;
         $result = this.transferRecord (methodName, Manager_ID, recordID, remoteCenterServer);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 4:  // ServerCenterApp/ServerCenterIDL/editRecord
       {
         String methodName = in.read_string ();
         String Manager_ID = in.read_string ();
         String recordID = in.read_string ();
         String fieldName = in.read_string ();
         String newValue = in.read_string ();
         String $result = null;
         $result = this.editRecord (methodName, Manager_ID, recordID, fieldName, newValue);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 5:  // ServerCenterApp/ServerCenterIDL/shutdown
       {
         this.shutdown ();
         out = $rh.createReply();
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:ServerCenterApp/ServerCenterIDL:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public ServerCenterIDL _this() 
  {
    return ServerCenterIDLHelper.narrow(
    super._this_object());
  }

  public ServerCenterIDL _this(org.omg.CORBA.ORB orb) 
  {
    return ServerCenterIDLHelper.narrow(
    super._this_object(orb));
  }


} // class ServerCenterIDLPOA
