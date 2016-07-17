$(document).ready(function(){

    $("#phoneAddedToCartMessage").hide();

    function addPhoneToCart() {
        var addPhoneButtons = document.getElementsByName("addPhoneToCart");
        for (var i = 0; i < addPhoneButtons.length; i++) {
            addPhoneButtons[i].onclick = function () {
                var quantity = this.previousElementSibling.value;
                this.previousElementSibling.value = 1;
                if (!isNaN(parseInt(quantity)) && isFinite(quantity) && quantity > 0) {
                    var url = this.getAttribute("data-url");
                    url += "?count=" + quantity;
                    $.ajax({
                        url: url,
                        type: 'PUT',
                        success: function() {
                            $("#phoneAddedToCartMessage").show();
                            setTimeout(function(){
                                $("#phoneAddedToCartMessage").hide();
                            }, 2500);
                        }
                    });
                }
            }
        }
    }

    addPhoneToCart();

});