package arc.nov.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RegistrationServlet
 */
@WebServlet("/RegistrationServlet")
public class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String URL = "jdbc:mysql://localhost:3306/crud_db";
	private static final String USER = "root";
	private static final String PASSWORD = "Archer@1234";
	


	@Override
	public void init() throws ServletException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Drivers loaded successfully");
		} catch(Exception e) {
			throw new ServletException("Unable to load drivers");
		}
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		String action=request.getParameter("action");
		if("delete".equals(action))
		{
			doDelete(request, response);
		}
		
		try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)){
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM users");
			
			out.println("<!doctype html>");
			out.println("<html><head><title>CRUD Application</title></head>");
			out.println("<body>");
			// data display
			out.println("<table border = '1'>");
			out.println("<tr>");
			out.println("<th>Id</th> <th>Name</th> <th>Email</th> <th>Contact</th> <th>Action</th>");
			out.println("</tr>");
			
			while(rs.next()) {
				out.println("<tr>");
				out.println("<td>"+rs.getString("id")+"</td>");
				out.println("<td>"+rs.getString("name")+"</td>");
				out.println("<td>"+rs.getString("email")+"</td>");
				out.println("<td>"+rs.getString("mobile")+"</td>");
				
				out.println("<td>");
				out.println("<a href='EditServlet?action=edit&id="+ rs.getInt("id")+"'> Edit | </a>");
				out.println("<a href='RegistrationServlet?action=delete&id="+ rs.getInt("id")+"'> Delete </a>");
				
				out.println("</td>");
				
				out.println("</tr>");
				
			}
			out.println("</table>");
			out.println();
			
			//input                                                             
			out.println("<h2>Enter your details</h2>");
			out.println("<input type='hidden' name='action' value='insert'>");
			out.println("<form action='RegistrationServlet' method='post'>");
			out.println(" Name: <input type='text' name='name' placeholder='Enter your name'> <br><br>");
			out.println(" Email: <input type='email' name='email' placeholder='Enter your Email'> <br><br>");
			out.println(" Contact: <input type='text' name='mobile' placeholder='Enter your mobile'> <br><br>");
			out.println("<input type='submit' value='Register'> <br><br>");
			out.println("</form>");
			out.println("</body></html");
					
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new ServletException("Data display error");
		}
	}
		

		@Override
		protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
			try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
				int id=Integer.parseInt(request.getParameter("id"));

				PreparedStatement pstmt = conn.prepareStatement("DELETE FROM users WHERE id=?");
				pstmt.setInt(1, id);
				pstmt.executeUpdate();
			}
			catch (Exception e) {
				e.printStackTrace();
				throw new ServletException("Data Deletion Error");
			}
		}
		


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//int id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String mobile = request.getParameter("mobile");
		
		
		
		System.out.println("In Post(): "+name+", "+email+", "+mobile); 
		
		
		//String option = request.getParameter("action");
		
		try {
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			System.out.println("Connection established");
			
			
			String sql = "INSERT INTO users (name,email,mobile)VALUES(?,?,?)";
			
			PreparedStatement statement = conn.prepareStatement(sql);
			
			statement.setString(1, name);
			statement.setString(2, email);
			statement.setString(3, mobile);
			
			statement.executeUpdate();
			System.out.println("Record loaded successfully");
			
			
		} catch (SQLException e) {

			e.printStackTrace();
		}
		
		
	}

}
