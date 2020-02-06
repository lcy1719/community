function api() {
    var data = {
        key: "HAWBZ-6ILCF-S4KJK-NIYZE-J2ZB6-XRB3Q",
    }
    var url = "https://apis.map.qq.com/ws/location/v1/ip";
    data.output = "jsonp";
    $.ajax({
        type: "get",
        dataType: 'jsonp',
        data: data,
        jsonp: "callback",
        jsonpCallback: "QQmap",
        url: url,
        success: function (data) {
            //业务处理
            alert(JSON.stringify(data));
        },
        error: function () {
            alert("出错");
            //业务处理
        }

    });
}