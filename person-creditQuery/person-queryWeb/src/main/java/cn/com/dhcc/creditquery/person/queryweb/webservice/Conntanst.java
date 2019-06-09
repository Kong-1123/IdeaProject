package cn.com.dhcc.creditquery.person.queryweb.webservice;

/**
 * @author lekang.liu
 * @date 2018年3月29日
 * @README 查询接口return CODE 共7位，其意义如下：
 *         <h1>前两位</h1>标识系统分类，00为平台系统，01为监管业务系统...
 *         <h1>中两位</h1>标识返回信息来源，00为监管查询处理，01为人行查询处理...
 *         <h1>后三位</h1>标识返回信息类型及编号；其中，第一位表示信息级别，0为成功，1为业务校验，2为系统级异常...
 *         后两位为信息编号由00开始依次排序。
 * 
 */
public class Conntanst {

	public static String CODE_SUSSCE = "0100000";
	public static String MSG_SUSSCE = "查询操作成功";
	public static String CODE_NOSYS = "0100101";
	public static String MSG_NOSYS = "客户系统标识认证失败";
	public static String CODE_VLIDATA = "0100102";
	public static String MSG_VLIDATA = "数据校验失败";
	public static String CODE_EXC = "0100200";
	public static String MSG_EXC = "接口处理异常";
	public static String CODE_NON_WORK_DAY = "0100102";
	public static String MSG_NON_WORK_DAY = "非工作日、工作时间禁止查询";
	public static String CODE_QUERY_QUANTITY_BAN = "0100103";
	public static String MSG_QUERY_QUANTITY_BAN  = "当日查询量已满，禁止查询";
	public static String CODE_USER_NONEXISTENT = "0100104";
	public static String MSG_USER_NONEXISTENT  = "查询用户不存在";
	public static String CODE_USER_IS_NULL = "0100105";
	public static String MSG_USER_IS_NULL  = "查询用户为空";
	public static String CODE_USER_IS_LOCKED = "0100106";
	public static String MSG_USER_IS_LOCKED  = "用户被锁定";
	public static String CODE_USER_IS_STOP = "0100106";
	public static String MSG_USER_IS_STOP  = "用户被停用";
	public static String CODE_USER_QUERY_ROLE = "0100107";
	public static String MSG_USER_QUERY_ROLE  = "用户无接口查询权限";
	public static String CODE_USER_QUERY_NOCREDIT = "0100108";
	public static String MSG_USER_QUERY_NOCREDIT  = "用户未配置征信用户";
	public static String CODE_FILE_NOT_FOND = "0100109";
	public static String MSG_FILE_NOT_FOND  = "档案文件不存在";
	public static String CODE_BATCHFILE_NOT_FOND = "0100111";
	public static String MSG_BATCHFILE_NOT_FOND  = "批量文件不存在";
	public static String CODE_BATCHFILE_FILEMODE = "0100112";
	public static String MSG_BATCHFILE_FILEMODE  = "批量文件格式错误";
	public static String CODE_BATCHQUERY_USERDIFFER = "0100113";
	public static String MSG_BATCHQUERY_USERDIFFER  = "结果获取用户与请求用户不一致";
	
	public static String CODE_USER_NOT_USE = "0100110";
	public static String MSG_USER_NOT_USE  = "用户不可用";
	
	public static String CODE_NOT_REPORT = "0100114";
	public static String MSG_NOT_REPORT  = "未查得信用报告";
	
	public static String CODE_VALIDATE = "0100115";
    public static String MSG_VALIDATE  = "异常规则验证未通过";
	
    public static String CODE_NOT_TRACE_REPORT = "0100116";
	public static String MSG_NOT_TRACE_REPORT  = "查无此人";

	public static String CODE_USER_IS_LOCKED_BUSINESS = "0100117";
	public static String MSG_USER_IS_LOCKED_BUSINESS  = "用户因为个人业务被锁定";

}
