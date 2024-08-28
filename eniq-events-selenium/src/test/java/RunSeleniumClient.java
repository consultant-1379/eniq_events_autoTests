import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

public class RunSeleniumClient {
	static boolean unix = false;
	public static void main(String[] args) throws IOException {
	if(args.length > 0)
	{
		if(args[0].equals("startUnix"))
		{
		unix = true;
		args[0] = "start";
		}
		if(args[0].equals("stopUnix"))
		{
		unix = true;
		args[0] = "stop";
		}
		if(args[0].equals("quitUnix"))
		{
		unix = true;
		args[0] = "quit";
		}
	}
		HashMap<String, String> iniMap = getIni(args);
		System.out.println(iniMap.toString()); 
		Socket kkSocket = null;
		PrintWriter out = null;
		BufferedReader in = null;

		try {
			if(unix == true)
			{
				kkSocket = new Socket("localhost", 4445);	
			}
			else
			{
				kkSocket = new Socket(iniMap.get("host"), 4445);	
			}	
			out = new PrintWriter(kkSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
					kkSocket.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Cant find host");
			System.exit(1);
		} catch (IOException e) {
			System.err
					.println("Couldn't get I/O for the connection maybe listener Class isn't running");
			System.exit(1);
		}

		// FIRST SEND THE INI
		ObjectOutputStream ois = new ObjectOutputStream(
				kkSocket.getOutputStream());
		ois.writeObject(iniMap);
		System.out.println("Finished Sending ini");
		String fromServer;

		while (!(fromServer = in.readLine()).equals("0")) {
			System.out.println("Server: " + fromServer);
		}
		if (args != null) {
			if (args.length > 0) {
				out.println(args[0]);
			} else {
				out.println();
			}
		}

		while (!(fromServer = in.readLine()).equals("0")) {

			if (fromServer.equals("00")) {
				BufferedReader stdIn = new BufferedReader(
						new InputStreamReader(System.in));
				String fromUser = stdIn.readLine();
				out.println(fromUser);
				stdIn.close();
			}
			System.out.println("Server: " + fromServer);
		}

		out.close();
		in.close();
		// stdIn.close();
		kkSocket.close();
		System.out.println("DONE");
	}

	private static HashMap<String, String> getIni(String args[]) {
		HashMap<String, String> iniMap = new HashMap<String, String>();
		try {
			FileInputStream fstream = new FileInputStream("buildproperties.ini");
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				String[] tokens = strLine.split("=");
				if (tokens.length == 2) {
					iniMap.put(tokens[0].trim(), tokens[1].trim());
				} else {
					iniMap.put(tokens[0].trim(), "");
				}

			}
			if(unix == true)
			{
				iniMap.put("host", InetAddress.getLocalHost().getHostAddress().toString().trim());
			}
			iniMap.put("hubURL", "http://"
					+ InetAddress.getLocalHost().getHostAddress().toString()
							.trim() + ":4444");
			if (args.length > 1) {
				iniMap.put("port", args[1]);
			} else {
				iniMap.put("port", "1234");
			}
			in.close();
		} catch (IOException e) {
			System.err.println("Error: " + e.getMessage());
		}
		return iniMap;
	}
}