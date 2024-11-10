<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>User List</title>
</head>
<body>
<h2>User List</h2>
<a href="users?action=new">Add New User</a>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Email</th>
        <th>Actions</th>
    </tr>
    <c:forEach var="user" items="${userList}">
        <tr>
            <td>${user.id}</td>
            <td>${user.name}</td>
            <td>${user.email}</td>
            <td>
                <a href="users?action=edit&id=${user.id}">Edit</a>
                <a href="users?action=delete&id=${user.id}">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>