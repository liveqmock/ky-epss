package epss.repository.model.model_show;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: vincent
 * Date: 13-2-17
 * Time: 下午9:54
 * To change this template use File | Settings | File Templates.
 */

public class CstplItemShow implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.PKID
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private String pkid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.BELONG_TO_TYPE
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private String belongToType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.BELONG_TO_PKID
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private String belongToPkid;
    /**
     * 编号，用作显示用，不在数据库里存储，动态显示
     */
    private String strNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.ORDERID
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private Integer orderid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.GRADE
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private Integer grade;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.NAME/EPSS.ES_ITEM_INFO.NAME
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.ITEM_PKID
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private String itemPkid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.PARENT_PKID
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private String parentPkid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.NOTE/EPSS.ES_ITEM_INFO.NOTE
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private String note;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.CORRESPONDING_PKID
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private String correspondingPkid;

    private String strCorrespondingItemNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_ITEM_INFO.UNIT
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private String unit;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_ITEM_INFO.CONTRACT_UNIT_PRICE
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private BigDecimal contractUnitPrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_ITEM_INFO.CONTRACT_QUANTITY
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private BigDecimal contractQuantity;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_ITEM_INFO.CONTRACT_AMOUNT
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private BigDecimal contractAmount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_ITEM_INFO.SIGN_PART_A_PRICE
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private BigDecimal signPartAPrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_ITEM_INFO.DELETED_FLAG
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private String deletedFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_ITEM_INFO.ORIGIN_FLAG
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private String originFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_ITEM_INFO.CREATED_BY
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private String createdBy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_ITEM_INFO.CREATED_DATE
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private String createdDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_ITEM_INFO.LAST_UPD_BY
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private String lastUpdBy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_ITEM_INFO.LAST_UPD_DATE
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private String lastUpdDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_ITEM_INFO.MODIFICATION_NUM
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private Integer modificationNum;

    // 对比项
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.PKID
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private String pkidContrast;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.BELONG_TO_TYPE
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private String belongToTypeContrast;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.BELONG_TO_PKID
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private String belongToPkidContrast;

    /**
     * 编号，用作显示用，不在数据库里存储，动态显示
     */
    private String strNoContrast;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.ORDERID
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private Integer orderidContrast;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.GRADE
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private Integer gradeContrast;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.NAME/EPSS.ES_ITEM_INFO.NAME
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private String nameContrast;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.ITEM_PKID
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private String itemPkidContrast;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.PARENT_PKID
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private String parentPkidContrast;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.NOTE/EPSS.ES_ITEM_INFO.NOTE
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private String noteContrast;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_CTT_ITEM.CORRESPONDING_PKID
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private String correspondingPkidContrast;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_ITEM_INFO.UNIT
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private String unitContrast;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_ITEM_INFO.CONTRACT_UNIT_PRICE
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private BigDecimal contractUnitPriceContrast;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_ITEM_INFO.CONTRACT_QUANTITY
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private BigDecimal contractQuantityContrast;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_ITEM_INFO.CONTRACT_AMOUNT
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private BigDecimal contractAmountContrast;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_ITEM_INFO.SIGN_PART_A_PRICE
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private BigDecimal signPartAPriceContrast;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_ITEM_INFO.DELETED_FLAG
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private String deletedFlagContrast;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_ITEM_INFO.ORIGIN_FLAG
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private String originFlagContrast;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_ITEM_INFO.CREATED_BY
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private String createdByContrast;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_ITEM_INFO.CREATED_DATE
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private String createdDateContrast;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_ITEM_INFO.LAST_UPD_BY
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private String lastUpdByContrast;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_ITEM_INFO.LAST_UPD_DATE
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private String lastUpdDateContrast;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column EPSS.ES_ITEM_INFO.MODIFICATION_NUM
     *
     * @mbggenerated Tue Feb 19 22:30:38 CST 2013
     */
    private Integer modificationNumContrast;

    /*编号，用作显示用，不在数据库里存储，动态显示。依据correspondingPkid*/
    private String correspondingItemNoContrast;

    private String spareField;

    public CstplItemShow() {
        //To change body of created methods use File | Settings | File Templates.
    }

    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }

        if (obj.getClass() == CstplItemShow.class)
        {
            CstplItemShow cstplItemShow = (CstplItemShow)obj;
            return ((cstplItemShow.pkid==null&&this.pkid==null)||
                      cstplItemShow.pkid.equals(this.pkid))
                    &&((cstplItemShow.belongToType==null&&this.belongToType==null)||
                        cstplItemShow.belongToType .equals(this.belongToType))
                    &&((cstplItemShow.belongToPkid==null&&this.belongToPkid==null)||
                        cstplItemShow.belongToPkid .equals(this.belongToPkid))
                    &&((cstplItemShow.strNo==null&&this.strNo==null)||
                        cstplItemShow.strNo .equals(this.strNo))
                    &&(cstplItemShow.orderid==this.orderid)
                    &&(cstplItemShow.grade==this.grade)
                    &&((cstplItemShow.name==null&&this.name==null)||
                        cstplItemShow.name .equals(this.name))
                    &&((cstplItemShow.itemPkid==null&&this.itemPkid==null)||
                        cstplItemShow.itemPkid .equals(this.itemPkid))
                    &&((cstplItemShow.parentPkid==null&&this.parentPkid==null)||
                        cstplItemShow.parentPkid .equals(this.parentPkid))
                    &&((cstplItemShow.note==null&&this.note==null)||
                        cstplItemShow.note .equals(this.note))
                    &&((cstplItemShow.correspondingPkid==null&&this.correspondingPkid==null)||
                        cstplItemShow.correspondingPkid .equals(this.correspondingPkid))
                    &&((cstplItemShow.unit==null&&this.unit==null)||
                        cstplItemShow.unit .equals(this.unit))
                    &&(cstplItemShow.contractUnitPrice==this.contractUnitPrice)
                    &&(cstplItemShow.contractQuantity==this.contractQuantity)
                    &&(cstplItemShow.contractAmount==this.contractAmount)
                    &&(cstplItemShow.signPartAPrice==this.signPartAPrice)
                    &&((cstplItemShow.deletedFlag==null&&this.deletedFlag==null)||
                        cstplItemShow.deletedFlag .equals(this.deletedFlag))
                    &&((cstplItemShow.originFlag==null&&this.originFlag==null)||
                        cstplItemShow.originFlag .equals(this.originFlag))
                    &&((cstplItemShow.createdBy==null&&this.createdBy==null)||
                        cstplItemShow.createdBy .equals(this.createdBy))
                    &&((cstplItemShow.createdDate==null&&this.createdDate==null)||
                        cstplItemShow.createdDate .equals(this.createdDate))
                    &&((cstplItemShow.lastUpdBy==null&&this.lastUpdBy==null)||
                        cstplItemShow.lastUpdBy .equals(this.lastUpdBy))
                    &&((cstplItemShow.lastUpdDate==null&&this.lastUpdDate==null)||
                        cstplItemShow.lastUpdDate .equals(this.lastUpdDate))
                    &&(cstplItemShow.modificationNum==this.modificationNum)

                    &&((cstplItemShow.pkidContrast==null&&this.pkidContrast==null)||
                        cstplItemShow.pkidContrast.equals(this.pkidContrast))
                    &&((cstplItemShow.belongToTypeContrast==null&&this.belongToTypeContrast==null)||
                        cstplItemShow.belongToTypeContrast .equals(this.belongToTypeContrast))
                    &&((cstplItemShow.belongToPkidContrast==null&&this.belongToPkidContrast==null)||
                        cstplItemShow.belongToPkidContrast .equals(this.belongToPkidContrast))
                    &&((cstplItemShow.strNoContrast==null&&this.strNoContrast==null)||
                        cstplItemShow.strNoContrast .equals(this.strNoContrast))
                    &&(cstplItemShow.orderidContrast==this.orderidContrast)
                    &&(cstplItemShow.gradeContrast==this.gradeContrast)
                    &&((cstplItemShow.nameContrast==null&&this.nameContrast==null)||
                        cstplItemShow.nameContrast .equals(this.nameContrast))
                    &&((cstplItemShow.itemPkidContrast==null&&this.itemPkidContrast==null)||
                        cstplItemShow.itemPkidContrast .equals(this.itemPkidContrast))
                    &&((cstplItemShow.parentPkidContrast==null&&this.parentPkidContrast==null)||
                        cstplItemShow.parentPkidContrast .equals(this.parentPkidContrast))
                    &&((cstplItemShow.noteContrast==null&&this.noteContrast==null)||
                        cstplItemShow.noteContrast .equals(this.noteContrast))
                    &&((cstplItemShow.correspondingPkidContrast==null&&this.correspondingPkidContrast==null)||
                        cstplItemShow.correspondingPkidContrast .equals(this.correspondingPkidContrast))
                    &&((cstplItemShow.unitContrast==null&&this.unitContrast==null)||
                        cstplItemShow.unitContrast .equals(this.unitContrast))
                    &&(cstplItemShow.contractUnitPriceContrast==this.contractUnitPriceContrast)
                    &&(cstplItemShow.contractQuantityContrast==this.contractQuantityContrast)
                    &&(cstplItemShow.contractAmountContrast==this.contractAmountContrast)
                    &&(cstplItemShow.signPartAPriceContrast==this.signPartAPriceContrast)
                    &&((cstplItemShow.deletedFlagContrast==null&&this.deletedFlagContrast==null)||
                        cstplItemShow.deletedFlagContrast .equals(this.deletedFlagContrast))
                    &&((cstplItemShow.originFlagContrast==null&&this.originFlagContrast==null)||
                        cstplItemShow.originFlagContrast .equals(this.originFlagContrast))
                    &&((cstplItemShow.createdByContrast==null&&this.createdByContrast==null)||
                        cstplItemShow.createdByContrast .equals(this.createdByContrast))
                    &&((cstplItemShow.createdDateContrast==null&&this.createdDateContrast==null)||
                        cstplItemShow.createdDateContrast .equals(this.createdDateContrast))
                    &&((cstplItemShow.lastUpdByContrast==null&&this.lastUpdByContrast==null)||
                        cstplItemShow.lastUpdByContrast .equals(this.lastUpdByContrast))
                    &&((cstplItemShow.lastUpdDateContrast==null&&this.lastUpdDateContrast==null)||
                        cstplItemShow.lastUpdDateContrast .equals(this.lastUpdDateContrast))
                    &&(cstplItemShow.modificationNumContrast==this.modificationNumContrast)
                    ;
        }
        return false;
    }

    public String getPkid() {
        return pkid;
    }

    public void setPkid(String pkid) {
        this.pkid = pkid;
    }

    public String getBelongToType() {
        return belongToType;
    }

    public void setBelongToType(String belongToType) {
        this.belongToType = belongToType;
    }

    public String getBelongToPkid() {
        return belongToPkid;
    }

    public void setBelongToPkid(String belongToPkid) {
        this.belongToPkid = belongToPkid;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemPkid() {
        return itemPkid;
    }

    public void setItemPkid(String itemPkid) {
        this.itemPkid = itemPkid;
    }

    public String getParentPkid() {
        return parentPkid;
    }

    public void setParentPkid(String parentPkid) {
        this.parentPkid = parentPkid;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCorrespondingPkid() {
        return correspondingPkid;
    }

    public void setCorrespondingPkid(String correspondingPkid) {
        this.correspondingPkid = correspondingPkid;
    }

    public String getStrCorrespondingItemNo() {
        return strCorrespondingItemNo;
    }

    public void setStrCorrespondingItemNo(String strCorrespondingItemNo) {
        this.strCorrespondingItemNo = strCorrespondingItemNo;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getContractUnitPrice() {
        return contractUnitPrice;
    }

    public void setContractUnitPrice(BigDecimal contractUnitPrice) {
        this.contractUnitPrice = contractUnitPrice;
    }

    public BigDecimal getContractQuantity() {
        return contractQuantity;
    }

    public void setContractQuantity(BigDecimal contractQuantity) {
        this.contractQuantity = contractQuantity;
    }

    public BigDecimal getContractAmount() {
        return contractAmount;
    }

    public void setContractAmount(BigDecimal contractAmount) {
        this.contractAmount = contractAmount;
    }

    public BigDecimal getSignPartAPrice() {
        return signPartAPrice;
    }

    public void setSignPartAPrice(BigDecimal signPartAPrice) {
        this.signPartAPrice = signPartAPrice;
    }

    public String getDeletedFlag() {
        return deletedFlag;
    }

    public void setDeletedFlag(String deletedFlag) {
        this.deletedFlag = deletedFlag;
    }

    public String getOriginFlag() {
        return originFlag;
    }

    public void setOriginFlag(String originFlag) {
        this.originFlag = originFlag;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastUpdBy() {
        return lastUpdBy;
    }

    public void setLastUpdBy(String lastUpdBy) {
        this.lastUpdBy = lastUpdBy;
    }

    public String getLastUpdDate() {
        return lastUpdDate;
    }

    public void setLastUpdDate(String lastUpdDate) {
        this.lastUpdDate = lastUpdDate;
    }

    public Integer getModificationNum() {
        return modificationNum;
    }

    public void setModificationNum(Integer modificationNum) {
        this.modificationNum = modificationNum;
    }

    public String getPkidContrast() {
        return pkidContrast;
    }

    public void setPkidContrast(String pkidContrast) {
        this.pkidContrast = pkidContrast;
    }

    public String getBelongToTypeContrast() {
        return belongToTypeContrast;
    }

    public void setBelongToTypeContrast(String belongToTypeContrast) {
        this.belongToTypeContrast = belongToTypeContrast;
    }

    public String getBelongToPkidContrast() {
        return belongToPkidContrast;
    }

    public void setBelongToPkidContrast(String belongToPkidContrast) {
        this.belongToPkidContrast = belongToPkidContrast;
    }

    public Integer getOrderidContrast() {
        return orderidContrast;
    }

    public void setOrderidContrast(Integer orderidContrast) {
        this.orderidContrast = orderidContrast;
    }

    public Integer getGradeContrast() {
        return gradeContrast;
    }

    public void setGradeContrast(Integer gradeContrast) {
        this.gradeContrast = gradeContrast;
    }

    public String getNameContrast() {
        return nameContrast;
    }

    public void setNameContrast(String nameContrast) {
        this.nameContrast = nameContrast;
    }

    public String getItemPkidContrast() {
        return itemPkidContrast;
    }

    public void setItemPkidContrast(String itemPkidContrast) {
        this.itemPkidContrast = itemPkidContrast;
    }

    public String getParentPkidContrast() {
        return parentPkidContrast;
    }

    public void setParentPkidContrast(String parentPkidContrast) {
        this.parentPkidContrast = parentPkidContrast;
    }

    public String getNoteContrast() {
        return noteContrast;
    }

    public void setNoteContrast(String noteContrast) {
        this.noteContrast = noteContrast;
    }

    public String getCorrespondingPkidContrast() {
        return correspondingPkidContrast;
    }

    public void setCorrespondingPkidContrast(String correspondingPkidContrast) {
        this.correspondingPkidContrast = correspondingPkidContrast;
    }

    public String getUnitContrast() {
        return unitContrast;
    }

    public void setUnitContrast(String unitContrast) {
        this.unitContrast = unitContrast;
    }

    public BigDecimal getContractUnitPriceContrast() {
        return contractUnitPriceContrast;
    }

    public void setContractUnitPriceContrast(BigDecimal contractUnitPriceContrast) {
        this.contractUnitPriceContrast = contractUnitPriceContrast;
    }

    public BigDecimal getContractQuantityContrast() {
        return contractQuantityContrast;
    }

    public void setContractQuantityContrast(BigDecimal contractQuantityContrast) {
        this.contractQuantityContrast = contractQuantityContrast;
    }

    public BigDecimal getContractAmountContrast() {
        return contractAmountContrast;
    }

    public void setContractAmountContrast(BigDecimal contractAmountContrast) {
        this.contractAmountContrast = contractAmountContrast;
    }

    public BigDecimal getSignPartAPriceContrast() {
        return signPartAPriceContrast;
    }

    public void setSignPartAPriceContrast(BigDecimal signPartAPriceContrast) {
        this.signPartAPriceContrast = signPartAPriceContrast;
    }

    public String getDeletedFlagContrast() {
        return deletedFlagContrast;
    }

    public void setDeletedFlagContrast(String deletedFlagContrast) {
        this.deletedFlagContrast = deletedFlagContrast;
    }

    public String getOriginFlagContrast() {
        return originFlagContrast;
    }

    public void setOriginFlagContrast(String originFlagContrast) {
        this.originFlagContrast = originFlagContrast;
    }

    public String getCreatedByContrast() {
        return createdByContrast;
    }

    public void setCreatedByContrast(String createdByContrast) {
        this.createdByContrast = createdByContrast;
    }

    public String getCreatedDateContrast() {
        return createdDateContrast;
    }

    public void setCreatedDateContrast(String createdDateContrast) {
        this.createdDateContrast = createdDateContrast;
    }

    public String getLastUpdByContrast() {
        return lastUpdByContrast;
    }

    public void setLastUpdByContrast(String lastUpdByContrast) {
        this.lastUpdByContrast = lastUpdByContrast;
    }

    public String getLastUpdDateContrast() {
        return lastUpdDateContrast;
    }

    public void setLastUpdDateContrast(String lastUpdDateContrast) {
        this.lastUpdDateContrast = lastUpdDateContrast;
    }

    public Integer getModificationNumContrast() {
        return modificationNumContrast;
    }

    public void setModificationNumContrast(Integer modificationNumContrast) {
        this.modificationNumContrast = modificationNumContrast;
    }

    public String getStrNo() {
        return strNo;
    }

    public void setStrNo(String strNo) {
        this.strNo = strNo;
    }

    public String getStrNoContrast() {
        return strNoContrast;
    }

    public void setStrNoContrast(String strNoContrast) {
        this.strNoContrast = strNoContrast;
    }

    public String getCorrespondingItemNoContrast() {
        return correspondingItemNoContrast;
    }

    public void setCorrespondingItemNoContrast(String correspondingItemNoContrast) {
        this.correspondingItemNoContrast = correspondingItemNoContrast;
    }

    public String getSpareField() {
        return spareField;
    }

    public void setSpareField(String spareField) {
        this.spareField = spareField;
    }
}