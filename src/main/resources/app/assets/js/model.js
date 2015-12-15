var Settings = (function (){
    var module = {};
    var settings = undefined;

    module.getRemoteSettings = function (callback){
        if(settings === undefined){
            $.ajax({
                url: "/settings/",
                dataType : "json",
                success: function (data) {
                    settings = data;
                    callback(settings);
                },
                error : function (data){
                    console.log('Error while loading settings : '+ JSON.stringify(data));
                }
            })
        }else {
            callback(settings);
        }
    }

    return module;
})();