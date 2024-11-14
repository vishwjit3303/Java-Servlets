package arc.nov.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/AlphaRecords")
public class AlphaRecords extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");

        String data = name + ", " + email + ", " + mobile + "\n";

        String filepath = getServletContext().getRealPath("WEB-INF/Registration.txt");

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
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                totalRecords++;
                lines.add(line);
                //String[] fields = line.split(",");
            }
        }
        Collections.sort(lines, new Comparator<String>() {
            @Override
            public int compare(String line1, String line2) {
                String name1 = line1.split(",")[0].trim();
                String name2 = line2.split(",")[0].trim();
                return name1.compareToIgnoreCase(name2);
            }
        });
        for (String line : lines) {
            String[] fields = line.split(",");
            out.println("<tr>");
            for (String field : fields) {
                out.println("<td>" + field.trim() + "</td>");
            }
            out.println("</tr>");
        }
        out.println("</table>");
        out.println("<p>Total Record Count: " + totalRecords + "</p>");
        out.println("<br><a href='index.html'>Register another Name</a>");
        out.println("</body></html>");


	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}
