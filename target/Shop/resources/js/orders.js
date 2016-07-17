$(document).ready(function(){

    var deliveredButtons = document.getElementsByName("deliveredButton");
    for (var i = 0; i < deliveredButtons.length; i++) {
        deliveredButtons[i].onclick = function () {
            var deliveredButtonId = this.id;
            var phoneId = deliveredButtonId.substring(deliveredButtonId.indexOf("-") + 1);
            console.log("phoneId: " + phoneId);

            var url = this.getAttribute("data-url");
            $.ajax({
                url: url,
                type: 'PUT',
                success: function() {
                    var phonesTable = document.getElementById("ordersTable-" + phoneId);
                    var orderStatuses = phonesTable.getElementsByTagName('tr');
                    console.log(orderStatuses);
                    for (var j = 1; j < orderStatuses.length; j++) {
                        orderStatuses[j].lastElementChild.firstElementChild.innerHTML = "DELIVERED";
                    }
                }
            });
        }
    }

});