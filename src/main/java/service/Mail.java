package service;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Mail {
	public static boolean send(String mail,String sub,String content) {
		String API_KEY = "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";

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
