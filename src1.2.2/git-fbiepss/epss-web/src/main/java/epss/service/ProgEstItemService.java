package epss.service;

import epss.repository.model.model_show.ProgEstItemShow;
import epss.repository.model.model_show.ProgInfoShow;
import skyline.util.ToolUtil;
import epss.repository.dao.EsItemStlTkcttEngStaMapper;
import epss.repository.model.EsItemStlTkcttEngSta;
import epss.repository.model.EsItemStlTkcttEngStaExample;
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
     * @param progEstItemShowPara
     * @return
     */
    public List<EsItemStlTkcttEngSta> isExistInDb(ProgEstItemShow progEstItemShowPara) {
        EsItemStlTkcttEngStaExample example = new EsItemStlTkcttEngStaExample();
        example.createCriteria()
                .andTkcttPkidEqualTo(progEstItemShowPara.getTkctt_BelongToPkid())
                .andTkcttItemPkidEqualTo(progEstItemShowPara.getTkctt_Pkid())
                .andPeriodNoEqualTo(progEstItemShowPara.getEng_PeriodNo());
        return esItemStlTkcttEngStaMapper.selectByExample(example);
    }

    public int deleteRecord(String strPkId){
        return esItemStlTkcttEngStaMapper.deleteByPrimaryKey(strPkId);
    }

    public void updateRecord(ProgEstItemShow progEstItemShowPara){
        EsItemStlTkcttEngSta esItemStlTkcttEngStaTemp=fromModelShowToModel(progEstItemShowPara);
        esItemStlTkcttEngStaTemp.setModificationNum(
                ToolUtil.getIntIgnoreNull(progEstItemShowPara.getEng_ModificationNum())+1);
        esItemStlTkcttEngStaTemp.setDeleteFlag("0");
        esItemStlTkcttEngStaTemp.setLastUpdBy(platformService.getStrLastUpdBy());
        esItemStlTkcttEngStaTemp.setLastUpdDate(platformService.getStrLastUpdDate());
        esItemStlTkcttEngStaMapper.updateByPrimaryKey(esItemStlTkcttEngStaTemp) ;
    }

    public void insertRecord(ProgEstItemShow progEstItemShowPara){
        EsItemStlTkcttEngSta esItemStlTkcttEngStaTemp=fromModelShowToModel(progEstItemShowPara);
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
            (ProgEstItemShow progEstItemShowPara){
        EsItemStlTkcttEngSta esItemStlTkcttEngStaTemp=new EsItemStlTkcttEngSta();
        esItemStlTkcttEngStaTemp.setPkid(progEstItemShowPara.getEng_Pkid());
        esItemStlTkcttEngStaTemp.setPeriodNo(progEstItemShowPara.getEng_PeriodNo());
        esItemStlTkcttEngStaTemp.setTkcttPkid(progEstItemShowPara.getEng_TkcttPkid());
        esItemStlTkcttEngStaTemp.setTkcttItemPkid(progEstItemShowPara.getEng_TkcttItemPkid());
        esItemStlTkcttEngStaTemp.setBeginToCurrentPeriodQty(progEstItemShowPara.getEng_BeginToCurrentPeriodEQty());
        esItemStlTkcttEngStaTemp.setCurrentPeriodQty(progEstItemShowPara.getEng_CurrentPeriodEQty());
        esItemStlTkcttEngStaTemp.setDeleteFlag(progEstItemShowPara.getEng_DeletedFlag());
        esItemStlTkcttEngStaTemp.setCreatedBy(progEstItemShowPara.getEng_CreatedBy());
        esItemStlTkcttEngStaTemp.setCreatedDate(progEstItemShowPara.getEng_CreatedDate());
        esItemStlTkcttEngStaTemp.setLastUpdBy(progEstItemShowPara.getEng_LastUpdBy());
        esItemStlTkcttEngStaTemp.setLastUpdDate(progEstItemShowPara.getEng_LastUpdDate());
        esItemStlTkcttEngStaTemp.setModificationNum(progEstItemShowPara.getEng_ModificationNum());
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
