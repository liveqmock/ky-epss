package epss.service;

import epss.repository.model.model_show.ProgEstStaItemShow;
import epss.repository.model.model_show.ProgInfoShow;
import epss.common.utils.ToolUtil;
import epss.repository.dao.EsItemStlTkcttEngStaMapper;
import epss.repository.model.EsItemStlTkcttEngSta;
import epss.repository.model.EsItemStlTkcttEngStaExample;
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
public class ProgEstItemService {
    @Resource
    private EsItemStlTkcttEngStaMapper esItemStlTkcttEngStaMapper;
    @Resource
    private EsFlowService esFlowService;
    @Resource
    private PlatformService platformService;

    public List<EsItemStlTkcttEngSta> selectRecordsByExample(EsItemStlTkcttEngSta EsItemStlTkcttEngStaPara){
        EsItemStlTkcttEngStaExample example = new EsItemStlTkcttEngStaExample();
        example.createCriteria()
                .andTkcttPkidEqualTo(EsItemStlTkcttEngStaPara.getTkcttPkid())
                .andTkcttItemPkidEqualTo(EsItemStlTkcttEngStaPara.getTkcttItemPkid())
                .andPeriodNoEqualTo(EsItemStlTkcttEngStaPara.getPeriodNo());
        return esItemStlTkcttEngStaMapper.selectByExample(example);
    }

    /**
     * 判断记录是否已存在
     *
     * @param progEstStaItemShowPara
     * @return
     */
    public List<EsItemStlTkcttEngSta> isExistInDb(ProgEstStaItemShow progEstStaItemShowPara) {
        EsItemStlTkcttEngStaExample example = new EsItemStlTkcttEngStaExample();
        example.createCriteria()
                .andTkcttPkidEqualTo(progEstStaItemShowPara.getTkctt_BelongToPkid())
                .andTkcttItemPkidEqualTo(progEstStaItemShowPara.getTkctt_Pkid())
                .andPeriodNoEqualTo(progEstStaItemShowPara.getEng_PeriodNo());
        return esItemStlTkcttEngStaMapper.selectByExample(example);
    }

    public int deleteRecord(String strPkId){
        return esItemStlTkcttEngStaMapper.deleteByPrimaryKey(strPkId);
    }

    public void updateRecord(ProgEstStaItemShow progEstStaItemShowPara){
        EsItemStlTkcttEngSta esItemStlTkcttEngStaTemp=fromModelShowToModel(progEstStaItemShowPara);
        esItemStlTkcttEngStaTemp.setModificationNum(
                ToolUtil.getIntIgnoreNull(progEstStaItemShowPara.getEng_ModificationNum())+1);
        esItemStlTkcttEngStaTemp.setDeleteFlag("0");
        esItemStlTkcttEngStaTemp.setLastUpdBy(platformService.getStrLastUpdBy());
        esItemStlTkcttEngStaTemp.setLastUpdDate(platformService.getStrLastUpdDate());
        esItemStlTkcttEngStaMapper.updateByPrimaryKey(esItemStlTkcttEngStaTemp) ;
    }

    public void insertRecord(ProgEstStaItemShow progEstStaItemShowPara){
        EsItemStlTkcttEngSta esItemStlTkcttEngStaTemp=fromModelShowToModel(progEstStaItemShowPara);
        esItemStlTkcttEngStaTemp.setCreatedBy(platformService.getStrLastUpdBy());
        esItemStlTkcttEngStaTemp.setCreatedDate(platformService.getStrLastUpdDate());
        esItemStlTkcttEngStaTemp.setDeleteFlag("0");
        esItemStlTkcttEngStaTemp.setLastUpdBy(platformService.getStrLastUpdBy());
        esItemStlTkcttEngStaTemp.setLastUpdDate(platformService.getStrLastUpdDate());
        esItemStlTkcttEngStaMapper.insert(esItemStlTkcttEngStaTemp);
    }

    public void setFromLastStageApproveDataToThisStageBeginData(ProgInfoShow progInfoShowPara){
        // 插入新数据之后,就得把上期批准了的数据作为今期数据的起始数据
        String strLatestApprovedPeriodNo= ToolUtil.getStrIgnoreNull(
                esFlowService.getLatestApprovedPeriodNo(progInfoShowPara.getStlType(), progInfoShowPara.getStlPkid()));
        if(strLatestApprovedPeriodNo!=null){
            EsItemStlTkcttEngStaExample example = new EsItemStlTkcttEngStaExample();
            example.createCriteria()
                    .andTkcttPkidEqualTo(progInfoShowPara.getStlPkid())
                    .andPeriodNoEqualTo(strLatestApprovedPeriodNo);
            List<EsItemStlTkcttEngSta> esItemStlTkcttEngStaList =
                    esItemStlTkcttEngStaMapper.selectByExample(example);
            for(EsItemStlTkcttEngSta itemUnit:esItemStlTkcttEngStaList){
                itemUnit.setCurrentPeriodQty(null);
                itemUnit.setPeriodNo(progInfoShowPara.getPeriodNo());
                esItemStlTkcttEngStaMapper.insert(itemUnit);
            }
        }
    }

    private EsItemStlTkcttEngSta fromModelShowToModel
            (ProgEstStaItemShow progEstStaItemShowPara){
        EsItemStlTkcttEngSta esItemStlTkcttEngStaTemp=new EsItemStlTkcttEngSta();
        esItemStlTkcttEngStaTemp.setPkid(progEstStaItemShowPara.getEng_Pkid());
        esItemStlTkcttEngStaTemp.setPeriodNo(progEstStaItemShowPara.getEng_PeriodNo());
        esItemStlTkcttEngStaTemp.setTkcttPkid(progEstStaItemShowPara.getEng_TkcttPkid());
        esItemStlTkcttEngStaTemp.setTkcttItemPkid(progEstStaItemShowPara.getEng_TkcttItemPkid());
        esItemStlTkcttEngStaTemp.setBeginToCurrentPeriodQty(progEstStaItemShowPara.getEng_BeginToCurrentPeriodEQty());
        esItemStlTkcttEngStaTemp.setCurrentPeriodQty(progEstStaItemShowPara.getEng_CurrentPeriodEQty());
        esItemStlTkcttEngStaTemp.setDeleteFlag(progEstStaItemShowPara.getEng_DeletedFlag());
        esItemStlTkcttEngStaTemp.setCreatedBy(progEstStaItemShowPara.getEng_CreatedBy());
        esItemStlTkcttEngStaTemp.setCreatedDate(progEstStaItemShowPara.getEng_CreatedDate());
        esItemStlTkcttEngStaTemp.setLastUpdBy(progEstStaItemShowPara.getEng_LastUpdBy());
        esItemStlTkcttEngStaTemp.setLastUpdDate(progEstStaItemShowPara.getEng_LastUpdDate());
        esItemStlTkcttEngStaTemp.setModificationNum(progEstStaItemShowPara.getEng_ModificationNum());
        return esItemStlTkcttEngStaTemp;
    }

    public int deleteItemsByInitStlTkcttEng(String strTkcttPkid,String strPeriodNo){
        EsItemStlTkcttEngStaExample example = new EsItemStlTkcttEngStaExample();
        example.createCriteria().
                andTkcttPkidEqualTo(strTkcttPkid).
                andPeriodNoEqualTo(strPeriodNo);
        return esItemStlTkcttEngStaMapper.deleteByExample(example);
    }
}
