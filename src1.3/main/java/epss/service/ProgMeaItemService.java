package epss.service;

import epss.repository.dao.ProgMeaItemMapper;
import epss.repository.dao.notMyBits.CommonMapper;
import epss.repository.model.ProgMeaItemExample;
import epss.repository.model.model_show.ProgMeaInfoShow;
import epss.repository.model.model_show.ProgMeaItemShow;
import epss.common.utils.ToolUtil;
import epss.repository.model.ProgMeaItem;
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
public class ProgMeaItemService {
    @Resource
    private ProgMeaItemMapper progMeaItemMapper;
    @Resource
    private PlatformService platformService;
    @Resource
    private CommonMapper commonMapper;
    /**
     * 判断记录是否已存在
     *
     * @param progMeaItemShowPara
     * @return
     */
    public List<ProgMeaItem> isExistInDb(ProgMeaItemShow progMeaItemShowPara) {
        ProgMeaItemExample example = new ProgMeaItemExample();
        example.createCriteria()
                .andProgMeaInfoPkidEqualTo(progMeaItemShowPara.getProgMea_PorgMeaInfoPkid())
                .andTkcttItemPkidEqualTo(progMeaItemShowPara.getProgMea_TkcttItemPkid());
        return progMeaItemMapper.selectByExample(example);
    }

    public List<ProgMeaItem> getProgMeaItemListByModel(ProgMeaItem progMeaItemPara){
        ProgMeaItemExample example = new ProgMeaItemExample();
        example.createCriteria()
                .andProgMeaInfoPkidEqualTo(progMeaItemPara.getProgMeaInfoPkid())
                .andTkcttItemPkidEqualTo(progMeaItemPara.getTkcttItemPkid());
        return progMeaItemMapper.selectByExample(example);
    }
    public int deleteRecord(String strPkId){
        return progMeaItemMapper.deleteByPrimaryKey(strPkId);
    }

    public void updateRecord(ProgMeaItemShow progMeaItemShowPara){
        progMeaItemShowPara.setProgMea_RecVersion(
                ToolUtil.getIntIgnoreNull(progMeaItemShowPara.getProgMea_RecVersion()) + 1);
        progMeaItemShowPara.setProgMea_ArchivedFlag("0");
        progMeaItemShowPara.setProgMea_UpdatedBy(platformService.getStrLastUpdBy());
        progMeaItemShowPara.setProgMea_UpdatedByName(commonMapper.selectOpernameByCreatedBy(platformService.getStrLastUpdBy()));
        progMeaItemShowPara.setProgMea_UpdatedTime(platformService.getStrLastUpdTime());
        progMeaItemMapper.updateByPrimaryKey(fromShowModelToModel(progMeaItemShowPara)) ;
    }

    public void insertRecord(ProgMeaItemShow progMeaItemShowPara){
        progMeaItemShowPara.setProgMea_CreatedBy(platformService.getStrLastUpdBy());
        progMeaItemShowPara.setProgMea_CreatedByName(commonMapper.selectOpernameByCreatedBy(platformService.getStrLastUpdBy()));
        progMeaItemShowPara.setProgMea_CreatedTime(platformService.getStrLastUpdTime());
        progMeaItemShowPara.setProgMea_ArchivedFlag("0");
        progMeaItemMapper.insert(fromShowModelToModel(progMeaItemShowPara));
    }

    private ProgMeaItem fromShowModelToModel
            (ProgMeaItemShow progMeaItemShowPara){
        ProgMeaItem progMeaItemTemp =new ProgMeaItem();
        progMeaItemTemp.setPkid(progMeaItemShowPara.getProgMea_Pkid());
        progMeaItemTemp.setProgMeaInfoPkid(progMeaItemShowPara.getProgMea_PorgMeaInfoPkid());
        progMeaItemTemp.setTkcttItemPkid(progMeaItemShowPara.getProgMea_TkcttItemPkid());
        progMeaItemTemp.setAddUpQty(progMeaItemShowPara.getProgMea_AddUpQty());
        progMeaItemTemp.setAddUpAmt(progMeaItemShowPara.getProgMea_AddUpAmt());
        progMeaItemTemp.setThisStageQty(progMeaItemShowPara.getProgMea_ThisStageQty());
        progMeaItemTemp.setThisStageAmt(progMeaItemShowPara.getProgMea_ThisStageAmt());
        progMeaItemTemp.setArchivedFlag(progMeaItemShowPara.getProgMea_ArchivedFlag());
        progMeaItemTemp.setCreatedBy(progMeaItemShowPara.getProgMea_CreatedBy());
        progMeaItemTemp.setCreatedTime(progMeaItemShowPara.getProgMea_CreatedTime());
        progMeaItemTemp.setUpdatedBy(progMeaItemShowPara.getProgMea_UpdatedBy());
        progMeaItemTemp.setUpdatedTime(progMeaItemShowPara.getProgMea_UpdatedTime());
        progMeaItemTemp.setRecVersion(progMeaItemShowPara.getProgMea_RecVersion());
        return progMeaItemTemp;
    }

    public void setFromLastStageApproveDataToThisStageBeginData(ProgMeaInfoShow progMeaInfoShowPara){
        // 插入新数据之后,就得把上期批准了的数据作为今期数据的起始数据
       /* String strLatestApprovedStageNo= ToolUtil.getStrIgnoreNull(
                esFlowService.getLatestApprovedStageNo(progMeaInfoShowPara.getStlType(), progMeaInfoShowPara.getStlPkid()));
        if(strLatestApprovedStageNo!=null){
            ProgMeaItemExample example = new ProgMeaItemExample();
            example.createCriteria()
                    .andTkcttItemPkidIn(progMeaInfoShowPara.getStlPkid())
                    .andStageNoEqualTo(strLatestApprovedStageNo);
            List<ProgMeaItem> progMeaItemList =
                    progMeaItemMapper.selectByExample(example);
            for(ProgMeaItem itemUnit: progMeaItemList){
                itemUnit.setThisStageQty(null);
                itemUnit.setStageNo(progMeaInfoShowPara.getStageNo());
                progMeaItemMapper.insert(itemUnit);
            }
        }*/
    }

    public int deleteItemsByInitStlTkcttEng(String strTkcttInfoPkid){
        ProgMeaItemExample example = new ProgMeaItemExample();
        example.createCriteria().andProgMeaInfoPkidEqualTo(strTkcttInfoPkid);
        return progMeaItemMapper.deleteByExample(example);
    }
}
