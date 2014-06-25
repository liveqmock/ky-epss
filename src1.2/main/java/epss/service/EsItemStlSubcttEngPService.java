package epss.service;

import epss.common.utils.ToolUtil;
import epss.repository.dao.EsItemStlSubcttEngPMapper;
import epss.repository.model.EsItemStlSubcttEngM;
import epss.repository.model.EsItemStlSubcttEngP;
import epss.repository.model.EsItemStlSubcttEngPExample;
import epss.repository.model.model_show.ProgMatQtyItemShow;
import epss.repository.model.model_show.ProgSubstlItemShow;
import epss.service.common.EsCommonService;
import org.springframework.stereotype.Service;
import platform.service.PlatformService;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-5-20
 * Time: ����10:02
 * To change this template use File | Settings | File Templates.
 */
@Service
public class EsItemStlSubcttEngPService {
    @Resource
    private EsItemStlSubcttEngPMapper esItemStlSubcttEngPMapper;
    @Resource
    private PlatformService platformService;
    @Resource
    private EsCommonService esCommonService;

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
                .andPeriodNoEqualTo(esItemStlSubcttEngPPara.getPeriodNo());
        return esItemStlSubcttEngPMapper.selectByExample(example);
    }

    public void deleteRecordDetail(String strPkId){
        esItemStlSubcttEngPMapper.deleteByPrimaryKey(strPkId);
    }

    public void updateRecordDetail(EsItemStlSubcttEngP esItemStlSubcttEngPPara){
        esItemStlSubcttEngPPara.setRecVersion(
                ToolUtil.getIntIgnoreNull(esItemStlSubcttEngPPara.getRecVersion())+1);
        esItemStlSubcttEngPPara.setArchivedFlag("0");
        esItemStlSubcttEngPPara.setLastUpdBy(platformService.getStrLastUpdBy());
        esItemStlSubcttEngPPara.setLastUpdDate(platformService.getStrLastUpdDate());
        esItemStlSubcttEngPMapper.updateByPrimaryKey(esItemStlSubcttEngPPara) ;
    }

    public void insertRecordDetail(ProgSubstlItemShow progSubstlItemShowPara){
        progSubstlItemShowPara.setEngPMng_CreatedBy(platformService.getStrLastUpdBy());
        progSubstlItemShowPara.setEngPMng_CreatedByName(esCommonService.getOperNameByOperId(platformService.getStrLastUpdBy()));
        progSubstlItemShowPara.setEngPMng_CreatedDate(platformService.getStrLastUpdDate());
        progSubstlItemShowPara.setEngPMng_LastUpdBy(platformService.getStrLastUpdBy());
        progSubstlItemShowPara.setEngPMng_LastUpdByName(esCommonService.getOperNameByOperId(platformService.getStrLastUpdBy()));
        progSubstlItemShowPara.setEngPMng_LastUpdDate(platformService.getStrLastUpdDate());
        esItemStlSubcttEngPMapper.insert(fromConstructToModel(progSubstlItemShowPara));
    }

    private EsItemStlSubcttEngP fromConstructToModel
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
        return esItemStlSubcttEngP;
    }

    public void deleteRecordByExample(String strSubcttInfoPkidPara,String strPeriodNoPara){
        EsItemStlSubcttEngPExample example = new EsItemStlSubcttEngPExample();
        example.createCriteria()
                .andSubcttPkidEqualTo(strSubcttInfoPkidPara)
                .andPeriodNoEqualTo(strPeriodNoPara);
        esItemStlSubcttEngPMapper.deleteByExample(example);
    }
}
