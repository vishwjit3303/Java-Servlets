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
 * Servlet implementation class IssuedServlet
 */
@WebServlet("/IssuedServlet")
public class IssuedServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String URL = "jdbc:mysql://localhost:3306/book_db";
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
		
		try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)){
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM book");
			
			out.println("<!doctype html>");
			out.println("<html><head><title>Library Application</title></head>");
			out.println("<body>");
			// data display
			out.println("<table border = '1'>");
			out.println("<tr>");
            out.println("<tr><th>Book ID</th><th>Book Name</th><th>Author</th><th>Quantity</th><th>Issued</th><th>Issued By (Student ID)</th></tr>");
			out.println("</tr>");
			
			while(rs.next()) {
				out.println("<tr>");
				out.println("<td>"+rs.getString("BookId")+"</td>");
				out.println("<td>"+rs.getString("Name")+"</td>");
				out.println("<td>"+rs.getString("Author")+"</td>");
				out.println("<td>"+rs.getString("Quantity")+"</td>");
				out.println("<td>"+rs.getString("Issued")+"</td>");
				 out.println("<td>" + rs.getInt("studentId") + "</td>");
				
				out.println("</td>");
				out.println("</tr>");
			}
			
			out.println("</table>");
			out.println("<a href='IssuedServlet'>Issue Book </a>");
			out.println();
			
			
			out.println("<h2>Enter Id for Issue Book</h2>");
			out.println("<form action='IssuedServlet' method='post'>");
			
			out.println(" Book Id: <input type='text' name='bookid' placeholder='Enter Book Id'> <br><br>");
            out.println("Student ID: <input type='text' name='studentid' placeholder='Enter Student ID'><br><br>");
			out.println("<input type='submit' value='Issue Book'> <br><br>");
			out.println("</form>");
			out.println("</body></html");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServletException("Data display error");
			
		}
			
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    int id = Integer.parseInt(request.getParameter("bookid"));
	    int studentId = Integer.parseInt(request.getParameter("studentid"));
	    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
	        System.out.println("Connection established");

	        // Check available quantity before issuing
	        String checkSql = "SELECT Quantity FROM book WHERE BookId = ?";
	        PreparedStatement checkStmt = conn.prepareStatement(checkSql);
	        checkStmt.setInt(1, id);
	        ResultSet rs = checkStmt.executeQuery();

	        if (rs.next()) {
	            int quantity = rs.getInt("Quantity");

	            if (quantity > 0) {
	                // Issue book if available
	                String sql = "UPDATE book SET Quantity = Quantity - 1, Issued = Issued + 1, studentId=? WHERE BookId = ?";
	                PreparedStatement statement = conn.prepareStatement(sql);
	                statement.setInt(1, studentId);
	                statement.setInt(2, id);
	                
	                statement.executeUpdate();
	                
	                System.out.println("Book issued successfully");
	            } else {
	                System.out.println("Book with ID " + id + " is not available for issuance.");

	            }
	        } 

	        response.sendRedirect("BookServlet");
	        

	    } catch (SQLException e) {
	    	e.printStackTrace();
			 response.setContentType("text/html");
		     PrintWriter out = response.getWriter();
		     out.println("<html><body>");
		     out.println("<h3>Error: Id not present</h3>");
		     out.println("<p>" + e.getMessage() + "</p>"); // Prints the SQL exception message
		     out.println("</body></html>");
	    }
	}
}