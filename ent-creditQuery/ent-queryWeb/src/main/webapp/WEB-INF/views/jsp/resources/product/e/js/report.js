$(function(){
	//pageInit();
});
function pageInit(){

	var pPageWidth = 794; //��ӡʱ��A4������ȣ�96dpi��
	var pPageHeight = $(document).height();
	var pContentwidth = 720; //��ӡʱ�����ݿ�ȣ�96dpi��,ͬ��css
	var A4Heigth = 1123; //A4ֽ�ĸ߶ȣ�px��96dip�����̶�ֵ

	var markBox = $('<div class="u-wmarkbox"/>').css("cssText","width:auto!important;width:".concat(pPageWidth,"px;min-width:",pPageWidth,"px")).css({
								"display":"none",
                "position": "absolute",
                "top": 0,
                "left": 0,
                "height": pPageHeight.toString(10).concat("px"), //pPageHeight, 100%
                "backgroundColor": "#FFF",
                "overflow": "hidden",
								"zIndex": 1
            });
	var wmimg = new Image();
	wmimg.onload = function(){repeatwmimg(this);}
	wmimg.src = '../../images/pbccrc_watermark2.gif';
	
	function repeatwmimg(oimg){
		var imgwidth = oimg.width;
		var imgheight = oimg.height;
		
		//���ˮӡͼƬ�߶Ⱥ�����
		
		//����Ӧ�û�Ҫ���ϴ�ӡʱÿһҳ�����±߾�4cm(ͬ��css)��72px(96dpi)
		//var wmHeight = Math.ceil(pPageHeight / A4Heigth) * A4Heigth;
		//var wmHeight = Math.ceil(pPageHeight / A4Heigth) * (A4Heigth + 145);
		var wmHeight = Math.ceil(pPageHeight / A4Heigth) * A4Heigth;
		markBox.css("height", wmHeight.toString(10).concat("px"));
		
		var hi = Math.ceil(pPageWidth / imgwidth);
		var vi = Math.ceil(wmHeight / imgheight);
		for(var i = 0; i < vi; i++){
			var vidiv = $('<div class="u-row"/>').css({
				"width": (imgwidth * hi).toString(10).concat("px"),
				"height": imgheight.toString(10).concat("px")
			});
			for(var j = 0; j < hi; j++){
				vidiv.append($(oimg).clone(false).css({"display":"block","float":"left","border":"none"}));
			}
			vidiv.appendTo(markBox);
		}
		markBox.prependTo("body");
	};
}
