package epss.service;

import epss.repository.dao.ProgWorkqtyItemMapper;
import epss.repository.dao.notMyBits.CommonMapper;
import epss.repository.model.ProgWorkqtyItemExample;
import epss.repository.model.ProgWorkqtyItem;
import epss.common.utils.ToolUtil;
import epss.repository.model.model_show.ProgWorkqtyInfoShow;
import epss.repository.model.model_show.ProgWorkqtyItemShow;
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
public class ProgWorkqtyItemService {
    @Resource
    private ProgWorkqtyItemMapper progWorkqtyItemMapper;
    @Resource
    private PlatformService platformService;
    @Resource
    private CommonMapper commonMapper;

    /**
     * 判断记录是否已存在
     *
     * @param progWorkqtyItemShowPara
     * @return
     */
    public List<ProgWorkqtyItem> isExistInDb(ProgWorkqtyItemShow progWorkqtyItemShowPara) {
        ProgWorkqtyItemExample example = new ProgWorkqtyItemExample();
        example.createCriteria()
                .andProgWorkqtyInfoPkidEqualTo(progWorkqtyItemShowPara.getProgWork_ProgWorkqtyInfoPkid())
                .andSubcttItemPkidEqualTo(progWorkqtyItemShowPara.getProgWork_SubcttItemPkid());
        return progWorkqtyItemMapper.selectByExample(example);
    }

    public ProgWorkqtyItem selectRecordsByPrimaryKey(String strPkId){
        return progWorkqtyItemMapper.selectByPrimaryKey(strPkId);
    }

    public List<ProgWorkqtyItem> selectRecordsByExample(ProgWorkqtyItem esStlSubcttEngMPara){
        ProgWorkqtyItemExample example = new ProgWorkqtyItemExample();
        example.createCriteria().andProgWorkqtyInfoPkidEqualTo(esStlSubcttEngMPara.getProgWorkqtyInfoPkid())
                               .andSubcttItemPkidEqualTo(esStlSubcttEngMPara.getSubcttItemPkid());
        return progWorkqtyItemMapper.selectByExample(example);
    }
	
    public void setFromLastStageApproveDataToThisStageBeginData(ProgWorkqtyInfoShow progWorkqtyInfoShowPara){
        // 插入新数据之后,就得把上期批准了的数据作为今期数据的起始数据
        /*String strLatestApprovedStageNo= ToolUtil.getStrIgnoreNull(
                esFlowService.getLatestApprovedStageNo(progEstInfoShowPara.getStlType(), progEstInfoShowPara.getStlPkid()));
        if(strLatestApprovedStageNo!=null){
            ProgWorkqtyItemExample example = new ProgWorkqtyItemExample();
            example.createCriteria()
                    .andSubcttPkidEqualTo(progEstInfoShowPara.getStlPkid())
                    .andStageNoEqualTo(strLatestApprovedStageNo);
            List<ProgWorkqtyItem> progWorkqtyItemList =
                    progWorkqtyItemMapper.selectByExample(example);
            for(ProgWorkqtyItem itemUnit: progWorkqtyItemList){
                itemUnit.setCurrentPeriodMQty(null);
                itemUnit.setStageNo(progEstInfoShowPara.getStageNo());
                progWorkqtyItemMapper.insert(itemUnit);
            }
        }*/
    }

    public int deleteRecord(String strPkId){
        return progWorkqtyItemMapper.deleteByPrimaryKey(strPkId);
    }

    private ProgWorkqtyItem fromShowModelToModel
            (ProgWorkqtyItemShow progWorkqtyItemShowPara){
        ProgWorkqtyItem progWorkqtyItemTemp =new ProgWorkqtyItem();
        progWorkqtyItemTemp.setPkid(progWorkqtyItemShowPara.getProgWork_Pkid());
        progWorkqtyItemTemp.setProgWorkqtyInfoPkid(progWorkqtyItemShowPara.getProgWork_ProgWorkqtyInfoPkid());
        progWorkqtyItemTemp.setAddUpQty(progWorkqtyItemShowPara.getProgWork_AddUpQty());
        progWorkqtyItemTemp.setThisStageQty(progWorkqtyItemShowPara.getProgWork_ThisStageQty());
        progWorkqtyItemTemp.setArchivedFlag(progWorkqtyItemShowPara.getProgWork_ArchivedFlag());
        progWorkqtyItemTemp.setOriginFlag(progWorkqtyItemShowPara.getProgWork_OriginFlag());
        progWorkqtyItemTemp.setCreatedBy(progWorkqtyItemShowPara.getProgWork_CreatedBy());
        progWorkqtyItemTemp.setCreatedTime(progWorkqtyItemShowPara.getProgWork_CreatedTime());
        progWorkqtyItemTemp.setUpdatedBy(progWorkqtyItemShowPara.getProgWork_UpdatedBy());
        progWorkqtyItemTemp.setUpdatedTime(progWorkqtyItemShowPara.getProgWork_UpdatedTime());
        progWorkqtyItemTemp.setRecVersion(progWorkqtyItemShowPara.getProgWork_RecVersion());
        progWorkqtyItemTemp.setRemark(progWorkqtyItemShowPara.getProgWork_Remark());
        progWorkqtyItemTemp.setSubcttItemPkid(progWorkqtyItemShowPara.getProgWork_SubcttItemPkid());
        progWorkqtyItemTemp.setTid(progWorkqtyItemShowPara.getProgWork_Tid());
        return progWorkqtyItemTemp;
    }

    public void updateRecord(ProgWorkqtyItemShow progWorkqtyItemShowPara){
        progWorkqtyItemShowPara.setProgWork_RecVersion(
                ToolUtil.getIntIgnoreNull(progWorkqtyItemShowPara.getProgWork_RecVersion()) + 1);
        progWorkqtyItemShowPara.setProgWork_ArchivedFlag("0");
        progWorkqtyItemShowPara.setProgWork_UpdatedBy(platformService.getStrLastUpdBy());
        progWorkqtyItemShowPara.setProgWork_UpdatedByName(commonMapper.selectOpernameByCreatedBy(platformService.getStrLastUpdBy()));
        progWorkqtyItemShowPara.setProgWork_UpdatedTime(platformService.getStrLastUpdTime());
        progWorkqtyItemMapper.updateByPrimaryKey(fromShowModelToModel(progWorkqtyItemShowPara)) ;
    }

    public void insertRecord(ProgWorkqtyItemShow progWorkqtyItemShowPara){
        progWorkqtyItemShowPara.setProgWork_CreatedBy(platformService.getStrLastUpdBy());
        progWorkqtyItemShowPara.setProgWork_CreatedByName(commonMapper.selectOpernameByCreatedBy(platformService.getStrLastUpdBy()));
        progWorkqtyItemShowPara.setProgWork_CreatedTime(platformService.getStrLastUpdTime());
        progWorkqtyItemShowPara.setProgWork_ArchivedFlag("0");
        progWorkqtyItemMapper.insert(fromShowModelToModel(progWorkqtyItemShowPara)) ;
    }

    public int deleteItemsByInitStlSubcttEng(String strProgWorkqtyInfoPkid){
        ProgWorkqtyItemExample example = new ProgWorkqtyItemExample();
        example.createCriteria().andProgWorkqtyInfoPkidEqualTo(strProgWorkqtyInfoPkid);
        return progWorkqtyItemMapper.deleteByExample(example);
    }
}
