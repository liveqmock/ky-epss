package epss.repository.model;

import java.math.BigDecimal;

public class EsItemStlTkcttEngSta {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.PKID
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    private String pkid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.PERIOD_NO
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    private String periodNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.TKCTT_PKID
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    private String tkcttPkid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.TKCTT_ITEM_PKID
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    private String tkcttItemPkid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.BEGIN_TO_CURRENT_PERIOD_QTY
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    private BigDecimal beginToCurrentPeriodQty;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.CURRENT_PERIOD_QTY
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    private BigDecimal currentPeriodQty;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.M_PURCHASE_UNIT_PRICE
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    private BigDecimal mPurchaseUnitPrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.DELETE_FLAG
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    private String deleteFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.CREATED_BY
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    private String createdBy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.CREATED_DATE
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    private String createdDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.LAST_UPD_BY
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    private String lastUpdBy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.LAST_UPD_DATE
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    private String lastUpdDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.MODIFICATION_NUM
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    private Integer modificationNum;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.PKID
     *
     * @return the value of EPSS.ES_ITEM_STL_TKCTT_ENG_STA.PKID
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    public String getPkid() {
        return pkid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.PKID
     *
     * @param pkid the value for EPSS.ES_ITEM_STL_TKCTT_ENG_STA.PKID
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    public void setPkid(String pkid) {
        this.pkid = pkid == null ? null : pkid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.PERIOD_NO
     *
     * @return the value of EPSS.ES_ITEM_STL_TKCTT_ENG_STA.PERIOD_NO
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    public String getPeriodNo() {
        return periodNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.PERIOD_NO
     *
     * @param periodNo the value for EPSS.ES_ITEM_STL_TKCTT_ENG_STA.PERIOD_NO
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    public void setPeriodNo(String periodNo) {
        this.periodNo = periodNo == null ? null : periodNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.TKCTT_PKID
     *
     * @return the value of EPSS.ES_ITEM_STL_TKCTT_ENG_STA.TKCTT_PKID
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    public String getTkcttPkid() {
        return tkcttPkid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.TKCTT_PKID
     *
     * @param tkcttPkid the value for EPSS.ES_ITEM_STL_TKCTT_ENG_STA.TKCTT_PKID
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    public void setTkcttPkid(String tkcttPkid) {
        this.tkcttPkid = tkcttPkid == null ? null : tkcttPkid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.TKCTT_ITEM_PKID
     *
     * @return the value of EPSS.ES_ITEM_STL_TKCTT_ENG_STA.TKCTT_ITEM_PKID
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    public String getTkcttItemPkid() {
        return tkcttItemPkid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.TKCTT_ITEM_PKID
     *
     * @param tkcttItemPkid the value for EPSS.ES_ITEM_STL_TKCTT_ENG_STA.TKCTT_ITEM_PKID
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    public void setTkcttItemPkid(String tkcttItemPkid) {
        this.tkcttItemPkid = tkcttItemPkid == null ? null : tkcttItemPkid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.BEGIN_TO_CURRENT_PERIOD_QTY
     *
     * @return the value of EPSS.ES_ITEM_STL_TKCTT_ENG_STA.BEGIN_TO_CURRENT_PERIOD_QTY
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    public BigDecimal getBeginToCurrentPeriodQty() {
        return beginToCurrentPeriodQty;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.BEGIN_TO_CURRENT_PERIOD_QTY
     *
     * @param beginToCurrentPeriodQty the value for EPSS.ES_ITEM_STL_TKCTT_ENG_STA.BEGIN_TO_CURRENT_PERIOD_QTY
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    public void setBeginToCurrentPeriodQty(BigDecimal beginToCurrentPeriodQty) {
        this.beginToCurrentPeriodQty = beginToCurrentPeriodQty;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.CURRENT_PERIOD_QTY
     *
     * @return the value of EPSS.ES_ITEM_STL_TKCTT_ENG_STA.CURRENT_PERIOD_QTY
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    public BigDecimal getCurrentPeriodQty() {
        return currentPeriodQty;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.CURRENT_PERIOD_QTY
     *
     * @param currentPeriodQty the value for EPSS.ES_ITEM_STL_TKCTT_ENG_STA.CURRENT_PERIOD_QTY
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    public void setCurrentPeriodQty(BigDecimal currentPeriodQty) {
        this.currentPeriodQty = currentPeriodQty;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.M_PURCHASE_UNIT_PRICE
     *
     * @return the value of EPSS.ES_ITEM_STL_TKCTT_ENG_STA.M_PURCHASE_UNIT_PRICE
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    public BigDecimal getmPurchaseUnitPrice() {
        return mPurchaseUnitPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.M_PURCHASE_UNIT_PRICE
     *
     * @param mPurchaseUnitPrice the value for EPSS.ES_ITEM_STL_TKCTT_ENG_STA.M_PURCHASE_UNIT_PRICE
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    public void setmPurchaseUnitPrice(BigDecimal mPurchaseUnitPrice) {
        this.mPurchaseUnitPrice = mPurchaseUnitPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.DELETE_FLAG
     *
     * @return the value of EPSS.ES_ITEM_STL_TKCTT_ENG_STA.DELETE_FLAG
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    public String getDeleteFlag() {
        return deleteFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.DELETE_FLAG
     *
     * @param deleteFlag the value for EPSS.ES_ITEM_STL_TKCTT_ENG_STA.DELETE_FLAG
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag == null ? null : deleteFlag.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.CREATED_BY
     *
     * @return the value of EPSS.ES_ITEM_STL_TKCTT_ENG_STA.CREATED_BY
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.CREATED_BY
     *
     * @param createdBy the value for EPSS.ES_ITEM_STL_TKCTT_ENG_STA.CREATED_BY
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy == null ? null : createdBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.CREATED_DATE
     *
     * @return the value of EPSS.ES_ITEM_STL_TKCTT_ENG_STA.CREATED_DATE
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    public String getCreatedDate() {
        return createdDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.CREATED_DATE
     *
     * @param createdDate the value for EPSS.ES_ITEM_STL_TKCTT_ENG_STA.CREATED_DATE
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate == null ? null : createdDate.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.LAST_UPD_BY
     *
     * @return the value of EPSS.ES_ITEM_STL_TKCTT_ENG_STA.LAST_UPD_BY
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    public String getLastUpdBy() {
        return lastUpdBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.LAST_UPD_BY
     *
     * @param lastUpdBy the value for EPSS.ES_ITEM_STL_TKCTT_ENG_STA.LAST_UPD_BY
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    public void setLastUpdBy(String lastUpdBy) {
        this.lastUpdBy = lastUpdBy == null ? null : lastUpdBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.LAST_UPD_DATE
     *
     * @return the value of EPSS.ES_ITEM_STL_TKCTT_ENG_STA.LAST_UPD_DATE
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    public String getLastUpdDate() {
        return lastUpdDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.LAST_UPD_DATE
     *
     * @param lastUpdDate the value for EPSS.ES_ITEM_STL_TKCTT_ENG_STA.LAST_UPD_DATE
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    public void setLastUpdDate(String lastUpdDate) {
        this.lastUpdDate = lastUpdDate == null ? null : lastUpdDate.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.MODIFICATION_NUM
     *
     * @return the value of EPSS.ES_ITEM_STL_TKCTT_ENG_STA.MODIFICATION_NUM
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    public Integer getModificationNum() {
        return modificationNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.ES_ITEM_STL_TKCTT_ENG_STA.MODIFICATION_NUM
     *
     * @param modificationNum the value for EPSS.ES_ITEM_STL_TKCTT_ENG_STA.MODIFICATION_NUM
     *
     * @mbggenerated Fri Jun 27 18:37:11 CST 2014
     */
    public void setModificationNum(Integer modificationNum) {
        this.modificationNum = modificationNum;
    }
}