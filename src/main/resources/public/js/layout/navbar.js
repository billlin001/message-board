"use strict";

(function() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    $("#btnLogin").click(function () {
        console.log("click login...");
        location.href = "/login";
    });

    $("#btnSignUp").click(function () {
        console.log("click sign up...");
        location.href = "/signup";
    });

    $("#btnLogout").click(function () {
        console.log("click sign up...");
        location.href = "/logout"

        $.post({
            url: "/logout",
            success: function () {
                console.log("logout success");
                //location.reload("/");
                location.href = "/";
            },
            error: function () {
                console.log("logout fail...");
            }
        });
    });
})($);
