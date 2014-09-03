package epss.service;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumPreStatusFlag;
import epss.common.enums.ESEnumStatusFlag;
import epss.repository.model.model_show.CttInfoShow;
import skyline.util.ToolUtil;
import epss.repository.dao.not_mybatis.MyCttStlMapper;
import epss.repository.model.model_show.ProgInfoShow;
import epss.repository.dao.EsInitStlMapper;
import epss.repository.model.*;
import epss.repository.model.model_show.ProgSubstlItemShow;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-5-20
 * Time: ����10:02
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ProgStlInfoService {
    @Resource
    private EsInitStlMapper esInitStlMapper;
    @Resource
    private MyCttStlMapper myCttStlMapper;
    @Resource
    private FlowCtrlService flowCtrlService;
    @Resource
    private ProgSubstlItemService progSubstlItemService;

    /**
     * �жϼ�¼�Ƿ��Ѵ���
     *
     * @param progInfoShowPara
     * @return
     */
    public List<EsInitStl> getExistedEsInitStlSubcttEngInDb(ProgInfoShow progInfoShowPara) {
        EsInitStlExample example = new EsInitStlExample();
        example.createCriteria().andStlTypeEqualTo(progInfoShowPara.getStlType())
                                .andStlPkidEqualTo(progInfoShowPara.getStlPkid())
                                .andPeriodNoEqualTo(progInfoShowPara.getPeriodNo());
        return esInitStlMapper.selectByExample(example);
    }

    private EsInitStl fromModelShowToModel(ProgInfoShow progInfoShowPara){
        EsInitStl esInitStlTemp =new EsInitStl();
        esInitStlTemp.setPkid(progInfoShowPara.getPkid());
        esInitStlTemp.setStlType(progInfoShowPara.getStlType());
        esInitStlTemp.setStlPkid(progInfoShowPara.getStlPkid());
        esInitStlTemp.setId(progInfoShowPara.getId());
        esInitStlTemp.setPeriodNo(progInfoShowPara.getPeriodNo());
        esInitStlTemp.setNote(progInfoShowPara.getNote());
        esInitStlTemp.setAttachment(progInfoShowPara.getAttachment());
        esInitStlTemp.setDeletedFlag(progInfoShowPara.getDeletedFlag());
        esInitStlTemp.setEndFlag(progInfoShowPara.getEndFlag());
        esInitStlTemp.setCreatedBy(progInfoShowPara.getCreatedBy());
        esInitStlTemp.setCreatedDate(progInfoShowPara.getCreatedDate());
        esInitStlTemp.setLastUpdBy(progInfoShowPara.getLastUpdBy());
        esInitStlTemp.setLastUpdDate(progInfoShowPara.getLastUpdDate());
        esInitStlTemp.setModificationNum(progInfoShowPara.getModificationNum());
        esInitStlTemp.setAutoLinkAdd(progInfoShowPara.getAutoLinkAdd());
        return esInitStlTemp;
    }

    public EsInitStl selectRecordsByPrimaryKey(String strPkId){
        return esInitStlMapper.selectByPrimaryKey(strPkId);
    }
    public List<EsInitStl> selectRecordsByRecord(EsInitStl esInitStlPara){
        EsInitStlExample esInitStlExample=new EsInitStlExample();
        EsInitStlExample.Criteria criteria = esInitStlExample.createCriteria();
        criteria.andStlTypeEqualTo(esInitStlPara.getStlType()).
                andStlPkidEqualTo(esInitStlPara.getStlPkid()).
                andPeriodNoEqualTo(esInitStlPara.getPeriodNo());
        return esInitStlMapper.selectByExample(esInitStlExample);
    }

    public void insertRecord(ProgInfoShow progInfoShowPara){
        progInfoShowPara.setCreatedBy(ToolUtil.getOperatorManager().getOperatorId());
        progInfoShowPara.setCreatedDate(ToolUtil.getStrLastUpdDate());
        progInfoShowPara.setDeletedFlag("0");
        progInfoShowPara.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        progInfoShowPara.setLastUpdDate(ToolUtil.getStrLastUpdDate());
        esInitStlMapper.insert(fromModelShowToModel(progInfoShowPara)) ;
    }
    public void insertRecord(EsInitStl esInitStlPara){
        esInitStlPara.setCreatedBy(ToolUtil.getOperatorManager().getOperatorId());
        esInitStlPara.setCreatedDate(ToolUtil.getStrLastUpdDate());
        esInitStlPara.setDeletedFlag("0");
        esInitStlPara.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        esInitStlPara.setLastUpdDate(ToolUtil.getStrLastUpdDate());
        esInitStlMapper.insert(esInitStlPara) ;
    }
    public EsInitStl initStlData(String strStlTypePara,EsCttInfo esCttInfoPara){
        EsInitStl esInitStl=new EsInitStl();
        esInitStl.setId(getStrMaxStlId(strStlTypePara));
        esInitStl.setStlType(strStlTypePara);
        esInitStl.setStlPkid(esCttInfoPara.getPkid());
        esInitStl.setPeriodNo("NULL");
        return esInitStl;
    }
    @Transactional
    public void insertRecordForOperRes(EsCttInfo esCttInfoPara) {
        //�������������жϲ�������
        if (esCttInfoPara.getCttType().equals(ESEnum.ITEMTYPE0.getCode())) {
            //�ܰ�����
            insertRecord(initStlData(ESEnum.ITEMTYPE6.getCode(), esCttInfoPara));
            //�ܰ�ͳ��
            insertRecord(initStlData(ESEnum.ITEMTYPE7.getCode(), esCttInfoPara));
        } else if (esCttInfoPara.getCttType().equals(ESEnum.ITEMTYPE2.getCode())) {
            //�ְ���������
            insertRecord(initStlData(ESEnum.ITEMTYPE3.getCode(), esCttInfoPara));
            //�ְ����Ͻ���
            insertRecord(initStlData(ESEnum.ITEMTYPE4.getCode(), esCttInfoPara));
            //�ְ��۸����
            insertRecord(initStlData(ESEnum.ITEMTYPE5.getCode(), esCttInfoPara));
        }
    }

    @Transactional
    public void insertStlAndPowerRecord(EsInitStl esInitStlPara){
        esInitStlPara.setCreatedBy(ToolUtil.getOperatorManager().getOperatorId());
        esInitStlPara.setCreatedDate(ToolUtil.getStrLastUpdDate());
        esInitStlPara.setDeletedFlag("0");
        esInitStlPara.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        esInitStlPara.setLastUpdDate(ToolUtil.getStrLastUpdDate());
        esInitStlMapper.insert(esInitStlPara) ;
        EsInitPower esInitPowerTemp=new EsInitPower();
        esInitPowerTemp.setPowerType(ESEnum.ITEMTYPE5.getCode());
        esInitPowerTemp.setPowerPkid(esInitStlPara.getStlPkid());
        esInitPowerTemp.setPeriodNo(esInitStlPara.getPeriodNo());
        esInitPowerTemp.setStatusFlag(ESEnumStatusFlag.STATUS_FLAG2.getCode());
        esInitPowerTemp.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG3.getCode());
        flowCtrlService.insertRecordByStl(esInitPowerTemp);
    }
    @Transactional
    public void updateRecordForSubCttPApprovePass(EsInitStl esInitStlPara,List<ProgSubstlItemShow> progSubstlItemShowListForApprovePara){
        //����ǼǱ����
        esInitStlPara.setModificationNum(
                ToolUtil.getIntIgnoreNull(esInitStlPara.getModificationNum())+1);
        esInitStlPara.setDeletedFlag("0");
        esInitStlPara.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        esInitStlPara.setLastUpdDate(ToolUtil.getStrLastUpdDate());
        esInitStlMapper.updateByPrimaryKey(esInitStlPara) ;
        //Power�����
        EsInitPower esInitPowerTemp = new EsInitPower();
        esInitPowerTemp.setPowerType(esInitStlPara.getStlType());
        esInitPowerTemp.setPowerPkid(esInitStlPara.getStlPkid());
        esInitPowerTemp.setPeriodNo(esInitStlPara.getPeriodNo());
        esInitPowerTemp.setStatusFlag(ESEnumStatusFlag.STATUS_FLAG3.getCode());
        esInitPowerTemp.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG5.getCode());
        flowCtrlService.updateRecordByStl(esInitPowerTemp);
        //���۸������������ݲ�����es_item_stl_subctt_eng_p��
        for (int i=0;i<progSubstlItemShowListForApprovePara.size();i++){
            ProgSubstlItemShow itemUnit=progSubstlItemShowListForApprovePara.get(i);
            itemUnit.setEngPMng_RowNo(i);
            progSubstlItemService.insertRecordDetail(itemUnit);
        }
    }
    public void updateRecord(ProgInfoShow progInfoShowPara){
        progInfoShowPara.setModificationNum(
                ToolUtil.getIntIgnoreNull(progInfoShowPara.getModificationNum())+1);
        progInfoShowPara.setDeletedFlag("0");
        progInfoShowPara.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        progInfoShowPara.setLastUpdDate(ToolUtil.getStrLastUpdDate());
        esInitStlMapper.updateByPrimaryKey(fromModelShowToModel(progInfoShowPara)) ;
    }
    public void updateRecord(EsInitStl esInitStlPara){
        esInitStlPara.setModificationNum(
                ToolUtil.getIntIgnoreNull(esInitStlPara.getModificationNum())+1);
        esInitStlPara.setDeletedFlag("0");
        esInitStlPara.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        esInitStlPara.setLastUpdDate(ToolUtil.getStrLastUpdDate());
        esInitStlMapper.updateByPrimaryKey(esInitStlPara) ;
    }

    @Transactional
    public void deleteRecord(ProgInfoShow progInfoShowPara){
        EsInitStlExample example = new EsInitStlExample();
        example.createCriteria().andStlTypeEqualTo(progInfoShowPara.getStlType())
                .andStlPkidEqualTo(progInfoShowPara.getStlPkid())
                .andPeriodNoEqualTo(progInfoShowPara.getPeriodNo());
        esInitStlMapper.deleteByExample(example);

        flowCtrlService.deleteRecord(
                progInfoShowPara.getStlType()
                , progInfoShowPara.getStlPkid()
                , progInfoShowPara.getPeriodNo());
    }
    public void deleteRecord(EsInitStl esInitStlPara){
        EsInitStlExample example = new EsInitStlExample();
        example.createCriteria()
                .andStlTypeEqualTo(esInitStlPara.getStlType())
                .andStlPkidEqualTo(esInitStlPara.getStlPkid())
                .andPeriodNoEqualTo(esInitStlPara.getPeriodNo());
        esInitStlMapper.deleteByExample(example);

        flowCtrlService.deleteRecord(
                esInitStlPara.getStlType()
                , esInitStlPara.getStlPkid()
                , esInitStlPara.getPeriodNo());

    }
    @Transactional
    public void deleteRecordForSubCttPApprovePass(EsInitStl esInitStlPara,String powerType){
        //ɾ��stl����stl_typeΪ5�ļ�¼
        EsInitStlExample example = new EsInitStlExample();
        example.createCriteria()
                .andStlTypeEqualTo(esInitStlPara.getStlType())
                .andStlPkidEqualTo(esInitStlPara.getStlPkid())
                .andPeriodNoEqualTo(esInitStlPara.getPeriodNo());
        esInitStlMapper.deleteByExample(example);
        //ɾ��power����power_typeΪ5�ļ�¼
        flowCtrlService.deleteRecord(
                esInitStlPara.getStlType()
                , esInitStlPara.getStlPkid()
                , esInitStlPara.getPeriodNo());
        //����power����power_typeΪ3����4�ļ�¼״̬Ϊ���״̬
        EsInitPower esInitPowerTemp = new EsInitPower();
        esInitPowerTemp.setPowerType(powerType);
        esInitPowerTemp.setPowerPkid(esInitStlPara.getStlPkid());
        esInitPowerTemp.setPeriodNo(esInitStlPara.getPeriodNo());
        EsInitPower esInitPower = flowCtrlService.selectByPrimaryKey(esInitPowerTemp);
        esInitPower.setStatusFlag(ESEnumStatusFlag.STATUS_FLAG1.getCode());
        esInitPower.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG6.getCode());
        flowCtrlService.updateRecordByPower(esInitPower);
        //ɾ��es_item_stl_subctt_eng_p���е���Ӧ��¼
        progSubstlItemService.deleteRecordByExample(esInitStlPara.getStlPkid(),esInitStlPara.getPeriodNo());
    }
    public int deleteRecord(String strPkId){
        return esInitStlMapper.deleteByPrimaryKey(strPkId);
    }

    public String getStrMaxStlId(String strCttType){
        return myCttStlMapper.getStrMaxStlId(strCttType) ;
    }
}
