package epss.service;

import epss.common.enums.EnumOperType;
import epss.repository.dao.CttInfoMapper;
import epss.repository.dao.not_mybatis.MyCttInfoMapper;
import epss.repository.dao.not_mybatis.MyDeptAndOperMapper;
import epss.repository.model.*;
import epss.repository.model.model_show.CttInfoShow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Autowired
    private MyDeptAndOperMapper myDeptAndOperMapper;

    public String getUserName(String operPkidPara){
        return myDeptAndOperMapper.getUserName(ToolUtil.getStrIgnoreNull(operPkidPara));
    }

    public List<CttInfo> getListByModelShow(CttInfoShow cttInfoShowPara) {
        CttInfoExample example= new CttInfoExample();
        CttInfoExample.Criteria criteria = example.createCriteria();
        //可以为NULL的项
        if(!ToolUtil.getStrIgnoreNull(cttInfoShowPara.getCttType()).equals("")){
            criteria.andCttTypeEqualTo(ToolUtil.getStrIgnoreNull(cttInfoShowPara.getCttType()));
        }
        if(!ToolUtil.getStrIgnoreNull(cttInfoShowPara.getId()).equals("")){
            criteria.andIdEqualTo(ToolUtil.getStrIgnoreNull(cttInfoShowPara.getId()));
        }
        if(!ToolUtil.getStrIgnoreNull(cttInfoShowPara.getName()).equals("")){
            criteria.andNameEqualTo(ToolUtil.getStrIgnoreNull(cttInfoShowPara.getName()));
        }
        if(!ToolUtil.getStrIgnoreNull(cttInfoShowPara.getParentPkid()).equals("")){
            criteria.andParentPkidEqualTo(ToolUtil.getStrIgnoreNull(cttInfoShowPara.getParentPkid()));
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
    public List<CttInfo> getListByModel(CttInfo cttInfoPara) {
        return getListByModelShow(fromModelToModelShow(cttInfoPara));
    }

    public List<CttInfoShow> getCttInfoListByCttType_Status(
            String strCttyTypePara,String strStatusPara) {
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

    public List<CttInfoShow> selectRecordsFromCtt(String parentPkidPara,String strTkcttInfoName){
        return  myCttInfoMapper.selectRecordsFromCtt(parentPkidPara,strTkcttInfoName);
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

    public boolean findChildRecordsByPkid(String strPkidPara) {
        CttInfoExample example = new CttInfoExample();
        CttInfoExample.Criteria criteria = example.createCriteria();
        criteria.andParentPkidEqualTo(strPkidPara);
        return (cttInfoMapper.selectByExample(example)).size()>0;
    }

    public FlowCtrlHis fromCttInfoToFlowCtrlHis(CttInfo cttInfoPara,String strOperTypePara){
        FlowCtrlHis flowCtrlHisTemp =new FlowCtrlHis();
        flowCtrlHisTemp.setInfoType(cttInfoPara.getCttType());
        flowCtrlHisTemp.setInfoPkid(cttInfoPara.getPkid());
        flowCtrlHisTemp.setInfoId(cttInfoPara.getId());
        flowCtrlHisTemp.setInfoName(cttInfoPara.getName());
        flowCtrlHisTemp.setFlowStatus(cttInfoPara.getFlowStatus());
        flowCtrlHisTemp.setFlowStatusReason(cttInfoPara.getFlowStatusReason());
        flowCtrlHisTemp.setFlowStatusRemark(cttInfoPara.getFlowStatusRemark());
        flowCtrlHisTemp.setCreatedBy(cttInfoPara.getCreatedBy());
        flowCtrlHisTemp.setCreatedByName(getUserName(cttInfoPara.getCreatedBy()));
        flowCtrlHisTemp.setCreatedTime(cttInfoPara.getCreatedTime());
        flowCtrlHisTemp.setRemark(cttInfoPara.getRemark());
        flowCtrlHisTemp.setOperType(strOperTypePara);
        return flowCtrlHisTemp;
    }

    @Transactional
    public void insertRecord(CttInfoShow cttInfoShowPara) {
        String strOperatorIdTemp=ToolUtil.getOperatorManager().getOperator().getPkid();
        String strLastUpdTimeTemp=ToolUtil.getStrLastUpdTime();
        CttInfo cttInfoTemp=fromModelShowToModel(cttInfoShowPara);
        cttInfoTemp.setArchivedFlag("0");
        cttInfoTemp.setCreatedBy(strOperatorIdTemp);
        cttInfoTemp.setCreatedTime(strLastUpdTimeTemp);
        cttInfoTemp.setLastUpdBy(strOperatorIdTemp);
        cttInfoTemp.setLastUpdTime(strLastUpdTimeTemp);
        cttInfoMapper.insertSelective(cttInfoTemp);
        flowCtrlHisService.insertRecord(
                fromCttInfoToFlowCtrlHis(cttInfoTemp,EnumOperType.OPER_TYPE0.getCode()));
    }
    @Transactional
    public void insertRecord(CttInfo cttInfoPara) {
        String strOperatorIdTemp=ToolUtil.getOperatorManager().getOperator().getPkid();
        String strLastUpdTimeTemp=ToolUtil.getStrLastUpdTime();
        cttInfoPara.setArchivedFlag("0");
        cttInfoPara.setCreatedBy(strOperatorIdTemp);
        cttInfoPara.setCreatedTime(strLastUpdTimeTemp);
        cttInfoPara.setLastUpdBy(strOperatorIdTemp);
        cttInfoPara.setLastUpdTime(strLastUpdTimeTemp);
        cttInfoMapper.insertSelective(cttInfoPara);
        flowCtrlHisService.insertRecord(
                fromCttInfoToFlowCtrlHis(cttInfoPara,EnumOperType.OPER_TYPE0.getCode()));
    }
    @Transactional
    public String updateRecord(CttInfoShow cttInfoShowPara){
        // 为了防止异步操作数据
        return updateRecord(fromModelShowToModel(cttInfoShowPara));
    }
    @Transactional
    public String updateRecord(CttInfo cttInfoPara){
        CttInfo cttInfoTemp=getCttInfoByPkId(cttInfoPara.getPkid());
        if(cttInfoTemp!=null){
            //此条记录目前在数据库中的版本
            int intRecVersionInDB=ToolUtil.getIntIgnoreNull(cttInfoTemp.getRecVersion());
            int intRecVersion=ToolUtil.getIntIgnoreNull(cttInfoPara.getRecVersion());
            if(intRecVersionInDB!=intRecVersion) {
                return "1";
            }
        }
        cttInfoPara.setRecVersion(
                ToolUtil.getIntIgnoreNull(cttInfoPara.getRecVersion())+1);
        cttInfoPara.setArchivedFlag("0");
        cttInfoPara.setLastUpdBy(ToolUtil.getOperatorManager().getOperator().getPkid());
        cttInfoPara.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        cttInfoMapper.updateByPrimaryKey(cttInfoPara);
        flowCtrlHisService.insertRecord(
                fromCttInfoToFlowCtrlHis(cttInfoPara,EnumOperType.OPER_TYPE1.getCode()));
        return "0";
    }
    @Transactional
    public int deleteRecord(String strCttInfoPkidPara){
        CttInfo cttInfoTemp = getCttInfoByPkId(strCttInfoPkidPara);
        flowCtrlHisService.insertRecord(
                fromCttInfoToFlowCtrlHis(cttInfoTemp,EnumOperType.OPER_TYPE1.getCode()));
        return cttInfoMapper.deleteByPrimaryKey(strCttInfoPkidPara);
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
        cttInfoTemp.setFlowStatusReason(cttInfoTemp.getFlowStatusReason());
        cttInfoTemp.setFlowStatusRemark(cttInfoTemp.getFlowStatusRemark());
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
        cttInfoShowTemp.setFlowStatusRemark(cttInfoPara.getFlowStatusRemark());
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
