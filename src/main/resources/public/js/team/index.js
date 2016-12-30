"use strict";

(function() {
    var deleteId = 0;

    $("button[name='btnDelete']").click(function(){
        deleteId = $(this).val();
    });

    $("#btnCancel").click(function() {
        $("#confirmModal").modal("hide");
    });

    $("#btnSure").click(function () {
        console.log("click Delete button...");
        var id = deleteId;
        console.log("id = " + id);

        $.ajax({
            url: "/teams/" + id,
            type: "delete",
            success: function () {
                console.log("delete success, id = " + id);
                location.href="/";
            },
            error: function () {
                console.log("delete fail...");
            }
        });
    });

    $('#teamTable').DataTable();
})($);



