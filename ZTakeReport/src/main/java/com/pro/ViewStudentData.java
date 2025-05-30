//package com.pro;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.sql.*;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//@WebServlet("/ViewStudentData")
//public class ViewStudentData extends HttpServlet {
//
//    private static final String DB_URL = "jdbc:mysql://localhost:3306/StudentRegister";
//    private static final String DB_USER = "sumanraj";
//    private static final String DB_PASS = "12345678";
//
//    private static final String View_Query = "SELECT * FROM Registered_Student_List";
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
//        res.setContentType("text/html");
//        PrintWriter out = res.getWriter();
//
//        out.println("<!DOCTYPE html><html><head><title>Student List</title>");
//        out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css' rel='stylesheet'>");
//        out.println("<style>");
//        out.println("body { background-color: #ccebff; font-size: 0.9rem; }");
//        out.println(".table-container { max-height: 80vh; overflow-y: auto; }");
//        out.println("thead th { position: sticky; top: 0; background-color: #212529; color: white; }");
//        out.println("</style></head><body>");
//        out.println("<div class='container mt-4'>");
//
//        // Navigation
//        out.println("<div class='mb-3'>");
//        out.println("<a href='Index.jsp' class='btn btn-outline-success fw-bold'>Home</a> ");
//        out.println("<a href='RegisterStudent.jsp' class='btn btn-outline-primary fw-bold'>Register Student</a>");
//        out.println("</div>");
//
//        Connection con = null;
//        PreparedStatement ps = null;
//
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
//            ps = con.prepareStatement(View_Query);
//            ResultSet rs = ps.executeQuery();
//
//            out.println("<div class='table-container'>");
//            out.println("<table class='table table-bordered table-striped table-hover text-center'>");
//            out.println("<thead class='table-dark'><tr>");
//            out.println("<th>ID</th><th>Student Name</th><th>Phone No</th><th>Native</th><th>Email</th><th>DOB</th><th>Department</th><th>Edit</th><th>Delete</th>");
//            out.println("</tr></thead><tbody>");
//
//            while (rs.next()) {
//                out.println("<tr>");
//                out.println("<td>" + rs.getInt("ID") + "</td>");
//                out.println("<td>" + rs.getString("StudentName") + "</td>");
//                out.println("<td>" + rs.getString("PhoneNo") + "</td>");
//                out.println("<td>" + rs.getString("Native") + "</td>");
//                out.println("<td>" + rs.getString("Email") + "</td>");
//                out.println("<td>" + rs.getDate("DOB") + "</td>");
//                out.println("<td>" + rs.getString("Department") + "</td>");
//
//                // Edit
//                out.println("<td><form action='UpdataStudent.jsp' method='get'>");
//                out.println("<input type='hidden' name='id' value='" + rs.getInt("ID") + "'/>");
//                out.println("<input type='hidden' name='name' value='" + rs.getString("StudentName") + "'/>");
//                out.println("<input type='hidden' name='phone' value='" + rs.getString("PhoneNo") + "'/>");
//                out.println("<input type='hidden' name='nativeplace' value='" + rs.getString("Native") + "'/>");
//                out.println("<input type='hidden' name='email' value='" + rs.getString("Email") + "'/>");
//                out.println("<input type='hidden' name='dob' value='" + rs.getString("DOB") + "'/>");
//                out.println("<input type='hidden' name='dept' value='" + rs.getString("Department") + "'/>");
//                out.println("<button type='submit' class='btn btn-warning btn-sm'>Edit</button>");
//                out.println("</form></td>");
//
//                // Delete
//                out.println("<td><form action='DeleteStudent' method='post'>");
//                out.println("<input type='hidden' name='id' value='" + rs.getInt("ID") + "'/>");
//                out.println("<button type='submit' class='btn btn-danger btn-sm'>Delete</button>");
//                out.println("</form></td>");
//                out.println("</tr>");
//            }
//
//            out.println("</tbody></table></div>");
//
//        } catch (Exception e) {
//            out.println("<div class='alert alert-danger'>Error: " + e.getMessage() + "</div>");
//        } finally {
//            try {
//                if (ps != null) ps.close();
//                if (con != null) con.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//
//        // PDF column selection
//        out.println("<hr><form method='get' action='DownloadStudentPDF' target='_blank' class='text-center'>");
//
//        String[] columns = {"ID", "StudentName", "PhoneNo", "Native", "Email", "DOB", "Department"};
//        String[] labels = {"ID", "Name", "Phone", "Native", "Email", "DOB", "Department"};
//
//        for (int i = 0; i < columns.length; i++) {
//            out.println("<label class='form-check form-check-inline'>");
//            out.println("<input class='form-check-input column-checkbox' type='checkbox' value='" + columns[i] + "' onclick='trackSelection(this)'> " + labels[i]);
//            out.println("</label>");
//        }
//
//        // Clear button
//        out.println("<div class='form-check form-check-inline'>");
//        out.println("<button type='button' class='btn btn-danger btn-sm ms-2' onclick='clearAllCheckboxes()'>Clear</button>");
//        out.println("</div>");
//
//        // Selected columns
//        out.println("<hr><h5 class='text-center fw-bold'>Selected Columns for Report</h5>");
//        out.println("<div class='d-flex justify-content-center'>");
//        out.println("<div id='selectedColumnsTable'></div>");
//        out.println("</div>");
//
//        out.println("<input type='hidden' name='columns' id='columnsInput'>");
//        out.println("<button type='submit' class='btn btn-outline-danger fw-bold mt-2'>Download Selected Columns (PDF)</button>");
//        
//
//        // JS for dynamic column selection
//        out.println("<script>");
//        out.println("let selectedColumns = [];");
//
//        //track column
//        out.println("function trackSelection(checkbox) {");
//        out.println("  const value = checkbox.value;");
//        out.println("  if (checkbox.checked) {");
//        out.println("    if (!selectedColumns.includes(value)) selectedColumns.push(value);");
//        out.println("  } else {");
//        out.println("    selectedColumns = selectedColumns.filter(col => col !== value);");
//        out.println("  }");
//        out.println("  updateHiddenInput();");
//        out.println("  renderSelectedColumns();");
//        out.println("}");
//
//        //clear button
//        out.println("function clearAllCheckboxes() {");
//        out.println("  document.querySelectorAll('.column-checkbox').forEach(cb => cb.checked = false);");
//        out.println("  selectedColumns = [];");
//        out.println("  updateHiddenInput();");
//        out.println("  renderSelectedColumns();");
//        out.println("}");
//
//        
//        out.println("function updateHiddenInput() {");
//        out.println("  document.getElementById('columnsInput').value = selectedColumns.join(',');");
//        out.println("}");
//
//        out.println("function renderSelectedColumns() {");
//        out.println("  const container = document.getElementById('selectedColumnsTable');");
//        out.println("  container.innerHTML = '';");
//        out.println("  if (selectedColumns.length === 0) return;");
//        out.println("  const table = document.createElement('table');");
//        out.println("  table.className = 'table table-bordered table-striped table-hover text-center';");
//        out.println("  const thead = document.createElement('thead');");
//        out.println("  thead.className = 'table-dark';");
//        out.println("  const headRow = document.createElement('tr');");
//        out.println("  selectedColumns.forEach(col => {");
//        out.println("    const th = document.createElement('th');");
//        out.println("    th.textContent = col;");
//        out.println("    headRow.appendChild(th);");
//        out.println("  });");
//        out.println("  thead.appendChild(headRow);");
//        out.println("  table.appendChild(thead);");
//        out.println("  container.appendChild(table);");
//        out.println("}");
//        out.println("</script>");
//        out.println("</form>");
//        
//        
//        
//     // CSV Form
//        out.println("<hr><form method='get' action='DownloadStudentCSV' target='_blank' class='text-center'>");
//
//        String[] columnss = {"ID", "StudentName", "PhoneNo", "Native", "Email", "DOB", "Department"};
//        String[] labelss = {"ID", "Name", "Phone", "Native", "Email", "DOB", "Department"};
//
//        for (int i = 0; i < columnss.length; i++) {
//            out.println("<label class='form-check form-check-inline'>");
//            out.println("<input class='form-check-input column-checkbox-csv' type='checkbox' value='" + columnss[i] + "' onclick='trackSelectionCSV(this)'> " + labelss[i]);
//            out.println("</label>");
//        }
//
//        // Clear button
//        out.println("<div class='form-check form-check-inline'>");
//        out.println("<button type='button' class='btn btn-danger btn-sm ms-2' onclick='clearAllCheckboxesCSV()'>Clear</button>");
//        out.println("</div>");
//
//        out.println("<hr><h5 class='text-center fw-bold'>Selected Columns for CSV Report</h5>");
//        out.println("<div class='d-flex justify-content-center'>");
//        out.println("<div id='selectedColumnsTableCSV'></div>");
//        out.println("</div>");
//
//        out.println("<input type='hidden' name='columns' id='columnsInputCSV'>");
//        out.println("<button type='submit' class='btn btn-outline-danger fw-bold mt-2'>Download Selected Columns (CSV)</button>");
//
//        out.println("<script>");
//        out.println("let selectedColumnsCSV = [];");
//
//        out.println("function trackSelectionCSV(checkbox) {");
//        out.println("  const value = checkbox.value;");
//        out.println("  if (checkbox.checked) {");
//        out.println("    if (!selectedColumnsCSV.includes(value)) selectedColumnsCSV.push(value);");
//        out.println("  } else {");
//        out.println("    selectedColumnsCSV = selectedColumnsCSV.filter(col => col !== value);");
//        out.println("  }");
//        out.println("  updateHiddenInputCSV();");
//        out.println("  renderSelectedColumnsCSV();");
//        out.println("}");
//
//        out.println("function clearAllCheckboxesCSV() {");
//        out.println("  document.querySelectorAll('.column-checkbox-csv').forEach(cb => cb.checked = false);");
//        out.println("  selectedColumnsCSV = [];");
//        out.println("  updateHiddenInputCSV();");
//        out.println("  renderSelectedColumnsCSV();");
//        out.println("}");
//
//        out.println("function updateHiddenInputCSV() {");
//        out.println("  document.getElementById('columnsInputCSV').value = selectedColumnsCSV.join(',');");
//        out.println("}");
//
//        out.println("function renderSelectedColumnsCSV() {");
//        out.println("  const container = document.getElementById('selectedColumnsTableCSV');");
//        out.println("  container.innerHTML = '';");
//        out.println("  if (selectedColumnsCSV.length === 0) return;");
//        out.println("  const table = document.createElement('table');");
//        out.println("  table.className = 'table table-bordered table-striped table-hover text-center';");
//        out.println("  const thead = document.createElement('thead');");
//        out.println("  thead.className = 'table-dark';");
//        out.println("  const headRow = document.createElement('tr');");
//        out.println("  selectedColumnsCSV.forEach(col => {");
//        out.println("    const th = document.createElement('th');");
//        out.println("    th.textContent = col;");
//        out.println("    headRow.appendChild(th);");
//        out.println("  });");
//        out.println("  thead.appendChild(headRow);");
//        out.println("  table.appendChild(thead);");
//        out.println("  container.appendChild(table);");
//        out.println("}");
//        out.println("</script>");
//        out.println("</form>");
//        
//        
//        out.println("<br><br><br><br><br><br><br></div></body></html>");
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
//        doGet(req, res);  // Optionally reuse doGet
//    }
//}



//package com.pro;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.sql.*;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//@WebServlet("/ViewStudentData")
//public class ViewStudentData extends HttpServlet {
//
//    private static final String DB_URL = "jdbc:mysql://localhost:3306/StudentRegister";
//    private static final String DB_USER = "sumanraj";
//    private static final String DB_PASS = "12345678";
//
//    private static final String View_Query = "SELECT * FROM Registered_Student_List";
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
//        res.setContentType("text/html");
//        PrintWriter out = res.getWriter();
//
//        out.println("<!DOCTYPE html><html><head><title>Student List</title>");
//        out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css' rel='stylesheet'>");
//        out.println("<style>");
//        out.println("body { background-color: #ccebff; font-size: 0.9rem; }");
//        out.println(".table-container { max-height: 80vh; overflow-y: auto; }");
//        out.println("thead th { position: sticky; top: 0; background-color: #212529; color: white; }");
//        out.println("</style></head><body>");
//        out.println("<div class='container mt-4'>");
//
//        // Navigation
//        out.println("<div class='mb-3'>");
//        out.println("<a href='Index.jsp' class='btn btn-outline-success fw-bold'>Home</a> ");
//        out.println("<a href='RegisterStudent.jsp' class='btn btn-outline-primary fw-bold'>Register Student</a>");
//        out.println("</div>");
//
//        Connection con = null;
//        PreparedStatement ps = null;
//
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
//            ps = con.prepareStatement(View_Query);
//            ResultSet rs = ps.executeQuery();
//
//            out.println("<div class='table-container'>");
//            out.println("<table class='table table-bordered table-striped table-hover text-center'>");
//            out.println("<thead class='table-dark'><tr>");
//            out.println("<th>ID</th><th>Student Name</th><th>Phone No</th><th>Native</th><th>Email</th><th>DOB</th><th>Department</th><th>Edit</th><th>Delete</th>");
//            out.println("</tr></thead><tbody>");
//
//            while (rs.next()) {
//                out.println("<tr>");
//                out.println("<td>" + rs.getInt("ID") + "</td>");
//                out.println("<td>" + rs.getString("StudentName") + "</td>");
//                out.println("<td>" + rs.getString("PhoneNo") + "</td>");
//                out.println("<td>" + rs.getString("Native") + "</td>");
//                out.println("<td>" + rs.getString("Email") + "</td>");
//                out.println("<td>" + rs.getDate("DOB") + "</td>");
//                out.println("<td>" + rs.getString("Department") + "</td>");
//
//                // Edit
//                out.println("<td><form action='UpdataStudent.jsp' method='get'>");
//                out.println("<input type='hidden' name='id' value='" + rs.getInt("ID") + "'/>");
//                out.println("<input type='hidden' name='name' value='" + rs.getString("StudentName") + "'/>");
//                out.println("<input type='hidden' name='phone' value='" + rs.getString("PhoneNo") + "'/>");
//                out.println("<input type='hidden' name='nativeplace' value='" + rs.getString("Native") + "'/>");
//                out.println("<input type='hidden' name='email' value='" + rs.getString("Email") + "'/>");
//                out.println("<input type='hidden' name='dob' value='" + rs.getString("DOB") + "'/>");
//                out.println("<input type='hidden' name='dept' value='" + rs.getString("Department") + "'/>");
//                out.println("<button type='submit' class='btn btn-warning btn-sm'>Edit</button>");
//                out.println("</form></td>");
//
//                // Delete
//                out.println("<td><form action='DeleteStudent' method='post'>");
//                out.println("<input type='hidden' name='id' value='" + rs.getInt("ID") + "'/>");
//                out.println("<button type='submit' class='btn btn-danger btn-sm'>Delete</button>");
//                out.println("</form></td>");
//                out.println("</tr>");
//            }
//
//            out.println("</tbody></table></div>");
//
//        } catch (Exception e) {
//            out.println("<div class='alert alert-danger'>Error: " + e.getMessage() + "</div>");
//        } finally {
//            try {
//                if (ps != null) ps.close();
//                if (con != null) con.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//
//     // --- PDF Column Selection + GroupBy Column ---
//        out.println("<hr><form method='get' action='DownloadStudentPDF' target='_blank' class='text-center'>");
//
//        String[] columns = {"ID", "StudentName", "PhoneNo", "Native", "Email", "DOB", "Department"};
//        String[] labels = {"ID", "Name", "Phone", "Native", "Email", "DOB", "Department"};
//
//        // Column checkboxes
//        out.println("<h5 class='fw-bold'>Select Columns</h5>");
//        for (int i = 0; i < columns.length; i++) {
//            out.println("<label class='form-check form-check-inline'>");
//            out.println("<input class='form-check-input column-checkbox' type='checkbox' value='" + columns[i] + "' onclick='trackSelection(this)'> " + labels[i]);
//            out.println("</label>");
//        }
//
//        // GroupBy checkboxes
//        out.println("<hr><h5 class='fw-bold'>Group By</h5>");
//        for (int i = 0; i < columns.length; i++) {
//            out.println("<label class='form-check form-check-inline'>");
//            out.println("<input class='form-check-input groupby-checkbox' type='checkbox' value='" + columns[i] + "' onclick='trackGroupBy(this)'> " + labels[i]);
//            out.println("</label>");
//        }
//
//        // Clear button
//        out.println("<div class='form-check form-check-inline'>");
//        out.println("<button type='button' class='btn btn-danger btn-sm ms-2' onclick='clearAllCheckboxes()'>Clear</button>");
//        out.println("</div>");
//
//        // Display selected columns
//        out.println("<hr><h5 class='text-center fw-bold'>Selected Columns for Report</h5>");
//        out.println("<div class='d-flex justify-content-center'><div id='selectedColumnsTable'></div></div>");
//
//        // Display selected group by
//        out.println("<h5 class='text-center fw-bold mt-3'>Group By Columns</h5>");
//        out.println("<div class='d-flex justify-content-center'><div id='groupByTable'></div></div>");
//
//        out.println("<input type='hidden' name='columns' id='columnsInput'>");
//        out.println("<input type='hidden' name='groupBy' id='groupByInput'>");
//        out.println("<button type='submit' class='btn btn-outline-danger fw-bold mt-3'>Download PDF</button>");
//        out.println("</form>");
//
//        // JavaScript for dynamic behavior
//        out.println("<script>");
//        out.println("let selectedColumns = [], selectedGroupBy = [];");
//
//        out.println("function trackSelection(checkbox) {");
//        out.println("  const val = checkbox.value;");
//        out.println("  if (checkbox.checked && !selectedColumns.includes(val)) selectedColumns.push(val);");
//        out.println("  else selectedColumns = selectedColumns.filter(c => c !== val);");
//        out.println("  updateHiddenInputs(); renderTables();");
//        out.println("}");
//
//        out.println("function trackGroupBy(checkbox) {");
//        out.println("  const val = checkbox.value;");
//        out.println("  if (checkbox.checked && !selectedGroupBy.includes(val)) selectedGroupBy.push(val);");
//        out.println("  else selectedGroupBy = selectedGroupBy.filter(c => c !== val);");
//        out.println("  updateHiddenInputs(); renderTables();");
//        out.println("}");
//
//        //clear
//        out.println("function clearAllCheckboxes() {");
//        out.println("  document.querySelectorAll('.column-checkbox, .groupby-checkbox').forEach(cb => cb.checked = false);");
//        out.println("  selectedColumns = []; selectedGroupBy = [];");
//        out.println("  updateHiddenInputs(); renderTables();");
//        out.println("}");
//
//        
//        out.println("function updateHiddenInputs() {");
//        out.println("  document.getElementById('columnsInput').value = selectedColumns.join(',');");
//        out.println("  document.getElementById('groupByInput').value = selectedGroupBy.join(',');");
//        out.println("}");
//
//        out.println("function renderTables() {");
//        out.println("  const sc = document.getElementById('selectedColumnsTable');");
//        out.println("  const gb = document.getElementById('groupByTable');");
//
//        out.println("  sc.innerHTML = '';");
//        out.println("  gb.innerHTML = '';");
//
//        out.println("  if (selectedColumns.length > 0) {");
//        out.println("    const table = document.createElement('table');");
//        out.println("    table.className = 'table table-bordered table-striped table-hover text-center';");
//        out.println("    const thead = document.createElement('thead');");
//        out.println("    thead.className = 'table-dark';");
//        out.println("    const row = document.createElement('tr');");
//        out.println("    selectedColumns.forEach(col => {");
//        out.println("      const th = document.createElement('th');");
//        out.println("      th.textContent = col;");
//        out.println("      row.appendChild(th);");
//        out.println("    });");
//        out.println("    thead.appendChild(row); table.appendChild(thead);");
//        out.println("    sc.appendChild(table);");
//        out.println("  }");
//
//        out.println("  if (selectedGroupBy.length > 0) {");
//        out.println("    const table = document.createElement('table');");
//        out.println("    table.className = 'table table-bordered table-striped table-hover text-center';");
//        out.println("    const thead = document.createElement('thead');");
//        out.println("    thead.className = 'table-info';");
//        out.println("    const row = document.createElement('tr');");
//        out.println("    selectedGroupBy.forEach(col => {");
//        out.println("      const th = document.createElement('th');");
//        out.println("      th.textContent = col;");
//        out.println("      row.appendChild(th);");
//        out.println("    });");
//        out.println("    thead.appendChild(row); table.appendChild(thead);");
//        out.println("    gb.appendChild(table);");
//        out.println("  }");
//        out.println("}");
//        out.println("</script>");
//
//        out.println("<br><br><br><br><br><br><br></body></html>");
//    }
//}






package com.pro;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ViewStudentData")
public class ViewStudentData extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/StudentRegister";
    private static final String DB_USER = "sumanraj";
    private static final String DB_PASS = "12345678";

    private static final String View_Query = "SELECT * FROM Registered_Student_List";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        out.println("<!DOCTYPE html><html><head><title>Student List</title>");
        out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css' rel='stylesheet'>");
        out.println("<style>");
        out.println("body { background-color: #ccebff; font-size: 0.9rem; }");
        out.println(".table-container { max-height: 80vh; overflow-y: auto; }");
        out.println("thead th { position: sticky; top: 0; background-color: #212529; color: white; }");
        out.println("</style></head><body>");
        out.println("<div class='container mt-4'>");

        // Navigation
        out.println("<div class='mb-3'>");
        out.println("<a href='Index.jsp' class='btn btn-outline-success fw-bold'>Home</a> ");
        out.println("<a href='RegisterStudent.jsp' class='btn btn-outline-primary fw-bold'>Register Student</a>");
        out.println("</div>");

        Connection con = null;
        PreparedStatement ps = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            ps = con.prepareStatement(View_Query);
            ResultSet rs = ps.executeQuery();

            out.println("<div class='table-container'>");
            out.println("<table class='table table-bordered table-striped table-hover text-center'>");
            out.println("<thead class='table-dark'><tr>");
            out.println("<th>ID</th><th>Student Name</th><th>Phone No</th><th>Native</th><th>Email</th><th>DOB</th><th>Department</th><th>Edit</th><th>Delete</th>");
            out.println("</tr></thead><tbody>");

            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getInt("ID") + "</td>");
                out.println("<td>" + rs.getString("StudentName") + "</td>");
                out.println("<td>" + rs.getString("PhoneNo") + "</td>");
                out.println("<td>" + rs.getString("Native") + "</td>");
                out.println("<td>" + rs.getString("Email") + "</td>");
                out.println("<td>" + rs.getDate("DOB") + "</td>");
                out.println("<td>" + rs.getString("Department") + "</td>");

                // Edit form (fixed JSP name)
                out.println("<td><form action='UpdateStudent.jsp' method='get'>");
                out.println("<input type='hidden' name='id' value='" + rs.getInt("ID") + "'/>");
                out.println("<input type='hidden' name='name' value='" + rs.getString("StudentName") + "'/>");
                out.println("<input type='hidden' name='phone' value='" + rs.getString("PhoneNo") + "'/>");
                out.println("<input type='hidden' name='nativeplace' value='" + rs.getString("Native") + "'/>");
                out.println("<input type='hidden' name='email' value='" + rs.getString("Email") + "'/>");
                out.println("<input type='hidden' name='dob' value='" + rs.getString("DOB") + "'/>");
                out.println("<input type='hidden' name='dept' value='" + rs.getString("Department") + "'/>");
                out.println("<button type='submit' class='btn btn-warning btn-sm'>Edit</button>");
                out.println("</form></td>");

                // Delete form
                out.println("<td><form action='DeleteStudent' method='post' onsubmit='return confirm(\"Are you sure to delete this student?\");'>");
                out.println("<input type='hidden' name='id' value='" + rs.getInt("ID") + "'/>");
                out.println("<button type='submit' class='btn btn-danger btn-sm'>Delete</button>");
                out.println("</form></td>");
                out.println("</tr>");
            }

            out.println("</tbody></table></div>");

        } catch (Exception e) {
            out.println("<div class='alert alert-danger'>Error: " + e.getMessage() + "</div>");
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // --- PDF Column Selection + GroupBy Column ---
        out.println("<hr><form method='get' action='DownloadStudentPDF' target='_blank' class='text-center' onsubmit='return validateForm()'>");

        
        String[] columns = {"ID", "StudentName", "PhoneNo", "Native", "Email", "DOB", "Department"};
        String[] labels = {"ID", "Name", "Phone", "Native", "Email", "DOB", "Department"};

        //page orientation
        out.println("<h5 class='fw-bold'>Select Page Orientation</h5>");
        out.println("<div class='form-check form-check-inline'>");
        out.println("<input class='form-check-input' type='radio' name='orientation' id='portrait' value='portrait' checked>");
        out.println("<label class='form-check-label' for='portrait'>Portrait</label>");
        out.println("</div>");
        out.println("<div class='form-check form-check-inline'>");
        out.println("<input class='form-check-input' type='radio' name='orientation' id='landscape' value='landscape'>");
        out.println("<label class='form-check-label' for='landscape'>Landscape</label>");
        out.println("</div>");
        
        // Column checkboxes
        out.println("<hr><h5 class='fw-bold'>Select Columns</h5>");
        for (int i = 0; i < columns.length; i++) {
            out.println("<label class='form-check form-check-inline'>");
            out.println("<input class='form-check-input column-checkbox' type='checkbox' value='" + columns[i] + "' onclick='trackSelection(this)'> " + labels[i]);
            out.println("</label>");
        }
        //select all columns
        out.println("<button type='button' class='btn btn-sm btn-primary mb-2' onclick='selectAllColumns()'>Select All Columns</button>");

        // GroupBy checkboxes
        out.println("<hr><h5 class='fw-bold'>Group By</h5>");
        for (int i = 0; i < columns.length; i++) {
            out.println("<label class='form-check form-check-inline'>");
            out.println("<input class='form-check-input groupby-checkbox' type='checkbox' value='" + columns[i] + "' onclick='trackGroupBy(this)'> " + labels[i]);
            out.println("</label>");
        }
        

        // Clear button
        out.println("<div class='form-check form-check-inline'>");
        out.println("<button type='button' class='btn btn-danger btn-sm ms-2' onclick='clearAllCheckboxes()'>Clear</button>");
        out.println("</div>");

        // Display selected columns
        out.println("<hr><h5 class='text-center fw-bold'>Selected Columns for Report</h5>");
        out.println("<div class='d-flex justify-content-center'><div id='selectedColumnsTable'></div></div>");

        // Display selected group by
        out.println("<h5 class='text-center fw-bold mt-3'>Group By Columns</h5>");
        out.println("<div class='d-flex justify-content-center'><div id='groupByTable'></div></div>");

        // Hidden inputs to send data to server
        out.println("<input type='hidden' name='columns' id='columnsInput'>");
        out.println("<input type='hidden' name='groupBy' id='groupByInput'>");

        out.println("<button type='submit' class='btn btn-outline-danger fw-bold mt-3'>Download PDF</button>");
        out.println("</form>");
        
        
      
        // PDF JavaScript for dynamic behavior
        out.println("<script>");
        
        
        
        out.println("let selectedColumns = [];");
        out.println("let selectedGroupBy = [];");

        //track column select
        out.println("function trackSelection(checkbox) {");
        out.println("  const val = checkbox.value;");
        out.println("  if (checkbox.checked && !selectedColumns.includes(val)) selectedColumns.push(val);");
        out.println("  else selectedColumns = selectedColumns.filter(c => c !== val);");
        out.println("  updateHiddenInputs(); renderTables();");
        out.println("}");

        //track group by
        out.println("function trackGroupBy(checkbox) {");
        out.println("  const val = checkbox.value;");
        out.println("  if (checkbox.checked) {");
        out.println("    if (!selectedGroupBy.includes(val)) selectedGroupBy.push(val);");
        out.println("    // Check corresponding Select Column checkbox if not checked");
        out.println("    let selectCheckbox = document.querySelector('.column-checkbox[value=\"' + val + '\"]');");
        out.println("    if (selectCheckbox && !selectCheckbox.checked) {");
        out.println("      selectCheckbox.checked = true;");
        out.println("      if (!selectedColumns.includes(val)) selectedColumns.push(val);");
        out.println("    }");
        out.println("  } else {");
        out.println("    selectedGroupBy = selectedGroupBy.filter(c => c !== val);");
        out.println("  }");
        out.println("  updateHiddenInputs(); renderTables();");
        out.println("}");
        
        //select all columns
        out.println("function selectAllColumns() {");
        out.println("  const checkboxes = document.querySelectorAll('.column-checkbox');");
        out.println("  selectedColumns = [];");
        out.println("  checkboxes.forEach(cb => {");
        out.println("    cb.checked = true;");
        out.println("    if (!selectedColumns.includes(cb.value)) selectedColumns.push(cb.value);");
        out.println("  });");
        out.println("  updateHiddenInputs();");
        out.println("  renderTables();");
        out.println("}");

        //clear button
        out.println("function clearAllCheckboxes() {");
        out.println("  document.querySelectorAll('.column-checkbox, .groupby-checkbox').forEach(cb => cb.checked = false);");
        out.println("  selectedColumns = [];");
        out.println("  selectedGroupBy = [];");
        out.println("  updateHiddenInputs(); renderTables();");
        out.println("}");

        out.println("function updateHiddenInputs() {");
        out.println("  document.getElementById('columnsInput').value = selectedColumns.join(',');");
        out.println("  document.getElementById('groupByInput').value = selectedGroupBy.join(',');");
        out.println("}");

        out.println("function renderTables() {");
        out.println("  const sc = document.getElementById('selectedColumnsTable');");
        out.println("  const gb = document.getElementById('groupByTable');");

        out.println("  sc.innerHTML = '';");
        out.println("  gb.innerHTML = '';");

        out.println("  if (selectedColumns.length > 0) {");
        out.println("    const table = document.createElement('table');");
        out.println("    table.className = 'table table-bordered table-striped table-hover text-center';");
        out.println("    const thead = document.createElement('thead');");
        out.println("    thead.className = 'table-dark';");
        out.println("    const row = document.createElement('tr');");
        out.println("    selectedColumns.forEach(col => {");
        out.println("      const th = document.createElement('th');");
        out.println("      th.textContent = col;");
        out.println("      row.appendChild(th);");
        out.println("    });");
        out.println("    thead.appendChild(row);");
        out.println("    table.appendChild(thead);");
        out.println("    sc.appendChild(table);");
        out.println("  }");

        out.println("  if (selectedGroupBy.length > 0) {");
        out.println("    const table = document.createElement('table');");
        out.println("    table.className = 'table table-bordered table-striped table-hover text-center';");
        out.println("    const thead = document.createElement('thead');");
        out.println("    thead.className = 'table-info';");
        out.println("    const row = document.createElement('tr');");
        out.println("    selectedGroupBy.forEach(col => {");
        out.println("      const th = document.createElement('th');");
        out.println("      th.textContent = col;");
        out.println("      row.appendChild(th);");
        out.println("    });");
        out.println("    thead.appendChild(row);");
        out.println("    table.appendChild(thead);");
        out.println("    gb.appendChild(table);");
        out.println("  }");
        out.println("}");

        out.println("function validateForm() {");
        out.println("  if (selectedColumns.length === 0) {");
        out.println("    alert('Please select at least one column to generate the PDF.');");
        out.println("    return false;");
        out.println("  }");
        out.println("  return true;");
        out.println("}");        
        out.println("</script>");
        
        
     // CSV Form
        out.println("<hr><form method='get' action='DownloadStudentCSV' target='_blank' class='text-center'>");

        String[] columnss = {"ID", "StudentName", "PhoneNo", "Native", "Email", "DOB", "Department"};
        String[] labelss = {"ID", "Name", "Phone", "Native", "Email", "DOB", "Department"};

        for (int i = 0; i < columnss.length; i++) {
            out.println("<label class='form-check form-check-inline'>");
            out.println("<input class='form-check-input column-checkbox-csv' type='checkbox' value='" + columnss[i] + "' onclick='trackSelectionCSV(this)'> " + labelss[i]);
            out.println("</label>");
        }

        // Clear button
        out.println("<div class='form-check form-check-inline'>");
        out.println("<button type='button' class='btn btn-danger btn-sm ms-2' onclick='clearAllCheckboxesCSV()'>Clear</button>");
        out.println("</div>");

        out.println("<hr><h5 class='text-center fw-bold'>Selected Columns for CSV Report</h5>");
        out.println("<div class='d-flex justify-content-center'>");
        out.println("<div id='selectedColumnsTableCSV'></div>");
        out.println("</div>");

        out.println("<input type='hidden' name='columns' id='columnsInputCSV'>");
        out.println("<button type='submit' class='btn btn-outline-danger fw-bold mt-2'>Download Selected Columns (CSV)</button>");

        out.println("<script>");
        out.println("let selectedColumnsCSV = [];");

        out.println("function trackSelectionCSV(checkbox) {");
        out.println("  const value = checkbox.value;");
        out.println("  if (checkbox.checked) {");
        out.println("    if (!selectedColumnsCSV.includes(value)) selectedColumnsCSV.push(value);");
        out.println("  } else {");
        out.println("    selectedColumnsCSV = selectedColumnsCSV.filter(col => col !== value);");
        out.println("  }");
        out.println("  updateHiddenInputCSV();");
        out.println("  renderSelectedColumnsCSV();");
        out.println("}");

        out.println("function clearAllCheckboxesCSV() {");
        out.println("  document.querySelectorAll('.column-checkbox-csv').forEach(cb => cb.checked = false);");
        out.println("  selectedColumnsCSV = [];");
        out.println("  updateHiddenInputCSV();");
        out.println("  renderSelectedColumnsCSV();");
        out.println("}");

        out.println("function updateHiddenInputCSV() {");
        out.println("  document.getElementById('columnsInputCSV').value = selectedColumnsCSV.join(',');");
        out.println("}");

        out.println("function renderSelectedColumnsCSV() {");
        out.println("  const container = document.getElementById('selectedColumnsTableCSV');");
        out.println("  container.innerHTML = '';");
        out.println("  if (selectedColumnsCSV.length === 0) return;");
        out.println("  const table = document.createElement('table');");
        out.println("  table.className = 'table table-bordered table-striped table-hover text-center';");
        out.println("  const thead = document.createElement('thead');");
        out.println("  thead.className = 'table-dark';");
        out.println("  const headRow = document.createElement('tr');");
        out.println("  selectedColumnsCSV.forEach(col => {");
        out.println("    const th = document.createElement('th');");
        out.println("    th.textContent = col;");
        out.println("    headRow.appendChild(th);");
        out.println("  });");
        out.println("  thead.appendChild(headRow);");
        out.println("  table.appendChild(thead);");
        out.println("  container.appendChild(table);");
        out.println("}");
        out.println("</script>");
      
        
        System.out.println("Hello");

        out.println("<br><br><br><br><br><br><br></body></html>");
    }
}


