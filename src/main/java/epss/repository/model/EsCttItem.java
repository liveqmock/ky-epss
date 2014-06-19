package epss.repository.model;

import java.math.BigDecimal;

public class EsCttItem {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.PKID
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    private String pkid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.BELONG_TO_TYPE
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    private String belongToType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.BELONG_TO_PKID
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    private String belongToPkid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.PARENT_PKID
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    private String parentPkid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.GRADE
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    private Integer grade;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.ORDERID
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    private Integer orderid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.NAME
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.UNIT
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    private String unit;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.CONTRACT_UNIT_PRICE
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    private BigDecimal contractUnitPrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.CONTRACT_QUANTITY
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    private BigDecimal contractQuantity;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.CONTRACT_AMOUNT
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    private BigDecimal contractAmount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.SIGN_PART_A_PRICE
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    private BigDecimal signPartAPrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.DELETED_FLAG
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    private String deletedFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.ORIGIN_FLAG
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    private String originFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.CREATED_BY
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    private String createdBy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.CREATED_DATE
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    private String createdDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.LAST_UPD_BY
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    private String lastUpdBy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.LAST_UPD_DATE
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    private String lastUpdDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.MODIFICATION_NUM
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    private Integer modificationNum;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.NOTE
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    private String note;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.CORRESPONDING_PKID
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    private String correspondingPkid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.SPARE_FIELD
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    private String spareField;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.ITEM_PKID
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    private String itemPkid;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.ES_CTT_ITEM.PKID
     *
     * @return the value of EPSS.ES_CTT_ITEM.PKID
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public String getPkid() {
        return pkid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.ES_CTT_ITEM.PKID
     *
     * @param pkid the value for EPSS.ES_CTT_ITEM.PKID
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public void setPkid(String pkid) {
        this.pkid = pkid == null ? null : pkid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.ES_CTT_ITEM.BELONG_TO_TYPE
     *
     * @return the value of EPSS.ES_CTT_ITEM.BELONG_TO_TYPE
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public String getBelongToType() {
        return belongToType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.ES_CTT_ITEM.BELONG_TO_TYPE
     *
     * @param belongToType the value for EPSS.ES_CTT_ITEM.BELONG_TO_TYPE
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public void setBelongToType(String belongToType) {
        this.belongToType = belongToType == null ? null : belongToType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.ES_CTT_ITEM.BELONG_TO_PKID
     *
     * @return the value of EPSS.ES_CTT_ITEM.BELONG_TO_PKID
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public String getBelongToPkid() {
        return belongToPkid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.ES_CTT_ITEM.BELONG_TO_PKID
     *
     * @param belongToPkid the value for EPSS.ES_CTT_ITEM.BELONG_TO_PKID
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public void setBelongToPkid(String belongToPkid) {
        this.belongToPkid = belongToPkid == null ? null : belongToPkid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.ES_CTT_ITEM.PARENT_PKID
     *
     * @return the value of EPSS.ES_CTT_ITEM.PARENT_PKID
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public String getParentPkid() {
        return parentPkid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.ES_CTT_ITEM.PARENT_PKID
     *
     * @param parentPkid the value for EPSS.ES_CTT_ITEM.PARENT_PKID
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public void setParentPkid(String parentPkid) {
        this.parentPkid = parentPkid == null ? null : parentPkid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.ES_CTT_ITEM.GRADE
     *
     * @return the value of EPSS.ES_CTT_ITEM.GRADE
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public Integer getGrade() {
        return grade;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.ES_CTT_ITEM.GRADE
     *
     * @param grade the value for EPSS.ES_CTT_ITEM.GRADE
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.ES_CTT_ITEM.ORDERID
     *
     * @return the value of EPSS.ES_CTT_ITEM.ORDERID
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public Integer getOrderid() {
        return orderid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.ES_CTT_ITEM.ORDERID
     *
     * @param orderid the value for EPSS.ES_CTT_ITEM.ORDERID
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.ES_CTT_ITEM.NAME
     *
     * @return the value of EPSS.ES_CTT_ITEM.NAME
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.ES_CTT_ITEM.NAME
     *
     * @param name the value for EPSS.ES_CTT_ITEM.NAME
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.ES_CTT_ITEM.UNIT
     *
     * @return the value of EPSS.ES_CTT_ITEM.UNIT
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public String getUnit() {
        return unit;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.ES_CTT_ITEM.UNIT
     *
     * @param unit the value for EPSS.ES_CTT_ITEM.UNIT
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.ES_CTT_ITEM.CONTRACT_UNIT_PRICE
     *
     * @return the value of EPSS.ES_CTT_ITEM.CONTRACT_UNIT_PRICE
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public BigDecimal getContractUnitPrice() {
        return contractUnitPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.ES_CTT_ITEM.CONTRACT_UNIT_PRICE
     *
     * @param contractUnitPrice the value for EPSS.ES_CTT_ITEM.CONTRACT_UNIT_PRICE
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public void setContractUnitPrice(BigDecimal contractUnitPrice) {
        this.contractUnitPrice = contractUnitPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.ES_CTT_ITEM.CONTRACT_QUANTITY
     *
     * @return the value of EPSS.ES_CTT_ITEM.CONTRACT_QUANTITY
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public BigDecimal getContractQuantity() {
        return contractQuantity;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.ES_CTT_ITEM.CONTRACT_QUANTITY
     *
     * @param contractQuantity the value for EPSS.ES_CTT_ITEM.CONTRACT_QUANTITY
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public void setContractQuantity(BigDecimal contractQuantity) {
        this.contractQuantity = contractQuantity;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.ES_CTT_ITEM.CONTRACT_AMOUNT
     *
     * @return the value of EPSS.ES_CTT_ITEM.CONTRACT_AMOUNT
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public BigDecimal getContractAmount() {
        return contractAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.ES_CTT_ITEM.CONTRACT_AMOUNT
     *
     * @param contractAmount the value for EPSS.ES_CTT_ITEM.CONTRACT_AMOUNT
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public void setContractAmount(BigDecimal contractAmount) {
        this.contractAmount = contractAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.ES_CTT_ITEM.SIGN_PART_A_PRICE
     *
     * @return the value of EPSS.ES_CTT_ITEM.SIGN_PART_A_PRICE
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public BigDecimal getSignPartAPrice() {
        return signPartAPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.ES_CTT_ITEM.SIGN_PART_A_PRICE
     *
     * @param signPartAPrice the value for EPSS.ES_CTT_ITEM.SIGN_PART_A_PRICE
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public void setSignPartAPrice(BigDecimal signPartAPrice) {
        this.signPartAPrice = signPartAPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.ES_CTT_ITEM.DELETED_FLAG
     *
     * @return the value of EPSS.ES_CTT_ITEM.DELETED_FLAG
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public String getDeletedFlag() {
        return deletedFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.ES_CTT_ITEM.DELETED_FLAG
     *
     * @param deletedFlag the value for EPSS.ES_CTT_ITEM.DELETED_FLAG
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public void setDeletedFlag(String deletedFlag) {
        this.deletedFlag = deletedFlag == null ? null : deletedFlag.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.ES_CTT_ITEM.ORIGIN_FLAG
     *
     * @return the value of EPSS.ES_CTT_ITEM.ORIGIN_FLAG
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public String getOriginFlag() {
        return originFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.ES_CTT_ITEM.ORIGIN_FLAG
     *
     * @param originFlag the value for EPSS.ES_CTT_ITEM.ORIGIN_FLAG
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public void setOriginFlag(String originFlag) {
        this.originFlag = originFlag == null ? null : originFlag.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.ES_CTT_ITEM.CREATED_BY
     *
     * @return the value of EPSS.ES_CTT_ITEM.CREATED_BY
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.ES_CTT_ITEM.CREATED_BY
     *
     * @param createdBy the value for EPSS.ES_CTT_ITEM.CREATED_BY
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy == null ? null : createdBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.ES_CTT_ITEM.CREATED_DATE
     *
     * @return the value of EPSS.ES_CTT_ITEM.CREATED_DATE
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public String getCreatedDate() {
        return createdDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.ES_CTT_ITEM.CREATED_DATE
     *
     * @param createdDate the value for EPSS.ES_CTT_ITEM.CREATED_DATE
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate == null ? null : createdDate.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.ES_CTT_ITEM.LAST_UPD_BY
     *
     * @return the value of EPSS.ES_CTT_ITEM.LAST_UPD_BY
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public String getLastUpdBy() {
        return lastUpdBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.ES_CTT_ITEM.LAST_UPD_BY
     *
     * @param lastUpdBy the value for EPSS.ES_CTT_ITEM.LAST_UPD_BY
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public void setLastUpdBy(String lastUpdBy) {
        this.lastUpdBy = lastUpdBy == null ? null : lastUpdBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.ES_CTT_ITEM.LAST_UPD_DATE
     *
     * @return the value of EPSS.ES_CTT_ITEM.LAST_UPD_DATE
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public String getLastUpdDate() {
        return lastUpdDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.ES_CTT_ITEM.LAST_UPD_DATE
     *
     * @param lastUpdDate the value for EPSS.ES_CTT_ITEM.LAST_UPD_DATE
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public void setLastUpdDate(String lastUpdDate) {
        this.lastUpdDate = lastUpdDate == null ? null : lastUpdDate.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.ES_CTT_ITEM.MODIFICATION_NUM
     *
     * @return the value of EPSS.ES_CTT_ITEM.MODIFICATION_NUM
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public Integer getModificationNum() {
        return modificationNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.ES_CTT_ITEM.MODIFICATION_NUM
     *
     * @param modificationNum the value for EPSS.ES_CTT_ITEM.MODIFICATION_NUM
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public void setModificationNum(Integer modificationNum) {
        this.modificationNum = modificationNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.ES_CTT_ITEM.NOTE
     *
     * @return the value of EPSS.ES_CTT_ITEM.NOTE
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public String getNote() {
        return note;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.ES_CTT_ITEM.NOTE
     *
     * @param note the value for EPSS.ES_CTT_ITEM.NOTE
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.ES_CTT_ITEM.CORRESPONDING_PKID
     *
     * @return the value of EPSS.ES_CTT_ITEM.CORRESPONDING_PKID
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public String getCorrespondingPkid() {
        return correspondingPkid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.ES_CTT_ITEM.CORRESPONDING_PKID
     *
     * @param correspondingPkid the value for EPSS.ES_CTT_ITEM.CORRESPONDING_PKID
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public void setCorrespondingPkid(String correspondingPkid) {
        this.correspondingPkid = correspondingPkid == null ? null : correspondingPkid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.ES_CTT_ITEM.SPARE_FIELD
     *
     * @return the value of EPSS.ES_CTT_ITEM.SPARE_FIELD
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public String getSpareField() {
        return spareField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.ES_CTT_ITEM.SPARE_FIELD
     *
     * @param spareField the value for EPSS.ES_CTT_ITEM.SPARE_FIELD
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public void setSpareField(String spareField) {
        this.spareField = spareField == null ? null : spareField.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column EPSS.ES_CTT_ITEM.ITEM_PKID
     *
     * @return the value of EPSS.ES_CTT_ITEM.ITEM_PKID
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public String getItemPkid() {
        return itemPkid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column EPSS.ES_CTT_ITEM.ITEM_PKID
     *
     * @param itemPkid the value for EPSS.ES_CTT_ITEM.ITEM_PKID
     *
     * @mbggenerated Tue Jun 17 14:12:13 CST 2014
     */
    public void setItemPkid(String itemPkid) {
        this.itemPkid = itemPkid == null ? null : itemPkid.trim();
    }
}