function bindCustomBtnEvent(returnObj, urlObj) {
	$(".btnWrap .illustrationBtn")
			.bind(
					"click",
					function() {
						var index = $("#grid-data .active").parents("tr")
								.index();
						// var list = gridData.list[index];
						var checkStatus = returnObj.table.checkStatus('grid'), data = checkStatus.data[0];
						//console.log(data);
						if (checkStatus.data.length != 1) {
							layerMsg("只能选择一条数据");
						} else {
							// debugger
							illustrationBtn(urlObj, data);
						}
					});
	$(".btnWrap .exportBtn")
	.bind(
			"click",
			function() {
				var index = $("#grid-data .active").parents("tr")
						.index();
				// var list = gridData.list[index];
				var checkStatus = returnObj.table.checkStatus('grid'), data = checkStatus.data;
				exportBtn(returnObj, urlObj);
			});
}

function illustrationBtn(urlObj, data) {
	var id = data.id, illustration = data.illustration, url = urlObj.illustrationPage;
	layerOpen(900, 400, urlObj.icon, urlObj.name + "-异常查询说明", url + "?id=" + id
			+ "&illustration=" + illustration, 2, function() {
	});
}

function exportBtn(returnObj, obj) {
    if(returnObj.table.cache.grid.length > 0){
        var table = returnObj.table;
        var idArr = [],
        checkStatus = table.checkStatus('grid');
        var data = checkStatus.data;
        for (var i = 0; i < data.length; i++) {
            idArr.push(data[i].id);
        }
        var searchData = $("form").serializeJSON();
        var sData = returnObj.searchData;
        if(sData){
            for(var i in sData){
                if(searchData[i] != "" && !searchData[i]){
                    searchData[i] = sData[i]; 
                }
            }
        }
        searchData.ids = idArr + "";
        var str = paramObjToStr(searchData);
        window.location.href = obj.exportUrl + "&" + str;
    }else{
        layerMsg("无数据可导出");
    }
}