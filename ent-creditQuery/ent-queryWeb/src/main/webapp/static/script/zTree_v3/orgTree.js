var zTreeObj;
function orgCodeSetName(code, data) {
	if (code == data.id) {
		$(".orgTree").val(data.name);
		// zTreeObj.selectNode(data);
		return false;
	} else {
		if (data.children) {
			data.children.forEach(function(item, index, array) {
				orgCodeSetName(code, item);
			});
		}
	}
}
function orgTreeQuery(val, first) {
	$('.curSelectedNode').removeClass('curSelectedNode');
	if (!val) {
		return;
	}
	if (first) {
		var id = $(".orgTree").next().val();
		var nodes = zTreeObj.getNodesByParam("name", val, null);
		for (var i = 0; i < nodes.length; i++) {
			var curNode = nodes[i];
			if (curNode.id == id) {
				zTreeObj.selectNode(curNode);
				break;
			}
		}
		$(".orgQuery input")[0].focus();
		return;
	}
	var nodes = zTreeObj.getNodesByParamFuzzy("name", val, null);
	for (var i = 0; i < nodes.length; i++) {
		var curNode = nodes[i];
		zTreeObj.selectNode(curNode);
		if (i > 0) {
			var preNode = nodes[i - 1];
			$("#" + preNode.tId + "_a").addClass("curSelectedNode");
		}
	}
	$(".orgQuery input")[0].focus();
}

function orgTreeFun(treeData) {
	if ($(".orgTree").length < 1) {
		return false;
	};
	var orgTreeWrap = '<div class="orgTreeWrap" ><div class="orgQuery" ><input type="text" placeholder="请输入机构名称"/></div><ul id="orgTree" style="" class="ztree"></ul></div>';
	$(".modal-body").append(orgTreeWrap);
	// $("#query_form_id .modal-body").append(orgTreeWrap);
	$(".orgTreeWrap").css({
		"display" : "none",
		"height" : "100%",
		"overflow" : "hidden"
	});
	$(".orgQuery").css({
		"width" : "100%",
		"height" : "30px",
		"line-height" : "30px",
		"padding" : "10px 0",
		"margin-bottom" : "5px",
		"border-bottom" : "1px solid #eef"
	});
	$(".orgQuery input").css({
		"color" : "#ccc",
		"width" : "90%",
		"height" : "28px",
		"display" : "block",
		"line-height" : "28px",
		"border" : "1px solid #dedede",
		"border-radius" : "3px",
		"padding" : "0 5px",
		"margin" : "0 auto"
	});
	$(".orgTreeWrap #orgTree").css({
		"width" : "95%",
		"height" : "75%",
		"background" : "#fff",
		"overflow" : "auto"
	});
	// zTree 的参数配置，深入使用请参考 API 文档（setting 配置详解）
	var setting = {
		isSimpleData : true,
		view : {
			showLine : true
		},
		treeObj : $("#orgTree"),
		callback : {
			beforeAsync : null,
			beforeCheck : null,
			beforeClick : null,
			beforeCollapse : null,
			beforeDblClick : null,
			beforeDrag : null,
			beforeDragOpen : null,
			beforeDrop : null,
			beforeEditName : null,
			beforeExpand : null,
			beforeMouseDown : null,
			beforeMouseUp : null,
			beforeRemove : null,
			beforeRename : null,
			beforeRightClick : null,
			onAsyncError : null,
			onAsyncSuccess : null,
			onCheck : null,
			onClick : onClick,
			onCollapse : null,
			onDblClick : null,
			onDrag : null,
			onDragMove : null,
			onDrop : null,
			onExpand : null,
			onMouseDown : null,
			onMouseUp : null,
			onNodeCreated : null,
			onRemove : null,
			onRename : null,
			onRightClick : null
		},
	};
	// zTree 的点击跳转
	function onClick(event, treeId, treeNode) {
		$(".orgTree").val(treeNode.name);
		$(".orgQuery input").val(treeNode.name).css({
			"color" : "#333"
		});
/*		if ($("input[name='uporg']").length < 1) {
			$("input[name='search_IN_orgId']").val(treeNode.id);
			$("input[name='orgId']").val(treeNode.id);
		}*/
		$(".orgTree").next().val(treeNode.id);
	}
	;

	if ($("form").attr("id") == "create_form_id"
			|| $("form").attr("id") == "query_form_id") {
		/*
		 * $(".orgTree").val(treeData.name);
		 * $(".orgTree").next().val(treeData.id);
		 */
	}

	$(".orgTree").click(
			function() {
				first = true;
				$(this).blur();
				var val = $(this).val();
				$("#orgTree").show();
				var index = layerTree(600, 320, "机构树", $(".orgTreeWrap"), 1,
						function() {
							$(".layui-layer-btn").css({
								"border-top" : "1px solid #ccc"
							});
						}, function() {
							var searchVal = $(".orgQuery input").val();
							if(!searchVal){
								$(".orgTree").val("");
								$(".orgTree").next().val("");
							}
							$(".orgTreeWrap").hide();
						});
				layer.full(index);
				if (val != "") {
					$(".orgQuery input").val(val).css({
						"color" : "#333"
					});
					orgTreeQuery(val, true);
				}
			});
	zTreeObj = $.fn.zTree.init(setting.treeObj, setting, treeData);
	var nodes = zTreeObj.getNodes();
	for (var i = 0; i < nodes.length; i++) {
		// 设置节点展开
		zTreeObj.expandNode(nodes[i], true, false, false);
	}
	// zTreeObj.expandAll(true);
	$(".orgQuery input").bind("input", function() {
		var val = $(this).val();
		orgTreeQuery(val, false);
		$(this).css({
			"color" : "#333"
		})
	});
}
