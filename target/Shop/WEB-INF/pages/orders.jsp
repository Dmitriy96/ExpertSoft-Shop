<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />

    <title>Orders</title>

    <c:url value="/resources/css/bootstrap.min.css" var="bootstrapCss"/>
    <link href="${bootstrapCss}" rel="stylesheet"/>

</head>
<body>

<jsp:include page="fragments/header.jsp"/>

    <div class="container">
        <c:forEach items="${orders}" var="order" varStatus="loop">
            <p>
                <ul class="list-group">
                    <li class="list-group-item">
                        <div class="table-responsive">
                            <table id="ordersTable-${order.id}" class="table table-hover">
                                <thead>
                                <tr>
                                    <th><div class="cell-text-alignment">Phone name</div></th>
                                    <th><div class="cell-text-alignment">Phones quantity</div></th>
                                    <th><div class="cell-text-alignment">Order status</div></th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${order.orderItems}" var="orderItem" varStatus="loop">
                                    <tr>
                                        <td class="col-md-2">
                                            <div class="cell-text-alignment">${orderItem.phone.name}</div>
                                        </td>
                                        <td class="col-md-2">
                                            <div class="cell-text-alignment">${orderItem.quantity}</div>
                                        </td>
                                        <td class="col-md-2">
                                            <div data-name="orderStatus" class="cell-text-alignment">${order.status}</div>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </li>
                    <li class="list-group-item list-group-item-info">
                        <div class="row">
                            <span class="col-md-4">
                                <h4>
                                    Ordering date: ${order.date}
                                </h4>
                            </span>
                            <span class="col-md-4">
                                <h4>
                                    Total price: <fmt:formatNumber value="${order.totalPrice}"
                                                               maxFractionDigits="2" type="currency"/>
                                </h4>
                            </span>
                            <span class="col-md-2">
                                <button type="button" id="deliveredButton-${order.id}" name="deliveredButton"
                                        data-url="${pageContext.request.contextPath}/order/${order.id}/status"
                                        class="btn btn-warning" title="Delivered">
                                    Delivered
                                </button>
                            </span>
                        </div>
                    </li>
                </ul>
            </p>
        </c:forEach>
        <c:if test="${empty orders}">
            <div class="well well-lg text-center">
                You haven't got orders.
            </div>
        </c:if>
    </div>

    <c:url value="/resources/js/jquery-3.0.0.min.js" var="jquery"/>
    <script type="text/javascript" src="${jquery}"></script>
    <c:url value="/resources/js/orders.js" var="orders"/>
    <script type="text/javascript" src="${orders}"></script>

</body>
</html>
