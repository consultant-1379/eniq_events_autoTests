//When this file is updated, it should be compiled as follows:
//1. Check out the file RunTestsOnWindows.class
//2. Open a command prompt window in this folder
//3. Run the command "C:\Program Files\Java\jdk1.6.0_35\bin\javac.exe" runTestsOnWindows.java   (or whatever the currently available jdk is)
//4. Check back in RunTestsOnWindows.class
//
//Run it with the following format: java RunTestsOnWindows runTests host=10.42.33.82 -DHOST=http://atrcxb1931.athtem.eei.ericsson.se -DTEST_GROUP=DummyTestGroup
import java.io.*;
import java.net.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.FieldPosition;
public class RunTestsOnWindows{
	Socket requestSocket;
	ObjectOutputStream out;
 	ObjectInputStream in;
 	String message="";
 	String command="";
	RunTestsOnWindows(){}
	void run(String [] args)
	{
		String host="localhost";
		String testGroup="ALL";
		String strDate=format(new Date());

		try{
			if(args!=null){
				for(int i=0;i<args.length;i++){
					if(args[i].startsWith("host=")){
						host=args[i].substring(5);
					}else if(args[i].startsWith("-DTEST_GROUP=")){
						testGroup=args[i].substring(13);
						command+=args[i] + " ";
					}else{
						command+=args[i] + " ";
					}
				}
			}
			System.out.println("command:" + command);
			System.out.println("Connecting to " + host + " on port 2004");
			requestSocket = new Socket(host, 2004);
			System.out.println("Connected to " + host + " on port 2004");
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());
			do{
				try{
					message = (String)in.readObject();
					System.out.println("server>" + message);
					sendMessage(command);
					message = (String)in.readObject();
					System.out.println("server>" + message);
					message = (String)in.readObject();
					System.out.println("server>" + message);
					writeFile(testGroup + "-" + strDate + "-testresults.log",message);
					message = "bye";
					sendMessage(message);
					message = (String)in.readObject();
					System.out.println("server>" + message);
				}
				catch(ClassNotFoundException classNot){
					System.out.println("data received in unknown format");
					message="bye";
					writeFile(testGroup + "-" + strDate + "-testresults.log","data received in unknown format:" + classNot.getStackTrace().toString());
				}catch(IOException ioException){
					System.out.println("IOException");
					ioException.printStackTrace();
					message="bye";
					writeFile(testGroup + "-" + strDate + "-testresults.log","IOException:" + ioException.getStackTrace().toString());
				}catch(Exception e){
					e.printStackTrace();
					message="bye";
					writeFile(testGroup + "-" + strDate + "-testresults.log","Exception" + e.getStackTrace().toString());
				}
			}while(!message.equals("bye"));
		}
		catch(UnknownHostException unknownHost){
			System.err.println("You are trying to connect to an unknown host!");
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
		finally{
			//4: Closing connection
			try{
				in.close();
				out.close();
				requestSocket.close();
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
	}
	 String format(Date theDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy_hh-mm-ss"); 
		FieldPosition pos = new FieldPosition(0);
		StringBuffer empty = new StringBuffer();
		StringBuffer date = sdf.format(theDate, empty, pos);
		return date.toString();
	}

	void writeFile(String file,String output)
	{
		try{
			FileWriter fstream = new FileWriter(file);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(output);
			out.close();
		}catch (Exception e){
			System.err.println("Error: " + e.getMessage());
		}
	}
	void sendMessage(String msg)
	{
		try{
			out.writeObject(msg);
			out.flush();
			System.out.println("client>" + msg);
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	public static void main(String args[])
	{
		RunTestsOnWindows client = new RunTestsOnWindows();
		client.run(args);
	}
}