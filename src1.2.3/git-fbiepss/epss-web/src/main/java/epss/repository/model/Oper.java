package epss.repository.model;

import org.primefaces.model.UploadedFile;
import skyline.platform.db.AbstractBasicBean;
import skyline.platform.db.RecordSet;
import java.util.List;

public class Oper extends AbstractBasicBean implements Cloneable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.OPER.PKID
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    private String pkid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.OPER.TID
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    private String tid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.OPER.DEPT_PKID
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    private String deptPkid;
    private Dept dept;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.OPER.ID
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.OPER.NAME
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.OPER.PASSWD
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    private String passwd;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.OPER.ISSUPER
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    private String issuper;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.OPER.DUTY
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    private String duty;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.OPER.TYPE
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    private String type;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.OPER.SEX
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    private String sex;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.OPER.EMAIL
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    private String email;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.OPER.MOBILEPHONE
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    private String mobilephone;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.OPER.PHONE
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    private String phone;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.OPER.OTHERPHONE
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    private String otherphone;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.OPER.ENABLED
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    private String enabled;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.OPER.ARCHIVED_FLAG
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    private String archivedFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.OPER.CREATED_BY
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    private String createdBy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.OPER.CREATED_TIME
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    private String createdTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.OPER.LAST_UPD_BY
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    private String lastUpdBy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.OPER.LAST_UPD_TIME
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    private String lastUpdTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.OPER.REMARK
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    private String remark;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.OPER.REC_VERSION
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    private Integer recVersion;

    private String sessionKey;
	private String attachment;
	private UploadedFile file;

    public static final String TABLENAME = "oper";

    public String getTableName() {
        return TABLENAME;
    }

    public void addObject(List list, RecordSet rs) {
        Oper abb = new Oper();
        abb.pkid = rs.getString("pkid");
        abb.tid = rs.getString("tid");
        abb.deptPkid = rs.getString("dept_pkid");
        abb.id = rs.getString("id");
        abb.name = rs.getString("name");
        abb.passwd = rs.getString("passwd");
        abb.issuper = rs.getString("issuper");
        abb.duty = rs.getString("duty");
        abb.type = rs.getString("type");
        abb.sex = rs.getString("sex");
        abb.email = rs.getString("email");
        abb.mobilephone = rs.getString("mobilephone");
        abb.phone = rs.getString("phone");
        abb.otherphone = rs.getString("otherphone");
        abb.enabled = rs.getString("enabled");
        abb.archivedFlag = rs.getString("archived_flag");
        abb.createdBy = rs.getString("created_by");
        abb.createdTime = rs.getString("created_time");
        abb.lastUpdBy = rs.getString("last_up_dBy");
        abb.lastUpdTime = rs.getString("last_upd_time");
        abb.remark = rs.getString("remark");
        abb.recVersion = rs.getInt("recVersion");
        abb.recVersion = rs.getInt("attachment");
        list.add(abb);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.OPER.PKID
     *
     * @return the value of EPSS.OPER.PKID
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public String getPkid() {
        return pkid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.OPER.PKID
     *
     * @param pkid the value for EPSS.OPER.PKID
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public void setPkid(String pkid) {
        this.pkid = pkid == null ? null : pkid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.OPER.TID
     *
     * @return the value of EPSS.OPER.TID
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public String getTid() {
        return tid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.OPER.TID
     *
     * @param tid the value for EPSS.OPER.TID
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public void setTid(String tid) {
        this.tid = tid == null ? null : tid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.OPER.DEPT_PKID
     *
     * @return the value of EPSS.OPER.DEPT_PKID
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public String getDeptPkid() {
        return deptPkid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.OPER.DEPT_PKID
     *
     * @param deptPkid the value for EPSS.OPER.DEPT_PKID
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public void setDeptPkid(String deptPkid) {
        this.deptPkid = deptPkid == null ? null : deptPkid.trim();
    }

    public Dept getDept() {
        return dept;
    }

    public void setDept(Dept dept) {
        this.dept = dept;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.OPER.ID
     *
     * @return the value of EPSS.OPER.ID
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.OPER.ID
     *
     * @param id the value for EPSS.OPER.ID
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.OPER.NAME
     *
     * @return the value of EPSS.OPER.NAME
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.OPER.NAME
     *
     * @param name the value for EPSS.OPER.NAME
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.OPER.PASSWD
     *
     * @return the value of EPSS.OPER.PASSWD
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public String getPasswd() {
        return passwd;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.OPER.PASSWD
     *
     * @param passwd the value for EPSS.OPER.PASSWD
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public void setPasswd(String passwd) {
        this.passwd = passwd == null ? null : passwd.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.OPER.ISSUPER
     *
     * @return the value of EPSS.OPER.ISSUPER
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public String getIssuper() {
        return issuper;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.OPER.ISSUPER
     *
     * @param issuper the value for EPSS.OPER.ISSUPER
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public void setIssuper(String issuper) {
        this.issuper = issuper == null ? null : issuper.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.OPER.DUTY
     *
     * @return the value of EPSS.OPER.DUTY
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public String getDuty() {
        return duty;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.OPER.DUTY
     *
     * @param duty the value for EPSS.OPER.DUTY
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public void setDuty(String duty) {
        this.duty = duty == null ? null : duty.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.OPER.TYPE
     *
     * @return the value of EPSS.OPER.TYPE
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public String getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.OPER.TYPE
     *
     * @param type the value for EPSS.OPER.TYPE
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.OPER.SEX
     *
     * @return the value of EPSS.OPER.SEX
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public String getSex() {
        return sex;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.OPER.SEX
     *
     * @param sex the value for EPSS.OPER.SEX
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.OPER.EMAIL
     *
     * @return the value of EPSS.OPER.EMAIL
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public String getEmail() {
        return email;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.OPER.EMAIL
     *
     * @param email the value for EPSS.OPER.EMAIL
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.OPER.MOBILEPHONE
     *
     * @return the value of EPSS.OPER.MOBILEPHONE
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public String getMobilephone() {
        return mobilephone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.OPER.MOBILEPHONE
     *
     * @param mobilephone the value for EPSS.OPER.MOBILEPHONE
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone == null ? null : mobilephone.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.OPER.PHONE
     *
     * @return the value of EPSS.OPER.PHONE
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public String getPhone() {
        return phone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.OPER.PHONE
     *
     * @param phone the value for EPSS.OPER.PHONE
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.OPER.OTHERPHONE
     *
     * @return the value of EPSS.OPER.OTHERPHONE
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public String getOtherphone() {
        return otherphone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.OPER.OTHERPHONE
     *
     * @param otherphone the value for EPSS.OPER.OTHERPHONE
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public void setOtherphone(String otherphone) {
        this.otherphone = otherphone == null ? null : otherphone.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.OPER.ENABLED
     *
     * @return the value of EPSS.OPER.ENABLED
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public String getEnabled() {
        return enabled;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.OPER.ENABLED
     *
     * @param enabled the value for EPSS.OPER.ENABLED
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public void setEnabled(String enabled) {
        this.enabled = enabled == null ? null : enabled.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.OPER.ARCHIVED_FLAG
     *
     * @return the value of EPSS.OPER.ARCHIVED_FLAG
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public String getArchivedFlag() {
        return archivedFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.OPER.ARCHIVED_FLAG
     *
     * @param archivedFlag the value for EPSS.OPER.ARCHIVED_FLAG
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public void setArchivedFlag(String archivedFlag) {
        this.archivedFlag = archivedFlag == null ? null : archivedFlag.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.OPER.CREATED_BY
     *
     * @return the value of EPSS.OPER.CREATED_BY
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.OPER.CREATED_BY
     *
     * @param createdBy the value for EPSS.OPER.CREATED_BY
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy == null ? null : createdBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.OPER.CREATED_TIME
     *
     * @return the value of EPSS.OPER.CREATED_TIME
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public String getCreatedTime() {
        return createdTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.OPER.CREATED_TIME
     *
     * @param createdTime the value for EPSS.OPER.CREATED_TIME
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime == null ? null : createdTime.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.OPER.LAST_UPD_BY
     *
     * @return the value of EPSS.OPER.LAST_UPD_BY
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public String getLastUpdBy() {
        return lastUpdBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.OPER.LAST_UPD_BY
     *
     * @param lastUpdBy the value for EPSS.OPER.LAST_UPD_BY
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public void setLastUpdBy(String lastUpdBy) {
        this.lastUpdBy = lastUpdBy == null ? null : lastUpdBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.OPER.LAST_UPD_TIME
     *
     * @return the value of EPSS.OPER.LAST_UPD_TIME
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public String getLastUpdTime() {
        return lastUpdTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.OPER.LAST_UPD_TIME
     *
     * @param lastUpdTime the value for EPSS.OPER.LAST_UPD_TIME
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public void setLastUpdTime(String lastUpdTime) {
        this.lastUpdTime = lastUpdTime == null ? null : lastUpdTime.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.OPER.REMARK
     *
     * @return the value of EPSS.OPER.REMARK
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.OPER.REMARK
     *
     * @param remark the value for EPSS.OPER.REMARK
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.OPER.REC_VERSION
     *
     * @return the value of EPSS.OPER.REC_VERSION
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public Integer getRecVersion() {
        return recVersion;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.OPER.REC_VERSION
     *
     * @param recVersion the value for EPSS.OPER.REC_VERSION
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public void setRecVersion(Integer recVersion) {
        this.recVersion = recVersion;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.OPER.ATTACHMENT
     *
     * @return the value of EPSS.OPER.ATTACHMENT
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public String getAttachment() {
        return attachment;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.OPER.ATTACHMENT
     *
     * @param attachment the value for EPSS.OPER.ATTACHMENT
     *
     * @mbggenerated Mon Sep 22 16:37:37 CST 2014
     */
    public void setAttachment(String attachment) {
        this.attachment = attachment == null ? null : attachment.trim();
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }
}