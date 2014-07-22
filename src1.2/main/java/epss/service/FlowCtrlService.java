package epss.service;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumPreStatusFlag;
import epss.common.enums.ESEnumStatusFlag;
import epss.common.enums.ESEnumSubcttType;
import epss.common.utils.MessageUtil;
import epss.repository.model.model_show.CttInfoShow;
import epss.repository.model.model_show.FlowCtrlShow;
import epss.repository.model.model_show.ProgInfoShow;
import epss.common.utils.ToolUtil;
import epss.repository.dao.EsInitPowerHisMapper;
import epss.repository.dao.EsInitPowerMapper;
import epss.repository.model.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import platform.service.PlatformService;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-7-1
 * Time: 下午4:21
 * To change this template use File | Settings | File Templates.
 */
@Service
public class FlowCtrlService {
    @Autowired
    private EsInitPowerMapper esInitPowerMapper;
    @Autowired
    private EsInitPowerHisMapper esInitPowerHisMapper;
    @Resource
    private PlatformService platformService;
    @Resource
    private EsFlowService esFlowService;
    @Resource
    private ProgStlInfoService progStlInfoService;
    @Resource
    private CttInfoService cttInfoService;

    public List<EsInitPower> selectListByModel() {
        EsInitPowerExample example= new EsInitPowerExample();
        EsInitPowerExample.Criteria criteria = example.createCriteria();
        example.setOrderByClause("POWER_TYPE ASC,STATUS_FLAG ASC") ;
        return esInitPowerMapper.selectByExample(example);
    }

    public List<EsInitPower> selectListByModel(EsInitPower esInitPower) {
        EsInitPowerExample example= new EsInitPowerExample();
        EsInitPowerExample.Criteria criteria = example.createCriteria();
        criteria.andPowerTypeLike("%" + (esInitPower.getPowerType()) + "%")
                .andPowerPkidLike("%" + ToolUtil.getStrIgnoreNull(esInitPower.getPowerPkid()) + "%")
                .andPeriodNoLike("%" + ToolUtil.getStrIgnoreNull(esInitPower.getPeriodNo()) + "%");
        if(!ToolUtil.getStrIgnoreNull(esInitPower.getStatusFlag()).trim().equals("")){
            criteria.andStatusFlagEqualTo(esInitPower.getStatusFlag());
        }
        example.setOrderByClause("PERIOD_NO ASC") ;
        return esInitPowerMapper.selectByExample(example);
    }

    public EsInitPower selectByPrimaryKey(EsInitPower esInitPower){
        EsInitPowerKey esInitPowerKey=new EsInitPowerKey();
        esInitPowerKey.setPowerType(esInitPower.getPowerType());
        esInitPowerKey.setPowerPkid(esInitPower.getPowerPkid());
        esInitPowerKey.setPeriodNo(esInitPower.getPeriodNo());
        return esInitPowerMapper.selectByPrimaryKey(esInitPowerKey);
    }

    public List<EsInitPower> selectListByModel(String strPowerType,String strPowerPkid,String strPeriodNo) {
        EsInitPowerExample example= new EsInitPowerExample();
        EsInitPowerExample.Criteria criteria = example.createCriteria();
        criteria.andPowerTypeLike("%" + strPowerType + "%")
                .andPowerPkidLike("%" + strPowerPkid + "%")
                .andPeriodNoLike("%" + strPeriodNo + "%");
        return esInitPowerMapper.selectByExample(example);
    }
    public List<EsInitPower> selectListByModel(String strPowerType,String strPowerPkid) {
        EsInitPowerExample example= new EsInitPowerExample();
        EsInitPowerExample.Criteria criteria = example.createCriteria();
        criteria.andPowerTypeLike("%" + strPowerType + "%")
                .andPowerPkidLike("%" + strPowerPkid + "%");
        return esInitPowerMapper.selectByExample(example);
    }

    public void accountAction(EsInitStl esInitStlPara) {
        EsInitPower esInitPowerTemp = new EsInitPower();
        esInitPowerTemp.setPowerType(esInitStlPara.getStlType());
        esInitPowerTemp.setPowerPkid(esInitStlPara.getStlPkid());
        esInitPowerTemp.setPeriodNo(esInitStlPara.getPeriodNo());
        esInitPowerTemp.setStatusFlag(ESEnumStatusFlag.STATUS_FLAG4.getCode());
        esInitPowerTemp.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG7.getCode());
        esInitPowerTemp.setCreatedBy(esInitStlPara.getCreatedBy());
        esInitPowerTemp.setCreatedDate(esInitStlPara.getCreatedDate());
        esInitPowerTemp.setLastUpdBy(esInitStlPara.getLastUpdBy());
        esInitPowerTemp.setLastUpdDate(esInitStlPara.getLastUpdDate());
        esInitPowerMapper.updateByPrimaryKey(esInitPowerTemp);
        esInitPowerHisMapper.insert(fromEsInitPowerToEsInitPowerHis(esInitPowerTemp,"update"));
    }

    public void insertRecord(FlowCtrlShow flowCtrlShowPara) {
        EsInitPower esInitPowerTemp= fromFlowCtrlShowToPower(flowCtrlShowPara);
        esInitPowerTemp.setCreatedBy(platformService.getStrLastUpdBy());
        esInitPowerTemp.setCreatedDate(platformService.getStrLastUpdDate());
        esInitPowerMapper.insertSelective(esInitPowerTemp);
        esInitPowerHisMapper.insert(fromEsInitPowerToEsInitPowerHis(esInitPowerTemp,"insert"));
    }
    public void insertRecordByCtt(CttInfoShow cttInfoShowPara) {
        cttInfoShowPara.setDeletedFlag("0");
        cttInfoShowPara.setLastUpdBy(platformService.getStrLastUpdBy());
        cttInfoShowPara.setLastUpdDate(platformService.getStrLastUpdDate());
        EsInitPower esInitPowerTemp=fromCttInfoShowToPower(cttInfoShowPara);
        esInitPowerMapper.insertSelective(fromCttInfoShowToPower(cttInfoShowPara));
        esInitPowerHisMapper.insert(fromEsInitPowerToEsInitPowerHis(esInitPowerTemp,"insert"));
    }
    public void insertRecordByStl( EsInitPower esInitPowerPara) {
        esInitPowerPara.setCreatedBy(platformService.getStrLastUpdBy());
        esInitPowerPara.setCreatedDate(platformService.getStrLastUpdDate());
        esInitPowerPara.setLastUpdBy(platformService.getStrLastUpdBy());
        esInitPowerPara.setLastUpdDate(platformService.getStrLastUpdDate());
        esInitPowerMapper.insertSelective(esInitPowerPara);
        esInitPowerHisMapper.insert(fromEsInitPowerToEsInitPowerHis(esInitPowerPara,"insert"));
    }

    public void updateRecord(FlowCtrlShow flowCtrlShowPara){
        EsInitPower esInitPowerTemp= fromFlowCtrlShowToPower(flowCtrlShowPara);
        esInitPowerTemp.setLastUpdBy(platformService.getStrLastUpdBy());
        esInitPowerTemp.setLastUpdDate(platformService.getStrLastUpdDate());
        esInitPowerMapper.updateByPrimaryKey(esInitPowerTemp);
        esInitPowerTemp.setCreatedBy(platformService.getStrLastUpdBy());
        esInitPowerTemp.setCreatedDate(platformService.getStrLastUpdDate());
        esInitPowerHisMapper.insert(fromEsInitPowerToEsInitPowerHis(esInitPowerTemp,"update"));
    }
    public void updateRecordByCtt(CttInfoShow cttInfoShowPara){
        cttInfoShowPara.setModificationNum(
                ToolUtil.getIntIgnoreNull(cttInfoShowPara.getModificationNum())+1);
        cttInfoShowPara.setLastUpdBy(platformService.getStrLastUpdBy());
        cttInfoShowPara.setLastUpdDate(platformService.getStrLastUpdDate());
        EsInitPower esInitPowerTemp=fromCttInfoShowToPower(cttInfoShowPara);
        esInitPowerMapper.updateByPrimaryKey(fromCttInfoShowToPower(cttInfoShowPara));
        esInitPowerHisMapper.insert(fromEsInitPowerToEsInitPowerHis(esInitPowerTemp,"update"));
    }
    @Transactional
    public void updateRecordByStl(ProgInfoShow progInfoShowPara){
        progInfoShowPara.setModificationNum(
                ToolUtil.getIntIgnoreNull(progInfoShowPara.getModificationNum()) + 1);
        progInfoShowPara.setDeletedFlag("0");
        progInfoShowPara.setLastUpdBy(platformService.getStrLastUpdBy());
        progInfoShowPara.setLastUpdDate(platformService.getStrLastUpdDate());
        EsInitPower esInitPowerTemp = fromProgInfoShowToPower(progInfoShowPara);
        esInitPowerMapper.updateByPrimaryKey(esInitPowerTemp);
        esInitPowerHisMapper.insert(fromEsInitPowerToEsInitPowerHis(esInitPowerTemp, "update"));
    }
    @Transactional
    public void updateRecordByStl(ProgInfoShow progInfoShowPara,String strFlowTypePara) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        progInfoShowPara.setModificationNum(
                ToolUtil.getIntIgnoreNull(progInfoShowPara.getModificationNum())+1);
        progInfoShowPara.setDeletedFlag("0");
        progInfoShowPara.setLastUpdBy(platformService.getStrLastUpdBy());
        progInfoShowPara.setLastUpdDate(platformService.getStrLastUpdDate());
        EsInitPower esInitPowerTemp=fromProgInfoShowToPower(progInfoShowPara);
        esInitPowerMapper.updateByPrimaryKey(esInitPowerTemp);
        esInitPowerTemp.setCreatedBy(platformService.getStrLastUpdBy());
        esInitPowerTemp.setCreatedDate(platformService.getStrLastUpdDate());
        esInitPowerHisMapper.insert(fromEsInitPowerToEsInitPowerHis(esInitPowerTemp,"update"));

        EsCttInfo esCttInfo=cttInfoService.getCttInfoByPkId(progInfoShowPara.getStlPkid());
        if ("".equals(ToolUtil.getStrIgnoreNull(esCttInfo.getType()))){

        }else if (ESEnumSubcttType.TYPE0.getCode().equals(esCttInfo.getType())){

        }else if (ESEnumSubcttType.TYPE1.getCode().equals(esCttInfo.getType())){

        }else if (ESEnumSubcttType.TYPE2.getCode().equals(esCttInfo.getType())){

        }else if (ESEnumSubcttType.TYPE3.getCode().equals(esCttInfo.getType())) {
            if (ESEnum.ITEMTYPE3.getCode().equals(progInfoShowPara.getPowerType())) {
                String SubcttStlMStatus = ToolUtil.getStrIgnoreNull(
                        esFlowService.getStatusFlag(ESEnum.ITEMTYPE4.getCode(), progInfoShowPara.getStlPkid(), progInfoShowPara.getPeriodNo()));
                if (ESEnumStatusFlag.STATUS_FLAG2.getCode().compareTo(SubcttStlMStatus) > 0) {
                    return;
                }
            }else if (ESEnum.ITEMTYPE4.getCode().equals(progInfoShowPara.getPowerType())){
                String SubcttStlQStatus = ToolUtil.getStrIgnoreNull(
                        esFlowService.getStatusFlag(ESEnum.ITEMTYPE3.getCode(), progInfoShowPara.getStlPkid(), progInfoShowPara.getPeriodNo()));
                if (ESEnumStatusFlag.STATUS_FLAG2.getCode().compareTo(SubcttStlQStatus) > 0) {
                    return;
                }
            }
        }else if (ESEnumSubcttType.TYPE4.getCode().equals(esCttInfo.getType())){

        }else if (ESEnumSubcttType.TYPE5.getCode().equals(esCttInfo.getType())){

        }else if (ESEnumSubcttType.TYPE6.getCode().equals(esCttInfo.getType())){

        }
        if ("DoubleCheckPass".equals(strFlowTypePara)){
            EsInitStl esInitStlTemp = new EsInitStl();
            esInitStlTemp.setStlType(ESEnum.ITEMTYPE5.getCode());
            esInitStlTemp.setStlPkid(progInfoShowPara.getStlPkid());
            esInitStlTemp.setPeriodNo(progInfoShowPara.getPeriodNo());
            esInitStlTemp.setNote("");
            // 结算登记表和流程控制表登记
            progStlInfoService.insertStlAndPowerRecord(esInitStlTemp);
        } else if ("DoubleCheckFail".equals(strFlowTypePara)) {
            ProgInfoShow progInfoShowTemp = (ProgInfoShow) BeanUtils.cloneBean(progInfoShowPara);
            progInfoShowTemp.setStlType(ESEnum.ITEMTYPE5.getCode());
            progStlInfoService.deleteRecord(progInfoShowTemp);
        }
    }
    public void updateRecordByStl(EsInitPower esInitPowerPara){
        EsInitPower esInitPower = selectByPrimaryKey(esInitPowerPara);
        if (esInitPower!=null){
            esInitPower.setStatusFlag(esInitPowerPara.getStatusFlag());
            esInitPower.setPreStatusFlag(esInitPowerPara.getPreStatusFlag());
            esInitPower.setLastUpdBy(platformService.getStrLastUpdBy());
            esInitPower.setLastUpdDate(platformService.getStrLastUpdDate());
            esInitPowerMapper.updateByPrimaryKey(esInitPower);
            esInitPower.setCreatedBy(platformService.getStrLastUpdBy());
            esInitPower.setCreatedDate(platformService.getStrLastUpdDate());
            esInitPowerHisMapper.insert(fromEsInitPowerToEsInitPowerHis(esInitPower,"update"));
        }
    }
    public void updateRecordByPower(EsInitPower esInitPowerPara){
        esInitPowerPara.setLastUpdBy(platformService.getStrLastUpdBy());
        esInitPowerPara.setLastUpdDate(platformService.getStrLastUpdDate());
        esInitPowerMapper.updateByPrimaryKey(esInitPowerPara);
        esInitPowerHisMapper.insert(fromEsInitPowerToEsInitPowerHis(esInitPowerPara,"update"));
    }

    private EsInitPower fromCttInfoShowToPower(CttInfoShow cttInfoShowPara) {
        EsInitPower esInitPowerTemp = new EsInitPower();
        esInitPowerTemp.setPowerType(cttInfoShowPara.getPowerType());
        esInitPowerTemp.setPowerPkid(cttInfoShowPara.getPowerPkid());
        esInitPowerTemp.setPeriodNo(cttInfoShowPara.getPeriodNo());
        esInitPowerTemp.setStatusFlag(cttInfoShowPara.getStatusFlag());
        esInitPowerTemp.setPreStatusFlag(cttInfoShowPara.getPreStatusFlag());
        esInitPowerTemp.setCreatedBy(cttInfoShowPara.getCreatedBy());
        esInitPowerTemp.setCreatedDate(cttInfoShowPara.getCreatedDate());
        esInitPowerTemp.setLastUpdBy(cttInfoShowPara.getLastUpdBy());
        esInitPowerTemp.setLastUpdDate(cttInfoShowPara.getLastUpdDate());
        esInitPowerTemp.setSpareField(cttInfoShowPara.getSpareField());
        return esInitPowerTemp;
    }
    private EsInitPower fromFlowCtrlShowToPower(FlowCtrlShow flowCtrlShowPara) {
        EsInitPower esInitPowerTemp = new EsInitPower();
        esInitPowerTemp.setPowerType(flowCtrlShowPara.getPowerType());
        esInitPowerTemp.setPowerPkid(flowCtrlShowPara.getPowerPkid());
        esInitPowerTemp.setPeriodNo(flowCtrlShowPara.getPeriodNo());
        esInitPowerTemp.setStatusFlag(flowCtrlShowPara.getStatusFlag());
        esInitPowerTemp.setPreStatusFlag(flowCtrlShowPara.getPreStatusFlag());
        esInitPowerTemp.setCreatedBy(flowCtrlShowPara.getCreatedBy());
        esInitPowerTemp.setCreatedDate(flowCtrlShowPara.getCreatedDate());
        esInitPowerTemp.setLastUpdBy(flowCtrlShowPara.getLastUpdBy());
        esInitPowerTemp.setLastUpdDate(flowCtrlShowPara.getLastUpdDate());
        esInitPowerTemp.setSpareField(flowCtrlShowPara.getSpareField());
        return esInitPowerTemp;
    }
    private EsInitPower fromProgInfoShowToPower(ProgInfoShow progInfoShowPara) {
        EsInitPower esInitPowerTemp=new EsInitPower();
        esInitPowerTemp.setPowerType(progInfoShowPara.getPowerType());
        esInitPowerTemp.setPowerPkid(progInfoShowPara.getPowerPkid());
        esInitPowerTemp.setPeriodNo(progInfoShowPara.getPeriodNo());
        esInitPowerTemp.setStatusFlag(progInfoShowPara.getStatusFlag());
        esInitPowerTemp.setPreStatusFlag(progInfoShowPara.getPreStatusFlag());
        esInitPowerTemp.setCreatedBy(progInfoShowPara.getCreatedBy());
        esInitPowerTemp.setCreatedDate(progInfoShowPara.getCreatedDate());
        esInitPowerTemp.setLastUpdBy(progInfoShowPara.getLastUpdBy());
        esInitPowerTemp.setLastUpdDate(progInfoShowPara.getLastUpdDate());
        esInitPowerTemp.setSpareField(progInfoShowPara.getSpareField());
        return esInitPowerTemp;
    }
    private EsInitPowerHis fromEsInitPowerToEsInitPowerHis(EsInitPower esInitPowerPara,String strOperType){
        EsInitPowerHis esInitPowerHis =new EsInitPowerHis();
        esInitPowerHis.setPowerType(esInitPowerPara.getPowerType());
        esInitPowerHis.setPowerPkid(esInitPowerPara.getPowerPkid());
        esInitPowerHis.setPeriodNo(esInitPowerPara.getPeriodNo());
        esInitPowerHis.setStatusFlag(esInitPowerPara.getStatusFlag());
        esInitPowerHis.setPreStatusFlag(esInitPowerPara.getPreStatusFlag());
        esInitPowerHis.setCreatedDate(esInitPowerPara.getCreatedDate());
        esInitPowerHis.setCreatedBy(esInitPowerPara.getCreatedBy());
        esInitPowerHis.setSpareField(strOperType);
        return esInitPowerHis;
    }

    public int deleteRecord(String strPowerType,String strPowerPkid,String strPeriodNo){
        int deleteNum=0;
        List<EsInitPower> esInitPowerListTemp=selectListByModel(strPowerType,strPowerPkid,strPeriodNo);
        if(esInitPowerListTemp.size()>0){
            EsInitPowerKey  esInitPowerKey=new EsInitPowerKey();
            esInitPowerKey.setPowerType(strPowerType);
            esInitPowerKey.setPowerPkid(strPowerPkid);
            esInitPowerKey.setPeriodNo(strPeriodNo);
            deleteNum=esInitPowerMapper.deleteByPrimaryKey(esInitPowerKey);
            EsInitPower esInitPower =esInitPowerListTemp.get(0);
            esInitPower.setCreatedBy(platformService.getStrLastUpdBy());
            esInitPower.setCreatedDate(platformService.getStrLastUpdDate());
            esInitPowerHisMapper.insert(fromEsInitPowerToEsInitPowerHis(esInitPower,"Del"));
        }
        return deleteNum;
    }
    public void deleteRecordByCtt(CttInfoShow cttInfoShowPara){
        List<EsInitPower> esInitPowerListTemp=selectListByModel(
                cttInfoShowPara.getPowerType(),
                cttInfoShowPara.getPowerPkid(),
                cttInfoShowPara.getPeriodNo());
        if(esInitPowerListTemp.size()>0){
            EsInitPowerKey  esInitPowerKey=new EsInitPowerKey();
            esInitPowerKey.setPowerType(cttInfoShowPara.getPowerType());
            esInitPowerKey.setPowerPkid(cttInfoShowPara.getPowerPkid());
            esInitPowerKey.setPeriodNo(cttInfoShowPara.getPeriodNo());
            esInitPowerMapper.deleteByPrimaryKey(esInitPowerKey);
            EsInitPower esInitPower =esInitPowerListTemp.get(0);
            esInitPower.setCreatedBy(platformService.getStrLastUpdBy());
            esInitPower.setCreatedDate(platformService.getStrLastUpdDate());
            esInitPowerHisMapper.insert(fromEsInitPowerToEsInitPowerHis(esInitPower,"Del"));
        }
    }
    public void deleteRecordByStl(ProgInfoShow progInfoShowPara){
        List<EsInitPower> esInitPowerListTemp=selectListByModel(
                progInfoShowPara.getPowerType(),
                progInfoShowPara.getPowerPkid(),
                progInfoShowPara.getPeriodNo());
        if(esInitPowerListTemp.size()>0){
            EsInitPowerKey  esInitPowerKey=new EsInitPowerKey();
            esInitPowerKey.setPowerType(progInfoShowPara.getPowerType());
            esInitPowerKey.setPowerPkid(progInfoShowPara.getPowerPkid());
            esInitPowerKey.setPeriodNo(progInfoShowPara.getPeriodNo());
            esInitPowerMapper.deleteByPrimaryKey(esInitPowerKey);
            EsInitPower esInitPower =esInitPowerListTemp.get(0);
            esInitPower.setCreatedBy(platformService.getStrLastUpdBy());
            esInitPower.setCreatedDate(platformService.getStrLastUpdDate());
            esInitPowerHisMapper.insert(fromEsInitPowerToEsInitPowerHis(esInitPower,"Del"));
        }
    }
    public void deleteRecord(String strPowerType,String strPowerPkid){
        List<EsInitPower> esInitPowerListTemp=selectListByModel(strPowerType,strPowerPkid);
        if(esInitPowerListTemp.size()>0){
            EsInitPowerKey  esInitPowerKey=new EsInitPowerKey();
            esInitPowerKey.setPowerType(strPowerType);
            esInitPowerKey.setPowerPkid(strPowerPkid);
            esInitPowerMapper.deleteByPrimaryKey(esInitPowerKey);
            EsInitPower esInitPower =esInitPowerListTemp.get(0);
            esInitPower.setCreatedBy(platformService.getStrLastUpdBy());
            esInitPower.setCreatedDate(platformService.getStrLastUpdDate());
            esInitPowerHisMapper.insert(fromEsInitPowerToEsInitPowerHis(esInitPower,"Del"));
        }
    }
}

