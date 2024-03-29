package epss.service;

import epss.repository.model.model_show.ProgInfoShow;
import epss.common.utils.ToolUtil;
import epss.repository.dao.EsItemStlSubcttEngMMapper;
import epss.repository.model.EsItemStlSubcttEngM;
import epss.repository.model.EsItemStlSubcttEngMExample;
import epss.repository.model.model_show.ProgMatQtyItemShow;
import epss.service.common.EsFlowService;
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
public class EsItemStlSubcttEngMService {
    @Resource
    private EsItemStlSubcttEngMMapper esItemStlSubcttEngMMapper;
    @Resource
    private EsFlowService esFlowService;
    @Resource
    private PlatformService platformService;

    /**
     * 判断记录是否已存在
     *
     * @param progMatQtyItemShowPara
     * @return
     */
    public List<EsItemStlSubcttEngM> isExistInDb(ProgMatQtyItemShow progMatQtyItemShowPara) {
        EsItemStlSubcttEngMExample example = new EsItemStlSubcttEngMExample();
        example.createCriteria()
                .andSubcttPkidEqualTo(progMatQtyItemShowPara.getEngMMng_SubcttPkid())
                .andPeriodNoEqualTo(progMatQtyItemShowPara.getEngMMng_PeriodNo())
                .andSubcttItemPkidEqualTo(progMatQtyItemShowPara.getEngMMng_SubcttItemPkid());
        return esItemStlSubcttEngMMapper.selectByExample(example);
    }

    public EsItemStlSubcttEngM selectRecordsByPrimaryKey(String strPkId){
        return esItemStlSubcttEngMMapper.selectByPrimaryKey(strPkId);
    }

    public List<EsItemStlSubcttEngM> selectRecordsByExample(EsItemStlSubcttEngM esStlSubcttEngMPara){
        EsItemStlSubcttEngMExample example = new EsItemStlSubcttEngMExample();
        example.createCriteria()
                               .andSubcttPkidEqualTo(esStlSubcttEngMPara.getSubcttPkid())
                               .andSubcttItemPkidEqualTo(esStlSubcttEngMPara.getSubcttItemPkid())
                               .andPeriodNoEqualTo(esStlSubcttEngMPara.getPeriodNo());
        return esItemStlSubcttEngMMapper.selectByExample(example);
    }
	
    public void setFromLastStageApproveDataToThisStageBeginData(ProgInfoShow progInfoShowPara){
        // 插入新数据之后,就得把上期批准了的数据作为今期数据的起始数据
        String strLatestApprovedPeriodNo= ToolUtil.getStrIgnoreNull(
                esFlowService.getLatestApprovedPeriodNo(progInfoShowPara.getStlType(), progInfoShowPara.getStlPkid()));
        if(strLatestApprovedPeriodNo!=null){
            EsItemStlSubcttEngMExample example = new EsItemStlSubcttEngMExample();
            example.createCriteria()
                    .andSubcttPkidEqualTo(progInfoShowPara.getStlPkid())
                    .andPeriodNoEqualTo(strLatestApprovedPeriodNo);
            List<EsItemStlSubcttEngM> esItemStlSubcttEngMList =
                    esItemStlSubcttEngMMapper.selectByExample(example);
            for(EsItemStlSubcttEngM itemUnit:esItemStlSubcttEngMList){
                itemUnit.setCurrentPeriodMQty(null);
                itemUnit.setPeriodNo(progInfoShowPara.getPeriodNo());
                esItemStlSubcttEngMMapper.insert(itemUnit);
            }
        }
    }

    public int deleteRecord(String strPkId){
        return esItemStlSubcttEngMMapper.deleteByPrimaryKey(strPkId);
    }

    private EsItemStlSubcttEngM fromConstructToModel
            (ProgMatQtyItemShow progMatQtyItemShowPara){
        EsItemStlSubcttEngM esItemStlSubcttEngMTemp=new EsItemStlSubcttEngM();
        esItemStlSubcttEngMTemp.setPkid(progMatQtyItemShowPara.getEngMMng_Pkid());
        esItemStlSubcttEngMTemp.setPeriodNo(progMatQtyItemShowPara.getEngMMng_PeriodNo());
        esItemStlSubcttEngMTemp.setSubcttPkid(progMatQtyItemShowPara.getEngMMng_SubcttPkid());
        esItemStlSubcttEngMTemp.setSubcttItemPkid(progMatQtyItemShowPara.getEngMMng_SubcttItemPkid());
        esItemStlSubcttEngMTemp.setBeginToCurrentPeriodMQty(progMatQtyItemShowPara.getEngMMng_BeginToCurrentPeriodMQty());
        esItemStlSubcttEngMTemp.setCurrentPeriodMQty(progMatQtyItemShowPara.getEngMMng_CurrentPeriodMQty());
        esItemStlSubcttEngMTemp.setmPurchaseUnitPrice(progMatQtyItemShowPara.getEngMMng_MPurchaseUnitPrice());
        esItemStlSubcttEngMTemp.setDeleteFlag(progMatQtyItemShowPara.getEngMMng_DeletedFlag());
        esItemStlSubcttEngMTemp.setCreatedBy(progMatQtyItemShowPara.getEngMMng_CreatedBy());
        esItemStlSubcttEngMTemp.setCreatedDate(progMatQtyItemShowPara.getEngMMng_CreatedDate());
        esItemStlSubcttEngMTemp.setLastUpdBy(progMatQtyItemShowPara.getEngMMng_LastUpdBy());
        esItemStlSubcttEngMTemp.setLastUpdDate(progMatQtyItemShowPara.getEngMMng_LastUpdDate());
        esItemStlSubcttEngMTemp.setModificationNum(progMatQtyItemShowPara.getEngMMng_ModificationNum());
        return esItemStlSubcttEngMTemp;
    }

    public void updateRecord(ProgMatQtyItemShow progMatQtyItemShowPara){
        EsItemStlSubcttEngM esItemStlSubcttEngMTemp=fromConstructToModel(progMatQtyItemShowPara);
        esItemStlSubcttEngMTemp.setModificationNum(
                ToolUtil.getIntIgnoreNull(esItemStlSubcttEngMTemp.getModificationNum())+1);
        esItemStlSubcttEngMTemp.setDeleteFlag("0");
        esItemStlSubcttEngMTemp.setLastUpdBy(platformService.getStrLastUpdBy());
        esItemStlSubcttEngMTemp.setLastUpdDate(platformService.getStrLastUpdDate());
        esItemStlSubcttEngMMapper.updateByPrimaryKey(esItemStlSubcttEngMTemp) ;
    }

    public void insertRecord(ProgMatQtyItemShow progMatQtyItemShowPara){
        EsItemStlSubcttEngM esItemStlSubcttEngMTemp=fromConstructToModel(progMatQtyItemShowPara);
        esItemStlSubcttEngMTemp.setCreatedBy(platformService.getStrLastUpdBy());
        esItemStlSubcttEngMTemp.setCreatedDate(platformService.getStrLastUpdDate());
        esItemStlSubcttEngMTemp.setDeleteFlag("0");
        esItemStlSubcttEngMTemp.setLastUpdBy(platformService.getStrLastUpdBy());
        esItemStlSubcttEngMTemp.setLastUpdDate(platformService.getStrLastUpdDate());
        esItemStlSubcttEngMMapper.insert(esItemStlSubcttEngMTemp) ;
    }

    public int deleteItemsByInitStlSubcttEng(String strSubcttPkid,String strPeriodNo){
        EsItemStlSubcttEngMExample example = new EsItemStlSubcttEngMExample();
        example.createCriteria().
                andSubcttPkidEqualTo(strSubcttPkid).
                andPeriodNoEqualTo(strPeriodNo);
        return esItemStlSubcttEngMMapper.deleteByExample(example);
    }
}
