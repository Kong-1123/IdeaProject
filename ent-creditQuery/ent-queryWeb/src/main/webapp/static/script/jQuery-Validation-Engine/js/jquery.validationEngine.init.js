/**
 * [ajax验证回调方法]
 */
/*function ajaxUserCall(field,rules,i,options){

}*/
/**
 * [为自定义校验方法返回ID和VALUE等值]
 * @param  {[object]} field   [当前校验对象]
 * @param  {[object]} options [校验框架配置对象]
 * @return {[arr]} [数组对象，包含 1.关联验证对象选中项的id与value；
 * 2.验证对象的id和value；3.当前校验规则提示信息对象]
 */
function returnObject(field, options, ruleName) {
	var iframe = $("#iframeWrap iframe")[0];
	// console.log(iframe);
	// var linkedArr = [];
	var linkedArr = {};
	if (iframe != undefined) {
		var selId = "idType";
		var selVal = iframe.contentWindow.document.getElementById("idType").value;
		linkedArr[selId] = {
			id : selId,
			value : selVal
		};
	} else {
		var linked = $("[validateId]").not(field);
		// console.log($("[validateId]"));
		if (linked.length != 0) {
			linked.each(function(i) {
						var linkedValidateId = $(this).attr("validateId");
						var tagName = $(this).prop("tagName");
						var chkOrRdVal = $(this).find(":checked").val();
						if (linkedValidateId == field.attr("validateId")) {
							if (tagName == "INPUT") {
								//console.log("input");
								var iptId = $(this).attr("id");
								var iptVal = $(this).val();
								linkedArr[iptId] = {
									id : iptId,
									value : iptVal
								};
								/*
								 * eval("var iptEle" + i +
								 * "={iptId:iptId,iptVal:iptVal}");
								 * eval("linkedArr.push(iptEle" + i +")");
								 */
							} else if (tagName == "SELECT") {

								var selId = $(this).attr("id");
								var selVal = $(this).find("option:selected").val();
								linkedArr[selId] = {
									id : selId,
									value : selVal
								};
								/*
								 * eval("var selEle" + i +
								 * "={SelId:SelId,SelVal:SelVal}");
								 * eval("linkedArr.push(selEle" + i +")");
								 */
							} else if ($(this).hasClass("radioWrap")) {
								var rdId = $(this).find("input:checked").attr("id");
								var rdVal = $(this).find("input:checked").val();
								if (rdId != undefined) {
									linkedArr[rdId] = {
										id : rdId,
										value : rdVal
									};
									/*
									 * eval("var rdEle" + i +
									 * "={rdId:rdId,rdVal:rdVal}");
									 * eval("linkedArr.push(rdEle" + i +")");
									 */
								}
							} else if ($(this).hasClass("checkboxWrap")) {
								eval("var chkEle" + i + "=[]");
								for (var j = 0; j < $(this).find(":checked").length; j++) {
									eval("var chkId"
											+ j
											+ " = $(this).find('input:checked').eq(j).attr('id')");
									eval("var chkVal"
											+ j
											+ " = $(this).find('input:checked').eq(j).val()");
									eval("var chkEleChild" + j
											+ "={chkId:chkId" + j
											+ ",chkVal:chkVal" + j + "}")
									eval("chkEle" + i + ".push(chkEleChild" + j
											+ ")");
								}
								linkedArr[chkId] = eval("chkEle" + i);
							} else {
								return false;
							}
						}
					})
		}
	}
	var iptEle = {};
	var iptClass = field.attr("class");
	iptEle.value = field.val();
	iptEle.id = field.attr("id");
	// var ruleName = iptClass.match(/\[([^\[]*[^\]])\]/)[1];
	// var ruleName = /funcCall\[(.*?)\]/.exec(iptClass)[1];
	var arr = [ linkedArr, iptEle, options.allrules[ruleName] ];
	return arr;
}

/**
 * [为必填项输入框后添加 “ * ” ]
 */
function addRequiredIcon(){
	var icon = '<span class="star" id="certtypeSpan">*</span>';
	var ipts = $("form").not("#detail_form_id").find("input[type='text'],input[type='password'],select,textarea");
	ipts.each(function(){
		var className = $(this).attr("class");
		if( className!=undefined && className.indexOf("required")>=0){
			$(this).parents("li").find(".formLabel").before(icon); 
		}
	})
}


/**
 * [校验通过回调函数]
 * @return {[type]} [description]
 */
/*function validateSubmit(submitUrl) {
	$("form").validationEngine(
	{
		ajaxSubmit: true, 
		ajaxSubmitFile: submitUrl,
		onValidationComplete: function(form, status){
	      if(status){
				submitPage(submitUrl);
	      }else{
	      		onFailure();
	      }
	    }
	}
	)
}*/

/**
 * [selectValidate layui中校验select方法]
 * @return {[type]} [description]
 */
function selectValidate(){
	$("select").each(function(){
		var t =$(this);
        var className1 = t.attr("class");
        var className2 =  t.next().find(".layui-input").attr("class");
        if(!className2.indexOf()>-1){
        	t.next().find(".layui-input").addClass(className1);
        }
    })
}

/**
 * [校验未通过回调函数]
 * @return {[type]} [description]
 */
function onFailure() {
	window.hasSubmit = false;
  	layerMsg("验证不通过，提交失败！")
}

/**
 * [初始化验证框架]
 */
$(function(){
		addRequiredIcon();
		var submitUrl,formData;
		window.hasSubmit = false;
		$("form").validationEngine('attach', {
			validationEventTrigger: "input propertychange blur",
			promptPosition:"bottomLeft",
			relative: true,
			isOverflown:true,
            overflownDIV: "#form-wrap",
            ajaxSubmit: true, 
			onValidationComplete: function(form, status){
		      if(status){
		      	  // 校验通过后禁止提交按钮，防止重复提交
		      	  $("#submitPage").attr("disabled",true);
		      	  // 提交前取消表单数据disabled
		      	  $("form select,form input,form textarea").attr("disabled",false);
		      	  // 身份证15位转18位后提交
		      	  var len18,len15 = $("form input.idCard15").val();
	      	  	  if(len15){
					len18 = idLen15To18($(".idCard15"),len15);
	      	  	  	$("form input.idCard15").val(len18);
	      	  	  }
			  	  formData = $("form").serializeJSON();
			  	  if(window.jsonData){
			  	  	 formData = updateSubmitData(jsonData,formData);
			  	  }
		      	  submitUrl = $("form").attr("action");
		      	  if(window.hasSubmit){
		      		  return;
		      	  }
		          window.hasSubmit = true;
		      	  if(window.submitFun != undefined){
		      	  	submitFun(submitUrl,formData);
		      	  }else{
					submitPage(submitUrl,formData);
		      	  }
		      }else{
		      		onFailure()
		      }
		    }
		}); 
});

/**
 * [updateSubmitData 更新findbyid取回数据进行提交]
 * @param  {[type]} jsonData [findbyid取回数据]
 * @param  {[type]} formData [表单进行修改后的数据]
 * @return {[type]}          [description]
 */
function updateSubmitData(jsonData,formData){
	for(var k in formData ){
		jsonData[k] = formData[k];
	}
	return jsonData;
}