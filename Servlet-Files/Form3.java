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

public class Form3 extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public Form3() {
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
		String accountno=request.getParameter("accountnumber");
		String amt=request.getParameter("amount");
		String debit=request.getParameter("deb");
		String credit=request.getParameter("cred");
		String abc;int z,y;
		if(amt.matches("[0-9]+")&&amt.length()<=10)
		{
		int amount1=Integer.parseInt(amt);
		if(debit !=null)
		{
			try
			{
				ResultSet rs1=st.executeQuery("select amount from account_details where id=" + idn + " and accounttype=\'Savings Account\';");
				if(rs1.next())
				{
					abc=rs1.getString(1);
					y=Integer.parseInt(abc);
				ResultSet rs=st.executeQuery("select amount from account_details where accountnumber=" + accountno + ";");
				if(rs.next())
				{
					abc = rs.getString(1);
					z=Integer.parseInt(abc);
					if(amount1<=z){
						   try 
					        {
					        	String query = "UPDATE account_details set amount = "+ "amount-"+ amount1+" where accountnumber=" + accountno + ";";
					        	String query1= "UPDATE account_details set amount = "+ "amount+"+ amount1+" where id=" + idn + " and accounttype=\'Savings Account\';";
					        	System.out.println(query);
					        	int val = st.executeUpdate(query);
					        	System.out.println(query1);
					        	val = st.executeUpdate(query1);
					        	ResultSet rs2=st.executeQuery("select amount from account_details where id=" + idn + " and accounttype=\'Savings Account\';");
					        	if(rs2.next()){
					        	abc=rs2.getString(1);
								y=Integer.parseInt(abc);}
					        	String query2="insert into trans_detail values("+idn+",sysdate(),\'Deposit\',null,null,"+amount1+","+y+");";
					        	System.out.println(query2);
					        	val = st.executeUpdate(query2);
					        	RequestDispatcher rd=request.getRequestDispatcher("Transactions.html");
								rd.include(request,response);
								out.println("<p style=\" color: red;font-size:20px;text-align:center\">Transactions Successfully</p>");
					        } 
					        catch (Exception e)
					        {
					        	e.printStackTrace();
					        }
					}
					else
					{
						out.println("<p style=\" color: yellow;font-size:20px;text-align:center\">Invalid Amount</p>");
						RequestDispatcher rd=request.getRequestDispatcher("Transactions.html");
						rd.include(request,response);
					}
				}
				else
				{
	                out.println("<p style=\" color: yellow;font-size:20px;text-align:center\">Invalid Account Number</p>");
	                RequestDispatcher rd=request.getRequestDispatcher("Transactions.html");
					rd.include(request,response);
				}
				}
			else
			{
                out.println("<p style=\" color: yellow;font-size:20px;text-align:center\">No Savings Account Found!</p>");
                RequestDispatcher rd=request.getRequestDispatcher("Transactions.html");
				rd.include(request,response);
			}
			}
			catch(Exception e)
	    	{
	    		e.printStackTrace();
	    	}
		}
		else
		{
			try
			{
				ResultSet rs=st.executeQuery("select amount from account_details where id=" + idn + " and accounttype=\'Savings Account\';");
				if(rs.next())
				{
					abc = rs.getString(1);
					z=Integer.parseInt(abc);
					if(amount1<=z){
					        try 
					        {
					        	String query = "UPDATE account_details set amount = "+ "amount+"+ amount1+" where accountnumber=" + accountno + ";";
					        	String query1= "UPDATE account_details set amount = "+ "amount-"+ amount1+" where id=" + idn + " and accounttype=\'Savings Account\';";
					        	System.out.println(query);
					        	int val = st.executeUpdate(query);
					        	System.out.println(query1);
					        	val = st.executeUpdate(query1);
					        	ResultSet rs3=st.executeQuery("select amount from account_details where id=" + idn + " and accounttype=\'Savings Account\';");
					        	if(rs3.next()){
					        	abc = rs3.getString(1);
								z=Integer.parseInt(abc);}
					        	String query2="insert into trans_detail values("+idn+",sysdate(),\'Cheque Withdrawal \',"+(Integer.parseInt(accountno)*100+idn)+","+amount1+",null,"+z+");";
					        	System.out.println(query2);
					        	val = st.executeUpdate(query2);
					        	RequestDispatcher rd=request.getRequestDispatcher("Transactions.html");
								rd.include(request,response);
								out.println("<p style=\" color: red;font-size:20px;text-align:center\">Transactions Successfully</p>");
					        } 
					        catch (Exception e)
					        {
					        	e.printStackTrace();
					        }
					}
					else
					{
						out.println("<p style=\" color: yellow;font-size:20px;text-align:center\">Invalid Amount</p>");
						RequestDispatcher rd=request.getRequestDispatcher("Transactions.html");
						rd.include(request,response);
					}
				}
				else
				{
	                out.println("<p style=\" color: yellow;font-size:20px;text-align:center\">No Savings Account Found!</p>");
	                RequestDispatcher rd=request.getRequestDispatcher("Transactions.html");
					rd.include(request,response);
				}
			}
			catch(Exception e)
	    	{
	    		e.printStackTrace();
	    	}
		}
		}
		else
	     {
	        out.println("<p style=\" color: black;font-size:20px;text-align:center\">Plz enter valid amount</p>");
	     }
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
