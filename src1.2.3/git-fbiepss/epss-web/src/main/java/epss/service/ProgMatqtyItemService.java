package epss.service;

import epss.repository.dao.ProgStlItemSubMMapper;
import epss.repository.model.ProgStlItemSubMExample;
import epss.repository.model.model_show.ProgStlInfoShow;
import epss.repository.model.model_show.ProgStlItemSubMShow;
import skyline.util.ToolUtil;
import epss.repository.model.ProgStlItemSubM;
import org.springframework.stereotype.Service;
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
public class ProgMatqtyItemService {
    @Resource
    private ProgStlItemSubMMapper progStlItemSubMMapper;
    @Resource
    private ProgStlInfoService progStlInfoService;

    /**
     * �жϼ�¼�Ƿ��Ѵ���
     *
     * @param progStlItemSubMShowPara
     * @return
     */
    public List<ProgStlItemSubM> isExistInDb(ProgStlItemSubMShow progStlItemSubMShowPara) {
        ProgStlItemSubMExample example = new ProgStlItemSubMExample();
        example.createCriteria()
                .andSubcttPkidEqualTo(progStlItemSubMShowPara.getEngMMng_SubcttPkid())
                .andPeriodNoEqualTo(progStlItemSubMShowPara.getEngMMng_PeriodNo())
                .andSubcttItemPkidEqualTo(progStlItemSubMShowPara.getEngMMng_SubcttItemPkid());
        return progStlItemSubMMapper.selectByExample(example);
    }

    public ProgStlItemSubM selectRecordsByPrimaryKey(String strPkId){
        return progStlItemSubMMapper.selectByPrimaryKey(strPkId);
    }

    public List<ProgStlItemSubM> selectRecordsByExample(ProgStlItemSubM esStlSubcttEngMPara){
        ProgStlItemSubMExample example = new ProgStlItemSubMExample();
        example.createCriteria()
                               .andSubcttPkidEqualTo(esStlSubcttEngMPara.getSubcttPkid())
                               .andSubcttItemPkidEqualTo(esStlSubcttEngMPara.getSubcttItemPkid())
                               .andPeriodNoEqualTo(esStlSubcttEngMPara.getPeriodNo());
        return progStlItemSubMMapper.selectByExample(example);
    }
	
    public void setFromLastStageApproveDataToThisStageBeginData(ProgStlInfoShow progStlInfoShowPara){
        // ����������֮��,�͵ð�������׼�˵�������Ϊ�������ݵ���ʼ����
        String strLatestApprovedPeriodNo= ToolUtil.getStrIgnoreNull(
                progStlInfoService.getLatestDoubleCkeckedPeriodNo(
                        progStlInfoShowPara.getStlType(),
                        progStlInfoShowPara.getStlPkid()));
        if(strLatestApprovedPeriodNo!=null){
            ProgStlItemSubMExample example = new ProgStlItemSubMExample();
            example.createCriteria()
                    .andSubcttPkidEqualTo(progStlInfoShowPara.getStlPkid())
                    .andPeriodNoEqualTo(strLatestApprovedPeriodNo);
            List<ProgStlItemSubM> progStlItemSubMList =
                    progStlItemSubMMapper.selectByExample(example);
            for(ProgStlItemSubM itemUnit: progStlItemSubMList){
                itemUnit.setCurrentPeriodMQty(null);
                itemUnit.setPeriodNo(progStlInfoShowPara.getPeriodNo());
                progStlItemSubMMapper.insert(itemUnit);
            }
        }
    }

    public int deleteRecord(String strPkId){
        return progStlItemSubMMapper.deleteByPrimaryKey(strPkId);
    }

    private ProgStlItemSubM fromModelShowToModel
            (ProgStlItemSubMShow progStlItemSubMShowPara){
        ProgStlItemSubM progStlItemSubMTemp =new ProgStlItemSubM();
        progStlItemSubMTemp.setPkid(progStlItemSubMShowPara.getEngMMng_Pkid());
        progStlItemSubMTemp.setPeriodNo(progStlItemSubMShowPara.getEngMMng_PeriodNo());
        progStlItemSubMTemp.setSubcttPkid(progStlItemSubMShowPara.getEngMMng_SubcttPkid());
        progStlItemSubMTemp.setSubcttItemPkid(progStlItemSubMShowPara.getEngMMng_SubcttItemPkid());
        progStlItemSubMTemp.setBeginToCurrentPeriodMQty(progStlItemSubMShowPara.getEngMMng_BeginToCurrentPeriodMQty());
        progStlItemSubMTemp.setCurrentPeriodMQty(progStlItemSubMShowPara.getEngMMng_CurrentPeriodMQty());
        progStlItemSubMTemp.setmPurchaseUnitPrice(progStlItemSubMShowPara.getEngMMng_MPurchaseUnitPrice());
        progStlItemSubMTemp.setArchivedFlag(progStlItemSubMShowPara.getEngMMng_ArchivedFlag());
        progStlItemSubMTemp.setCreatedBy(progStlItemSubMShowPara.getEngMMng_CreatedBy());
        progStlItemSubMTemp.setCreatedTime(progStlItemSubMShowPara.getEngMMng_CreatedTime());
        progStlItemSubMTemp.setLastUpdBy(progStlItemSubMShowPara.getEngMMng_LastUpdBy());
        progStlItemSubMTemp.setLastUpdTime(progStlItemSubMShowPara.getEngMMng_LastUpdTime());
        progStlItemSubMTemp.setRecversion(progStlItemSubMShowPara.getEngMMng_Recversion());
        return progStlItemSubMTemp;
    }

    public void updateRecord(ProgStlItemSubMShow progStlItemSubMShowPara){
        ProgStlItemSubM progStlItemSubMTemp =fromModelShowToModel(progStlItemSubMShowPara);
        progStlItemSubMTemp.setRecversion(
                ToolUtil.getIntIgnoreNull(progStlItemSubMTemp.getRecversion())+1);
        progStlItemSubMTemp.setArchivedFlag("0");
        progStlItemSubMTemp.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        progStlItemSubMTemp.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        progStlItemSubMMapper.updateByPrimaryKey(progStlItemSubMTemp) ;
    }

    public void insertRecord(ProgStlItemSubMShow progStlItemSubMShowPara){
        ProgStlItemSubM progStlItemSubMTemp =fromModelShowToModel(progStlItemSubMShowPara);
        progStlItemSubMTemp.setCreatedBy(ToolUtil.getOperatorManager().getOperatorId());
        progStlItemSubMTemp.setCreatedTime(ToolUtil.getStrLastUpdTime());
        progStlItemSubMTemp.setArchivedFlag("0");
        progStlItemSubMTemp.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        progStlItemSubMTemp.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        progStlItemSubMMapper.insert(progStlItemSubMTemp) ;
    }

    public int deleteItemsByInitStlSubcttEng(String strSubcttPkid,String strPeriodNo){
        ProgStlItemSubMExample example = new ProgStlItemSubMExample();
        example.createCriteria().
                andSubcttPkidEqualTo(strSubcttPkid).
                andPeriodNoEqualTo(strPeriodNo);
        return progStlItemSubMMapper.deleteByExample(example);
    }
}
