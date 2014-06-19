package epss.service;

import epss.repository.dao.ProgEstItemMapper;
import epss.repository.dao.notMyBits.CommonMapper;
import epss.repository.model.ProgEstItemExample;
import epss.repository.model.model_show.ProgEstInfoShow;
import epss.repository.model.model_show.ProgEstItemShow;
import epss.common.utils.ToolUtil;
import epss.repository.model.ProgEstItem;
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
public class ProgEstItemService {
    @Resource
    private ProgEstItemMapper progEstItemMapper;
    @Resource
    private PlatformService platformService;
    @Resource
    private CommonMapper commonMapper;
    /**
     * 判断记录是否已存在
     *
     * @param progEstItemShowPara
     * @return
     */
    public List<ProgEstItem> isExistInDb(ProgEstItemShow progEstItemShowPara) {
        ProgEstItemExample example = new ProgEstItemExample();
        example.createCriteria()
                .andProgEstInfoPkidEqualTo(progEstItemShowPara.getProgEst_PorgEstInfoPkid())
                .andTkcttItemPkidEqualTo(progEstItemShowPara.getProgEst_TkcttItemPkid());
        return progEstItemMapper.selectByExample(example);
    }

    public List<ProgEstItem> getProgEstItemListByModel(ProgEstItem progEstItemPara){
        ProgEstItemExample example = new ProgEstItemExample();
        example.createCriteria()
                .andProgEstInfoPkidEqualTo(progEstItemPara.getProgEstInfoPkid())
                .andTkcttItemPkidEqualTo(progEstItemPara.getTkcttItemPkid());
        return progEstItemMapper.selectByExample(example);
    }
    public int deleteRecord(String strPkId){
        return progEstItemMapper.deleteByPrimaryKey(strPkId);
    }

    public void updateRecord(ProgEstItemShow progEstItemShowPara){
        progEstItemShowPara.setProgEst_RecVersion(
                ToolUtil.getIntIgnoreNull(progEstItemShowPara.getProgEst_RecVersion()) + 1);
        progEstItemShowPara.setProgEst_ArchivedFlag("0");
        progEstItemShowPara.setProgEst_UpdatedBy(platformService.getStrLastUpdBy());
        progEstItemShowPara.setProgEst_UpdatedByName(commonMapper.selectOpernameByCreatedBy(platformService.getStrLastUpdBy()));
        progEstItemShowPara.setProgEst_UpdatedTime(platformService.getStrLastUpdTime());
        progEstItemMapper.updateByPrimaryKey(fromShowModelToModel(progEstItemShowPara)) ;
    }

    public void insertRecord(ProgEstItemShow progEstItemShowPara){
        progEstItemShowPara.setProgEst_CreatedBy(platformService.getStrLastUpdBy());
        progEstItemShowPara.setProgEst_CreatedByName(commonMapper.selectOpernameByCreatedBy(platformService.getStrLastUpdBy()));
        progEstItemShowPara.setProgEst_CreatedTime(platformService.getStrLastUpdTime());
        progEstItemShowPara.setProgEst_ArchivedFlag("0");
        progEstItemMapper.insert(fromShowModelToModel(progEstItemShowPara));
    }

    private ProgEstItem fromShowModelToModel
            (ProgEstItemShow progEstItemShowPara){
        ProgEstItem progEstItemTemp =new ProgEstItem();
        progEstItemTemp.setPkid(progEstItemShowPara.getProgEst_Pkid());
        progEstItemTemp.setProgEstInfoPkid(progEstItemShowPara.getProgEst_PorgEstInfoPkid());
        progEstItemTemp.setTkcttItemPkid(progEstItemShowPara.getProgEst_TkcttItemPkid());
        progEstItemTemp.setAddUpQty(progEstItemShowPara.getProgEst_AddUpQty());
        progEstItemTemp.setAddUpAmt(progEstItemShowPara.getProgEst_AddUpAmt());
        progEstItemTemp.setThisStageQty(progEstItemShowPara.getProgEst_ThisStageQty());
        progEstItemTemp.setThisStageAmt(progEstItemShowPara.getProgEst_ThisStageAmt());
        progEstItemTemp.setArchivedFlag(progEstItemShowPara.getTkctt_ArchivedFlag());
        progEstItemTemp.setCreatedBy(progEstItemShowPara.getTkctt_CreatedBy());
        progEstItemTemp.setCreatedTime(progEstItemShowPara.getTkctt_CreatedTime());
        progEstItemTemp.setUpdatedBy(progEstItemShowPara.getTkctt_UpdatedBy());
        progEstItemTemp.setUpdatedTime(progEstItemShowPara.getTkctt_UpdatedTime());
        progEstItemTemp.setRecVersion(progEstItemShowPara.getProgEst_RecVersion());
        return progEstItemTemp;
    }
    public void setFromLastStageApproveDataToThisStageBeginData(ProgEstInfoShow progEstInfoShowPara){
        // 插入新数据之后,就得把上期批准了的数据作为今期数据的起始数据
        /*String strLatestApprovedStageNo= ToolUtil.getStrIgnoreNull(
                esFlowService.getLatestApprovedStageNo(progEstInfoShowPara.getStlType(), progEstInfoShowPara.getStlPkid()));
        if(strLatestApprovedStageNo!=null){
            ProgEstItemExample example = new ProgEstItemExample();
            example.createCriteria()
                    .andTkcttInfoPkidEqualTo(progEstInfoShowPara.getStlPkid())
                    .andStageNoEqualTo(strLatestApprovedStageNo);
            List<ProgEstItem> progEstItemList =
                    progEstItemMapper.selectByExample(example);
            for(ProgEstItem itemUnit: progEstItemList){
                itemUnit.setThisStageQty(null);
                itemUnit.setStageNo(progEstInfoShowPara.getStageNo());
                progEstItemMapper.insert(itemUnit);
            }
        }*/
    }

    public int deleteItemsByInitStlTkcttEng(String strProgEstInfoPkidPara){
        ProgEstItemExample example = new ProgEstItemExample();
        example.createCriteria().andProgEstInfoPkidEqualTo(strProgEstInfoPkidPara);
        return progEstItemMapper.deleteByExample(example);
    }
}
