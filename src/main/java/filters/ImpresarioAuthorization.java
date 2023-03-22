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
import javax.servlet.http.HttpFilter;

import org.json.simple.JSONObject;

import managers.Tools;

/**
 * Servlet Filter implementation class ImpresarioAuthorization
 */

public class ImpresarioAuthorization extends HttpFilter implements Filter {
       
    /**
     * @see HttpFilter#HttpFilter()
     */
    public ImpresarioAuthorization() {
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
//		System.out.println("impauth");
		String Id = (String) request.getAttribute("Id");
		int id = Integer.parseInt((Id));
		try {
			PreparedStatement psmt = Tools.DB.prepareStatement("select role from role where userId=?;");
			psmt.setInt(1, id);
			ResultSet rs=psmt.executeQuery();
			if(rs.next()) {
				if(rs.getString(1).equals("O")) {
					request.setAttribute("Id", id);
					chain.doFilter(request, response);
				}else {
					JSONObject json = new JSONObject();
					json.put("statusCode","401");
					json.put("Message", "Session expires");
					response.getWriter().append(json.toString());
				}
				
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
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
