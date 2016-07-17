<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Head</title>
        <c:url value="/resources/css/bootstrap.min.css" var="bootstrap"/>
        <link href="${bootstrap}" rel="stylesheet"/>
    </head>
    <body>

        <div class="container">
            <nav class="navbar navbar-default">
                <div class="container">
                    <div class="container-fluid">
                        <div class="collapse navbar-collapse">
                            <ul class="nav navbar-nav">
                                <li><a href="${pageContext.request.contextPath}/main">Home</a></li>
                                <li><a href="${pageContext.request.contextPath}/orders">Orders</a></li>
                                <li><a id="cartLink" href="${pageContext.request.contextPath}/cart">Cart</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </nav>
        </div>

    </body>
</html>
