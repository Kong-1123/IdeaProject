$(function(){
	//pageInit();
});
function pageInit(){

	var pPageWidth = 794; //打印时的A4完整宽度（96dpi）
	var pPageHeight = $(document).height();
	var pContentwidth = 720; //打印时的内容宽度（96dpi）,同步css
	var A4Heigth = 1123; //A4纸的高度（px，96dip）。固定值

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
		
		//获得水印图片高度后重置
		
		//这里应该还要加上打印时每一页的上下边距4cm(同步css)≈72px(96dpi)
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
