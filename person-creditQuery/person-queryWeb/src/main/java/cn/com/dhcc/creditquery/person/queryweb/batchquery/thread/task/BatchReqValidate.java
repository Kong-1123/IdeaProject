/*
package cn.com.dhcc.creditquery.person.queryweb.batchquery.thread.task;

import java.util.List;

import cn.com.dhcc.creditquery.person.queryweb.batchquery.thread.task.bean.BatchErrorBean;
import cn.com.dhcc.creditquery.person.queryweb.batchquery.thread.task.bean.BatchReqBean;
import cn.com.dhcc.creditquery.person.queryweb.util.ResultBeans;
import cn.com.dhcc.creditquery.person.queryweb.util.ValidateUtil;

*/
/**
 * 
 * @author lekang.liu
 * @date 2018年7月24日
 *//*

public class BatchReqValidate {

	*/
/**
	 * 校验请求，进行请求分类
	 * 
	 * @Title: validate
	 * @param preReqList
	 *            需进行校验的请求
	 * @param reqList
	 *            存放校验成功的请求list
	 * @param errorList
	 *            存放校验失败的请求list
	 * @return void 返回类型
	 *//*

	public static void validate(List<BatchReqBean> preReqList, List<BatchReqBean> reqList, List<BatchErrorBean> errorList) {
		for (BatchReqBean batchreqbean : preReqList) {
			ResultBeans validate = ValidateUtil.validate(batchreqbean);
			if (null != validate) {
				BatchErrorBean batchErrorBean = new BatchErrorBean();
				batchErrorBean.setContent(batchreqbean.toString());
				batchErrorBean.setErrorinfo(validate.getMsg());
				batchErrorBean.setErrorcode(validate.getCode());
				errorList.add(batchErrorBean);
			}else{
				reqList.add(batchreqbean);
			}

		}

	}

}
*/
