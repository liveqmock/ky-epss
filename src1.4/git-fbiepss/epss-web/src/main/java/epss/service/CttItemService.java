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
        String strOperatorIdTemp=ToolUtil.getOperatorManager().getOperator().getPkid();
        String strLastUpdTimeTemp=ToolUtil.getStrLastUpdTime();
        CttItem cttItemTemp =fromModelShowToModel(cttItemShowPara);
        cttItemTemp.setArchivedFlag("0");
        cttItemTemp.setOriginFlag("0");
        cttItemTemp.setCreatedBy(strOperatorIdTemp);
        cttItemTemp.setCreatedTime(strLastUpdTimeTemp);
        cttItemTemp.setLastUpdBy(strOperatorIdTemp);
        cttItemTemp.setLastUpdTime(strLastUpdTimeTemp);
        cttItemMapper.insertSelective(cttItemTemp);
    }
    public void insertRecord(CttItem cttItemPara){
        String strOperatorIdTemp=ToolUtil.getOperatorManager().getOperator().getPkid();
        String strLastUpdTimeTemp=ToolUtil.getStrLastUpdTime();
        cttItemPara.setArchivedFlag("0");
        cttItemPara.setOriginFlag("0");
        cttItemPara.setCreatedBy(strOperatorIdTemp);
        cttItemPara.setCreatedTime(strLastUpdTimeTemp);
        cttItemPara.setLastUpdBy(strOperatorIdTemp);
        cttItemPara.setLastUpdTime(strLastUpdTimeTemp);
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
        cttItemTemp.setArchivedFlag(cttItemShowPara.getArchivedFlag());
        cttItemTemp.setOriginFlag(cttItemShowPara.getOriginFlag());
        cttItemTemp.setCreatedTime(cttItemShowPara.getCreatedTime());
        cttItemTemp.setCreatedBy(cttItemShowPara.getCreatedBy());
        cttItemTemp.setLastUpdTime(cttItemShowPara.getLastUpdTime());
        cttItemTemp.setLastUpdBy(cttItemShowPara.getLastUpdBy());
        cttItemTemp.setRemark(cttItemShowPara.getRemark());
        cttItemTemp.setCorrespondingPkid(cttItemShowPara.getCorrespondingPkid());
        cttItemTemp.setRecVersion(cttItemShowPara.getRecVersion());
        cttItemTemp.setSpareField(cttItemShowPara.getSpareField());
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
        cttItemShowTemp.setRemark(cttItemPara.getRemark()) ;
        cttItemShowTemp.setCorrespondingPkid(cttItemPara.getCorrespondingPkid());
        return cttItemShowTemp;
    }

    public void updateRecord(CttItemShow cttItemShowPara) {
        CttItem cttItemTemp =fromModelShowToModel(cttItemShowPara);
        cttItemTemp.setRecVersion(
                ToolUtil.getIntIgnoreNull(cttItemTemp.getRecVersion())+1);
        cttItemTemp.setArchivedFlag("0");
        cttItemTemp.setOriginFlag("0");
        cttItemTemp.setLastUpdBy(ToolUtil.getOperatorManager().getOperator().getPkid());
        cttItemTemp.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        if(ToolUtil.getStrIgnoreNull(cttItemTemp.getSpareField()).length()==0) {
            cttItemTemp.setContractAmount(cttItemShowPara.getContractUnitPrice().multiply(cttItemShowPara.getContractQuantity()));
        }
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

    public void setAfterThisOrderidPlusOneByNode(CttItemShow cttItemShowPara){
        myCttItemMapper.setAfterThisOrderidPlusOneByNode(cttItemShowPara.getBelongToType(),
                cttItemShowPara.getBelongToPkid(),
                cttItemShowPara.getParentPkid(),
                cttItemShowPara.getGrade(),
                cttItemShowPara.getOrderid());
        if(ToolUtil.getStrIgnoreNull(cttItemShowPara.getSpareField()).length()==0) {
            cttItemShowPara.setContractAmount(cttItemShowPara.getContractUnitPrice().multiply(cttItemShowPara.getContractQuantity()));
        }
        insertRecord(cttItemShowPara);
    }

    public void setAfterThisOrderidSubOneByNode(CttItemShow cttItemShowPara){
        deleteRecord(cttItemShowPara.getPkid());
        myCttItemMapper.setAfterThisOrderidSubOneByNode(
                cttItemShowPara.getBelongToType(),
                cttItemShowPara.getBelongToPkid(),
                cttItemShowPara.getParentPkid(),
                cttItemShowPara.getGrade(),
                cttItemShowPara.getOrderid());
    }
}
