package com.luv2code.web.jdbc;


import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StudentDBUtil
{
	private DataSource dataSource;
	
	public StudentDBUtil(DataSource theDataSource)
	{
		dataSource = theDataSource;
	}
	public List<Student> getStudents() throws Exception
	{
		List<Student> students = new ArrayList<Student>();
		Connection myConn=null;
		Statement myStmt=null;
		ResultSet myRs=null;
		
		try {
		//get a connection
		myConn = dataSource.getConnection();
		
		//create sql statement
		String sql = "select * from student order by last_name";
		myStmt = myConn.createStatement();
		
		//execute query
		myRs = myStmt.executeQuery(sql);
		
		//process result set
		while(myRs.next())
		{
			//retrieve data from the result row
			int id=myRs.getInt("id");
			String firstName= myRs.getString("first_name");
			String lastName= myRs.getString("last_name");
			String email= myRs.getString("email");
			
			//create new student object
			Student tempStudent= new Student(id,firstName,lastName,email);
			
			//add it to the list of students
			students.add(tempStudent);
		}
		
		//close JDBC objects
		
			return students;
		}
		finally
		{
				//close JDBC objects
				close(myConn, myStmt, myRs);
		}

	}
	private void close(Connection myConn, Statement myStmt, ResultSet myRs) 
	{
		try
		{
			if(myRs!=null)
			{
				myRs.close();
			}
			if(myStmt!=null)
			{
				myStmt.close();
			}
			if(myConn!=null)
			{
				myConn.close();
			}
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
		}
		
	}
	public void addStudent(Student newStudent) throws Exception
	{
		Connection myConn=null;
		PreparedStatement myStmt=null;
		
		try 
		{
			//get db connection
			myConn = dataSource.getConnection();
			
			//create sql for insert
			String sql="insert into student"
						+"(first_name, last_name, email)"
						+"values(?,?,?)";
			myStmt= myConn.prepareStatement(sql);
			
			//set param values on the sql
			myStmt.setString(1, newStudent.getFirstName());
			myStmt.setString(2, newStudent.getLastName());
			myStmt.setString(3, newStudent.getEmail());
			
			//execute the sql insert
			myStmt.execute();

		}
		finally
		{
			//clean up JDBC
			close(myConn,myStmt,null);
		}
		
	}
	public Student getStudent(String theStudentID) throws Exception
	{
		Student theStudent= null;
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		int studentId;
		try 
		{
			//convert student id to int
			studentId=Integer.parseInt(theStudentID);
			
			//get connection to the db
			myConn=dataSource.getConnection();
			
			//create sql to get selected student
			String sql="select * from student where id=?";
			
			//create prepared statement
			myStmt = myConn.prepareStatement(sql);
			
			//set params
			myStmt.setInt(1, studentId);
			
			//execute statement
			myRs = myStmt.executeQuery();
			
			//retrieve data from the result set row
			if(myRs.next())
			{
				String firstName= myRs.getString("first_name");
				String lastName= myRs.getString("last_name");
				String email= myRs.getString("email");
						
				//use the studentId during construction
				theStudent = new Student(studentId,firstName,lastName, email);
			}
			else
			{
				throw new Exception("Could not find the student id:"+studentId);
			}
			
			return theStudent;
		}
		finally
		{
			//clean up JDBC obj
			close(myConn, myStmt, myRs);
		}
	}
	public void updateStudent(Student theStudent) throws Exception
	{
		
			Connection myConn=null;
			PreparedStatement myStmt=null;
		try 
		{
			//get db connection
			myConn= dataSource.getConnection();
			
			//sql update stmt
			String sql = "update student "
						 +"set first_name=?,last_name=?,email=? "
						 +"where id=?";
			
			//prepare statement
			myStmt= myConn.prepareStatement(sql);
			
			//set params
			myStmt.setString(1,theStudent.getFirstName());
			myStmt.setString(2,theStudent.getLastName());
			myStmt.setString(3,theStudent.getEmail());
			myStmt.setInt(4, theStudent.getId());
			
			//execute SQL statement
			myStmt.execute();
		}
		finally
		{
			close(myConn,myStmt, null);
		}
		
	}
	public void deleteStudent(String theStudentId) 
	throws Exception
	{	
		Connection myConn=null;
		PreparedStatement MyStmt=null;
		
		try
		{
			//convert student id to integer
			int studentId = Integer.parseInt(theStudentId);
			
			//get the connection to the db
			myConn = dataSource.getConnection();
			
			//create the sql statement
			String sql = "delete from student where id=?";
			
			//prepare statement
			MyStmt = myConn.prepareStatement(sql);
			
			//set params
			MyStmt.setInt(1, studentId);
			
			//execute the sql statement
			MyStmt.execute();
		}
		finally
		{
			//clean the jdbc objects
			close(myConn, MyStmt, null);
		}
	}
}
