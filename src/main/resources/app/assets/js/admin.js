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

    module.addData = function () {
        var wrapper = $(".input_fields_wrap"); //Fields wrapper
        var add_button = $(".add_field_button"); //Add button ID
        var size = 1;

        $(add_button).click(function (e) { //on add input button click
            e.preventDefault();
            size++;
            $(wrapper).append('<tr class="new_fields" id="tr'+size+'">' +
                    '<td><input class="fields" type="text" name="champJson[]" id="champJson' + size + '"></td>' +
                    '<td><input class="fields" type="text" name="champES[]" id="champES' + size + '"></td>' +
                    '<td><input class="fields" type="checkbox" name="not_analyzed[]" id="not_analyzed' + size + '"></td>' +
                    '</tr>'); //add input box
        });

        /*$(wrapper).on("click", ".remove_field", function (e) { //user click on remove text
            e.preventDefault();
            $(this).parent().parent().remove();
            size--;
        });*/


        $(".remove-last-val").click(
            function () {
                if (size > 1) { //Avoid deleting of first field
                    $("#tr" + size).remove();
                    size--;
                }
            }
        );

        $(".form-control").keyup(
            function () {
                var mess="";
                var jqxhr = $.getJSON($("#fileURL").val(), function() {})
                    .done(function(data) {
                    for(var i=0; i<3; i++){
                    var properties = data.features[i]["properties"];
                    mess = mess+"{";
                        for(var j in properties)
                        {
                             mess = mess+"\""+j+"\": \""+properties[j]+"\", ";
                        }
                        mess = mess + "}<br/>";
                    }
                    $('.msg_line').html("<label class=\"txt_msg_line\">"+mess+"</label>"); 
                    })
                    .fail(function() {
                        $('.msg_line').html("<label class=\"txt_msg_line\">lien incorrect</label>"); 
                    });
            }
        );
        

        $(".add_data_button").click(
            function () {
                var emptyField = false;
                var type = $("#elastic-type-field").val();
                // All fields must be filled
                for (var i = 1; i<(size+1); i++){
                    if ($("#champJson"+i).val() === "" || $("#champES"+i).val() === "")
                        emptyField = true;
                }
                if ($("#fileURL").val()=== "" || type === "")
                    emptyField = true;

                if(emptyField === false){
                    //Getting data properties in a single variable
                    var properties_data = {};
                    for (var i = 1; i < (size + 1); i++) {
                        var data = {};
                        data["champJson"] = $("#champJson" + i).val();
                        data["champES"] = $("#champES" + i).val();
                        if (document.getElementById("not_analyzed" + i).checked === true) {
                            data["mapping"] = true;
                        } else
                            data["mapping"] = false;
                        properties_data[i - 1] = data;
                    }
                    var location ={};
                    location["champES"] = "location";

                    if(document.getElementById("geocoding").checked === true){ // in case of reverse geocoding
                        var geolocation = {};
                        geolocation["champJson"] = "address";
                        geolocation["champES"] = "address";
                        geolocation["mapping"] = true;
                        properties_data[size] = geolocation;
                        properties_data[size+1] = location; // location is added at the end of properties_data
                    }
                    else
                        properties_data[size] = location;
                    console.log("Passe");
                    var fileJson = JSON.stringify({properties: properties_data, url: $("#fileURL").val(), type: type});
                    $.ajax({
                        url: "/admin/create-files",
                        dataType: 'json',
                        type: "PUT",
                        data: fileJson,
                        complete: function () {
                            window.location.reload();
                            AdminAlerts.showSuccessMessage("Data added");
                        }
                    })
                }
                else
                {
                    $.ajax({
                       complete: function () {
                           AdminAlerts.showSuccessMessage("All fields must be filled");
                       }
                   })
                }       
            });
        };
    
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
OpenDataPoitiersAdmin.addData();
OpenDataPoitiersAdmin.loadDashboardForm();
