package cn.com.dhcc.creditquery.ent.queryweb.vo;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <违规用户查询统计VO类>
 * @author Mingyu.Li
 * @date 2018年3月23日 
 * 
 */
public class ReleventVO implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1634082156475267774L;
	
	
	private String deptCode;
	private String C0000;
	private String C0001;
	private String C0002;
	private String C0003;
	private String C0004;
	private String C0005;
	private String C0006;
	private String C0007;
	private String C0008;
	private String C0009;
	private String C0010;

	public ReleventVO() {
		super();
	}
	

    /**
     * @param deptCode
     * @param c0000
     * @param c0001
     * @param c0002
     * @param c0003
     * @param c0004
     * @param c0005
     * @param c0006
     * @param c0007
     * @param c0008
     * @param c0009
     * @param c0010
     */
    public ReleventVO(String deptCode, String c0000, String c0001, String c0002, String c0003, String c0004, String c0005, String c0006, String c0007, String c0008, String c0009, String c0010) {
        super();
        this.deptCode = deptCode;
        C0000 = c0000;
        C0001 = c0001;
        C0002 = c0002;
        C0003 = c0003;
        C0004 = c0004;
        C0005 = c0005;
        C0006 = c0006;
        C0007 = c0007;
        C0008 = c0008;
        C0009 = c0009;
        C0010 = c0010;
    }



    /**
     * @return the deptCode
     */
    public String getDeptCode() {
        return deptCode;
    }


    /**
     * @param deptCode the deptCode to set
     */
    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }


    /**
     * @return the c0000
     */
    public String getC0000() {
        return C0000;
    }


    /**
     * @param c0000 the c0000 to set
     */
    public void setC0000(String c0000) {
        C0000 = c0000;
    }


    /**
     * @return the c0001
     */
    public String getC0001() {
        return C0001;
    }


    /**
     * @param c0001 the c0001 to set
     */
    public void setC0001(String c0001) {
        C0001 = c0001;
    }


    /**
     * @return the c0002
     */
    public String getC0002() {
        return C0002;
    }


    /**
     * @param c0002 the c0002 to set
     */
    public void setC0002(String c0002) {
        C0002 = c0002;
    }


    /**
     * @return the c0003
     */
    public String getC0003() {
        return C0003;
    }


    /**
     * @param c0003 the c0003 to set
     */
    public void setC0003(String c0003) {
        C0003 = c0003;
    }


    /**
     * @return the c0004
     */
    public String getC0004() {
        return C0004;
    }


    /**
     * @param c0004 the c0004 to set
     */
    public void setC0004(String c0004) {
        C0004 = c0004;
    }


    /**
     * @return the c0005
     */
    public String getC0005() {
        return C0005;
    }


    /**
     * @param c0005 the c0005 to set
     */
    public void setC0005(String c0005) {
        C0005 = c0005;
    }


    /**
     * @return the c0006
     */
    public String getC0006() {
        return C0006;
    }


    /**
     * @param c0006 the c0006 to set
     */
    public void setC0006(String c0006) {
        C0006 = c0006;
    }


    /**
     * @return the c0007
     */
    public String getC0007() {
        return C0007;
    }


    /**
     * @param c0007 the c0007 to set
     */
    public void setC0007(String c0007) {
        C0007 = c0007;
    }


    /**
     * @return the c0008
     */
    public String getC0008() {
        return C0008;
    }


    /**
     * @param c0008 the c0008 to set
     */
    public void setC0008(String c0008) {
        C0008 = c0008;
    }


    /**
     * @return the c0009
     */
    public String getC0009() {
        return C0009;
    }


    /**
     * @param c0009 the c0009 to set
     */
    public void setC0009(String c0009) {
        C0009 = c0009;
    }


    /**
     * @return the c0010
     */
    public String getC0010() {
        return C0010;
    }


    /**
     * @param c0010 the c0010 to set
     */
    public void setC0010(String c0010) {
        C0010 = c0010;
    }


    public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
		
	}

}
