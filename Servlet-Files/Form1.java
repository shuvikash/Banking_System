package co.edureka;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
public class Form1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public Form1() {
        super();
    }
   static Statement st;
   public void init(ServletConfig config) throws ServletException
    {
    	try
    	{
    		Class.forName("com.mysql.jdbc.Driver");
    		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/banking_system","root","123456789");
    		st=con.createStatement();
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		System.out.println("error");
    	}
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		HttpSession session=request.getSession();
		String uid=request.getParameter("userid");
		String pwd=request.getParameter("password");
		session.setAttribute("uid",uid);  
		session.setAttribute("pwd",pwd);
		String id;
		try
		{
			ResultSet rs=st.executeQuery("select id from user_pass where userid=\'"+uid+"\'and password=\'"+pwd+"\';");
			if(rs.next())
			{
				id = rs.getString(1);System.out.println("id="+id);
				if(id.equals("3"))
				{
					RequestDispatcher rd=request.getRequestDispatcher("admin.html");
					rd.forward(request,response);
				}
				else{
				session.setAttribute("id",id); 
				RequestDispatcher rd=request.getRequestDispatcher("Main_Menu.html");
				rd.forward(request,response);}
			}
			else
			{
				out.println("<p style=\" color: yellow;font-size:20px;text-align:right\">Invalid Username/Password</p>");
                RequestDispatcher rd=request.getRequestDispatcher("Log_in.html");
				rd.include(request,response);
			}
		}
		catch(Exception e)
    	{
    		e.printStackTrace();
    	}
		
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
