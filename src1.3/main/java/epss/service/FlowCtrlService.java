package epss.service;

import epss.repository.model.model_show.ProgEstInfoShow;
import epss.repository.model.model_show.SubcttInfoShow;
import epss.common.utils.ToolUtil;
import epss.repository.dao.FlowCtrlHisMapper;
import epss.repository.dao.FlowCtrlMapper;
import epss.repository.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import platform.service.PlatformService;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-7-1
 * Time: ÏÂÎç4:21
 * To change this template use File | Settings | File Templates.
 */
@Service
public class FlowCtrlService {
    @Autowired
    private FlowCtrlMapper flowCtrlMapper;
    @Autowired
    private FlowCtrlHisMapper flowCtrlHisMapper;

    public List<FlowCtrl> selectListByModel() {
        FlowCtrlExample example= new FlowCtrlExample();
        FlowCtrlExample.Criteria criteria = example.createCriteria();
        example.setOrderByClause("INFO_TYPE ASC,FLOW_STATUS ASC") ;
        return flowCtrlMapper.selectByExample(example);
    }

    public List<FlowCtrl> selectListByModel(FlowCtrl flowCtrl) {
        FlowCtrlExample example= new FlowCtrlExample();
        FlowCtrlExample.Criteria criteria = example.createCriteria();
        criteria.andInfoTypeLike("%" + (flowCtrl.getInfoType()) + "%")
                .andInfoPkidLike("%" + ToolUtil.getStrIgnoreNull(flowCtrl.getInfoPkid()) + "%")
                .andInfoIdLike("%" + ToolUtil.getStrIgnoreNull(flowCtrl.getInfoId()) + "%");
        if(!ToolUtil.getStrIgnoreNull(flowCtrl.getFlowStatus()).trim().equals("")){
            criteria.andFlowStatusEqualTo(flowCtrl.getFlowStatus());
        }
        example.setOrderByClause("STAGE_NO ASC") ;
        return flowCtrlMapper.selectByExample(example);
    }

    public FlowCtrl selectByPrimaryKey(FlowCtrl flowCtrl){
        EsInitPowerKey esInitPowerKey=new EsInitPowerKey();
        esInitPowerKey.setPowerType(flowCtrl.getInfoType());
        esInitPowerKey.setPowerPkid(flowCtrl.getInfoPkid());
        esInitPowerKey.setStageNo(flowCtrl.getInfoId());
        /*return flowCtrlMapper.selectByPrimaryKey(esInitPowerKey);*/
        return flowCtrlMapper.selectByPrimaryKey(flowCtrl.getPkid()) ;
    }

    public List<FlowCtrl> selectListByModel(String strInfoType,String strInfoPkid,String strInfoId) {
        FlowCtrlExample example= new FlowCtrlExample();
        FlowCtrlExample.Criteria criteria = example.createCriteria();
        criteria.andInfoTypeLike("%" + strInfoType + "%")
                .andInfoPkidLike("%" + strInfoPkid + "%")
                .andInfoIdLike("%" + strInfoId + "%");
        return flowCtrlMapper.selectByExample(example);
    }
    public List<FlowCtrl> selectListByModel(String strInfoType,String strInfoPkid) {
        FlowCtrlExample example= new FlowCtrlExample();
        FlowCtrlExample.Criteria criteria = example.createCriteria();
        criteria.andInfoTypeLike("%" + strInfoType + "%")
                .andInfoPkidLike("%" + strInfoPkid + "%");
        return flowCtrlMapper.selectByExample(example);
    }

    private FlowCtrlHis fromEsInitPowerToEsInitPowerHis(FlowCtrl flowCtrlPara,String strOperType){
        FlowCtrlHis flowCtrlHis =new FlowCtrlHis();
        flowCtrlHis.setInfoType(flowCtrlPara.getInfoType());
        flowCtrlHis.setInfoPkid(flowCtrlPara.getInfoPkid());
        flowCtrlHis.setInfoId(flowCtrlPara.getInfoId());
        flowCtrlHis.setFlowStatus(flowCtrlPara.getFlowStatus());
        flowCtrlHis.setFlowStatusRemark(flowCtrlPara.getFlowStatusRemark());
        flowCtrlHis.setCreatedTime(flowCtrlPara.getCreatedTime());
        flowCtrlHis.setCreatedBy(flowCtrlPara.getCreatedBy());
        flowCtrlHis.setOperType(strOperType);
        return flowCtrlHis;
    }

    /*public void insertRecord(FlowCtrlShow flowCtrlShowPara) {
        FlowCtrl flowCtrlTemp = fromItemConstructToPower(flowCtrlShowPara);
        flowCtrlTemp.setCreatedBy(platformService.getStrLastUpdBy());
        flowCtrlTemp.setCreatedTime(platformService.getStrLastUpdTime());
        flowCtrlMapper.insertSelective(flowCtrlTemp);
        flowCtrlHisMapper.insert(fromEsInitPowerToEsInitPowerHis(flowCtrlTemp,"insert"));
    }*/
    public void insertRecordByCtt(SubcttInfoShow subcttInfoShowPara) {
      /*  subcttInfoShowPara.setArchivedFlag("0");
        subcttInfoShowPara.setUpdatedBy(platformService.getStrLastUpdBy());
        subcttInfoShowPara.setUpdatedTime(platformService.getStrLastUpdTime());
        FlowCtrl flowCtrlTemp =fromCttConstructToPower(subcttInfoShowPara);
        flowCtrlMapper.insertSelective(fromCttConstructToPower(subcttInfoShowPara));
        flowCtrlHisMapper.insert(fromEsInitPowerToEsInitPowerHis(flowCtrlTemp,"insert"));*/
    }
    public void insertRecordByStl(ProgEstInfoShow progEstInfoShowPara) {
      /*  progEstInfoShowPara.setArchivedFlag("0");
        progEstInfoShowPara.setCreatedBy(platformService.getStrLastUpdBy());
        progEstInfoShowPara.setCreatedTime(platformService.getStrLastUpdTime());
        progEstInfoShowPara.setUpdatedBy(platformService.getStrLastUpdBy());
        progEstInfoShowPara.setUpdatedTime(platformService.getStrLastUpdTime());
        FlowCtrl flowCtrlTemp =fromStlConstructToPower(progEstInfoShowPara);
        flowCtrlMapper.insertSelective(flowCtrlTemp);
        flowCtrlHisMapper.insert(fromEsInitPowerToEsInitPowerHis(flowCtrlTemp,"insert"));*/
    }

    /*public void updateRecord(FlowCtrlShow flowCtrlShowPara){
        FlowCtrl flowCtrlTemp = fromItemConstructToPower(flowCtrlShowPara);
        flowCtrlTemp.setUpdatedBy(platformService.getStrLastUpdBy());
        flowCtrlTemp.setUpdatedTime(platformService.getStrLastUpdTime());
        flowCtrlMapper.updateByPrimaryKey(flowCtrlTemp);
        flowCtrlHisMapper.insert(fromEsInitPowerToEsInitPowerHis(flowCtrlTemp,"update"));
    }
    public void updateRecordByCtt(SubcttInfoShow subcttInfoShowPara){
        subcttInfoShowPara.setRecVersion(
                ToolUtil.getIntIgnoreNull(subcttInfoShowPara.getRecVersion()) + 1);
        subcttInfoShowPara.setUpdatedBy(platformService.getStrLastUpdBy());
        subcttInfoShowPara.setUpdatedTime(platformService.getStrLastUpdTime());
        FlowCtrl flowCtrlTemp =fromCttConstructToPower(subcttInfoShowPara);
        flowCtrlMapper.updateByPrimaryKey(fromCttConstructToPower(subcttInfoShowPara));
        flowCtrlHisMapper.insert(fromEsInitPowerToEsInitPowerHis(flowCtrlTemp,"update"));
    }
    public void updateRecordByStl(ProgEstInfoShow progEstInfoShowPara){
        progEstInfoShowPara.setRecVersion(
                ToolUtil.getIntIgnoreNull(progEstInfoShowPara.getRecVersion()) + 1);
        progEstInfoShowPara.setArchivedFlag("0");
        progEstInfoShowPara.setUpdatedBy(platformService.getStrLastUpdBy());
        progEstInfoShowPara.setUpdatedTime(platformService.getStrLastUpdTime());
        FlowCtrl flowCtrlTemp =fromStlConstructToPower(progEstInfoShowPara);
        flowCtrlMapper.updateByPrimaryKey(flowCtrlTemp);
        flowCtrlHisMapper.insert(fromEsInitPowerToEsInitPowerHis(flowCtrlTemp,"update"));
    }

    private FlowCtrl fromCttConstructToPower(SubcttInfoShow subcttInfoShowPara) {
        FlowCtrl flowCtrlTemp = new FlowCtrl();
        flowCtrlTemp.setInfoType(subcttInfoShowPara.getPowerType());
        flowCtrlTemp.setInfoPkid(subcttInfoShowPara.getPowerPkid());
        flowCtrlTemp.setInfoId(subcttInfoShowPara.getStageNo());
        flowCtrlTemp.setFlowStatus(subcttInfoShowPara.getFlowStatus());
        flowCtrlTemp.setFlowStatusRemark(subcttInfoShowPara.getFlowStatusRemark());
        flowCtrlTemp.setCreatedBy(subcttInfoShowPara.getCreatedBy());
        flowCtrlTemp.setCreatedTime(subcttInfoShowPara.getCreatedTime());
        flowCtrlTemp.setUpdatedBy(subcttInfoShowPara.getUpdatedBy());
        flowCtrlTemp.setUpdatedTime(subcttInfoShowPara.getUpdatedTime());
        flowCtrlTemp.setRemark(subcttInfoShowPara.getRemark());
        return flowCtrlTemp;
    }

    private FlowCtrl fromItemConstructToPower(FlowCtrlShow flowCtrlShowPara) {
        FlowCtrl flowCtrlTemp = new FlowCtrl();
        flowCtrlTemp.setInfoType(flowCtrlShowPara.getPowerType());
        flowCtrlTemp.setInfoPkid(flowCtrlShowPara.getPowerPkid());
        flowCtrlTemp.setInfoId(flowCtrlShowPara.getStageNo());
        flowCtrlTemp.setFlowStatus(flowCtrlShowPara.getFlowStatus());
        flowCtrlTemp.setFlowStatusRemark(flowCtrlShowPara.getFlowStatusRemark());
        flowCtrlTemp.setCreatedBy(flowCtrlShowPara.getCreatedBy());
        flowCtrlTemp.setCreatedTime(flowCtrlShowPara.getCreatedTime());
        flowCtrlTemp.setUpdatedBy(flowCtrlShowPara.getUpdatedBy());
        flowCtrlTemp.setUpdatedTime(flowCtrlShowPara.getUpdatedTime());
        flowCtrlTemp.setRemark(flowCtrlShowPara.getRemark());
        return flowCtrlTemp;
    }

    private FlowCtrl fromStlConstructToPower(ProgEstInfoShow progEstInfoShowPara) {
        FlowCtrl flowCtrlTemp =new FlowCtrl();
        flowCtrlTemp.setInfoType(progEstInfoShowPara.getPowerType());
        flowCtrlTemp.setInfoPkid(progEstInfoShowPara.getPowerPkid());
        flowCtrlTemp.setInfoId(progEstInfoShowPara.getStageNo());
        flowCtrlTemp.setFlowStatus(progEstInfoShowPara.getFlowStatus());
        flowCtrlTemp.setFlowStatusRemark(progEstInfoShowPara.getFlowStatusRemark());
        flowCtrlTemp.setCreatedBy(progEstInfoShowPara.getCreatedBy());
        flowCtrlTemp.setCreatedTime(progEstInfoShowPara.getCreatedTime());
        flowCtrlTemp.setUpdatedBy(progEstInfoShowPara.getUpdatedBy());
        flowCtrlTemp.setUpdatedTime(progEstInfoShowPara.getUpdatedTime());
        return flowCtrlTemp;
    }

    public int deleteRecord(String strPowerType,String strPowerPkid,String strInfoId){
        EsInitPowerKey  esInitPowerKey=new EsInitPowerKey();
        esInitPowerKey.setPowerType(strPowerType);
        esInitPowerKey.setPowerPkid(strPowerPkid);
        esInitPowerKey.setStageNo(strInfoId);
        int deleteNum= flowCtrlMapper.deleteByPrimaryKey(esInitPowerKey);
        List<FlowCtrl> flowCtrlListTemp =selectListByModel(strPowerType,strPowerPkid,strInfoId);
        if(flowCtrlListTemp.size()>0){
            FlowCtrl flowCtrl = flowCtrlListTemp.get(0);
            flowCtrlHisMapper.insert(fromEsInitPowerToEsInitPowerHis(flowCtrl,"Del"));
        }
        return deleteNum;
        return 0;
    }
    public void deleteRecordByCtt(SubcttInfoShow subcttInfoShowPara){
        EsInitPowerKey  esInitPowerKey=new EsInitPowerKey();
        esInitPowerKey.setPowerType(subcttInfoShowPara.getPowerType());
        esInitPowerKey.setPowerPkid(subcttInfoShowPara.getPowerPkid());
        esInitPowerKey.setStageNo(subcttInfoShowPara.getStageNo());
        flowCtrlMapper.deleteByPrimaryKey(esInitPowerKey);
        List<FlowCtrl> flowCtrlListTemp =selectListByModel(
                subcttInfoShowPara.getPowerType(),
                subcttInfoShowPara.getPowerPkid(),
                subcttInfoShowPara.getStageNo());
        if(flowCtrlListTemp.size()>0){
            FlowCtrl flowCtrl = flowCtrlListTemp.get(0);
            flowCtrlHisMapper.insert(fromEsInitPowerToEsInitPowerHis(flowCtrl,"Del"));
        }
    }
    public void deleteRecordByStl(ProgEstInfoShow progEstInfoShowPara){
        EsInitPowerKey  esInitPowerKey=new EsInitPowerKey();
        esInitPowerKey.setPowerType(progEstInfoShowPara.getPowerType());
        esInitPowerKey.setPowerPkid(progEstInfoShowPara.getPowerPkid());
        esInitPowerKey.setStageNo(progEstInfoShowPara.getStageNo());
        flowCtrlMapper.deleteByPrimaryKey(esInitPowerKey);
        List<FlowCtrl> flowCtrlListTemp =selectListByModel(
                progEstInfoShowPara.getPowerType(),
                progEstInfoShowPara.getPowerPkid(),
                progEstInfoShowPara.getStageNo());
        if(flowCtrlListTemp.size()>0){
            FlowCtrl flowCtrl = flowCtrlListTemp.get(0);
            flowCtrlHisMapper.insert(fromEsInitPowerToEsInitPowerHis(flowCtrl,"Del"));
        }
    }
    public void deleteRecord(String strPowerType,String strPowerPkid){
        EsInitPowerKey  esInitPowerKey=new EsInitPowerKey();
        esInitPowerKey.setPowerType(strPowerType);
        esInitPowerKey.setPowerPkid(strPowerPkid);
        flowCtrlMapper.deleteByPrimaryKey(esInitPowerKey);
        List<FlowCtrl> flowCtrlListTemp =selectListByModel(strPowerType,strPowerPkid);
        if(flowCtrlListTemp.size()>0){
            FlowCtrl flowCtrl = flowCtrlListTemp.get(0);
            flowCtrlHisMapper.insert(fromEsInitPowerToEsInitPowerHis(flowCtrl,"Del"));
        }
    }*/
}

