var OpenDataPoitiersIndex = (function () {
    var routes = [
       {verb: "GET",route: "/bike-shelters/all",testRoute: "/bike-shelters/all",description: "get all shelters"},
       {verb: "GET",route: "/bike-shelters/find?lat=:lat&lon=:lon&size=:size",testRoute: "/bike-shelters/find?lat=46.578636&lon=0.337959",description: "search closest shelters from lat/lon point, size is optional"},
       {verb: "GET",route: "/disabled-parkings/all",testRoute: "/disabled-parkings/all",description: "search closest disabled parkings from lat/lon point, size is optional"},
       {verb: "GET",route: "/disabled-parkings/find?lat=:lat&lon=:lon&size=:size",testRoute: "/disabled-parkings/find?lat=46.578636&lon=0.337959",description: "search closest disabled parkings from lat/lon point, size is optional"}
    ];

    var module = {};
    var labelOK = "<span class='label label-success'>OK</span>";
    var labelKO = "<span class='label label-danger'>KO</span>";

    module.buttonSubscription = function(){
        routes.forEach(function(route, index){
            console.log(route);
            $("#route-table tbody").append(
                "<tr>" +
                    "<td>"+route.verb+"</td>" +
                    "<td><a href='"+route.testRoute+"'>"+route.route+"</a></td>" +
                    "<td>"+route.description+"</td>" +
                    "<td id='route-table"+index+"'><span class='glyphicon glyphicon-refresh glyphicon-refresh-animate'></span></td>"+
                "</tr>"
            );

            $.ajax({
                url: route.testRoute,
                dataType: 'json',
                success : function(data){
                    $("#route-table"+index+ " span").replaceWith(labelOK);
                },
                error : function(data){
                    $("#route-table"+index+ " span").replaceWith(labelKO);
                }
            });

        });

    }

    return module;
})();

OpenDataPoitiersIndex.buttonSubscription();