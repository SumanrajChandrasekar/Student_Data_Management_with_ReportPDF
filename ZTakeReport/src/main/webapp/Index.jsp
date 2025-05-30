<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Home</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

<style>
    body {
        background-color: #ccebff;
    }
     .card-container {
        background-color: white;
        padding: 2.5rem;
        border-radius: 12px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        max-width: 30rem;
        margin: 3rem auto;
        font-size: 1.5rem;
</style>
</head>

<body>

  <div class="card-container">

    <h1 class="bg-info text-white text-center p-3 mb-4 rounded">Student Management Portal</h1>

    <div class="d-grid gap-3">
        <a href="RegisterStudent.jsp" class="btn btn-outline-primary btn-lg text-decoration-none fw-bold">➕ Register Student</a>
        <a href="RegisterDepartment.jsp" class="btn btn-outline-warning btn-lg text-decoration-none fw-bold">➕ Register Department</a>
        
        <!-- <a href="Report" class="btn btn-outline-warning btn-lg text-decoration-none fw-bold">🔍 Filter Student Data</a> -->
        <a href="ViewStudentData" class="btn btn-outline-success btn-lg text-decoration-none fw-bold">📋 View Student Data</a>
        <a href="ViewDepartmentData" class="btn btn-outline-secondary btn-lg text-decoration-none fw-bold">🏫 View Department Data</a>
    </div>

  </div>

</body>
</html>