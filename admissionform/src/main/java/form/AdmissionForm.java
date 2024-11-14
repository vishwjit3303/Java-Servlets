package form;

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

import com.oreilly.servlet.MultipartRequest;


@WebServlet("/AdmissionForm")
public class AdmissionForm extends HttpServlet {
	private static final long serialVersionUID = 1L;

    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		// TODO Auto-generated method stub
		String data_name=request.getParameter("name");
		String data_email=request.getParameter("email");
		String data_password = request.getParameter("password");
		String data_gender = request.getParameter("gender");
		String data_dob = request.getParameter("dob");

//		String[] languages = request.getParameterValues("languages");
//
//		StringBuilder langlist = new StringBuilder();
//		if (languages != null) {
//            for (String languages1 : languages) {
//                langlist.append(languages1);
//            }
//		}





		System.out.println("Name: "+data_name+"\n Email: "+data_email+"\n Pass: "+data_password+"\n Gender: "+data_gender);
		
		try (FileWriter filewriter = new FileWriter("D:/test/admission_data.txt", true)) {
//			filewriter.write("Name: "+ data_name + "\n");
//			filewriter.write("Email: "+ data_email+ "\n");
//			filewriter.write("Password: "+ data_password+ "\n");
//			filewriter.write("Gender: "+ data_gender+ "\n");
//			filewriter.write("DOB: "+ data_dob+ "\n");
////			filewriter.write("Language: "+ langlist+ "\n");
//			filewriter.write("--------------------------------------------------\n");
			
			
		} catch(Exception e) {
			e.printStackTrace();
			
		}


		out.println("<html><body>");
		out.println("<h2>Data Submitted Successfully..!!</h2>");
		out.println("<p> Data is:</p>");
		out.println("<p><b>Name:</b> " + data_name + "<br>");
		out.println("<b>Email:</b> " + data_email + "<br>");
		out.println("<b>Password:</b> " + data_password + "<br>");
		out.println("<b>Gender:</b> " + data_gender + "<br>");
		out.println("<b>DOB:</b> " + data_dob + "<br>");
//		out.println("<b>Language:</b>"+ langlist+"</p>");
		out.println("</body></html>");

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    response.setContentType("text/html");
	    PrintWriter out = response.getWriter();

	        MultipartRequest multipartRequest = new MultipartRequest(request, "D:/test");

			String data_name=multipartRequest.getParameter("name");
			String data_email=multipartRequest.getParameter("email");
			String data_password = multipartRequest.getParameter("password");
			String data_gender = multipartRequest.getParameter("gender");
			String data_dob = multipartRequest.getParameter("dob");
	
//			String[] languages = multipartRequest.getParameterValues("languages");
//
//			StringBuilder langlist = new StringBuilder();
//			if (languages != null) {
//	            for (String languages1 : languages) {
//	                langlist.append(languages1);
//	            }
//			}


	        String data = data_name + ", "+ data_email +", "+data_password+", "+data_gender+", "+data_dob+"\n";

	        System.out.println("Name: "+data_name+"\n Email: "+data_email+"\n Pass: "+data_password+"\n Gender: "+data_gender);
		
	        try (FileWriter filewriter = new FileWriter("D:/test/admission_data.txt", true)) {
//				filewriter.write("Name: "+ data_name + "\n");
//				filewriter.write("Email: "+ data_email+ "\n");
//				filewriter.write("Password: "+ data_password+ "\n");
//				filewriter.write("Gender: "+ data_gender+ "\n");
//				filewriter.write("DOB: "+ data_dob+ "\n");
//				filewriter.write("Skills: "+ skillsList+ "\n");
//				filewriter.write("Language: "+ langlist+ "\n");
//				filewriter.write("--------------------------------------------------\n");	
	        	filewriter.write(data);
				
				} catch(Exception e) {
					e.printStackTrace();
					
				}

//			out.println("<html><body>");
//			out.println("<h2>Data Submitted Successfully..!!</h2>");
//			out.println("<p> Data is:</p>");
//			out.println("<p><b>Name:</b> " + data_name + "<br>");
//			out.println("<b>Email:</b> " + data_email + "<br>");
//			out.println("<b>Password:</b> " + data_password + "<br>");
//			out.println("<b>Gender:</b> " + data_gender + "<br>");
//			out.println("<b>DOB:</b> " + data_dob + "<br>");
//			out.println("<b>Skills:</b> " + skillsList+ "<br>");
//			out.println("<b>Language:</b>"+ langlist+"</p>");
//			out.println("</body></html>");
			
			
			
			// read all records
			out.println("<!doctype html>");
			out.println("<html>");
			out.println("<head><title>Records </title></head>");
			out.println("<body>");
			out.println("<h2>All Registration Records </h2>");
			
			out.println("<table border='1'>");
			out.println("<tr> <th>Name</th> <th>Email</th> <th>Password</th> <th>Gender</th> <th>Date Of Birth</th>  </tr>");
			
			int totalrecords=0;
			
			try(BufferedReader br = new BufferedReader(new FileReader("D:/test/admission_data.txt"))){
				String line="";
				
				while((line=br.readLine())!=null) {
					totalrecords++;
					
					String[]fields = line.split(",");
					out.println("<tr>");
					for(String field : fields) {
						out.println("<td>"+field+"</td>");
					}
					out.println("</tr");
				}
			}
			out.println("</table>");
			out.println("<p>Total Records Count= "+totalrecords+"</p>");
			out.println("<br><a href='index.html'>Fill Another Admission Form</a>");
			out.println("</body></html>");			
	}
}

