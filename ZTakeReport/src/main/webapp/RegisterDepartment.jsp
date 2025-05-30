<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Register Department</title>
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
   <!--  <a href="ViewStudentData" class="btn btn-outline-success text-decoration-none fw-bold">View Student Data</a> -->
    <a href="ViewDepartmentData" class="btn btn-outline-secondary text-decoration-none fw-bold">View Department Data</a>
</div>

<div class="card-container">

    <h1 class="bg-info text-white text-center p-2 rounded">Register Department</h1>

    <p id="idMessage" style="color: red; font-weight: bold;"></p>

    <form action="RegisterDepartment" method="post">
        <table>
            <tr>
                <td><input type="text" name="id" id="id" required placeholder="Enter ID" oninput="checkDepartmenttId()"></td>
            </tr>
            <tr> 
                <td><input type="text" name="deptname" placeholder="Enter DepartmentName" required></td>
            </tr>
            <tr>
                <td><input type="tel" name="deptcode"  maxlength="10" placeholder="Enter DepartmentCode" required></td>
            </tr>
           
            <tr>
                <td><input type="submit" id="submit" value="Submit" class="btn btn-success mt-3"></td>
                <td><input type="reset" value="Cancel" class="btn btn-danger mt-3"></td>
            </tr>
        </table>
    </form>

</div>

<script>
function checkDepartmenttId() {
    const Id = document.getElementById("id").value.trim();
    const submitBtn = document.getElementById("submit");
    const idMessage = document.getElementById("idMessage");

    if (Id != "") {
        fetch('RegisterStudent?id=' + Id)
        .then(function (respo) {
            return respo.json(); 
        })
        .then(function (data) {
            if (data.exists) {
                submitBtn.value = "Update";
                idMessage.textContent = "ID already exists. Form will update existing record.";

                document.querySelector('input[name="deptname"]').value = data.deptname;
                document.querySelector('input[name="deptcode"]').value = data.deptcode;
                  
            } else {
                submitBtn.value = "Save";
                idMessage.textContent = "Register new dept";

                document.querySelector('input[name="deptname"]').value = '';
                document.querySelector('input[name="deptcode"]').value = '';
                
            }
        });
        
    } else {
        submitBtn.value = "Submit";
        idMessage.textContent = "";
    }
    
    if(Id==""){
    		document.querySelector('input[name="deptname"]').value = '';
            document.querySelector('input[name="deptcodes"]').value = '';
           
    }
    
}
</script>

</body>
</html>