package epss.service.common;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumStatusFlag;
import epss.repository.model.model_show.CttInfoShow;
import epss.repository.model.model_show.ProgInfoShow;
import epss.common.utils.ToolUtil;
import epss.repository.dao.common.FlowMapper;
import epss.repository.model.EsInitPower;
import epss.repository.model.EsInitPowerHis;
import epss.repository.model.EsInitStl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-7-1
 * Time: 下午4:21
 * To change this template use File | Settings | File Templates.
 */
@Service
public class EsFlowService {
    @Autowired
    private FlowMapper flowMapper;

    public List<CttInfoShow> selectCttByStatusFlagBegin_End(CttInfoShow cttInfoShowPara){
        return flowMapper.selectCttByStatusFlagBegin_End(cttInfoShowPara);
    }

    public List<ProgInfoShow> selectSubcttStlQMByStatusFlagBegin_End(ProgInfoShow progInfoShowPara){
        return flowMapper.selectSubcttStlQMByStatusFlagBegin_End(progInfoShowPara);
    }

    public List<ProgInfoShow> selectNotFormEsInitSubcttStlP(String strParentPkid,
                                                             String strStlPkid,
                                                             String strPeriodNo){
        return flowMapper.selectNotFormEsInitSubcttStlP(strParentPkid, strStlPkid, strPeriodNo);
    }
    public List<ProgInfoShow> selectFormPreEsInitSubcttStlP(String strParentPkid,
                                                              String strStlPkid,
                                                              String strPeriodNo){
        return flowMapper.selectFormPreEsInitSubcttStlP(strParentPkid,strStlPkid,strPeriodNo);
    }
    public List<ProgInfoShow> selectFormingEsInitSubcttStlP(String strParentPkid,
                                                              String strStlPkid,
                                                              String strPeriodNo){
        return flowMapper.selectFormingEsInitSubcttStlP(strParentPkid,strStlPkid,strPeriodNo);
    }
    public List<ProgInfoShow> selectFormedEsInitSubcttStlP(String strParentPkid,
                                                             String strStlPkid,
                                                             String strPeriodNo){
        return flowMapper.selectFormedEsInitSubcttStlPList(strParentPkid,strStlPkid,strPeriodNo);
    }

    public List<ProgInfoShow> getFormedAfterEsInitSubcttStlPList(String strParentPkid,
                                                                  String strStlPkid,
                                                                  String strPeriodNo){
        return flowMapper.getFormedAfterEsInitSubcttStlPList(strParentPkid,strStlPkid,strPeriodNo);
    }

    public List<EsInitStl> selectIsUsedInQMPBySubcttPkid(String strSubcttPkid){
        return flowMapper.selectIsUsedInQMPBySubcttPkid(strSubcttPkid);
    }

    public List<ProgInfoShow> selectTkcttStlSMByStatusFlagBegin_End(ProgInfoShow progInfoShowPara){
        return flowMapper.selectTkcttStlSMByStatusFlagBegin_End(progInfoShowPara);
    }

    public String getLatestDoubleCkeckedPeriodNo(String strPowerType,String strPowerPkid) {
        return flowMapper.getLatestDoubleCkeckedPeriodNo(strPowerType, strPowerPkid);
    }

    public String getLatestApprovedPeriodNo(String strPowerType,String strPowerPkid) {
        return flowMapper.getLatestApprovedPeriodNo(strPowerType, strPowerPkid);
    }

    public String getLatestApprovedPeriodNoByEndPeriod(String strPowerType,String strPowerPkid,String strEndPeriodNo) {
        return flowMapper.getLatestApprovedPeriodNoByEndPeriod(strPowerType, strPowerPkid,strEndPeriodNo);
    }

    private EsInitPowerHis  fromEsInitPowerToEsInitPowerHis(EsInitPower esInitPowerPara,String strOperType){
        EsInitPowerHis esInitPowerHis =new EsInitPowerHis();
        esInitPowerHis.setPowerType(esInitPowerPara.getPowerType());
        esInitPowerHis.setPowerPkid(esInitPowerPara.getPowerPkid());
        esInitPowerHis.setPeriodNo(esInitPowerPara.getPeriodNo());
        esInitPowerHis.setStatusFlag(esInitPowerPara.getStatusFlag());
        esInitPowerHis.setPreStatusFlag(esInitPowerPara.getPreStatusFlag());
        esInitPowerHis.setCreatedDate(esInitPowerPara.getCreatedDate());
        esInitPowerHis.setCreatedBy(esInitPowerPara.getCreatedBy());
        esInitPowerHis.setSpareField(strOperType);
        return esInitPowerHis;
    }

    public Integer getChildrenOfThisRecordInEsInitCtt(String strCttType,String strBelongToPkid){
        return flowMapper.getChildrenOfThisRecordInEsInitCtt(strCttType,strBelongToPkid);
    }

    public String subCttStlCheckForMng(String stlType,String subCttPkid,String periodNo) {
        String strReturnTemp="";
        if(ESEnum.ITEMTYPE3.getCode().equals(stlType)||ESEnum.ITEMTYPE4.getCode().equals(stlType)){ //分包结算
            String quantityMaxPeriod = ToolUtil.getStrIgnoreNull(
                    getMaxPeriodNo(ESEnum.ITEMTYPE3.getCode(),subCttPkid));
            String materialMaxPeriod = ToolUtil.getStrIgnoreNull(
                    getMaxPeriodNo(ESEnum.ITEMTYPE4.getCode(),subCttPkid));

            String quantityStatus=ToolUtil.getStrIgnoreNull(
                    getStatusFlag(ESEnum.ITEMTYPE3.getCode(),subCttPkid,quantityMaxPeriod));
            String materialStatus=ToolUtil.getStrIgnoreNull(
                    getStatusFlag(ESEnum.ITEMTYPE4.getCode(),subCttPkid,materialMaxPeriod));

            System.out.println("\n数量结算最大期号："+quantityMaxPeriod+"\n材料结算最大期号："+materialMaxPeriod);
            System.out.println("\n数量结算最大期号对应状态标志："+quantityStatus+"\n材料结算最大期号对应状态标志："+materialStatus);

            if (ESEnum.ITEMTYPE3.getCode().equals(stlType)){
                if (periodNo.compareTo(quantityMaxPeriod)<=0){ //首先和自身比较期号大小，如果比自身小或者等于则不能录入
                    strReturnTemp="应录入大于[" + quantityMaxPeriod + "]期的分包数量结算数据!";
                    return strReturnTemp;
                }else {
                    if (quantityStatus.equals("")&&!quantityMaxPeriod.equals("")){
                        strReturnTemp="分包数量结算第["+quantityMaxPeriod+"]期数据还未批准通过，不能录入新数据!";
                        return strReturnTemp;
                    }
                    if (ESEnumStatusFlag.STATUS_FLAG2.getCode().compareTo(quantityStatus)>0){ //判断是否有非批准状态的数据存在，如果有不能录入
                        if (!quantityStatus.equals("")){
                            strReturnTemp="分包数量结算第["+quantityMaxPeriod+"]期数据还未批准通过，不能录入新数据！";
                            return strReturnTemp;
                        }else {
                            if (quantityMaxPeriod.compareTo(materialMaxPeriod)<0&&periodNo.compareTo(materialMaxPeriod)!=0){
                                strReturnTemp="第["+materialMaxPeriod+"]期分包材料结算已经开始，请录入["+materialMaxPeriod+"]期的分包数量结算数据！";
                                return strReturnTemp;
                            }
                        }
                    } else{//（>quantityMaxPeriod &&=3）在以上两个条件均不满足的情况下，此时说明和自身比较没有问题，接下来要和材料结算比较
                        if (quantityMaxPeriod.compareTo(materialMaxPeriod)<0){
                            if (periodNo.compareTo(materialMaxPeriod)!=0){
                                strReturnTemp="第["+materialMaxPeriod+"]期分包材料结算已经开始，请录入["+materialMaxPeriod+"]期的分包数量结算数据！";
                                return strReturnTemp;
                            }
                        }
                    }
                }
            }
            if (ESEnum.ITEMTYPE4.getCode().equals(stlType)){
                if (periodNo.compareTo(materialMaxPeriod)<=0){ //首先和自身比较期号大小，如果比自身小或者等于则不能录入
                    strReturnTemp="应录入大于["+materialMaxPeriod+"]期的分包材料结算数据!";
                    return strReturnTemp;
                }else {
                    if (materialStatus.equals("")&&!materialMaxPeriod.equals("")){
                        strReturnTemp="分包材料结算第["+materialMaxPeriod+"]期数据还未批准通过，不能录入新数据!";
                        return strReturnTemp;
                    }

                    if (ESEnumStatusFlag.STATUS_FLAG2.getCode().compareTo(materialStatus)>0){ //判断是否有非批准状态的数据存在，如果有不能录入
                        if (!materialMaxPeriod.equals("")){
                            strReturnTemp="分包材料结算第["+materialMaxPeriod+"]期数据还未批准通过，不能录入新数据！";
                            return strReturnTemp;
                        }else {
                            if (materialMaxPeriod.compareTo(quantityMaxPeriod)<0&&periodNo.compareTo(quantityMaxPeriod)!=0){
                                strReturnTemp="第["+quantityMaxPeriod+"]期分包数量结算已经开始，请录入["+quantityMaxPeriod+"]期的分包材料结算数据！";
                                return strReturnTemp;
                            }
                        }
                    } else{//（>materialMaxPeriod &&=3）在以上两个条件均不满足的情况下，此时说明和自身比较没有问题，接下来要和材料结算比较
                        if (materialMaxPeriod.compareTo(quantityMaxPeriod)<0){
                            if (periodNo.compareTo(quantityMaxPeriod)!=0){
                                strReturnTemp="第["+quantityMaxPeriod+"]期分包数量结算已经开始，请录入["+quantityMaxPeriod+"]期的分包材料结算数据！";
                                return strReturnTemp;
                            }
                        }
                    }
                }
            }
        }else if(ESEnum.ITEMTYPE6.getCode().equals(stlType)||ESEnum.ITEMTYPE7.getCode().equals(stlType)){// 总包结算
            if (ESEnum.ITEMTYPE6.getCode().equals(stlType)){
                String strStaQtyMaxPeriod = ToolUtil.getStrIgnoreNull(
                        getMaxPeriodNo(ESEnum.ITEMTYPE6.getCode(),subCttPkid));
                String strStaQtyMaxPeriodStatus=ToolUtil.getStrIgnoreNull(
                        getStatusFlag(ESEnum.ITEMTYPE6.getCode(),subCttPkid,strStaQtyMaxPeriod));

                if (periodNo.compareTo(strStaQtyMaxPeriod)<=0){ //首先和自身比较期号大小，如果比自身小或者等于则不能录入
                    strReturnTemp="应录入大于[" + strStaQtyMaxPeriod + "]期的总包统计数据!";
                    return strReturnTemp;
                }else {
                    if (strStaQtyMaxPeriod.equals("")&&!strStaQtyMaxPeriodStatus.equals("")){
                        strReturnTemp="总包统计结算第["+strStaQtyMaxPeriod+"]期数据还未批准通过，不能录入新数据!";
                        return strReturnTemp;
                    }
                    if (ESEnumStatusFlag.STATUS_FLAG3.getCode().compareTo(strStaQtyMaxPeriodStatus)>0){ //判断是否有非批准状态的数据存在，如果有不能录入
                        if (!strStaQtyMaxPeriodStatus.equals("")){
                            strReturnTemp="总包统计结算第["+strStaQtyMaxPeriod+"]期数据还未批准通过，不能录入新数据！";
                            return strReturnTemp;
                        }
                    }
                }
            }else{
                if (ESEnum.ITEMTYPE7.getCode().equals(stlType)){
                    String strMeaQtyMaxPeriod = ToolUtil.getStrIgnoreNull(
                            getMaxPeriodNo(ESEnum.ITEMTYPE7.getCode(),subCttPkid));
                    String strMeaQtyMaxPeriodStatus=ToolUtil.getStrIgnoreNull(
                            getStatusFlag(ESEnum.ITEMTYPE7.getCode(),subCttPkid,strMeaQtyMaxPeriod));

                    if (periodNo.compareTo(strMeaQtyMaxPeriod)<=0){ //首先和自身比较期号大小，如果比自身小或者等于则不能录入
                        strReturnTemp="应录入大于[" + strMeaQtyMaxPeriod + "]期的总包统计数据!";
                        return strReturnTemp;
                    }else {
                        if (strMeaQtyMaxPeriod.equals("")&&!strMeaQtyMaxPeriodStatus.equals("")){
                            strReturnTemp="总包统计结算第["+strMeaQtyMaxPeriod+"]期数据还未批准通过，不能录入新数据!";
                            return strReturnTemp;
                        }
                        if (ESEnumStatusFlag.STATUS_FLAG3.getCode().compareTo(strMeaQtyMaxPeriodStatus)>0){ //判断是否有非批准状态的数据存在，如果有不能录入
                            if (!strMeaQtyMaxPeriodStatus.equals("")){
                                strReturnTemp="总包统计结算第["+strMeaQtyMaxPeriod+"]期数据还未批准通过，不能录入新数据！";
                                return strReturnTemp;
                            }
                        }
                    }
                }
            }

        }
        return strReturnTemp;
    }

    //TODO add by yxy
    public String getMaxPeriodNo(String stlType, String subCttPkid) {
        return flowMapper.getMaxPeriodNo(stlType,subCttPkid);
    }
    public String getStatusFlag(String stlType,String subCttPkid,String periodNo){
        return flowMapper.getStatusFlag(stlType,subCttPkid,periodNo);
    }
}

