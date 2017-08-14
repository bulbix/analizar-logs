<%-- 
    Document   : login
    Created on : 25/07/2017, 10:01:22 AM
    Author     : acruzb
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
    <title>Inicio de sesión</title>
    <jsp:include page="fragments/header.jsp" />
</head>
<body onload='document.loginForm.username.focus();'>
<div class="container">
    <div class="row" style="margin-top:20px">
        <div class="col-xs-12 col-sm-8 col-md-6 col-sm-offset-2 col-md-offset-3">
            <form action="<c:url value='/login' />" method="post">
                <fieldset>
                    <h1>Please Sign In</h1>

                    <c:if test="${not empty error}">
                        <div class="alert alert-danger">
                            Invalid username and password.
                        </div>
                    </c:if>
                    <c:if test="${not empty msg}">
			<div class="msg">${msg}</div>
                    </c:if>
<!--                    <div th:if="${param.logout}">
                        <div class="alert alert-info">
                            You have been logged out.
                        </div>
                    </div>-->
                    <div class="form-group">
                        <input type="text" name="username" id="username" class="form-control input-lg"
                               placeholder="UserName" required="true" autofocus="true"/>
                    </div>
                    <div class="form-group">
                        <input type="password" name="password" id="password" class="form-control input-lg"
                               placeholder="Password" required="true"/>
                    </div>

                    <div class="row">
                        <div class="col-xs-6 col-sm-6 col-md-6">
                            <input type="submit" class="btn btn-lg btn-primary btn-block" value="Sign In"/>
                        </div>
                        <div class="col-xs-6 col-sm-6 col-md-6">
                            <a href="<c:url value='/motor' />">Ir al motor de búsqueda</a>
                        </div>
                    </div>
                </fieldset>
            </form>
        </div>
    </div>
</div>
<jsp:include page="fragments/footer.jsp" />
</body>
</html>