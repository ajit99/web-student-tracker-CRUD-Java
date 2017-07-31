package com.luv2code.web.jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import java.sql.Statement;
import java.sql.Connection;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	
	//Define datasource/connection pool for resource injection
	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		//Step 1: Set up the print writer
		PrintWriter out = response.getWriter();
		response.setContentType("text/plain");
		
		//Step 2: Get a connection to the database
		Connection myConn=null;
		Statement myStmt=null;
		ResultSet myRs=null;
		
		try
		{
			myConn= dataSource.getConnection();
			
			//Step 3: Create a SQL statement
			String sql="select * from student";
			myStmt = myConn.createStatement();
			
			//Step 4: Execute that SQL statement
			myRs = myStmt.executeQuery(sql);
			
			//Step 5: Process the result set
			while(myRs.next())
			{
				String email = myRs.getString("email");
				String firstname = myRs.getString("first_name");
				out.println(firstname+" "+email);
			}
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
		}
		
	}

}
