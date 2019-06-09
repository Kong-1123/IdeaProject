/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.ent.authorizemanager.entity;

import java.io.Serializable;
import java.util.Date;

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
 * 授权文件实体类
 * @author sjk
 * @date 2019年2月22日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="CEQ_ARCHIVEFILES")
public class CeqAuthorizeFile implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 档案资料ID
	 */
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	private String id;
	
	/**
	 * 关联档案ID
	 */
	@Column(name="ARCHIVE_ID")
	private String archiveId;
	
	/**
	 * 档案资料描述
	 */
	@Column(name="FILE_DESCRIBE")
	private String fileDescribe;
	
	/**
	 * 纸质档案编号
	 */
	@Column(name="PAPERFILE_ID")
	private String paperFileId;
	
	/**
	 * 文件名
	 */
	@Column(name="FILE_NAME")
	private String fileName;
	
	/**
	 * 档案文件路径
	 */
	@Column(name="FILE_PATH")
	private String filePath;
	
	/**
	 * 档案资料状态(1-有效，2-无资料)
	 */
	@Column(name="STATUS")
	private String status;
	
	/**
	 * 操作用户
	 */
	@Column(name="OPERATOR")
	private String operator;
	
	/**
	 * 操作机构
	 */
	@Column(name="OPER_ORG")
	private String operOrg;
	
	/**
	 * 操作时间
	 */
	@Column(name="OPER_TIME")
	private Date operTime;
	
	/**
	 * 创建时间
	 */
	@Column(name="CREATE_TIME")
	private Date createTime;
	
	/**
	 * 文件类型
	 */
	@Column(name="FILE_TYPE")
	private String fileType;
	
	/**
	 * 影像系统url
	 */
	@Column(name="IMAGE_SYS_URL")
	private String imageSysUrl;
	
	/**
	 * EXT1 - EXT6 
	 * *预留字段
	 */
	@Column(name = "EXT1",length=60)
	private String ext1;

	@Column(name = "EXT2",length=60)
	private String ext2;

	@Column(name = "EXT3",length=60)
	private String ext3;

	@Column(name = "EXT4",length=60)
	private String ext4;

	@Column(name = "EXT5",length=60)
	private String ext5;

	@Column(name = "EXT6",length=60)
	private String ext6;

	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
}
