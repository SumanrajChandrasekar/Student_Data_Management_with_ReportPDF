//package com.pro;
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.Statement;
//
//import com.itextpdf.text.Document;
//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.Chunk;
//import com.itextpdf.text.FontFactory;
//import com.itextpdf.text.BaseColor;
//import com.itextpdf.text.Phrase;
//import com.itextpdf.text.pdf.PdfPCell;
//import com.itextpdf.text.pdf.PdfPTable;
//import com.itextpdf.text.pdf.PdfWriter;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//@WebServlet("/DownloadDepartmentPDF")
//public class DownloadDepartmentPdf extends HttpServlet {
//
//    private static final String DB_URL = "jdbc:mysql://localhost:3306/StudentRegister";
//    private static final String DB_USER = "sumanraj";
//    private static final String DB_PASS = "12345678";
//    
//    private static final String Select_Query = "Select * from Dept_List ";
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        response.setContentType("application/pdf");
//        response.setHeader("Content-Disposition", "attachment; filename=Department_data.pdf");
//
//        try {
//          Document document = new Document();
//          PdfWriter.getInstance(document, response.getOutputStream());
//          document.open();
//
//            //Add title and line break
//            document.add(new Paragraph("Department Data List", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
//            document.add(Chunk.NEWLINE);
//
//            //create table with 7 columns
//            PdfPTable table = new PdfPTable(3);
//            table.setWidthPercentage(100);
//
//            // Set custom column widths: ID column is smaller
//            float[] columnWidths = {1.0f, 3.0f, 3.0f};
//            table.setWidths(columnWidths);
//
//            // Table headers
//            String[] headers = {"ID", "DeptName", "DeptCode"};
//            for (String header : headers) {
//                PdfPCell cell = new PdfPCell(new Phrase(header));
//                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
//                cell.setNoWrap(true);  // Prevent wrapping
//                table.addCell(cell);
//            }
//
//            // Fetch data
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
//                 Statement stmt = conn.createStatement();
//                 ResultSet rs = stmt.executeQuery(Select_Query)) {
//
//                while (rs.next()) {
//                    PdfPCell idCell = new PdfPCell(new Phrase(String.valueOf(rs.getInt("ID")))); //get value for the cell
//                    idCell.setNoWrap(true);  //no wrap content
//                    table.addCell(idCell);  //add value to the cell
//
//                    PdfPCell nameCell = new PdfPCell(new Phrase(rs.getString("DeptName")));
//                    nameCell.setNoWrap(true);
//                    table.addCell(nameCell);
//
//                    PdfPCell phoneCell = new PdfPCell(new Phrase(rs.getString("DeptCode")));
//                    phoneCell.setNoWrap(true);
//                    table.addCell(phoneCell);
//
//                   
//                }
//            }
//
//            document.add(table);
//            document.close();
//
//        } catch (Exception e) {
//            throw new ServletException("Exception in PDF creation: " + e.getMessage());
//        }
//    }
//}
//
//


package com.pro;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/DownloadDepartmentPDF")
public class DownloadDepartmentPdf extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/StudentRegister";
    private static final String DB_USER = "sumanraj";
    private static final String DB_PASS = "12345678";
    private static final String Fetch_Query = "select * from Dept_List";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
        
    	 String selected = request.getParameter("columns");
         String[] selectedColumns = (selected != null && !selected.isEmpty()) ? selected.split(",") : new String[0];

         if (selectedColumns.length == 0) {
             response.setContentType("text/html");
             PrintWriter out = response.getWriter();
             out.println("<html><body><h3>No columns selected.</h3>");
             out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>");
             out.println("<a href='ViewStudentData' class='btn btn-outline-success btn-lg fw-bold mt-3'>Back</a>");
             out.println("</body></html>");
             return;
             
         }

         response.setContentType("application/pdf");
         response.setHeader("Content-Disposition", "attachment; filename=student_data.pdf");

         try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
              PreparedStatement ps = con.prepareStatement(Fetch_Query);
              ResultSet rs = ps.executeQuery()) {

             Document document = new Document();
             PdfWriter.getInstance(document, response.getOutputStream());
             document.open();

             // Title
             Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
             Paragraph title = new Paragraph("Registered Student Data", titleFont);
             title.setAlignment(Element.ALIGN_CENTER);
             document.add(title);
             document.add(Chunk.NEWLINE);

             // Create table with selected column count
             PdfPTable table = new PdfPTable(selectedColumns.length);
             table.setWidthPercentage(100);

             // Header row
             Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
             for (String col : selectedColumns) {
                 PdfPCell header = new PdfPCell(new Phrase(col, headerFont));
                 header.setBackgroundColor(BaseColor.DARK_GRAY);
                 header.setHorizontalAlignment(Element.ALIGN_CENTER);
                 table.addCell(header);
             }

             // Data rows
             while (rs.next()) {
                 for (String col : selectedColumns) {
                     String value;
                     try {
                         value = rs.getString(col);
                     } catch (SQLException e) {
                         value = "N/A"; // If the column doesn't exist or error occurs
                     }
                     table.addCell(new PdfPCell(new Phrase(value != null ? value : "")));
                 }
             }

             document.add(table);
             document.close();

         } catch (Exception e) {
             e.printStackTrace();
             response.setContentType("text/html");
             PrintWriter out = response.getWriter();
             out.println("<html><body><h3>Error: " + e.getMessage() + "</h3></body></html>");
         }
     }
 }