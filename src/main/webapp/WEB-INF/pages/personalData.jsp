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
        <form action="${pageContext.request.contextPath}/personal_data" method="post">
            <div class="form-group">
                <label for="nameInput">Name</label>
                <input type="text" class="form-control" id="nameInput" name="name" value="${order.name}" placeholder="Name">
                <c:if test="${not empty nameError}">
                    <div class="text-danger">${nameError}</div>
                </c:if>
            </div>
            <div class="form-group">
                <label for="surnameInput">Surname</label>
                <input type="text" class="form-control" id="surnameInput" name="surname" value="${order.surname}" placeholder="Surname">
                <c:if test="${not empty surnameError}">
                    <div class="text-danger">${surnameError}</div>
                </c:if>
            </div>
            <div class="form-group">
                <label for="phoneNumberInput">Phone number</label>
                <input type="text" class="form-control" id="phoneNumberInput" name="phoneNumber" value="${order.phoneNumber}" placeholder="Phone number format: X-X-XXXXXXX">
                <c:if test="${not empty phoneNumberError}">
                    <div class="text-danger">${phoneNumberError}</div>
                </c:if>
            </div>
            <div class="checkbox">
                <label>
                    <input id="deliveryCostInput" name="deliveryCost" type="checkbox"> Accept fixed delivery cost: 5$
                </label>
            </div>
            <c:if test="${not empty deliveryCostError}">
                <div class="text-danger">${deliveryCostError}</div>
            </c:if>
            <button type="submit" class="btn btn-success btn-lg">Ok</button>
        </form>
    </div>

</body>
</html>
