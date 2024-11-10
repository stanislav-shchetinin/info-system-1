<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>User Form</title>
</head>
<body>
<h2>User Form</h2>
<form action="users" method="post">
    <input type="hidden" name="id" value="${user.id}">
    <label>Name:</label>
    <input type="text" name="name" value="${user.name}">
    <br>
    <label>Email:</label>
    <input type="email" name="email" value="${user.email}">
    <br>
    <input type="submit" value="Save">
</form>
<a href="users">Back to user list</a>
</body>
</html>