package impresario;

import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import managers.Tools;

/**
 * Servlet implementation class suggetions
 */
@WebServlet("/cinimas/impresario/suggetions")
public class suggetions extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public suggetions() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String Id = ""+ request.getAttribute("Id");
		JSONObject res = new JSONObject();
		try {
			PreparedStatement ps= Tools.DB.prepareStatement("select screen.name,count(*) from screen inner join screenstructure on screen.userId=screenstructure.userId and screen.thId=screenstructure.thId and screen.scId=screenstructure.scId and screen.userId= ?  group by screen.userId,screen.thId,screen.scId order by count(*) desc ;");
			ps.setInt(1, Integer.parseInt(Id));
			ResultSet rs = ps.executeQuery();
			JSONObject jo = new JSONObject();
			while(rs.next()) {
				
				int count = rs.getInt(2);
				System.out.println(count);
				count = (count+100)-(count%100);
				if(((List<String>)jo.get(count))!= null){
					List<String> l = (List<String>) jo.get(count);
					l.add(rs.getString(1));
					jo.put(count, l);
				}else {
					List<String> l = new ArrayList<>();
					l.add(rs.getString(1));
					jo.put(count, l);
				}
				System.out.println(jo.toString());
			}
			ps = Tools.DB.prepareStatement("select showdate,maxcapacity,sum(showbalance) from impresariobuyedmovies where showdate between ? and ? and userId = ? group by showdate,maxcapacity order by showdate,maxcapacity desc;");
			ps.setDate(1, new Date(System.currentTimeMillis()+86400000));
			long a = System.currentTimeMillis() + (86400000*5);
			ps.setDate(2, new Date(a));
			ps.setInt(3, Integer.parseInt(Id));
			rs=ps.executeQuery();
			JSONObject jo1 = new JSONObject();
			while(rs.next()) {
				if(jo1.get(rs.getString(1)) != null) {
					JSONObject jo2 = (JSONObject) jo1.get(rs.getString(1));
					jo2.put(rs.getInt(2), rs.getInt(3));
					jo1.put(rs.getString(1), jo2);
				}else {
					JSONObject jo2 = new JSONObject();
					jo2.put(rs.getInt(2), rs.getInt(3));
					jo1.put(rs.getString(1), jo2);
				}
			}
			System.out.println(jo1);
			
				Date date = Date.valueOf(String.valueOf(new Date(System.currentTimeMillis()+86400000)));
				for(long i=date.getTime();i<date.getTime()+(86400000*5);i+=86400000) {
					if(!jo1.containsKey(String.valueOf(new Date(i)))) {
					jo1.put(String.valueOf(new Date(i)), null);
				}
				
			}
			
			System.out.println(jo1.toString()+"-----------");
			res.put("statusCode", "200");
			res.put("screen", jo);
			res.put("buyedshow", jo1);
			
			System.out.println(res.toString());
			response.getWriter().append(res.toString());
		}catch(Exception ex) {
			ex.printStackTrace();
			res.put("statusCode", "500");
			response.getWriter().append(res.toString());
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
