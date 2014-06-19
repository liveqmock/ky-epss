package epss.service;

import epss.common.enums.EnumFlowStatus;
import epss.common.utils.ToolUtil;
import epss.repository.dao.ProgMatqtyInfoMapper;
import epss.repository.dao.notMyBits.CommonMapper;
import epss.repository.dao.notMyBits.MyProgMatqtyInfoMapper;
import epss.repository.dao.notMyBits.MyProgWorkqtyInfoMapper;
import epss.repository.model.ProgMatqtyInfo;
import epss.repository.model.ProgMatqtyInfoExample;
import epss.repository.model.model_show.ProgMatqtyInfoShow;
import org.springframework.stereotype.Service;
import platform.service.PlatformService;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-5-20
 * Time: 上午10:02
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ProgMatqtyInfoService {
    @Resource
    private ProgMatqtyInfoMapper progMatqtyInfoMapper;
    @Resource
    private MyProgMatqtyInfoMapper myProgMatqtyInfoMapper;
    @Resource
    private MyProgWorkqtyInfoMapper myProgWorkqtyInfoMapper;
    @Resource
    private PlatformService platformService;
    @Resource
    private CommonMapper commonMapper;

    /**
     * 判断记录是否已存在
     *
     * @param progMatqtyInfoShowPara
     * @return
     */
    public List<ProgMatqtyInfo> getProgMatqtyInfoListByModel(ProgMatqtyInfoShow progMatqtyInfoShowPara) {
        ProgMatqtyInfoExample example = new ProgMatqtyInfoExample();
        example.createCriteria().andSubcttInfoPkidEqualTo(progMatqtyInfoShowPara.getSubcttInfoPkid())
                                .andStageNoEqualTo(progMatqtyInfoShowPara.getStageNo());
        return progMatqtyInfoMapper.selectByExample(example);
    }

    public List<ProgMatqtyInfo> getProgMatqtyInfoListByModel(ProgMatqtyInfo progMatqtyInfoPara) {
        ProgMatqtyInfoExample example = new ProgMatqtyInfoExample();
        example.createCriteria().andSubcttInfoPkidEqualTo(progMatqtyInfoPara.getSubcttInfoPkid())
                .andStageNoEqualTo(progMatqtyInfoPara.getStageNo());
        return progMatqtyInfoMapper.selectByExample(example);
    }

    public List<ProgMatqtyInfoShow> getProgMatqtyInfoListByFlowStatusBegin_End(ProgMatqtyInfoShow progMatqtyInfoShowPara){
        return myProgMatqtyInfoMapper.getProgMatqtyInfoListByFlowStatusBegin_End(progMatqtyInfoShowPara);
    }

    public String getStrMaxProgMatqtyInfoId(){
        return myProgMatqtyInfoMapper.getStrMaxProgMatqtyInfoId();
    }

    public String getLatestStageNo(String strSubcttInfoPkidPara,String strFlowStatusPara) {
        return myProgMatqtyInfoMapper.getLatestStageNo(strSubcttInfoPkidPara,strFlowStatusPara);
    }

    public String getLatestStageNoByEndStage(String strSubcttInfoPkidPara,
                                              String strEndStageNoPara,
                                              String strFlowStatusPara) {
        return myProgMatqtyInfoMapper.getLatestStageNoByEndStage(
                strSubcttInfoPkidPara,
                strEndStageNoPara,
                strFlowStatusPara);
    }

    private ProgMatqtyInfo fromShowModelToModel(ProgMatqtyInfoShow progMatqtyInfoShowPara){
        ProgMatqtyInfo progMatqtyInfoTemp =new ProgMatqtyInfo();
        progMatqtyInfoTemp.setPkid(progMatqtyInfoShowPara.getPkid());
        progMatqtyInfoTemp.setId(progMatqtyInfoShowPara.getId());
        progMatqtyInfoTemp.setSubcttInfoPkid(progMatqtyInfoShowPara.getSubcttInfoPkid());
        progMatqtyInfoTemp.setStageNo(progMatqtyInfoShowPara.getStageNo());
        progMatqtyInfoTemp.setArchivedFlag(progMatqtyInfoShowPara.getArchivedFlag());
        progMatqtyInfoTemp.setOriginFlag(progMatqtyInfoShowPara.getOriginFlag());
        progMatqtyInfoTemp.setFlowStatus(progMatqtyInfoShowPara.getFlowStatus());
        progMatqtyInfoTemp.setFlowStatusRemark(progMatqtyInfoShowPara.getFlowStatusRemark());
        progMatqtyInfoTemp.setCreatedBy(progMatqtyInfoShowPara.getCreatedBy());
        progMatqtyInfoTemp.setCreatedTime(progMatqtyInfoShowPara.getCreatedTime());
        progMatqtyInfoTemp.setUpdatedBy(progMatqtyInfoShowPara.getUpdatedBy());
        progMatqtyInfoTemp.setUpdatedTime(progMatqtyInfoShowPara.getUpdatedTime());
        progMatqtyInfoTemp.setRecVersion(progMatqtyInfoShowPara.getRecVersion());
        progMatqtyInfoTemp.setAttachment(progMatqtyInfoShowPara.getAttachment());
        progMatqtyInfoTemp.setRemark(progMatqtyInfoShowPara.getRemark());
        progMatqtyInfoTemp.setTid(progMatqtyInfoShowPara.getTid());
        return progMatqtyInfoTemp;
    }

    public ProgMatqtyInfo selectRecordsByPrimaryKey(String strPkId){
        return progMatqtyInfoMapper.selectByPrimaryKey(strPkId);
    }
    public void insertRecord(ProgMatqtyInfoShow progMatqtyInfoShowPara){
        progMatqtyInfoShowPara.setCreatedBy(platformService.getStrLastUpdBy());
        progMatqtyInfoShowPara.setCreatedByName(commonMapper.selectOpernameByCreatedBy(platformService.getStrLastUpdBy()));
        progMatqtyInfoShowPara.setCreatedTime(platformService.getStrLastUpdTime());
        progMatqtyInfoShowPara.setArchivedFlag("0");
        progMatqtyInfoMapper.insert(fromShowModelToModel(progMatqtyInfoShowPara)) ;
    }
    public void updateRecord(ProgMatqtyInfoShow progMatqtyInfoShowPara){
        progMatqtyInfoShowPara.setRecVersion(
                ToolUtil.getIntIgnoreNull(progMatqtyInfoShowPara.getRecVersion()) + 1);
        progMatqtyInfoShowPara.setArchivedFlag("0");
        progMatqtyInfoShowPara.setUpdatedBy(platformService.getStrLastUpdBy());
        progMatqtyInfoShowPara.setUpdatedByName(commonMapper.selectOpernameByCreatedBy(platformService.getStrLastUpdBy()));
        progMatqtyInfoShowPara.setUpdatedTime(platformService.getStrLastUpdTime());
        progMatqtyInfoMapper.updateByPrimaryKey(fromShowModelToModel(progMatqtyInfoShowPara)) ;
    }

    public void updateRecordByProgSubstlInfo(ProgMatqtyInfoShow progMatqtyInfoShowPara){
        List<ProgMatqtyInfo> progMatqtyInfoListTemp=getProgMatqtyInfoListByModel(progMatqtyInfoShowPara);
        if(progMatqtyInfoListTemp.size()>0){
            ProgMatqtyInfo progMatqtyInfoTemp=progMatqtyInfoListTemp.get(0);
            progMatqtyInfoTemp.setRecVersion(
                    ToolUtil.getIntIgnoreNull(progMatqtyInfoTemp.getRecVersion()) + 1);
            progMatqtyInfoTemp.setArchivedFlag("0");
            progMatqtyInfoTemp.setUpdatedBy(platformService.getStrLastUpdBy());
            progMatqtyInfoTemp.setUpdatedTime(platformService.getStrLastUpdTime());
            progMatqtyInfoTemp.setFlowStatus(progMatqtyInfoShowPara.getFlowStatus());
            progMatqtyInfoTemp.setFlowStatusRemark(progMatqtyInfoShowPara.getFlowStatusRemark());
            progMatqtyInfoMapper.updateByPrimaryKey(progMatqtyInfoTemp) ;
        }
    }
    public void deleteRecord(ProgMatqtyInfoShow progMatqtyInfoShowPara){
        progMatqtyInfoMapper.deleteByPrimaryKey(progMatqtyInfoShowPara.getPkid());
    }

    public int deleteRecord(String strPkId){
        return progMatqtyInfoMapper.deleteByPrimaryKey(strPkId);
    }

    /*public String subCttStlCheckForMng(String stlType,String subCttPkid,String periodNo) {
        String strReturnTemp="";
        if(ESEnum.ITEMTYPE3.getCode().equals(stlType)||ESEnum.ITEMTYPE4.getCode().equals(stlType)){ //分包结算
            String quantityMaxPeriod = ToolUtil.getStrIgnoreNull(
                    getMaxStageNo(ESEnum.ITEMTYPE3.getCode(),subCttPkid));
            String materialMaxPeriod = ToolUtil.getStrIgnoreNull(
                    getMaxStageNo(ESEnum.ITEMTYPE4.getCode(),subCttPkid));

            String quantityStatus=ToolUtil.getStrIgnoreNull(
                    getFlowStatus(ESEnum.ITEMTYPE3.getCode(),subCttPkid,quantityMaxPeriod));
            String materialStatus=ToolUtil.getStrIgnoreNull(
                    getFlowStatus(ESEnum.ITEMTYPE4.getCode(),subCttPkid,materialMaxPeriod));

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
                    if (EnumFlowStatus.FLOW_STATUS3.getCode().compareTo(quantityStatus)>0){ //判断是否有非批准状态的数据存在，如果有不能录入
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

                    if (EnumFlowStatus.FLOW_STATUS3.getCode().compareTo(materialStatus)>0){ //判断是否有非批准状态的数据存在，如果有不能录入
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
                        getMaxStageNo(ESEnum.ITEMTYPE6.getCode(),subCttPkid));
                String strStaQtyMaxPeriodStatus=ToolUtil.getStrIgnoreNull(
                        getFlowStatus(ESEnum.ITEMTYPE6.getCode(),subCttPkid,strStaQtyMaxPeriod));

                if (periodNo.compareTo(strStaQtyMaxPeriod)<=0){ //首先和自身比较期号大小，如果比自身小或者等于则不能录入
                    strReturnTemp="应录入大于[" + strStaQtyMaxPeriod + "]期的总包统计数据!";
                    return strReturnTemp;
                }else {
                    if (strStaQtyMaxPeriod.equals("")&&!strStaQtyMaxPeriodStatus.equals("")){
                        strReturnTemp="总包统计结算第["+strStaQtyMaxPeriod+"]期数据还未批准通过，不能录入新数据!";
                        return strReturnTemp;
                    }
                    if (EnumFlowStatus.FLOW_STATUS3.getCode().compareTo(strStaQtyMaxPeriodStatus)>0){ //判断是否有非批准状态的数据存在，如果有不能录入
                        if (!strStaQtyMaxPeriodStatus.equals("")){
                            strReturnTemp="总包统计结算第["+strStaQtyMaxPeriod+"]期数据还未批准通过，不能录入新数据！";
                            return strReturnTemp;
                        }
                    }
                }
            }else{
                if (ESEnum.ITEMTYPE7.getCode().equals(stlType)){
                    String strMeaQtyMaxPeriod = ToolUtil.getStrIgnoreNull(
                            getMaxStageNo(ESEnum.ITEMTYPE7.getCode(),subCttPkid));
                    String strMeaQtyMaxPeriodStatus=ToolUtil.getStrIgnoreNull(
                            getFlowStatus(ESEnum.ITEMTYPE7.getCode(),subCttPkid,strMeaQtyMaxPeriod));

                    if (periodNo.compareTo(strMeaQtyMaxPeriod)<=0){ //首先和自身比较期号大小，如果比自身小或者等于则不能录入
                        strReturnTemp="应录入大于[" + strMeaQtyMaxPeriod + "]期的总包统计数据!";
                        return strReturnTemp;
                    }else {
                        if (strMeaQtyMaxPeriod.equals("")&&!strMeaQtyMaxPeriodStatus.equals("")){
                            strReturnTemp="总包统计结算第["+strMeaQtyMaxPeriod+"]期数据还未批准通过，不能录入新数据!";
                            return strReturnTemp;
                        }
                        if (EnumFlowStatus.FLOW_STATUS3.getCode().compareTo(strMeaQtyMaxPeriodStatus)>0){ //判断是否有非批准状态的数据存在，如果有不能录入
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
    }*/

    public String subCttStlCheckForMng(String subcttInfoPkidPara, String stageNo) {
        String strReturnTemp = "";
        String quantityMaxPeriod = ToolUtil.getStrIgnoreNull(
                myProgWorkqtyInfoMapper.getMaxStageNo(subcttInfoPkidPara));
        String materialMaxPeriod = ToolUtil.getStrIgnoreNull(
                myProgMatqtyInfoMapper.getMaxStageNo(subcttInfoPkidPara));

        String quantityStatus = ToolUtil.getStrIgnoreNull(
                myProgWorkqtyInfoMapper.getFlowStatus(subcttInfoPkidPara, quantityMaxPeriod));
        String materialStatus = ToolUtil.getStrIgnoreNull(
                myProgMatqtyInfoMapper.getFlowStatus(subcttInfoPkidPara, materialMaxPeriod));

        System.out.println("\n数量结算最大期号：" + quantityMaxPeriod + "\n材料结算最大期号：" + materialMaxPeriod);
        System.out.println("\n数量结算最大期号对应状态标志：" + quantityStatus + "\n材料结算最大期号对应状态标志：" + materialStatus);

        if (stageNo.compareTo(materialMaxPeriod) <= 0) { //首先和自身比较期号大小，如果比自身小或者等于则不能录入
            strReturnTemp = "应录入大于[" + materialMaxPeriod + "]期的分包材料结算数据!";
            return strReturnTemp;
        } else {
            if (materialStatus.equals("") && !materialMaxPeriod.equals("")) {
                strReturnTemp = "分包材料结算第[" + materialMaxPeriod + "]期数据还未批准通过，不能录入新数据!";
                return strReturnTemp;
            }
            if (EnumFlowStatus.FLOW_STATUS3.getCode().compareTo(materialStatus) > 0) { //判断是否有非批准状态的数据存在，如果有不能录入
                if (!materialMaxPeriod.equals("")) {
                    strReturnTemp = "分包材料结算第[" + materialMaxPeriod + "]期数据还未批准通过，不能录入新数据！";
                    return strReturnTemp;
                } else {
                    if (materialMaxPeriod.compareTo(quantityMaxPeriod) < 0 && stageNo.compareTo(quantityMaxPeriod) != 0) {
                        strReturnTemp = "第[" + quantityMaxPeriod + "]期分包数量结算已经开始，请录入[" + quantityMaxPeriod + "]期的分包材料结算数据！";
                        return strReturnTemp;
                    }
                }
            } else {//（>materialMaxPeriod &&=3）在以上两个条件均不满足的情况下，此时说明和自身比较没有问题，接下来要和材料结算比较
                if (materialMaxPeriod.compareTo(quantityMaxPeriod) < 0) {
                    if (stageNo.compareTo(quantityMaxPeriod) != 0) {
                        strReturnTemp = "第[" + quantityMaxPeriod + "]期分包数量结算已经开始，请录入[" + quantityMaxPeriod + "]期的分包材料结算数据！";
                        return strReturnTemp;
                    }
                }
            }
        }
        return "";
    }
}
