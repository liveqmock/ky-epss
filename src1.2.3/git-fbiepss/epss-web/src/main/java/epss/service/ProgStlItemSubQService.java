package epss.service;

import epss.repository.dao.ProgStlItemSubQMapper;
import epss.repository.dao.not_mybatis.MyProgWorkqtyItemMapper;
import epss.repository.model.ProgStlItemSubQ;
import epss.repository.model.ProgStlItemSubQExample;
import epss.repository.model.model_show.ProgStlInfoShow;
import skyline.util.ToolUtil;
import epss.repository.model.model_show.ProgStlItemSubQShow;
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
public class ProgStlItemSubQService {
    @Resource
    private ProgStlItemSubQMapper progStlItemSubQMapper;
    @Resource
    private MyProgWorkqtyItemMapper myProgWorkqtyItemMapper;
    @Resource
    private ProgStlInfoService progStlInfoService;

    public List<ProgStlItemSubQ> selectRecordsByExample(ProgStlItemSubQ progStlItemSubQPara){
        ProgStlItemSubQExample example = new ProgStlItemSubQExample();
        example.createCriteria()
                .andSubcttPkidEqualTo(progStlItemSubQPara.getSubcttPkid())
                .andSubcttItemPkidEqualTo(progStlItemSubQPara.getSubcttItemPkid())
                .andPeriodNoEqualTo(progStlItemSubQPara.getPeriodNo());
        return progStlItemSubQMapper.selectByExample(example);
    }

    /**
     * 判断记录是否已存在
     *
     * @param progStlItemSubQShowPara
     * @return
     */
    public List<ProgStlItemSubQ> isExistInDb(ProgStlItemSubQShow progStlItemSubQShowPara) {
        ProgStlItemSubQExample example = new ProgStlItemSubQExample();
        example.createCriteria()
                .andSubcttItemPkidEqualTo(progStlItemSubQShowPara.getSubctt_Pkid())
                .andPeriodNoEqualTo(progStlItemSubQShowPara.getEngQMng_PeriodNo())
                .andSubcttPkidEqualTo(progStlItemSubQShowPara.getSubctt_BelongToPkid());
        return progStlItemSubQMapper.selectByExample(example);
    }

    public List<ProgStlItemSubQ> getProgWorkqtyItemListByPeriod(
            String strSubcttPkid,String strPeriodNoBegin,String strPeriodNoEnd){
        return myProgWorkqtyItemMapper.getProgWorkqtyItemListByPeriod(strSubcttPkid, strPeriodNoBegin, strPeriodNoEnd);
    }

    public List<String> getProgWorkqtyItemPeriodsByPeriod(
            String strSubcttPkid,
            String strPeriodNoBegin,
            String strPeriodNoEnd){
        return myProgWorkqtyItemMapper.getProgWorkqtyItemPeriodsByPeriod(strSubcttPkid, strPeriodNoBegin, strPeriodNoEnd);
    }

    private ProgStlItemSubQ fromModelShowToModel
            (ProgStlItemSubQShow progStlItemSubQShowPara){
        ProgStlItemSubQ progStlItemSubQTemp =new ProgStlItemSubQ();
        progStlItemSubQTemp.setPkid(progStlItemSubQShowPara.getEngQMng_Pkid());
        progStlItemSubQTemp.setPeriodNo(progStlItemSubQShowPara.getEngQMng_PeriodNo());
        progStlItemSubQTemp.setSubcttPkid(progStlItemSubQShowPara.getEngQMng_SubcttPkid());
        progStlItemSubQTemp.setSubcttItemPkid(progStlItemSubQShowPara.getEngQMng_SubcttItemPkid());
        progStlItemSubQTemp.setBeginToCurrentPeriodEQty(progStlItemSubQShowPara.getEngQMng_BeginToCurrentPeriodEQty());
        progStlItemSubQTemp.setCurrentPeriodEQty(progStlItemSubQShowPara.getEngQMng_CurrentPeriodEQty());
        progStlItemSubQTemp.setArchivedFlag(progStlItemSubQShowPara.getEngQMng_ArchivedFlag());
        progStlItemSubQTemp.setCreatedBy(progStlItemSubQShowPara.getEngQMng_CreatedBy());
        progStlItemSubQTemp.setCreatedTime(progStlItemSubQShowPara.getEngQMng_CreatedTime());
        progStlItemSubQTemp.setLastUpdBy(progStlItemSubQShowPara.getEngQMng_LastUpdBy());
        progStlItemSubQTemp.setLastUpdTime(progStlItemSubQShowPara.getEngQMng_LastUpdTime());
        progStlItemSubQTemp.setRecVersion(progStlItemSubQShowPara.getEngQMng_RecVersion());
        return progStlItemSubQTemp;
    }

    public int deleteRecord(String strPkId){
        return progStlItemSubQMapper.deleteByPrimaryKey(strPkId);
    }

    public void updateRecord(ProgStlItemSubQShow progStlItemSubQShowPara){
        ProgStlItemSubQ progStlItemSubQTemp =fromModelShowToModel(progStlItemSubQShowPara);
        progStlItemSubQTemp.setRecVersion(
                ToolUtil.getIntIgnoreNull(progStlItemSubQTemp.getRecVersion())+1);
        progStlItemSubQTemp.setArchivedFlag("0");
        progStlItemSubQTemp.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        progStlItemSubQTemp.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        progStlItemSubQMapper.updateByPrimaryKey(progStlItemSubQTemp) ;
    }

    public void insertRecord(ProgStlItemSubQShow progStlItemSubQShowPara){
        ProgStlItemSubQ progStlItemSubQTemp =fromModelShowToModel(progStlItemSubQShowPara);
        progStlItemSubQTemp.setCreatedBy(ToolUtil.getOperatorManager().getOperatorId());
        progStlItemSubQTemp.setCreatedTime(ToolUtil.getStrLastUpdTime());
        progStlItemSubQTemp.setArchivedFlag("0");
        progStlItemSubQTemp.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        progStlItemSubQTemp.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        progStlItemSubQMapper.insert(progStlItemSubQTemp);
    }

    public void setFromLastStageAddUpToDataToThisStageBeginData(ProgStlInfoShow progStlInfoShowPara){
        // 插入新数据之后,就得把上期批准了的数据作为今期数据的起始数据
        String strLatestApprovedPeriodNo= ToolUtil.getStrIgnoreNull(
                progStlInfoService.getLatestDoubleCkeckedPeriodNo(
                        progStlInfoShowPara.getStlType(), progStlInfoShowPara.getStlPkid()));
        if(strLatestApprovedPeriodNo!=null){
            ProgStlItemSubQExample example = new ProgStlItemSubQExample();
            example.createCriteria()
                   .andSubcttPkidEqualTo(progStlInfoShowPara.getStlPkid())
                   .andPeriodNoEqualTo(strLatestApprovedPeriodNo);
            List<ProgStlItemSubQ> progStlItemSubQList =
                    progStlItemSubQMapper.selectByExample(example);
            for(ProgStlItemSubQ itemUnit: progStlItemSubQList){
                itemUnit.setCurrentPeriodEQty(null);
                itemUnit.setPeriodNo(progStlInfoShowPara.getPeriodNo());
                progStlItemSubQMapper.insert(itemUnit);
            }
        }
    }

    public int delByCttPkidAndPeriodNo(String strSubcttPkid,String strPeriodNo){
        ProgStlItemSubQExample example = new ProgStlItemSubQExample();
        example.createCriteria().
                andSubcttPkidEqualTo(strSubcttPkid).
                andPeriodNoEqualTo(strPeriodNo);
        return progStlItemSubQMapper.deleteByExample(example);
    }
}
