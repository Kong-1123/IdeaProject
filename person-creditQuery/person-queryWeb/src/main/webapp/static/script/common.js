function requestAddRandom(e) {
	if (e.indexOf("?") >= 0) {
		var t = e.split("?");
		e = t[0] + "?" + Math.random() + "&" + t[1]
	} else {
		e = e + "?" + Math.random()
	}
	return e
}
function getLocationArr(e) {
	var t = /(\w+):\/\/([^\:|\/]+)(\:\d*)?(.*\/)?(.*\/)([^#|\?|\n]+)?(#.*)?(\?.*)?/i;
	var i = e.match(t);
	return i
}
function getProjectName(e) {
	var t = /(\w+):\/\/([^\:|\/]+)(\:\d*)?(.*\/)?(.*\/)([^#|\?|\n]+)?(#.*)?(\?.*)?/i;
	var i = e.match(t);
	var a = i[4];
	return a
}
function returnOneUrlObj(e, t) {
	var i = t.tabs;
	for (var a = 0; a < i.length; a++) {
		if (e == i[a].eName) {
			return i[a]
		}
	}
}
function loadDic(e, t, i, a) {
	var n = "", r = getDic(e, i);
	if (a) {
		n = ""
	} else {
		n = "<option value='' selected='selected'>请选择</option>"
	}
	for ( var o in r) {
		n += "<option value=" + o + " title='" + r[o] + "'>" + r[o]
				+ "</option>"
	}
	$("[name='" + t + "']").empty().append(n)
}
function cityDic(n, e, r, o) {
	var t = $("." + e).eq(0);
	var l = $("." + e).eq(2);
	var s = $("." + e).eq(4);
	cityDicLoad(n, t, r + "1");
	o.render("select");
	o.on("select(applyBusiDist1)", function(e) {
		s.empty();
		var t = e.value;
		$("#cityHidden").val(t);
		var i = e.text;
		t = t.substring(0, 2);
		if (t == 71 || t == 81 || t == 82) {
			var a = "<option value='" + t + "0000' title='" + i + "'>" + i
					+ "</option>";
			$(this).nextAll().not(".star").empty().append(a)
		} else {
			cityDicLoad(n, l, r + "2", t)
		}
		o.render("select")
	});
	o.on("select(applyBusiDist2)", function(e) {
		var t = e.value;
		if (t != "") {
			$("#cityHidden").val(t)
		}
		var i = e.text;
		t = t.substring(0, 4);
		if (t == 7100 || t == 8100 || t == 8200) {
			var a = "<option value='" + t + "00' title='" + i + "'>" + i
					+ "</option>";
			$(this).nextAll().not(".star").empty().append(a)
		} else {
			cityDicLoad(n, s, r + "3", t)
		}
		o.render("select")
	});
	o.on("select(applyBusiDist3)", function(e) {
		var t = e.value;
		if (t != "") {
			$("#cityHidden").val(t)
		}
	})
}
function investDic(a, e, n) {
	var t = $("." + e).eq(0);
	var r = $("." + e).eq(1);
	var o = $("." + e).eq(2);
	var l = $("." + e).eq(3);
	investDicLoad(a, t, n + "1");
	t.change(function() {
		var e = $(this).val();
		var t = $(this).find(":selected").text();
		if (e == "9") {
			var i = "<option value='" + e + "' title='" + t + "'>" + t
					+ "</option>";
			$(this).nextAll().not(".star").empty().append(i)
		} else {
			investDicLoad(a, r, n + "2", e)
		}
	});
	r.change(function() {
		var e = $(this).val();
		var t = $(this).find(":selected").text();
		if (e == "9") {
			var i = "<option value='" + e + "' title='" + t + "'>" + t
					+ "</option>";
			$(this).nextAll().not(".star").empty().append(i)
		} else {
			investDicLoad(a, o, n + "3", e)
		}
	});
	o.change(function() {
		var e = $(this).val();
		var t = $(this).find(":selected").text();
		if (e == "9") {
			var i = "<option value='" + e + "' title='" + t + "'>" + t
					+ "</option>";
			$(this).nextAll().not(".star").empty().append(i)
		} else {
			investDicLoad(a, l, n + "4", e)
		}
	})
}
function cityDicLoad(e, t, i, a) {
	var n = "<option value='' selected='selected'>请选择</option>";
	var r = getDic(e, i);
	for ( var o in r) {
		if (a != undefined) {
			if (a == o.substring(0, 2)) {
				n += "<option value=" + o + " title='" + r[o] + "'>" + r[o]
						+ "</option>"
			} else if (a == o.substring(0, 4)) {
				n += "<option value=" + o + " title='" + r[o] + "'>" + r[o]
						+ "</option>"
			}
		} else {
			n += "<option value=" + o + " title='" + r[o] + "'>" + r[o]
					+ "</option>"
		}
	}
	t.empty().append(n)
}
function investDicLoad(e, t, i, a) {
	var n = "<option value='' selected='selected'>请选择</option>";
	var r = getDic(e, i);
	for ( var o in r) {
		if (a != undefined) {
			if (a == o.substring(0, 1)) {
				n += "<option value=" + o + " title='" + r[o] + "'>" + r[o]
						+ "</option>"
			} else if (a == o.substring(0, 3)) {
				n += "<option value=" + o + " title='" + r[o] + "'>" + r[o]
						+ "</option>"
			} else if (a.substring(1) == o.substring(0, 3)) {
				n += "<option value=" + o + " title='" + r[o] + "'>" + r[o]
						+ "</option>"
			}
		} else {
			n += "<option value=" + o + " title='" + r[o] + "'>" + r[o]
					+ "</option>"
		}
	}
	t.empty().append(n)
}
function cityDicShow(n, r, e, o, t) {
	if (t == (null || undefined)) {
		return false
	}
	var i = $("select." + e);
	var a = i.eq(0);
	var l = i.eq(1);
	var s = i.eq(2);
	cityDicLoadShow(r, a, o + "1", t);
	cityDicLoadShow(r, l, o + "2", t);
	cityDicLoadShow(r, s, o + "3", t);
	n.on("select(applyBusiDist1)", function(e) {
		s.empty();
		var t = e.value;
		$("#cityHidden").val(t);
		var i = e.text;
		t = t.substring(0, 2);
		if (t == 71 || t == 81 || t == 82) {
			var a = "<option value='" + t + "0000' title='" + i + "'>" + i
					+ "</option>";
			$(this).nextAll().not(".star").empty().append(a)
		} else {
			cityDicLoad(r, l, o + "2", t)
		}
		n.render("select")
	});
	n.on("select(applyBusiDist2)", function(e) {
		var t = e.value;
		if (t != "") {
			$("#cityHidden").val(t)
		}
		var i = e.text;
		t = t.substring(0, 4);
		if (t == 7100 || t == 8100 || t == 8200) {
			var a = "<option value='" + t + "00' title='" + i + "'>" + i
					+ "</option>";
			$(this).nextAll().not(".star").empty().append(a)
		} else {
			cityDicLoad(r, s, o + "3", t)
		}
		n.render("select")
	});
	n.on("select(applyBusiDist3)", function(e) {
		var t = e.value;
		if (t != "") {
			$("#cityHidden").val(t)
		}
	})
}
function investDicShow(a, e, n, t) {
	var i = $("." + e).eq(0);
	var r = $("." + e).eq(1);
	var o = $("." + e).eq(2);
	var l = $("." + e).eq(3);
	investDicLoadShow(a, i, n + "1", t);
	investDicLoadShow(a, r, n + "2", t);
	investDicLoadShow(a, o, n + "3", t);
	investDicLoadShow(a, l, n + "4", t);
	if (t == (null || undefined || "")) {
		investDic(a, e, n)
	}
	i.change(function() {
		var e = $(this).val();
		var t = $(this).find(":selected").text();
		if (e == "9") {
			var i = "<option value='" + e + "' title='" + t + "'>" + t
					+ "</option>";
			$(this).nextAll().not(".star").empty().append(i)
		} else {
			investDicLoad(a, r, n + "2", e)
		}
	});
	r.change(function() {
		var e = $(this).val();
		var t = $(this).find(":selected").text();
		if (e == "9") {
			var i = "<option value='" + e + "' title='" + t + "'>" + t
					+ "</option>";
			$(this).nextAll().not(".star").empty().append(i)
		} else {
			investDicLoad(a, o, n + "3", e)
		}
	});
	o.change(function() {
		var e = $(this).val();
		var t = $(this).find(":selected").text();
		if (e == "9") {
			var i = "<option value='" + e + "' title='" + t + "'>" + t
					+ "</option>";
			$(this).nextAll().not(".star").empty().append(i)
		} else {
			investDicLoad(a, l, n + "4", e)
		}
	})
}
function cityDicLoadShow(e, t, i, a) {
	if (a == (null || undefined)) {
		return false
	}
	var n = "<option value='' selected='selected'>请选择</option>";
	var r = getDic(e, i);
	var o = t.attr("id");
	for ( var l in r) {
		if (o.indexOf("1") < 0) {
			if (a.substring(0, 2) == l.substring(0, 2) && o.indexOf("2") > 0) {
				if (a.substring(0, 4) == l.substring(0, 4)) {
					n += "<option value=" + l + " title='" + r[l]
							+ "' selected>" + r[l] + "</option>"
				} else {
					n += "<option value=" + l + " title='" + r[l] + "'>" + r[l]
							+ "</option>"
				}
			} else if (a.substring(0, 4) == l.substring(0, 4)) {
				if (a == l) {
					n += "<option value=" + l + " title='" + r[l]
							+ "' selected>" + r[l] + "</option>"
				} else {
					n += "<option value=" + l + " title='" + r[l] + "'>" + r[l]
							+ "</option>"
				}
			}
		} else {
			if (a.substring(0, 2) == l.substring(0, 2)) {
				n += "<option value=" + l + " title='" + r[l] + "' selected>"
						+ r[l] + "</option>"
			} else {
				n += "<option value=" + l + " title='" + r[l] + "'>" + r[l]
						+ "</option>"
			}
		}
	}
	t.empty().append(n)
}
function investDicLoadShow(e, t, i, a) {
	var n = "<option value='' selected='selected'>请选择</option>";
	var r = getDic(e, i);
	var o = t.attr("id");
	if (a) {
		var l = getCode1(a);
		for ( var s in r) {
			if (o.indexOf("1") < 0) {
				if (o.indexOf("2") > 0) {
					if (l + a.substring(0, 2) == s && s.substring(0, 1) == l) {
						n += "<option value=" + s + " title='" + r[s]
								+ "' selected>" + r[s] + "</option>"
					} else {
						n += "<option value=" + s + " title='" + r[s] + "'>"
								+ r[s] + "</option>"
					}
				} else if (o.indexOf("3") > 0 && s.substring(0, 1) == l) {
					if (l + a.substring(0, 3) == s) {
						n += "<option value=" + s + " title='" + r[s]
								+ "' selected>" + r[s] + "</option>"
					} else {
						n += "<option value=" + s + " title='" + r[s] + "'>"
								+ r[s] + "</option>"
					}
				} else if (o.indexOf("4") > 0
						&& s.substring(0, 3) == a.substring(0, 3)) {
					if (a == s) {
						n += "<option value=" + s + " title='" + r[s]
								+ "' selected>" + r[s] + "</option>"
					} else {
						n += "<option value=" + s + " title='" + r[s] + "'>"
								+ r[s] + "</option>"
					}
				}
			} else {
				if (l == s) {
					n += "<option value=" + s + " title='" + r[s]
							+ "' selected>" + r[s] + "</option>"
				} else {
					n += "<option value=" + s + " title='" + r[s] + "'>" + r[s]
							+ "</option>"
				}
			}
		}
	}
	t.empty().append(n)
}
function getCode1(e) {
	var t, i = getProjectName(location.href);
	ajaxFunc(i + "common/findByCode4", function(e) {
		t = e.code1
	}, function(e) {
		layerMsg("请求出错！")
	}, {
		code4 : e
	});
	return t
}
function getDic(e, t, i) {
	var a;
	if (!i) {
		var i = getProjectName(location.href)
	}
	var n = window.sessionStorage;
	pName = i.split("/")[1];
	if (n && n[pName + "_" + t]) {
		var r = JSON.parse(n[pName + "_" + t]);
		return r
	} else {
		ajaxFunc(i + e, function(e) {
			a = e;
			if (t != "deptCodeName")
				n[pName + "_" + t] = JSON.stringify(e)
		}, function() {
			layerMsg("请求出错！")
		}, {
			type : t
		}, true);
		return a
	}
}
function linkedDic(e, t, i, a, n) {
	var r = $("[name=" + e + "]");
	if (r.find(":selected").val() != "") {
		var o = a + r.find(":selected").val();
		loadDic(t, i, o);
		for ( var l in n) {
			if (l == i) {
				$("[name=" + i + "]").find("[value='" + n[i] + "']").prop(
						"selected", true)
			}
		}
	}
	r.change(function() {
		if (r.find(":selected").val() != "") {
			o = a + r.find(":selected").val();
			loadDic(t, i, o)
		}
	})
}
function filterDic(t, i, a) {
	var e = $("[name=" + t + "] option");
	e.each(function() {
		if (i != false) {
			if ($(this).attr("value") == i) {
				$(this).prop("selected", true)
			}
		}
		if (a != undefined) {
			for (var e = 0; e < a.length; e++) {
				if ($(this).attr("value") == a[e]) {
					$(this).addClass(t + "Filter")
				}
			}
		}
	});
	if (a != undefined) {
		e.not("." + t + "Filter").remove()
	}
}
function ajaxFunc(e, i, t, a, n) {
	var r;
	if (!a) {
		a = null
	}
	$.ajax({
		type : "post",
		url : requestAddRandom(e),
		data : a,
		dataType : "json",
		async : false,
		beforeSend : function(e) {
			if (n != true) {
				layerLoading()
			}
		},
		success : function(e, t) {
			r = strToJson(e);
			if (i != undefined) {
				i(r)
			}
		},
		complete : function(e, t) {
			if (n != true) {
				layerCloseLoad()
			}
		},
		error : function(e) {
			r = strToJson(e);
			if (t != undefined) {
				t(r)
			}
		}
	})
}
function ajaxFuncForJson(e, i, t, a, n) {
	var r;
	if (!a) {
		a = null
	}
	$.ajax({
		type : "post",
		url : requestAddRandom(e),
		contentType : "application/json",
		data : a,
		async : false,
		beforeSend : function(e) {
			if (n != true) {
				layerLoading()
			}
		},
		success : function(e, t) {
			r = strToJson(e);
			if (i != undefined) {
				i(r)
			}
		},
		complete : function(e, t) {
			if (n != true) {
				layerCloseLoad()
			}
		},
		error : function(e) {
			r = strToJson(e);
			if (t != undefined) {
				t(r)
			}
		}
	})
}
function strToJson(e) {
	var t;
	if (typeof e == "string") {
		t = e == "" ? {} : $.parseJSON(e)
	} else {
		t = e
	}
	return t
}
function changeDatetoString(e) {
	var t = new Date(e);
	var i = t.getFullYear() + "-" + (t.getMonth() + 1) + "-" + t.getDate();
	return i
}
function fmtNumForShow(e, t) {
	if (e != null && e != undefined && e != "") {
		if (typeof e != "number") {
			e = parseFloat(e)
		}
		if (t != undefined) {
			e = e.toFixed(t)
		} else {
			e = e.toFixed(2)
		}
		if (typeof e != "string") {
			e = e.toString()
		}
		if (e.indexOf(".") > -1) {
			var i = e.split(".")[0].replace(/\d{1,3}(?=(\d{3})+(\.\d*)?$)/g,
					"$&,");
			return i + "." + e.split(".")[1]
		} else {
			var a = e.split(".")[0].replace(/\d{1,3}(?=(\d{3})+(\.\d*)?$)/g,
					"$&,");
			return a
		}
	} else {
		return e
	}
}
function fmtNumForSubmit(e) {
	if (e != null) {
		if (typeof e != "string") {
			e = e.toString()
		}
		if (e.split(".")[1] == "00") {
			return e.split(".00")[0].replace(/,/g, "")
		} else {
			return e.replace(/,/g, "")
		}
	}
}
function submitPage(a, e) {
	ajaxFunc(a, function(e) {
		if (a.indexOf("systemOrg") > -1) {
			var t = getProjectName(location.href);
			t = t.replace(/\//g, "");
			var i = window.sessionStorage;
			i.removeItem(t + "_orgTreeData")
		}
		submitMsg(e)
	}, function(e) {
		layerMsg("error:" + JSON.stringify(e));
		window.hasSubmit = false
	}, e)
}
function submitMsg(t) {
	if (t.code == "00000000") {
		layerAlert(t.msg, function() {
			var e = $("#iframeWrap", parent.document);
			if (e.hasClass("iframeTabs")) {
				submitTabsMsg(t, e)
			} else {
				reloadParentPage()
			}
		})
	} else {
		layerMsg(t.msg);
		$(".modal-footer button").attr("disabled", false);
		window.hasSubmit = false
	}
}
function bindPageBtnEvent() {
	var e = $(".modal-footer button");
	var t = parent.listObj;
	var i = parent.urlObj;
	e.each(function() {
		var e = $(this).attr("id");
		if (e == "closePage") {
			$("#closePage").bind("click", function() {
				closeParentPage()
			})
		} else if (e == "fastSubmitPage") {
			$("#fastSubmitPage").bind("click", function() {
				fastSubmitPage(i)
			})
		} else if (e == "delectPage") {
			$("#delectPage").bind("click", function() {
				delectPage(t, i)
			})
		} else if (e == "resetPage") {
			$("#resetPage").bind("click", function() {
				resetPage()
			})
		} else if (e == "stopReason") {
			$("#stopReason").bind("click", function() {
				var e = {
					id : $("[name='stopId']").val(),
					stopReason : $("[name='stopReason']").val()
				};
				stopSubmitFun(i.stopUser, e)
			})
		} else {
			return
		}
	})
}
function stopSubmitFun(e, t) {
	var i;
	ajaxFunc(e, function(e) {
		if (e.code == "00000000") {
			i = "操作成功！"
		} else if (e.code == "00000001") {
			i = "操作失败！"
		}
		layerAlert(i, reloadParentPage)
	}, function(e) {
		layerMsg("error:" + JSON.stringify(e))
	}, t)
}
function setComAndBizObj(e) {
	var t = $.parseJSON(e.comData);
	var i = $.parseJSON(e.bizData);
	if (i != null) {
		if (parent.comAndBiz.bizData == undefined) {
			parent.comAndBiz.bizData = i
		} else {
			for ( var a in i) {
				parent.comAndBiz.bizData[a] = i[a]
			}
		}
	}
	if (parent.comAndBiz.comData == undefined) {
		parent.comAndBiz.comData = t
	}
}
function getComAndBizObj(e) {
	var t = parent.comAndBiz;
	if (!$.isEmptyObject(t)) {
		if (e == "com") {
			return t.comData
		} else if (e == "biz") {
			return t.bizData
		} else if (e == null) {
			return t
		}
	} else {
		return undefined
	}
}
function setReadonly(e, t) {
	var i = $("#form-wrap").find("input,textarea,select"), a = [];
	i.each(function() {
		a.push($(this).attr("name"))
	});
	if (e == undefined) {
		e = a
	}
	if (t) {
		a = removeDuplicatedItem(a);
		for (var n = 0; n < e.length; n++) {
			a.removeByValue(e[n])
		}
		e = a
	}
	for (var n = 0; n < e.length; n++) {
		i = $("[name='" + e[n] + "']");
		if (!i.hasClass("disabled")) {
			i.addClass("disabled").attr("placeholder", "")
		}
		$("[name='" + e[n] + "']").attr("disabled", "disabled")
	}
}
function removeReadonly(e) {
	if (e == undefined) {
		e = [];
		var t = $("table").find(".disabled");
		t.each(function() {
			e.push($(this).attr("name"))
		})
	}
	for (var i = 0; i < e.length; i++) {
		var t = $("[name='" + e[i] + "']");
		if (t.hasClass("disabled")) {
			t.removeClass("disabled")
		}
		$("[name='" + e[i] + "']").attr("disabled", false)
	}
}
function resetPage() {
	$("#resetPage").attr("type", "button");
	var e = $("#form-wrap,.layui-form").find("input,textarea,select").not(
			".noReset");
	e.each(function() {
		if ($(this).prop("tagName") == "INPUT"
				|| $(this).prop("tagName") == "TEXTAREA") {
			$(this).val("")
		} else if ($(this).prop("tagName") == "SELECT") {
			$(this).get(0).selectedIndex = 0
		}
	})
}
function fastSubmitPage(e) {
	$("form").attr("action", e);
	$("form").submit()
}
function delectPage(e, t) {
	layerConfirm("确认此项操作？", function() {
		delectCallback(e, t)
	})
}
function closeParentPage() {
	if ($("form").attr("id") != "detail_form_id") {
		parent.location.reload()
	} else {
		var e = "";
		if ($(".iframeTabs", parent.document).length > 0) {
			e = "page"
		} else {
			e = "iframe"
		}
		$(".layui-layer-page", parent.document).remove();
		parent.layer.closeAll(e)
	}
}
function transmitId() {
	var e, t = [];
	if ($(".listWrap table tbody input[type='radio']").length > 0) {
		e = "radio"
	} else {
		e = "checkbox"
	}
	$(".listWrap table tbody input[type=" + e + "]").each(function() {
		if (this.checked) {
			var e = $(this).attr("id");
			t.push(e)
		}
	});
	return t
}
function isAllow(e, t) {
	var i = $(".tabHead li", parent.document).not(".empty");
	if (t == undefined) {
		i.removeClass("allow");
		if (e == undefined || e.length == 0) {
			i.addClass("allow")
		} else {
			for (var a = 0; a < e.length; a++) {
				i.eq(e[a]).addClass("allow")
			}
		}
	} else if (t == true) {
		i.addClass("allow");
		for (var a = 0; a < e.length; a++) {
			i.eq(e[a]).removeClass("allow")
		}
	}
	i.eq(0).addClass("allow")
}
function getUrlList(e, r, o) {
	var l, s = getProjectName(location.href);
	$.ajaxSetup({
		async : false
	});
	$.get(requestAddRandom(e), function(e, t) {
		if (t == "success") {
			l = strToJson(e);
			var i = l.tabs;
			if (i != undefined) {
				for (var a = 0; a < i.length; a++) {
					for ( var n in i[a]) {
						if (n != "name" && n != "eName" && n != "icon") {
							if (r && r.in_array(n)) {
								i[a][n] = o + i[a][n]
							} else {
								i[a][n] = s + i[a][n]
							}
						}
					}
				}
			} else {
				for ( var n in e) {
					if (n != "name" && n != "eName") {
						if (r && r.in_array(n)) {
							e[n] = o + e[n]
						} else {
							e[n] = s + e[n]
						}
					}
				}
			}
		} else {
			layerMsg("请求出错！")
		}
	});
	return l
}
function bindBtnEvent(e, t) {
	var i;
	$(".createBtn").bind("click", function() {
		i = createBtn(e, t);
		layerIndex = i
	});
	$(".updateBtn").bind("click", function() {
		i = updateBtn(e, t);
		layerIndex = i
	});
	$(".deleteBtn").bind("click", function() {
		i = deleteBtn(e, t);
		layerIndex = i
	});
	$(".searchBtn").bind("click", function() {
		i = searchBtn(e, t);
		layerIndex = i
	});
	$(".detailBtn").bind("click", function() {
		i = detailBtn(e, t);
		layerIndex = i
	});
	$(".exportBtn").bind("click", function() {
		i = exportBtn(e, t);
		layerIndex = i
	});
	$(".resetBtn").bind("click", function() {
		i = resetPage(e, t);
		layerIndex = i
	})
}
function createBtn(e, a) {
	var n = a.tabObj;
	if (!n || n.field == undefined) {
		n = {};
		n.field = "id"
	}
	var t = getField(n, null);
	if (n && n.tabs) {
		var i = {
			ele : ".datail",
			name : a.name + "-新增页",
			state : 0,
			tabs : n.tabs,
			urlList : n.listObj,
			fieldName : t.fieldName,
			fieldValue : t.fieldValue,
			data : "",
			showList : n.showList,
			custom : n.custom
		};
		index = createBox(i);
		return index
	} else {
		var r = a.creUrl;
		if (n && n.linkBasic) {
			layerPrompt("请输入客户号", function(e, t, i) {
				r += "?" + n.field + "=" + n.value;
				layerOpen(900, 500, a.icon, a.name + "-新增页", r, 2, function() {
				}, true)
			})
		} else {
			layerOpen(900, 500, a.icon, a.name + "-新增页", r, 2, function() {
			}, true)
		}
	}
}
function getField(e, n) {
	var t, i, r;
	if (!e || e.field == undefined) {
		e = {};
		e.field = "id"
	} else if (e && e.field != undefined) {
		if (typeof e.field == "string") {
			t = e.field;
			if (n) {
				i = n[e.field]
			}
		} else {
			r = [];
			var o = listObj.tabs;
			$.each(o, function(t) {
				var i = {
					eName : o[t].eName
				};
				var a = e.field;
				$.each(a, function(e) {
					if (t == a[e].tabNum) {
						i.fieldName = a[e].name;
						if (n) {
							i.fieldValue = n[a[e].name]
						}
						return false
					} else {
						i.fieldName = "id";
						if (n) {
							i.fieldValue = n["id"]
						}
					}
				});
				r.push(i)
			});
			t = r
		}
	}
	return {
		fieldName : t,
		fieldValue : i
	}
}
function updateBtn(e, t) {
	var i = t.tabObj;
	var a = e.table;
	var n = [], r = a.checkStatus("grid");
	var o = r.data;
	if (o.length == 1) {
		var l = getField(i, o[0]);
		if (i && i.tabs) {
			var s = {
				ele : "body",
				name : t.name + "-修改页",
				state : 1,
				tabs : i.tabs,
				urlList : i.listObj,
				fieldName : l.fieldName,
				fieldValue : l.fieldValue,
				pageName : t.pageName,
				showList : i.showList,
				custom : i.custom,
				authority : i.authority
			};
			var f = createBox(s)
		} else {
			if (!i || i.field == undefined) {
				i = {};
				i.field = "id"
			}
			layerOpen(900, 500, t.icon, t.name + "-修改页", t.updUrl + "?"
					+ i.field + "=" + o[0][i.field], 2, function() {
			}, true)
		}
	} else {
		layerMsg("请选择1条进行操作！")
	}
}
function detailBtn(e, t) {
	var i = t.tabObj;
	var a = getField(i, e);
	if (i && i.tabs) {
		var n = {
			ele : "body",
			name : t.name + "-详情页",
			state : 2,
			tabs : i.tabs,
			urlList : i.listObj,
			fieldName : a.fieldName,
			fieldValue : a.fieldValue,
			pageName : t.pageName,
			showList : i.showList,
			custom : i.custom,
			authority : i.authority
		};
		var r = createBox(n)
	} else {
		if (!i || i.field == undefined) {
			i = {};
			i.field = "id"
		}
		layerOpen(900, 500, t.icon, t.name + "-详情页", t.detailUrl + "?"
				+ i.field + "=" + e[i.field], 2, function() {
		}, true)
	}
}
function deleteBtn(e, t) {
	var i, a = t.tabObj;
	if (!a || a.field == undefined) {
		a = {};
		i = "id"
	} else {
		if (a.field[0].tabNum == 0) {
			i = a.field[0].name
		} else {
			i = "id"
		}
	}
	var n = e.table;
	var r = [], o = n.checkStatus("grid");
	var l = o.data;
	if (l.length > 0) {
		if (l.length == 1) {
			r = l[0][i]
		} else {
			for (var s = 0; s < l.length; s++) {
				r.push(l[s][i])
			}
		}
		layerConfirm("确认此项操作？", function() {
			deleteBtnCallback(e, r, t)
		})
	} else {
		layerMsg("请选择1条以上进行操作！")
	}
}
function searchBtn(e, t) {
	var i = e.dataTable;
	var a = $("form").serializeJSON();
	var n = e.searchData;
	if (n) {
		for ( var r in n) {
			if (a[r] != "" && !a[r]) {
				a[r] = n[r]
			}
		}
	}
	i.reload({
		where : a,
		page : {
			curr : 1
		}
	})
}
function exportBtn(e, t) {
	if (e.table.cache.grid.length > 0) {
		var i = $(".layui-laypage-count").text();
		var a = parseInt(i.substring(1, i.length - 1));
		if (a > 5e3) {
			layerMsg("暂不支持导出大于5000条的数据，请添加查询条件");
			return
		}
		var n = e.table;
		var r = [], o = n.checkStatus("grid");
		var l = o.data;
		for (var s = 0; s < l.length; s++) {
			r.push(l[s].id)
		}
		var f = $("form").serializeJSON();
		var c = e.searchData;
		if (c) {
			for ( var s in c) {
				if (f[s] != "" && !f[s]) {
					f[s] = c[s]
				}
			}
		}
		f.ids = r + "";
		var d = paramObjToStr(f);
		window.location.href = t.exportUrl + "?" + d
	} else {
		layerMsg("无数据可导出")
	}
}
function onlyTheFirst() {
	var t = $("#noRpt option");
	t.each(function(e) {
		if (t.eq(e).val() == "1" || t.eq(e).val() == "2") {
			$(this).remove()
		}
	})
}
function closePage() {
	window.location.reload()
}
function reloadGrid(e, t) {
	e.reload({
		where : t,
		page : {
			curr : 1
		}
	})
}
function removeDuplicatedItem(e) {
	for (var t = 0; t < e.length - 1; t++) {
		for (var i = t + 1; i < e.length; i++) {
			if (e[t] == e[i]) {
				e.splice(i, 1);
				i--
			}
		}
	}
	return e
}
Array.prototype.in_array = function(e) {
	for (i = 0; i < this.length; i++) {
		if (this[i] == e)
			return true
	}
	return false
};
Array.prototype.removeByValue = function(e) {
	for (var t = 0; t < this.length; t++) {
		if (this[t] == e) {
			this.splice(t, 1);
			break
		}
	}
};
function BigDecimalAdd(e, t, i) {
	if (e == "") {
		e = "0"
	}
	if (t == "") {
		t = "0"
	}
	var a = new BigDecimal(e).add(new BigDecimal(t)).setScale(i,
			MathContext.ROUND_HALF_UP).toString();
	return a
}
function BigDecimalSubtract(e, t, i) {
	if (e == "") {
		e = "0"
	}
	if (t == "") {
		t = "0"
	}
	var a = new BigDecimal(e).subtract(new BigDecimal(t)).setScale(i,
			MathContext.ROUND_HALF_UP).toString();
	return a
}
function paramObjToStr(i, a) {
	var n = "";
	if (i instanceof String || i instanceof Number || i instanceof Boolean) {
		n += "&" + a + "=" + encodeURIComponent(i)
	} else {
		$.each(i, function(e) {
			var t = a == null ? e : a
					+ (i instanceof Array ? "[" + e + "]" : "." + e);
			n += "&" + paramObjToStr(this, t)
		})
	}
	return n.substr(1)
}
function getParamObj() {
	var e = location.search;
	var t = new Object;
	if (e.indexOf("?") != -1) {
		var i = e.substr(1);
		strs = i.split("&");
		for (var a = 0; a < strs.length; a++) {
			t[strs[a].split("=")[0]] = decodeURIComponent(strs[a].split("=")[1])
		}
	}
	return t
}
function getAjaxData(e, t) {
	var i, a = "POST";
	if (!t) {
		a = "GET"
	}
	$.ajax({
		type : a,
		url : requestAddRandom(e),
		data : t,
		success : function(e) {
			i = strToJson(e)
		},
		async : false
	});
	return i
}
function BrowserType() {
	var e = navigator.userAgent;
	var t = e.indexOf("Opera") > -1;
	var i = window.ActiveXObject || "ActiveXObject" in window;
	var a = e.indexOf("Edge") > -1;
	var n = e.indexOf("Firefox") > -1;
	var r = e.indexOf("Safari") > -1 && e.indexOf("Chrome") == -1;
	var o = e.indexOf("Chrome") > -1 && e.indexOf("Safari") > -1 && !a;
	if (i) {
		var l = new RegExp("MSIE (\\d+\\.\\d+);");
		l.test(e);
		var s = parseFloat(RegExp["$1"]);
		if (e.indexOf("MSIE 6.0") != -1) {
			return "IE6"
		} else if (s == 7) {
			return "IE7"
		} else if (s == 8) {
			return "IE8"
		} else if (s == 9) {
			return "IE9"
		} else if (s == 10) {
			return "IE10"
		} else if (e.toLowerCase().match(/rv:([\d.]+)\) like gecko/)) {
			return "IE11"
		} else {
			return "0"
		}
	}
	if (n) {
		return "FF"
	}
	if (t) {
		return "Opera"
	}
	if (r) {
		return "Safari"
	}
	if (o) {
		return "Chrome"
	}
	if (a) {
		return "Edge"
	}
}
function isAcrobatPluginInstall() {
	if (window.ActiveXObject || navigator.userAgent.indexOf("Trident") > -1) {
		for (x = 2; x < 10; x++) {
			try {
				oAcro = eval("new ActiveXObject('PDF.PdfCtrl." + x + "');");
				if (oAcro) {
					return true
				}
			} catch (e) {
			}
		}
		try {
			oAcro4 = new ActiveXObject("PDF.PdfCtrl.1");
			if (oAcro4)
				return true
		} catch (e) {
		}
		try {
			oAcro7 = new ActiveXObject("AcroPDF.PDF.1");
			if (oAcro7)
				return true
		} catch (e) {
		}
	} else {
		return true
	}
}
function orgTree() {
	var e = [];
	if ($(".orgTree").length > 0) {
		var e = [ "../static/script/zTree_v3/jquery.ztree.core.min.js",
				"../static/script/zTree_v3/orgTree.js",
				"../static/script/zTree_v3/style/static.css",
				"../static/script/zTree_v3/style/zTreeStyle_01.css" ];
		for (var t = 0; t < e.length; t++) {
			var i = e[t];
			if (i.match(/.*.js$/)) {
				$("body").append(
						'<script type="text/javascript" src="' + i
								+ '"><\/script>')
			} else if (i.match(/.*.css$/)) {
				loadCss(i)
			}
		}
		var a = getProjectName(location.href);
		var n = getAjaxData(a + "common/orgTree", {
			type : "orgTreeData"
		});
		orgTreeFun(n)
	}
}
function menuTree() {
	var e = [];
	if ($(".menuTree").length > 0) {
		var e = [ "../static/script/zTree_v3/jquery.ztree.core.min.js",
				"../static/script/zTree_v3/menuTree.js",
				"../static/script/zTree_v3/style/static.css",
				"../static/script/zTree_v3/style/zTreeStyle_01.css" ];
		for (var t = 0; t < e.length; t++) {
			var i = e[t];
			if (i.match(/.*.js$/)) {
				$("body").append(
						'<script type="text/javascript" src="' + i
								+ '"><\/script>')
			} else if (i.match(/.*.css$/)) {
				loadCss(i)
			}
		}
		var a = getProjectName(location.href);
		var n = getAjaxData(a + "common/menuTree", {
			type : "menuTreeData"
		});
		menuTreeFun(n)
	}
}
function reloadGrid(e, t, i) {
	if (!i) {
		i = 1
	}
	var a = e.dataTable;
	a.reload({
		where : t,
		page : {
			curr : i
		}
	})
}