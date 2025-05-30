package com.pro;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/UpdateDepartmentData")
public class UpdateDepartmentData extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/StudentRegister";
    private static final String DB_USER = "sumanraj";
    private static final String DB_PASS = "12345678";

    private static final String UPDATE_QUERY = "UPDATE Dept_List SET DeptName=?, DeptCode=? WHERE ID=?";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        int id = Integer.parseInt(req.getParameter("id"));
        String deptname = req.getParameter("deptname");
        String deptcode = req.getParameter("deptcode");
       

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                 PreparedStatement ps = con.prepareStatement(UPDATE_QUERY)) {

                ps.setString(1, deptname);
                ps.setString(2, deptcode);
                ps.setInt(3, id);

                int rowsAffected = ps.executeUpdate();

                if (rowsAffected > 0) {
                    res.sendRedirect("ViewDepartmentData");
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