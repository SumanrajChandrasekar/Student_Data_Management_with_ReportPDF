<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Update Student</title>
<link rel="stylesheet" href="css/bootstrap.css"> 
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

<style>
    body {
        background-color: #ccebff;
        font-size: 2rem;
    }

    .card-container {
        background-color: white;
        padding: 2.5rem;
        border-radius: 12px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        max-width: 30rem;
        margin: 3rem auto;
        font-size: 1.5rem;
    }

    .btn-lg-custom {
        font-size: 2rem;
        padding: 0.8rem 1.6rem;
        border-radius: 0.5rem;
    }
</style>
</head>


<body>

<div class="text-center mt-3">
    <a href="Index.jsp" class="btn btn-outline-primary text-decoration-none fw-bold">Home</a>
    <a href="ViewStudentData" class="btn btn-outline-success text-decoration-none fw-bold">View Student Data</a>
</div>

<div class="card-container">

    <h1 class="bg-info text-white text-center p-2 rounded">Update Student Data</h1>


    <%
    String id = request.getParameter("id");
    String name = request.getParameter("name");
    String phone = request.getParameter("phone");
    String nativeplace = request.getParameter("nativeplace");
    String email = request.getParameter("email");
    String dob = request.getParameter("dob");
    String dept = request.getParameter("dept");
%>

<form action="UpdateStudentData" method="post">
    <table>
        <tr>
            <td><input type="text" name="id" id="id" value="<%= id %>" readonly></td>
        </tr>
        <tr> 
            <td><input type="text" name="name" placeholder="Enter Name" value="<%= name %>" required></td>
        </tr>
        <tr>
            <td><input type="tel" name="phone" pattern="[6-9]{1}[0-9]{9}" maxlength="10" placeholder="Enter Phone no" value="<%= phone %>" required></td>
        </tr>
        <tr>
            <td><input type="text" name="nativeplace" placeholder="Enter native" value="<%= nativeplace %>" required></td>
        </tr>
        <tr>
            <td><input type="email" name="email" placeholder="Enter Email" value="<%= email %>" required></td>
        </tr>
        <tr>
            <td><input type="date" name="dob" placeholder="Enter DOB" value="<%= dob %>" required></td>
        </tr>
        <tr>
            <td><input type="text" name="dept" placeholder="Enter dept" value="<%= dept %>" required></td>
        </tr>
        <tr>
            <td><input type="submit" id="submit" value="Update" class="btn btn-success mt-3"></td>
            <td><input type="reset" value="Cancel" class="btn btn-danger mt-3"></td>
        </tr>
    </table>
</form>

</div>

</body>
</html>