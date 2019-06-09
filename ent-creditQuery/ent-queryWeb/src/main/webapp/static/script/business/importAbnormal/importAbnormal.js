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
}

function illustrationBtn(urlObj, data) {
	var id = data.id, illustration = data.illustration, url = urlObj.illustrationPage;
	layerOpen(900, 400, urlObj.icon, urlObj.name + "-异常查询说明", url + "?id=" + id
			+ "&illustration=" + illustration, 2, function() {
	});
}