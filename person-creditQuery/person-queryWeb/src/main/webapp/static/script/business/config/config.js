/**
 * [detailBtn list页面详情按钮方法]
 * @param  {[Object]} data [当前行数据]
 * @param  {[Object]} obj [list页面中的页签组件配置项对象]
 */
function detailBtn(data, obj) {
	layerOpen(900, 500, obj.icon,obj.name + "-详情页", obj.detailUrl + "?configid=" + data.configId, 2,
		function() {},
		true)
}
function updateBtn(data, obj) {
    var i = data.table;
    var n = [],
    a = i.checkStatus("grid");
    var r = a.data;
    if (r.length == 1) {
        layerOpen(900, 500, obj.icon, obj.name + "-修改页", obj.updUrl + "?configid=" + r[0].configId, 2,
        function() {},
        true)
    } else {
        layerMsg("请选择1条进行操作！")
    }
}

