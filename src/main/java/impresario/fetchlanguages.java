package impresario;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import managers.Tools;

/**
 * Servlet implementation class fetchlanguages
 */
@WebServlet("/cinimas/impresario/fetchlanguages")
public class fetchlanguages extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public fetchlanguages() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String Id = ""+request.getParameter("Id");
		JSONObject js = new JSONObject();
		try {
			PreparedStatement ps = Tools.DB.prepareStatement("select language.lId,language from languagerelations,language where movieId = ? and languagerelations.lId=language.lId;");
			ps.setInt(1, Integer.parseInt(Id));
			ResultSet rs = ps.executeQuery();
			JSONObject ks =  new JSONObject();
			while(rs.next()) {
				ks.put(rs.getInt(1), rs.getString(2));
			}
			js.put("statusCode", 200);
			js.put("languages", ks.toString());
		}catch(Exception ex) {
			js.put("statusCode", 500);
			js.put("Message", ex.getMessage());
		}
		response.getWriter().append(js.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
