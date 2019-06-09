///**
// * crcModal v1.5.0
// * Created by yc on 2015/9/27.
// * 1.添加是否可拖动属性，以解决弹窗在较小显示屏下无法操作的问题
// * 2.修改传入参数为自动判断。 ([options:object,配置][,url:string,iframe地址][callback:function,回调函数])
// * 3.修改窗体boxSizing样式，避免被bootstarp的默认样式覆盖而导致窗体内容计算错误

// * 4.2016-5-19修改如果设置不显示遮罩的时候可以点击弹框后面的页面内容。
// */
// options, url, wfun

// 改1.4，当showmask=false时，点击弹出弹窗后再次点击弹出时会有问题。
// 加入全局变量，在页面已有弹窗时不会新弹出窗口
// 2016-5-23
// {crcmodal : {isopen: false, modal: null}};
// isopen	: [true/false] 当前是否有显示的弹窗
// modal	: [object] 弹窗对象。含有close方法

//v1.4.1
//2016-5-31
//修改测试参数的宽高值，规范为整数

//v1.5.0
//2016-7-29
//修正传入的参数宽高错误问题，修改回调函数在子页面的保存方式（改为原生js，在子页面的window对象中添加一个全局变量），以及一些细节修改
//参数宽高好像没有问题，有待确认
//2016-8-3
//修改使能配合crcmodata.js完成弹窗时的父子页面能够管理复选框操作功能



;
(function ($) {
	
	//添加全局变量s
	var CurrentWindow = this;	//获得当前窗口对象
	
	if ($.isWindow(CurrentWindow)) {
			//针对此插件对全局对象的初始化
		if(typeof(CurrentWindow._crcGlobal) == "undefined"){
			//还没有全局变量则附加全局变量并且给全局变量加上CRCModal空的插件
			CurrentWindow._crcGlobal = {crcmodal : {isopen: false, modal: null}};
		}else if(typeof(CurrentWindow._crcGlobal.crcmodal) == "undefined"){
			//有全局变量但是还没有CRCModal插件,加上crcmodal
			CurrentWindow._crcGlobal = $.extend({}, CurrentWindow._crcGlobal, {crcmodal : {isopen: false, modal: null}});
		}else{
			//有全局变量而且已经有了CRCModal插件,则清空已有的CRCModal插件，因为这是针对这个整个页面的CRCModal插件控制对象，所以在这里出现全局变量里已存在CRCModal插件是不正常的
			CurrentWindow._crcGlobal.crcmodal = {isopen: false, modal: null};
		}
		//CurrentWindow._crcGlobal.crcmodal初始化完成。
		//结构是 _crcGlobal.crcmodal = {isopen: false, modal: null};
	} //end test window
	//添加全局变量e
	
	$.extend({  
    crcModal: function()
		{
			if(!CurrentWindow._crcGlobal.crcmodal.isopen){
				
				var defaults = {
					'showmask'	: true,   // 是否显示遮罩. true / false 
					'type'			:	"normal",  // 遮罩颜色，在showmask=true时. [normal:#333,warning:#5E1111,info:#1C2F62]
					'showclose'	: true,	//是否显示显示窗栏上的关闭按钮
					'width'			:	800,  // 宽度 int
					'height'		:	494,  // 高度 int
					'title'			:	'',		//窗体标题
					'scrolling'	: 'auto',	//是否显示滚动条 [auto,no,yes]
					'bgClose'		: false,	//点击遮罩关闭窗口
					'drag'			: true,	//是否可拖动
					'url'				: '',		//iframe的url
					'callback'	: null,	//回调函数
					'contwd'		: null  //用来作为数据关联父子页面时的弹窗时使用
				} //end defaults
				
				var options, url;
				var settings;
				var ttheight = 32; //弹窗标题栏的高度，固定设置
				var callbackey = "pwcallback"; //回调的key名称
				var isIframe = false;
				var dragac = false;
				var cleft = 0;
				var ctop = 0;
				var czi = 999;
				var mcoffsetTop = 0;
				var background;
				var objmodal = null;
				
				//转换成整数
				function myParseInt(v){
					var r, t;
					if($.isNumeric(v)){
						r = parseInt(v,10);
					}else{
						t = parseInt(v,10);
						if($.isNumeric(t))
							{ r = t; }
						else
							{ r = 0; }
					}
					return r;
				};
					
				var uniqID = "modal".concat(Math.floor(Math.random()*99999));
				
				if(arguments.length){
					$.each(arguments, function(i, n){
						//if($.type(n)=='object' && !(n instanceof $)){
						if($.type(n)==='object' && $.isPlainObject(n)){
							options = n;
						}else if($.type(n)=='string'){
							defaults.url = n;
						}else if($.type(n)=='function'){
							defaults.callback = n;
						}else if(n instanceof $){
							defaults.url = n;
						}
					});
				}
				
				if (typeof(options) != "undefined") {
						options.width = myParseInt(options.width);
						options.height = myParseInt(options.height);
						options.height = options.width?options.height?options.height:(options.width*.618):options.height?options.height:defaults.height;
						settings = $.extend({}, defaults, options);
				}
				else {
						settings = $.extend({}, defaults);
				}

				if (settings.type=="normal"){
					background = "#333333";
				}else if (settings.type=="warning"){
					background = "#5E1111";
				}else if (settings.type=="info"){
					background = "#1C2F62";
				}else{
					background = "#333333";
				}
				
				if (!settings.showmask) {
					background = 'transparent';
				}
				
				
	
				var modalBg = $('<div/>').css({
									"position": "absolute",
									"top": 0,
									"left": 0,
									"width": "100%",
									"height": "100%",
									"backgroundColor": background,
									"opacity": 0.5,
									"display": (settings.showmask) ? "block" : "none"
							});
				
				var mcLeft = ($(document).width() - parseInt(settings.width))/2;
				var mcTop = ($(window).height() - parseInt(settings.height))/2;
				
				var modalContent = $('<div/>').css({
									"position": "absolute",
									"backgroundColor": "#ffffff",
									"width": settings.width,
									"height": settings.height,
									"left": (mcLeft < 0 ? 0 : mcLeft),
									"top": (mcTop < 0 ? 0 : mcTop),
									"border": "2px solid #CCCCCC",
									"borderRadius": "3px",
									"boxSizing": "content-box"
							});
	
				var modalContainer = $('<div id="'.concat(uniqID, '" class="m-modal"/>')).css({
									position: "fixed",
									width: "100%",
									height: $(document).height(),
									zIndex: czi,
									left: 0,
									top: 0
							}).append(modalBg).append(modalContent); //确定modalContainer的位置
				
				//使后面的可以点击，并且复原前面的窗口	
				if (!settings.showmask) {
					modalContainer.css("pointer-events", "none");
					modalContent.css("pointer-events", "auto");
				}
				
				var mContentWrap = $('<div/>').css({
									"position": "relative",
									"width": "100%",
									"zIndex": 3
							});
							
				var iframeLayer = $('<div/>').css({
									"display": "none",
									"position": "absolute",
									"top": "0",
									"left": "0",
									"backgroundColor": "#ffffff",
									"opacity": 0.3,
									"zIndex": 2
							});
							
	
				var modalheader;
				if(settings.showclose || settings.title){
					modalheader = $('<div class="u-modal-hd" style="height:'.concat(ttheight,'px;"/>'));
				}
	
				function linkParWinCallback(_ifr, _p){
					
					if(_p.callback){ //回调函数
						_ifr.contentWindow[callbackey] = _p.callback;
					}
					if(_p.contwd){ //父子窗口联动checkboxlist						
						if(typeof(_ifr.contentWindow["_crc_coninit"])!="undefined"){
							(_ifr.contentWindow["_crc_coninit"])({"pk":_p.contwd.pk,"sk":_p.contwd.sk,"ps":_p.contwd.ps,"psdataname":_p.contwd.psdataname,"initdata":_p.contwd.initdata}); //本地测试的时候这里在chrome里会因为跨域的安全性而被拦截.pk是jquery对象，sk是字符串，ps是jquery对象或null,psdataname是字符串，initdata是数组（可能是空数组）
						}
					}
				};
				
				function setIframeDocumentOnLoad(ifr, p) { //参数p是settings
					var oifr;
					if(ifr instanceof $){
						oifr = ifr[0];
					}else{
						oifr = ifr;
					}
					if (oifr.attachEvent) {
						//ie
						oifr.attachEvent('onload',  function(){
							linkParWinCallback(oifr, p);
						});
					} else {
						//!ie
						oifr.onload = function() {
							linkParWinCallback(oifr, p);
						};
					}
				};
				
				var shakemodal = function(){
					for (
						var b = ["#EEEEEE","#E3E3E3","#EEEEEE","#CCCCCC","#F9F9F9","#FFFFFF","#F9F9F9","#CCCCCC"],
						c = function (i) {
							setTimeout(function () {
								modalContent.css("borderColor", b[i]);
							}, 30 * i)
						},
						d = 0; d < b.length; d++){c(d)}
				};
	
				
				var writeModal = function(){
					
					$(window).resize(function(){
						
							var mcL = ($(window).width() - parseInt(settings.width))/2;
							var mcT = ($(window).height() - parseInt(settings.height))/2;
							
							modalContent.css({
									"left": (mcL < 0 ? 0 : mcL),
									"top": (mcT < 0 ? 0 : mcT)
							});
						
							modalBg.css({
									"height": $(document).height()
							})
					});//窗口大小变化时,将弹窗移动到屏幕中间位置
					
					if(settings.title){
						modalheader.append('<h1 class="u-modal-title">'.concat(settings.title, '</h1>'));
					}//标题栏
					
					if(settings.showclose){
						modalheader.append('<div class="u-modal-closebtn"><i title="close">X</i></div>');
					}//关闭按钮
					
					if(modalheader){
						modalContent.append(modalheader);
						//设置拖动
						if(settings.drag){
							modalheader.bind('mousedown', function(e){
								dragac = true;
								cleft = e.pageX - parseInt(modalContent.css("left"));
								ctop = e.pageY - parseInt(modalContent.css("top"));
								modalContent.css("zIndex", czi+1);
								modalContent.addClass('u-modal-hd-active');
								iframeLayer.css("display", "block");
							}).bind('mouseup', function() {
								dragac = false;
								modalContent.css("zIndex", "").removeClass('u-modal-hd-active u-modal-hd-move');
								iframeLayer.css("display", "none");
							}).bind('mouseover', function() {
								modalContent.addClass('u-modal-hd-hover');
							}).bind('mouseout', function() {
								//dragac = false;
								modalContent.css("zIndex", "").removeClass('u-modal-hd-hover');
							}); //end modalheader.mousedown,mouseup,mouseover,mouseout
							$(document).mousemove(function(a) {
								if (dragac) {
									a.preventDefault();
									mcoffsetTop = (a.pageY <= ctop) ? 0 : a.pageY - ctop;
									modalContent.css({"left": a.pageX - cleft, "top": mcoffsetTop}).addClass('u-modal-hd-move');
								}
							});
						}
					}
	
					if ($.type(settings.url)=="string"){
						//字符串
						isIframe = true;
						var modalhdheight = 0;
						if(modalheader){
							modalhdheight = ttheight;
						}
						var coniframeHeight = settings.height-modalhdheight; //边框高度*2
						
						var coniframe = $('<iframe class="u-modal-icontent" name="con-'.concat(
																	uniqID,
																	'" width="100%" height="',
																	coniframeHeight, 
																	'" frameborder="0" scrolling="', 
																	settings.scrolling, 
																	'" src="', 
																	settings.url, 
																	'"></iframe>'
																)
															).css("zIndex", "1");
						
						if(settings.callback || settings.contwd){
							setIframeDocumentOnLoad(coniframe, settings);
						}
						
						//加iframe遮罩
						iframeLayer.css({
							"width" : settings.width,
							"height" : coniframeHeight	
						});
						
						//加外套
						mContentWrap.css({
							"height" : coniframeHeight
						});
						
						mContentWrap.append(iframeLayer).append(coniframe);
	
						modalContent.append(mContentWrap);
						
					}else if (settings.url instanceof $) {
						//jquery 对象
						modalContent.append('<div class="u-modal-hwrap">'.concat((settings.url).html(),'</div>'));
					}else{
						//未知，待处理
					}
					
					modalContainer.prependTo("body");
					
					if(settings.bgClose){
							modalBg.click(function(){
									//modalContent.empty();
									//modalContainer.remove();
									closeModal();
							});
					}else{
						if (!settings.showmask) {
							modalBg.click(function(){
								shakemodal();
							});
						}
					};
					
					$("#".concat(uniqID," .u-modal-closebtn i")).click(function(){
							//$(modalContent).empty();
							//$(modalContainer).remove();
							closeModal();
					});
					
					
	
				}; // end function writeModal
				
				writeModal();
				
				var closeModal = function(){
					$(modalContent).empty();
					$(modalContainer).remove();
					CurrentWindow._crcGlobal.crcmodal.isopen = false;
					CurrentWindow._crcGlobal.crcmodal.modal = null;
					if(settings.contwd){ //如果使用于父子窗口的联动则恢复初始化状态
						if(arguments.length){
							if(typeof(((settings.contwd).pk).data("crcmdtem"))!="undefined")
								{ ((settings.contwd).pk).data("crcmdtem").tv = ((settings.contwd).pk).data("crcmdtem").rv.slice(0); }
						}else{
							if(typeof(((settings.contwd).pk).data("crcmdtem"))!="undefined")
								{ ((settings.contwd).pk).data("crcmdtem").rv = ((settings.contwd).pk).data("crcmdtem").tv.slice(0); }
						}
					}
				};
				
				objmodal = {
					'id'				:	uniqID,
					'iframeid'	:	'con-'.concat(uniqID),
					'close'			:	closeModal,
					'shake'			: shakemodal
				};
				
				//加入全局变量
				CurrentWindow["_crcGlobal"].crcmodal = {
					isopen : true,
					modal : objmodal
				};
				
				return objmodal;
			
			}else{
				//试图在已经存在弹窗时再次弹出闪动提醒
				(CurrentWindow["_crcGlobal"].crcmodal.modal.shake)();
				//直接返回已经存在的对象
				return CurrentWindow["_crcGlobal"].crcmodal.modal;
			}//end test crcmodal.isopen
		} //end crcModal
	}); //end jquery extend
})(jQuery);
