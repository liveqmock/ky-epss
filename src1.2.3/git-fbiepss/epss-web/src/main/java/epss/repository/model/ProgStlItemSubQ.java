package epss.repository.model;

import java.math.BigDecimal;

public class ProgStlItemSubQ {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.PROG_STL_ITEM_SUB_Q.PKID
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    private String pkid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.PROG_STL_ITEM_SUB_Q.PERIOD_NO
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    private String periodNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.PROG_STL_ITEM_SUB_Q.SUBCTT_PKID
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    private String subcttPkid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.PROG_STL_ITEM_SUB_Q.SUBCTT_ITEM_PKID
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    private String subcttItemPkid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.PROG_STL_ITEM_SUB_Q.BEGIN_TO_CURRENT_PERIOD_E_QTY
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    private BigDecimal beginToCurrentPeriodEQty;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.PROG_STL_ITEM_SUB_Q.CURRENT_PERIOD_E_QTY
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    private BigDecimal currentPeriodEQty;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.PROG_STL_ITEM_SUB_Q.ARCHIVED_FLAG
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    private String archivedFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.PROG_STL_ITEM_SUB_Q.CREATED_BY
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    private String createdBy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.PROG_STL_ITEM_SUB_Q.CREATED_TIME
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    private String createdTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.PROG_STL_ITEM_SUB_Q.LAST_UPD_BY
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    private String lastUpdBy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.PROG_STL_ITEM_SUB_Q.LAST_UPD_TIME
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    private String lastUpdTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.PROG_STL_ITEM_SUB_Q.REC_VERSION
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    private Integer recVersion;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.PROG_STL_ITEM_SUB_Q.REMARK
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    private String remark;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.PROG_STL_ITEM_SUB_Q.PKID
     *
     * @return the value of EPSS.PROG_STL_ITEM_SUB_Q.PKID
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    public String getPkid() {
        return pkid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.PROG_STL_ITEM_SUB_Q.PKID
     *
     * @param pkid the value for EPSS.PROG_STL_ITEM_SUB_Q.PKID
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    public void setPkid(String pkid) {
        this.pkid = pkid == null ? null : pkid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.PROG_STL_ITEM_SUB_Q.PERIOD_NO
     *
     * @return the value of EPSS.PROG_STL_ITEM_SUB_Q.PERIOD_NO
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    public String getPeriodNo() {
        return periodNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.PROG_STL_ITEM_SUB_Q.PERIOD_NO
     *
     * @param periodNo the value for EPSS.PROG_STL_ITEM_SUB_Q.PERIOD_NO
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    public void setPeriodNo(String periodNo) {
        this.periodNo = periodNo == null ? null : periodNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.PROG_STL_ITEM_SUB_Q.SUBCTT_PKID
     *
     * @return the value of EPSS.PROG_STL_ITEM_SUB_Q.SUBCTT_PKID
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    public String getSubcttPkid() {
        return subcttPkid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.PROG_STL_ITEM_SUB_Q.SUBCTT_PKID
     *
     * @param subcttPkid the value for EPSS.PROG_STL_ITEM_SUB_Q.SUBCTT_PKID
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    public void setSubcttPkid(String subcttPkid) {
        this.subcttPkid = subcttPkid == null ? null : subcttPkid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.PROG_STL_ITEM_SUB_Q.SUBCTT_ITEM_PKID
     *
     * @return the value of EPSS.PROG_STL_ITEM_SUB_Q.SUBCTT_ITEM_PKID
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    public String getSubcttItemPkid() {
        return subcttItemPkid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.PROG_STL_ITEM_SUB_Q.SUBCTT_ITEM_PKID
     *
     * @param subcttItemPkid the value for EPSS.PROG_STL_ITEM_SUB_Q.SUBCTT_ITEM_PKID
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    public void setSubcttItemPkid(String subcttItemPkid) {
        this.subcttItemPkid = subcttItemPkid == null ? null : subcttItemPkid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.PROG_STL_ITEM_SUB_Q.BEGIN_TO_CURRENT_PERIOD_E_QTY
     *
     * @return the value of EPSS.PROG_STL_ITEM_SUB_Q.BEGIN_TO_CURRENT_PERIOD_E_QTY
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    public BigDecimal getBeginToCurrentPeriodEQty() {
        return beginToCurrentPeriodEQty;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.PROG_STL_ITEM_SUB_Q.BEGIN_TO_CURRENT_PERIOD_E_QTY
     *
     * @param beginToCurrentPeriodEQty the value for EPSS.PROG_STL_ITEM_SUB_Q.BEGIN_TO_CURRENT_PERIOD_E_QTY
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    public void setBeginToCurrentPeriodEQty(BigDecimal beginToCurrentPeriodEQty) {
        this.beginToCurrentPeriodEQty = beginToCurrentPeriodEQty;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.PROG_STL_ITEM_SUB_Q.CURRENT_PERIOD_E_QTY
     *
     * @return the value of EPSS.PROG_STL_ITEM_SUB_Q.CURRENT_PERIOD_E_QTY
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    public BigDecimal getCurrentPeriodEQty() {
        return currentPeriodEQty;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.PROG_STL_ITEM_SUB_Q.CURRENT_PERIOD_E_QTY
     *
     * @param currentPeriodEQty the value for EPSS.PROG_STL_ITEM_SUB_Q.CURRENT_PERIOD_E_QTY
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    public void setCurrentPeriodEQty(BigDecimal currentPeriodEQty) {
        this.currentPeriodEQty = currentPeriodEQty;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.PROG_STL_ITEM_SUB_Q.ARCHIVED_FLAG
     *
     * @return the value of EPSS.PROG_STL_ITEM_SUB_Q.ARCHIVED_FLAG
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    public String getArchivedFlag() {
        return archivedFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.PROG_STL_ITEM_SUB_Q.ARCHIVED_FLAG
     *
     * @param archivedFlag the value for EPSS.PROG_STL_ITEM_SUB_Q.ARCHIVED_FLAG
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    public void setArchivedFlag(String archivedFlag) {
        this.archivedFlag = archivedFlag == null ? null : archivedFlag.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.PROG_STL_ITEM_SUB_Q.CREATED_BY
     *
     * @return the value of EPSS.PROG_STL_ITEM_SUB_Q.CREATED_BY
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.PROG_STL_ITEM_SUB_Q.CREATED_BY
     *
     * @param createdBy the value for EPSS.PROG_STL_ITEM_SUB_Q.CREATED_BY
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy == null ? null : createdBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.PROG_STL_ITEM_SUB_Q.CREATED_TIME
     *
     * @return the value of EPSS.PROG_STL_ITEM_SUB_Q.CREATED_TIME
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    public String getCreatedTime() {
        return createdTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.PROG_STL_ITEM_SUB_Q.CREATED_TIME
     *
     * @param createdTime the value for EPSS.PROG_STL_ITEM_SUB_Q.CREATED_TIME
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime == null ? null : createdTime.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.PROG_STL_ITEM_SUB_Q.LAST_UPD_BY
     *
     * @return the value of EPSS.PROG_STL_ITEM_SUB_Q.LAST_UPD_BY
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    public String getLastUpdBy() {
        return lastUpdBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.PROG_STL_ITEM_SUB_Q.LAST_UPD_BY
     *
     * @param lastUpdBy the value for EPSS.PROG_STL_ITEM_SUB_Q.LAST_UPD_BY
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    public void setLastUpdBy(String lastUpdBy) {
        this.lastUpdBy = lastUpdBy == null ? null : lastUpdBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.PROG_STL_ITEM_SUB_Q.LAST_UPD_TIME
     *
     * @return the value of EPSS.PROG_STL_ITEM_SUB_Q.LAST_UPD_TIME
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    public String getLastUpdTime() {
        return lastUpdTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.PROG_STL_ITEM_SUB_Q.LAST_UPD_TIME
     *
     * @param lastUpdTime the value for EPSS.PROG_STL_ITEM_SUB_Q.LAST_UPD_TIME
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    public void setLastUpdTime(String lastUpdTime) {
        this.lastUpdTime = lastUpdTime == null ? null : lastUpdTime.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.PROG_STL_ITEM_SUB_Q.REC_VERSION
     *
     * @return the value of EPSS.PROG_STL_ITEM_SUB_Q.REC_VERSION
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    public Integer getRecVersion() {
        return recVersion;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.PROG_STL_ITEM_SUB_Q.REC_VERSION
     *
     * @param recVersion the value for EPSS.PROG_STL_ITEM_SUB_Q.REC_VERSION
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    public void setRecVersion(Integer recVersion) {
        this.recVersion = recVersion;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.PROG_STL_ITEM_SUB_Q.REMARK
     *
     * @return the value of EPSS.PROG_STL_ITEM_SUB_Q.REMARK
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.PROG_STL_ITEM_SUB_Q.REMARK
     *
     * @param remark the value for EPSS.PROG_STL_ITEM_SUB_Q.REMARK
     *
     * @mbggenerated Wed Dec 10 15:26:33 CST 2014
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}