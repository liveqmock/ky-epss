package epss.service;

import epss.repository.dao.ProgStlItemSubStlmentMapper;
import epss.repository.model.ProgStlItemSubStlmentExample;
import epss.repository.model.model_show.ProgStlItemSubStlmentShow;
import skyline.util.ToolUtil;
import epss.repository.dao.not_mybatis.MyQueryMapper;
import epss.repository.model.ProgStlItemSubStlment;
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
    private ProgStlItemSubStlmentMapper progStlItemSubStlmentMapper;
    @Resource
    private MyQueryMapper myQueryMapper;

    public ProgStlItemSubStlment selectRecordsByDetailPrimaryKey(String strPkId){
        return progStlItemSubStlmentMapper.selectByPrimaryKey(strPkId);
    }

    public List<ProgStlItemSubStlment> selectRecordsByDetailExample(ProgStlItemSubStlment progStlItemSubStlmentPara){
        ProgStlItemSubStlmentExample example = new ProgStlItemSubStlmentExample();
        example.createCriteria().andPeriodNoEqualTo(progStlItemSubStlmentPara.getPeriodNo());
        return progStlItemSubStlmentMapper.selectByExample(example);
    }
    public List<ProgStlItemSubStlment> selectRecordsByDetailExampleForAccount(ProgStlItemSubStlment progStlItemSubStlmentPara){
        ProgStlItemSubStlmentExample example = new ProgStlItemSubStlmentExample();
        example.createCriteria().andSubcttPkidEqualTo(progStlItemSubStlmentPara.getSubcttPkid())
                .andPeriodNoEqualTo(progStlItemSubStlmentPara.getPeriodNo())
                .andAddUpAmtIsNotNull()
                .andSubcttItemPkidLike("stl");
        example.setOrderByClause("ROW_NO ASC") ;
        return progStlItemSubStlmentMapper.selectByExample(example);
    }
    public List<ProgStlItemSubStlment> selectRecordsForAccount(String strSubcttPkidPara,String strPeriodNoPara){
        return myQueryMapper.selectRecordsForAccount(strSubcttPkidPara,strPeriodNoPara);
    }

    public void deleteRecordDetail(String strPkId){
        progStlItemSubStlmentMapper.deleteByPrimaryKey(strPkId);
    }

    public void updateRecordDetail(ProgStlItemSubStlment progStlItemSubStlmentPara){
        progStlItemSubStlmentPara.setRecVersion(
                ToolUtil.getIntIgnoreNull(progStlItemSubStlmentPara.getRecVersion())+1);
        progStlItemSubStlmentPara.setArchivedFlag("0");
        progStlItemSubStlmentPara.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        progStlItemSubStlmentPara.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        progStlItemSubStlmentMapper.updateByPrimaryKey(progStlItemSubStlmentPara) ;
    }

    public void insertRecordDetail(ProgStlItemSubStlmentShow progStlItemSubStlmentShowPara){
        progStlItemSubStlmentShowPara.setEngPMng_CreatedBy(ToolUtil.getOperatorManager().getOperatorId());
        progStlItemSubStlmentShowPara.setEngPMng_CreatedByName(ToolUtil.getUserName(ToolUtil.getOperatorManager().getOperatorId()));
        progStlItemSubStlmentShowPara.setEngPMng_CreatedTime(ToolUtil.getStrLastUpdTime());
        progStlItemSubStlmentShowPara.setEngPMng_LastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        progStlItemSubStlmentShowPara.setEngPMng_LastUpdByName(ToolUtil.getUserName(ToolUtil.getOperatorManager().getOperatorId()));
        progStlItemSubStlmentShowPara.setEngPMng_LastUpdTime(ToolUtil.getStrLastUpdTime());
        progStlItemSubStlmentMapper.insert(fromModelShowToModel(progStlItemSubStlmentShowPara));
    }

    private ProgStlItemSubStlment fromModelShowToModel
            (ProgStlItemSubStlmentShow progStlItemSubStlmentShowPara){
        ProgStlItemSubStlment progStlItemSubStlment =new ProgStlItemSubStlment();
        progStlItemSubStlment.setSubstlType(progStlItemSubStlmentShowPara.getEngPMng_SubStlType());
        progStlItemSubStlment.setId(null);
        progStlItemSubStlment.setSubcttPkid(progStlItemSubStlmentShowPara.getSubctt_Pkid());
        progStlItemSubStlment.setSubcttName(progStlItemSubStlmentShowPara.getSubctt_Name());
        progStlItemSubStlment.setPeriodNo(progStlItemSubStlmentShowPara.getEngPMng_PeriodNo());
        progStlItemSubStlment.setSubcttItemPkid(progStlItemSubStlmentShowPara.getSubctt_ItemPkid());
        progStlItemSubStlment.setSubcttItemName(progStlItemSubStlmentShowPara.getSubctt_ItemName());
        progStlItemSubStlment.setUnit(progStlItemSubStlmentShowPara.getSubctt_Unit());
        progStlItemSubStlment.setUnitPrice(progStlItemSubStlmentShowPara.getSubctt_ContractUnitPrice());
        progStlItemSubStlment.setThisStageQty(progStlItemSubStlmentShowPara.getEngPMng_CurrentPeriodEQty());
        progStlItemSubStlment.setThisStageAmt(progStlItemSubStlmentShowPara.getEngPMng_CurrentPeriodAmt());
        progStlItemSubStlment.setAddUpQty(progStlItemSubStlmentShowPara.getEngPMng_BeginToCurrentPeriodEQty());
        progStlItemSubStlment.setAddUpAmt(progStlItemSubStlmentShowPara.getEngPMng_BeginToCurrentPeriodAmt());
        progStlItemSubStlment.setArchivedFlag("0");
        progStlItemSubStlment.setOriginFlag("0");
        progStlItemSubStlment.setCreatedBy(progStlItemSubStlmentShowPara.getEngPMng_CreatedBy());
        progStlItemSubStlment.setCreatedByName(progStlItemSubStlmentShowPara.getEngPMng_CreatedByName());
        progStlItemSubStlment.setCreatedTime(progStlItemSubStlmentShowPara.getEngPMng_CreatedTime());
        progStlItemSubStlment.setLastUpdBy(progStlItemSubStlmentShowPara.getEngPMng_LastUpdBy());
        progStlItemSubStlment.setLastUpdByName(progStlItemSubStlmentShowPara.getEngPMng_LastUpdByName());
        progStlItemSubStlment.setLastUpdTime(progStlItemSubStlmentShowPara.getEngPMng_LastUpdTime());
        progStlItemSubStlment.setRemark("");
        progStlItemSubStlment.setContractQuantity(progStlItemSubStlmentShowPara.getSubctt_ContractQuantity());
        progStlItemSubStlment.setContractAmount(progStlItemSubStlmentShowPara.getSubctt_ContractAmount());
        progStlItemSubStlment.setStrno(progStlItemSubStlmentShowPara.getSubctt_StrNo());
        progStlItemSubStlment.setRowNo(progStlItemSubStlmentShowPara.getEngPMng_RowNo());
        return progStlItemSubStlment;
    }

    public void deleteRecordByExample(String strCttInfoPkidPara,String strPeriodNoPara){
        ProgStlItemSubStlmentExample example = new ProgStlItemSubStlmentExample();
        example.createCriteria()
                .andSubcttPkidEqualTo(strCttInfoPkidPara)
                .andPeriodNoEqualTo(strPeriodNoPara);
        progStlItemSubStlmentMapper.deleteByExample(example);
    }

    public ProgStlItemSubStlmentShow fromModelToShow(ProgStlItemSubStlment progStlItemSubStlmentPara) {
        ProgStlItemSubStlmentShow progStlItemSubStlmentShowTemp = new ProgStlItemSubStlmentShow();
        progStlItemSubStlmentShowTemp.setSubctt_ItemPkid(progStlItemSubStlmentPara.getSubcttItemPkid());
        progStlItemSubStlmentShowTemp.setSubctt_StrNo(progStlItemSubStlmentPara.getStrno());
        progStlItemSubStlmentShowTemp.setSubctt_ItemName(progStlItemSubStlmentPara.getSubcttItemName());
        progStlItemSubStlmentShowTemp.setSubctt_Unit(progStlItemSubStlmentPara.getUnit());
        progStlItemSubStlmentShowTemp.setSubctt_ContractUnitPrice(progStlItemSubStlmentPara.getUnitPrice());
        progStlItemSubStlmentShowTemp.setSubctt_ContractQuantity(progStlItemSubStlmentPara.getContractQuantity());
        progStlItemSubStlmentShowTemp.setSubctt_ContractAmount(progStlItemSubStlmentPara.getContractAmount());
        progStlItemSubStlmentShowTemp.setEngPMng_CurrentPeriodEQty(progStlItemSubStlmentPara.getThisStageQty());
        progStlItemSubStlmentShowTemp.setEngPMng_CurrentPeriodAmt(progStlItemSubStlmentPara.getThisStageAmt());
        progStlItemSubStlmentShowTemp.setEngPMng_BeginToCurrentPeriodEQty(progStlItemSubStlmentPara.getAddUpQty());
        progStlItemSubStlmentShowTemp.setEngPMng_BeginToCurrentPeriodAmt(progStlItemSubStlmentPara.getAddUpAmt());
        progStlItemSubStlmentShowTemp.setSubctt_Remark(progStlItemSubStlmentPara.getRemark());
        progStlItemSubStlmentShowTemp.setEngPMng_SubStlType(progStlItemSubStlmentPara.getSubstlType());
        return progStlItemSubStlmentShowTemp;
    }
}
