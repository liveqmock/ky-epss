package epss.service;

import epss.repository.dao.ProgStlItemTkMeaMapper;
import epss.repository.model.ProgStlItemTkMea;
import epss.repository.model.ProgStlItemTkMeaExample;
import epss.repository.model.model_show.ProgStlItemTkMeaShow;
import epss.repository.model.model_show.ProgStlInfoShow;
import skyline.util.ToolUtil;
import org.springframework.stereotype.Service;
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
    private ProgStlItemTkMeaMapper progStlItemTkMeaMapper;
    @Resource
    private ProgStlInfoService progStlInfoService;
    /**
     * 判断记录是否已存在
     *
     * @param progStlItemTkMeaShowPara
     * @return
     */
    public List<ProgStlItemTkMea> isExistInDb(ProgStlItemTkMeaShow progStlItemTkMeaShowPara) {
        ProgStlItemTkMeaExample example = new ProgStlItemTkMeaExample();
        example.createCriteria()
                .andTkcttPkidEqualTo(progStlItemTkMeaShowPara.getTkctt_BelongToPkid())
                .andPeriodNoEqualTo(progStlItemTkMeaShowPara.getEng_PeriodNo())
                .andTkcttItemPkidEqualTo(progStlItemTkMeaShowPara.getTkctt_Pkid());
        return progStlItemTkMeaMapper.selectByExample(example);
    }

    public List<ProgStlItemTkMea> selectRecordsByKeyExample(ProgStlItemTkMea esStlTkcttEngMeaPara){
        ProgStlItemTkMeaExample example = new ProgStlItemTkMeaExample();
        example.createCriteria()
                               .andTkcttPkidEqualTo(esStlTkcttEngMeaPara.getTkcttPkid())
                               .andTkcttItemPkidEqualTo(esStlTkcttEngMeaPara.getTkcttItemPkid())
                               .andPeriodNoEqualTo(esStlTkcttEngMeaPara.getPeriodNo());
        return progStlItemTkMeaMapper.selectByExample(example);
    }

    public List<ProgStlItemTkMea> selectRecordsByPkidPeriodNoExample(ProgStlItemTkMea esStlTkcttEngMeaPara){
        ProgStlItemTkMeaExample example = new ProgStlItemTkMeaExample();
        example.createCriteria()
                .andTkcttPkidEqualTo(esStlTkcttEngMeaPara.getTkcttPkid())
                .andPeriodNoEqualTo(esStlTkcttEngMeaPara.getPeriodNo());
        return progStlItemTkMeaMapper.selectByExample(example);
    }

    public int deleteRecord(String strPkId){
        return progStlItemTkMeaMapper.deleteByPrimaryKey(strPkId);
    }

    public void updateRecord(ProgStlItemTkMeaShow progStlItemTkMeaShowPara){
        ProgStlItemTkMea esStlTkcttEngMeaTemp=fromModelShowToModel(progStlItemTkMeaShowPara);
        esStlTkcttEngMeaTemp.setRecversion(
                ToolUtil.getIntIgnoreNull(esStlTkcttEngMeaTemp.getRecversion())+1);
        esStlTkcttEngMeaTemp.setArchivedFlag("0");
        esStlTkcttEngMeaTemp.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        esStlTkcttEngMeaTemp.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        progStlItemTkMeaMapper.updateByPrimaryKey(esStlTkcttEngMeaTemp) ;
    }

    public void insertRecord(ProgStlItemTkMeaShow progStlItemTkMeaShowPara){
        ProgStlItemTkMea esStlTkcttEngMeaTemp=fromModelShowToModel(progStlItemTkMeaShowPara);
        esStlTkcttEngMeaTemp.setCreatedBy(ToolUtil.getOperatorManager().getOperatorId());
        esStlTkcttEngMeaTemp.setCreatedTime(ToolUtil.getStrLastUpdTime());
        esStlTkcttEngMeaTemp.setArchivedFlag("0");
        esStlTkcttEngMeaTemp.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        esStlTkcttEngMeaTemp.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        progStlItemTkMeaMapper.insert(esStlTkcttEngMeaTemp) ;
    }


    private ProgStlItemTkMea fromModelShowToModel
            (ProgStlItemTkMeaShow progStlItemTkMeaShowPara){
        ProgStlItemTkMea progStlItemTkMeaTemp =new ProgStlItemTkMea();
        progStlItemTkMeaTemp.setPkid(progStlItemTkMeaShowPara.getEng_Pkid());
        progStlItemTkMeaTemp.setPeriodNo(progStlItemTkMeaShowPara.getEng_PeriodNo());
        progStlItemTkMeaTemp.setTkcttPkid(progStlItemTkMeaShowPara.getEng_TkcttPkid());
        progStlItemTkMeaTemp.setTkcttItemPkid(progStlItemTkMeaShowPara.getEng_TkcttItemPkid());
        progStlItemTkMeaTemp.setBeginToCurrentPeriodQty(progStlItemTkMeaShowPara.getEng_BeginToCurrentPeriodEQty());
        progStlItemTkMeaTemp.setCurrentPeriodQty(progStlItemTkMeaShowPara.getEng_CurrentPeriodEQty());
        progStlItemTkMeaTemp.setArchivedFlag(progStlItemTkMeaShowPara.getEng_ArchivedFlag());
        progStlItemTkMeaTemp.setCreatedBy(progStlItemTkMeaShowPara.getEng_CreatedBy());
        progStlItemTkMeaTemp.setCreatedTime(progStlItemTkMeaShowPara.getEng_CreatedTime());
        progStlItemTkMeaTemp.setLastUpdBy(progStlItemTkMeaShowPara.getEng_LastUpdBy());
        progStlItemTkMeaTemp.setLastUpdTime(progStlItemTkMeaShowPara.getEng_LastUpdTime());
        progStlItemTkMeaTemp.setRecversion(progStlItemTkMeaShowPara.getEng_Recversion());
        return progStlItemTkMeaTemp;
    }

    public void setFromLastStageApproveDataToThisStageBeginData(ProgStlInfoShow progStlInfoShowPara){
        // 插入新数据之后,就得把上期批准了的数据作为今期数据的起始数据
        String strLatestApprovedPeriodNo= ToolUtil.getStrIgnoreNull(
                progStlInfoService.getLatestApprovedPeriodNo(progStlInfoShowPara.getStlType(), progStlInfoShowPara.getStlPkid()));
        if(strLatestApprovedPeriodNo!=null){
            ProgStlItemTkMeaExample example = new ProgStlItemTkMeaExample();
            example.createCriteria()
                    .andTkcttPkidEqualTo(progStlInfoShowPara.getStlPkid())
                    .andPeriodNoEqualTo(strLatestApprovedPeriodNo);
            List<ProgStlItemTkMea> progStlItemTkMeaList =
                    progStlItemTkMeaMapper.selectByExample(example);
            for(ProgStlItemTkMea itemUnit: progStlItemTkMeaList){
                itemUnit.setCurrentPeriodQty(null);
                itemUnit.setPeriodNo(progStlInfoShowPara.getPeriodNo());
                progStlItemTkMeaMapper.insert(itemUnit);
            }
        }
    }

    public int deleteItemsByInitStlTkcttEng(String strTkcttPkid,String strPeriodNo){
        ProgStlItemTkMeaExample example = new ProgStlItemTkMeaExample();
        example.createCriteria().
                andTkcttPkidEqualTo(strTkcttPkid).
                andPeriodNoEqualTo(strPeriodNo);
        return progStlItemTkMeaMapper.deleteByExample(example);
    }
}
