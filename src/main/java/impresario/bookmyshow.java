package impresario;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import models.Show;
import managers.impresariodb;

/**
 * Servlet implementation class bookmyshow
 */
@WebServlet("/cinimas/impresario/bookmyshow")
public class bookmyshow extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public bookmyshow() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
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
		String Id = ""+request.getAttribute("Id");
		try {
			json = (JSONObject) parser.parse(jsoninput);
			int capacity = Integer.parseInt(""+json.get("capacity"));
			capacity = (capacity+100)-(capacity%100);
			System.out.println(json.get("showdate"));
			
			impresariodb.createcreateshow(Integer.parseInt(Id),Integer.parseInt(""+json.get("movieId")),Integer.parseInt(""+json.get("lId")),""+json.get("showdate"),Integer.parseInt(""+json.get("stId")),Integer.parseInt(""+json.get("thId")),Integer.parseInt(""+json.get("scId")),capacity,(JSONArray)json.get("rate"));
			resobj.put("statusCode", 200);
		}catch(Exception ex) {
			resobj.put("statusCode", ex.getMessage());
			resobj.put("Message", ex.getMessage());
		}
		response.getWriter().append(resobj.toString());
	}

}
