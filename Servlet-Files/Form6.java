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
public class Form6 extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public Form6() {
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
		int accno=Integer.parseInt(request.getParameter("accno"));
		int amount=Integer.parseInt(request.getParameter("amount"));
		String uid=request.getParameter("userid");
		String pwd=request.getParameter("password");
		String deposit=request.getParameter("dep");
		String delete=request.getParameter("del");
		String create=request.getParameter("cre");
		int id,y,z;
		if(deposit!=null){
		try
		{
			ResultSet rs=st.executeQuery("select a.id,a.amount,c.amount from account_details a,credit_card c where a.id=c.id and accountnumber="+accno+";");
			if(rs.next())
			{
				id = Integer.parseInt(rs.getString(1));System.out.println("id="+id);
				y = Integer.parseInt(rs.getString(2));
				z = Integer.parseInt(rs.getString(3));
				
				String query1,query2,query3;
				if(z<=amount)
				{
					query1= "UPDATE account_details set amount = "+ "amount+"+ amount+" where id=" + id + " and accounttype=\'Savings Account\';";
					query2="insert into trans_detail values("+id+",sysdate(),\'Self Deposit\',null,null,"+amount+","+(y+amount)+");";
					query3 = "UPDATE credit_card set amount = "+ 0+" where id=" + id + ";";
				}
				else{
					z=z-amount;
					query1= "UPDATE account_details set amount = "+ "amount+"+amount+" where id=" + id + " and accounttype=\'Savings Account\';";
					query2="insert into trans_detail values("+id+",sysdate(),\'Self Deposit\',null,null,"+amount+","+(y+amount)+");";
					query3 = "UPDATE credit_card set amount = "+ z+" where id=" + id + ";";}
				System.out.println(query1);
	        	int val = st.executeUpdate(query1);
	        	System.out.println(query2);
	        	val = st.executeUpdate(query2);
	        	System.out.println(query3);
	        	val = st.executeUpdate(query3);

				RequestDispatcher rd=request.getRequestDispatcher("admin.html");
				rd.include(request,response);
				out.println("<p style=\" color: red;font-size:20px;text-align:center\">Deposit Successfully</p>");
			}
			else
			{
                RequestDispatcher rd=request.getRequestDispatcher("admin.html");
				rd.include(request,response);
				out.println("<p style=\" color: yellow;font-size:20px;text-align:center\">Account Does Not Exist</p>");
			}
		}
		catch(Exception e)
    	{
    		e.printStackTrace();
    	}
		}
		else if(delete!=null)
		{
			try 
	        {
				ResultSet rs=st.executeQuery("select id from user_pass where userid=\'"+uid+"\'and password=\'"+pwd+"\';");
				if(rs.next())
				{
	        	String query = "delete from user_pass where userid=\'"+uid+"\' and password=\'"+pwd+"\';";
	        	System.out.println(query);
	        	int val = st.executeUpdate(query);
	        	RequestDispatcher rd=request.getRequestDispatcher("admin.html");
				rd.include(request,response);
				out.println("<p style=\" color: red;font-size:20px;text-align:center\">User Deleted.</p>");
				}
				else{
					out.println("<p style=\" color: red;font-size:20px;text-align:center\">No User Found!!</p>");
					RequestDispatcher rd=request.getRequestDispatcher("admin.html");
					rd.include(request,response);}
	        } 
	        catch (Exception e)
	        {
	        	//e.printStackTrace();
				out.println("<p style=\" color: red;font-size:20px;text-align:center\">Something Went Wrong!!</p>");
				RequestDispatcher rd=request.getRequestDispatcher("admin.html");
				rd.include(request,response);

	        }
		}
		else
		{
			try 
	        {
				ResultSet rs=st.executeQuery("select id from user_pass where userid=\'"+uid+"\'and password=\'"+pwd+"\';");
				if(!rs.next())
				{
	        	String query = "insert into user_pass values(\'"+uid+"\',\'"+pwd+"\',default);";
	        	System.out.println(query);
	        	int val = st.executeUpdate(query);
	        	RequestDispatcher rd=request.getRequestDispatcher("admin.html");
				rd.include(request,response);
				out.println("<p style=\" color: red;font-size:20px;text-align:center\">User Created.</p>");
				}
				else{
					out.println("<p style=\" color: red;font-size:20px;text-align:center\">User Already Exists!!</p>");
					RequestDispatcher rd=request.getRequestDispatcher("admin.html");
					rd.include(request,response);}
	        } 
	        catch (Exception e)
	        {
	        	//e.printStackTrace();
				out.println("<p style=\" color: red;font-size:20px;text-align:center\">Something Went Wrong!!</p>");
				RequestDispatcher rd=request.getRequestDispatcher("admin.html");
				rd.include(request,response);

	        }	
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
