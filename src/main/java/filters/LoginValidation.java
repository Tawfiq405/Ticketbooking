package filters;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Servlet Filter implementation class LoginValidation
 */

public class LoginValidation extends HttpFilter implements Filter {
       
    /**
     * @see HttpFilter#HttpFilter()
     */
    public LoginValidation() {
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
		System.out.println("Loginvalidation");
		if(request.getParameter("email")!=null){
			request.setAttribute("email", ""+request.getParameter("email"));
			request.setAttribute("otp", ""+request.getParameter("otp"));
			chain.doFilter(request, response);
		}else {
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
			if(json!=null) {
				String email = (String)json.get("email");
				System.out.println(email);
				if(email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")){
					request.setAttribute("email", email);
					chain.doFilter(request, response);
				}else {
					System.out.println("else");
					resobj.put("statusCode","500");
					resobj.put("Message","error occured plz give valid input !!");
					res.getWriter().append(resobj.toString());
				}
			}
		}catch(ParseException pe) {
			
			resobj.put("statusCode","500");
			resobj.put("Message",pe.getMessage());
			res.getWriter().append(resobj.toString());			
		}
		
		
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
