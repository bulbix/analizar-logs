<%-- 
    Document   : 403
    Created on : 25/07/2017, 10:18:16 AM
    Author     : acruzb
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
    
    </head>
<body>
    <div class="container">
        <div class="starter-template">
            <h1>403 - Access is denied</h1>
            <c:if test="${pageContext.request.userPrincipal.name != null}">
		<h2>
                    <h3>Welcome : Hello '${pageContext.request.userPrincipal.name}' you do not have permission to access this page.</h3>
		</h2>
            </c:if>
        </div>

    </div>
</body>
</html>