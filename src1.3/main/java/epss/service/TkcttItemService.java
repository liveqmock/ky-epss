package epss.service;

import epss.common.utils.ToolUtil;
import epss.repository.dao.TkcttItemMapper;
import epss.repository.dao.notMyBits.MyTkcttItemMapper;
import epss.repository.model.TkcttItem;
import epss.repository.model.TkcttItemExample;
import epss.repository.model.model_show.TkcttItemShow;
import org.springframework.stereotype.Service;
import platform.service.PlatformService;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vincent
 * Date: 13-2-19
 * Time: 下午9:44
 * To change this template use File | Settings | File Templates.
 */
@Service
public class TkcttItemService {
    @Resource
    private TkcttItemMapper tkcttItemMapper;
    @Resource
    private MyTkcttItemMapper myTkcttItemMapper;
    @Resource
    private PlatformService platformService;

    public Integer getMaxOrderidInEsItemHieRelapList(String strBelongToPkid,
                                                     String strParentPkid,
                                                     Integer intGrade) {
        TkcttItemExample example = new TkcttItemExample();
        example.createCriteria()
                .andTkcttInfoPkidEqualTo(strBelongToPkid)
                .andParentPkidEqualTo(strParentPkid)
                .andGradeEqualTo(intGrade);
        example.setOrderByClause("ORDERID DESC");
        List<TkcttItem> tkcttItemList = tkcttItemMapper.selectByExample(example);
        if (tkcttItemList.size() == 0) {
            return 0;
        } else {
            return tkcttItemList.get(0).getOrderid();
        }
    }

    public List<TkcttItem> getTkcttItemListByTkcttInfoPkid(String strTkcttInfoPkidPara) {
        TkcttItemExample example = new TkcttItemExample();
        example.createCriteria().andTkcttInfoPkidEqualTo(strTkcttInfoPkidPara);
        example.setOrderByClause("GRADE ASC,ORDERID ASC");
        return tkcttItemMapper.selectByExample(example);
    }

    public void setOrderidPlusOneByInfoPkidAndParentPkidAndGrade(
            String strTkcttInfoPkid,
            String strParentPkid,
            Integer intGrade,
            Integer intOrderid) {
        myTkcttItemMapper.setOrderidPlusOneByInfoPkidAndParentPkidAndGrade(
                strTkcttInfoPkid,
                strParentPkid,
                intGrade,
                intOrderid);
    }

    public void setOrderidSubOneByInfoPkidAndParentPkidAndGrade(String strTkcttInfoPkidPara,
                                                                String strParentPkidPara,
                                                                Integer intGradePara) {
        myTkcttItemMapper.setOrderidSubOneByInfoPkidAndParentPkidAndGrade(
                strTkcttInfoPkidPara,
                strParentPkidPara,
                intGradePara);
    }

    /**
     * 判断记录是否已存在
     *
     * @param tkcttItem
     * @return
     */
    public boolean isExistSameIdInDb(TkcttItem tkcttItem) {
        TkcttItemExample example = new TkcttItemExample();
        TkcttItemExample.Criteria criteria = example.createCriteria();
        criteria
                .andTkcttInfoPkidEqualTo(tkcttItem.getTkcttInfoPkid())
                .andParentPkidEqualTo(tkcttItem.getParentPkid())
                .andGradeEqualTo(tkcttItem.getGrade())
                .andOrderidEqualTo(tkcttItem.getOrderid());
        return tkcttItemMapper.countByExample(example) >= 1;
    }

    public boolean isExistSameRecordInDb(TkcttItem tkcttItem) {
        TkcttItemExample example = new TkcttItemExample();
        TkcttItemExample.Criteria criteria = example.createCriteria();
        criteria
                .andTkcttInfoPkidEqualTo(tkcttItem.getTkcttInfoPkid())
                .andParentPkidEqualTo(tkcttItem.getParentPkid())
                .andGradeEqualTo(tkcttItem.getGrade())
                .andOrderidEqualTo(tkcttItem.getOrderid());

        if (tkcttItem.getName() == null) {
            criteria.andNameIsNull();
        } else {
            criteria.andNameEqualTo(tkcttItem.getName());
        }
        return tkcttItemMapper.countByExample(example) >= 1;
    }

    public void insertRecord(TkcttItemShow tkcttItemShowPara) {
        TkcttItem tkcttItemTemp = fromShowModelToModel(tkcttItemShowPara);
        tkcttItemTemp.setArchivedFlag("0");
        tkcttItemTemp.setOriginFlag("0");
        tkcttItemTemp.setCreatedBy(platformService.getStrLastUpdBy());
        tkcttItemTemp.setCreatedTime(platformService.getStrLastUpdTime());
        tkcttItemTemp.setUpdatedBy(platformService.getStrLastUpdBy());
        tkcttItemTemp.setUpdatedTime(platformService.getStrLastUpdTime());
        tkcttItemMapper.insertSelective(tkcttItemTemp);
    }

    /*总包合同到层级关系*/
    public TkcttItem fromShowModelToModel(TkcttItemShow tkcttItemShowPara) {
        TkcttItem cttItemTemp = new TkcttItem();
        cttItemTemp.setUnit(tkcttItemShowPara.getUnit());
        cttItemTemp.setPkid(tkcttItemShowPara.getPkid());
        cttItemTemp.setTkcttInfoPkid(tkcttItemShowPara.getTkcttInfoPkid());
        cttItemTemp.setParentPkid(tkcttItemShowPara.getParentPkid());
        cttItemTemp.setGrade(tkcttItemShowPara.getGrade());
        cttItemTemp.setOrderid(tkcttItemShowPara.getOrderid());
        cttItemTemp.setName(tkcttItemShowPara.getName());
        cttItemTemp.setUnitPrice(tkcttItemShowPara.getUnitPrice());
        cttItemTemp.setQty(tkcttItemShowPara.getQty());
        cttItemTemp.setAmt(tkcttItemShowPara.getAmt());
        cttItemTemp.setArchivedFlag(tkcttItemShowPara.getArchivedFlag());
        cttItemTemp.setOriginFlag(tkcttItemShowPara.getOriginFlag());
        cttItemTemp.setCreatedTime(tkcttItemShowPara.getCreatedTime());
        cttItemTemp.setCreatedBy(tkcttItemShowPara.getCreatedBy());
        cttItemTemp.setUpdatedTime(tkcttItemShowPara.getUpdatedTime());
        cttItemTemp.setUpdatedBy(tkcttItemShowPara.getUpdatedBy());
        cttItemTemp.setRemark(tkcttItemShowPara.getRemark());
        cttItemTemp.setRecVersion(tkcttItemShowPara.getRecVersion());
        return cttItemTemp;
    }

    /*层级关系到总包合同*/
    private TkcttItemShow getitemOfEsItemHieRelapFromEsItemHieRelap(TkcttItem subcttItemPara) {
        TkcttItemShow cttItemShowTemp = new TkcttItemShow();
        cttItemShowTemp.setPkid(subcttItemPara.getPkid());
        cttItemShowTemp.setTkcttInfoPkid(subcttItemPara.getTkcttInfoPkid());
        cttItemShowTemp.setParentPkid(subcttItemPara.getParentPkid());
        cttItemShowTemp.setGrade(subcttItemPara.getGrade());
        cttItemShowTemp.setOrderid(subcttItemPara.getOrderid());
        cttItemShowTemp.setName(subcttItemPara.getName());
        cttItemShowTemp.setRemark(subcttItemPara.getRemark());
        return cttItemShowTemp;
    }

    public void updateRecord(TkcttItemShow tkcttItemShowPara) {
        TkcttItem tkcttItemTemp = fromShowModelToModel(tkcttItemShowPara);
        tkcttItemTemp.setRecVersion(
                ToolUtil.getIntIgnoreNull(tkcttItemTemp.getRecVersion()) + 1);
        tkcttItemTemp.setArchivedFlag("0");
        tkcttItemTemp.setOriginFlag("0");
        tkcttItemTemp.setUpdatedBy(platformService.getStrLastUpdBy());
        tkcttItemTemp.setUpdatedTime(platformService.getStrLastUpdTime());
        tkcttItemMapper.updateByPrimaryKey(tkcttItemTemp);
    }

    public int deleteRecord(String strPkId) {
        return tkcttItemMapper.deleteByPrimaryKey(strPkId);
    }

    public TkcttItem getEsItemHieRelapByPkId(String strPkId) {
        return tkcttItemMapper.selectByPrimaryKey(strPkId);
    }
}
