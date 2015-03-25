package epss.service;

import epss.repository.dao.ProgStlItemSubFMapper;
import epss.repository.model.ProgStlItemSubF;
import epss.repository.model.ProgStlItemSubFExample;
import epss.repository.model.model_show.ProgStlInfoShow;
import epss.repository.model.model_show.ProgStlItemSubFShow;
import org.springframework.stereotype.Service;
import skyline.util.ToolUtil;
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
public class ProgStlItemSubFService {
    @Resource
    private ProgStlItemSubFMapper progStlItemSubFMapper;
    @Resource
    private ProgStlInfoService progStlInfoService;

    /**
     * 判断记录是否已存在
     *
     * @param progStlItemSubFShowPara
     * @return
     */
    public List<ProgStlItemSubF> isExistInDb(ProgStlItemSubFShow progStlItemSubFShowPara) {
        ProgStlItemSubFExample example = new ProgStlItemSubFExample();
        example.createCriteria()
                .andSubcttPkidEqualTo(progStlItemSubFShowPara.getEngSMng_SubcttPkid())
                .andPeriodNoEqualTo(progStlItemSubFShowPara.getEngSMng_PeriodNo())
                .andSubcttItemPkidEqualTo(progStlItemSubFShowPara.getEngSMng_SubcttItemPkid());
        return progStlItemSubFMapper.selectByExample(example);
    }

    public List<ProgStlItemSubF> selectRecordsByExample(ProgStlItemSubF esStlSubcttEngMPara){
        ProgStlItemSubFExample example = new ProgStlItemSubFExample();
        example.createCriteria()
                               .andSubcttPkidEqualTo(esStlSubcttEngMPara.getSubcttPkid())
                               .andSubcttItemPkidEqualTo(esStlSubcttEngMPara.getSubcttItemPkid())
                               .andPeriodNoEqualTo(esStlSubcttEngMPara.getPeriodNo());
        return progStlItemSubFMapper.selectByExample(example);
    }

    private ProgStlItemSubF fromModelShowToModel
            (ProgStlItemSubFShow progStlItemSubFShowPara){
        ProgStlItemSubF ProgStlItemSubFTemp =new ProgStlItemSubF();
        ProgStlItemSubFTemp.setPkid(progStlItemSubFShowPara.getEngSMng_Pkid());
        ProgStlItemSubFTemp.setPeriodNo(progStlItemSubFShowPara.getEngSMng_PeriodNo());
        ProgStlItemSubFTemp.setSubcttPkid(progStlItemSubFShowPara.getEngSMng_SubcttPkid());
        ProgStlItemSubFTemp.setSubcttItemPkid(progStlItemSubFShowPara.getEngSMng_SubcttItemPkid());
        ProgStlItemSubFTemp.setAddUpToAmt(progStlItemSubFShowPara.getEngSMng_AddUpToAmt());
        ProgStlItemSubFTemp.setThisStageAmt(progStlItemSubFShowPara.getEngSMng_ThisStageAmt());
        ProgStlItemSubFTemp.setArchivedFlag(progStlItemSubFShowPara.getEngSMng_ArchivedFlag() );
        ProgStlItemSubFTemp.setCreatedBy(progStlItemSubFShowPara.getEngSMng_CreatedBy());
        ProgStlItemSubFTemp.setCreatedTime(progStlItemSubFShowPara.getEngSMng_CreatedTime());
        ProgStlItemSubFTemp.setLastUpdBy(progStlItemSubFShowPara.getEngSMng_LastUpdBy());
        ProgStlItemSubFTemp.setLastUpdTime(progStlItemSubFShowPara.getEngSMng_LastUpdTime());
        ProgStlItemSubFTemp.setRecVersion(progStlItemSubFShowPara.getEngSMng_RecVersion());
        return ProgStlItemSubFTemp;
    }

    public void updateRecord(ProgStlItemSubFShow progStlItemSubFShowPara){
        ProgStlItemSubF ProgStlItemSubFTemp =fromModelShowToModel(progStlItemSubFShowPara);
        ProgStlItemSubFTemp.setRecVersion(
                ToolUtil.getIntIgnoreNull(ProgStlItemSubFTemp.getRecVersion())+1);
        ProgStlItemSubFTemp.setArchivedFlag("0");
        ProgStlItemSubFTemp.setLastUpdBy(ToolUtil.getOperatorManager().getOperator().getPkid());
        ProgStlItemSubFTemp.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        progStlItemSubFMapper.updateByPrimaryKey(ProgStlItemSubFTemp) ;
    }

    public void insertRecord(ProgStlItemSubFShow progStlItemSubFShowPara){
        ProgStlItemSubF ProgStlItemSubFTemp =fromModelShowToModel(progStlItemSubFShowPara);
        ProgStlItemSubFTemp.setCreatedBy(ToolUtil.getOperatorManager().getOperator().getPkid());
        ProgStlItemSubFTemp.setCreatedTime(ToolUtil.getStrLastUpdTime());
        ProgStlItemSubFTemp.setArchivedFlag("0");
        ProgStlItemSubFTemp.setLastUpdBy(ToolUtil.getOperatorManager().getOperator().getPkid());
        ProgStlItemSubFTemp.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        progStlItemSubFMapper.insert(ProgStlItemSubFTemp) ;
    }

    public int delByCttPkidAndPeriodNo(String strSubcttPkid,String strPeriodNo){
        ProgStlItemSubFExample example = new ProgStlItemSubFExample();
        example.createCriteria().
                andSubcttPkidEqualTo(strSubcttPkid).
                andPeriodNoEqualTo(strPeriodNo);
        return progStlItemSubFMapper.deleteByExample(example);
    }



    public void setFromLastStageAddUpToDataToThisStageBeginData(ProgStlInfoShow progStlInfoShowPara){
        // 插入新数据之后,就得把上期批准了的数据作为今期数据的起始数据
        String strLatestApprovedPeriodNo= ToolUtil.getStrIgnoreNull(
                progStlInfoService.getLatestDoubleCkeckedPeriodNo(
                        progStlInfoShowPara.getStlType(),
                        progStlInfoShowPara.getStlPkid()));
        if(strLatestApprovedPeriodNo!=null){
            ProgStlItemSubFExample example = new ProgStlItemSubFExample();
            example.createCriteria()
                    .andSubcttPkidEqualTo(progStlInfoShowPara.getStlPkid())
                    .andPeriodNoEqualTo(strLatestApprovedPeriodNo);
            List<ProgStlItemSubF> progStlItemSubFList =
                    progStlItemSubFMapper.selectByExample(example);
            for(ProgStlItemSubF itemUnit: progStlItemSubFList){
                itemUnit.setThisStageAmt(null);
                itemUnit.setPeriodNo(progStlInfoShowPara.getPeriodNo());
                progStlItemSubFMapper.insert(itemUnit);
            }
        }
    }
}
