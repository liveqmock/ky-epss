package epss.service;

import epss.common.enums.EnumOperType;
import epss.repository.dao.CttInfoMapper;
import epss.repository.dao.not_mybatis.MyCttInfoMapper;
import epss.repository.model.*;
import epss.repository.model.model_show.CttInfoShow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import skyline.util.ToolUtil;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-1-31
 * Time: 下午6:31
 * To change this template use File | Settings | File Templates.
 */
@Service
public class CttInfoService {
    @Autowired
    private CttInfoMapper cttInfoMapper;
    @Autowired
    private MyCttInfoMapper myCttInfoMapper;
    @Resource
    private FlowCtrlHisService flowCtrlHisService;

    public List<CttInfo> selectListByModel(CttInfoShow cttInfoShowPara) {
        CttInfoExample example= new CttInfoExample();
        CttInfoExample.Criteria criteria = example.createCriteria();
        criteria.andCttTypeEqualTo(ToolUtil.getStrIgnoreNull(cttInfoShowPara.getCttType()))
                .andIdLike("%" + ToolUtil.getStrIgnoreNull(cttInfoShowPara.getId()) + "%")
                .andNameLike("%" + ToolUtil.getStrIgnoreNull(cttInfoShowPara.getName()) + "%");
        //可以为NULL的项
        if(!ToolUtil.getStrIgnoreNull(cttInfoShowPara.getParentPkid()).equals("")){
            criteria.andParentPkidLike(ToolUtil.getStrIgnoreNull(cttInfoShowPara.getParentPkid()));
        }
        if(!ToolUtil.getStrIgnoreNull(cttInfoShowPara.getSignDate()).equals("")){
            criteria.andSignDateLike(ToolUtil.getStrIgnoreNull(cttInfoShowPara.getSignDate()));
        }
        if(!ToolUtil.getStrIgnoreNull(cttInfoShowPara.getSignPartA()).equals("")){
            criteria.andSignPartALike(ToolUtil.getStrIgnoreNull(cttInfoShowPara.getSignPartA()));
        }
        if(!ToolUtil.getStrIgnoreNull(cttInfoShowPara.getSignPartB()).equals("")){
            criteria.andSignPartBLike(ToolUtil.getStrIgnoreNull(cttInfoShowPara.getSignPartB()));
        }
        if(!ToolUtil.getStrIgnoreNull(cttInfoShowPara.getCttStartDate()).equals("")){
            criteria.andCttStartDateLike(ToolUtil.getStrIgnoreNull(cttInfoShowPara.getCttStartDate()));
        }
        if(!ToolUtil.getStrIgnoreNull(cttInfoShowPara.getCttEndDate()).equals("")){
            criteria.andCttEndDateLike(ToolUtil.getStrIgnoreNull(cttInfoShowPara.getCttEndDate()));
        }
        example.setOrderByClause("ID ASC") ;
        return cttInfoMapper.selectByExample(example);
    }

    public List<CttInfoShow> getCttInfoListByCttType_Status(String strCttyTypePara,String strStatusPara) {
        return myCttInfoMapper.getCttInfoListByCttType_Status(strCttyTypePara,strStatusPara);
    }

    public List<CttInfoShow> getCttInfoListByCttType_ParentPkid_Status(
            String strCttyTypePara,
            String strParentPkidPara,
            String strStatusPara) {
        return myCttInfoMapper.getCttInfoListByCttType_ParentPkid_Status(
                strCttyTypePara,
                strParentPkidPara,
                strStatusPara);
    }

    public List<CttInfoShow> selectRecordsFromCtt(String parentPkidPara){
        return  myCttInfoMapper.selectRecordsFromCtt(parentPkidPara);
    }

    public List<CttInfo> getEsInitCttByCttTypeAndBelongToPkId(String strCttType,String strBelongToPkid) {
        CttInfoExample example= new CttInfoExample();
        CttInfoExample.Criteria criteria = example.createCriteria();
        criteria.andCttTypeEqualTo(ToolUtil.getStrIgnoreNull(strCttType))
                .andParentPkidEqualTo(ToolUtil.getStrIgnoreNull(strBelongToPkid));
        example.setOrderByClause("ID ASC") ;
        return cttInfoMapper.selectByExample(example);
    }

    public List<CttInfo> getEsInitCttListByCttType(String strCttType) {
        CttInfoExample example= new CttInfoExample();
        CttInfoExample.Criteria criteria = example.createCriteria();
        criteria.andCttTypeEqualTo(ToolUtil.getStrIgnoreNull(strCttType));
        example.setOrderByClause("ID ASC") ;
        return cttInfoMapper.selectByExample(example);
    }

    public CttInfo getCttInfoByPkId(String strPkid) {
        return cttInfoMapper.selectByPrimaryKey(strPkid);
    }

    /**
     * 判断记录是否已存在
     *
     * @param   cttInfoShowPara
     * @return
     */
    public boolean isExistInDb(CttInfoShow cttInfoShowPara) {
        CttInfoExample example = new CttInfoExample();
        CttInfoExample.Criteria criteria = example.createCriteria();
        criteria.andCttTypeEqualTo(cttInfoShowPara.getCttType())
                .andNameEqualTo(cttInfoShowPara.getName());
        return cttInfoMapper.countByExample(example) >= 1;
    }
    public boolean isExistInDb(CttInfo cttInfoPara) {
        CttInfoExample example = new CttInfoExample();
        CttInfoExample.Criteria criteria = example.createCriteria();
        criteria.andCttTypeEqualTo(cttInfoPara.getCttType())
                .andIdEqualTo(cttInfoPara.getId())
                .andNameEqualTo(cttInfoPara.getName());
        return cttInfoMapper.countByExample(example) >= 1;
    }
    //验证合同编号和名称是否已存在
    public boolean IdisExistInDb(CttInfoShow cttInfoShowPara) {
        CttInfoExample example = new CttInfoExample();
        CttInfoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(cttInfoShowPara.getId());
        return cttInfoMapper.countByExample(example) >= 1;
    }
    public boolean NameisExistInDb(CttInfoShow cttInfoShowPara) {
        CttInfoExample example = new CttInfoExample();
        CttInfoExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo(cttInfoShowPara.getName());
        return cttInfoMapper.countByExample(example) >= 1;
    }

    public void insertRecord(CttInfoShow cttInfoShowPara) {
        String strOperatorIdTemp=ToolUtil.getOperatorManager().getOperatorId();
        String strLastUpdTimeTemp=ToolUtil.getStrLastUpdTime();
        cttInfoShowPara.setArchivedFlag("0");
        cttInfoShowPara.setCreatedBy(strOperatorIdTemp);
        cttInfoShowPara.setCreatedTime(strLastUpdTimeTemp);
        cttInfoShowPara.setLastUpdBy(strOperatorIdTemp);
        cttInfoShowPara.setLastUpdTime(strLastUpdTimeTemp);
        cttInfoMapper.insertSelective(fromModelShowToModel(cttInfoShowPara));
    }
    public void insertRecord(CttInfo cttInfoPara) {
        String strOperatorIdTemp=ToolUtil.getOperatorManager().getOperatorId();
        String strLastUpdTimeTemp=ToolUtil.getStrLastUpdTime();
        cttInfoPara.setArchivedFlag("0");
        cttInfoPara.setCreatedBy(strOperatorIdTemp);
        cttInfoPara.setCreatedTime(strLastUpdTimeTemp);
        cttInfoPara.setLastUpdBy(strOperatorIdTemp);
        cttInfoPara.setLastUpdTime(strLastUpdTimeTemp);
        cttInfoMapper.insertSelective(cttInfoPara);
    }

    public FlowCtrlHis fromCttInfoToFlowCtrlHis(CttInfo cttInfoPara){
        FlowCtrlHis flowCtrlHisTemp =new FlowCtrlHis();
        flowCtrlHisTemp.setInfoType(cttInfoPara.getCttType());
        flowCtrlHisTemp.setInfoPkid(cttInfoPara.getPkid());
        flowCtrlHisTemp.setInfoId(cttInfoPara.getId());
        flowCtrlHisTemp.setInfoName(cttInfoPara.getName());
        //flowCtrlHisTemp.setPeriodNo();
        flowCtrlHisTemp.setFlowStatus(cttInfoPara.getFlowStatus());
        flowCtrlHisTemp.setFlowStatusReason(cttInfoPara.getFlowStatusReason());
        flowCtrlHisTemp.setCreatedBy(cttInfoPara.getCreatedBy());
        flowCtrlHisTemp.setCreatedByName(ToolUtil.getUserName(cttInfoPara.getCreatedBy()));
        flowCtrlHisTemp.setCreatedTime(cttInfoPara.getCreatedTime());
        flowCtrlHisTemp.setRemark(cttInfoPara.getRemark());
        return flowCtrlHisTemp;
    }
    public void updateRecord(CttInfo cttInfoPara,String strPowerTypePara){
        try {
            cttInfoPara.setRecVersion(
                ToolUtil.getIntIgnoreNull(cttInfoPara.getRecVersion())+1);
            cttInfoPara.setArchivedFlag("0");
            cttInfoPara.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
            cttInfoPara.setLastUpdTime(ToolUtil.getStrLastUpdTime());
            cttInfoMapper.updateByPrimaryKey(cttInfoPara);
            FlowCtrlHis flowCtrlHisTemp=fromCttInfoToFlowCtrlHis(cttInfoPara);
            List<Oper> operListTemp=flowCtrlHisService.selectOperByExample(ToolUtil.getOperatorManager().getOperatorId());
            for(Oper operTemp:operListTemp){
                if (ToolUtil.getOperatorManager().getOperatorId().equals(operTemp.getId())){
                    flowCtrlHisTemp.setTid(operTemp.getTid());
                }
            }
            if(strPowerTypePara.contains("Pass")){
                flowCtrlHisService.insertRecord(flowCtrlHisTemp, EnumOperType.OPER_TYPE0.getCode());
            }else if(strPowerTypePara.contains("Fail")){
                flowCtrlHisService.insertRecord(flowCtrlHisTemp, EnumOperType.OPER_TYPE1.getCode());
            }else if(strPowerTypePara.contains("Del")){
                flowCtrlHisService.insertRecord(flowCtrlHisTemp, EnumOperType.OPER_TYPE2.getCode());
            }else if(strPowerTypePara.equals("AttachAdd")){
                flowCtrlHisTemp.setRemark(flowCtrlHisTemp.getRemark()+"AttachAdd");
                flowCtrlHisService.insertRecord(flowCtrlHisTemp, EnumOperType.OPER_TYPE1.getCode());
            }else if(strPowerTypePara.equals("AttachRemove")){
                flowCtrlHisTemp.setRemark(flowCtrlHisTemp.getRemark()+"AttachRemove");
                flowCtrlHisService.insertRecord(flowCtrlHisTemp, EnumOperType.OPER_TYPE2.getCode());
            }
        } catch (Exception e) {
            throw e;
        }
    }
    public void updateRecord(CttInfoShow cttInfoShowPara){
        cttInfoShowPara.setRecVersion(
                ToolUtil.getIntIgnoreNull(cttInfoShowPara.getRecVersion())+1);
        cttInfoShowPara.setArchivedFlag("0");
        cttInfoShowPara.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        cttInfoShowPara.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        cttInfoMapper.updateByPrimaryKey(fromModelShowToModel(cttInfoShowPara));
    }
    public void updateRecordForOperRes(CttInfoShow cttInfoShowPara){
        CttInfo cttInfoTemp = getCttInfoByPkId(cttInfoShowPara.getPkid());
        cttInfoTemp.setName(cttInfoShowPara.getName());
        cttInfoTemp.setRecVersion(
                ToolUtil.getIntIgnoreNull(cttInfoShowPara.getRecVersion())+1);
        cttInfoTemp.setArchivedFlag("0");
        cttInfoTemp.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        cttInfoTemp.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        cttInfoMapper.updateByPrimaryKey(cttInfoTemp);
    }

    public int deleteRecord(String strPkId){
        return cttInfoMapper.deleteByPrimaryKey(strPkId);
    }

    public String getStrMaxCttId(String strCttType){
        return myCttInfoMapper.getStrMaxCttId(strCttType) ;
    }

    public CttInfo fromModelShowToModel(CttInfoShow cttInfoShowPara) {
        CttInfo cttInfoTemp = new CttInfo();
        cttInfoTemp.setPkid(cttInfoShowPara.getPkid());
        cttInfoTemp.setCttType(cttInfoShowPara.getCttType());
        cttInfoTemp.setParentPkid(cttInfoShowPara.getParentPkid());
        cttInfoTemp.setId(cttInfoShowPara.getId());
        cttInfoTemp.setName(cttInfoShowPara.getName());
        cttInfoTemp.setCttStartDate(cttInfoShowPara.getCttStartDate());
        cttInfoTemp.setCttEndDate(cttInfoShowPara.getCttEndDate());
        cttInfoTemp.setSignDate(cttInfoShowPara.getSignDate());
        cttInfoTemp.setSignPartA(cttInfoShowPara.getSignPartA());
        cttInfoTemp.setSignPartB(cttInfoShowPara.getSignPartB());
        cttInfoTemp.setFlowStatus(cttInfoShowPara.getFlowStatus());
        cttInfoTemp.setFlowStatusReason(cttInfoShowPara.getFlowStatusReason());
        cttInfoTemp.setRemark(cttInfoShowPara.getRemark());
        cttInfoTemp.setAttachment(cttInfoShowPara.getAttachment());
        cttInfoTemp.setArchivedFlag(cttInfoShowPara.getArchivedFlag());
        cttInfoTemp.setCreatedBy(cttInfoShowPara.getCreatedBy());
        cttInfoTemp.setCreatedTime(cttInfoShowPara.getCreatedTime());
        cttInfoTemp.setLastUpdBy(cttInfoShowPara.getLastUpdBy());
        cttInfoTemp.setLastUpdTime(cttInfoShowPara.getLastUpdTime());
        cttInfoTemp.setRecVersion(cttInfoShowPara.getRecVersion());
        cttInfoTemp.setType(cttInfoShowPara.getType());
        return cttInfoTemp;
    }
    public CttInfoShow fromModelToModelShow(CttInfo cttInfoPara) {
        CttInfoShow cttInfoShowTemp = new CttInfoShow();
        cttInfoShowTemp.setPkid(cttInfoPara.getPkid());
        cttInfoShowTemp.setCttType(cttInfoPara.getCttType());
        cttInfoShowTemp.setParentPkid(cttInfoPara.getParentPkid());
        cttInfoShowTemp.setId(cttInfoPara.getId());
        cttInfoShowTemp.setName(cttInfoPara.getName());
        cttInfoShowTemp.setCttStartDate(cttInfoPara.getCttStartDate());
        cttInfoShowTemp.setCttEndDate(cttInfoPara.getCttEndDate());
        cttInfoShowTemp.setSignDate(cttInfoPara.getSignDate());
        cttInfoShowTemp.setSignPartA(cttInfoPara.getSignPartA());
        cttInfoShowTemp.setSignPartB(cttInfoPara.getSignPartB());
        cttInfoShowTemp.setRemark(cttInfoPara.getRemark());
        cttInfoShowTemp.setFlowStatus(cttInfoPara.getFlowStatus());
        cttInfoShowTemp.setFlowStatusReason(cttInfoPara.getFlowStatusReason());
        cttInfoShowTemp.setAttachment(cttInfoPara.getAttachment());
        cttInfoShowTemp.setArchivedFlag(cttInfoPara.getArchivedFlag());
        cttInfoShowTemp.setCreatedBy(cttInfoPara.getCreatedBy());
        cttInfoShowTemp.setCreatedTime(cttInfoPara.getCreatedTime());
        cttInfoShowTemp.setLastUpdBy(cttInfoPara.getLastUpdBy());
        cttInfoShowTemp.setLastUpdTime(cttInfoPara.getLastUpdTime());
        cttInfoShowTemp.setRecVersion(cttInfoPara.getRecVersion());
        cttInfoShowTemp.setType(cttInfoPara.getType());
        return cttInfoShowTemp;
    }
    //更新甲供材情况
    public int updateByPKid(CttInfo cttInfoPara){
        return cttInfoMapper.updateByPrimaryKey(cttInfoPara);
    }

    public Integer getChildrenOfThisRecordInEsInitCtt(String strCttType,String strBelongToPkid){
        return myCttInfoMapper.getChildrenOfThisRecordInEsInitCtt(strCttType,strBelongToPkid);
    }

    public List<CttInfoShow> selectCttByStatusFlagBegin_End(CttInfoShow cttInfoShowPara){
        return myCttInfoMapper.selectCttByStatusFlagBegin_End(cttInfoShowPara);
    }

    /*public List<AllTabColumns>  getColumnNameByTableName(String strTableName){
        *//*return commonMapper.getColumnNameByTableName(strTableName) ;*//*
        AllTabColumnsExample  example = new AllTabColumnsExample();
        example.createCriteria().andTableNameEqualTo(strTableName);
        return allTabColumnsMapper.selectByExample(example) ;
    }*/
}
