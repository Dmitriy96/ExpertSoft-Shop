$(document).ready(function(){

    function removePhone(childElement) {
        var phoneTableRow = childElement;
        while (phoneTableRow.nodeName != "TR") {
            phoneTableRow = phoneTableRow.parentNode;
        }
        phoneTableRow.parentNode.removeChild(phoneTableRow);
    }

    function checkIfTableEmpty() {
        var tableRows = document.getElementById("phonesTable").rows;
        if (tableRows.length <= 1) {
            var emptyTableMessage = document.getElementById("emptyTableMessage");
            emptyTableMessage.classList.remove("hidden");
            var submitButton = document.getElementById("submitButton");
            submitButton.classList.add("disabled");
            var updateButton = document.getElementById("submitButton");
            updateButton.classList.add("updateButton");
        }
    }

    function setRemovePhoneButtonsClickListeners() {
        var removePhoneButtons = document.getElementsByName("removePhoneButton");
        for (var i = 0; i < removePhoneButtons.length; i++) {
            removePhoneButtons[i].onclick = function () {
                var that = this;
                var url = that.getAttribute("data-url");
                var removeButtonId = that.id;
                var phoneId = removeButtonId.substring(removeButtonId.indexOf("-") + 1);
                url += "?id=" + phoneId;
                $.ajax({
                    url: url,
                    type: 'PUT',
                    success: function (data) {
                        removePhone(that);
                        checkIfTableEmpty();
                        document.getElementById("totalPrice").innerHTML = "Total price: $" + data;
                    }
                });
            }
        }
    }

    function setSubmitPhonesButtonClickListener() {
        var submitButton = document.getElementById("submitButton");
        submitButton.onclick = function () {
            if (!this.classList.contains("disabled")) {
                document.getElementById("submitCartForm").submit();
            }
        }
    }

    function setUpdatePhonesButtonClickListener() {
        var submitButton = document.getElementById("updateButton");
        submitButton.onclick = function () {
            if (!this.classList.contains("disabled")) {
                document.getElementById("updateCartForm").submit();
            }
        }
    }

    checkIfTableEmpty();

    setRemovePhoneButtonsClickListeners();

    setSubmitPhonesButtonClickListener();

    setUpdatePhonesButtonClickListener();
    
});