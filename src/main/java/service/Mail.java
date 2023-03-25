package service;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Mail {
	public static boolean send(String mail,String sub,String content) {
		String API_KEY = "";

		  try {
	          URL url = new URL("https://api.sendinblue.com/v3/smtp/email");

	          HttpURLConnection con = (HttpURLConnection) url.openConnection();
	          con.setRequestMethod("POST");
	          con.setRequestProperty("Content-Type", "application/json");
	          con.setRequestProperty("api-key", API_KEY);
	          con.setDoOutput(true);

	          String payload = "{\"sender\":{\"email\":\"TawfiqMovieBooking@gmail.com\"},\"to\":[{\"email\":\""+mail+"\"}],\"subject\":\""+sub+"\",\"textContent\":\""+content+"\"}";
	          OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
	          writer.write(payload);
	          writer.flush();
	          writer.close();

	          int responseCode = con.getResponseCode();
	          return (responseCode==201)? true:false;
	      } catch (Exception e) {
	    	  return false;
	      }
	}
}
