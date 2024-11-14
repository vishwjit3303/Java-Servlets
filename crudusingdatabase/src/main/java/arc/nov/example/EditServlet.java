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


@WebServlet("/EditServlet")
public class EditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String URL = "jdbc:mysql://localhost:3306/crud_db";
	private static final String USER = "root";
	private static final String PASSWORD = "Archer@1234";
	
	@Override
	public void init() throws ServletException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Drivers loaded Sucessfully");
		} catch (Exception e) {
			throw new ServletException("Unable to load Drivers");
		}

	}
    

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		
		try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM users");
			out.println("<!doctype html>");
			out.println("<html> <head> <title> CRUD Application </title> </head>");
			out.println("<body>");
			// Data Display
			out.println("<table border='1'>");
			
			out.println("<tr>");
			out.println("<th> ID </th><th> Name </th> <th> Email </th> <th> Contact </th> <th> Action </th>");
			out.println("</tr>");
			while(rs.next()) {
				out.println("<tr>");		
				out.println("<td> "+rs.getInt("id")+" </td>");		
				out.println("<td> "+rs.getString("name")+" </td>");
				out.println("<td> "+rs.getString("email")+" </td>");
				out.println("<td> "+rs.getString("mobile")+" </td>");
				
				out.println("<td> ");
				out.println("<a href='EditServlet?action=edit&id="+rs.getInt("id")+"'> Edit | </a> ");
				out.println("<a href='RegistrationServlet?action=delete&id="+rs.getInt("id")+"'> Delete </a> ");
				out.println("</td>");				
				out.println("</tr>");
			}
					
			out.println("</table>");
			out.println();
			// Data Input
			out.println("<h2> Update the Details</h2>");
			out.println("<form action='EditServlet' method='post'>");
			
				int id=Integer.parseInt(request.getParameter("id"));
				System.out.println("Uddate ID: "+id);
				PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users WHERE id=?");
				pstmt.setInt(1, id);
				ResultSet rst =  pstmt.executeQuery();
				System.out.println("resultSet Ready");

			while(rst.next()) {
				out.println("<input type='hidden' name='id'  value='"+id+"'><br><br>");
				out.println("Name: <input type='text' name='name'  value='"+rst.getString("name")+"'><br><br>");
				out.println("Email: <input type='email' name='email' value='"+rst.getString("email")+"'><br><br>");
				out.println("Contact: <input type='text' name='mobile' value='"+rst.getString("mobile")+"'><br><br>");
			}
			out.println("<input type='submit' value='Confirm'>");
			out.println("</form>");
			out.println("</body></html>");			
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new ServletException("Data Display Error");
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String mobile = request.getParameter("mobile");
		System.out.println("In up_doPost(): "+id + name + "," + email + "," + mobile);
		try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
			PreparedStatement pstmt = conn.prepareStatement("UPDATE users SET name=?, email=?, mobile=? WHERE id=?");
			pstmt.setString(1, name);
			pstmt.setString(2, email);
			pstmt.setString(3, mobile);
			pstmt.setInt(4, id);
			pstmt.executeUpdate();
			
			response.sendRedirect("RegistrationServlet");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
