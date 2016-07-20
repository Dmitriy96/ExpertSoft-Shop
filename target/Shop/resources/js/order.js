$(document).ready(function(){

    document.getElementById("orderButton").onclick = function () {
        var submitOrderForm = document.getElementById("submitOrderForm");
        submitOrderForm.action = this.getAttribute("data-url");
        submitOrderForm.method = "post";
        submitOrderForm.submit();
    };

    document.getElementById("cancelButton").onclick = function () {
        var submitOrderForm = document.getElementById("submitOrderForm");
        submitOrderForm.action = this.getAttribute("data-url");
        submitOrderForm.method = "get";
        submitOrderForm.submit();
    };

});