package com.pro;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/DownloadStudentPDF")
public class DownloadStudentPdf extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/StudentRegister";
    private static final String DB_USER = "sumanraj";
    private static final String DB_PASS = "12345678";

    class FooterPageEvent extends PdfPageEventHelper {
        Font footerFont = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.GRAY);

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            Rectangle rect = writer.getBoxSize("art");
            Phrase pageNum = new Phrase(String.valueOf(writer.getPageNumber()), footerFont);

            boolean isLandscape = document.getPageSize().getWidth() > document.getPageSize().getHeight();

            float x;
            if (isLandscape) {
                x = rect.getLeft() + (rect.getRight() - rect.getLeft()) * 0.75f; // Shift center slightly to right
            } else {
                x = (rect.getLeft() + rect.getRight()) / 2;
            }

            float y = document.bottom() - 10;

            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_CENTER,
                    pageNum,
                    x,
                    y,
                    0);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String selected = request.getParameter("columns");
        String groupBy = request.getParameter("groupBy");
        String[] selectedColumns = (selected != null && !selected.isEmpty()) ? selected.split(",") : new String[0];
        String[] groupByColumns = (groupBy != null && !groupBy.isEmpty()) ? groupBy.split(",") : new String[0];

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

        String query = "SELECT " + String.join(",", selectedColumns) + " FROM Registered_Student_List";
        if (groupByColumns.length > 0) {
            query += " ORDER BY " + String.join(",", groupByColumns);
        }

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            String orientation = request.getParameter("orientation");
            Document document = "landscape".equalsIgnoreCase(orientation)
                    ? new Document(PageSize.A4.rotate(), 30, 30, 40, 30)
                    : new Document(PageSize.A4, 30, 30, 40, 30);

            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
            writer.setPageEvent(new FooterPageEvent());
            document.open();

            String logoPath = getServletContext().getRealPath("/images/logo.png");
            Image logo = Image.getInstance(logoPath);
            logo.setAlignment(Image.ALIGN_LEFT);

            Font collegeFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font addressFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Font tableTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13);
            Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

            if ("landscape".equalsIgnoreCase(orientation)) {
            	
            	logo.scaleToFit(50, 50);
                logo.setAlignment(Image.ALIGN_LEFT);

              
                PdfPTable innerTable = new PdfPTable(2);
                innerTable.setWidths(new float[]{1, 4});
                innerTable.setWidthPercentage(100);

                PdfPCell logoCell = new PdfPCell(logo, false);
                logoCell.setBorder(Rectangle.NO_BORDER);
                logoCell.setRowspan(2);
                logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                innerTable.addCell(logoCell);

                PdfPCell titleCell = new PdfPCell(new Phrase("ABC COLLEGE OF ENGINEERING", collegeFont));
                //titleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                titleCell.setPaddingLeft(115f);
                titleCell.setBorder(Rectangle.NO_BORDER);
                titleCell.setPaddingBottom(5);
                innerTable.addCell(titleCell);

                PdfPCell addressCell = new PdfPCell(new Phrase("2nd Street, Chennai - 600001", addressFont));
                //addressCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                addressCell.setPaddingLeft(160f);
                addressCell.setBorder(Rectangle.NO_BORDER);
                innerTable.addCell(addressCell);

                PdfPTable boxWrapper = new PdfPTable(1);
                boxWrapper.setWidthPercentage(100);
                PdfPCell boxCell = new PdfPCell();
                boxCell.addElement(innerTable);
                boxCell.setPadding(10);
                boxCell.setBorderWidth(1f);
                boxWrapper.addCell(boxCell);

                document.add(boxWrapper);

                // Spacing before heading
                Paragraph spaceBeforeHeading = new Paragraph();
                spaceBeforeHeading.setSpacingBefore(15f);
                document.add(spaceBeforeHeading);

                // Title + Date row
                String reportDate = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());
                PdfPTable headingRow = new PdfPTable(2);
                headingRow.setWidthPercentage(100);
                headingRow.setWidths(new float[]{6, 1}); // wider left column

                PdfPCell headingCell = new PdfPCell(new Phrase("Student Report", tableTitleFont));
                headingCell.setBorder(Rectangle.NO_BORDER);
                headingCell.setPaddingLeft(80f);
                headingCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headingCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                headingCell.setPaddingBottom(5f);

                PdfPCell dateCell = new PdfPCell(new Phrase("Date: " + reportDate, dateFont));
                dateCell.setBorder(Rectangle.NO_BORDER);
                dateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                dateCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                dateCell.setPaddingBottom(5f);
                dateCell.setPaddingRight(10f);

                headingRow.addCell(headingCell);
                headingRow.addCell(dateCell);
                document.add(headingRow);

                

                // Space before table
                Paragraph spaceBeforeTable = new Paragraph();
                spaceBeforeTable.setSpacingBefore(15f);
                document.add(spaceBeforeTable);
            } else {
            	
                logo.scaleToFit(50, 50);
                logo.setAlignment(Image.ALIGN_LEFT);

              
                PdfPTable innerTable = new PdfPTable(2);
                innerTable.setWidths(new float[]{1, 4});
                innerTable.setWidthPercentage(100);

                PdfPCell logoCell = new PdfPCell(logo, false);
                logoCell.setBorder(Rectangle.NO_BORDER);
                logoCell.setRowspan(2);
                logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                innerTable.addCell(logoCell);

                PdfPCell titleCell = new PdfPCell(new Phrase("ABC COLLEGE OF ENGINEERING", collegeFont));
                titleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                titleCell.setPaddingLeft(28f);
                titleCell.setBorder(Rectangle.NO_BORDER);
                titleCell.setPaddingBottom(5);
                innerTable.addCell(titleCell);

                PdfPCell addressCell = new PdfPCell(new Phrase("2nd Street, Chennai - 600001", addressFont));
                addressCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                addressCell.setPaddingLeft(80f);
                addressCell.setBorder(Rectangle.NO_BORDER);
                innerTable.addCell(addressCell);

                PdfPTable boxWrapper = new PdfPTable(1);
                boxWrapper.setWidthPercentage(100);
                PdfPCell boxCell = new PdfPCell();
                boxCell.addElement(innerTable);
                boxCell.setPadding(10);
                boxCell.setBorderWidth(1f);
                boxWrapper.addCell(boxCell);

                document.add(boxWrapper);

                // Spacing before heading
                Paragraph spaceBeforeHeading = new Paragraph();
                spaceBeforeHeading.setSpacingBefore(15f);
                document.add(spaceBeforeHeading);

                // Title + Date row
                String reportDate = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());
                PdfPTable headingRow = new PdfPTable(2);
                headingRow.setWidthPercentage(100);
                headingRow.setWidths(new float[]{6, 2}); // wider left column

                PdfPCell headingCell = new PdfPCell(new Phrase("Student Report", tableTitleFont));
                headingCell.setBorder(Rectangle.NO_BORDER);
                headingCell.setPaddingLeft(100f);
                headingCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headingCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                headingCell.setPaddingBottom(5f);
//
                PdfPCell dateCell = new PdfPCell(new Phrase("Date: " + reportDate, dateFont));
                dateCell.setBorder(Rectangle.NO_BORDER);
                dateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                dateCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
               
               

                headingRow.addCell(headingCell);
                headingRow.addCell(dateCell);
                document.add(headingRow);

                

                // Space before table
                Paragraph spaceBeforeTable = new Paragraph();
                spaceBeforeTable.setSpacingBefore(15f);
                document.add(spaceBeforeTable);
            }

            document.add(Chunk.NEWLINE);

            

            PdfPTable table = new PdfPTable(selectedColumns.length);
            table.setWidthPercentage(100);

            float[] columnWidths = new float[selectedColumns.length];
            for (int i = 0; i < selectedColumns.length; i++) {
                String col = selectedColumns[i].toLowerCase();
                if (col.contains("name")) columnWidths[i] = 2.3f;
                else if (col.contains("email")) columnWidths[i] = 5f;
                else if (col.contains("id") || col.contains("roll")) columnWidths[i] = 1.1f;
                else if (col.contains("department")) columnWidths[i] = 1.75f;
                else if (col.contains("native")) columnWidths[i] = 2.6f;
                else if (col.contains("phone")) columnWidths[i] = 2.8f;
                else if (col.contains("dob")) columnWidths[i] = 2.6f;
                else columnWidths[i] = 2f;
            }
            table.setWidths(columnWidths);

            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.BLACK);
            BaseColor headerBg = new BaseColor(220, 220, 220);

            for (String col : selectedColumns) {
                PdfPCell cell = new PdfPCell(new Phrase(col.toUpperCase(), headerFont));
                cell.setBackgroundColor(headerBg);
                cell.setPadding(7);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            Font rowFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
            BaseColor rowColor1 = BaseColor.WHITE;
            BaseColor rowColor2 = new BaseColor(240, 240, 240);
            int rowIndex = 0;
            Map<String, String> lastGroupValues = new HashMap<>();

            while (rs.next()) {
                boolean groupChanged = false;
                StringBuilder groupHeader = new StringBuilder();

                for (String groupCol : groupByColumns) {
                    String currentVal = rs.getString(groupCol);
                    String lastVal = lastGroupValues.get(groupCol);
                    if (!currentVal.equals(lastVal)) {
                        groupChanged = true;
                        lastGroupValues.put(groupCol, currentVal);
                    }
                    groupHeader.append(groupCol.toUpperCase()).append(": ").append(currentVal).append("  ");
                }

                if (groupChanged && groupHeader.length() > 0) {
                    Font groupFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.BLACK);
                    PdfPCell groupCell = new PdfPCell(new Phrase(groupHeader.toString().trim(), groupFont));
                    groupCell.setColspan(selectedColumns.length);
                    groupCell.setBackgroundColor(new BaseColor(240, 240, 230));
                    groupCell.setPadding(6);
                    table.addCell(groupCell);
                }

                BaseColor bg = (rowIndex % 2 == 0) ? rowColor1 : rowColor2;
                for (String col : selectedColumns) {
                    String value = rs.getString(col);
                    PdfPCell cell = new PdfPCell(new Phrase(value != null ? value : "", rowFont));
                    cell.setBackgroundColor(bg);
                    cell.setPadding(5);
                    cell.setBorderWidth(0.5f);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                }
                rowIndex++;
            }

            document.add(table);
            //document.add(new Paragraph(" ", FontFactory.getFont(FontFactory.HELVETICA, 1)).setSpacingBefore(25f));
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body><h3>Error generating PDF: " + e.getMessage() + "</h3></body></html>");
        }
    }
}