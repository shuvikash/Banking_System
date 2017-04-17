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
public class Form5 extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public Form5() {
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
		String name=request.getParameter("name");
		String cardno=request.getParameter("cardnumber");
		String cvv=request.getParameter("cvv");
		String amt=request.getParameter("amount");
		int amount1;
		String abc;int z,y;
		try
		{
			if(amt.matches("[0-9]+"))
			{
				amount1=Integer.parseInt(amt);
			ResultSet rs=st.executeQuery("select * from credit_card where id=" + idn + " and cardnumber=" + cardno +" and cvv2cvc2=" + cvv +" and name=\'" + name + "\';");
			
			if(rs.next())
			{
				ResultSet rs1=st.executeQuery("select c.amount,a.amount from credit_card c,account_details a where c.id=a.id and c.id=" + idn + " and accounttype=\'Savings Account\';");
				if(rs1.next())
				{
					abc = rs1.getString(1);
					z=Integer.parseInt(abc);
					abc=rs1.getString(2);
					y=Integer.parseInt(abc);
					if((z+amount1)<100000){
						   try 
					        {
					        	String query = "UPDATE credit_card set amount = "+ "amount+"+ amount1+" where id=" + idn + ";";
					        	String query1= "UPDATE account_details set amount = "+ "amount-"+ amount1+" where id=" + idn + " and accounttype=\'Savings Account\';";
					        	y=y-amount1;
					        	String query2="insert into trans_detail values("+idn+",sysdate(),\'Withdrawal from ATM\',null,"+amount1+",null,"+y+");";
					        	System.out.println(query);
					        	int val = st.executeUpdate(query);
					        	System.out.println(query1);
					        	val = st.executeUpdate(query1);
					        	System.out.println(query2);
					        	val = st.executeUpdate(query2);
					        	//confirmation msg
								RequestDispatcher rd=request.getRequestDispatcher("Authorize.html");
								rd.include(request,response);
								out.println("<p style=\" color: yellow;font-size:20px;text-align:center\">Your Transaction is Approved</p>");
					        } 
					        catch (Exception e)
					        {
					        	e.printStackTrace();
					        }
					}
					else
					{
						out.println("<p style=\" color: yellow;font-size:20px;text-align:center\">Amount Limit Exceeds</p>");
						RequestDispatcher rd=request.getRequestDispatcher("Authorize.html");
						rd.include(request,response);
					}
				}
				else
				{
	                out.println("<p style=\" color: yellow;font-size:20px;text-align:center\">Your Transaction is Rejected</p>");
	                RequestDispatcher rd=request.getRequestDispatcher("Authorize.html");
					rd.include(request,response);
				}
				
			}
			else
			{
                RequestDispatcher rd=request.getRequestDispatcher("Authorize.html");
				rd.include(request,response);
				out.println("<p style=\" color: yellow;font-size:20px;text-align:center\">Your Transaction is Rejected</p>");
			}
			}
			else
		     {
		        out.println("<p style=\" color: black;font-size:20px;text-align:center\">Invalid Amount</p>");
		        RequestDispatcher rd=request.getRequestDispatcher("Authorize.html");
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
