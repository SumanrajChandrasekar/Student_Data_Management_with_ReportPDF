package com.pro;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

	@WebServlet("/DeleteDepartmentData")
	public class DeleteDepartmentData extends HttpServlet {
		
		
		    private static final String DB_URL = "jdbc:mysql://localhost:3306/StudentRegister";
		    private static final String DB_USER = "sumanraj";
		    private static final String DB_PASS = "12345678";
		    
		    private static final String Delete_Query = "DELETE from Dept_List where ID=?";
		@Override
		protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

			
			res.setContentType("text/html");
		
			PrintWriter out = res.getWriter();
			
			int id=Integer.parseInt(req.getParameter("id"));

			
		
			
			
			out.println("<!DOCTYPE html>");
			out.println("<html><head><title>Delete Student List</title></head><body>");
			
			Connection con =null;
			PreparedStatement ps = null;
			
			try {
				
				
	            Class.forName("com.mysql.cj.jdbc.Driver");
	            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
	            ps = con.prepareStatement(Delete_Query);
						
						ps.setInt(1, id);
						
						int rowsAffected = ps.executeUpdate();

		                if (rowsAffected > 0) {
		                    res.sendRedirect("ViewDepartmentData");
		                } else {
		                    out.println("<h3 style='color:red'>Update Failed</h3>");
		                }
			    		
			} catch (ClassNotFoundException e) {
			    e.printStackTrace();
	      	    out.println("<h3 style='color:red;'>ClassNotFoundException: " + e.getMessage() + "</h3>");
			} catch (SQLException e) {
			    e.printStackTrace();
			    out.println("<h3 style='color:red;'>SQLException: " + e.getMessage() + "</h3>");
			} catch (Exception e) {
			    e.printStackTrace();
			    out.println("<h3 style='color:red;'>Exception: " + e.getMessage() + "</h3>");
			    
		   
			out.println("</body>");
			out.println("</html>");
			} finally {
	            try { 
	            	
	            	if (ps != null) ps.close(); 
	            	if (con != null) con.close(); 
	            
	              	} catch (SQLException e) {}
			}
		}
	           
		@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
			
		}
	}
		
