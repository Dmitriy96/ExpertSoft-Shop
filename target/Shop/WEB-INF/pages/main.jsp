<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />

    <title>Main</title>

    <c:url value="/resources/css/bootstrap.min.css" var="bootstrapCss"/>
    <link href="${bootstrapCss}" rel="stylesheet"/>

</head>
<body>

    <jsp:include page="fragments/header.jsp"/>

    <div class="container">
        <div id="phoneAddedToCartMessage" class="alert alert-success">
            <span class="glyphicon glyphicon-ok"></span>
            Phone successfully added to cart!
        </div>
        <div class="table-responsive">
            <table id="phonesTable" class="table table-hover">
                <thead>
                <tr>
                    <th><div class="cell-text-alignment">Name</div></th>
                    <th><div class="cell-text-alignment">Code</div></th>
                    <th><div class="cell-text-alignment">Price</div></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${phones}" var="orderItem" varStatus="loop">
                    <tr>
                        <td class="col-md-2">
                            <div class="cell-text-alignment">${orderItem.name}</div>
                        </td>
                        <td class="col-md-2">
                            <div class="cell-text-alignment">${orderItem.code}</div>
                        </td>
                        <td class="col-md-2">
                            <div class="cell-text-alignment">${orderItem.price}</div>
                        </td>
                        <td class="col-md-2">
                            <div class="cell-alignment">
                                <input type="text" name="quantityInput" min="1" value="1"/>
                                <button type="button" id="${orderItem.id}" name="addPhoneToCart" data-url="${pageContext.request.contextPath}/cart/add/${orderItem.id}" class="btn btn-info" title="Add">
                                    <span class="glyphicon glyphicon-shopping-cart"></span>
                                </button>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <c:if test="${empty phones}">
                <div class="well well-lg text-center">
                    There are no phones in the shop.
                </div>
            </c:if>
        </div>
    </div>

    <c:url value="/resources/js/jquery-3.0.0.min.js" var="jquery"/>
    <script type="text/javascript" src="${jquery}"></script>
    <c:url value="/resources/js/main.js" var="main"/>
    <script type="text/javascript" src="${main}"></script>


</body>
</html>
