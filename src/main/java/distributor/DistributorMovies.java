package distributor;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import models.Movie;
import service.Service;
import managers.Tools;



/**
 * Servlet implementation class DistributorMovies
 */
@WebServlet(urlPatterns = {"/cinimas/impresario/DistributorMovies", "/cinimas/distributor/DistributorMovies"})
public class DistributorMovies extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DistributorMovies() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("mymovies");
    	JSONObject resobj = new JSONObject();
		JSONArray jsonarr = Service.getDistributorMovies();
		if(jsonarr!=null) {
			resobj.put("statusCode", 200);
			resobj.put("jsonarr", jsonarr);
		}else {
			resobj.put("statusCode", 500);
			resobj.put("Message", "error");
		}
//		System.out.println(getdistributormovies.distributormovies);
		response.getWriter().append(resobj.toString());
		
		
	}
	
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
