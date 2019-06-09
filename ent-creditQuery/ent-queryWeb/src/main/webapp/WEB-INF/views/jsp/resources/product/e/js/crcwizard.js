/**
 * CRCCWizard v1.1 by YC
 * 参数 steps:"step1,step2...", current:0(从0开始)
 * 2016/4/18.
 
 * v1.1
 * 2016/5/24.
 * 修改li结构保证加上背景色后箭头样式仍然正确
**/
;
(function ($) {
	//私有函数s
	  //转义用于输出到页面上的非用于html的字符串
	function escapeHtml (string) {
		var entityMap = {
			'&': '&amp;',
			'<': '&lt;',
			'>': '&gt;',
			'"': '&quot;',
			"'": '&#39;',
			'/': '&#x2F;',
			'`': '&#x60;',
			'=': '&#x3D;'
		};
		return String(string).replace(/[&<>"'`=\/]/g, function fromEntityMap (s) {
			return entityMap[s];
		});
	}; //end escapeHtml
	
	function initWizard($plugin, settings){

		var curIndex = settings.current;
		var stepsArr = settings.steps;
		var stepsLen = stepsArr.length;
		var stepsWidth = (100 / stepsLen).toString(10).concat("%");
		
		var oWizardWrap = $('<ul class="m-wizard-wrap"/>');
		
		for (var i = 0; i < stepsLen; i++){
			var oStep = $('<li/>').css({
					"width": stepsWidth
				});
			if(i==curIndex){
				oStep.addClass("z-wizard-current");
			}else{
				oStep.addClass("u-wizard-step");
			}
			var stepname = stepsArr[i]; //escapeHtml(stepsArr[i]);
			var oSteptt = $('<span class="u-wizard-txt"/>').html(stepname).attr("title", stepname);
			var oArrow = $('<span class="u-wizard-steparw"/>');
			oArrow.add(oSteptt).appendTo(oStep);
			oStep.appendTo(oWizardWrap);
			
		}
		
		oWizardWrap.appendTo($plugin);
		
	}; //end initWizard

	//私有函数e
	
	$.fn.CRCCWizard = function () {
				var defaults = {
            "steps": [],
						"current": 0
        }; //end defaults
				
				var settings, optionss;
				
				if(arguments.length){
					$.each(arguments, function(i, n){
						if($.type(n)==='object'){
							optionss = n;
						}else if($.type(n)==='array'){
							defaults.steps = n;
						}else if($.type(n)==='number'){
							defaults.current = parseInt(n);
						}else if($.type(n)==='string'){
							if($.isNumeric(n)){
								defaults.current = parseInt(n);
							}else{
								defaults.steps = n.split(",");
							}
						}
					}); //end arguments each
				}
				
				settings = $.extend({}, defaults, optionss);
				
				return this.each(function () {
            var plugin = $(this);
            initWizard(plugin, settings);
        }); //e each
				
	}; //end fn.CRCCWizard
})(jQuery);