package managers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DbConfig
 */
@WebServlet(urlPatterns = "/DbConfig",loadOnStartup = 0)
public class DbConfig extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DbConfig() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    

	@Override
	public void init() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Tools.DB = DriverManager.getConnection("jdbc:mysql://localhost:3306/MTbookings","tawfiq","Tawfiq$445");
			System.out.println("successfully connected to db");
		}catch(ClassNotFoundException | SQLException ex ) {
			ex.printStackTrace();
		}
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
