package epss.service;

import epss.repository.dao.ProgStlItemSubFMapper;
import epss.repository.model.ProgStlItemSubF;
import epss.repository.model.ProgStlItemSubFExample;
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
    private ProgStlItemSubFMapper ProgStlItemSubFMapper;

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
        return ProgStlItemSubFMapper.selectByExample(example);
    }

    public List<ProgStlItemSubF> selectRecordsByExample(ProgStlItemSubF esStlSubcttEngMPara){
        ProgStlItemSubFExample example = new ProgStlItemSubFExample();
        example.createCriteria()
                               .andSubcttPkidEqualTo(esStlSubcttEngMPara.getSubcttPkid())
                               .andSubcttItemPkidEqualTo(esStlSubcttEngMPara.getSubcttItemPkid())
                               .andPeriodNoEqualTo(esStlSubcttEngMPara.getPeriodNo());
        return ProgStlItemSubFMapper.selectByExample(example);
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
        ProgStlItemSubFMapper.updateByPrimaryKey(ProgStlItemSubFTemp) ;
    }

    public void insertRecord(ProgStlItemSubFShow progStlItemSubFShowPara){
        ProgStlItemSubF ProgStlItemSubFTemp =fromModelShowToModel(progStlItemSubFShowPara);
        ProgStlItemSubFTemp.setCreatedBy(ToolUtil.getOperatorManager().getOperator().getPkid());
        ProgStlItemSubFTemp.setCreatedTime(ToolUtil.getStrLastUpdTime());
        ProgStlItemSubFTemp.setArchivedFlag("0");
        ProgStlItemSubFTemp.setLastUpdBy(ToolUtil.getOperatorManager().getOperator().getPkid());
        ProgStlItemSubFTemp.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        ProgStlItemSubFMapper.insert(ProgStlItemSubFTemp) ;
    }

    public int delByCttPkidAndPeriodNo(String strSubcttPkid,String strPeriodNo){
        ProgStlItemSubFExample example = new ProgStlItemSubFExample();
        example.createCriteria().
                andSubcttPkidEqualTo(strSubcttPkid).
                andPeriodNoEqualTo(strPeriodNo);
        return ProgStlItemSubFMapper.deleteByExample(example);
    }
}
