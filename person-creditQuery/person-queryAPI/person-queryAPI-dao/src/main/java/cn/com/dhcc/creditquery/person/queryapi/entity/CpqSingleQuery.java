package cn.com.dhcc.creditquery.person.queryapi.entity;

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

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "cpq_single_query")
public class CpqSingleQuery implements java.io.Serializable {

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
	@Column(name = "QUERIER_NAME", length = 30)
	private String querierName;
	@Column(name = "QUERIER_CERTYPE", length = 2)
	private String querierCertype;
	@Column(name = "QUERIER_CERTNO", length = 18)
	private String querierCertno;
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
	/**
	 *  批量查询批次号
	 */
	@Column(name = "MSG_NO", length = 32)
	private String msgNo;
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