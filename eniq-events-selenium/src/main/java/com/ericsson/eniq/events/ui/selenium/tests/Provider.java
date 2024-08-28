package com.ericsson.eniq.events.ui.selenium.tests;

import com.ericsson.eniq.events.ui.selenium.common.logging.SeleniumLogger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;
public class Provider{
	static Logger logger = Logger.getLogger(SeleniumLogger.class.getName());
	ServerSocket providerSocket;
	Socket connection = null;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message;
	Provider(){}
	void run() throws Exception
	{
		try{
			//1. creating a server socket
			providerSocket = new ServerSocket(2004, 10);
			//2. Wait for connection
			logger.info("Waiting for connection");
			connection = providerSocket.accept();
			logger.info("Connection received from " + connection.getInetAddress().getHostName());
			//3. get Input and Output streams
			out = new ObjectOutputStream(connection.getOutputStream());
			out.flush();
			in = new ObjectInputStream(connection.getInputStream());
			sendMessage("Connection successful");
			//4. The two parts communicate via the input and output streams
			do{
				try{
					message = (String)in.readObject();
					logger.info("client>" + message);
					if (message.equals("bye")){
						sendMessage("bye");
					}else if(message.toLowerCase().startsWith("runtests")){
						message=message.substring(8);					
						sendMessage("runTests accepted:" + message);
						int gg=0;
						while(message.contains(" -D")){
							message=message.substring(message.indexOf(" -D") + 3);
							if(message.contains("=")){
								String varName=message.substring(0,message.indexOf("="));
								String varValue=message.substring(0,message.indexOf("="));
								if(message.contains(" -D")){
									varValue=message.substring(message.indexOf("=")+1,message.indexOf(" -D"));
								}else{
									varValue=message.substring(message.indexOf("=")+1,message.length());
								}
								logger.info("current value of" + varName + ":" + System.getProperty(varName));
								logger.info("Setting property:" + varName + "=" + varValue);
								System.setProperty(varName, varValue);
								logger.info("after setting value of" + varName + ":" + System.getProperty(varName));
							}
						}
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						System.setOut(new PrintStream(baos));
						SeleniumTestSuite.doTests();
						String results=readFile(SeleniumLogger.getFileName());
						sendMessage(results);
						sendMessage("bye");
						System.exit(7);
					}
					else{
						sendMessage("continue");
					}
				}
				catch(ClassNotFoundException classnot){
					logger.info("Data received in unknown format");
				}
			}while(!message.equals("bye"));
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
		finally{
			//4: Closing connection
			try{
				in.close();
				out.close();
				providerSocket.close();
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
	}
	void sendMessage(String msg)
	{
		try{
			out.writeObject(msg);
			out.flush();
			logger.info("server>" + msg);
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	public static void main(String args[])
	{
		Provider server = new Provider();
		try{
			while(true){
				server.run();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private String readFile(String file){
		String contents="";
		try{
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null)   {
				contents+=strLine + "\n";
			}
			in.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
			return e.getMessage();
		}
		return contents;
	}
}
  