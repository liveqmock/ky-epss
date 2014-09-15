package epss.service;

import epss.repository.model.CttItem;
import epss.repository.model.CttItemExample;
import skyline.util.ToolUtil;
import epss.repository.dao.CttItemMapper;
import epss.repository.dao.not_mybatis.MyCttItemMapper;
import epss.repository.model.model_show.CttInfoShow;
import epss.repository.model.model_show.CttItemShow;
import org.springframework.stereotype.Service;
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
public class CttItemService {
    @Resource
    private CttItemMapper cttItemMapper;
    @Resource
    private MyCttItemMapper myCttItemMapper;

    public Integer getMaxOrderidInEsCttItemList(String strBelongToType,
                                                 String strBelongToPkid,
                                                 String strParentPkid,
                                                 Integer intGrade){
        CttItemExample example = new CttItemExample();
        example.createCriteria()
                .andBelongToTypeEqualTo(strBelongToType)
                .andBelongToPkidEqualTo(strBelongToPkid)
                .andParentPkidEqualTo(strParentPkid)
                .andGradeEqualTo(intGrade);
        example .setOrderByClause("ORDERID DESC") ;
        List<CttItem> cttItemList = cttItemMapper.selectByExample(example);
        if(cttItemList.size() ==0){
            return 0;
        }
        else{
            return  cttItemList.get(0).getOrderid();
        }
    }
    public List<CttItem> getEsItemList(String strBelongToType,String strItemBelongToPkid){
        CttItemExample example = new CttItemExample();
        example.createCriteria().andBelongToTypeEqualTo(strBelongToType)
                .andBelongToPkidEqualTo(strItemBelongToPkid);
        example .setOrderByClause("GRADE ASC,ORDERID ASC") ;
        return cttItemMapper.selectByExample(example);
        /*return commonMapper.selEsItemHieRelapListByTypeAndId(strBelongToType,strItemBelongToId);*/
    }

    /**
     * 判断记录是否已存在
     *
     * @param   strPkId
     * @return
     */
    public CttItem getEsCttItemByPkId(String strPkId){
        return cttItemMapper.selectByPrimaryKey(strPkId) ;
    }
    public boolean isExistSameNameNodeInDb(CttItem cttItem) {
        CttItemExample example = new CttItemExample();
        CttItemExample.Criteria criteria = example.createCriteria();
        criteria
                .andBelongToTypeEqualTo(cttItem.getBelongToType())
                .andBelongToPkidEqualTo(cttItem.getBelongToPkid())
                .andParentPkidEqualTo(cttItem.getParentPkid())
                .andGradeEqualTo(cttItem.getGrade());
        if(cttItem.getName()==null){
            criteria.andNameIsNull();
        }
        else{
            criteria.andNameEqualTo(cttItem.getName());
        }
        if(cttItem.getSpareField()==null){
            criteria.andSpareFieldIsNull();
        }
        else{
            criteria.andSpareFieldEqualTo(cttItem.getSpareField());
        }
        return cttItemMapper.countByExample(example) >= 1;
    }
    public boolean isExistSameRecordInDb(CttItem cttItem) {
        CttItemExample example = new CttItemExample();
        CttItemExample.Criteria criteria = example.createCriteria();
        criteria
                .andBelongToTypeEqualTo(cttItem.getBelongToType())
                .andBelongToPkidEqualTo(cttItem.getBelongToPkid())
                .andParentPkidEqualTo(cttItem.getParentPkid())
                .andGradeEqualTo(cttItem.getGrade())
                .andOrderidEqualTo(cttItem.getOrderid());

        if(cttItem.getName()==null){
            criteria.andNameIsNull();
        }
        else{
            criteria.andNameEqualTo(cttItem.getName());
        }
        if(cttItem.getSpareField()==null){
            criteria.andSpareFieldIsNull();
        }
        else{
            criteria.andSpareFieldEqualTo(cttItem.getSpareField());
        }
        return cttItemMapper.countByExample(example) >= 1;
    }

    public void insertRecord(CttItemShow cttItemShowPara) {
        String strOperatorIdTemp=ToolUtil.getOperatorManager().getOperatorId();
        String strLastUpdTimeTemp=ToolUtil.getStrLastUpdTime();
        CttItem cttItemTemp =fromModelShowToModel(cttItemShowPara);
        cttItemTemp.setArchivedFlag("0");
        cttItemTemp.setOriginFlag("0");
        cttItemTemp.setCreatedBy(strOperatorIdTemp);
        cttItemTemp.setCreatedDate(strLastUpdTimeTemp);
        cttItemTemp.setLastUpdBy(strOperatorIdTemp);
        cttItemTemp.setLastUpdDate(strLastUpdTimeTemp);
        cttItemMapper.insertSelective(cttItemTemp);
    }
    public void insertRecord(CttItem cttItemPara){
        String strOperatorIdTemp=ToolUtil.getOperatorManager().getOperatorId();
        String strLastUpdTimeTemp=ToolUtil.getStrLastUpdTime();
        cttItemPara.setArchivedFlag("0");
        cttItemPara.setOriginFlag("0");
        cttItemPara.setCreatedBy(strOperatorIdTemp);
        cttItemPara.setCreatedDate(strLastUpdTimeTemp);
        cttItemPara.setLastUpdBy(strOperatorIdTemp);
        cttItemPara.setLastUpdDate(strLastUpdTimeTemp);
        cttItemMapper.insert(cttItemPara);
    }

    /*总包合同到层级关系*/
    public CttItem fromModelShowToModel(CttItemShow cttItemShowPara){
        CttItem cttItemTemp =new CttItem() ;
        cttItemTemp.setUnit(cttItemShowPara.getUnit());
        cttItemTemp.setPkid(cttItemShowPara.getPkid()) ;
        cttItemTemp.setBelongToType(cttItemShowPara.getBelongToType()) ;
        cttItemTemp.setBelongToPkid(cttItemShowPara.getBelongToPkid()) ;
        cttItemTemp.setParentPkid(cttItemShowPara.getParentPkid()) ;
        cttItemTemp.setGrade(cttItemShowPara.getGrade()) ;
        cttItemTemp.setOrderid(cttItemShowPara.getOrderid()) ;
        cttItemTemp.setName(cttItemShowPara.getName()) ;
        cttItemTemp.setContractUnitPrice(cttItemShowPara.getContractUnitPrice());
        cttItemTemp.setContractQuantity(cttItemShowPara.getContractQuantity());
        cttItemTemp.setSignPartAPrice(cttItemShowPara.getSignPartAPrice());
        cttItemTemp.setContractAmount(cttItemShowPara.getContractAmount());
        cttItemTemp.setArchivedFlag(cttItemShowPara.getDeletedFlag());
        cttItemTemp.setOriginFlag(cttItemShowPara.getOriginFlag());
        cttItemTemp.setCreatedDate(cttItemShowPara.getCreatedDate());
        cttItemTemp.setCreatedBy(cttItemShowPara.getCreatedBy());
        cttItemTemp.setLastUpdDate(cttItemShowPara.getLastUpdDate());
        cttItemTemp.setLastUpdBy(cttItemShowPara.getLastUpdBy());
        cttItemTemp.setNote(cttItemShowPara.getNote()) ;
        cttItemTemp.setCorrespondingPkid(cttItemShowPara.getCorrespondingPkid());
        cttItemTemp.setModificationNum(cttItemShowPara.getModificationNum());
        return cttItemTemp;
    }
    /*层级关系到总包合同*/
    private CttItemShow fromModelToModelShow(CttItem cttItemPara){
        CttItemShow cttItemShowTemp =new CttItemShow() ;
        cttItemShowTemp.setPkid(cttItemPara.getPkid()) ;
        cttItemShowTemp.setBelongToType(cttItemPara.getBelongToType()) ;
        cttItemShowTemp.setBelongToPkid(cttItemPara.getBelongToPkid()) ;
        cttItemShowTemp.setParentPkid(cttItemPara.getParentPkid()) ;
        cttItemShowTemp.setGrade(cttItemPara.getGrade()) ;
        cttItemShowTemp.setOrderid(cttItemPara.getOrderid()) ;
        cttItemShowTemp.setName(cttItemPara.getName()) ;
        cttItemShowTemp.setNote(cttItemPara.getNote()) ;
        cttItemShowTemp.setCorrespondingPkid(cttItemPara.getCorrespondingPkid());
        return cttItemShowTemp;
    }

    public void updateRecord(CttItemShow cttItemShowPara) {
        CttItem cttItemTemp =fromModelShowToModel(cttItemShowPara);
        cttItemTemp.setModificationNum(
                ToolUtil.getIntIgnoreNull(cttItemTemp.getModificationNum())+1);
        cttItemTemp.setArchivedFlag("0");
        cttItemTemp.setOriginFlag("0");
        cttItemTemp.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        cttItemTemp.setLastUpdDate(ToolUtil.getStrLastUpdDate());
        cttItemMapper.updateByPrimaryKey(cttItemTemp) ;
    }

    public int deleteRecord(String strPkId){
        return cttItemMapper.deleteByPrimaryKey(strPkId);
    }
    public int deleteRecord(CttInfoShow cttInfoShowPara){
        CttItemExample example = new CttItemExample();
        example.createCriteria()
                .andBelongToTypeEqualTo(cttInfoShowPara.getCttType())
                .andBelongToPkidEqualTo(cttInfoShowPara.getPkid());
        return cttItemMapper.deleteByExample(example);
    }

    public void setAfterThisOrderidPlusOneByNode(String strBelongToType,
                                                   String strBelongToPkid,
                                                   String strParentPkid,
                                                   Integer intGrade,
                                                   Integer intOrderid){
        myCttItemMapper.setAfterThisOrderidPlusOneByNode(strBelongToType,
                strBelongToPkid,
                strParentPkid,
                intGrade,
                intOrderid);
    }

    public void setAfterThisOrderidSubOneByNode(String strBelongToType,
                                                String strBelongToPkid,
                                                String strParentPkid,
                                                Integer intGrade,
                                                Integer intOrderid){
        myCttItemMapper.setAfterThisOrderidSubOneByNode(strBelongToType,
                strBelongToPkid,
                strParentPkid,
                intGrade,
                intOrderid);
    }
}
