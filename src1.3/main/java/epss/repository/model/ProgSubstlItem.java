package epss.repository.model;

import java.math.BigDecimal;

public class ProgSubstlItem {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.PROG_SUBSTL_ITEM.PKID
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    private String pkid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.PROG_SUBSTL_ITEM.PROG_SUBSTL_INFO_PKID
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    private String progSubstlInfoPkid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.PROG_SUBSTL_ITEM.UNIT
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    private String unit;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.PROG_SUBSTL_ITEM.UNIT_PRICE
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    private BigDecimal unitPrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.PROG_SUBSTL_ITEM.THIS_STAGE_QTY
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    private BigDecimal thisStageQty;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.PROG_SUBSTL_ITEM.THIS_STAGE_AMT
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    private BigDecimal thisStageAmt;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.PROG_SUBSTL_ITEM.ADD_UP_QTY
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    private BigDecimal addUpQty;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.PROG_SUBSTL_ITEM.ADD_UP_AMT
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    private BigDecimal addUpAmt;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.PROG_SUBSTL_ITEM.ARCHIVED_FLAG
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    private String archivedFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.PROG_SUBSTL_ITEM.ORIGIN_FLAG
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    private String originFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.PROG_SUBSTL_ITEM.CREATED_BY
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    private String createdBy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.PROG_SUBSTL_ITEM.CREATED_TIME
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    private String createdTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.PROG_SUBSTL_ITEM.UPDATED_BY
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    private String updatedBy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.PROG_SUBSTL_ITEM.UPDATED_TIME
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    private String updatedTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.PROG_SUBSTL_ITEM.REC_VERSION
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    private Integer recVersion;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.PROG_SUBSTL_ITEM.REMARK
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    private String remark;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.PROG_SUBSTL_ITEM.SUBCTT_ITEM_PKID
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    private String subcttItemPkid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.PROG_SUBSTL_ITEM.TID
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    private String tid;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.PROG_SUBSTL_ITEM.PKID
     *
     * @return the value of EPSS.PROG_SUBSTL_ITEM.PKID
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    public String getPkid() {
        return pkid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.PROG_SUBSTL_ITEM.PKID
     *
     * @param pkid the value for EPSS.PROG_SUBSTL_ITEM.PKID
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    public void setPkid(String pkid) {
        this.pkid = pkid == null ? null : pkid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.PROG_SUBSTL_ITEM.PROG_SUBSTL_INFO_PKID
     *
     * @return the value of EPSS.PROG_SUBSTL_ITEM.PROG_SUBSTL_INFO_PKID
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    public String getProgSubstlInfoPkid() {
        return progSubstlInfoPkid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.PROG_SUBSTL_ITEM.PROG_SUBSTL_INFO_PKID
     *
     * @param progSubstlInfoPkid the value for EPSS.PROG_SUBSTL_ITEM.PROG_SUBSTL_INFO_PKID
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    public void setProgSubstlInfoPkid(String progSubstlInfoPkid) {
        this.progSubstlInfoPkid = progSubstlInfoPkid == null ? null : progSubstlInfoPkid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.PROG_SUBSTL_ITEM.UNIT
     *
     * @return the value of EPSS.PROG_SUBSTL_ITEM.UNIT
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    public String getUnit() {
        return unit;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.PROG_SUBSTL_ITEM.UNIT
     *
     * @param unit the value for EPSS.PROG_SUBSTL_ITEM.UNIT
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.PROG_SUBSTL_ITEM.UNIT_PRICE
     *
     * @return the value of EPSS.PROG_SUBSTL_ITEM.UNIT_PRICE
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.PROG_SUBSTL_ITEM.UNIT_PRICE
     *
     * @param unitPrice the value for EPSS.PROG_SUBSTL_ITEM.UNIT_PRICE
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.PROG_SUBSTL_ITEM.THIS_STAGE_QTY
     *
     * @return the value of EPSS.PROG_SUBSTL_ITEM.THIS_STAGE_QTY
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    public BigDecimal getThisStageQty() {
        return thisStageQty;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.PROG_SUBSTL_ITEM.THIS_STAGE_QTY
     *
     * @param thisStageQty the value for EPSS.PROG_SUBSTL_ITEM.THIS_STAGE_QTY
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    public void setThisStageQty(BigDecimal thisStageQty) {
        this.thisStageQty = thisStageQty;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.PROG_SUBSTL_ITEM.THIS_STAGE_AMT
     *
     * @return the value of EPSS.PROG_SUBSTL_ITEM.THIS_STAGE_AMT
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    public BigDecimal getThisStageAmt() {
        return thisStageAmt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.PROG_SUBSTL_ITEM.THIS_STAGE_AMT
     *
     * @param thisStageAmt the value for EPSS.PROG_SUBSTL_ITEM.THIS_STAGE_AMT
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    public void setThisStageAmt(BigDecimal thisStageAmt) {
        this.thisStageAmt = thisStageAmt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.PROG_SUBSTL_ITEM.ADD_UP_QTY
     *
     * @return the value of EPSS.PROG_SUBSTL_ITEM.ADD_UP_QTY
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    public BigDecimal getAddUpQty() {
        return addUpQty;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.PROG_SUBSTL_ITEM.ADD_UP_QTY
     *
     * @param addUpQty the value for EPSS.PROG_SUBSTL_ITEM.ADD_UP_QTY
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    public void setAddUpQty(BigDecimal addUpQty) {
        this.addUpQty = addUpQty;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.PROG_SUBSTL_ITEM.ADD_UP_AMT
     *
     * @return the value of EPSS.PROG_SUBSTL_ITEM.ADD_UP_AMT
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    public BigDecimal getAddUpAmt() {
        return addUpAmt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.PROG_SUBSTL_ITEM.ADD_UP_AMT
     *
     * @param addUpAmt the value for EPSS.PROG_SUBSTL_ITEM.ADD_UP_AMT
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    public void setAddUpAmt(BigDecimal addUpAmt) {
        this.addUpAmt = addUpAmt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.PROG_SUBSTL_ITEM.ARCHIVED_FLAG
     *
     * @return the value of EPSS.PROG_SUBSTL_ITEM.ARCHIVED_FLAG
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    public String getArchivedFlag() {
        return archivedFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.PROG_SUBSTL_ITEM.ARCHIVED_FLAG
     *
     * @param archivedFlag the value for EPSS.PROG_SUBSTL_ITEM.ARCHIVED_FLAG
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    public void setArchivedFlag(String archivedFlag) {
        this.archivedFlag = archivedFlag == null ? null : archivedFlag.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.PROG_SUBSTL_ITEM.ORIGIN_FLAG
     *
     * @return the value of EPSS.PROG_SUBSTL_ITEM.ORIGIN_FLAG
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    public String getOriginFlag() {
        return originFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.PROG_SUBSTL_ITEM.ORIGIN_FLAG
     *
     * @param originFlag the value for EPSS.PROG_SUBSTL_ITEM.ORIGIN_FLAG
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    public void setOriginFlag(String originFlag) {
        this.originFlag = originFlag == null ? null : originFlag.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.PROG_SUBSTL_ITEM.CREATED_BY
     *
     * @return the value of EPSS.PROG_SUBSTL_ITEM.CREATED_BY
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.PROG_SUBSTL_ITEM.CREATED_BY
     *
     * @param createdBy the value for EPSS.PROG_SUBSTL_ITEM.CREATED_BY
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy == null ? null : createdBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.PROG_SUBSTL_ITEM.CREATED_TIME
     *
     * @return the value of EPSS.PROG_SUBSTL_ITEM.CREATED_TIME
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    public String getCreatedTime() {
        return createdTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.PROG_SUBSTL_ITEM.CREATED_TIME
     *
     * @param createdTime the value for EPSS.PROG_SUBSTL_ITEM.CREATED_TIME
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime == null ? null : createdTime.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.PROG_SUBSTL_ITEM.UPDATED_BY
     *
     * @return the value of EPSS.PROG_SUBSTL_ITEM.UPDATED_BY
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    public String getUpdatedBy() {
        return updatedBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.PROG_SUBSTL_ITEM.UPDATED_BY
     *
     * @param updatedBy the value for EPSS.PROG_SUBSTL_ITEM.UPDATED_BY
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy == null ? null : updatedBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.PROG_SUBSTL_ITEM.UPDATED_TIME
     *
     * @return the value of EPSS.PROG_SUBSTL_ITEM.UPDATED_TIME
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    public String getUpdatedTime() {
        return updatedTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.PROG_SUBSTL_ITEM.UPDATED_TIME
     *
     * @param updatedTime the value for EPSS.PROG_SUBSTL_ITEM.UPDATED_TIME
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime == null ? null : updatedTime.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.PROG_SUBSTL_ITEM.REC_VERSION
     *
     * @return the value of EPSS.PROG_SUBSTL_ITEM.REC_VERSION
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    public Integer getRecVersion() {
        return recVersion;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.PROG_SUBSTL_ITEM.REC_VERSION
     *
     * @param recVersion the value for EPSS.PROG_SUBSTL_ITEM.REC_VERSION
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    public void setRecVersion(Integer recVersion) {
        this.recVersion = recVersion;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.PROG_SUBSTL_ITEM.REMARK
     *
     * @return the value of EPSS.PROG_SUBSTL_ITEM.REMARK
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.PROG_SUBSTL_ITEM.REMARK
     *
     * @param remark the value for EPSS.PROG_SUBSTL_ITEM.REMARK
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.PROG_SUBSTL_ITEM.SUBCTT_ITEM_PKID
     *
     * @return the value of EPSS.PROG_SUBSTL_ITEM.SUBCTT_ITEM_PKID
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    public String getSubcttItemPkid() {
        return subcttItemPkid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.PROG_SUBSTL_ITEM.SUBCTT_ITEM_PKID
     *
     * @param subcttItemPkid the value for EPSS.PROG_SUBSTL_ITEM.SUBCTT_ITEM_PKID
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    public void setSubcttItemPkid(String subcttItemPkid) {
        this.subcttItemPkid = subcttItemPkid == null ? null : subcttItemPkid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.PROG_SUBSTL_ITEM.TID
     *
     * @return the value of EPSS.PROG_SUBSTL_ITEM.TID
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    public String getTid() {
        return tid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.PROG_SUBSTL_ITEM.TID
     *
     * @param tid the value for EPSS.PROG_SUBSTL_ITEM.TID
     *
     * @mbggenerated Sun Jun 08 03:16:01 CST 2014
     */
    public void setTid(String tid) {
        this.tid = tid == null ? null : tid.trim();
    }
}