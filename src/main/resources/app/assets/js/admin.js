var AdminAlerts = (function () {
    var module = {};

    var alertSuccess = $("#alert-success");
    var alertError = $("#alert-error");

    var setAlertValueAndShow = function (alert, message) {
        alert.find("span:nth-child(2)").text(message);
        alert.show();
    }

    module.showErrorMessage = function (message) {
        setAlertValueAndShow(alertError, message);
    }

    module.showSuccessMessage = function (message) {
        setAlertValueAndShow(alertSuccess, message);
    }

    module.hideAlerts = function () {
        alertSuccess.hide();
        alertError.hide();
    }

    return module;
})();


var OpenDataPoitiersAdmin = (function () {
    var module = {};

    module.buttonSubscription = function () {
        Settings.getRemoteSettings(
            function (settings) {
                var sources = settings.sources;
                sources.all = {
                    openDataFileURL: "",
                    reloadDataURL: "/admin/reload/all"
                };
                sources.reloadSettings = {
                    openDataFileURL: "",
                    reloadDataURL: "/admin/reload-default-settings"
                }

                for (index in sources) {
                    var source = sources[index];
                    $("#sources-table tbody").append(
                        '<tr>' +
                        '<td class="text-nowrap">' + index + '</td>' +
                        '<td><button id="' + index + '-button" class="btn btn-default" type="submit">Reload Data</button></td>' +
                        '<td>' + source.openDataFileURL + '</td>' +
                        '</tr>'
                    );


                    (function (url) {

                        $("#" + index + '-button').click(function () {
                            AdminAlerts.hideAlerts();

                            $.ajax({
                                url: url,
                                dataType: 'text',
                                type: "PUT",
                                data:"",
                                complete: function (data) {
                                    if (data.status === 200) {
                                        AdminAlerts.showSuccessMessage("Loaded data successfully");
                                    }else{
                                        AdminAlerts.showErrorMessage("Error while loading Data");
                                    }
                                }
                            });
                        });
                    })(source.reloadDataURL);
                }
            }
        )
    }

    module.loadDashboardForm = function () {
        Settings.getRemoteSettings(
            function (settings) {
                $("#dashboardURL").val(settings.dashboardURL);

                $("#dashboardUpdateButton").click(
                    function () {
                        $.ajax({
                            url: "/admin/settings/dashboard-url",
                            dataType: "application/json",
                            type: "PUT",
                            data: $("#dashboardURL").val(),
                            complete: function (data) {
                                if (data.status === 200) {
                                    AdminAlerts.showSuccessMessage("Dashboard's URL updated successfully");
                                } else {
                                    AdminAlerts.showErrorMessage("Error while updating dashboard's URL");
                                }
                            }
                        })
                    }
                )
            }
        )
    }

    return module;
})();

OpenDataPoitiersAdmin.buttonSubscription();
OpenDataPoitiersAdmin.loadDashboardForm();