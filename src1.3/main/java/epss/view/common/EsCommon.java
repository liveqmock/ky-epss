package epss.view.common;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-3-26
 * Time: 下午6:12
 * To change this template use File | Settings | File Templates.
 */

import epss.common.utils.ToolUtil;
import platform.service.PlatformService;
import epss.repository.model.SignPart;
import epss.service.EsCommonService;
import epss.service.SignPartService;
import org.apache.commons.lang.StringUtils;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.view.build.utils.Util;
import platform.repository.model.Ptoper;
import platform.service.ToolsService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.model.SelectItem;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by IntelliJ IDEA
 * User Think
 * Date 13-3-26
 * Time 下午6:12
 */
@ManagedBean
@ViewScoped
public class EsCommon implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(EsCommon.class);

    @ManagedProperty(value = "#{toolsService}")
    private ToolsService toolsService;
    @ManagedProperty(value = "#{signPartService}")
    private SignPartService signPartService;
    @ManagedProperty(value = "#{esCommonService}")
    private EsCommonService esCommonService;

    private HtmlGraphicImage image;

    private List<SelectItem> originFlagList;

    private List<SelectItem> itemTypeList; // ITEM追加类型列表
    //成本计划用
    private List<SelectItem> cstplItemNamelist;
    //分包合同用
    private List<SelectItem> subcttItemNamelist;
    //客户信息用
    private List<SelectItem> customerlist;

    //上传下载文件
    private StreamedContent downloadFile;
    private UploadedFile uploadedFile;
    private List<SelectItem> deleteFlagList;
    private List<SelectItem> endFlagList;

    private String strDateThisPeriod;
    private String strDateLastPeriod;

    private String strToday;
    private BigDecimal bigDecimal0;
    // 操作员列表
    private Map<String,String> opersMap;

    @PostConstruct
    public void init() {
        opersMap=new HashMap<String, String>();
        List<Ptoper> ptoperList=esCommonService.getOperList();
        for(Ptoper itemUnit:ptoperList){
            opersMap.put(itemUnit.getOperid(), itemUnit.getOpername());
        }

        this.originFlagList = toolsService.getEnuSelectItemList("ORIGIN_FLAG", false, false);
        this.itemTypeList = toolsService.getEnuSelectItemList("ITEM_TYPE", true, false);
        this.cstplItemNamelist = toolsService.getEnuSelectItemList("CSTPLITEM_NAME", false, false);
        this.subcttItemNamelist= toolsService.getEnuSelectItemList("SUBCTTITEM_NAME", false, false);
        this.deleteFlagList = toolsService.getEnuSelectItemList("ARCHIVED_FLAG", true, false);
        this.endFlagList = toolsService.getEnuSelectItemList("END_FLAG", true, false);

        List<SignPart> signPartList = signPartService.selectListByModel();
        customerlist=new ArrayList<SelectItem>();
        SelectItem selectItem=new SelectItem("","全部");
        customerlist.add(selectItem);
        for(SignPart itemUnit: signPartList){
            selectItem=new SelectItem();
            selectItem.setValue(itemUnit.getPkid());
            selectItem.setLabel(itemUnit.getName());
            customerlist.add(selectItem);
        }

        Date dateNow =new Date();
        Date dateLast = ToolUtil.getLastMonthDate(dateNow);

        strDateThisPeriod= PlatformService.dateFormat(dateNow, "yyyyMM");
        strDateLastPeriod= PlatformService.dateFormat(dateLast, "yyyyMM");
        strToday= PlatformService.dateFormat(dateLast, "yyyyMMdd");

        bigDecimal0=new BigDecimal(0);
    }

    public String getCustNameByCustIdFromList(String strCustId){
        for(SelectItem itemUnit:customerlist){
            if(itemUnit.getValue().equals(strCustId)){
                return itemUnit.getLabel();
            }
        }
        return "";
    }

    public String getOperNameByOperId(String strOperId){
        return Util.getUserName(strOperId);
    }

    public String originFlagListValueOfAlias(String strValue){
        if(!StringUtils.isEmpty(strValue))
        {
            return  originFlagList.get(Integer .parseInt(strValue)).getLabel() ;
        }
        return "";
    }

    public String getLabelByValueInDeleteFlaglist(String strValue){
        if(!StringUtils.isEmpty(strValue)){
            for(SelectItem itemUnit:deleteFlagList){
                if(itemUnit.getValue().equals(strValue)){
                    return itemUnit.getLabel();
                }
            }
        }
        return "";
    }
    public String getValueByLabelInDeleteFlaglist(String strLabel){
        if(!StringUtils.isEmpty(strLabel)){
            for(SelectItem itemUnit:deleteFlagList){
                if(itemUnit.getLabel().equals(strLabel)){
                    return itemUnit.getValue().toString();
                }
            }
        }
        return "";
    }

    public String itemTypeListValueOfAlias(String strValue){
        if(!StringUtils.isEmpty(strValue)){
            if(itemTypeList.get(0).getLabel().equals("全部")){
                return  itemTypeList.get(Integer .parseInt(strValue)+1).getLabel() ;
            }else{
                return  itemTypeList.get(Integer .parseInt(strValue)).getLabel() ;
            }
        }
        else{
            return "";
        }
    }

    public Integer getIndexOfCstplItemNamelist(String strLabel){
        if(!StringUtils.isEmpty(strLabel)){
            for(SelectItem itemUnit:cstplItemNamelist){
                if(itemUnit.getLabel().equals(strLabel)){
                    return cstplItemNamelist.indexOf(itemUnit);
                }
            }
        }
        return -1;
    }
    public Integer getIndexOfSubcttItemNamelist(String strLabel){
        if(!StringUtils.isEmpty(strLabel)){
            for(SelectItem itemUnit:subcttItemNamelist){
                if(itemUnit.getLabel().equals(strLabel)){
                    return subcttItemNamelist.indexOf(itemUnit);
                }
            }
        }
        return -1;
    }

    public String getLabelByValueInItemTypeList(String strValue){
        if(!StringUtils.isEmpty(strValue)){
            for(SelectItem itemUnit:itemTypeList){
                if(itemUnit.getValue().equals(strValue)){
                    return itemUnit.getLabel();
                }
            }
        }
        return "";
    }

    //职能字段 begin

    public static Logger getLogger() {
        return logger;
    }

    public List<SelectItem> getDeleteFlagList() {
        return deleteFlagList;
    }

    public void setDeleteFlagList(List<SelectItem> deleteFlagList) {
        this.deleteFlagList = deleteFlagList;
    }

    public List<SelectItem> getEndFlagList() {
        return endFlagList;
    }

    public void setEndFlagList(List<SelectItem> endFlagList) {
        this.endFlagList = endFlagList;
    }

    public ToolsService getToolsService() {
        return toolsService;
    }

    public void setToolsService(ToolsService toolsService) {
        this.toolsService = toolsService;
    }

    public SignPartService getSignPartService() {
        return signPartService;
    }

    public void setSignPartService(SignPartService signPartService) {
        this.signPartService = signPartService;
    }

    public EsCommonService getEsCommonService() {
        return esCommonService;
    }

    public void setEsCommonService(EsCommonService esCommonService) {
        this.esCommonService = esCommonService;
    }

    public HtmlGraphicImage getImage() {
        return image;
    }

    public void setImage(HtmlGraphicImage image) {
        this.image = image;
    }

    public List<SelectItem> getOriginFlagList() {
        return originFlagList;
    }

    public void setOriginFlagList(List<SelectItem> originFlagList) {
        this.originFlagList = originFlagList;
    }

    public List<SelectItem> getItemTypeList() {
        return itemTypeList;
    }

    public void setItemTypeList(List<SelectItem> itemTypeList) {
        this.itemTypeList = itemTypeList;
    }

    public List<SelectItem> getCstplItemNamelist() {
        return cstplItemNamelist;
    }

    public void setCstplItemNamelist(List<SelectItem> cstplItemNamelist) {
        this.cstplItemNamelist = cstplItemNamelist;
    }

    public StreamedContent getDownloadFile() {
        return downloadFile;
    }

    public void setDownloadFile(StreamedContent downloadFile) {
        this.downloadFile = downloadFile;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public List<SelectItem> getSubcttItemNamelist() {
        return subcttItemNamelist;
    }

    public void setSubcttItemNamelist(List<SelectItem> subcttItemNamelist) {
        this.subcttItemNamelist = subcttItemNamelist;
    }

    public List<SelectItem> getCustomerlist() {
        return customerlist;
    }

    public void setCustomerlist(List<SelectItem> customerlist) {
        this.customerlist = customerlist;
    }

    public String getStrDateThisPeriod() {
        return strDateThisPeriod;
    }

    public void setStrDateThisPeriod(String strDateThisPeriod) {
        this.strDateThisPeriod = strDateThisPeriod;
    }

    public String getStrDateLastPeriod() {
        return strDateLastPeriod;
    }

    public void setStrDateLastPeriod(String strDateLastPeriod) {
        this.strDateLastPeriod = strDateLastPeriod;
    }

    public String getStrToday() {
        return strToday;
    }

    public void setStrToday(String strToday) {
        this.strToday = strToday;
    }

    public BigDecimal getBigDecimal0() {
        return bigDecimal0;
    }

    public Map<String, String> getOpersMap() {
        return opersMap;
    }

    public void setOpersMap(Map<String, String> opersMap) {
        this.opersMap = opersMap;
    }
//职能字段 End
}
