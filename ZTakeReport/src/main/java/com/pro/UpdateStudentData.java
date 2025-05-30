package com.pro;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/UpdateStudentData")
public class UpdateStudentData extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/StudentRegister";
    private static final String DB_USER = "sumanraj";
    private static final String DB_PASS = "12345678";

    private static final String UPDATE_QUERY = "UPDATE Registered_Student_List SET StudentName=?, PhoneNo=?, Native=?, Email=?, DOB=?, Department=? WHERE ID=?";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        int id = Integer.parseInt(req.getParameter("id"));
        String name = req.getParameter("name");
        String phone = req.getParameter("phone");
        String nativePlace = req.getParameter("nativeplace");
        String email = req.getParameter("email");
        String dob = req.getParameter("dob");
        String dept = req.getParameter("dept");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                 PreparedStatement ps = con.prepareStatement(UPDATE_QUERY)) {

                ps.setString(1, name);
                ps.setString(2, phone);
                ps.setString(3, nativePlace);
                ps.setString(4, email);
                ps.setString(5, dob);
                ps.setString(6, dept);
                ps.setInt(7, id);

                int rowsAffected = ps.executeUpdate();

                if (rowsAffected > 0) {
                    res.sendRedirect("ViewStudentData");
                } else {
                    out.println("<h3 style='color:red'>Update Failed</h3>");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3 style='color:red'>Error: " + e.getMessage() + "</h3>");
        }
    }
}