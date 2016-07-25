<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />

    <title>Cart</title>

    <c:url value="/resources/css/bootstrap.min.css" var="bootstrapCss"/>
    <link href="${bootstrapCss}" rel="stylesheet"/>

</head>
<body>

    <jsp:include page="fragments/header.jsp"/>

    <div class="container">
        <form:form id="updateCartForm" action="${pageContext.request.contextPath}/cart/update" modelAttribute="phoneQuantityList" method="post">
            <c:set var="formHasErrors">
                <form:errors path="*" cssClass="hidden"/>
            </c:set>
            <ul class="list-group">
                <li class="list-group-item">
                    <div class="table-responsive">
                            <table id="phonesTable" class="table table-hover">
                                <thead>
                                    <tr>
                                        <th><div class="cell-text-alignment col-md-2">Name</div></th>
                                        <th><div class="cell-text-alignment col-md-2">Code</div></th>
                                        <th><div class="cell-text-alignment col-md-2">Price</div></th>
                                        <th><div class="cell-text-alignment col-md-3">Quantity</div></th>
                                        <th></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${order.orderItems}" var="orderItem" varStatus="loop">
                                        <tr>
                                            <td>
                                                <div class="cell-text-alignment col-md-2">${orderItem.phone.name}</div>
                                            </td>
                                            <td>
                                                <div class="cell-text-alignment col-md-2">${orderItem.phone.code}</div>
                                            </td>
                                            <td>
                                                <div class="cell-text-alignment col-md-2">${orderItem.phone.price}</div>
                                            </td>
                                            <td>
                                                <div class="cell-text-alignment">
                                                    <c:set var="phoneIncorrectQuantity" value="phoneQuantity-${loop.index}"/>
                                                    <form:input path="phonesQuantityList[${loop.index}].id" cssClass="hidden" type="text" value="${orderItem.phone.id}"/>
                                                    <div class="row">
                                                        <c:if test="${empty requestScope[phoneIncorrectQuantity]}">
                                                            <form:input name="${orderItem.phone.id}" type="text" value="${orderItem.quantity}" path="phonesQuantityList[${loop.index}].quantity"/>
                                                        </c:if>
                                                        <c:if test="${not empty requestScope[phoneIncorrectQuantity]}">
                                                            <form:input name="${orderItem.phone.id}" type="text" value="${requestScope[phoneIncorrectQuantity]}" path="phonesQuantityList[${loop.index}].quantity"/>
                                                        </c:if>
                                                    </div>
                                                    <div class="row">
                                                        <form:errors path="phonesQuantityList[${loop.index}].quantity" cssClass="text-danger"/>
                                                    </div>
                                                </div>
                                            </td>
                                            <td>
                                                <div class="cell-alignment col-md-1">
                                                    <button type="button" id="removeButton-${orderItem.phone.id}" name="removePhoneButton" data-url="${pageContext.request.contextPath}/cart/remove" class="btn btn-danger" title="Remove">
                                                        <span class="glyphicon glyphicon-remove"></span>
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        <div id="emptyTableMessage" class="well well-lg text-center hidden">
                            There are no phones in the cart.
                        </div>
                    </div>
                </li>
                <c:if test="${order.totalPrice != null && order.totalPrice != 0.0}">
                    <li class="list-group-item list-group-item-info">
                        <div class="row">
                            <span class="col-md-3 col-md-offset-3">
                                <h4 id="totalPrice">
                                    Total price: <fmt:formatNumber value="${order.totalPrice}"
                                                                   maxFractionDigits="2" type="currency"/>
                                </h4>
                            </span>
                            <span class="col-md-2">
                                <button type="button" id="updateButton" class="btn btn-warning btn-lg">Update</button>
                            </span>
                            <span class="col-md-2 col-md-offset-2">
                                <c:if test="${empty formHasErrors}">
                                    <button type="button" id="submitButton" class="btn btn-info btn-lg">Submit</button>
                                </c:if>
                                <c:if test="${not empty formHasErrors}">
                                    <button type="button" id="submitButton" class="btn btn-info btn-lg disabled">Submit</button>
                                </c:if>
                            </span>
                        </div>
                    </li>
                </c:if>
            </ul>
        </form:form>
        <form id="submitCartForm" action="${pageContext.request.contextPath}/cart" method="post"></form>
    </div>

    <c:url value="/resources/js/jquery-3.0.0.min.js" var="jquery"/>
    <script type="text/javascript" src="${jquery}"></script>
    <c:url value="/resources/js/cart.js" var="cart"/>
    <script type="text/javascript" src="${cart}"></script>


</body>
</html>
