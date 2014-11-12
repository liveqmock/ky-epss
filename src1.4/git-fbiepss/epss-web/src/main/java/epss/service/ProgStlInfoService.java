package epss.service;

import epss.common.enums.*;
import epss.repository.dao.FlowCtrlHisMapper;
import epss.repository.model.model_show.ProgStlInfoShow;
import org.apache.commons.lang.StringUtils;
import skyline.util.ToolUtil;
import epss.repository.dao.not_mybatis.MyProgStlInfoMapper;
import epss.repository.dao.ProgStlInfoMapper;
import epss.repository.model.*;
import epss.repository.model.model_show.ProgStlItemSubStlmentShow;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.ArrayList;
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
    private FlowCtrlHisService flowCtrlHisService;
    @Resource
    private CttInfoService cttInfoService;
    @Resource
    private ProgStlItemSubQService progStlItemSubQService;
    @Resource
    private ProgStlItemSubMService progStlItemSubMService;
    @Resource
    private ProgStlItemSubStlmentService progStlItemSubStlmentService;
    @Resource
    private ProgStlItemTkEstService progStlItemTkEstService;
    @Resource
    private ProgStlItemTkMeaService progStlItemTkMeaService;

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
                .andStlPkidEqualTo(progStlInfoPara.getStlPkid());
        //可以为NULL的项
        if(!ToolUtil.getStrIgnoreNull(progStlInfoPara.getPeriodNo()).equals("")){
            criteria.andPeriodNoEqualTo(progStlInfoPara.getPeriodNo());
        }
        if(!ToolUtil.getStrIgnoreNull(progStlInfoPara.getFlowStatus()).equals("")){
            criteria.andFlowStatusEqualTo(progStlInfoPara.getFlowStatus());
        }
        return progStlInfoMapper.selectByExample(example);
    }

    public List<ProgStlInfoShow> getInitStlShowListByModel(ProgStlInfo progStlInfoPara) {
        ProgStlInfoExample example = new ProgStlInfoExample();
        ProgStlInfoExample.Criteria criteria = example.createCriteria();
        criteria
                .andStlTypeEqualTo(progStlInfoPara.getStlType())
                .andStlPkidEqualTo(progStlInfoPara.getStlPkid());
        //可以为NULL的项
        if(!ToolUtil.getStrIgnoreNull(progStlInfoPara.getPeriodNo()).equals("")){
            criteria.andPeriodNoEqualTo(progStlInfoPara.getPeriodNo());
        }
        if(!ToolUtil.getStrIgnoreNull(progStlInfoPara.getFlowStatus()).equals("")){
            criteria.andFlowStatusEqualTo(progStlInfoPara.getFlowStatus());
        }
        if(!ToolUtil.getStrIgnoreNull(progStlInfoPara.getAutoLinkAdd()).equals("")){
            criteria.andAutoLinkAddEqualTo(progStlInfoPara.getAutoLinkAdd());
        }
        List<ProgStlInfoShow> progStlInfoShowListTemp=new ArrayList<>();
        List<ProgStlInfo> progStlInfoListTemp=progStlInfoMapper.selectByExample(example);
        for(ProgStlInfo progStlInfoUnit:progStlInfoListTemp){
            ProgStlInfoShow progStlInfoShowTemp=fromModelToModelShow(progStlInfoUnit);
            progStlInfoShowListTemp.add(progStlInfoShowTemp);
        }
        return progStlInfoShowListTemp;
    }

    public List<ProgStlInfoShow> getInitStlShowListByInfoTypePkid(
            String infoTypePara,String infoPkidPara) {
        return myProgStlInfoMapper.getInitStlShowListByInfoTypePkid(infoTypePara, infoPkidPara);
    }

    public String getMaxPeriodNo(String stlType, String subCttPkid) {
        return myProgStlInfoMapper.getMaxPeriodNo(stlType,subCttPkid);
    }
	public String getSubcttMaxPeriodNo(String subCttPkid) {
        return myProgStlInfoMapper.getSubcttMaxPeriodNo(subCttPkid);
    }

    public String progStlInfoMngPreCheck(ProgStlInfoShow progStlInfoShowPara) {
        String stlType=progStlInfoShowPara.getStlType();
        String cttPkid=progStlInfoShowPara.getStlPkid();
        String periodNo=progStlInfoShowPara.getPeriodNo();
        String strReturnTemp="";
        if(EnumResType.RES_TYPE3.getCode().equals(stlType)||
                EnumResType.RES_TYPE4.getCode().equals(stlType)){ //分包结算
            String quantityMaxPeriod = ToolUtil.getStrIgnoreNull(
                    getMaxPeriodNo(EnumResType.RES_TYPE3.getCode(),cttPkid));
            String materialMaxPeriod = ToolUtil.getStrIgnoreNull(
                    getMaxPeriodNo(EnumResType.RES_TYPE4.getCode(),cttPkid));
            String stlmentMaxPeriod = ToolUtil.getStrIgnoreNull(
                    getMaxPeriodNo(EnumResType.RES_TYPE5.getCode(),cttPkid));
            String quantityStatus="";
            String materialStatus="";
            String stlmentStatus="";
            ProgStlInfo progStlInfoTemp=new ProgStlInfo();
            progStlInfoTemp.setStlType(EnumResType.RES_TYPE3.getCode());
            progStlInfoTemp.setStlPkid(cttPkid);
            progStlInfoTemp.setPeriodNo(quantityMaxPeriod);
            List<ProgStlInfo> progStlInfoListTemp=getInitStlListByModel(progStlInfoTemp);
            if(progStlInfoListTemp.size()>0) {
                quantityStatus = ToolUtil.getStrIgnoreNull(progStlInfoListTemp.get(0).getFlowStatus());
            }

            progStlInfoTemp.setStlType(EnumResType.RES_TYPE4.getCode());
            progStlInfoTemp.setPeriodNo(materialMaxPeriod);
            progStlInfoListTemp=getInitStlListByModel(progStlInfoTemp);
            if(progStlInfoListTemp.size()>0) {
                materialStatus=ToolUtil.getStrIgnoreNull(progStlInfoListTemp.get(0).getFlowStatus());
            }

            progStlInfoTemp.setStlType(EnumResType.RES_TYPE5.getCode());
            progStlInfoTemp.setPeriodNo(stlmentMaxPeriod);
            progStlInfoListTemp=getInitStlListByModel(progStlInfoTemp);
            if(progStlInfoListTemp.size()>0) {
                stlmentStatus=ToolUtil.getStrIgnoreNull(progStlInfoListTemp.get(0).getFlowStatus());
            }
            System.out.println("\n数量结算最大期号："+quantityMaxPeriod+"\n材料结算最大期号："+materialMaxPeriod+"\n结算单最大期号："+stlmentMaxPeriod);
            System.out.println("\n数量结算最大期号对应状态标志："+quantityStatus+"\n材料结算最大期号对应状态标志："+materialStatus+"\n结算单最大期号对应状态标志："+stlmentStatus);

            if (EnumResType.RES_TYPE3.getCode().equals(stlType)){
                //和自身比较
                if (periodNo.compareTo(quantityMaxPeriod)<=0){//1.和自身期号比较
                    strReturnTemp="应录入大于[" + quantityMaxPeriod + "]期的分包数量结算数据!";
                    return strReturnTemp;
                }else {
                    if (!("".equals(quantityMaxPeriod))&&
                            EnumFlowStatus.FLOW_STATUS2.getCode().compareTo(quantityStatus)>0){//2.和自身状态比较
                        strReturnTemp="分包数量结算第["+quantityMaxPeriod+"]期数据还未复合通过，不能录入新数据!";
                        return strReturnTemp;
                    }
                }
                //和材料比较
                if (!("".equals(materialMaxPeriod))&&periodNo.compareTo(materialMaxPeriod)!=0){
                    if (quantityMaxPeriod.compareTo(materialMaxPeriod)!=0){
                        strReturnTemp="第["+materialMaxPeriod+"]期分包材料结算已经开始，请录入["+materialMaxPeriod+"]期的分包数量结算数据！";
                        return strReturnTemp;
                    }
                    if (EnumFlowStatus.FLOW_STATUS2.getCode().compareTo(materialStatus)>0){
                        strReturnTemp="材料结算第["+materialMaxPeriod+"]期数据还未复合通过，不能录入新数据!";
                        return strReturnTemp;
                    }
                }
                //和结算单比较
                if (!("".equals(stlmentMaxPeriod))&&periodNo.compareTo(stlmentMaxPeriod)>0){
                    if (EnumFlowStatus.FLOW_STATUS2.getCode().compareTo(stlmentStatus)>=0){//2.和自身状态比较
                        strReturnTemp="结算单第["+stlmentMaxPeriod+"]期数据还未批准通过，不能录入新数据!";
                        return strReturnTemp;
                    }
                }
            }
            if (EnumResType.RES_TYPE4.getCode().equals(stlType)){
                //和自身比较
                if (periodNo.compareTo(materialMaxPeriod)<=0){//1.和自身期号比较
                    strReturnTemp="应录入大于[" + materialMaxPeriod + "]期的分包材料结算数据!";
                    return strReturnTemp;
                }else {
                    if (!("".equals(materialMaxPeriod))&&
                            EnumFlowStatus.FLOW_STATUS2.getCode().compareTo(materialStatus)>0){//2.和自身状态比较
                        strReturnTemp="分包材料结算第["+materialMaxPeriod+"]期数据还未复合通过，不能录入新数据!";
                        return strReturnTemp;
                    }
                }
                //和数量比较
                if (!("".equals(quantityMaxPeriod))&&periodNo.compareTo(quantityMaxPeriod)!=0){
                    if (quantityMaxPeriod.compareTo(materialMaxPeriod)!=0){
                        strReturnTemp="第["+quantityMaxPeriod+"]期分包数量结算已经开始，请录入["+quantityMaxPeriod+"]期的分包材料结算数据！";
                        return strReturnTemp;
                    }
                    if (EnumFlowStatus.FLOW_STATUS2.getCode().compareTo(quantityStatus)>0){
                        strReturnTemp="数量结算第["+quantityMaxPeriod+"]期数据还未复合通过，不能录入新数据!";
                        return strReturnTemp;
                    }
                }
                //和结算单比较
                if (!("".equals(stlmentMaxPeriod))&&periodNo.compareTo(stlmentMaxPeriod)>0){
                    if (EnumFlowStatus.FLOW_STATUS2.getCode().compareTo(stlmentStatus)>=0){//2.和自身状态比较
                        strReturnTemp="结算单第["+stlmentMaxPeriod+"]期数据还未批准通过，不能录入新数据!";
                        return strReturnTemp;
                    }
                }
            }
        }else if(EnumResType.RES_TYPE6.getCode().equals(stlType)|| EnumResType.RES_TYPE7.getCode().equals(stlType)){// 总包结算
            if (EnumResType.RES_TYPE6.getCode().equals(stlType)){
                String strStaQtyMaxPeriod = ToolUtil.getStrIgnoreNull(
                        getMaxPeriodNo(EnumResType.RES_TYPE6.getCode(),cttPkid));
                String strStaQtyMaxPeriodStatus="";
                ProgStlInfo progStlInfoTemp=new ProgStlInfo();
                progStlInfoTemp.setStlType(EnumResType.RES_TYPE6.getCode());
                progStlInfoTemp.setStlPkid(cttPkid);
                progStlInfoTemp.setPeriodNo(strStaQtyMaxPeriod);
                List<ProgStlInfo> progStlInfoListTemp=getInitStlListByModel(progStlInfoTemp);
                if(progStlInfoListTemp.size()>0) {
                    strStaQtyMaxPeriodStatus = ToolUtil.getStrIgnoreNull(
                            progStlInfoListTemp.get(0).getFlowStatus());
                }

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
                    String strMeaQtyMaxPeriodStatus="";
                    ProgStlInfo progStlInfoTemp=new ProgStlInfo();
                    progStlInfoTemp.setStlType(EnumResType.RES_TYPE7.getCode());
                    progStlInfoTemp.setStlPkid(cttPkid);
                    progStlInfoTemp.setPeriodNo(strMeaQtyMaxPeriod);

                    List<ProgStlInfo> progStlInfoListTemp=getInitStlListByModel(progStlInfoTemp);
                    if(progStlInfoListTemp.size()>0) {
                        strMeaQtyMaxPeriodStatus = ToolUtil.getStrIgnoreNull(
                                progStlInfoListTemp.get(0).getFlowStatus());
                    }

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
	public String progStlInfoAppFailPreCheck(String stlType,String cttPkid,String periodNo) {
        String strReturnTemp="";
        if(EnumResType.RES_TYPE3.getCode().equals(stlType)|| EnumResType.RES_TYPE4.getCode().equals(stlType)) {
            String subCttMaxPeriod = ToolUtil.getStrIgnoreNull(getSubcttMaxPeriodNo(cttPkid));
            if (periodNo.compareTo(subCttMaxPeriod)<0) {
                strReturnTemp="此合同分包结算第[" + subCttMaxPeriod + "]期数据已存在，不能退回该数据，请核对近期结算数据!";
                return strReturnTemp;
            }
        }
        if (EnumResType.RES_TYPE6.getCode().equals(stlType)) {
            String strStaQtyMaxPeriod = ToolUtil.getStrIgnoreNull(getMaxPeriodNo(EnumResType.RES_TYPE6.getCode(), cttPkid));
            if (periodNo.compareTo(strStaQtyMaxPeriod)<0) {
                strReturnTemp = "总包统计结算第[" + strStaQtyMaxPeriod + "]期数据已存在，不能退回该数据!";
                return strReturnTemp;
            }
        }
        if(EnumResType.RES_TYPE7.getCode().equals(stlType)){
            String strMeaQtyMaxPeriod = ToolUtil.getStrIgnoreNull(getMaxPeriodNo(EnumResType.RES_TYPE7.getCode(),cttPkid));
            if (periodNo.compareTo(strMeaQtyMaxPeriod)<0){
                strReturnTemp="总包计量结算第["+strMeaQtyMaxPeriod+"]期数据已存在，不能退回该数据!";
                return strReturnTemp;
            }
        }
        return strReturnTemp;
    }

    public FlowCtrlHis fromProgStlInfoToFlowCtrlHis(ProgStlInfo progStlInfoPara){
        FlowCtrlHis flowCtrlHis =new FlowCtrlHis();
        flowCtrlHis.setInfoType(progStlInfoPara.getStlType());
        flowCtrlHis.setInfoPkid(progStlInfoPara.getStlPkid());
        CttInfo cttInfoTemp=cttInfoService.getCttInfoByPkId(progStlInfoPara.getStlPkid());
        flowCtrlHis.setInfoId(cttInfoTemp.getId());
        flowCtrlHis.setInfoName(cttInfoTemp.getName());
        flowCtrlHis.setPeriodNo(progStlInfoPara.getPeriodNo());
        flowCtrlHis.setFlowStatus(progStlInfoPara.getFlowStatus());
        flowCtrlHis.setFlowStatusReason(progStlInfoPara.getFlowStatusReason());
        flowCtrlHis.setCreatedTime(progStlInfoPara.getCreatedTime());
        flowCtrlHis.setCreatedBy(progStlInfoPara.getCreatedBy());
        flowCtrlHis.setOperType(EnumOperType.OPER_TYPE0.getCode());
        return flowCtrlHis;
    }
    public FlowCtrlHis fromProgStlInfoShowToFlowCtrlHis(ProgStlInfoShow progStlInfoShowPara){
        FlowCtrlHis flowCtrlHis =new FlowCtrlHis();
        flowCtrlHis.setInfoType(progStlInfoShowPara.getStlType());
        flowCtrlHis.setInfoPkid(progStlInfoShowPara.getStlPkid());
        flowCtrlHis.setInfoId(progStlInfoShowPara.getStlId());
        flowCtrlHis.setInfoName(progStlInfoShowPara.getStlName());
        flowCtrlHis.setPeriodNo(progStlInfoShowPara.getPeriodNo());
        flowCtrlHis.setFlowStatus(progStlInfoShowPara.getFlowStatus());
        flowCtrlHis.setFlowStatusReason(progStlInfoShowPara.getFlowStatusReason());
        flowCtrlHis.setCreatedTime(progStlInfoShowPara.getCreatedTime());
        flowCtrlHis.setCreatedBy(progStlInfoShowPara.getCreatedBy());
        flowCtrlHis.setOperType(EnumOperType.OPER_TYPE0.getCode());
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
        progStlInfoTemp.setRecVersion(progStlInfoShowPara.getRecVersion());
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
        progStlInfoShowTemp.setRecVersion(progStlInfoPara.getRecVersion());
        progStlInfoShowTemp.setAutoLinkAdd(progStlInfoPara.getAutoLinkAdd());
        progStlInfoShowTemp.setFlowStatus(progStlInfoPara.getFlowStatus());
        progStlInfoShowTemp.setFlowStatusReason(progStlInfoPara.getFlowStatusReason());
        return progStlInfoShowTemp;
    }

    public ProgStlInfo getProgStlInfoByPkid(String strPkId){
        return progStlInfoMapper.selectByPrimaryKey(strPkId);
    }

    // 插入 Start
    public void insertRecord(ProgStlInfoShow progStlInfoShowPara){
        String strOperatorIdTemp=ToolUtil.getOperatorManager().getOperator().getPkid();
        String strLastUpdTimeTemp=ToolUtil.getStrLastUpdTime();
        progStlInfoShowPara.setCreatedBy(strOperatorIdTemp);
        progStlInfoShowPara.setCreatedTime(strLastUpdTimeTemp);
        progStlInfoShowPara.setArchivedFlag("0");
        progStlInfoShowPara.setLastUpdBy(strOperatorIdTemp);
        progStlInfoShowPara.setLastUpdTime(strLastUpdTimeTemp);
        progStlInfoMapper.insert(fromModelShowToModel(progStlInfoShowPara)) ;
        flowCtrlHisService.insertRecord(
                fromProgStlInfoShowToFlowCtrlHis(progStlInfoShowPara));
    }
    public void insertRecord(ProgStlInfo progStlInfoPara){
        String strOperatorIdTemp=ToolUtil.getOperatorManager().getOperator().getPkid();
        String strLastUpdTimeTemp=ToolUtil.getStrLastUpdTime();
        progStlInfoPara.setCreatedBy(strOperatorIdTemp);
        progStlInfoPara.setCreatedTime(strLastUpdTimeTemp);
        progStlInfoPara.setArchivedFlag("0");
        progStlInfoPara.setLastUpdBy(strOperatorIdTemp);
        progStlInfoPara.setLastUpdTime(strLastUpdTimeTemp);
        progStlInfoMapper.insert(progStlInfoPara) ;
        flowCtrlHisService.insertRecord(
                fromProgStlInfoToFlowCtrlHis(progStlInfoPara));
    }
    @Transactional
    public void addSubStlQInfoAndItemInitDataAction(ProgStlInfoShow progStlInfoShowPara) {
        insertRecord(progStlInfoShowPara);
        progStlItemSubQService.setFromLastStageAddUpToDataToThisStageBeginData(progStlInfoShowPara);
    }
    @Transactional
    public void addSubStlMInfoAndItemInitDataAction(ProgStlInfoShow progStlInfoShowPara) {
        insertRecord(progStlInfoShowPara);
        progStlItemSubMService.setFromLastStageAddUpToDataToThisStageBeginData(progStlInfoShowPara);
    }
    @Transactional
    public void addTkStlEstInfoAndItemInitDataAction(ProgStlInfoShow progStlInfoShowPara) {
        insertRecord(progStlInfoShowPara);
        progStlItemTkEstService.setFromLastStageAddUpToDataToThisStageBeginData(progStlInfoShowPara);
    }
    @Transactional
    public void addTkStlMeaInfoAndItemInitDataAction(ProgStlInfoShow progStlInfoShowPara) {
        insertRecord(progStlInfoShowPara);
        progStlItemTkMeaService.setFromLastStageAddUpToDataToThisStageBeginData(progStlInfoShowPara);
    }
    // 插入 End
    // 更新 Start
    @Transactional
    public void updSubPApprovePass(
            ProgStlInfo progStlInfoPara,
            List<ProgStlItemSubStlmentShow> progStlItemSubStlmentShowListForApprovePara){
        //结算登记表更新
        ProgStlInfoShow progStlInfoShowTemp=fromModelToModelShow(progStlInfoPara);
        progStlInfoShowTemp.setFlowStatus(EnumFlowStatus.FLOW_STATUS3.getCode());
        progStlInfoShowTemp.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON5.getCode());
        updateRecord(progStlInfoShowTemp) ;
        //将价格结算的完整数据插入至PROG_STL_ITEM_SUB_STLMENT表
        for (int i=0;i< progStlItemSubStlmentShowListForApprovePara.size();i++){
            ProgStlItemSubStlmentShow itemUnit= progStlItemSubStlmentShowListForApprovePara.get(i);
            itemUnit.setEngPMng_RowNo(i);
            progStlItemSubStlmentService.insertRecordDetail(itemUnit);
        }
    }
    @Transactional
    public String updateRecord(ProgStlInfoShow progStlInfoShowPara){
        // 为了防止异步操作数据
        return updateRecord(fromModelShowToModel(progStlInfoShowPara));
    }
    public String updateRecord(ProgStlInfo progStlInfoPara){
        // 为了防止异步操作数据
        ProgStlInfo progStlInfoTemp=getProgStlInfoByPkid(progStlInfoPara.getPkid());
        if(progStlInfoTemp!=null){
            //此条记录目前在数据库中的版本
            int intRecVersionInDB=ToolUtil.getIntIgnoreNull(progStlInfoTemp.getRecVersion());
            int intRecVersion=ToolUtil.getIntIgnoreNull(progStlInfoPara.getRecVersion());
            if(intRecVersionInDB!=intRecVersion) {
                return "1";
            }
        }
        progStlInfoPara.setRecVersion(
                ToolUtil.getIntIgnoreNull(progStlInfoPara.getRecVersion())+1);
        progStlInfoPara.setArchivedFlag("0");
        progStlInfoPara.setLastUpdBy(ToolUtil.getOperatorManager().getOperator().getPkid());
        progStlInfoPara.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        progStlInfoMapper.updateByPrimaryKey(progStlInfoPara);
        flowCtrlHisService.insertRecord(
                fromProgStlInfoToFlowCtrlHis(progStlInfoPara));
        return "0";
    }
    @Transactional
    public void updAutoLinkTask(ProgStlInfo progStlInfoPara) {

        // 数量结算
        if (EnumResType.RES_TYPE3.getCode().equals(progStlInfoPara.getStlType())) {
            if (EnumFlowStatus.FLOW_STATUS0.getCode().equals(progStlInfoPara.getFlowStatus())&&
                    EnumFlowStatusReason.FLOW_STATUS_REASON0.getCode().equals(progStlInfoPara.getFlowStatusReason())){
                CttInfo cttInfoTemp =cttInfoService.getCttInfoByPkId(progStlInfoPara.getStlPkid());
                if (("3").equals(cttInfoTemp.getType())||("6").equals(cttInfoTemp.getType())){
                    ProgStlInfoShow progStlInfoShowQryM =new ProgStlInfoShow();
                    progStlInfoShowQryM.setStlType("4");
                    progStlInfoShowQryM.setStlPkid(progStlInfoPara.getStlPkid());
                    progStlInfoShowQryM.setPeriodNo(progStlInfoPara.getPeriodNo());
                    List<ProgStlInfoShow> progStlInfoShowConstructsTemp =selectSubcttStlQMByStatusFlagBegin_End(progStlInfoShowQryM);
                    if (progStlInfoShowConstructsTemp.size()==0){
                        progStlInfoPara.setAutoLinkAdd("0");
                        progStlInfoShowQryM.setAutoLinkAdd("1");
                        progStlInfoShowQryM.setId(getMaxId( progStlInfoShowQryM.getStlType()));
                        insertRecord(progStlInfoShowQryM);
                    }else{
                        for (ProgStlInfoShow esISSOMPCUnit : progStlInfoShowConstructsTemp) {
                            if(("").equals(ToolUtil.getStrIgnoreNull(esISSOMPCUnit.getFlowStatus()))){
                                progStlInfoPara.setAutoLinkAdd("0");
                                progStlInfoShowQryM.setAutoLinkAdd("1");
                                updateRecord(progStlInfoShowQryM);
                            }else{
                                if(("1").equals(esISSOMPCUnit.getAutoLinkAdd())){
                                    progStlInfoPara.setAutoLinkAdd("0");
                                }else{
                                    progStlInfoPara.setAutoLinkAdd("1");
                                }
                            }
                        }
                    }
                }
            } else {
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
                    if(EnumSubcttType.TYPE0.getCode().equals(
                        cttInfoService.getCttInfoByPkId(progStlInfoPara.getStlPkid()).getType())) {
                        progStlInfoSubStlmentTemp.setId(getStrMaxStlId(progStlInfoSubStlmentTemp.getStlType()));
                        progStlInfoSubStlmentTemp.setFlowStatus(EnumFlowStatus.FLOW_STATUS2.getCode());
                        progStlInfoSubStlmentTemp.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON3.getCode());
                        insertRecord(progStlInfoSubStlmentTemp);
                    }else if(EnumSubcttType.TYPE3.getCode().equals(
                            cttInfoService.getCttInfoByPkId(progStlInfoPara.getStlPkid()).getType())) {
                        ProgStlInfo progStlInfoSubMTemp = new ProgStlInfo();
                        progStlInfoSubMTemp.setStlType(EnumResType.RES_TYPE4.getCode());
                        progStlInfoSubMTemp.setStlPkid(progStlInfoPara.getStlPkid());
                        progStlInfoSubMTemp.setPeriodNo(progStlInfoPara.getPeriodNo());
                        progStlInfoSubMTemp.setFlowStatus(EnumFlowStatus.FLOW_STATUS2.getCode());
                        // 本分包合同及本期的分包材料结算也复核了
                        if (getInitStlListByModel(progStlInfoSubMTemp).size() > 0) {
                            progStlInfoSubStlmentTemp.setId(getStrMaxStlId(progStlInfoSubStlmentTemp.getStlType()));
                            progStlInfoSubStlmentTemp.setFlowStatus(EnumFlowStatus.FLOW_STATUS2.getCode());
                            progStlInfoSubStlmentTemp.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON3.getCode());
                            insertRecord(progStlInfoSubStlmentTemp);
                        }
                    }
                }
            }
            if(EnumFlowStatusReason.FLOW_STATUS_REASON4.getCode().equals(progStlInfoPara.getFlowStatusReason())){
                // 删除分包结算单信息数据
                ProgStlInfoExample exampleSubMStlInfo = new ProgStlInfoExample();
                exampleSubMStlInfo.createCriteria()
                        .andStlTypeEqualTo(EnumResType.RES_TYPE5.getCode())
                        .andStlPkidEqualTo(progStlInfoSubStlmentTemp.getStlPkid())
                        .andPeriodNoEqualTo(progStlInfoSubStlmentTemp.getPeriodNo());
                progStlInfoMapper.deleteByExample(exampleSubMStlInfo);
                }
            }
        }else
        // 材料结算
        if (EnumResType.RES_TYPE4.getCode().equals(progStlInfoPara.getStlType())) {
            if (EnumFlowStatus.FLOW_STATUS0.getCode().equals(progStlInfoPara.getFlowStatus())&&
                    EnumFlowStatusReason.FLOW_STATUS_REASON0.getCode().equals(progStlInfoPara.getFlowStatusReason())){
                CttInfo cttInfoTemp =cttInfoService.getCttInfoByPkId(progStlInfoPara.getStlPkid());
                if (("3").equals(cttInfoTemp.getType())||("6").equals(cttInfoTemp.getType())){
                    ProgStlInfoShow progStlInfoShowQryQ =new ProgStlInfoShow();
                    progStlInfoShowQryQ.setStlPkid(progStlInfoPara.getStlPkid());
                    progStlInfoShowQryQ.setStlType("3");
                    progStlInfoShowQryQ.setPeriodNo(progStlInfoPara.getPeriodNo());
                    List<ProgStlInfoShow> progStlInfoShowConstructsTemp =selectSubcttStlQMByStatusFlagBegin_End(progStlInfoShowQryQ);
                    if (progStlInfoShowConstructsTemp.size()==0){
                        progStlInfoPara.setAutoLinkAdd("0");
                        progStlInfoShowQryQ.setAutoLinkAdd("1");
                        progStlInfoShowQryQ.setId(getMaxId(progStlInfoShowQryQ.getStlType()));
                        insertRecord(progStlInfoShowQryQ);
                    }else{
                        for (ProgStlInfoShow esISSOMPCUnit : progStlInfoShowConstructsTemp) {
                            if(("").equals(ToolUtil.getStrIgnoreNull(esISSOMPCUnit.getFlowStatus()))){
                                progStlInfoPara.setAutoLinkAdd("0");
                                progStlInfoShowQryQ.setAutoLinkAdd("1");
                                updateRecord(progStlInfoShowQryQ);
                            }else{
                                if(("1").equals(esISSOMPCUnit.getAutoLinkAdd())){
                                    progStlInfoPara.setAutoLinkAdd("0");
                                }else{
                                    progStlInfoPara.setAutoLinkAdd("1");
                                }
                            }
                        }
                    }
                }
            }else{
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
                    if(EnumSubcttType.TYPE1.getCode().equals(
                            cttInfoService.getCttInfoByPkId(progStlInfoPara.getStlPkid()).getType())) {
                        progStlInfoSubStlmentTemp.setId(getStrMaxStlId(progStlInfoSubStlmentTemp.getStlType()));
                        progStlInfoSubStlmentTemp.setFlowStatus(EnumFlowStatus.FLOW_STATUS2.getCode());
                        progStlInfoSubStlmentTemp.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON3.getCode());
                        insertRecord(progStlInfoSubStlmentTemp);
                    }else if(EnumSubcttType.TYPE3.getCode().equals(
                        cttInfoService.getCttInfoByPkId(progStlInfoPara.getStlPkid()).getType())) {
                        ProgStlInfo progStlInfoSubMTemp = new ProgStlInfo();
                        progStlInfoSubMTemp.setStlType(EnumResType.RES_TYPE3.getCode());
                        progStlInfoSubMTemp.setStlPkid(progStlInfoPara.getStlPkid());
                        progStlInfoSubMTemp.setPeriodNo(progStlInfoPara.getPeriodNo());
                        progStlInfoSubMTemp.setFlowStatus(EnumFlowStatus.FLOW_STATUS2.getCode());
                        // 本分包合同及本期的分包材料结算也复核了
                        if (getInitStlListByModel(progStlInfoSubMTemp).size() > 0) {
                            progStlInfoSubStlmentTemp.setId(getStrMaxStlId(progStlInfoSubStlmentTemp.getStlType()));
                            progStlInfoSubStlmentTemp.setFlowStatus(EnumFlowStatus.FLOW_STATUS2.getCode());
                            progStlInfoSubStlmentTemp.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON3.getCode());
                            insertRecord(progStlInfoSubStlmentTemp);
                        }
                    }
                }
            }
            if(EnumFlowStatusReason.FLOW_STATUS_REASON4.getCode().equals(progStlInfoPara.getFlowStatusReason())){
                // 删除分包结算单信息数据
                ProgStlInfoExample exampleSubMStlInfo = new ProgStlInfoExample();
                exampleSubMStlInfo.createCriteria()
                        .andStlTypeEqualTo(EnumResType.RES_TYPE5.getCode())
                        .andStlPkidEqualTo(progStlInfoSubStlmentTemp.getStlPkid())
                        .andPeriodNoEqualTo(progStlInfoSubStlmentTemp.getPeriodNo());
                progStlInfoMapper.deleteByExample(exampleSubMStlInfo);
                }
            }
        }
        updateRecord(progStlInfoPara);
    }
    // 更新 End

    // 删除 Start
    // 分包数量结算
    @Transactional
    public int delSubQStlInfoAndItem(ProgStlInfoShow progStlInfoShowPara) {
        // 删除分包数量结算详细数据
        int intDelSubQItemNum =
                progStlItemSubQService.delByCttPkidAndPeriodNo(
                        progStlInfoShowPara.getStlPkid(),
                        progStlInfoShowPara.getPeriodNo());
        // 删除分包数量结算信息数据
        ProgStlInfoExample exampleSubQStlInfo = new ProgStlInfoExample();
        exampleSubQStlInfo.createCriteria()
                .andStlTypeEqualTo(EnumResType.RES_TYPE3.getCode())
                .andStlPkidEqualTo(progStlInfoShowPara.getStlPkid())
                .andPeriodNoEqualTo(progStlInfoShowPara.getPeriodNo());
        int intDelSubQInfoNum=progStlInfoMapper.deleteByExample(exampleSubQStlInfo);

        if (intDelSubQItemNum <= 0 && intDelSubQInfoNum <= 0) {
            return 0;
        }
        return 1;
    }
    // 分包材料结算
    @Transactional
    public int delSubMStlInfoAndItem(ProgStlInfoShow progStlInfoShowPara) {
        // 删除分包材料结算详细数据
        int intDelSubMItemNum =
                progStlItemSubMService.delByCttPkidAndPeriodNo(
                        progStlInfoShowPara.getStlPkid(),
                        progStlInfoShowPara.getPeriodNo());
        // 删除分包材料结算信息数据
        ProgStlInfoExample exampleSubMStlInfo = new ProgStlInfoExample();
        exampleSubMStlInfo.createCriteria()
                .andStlTypeEqualTo(EnumResType.RES_TYPE4.getCode())
                .andStlPkidEqualTo(progStlInfoShowPara.getStlPkid())
                .andPeriodNoEqualTo(progStlInfoShowPara.getPeriodNo());
        int intDelSubMInfoNum=progStlInfoMapper.deleteByExample(exampleSubMStlInfo);

        if (intDelSubMItemNum + intDelSubMInfoNum == 0) {
            return 0;
        }
        return 1;
    }
    // 分包数量材料结算
    @Transactional
    public int delSubQMStlInfoAndItem(ProgStlInfoShow progStlInfoShowPara) {
        // 删除分包数量结算详细数据
        int intDelSubQItemNum =
                progStlItemSubQService.delByCttPkidAndPeriodNo(
                        progStlInfoShowPara.getStlPkid(),
                        progStlInfoShowPara.getPeriodNo());
        // 删除分包数量结算信息数据
        ProgStlInfoExample exampleSubQStlInfo = new ProgStlInfoExample();
        exampleSubQStlInfo.createCriteria()
                .andStlTypeEqualTo(EnumResType.RES_TYPE3.getCode())
                .andStlPkidEqualTo(progStlInfoShowPara.getStlPkid())
                .andPeriodNoEqualTo(progStlInfoShowPara.getPeriodNo());
        int intDelSubQInfoNum=progStlInfoMapper.deleteByExample(exampleSubQStlInfo);

        // 删除分包材料结算详细数据
        int intDelSubMItemNum =
                progStlItemSubMService.delByCttPkidAndPeriodNo(
                        progStlInfoShowPara.getStlPkid(),
                        progStlInfoShowPara.getPeriodNo());
        // 删除分包材料结算信息数据
        ProgStlInfoExample exampleSubMStlInfo = new ProgStlInfoExample();
        exampleSubMStlInfo.createCriteria()
                .andStlTypeEqualTo(EnumResType.RES_TYPE4.getCode())
                .andStlPkidEqualTo(progStlInfoShowPara.getStlPkid())
                .andPeriodNoEqualTo(progStlInfoShowPara.getPeriodNo());
        int intDelSubMInfoNum=progStlInfoMapper.deleteByExample(exampleSubMStlInfo);

        if (intDelSubQItemNum+intDelSubQInfoNum+intDelSubMItemNum+intDelSubMInfoNum == 0) {
            return 0;
        }
        return 1;
    }
    //分包价格结算
    @Transactional
    public void delSubPApproveFailTo(ProgStlInfo progStlInfoPara,String powerType){
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
        updateRecord(progStlInfoTemp);
        //删除PROG_STL_ITEM_SUB_STLMENT表中的相应记录
        progStlItemSubStlmentService.deleteRecordByExample(progStlInfoPara.getStlPkid(), progStlInfoPara.getPeriodNo());
    }
    // 总包统计结算
    @Transactional
    public int delTkEstStlInfoAndItem(ProgStlInfoShow progStlInfoShowPara) {
        // 删除详细数据
        int intDelTkEstItemNum =
                progStlItemTkEstService.delByCttPkidAndPeriodNo(
                        progStlInfoShowPara.getStlPkid(),
                        progStlInfoShowPara.getPeriodNo());
        // 删除总包统计结算信息数据
        ProgStlInfoExample exampleTkEstStlInfo = new ProgStlInfoExample();
        exampleTkEstStlInfo.createCriteria()
                .andStlTypeEqualTo(EnumResType.RES_TYPE6.getCode())
                .andStlPkidEqualTo(progStlInfoShowPara.getStlPkid())
                .andPeriodNoEqualTo(progStlInfoShowPara.getPeriodNo());
        int intDelTkEstInfoNum=progStlInfoMapper.deleteByExample(exampleTkEstStlInfo);
        if (intDelTkEstItemNum + intDelTkEstInfoNum == 0) {
            return 0;
        }
        return 1;
    }
    // 总包计量结算
    @Transactional
    public int delTkMeaStlInfoAndItem(ProgStlInfoShow progStlInfoShowPara) {
        // 删除详细数据
        int intDelTkMeaItemNum =
                progStlItemTkMeaService.delByCttPkidAndPeriodNo(
                        progStlInfoShowPara.getStlPkid(),
                        progStlInfoShowPara.getPeriodNo());
        // 删除总包统计结算信息数据
        ProgStlInfoExample exampleTkEstStlInfo = new ProgStlInfoExample();
        exampleTkEstStlInfo.createCriteria()
                .andStlTypeEqualTo(EnumResType.RES_TYPE7.getCode())
                .andStlPkidEqualTo(progStlInfoShowPara.getStlPkid())
                .andPeriodNoEqualTo(progStlInfoShowPara.getPeriodNo());
        int intDelTkMeaInfoNum=progStlInfoMapper.deleteByExample(exampleTkEstStlInfo);
        if (intDelTkMeaItemNum + intDelTkMeaInfoNum == 0) {
            return 0;
        }
        return 1;
    }
    // 删除 End
    public String getMaxId(String strStlType) {
        Integer intTemp;
        String strMaxId = getStrMaxStlId(strStlType);
        if (StringUtils.isEmpty(ToolUtil.getStrIgnoreNull(strMaxId))) {
            strMaxId = "STLQ" + ToolUtil.getStrToday() + "001";
        } else {
            if (strMaxId.length() > 3) {
                String strTemp = strMaxId.substring(strMaxId.length() - 3).replaceFirst("^0+", "");
                if (ToolUtil.strIsDigit(strTemp)) {
                    intTemp = Integer.parseInt(strTemp);
                    intTemp = intTemp + 1;
                    strMaxId = strMaxId.substring(0, strMaxId.length() - 3) + StringUtils.leftPad(intTemp.toString(), 3, "0");
                } else {
                    strMaxId += "001";
                }
            }
        }
        return strMaxId;
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

    public List<ProgStlInfoShow> getNotFormSubcttStlP(String strParentPkid,
                                                               String strStlPkid,
                                                               String strPeriodNo){
        return myProgStlInfoMapper.getNotFormSubcttStlP(strParentPkid, strStlPkid, strPeriodNo);
    }
    public List<ProgStlInfoShow> getFormPreSubcttStlP(String strParentPkid,
                                                               String strStlPkid,
                                                               String strPeriodNo){
        return myProgStlInfoMapper.getFormPreSubcttStlP(strParentPkid,strStlPkid,strPeriodNo);
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
