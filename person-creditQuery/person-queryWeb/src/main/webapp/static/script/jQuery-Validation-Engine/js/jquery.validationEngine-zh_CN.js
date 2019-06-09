(function($) {
	var pName = window.sessionStorage.projectName;
	if(!pName){
		var re = /(\w+):\/\/([^\:|\/]+)(\:\d*)?(.*\/)?(.*\/)([^#|\?|\n]+)?(#.*)?(\?.*)?/i;
		var url = top.location.href;
	    var arr = url.match(re);
		pName = arr[4]+arr[5];
	}
	$.fn.validationEngineLanguage = function() {
	};
	$.validationEngineLanguage = {
		newLang : function() {
			$.validationEngineLanguage.allRules = {
				"required" : { // Add your regex rules here, you can take
					// telephone as an example
					"regex" : "none",
					"alertText" : "* 输入不能为空",
					"alertTextCheckboxMultiple" : "* 请选择一个项目",
					"alertTextCheckboxe" : "* 该选项为必选",
					"alertTextDateRange" : "* 日期范围不可空白"
				},
				"noSpecialCaracters" : {
					"regex" : "^[0-9a-zA-Z]+$",
					"alertText" : "* 请输入英文字母或数字."
				},
				"dateRange" : {
					"regex" : "none",
					"alertText" : "* 无效的 ",
					"alertText2" : " 日期范围"
				},
				"dateTimeRange" : {
					"regex" : "none",
					"alertText" : "* 无效的 ",
					"alertText2" : " 时间范围"
				},
				"minSize" : {
					"regex" : "none",
					"alertText" : "* 最少 ",
					"alertText2" : " 个字符"
				},
				"maxSize" : {
					"regex" : "none",
					"alertText" : "* 最多 ",
					"alertText2" : " 个字符"
				},
				"initCredOrgNmSize" : {
					"regex" : "^\\d{18}$",
					"alertText" : "* 应为18 位"
				},
				"creditIdSize" : {
					"regex" : "^\\d{4}$",
					"alertText" : "* 应为 4 位"
				},
				"requiredSize" : {
					"regex" : "^[0-9a-zA-Z]{14}$",
					"alertText" : "* 应为数字和英文的字母组成且 14 位"
				},
				"groupRequired" : {
					"regex" : "none",
					"alertText" : "* 至少填写其中一项"
				},
				"min" : {
					"regex" : "none",
					"alertText" : "* 最小值为 "
				},
				"max" : {
					"regex" : "none",
					"alertText" : "* 最大值为 "
				},
				"past" : {
					"regex" : "none",
					"alertText" : "* 日期需在 ",
					"alertText2" : " 之前"
				},
				"future" : {
					"regex" : "none",
					"alertText" : "* 日期需在 ",
					"alertText2" : " 之后"
				},
				"maxCheckbox" : {
					"regex" : "none",
					"alertText" : "* 最多选择 ",
					"alertText2" : " 个项目"
				},
				"minCheckbox" : {
					"regex" : "none",
					"alertText" : "* 最少选择 ",
					"alertText2" : " 个项目"
				},
				"equals" : {
					"regex" : "none",
					"alertText" : "* 两次输入的密码不一致"
				},
				"creditCard" : {
					"regex" : "none",
					"alertText" : "* 无效的信用卡号码"
				},
				"phone" : {
					// credit:jquery.h5validate.js / orefalo
					"regex" : /^([\+][0-9]{1,3}[ \.\-])?([\(]{1}[0-9]{2,6}[\)])?([0-9 \.\-\/]{3,20})((x|ext|extension)[ ]?[0-9]{1,4})?$/,
					"alertText" : "* 无效的电话号码"
				},
				"mobile" : {
					"regex" : /^[1][3,4,5,7,8][0-9]{9}$/,
					"alertText" : "* 无效的手机号码"
				},
				"email" : {
					// Shamelessly lifted from Scott Gonzalez via the
					// Bassistance Validation plugin
					// http://projects.scottsplayground.com/email_address_validation/
					"regex" : /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i,
					"alertText" : "* 无效的邮件地址"
				},
				"integer" : {
					"regex" : /^[\-\+]?\d+$/,
					"alertText" : "* 无效的整数"
				},
				"money" : {
					//只能是整数或逗号
					"regex" : /^\d+(,\d+)*$/,
					"alertText" : "* 无效的自然数"
				},
				"number" : {
					// Number, including positive, negative, and floating
					// decimal. credit:orefalo
					"regex" : /^[\-\+]?((([0-9]{1,3})([,][0-9]{3})*)|([0-9]+))?([\.]([0-9]+))?$/,
					"alertText" : "* 无效的数值"
				},
				"date" : {
					"regex" : /^\d{4}[\/\-](0?[1-9]|1[012])[\/\-](0?[1-9]|[12][0-9]|3[01])$/,
					"alertText" : "* 无效的日期，格式必需为 YYYY-MM-DD"
				},
				"ipv4" : {
					"regex" : /^((([01]?[0-9]{1,2})|(2[0-4][0-9])|(25[0-5]))[.]){3}(([0-1]?[0-9]{1,2})|(2[0-4][0-9])|(25[0-5]))$/,
					"alertText" : "* 无效的 IP 地址"
				},
				"url" : {
					"regex" : /^(https?|ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(\#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i,
					"alertText" : "* 无效的网址"
				},
				"onlyNumberSp" : {
					"regex" : /^[0-9\ ]+$/,
					"alertText" : "* 只能填写数字"
				},
				"shiNumber" : {// /^\d*[0-9](|.\d*[0-9]|,\d*[0-9])?$/
					"regex" : /(?:^(?:[1-9][\d]?)(?:\.[\d]{1,2})?$)|(?:^100(?:\.0{1,2})?$)/,
					"alertText" : "* 只能填写实数"
				},
				"onlyLetterSp" : {
					"regex" : /^[a-zA-Z_\ \']+$/,
					"alertText" : "* 只能填写英文字母"
				},
				"onlyLetter" : {
					"regex" : "/^[a-zA-Z]+$/",
					"alertText" : "* 请输入英文字母."
				},
				"onlyLetterNumber" : {
					"regex" : /^[0-9a-zA-Z]+$/,
					"alertText" : "* 只能填写数字与英文字母"
				},
				"onlyLetterSymbol" : {
					"regex" : /^[0-9a-zA-Z/\-_]+$/,
					"alertText" : "* 只能填写数字与英文字母和特殊符号-,_或/"
				},
				"onlyUInt" : {
					"regex" : /^\d+$/,
					"alertText" : "* 只能填写自然数"
				},
				"onlyGtZreo" : {
					"regex" : /^[1-9]{1}[0-9]*$/,
					"alertText" : "* 只能填写大于0自然数"
				},
				// tls warning:homegrown not fielded
				"dateFormat" : {
					"regex" : /^\d{4}[\/\-](0?[1-9]|1[012])[\/\-](0?[1-9]|[12][0-9]|3[01])$|^(?:(?:(?:0?[13578]|1[02])(\/|-)31)|(?:(?:0?[1,3-9]|1[0-2])(\/|-)(?:29|30)))(\/|-)(?:[1-9]\d\d\d|\d[1-9]\d\d|\d\d[1-9]\d|\d\d\d[1-9])$|^(?:(?:0?[1-9]|1[0-2])(\/|-)(?:0?[1-9]|1\d|2[0-8]))(\/|-)(?:[1-9]\d\d\d|\d[1-9]\d\d|\d\d[1-9]\d|\d\d\d[1-9])$|^(0?2(\/|-)29)(\/|-)(?:(?:0[48]00|[13579][26]00|[2468][048]00)|(?:\d\d)?(?:0[48]|[2468][048]|[13579][26]))$/,
					"alertText" : "* 无效的日期格式"
				},
				// tls warning:homegrown not fielded
				"dateTimeFormat" : {
					"regex" : /^\d{4}[\/\-](0?[1-9]|1[012])[\/\-](0?[1-9]|[12][0-9]|3[01])\s+(1[012]|0?[1-9]){1}:(0?[1-5]|[0-6][0-9]){1}:(0?[0-6]|[0-6][0-9]){1}\s+(am|pm|AM|PM){1}$|^(?:(?:(?:0?[13578]|1[02])(\/|-)31)|(?:(?:0?[1,3-9]|1[0-2])(\/|-)(?:29|30)))(\/|-)(?:[1-9]\d\d\d|\d[1-9]\d\d|\d\d[1-9]\d|\d\d\d[1-9])$|^((1[012]|0?[1-9]){1}\/(0?[1-9]|[12][0-9]|3[01]){1}\/\d{2,4}\s+(1[012]|0?[1-9]){1}:(0?[1-5]|[0-6][0-9]){1}:(0?[0-6]|[0-6][0-9]){1}\s+(am|pm|AM|PM){1})$/,
					"alertText" : "* 无效的日期或时间格式",
					"alertText2" : "可接受的格式： ",
					"alertText3" : "mm/dd/yyyy hh:mm:ss AM|PM 或 ",
					"alertText4" : "yyyy-mm-dd hh:mm:ss AM|PM"
				},
				
				"timeFormat":{
					"regex": /([01][0-9]|2[0-3])(:[0-5][0-9]){1,2}-([01][0-9]|2[0-3])(:[0-5][0-9]){1,2}/,
					"alertText":"* 无效的时间格式，标准格式为hh:mm:ss-hh:mm:ss"
				},
				/**
				 * 正则验证规则补充 Author: ciaoca@gmail.com Date: 2013-10-12
				 */
				"chinese" : {
					"regex" : /^[\u4E00-\u9FA5]+$/,
					"alertText" : "* 只能填写中文汉字"
				},
				"blank" : {
					"regex" : /^\S.*\S$/,
					"alertText" : "* 前后不能有空格"
				},
				"cnAndEn" : {
					//"regex" : /^[\u4E00-\u9FA5·s_a-zA-Z.s]+$/,
					"regex" : /^[\u4E00-\u9FA5·s_a-zA-Z.s][\s\u4E00-\u9FA5·s_a-zA-Z.s]*[\u4E00-\u9FA5·s_a-zA-Z.s]*$/,
					"alertText" : "* 只能填写中文汉字与英文字母"
				},
				"chinaId" : {
					/**
					 * 2013年1月1日起第一代身份证已停用，此处仅验证 18 位的身份证号码 如需兼容 15
					 * 位的身份证号码，请使用宽松的 chinaIdLoose 规则 /^[1-9]\d{5}[1-9]\d{3}( (
					 * (0[13578]|1[02]) (0[1-9]|[12]\d|3[01]) )|( (0[469]|11)
					 * (0[1-9]|[12]\d|30) )|( 02 (0[1-9]|[12]\d) )
					 * )(\d{4}|\d{3}[xX])$/i
					 */
					"regex" : /^[1-9]\d{5}[1-9]\d{3}(((0[13578]|1[02])(0[1-9]|[12]\d|3[0-1]))|((0[469]|11)(0[1-9]|[12]\d|30))|(02(0[1-9]|[12]\d)))(\d{4}|\d{3}[xX])$/,
					"alertText" : "* 无效的身份证号码"
				},
				"chinaIdLoose" : {
					"regex" : /^(\d{18}|\d{15}|\d{17}[xX])$/,
					"alertText" : "* 无效的身份证号码"
				},
				"chinaZip" : {
					"regex" : /^\d{6}$/,
					"alertText" : "* 无效的邮政编码"
				},
				"qq" : {
					"regex" : /^[1-9]\d{4,10}$/,
					"alertText" : "* 无效的 QQ 号码"
				},
				"checkCard":{
					"alertText0" : "* 请选择证件类型",
					"alertText2" : "* 无效的身份证号码",
					"alertText3" : "* 无效的港澳居民往来大陆通行证代码",
					"alertText5" : "* 无效的台湾居民往来大陆通行证代码"
				},
				"orgCode" : {
					"alertText10" : "* 无效的中征码（原贷款卡编码）",
					"alertText20" : "* 无效的统一社会信用代码",
					"alertText30" : "* 无效的组织机构代码"
				},
				// 判断菜单ID不能重复 不能存在
				"ajaxFindById" : {
					"url" : pName+"sysMenu/ajaxFindById",
					"alertText" : "* 菜单ID不能重复.",
						"alertTextLoad" : "* 检查中, 请稍后..."
				},
				// 判断父级菜单ID是否存在
				"ajaxFindByParentId" : {
					"url" : pName+"sysMenu/ajaxFindByParentId",
					"alertText" : "* 父菜单ID不存在.",
					"alertTextLoad" : "* 检查中, 请稍后..."
				},
				"numAndLet" : {
					"regex" : /^[0-9a-zA-Z]+$/,
					"alertText" : "* 请输入英文字母或数字."
				},
				"timeQuantum":{
                    "regex": /([01][0-9]|2[0-3])(:[0-5][0-9])-([01][0-9]|2[0-3])(:[0-5][0-9])/,
                    "alertText":"* 无效的时间格式.可接受的格式：hh:mm-hh:mm "
                },
                "checkExpireDate":{
                	"alertText" : "* 授权到期日应晚于查询时间."
                },
                "checkStartDate":{
                	"alertText" : "* 授权起始日应早于查询时间."
                },
                "checkassocbsnssData":{
                    "alertText" : "* 查询原因为贷后管理时，必须填写关联业务数据."
                },
                "credituser":{
                    "regex":/^[0-9a-zA-Z_\-\/]+$/,
                    "alertText":"征信用户只能填写数字、英文、_、-、/"
                },
                "haveBank":{
                	"regex":/^\S+$/,
                	"alertText":"不允许存在空格"
                }
               
			};

		}
	};
	$.validationEngineLanguage.newLang();
})(jQuery);
