package epss.service;

import epss.repository.dao.ProgStlItemTkEstMapper;
import epss.repository.model.ProgStlItemTkEstExample;
import epss.repository.model.model_show.ProgStlItemTkEstShow;
import epss.repository.model.model_show.ProgStlInfoShow;
import skyline.util.ToolUtil;
import epss.repository.model.ProgStlItemTkEst;
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
public class ProgEstItemService {
    @Resource
    private ProgStlItemTkEstMapper progStlItemTkEstMapper;
    @Resource
    private ProgStlInfoService progStlInfoService;

    public List<ProgStlItemTkEst> selectRecordsByExample(ProgStlItemTkEst progStlItemTkEstPara){
        ProgStlItemTkEstExample example = new ProgStlItemTkEstExample();
        example.createCriteria()
                .andTkcttPkidEqualTo(progStlItemTkEstPara.getTkcttPkid())
                .andTkcttItemPkidEqualTo(progStlItemTkEstPara.getTkcttItemPkid())
                .andPeriodNoEqualTo(progStlItemTkEstPara.getPeriodNo());
        return progStlItemTkEstMapper.selectByExample(example);
    }

    /**
     * 判断记录是否已存在
     *
     * @param progStlItemTkEstShowPara
     * @return
     */
    public List<ProgStlItemTkEst> isExistInDb(ProgStlItemTkEstShow progStlItemTkEstShowPara) {
        ProgStlItemTkEstExample example = new ProgStlItemTkEstExample();
        example.createCriteria()
                .andTkcttPkidEqualTo(progStlItemTkEstShowPara.getTkctt_BelongToPkid())
                .andTkcttItemPkidEqualTo(progStlItemTkEstShowPara.getTkctt_Pkid())
                .andPeriodNoEqualTo(progStlItemTkEstShowPara.getEng_PeriodNo());
        return progStlItemTkEstMapper.selectByExample(example);
    }

    public int deleteRecord(String strPkId){
        return progStlItemTkEstMapper.deleteByPrimaryKey(strPkId);
    }

    public void updateRecord(ProgStlItemTkEstShow progStlItemTkEstShowPara){
        ProgStlItemTkEst progStlItemTkEstTemp =fromModelShowToModel(progStlItemTkEstShowPara);
        progStlItemTkEstTemp.setModificationNum(
                ToolUtil.getIntIgnoreNull(progStlItemTkEstShowPara.getEng_ModificationNum())+1);
        progStlItemTkEstTemp.setArchivedFlag("0");
        progStlItemTkEstTemp.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        progStlItemTkEstTemp.setLastUpdDate(ToolUtil.getStrLastUpdDate());
        progStlItemTkEstMapper.updateByPrimaryKey(progStlItemTkEstTemp) ;
    }

    public void insertRecord(ProgStlItemTkEstShow progStlItemTkEstShowPara){
        ProgStlItemTkEst progStlItemTkEstTemp =fromModelShowToModel(progStlItemTkEstShowPara);
        progStlItemTkEstTemp.setCreatedBy(ToolUtil.getOperatorManager().getOperatorId());
        progStlItemTkEstTemp.setCreatedDate(ToolUtil.getStrLastUpdDate());
        progStlItemTkEstTemp.setArchivedFlag("0");
        progStlItemTkEstTemp.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        progStlItemTkEstTemp.setLastUpdDate(ToolUtil.getStrLastUpdDate());
        progStlItemTkEstMapper.insert(progStlItemTkEstTemp);
    }

    public void setFromLastStageApproveDataToThisStageBeginData(ProgStlInfoShow progStlInfoShowPara){
        // 插入新数据之后,就得把上期批准了的数据作为今期数据的起始数据
        String strLatestApprovedPeriodNo= ToolUtil.getStrIgnoreNull(
                progStlInfoService.getLatestApprovedPeriodNo(progStlInfoShowPara.getStlType(), progStlInfoShowPara.getStlPkid()));
        if(strLatestApprovedPeriodNo!=null){
            ProgStlItemTkEstExample example = new ProgStlItemTkEstExample();
            example.createCriteria()
                    .andTkcttPkidEqualTo(progStlInfoShowPara.getStlPkid())
                    .andPeriodNoEqualTo(strLatestApprovedPeriodNo);
            List<ProgStlItemTkEst> progStlItemTkEstList =
                    progStlItemTkEstMapper.selectByExample(example);
            for(ProgStlItemTkEst itemUnit: progStlItemTkEstList){
                itemUnit.setCurrentPeriodQty(null);
                itemUnit.setPeriodNo(progStlInfoShowPara.getPeriodNo());
                progStlItemTkEstMapper.insert(itemUnit);
            }
        }
    }

    private ProgStlItemTkEst fromModelShowToModel
            (ProgStlItemTkEstShow progStlItemTkEstShowPara){
        ProgStlItemTkEst progStlItemTkEstTemp =new ProgStlItemTkEst();
        progStlItemTkEstTemp.setPkid(progStlItemTkEstShowPara.getEng_Pkid());
        progStlItemTkEstTemp.setPeriodNo(progStlItemTkEstShowPara.getEng_PeriodNo());
        progStlItemTkEstTemp.setTkcttPkid(progStlItemTkEstShowPara.getEng_TkcttPkid());
        progStlItemTkEstTemp.setTkcttItemPkid(progStlItemTkEstShowPara.getEng_TkcttItemPkid());
        progStlItemTkEstTemp.setBeginToCurrentPeriodQty(progStlItemTkEstShowPara.getEng_BeginToCurrentPeriodEQty());
        progStlItemTkEstTemp.setCurrentPeriodQty(progStlItemTkEstShowPara.getEng_CurrentPeriodEQty());
        progStlItemTkEstTemp.setArchivedFlag(progStlItemTkEstShowPara.getEng_DeletedFlag());
        progStlItemTkEstTemp.setCreatedBy(progStlItemTkEstShowPara.getEng_CreatedBy());
        progStlItemTkEstTemp.setCreatedDate(progStlItemTkEstShowPara.getEng_CreatedDate());
        progStlItemTkEstTemp.setLastUpdBy(progStlItemTkEstShowPara.getEng_LastUpdBy());
        progStlItemTkEstTemp.setLastUpdDate(progStlItemTkEstShowPara.getEng_LastUpdDate());
        progStlItemTkEstTemp.setModificationNum(progStlItemTkEstShowPara.getEng_ModificationNum());
        return progStlItemTkEstTemp;
    }

    public int deleteItemsByInitStlTkcttEng(String strTkcttPkid,String strPeriodNo){
        ProgStlItemTkEstExample example = new ProgStlItemTkEstExample();
        example.createCriteria().
                andTkcttPkidEqualTo(strTkcttPkid).
                andPeriodNoEqualTo(strPeriodNo);
        return progStlItemTkEstMapper.deleteByExample(example);
    }
}
