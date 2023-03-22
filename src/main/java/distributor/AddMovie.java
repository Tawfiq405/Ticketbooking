package distributor;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import models.Movie;
import enums.Experience;
import enums.Rating;
import service.DBmanager;
import service.Service;


/**
 * Servlet implementation class AddMovie
 */
@WebServlet("/cinimas/distributor/AddMovie")
public class AddMovie extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddMovie() {
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
		System.out.println("enter");
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
			Movie movie =  Service.createmovieobj(json);
			System.out.println(movie);
			boolean val = DBmanager.insert(movie);
			System.out.println(val);
			if(val) {
				resobj.put("statusCode","200");
				response.getWriter().append(resobj.toString());	
			}else {
				resobj.put("statusCode","500");
				resobj.put("Message","error occured plz give valid input !!");
				response.getWriter().append(resobj.toString());	
			}
		}catch(ParseException pe) {
			
			resobj.put("statusCode","500");
			resobj.put("Message","error occured plz give valid input !!");
			response.getWriter().append(resobj.toString());			
		}
		
		
	}


}
