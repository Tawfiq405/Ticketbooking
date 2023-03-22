package filters;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import managers.Tools;

/**
 * Servlet Filter implementation class SigninValidation
 */

public class SignupValidation extends HttpFilter implements Filter {
       
    /**
     * @see HttpFilter#HttpFilter()
     */
    public SignupValidation() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String inp="";
		String jsoninput ="";
		BufferedReader reader = request.getReader();
		HttpServletResponse res = (HttpServletResponse)response;
		JSONObject resobj = new JSONObject();
		while((inp = reader.readLine())!=null) {
			jsoninput+=inp;
		}
		JSONParser parser = new JSONParser();
		JSONObject json = null;
		try {
			json = (JSONObject) parser.parse(jsoninput);
			System.out.println(json+"----------------");
		}catch(ParseException pe) {
			resobj.put("statusCode","500");
			resobj.put("Message","error occured plz give valid input !!");
			res.getWriter().append(resobj.toString());			
		}
		String name = (String)json.get("name");
		System.out.println(name);
		String number = (String)json.get("number");
		System.out.println(number);
		String email = (String)json.get("email");
		System.out.println(email);
		try {
			PreparedStatement ps = Tools.DB.prepareStatement("select * from user where email = ?");
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			if(!rs.next()) {
				
				if(email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$") && number.matches("^(?:\\+91|0)?[6789]\\d{9}$") ){
					json.put("statusCode",200);
					request.setAttribute("json", json.toString());
					chain.doFilter(request, response);
				}else {
					System.out.println("else");
					resobj.put("statusCode","500");
					resobj.put("email",!(email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")));
					resobj.put("number",!(number.matches("^(?:\\+91|0)?[6789]\\d{9}$")));
//					resobj.put("password",!(password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")));
					resobj.put("Message","error occured plz give valid input !!");
					res.getWriter().append(resobj.toString());
				}
			}else {
				resobj.put("email",true);
				resobj.put("number",!(number.matches("^(?:\\+91|0)?[6789]\\d{9}$")));
//				resobj.put("password",!(password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%^#*?&])[A-Za-z\\d@$#^!%*?&]{8,}$")));
				resobj.put("statusCode","500");
				resobj.put("Message","error occured plz give valid input");
				res.getWriter().append(resobj.toString());
			}
			
		}catch(SQLException e) {
			resobj.put("statusCode","500");
			resobj.put("Message",e.getMessage());
			res.getWriter().append(resobj.toString());
		}
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
