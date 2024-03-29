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

public class CttItemShow implements Serializable {
    /*编号，用作显示用，不在数据库里存储，动态显示.依据grade,orderid*/
    private String strNo;
    /*对应Pkid，用作显示用，不在数据库里存储，动态显示。依据correspondingPkid*/
    private String strCorrespondingItemPkid;
    /*对应编号，用作显示用，不在数据库里存储，动态显示。依据correspondingPkid*/
    private String strCorrespondingItemNo;
    /*对应编号名称，用作显示用，不在数据库里存储，动态显示。依据correspondingPkid*/
    private String strCorrespondingItemName;

    /*EPSS.ES_CTT_ITEM.PKID*/
    private String pkid;
    /*EPSS.ES_CTT_ITEM.BELONG_TO_TYPE*/
    private String belongToType;
    /*EPSS.ES_CTT_ITEM.BELONG_TO_PKID*/
    private String belongToPkid;
    /*EPSS.ES_CTT_ITEM.ORDERID*/
    private Integer orderid;
    /*EPSS.ES_CTT_ITEM.GRADE*/
    private Integer grade;
    /*EPSS.ES_CTT_ITEM.PARENT_PKID*/
    private String parentPkid;
    /*EPSS.ES_CTT_ITEM.CORRESPONDING_PKID*/
    private String correspondingPkid;

    /*EPSS.ES_CTT_ITEM.NAME/EPSS.ES_ITEM_INFO.NAME*/
    private String name;
    /*EPSS.ES_CTT_ITEM.NOTE/EPSS.ES_ITEM_INFO.NOTE*/
    private String note;

    /*EPSS.ES_ITEM_INFO.UNIT*/
    private String unit;
    /*EPSS.ES_ITEM_INFO.CONTRACT_UNIT_PRICE*/
    private BigDecimal contractUnitPrice;
    /*EPSS.ES_ITEM_INFO.CONTRACT_QUANTITY*/
    private BigDecimal contractQuantity;
    /*EPSS.ES_ITEM_INFO.CONTRACT_AMOUNT*/
    private BigDecimal contractAmount;
    /*EPSS.ES_ITEM_INFO.SIGN_PART_A_PRICE*/
    private BigDecimal signPartAPrice;
    /*EPSS.ES_ITEM_INFO.DELETED_FLAG*/
    private String deletedFlag;
    /*EPSS.ES_ITEM_INFO.ORIGIN_FLAG*/
    private String originFlag;
    /*EPSS.ES_ITEM_INFO.CREATED_BY*/
    private String createdBy;
    /*EPSS.ES_ITEM_INFO.CREATED_DATE*/
    private String createdDate;
    /*EPSS.ES_ITEM_INFO.LAST_UPD_BY*/
    private String lastUpdBy;
    /*EPSS.ES_ITEM_INFO.LAST_UPD_DATE*/
    private String lastUpdDate;
    /*EPSS.ES_ITEM_INFO.MODIFICATION_NUM*/
    private Integer modificationNum;

    private String createdByName;
    private String lastUpdByName;

    public CttItemShow() {

    }

    public CttItemShow(String strBelongToType,
                       String strBelongToPkid) {
        //To change body of created methods use File | Settings | File Templates.
        /*EPSS.ES_CTT_ITEM.BelongToType*/
        this.belongToType =strBelongToType ;
        /*EPSS.ES_CTT_ITEM.ITEMBELONGTOPKID*/
        this.belongToPkid =strBelongToPkid ;
    }

    public CttItemShow(
            String strPkId,
            String strBelongToType,
            String strBelongToPkid,
            String strParentPkid,
            Integer intGrade,
            Integer intOrderid,
            String strName,
            String strUnit,
            BigDecimal bdContractUnitPrice,
            BigDecimal bdContractQuantity,
            BigDecimal bdContractAmount,
            BigDecimal bdSignPartAPrice,
            String strDeletedFlag,
            String strOriginFlag,
            String strCreatedBy,
            String strCreatedByName,
            String dtCreatedDate,
            String strLastUpdBy,
            String strLastUpdByName,
            String dtLastUpdDate,
            Integer intModificationNum,
            String strNote,
            String strCorrespondingPkid,
            String strNo,
            String strCorrespondingItemNo) {
        /*EPSS.ES_ITEM_INFO.PKID*/
        this.pkid=strPkId;
        /*EPSS.ES_CTT_ITEM.BelongToType*/
        this.belongToType =strBelongToType ;
        /*EPSS.ES_CTT_ITEM.ITEMBELONGTOPKID*/
        this.belongToPkid =strBelongToPkid ;
        this.strNo =strNo ;
        this.orderid =intOrderid;
        /*EPSS.ES_ITEM_INFO.ID*/
        this.grade=intGrade;
        /*EPSS.ES_ITEM_INFO.NAME*/
        this.name=strName;
        this.parentPkid =strParentPkid;
        /*EPSS.ES_ITEM_INFO.NOTE*/
        this.note=strNote;
        /*EPSS.ES_CTT_ITEM.CORRESPONDING_PKID*/
        this.correspondingPkid =strCorrespondingPkid;

        /*EPSS.ES_ITEM_INFO.UNIT*/
        this.unit=strUnit;
        /*EPSS.ES_ITEM_INFO.CONTRACT_UNIT_PRICE*/
        this.contractUnitPrice=bdContractUnitPrice;
        /*EPSS.ES_ITEM_INFO.CONTRACT_QUANTITY*/
        this.contractQuantity=bdContractQuantity;
        /*EPSS.ES_ITEM_INFO.CONTRACT_AMOUNT*/
        this.contractAmount=bdContractAmount;
        /*EPSS.ES_ITEM_INFO.SIGN_PART_A_PRICE*/
        this.signPartAPrice=bdSignPartAPrice;
        /*EPSS.ES_ITEM_INFO.DELETED_FLAG*/
        this.deletedFlag=strDeletedFlag;
        /*EPSS.ES_ITEM_INFO.ORIGIN_FLAG*/
        this.originFlag=strOriginFlag;
        /*EPSS.ES_ITEM_INFO.CREATED_BY*/
        this.createdBy=strCreatedBy;
        this.createdByName=strCreatedByName;
        /*EPSS.ES_ITEM_INFO.CREATED_DATE*/
        this.createdDate=dtCreatedDate;
        /*EPSS.ES_ITEM_INFO.LAST_UPD_BY*/
        this.lastUpdBy=strLastUpdBy;
        this.lastUpdByName=strLastUpdByName;
        /*EPSS.ES_ITEM_INFO.LAST_UPD_DATE*/
        this.lastUpdDate=dtLastUpdDate;
        /*EPSS.ES_ITEM_INFO.MODIFICATION_NUM*/
        this.modificationNum=intModificationNum;

        this.strNo =strNo ;
        this.strCorrespondingItemNo =strCorrespondingItemNo;
    }

    public boolean equals(Object obj)
    {
        if (this == obj){
            return true;
        }

        if (obj.getClass() == CttItemShow.class)
        {
            CttItemShow itemForTkcttAndCstpl = (CttItemShow)obj;
            return ((itemForTkcttAndCstpl.pkid==null&&this.pkid==null)||
                      itemForTkcttAndCstpl.pkid.equals(this.pkid))
                    &&((itemForTkcttAndCstpl.belongToType==null&&this.belongToType==null)||
                        itemForTkcttAndCstpl.belongToType .equals(this.belongToType))
                    &&((itemForTkcttAndCstpl.belongToPkid==null&&this.belongToPkid==null)||
                        itemForTkcttAndCstpl.belongToPkid .equals(this.belongToPkid))
                    &&((itemForTkcttAndCstpl.strNo==null&&this.strNo==null)||
                        itemForTkcttAndCstpl.strNo .equals(this.strNo))
                    &&(itemForTkcttAndCstpl.orderid==this.orderid)
                    &&(itemForTkcttAndCstpl.grade==this.grade)
                    &&((itemForTkcttAndCstpl.name==null&&this.name==null)||
                        itemForTkcttAndCstpl.name .equals(this.name))
                    &&((itemForTkcttAndCstpl.parentPkid==null&&this.parentPkid==null)||
                        itemForTkcttAndCstpl.parentPkid .equals(this.parentPkid))
                    &&((itemForTkcttAndCstpl.note==null&&this.note==null)||
                        itemForTkcttAndCstpl.note .equals(this.note))
                    &&((itemForTkcttAndCstpl.correspondingPkid==null&&this.correspondingPkid==null)||
                        itemForTkcttAndCstpl.correspondingPkid .equals(this.correspondingPkid))
                    &&((itemForTkcttAndCstpl.unit==null&&this.unit==null)||
                        itemForTkcttAndCstpl.unit .equals(this.unit))
                    &&(itemForTkcttAndCstpl.contractUnitPrice==this.contractUnitPrice)
                    &&(itemForTkcttAndCstpl.contractQuantity==this.contractQuantity)
                    &&(itemForTkcttAndCstpl.contractAmount==this.contractAmount)
                    &&(itemForTkcttAndCstpl.signPartAPrice==this.signPartAPrice)
                    &&((itemForTkcttAndCstpl.deletedFlag==null&&this.deletedFlag==null)||
                        itemForTkcttAndCstpl.deletedFlag .equals(this.deletedFlag))
                    &&((itemForTkcttAndCstpl.originFlag==null&&this.originFlag==null)||
                        itemForTkcttAndCstpl.originFlag .equals(this.originFlag))
                    &&((itemForTkcttAndCstpl.createdBy==null&&this.createdBy==null)||
                        itemForTkcttAndCstpl.createdBy .equals(this.createdBy))
                    &&((itemForTkcttAndCstpl.createdDate==null&&this.createdDate==null)||
                        itemForTkcttAndCstpl.createdDate .equals(this.createdDate))
                    &&((itemForTkcttAndCstpl.lastUpdBy==null&&this.lastUpdBy==null)||
                        itemForTkcttAndCstpl.lastUpdBy .equals(this.lastUpdBy))
                    &&((itemForTkcttAndCstpl.lastUpdDate==null&&this.lastUpdDate==null)||
                        itemForTkcttAndCstpl.lastUpdDate .equals(this.lastUpdDate))
                    &&(itemForTkcttAndCstpl.modificationNum==this.modificationNum)
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

    public String getStrNo() {
        return strNo;
    }

    public void setStrNo(String strNo) {
        this.strNo = strNo;
    }

    public String getStrCorrespondingItemNo() {
        return strCorrespondingItemNo;
    }

    public void setStrCorrespondingItemNo(String strCorrespondingItemNo) {
        this.strCorrespondingItemNo = strCorrespondingItemNo;
    }

    public String getStrCorrespondingItemName() {
        return strCorrespondingItemName;
    }

    public void setStrCorrespondingItemName(String strCorrespondingItemName) {
        this.strCorrespondingItemName = strCorrespondingItemName;
    }

    public String getStrCorrespondingItemPkid() {
        return strCorrespondingItemPkid;
    }

    public void setStrCorrespondingItemPkid(String strCorrespondingItemPkid) {
        this.strCorrespondingItemPkid = strCorrespondingItemPkid;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getLastUpdByName() {
        return lastUpdByName;
    }

    public void setLastUpdByName(String lastUpdByName) {
        this.lastUpdByName = lastUpdByName;
    }
}