var OpenDataPoitiersIndex = (function () {
    var module = {};
    var labelOK = "<span class='label label-success'>OK</span>";
    var labelKO = "<span class='label label-danger'>KO</span>";

    module.buttonSubscription = function () {
        Settings.getRemoteSettings(function (settings) {
            var routes = settings.routes;
            routes.forEach(function (route, index) {
                $("#routes-table tbody").append(
                    '<tr>' +
                        '<td>' + route.verb + '</td>' +
                        '<td><a href="' + route.testRoute + '">' + route.route + '</a></td>' +
                        '<td>' + route.description + '</td>' +
                        '<td id="route-table' + index + '"><span class="glyphicon glyphicon-refresh glyphicon-refresh-animate"></span></td>' +
                    '</tr>'
                );
                $.ajax({
                    url: route.testRoute,
                    dataType: 'json',
                    success: function (data) {
                        $("#route-table" + index + " span").replaceWith(labelOK);
                    },
                    error: function (data) {
                        $("#route-table" + index + " span").replaceWith(labelKO);
                    }
                });
                
            });
        })

    }
    
    module.addListData = function () {
        Settings.getRemoteSettings(
            function (settings) {
            var sources = settings.sources;
            for (index in sources) {
                var source = sources[index];
                $("#list_data").append(
                        "<li>"+index+", <a href=\""+source.openDataFileURL+"\">le fichier <b>data.gouv.fr</b></a></li>"
                );
            }
        })
    }

    return module;
})();

OpenDataPoitiersIndex.buttonSubscription();
OpenDataPoitiersIndex.addListData();