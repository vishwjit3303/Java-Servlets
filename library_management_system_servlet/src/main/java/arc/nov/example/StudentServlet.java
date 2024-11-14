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

@WebServlet("/StudentServlet")
public class StudentServlet extends HttpServlet {
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Display form for adding new students
        out.println("<!doctype html>");
        out.println("<html><head><title>Student Management</title></head>");
        out.println("<body>");
        out.println("<h2>Add New Student</h2>");
        out.println("<form action='StudentServlet' method='post'>");
        out.println("Name: <input type='text' name='name' placeholder='Enter Student Name'><br><br>");
        out.println("Email: <input type='email' name='email' placeholder='Enter Student Email'><br><br>");
        out.println("<input type='submit' value='Add Student'><br><br>");
        out.println("</form>");

        // Display existing students in a table
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM student");

            out.println("<h2>Student List</h2>");
            out.println("<table border='1'>");
            out.println("<tr><th>Student ID</th><th>Name</th><th>Email</th><th>Actions</th></tr>");

            while (rs.next()) {
                int studentId = rs.getInt("StudentId");
                out.println("<tr>");
                out.println("<td>" + studentId + "</td>");
                out.println("<td>" + rs.getString("Name") + "</td>");
                out.println("<td>" + rs.getString("Email") + "</td>");
                out.println("<td><a href='IssuedServlet?studentId=" + studentId + "'>Issue Book</a></td>");
                out.println("</tr>");
            }
            out.println("</table>");
            out.println("</body></html>");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Error retrieving student data");
        }

        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO student (Name, Email) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.executeUpdate();

            System.out.println("Student added successfully");
            response.sendRedirect("StudentServlet");

        } catch (SQLException e) {
            e.printStackTrace();
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h3>Error: Unable to add student</h3>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("</body></html>");
        }
    }
}
