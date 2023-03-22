package filters;

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
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;

import managers.Tools;



/**
 * Servlet Filter implementation class Authentication
 */

public class Authentication extends HttpFilter implements Filter {
       
    /**
     * @see HttpFilter#HttpFilter()
     */
    public Authentication() {
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
		System.out.println("---------------");
		Cookie[] cookies = ((HttpServletRequest)request).getCookies();
		if(cookies==null) {
			cookies=new Cookie[0];
		}
		
		String session=null;
		for(Cookie cookie : cookies) {
			if(cookie.getName().equals("sessionId")) {
				session=cookie.getValue();
				break;
			}
		}
		System.out.println(session!= null);
		if(session!= null) {
			ResultSet rs=null;
			try {
				PreparedStatement psmt = Tools.DB.prepareStatement("select * from sessions where session = ?;");
				psmt.setString(1, session);
				rs=psmt.executeQuery();
				System.out.println(session);
				if(rs.next()) {
					System.out.println(rs.getInt(1));
					request.setAttribute("Id", ""+rs.getInt(1));
					chain.doFilter(request, response);
				}else {
					JSONObject json = new JSONObject();
					json.put("statusCode","401");
					json.put("Message", "Session expires");
					response.getWriter().append(json.toString());
				}
			} catch (SQLException e) {
				JSONObject json = new JSONObject();
				json.put("statusCode","500");
				json.put("Message", e.getMessage());
				response.getWriter().append(json.toString());
			}
		}else {
			JSONObject json = new JSONObject();
			json.put("statusCode","401");
			json.put("Message", "unauthorized");
			response.getWriter().append(json.toString());
		}
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
