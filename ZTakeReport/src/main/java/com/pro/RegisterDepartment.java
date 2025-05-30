package com.pro;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/RegisterDepartment")
public class RegisterDepartment extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/StudentRegister";
    private static final String DB_USER = "sumanraj";
    private static final String DB_PASS = "12345678";

    private static final String CHECK_ID_QUERY = "SELECT * FROM Dept_List WHERE ID=?";
    private static final String UPDATE_QUERY = "UPDATE Dept_List SET DeptName=?, DeptCode=? WHERE ID=?";
    private static final String INSERT_QUERY = "INSERT INTO Dept_List (DeptName, DeptCode) VALUES (?, ?)";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json");
        PrintWriter out = res.getWriter();
        ObjectMapper mapper = new ObjectMapper();

        String ids = req.getParameter("id");

        try {
            int ID = Integer.parseInt(ids);

            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                 PreparedStatement pst = con.prepareStatement(CHECK_ID_QUERY)) {

                pst.setInt(1, ID);
                ResultSet rs = pst.executeQuery();
                
                Student student = new Student();
                	
                 if (rs.next()) {
                    	student.setExists(true);
                        student.setName(rs.getString("DeptName"));
                        student.setPhone(rs.getString("DeptCode"));
                        

                    } else {

                    	student.setExists(false);
                    }
                    String jsonResponse = mapper.writeValueAsString(student);
                    out.write(jsonResponse);
                }
            
        } catch (NumberFormatException e) {
            out.println("<h3 style='color:red'>Invalid ID format: " + ids + "</h3>");
        } catch (ClassNotFoundException | SQLException e) {
            out.println("<h3 style='color:red'>" + e.getClass().getSimpleName() + ": " + e.getMessage() + "</h3>");
        } catch (Exception e) {
            out.println("<h3 style='color:red'>Exception: " + e.getMessage() + "</h3>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css' rel='stylesheet'>");

        String ids = req.getParameter("id");
        String DeptName = req.getParameter("deptname");
        String DeptCode = req.getParameter("deptcode");
       

        try {
            int ID = Integer.parseInt(ids);

            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {

                int result1 = 0;
                int result2 = 0;
              
                try (PreparedStatement checkStmt = con.prepareStatement(CHECK_ID_QUERY)) {
                    checkStmt.setInt(1, ID);

                    try (ResultSet rs = checkStmt.executeQuery()) {
                        if (rs.next()) {
                            try (PreparedStatement pst = con.prepareStatement(UPDATE_QUERY)) {
                                pst.setString(1, DeptName);
                                pst.setString(2, DeptCode);
                               
                                pst.setInt(3, ID);
                                result1 = pst.executeUpdate();
                            }
                            if (result1 > 0) {
                            	out.println("<h3 style='color:green;'>Student Detail Updated Successfully!</h3>");
                            } else {
                            	out.println("<h3 style='color:red;'>Failed to Update Student.</h3>");
                            }
                        } else {
                            try (PreparedStatement pst = con.prepareStatement(INSERT_QUERY)) {
                                pst.setString(1, DeptName);
                                pst.setString(2, DeptCode);
                                
                                result2 = pst.executeUpdate();
                            }
                            		           
    			    		
                            
                            if (result2 > 0) {
                                out.println("<h3 style='color:green;'>Student Detail Added Successfully!</h3>");
                            } else {
                                out.println("<h3 style='color:red;'>Failed to Add Student.</h3>");
                                res.sendRedirect("ViewStudentData");
                            }
                        }
                    }
                }
            out.println("<a href= 'Index.jsp' class=\"btn btn-outline-primary  text-decoration-none fw-bold\">Home</a>"); 
            out.println("<a href='ViewDepartmentData'  class='btn btn-outline-success text-decoration-none fw-bold'>View Department Data</a>"); 
            }        
        } catch (NumberFormatException e) {
            out.println("<h3 style='color:red'>Invalid ID format: " + ids + "</h3>");
        } catch (ClassNotFoundException | SQLException e) {
            out.println("<h3 style='color:red'>" + e.getClass().getSimpleName() + ": " + e.getMessage() + "</h3>");
        } catch (Exception e) {
            out.println("<h3 style='color:red'>Exception: " + e.getMessage() + "</h3>");
        }
    }
}