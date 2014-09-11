package epss.service;

import epss.common.enums.ESEnum;
import epss.repository.dao.EsCttItemMapper;
import epss.repository.model.*;
import org.springframework.transaction.annotation.Transactional;
import skyline.util.MessageUtil;
import skyline.util.ToolUtil;
import epss.repository.dao.OperResMapper;
import epss.repository.dao.not_mybatis.MyOperResMapper;
import epss.repository.model.model_show.CttInfoShow;
import epss.repository.model.model_show.DeptOperShow;
import epss.repository.model.model_show.OperResShow;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * Created by XIANGYANG on 2014/7/27.
 */
@Service
public class OperResService {
    @Resource
    private MyOperResMapper myOperResMapper;
    @Resource
    private OperResMapper operResMapper;
    @Resource
    private CttInfoService cttInfoService;
    @Resource
    private CttItemService cttItemService;
    @Resource
    private ProgStlInfoService progStlInfoService;

    public List<OperResShow> selectOperaResRecordsByModelShow(OperResShow operResShowPara){
        return myOperResMapper.selectOperaResRecordsByModelShow(operResShowPara);
    }
    public List<OperResShow> selectOperaResRecordsByModel(OperRes operResPara){
        return myOperResMapper.selectOperaResRecordsByModelShow(fromModelToModelShow(operResPara));
    }
    public List<OperResShow> getInfoListByOperPkid(String strInfoTypePara,String strOperPkidPara){
        return myOperResMapper.getInfoListByOperPkid(strInfoTypePara,strOperPkidPara);
    }
    public List<OperResShow> getInfoListByOperFlowPkid(String strInfoTypePara,String strFlowStatusPara){
        String strOperatorIdTemp=ToolUtil.getOperatorManager().getOperatorId();
        return myOperResMapper.getInfoListByOperFlowPkid(strInfoTypePara,strFlowStatusPara,strOperatorIdTemp);
    }
	public List<CttInfoShow> selectRecordsFromCtt(String parentPkidPara){
        return  myOperResMapper.selectRecordsFromCtt(parentPkidPara);
    }
    public OperRes getOperResByPkid(String strOperResPkidPara){
        return  operResMapper.selectByPrimaryKey(strOperResPkidPara);
    }
    public void insertRecord(OperResShow operResShowPara){
        String strOperatorIdTemp=ToolUtil.getOperatorManager().getOperatorId();
        String strLastUpdTime=ToolUtil.getStrLastUpdTime();
        operResShowPara.setCreatedBy(strOperatorIdTemp);
        operResShowPara.setCreatedTime(strLastUpdTime);
        operResShowPara.setLastUpdBy(strOperatorIdTemp);
        operResShowPara.setLastUpdTime(strLastUpdTime);
        operResMapper.insert(fromOperShowToModel(operResShowPara));
    }
    public void insertRecord(OperRes operResPara){
        String strOperatorIdTemp=ToolUtil.getOperatorManager().getOperatorId();
        String strLastUpdTime=ToolUtil.getStrLastUpdTime();
        operResPara.setCreatedBy(strOperatorIdTemp);
        operResPara.setCreatedTime(strLastUpdTime);
        operResPara.setLastUpdBy(strOperatorIdTemp);
        operResPara.setLastUpdTime(strLastUpdTime);
        operResMapper.insertSelective(operResPara);
    }
    public void updateRecord(OperRes operResPara){
        String strOperatorIdTemp=ToolUtil.getOperatorManager().getOperatorId();
        String strLastUpdTime=ToolUtil.getStrLastUpdTime();
        operResPara.setLastUpdBy(strOperatorIdTemp);
        operResPara.setLastUpdTime(strLastUpdTime);
        operResMapper.updateByPrimaryKey(operResPara);
    }
    public void deleteRecord(OperRes operResPara){
        OperResExample example =new OperResExample();
        OperResExample.Criteria criteria = example.createCriteria();
        if(!ToolUtil.getStrIgnoreNull(operResPara.getInfoType()).equals("")){
            criteria.andInfoTypeEqualTo(operResPara.getInfoType());
        }
        if(!ToolUtil.getStrIgnoreNull(operResPara.getFlowStatus()).equals("")){
            criteria.andFlowStatusEqualTo(operResPara.getFlowStatus());
        }
        criteria.andInfoPkidEqualTo(operResPara.getInfoPkid());
        operResMapper.deleteByExample(example);
    }
    public EsInitStl initStlData(String strStlTypePara,CttInfoShow cttInfoShowPara){
        EsInitStl esInitStl=new EsInitStl();
        esInitStl.setStlType(strStlTypePara);
        esInitStl.setStlPkid(cttInfoShowPara.getPkid());
        esInitStl.setPeriodNo("NULL");
        return esInitStl;
    }
    @Transactional
    public String deleteResRecord(CttInfoShow cttInfoShowPara){
        if (ESEnum.ITEMTYPE0.getCode().equals(cttInfoShowPara.getCttType())
                ||ESEnum.ITEMTYPE1.getCode().equals(cttInfoShowPara.getCttType())
                ||ESEnum.ITEMTYPE2.getCode().equals(cttInfoShowPara.getCttType())){
            if (cttItemService.getEsItemList(cttInfoShowPara.getCttType(),cttInfoShowPara.getPkid()).size()>0
                    ||cttInfoService.getCttInfoByPkId(cttInfoShowPara.getPkid()).getFlowStatus()!=null) {
                return "数据已被引用，不可删除！";
            }else {
                int deleteRecordNumOfCtt = cttInfoService.deleteRecord(cttInfoShowPara.getPkid());
                if (ESEnum.ITEMTYPE0.getCode().equals(cttInfoShowPara.getCttType())){
                    progStlInfoService.deleteRecord(initStlData(ESEnum.ITEMTYPE6.getCode(),cttInfoShowPara));
                    progStlInfoService.deleteRecord(initStlData(ESEnum.ITEMTYPE6.getCode(),cttInfoShowPara));
                    progStlInfoService.deleteRecord(initStlData(ESEnum.ITEMTYPE7.getCode(),cttInfoShowPara));
                }else if (ESEnum.ITEMTYPE2.getCode().equals(cttInfoShowPara.getCttType())){
                    progStlInfoService.deleteRecord(initStlData(ESEnum.ITEMTYPE3.getCode(),cttInfoShowPara));
                    progStlInfoService.deleteRecord(initStlData(ESEnum.ITEMTYPE4.getCode(),cttInfoShowPara));
                    progStlInfoService.deleteRecord(initStlData(ESEnum.ITEMTYPE5.getCode(),cttInfoShowPara));
                }
            }
        }else {
            if (progStlInfoService.getInitStlListByModelShow(
                    initStlData(cttInfoShowPara.getCttType(), cttInfoShowPara))==null){
                return "数据已被引用，不可删除！";
            }else {
                progStlInfoService.deleteRecord(initStlData(cttInfoShowPara.getCttType(),cttInfoShowPara));
            }
        }
        OperResExample example=new OperResExample();
        OperResExample.Criteria criteria = example.createCriteria();
        criteria.andInfoTypeEqualTo(cttInfoShowPara.getCttType())
                .andInfoPkidEqualTo(cttInfoShowPara.getPkid());
        operResMapper.deleteByExample(example);
        return "删除数据完成。";
    }
    private OperRes fromOperShowToModel(OperResShow record) {
        OperRes operResPara=new OperRes();
        operResPara.setTid(record.getTid());
        operResPara.setOperPkid(record.getOperPkid());
        operResPara.setFlowStatus(record.getFlowStatus());
        operResPara.setInfoType(record.getInfoType());
        operResPara.setInfoPkid(record.getInfoPkid());
        operResPara.setArchivedFlag(record.getArchivedFlag());
        operResPara.setCreatedBy(record.getCreatedBy());
        operResPara.setCreatedTime(record.getCreatedTime());
        operResPara.setLastUpdBy(record.getLastUpdBy());
        operResPara.setLastUpdTime(record.getLastUpdTime());
        operResPara.setRemark(record.getRemark());
        operResPara.setRecversion( ToolUtil.getIntIgnoreNull(record.getRecversion()));
        return operResPara;
    }
    private OperResShow fromModelToModelShow(OperRes operResPara) {
        OperResShow operResShowTemp=new OperResShow();
        operResShowTemp.setTid(operResPara.getTid());
        operResShowTemp.setOperPkid(operResPara.getOperPkid());
        operResShowTemp.setFlowStatus(operResPara.getFlowStatus());
        operResShowTemp.setInfoType(operResPara.getInfoType());
        operResShowTemp.setInfoPkid(operResPara.getInfoPkid());
        operResShowTemp.setArchivedFlag(operResPara.getArchivedFlag());
        operResShowTemp.setCreatedBy(operResPara.getCreatedBy());
        operResShowTemp.setCreatedTime(operResPara.getCreatedTime());
        operResShowTemp.setLastUpdBy(operResPara.getLastUpdBy());
        operResShowTemp.setLastUpdTime(operResPara.getLastUpdTime());
        operResShowTemp.setRemark(operResPara.getRemark());
        operResShowTemp.setRecversion( ToolUtil.getIntIgnoreNull(operResPara.getRecversion()));
        operResShowTemp.setType(operResPara.getType());
        return operResShowTemp;
    }
}
