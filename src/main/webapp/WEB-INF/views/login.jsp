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
    <link href="<c:url value='/css/login.css'/>" rel="stylesheet" type="text/css">
</head>
<body onload='document.loginForm.username.focus();'>
    <!--    <div class="container">
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
    <div th:if="${param.logout}">
        <div class="alert alert-info">
            You have been logged out.
        </div>
    </div>
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
</div>-->
    <div class="container">
        <form action="<c:url value='/login'/>" method="post">
            <div id="login-box">
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">
                        Invalid username and password.
                    </div>
                </c:if>
                <c:if test="${not empty msg}">
                    <div class="msg">${msg}</div>
                </c:if>
<!--                <div th:if="${param.logout}">
                    <div class="alert alert-info">
                        You have been logged out.
                    </div>
                </div>-->
                <div class="logo">
                    <img src="http://lorempixel.com/output/people-q-c-100-100-1.jpg" class="img img-responsive img-circle center-block">
                    <h1 class="logo-caption"><span class="tweak">L</span>ogin</h1>
                </div>
                <div class="controls">
                    <input type="text" name="username" placeholder="Usuario" class="form-control">
                    <input type="password" name="password" placeholder="Contraseña" class="form-control">
                    <button type="submit" class="btn btn-default btn-block btn-custom">Iniciar</button>
                </div>
            </div> 
        </form>
    </div> 
    <div id="particles-js">
        <canvas class="particles-js-canvas-el" width="1423" height="496" style="width: 100%; height: 100%;"></canvas>
    </div>
    <jsp:include page="fragments/footer.jsp" />
    <script type="text/javascript" src="<c:url value='/js/extra/particles.min.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/js/login.js'/>"></script>
</body>
</html>