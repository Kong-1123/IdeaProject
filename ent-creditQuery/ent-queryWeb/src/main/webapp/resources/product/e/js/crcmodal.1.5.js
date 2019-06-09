///**
// * crcModal v1.5.0
// * Created by yc on 2015/9/27.
// * 1.����Ƿ���϶����ԣ��Խ�������ڽ�С��ʾ�����޷�����������
// * 2.�޸Ĵ������Ϊ�Զ��жϡ� ([options:object,����][,url:string,iframe��ַ][callback:function,�ص�����])
// * 3.�޸Ĵ���boxSizing��ʽ�����ⱻbootstarp��Ĭ����ʽ���Ƕ����´������ݼ������

// * 4.2016-5-19�޸�������ò���ʾ���ֵ�ʱ����Ե����������ҳ�����ݡ�
// */
// options, url, wfun

// ��1.4����showmask=falseʱ����������������ٴε������ʱ�������⡣
// ����ȫ�ֱ�������ҳ�����е���ʱ�����µ�������
// 2016-5-23
// {crcmodal : {isopen: false, modal: null}};
// isopen	: [true/false] ��ǰ�Ƿ�����ʾ�ĵ���
// modal	: [object] �������󡣺���close����

//v1.4.1
//2016-5-31
//�޸Ĳ��Բ����Ŀ��ֵ���淶Ϊ����

//v1.5.0
//2016-7-29
//��������Ĳ�����ߴ������⣬�޸Ļص���������ҳ��ı��淽ʽ����Ϊԭ��js������ҳ���window���������һ��ȫ�ֱ��������Լ�һЩϸ���޸�
//������ߺ���û�����⣬�д�ȷ��
//2016-8-3
//�޸�ʹ�����crcmodata.js��ɵ���ʱ�ĸ���ҳ���ܹ�����ѡ���������



;
(function ($) {
	
	//���ȫ�ֱ���s
	var CurrentWindow = this;	//��õ�ǰ���ڶ���
	
	if ($.isWindow(CurrentWindow)) {
			//��Դ˲����ȫ�ֶ���ĳ�ʼ��
		if(typeof(CurrentWindow._crcGlobal) == "undefined"){
			//��û��ȫ�ֱ����򸽼�ȫ�ֱ������Ҹ�ȫ�ֱ�������CRCModal�յĲ��
			CurrentWindow._crcGlobal = {crcmodal : {isopen: false, modal: null}};
		}else if(typeof(CurrentWindow._crcGlobal.crcmodal) == "undefined"){
			//��ȫ�ֱ������ǻ�û��CRCModal���,����crcmodal
			CurrentWindow._crcGlobal = $.extend({}, CurrentWindow._crcGlobal, {crcmodal : {isopen: false, modal: null}});
		}else{
			//��ȫ�ֱ��������Ѿ�����CRCModal���,��������е�CRCModal�������Ϊ��������������ҳ���CRCModal������ƶ����������������ȫ�ֱ������Ѵ���CRCModal����ǲ�������
			CurrentWindow._crcGlobal.crcmodal = {isopen: false, modal: null};
		}
		//CurrentWindow._crcGlobal.crcmodal��ʼ����ɡ�
		//�ṹ�� _crcGlobal.crcmodal = {isopen: false, modal: null};
	} //end test window
	//���ȫ�ֱ���e
	
	$.extend({  
    crcModal: function()
		{
			if(!CurrentWindow._crcGlobal.crcmodal.isopen){
				
				var defaults = {
					'showmask'	: true,   // �Ƿ���ʾ����. true / false 
					'type'			:	"normal",  // ������ɫ����showmask=trueʱ. [normal:#333,warning:#5E1111,info:#1C2F62]
					'showclose'	: true,	//�Ƿ���ʾ��ʾ�����ϵĹرհ�ť
					'width'			:	800,  // ��� int
					'height'		:	494,  // �߶� int
					'title'			:	'',		//�������
					'scrolling'	: 'auto',	//�Ƿ���ʾ������ [auto,no,yes]
					'bgClose'		: false,	//������ֹرմ���
					'drag'			: true,	//�Ƿ���϶�
					'url'				: '',		//iframe��url
					'callback'	: null,	//�ص�����
					'contwd'		: null  //������Ϊ���ݹ�������ҳ��ʱ�ĵ���ʱʹ��
				} //end defaults
				
				var options, url;
				var settings;
				var ttheight = 32; //�����������ĸ߶ȣ��̶�����
				var callbackey = "pwcallback"; //�ص���key����
				var isIframe = false;
				var dragac = false;
				var cleft = 0;
				var ctop = 0;
				var czi = 999;
				var mcoffsetTop = 0;
				var background;
				var objmodal = null;
				
				//ת��������
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
							}).append(modalBg).append(modalContent); //ȷ��modalContainer��λ��
				
				//ʹ����Ŀ��Ե�������Ҹ�ԭǰ��Ĵ���	
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
					
					if(_p.callback){ //�ص�����
						_ifr.contentWindow[callbackey] = _p.callback;
					}
					if(_p.contwd){ //���Ӵ�������checkboxlist						
						if(typeof(_ifr.contentWindow["_crc_coninit"])!="undefined"){
							(_ifr.contentWindow["_crc_coninit"])({"pk":_p.contwd.pk,"sk":_p.contwd.sk,"ps":_p.contwd.ps,"psdataname":_p.contwd.psdataname,"initdata":_p.contwd.initdata}); //���ز��Ե�ʱ��������chrome�����Ϊ����İ�ȫ�Զ�������.pk��jquery����sk���ַ�����ps��jquery�����null,psdataname���ַ�����initdata�����飨�����ǿ����飩
						}
					}
				};
				
				function setIframeDocumentOnLoad(ifr, p) { //����p��settings
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
					});//���ڴ�С�仯ʱ,�������ƶ�����Ļ�м�λ��
					
					if(settings.title){
						modalheader.append('<h1 class="u-modal-title">'.concat(settings.title, '</h1>'));
					}//������
					
					if(settings.showclose){
						modalheader.append('<div class="u-modal-closebtn"><i title="close">X</i></div>');
					}//�رհ�ť
					
					if(modalheader){
						modalContent.append(modalheader);
						//�����϶�
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
						//�ַ���
						isIframe = true;
						var modalhdheight = 0;
						if(modalheader){
							modalhdheight = ttheight;
						}
						var coniframeHeight = settings.height-modalhdheight; //�߿�߶�*2
						
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
						
						//��iframe����
						iframeLayer.css({
							"width" : settings.width,
							"height" : coniframeHeight	
						});
						
						//������
						mContentWrap.css({
							"height" : coniframeHeight
						});
						
						mContentWrap.append(iframeLayer).append(coniframe);
	
						modalContent.append(mContentWrap);
						
					}else if (settings.url instanceof $) {
						//jquery ����
						modalContent.append('<div class="u-modal-hwrap">'.concat((settings.url).html(),'</div>'));
					}else{
						//δ֪��������
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
					if(settings.contwd){ //���ʹ���ڸ��Ӵ��ڵ�������ָ���ʼ��״̬
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
				
				//����ȫ�ֱ���
				CurrentWindow["_crcGlobal"].crcmodal = {
					isopen : true,
					modal : objmodal
				};
				
				return objmodal;
			
			}else{
				//��ͼ���Ѿ����ڵ���ʱ�ٴε�����������
				(CurrentWindow["_crcGlobal"].crcmodal.modal.shake)();
				//ֱ�ӷ����Ѿ����ڵĶ���
				return CurrentWindow["_crcGlobal"].crcmodal.modal;
			}//end test crcmodal.isopen
		} //end crcModal
	}); //end jquery extend
})(jQuery);
