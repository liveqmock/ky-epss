package epss.service;

import epss.repository.dao.ProgMatqtyItemMapper;
import epss.repository.dao.notMyBits.CommonMapper;
import epss.repository.model.ProgMatqtyItemExample;
import epss.repository.model.ProgMatqtyItem;
import epss.common.utils.ToolUtil;
import epss.repository.model.model_show.ProgMatqtyInfoShow;
import epss.repository.model.model_show.ProgMatqtyItemShow;
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
public class ProgMatqtyItemService {
    @Resource
    private ProgMatqtyItemMapper progMatqtyItemMapper;
    @Resource
    private PlatformService platformService;
    @Resource
    private CommonMapper commonMapper;

    /**
     * 判断记录是否已存在
     *
     * @param progMatqtyItemShowPara
     * @return
     */
    public List<ProgMatqtyItem> isExistInDb(ProgMatqtyItemShow progMatqtyItemShowPara) {
        ProgMatqtyItemExample example = new ProgMatqtyItemExample();
        example.createCriteria()
                .andProgMatqtyInfoPkidEqualTo(progMatqtyItemShowPara.getProgMat_ProgMatqtyInfoPkid())
                .andSubcttItemPkidEqualTo(progMatqtyItemShowPara.getProgMat_SubcttItemPkid());
        return progMatqtyItemMapper.selectByExample(example);
    }

    public ProgMatqtyItem selectRecordsByPrimaryKey(String strPkId){
        return progMatqtyItemMapper.selectByPrimaryKey(strPkId);
    }

    public List<ProgMatqtyItem> selectRecordsByExample(ProgMatqtyItem esStlSubcttEngMPara){
        ProgMatqtyItemExample example = new ProgMatqtyItemExample();
        example.createCriteria().andProgMatqtyInfoPkidEqualTo(esStlSubcttEngMPara.getProgMatqtyInfoPkid())
                               .andSubcttItemPkidEqualTo(esStlSubcttEngMPara.getSubcttItemPkid());
        return progMatqtyItemMapper.selectByExample(example);
    }
	
    public void setFromLastStageApproveDataToThisStageBeginData(ProgMatqtyInfoShow progMatqtyInfoShowPara){
        // 插入新数据之后,就得把上期批准了的数据作为今期数据的起始数据
        /*String strLatestApprovedStageNo= ToolUtil.getStrIgnoreNull(
                esFlowService.getLatestApprovedStageNo(progEstInfoShowPara.getStlType(), progEstInfoShowPara.getStlPkid()));
        if(strLatestApprovedStageNo!=null){
            ProgMatqtyItemExample example = new ProgMatqtyItemExample();
            example.createCriteria()
                    .andSubcttPkidEqualTo(progEstInfoShowPara.getStlPkid())
                    .andStageNoEqualTo(strLatestApprovedStageNo);
            List<ProgMatqtyItem> progMatqtyItemList =
                    progMatqtyItemMapper.selectByExample(example);
            for(ProgMatqtyItem itemUnit: progMatqtyItemList){
                itemUnit.setCurrentPeriodMQty(null);
                itemUnit.setStageNo(progEstInfoShowPara.getStageNo());
                progMatqtyItemMapper.insert(itemUnit);
            }
        }*/
    }

    public int deleteRecord(String strPkId){
        return progMatqtyItemMapper.deleteByPrimaryKey(strPkId);
    }

    private ProgMatqtyItem fromShowModelToModel
            (ProgMatqtyItemShow progMatqtyItemShowPara){
        ProgMatqtyItem progMatqtyItemTemp =new ProgMatqtyItem();
        progMatqtyItemTemp.setPkid(progMatqtyItemShowPara.getProgMat_Pkid());
        progMatqtyItemTemp.setProgMatqtyInfoPkid(progMatqtyItemShowPara.getProgMat_ProgMatqtyInfoPkid());
        progMatqtyItemTemp.setAddUpQty(progMatqtyItemShowPara.getProgMat_AddUpQty());
        progMatqtyItemTemp.setThisStageQty(progMatqtyItemShowPara.getProgMat_ThisStageQty());
        progMatqtyItemTemp.setmPurchaseUnitPrice(progMatqtyItemShowPara.getProgMat_MPurchaseUnitPrice());
        progMatqtyItemTemp.setArchivedFlag(progMatqtyItemShowPara.getProgMat_ArchivedFlag());
        progMatqtyItemTemp.setOriginFlag(progMatqtyItemShowPara.getProgMat_OriginFlag());
        progMatqtyItemTemp.setCreatedBy(progMatqtyItemShowPara.getProgMat_CreatedBy());
        progMatqtyItemTemp.setCreatedTime(progMatqtyItemShowPara.getProgMat_CreatedTime());
        progMatqtyItemTemp.setUpdatedBy(progMatqtyItemShowPara.getProgMat_UpdatedBy());
        progMatqtyItemTemp.setUpdatedTime(progMatqtyItemShowPara.getProgMat_UpdatedTime());
        progMatqtyItemTemp.setRecVersion(progMatqtyItemShowPara.getProgMat_RecVersion());
        progMatqtyItemTemp.setRemark(progMatqtyItemShowPara.getProgMat_Remark());
        progMatqtyItemTemp.setSubcttItemPkid(progMatqtyItemShowPara.getProgMat_SubcttItemPkid());
        progMatqtyItemTemp.setTid(progMatqtyItemShowPara.getProgMat_Tid());
        return progMatqtyItemTemp;
    }

    public void updateRecord(ProgMatqtyItemShow progMatqtyItemShowPara){
        progMatqtyItemShowPara.setProgMat_RecVersion(
                ToolUtil.getIntIgnoreNull(progMatqtyItemShowPara.getProgMat_RecVersion()) + 1);
        progMatqtyItemShowPara.setProgMat_ArchivedFlag("0");
        progMatqtyItemShowPara.setProgMat_UpdatedBy(platformService.getStrLastUpdBy());
        progMatqtyItemShowPara.setProgMat_UpdatedByName(commonMapper.selectOpernameByCreatedBy(platformService.getStrLastUpdBy()));
        progMatqtyItemShowPara.setProgMat_UpdatedTime(platformService.getStrLastUpdTime());
        progMatqtyItemMapper.updateByPrimaryKey(fromShowModelToModel(progMatqtyItemShowPara)) ;
    }

    public void insertRecord(ProgMatqtyItemShow progMatqtyItemShowPara){
        progMatqtyItemShowPara.setProgMat_CreatedBy(platformService.getStrLastUpdBy());
        progMatqtyItemShowPara.setProgMat_CreatedByName(commonMapper.selectOpernameByCreatedBy(platformService.getStrLastUpdBy()));
        progMatqtyItemShowPara.setProgMat_CreatedTime(platformService.getStrLastUpdTime());
        progMatqtyItemShowPara.setProgMat_ArchivedFlag("0");
        progMatqtyItemMapper.insert(fromShowModelToModel(progMatqtyItemShowPara)) ;
    }

    public int deleteItemsByInitStlSubcttEng(String strProgMatqtyInfoPkid){
        ProgMatqtyItemExample example = new ProgMatqtyItemExample();
        example.createCriteria().andProgMatqtyInfoPkidEqualTo(strProgMatqtyInfoPkid);
        return progMatqtyItemMapper.deleteByExample(example);
    }
}
