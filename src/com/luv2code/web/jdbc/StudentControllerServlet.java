package com.luv2code.web.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private StudentDBUtil studentUtil;
	
	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;
	
	@Override
	public void init() throws ServletException 
	{
		// TODO Auto-generated method stub
		super.init();
		//create an instance of student db util and pass conn/data source
		try
		{
			studentUtil = new StudentDBUtil(dataSource);
		}
		catch(Exception exc)
		{
			throw new ServletException(exc);
		}
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		//list the students..in MVC fashion
		try
		{
			//read the command param
			String theCommand= request.getParameter("command");
			
			if(theCommand==null)
			{
				theCommand="LIST";
			}
			
			//route the appropriate method
			switch(theCommand)
			{
				case "LIST"		: listStudents(request, response);break;
				case "ADD" 		: addStudents(request, response);break;
				case "LOAD"		: loadStudents(request,response);break;
				case "UPDATE"	:updateStudents(request,response);break;
				case "DELETE"	:deleteStudents(request,response);break;
				default	   		: listStudents(request, response);break;
			}		
		}
		catch (Exception e)
		{
			throw new ServletException(e);
		}
	}


	private void deleteStudents(HttpServletRequest request, HttpServletResponse response) 
	throws Exception
	{
		//read the student id from the form data
		String theStudentId= request.getParameter("studentID");
		
		//delete the student from the db
		studentUtil.deleteStudent(theStudentId);
		
		//send them back to the list users page
		listStudents(request,response);
	}


	private void updateStudents(HttpServletRequest request, HttpServletResponse response) 
	throws Exception
	{
		//read student info from form data
		int id = Integer.parseInt(request.getParameter("studentID"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		//create the student object
		Student theStudent = new Student(id,firstName,lastName,email);
		
		//perform the update on the database
		studentUtil.updateStudent(theStudent);
		
		//send them back to the list-students page
		listStudents(request,response);
		
	}


	private void loadStudents(HttpServletRequest request, HttpServletResponse response) 
	throws Exception
	{
		//read student id from the form data
		String theStudentID= request.getParameter("studentID");
		
		//get the student from the database
		Student theStudent = studentUtil.getStudent(theStudentID);
		
		//place student in the request attribute
		request.setAttribute("THE_STUDENT",theStudent);

		//send to jsp page: update-student-form.jsp
		RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp");
		dispatcher.forward(request, response);
	}


	private void addStudents(HttpServletRequest request, HttpServletResponse response) 
	throws Exception
	{
		//read student info from the form data
		String firstName=request.getParameter("firstName");
		String lastName=request.getParameter("lastName");
		String email=request.getParameter("email");
		
		//create new student object
		Student newStudent=new Student(firstName, lastName, email);
		
		//add the student to the database
		studentUtil.addStudent(newStudent);
		
		//send back to the main page(the student list)
		listStudents(request, response);
	}

	private void listStudents(HttpServletRequest request, HttpServletResponse response) 
	throws Exception
	{
		//get students from db util
		List<Student> students = studentUtil.getStudents();
		
		//add students to the request
		request.setAttribute("STUDENT_LIST", students);
		
		//send to the JSP page(view)
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list_students.jsp");
		dispatcher.forward(request, response);
	}

}
