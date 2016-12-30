"use strict";

(function() {
    var postId = 0;
    var teamId = 0;

    $("button[name='btnDelete']").click(function(){
        postId = $(this).val();
        teamId = $("#teamId").val();
    });

    $("#btnCancel").click(function() {
        $("#confirmModal").modal("hide");
    });

    $("#btnSure").click(function () {
        $.ajax({
            url: "/teams/" + teamId + "/posts/" + postId,
            type: "delete",
            success: function () {
                console.log("delete success, id = " + postId);
                location.href = "/teams/" + teamId;
            },
            error: function () {
                console.log("delete fail...");
            }
        });
    });
})($);

