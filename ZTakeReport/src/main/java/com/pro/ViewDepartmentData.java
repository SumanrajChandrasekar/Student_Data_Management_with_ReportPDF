package com.pro;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ViewDepartmentData")
public class ViewDepartmentData extends HttpServlet {
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/StudentRegister";
    private static final String DB_USER = "sumanraj";
    private static final String DB_PASS = "12345678";
    
    private static final String View_Query = "SELECT * FROM Dept_List";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        
        out.println("<!DOCTYPE html><html><head><title>Department List</title>");
        out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css' rel='stylesheet'>");
        out.println("<style>");
        out.println("body { background-color: #ccebff; font-size: 0.9rem; }");
        out.println(".table-container { max-height: 80vh; overflow-y: auto; }");
        out.println("thead th { position: sticky; top: 0; background-color: #212529; color: white; }");
        out.println("</style></head><body>");
        out.println("<div class='container mt-4'>");

        out.println("<div class='mb-3'>");      
        out.println("<a href='Index.jsp' class='btn btn-outline-success fw-bold'>Home</a>");
        out.println("<a href='RegisterDepartment.jsp' class='btn btn-outline-primary fw-bold'>Register Department</a>");
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
            out.println("<th>ID</th>");
            out.println("<th>DeptName</th>");
            out.println("<th>DeptCode</th>");
            out.println("<th>Edit</th>");
            out.println("<th>Delete</th>");
            out.println("</tr></thead><tbody>");
            
            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getInt(1) + "</td>");
                out.println("<td>" + rs.getString(2) + "</td>");
                out.println("<td>" + rs.getString(3) + "</td>");

                // Edit
                out.println("<td>");
                out.println("<form action='UpdateDepartmentData.jsp' method='get' onsubmit='return confirm(\"Are you sure you want to edit?\")'>");
                out.println("<input type='hidden' name='id' value='" + rs.getInt("ID") + "'/>");
                out.println("<input type='hidden' name='deptname' value='" + rs.getString("DeptName") + "'/>");
                out.println("<input type='hidden' name='deptcode' value='" + rs.getString("DeptCode") + "'/>");
                out.println("<button type='submit' class='btn btn-warning btn-sm fw-bold'>Edit</button>");
                out.println("</form>");
                out.println("</td>");

                // Delete
                out.println("<td>");
                out.println("<form action='DeleteDepartmentData' method='post' onsubmit='return confirm(\"Are you sure you want to delete?\")'>");
                out.println("<input type='hidden' name='id' value='" + rs.getInt(1) + "'/>");
                out.println("<button type='submit' class='btn btn-danger btn-sm fw-bold'>Delete</button>");
                out.println("</form>");
                out.println("</td>");
                out.println("</tr>");
            }

            out.println("</tbody></table></div>");

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<div class='alert alert-danger text-center'>Error: " + e.getMessage() + "</div>");
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // PDF download form and column selection
        out.println("<hr><form method='get' action='DownloadDepartmentPDF' target='_blank' class='text-center'>");

        // Columns and labels
        String[] columns = {"ID", "DeptName", "DeptCode"};
        String[] labels = {"ID", "Dept Name", "Dept Code"};

        // Group by dropdown
        out.println("<div class='mb-3'>");
        out.println("<label class='form-label fw-bold'>Group By Column</label>");
        out.println("<select class='form-select w-auto d-inline' id='groupBySelect' onchange='selectGroupByColumn()'>");
        out.println("<option value=''>-- Select Column --</option>");
        for (int i = 0; i < columns.length; i++) {
            out.println("<option value='" + columns[i] + "'>" + labels[i] + "</option>");
        }
        out.println("</select>");
        out.println("</div>");

        // Column checkboxes
        for (int i = 0; i < columns.length; i++) {
            out.println("<label class='form-check form-check-inline'>");
            out.println("<input class='form-check-input column-checkbox' type='checkbox' value='" + columns[i] + "' onclick='trackSelection(this)'> " + labels[i]);
            out.println("</label>");
        }

        // Clear button
        out.println("<div class='form-check form-check-inline'>");
        out.println("<button type='button' class='btn btn-danger btn-sm ms-2' onclick='clearAllCheckboxes()'>Clear</button>");
        out.println("</div>");

        // Selected columns display
        out.println("<hr><h5 class='text-center fw-bold'>Selected Columns for Report</h5>");
        out.println("<div class='d-flex justify-content-center'>");
        out.println("<div id='selectedColumnsTable'></div>");
        out.println("</div>");

        out.println("<input type='hidden' name='columns' id='columnsInput'>");
        out.println("<button type='submit' class='btn btn-outline-danger fw-bold mt-2'>Download Selected Columns (PDF)</button>");

        // JavaScript
        out.println("<script>");
        out.println("let selectedColumns = [];");

        // trackSelection
        out.println("function trackSelection(checkbox) {");
        out.println("  const value = checkbox.value;");
        out.println("  if (checkbox.checked) {");
        out.println("    if (!selectedColumns.includes(value)) selectedColumns.push(value);");
        out.println("  } else {");
        out.println("    selectedColumns = selectedColumns.filter(col => col !== value);");
        out.println("  }");
        out.println("  updateHiddenInput();");
        out.println("  renderSelectedColumns();");
        out.println("}");

        // clearAllCheckboxes
        out.println("function clearAllCheckboxes() {");
        out.println("  document.querySelectorAll('.column-checkbox').forEach(cb => cb.checked = false);");
        out.println("  selectedColumns = [];");
        out.println("  updateHiddenInput();");
        out.println("  renderSelectedColumns();");
        out.println("}");

        // update hidden input
        out.println("function updateHiddenInput() {");
        out.println("  document.getElementById('columnsInput').value = selectedColumns.join(',');");
        out.println("}");

        // render selected columns
        out.println("function renderSelectedColumns() {");
        out.println("  const container = document.getElementById('selectedColumnsTable');");
        out.println("  container.innerHTML = '';");
        out.println("  if (selectedColumns.length === 0) return;");
        out.println("  const table = document.createElement('table');");
        out.println("  table.className = 'table table-bordered table-striped table-hover text-center';");
        out.println("  const thead = document.createElement('thead');");
        out.println("  thead.className = 'table-dark';");
        out.println("  const headRow = document.createElement('tr');");
        out.println("  selectedColumns.forEach(col => {");
        out.println("    const th = document.createElement('th');");
        out.println("    th.textContent = col;");
        out.println("    headRow.appendChild(th);");
        out.println("  });");
        out.println("  thead.appendChild(headRow);");
        out.println("  table.appendChild(thead);");
        out.println("  container.appendChild(table);");
        out.println("}");

        out.println("function selectGroupByColumn() {");
        out.println("  const selectedGroupBy = document.getElementById('groupBySelect').value;");
        out.println("  if (!selectedGroupBy) return;");

        out.println("  const checkboxes = document.querySelectorAll('.column-checkbox');");

        out.println("  // Uncheck all checkboxes and reset selectedColumns array");
        out.println("  checkboxes.forEach(cb => cb.checked = false);");
        out.println("  selectedColumns = [];");

        out.println("  // Now select the matching checkbox");
        out.println("  checkboxes.forEach(cb => {");
        out.println("    if (cb.value === selectedGroupBy) {");
        out.println("      cb.checked = true;");
        out.println("      selectedColumns.push(cb.value);");
        out.println("    }");
        out.println("  });");

        out.println("  updateHiddenInput();");
        out.println("  renderSelectedColumns();");
        out.println("}");
        out.println("</form>");
        out.println("</script>");

        out.println("<br><br><br><br><br><br><br><br></div></body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
    }
}