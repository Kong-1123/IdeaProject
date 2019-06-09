;(function($){
	$.extend({
		inputStyle:function(){
			function check(el,cl){
				$(el).each(function(){
					$(this).parent('i').removeClass(cl);

					var checked = $(this).prop('checked');
					if(checked){
						$(this).parent('i').addClass(cl);
					}
				})
			}	
			$('input[type="radio"]').on('click',function(){
				check('input[type="radio"]','radio_bg_check');
			})
			$('input[type="checkbox"]').on('click',function(){
				check('input[type="checkbox"]','checkbox_bg_check');
			})
		}
		
	})

})(jQuery)

//调用
$(function(){
	$.inputStyle();
})

/**复选框**/
$(document).ready(function() {
	$('input').iCheck({
		checkboxClass: 'icheckbox_square-blue', // 注意square和blue的对应关系,用于type=checkbox
		radioClass: 'iradio_square-blue', // 用于type=radio
		increaseArea: '20%' // 增大可以点击的区域
	});
});

/**复选框**/
