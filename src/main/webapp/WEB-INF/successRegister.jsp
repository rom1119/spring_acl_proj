<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<body>
<h1 th:text="#{label.form.title}">Register success <%${user}%></h1>


<a th:href="@{/login.html}" th:text="#{label.form.loginLink}">login</a>
</body>
</html>