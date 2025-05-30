package com.pro;

import java.io.*;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/DownloadStudentCSV")
public class DownloadStudentCsv extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/StudentRegister";
    private static final String DB_USER = "sumanraj";
    private static final String DB_PASS = "12345678";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String columnsParam = req.getParameter("columns");
        if (columnsParam == null || columnsParam.trim().isEmpty()) {
            res.setContentType("text/plain");
            res.getWriter().println("No columns selected.");
            return;
        }

        String[] columns = columnsParam.split(",");
        res.setContentType("text/csv");
        res.setHeader("Content-Disposition", "attachment;filename=students.csv");

        try (
            Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            PrintWriter out = res.getWriter()
        ) {
            Class.forName("com.mysql.cj.jdbc.Driver");

            StringBuilder queryBuilder = new StringBuilder("SELECT ");
            for (int i = 0; i < columns.length; i++) {
                queryBuilder.append(columns[i]);
                if (i < columns.length - 1) queryBuilder.append(", ");
            }
            queryBuilder.append(" FROM Registered_Student_List");

            try (PreparedStatement ps = con.prepareStatement(queryBuilder.toString());
                 ResultSet rs = ps.executeQuery()) {

                // Header row
                for (int i = 0; i < columns.length; i++) {
                    out.print(columns[i]);
                    if (i < columns.length - 1) out.print(",");
                }
                out.println();

                // Data rows
                while (rs.next()) {
                    for (int i = 0; i < columns.length; i++) {
                        out.print(rs.getString(columns[i]));
                        if (i < columns.length - 1) out.print(",");
                    }
                    out.println();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.setContentType("text/plain");
            res.getWriter().println("Error: " + e.getMessage());
        }
    }
}