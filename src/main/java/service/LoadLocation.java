package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Servlet implementation class LoadLocation
 */
@WebServlet("/cinimas/LoadLocation")
public class LoadLocation extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoadLocation() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String jsoninput =(String)request.getParameter("details");
		JSONObject resobj = new JSONObject();
		JSONParser parser = new JSONParser();
		JSONObject json = null;
		try {
			json = (JSONObject) parser.parse(jsoninput);
			String address = (""+json.get("city"))+","+(""+json.get("state")+","+(""+json.get("country")));
			System.out.println(address);
	        String nominatimUrl = "https://nominatim.openstreetmap.org/search?q=" + URLEncoder.encode(address.replace(" ", ""), "UTF-8") + "&format=json";

	        URL url = new URL(nominatimUrl);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("GET");

	        BufferedReader reader1 = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        StringBuilder res = new StringBuilder();
	        String line;
	        while ((line = reader1.readLine()) != null) {
	            res.append(line);
	        }
	        reader1.close();
	        String data = res.toString();
	        JSONArray jsonArr = new JSONArray();
			try {
				jsonArr = (JSONArray)parser.parse(data);
				System.out.println(jsonArr);
				if(jsonArr.size()>0) {
					resobj.put("statusCode", "200");
					resobj.put("jsonarr",jsonArr.toString());
				}else {
					resobj.put("statusCode", "404");
				}
			} catch (ParseException e) {
				resobj.put("statusCode", "404");
			}
			response.getWriter().append(resobj.toString());	
		}catch(ParseException pe) {
			resobj.put("statusCode","500");
			resobj.put("Message","error occured plz give valid input !!");
			response.getWriter().append(resobj.toString());			
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String inp="";
		String jsoninput ="";
		BufferedReader reader = request.getReader();
		JSONObject resobj = new JSONObject();
		while((inp = reader.readLine())!=null) {
			jsoninput+=inp;
		}
		JSONParser parser = new JSONParser();
		JSONObject json = null;
		try {
			json = (JSONObject) parser.parse(jsoninput);
			String address = (""+json.get("latitude"))+","+(""+json.get("longitude"));
			System.out.println(address);
	        String nominatimUrl = "https://nominatim.openstreetmap.org/search?q=" + URLEncoder.encode(address, "UTF-8") + "&format=json";

	        URL url = new URL(nominatimUrl);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("GET");

	        BufferedReader reader1 = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        StringBuilder res = new StringBuilder();
	        String line;
	        while ((line = reader1.readLine()) != null) {
	            res.append(line);
	        }
	        reader1.close();
	        String data = res.toString();
	        JSONArray jsonArr = new JSONArray();
			try {
				jsonArr = (JSONArray)parser.parse(data);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(jsonArr.size());
			try {
				JSONObject jsobj = (JSONObject)jsonArr.get(0);
				System.out.println(jsobj);
				String[] returnable  = ((String)jsobj.get("display_name")).toLowerCase().split(",");
				resobj.put("lat", (String)jsobj.get("lat"));
				resobj.put("lon", (String)jsobj.get("lon"));
				for(int i=0;i<returnable.length;i++) {
					if(returnable[i].replaceAll("[0-9]","").length()==returnable[i].length()) {
						resobj.put("loca", returnable[i]);
						break;
					}
				}
				
				resobj.put("statusCode", "200");
				resobj.put("Message", "success");
			}catch(Exception ex) {
				
			}
			response.getWriter().append(resobj.toString());
		}catch(ParseException pe) {
			resobj.put("statusCode","500");
			resobj.put("Message","error occured plz give valid input !!");
			response.getWriter().append(resobj.toString());			
		}
		
	}

}
