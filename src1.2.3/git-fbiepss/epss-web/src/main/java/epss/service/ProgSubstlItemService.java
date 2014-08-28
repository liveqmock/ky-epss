package epss.service;

import skyline.util.ToolUtil;
import epss.repository.dao.EsItemStlSubcttEngPMapper;
import epss.repository.dao.not_mybatis.MyQueryMapper;
import epss.repository.model.EsItemStlSubcttEngP;
import epss.repository.model.EsItemStlSubcttEngPExample;
import epss.repository.model.model_show.ProgSubstlItemShow;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-5-20
 * Time: ÉÏÎç10:02
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ProgSubstlItemService {
    @Resource
    private EsItemStlSubcttEngPMapper esItemStlSubcttEngPMapper;
    @Resource
    private MyQueryMapper myQueryMapper;

    public EsItemStlSubcttEngP selectRecordsByDetailPrimaryKey(String strPkId){
        return esItemStlSubcttEngPMapper.selectByPrimaryKey(strPkId);
    }

    public List<EsItemStlSubcttEngP> selectRecordsByDetailExample(EsItemStlSubcttEngP esItemStlSubcttEngPPara){
        EsItemStlSubcttEngPExample example = new EsItemStlSubcttEngPExample();
        example.createCriteria().andPeriodNoEqualTo(esItemStlSubcttEngPPara.getPeriodNo());
        return esItemStlSubcttEngPMapper.selectByExample(example);
    }
    public List<EsItemStlSubcttEngP> selectRecordsByDetailExampleForAccount(EsItemStlSubcttEngP esItemStlSubcttEngPPara){
        EsItemStlSubcttEngPExample example = new EsItemStlSubcttEngPExample();
        example.createCriteria().andSubcttPkidEqualTo(esItemStlSubcttEngPPara.getSubcttPkid())
                .andPeriodNoEqualTo(esItemStlSubcttEngPPara.getPeriodNo())
                .andAddUpAmtIsNotNull()
                .andSubcttItemPkidLike("stl");
        example.setOrderByClause("ROW_NO ASC") ;
        return esItemStlSubcttEngPMapper.selectByExample(example);
    }
    public List<EsItemStlSubcttEngP> selectRecordsForAccount(String strSubcttPkidPara,String strPeriodNoPara){
        return myQueryMapper.selectRecordsForAccount(strSubcttPkidPara,strPeriodNoPara);
    }

    public void deleteRecordDetail(String strPkId){
        esItemStlSubcttEngPMapper.deleteByPrimaryKey(strPkId);
    }

    public void updateRecordDetail(EsItemStlSubcttEngP esItemStlSubcttEngPPara){
        esItemStlSubcttEngPPara.setRecVersion(
                ToolUtil.getIntIgnoreNull(esItemStlSubcttEngPPara.getRecVersion())+1);
        esItemStlSubcttEngPPara.setArchivedFlag("0");
        esItemStlSubcttEngPPara.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        esItemStlSubcttEngPPara.setLastUpdDate(ToolUtil.getStrLastUpdDate());
        esItemStlSubcttEngPMapper.updateByPrimaryKey(esItemStlSubcttEngPPara) ;
    }

    public void insertRecordDetail(ProgSubstlItemShow progSubstlItemShowPara){
        progSubstlItemShowPara.setEngPMng_CreatedBy(ToolUtil.getOperatorManager().getOperatorId());
        progSubstlItemShowPara.setEngPMng_CreatedByName(ToolUtil.getUserName(ToolUtil.getOperatorManager().getOperatorId()));
        progSubstlItemShowPara.setEngPMng_CreatedDate(ToolUtil.getStrLastUpdDate());
        progSubstlItemShowPara.setEngPMng_LastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        progSubstlItemShowPara.setEngPMng_LastUpdByName(ToolUtil.getUserName(ToolUtil.getOperatorManager().getOperatorId()));
        progSubstlItemShowPara.setEngPMng_LastUpdDate(ToolUtil.getStrLastUpdDate());
        esItemStlSubcttEngPMapper.insert(fromModelShowToModel(progSubstlItemShowPara));
    }

    private EsItemStlSubcttEngP fromModelShowToModel
            (ProgSubstlItemShow progSubstlItemShowPara){
        EsItemStlSubcttEngP esItemStlSubcttEngP=new EsItemStlSubcttEngP();
        esItemStlSubcttEngP.setSubstlType(progSubstlItemShowPara.getEngPMng_SubStlType());
        esItemStlSubcttEngP.setId(null);
        esItemStlSubcttEngP.setSubcttPkid(progSubstlItemShowPara.getSubctt_Pkid());
        esItemStlSubcttEngP.setSubcttName(progSubstlItemShowPara.getSubctt_Name());
        esItemStlSubcttEngP.setPeriodNo(progSubstlItemShowPara.getEngPMng_PeriodNo());
        esItemStlSubcttEngP.setSubcttItemPkid(progSubstlItemShowPara.getSubctt_ItemPkid());
        esItemStlSubcttEngP.setSubcttItemName(progSubstlItemShowPara.getSubctt_ItemName());
        esItemStlSubcttEngP.setUnit(progSubstlItemShowPara.getSubctt_Unit());
        esItemStlSubcttEngP.setUnitPrice(progSubstlItemShowPara.getSubctt_ContractUnitPrice());
        esItemStlSubcttEngP.setThisStageQty(progSubstlItemShowPara.getEngPMng_CurrentPeriodEQty());
        esItemStlSubcttEngP.setThisStageAmt(progSubstlItemShowPara.getEngPMng_CurrentPeriodAmt());
        esItemStlSubcttEngP.setAddUpQty(progSubstlItemShowPara.getEngPMng_BeginToCurrentPeriodEQty());
        esItemStlSubcttEngP.setAddUpAmt(progSubstlItemShowPara.getEngPMng_BeginToCurrentPeriodAmt());
        esItemStlSubcttEngP.setArchivedFlag("0");
        esItemStlSubcttEngP.setOriginFlag("0");
        esItemStlSubcttEngP.setCreatedBy(progSubstlItemShowPara.getEngPMng_CreatedBy());
        esItemStlSubcttEngP.setCreatedByName(progSubstlItemShowPara.getEngPMng_CreatedByName());
        esItemStlSubcttEngP.setCreatedDate(progSubstlItemShowPara.getEngPMng_CreatedDate());
        esItemStlSubcttEngP.setLastUpdBy(progSubstlItemShowPara.getEngPMng_LastUpdBy());
        esItemStlSubcttEngP.setLastUpdByName(progSubstlItemShowPara.getEngPMng_LastUpdByName());
        esItemStlSubcttEngP.setLastUpdDate(progSubstlItemShowPara.getEngPMng_LastUpdDate());
        esItemStlSubcttEngP.setRemark("");
        esItemStlSubcttEngP.setContractQuantity(progSubstlItemShowPara.getSubctt_ContractQuantity());
        esItemStlSubcttEngP.setContractAmount(progSubstlItemShowPara.getSubctt_ContractAmount());
        esItemStlSubcttEngP.setStrno(progSubstlItemShowPara.getSubctt_StrNo());
        esItemStlSubcttEngP.setRowNo(progSubstlItemShowPara.getEngPMng_RowNo());
        return esItemStlSubcttEngP;
    }

    public void deleteRecordByExample(String strSubcttInfoPkidPara,String strPeriodNoPara){
        EsItemStlSubcttEngPExample example = new EsItemStlSubcttEngPExample();
        example.createCriteria()
                .andSubcttPkidEqualTo(strSubcttInfoPkidPara)
                .andPeriodNoEqualTo(strPeriodNoPara);
        esItemStlSubcttEngPMapper.deleteByExample(example);
    }

    public ProgSubstlItemShow fromModelToShow(EsItemStlSubcttEngP esItemStlSubcttEngPPara) {
        ProgSubstlItemShow progSubstlItemShowTemp = new ProgSubstlItemShow();
        progSubstlItemShowTemp.setSubctt_ItemPkid(esItemStlSubcttEngPPara.getSubcttItemPkid());
        progSubstlItemShowTemp.setSubctt_StrNo(esItemStlSubcttEngPPara.getStrno());
        progSubstlItemShowTemp.setSubctt_ItemName(esItemStlSubcttEngPPara.getSubcttItemName());
        progSubstlItemShowTemp.setSubctt_Unit(esItemStlSubcttEngPPara.getUnit());
        progSubstlItemShowTemp.setSubctt_ContractUnitPrice(esItemStlSubcttEngPPara.getUnitPrice());
        progSubstlItemShowTemp.setSubctt_ContractQuantity(esItemStlSubcttEngPPara.getContractQuantity());
        progSubstlItemShowTemp.setSubctt_ContractAmount(esItemStlSubcttEngPPara.getContractAmount());
        progSubstlItemShowTemp.setEngPMng_CurrentPeriodEQty(esItemStlSubcttEngPPara.getThisStageQty());
        progSubstlItemShowTemp.setEngPMng_CurrentPeriodAmt(esItemStlSubcttEngPPara.getThisStageAmt());
        progSubstlItemShowTemp.setEngPMng_BeginToCurrentPeriodEQty(esItemStlSubcttEngPPara.getAddUpQty());
        progSubstlItemShowTemp.setEngPMng_BeginToCurrentPeriodAmt(esItemStlSubcttEngPPara.getAddUpAmt());
        progSubstlItemShowTemp.setSubctt_Note(esItemStlSubcttEngPPara.getRemark());
        progSubstlItemShowTemp.setEngPMng_SubStlType(esItemStlSubcttEngPPara.getSubstlType());
        return progSubstlItemShowTemp;
    }
}
