package epss.repository.model;

public class Dept {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.DEPT.PKID
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    private String pkid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.DEPT.TID
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    private String tid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.DEPT.ID
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.DEPT.NAME
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.DEPT.PARENTPKID
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    private String parentpkid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.DEPT.LEAF
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    private String leaf;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.DEPT.STATUS
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    private String status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.DEPT.ISDUMMY
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    private String isdummy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.DEPT.ARCHIVED_FLAG
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    private String archivedFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.DEPT.CREATED_BY
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    private String createdBy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.DEPT.CREATED_TIME
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    private String createdTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.DEPT.LAST_UPD_BY
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    private String lastUpdBy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.DEPT.LAST_UPD_TIME
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    private String lastUpdTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.DEPT.REMARK
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    private String remark;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.DEPT.RECVERSION
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    private Integer recversion;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.DEPT.PKID
     *
     * @return the value of EPSS.DEPT.PKID
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    public String getPkid() {
        return pkid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.DEPT.PKID
     *
     * @param pkid the value for EPSS.DEPT.PKID
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    public void setPkid(String pkid) {
        this.pkid = pkid == null ? null : pkid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.DEPT.TID
     *
     * @return the value of EPSS.DEPT.TID
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    public String getTid() {
        return tid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.DEPT.TID
     *
     * @param tid the value for EPSS.DEPT.TID
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    public void setTid(String tid) {
        this.tid = tid == null ? null : tid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.DEPT.ID
     *
     * @return the value of EPSS.DEPT.ID
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.DEPT.ID
     *
     * @param id the value for EPSS.DEPT.ID
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.DEPT.NAME
     *
     * @return the value of EPSS.DEPT.NAME
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.DEPT.NAME
     *
     * @param name the value for EPSS.DEPT.NAME
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.DEPT.PARENTPKID
     *
     * @return the value of EPSS.DEPT.PARENTPKID
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    public String getParentpkid() {
        return parentpkid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.DEPT.PARENTPKID
     *
     * @param parentpkid the value for EPSS.DEPT.PARENTPKID
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    public void setParentpkid(String parentpkid) {
        this.parentpkid = parentpkid == null ? null : parentpkid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.DEPT.LEAF
     *
     * @return the value of EPSS.DEPT.LEAF
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    public String getLeaf() {
        return leaf;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.DEPT.LEAF
     *
     * @param leaf the value for EPSS.DEPT.LEAF
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    public void setLeaf(String leaf) {
        this.leaf = leaf == null ? null : leaf.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.DEPT.STATUS
     *
     * @return the value of EPSS.DEPT.STATUS
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.DEPT.STATUS
     *
     * @param status the value for EPSS.DEPT.STATUS
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.DEPT.ISDUMMY
     *
     * @return the value of EPSS.DEPT.ISDUMMY
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    public String getIsdummy() {
        return isdummy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.DEPT.ISDUMMY
     *
     * @param isdummy the value for EPSS.DEPT.ISDUMMY
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    public void setIsdummy(String isdummy) {
        this.isdummy = isdummy == null ? null : isdummy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.DEPT.ARCHIVED_FLAG
     *
     * @return the value of EPSS.DEPT.ARCHIVED_FLAG
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    public String getArchivedFlag() {
        return archivedFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.DEPT.ARCHIVED_FLAG
     *
     * @param archivedFlag the value for EPSS.DEPT.ARCHIVED_FLAG
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    public void setArchivedFlag(String archivedFlag) {
        this.archivedFlag = archivedFlag == null ? null : archivedFlag.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.DEPT.CREATED_BY
     *
     * @return the value of EPSS.DEPT.CREATED_BY
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.DEPT.CREATED_BY
     *
     * @param createdBy the value for EPSS.DEPT.CREATED_BY
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy == null ? null : createdBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.DEPT.CREATED_TIME
     *
     * @return the value of EPSS.DEPT.CREATED_TIME
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    public String getCreatedTime() {
        return createdTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.DEPT.CREATED_TIME
     *
     * @param createdTime the value for EPSS.DEPT.CREATED_TIME
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime == null ? null : createdTime.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.DEPT.LAST_UPD_BY
     *
     * @return the value of EPSS.DEPT.LAST_UPD_BY
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    public String getLastUpdBy() {
        return lastUpdBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.DEPT.LAST_UPD_BY
     *
     * @param lastUpdBy the value for EPSS.DEPT.LAST_UPD_BY
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    public void setLastUpdBy(String lastUpdBy) {
        this.lastUpdBy = lastUpdBy == null ? null : lastUpdBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.DEPT.LAST_UPD_TIME
     *
     * @return the value of EPSS.DEPT.LAST_UPD_TIME
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    public String getLastUpdTime() {
        return lastUpdTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.DEPT.LAST_UPD_TIME
     *
     * @param lastUpdTime the value for EPSS.DEPT.LAST_UPD_TIME
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    public void setLastUpdTime(String lastUpdTime) {
        this.lastUpdTime = lastUpdTime == null ? null : lastUpdTime.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.DEPT.REMARK
     *
     * @return the value of EPSS.DEPT.REMARK
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.DEPT.REMARK
     *
     * @param remark the value for EPSS.DEPT.REMARK
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.DEPT.RECVERSION
     *
     * @return the value of EPSS.DEPT.RECVERSION
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    public Integer getRecversion() {
        return recversion;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.DEPT.RECVERSION
     *
     * @param recversion the value for EPSS.DEPT.RECVERSION
     *
     * @mbggenerated Tue Aug 12 17:29:38 CST 2014
     */
    public void setRecversion(Integer recversion) {
        this.recversion = recversion;
    }
}