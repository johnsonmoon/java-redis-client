$(document).ready(function () {
    executeGet(
        "/configs",
        function (data) {
            var dataStr = JSON.stringify(data, null, "\t");
            $("#java_redis_client_settings_input_edit_content").val(dataStr);
        },
        function (data, code) {
            alert(data.message);
        }
    );

    $("#java_redis_client_settings_input_form_submit").click(function () {
        var settings = $("#java_redis_client_settings_input_edit_content").val();
        executePost(
            "/configs",
            settings,
            function (data) {
                alert("Modified: [" + data.modified + "]");
            },
            function (data, code) {
                alert(data.message);
            }
        );
    });
});