<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />

    <title>Order overview</title>

    <c:url value="/resources/css/bootstrap.min.css" var="bootstrapCss"/>
    <link href="${bootstrapCss}" rel="stylesheet"/>

</head>
<body>

    <jsp:include page="fragments/header.jsp"/>

    <div class="container">
        <div class="page-header">
            <h2>Order overview</h2>
        </div>
        <ul class="list-group">
            <li class="list-group-item">
                <h4><b>Name:</b> ${order.name}</h4>
            </li>
            <li class="list-group-item">
                <h4><b>Surname:</b> ${order.surname}</h4>
            </li>
            <li class="list-group-item">
                <h4><b>Phone number:</b> ${order.phoneNo}</h4>
            </li>
            <li class="list-group-item">
                <h4><b>Date:</b> ${order.date}</h4>
            </li>
            <li class="list-group-item">
                <div class="table-responsive">
                    <h4><b>Ordered phones</b></h4>
                    <table id="phonesTable" class="table table-hover">
                        <thead>
                        <tr>
                            <th><div class="cell-text-alignment">Name</div></th>
                            <th><div class="cell-text-alignment">Code</div></th>
                            <th><div class="cell-text-alignment">Quantity</div></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${order.orderItems}" var="orderItem" varStatus="loop">
                            <tr>
                                <td>
                                    <div class="cell-text-alignment">${orderItem.phone.name}</div>
                                </td>
                                <td>
                                    <div class="cell-text-alignment">${orderItem.phone.code}</div>
                                </td>
                                <td>
                                    <div class="cell-text-alignment">${orderItem.quantity}</div>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </li>
            <li class="list-group-item">
                <h4>
                    <b>Total price:</b> <fmt:formatNumber value="${order.totalPrice}"
                                                   maxFractionDigits="2" type="currency"/>
                </h4>
            </li>
        </ul>
        <div class="row">
            <button id="orderButton" type="button" data-url="${pageContext.request.contextPath}/order" class="btn btn-success btn-lg">Order</button>
            <button id="cancelButton" type="button" data-url="${pageContext.request.contextPath}/cart" class="btn btn-danger btn-lg">Cancel</button>
        </div>
        <form id="submitOrderForm" action="${pageContext.request.contextPath}/order" method="post"></form>
    </div>

    <c:url value="/resources/js/jquery-3.0.0.min.js" var="jquery"/>
    <script type="text/javascript" src="${jquery}"></script>
    <c:url value="/resources/js/order-overview.js" var="orderOverview"/>
    <script type="text/javascript" src="${orderOverview}"></script>

</body>
</html>
