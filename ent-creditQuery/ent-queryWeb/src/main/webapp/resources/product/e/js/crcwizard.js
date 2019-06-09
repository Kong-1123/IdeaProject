/**
 * CRCCWizard v1.1 by YC
 * ���� steps:"step1,step2...", current:0(��0��ʼ)
 * 2016/4/18.
 
 * v1.1
 * 2016/5/24.
 * �޸�li�ṹ��֤���ϱ���ɫ���ͷ��ʽ��Ȼ��ȷ
**/
;
(function ($) {
	//˽�к���s
	  //ת�����������ҳ���ϵķ�����html���ַ���
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

	//˽�к���e
	
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