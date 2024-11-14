package arc.nov.example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/RegistrationServlet")
public class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		String option = request.getParameter("action");
		System.out.println("Action: "+option);
		String filepath = getServletContext().getRealPath("WEB-INF/Registration.txt");
		
		if(option.equals("create")) {
			
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			String mobile = request.getParameter("mobile");
			
			writeToFile(filepath, name, email, mobile);
			displayRecords(out,filepath);
			
		} else if(option.equals("delete")) {
			
			String email = request.getParameter("email");
			deleteRecord(email,filepath);	
			displayRecords(out,filepath);
			
		} else if(option.equals("update")) {
			
			String oldemail = request.getParameter("oldemail");
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			String mobile = request.getParameter("mobile");
			
			String newdata = name +", "+email+", "+mobile;
			updateWrite(filepath, newdata, oldemail);
			displayRecords(out,filepath);
			
		} else if(option.equals("search")) {
			String searchterm = request.getParameter("searchterm");
			searchRecord(out,filepath,searchterm);
		}
	}

	private void writeToFile(String filepath, String name, String email, String mobile) throws IOException {
		try (FileWriter fw = new FileWriter(filepath, true)) {
			fw.write(name + "," + email + "," + mobile + "\n");
		}
		
	}

	private void displayRecords(PrintWriter out, String filepath) throws FileNotFoundException, IOException {
		out.println("<!doctype html>");
		out.println("<html>");
		out.println("<head> <title> Registration Data </title> </head>");
		out.println("<body>");
		out.println("<h2> All Registration Records </h2>");

		out.println("<table border='1'>");
		out.println("<tr> <th>Name</th> <th>Email</th> <th>Contact</th> <th>Action</th> </tr>");
		int totalrecords = 0;
		
		try(BufferedReader br = new BufferedReader(new FileReader(filepath))){
			String line="";
			while((line=br.readLine())!=null) {
				totalrecords++;
				String []fields = line.split(",");
				out.println("<tr>");
				for(String field : fields) {
					out.println("<td>"+field+"</td>");
				}
				out.println("<td>");
				out.println("<form action='RegistrationServlet' method='post' style='display:inline;'>");
				out.println("<input type = 'hidden' name='action' value='delete' >");
				out.println("<input type='hidden' name='email' value='" + fields[1] + "'>");
				out.println("<input type='submit' value='Delete'>");
				out.println("</form>");
				out.println("<button onclick=\"showUpdateForm('"+ fields[0]+"', '"+fields[1]+"', '"+ fields[2]+"')\">Update</button>");
				out.println("</td>");
				
				out.println("</tr>");
			}
			
		}
		out.println("</table>");
		out.println("<p>Total Record Count: " + totalrecords + " </p>");
		out.println("<h2>Search</h2>");
		out.println("<form action='RegistrationServlet'  method='post'>");
		out.println("<input type='hidden' name='action' value='search'>");
		out.println("Search: <input type='text' name='searchterm' placeholder='Enter search term'>");
		out.println("<input type='submit' value='Search'>");
		out.println("</form>");
		
		
		//for update hidden by default
		out.println("<div id='updateForm' style='display:none;'>");
		out.println("<h3> Update Records </h3>");
		out.println("<form action='RegistrationServlet' method='post'>");
		out.println("<input type='hidden' name='action' value='update'>");
		out.println("<input type='hidden' name='oldemail' id='oldemail'>");
		out.println("Name: <input type='text' name='name' id='updatename'><br> <br>");
		out.println("Email: <input type='text' name='email' id='updateemail'><br> <br>");
		out.println("Contact: <input type='text' name='mobile' id='updatemobile'><br> <br>");
		out.println("<input type='submit' value='Update'>");		
		out.println("</div>");
		
		// script for updation operation
		out.println("<script type = 'text/javascript'>");
		out.println("function showUpdateForm(name, email, mobile){");
		out.println("alert('update started!!!!');");
		out.println("document.getElementById('updateForm').style.display='block'");
		out.println("document.getElementById('updatename').value=name;");
		out.println("document.getElementById('updateemail').value=email;");
		out.println("document.getElementById('updatemobile').value=mobile;");
		out.println("document.getElementById('oldemail').value=email;");
		out.println("}");
		out.println("</script>");
		
		out.println("<br> <a href='index.html'> Register another name </a>");
		out.println("</body></html>");
		
		
	}

	private void deleteRecord(String email, String filepath) throws FileNotFoundException, IOException {
		List<String> records = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
			String line = "";
			while ((line = br.readLine()) != null) {
				String[] fields = line.split(",");
				if (!fields[1].equals(email)) {
					records.add(line);
				}
			}
		}

		try (FileWriter fw = new FileWriter(filepath)) {
			for (String record : records) {
				fw.write(record + "\n");
			}
		}
		
	}

	private void updateWrite(String filepath, String newdata, String oldemail) throws IOException {
		List<String> records = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
			String line = "";
			while ((line = br.readLine()) != null) {
				if (line.split(",")[1].equals(oldemail))
					records.add(newdata);
				else
					records.add(line);
			}
		}

		try (FileWriter fw = new FileWriter(filepath)) {
			for (String record : records) {
				fw.write(record + "\n");
			}
		}
		
	}

	private void searchRecord(PrintWriter out, String filepath, String searchterm) throws FileNotFoundException, IOException {
		out.println("<!doctype html>");
		out.println("<html>");
		out.println("<head> <title> search operation </title> </head>");
		out.println("<body>");
		out.println("<h2> Records </h2>");
		
		out.println("<table border='1'>");
		out.println("<tr> <th>Name</th> <th>Email</th> <th>Contact</th> <th>Action</th> </tr>");
		int totalrecords = 0;

		try(BufferedReader br = new BufferedReader(new FileReader(filepath))){
			
			String line="";
			while((line = br.readLine())!=null) {
				totalrecords++;
				if(line.toLowerCase().contains(searchterm.toLowerCase())) {
					String [] fields = line.split(",");
					for(String field : fields) {
						out.println("<td>"+field+"</td>");
					}
					out.println("<td>");
					out.println("<form action='RegistrationServlet' method='post'>");
					out.println("<input type='hidden' name='action' value='delete'>");
					out.println("<input type='hidden' name='email' value='"+ fields[1]+"'>");
					out.println("<input type='submit' value='Delete'");
					out.println("</form>");
					
					out.println("<form action='RegistrationServlet' method='post'>");
					out.println("<input type='hidden' name='action' value='update'>");
					out.println("<input type='hidden' name='data' value='" + line + "'>");
					out.println("<input type='submit' value='Update'>");
					out.println("</form>");
					out.println("</td>");

					out.println("</tr>");
				}
			}
		}
		out.println("</table>");
		
		out.println("<br> <a href='index.html'>Register another Name</a>");

		out.println("</body> </html>");
		
	}
	

}
