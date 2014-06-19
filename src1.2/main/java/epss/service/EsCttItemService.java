package epss.service;

import epss.common.utils.ToolUtil;
import epss.repository.dao.EsCttItemMapper;
import epss.repository.dao.common.CommonMapper;
import epss.repository.model.EsCttItem;
import epss.repository.model.EsCttItemExample;
import epss.repository.model.model_show.CttItemShow;
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
public class EsCttItemService {
    @Resource
    private EsCttItemMapper esCttItemMapper;
    @Resource
    private CommonMapper commonMapper;
    @Resource
    private PlatformService platformService;

    public Integer getMaxOrderidInEsItemHieRelapList(String strBelongToType,
                                                      String strBelongToPkid,
                                                      String strParentPkid,
                                                      Integer intGrade){
        EsCttItemExample example = new EsCttItemExample();
        example.createCriteria()
                .andBelongToTypeEqualTo(strBelongToType)
                .andBelongToPkidEqualTo(strBelongToPkid)
                .andParentPkidEqualTo(strParentPkid)
                .andGradeEqualTo(intGrade);
        example .setOrderByClause("ORDERID DESC") ;
        List<EsCttItem> esCttItemList = esCttItemMapper.selectByExample(example);
        if(esCttItemList.size() ==0){
            return 0;
        }
        else{
            return  esCttItemList.get(0).getOrderid();
        }
    }
    public List<EsCttItem> getEsItemHieRelapListByTypeAndPkid(String strBelongToType,String strItemBelongToPkid){
        EsCttItemExample example = new EsCttItemExample();
        example.createCriteria().andBelongToTypeEqualTo(strBelongToType)
                .andBelongToPkidEqualTo(strItemBelongToPkid);
        example .setOrderByClause("GRADE ASC,ORDERID ASC") ;
        return esCttItemMapper.selectByExample(example);
        /*return commonMapper.selEsItemHieRelapListByTypeAndId(strBelongToType,strItemBelongToId);*/
    }

    public void setAfterThisOrderidPlusOneByTypeAndIdAndParentPkidAndGrade(String strBelongToType,
                                                                             String strBelongToPkid,
                                                                             String strParentPkid,
                                                                             Integer intGrade,
                                                                             Integer intOrderid){
        commonMapper.setAfterThisOrderidPlusOneByTypeAndIdAndParentPkidAndGrade(strBelongToType,
                                                                                  strBelongToPkid,
                                                                                  strParentPkid,
                                                                                  intGrade,
                                                                                  intOrderid);
    }

    public void setAfterThisOrderidSubOneByTypeAndIdAndParentPkidAndGrade(String strBelongToType,
                                                                            String strBelongToPkid,
                                                                            String strParentPkid,
                                                                            Integer intGrade,
                                                                            Integer intOrderid){
        commonMapper.setAfterThisOrderidSubOneByTypeAndIdAndParentPkidAndGrade(strBelongToType,
                                                                                 strBelongToPkid,
                                                                                 strParentPkid,
                                                                                 intGrade,
                                                                                 intOrderid);
    }

    /**
     * 判断记录是否已存在
     *
     * @param   esCttItem
     * @return
     */
    public boolean isExistSameIdInDb(EsCttItem esCttItem) {
        EsCttItemExample example = new EsCttItemExample();
        EsCttItemExample.Criteria criteria = example.createCriteria();
        criteria
                .andBelongToTypeEqualTo(esCttItem.getBelongToType())
                .andBelongToPkidEqualTo(esCttItem.getBelongToPkid())
                .andParentPkidEqualTo(esCttItem.getParentPkid())
                .andGradeEqualTo(esCttItem.getGrade())
                .andOrderidEqualTo(esCttItem.getOrderid());
        return esCttItemMapper.countByExample(example) >= 1;
    }
    public boolean isExistSameRecordInDb(EsCttItem esCttItem) {
        EsCttItemExample example = new EsCttItemExample();
        EsCttItemExample.Criteria criteria = example.createCriteria();
        criteria
                .andBelongToTypeEqualTo(esCttItem.getBelongToType())
                .andBelongToPkidEqualTo(esCttItem.getBelongToPkid())
                .andParentPkidEqualTo(esCttItem.getParentPkid())
                .andGradeEqualTo(esCttItem.getGrade())
                .andOrderidEqualTo(esCttItem.getOrderid());

        if(esCttItem.getName()==null){
            criteria.andNameIsNull();
        }
        else{
            criteria.andNameEqualTo(esCttItem.getName());
        }
        return esCttItemMapper.countByExample(example) >= 1;
    }

    public void insertRecord(CttItemShow cttItemShowPara) {
        EsCttItem esCttItemTemp=fromConstructToModel(cttItemShowPara);
        esCttItemTemp.setDeletedFlag("0");
        esCttItemTemp.setOriginFlag("0");
        esCttItemTemp.setCreatedBy(platformService.getStrLastUpdBy());
        esCttItemTemp.setCreatedDate(platformService.getStrLastUpdDate());
        esCttItemTemp.setLastUpdBy(platformService.getStrLastUpdBy());
        esCttItemTemp.setLastUpdDate(platformService.getStrLastUpdDate());
        esCttItemMapper.insertSelective(esCttItemTemp);
    }

    /*总包合同到层级关系*/
    public EsCttItem fromConstructToModel(CttItemShow cttItemShowPara){
        EsCttItem esCttItemTemp =new EsCttItem() ;
        esCttItemTemp.setUnit(cttItemShowPara.getUnit());
        esCttItemTemp.setPkid(cttItemShowPara.getPkid()) ;
        esCttItemTemp.setBelongToType(cttItemShowPara.getBelongToType()) ;
        esCttItemTemp.setBelongToPkid(cttItemShowPara.getBelongToPkid()) ;
        esCttItemTemp.setParentPkid(cttItemShowPara.getParentPkid()) ;
        esCttItemTemp.setGrade(cttItemShowPara.getGrade()) ;
        esCttItemTemp.setOrderid(cttItemShowPara.getOrderid()) ;
        esCttItemTemp.setName(cttItemShowPara.getName()) ;
        esCttItemTemp.setContractUnitPrice(cttItemShowPara.getContractUnitPrice());
        esCttItemTemp.setContractQuantity(cttItemShowPara.getContractQuantity());
        esCttItemTemp.setSignPartAPrice(cttItemShowPara.getSignPartAPrice());
        esCttItemTemp.setContractAmount(cttItemShowPara.getContractAmount());
        esCttItemTemp.setDeletedFlag(cttItemShowPara.getDeletedFlag());
        esCttItemTemp.setOriginFlag(cttItemShowPara.getOriginFlag());
        esCttItemTemp.setCreatedDate(cttItemShowPara.getCreatedDate());
        esCttItemTemp.setCreatedBy(cttItemShowPara.getCreatedBy());
        esCttItemTemp.setLastUpdDate(cttItemShowPara.getLastUpdDate());
        esCttItemTemp.setLastUpdBy(cttItemShowPara.getLastUpdBy());
        esCttItemTemp.setNote(cttItemShowPara.getNote()) ;
        esCttItemTemp.setCorrespondingPkid(cttItemShowPara.getCorrespondingPkid());
        esCttItemTemp.setModificationNum(cttItemShowPara.getModificationNum());
        return esCttItemTemp;
    }
    /*层级关系到总包合同*/
    private CttItemShow getitemOfEsItemHieRelapFromEsItemHieRelap(EsCttItem esCttItemPara){
        CttItemShow cttItemShowTemp =new CttItemShow() ;
        cttItemShowTemp.setPkid(esCttItemPara.getPkid()) ;
        cttItemShowTemp.setBelongToType(esCttItemPara.getBelongToType()) ;
        cttItemShowTemp.setBelongToPkid(esCttItemPara.getBelongToPkid()) ;
        cttItemShowTemp.setParentPkid(esCttItemPara.getParentPkid()) ;
        cttItemShowTemp.setGrade(esCttItemPara.getGrade()) ;
        cttItemShowTemp.setOrderid(esCttItemPara.getOrderid()) ;
        cttItemShowTemp.setName(esCttItemPara.getName()) ;
        cttItemShowTemp.setNote(esCttItemPara.getNote()) ;
        cttItemShowTemp.setCorrespondingPkid(esCttItemPara.getCorrespondingPkid());
        return cttItemShowTemp;
    }

    public void updateRecord(CttItemShow cttItemShowPara) {
        EsCttItem esCttItemTemp=fromConstructToModel(cttItemShowPara);
        esCttItemTemp.setModificationNum(
                ToolUtil.getIntIgnoreNull(esCttItemTemp.getModificationNum())+1);
        esCttItemTemp.setDeletedFlag("0");
        esCttItemTemp.setOriginFlag("0");
        esCttItemTemp.setLastUpdBy(platformService.getStrLastUpdBy());
        esCttItemTemp.setLastUpdDate(platformService.getStrLastUpdDate());
        esCttItemMapper.updateByPrimaryKey(esCttItemTemp) ;
    }

    public int deleteRecord(String strPkId){
        return esCttItemMapper.deleteByPrimaryKey(strPkId);
    }

    public EsCttItem getEsItemHieRelapByPkId(String strPkId){
        return esCttItemMapper.selectByPrimaryKey(strPkId) ;
    }
}
