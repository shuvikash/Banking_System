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

import com.mysql.jdbc.ResultSetMetaData;

public class Form4 extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public Form4() {
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
		String m=(String)session.getAttribute("uid");
		String n=(String)session.getAttribute("pwd");
		String id=(String)session.getAttribute("id");//System.out.println("id="+id);
		int idn=Integer.parseInt(id);
		String startdate=request.getParameter("startdate");
		String enddate=request.getParameter("enddate");
		try
		{
			String query="select @rownum:=@rownum+1 as SI_NO,date as \'date(yyyy/mm/dd)\',description,chequeno,withdraw,deposit,available_balance from trans_detail,(select @rownum:=0) r where date>=str_to_date(\'"+startdate+"\',\'%Y-%m-%d\') and date<=str_to_date(\'"+enddate+"\',\'%Y-%m-%d\') and id="+idn+" order by date desc;";
			System.out.println(query);
			ResultSet rs=st.executeQuery(query);
			if(rs.next())
			{
				RequestDispatcher rd=request.getRequestDispatcher("Display_Statement.html");
				rd.include(request,response);
				 out.print("<table width=25% border=1>");
                 //out.print("<center><h1>Result:</h1></center>");                
                 /* Printing column names */
                 ResultSetMetaData rsmd=(ResultSetMetaData) rs.getMetaData();
                 out.print("<tr>");
                 out.print("<td>"+rsmd.getColumnName(1)+"</td>");
                    out.print("<td>"+rsmd.getColumnName(2)+"</td>");
                    out.print("<td>"+rsmd.getColumnName(3)+"</td>");
                    out.print("<td>"+rsmd.getColumnName(4)+"</td>");
                    out.print("<td>"+rsmd.getColumnName(5)+"</td>");
                    out.print("<td>"+rsmd.getColumnName(6)+"</td>");
                    out.print("<td>"+rsmd.getColumnName(7)+"</td></tr>");
                 do
                    {
                 out.print("<tr>");
                    out.print("<td>"+rs.getString(1)+"</td>");
                    out.print("<td>"+rs.getString(2)+"</td>");
                    out.print("<td>"+rs.getString(3)+"</td>");
                    out.print("<td>"+rs.getString(4)+"</td>");
                    out.print("<td>"+rs.getString(5)+"</td>");
                    out.print("<td>"+rs.getString(6)+"</td>");
                    out.print("<td>"+rs.getString(7)+"</td></tr>");
                 }while(rs.next());
                 out.print("</table>");

				
			}
			else
			{
                RequestDispatcher rd=request.getRequestDispatcher("Display_Statement.html");
				rd.include(request,response);
				out.println("<p style=\" color: yellow;font-size:20px;text-align:center\">No Record Found</p>");
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
