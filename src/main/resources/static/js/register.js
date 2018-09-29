$(document).ready(function () {

    $("#username-form").submit(function (event) {
        event.preventDefault();

        var username = {};
        username["username"] = $("#username").val();
        $("#btn-search").prop("disabled", true);

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/register",
            data: JSON.stringify(username),
            dataType: 'json',
            cache: false,
            timeout: 600000,
            success: [
                function (data) {
                    console.log("SUCCESS : ", data);
                }
            ]
        });
    });
});