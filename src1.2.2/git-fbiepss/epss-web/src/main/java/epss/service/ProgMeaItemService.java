package epss.service;

import epss.repository.model.model_show.ProgMeaItemShow;
import epss.repository.model.model_show.ProgInfoShow;
import skyline.util.ToolUtil;
import epss.repository.dao.EsItemStlTkcttEngMeaMapper;
import epss.repository.model.EsItemStlTkcttEngMea;
import epss.repository.model.EsItemStlTkcttEngMeaExample;
import org.springframework.stereotype.Service;
import skyline.service.PlatformService;

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
     * @param progMeaItemShowPara
     * @return
     */
    public List<EsItemStlTkcttEngMea> isExistInDb(ProgMeaItemShow progMeaItemShowPara) {
        EsItemStlTkcttEngMeaExample example = new EsItemStlTkcttEngMeaExample();
        example.createCriteria()
                .andTkcttPkidEqualTo(progMeaItemShowPara.getTkctt_BelongToPkid())
                .andPeriodNoEqualTo(progMeaItemShowPara.getEng_PeriodNo())
                .andTkcttItemPkidEqualTo(progMeaItemShowPara.getTkctt_Pkid());
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

    public void updateRecord(ProgMeaItemShow progMeaItemShowPara){
        EsItemStlTkcttEngMea esStlTkcttEngMeaTemp=fromModelShowToModel(progMeaItemShowPara);
        esStlTkcttEngMeaTemp.setModificationNum(
                ToolUtil.getIntIgnoreNull(esStlTkcttEngMeaTemp.getModificationNum())+1);
        esStlTkcttEngMeaTemp.setDeleteFlag("0");
        esStlTkcttEngMeaTemp.setLastUpdBy(platformService.getStrLastUpdBy());
        esStlTkcttEngMeaTemp.setLastUpdDate(platformService.getStrLastUpdDate());
        esItemStlTkcttEngMeaMapper.updateByPrimaryKey(esStlTkcttEngMeaTemp) ;
    }

    public void insertRecord(ProgMeaItemShow progMeaItemShowPara){
        EsItemStlTkcttEngMea esStlTkcttEngMeaTemp=fromModelShowToModel(progMeaItemShowPara);
        esStlTkcttEngMeaTemp.setCreatedBy(platformService.getStrLastUpdBy());
        esStlTkcttEngMeaTemp.setCreatedDate(platformService.getStrLastUpdDate());
        esStlTkcttEngMeaTemp.setDeleteFlag("0");
        esStlTkcttEngMeaTemp.setLastUpdBy(platformService.getStrLastUpdBy());
        esStlTkcttEngMeaTemp.setLastUpdDate(platformService.getStrLastUpdDate());
        esItemStlTkcttEngMeaMapper.insert(esStlTkcttEngMeaTemp) ;
    }


    private EsItemStlTkcttEngMea fromModelShowToModel
            (ProgMeaItemShow progMeaItemShowPara){
        EsItemStlTkcttEngMea esItemStlTkcttEngMeaTemp=new EsItemStlTkcttEngMea();
        esItemStlTkcttEngMeaTemp.setPkid(progMeaItemShowPara.getEng_Pkid());
        esItemStlTkcttEngMeaTemp.setPeriodNo(progMeaItemShowPara.getEng_PeriodNo());
        esItemStlTkcttEngMeaTemp.setTkcttPkid(progMeaItemShowPara.getEng_TkcttPkid());
        esItemStlTkcttEngMeaTemp.setTkcttItemPkid(progMeaItemShowPara.getEng_TkcttItemPkid());
        esItemStlTkcttEngMeaTemp.setBeginToCurrentPeriodQty(progMeaItemShowPara.getEng_BeginToCurrentPeriodEQty());
        esItemStlTkcttEngMeaTemp.setCurrentPeriodQty(progMeaItemShowPara.getEng_CurrentPeriodEQty());
        esItemStlTkcttEngMeaTemp.setDeleteFlag(progMeaItemShowPara.getEng_DeletedFlag());
        esItemStlTkcttEngMeaTemp.setCreatedBy(progMeaItemShowPara.getEng_CreatedBy());
        esItemStlTkcttEngMeaTemp.setCreatedDate(progMeaItemShowPara.getEng_CreatedDate());
        esItemStlTkcttEngMeaTemp.setLastUpdBy(progMeaItemShowPara.getEng_LastUpdBy());
        esItemStlTkcttEngMeaTemp.setLastUpdDate(progMeaItemShowPara.getEng_LastUpdDate());
        esItemStlTkcttEngMeaTemp.setModificationNum(progMeaItemShowPara.getEng_ModificationNum());
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
