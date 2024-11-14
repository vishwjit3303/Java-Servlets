package arc.nov.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Register")
public class Register extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String searchMobile = request.getParameter("mobile");
        boolean found = false;
        
        String filepath = getServletContext().getRealPath("WEB-INF/Registration.txt");
        
        out.println("<!doctype html>");
        out.println("<html>");
        out.println("<head> <title>Search Result</title> </head>");
        out.println("<body>");
        out.println("<h2>Search Result for Mobile: " + searchMobile + "</h2>");
        
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length > 2 && fields[2].trim().equals(searchMobile)) {
                    out.println("<table border='1'>");
                    out.println("<tr> <th>Name</th> <th>Email</th> <th>Contact</th> </tr>");
                    out.println("<tr>");
                    for (String field : fields) {
                        out.println("<td>" + field + "</td>");
                    }
                    out.println("</tr>");
                    out.println("</table>");
                    found = true;
                    break;
                }
            }
        }
        
        if (!found) {
            out.println("<p>No record found for mobile number: " + searchMobile + "</p>");
        }
        
        out.println("<br><a href='contact.html'>Search another Contact</a>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");

        String data = name + ", " + email + ", " + mobile + "\n";

        String filepath = getServletContext().getRealPath("WEB-INF/Registration.txt");
        System.out.println(filepath);

        // Append the new registration data to the file
        try (FileWriter fw = new FileWriter(filepath, true)) { // Append mode
            fw.write(data);
        }

        out.println("<!doctype html>");
        out.println("<html>");
        out.println("<head> <title>Registration Data</title> </head>");
        out.println("<body>");
        out.println("<h2>All Registration Records</h2>");
        
        // Display all registration records
        out.println("<table border='1'>");
        out.println("<tr> <th>Name</th> <th>Email</th> <th>Contact</th> </tr>");
        int totalRecords = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                totalRecords++;
                String[] fields = line.split(",");
                out.println("<tr>");
                for (String field : fields) {
                    out.println("<td>" + field + "</td>");
                }
                out.println("</tr>");
            }
        }
        out.println("</table>");
        out.println("<p>Total Record Count: " + totalRecords + "</p>");
        out.println("<br><a href='index.html'>Register another Name</a>");
        out.println("</body></html>");
    }
}
