<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
    <head>
        <jsp:include page="fragments/header.jsp" />
        <c:url value="/css/main.css" var="jstlCss" />
        <link href="${jstlCss}" rel="stylesheet" />
    </head>
    <body>
        <div class="container">
            <div class="starter-template">
                <h1>Spring Boot Web JSP Example</h1>
                <h2>Message: ${message}</h2>
                <h2>Bienvenido: ${usuario}</h2>
            </div>
        </div>
        <jsp:include page="fragments/footer.jsp" />
    </body>
</html>