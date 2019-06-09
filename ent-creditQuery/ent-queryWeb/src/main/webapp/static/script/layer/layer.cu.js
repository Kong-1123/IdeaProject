/**
 * [layerMsg 单行提示信息]
 * @param  {[String]}   txt      [提示信息文本内容]
 * @param  {Function} callback [回调函数]
 */
function layerMsg(txt,callback){
	layer.msg(txt, {
		  icon: 0,
		  zIndex: layer.zIndex,
		  time: 3000 //2秒关闭（如果不配置，默认是3秒）
	},callback);
}

/**
 * [layerAlert 带提示信息和确认按钮的弹框]
 * @param  {[String]}   txt      [提示信息文本内容]
 * @param  {Function} callback [回调函数]
 */
function layerAlert(txt,callback){
	layer.alert(txt,{zIndex: layer.zIndex},
	    function(index){
			if(callback!= undefined){
				callback();
			}
			layer.close(index);
	});
}

/**
 * [layerConfirm 带提示信息和确认与取消按钮的弹框]
 * @param  {[String]}   txt      [提示信息文本内容]
 * @param  {Function} callback [回调函数]
 */
function layerConfirm(txt,callback){
	layer.confirm(txt, {
	    btn: ['确认','取消'], //按钮
	    yes:function(index){
	    	if(callback!= undefined){
	    		callback();
	    	}
		    layer.close(index); //如果设定了yes回调，需进行手工关闭
		  },
		cancel:function(index){
		    layer.close(layer.index); //如果设定了yes回调，需进行手工关闭
		}
	});
}

function layerConfirmForReloadParentPage(txt,callback){
	layer.confirm(txt, {
	    btn: ['确认','取消'],
	    cancel:function(index){
	    	reloadParentPage();
	    }}, //按钮
	    function(index){
	    	if(callback!= undefined){
	    		callback();
	    	}
		    layer.close(index); //如果设定了yes回调，需进行手工关闭
		  },
		function(index){
			reloadParentPage();
		}
		
	);
}


/**
 * [layerPrompt 带输入框和确认与取消按钮的弹框]
 * @param  {[String]}   txt      [提示信息文本内容]
 * @param  {Function} callback [回调函数]
 */
function layerPrompt(txt,callback){
	layer.prompt({
	  formType: 0,
	  value: "",
	  title: txt,
	  area: ['200px', '60px'] //自定义文本域宽高
	}, function(value, index, elem){
		if(callback!= undefined){
			callback(value, index, elem);
		}
	  	layer.close(index);
	});
}

/**
 * [layerOpen 弹窗包含一个页面或一个jquery对象]
 * @param  {[Number]} w       [弹窗宽度]
 * @param  {[Numbe]} h       [弹窗高度]
 * @param  {[String]} title   [弹窗标题名称]
 * @param  {[Object/String]} content [jquery对象/URL]
 * @param  {[Number]} type    [类型，1：content为jquery对象，2：content为url。]
 * @param  {[String/Boolean]} callback    [成功时的回调函数]
 * @param  {[Boolean]} closeFlag    [关闭是否刷新页面，true为不刷新]
 * @return {[Number]}  index   [当前弹出层所在z-index数值]
 */
function layerOpen(w,h,title,content,type,callback,closeFlag){
	var layerOpt ={
	        type: type  // 1：content为jquery对象，2：content为url。
	        ,title: title
	        ,area: [w+"px", h+"px"]
	        ,shade: [0.8, '#393D49']
	        // ,shade: false
	     /*   ,offset: [ 
	          ($(window).height()-h)/2
	          ,($(window).width()-w)/2
	        ] */
	        ,maxmin: true
	        ,content: content
	        ,zIndex : 19891014
	        // ,zIndex: layer.zIndex //重点1
	        ,success: function(layero){
	          	layer.setTop(layero); // 置顶当前窗口
	          	if(callback){
	          		callback();
	          	}
	        }
	        ,cancel:function(index){
	        	if(typeof content == "object"){
	        		// content.remove();console.log(closeFlag);
	        		$(".layui-layer,.layui-layer-shade").remove();
	        		layer.close(index);
	        		if(!closeFlag){
	        			closePage();
	        		};
	        	};
	        }
    };
    var index = layer.open(layerOpt);
    return index;
}

/**
 * [layerTree 弹窗机构树或行业树]
 * @param  {[Number]}         w         [弹窗宽度]
 * @param  {[Numbe]}          h         [弹窗高度]
 * @param  {[String]}         title     [弹窗标题名称]
 * @param  {[Object/String]}  content   [jquery对象/URL]
 * @param  {[Number]}         type      [类型，1：content为jquery对象，2：content为url。]
 * @param  {[String/Boolean]} callback  [成功时的回调函数]
 * @param  {[String/Boolean]} cancel    [点击确认按钮时的回调函数]
 * @return {[Number]}         index     [当前弹出层所在z-index数值]
 */
function layerTree(w,h,title,content,type,callback,cancel){
	var layerOpt ={
	        type: type  // 1：content为jquery对象，2：content为url。
	        ,title: title
	        ,area: [w+"px", h+"px"]
	        ,shade: [0.8, '#393D49']
	        // ,shade: false
	      /*  ,offset: [ 
	          ($(window).height()-h)/2
	          ,($(window).width()-w)/2
	        ] */
	        // ,maxmin: true
	        ,content: content
	        ,btn: ['确定']
	        ,zIndex : 19891014
	        // ,zIndex: layer.zIndex //重点1
	        ,success: function(layero){
	          	layer.setTop(layero); // 置顶当前窗口
	          	if(callback){
	          		callback();
	          	}
	        }
	        ,yes:function(index,layero){
	        	if(typeof content == "object"){
	        		content.hide();
	        		cancel();
	        	};
	        	layer.close(index);
	        }
	        ,cancel:function(){
	        	if(typeof content == "object"){
	        		content.hide();
	        		cancel();
	        	};
	        }
    };
    var index = layer.open(layerOpt);
    return index;
}
/**
 * [layerLoading 弹出loading层]
 */
function layerLoading(){
		layer.load(2, {time: 10*1000});
}

/**
 * [layerCloseLoad 关闭loading层]
 */
function layerCloseLoad(){
	layer.closeAll('loading');
}
