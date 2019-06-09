package cn.com.dhcc.creditquery.person.queryweb.webservice.consts;

public enum GetBatchResult {
	
	
	SUCCESS("0100160","获取批量成功"),INVALID_PARAM("0100161","批次号或token为空"),VALIDATE_TOKEN_FAILED("0100162","token校验不通过"),
	NOT_COMPLETED("0100163","该批次不存在或尚未处理完毕");
	
	private String code;
	private String msg;
	
	private GetBatchResult(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
