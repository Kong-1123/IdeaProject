package cn.com.dhcc.creditquery.ent.queryweb.vo;

import java.util.Date;

import javax.validation.constraints.NotNull;

import cn.com.dhcc.creditquery.ent.queryweb.rules.QueryReqValidator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@QueryReqValidator
public class QueryReq {

	private String id;
	/**
	 *  企业名称
	 */
	private String enterpriseName;
	/**
	 *  组织机构代码
	 */
	private String orgInstCode;
	/**
	 *  中征码
	 */
	private String signCode;
	/**
	 * 统一社会信用代码
	 */
	private String uniformSocialCredCode;
	/**
	 *  相关联的业务数据
	 */
	private String assocbsnssData;
	/**
	 *  关联档案ID
	 */
	private String autharchiveId;
	/**
	 *  批量标志
	 */
	private String batchFlag;

	/**
	 *  人行登陆认证标识
	 */
	private String certificationMark;
	/**
	 *  渠道编号
	 */
	private String channelId;
	/**
	 *  审批请求ID
	 */
	private String checkId;
	/**
	 *  审批方式
	 */
	private String checkWay;
	/**
	 *  请求客户端IP
	 */
	private String clientIp;

	/**
	 *  征信用户
	 */
	private String creditUser;
	/**
	 * 征信用户密码
	 */
	private String creditPassWord;
	/**
	 *  客户系统编号
	 */
	private String cstmsysId;
	/**
	 *  错误信息
	 */
	private String errorInfo;
	/**
	 *  请求流水号
	 */
	private String flowId;
	/**
	 *  批量查询批次号
	 */
	private String msgNo;
	/**
	 *  操作机构
	 */
	private String operOrg;
	/**
	 *  操作用户
	 */
	private String operator;
	/**
	 *  查询原因
	 */
	@NotNull
	private String qryReason;
	/**
	 *  查询要求时效
	 */
	private String qtimeLimit;
	/**
	 *  查询版式
	 */
	@NotNull
	private String queryFormat;
	/**
	 *  查询人行通信模式
	 */
	private String queryMode;
	/** 金融机构代码
	 * 
	 */
	private String queryOrg;
	/**
	 *  查询时间
	 */
	private Date queryTime;
	/**
	 * 查询类型
	 */
	private String queryType;
	/**
	 *  审批机构
	 */
	private String rekOrg;
	/**
	 *  审批时间
	 */
	private Date rekTime;
	/** 审批用户
	 * 
	 */
	private String rekUser;
	
	/**
	 * 进行查询的法人机构
	 */
	private String topOrgCode;
	
	/** 结果类型
	 * 
	 */
	@NotNull
	private String resultType;
	/**
	 *  信用报告来源
	 */
	private String source;
	/**
	 * 数据状态
	 */
	private String status;
	/**
	 * 操作时间
	 */
	private Date updateTime;
	/**
	 * 信用报告id
	 */
	private String creditId;
	/**
	 * html格式信用报告
	 */
	private String html;
	/**
	 * xml格式信用报告
	 */
	private String xml;
	/**
	 * pdf格式信用报告
	 */
	private String pdf;
	/**
	 * 审批用户密码
	 */
	private String rekPasword;
	/**
	 * 组织机构代码
	 */
	private String corpNo;
	/**
	 * 登记注册号
	 */
	private String frgcorpno;
	/**
	 * 纳税人识别号(国税)
	 */
	private String gsRegiNo;
	/**
	 * 纳税人识别号(地税)
	 */
	private String dsRegiNo;
	/**
	 * 登记注册类型
	 */
	private String regiTypeCode;
	/**档案补录状态
	 * 1-无资料;
	 */
	private String archiveRevise;
	

	private String code;
	private String type;
	
	private String qryReason2;
	private String queryFormat2;
	private String queryType2;
	private String resultType2;
	
	/*========二代相关信息==========*/
	private String reportVersion;
	private String reportFormat;
	private String queryReasonID;
	/**
	 * 接口发起用户
	 */
	private String callSysUser;
	/**
	 * 接入系统审批员
	 */
	private String recheckUserName;
	
	/**
	 * 顶级机构
	 */
	private String topOrg;

}
