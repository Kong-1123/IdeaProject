package cn.com.dhcc.creditquery.person.queryweb.webservice.consts;

public enum AccessTokenResult {
	
	
	SUCCESS("0100150","获取token成功",1),INVALID_PARAM("0100151","客户系统ID或密钥为空",-1),CLIENT_STOPPED("0100152","客户系统已停用",2),ERROR_FOR_CODE("0100153","获取授权码错误",3),FAIL_FOR_ACCESS_TOKEN("0100154","获取token失败",4),FAIL_FOR_REFRESH_TOKEN("0100155","刷新token失败",5),OTHER("0100156","未知错误",6);
	
	private String code;
	private String msg;
	private int relativeCode;
	
	private AccessTokenResult(String code, String msg,int relativeCode) {
		this.code = code;
		this.msg = msg;
		this.relativeCode = relativeCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getRelativeCode() {
		return relativeCode;
	}

	public void setRelativeCode(int relativeCode) {
		this.relativeCode = relativeCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
