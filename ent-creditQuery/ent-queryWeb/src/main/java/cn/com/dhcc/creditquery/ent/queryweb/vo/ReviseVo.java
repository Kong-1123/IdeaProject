package cn.com.dhcc.creditquery.ent.queryweb.vo;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import cn.com.dhcc.creditquery.ent.query.bo.authorizemanager.CeqAuthorizeFileBo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ReviseVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5300149698721714707L;
	private String archiveRevise;
	private String resultInfoId;
	private String id;
	private String archiveid;
	private Date createtime;
	private String fileDescribe;
	private String filename;
	private String filepath;
	private String operator;
	private String operorg;
	private Date opertime;
	private String paperFileId;
	private String status;
	private String fileType;
	
	public CeqAuthorizeFileBo getCeqAuthorizeFileBo(){
		return new CeqAuthorizeFileBo(id, archiveid, createtime, fileDescribe, filename, filepath, operator, operorg, opertime, paperFileId, status,fileType);
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
}
