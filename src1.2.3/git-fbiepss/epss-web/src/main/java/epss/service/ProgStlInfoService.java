package epss.service;

import epss.common.enums.EnumResType;
import epss.common.enums.EnumFlowStatus;
import epss.common.enums.EnumFlowStatusReason;
import epss.repository.dao.FlowCtrlHisMapper;
import epss.repository.model.model_show.ProgStlInfoShow;
import org.springframework.beans.factory.annotation.Autowired;
import skyline.util.ToolUtil;
import epss.repository.dao.not_mybatis.MyProgStlInfoMapper;
import epss.repository.dao.ProgStlInfoMapper;
import epss.repository.model.*;
import epss.repository.model.model_show.ProgStlItemSubStlmentShow;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
public class ProgStlInfoService {
    @Resource
    private ProgStlInfoMapper progStlInfoMapper;
    @Resource
    private MyProgStlInfoMapper myProgStlInfoMapper;
    @Resource
    private FlowCtrlHisMapper flowCtrlHisMapper;
    @Resource
    private ProgSubstlItemService progSubstlItemService;
    @Resource
    private ProgWorkqtyItemService progWorkqtyItemService;
    @Resource
    private ProgMatqtyItemService progMatqtyItemService;

    // 判断记录是否存在
    public List<ProgStlInfo> getInitStlListByModelShow(ProgStlInfoShow progStlInfoShowPara) {
        ProgStlInfo progStlInfoTemp=fromModelShowToModel(progStlInfoShowPara);
        progStlInfoTemp.setFlowStatus(null);
        return getInitStlListByModel(progStlInfoTemp);
    }

    public List<ProgStlInfo> getInitStlListByModel(ProgStlInfo progStlInfoPara) {
        ProgStlInfoExample example = new ProgStlInfoExample();
        ProgStlInfoExample.Criteria criteria = example.createCriteria();
        criteria
                .andStlTypeEqualTo(progStlInfoPara.getStlType())
                .andStlPkidEqualTo(progStlInfoPara.getStlPkid())
                .andPeriodNoEqualTo(progStlInfoPara.getPeriodNo());
        //可以为NULL的项
        if(!ToolUtil.getStrIgnoreNull(progStlInfoPara.getFlowStatus()).equals("")){
            criteria.andFlowStatusEqualTo(progStlInfoPara.getFlowStatus());
        }
        return progStlInfoMapper.selectByExample(example);
    }

    public void accountAction(ProgStlInfo progStlInfoPara) {
        progStlInfoPara.setFlowStatus(EnumFlowStatus.FLOW_STATUS4.getCode());
        progStlInfoPara.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON7.getCode());
        flowCtrlHisMapper.insert(fromProgStlInfoToFlowCtrlHis(progStlInfoPara, "update"));
    }

    public FlowCtrlHis fromProgStlInfoToFlowCtrlHis(ProgStlInfo progStlInfoPara,String strOperType){
        FlowCtrlHis flowCtrlHis =new FlowCtrlHis();
        flowCtrlHis.setInfoType(progStlInfoPara.getStlType());
        flowCtrlHis.setInfoPkid(progStlInfoPara.getStlPkid());
        flowCtrlHis.setPeriodNo(progStlInfoPara.getPeriodNo());
        flowCtrlHis.setFlowStatus(progStlInfoPara.getFlowStatus());
        flowCtrlHis.setFlowStatusReason(progStlInfoPara.getFlowStatusReason());
        flowCtrlHis.setCreatedTime(progStlInfoPara.getCreatedTime());
        flowCtrlHis.setCreatedBy(progStlInfoPara.getCreatedBy());
        flowCtrlHis.setOperType(strOperType);
        return flowCtrlHis;
    }
    public ProgStlInfo fromModelShowToModel(ProgStlInfoShow progStlInfoShowPara){
        ProgStlInfo progStlInfoTemp =new ProgStlInfo();
        progStlInfoTemp.setPkid(progStlInfoShowPara.getPkid());
        progStlInfoTemp.setStlType(progStlInfoShowPara.getStlType());
        progStlInfoTemp.setStlPkid(progStlInfoShowPara.getStlPkid());
        progStlInfoTemp.setId(progStlInfoShowPara.getId());
        progStlInfoTemp.setPeriodNo(progStlInfoShowPara.getPeriodNo());
        progStlInfoTemp.setRemark(progStlInfoShowPara.getRemark());
        progStlInfoTemp.setAttachment(progStlInfoShowPara.getAttachment());
        progStlInfoTemp.setArchivedFlag(progStlInfoShowPara.getArchivedFlag());
        progStlInfoTemp.setCreatedBy(progStlInfoShowPara.getCreatedBy());
        progStlInfoTemp.setCreatedTime(progStlInfoShowPara.getCreatedTime());
        progStlInfoTemp.setLastUpdBy(progStlInfoShowPara.getLastUpdBy());
        progStlInfoTemp.setLastUpdTime(progStlInfoShowPara.getLastUpdTime());
        progStlInfoTemp.setRecversion(progStlInfoShowPara.getRecversion());
        progStlInfoTemp.setAutoLinkAdd(progStlInfoShowPara.getAutoLinkAdd());
        progStlInfoTemp.setFlowStatus(progStlInfoShowPara.getFlowStatus());
        progStlInfoTemp.setFlowStatusReason(progStlInfoShowPara.getFlowStatusReason());
        return progStlInfoTemp;
    }
    public ProgStlInfoShow fromModelToModelShow(ProgStlInfo progStlInfoPara){
        ProgStlInfoShow progStlInfoShowTemp =new ProgStlInfoShow();
        progStlInfoShowTemp.setPkid(progStlInfoPara.getPkid());
        progStlInfoShowTemp.setStlType(progStlInfoPara.getStlType());
        progStlInfoShowTemp.setStlPkid(progStlInfoPara.getStlPkid());
        progStlInfoShowTemp.setId(progStlInfoPara.getId());
        progStlInfoShowTemp.setPeriodNo(progStlInfoPara.getPeriodNo());
        progStlInfoShowTemp.setRemark(progStlInfoPara.getRemark());
        progStlInfoShowTemp.setAttachment(progStlInfoPara.getAttachment());
        progStlInfoShowTemp.setArchivedFlag(progStlInfoPara.getArchivedFlag());
        progStlInfoShowTemp.setCreatedBy(progStlInfoPara.getCreatedBy());
        progStlInfoShowTemp.setCreatedTime(progStlInfoPara.getCreatedTime());
        progStlInfoShowTemp.setLastUpdBy(progStlInfoPara.getLastUpdBy());
        progStlInfoShowTemp.setLastUpdTime(progStlInfoPara.getLastUpdTime());
        progStlInfoShowTemp.setRecversion(progStlInfoPara.getRecversion());
        progStlInfoShowTemp.setAutoLinkAdd(progStlInfoPara.getAutoLinkAdd());
        progStlInfoShowTemp.setFlowStatus(progStlInfoPara.getFlowStatus());
        progStlInfoShowTemp.setFlowStatusReason(progStlInfoPara.getFlowStatusReason());
        return progStlInfoShowTemp;
    }

    public ProgStlInfo selectRecordsByPrimaryKey(String strPkId){
        return progStlInfoMapper.selectByPrimaryKey(strPkId);
    }

    public void insertRecord(ProgStlInfoShow progStlInfoShowPara){
        String strOperatorIdTemp=ToolUtil.getOperatorManager().getOperatorId();
        String strLastUpdTimeTemp=ToolUtil.getStrLastUpdTime();
        progStlInfoShowPara.setCreatedBy(strOperatorIdTemp);
        progStlInfoShowPara.setCreatedTime(strLastUpdTimeTemp);
        progStlInfoShowPara.setArchivedFlag("0");
        progStlInfoShowPara.setLastUpdBy(strOperatorIdTemp);
        progStlInfoShowPara.setLastUpdTime(strLastUpdTimeTemp);
        progStlInfoMapper.insert(fromModelShowToModel(progStlInfoShowPara)) ;
    }
    public void insertRecord(ProgStlInfo progStlInfoPara){
        String strOperatorIdTemp=ToolUtil.getOperatorManager().getOperatorId();
        String strLastUpdTimeTemp=ToolUtil.getStrLastUpdTime();
        progStlInfoPara.setCreatedBy(strOperatorIdTemp);
        progStlInfoPara.setCreatedTime(strLastUpdTimeTemp);
        progStlInfoPara.setArchivedFlag("0");
        progStlInfoPara.setLastUpdBy(strOperatorIdTemp);
        progStlInfoPara.setLastUpdTime(strLastUpdTimeTemp);
        progStlInfoMapper.insert(progStlInfoPara) ;
    }

    @Transactional
    public String insertStlQAndItemRecordAction(ProgStlInfoShow progStlInfoShowPara) {
        insertRecord(progStlInfoShowPara);
        progWorkqtyItemService.setFromLastStageApproveDataToThisStageBeginData(progStlInfoShowPara);
        return "新增数据完成。";
    }
    @Transactional
    public String insertStlMAndItemRecordAction(ProgStlInfoShow progStlInfoShowPara) {
        insertRecord(progStlInfoShowPara);
        progMatqtyItemService.setFromLastStageApproveDataToThisStageBeginData(progStlInfoShowPara);
        return "新增数据完成。";
    }
    public ProgStlInfo initStlData(String strStlTypePara,CttInfo cttInfoPara){
        ProgStlInfo progStlInfo =new ProgStlInfo();
        progStlInfo.setId(getStrMaxStlId(strStlTypePara));
        progStlInfo.setStlType(strStlTypePara);
        progStlInfo.setStlPkid(cttInfoPara.getPkid());
        progStlInfo.setPeriodNo("NULL");
        return progStlInfo;
    }
    @Transactional
    public void insertRecordForOperRes(CttInfo cttInfoPara) {
        //结算表根据条件判断插入数据
        if (cttInfoPara.getCttType().equals(EnumResType.RES_TYPE0.getCode())) {
            //总包计量
            insertRecord(initStlData(EnumResType.RES_TYPE6.getCode(), cttInfoPara));
            //总包统计
            insertRecord(initStlData(EnumResType.RES_TYPE7.getCode(), cttInfoPara));
        } else if (cttInfoPara.getCttType().equals(EnumResType.RES_TYPE2.getCode())) {
            //分包数量结算
            insertRecord(initStlData(EnumResType.RES_TYPE3.getCode(), cttInfoPara));
            //分包材料结算
            insertRecord(initStlData(EnumResType.RES_TYPE4.getCode(), cttInfoPara));
            //分包价格结算
            insertRecord(initStlData(EnumResType.RES_TYPE5.getCode(), cttInfoPara));
        }
    }

    @Transactional
    public void updateRecordForSubCttPApprovePass(
            ProgStlInfo progStlInfoPara,
            List<ProgStlItemSubStlmentShow> progStlItemSubStlmentShowListForApprovePara){
        //结算登记表更新
        progStlInfoPara.setRecversion(
                ToolUtil.getIntIgnoreNull(progStlInfoPara.getRecversion())+1);
        progStlInfoPara.setArchivedFlag("0");
        progStlInfoPara.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        progStlInfoPara.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        progStlInfoPara.setFlowStatus(EnumFlowStatus.FLOW_STATUS3.getCode());
        progStlInfoPara.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON5.getCode());
        progStlInfoMapper.updateByPrimaryKey(progStlInfoPara) ;
        //将价格结算的完整数据插入至PROG_STL_ITEM_SUB_STLMENT表
        for (int i=0;i< progStlItemSubStlmentShowListForApprovePara.size();i++){
            ProgStlItemSubStlmentShow itemUnit= progStlItemSubStlmentShowListForApprovePara.get(i);
            itemUnit.setEngPMng_RowNo(i);
            progSubstlItemService.insertRecordDetail(itemUnit);
        }
    }
    @Transactional
    public void updateRecord(ProgStlInfoShow progStlInfoShowPara){
        progStlInfoShowPara.setRecversion(
                ToolUtil.getIntIgnoreNull(progStlInfoShowPara.getRecversion())+1);
        progStlInfoShowPara.setArchivedFlag("0");
        progStlInfoShowPara.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        progStlInfoShowPara.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        progStlInfoMapper.updateByPrimaryKey(fromModelShowToModel(progStlInfoShowPara));
    }
    @Transactional
    public void updateRecord(ProgStlInfo progStlInfoPara) {
        progStlInfoPara.setRecversion(
                ToolUtil.getIntIgnoreNull(progStlInfoPara.getRecversion()) + 1);
        progStlInfoPara.setArchivedFlag("0");
        progStlInfoPara.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        progStlInfoPara.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        progStlInfoMapper.updateByPrimaryKey(progStlInfoPara);

        // 数量结算
        if (EnumResType.RES_TYPE3.getCode().equals(progStlInfoPara.getStlType())) {
            // 先看本分包合同及本期的分包结算单
            ProgStlInfo progStlInfoSubStlmentTemp=new ProgStlInfo();
            progStlInfoSubStlmentTemp.setStlType(EnumResType.RES_TYPE5.getCode());
            progStlInfoSubStlmentTemp.setStlPkid(progStlInfoPara.getStlPkid());
            progStlInfoSubStlmentTemp.setPeriodNo(progStlInfoPara.getPeriodNo());
            // 状态为复核，且状态原因为复核通过
            if (EnumFlowStatus.FLOW_STATUS2.getCode().equals(progStlInfoPara.getFlowStatus()) &&
                    EnumFlowStatusReason.FLOW_STATUS_REASON3.getCode().equals(progStlInfoPara.getFlowStatusReason())) {
                // 本分包合同及本期的分包结算单不存在
                if(getInitStlListByModel(progStlInfoSubStlmentTemp).size()<=0) {
                    ProgStlInfo progStlInfoSubMTemp=new ProgStlInfo();
                    progStlInfoSubMTemp.setStlType(EnumResType.RES_TYPE4.getCode());
                    progStlInfoSubMTemp.setStlPkid(progStlInfoPara.getStlPkid());
                    progStlInfoSubMTemp.setPeriodNo(progStlInfoPara.getPeriodNo());
                    progStlInfoSubMTemp.setFlowStatus(EnumFlowStatus.FLOW_STATUS2.getCode());
                    // 本分包合同及本期的分包材料结算也复核了
                    if(getInitStlListByModel(progStlInfoSubMTemp).size()>0) {
                        progStlInfoSubStlmentTemp.setId(getStrMaxStlId(progStlInfoSubStlmentTemp.getStlType()));
                        progStlInfoSubStlmentTemp.setFlowStatus(EnumFlowStatus.FLOW_STATUS2.getCode());
                        progStlInfoSubStlmentTemp.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON3.getCode());
                        insertRecord(progStlInfoSubStlmentTemp);
                    }
                }
            }
            if(EnumFlowStatusReason.FLOW_STATUS_REASON4.getCode().equals(progStlInfoPara.getFlowStatusReason())){
                deleteRecord(progStlInfoSubStlmentTemp);
            }
        }else
        // 材料结算
        if (EnumResType.RES_TYPE4.getCode().equals(progStlInfoPara.getStlType())) {
            // 先看本分包合同及本期的分包结算单
            ProgStlInfo progStlInfoSubStlmentTemp=new ProgStlInfo();
            progStlInfoSubStlmentTemp.setStlType(EnumResType.RES_TYPE5.getCode());
            progStlInfoSubStlmentTemp.setStlPkid(progStlInfoPara.getStlPkid());
            progStlInfoSubStlmentTemp.setPeriodNo(progStlInfoPara.getPeriodNo());
            // 状态为复核，且状态原因为复核通过
            if (EnumFlowStatus.FLOW_STATUS2.getCode().equals(progStlInfoPara.getFlowStatus()) &&
                    EnumFlowStatusReason.FLOW_STATUS_REASON3.getCode().equals(progStlInfoPara.getFlowStatusReason())) {
                // 本分包合同及本期的分包结算单不存在
                if(getInitStlListByModel(progStlInfoSubStlmentTemp).size()<=0) {
                    ProgStlInfo progStlInfoSubMTemp=new ProgStlInfo();
                    progStlInfoSubMTemp.setStlType(EnumResType.RES_TYPE3.getCode());
                    progStlInfoSubMTemp.setStlPkid(progStlInfoPara.getStlPkid());
                    progStlInfoSubMTemp.setPeriodNo(progStlInfoPara.getPeriodNo());
                    progStlInfoSubMTemp.setFlowStatus(EnumFlowStatus.FLOW_STATUS2.getCode());
                    // 本分包合同及本期的分包材料结算也复核了
                    if(getInitStlListByModel(progStlInfoSubMTemp).size()>0) {
                        progStlInfoSubStlmentTemp.setId(getStrMaxStlId(progStlInfoSubStlmentTemp.getStlType()));
                        progStlInfoSubStlmentTemp.setFlowStatus(EnumFlowStatus.FLOW_STATUS2.getCode());
                        progStlInfoSubStlmentTemp.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON3.getCode());
                        insertRecord(progStlInfoSubStlmentTemp);
                    }
                }
            }
            if(EnumFlowStatusReason.FLOW_STATUS_REASON4.getCode().equals(progStlInfoPara.getFlowStatusReason())){
                deleteRecord(progStlInfoSubStlmentTemp);
            }
        }
    }

    public String getMaxPeriodNo(String stlType, String subCttPkid) {
        return myProgStlInfoMapper.getMaxPeriodNo(stlType,subCttPkid);
    }

    public String subCttStlCheckForMng(String stlType,String cttPkid,String periodNo) {
        String strReturnTemp="";
        if(EnumResType.RES_TYPE3.getCode().equals(stlType)|| EnumResType.RES_TYPE4.getCode().equals(stlType)){ //分包结算
            String quantityMaxPeriod = ToolUtil.getStrIgnoreNull(
                    getMaxPeriodNo(EnumResType.RES_TYPE3.getCode(),cttPkid));
            String materialMaxPeriod = ToolUtil.getStrIgnoreNull(
                    getMaxPeriodNo(EnumResType.RES_TYPE4.getCode(),cttPkid));
            ProgStlInfo progStlInfoTemp=new ProgStlInfo();
            progStlInfoTemp.setStlType(EnumResType.RES_TYPE3.getCode());
            progStlInfoTemp.setStlPkid(cttPkid);
            progStlInfoTemp.setPeriodNo(quantityMaxPeriod);
            String quantityStatus=ToolUtil.getStrIgnoreNull(
                    getInitStlListByModel(progStlInfoTemp).get(0).getFlowStatus());

            progStlInfoTemp.setStlType(EnumResType.RES_TYPE4.getCode());
            String materialStatus=ToolUtil.getStrIgnoreNull(
                    getInitStlListByModel(progStlInfoTemp).get(0).getFlowStatus());

            System.out.println("\n数量结算最大期号："+quantityMaxPeriod+"\n材料结算最大期号："+materialMaxPeriod);
            System.out.println("\n数量结算最大期号对应状态标志："+quantityStatus+"\n材料结算最大期号对应状态标志："+materialStatus);

            if (EnumResType.RES_TYPE3.getCode().equals(stlType)){
                if ("".equals(quantityMaxPeriod)){
                    return strReturnTemp;
                }
                if (periodNo.compareTo(quantityMaxPeriod)<=0){ //首先和自身比较期号大小，如果比自身小或者等于则不能录入
                    strReturnTemp="应录入大于[" + quantityMaxPeriod + "]期的分包数量结算数据!";
                    return strReturnTemp;
                }else {
                    if (quantityStatus.equals("")&&!quantityMaxPeriod.equals("")){
                        strReturnTemp="分包数量结算第["+quantityMaxPeriod+"]期数据还未批准通过，不能录入新数据!";
                        return strReturnTemp;
                    }
                    if (EnumFlowStatus.FLOW_STATUS2.getCode().compareTo(quantityStatus)>0){ //判断是否有非批准状态的数据存在，如果有不能录入
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
            if (EnumResType.RES_TYPE4.getCode().equals(stlType)){
                if ("".equals(materialMaxPeriod)){
                    return strReturnTemp;
                }
                if (periodNo.compareTo(materialMaxPeriod)<=0){ //首先和自身比较期号大小，如果比自身小或者等于则不能录入
                    strReturnTemp="应录入大于["+materialMaxPeriod+"]期的分包材料结算数据!";
                    return strReturnTemp;
                }else {
                    if (materialStatus.equals("")&&!materialMaxPeriod.equals("")){
                        strReturnTemp="分包材料结算第["+materialMaxPeriod+"]期数据还未批准通过，不能录入新数据!";
                        return strReturnTemp;
                    }

                    if (EnumFlowStatus.FLOW_STATUS2.getCode().compareTo(materialStatus)>0){ //判断是否有非批准状态的数据存在，如果有不能录入
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
        }else if(EnumResType.RES_TYPE6.getCode().equals(stlType)|| EnumResType.RES_TYPE7.getCode().equals(stlType)){// 总包结算
            if (EnumResType.RES_TYPE6.getCode().equals(stlType)){
                String strStaQtyMaxPeriod = ToolUtil.getStrIgnoreNull(
                        getMaxPeriodNo(EnumResType.RES_TYPE6.getCode(),cttPkid));

                ProgStlInfo progStlInfoTemp=new ProgStlInfo();
                progStlInfoTemp.setStlType(EnumResType.RES_TYPE6.getCode());
                progStlInfoTemp.setStlPkid(cttPkid);
                progStlInfoTemp.setPeriodNo(strStaQtyMaxPeriod);
                String strStaQtyMaxPeriodStatus = ToolUtil.getStrIgnoreNull(
                        getInitStlListByModel(progStlInfoTemp).get(0).getFlowStatus());

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
                if (EnumResType.RES_TYPE7.getCode().equals(stlType)){
                    String strMeaQtyMaxPeriod = ToolUtil.getStrIgnoreNull(
                            getMaxPeriodNo(EnumResType.RES_TYPE7.getCode(),cttPkid));
                    ProgStlInfo progStlInfoTemp=new ProgStlInfo();
                    progStlInfoTemp.setStlType(EnumResType.RES_TYPE7.getCode());
                    progStlInfoTemp.setStlPkid(cttPkid);
                    progStlInfoTemp.setPeriodNo(strMeaQtyMaxPeriod);
                    String strMeaQtyMaxPeriodStatus = ToolUtil.getStrIgnoreNull(
                            getInitStlListByModel(progStlInfoTemp).get(0).getFlowStatus());

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
    }

    @Transactional
    public void deleteRecord(ProgStlInfoShow progStlInfoShowPara){
        ProgStlInfoExample example = new ProgStlInfoExample();
        example.createCriteria().andStlTypeEqualTo(progStlInfoShowPara.getStlType())
                .andStlPkidEqualTo(progStlInfoShowPara.getStlPkid())
                .andPeriodNoEqualTo(progStlInfoShowPara.getPeriodNo());
        progStlInfoMapper.deleteByExample(example);
    }
    public void deleteRecord(ProgStlInfo progStlInfoPara){
        ProgStlInfoExample example = new ProgStlInfoExample();
        example.createCriteria()
                .andStlTypeEqualTo(progStlInfoPara.getStlType())
                .andStlPkidEqualTo(progStlInfoPara.getStlPkid())
                .andPeriodNoEqualTo(progStlInfoPara.getPeriodNo());
        progStlInfoMapper.deleteByExample(example);
    }
    @Transactional
    public void deleteRecordForSubCttPApprovePass(ProgStlInfo progStlInfoPara,String powerType){
        //删除stl表中stl_type为5的记录
        ProgStlInfoExample example = new ProgStlInfoExample();
        example.createCriteria()
                .andStlTypeEqualTo(progStlInfoPara.getStlType())
                .andStlPkidEqualTo(progStlInfoPara.getStlPkid())
                .andPeriodNoEqualTo(progStlInfoPara.getPeriodNo());
        progStlInfoMapper.deleteByExample(example);

        //更新power表中power_type为3或者4的记录状态为审核状态
        example = new ProgStlInfoExample();
        example.createCriteria()
                .andStlTypeEqualTo(powerType)
                .andStlPkidEqualTo(progStlInfoPara.getStlPkid())
                .andPeriodNoEqualTo(progStlInfoPara.getPeriodNo());
        List<ProgStlInfo> progStlInfoListTemp = progStlInfoMapper.selectByExample(example);
        ProgStlInfo progStlInfoTemp = progStlInfoListTemp.get(0);
        progStlInfoTemp.setFlowStatus(EnumFlowStatus.FLOW_STATUS1.getCode());
        progStlInfoTemp.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON6.getCode());
        progStlInfoMapper.updateByPrimaryKey(progStlInfoTemp);
        //删除PROG_STL_ITEM_SUB_STLMENT表中的相应记录
        progSubstlItemService.deleteRecordByExample(progStlInfoPara.getStlPkid(), progStlInfoPara.getPeriodNo());
    }
    public int deleteRecord(String strPkId){
        return progStlInfoMapper.deleteByPrimaryKey(strPkId);
    }
    @Transactional
    public String deleteStlMAndItemRecord(ProgStlInfoShow progStlInfoShowPara) {
        // 删除详细数据
        int deleteItemsNum =
                progMatqtyItemService.deleteItemsByInitStlSubcttEng(
                        progStlInfoShowPara.getStlPkid(),
                        progStlInfoShowPara.getPeriodNo());
        // 删除登记数据
        int deleteRecordOfStlMNum = deleteRecord(progStlInfoShowPara.getPkid());
        if (deleteItemsNum <= 0 && deleteRecordOfStlMNum <= 0) {
            return "该记录已删除。";
        }
        return "删除数据完成。";
    }

    public String deleteStlQAndItemRecord(ProgStlInfoShow progStlInfoShowPara) {
        // 删除详细数据
        int deleteItemsNum =
                progWorkqtyItemService.deleteItemsByInitStlSubcttEng(
                        progStlInfoShowPara.getStlPkid(),
                        progStlInfoShowPara.getPeriodNo());
        // 删除登记数据
        int deleteRecordOfStlQNum = deleteRecord(progStlInfoShowPara.getPkid());
        if (deleteItemsNum <= 0 && deleteRecordOfStlQNum <= 0) {
            return "该记录已删除。";
        }
        return "删除数据完成。";
    }

    public String getStrMaxStlId(String strCttType){
        return myProgStlInfoMapper.getStrMaxStlId(strCttType) ;
    }


    public String getLatestDoubleCkeckedPeriodNo(String strPowerType,String strPowerPkid) {
        return myProgStlInfoMapper.getLatestDoubleCkeckedPeriodNo(strPowerType, strPowerPkid);
    }

    public String getLatestApprovedPeriodNo(String strPowerType,String strPowerPkid) {
        return myProgStlInfoMapper.getLatestApprovedPeriodNo(strPowerType, strPowerPkid);
    }

    public String getLatestApprovedPeriodNoByEndPeriod(String strPowerType,String strPowerPkid,String strEndPeriodNo) {
        return myProgStlInfoMapper.getLatestApprovedPeriodNoByEndPeriod(strPowerType, strPowerPkid,strEndPeriodNo);
    }

    public List<ProgStlInfo> selectIsUsedInQMPBySubcttPkid(String strSubcttPkid){
        return myProgStlInfoMapper.selectIsUsedInQMPBySubcttPkid(strSubcttPkid);
    }

    public List<ProgStlInfoShow> selectSubcttStlQMByStatusFlagBegin_End(ProgStlInfoShow progStlInfoShowPara){
        return myProgStlInfoMapper.selectSubcttStlQMByStatusFlagBegin_End(progStlInfoShowPara);
    }

    public List<ProgStlInfoShow> selectNotFormEsInitSubcttStlP(String strParentPkid,
                                                               String strStlPkid,
                                                               String strPeriodNo){
        return myProgStlInfoMapper.selectNotFormEsInitSubcttStlP(strParentPkid, strStlPkid, strPeriodNo);
    }
    public List<ProgStlInfoShow> selectFormPreEsInitSubcttStlP(String strParentPkid,
                                                               String strStlPkid,
                                                               String strPeriodNo){
        return myProgStlInfoMapper.selectFormPreEsInitSubcttStlP(strParentPkid,strStlPkid,strPeriodNo);
    }
    public List<ProgStlInfoShow> selectFormingEsInitSubcttStlP(String strParentPkid,
                                                               String strStlPkid,
                                                               String strPeriodNo){
        return myProgStlInfoMapper.selectFormingEsInitSubcttStlP(strParentPkid,strStlPkid,strPeriodNo);
    }
    public List<ProgStlInfoShow> selectFormedEsInitSubcttStlP(String strParentPkid,
                                                              String strStlPkid,
                                                              String strPeriodNo){
        return myProgStlInfoMapper.selectFormedEsInitSubcttStlPList(strParentPkid,strStlPkid,strPeriodNo);
    }

    public List<ProgStlInfoShow> getFormedAfterEsInitSubcttStlPList(String strParentPkid,
                                                                    String strStlPkid,
                                                                    String strPeriodNo){
        return myProgStlInfoMapper.getFormedAfterEsInitSubcttStlPList(strParentPkid,strStlPkid,strPeriodNo);
    }

    public List<ProgStlInfoShow> selectTkcttStlSMByStatusFlagBegin_End(ProgStlInfoShow progStlInfoShowPara){
        return myProgStlInfoMapper.selectTkcttStlSMByStatusFlagBegin_End(progStlInfoShowPara);
    }
}
