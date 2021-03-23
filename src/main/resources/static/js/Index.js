$(document).ready(function () {
    funcListDisplay();

    $("#java_redis_client_input_form_btn_execute").click(function () {
        operationParam.args = [];
        if (operationParamArgsWidgetIdCache.length > 0) {
            for (var i = 0; i < operationParamArgsWidgetIdCache.length; i++) {
                var id = operationParamArgsWidgetIdCache[i];
                var type = operationParam.argTypes[i];
                var valueStr = $("#" + id).val();
                if (type === "java.lang.String") {
                    operationParam.args[i] = valueStr;
                } else {
                    operationParam.args[i] = eval(valueStr);
                }
            }
        }
        if (operationParam.argTypes.length === operationParam.args.length) {
            executePost(
                "/redis/operate",
                JSON.stringify(operationParam),
                function (data) {
                    showExecuteResultModal(data);
                },
                function (data, code) {
                    alert(data.message);
                }
            );
        } else {
            alert("Argument types length and argument values length not match!");
        }
    });
});

function funcListDisplay() {
    executeGet(
        "/redis/funcs",
        function (data) {
            if (data) {
                var funcList = data.funcs;
                if (funcList.length > 0) {
                    for (var i = 0; i < funcList.length; i++) {
                        appendFuncItem(funcList[i]);
                    }
                }
            }
        },
        function (data, code) {
            alert(data.message);
        }
    );
}

function appendFuncItem(item) {
    $("#java-redis-client-func-list-chooser").append("<option>" + item + "</option>");
}

var operationParam = {
    "argTypes": [],
    "args": [],
    "methodName": null
};

var operationParamArgsWidgetIdCache = [];

function paramFormDisplay() {
    $("#java_redis_client_input_form_param_list").empty();
    operationParam.argTypes = [];
    operationParam.args = [];
    operationParam.methodName = null;
    operationParamArgsWidgetIdCache = [];
    var item = $("#java-redis-client-func-list-chooser").val();
    var methodName;
    if (item.indexOf("(") !== -1 && item.indexOf(")") !== -1) {
        methodName = item.substr(0, item.indexOf("(")).trim();
        var paramStr = item.substring(item.indexOf("(") + 1, item.indexOf(")"));
        if (paramStr.indexOf(",") !== -1) {
            var paramList = paramStr.split(",");
            for (var i = 0; i < paramList.length; i++) {
                appendParamItem(paramList[i].trim(), i);
            }
        } else {
            appendParamItem(paramStr.trim(), 0);
        }
    } else {
        methodName = item.trim();
    }
    operationParam.methodName = methodName;
}

function appendParamItem(param, index) {
    operationParam.argTypes[index] = param;
    var id = "java_redis_client_input_form_param" + index;
    operationParamArgsWidgetIdCache[index] = id;
    var appendText = "<div class=\"input-group\"><span class=\"input-group-addon\">Param" + index + " : </span><input id=\"" + id + "\" type=\"text\" class=\"form-control\" placeholder=\"" + param + "\"></div><br>";
    $("#java_redis_client_input_form_param_list").append(appendText);
}

function showExecuteResultModal(data) {
    var result = data.result;
    $("#java_redis_client_execute_result_modal_txt_area").val(JSON.stringify(result, null, "\t"));
    executeResultModalDisplay();
}

function executeResultModalDisplay() {
    $("#java_redis_client_execute_result_modal").attr("class", "modal fade in");
    $("#java_redis_client_execute_result_modal").attr("style", "display: block");
}

function executeResultModalDisplayNone() {
    $("#java_redis_client_execute_result_modal").attr("class", "modal fade");
    $("#java_redis_client_execute_result_modal").attr("style", "display: none");
}