package epss.service;

import epss.common.enums.ESEnumOperType;
import epss.repository.dao.EsCttInfoMapper;
import epss.repository.dao.not_mybatis.MyCttInfoMapper;
import epss.repository.model.*;
import epss.repository.model.model_show.CttInfoShow;
import epss.repository.model.model_show.CttItemShow;
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
    private EsCttInfoMapper esCttInfoMapper;
    @Autowired
    private MyCttInfoMapper myCttInfoMapper;
    @Resource
    private FlowCtrlHisService flowCtrlHisService;


    public List<EsCttInfo> selectListByModel(CttInfoShow cttInfoShowPara) {
        EsCttInfoExample example= new EsCttInfoExample();
        EsCttInfoExample.Criteria criteria = example.createCriteria();
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
        return esCttInfoMapper.selectByExample(example);
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

    public List<EsCttInfo> getEsInitCttByCttTypeAndBelongToPkId(String strCttType,String strBelongToPkid) {
        EsCttInfoExample example= new EsCttInfoExample();
        EsCttInfoExample.Criteria criteria = example.createCriteria();
        criteria.andCttTypeEqualTo(ToolUtil.getStrIgnoreNull(strCttType))
                .andParentPkidEqualTo(ToolUtil.getStrIgnoreNull(strBelongToPkid));
        example.setOrderByClause("ID ASC") ;
        return esCttInfoMapper.selectByExample(example);
    }

    public List<EsCttInfo> getEsInitCttListByCttType(String strCttType) {
        EsCttInfoExample example= new EsCttInfoExample();
        EsCttInfoExample.Criteria criteria = example.createCriteria();
        criteria.andCttTypeEqualTo(ToolUtil.getStrIgnoreNull(strCttType));
        example.setOrderByClause("ID ASC") ;
        return esCttInfoMapper.selectByExample(example);
    }

    public EsCttInfo getCttInfoByPkId(String strPkid) {
        return esCttInfoMapper.selectByPrimaryKey(strPkid);
    }

    /**
     * 判断记录是否已存在
     *
     * @param   cttInfoShowPara
     * @return
     */
    public boolean isExistInDb(CttInfoShow cttInfoShowPara) {
        EsCttInfoExample example = new EsCttInfoExample();
        EsCttInfoExample.Criteria criteria = example.createCriteria();
        criteria.andCttTypeEqualTo(cttInfoShowPara.getCttType())
                .andNameEqualTo(cttInfoShowPara.getName());
        return esCttInfoMapper.countByExample(example) >= 1;
    }
    public boolean isExistInDb(EsCttInfo esCttInfoPara) {
        EsCttInfoExample example = new EsCttInfoExample();
        EsCttInfoExample.Criteria criteria = example.createCriteria();
        criteria.andCttTypeEqualTo(esCttInfoPara.getCttType())
                .andIdEqualTo(esCttInfoPara.getId())
                .andNameEqualTo(esCttInfoPara.getName());
        return esCttInfoMapper.countByExample(example) >= 1;
    }
    //验证合同编号和名称是否已存在
    public boolean IdisExistInDb(CttInfoShow cttInfoShowPara) {
        EsCttInfoExample example = new EsCttInfoExample();
        EsCttInfoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(cttInfoShowPara.getId());
        return esCttInfoMapper.countByExample(example) >= 1;
    }
    public boolean NameisExistInDb(CttInfoShow cttInfoShowPara) {
        EsCttInfoExample example = new EsCttInfoExample();
        EsCttInfoExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo(cttInfoShowPara.getName());
        return esCttInfoMapper.countByExample(example) >= 1;
    }

    public void insertRecord(CttInfoShow cttInfoShowPara) {
        String strOperatorIdTemp=ToolUtil.getOperatorManager().getOperatorId();
        String strLastUpdTimeTemp=ToolUtil.getStrLastUpdTime();
        cttInfoShowPara.setDeletedFlag("0");
        cttInfoShowPara.setCreatedBy(strOperatorIdTemp);
        cttInfoShowPara.setCreatedDate(strLastUpdTimeTemp);
        cttInfoShowPara.setLastUpdBy(strOperatorIdTemp);
        cttInfoShowPara.setLastUpdDate(strLastUpdTimeTemp);
        esCttInfoMapper.insertSelective(fromModelShowToModel(cttInfoShowPara));
    }
    public void insertRecord(EsCttInfo esCttInfoPara) {
        String strOperatorIdTemp=ToolUtil.getOperatorManager().getOperatorId();
        String strLastUpdTimeTemp=ToolUtil.getStrLastUpdTime();
        esCttInfoPara.setDeletedFlag("0");
        esCttInfoPara.setCreatedBy(strOperatorIdTemp);
        esCttInfoPara.setCreatedDate(strLastUpdTimeTemp);
        esCttInfoPara.setLastUpdBy(strOperatorIdTemp);
        esCttInfoPara.setLastUpdDate(strLastUpdTimeTemp);
        esCttInfoMapper.insertSelective(esCttInfoPara);
    }

    public FlowCtrlHis fromCttInfoToFlowCtrlHis(EsCttInfo esCttInfoPara){
        FlowCtrlHis flowCtrlHisTemp =new FlowCtrlHis();
        flowCtrlHisTemp.setInfoType(esCttInfoPara.getCttType());
        flowCtrlHisTemp.setInfoPkid(esCttInfoPara.getPkid());
        flowCtrlHisTemp.setInfoId(esCttInfoPara.getId());
        flowCtrlHisTemp.setInfoName(esCttInfoPara.getName());
        //flowCtrlHisTemp.setPeriodNo();
        flowCtrlHisTemp.setFlowStatus(esCttInfoPara.getFlowStatus());
        flowCtrlHisTemp.setFlowStatusReason(esCttInfoPara.getFlowStatusReason());
        flowCtrlHisTemp.setCreatedBy(esCttInfoPara.getCreatedBy());
        flowCtrlHisTemp.setCreatedByName(ToolUtil.getUserName(esCttInfoPara.getCreatedBy()));
        flowCtrlHisTemp.setCreatedTime(esCttInfoPara.getCreatedDate());
        flowCtrlHisTemp.setRemark(esCttInfoPara.getNote());
        return flowCtrlHisTemp;
    }
    public void updateRecord(EsCttInfo esCttInfoPara,String strPowerTypePara){
        try {esCttInfoPara.setModificationNum(
                ToolUtil.getIntIgnoreNull(esCttInfoPara.getModificationNum())+1);
            esCttInfoPara.setDeletedFlag("0");
            esCttInfoPara.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
            esCttInfoPara.setLastUpdDate(ToolUtil.getStrLastUpdTime());
            esCttInfoMapper.updateByPrimaryKey(esCttInfoPara);
            FlowCtrlHis flowCtrlHisTemp=fromCttInfoToFlowCtrlHis(esCttInfoPara);
            List<Oper> operListTemp=flowCtrlHisService.selectOperByExample(ToolUtil.getOperatorManager().getOperatorId());
            for(Oper operTemp:operListTemp){
                if (ToolUtil.getOperatorManager().getOperatorId().equals(operTemp.getId())){
                    flowCtrlHisTemp.setTid(operTemp.getTid());
                }
            }
            if(strPowerTypePara.contains("Pass")){
                flowCtrlHisService.insertRecord(flowCtrlHisTemp, ESEnumOperType.OPER_Type0.getCode());
            }else if(strPowerTypePara.contains("Fail")){
                flowCtrlHisService.insertRecord(flowCtrlHisTemp, ESEnumOperType.OPER_Type1.getCode());
            }else if(strPowerTypePara.contains("Del")){
                flowCtrlHisService.insertRecord(flowCtrlHisTemp, ESEnumOperType.OPER_Type2.getCode());
            }else if(strPowerTypePara.equals("AttachAdd")){
                flowCtrlHisTemp.setRemark(flowCtrlHisTemp.getRemark()+"AttachAdd");
                flowCtrlHisService.insertRecord(flowCtrlHisTemp, ESEnumOperType.OPER_Type1.getCode());
            }else if(strPowerTypePara.equals("AttachRemove")){
                flowCtrlHisTemp.setRemark(flowCtrlHisTemp.getRemark()+"AttachRemove");
                flowCtrlHisService.insertRecord(flowCtrlHisTemp, ESEnumOperType.OPER_Type2.getCode());
            }
        } catch (Exception e) {
            throw e;
        }
    }
    public void updateRecord(CttInfoShow cttInfoShowPara){
        cttInfoShowPara.setModificationNum(
                ToolUtil.getIntIgnoreNull(cttInfoShowPara.getModificationNum())+1);
        cttInfoShowPara.setDeletedFlag("0");
        cttInfoShowPara.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        cttInfoShowPara.setLastUpdDate(ToolUtil.getStrLastUpdTime());
        esCttInfoMapper.updateByPrimaryKey(fromModelShowToModel(cttInfoShowPara));
    }
    public void updateRecordForOperRes(CttInfoShow cttInfoShowPara){
        EsCttInfo esCttInfoTemp = getCttInfoByPkId(cttInfoShowPara.getPkid());
        esCttInfoTemp.setName(cttInfoShowPara.getName());
        esCttInfoTemp.setModificationNum(
                ToolUtil.getIntIgnoreNull(cttInfoShowPara.getModificationNum())+1);
        esCttInfoTemp.setDeletedFlag("0");
        esCttInfoTemp.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        esCttInfoTemp.setLastUpdDate(ToolUtil.getStrLastUpdTime());
        esCttInfoMapper.updateByPrimaryKey(esCttInfoTemp);
    }

    public int deleteRecord(String strPkId){
        return esCttInfoMapper.deleteByPrimaryKey(strPkId);
    }

    public String getStrMaxCttId(String strCttType){
        return myCttInfoMapper.getStrMaxCttId(strCttType) ;
    }

    public EsCttInfo fromModelShowToModel(CttInfoShow cttInfoShowPara) {
        EsCttInfo esCttInfoTemp = new EsCttInfo();
        esCttInfoTemp.setPkid(cttInfoShowPara.getPkid());
        esCttInfoTemp.setCttType(cttInfoShowPara.getCttType());
        esCttInfoTemp.setParentPkid(cttInfoShowPara.getParentPkid());
        esCttInfoTemp.setId(cttInfoShowPara.getId());
        esCttInfoTemp.setName(cttInfoShowPara.getName());
        esCttInfoTemp.setCttStartDate(cttInfoShowPara.getCttStartDate());
        esCttInfoTemp.setCttEndDate(cttInfoShowPara.getCttEndDate());
        esCttInfoTemp.setSignDate(cttInfoShowPara.getSignDate());
        esCttInfoTemp.setSignPartA(cttInfoShowPara.getSignPartA());
        esCttInfoTemp.setSignPartB(cttInfoShowPara.getSignPartB());
        esCttInfoTemp.setNote(cttInfoShowPara.getNote());
        esCttInfoTemp.setAttachment(cttInfoShowPara.getAttachment());
        esCttInfoTemp.setDeletedFlag(cttInfoShowPara.getDeletedFlag());
        esCttInfoTemp.setEndFlag(cttInfoShowPara.getEndFlag());
        esCttInfoTemp.setCreatedBy(cttInfoShowPara.getCreatedBy());
        esCttInfoTemp.setCreatedDate(cttInfoShowPara.getCreatedDate());
        esCttInfoTemp.setLastUpdBy(cttInfoShowPara.getLastUpdBy());
        esCttInfoTemp.setLastUpdDate(cttInfoShowPara.getLastUpdDate());
        esCttInfoTemp.setModificationNum(cttInfoShowPara.getModificationNum());
        esCttInfoTemp.setType(cttInfoShowPara.getType());
        return esCttInfoTemp;
    }
    public CttInfoShow fromModelToModelShow(EsCttInfo cttInfoPara) {
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
        cttInfoShowTemp.setNote(cttInfoPara.getNote());
        cttInfoShowTemp.setAttachment(cttInfoPara.getAttachment());
        cttInfoShowTemp.setDeletedFlag(cttInfoPara.getDeletedFlag());
        cttInfoShowTemp.setEndFlag(cttInfoPara.getEndFlag());
        cttInfoShowTemp.setCreatedBy(cttInfoPara.getCreatedBy());
        cttInfoShowTemp.setCreatedDate(cttInfoPara.getCreatedDate());
        cttInfoShowTemp.setLastUpdBy(cttInfoPara.getLastUpdBy());
        cttInfoShowTemp.setLastUpdDate(cttInfoPara.getLastUpdDate());
        cttInfoShowTemp.setModificationNum(cttInfoPara.getModificationNum());
        cttInfoShowTemp.setType(cttInfoPara.getType());
        return cttInfoShowTemp;
    }
    //更新甲供材情况
    public int  updateByPKid(EsCttInfo esCttInfoPara){
      // return esCttInfoMapper.updateByPrimaryKey(fromModelShowToModel(cttInfoShowPara));
        return esCttInfoMapper.updateByPrimaryKey(esCttInfoPara);
    }

    /*public List<AllTabColumns>  getColumnNameByTableName(String strTableName){
        *//*return commonMapper.getColumnNameByTableName(strTableName) ;*//*
        AllTabColumnsExample  example = new AllTabColumnsExample();
        example.createCriteria().andTableNameEqualTo(strTableName);
        return allTabColumnsMapper.selectByExample(example) ;
    }*/
}
