package epss.service;

import epss.repository.dao.not_mybatis.MyProgWorkqtyItemMapper;
import epss.repository.model.model_show.ProgInfoShow;
import skyline.util.ToolUtil;
import epss.repository.dao.EsItemStlSubcttEngQMapper;
import epss.repository.model.EsItemStlSubcttEngQ;
import epss.repository.model.EsItemStlSubcttEngQExample;
import epss.repository.model.model_show.ProgWorkqtyItemShow;
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
public class ProgWorkqtyItemService {
    @Resource
    private EsItemStlSubcttEngQMapper esItemStlSubcttEngQMapper;
    @Resource
    private MyProgWorkqtyItemMapper myProgWorkqtyItemMapper;
    @Resource
    private EsFlowService esFlowService;

    public List<EsItemStlSubcttEngQ> selectRecordsByExample(EsItemStlSubcttEngQ esItemStlSubcttEngQPara){
        EsItemStlSubcttEngQExample example = new EsItemStlSubcttEngQExample();
        example.createCriteria()
                .andSubcttPkidEqualTo(esItemStlSubcttEngQPara.getSubcttPkid())
                .andSubcttItemPkidEqualTo(esItemStlSubcttEngQPara.getSubcttItemPkid())
                .andPeriodNoEqualTo(esItemStlSubcttEngQPara.getPeriodNo());
        return esItemStlSubcttEngQMapper.selectByExample(example);
    }

    /**
     * 判断记录是否已存在
     *
     * @param progWorkqtyItemShowPara
     * @return
     */
    public List<EsItemStlSubcttEngQ> isExistInDb(ProgWorkqtyItemShow progWorkqtyItemShowPara) {
        EsItemStlSubcttEngQExample example = new EsItemStlSubcttEngQExample();
        example.createCriteria()
                .andSubcttItemPkidEqualTo(progWorkqtyItemShowPara.getSubctt_Pkid())
                .andPeriodNoEqualTo(progWorkqtyItemShowPara.getEngQMng_PeriodNo())
                .andSubcttPkidEqualTo(progWorkqtyItemShowPara.getSubctt_BelongToPkid());
        return esItemStlSubcttEngQMapper.selectByExample(example);
    }

    public List<EsItemStlSubcttEngQ> getProgWorkqtyItemListByPeriod(
            String strSubcttPkid,String strPeriodNoBegin,String strPeriodNoEnd){
        return myProgWorkqtyItemMapper.getProgWorkqtyItemListByPeriod(strSubcttPkid, strPeriodNoBegin, strPeriodNoEnd);
    }

    public List<String> getProgWorkqtyItemPeriodsByPeriod(
            String strSubcttPkid,
            String strPeriodNoBegin,
            String strPeriodNoEnd){
        return myProgWorkqtyItemMapper.getProgWorkqtyItemPeriodsByPeriod(strSubcttPkid, strPeriodNoBegin, strPeriodNoEnd);
    }

    private EsItemStlSubcttEngQ fromModelShowToModel
            (ProgWorkqtyItemShow progWorkqtyItemShowPara){
        EsItemStlSubcttEngQ esItemStlSubcttEngQTemp=new EsItemStlSubcttEngQ();
        esItemStlSubcttEngQTemp.setPkid(progWorkqtyItemShowPara.getEngQMng_Pkid());
        esItemStlSubcttEngQTemp.setPeriodNo(progWorkqtyItemShowPara.getEngQMng_PeriodNo());
        esItemStlSubcttEngQTemp.setSubcttPkid(progWorkqtyItemShowPara.getEngQMng_SubcttPkid());
        esItemStlSubcttEngQTemp.setSubcttItemPkid(progWorkqtyItemShowPara.getEngQMng_SubcttItemPkid());
        esItemStlSubcttEngQTemp.setBeginToCurrentPeriodEQty(progWorkqtyItemShowPara.getEngQMng_BeginToCurrentPeriodEQty());
        esItemStlSubcttEngQTemp.setCurrentPeriodEQty(progWorkqtyItemShowPara.getEngQMng_CurrentPeriodEQty());
        esItemStlSubcttEngQTemp.setDeleteFlag(progWorkqtyItemShowPara.getEngQMng_DeletedFlag());
        esItemStlSubcttEngQTemp.setCreatedBy(progWorkqtyItemShowPara.getEngQMng_CreatedBy());
        esItemStlSubcttEngQTemp.setCreatedDate(progWorkqtyItemShowPara.getEngQMng_CreatedDate());
        esItemStlSubcttEngQTemp.setLastUpdBy(progWorkqtyItemShowPara.getEngQMng_LastUpdBy());
        esItemStlSubcttEngQTemp.setLastUpdDate(progWorkqtyItemShowPara.getEngQMng_LastUpdDate());
        esItemStlSubcttEngQTemp.setModificationNum(progWorkqtyItemShowPara.getEngQMng_ModificationNum());
        return esItemStlSubcttEngQTemp;
    }

    public int deleteRecord(String strPkId){
        return esItemStlSubcttEngQMapper.deleteByPrimaryKey(strPkId);
    }

    public void updateRecord(ProgWorkqtyItemShow progWorkqtyItemShowPara){
        EsItemStlSubcttEngQ esItemStlSubcttEngQTemp=fromModelShowToModel(progWorkqtyItemShowPara);
        esItemStlSubcttEngQTemp.setModificationNum(
                ToolUtil.getIntIgnoreNull(esItemStlSubcttEngQTemp.getModificationNum())+1);
        esItemStlSubcttEngQTemp.setDeleteFlag("0");
        esItemStlSubcttEngQTemp.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        esItemStlSubcttEngQTemp.setLastUpdDate(ToolUtil.getStrLastUpdDate());
        esItemStlSubcttEngQMapper.updateByPrimaryKey(esItemStlSubcttEngQTemp) ;
    }

    public void insertRecord(ProgWorkqtyItemShow progWorkqtyItemShowPara){
        EsItemStlSubcttEngQ esItemStlSubcttEngQTemp=fromModelShowToModel(progWorkqtyItemShowPara);
        esItemStlSubcttEngQTemp.setCreatedBy(ToolUtil.getOperatorManager().getOperatorId());
        esItemStlSubcttEngQTemp.setCreatedDate(ToolUtil.getStrLastUpdDate());
        esItemStlSubcttEngQTemp.setDeleteFlag("0");
        esItemStlSubcttEngQTemp.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        esItemStlSubcttEngQTemp.setLastUpdDate(ToolUtil.getStrLastUpdDate());
        esItemStlSubcttEngQMapper.insert(esItemStlSubcttEngQTemp);
    }

    public void setFromLastStageApproveDataToThisStageBeginData(ProgInfoShow progInfoShowPara){
        // 插入新数据之后,就得把上期批准了的数据作为今期数据的起始数据
        String strLatestApprovedPeriodNo= ToolUtil.getStrIgnoreNull(
                esFlowService.getLatestDoubleCkeckedPeriodNo(progInfoShowPara.getStlType(), progInfoShowPara.getStlPkid()));
        if(strLatestApprovedPeriodNo!=null){
            EsItemStlSubcttEngQExample example = new EsItemStlSubcttEngQExample();
            example.createCriteria()
                    .andSubcttPkidEqualTo(progInfoShowPara.getStlPkid())
                    .andPeriodNoEqualTo(strLatestApprovedPeriodNo);
            List<EsItemStlSubcttEngQ> esItemStlSubcttEngQList =
                    esItemStlSubcttEngQMapper.selectByExample(example);
            for(EsItemStlSubcttEngQ itemUnit:esItemStlSubcttEngQList){
                itemUnit.setCurrentPeriodEQty(null);
                itemUnit.setPeriodNo(progInfoShowPara.getPeriodNo());
                esItemStlSubcttEngQMapper.insert(itemUnit);
            }
        }
    }

    public int deleteItemsByInitStlSubcttEng(String strSubcttPkid,String strPeriodNo){
        EsItemStlSubcttEngQExample example = new EsItemStlSubcttEngQExample();
        example.createCriteria().
                andSubcttPkidEqualTo(strSubcttPkid).
                andPeriodNoEqualTo(strPeriodNo);
        return esItemStlSubcttEngQMapper.deleteByExample(example);
    }
}
