/**layui**/
layui.use(['form', 'layedit', 'laydate'], function() {
	var form = layui.form,
		layer = layui.layer,
		layedit = layui.layedit,
		laydate = layui.laydate;
	 laydate.render({
		    elem: '.dateRange',
		    range: true
		  });
	 laydate.render({
		    elem: '.timeRange',
		    range: true,
		    type:"time"
		  });
	 laydate.render({
	    elem: '.layui-date' //指定元素
	  });
	 
		/*laydate.render({ 
			
		elem : '.date',
		format: 'yyyy-MM-dd'
	  });*/
		$('.date').each(function() {
			laydate.render({
				elem: this,
				position: 'fixed',
				format: 'yyyy-MM-dd'
			});
		});
	 	$('.month').each(function() {
			laydate.render({
				elem: this,
				position: 'fixed',
				format: 'yyyy-MM-dd',
				type: 'month'
			});
		});
		$('.year').each(function() {
			laydate.render({
				elem: this,
				position: 'fixed',
				format: 'yyyy-MM-dd',
				type: 'year'
			});
		});
		$('.dateTime').each(function() {
			laydate.render({
				elem: this,
				position: 'fixed',
				format: 'yyyy-MM-dd HH:mm:ss',
				type: 'datetime'
			});
		});
		/*$('.time').each(function() {
			laydate.render({
				elem: this,
				position: 'fixed',
				format: 'HH:mm',
				type: 'time'
			});
		});*/
	//日期
	/*$('.time').each(function() {
		laydate.render({
			elem: this,
			position: 'fixed',
			format: 'yyyy-MM-dd'
		});
	});*/
});
/**layui**/