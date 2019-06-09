/**
 * [bindSelect 根据页面绑定相关元素元素值展示表单项是否必填]
 * @param  {[Array]} arr [存储相关数据对象]
 * @param  {[Object]} jsonData [修改页面服务器返回数据对象]
 * @return {[type]}     [description]
 */
function bindSelect(arr,jsonData){
    $.each(arr,function(i,e){
        var target = $('input[name="'+ e.name +'"]');
        var star = '<span class="star" id="starSpan">*</span>'; 
        var className =""
        if(jsonData){
            if(e.className){
                className = e.className;
            }

            if($("#"+e.id).val() == 0){
                target.attr('class','validate[required'+ e.validate +'] '+ className +' layui-input');
                removeReadonly([e.name]);
                target.removeAttr('style');
                if(target.parents('li').find('.formLabel .star').length < 1){
                    target.parents('li').find('.formLabel').append(star);
                }
            }else{
                target.attr('class',className +' layui-input');
                setReadonly([e.name]);
                target.attr('style','border-color:rgb(222, 222, 222)!important;');
                target.parents('li').find('.formLabel .star').remove();
                target.val('');
            }
        }
        layui.form.on('select('+ e.id +')', function(data){
            if(data.value == 0){//判断是否限制查询量
                target.attr('class','validate[required'+ e.validate +'] '+ className +' layui-input');
                removeReadonly([e.name]);
                target.removeAttr('style');
                if(target.parents('li').find('.formLabel .star').length < 1){
                    target.parents('li').find('.formLabel').append(star);
                }
            }else{
                target.attr('class','layui-input');
                setReadonly([e.name]);
                target.attr('style','border-color:rgb(222, 222, 222)!important;');
                target.parents('li').find('.formLabel .star').remove();
                target.val('');
            }
      });
    })
}
