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


@WebServlet("/BookServlet")
public class BookServlet extends HttpServlet {
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
			out.println("<th>Id</th> <th>Book Name</th> <th>Author</th> <th>Quantity</th> <th>Issued</th> <th>Issued By (Student ID)</th>");

			out.println("</tr>");
			
			while(rs.next()) {
				out.println("<tr>");
				out.println("<td>"+rs.getString("BookId")+"</td>");
				out.println("<td>"+rs.getString("Name")+"</td>");
				out.println("<td>"+rs.getString("Author")+"</td>");
				out.println("<td>"+rs.getString("Quantity")+"</td>");
				out.println("<td>"+rs.getString("Issued")+"</td>");
				out.println("<td>"+rs.getString("StudentId")+"</td>");

				
				out.println("</td>");
				out.println("</tr>");
			}
			
			out.println("</table>");
			out.println("<a href='IssuedServlet'>Issue Book </a> <br><br>");
			out.println("<a href='StudentServlet'>Add Student</a><br><br>");
			out.println();
			
			
			out.println("<h2>Enter your details</h2>");
			out.println("<form action='BookServlet' method='post'>");
			
			out.println(" Book Name: <input type='text' name='name' placeholder='Enter Book Name'> <br><br>");
			out.println(" Author: <input type='text' name='author' placeholder='Enter Author Name'> <br><br>");
			out.println(" Ouantity: <input type='text' name='quantity' placeholder='Enter Quantity'> <br><br>");
			//out.println(" Issued: <input type='text' name='issued' placeholder='Enter Issued Number of Books'> <br><br>");
			//out.println(" Ouantity: <input type='text' name='quantity' placeholder='Enter Quantity'> <br><br>");
			out.println("<input type='submit' value='Add Book'> <br><br>");
			out.println("</form>");
			out.println("</body></html");
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServletException("Data display error");
			
		}
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		String author = request.getParameter("author");
		String quantity = request.getParameter("quantity");
		
		System.out.println("In Post(): "+name+", "+author+", "+quantity); 
		
		try {
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			System.out.println("Connection established");
			
			
			String sql = "INSERT INTO book (name,author,quantity, AvailableQuantity, Issued )VALUES(?,?,?,?,?)";
			
			PreparedStatement statement = conn.prepareStatement(sql);
			
			statement.setString(1, name);
			statement.setString(2, author);
			statement.setInt(3, Integer.parseInt(quantity));
			statement.setInt(4, Integer.parseInt(quantity));
			statement.setInt(5, 0);
			
			
			statement.executeUpdate();
			System.out.println("Record loaded successfully");
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
