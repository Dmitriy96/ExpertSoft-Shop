<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />

    <title>Order</title>

    <c:url value="/resources/css/bootstrap.min.css" var="bootstrapCss"/>
    <link href="${bootstrapCss}" rel="stylesheet"/>

</head>
<body>

    <jsp:include page="fragments/header.jsp"/>

    <div class="container">
        <div class="page-header">
            <h2>Personal data</h2>
        </div>
        <c:if test="${not empty error}">
            <div class="alert alert-danger">
                ${error}
            </div>
        </c:if>
        <form action="${pageContext.request.contextPath}/personal_data" method="post">
            <div class="form-group">
                <label for="nameInput">Name</label>
                <input type="text" class="form-control" id="nameInput" name="name" placeholder="Name">
            </div>
            <div class="form-group">
                <label for="surnameInput">Surname</label>
                <input type="text" class="form-control" id="surnameInput" name="surname" placeholder="Surname">
            </div>
            <div class="form-group">
                <label for="phoneNumberInput">Phone number</label>
                <input type="text" class="form-control" id="phoneNumberInput" name="phoneNumber" placeholder="Phone number">
            </div>
            <div class="checkbox">
                <label>
                    <input id="deliveryCostInput" name="deliveryCost" type="checkbox"> Accept fixed delivery cost: 5$
                </label>
            </div>
            <button type="submit" class="btn btn-success btn-lg">Ok</button>
        </form>
    </div>

</body>
</html>
