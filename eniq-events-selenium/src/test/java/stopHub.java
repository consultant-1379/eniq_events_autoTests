import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class stopHub
{

  public static void main(String[] args)
  {

    try {
    // Construct data
    String data = URLEncoder.encode("key1", "UTF-8") + "=" + URLEncoder.encode("value1", "UTF-8");
    data += "&" + URLEncoder.encode("key2", "UTF-8") + "=" + URLEncoder.encode("value2", "UTF-8");

    // Send data
    URL url = new URL("http://localhost:4444/lifecycle-manager?action=shutdown");
    URLConnection conn = url.openConnection();
    conn.setDoOutput(true);
    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
    wr.write(data);
    wr.flush();

    // Get the response
    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    String line;
    while ((line = rd.readLine()) != null) {
        // Process line...
    }
    wr.close();
    rd.close();
} catch (Exception e) {
}

  }

}
