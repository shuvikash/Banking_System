package co.edureka;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
public class Form2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public Form2() {
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
		String id=(String)session.getAttribute("id");System.out.println("id="+id);
		int idn=Integer.parseInt(id);
		String name=request.getParameter("name");
		String dob=request.getParameter("dob");//out.println(dob);
		String address=request.getParameter("address");
		String emailid=request.getParameter("email");
		String accounttype,opp;
		opp=request.getParameter("acctype");
		if(opp.equals("Savings Account"))
			accounttype="Savings Account";
		else if(opp.equals("Current Accounts"))
			accounttype="Current Accounts";
		else if(opp.equals("Fixed Deposit"))
			accounttype="Fixed Deposit";
		else
			accounttype="Recurring Deposit";
		try
		{
			ResultSet rs=st.executeQuery("select id from account_details where id="+idn+" and accounttype=\'"+accounttype+"\';");
			if(!rs.next())
			{
			String query = "INSERT into account_details VALUES(" + "default" + ",\'" + name + "\'," + "str_to_date(\'"+dob+"\',\'%Y-%m-%d')" + ",\'" + address + "\',\'" + emailid + "\',\'" + accounttype + "\'," + "1000" + ","+idn+");";
			String query2,query3;
        	System.out.println(query);
        	int val = st.executeUpdate(query);
        	if(accounttype.equals("Savings Account")){
        		Random rand = new Random();
        		query3="insert into credit_card values("+idn+",\'"+name+"\',"+rand.nextInt(1000000)+","+rand.nextInt(1000)+","+0+");";
            	System.out.println(query3);
    	        val = st.executeUpdate(query3);	
			query2="insert into trans_detail values("+idn+",sysdate(),\'Deposit\',null,null,"+1000+","+1000+");";
        	System.out.println(query2);
	        val = st.executeUpdate(query2);}
        	RequestDispatcher rd=request.getRequestDispatcher("Account_Create.html");
			rd.include(request,response);
	        out.println("<p style=\" color: black;font-size:20px;text-align:center\">Account Created Successfully</p>");
	        /*String query1 = "select accountnumber from account_details where accounttype='loan accounts';";
	        ResultSet rs=st.executeQuery(query1);
	        if(rs.next())
            {
             out.println(rs.getString(1));
            }*/
			}
			else{
				out.println("<p style=\" color: red;font-size:20px;text-align:center\">Account Already Exists!!</p>");
				RequestDispatcher rd=request.getRequestDispatcher("Account_Create.html");
				rd.include(request,response);}
		}
		catch(Exception e)
    	{
			RequestDispatcher rd=request.getRequestDispatcher("Account_Create.html");
			rd.include(request,response);
			out.println("<p style=\" color: red;font-size:20px;text-align:center\">User Account is not Created</p>");
    		e.printStackTrace();
    	}
		
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
