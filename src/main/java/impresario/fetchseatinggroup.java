package impresario;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import managers.Tools;

/**
 * Servlet implementation class fetchseatinggroup
 */
@WebServlet("/cinimas/impresario/fetchseatinggroup")
public class fetchseatinggroup extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public fetchseatinggroup() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @param Integer 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<Integer,JSONObject> seating = new HashMap<>();
		JSONObject res = new JSONObject();
		try {
			PreparedStatement ps = Tools.DB.prepareStatement("select gpId,seatingtype.seId,type from groupofseattype,seatingtype where groupofseattype.seId=seatingtype.seId;");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				if(seating.containsKey(rs.getInt(1))) {
					JSONObject json = seating.get(rs.getInt(1));
					json.put(rs.getInt(2), rs.getString(3));
					seating.put(rs.getInt(1),json );
				}else {
					JSONObject json = new JSONObject();
					json.put(rs.getInt(2), rs.getString(3));
					seating.put(rs.getInt(1),json );
				}
			}
			JSONObject json = new JSONObject();
			for(Map.Entry<Integer,JSONObject> d : seating.entrySet()) {
				json.put(d.getKey(), d.getValue());
			}
			res.put("statusCode", 200);
			res.put("json", json.toString());
		}catch(Exception ex) {
			res.put("statusCode", 500);
			res.put("Message", ex.getMessage());
		}
		response.getWriter().append(res.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
