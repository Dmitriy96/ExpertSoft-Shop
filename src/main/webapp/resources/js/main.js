$(document).ready(function(){

    $("#phoneAddedToCartMessage").hide();

    function addPhoneToCart() {
        var addPhoneButtons = document.getElementsByName("addPhoneToCart");
        for (var i = 0; i < addPhoneButtons.length; i++) {
            addPhoneButtons[i].onclick = function () {
                var that = this;
                var quantity = this.previousElementSibling.value;
                var url = this.getAttribute("data-url");
                url += "?id=" + that.id + "&quantity=" + quantity;
                $.ajax({
                    url: url,
                    type: 'PUT',
                    success: function() {
                        var phoneId = that.id;
                        var errorMessage = document.getElementById("phoneIdErrorText-" + phoneId);
                        errorMessage.innerHTML = "";
                        errorMessage.classList.add("hidden");
                        that.previousElementSibling.value = 1;
                        document.getElementById("phoneIdError-" + phoneId).classList.add("hidden");
                        $("#phoneAddedToCartMessage").show();
                        setTimeout(function(){
                            $("#phoneAddedToCartMessage").hide();
                        }, 2500);
                    },
                    error: function (message) {
                        console.log(message);
                        console.log(JSON.stringify(message));
                        var phoneId = that.id;
                        var errorMessage = document.getElementById("phoneIdErrorText-" + phoneId);
                        errorMessage.innerHTML = message.responseText;
                        errorMessage.classList.remove("hidden");
                        document.getElementById("phoneIdError-" + phoneId).classList.remove("hidden");
                    }
                });
            }
        }
    }

    addPhoneToCart();

});