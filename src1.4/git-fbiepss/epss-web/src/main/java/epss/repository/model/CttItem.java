package epss.repository.model;

import java.math.BigDecimal;

public class CttItem {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ctt_item.PKID
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    private String pkid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ctt_item.BELONG_TO_TYPE
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    private String belongToType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ctt_item.BELONG_TO_PKID
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    private String belongToPkid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ctt_item.PARENT_PKID
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    private String parentPkid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ctt_item.GRADE
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    private Integer grade;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ctt_item.ORDERID
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    private Integer orderid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ctt_item.NAME
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ctt_item.UNIT
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    private String unit;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ctt_item.CONTRACT_UNIT_PRICE
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    private BigDecimal contractUnitPrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ctt_item.CONTRACT_QUANTITY
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    private BigDecimal contractQuantity;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ctt_item.CONTRACT_AMOUNT
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    private BigDecimal contractAmount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ctt_item.SIGN_PART_A_PRICE
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    private BigDecimal signPartAPrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ctt_item.ARCHIVED_FLAG
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    private String archivedFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ctt_item.ORIGIN_FLAG
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    private String originFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ctt_item.CREATED_BY
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    private String createdBy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ctt_item.CREATED_TIME
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    private String createdTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ctt_item.LAST_UPD_BY
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    private String lastUpdBy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ctt_item.LAST_UPD_TIME
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    private String lastUpdTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ctt_item.REC_VERSION
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    private Integer recVersion;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ctt_item.REMARK
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    private String remark;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ctt_item.CORRESPONDING_PKID
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    private String correspondingPkid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ctt_item.SPARE_FIELD
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    private String spareField;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ctt_item.ITEM_PKID
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    private String itemPkid;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ctt_item.PKID
     *
     * @return the value of ctt_item.PKID
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public String getPkid() {
        return pkid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ctt_item.PKID
     *
     * @param pkid the value for ctt_item.PKID
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public void setPkid(String pkid) {
        this.pkid = pkid == null ? null : pkid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ctt_item.BELONG_TO_TYPE
     *
     * @return the value of ctt_item.BELONG_TO_TYPE
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public String getBelongToType() {
        return belongToType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ctt_item.BELONG_TO_TYPE
     *
     * @param belongToType the value for ctt_item.BELONG_TO_TYPE
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public void setBelongToType(String belongToType) {
        this.belongToType = belongToType == null ? null : belongToType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ctt_item.BELONG_TO_PKID
     *
     * @return the value of ctt_item.BELONG_TO_PKID
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public String getBelongToPkid() {
        return belongToPkid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ctt_item.BELONG_TO_PKID
     *
     * @param belongToPkid the value for ctt_item.BELONG_TO_PKID
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public void setBelongToPkid(String belongToPkid) {
        this.belongToPkid = belongToPkid == null ? null : belongToPkid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ctt_item.PARENT_PKID
     *
     * @return the value of ctt_item.PARENT_PKID
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public String getParentPkid() {
        return parentPkid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ctt_item.PARENT_PKID
     *
     * @param parentPkid the value for ctt_item.PARENT_PKID
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public void setParentPkid(String parentPkid) {
        this.parentPkid = parentPkid == null ? null : parentPkid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ctt_item.GRADE
     *
     * @return the value of ctt_item.GRADE
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public Integer getGrade() {
        return grade;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ctt_item.GRADE
     *
     * @param grade the value for ctt_item.GRADE
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ctt_item.ORDERID
     *
     * @return the value of ctt_item.ORDERID
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public Integer getOrderid() {
        return orderid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ctt_item.ORDERID
     *
     * @param orderid the value for ctt_item.ORDERID
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ctt_item.NAME
     *
     * @return the value of ctt_item.NAME
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ctt_item.NAME
     *
     * @param name the value for ctt_item.NAME
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ctt_item.UNIT
     *
     * @return the value of ctt_item.UNIT
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public String getUnit() {
        return unit;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ctt_item.UNIT
     *
     * @param unit the value for ctt_item.UNIT
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ctt_item.CONTRACT_UNIT_PRICE
     *
     * @return the value of ctt_item.CONTRACT_UNIT_PRICE
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public BigDecimal getContractUnitPrice() {
        return contractUnitPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ctt_item.CONTRACT_UNIT_PRICE
     *
     * @param contractUnitPrice the value for ctt_item.CONTRACT_UNIT_PRICE
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public void setContractUnitPrice(BigDecimal contractUnitPrice) {
        this.contractUnitPrice = contractUnitPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ctt_item.CONTRACT_QUANTITY
     *
     * @return the value of ctt_item.CONTRACT_QUANTITY
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public BigDecimal getContractQuantity() {
        return contractQuantity;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ctt_item.CONTRACT_QUANTITY
     *
     * @param contractQuantity the value for ctt_item.CONTRACT_QUANTITY
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public void setContractQuantity(BigDecimal contractQuantity) {
        this.contractQuantity = contractQuantity;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ctt_item.CONTRACT_AMOUNT
     *
     * @return the value of ctt_item.CONTRACT_AMOUNT
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public BigDecimal getContractAmount() {
        return contractAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ctt_item.CONTRACT_AMOUNT
     *
     * @param contractAmount the value for ctt_item.CONTRACT_AMOUNT
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public void setContractAmount(BigDecimal contractAmount) {
        this.contractAmount = contractAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ctt_item.SIGN_PART_A_PRICE
     *
     * @return the value of ctt_item.SIGN_PART_A_PRICE
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public BigDecimal getSignPartAPrice() {
        return signPartAPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ctt_item.SIGN_PART_A_PRICE
     *
     * @param signPartAPrice the value for ctt_item.SIGN_PART_A_PRICE
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public void setSignPartAPrice(BigDecimal signPartAPrice) {
        this.signPartAPrice = signPartAPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ctt_item.ARCHIVED_FLAG
     *
     * @return the value of ctt_item.ARCHIVED_FLAG
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public String getArchivedFlag() {
        return archivedFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ctt_item.ARCHIVED_FLAG
     *
     * @param archivedFlag the value for ctt_item.ARCHIVED_FLAG
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public void setArchivedFlag(String archivedFlag) {
        this.archivedFlag = archivedFlag == null ? null : archivedFlag.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ctt_item.ORIGIN_FLAG
     *
     * @return the value of ctt_item.ORIGIN_FLAG
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public String getOriginFlag() {
        return originFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ctt_item.ORIGIN_FLAG
     *
     * @param originFlag the value for ctt_item.ORIGIN_FLAG
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public void setOriginFlag(String originFlag) {
        this.originFlag = originFlag == null ? null : originFlag.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ctt_item.CREATED_BY
     *
     * @return the value of ctt_item.CREATED_BY
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ctt_item.CREATED_BY
     *
     * @param createdBy the value for ctt_item.CREATED_BY
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy == null ? null : createdBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ctt_item.CREATED_TIME
     *
     * @return the value of ctt_item.CREATED_TIME
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public String getCreatedTime() {
        return createdTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ctt_item.CREATED_TIME
     *
     * @param createdTime the value for ctt_item.CREATED_TIME
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime == null ? null : createdTime.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ctt_item.LAST_UPD_BY
     *
     * @return the value of ctt_item.LAST_UPD_BY
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public String getLastUpdBy() {
        return lastUpdBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ctt_item.LAST_UPD_BY
     *
     * @param lastUpdBy the value for ctt_item.LAST_UPD_BY
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public void setLastUpdBy(String lastUpdBy) {
        this.lastUpdBy = lastUpdBy == null ? null : lastUpdBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ctt_item.LAST_UPD_TIME
     *
     * @return the value of ctt_item.LAST_UPD_TIME
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public String getLastUpdTime() {
        return lastUpdTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ctt_item.LAST_UPD_TIME
     *
     * @param lastUpdTime the value for ctt_item.LAST_UPD_TIME
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public void setLastUpdTime(String lastUpdTime) {
        this.lastUpdTime = lastUpdTime == null ? null : lastUpdTime.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ctt_item.REC_VERSION
     *
     * @return the value of ctt_item.REC_VERSION
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public Integer getRecVersion() {
        return recVersion;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ctt_item.REC_VERSION
     *
     * @param recVersion the value for ctt_item.REC_VERSION
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public void setRecVersion(Integer recVersion) {
        this.recVersion = recVersion;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ctt_item.REMARK
     *
     * @return the value of ctt_item.REMARK
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ctt_item.REMARK
     *
     * @param remark the value for ctt_item.REMARK
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ctt_item.CORRESPONDING_PKID
     *
     * @return the value of ctt_item.CORRESPONDING_PKID
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public String getCorrespondingPkid() {
        return correspondingPkid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ctt_item.CORRESPONDING_PKID
     *
     * @param correspondingPkid the value for ctt_item.CORRESPONDING_PKID
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public void setCorrespondingPkid(String correspondingPkid) {
        this.correspondingPkid = correspondingPkid == null ? null : correspondingPkid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ctt_item.SPARE_FIELD
     *
     * @return the value of ctt_item.SPARE_FIELD
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public String getSpareField() {
        return spareField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ctt_item.SPARE_FIELD
     *
     * @param spareField the value for ctt_item.SPARE_FIELD
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public void setSpareField(String spareField) {
        this.spareField = spareField == null ? null : spareField.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ctt_item.ITEM_PKID
     *
     * @return the value of ctt_item.ITEM_PKID
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public String getItemPkid() {
        return itemPkid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ctt_item.ITEM_PKID
     *
     * @param itemPkid the value for ctt_item.ITEM_PKID
     *
     * @mbggenerated Wed Oct 08 14:42:56 CST 2014
     */
    public void setItemPkid(String itemPkid) {
        this.itemPkid = itemPkid == null ? null : itemPkid.trim();
    }
}