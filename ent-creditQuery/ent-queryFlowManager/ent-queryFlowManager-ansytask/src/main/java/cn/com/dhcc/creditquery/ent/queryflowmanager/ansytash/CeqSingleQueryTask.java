package cn.com.dhcc.creditquery.ent.queryflowmanager.ansytash;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
/**
 * 
 * 企业单笔请求报文实体
 * @author yuzhao.xue
 * @date 2019年3月18日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "ceq_single_query")
public class CeqSingleQueryTask implements java.io.Serializable {

	// Fields
	/**
	 * 
	 */
	private static final long serialVersionUID = -8565475985765707811L;
	
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	private String id;
	
	/**
	 * 企业名称
	 */
	@Column(name = "CLIENT_NAME")
	private String enterpriseName;
	/**
	 * 中征码
	 */
	@Column(name = "LOANCARD_CODE")
	private String signCode;
	/**
	 * 统一社会信用代码
	 */
	@Column(name = "US_CREDIT_CODE")
	private String uniformSocialCredCode;
	/**
	 * 组织机构代码
	 */
	@Column(name = "COPR_CODE")
	private String orgInstCode;
	/**
	 * 机构信用代码
	 */
	@Column(name = "ORG_CREDIT_CODE")
	private String orgCreditCode;
	
	/**
	 * 纳税人识别号(国税)
	 */
	@Column(name = "GS_REGI_CODE")
	private String gsRegiNo;
	
	/**
	 * 纳税人识别号(地税)
	 */
	@Column(name = "DS_REGI_CODE")
	private String dsRegiNo;
	
	/**
	 * 登记注册类型
	 */
	@Column(name = "REGI_TYPE_CODE")
	private String regiTypeCode;
	
	/**
	 * 登记注册号
	 */
	@Column(name = "FRGCORPNO")
	private String frgcorpno;
	
	
	@Column(name = "QUERY_REASON", length = 2)
	private String queryReason;
	@Column(name = "QUERY_FORMAT", length = 2)
	private String queryFormat;
	@Column(name = "REPORT_TYPE", length = 2)
	private String reportType;
	@Column(name = "QUERY_TYPE", precision = 5, scale = 0)
	private Integer queryType;
	@Column(name = "SYNC_FLAG", length = 1)
	private String syncFlag;
	@Column(name = "ASYNC_QUERY_FLAG", length = 1)
	private String asyncQueryFlag;
	@Column(name = "BUSINESS_LINE", length = 2)
	private String businessLine;
	@Column(name = "SYS_CODE", length = 20)
	private String sysCode;
	@Column(name = "QUERY_USER", length = 30)
	private String queryUser;
	@Column(name = "CALL_SYS_USER", length = 30)
	private String callSysUser;
	@Column(name = "BUSSID", length = 64)
	private String bussid;
	@Column(name = "RECHECK_USER", length = 30)
	private String recheckUser;
	@Column(name = "USER_IP", length = 15)
	private String userIp;
	@Column(name = "USER_MAC", length = 17)
	private String userMac;
	@Column(name = "REPORT_VERSION", length = 5)
	private String reportVersion;
	@Column(name = "REQ_ID", length = 32)
	private String reqId;
	private String status;
	
	@Column(name = "EXT1", length = 60)
	private String ext1;

	@Column(name = "EXT2", length = 60)
	private String ext2;

	@Column(name = "EXT3", length = 60)
	private String ext3;

	@Column(name = "EXT4", length = 60)
	private String ext4;

	@Column(name = "EXT5", length = 60)
	private String ext5;

	@Column(name = "EXT6", length = 60)
	private String ext6;

	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
		
	}
	

}