package epss.service;

import epss.repository.model.model_show.ProgEstMeaItemShow;
import epss.repository.model.model_show.ProgInfoShow;
import epss.common.utils.ToolUtil;
import epss.repository.dao.EsItemStlTkcttEngMeaMapper;
import epss.repository.model.EsItemStlTkcttEngMea;
import epss.repository.model.EsItemStlTkcttEngMeaExample;
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
public class ProgMeaItemService {
    @Resource
    private EsItemStlTkcttEngMeaMapper esItemStlTkcttEngMeaMapper;
    @Resource
    private EsFlowService esFlowService;
    @Resource
    private PlatformService platformService;
    /**
     * 判断记录是否已存在
     *
     * @param progEstMeaItemShowPara
     * @return
     */
    public List<EsItemStlTkcttEngMea> isExistInDb(ProgEstMeaItemShow progEstMeaItemShowPara) {
        EsItemStlTkcttEngMeaExample example = new EsItemStlTkcttEngMeaExample();
        example.createCriteria()
                .andTkcttPkidEqualTo(progEstMeaItemShowPara.getTkctt_BelongToPkid())
                .andPeriodNoEqualTo(progEstMeaItemShowPara.getEng_PeriodNo())
                .andTkcttItemPkidEqualTo(progEstMeaItemShowPara.getTkctt_Pkid());
        return esItemStlTkcttEngMeaMapper.selectByExample(example);
    }

    public List<EsItemStlTkcttEngMea> selectRecordsByKeyExample(EsItemStlTkcttEngMea esStlTkcttEngMeaPara){
        EsItemStlTkcttEngMeaExample example = new EsItemStlTkcttEngMeaExample();
        example.createCriteria()
                               .andTkcttPkidEqualTo(esStlTkcttEngMeaPara.getTkcttPkid())
                               .andTkcttItemPkidEqualTo(esStlTkcttEngMeaPara.getTkcttItemPkid())
                               .andPeriodNoEqualTo(esStlTkcttEngMeaPara.getPeriodNo());
        return esItemStlTkcttEngMeaMapper.selectByExample(example);
    }

    public List<EsItemStlTkcttEngMea> selectRecordsByPkidPeriodNoExample(EsItemStlTkcttEngMea esStlTkcttEngMeaPara){
        EsItemStlTkcttEngMeaExample example = new EsItemStlTkcttEngMeaExample();
        example.createCriteria()
                .andTkcttPkidEqualTo(esStlTkcttEngMeaPara.getTkcttPkid())
                .andPeriodNoEqualTo(esStlTkcttEngMeaPara.getPeriodNo());
        return esItemStlTkcttEngMeaMapper.selectByExample(example);
    }

    public int deleteRecord(String strPkId){
        return esItemStlTkcttEngMeaMapper.deleteByPrimaryKey(strPkId);
    }

    public void updateRecord(ProgEstMeaItemShow progEstMeaItemShowPara){
        EsItemStlTkcttEngMea esStlTkcttEngMeaTemp=fromModelShowToModel(progEstMeaItemShowPara);
        esStlTkcttEngMeaTemp.setModificationNum(
                ToolUtil.getIntIgnoreNull(esStlTkcttEngMeaTemp.getModificationNum())+1);
        esStlTkcttEngMeaTemp.setDeleteFlag("0");
        esStlTkcttEngMeaTemp.setLastUpdBy(platformService.getStrLastUpdBy());
        esStlTkcttEngMeaTemp.setLastUpdDate(platformService.getStrLastUpdDate());
        esItemStlTkcttEngMeaMapper.updateByPrimaryKey(esStlTkcttEngMeaTemp) ;
    }

    public void insertRecord(ProgEstMeaItemShow progEstMeaItemShowPara){
        EsItemStlTkcttEngMea esStlTkcttEngMeaTemp=fromModelShowToModel(progEstMeaItemShowPara);
        esStlTkcttEngMeaTemp.setCreatedBy(platformService.getStrLastUpdBy());
        esStlTkcttEngMeaTemp.setCreatedDate(platformService.getStrLastUpdDate());
        esStlTkcttEngMeaTemp.setDeleteFlag("0");
        esStlTkcttEngMeaTemp.setLastUpdBy(platformService.getStrLastUpdBy());
        esStlTkcttEngMeaTemp.setLastUpdDate(platformService.getStrLastUpdDate());
        esItemStlTkcttEngMeaMapper.insert(esStlTkcttEngMeaTemp) ;
    }


    private EsItemStlTkcttEngMea fromModelShowToModel
            (ProgEstMeaItemShow progEstMeaItemShowPara){
        EsItemStlTkcttEngMea esItemStlTkcttEngMeaTemp=new EsItemStlTkcttEngMea();
        esItemStlTkcttEngMeaTemp.setPkid(progEstMeaItemShowPara.getEng_Pkid());
        esItemStlTkcttEngMeaTemp.setPeriodNo(progEstMeaItemShowPara.getEng_PeriodNo());
        esItemStlTkcttEngMeaTemp.setTkcttPkid(progEstMeaItemShowPara.getEng_TkcttPkid());
        esItemStlTkcttEngMeaTemp.setTkcttItemPkid(progEstMeaItemShowPara.getEng_TkcttItemPkid());
        esItemStlTkcttEngMeaTemp.setBeginToCurrentPeriodQty(progEstMeaItemShowPara.getEng_BeginToCurrentPeriodEQty());
        esItemStlTkcttEngMeaTemp.setCurrentPeriodQty(progEstMeaItemShowPara.getEng_CurrentPeriodEQty());
        esItemStlTkcttEngMeaTemp.setDeleteFlag(progEstMeaItemShowPara.getEng_DeletedFlag());
        esItemStlTkcttEngMeaTemp.setCreatedBy(progEstMeaItemShowPara.getEng_CreatedBy());
        esItemStlTkcttEngMeaTemp.setCreatedDate(progEstMeaItemShowPara.getEng_CreatedDate());
        esItemStlTkcttEngMeaTemp.setLastUpdBy(progEstMeaItemShowPara.getEng_LastUpdBy());
        esItemStlTkcttEngMeaTemp.setLastUpdDate(progEstMeaItemShowPara.getEng_LastUpdDate());
        esItemStlTkcttEngMeaTemp.setModificationNum(progEstMeaItemShowPara.getEng_ModificationNum());
        return esItemStlTkcttEngMeaTemp;
    }

    public void setFromLastStageApproveDataToThisStageBeginData(ProgInfoShow progInfoShowPara){
        // 插入新数据之后,就得把上期批准了的数据作为今期数据的起始数据
        String strLatestApprovedPeriodNo= ToolUtil.getStrIgnoreNull(
                esFlowService.getLatestApprovedPeriodNo(progInfoShowPara.getStlType(), progInfoShowPara.getStlPkid()));
        if(strLatestApprovedPeriodNo!=null){
            EsItemStlTkcttEngMeaExample example = new EsItemStlTkcttEngMeaExample();
            example.createCriteria()
                    .andTkcttPkidEqualTo(progInfoShowPara.getStlPkid())
                    .andPeriodNoEqualTo(strLatestApprovedPeriodNo);
            List<EsItemStlTkcttEngMea> esItemStlTkcttEngMeaList =
                    esItemStlTkcttEngMeaMapper.selectByExample(example);
            for(EsItemStlTkcttEngMea itemUnit:esItemStlTkcttEngMeaList){
                itemUnit.setCurrentPeriodQty(null);
                itemUnit.setPeriodNo(progInfoShowPara.getPeriodNo());
                esItemStlTkcttEngMeaMapper.insert(itemUnit);
            }
        }
    }

    public int deleteItemsByInitStlTkcttEng(String strTkcttPkid,String strPeriodNo){
        EsItemStlTkcttEngMeaExample example = new EsItemStlTkcttEngMeaExample();
        example.createCriteria().
                andTkcttPkidEqualTo(strTkcttPkid).
                andPeriodNoEqualTo(strPeriodNo);
        return esItemStlTkcttEngMeaMapper.deleteByExample(example);
    }
}
