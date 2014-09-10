package epss.service;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumPreStatusFlag;
import epss.common.enums.ESEnumStatusFlag;
import epss.common.enums.ESEnumTaskDoneFlag;
import epss.repository.dao.FlowCtrlHisMapper;
import epss.repository.model.model_show.CttInfoShow;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private FlowCtrlHisMapper flowCtrlHisMapper;
    @Resource
    private ProgSubstlItemService progSubstlItemService;
    @Resource
    private ProgWorkqtyItemService progWorkqtyItemService;
    @Resource
    private ProgMatqtyItemService progMatqtyItemService;

    /**
     * �жϼ�¼�Ƿ��Ѵ���
     *
     * @param progInfoShowPara
     * @return
     */
    public List<EsInitStl> getInitStlListByModelShow(ProgInfoShow progInfoShowPara) {
        EsInitStlExample example = new EsInitStlExample();
        example.createCriteria().andStlTypeEqualTo(progInfoShowPara.getStlType())
                                .andStlPkidEqualTo(progInfoShowPara.getStlPkid())
                                .andPeriodNoEqualTo(progInfoShowPara.getPeriodNo());
        return esInitStlMapper.selectByExample(example);
    }
    public List<EsInitStl> getInitStlListByModelShow(EsInitStl esInitStlPara) {
        EsInitStlExample example = new EsInitStlExample();
        example.createCriteria().andStlTypeEqualTo(esInitStlPara.getStlType())
                .andStlPkidEqualTo(esInitStlPara.getStlPkid())
                .andPeriodNoEqualTo(esInitStlPara.getPeriodNo());
        return esInitStlMapper.selectByExample(example);
    }
    public List<EsInitStl> getInitStlListByModel(EsInitStl esInitStlPara) {
        EsInitStlExample example = new EsInitStlExample();
        example.createCriteria().andStlTypeEqualTo(esInitStlPara.getStlType())
                .andStlPkidEqualTo(esInitStlPara.getStlPkid());
        return esInitStlMapper.selectByExample(example);
    }

    public void accountAction(EsInitStl esInitStlPara) {
        esInitStlPara.setFlowStatus(ESEnumStatusFlag.STATUS_FLAG4.getCode());
        esInitStlPara.setFlowStatusReason(ESEnumPreStatusFlag.PRE_STATUS_FLAG7.getCode());
        flowCtrlHisMapper.insert(fromInitStlToEsInitPowerHis(esInitStlPara, "update"));
    }

    private FlowCtrlHis fromCttInfoToEsInitPowerHis(EsCttInfo cttInfoPara,String strOperType){
        FlowCtrlHis flowCtrlHisTemp =new FlowCtrlHis();
        flowCtrlHisTemp.setInfoType(cttInfoPara.getCttType());
        flowCtrlHisTemp.setInfoPkid(cttInfoPara.getPkid());
        flowCtrlHisTemp.setFlowStatus(cttInfoPara.getFlowStatus());
        flowCtrlHisTemp.setFlowStatusReason(cttInfoPara.getFlowStatusReason());
        flowCtrlHisTemp.setCreatedTime(cttInfoPara.getCreatedDate());
        flowCtrlHisTemp.setCreatedBy(cttInfoPara.getCreatedBy());
        flowCtrlHisTemp.setOperType(strOperType);
        return flowCtrlHisTemp;
    }
    private FlowCtrlHis fromCttInfoShowToEsInitPowerHis(CttInfoShow cttInfoShowPara,String strOperType){
        FlowCtrlHis flowCtrlHisTemp =new FlowCtrlHis();
        flowCtrlHisTemp.setInfoType(cttInfoShowPara.getCttType());
        flowCtrlHisTemp.setInfoPkid(cttInfoShowPara.getPkid());
        flowCtrlHisTemp.setPeriodNo("NULL");
        flowCtrlHisTemp.setFlowStatus(cttInfoShowPara.getFlowStatus());
        flowCtrlHisTemp.setFlowStatusReason(cttInfoShowPara.getFlowStatusReason());
        flowCtrlHisTemp.setCreatedTime(cttInfoShowPara.getCreatedDate());
        flowCtrlHisTemp.setCreatedBy(cttInfoShowPara.getCreatedBy());
        flowCtrlHisTemp.setOperType(strOperType);
        return flowCtrlHisTemp;
    }

    private FlowCtrlHis fromInitStlToEsInitPowerHis(EsInitStl esInitStlPara,String strOperType){
        FlowCtrlHis flowCtrlHis =new FlowCtrlHis();
        flowCtrlHis.setInfoType(esInitStlPara.getStlType());
        flowCtrlHis.setInfoPkid(esInitStlPara.getStlPkid());
        flowCtrlHis.setPeriodNo(esInitStlPara.getPeriodNo());
        flowCtrlHis.setFlowStatus(esInitStlPara.getFlowStatus());
        flowCtrlHis.setFlowStatusReason(esInitStlPara.getFlowStatusReason());
        flowCtrlHis.setCreatedTime(esInitStlPara.getCreatedDate());
        flowCtrlHis.setCreatedBy(esInitStlPara.getCreatedBy());
        flowCtrlHis.setOperType(strOperType);
        return flowCtrlHis;
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
        esInitStlTemp.setFlowStatus(progInfoShowPara.getFlowStatus());
        esInitStlTemp.setFlowStatusReason(progInfoShowPara.getFlowStatusReason());
        return esInitStlTemp;
    }

    public EsInitStl selectRecordsByPrimaryKey(String strPkId){
        return esInitStlMapper.selectByPrimaryKey(strPkId);
    }

    public void insertRecord(ProgInfoShow progInfoShowPara){
        String strOperatorIdTemp=ToolUtil.getOperatorManager().getOperatorId();
        String strLastUpdTimeTemp=ToolUtil.getStrLastUpdTime();
        progInfoShowPara.setCreatedBy(strOperatorIdTemp);
        progInfoShowPara.setCreatedDate(strLastUpdTimeTemp);
        progInfoShowPara.setDeletedFlag("0");
        progInfoShowPara.setLastUpdBy(strOperatorIdTemp);
        progInfoShowPara.setLastUpdDate(strLastUpdTimeTemp);
        esInitStlMapper.insert(fromModelShowToModel(progInfoShowPara)) ;
    }
    public void insertRecord(EsInitStl esInitStlPara){
        String strOperatorIdTemp=ToolUtil.getOperatorManager().getOperatorId();
        String strLastUpdTimeTemp=ToolUtil.getStrLastUpdTime();
        esInitStlPara.setCreatedBy(strOperatorIdTemp);
        esInitStlPara.setCreatedDate(strLastUpdTimeTemp);
        esInitStlPara.setDeletedFlag("0");
        esInitStlPara.setLastUpdBy(strOperatorIdTemp);
        esInitStlPara.setLastUpdDate(strLastUpdTimeTemp);
        esInitStlMapper.insert(esInitStlPara) ;
    }
    @Transactional
    public String insertStlQAndItemRecordAction(ProgInfoShow progInfoShowPara) {
        insertRecord(progInfoShowPara);
        progWorkqtyItemService.setFromLastStageApproveDataToThisStageBeginData(progInfoShowPara);
        return "����������ɡ�";
    }
    @Transactional
    public String insertStlMAndItemRecordAction(ProgInfoShow progInfoShowPara) {
        insertRecord(progInfoShowPara);
        progMatqtyItemService.setFromLastStageApproveDataToThisStageBeginData(progInfoShowPara);
        return "����������ɡ�";
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
        String strOperatorIdTemp=ToolUtil.getOperatorManager().getOperatorId();
        String strLastUpdTime=ToolUtil.getStrLastUpdTime();
        esInitStlPara.setFlowStatus(ESEnumStatusFlag.STATUS_FLAG2.getCode());
        esInitStlPara.setFlowStatusReason(ESEnumPreStatusFlag.PRE_STATUS_FLAG3.getCode());
        esInitStlPara.setCreatedBy(strOperatorIdTemp);
        esInitStlPara.setCreatedDate(strLastUpdTime);
        esInitStlPara.setDeletedFlag("0");
        esInitStlPara.setLastUpdBy(strOperatorIdTemp);
        esInitStlPara.setLastUpdDate(strLastUpdTime);
        esInitStlMapper.insert(esInitStlPara) ;
    }
    @Transactional
    public void updateRecordForSubCttPApprovePass(EsInitStl esInitStlPara,List<ProgSubstlItemShow> progSubstlItemShowListForApprovePara){
        //����ǼǱ����
        esInitStlPara.setModificationNum(
                ToolUtil.getIntIgnoreNull(esInitStlPara.getModificationNum())+1);
        esInitStlPara.setDeletedFlag("0");
        esInitStlPara.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        esInitStlPara.setLastUpdDate(ToolUtil.getStrLastUpdDate());
        esInitStlPara.setFlowStatus(ESEnumStatusFlag.STATUS_FLAG3.getCode());
        esInitStlPara.setFlowStatusReason(ESEnumPreStatusFlag.PRE_STATUS_FLAG5.getCode());
        esInitStlMapper.updateByPrimaryKey(esInitStlPara) ;
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
        esInitStlMapper.updateByPrimaryKey(fromModelShowToModel(progInfoShowPara));
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
    }
    public void deleteRecord(EsInitStl esInitStlPara){
        EsInitStlExample example = new EsInitStlExample();
        example.createCriteria()
                .andStlTypeEqualTo(esInitStlPara.getStlType())
                .andStlPkidEqualTo(esInitStlPara.getStlPkid())
                .andPeriodNoEqualTo(esInitStlPara.getPeriodNo());
        esInitStlMapper.deleteByExample(example);
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

        //����power����power_typeΪ3����4�ļ�¼״̬Ϊ���״̬
        example = new EsInitStlExample();
        example.createCriteria()
                .andStlTypeEqualTo(powerType)
                .andStlPkidEqualTo(esInitStlPara.getStlPkid())
                .andPeriodNoEqualTo(esInitStlPara.getPeriodNo());
        List<EsInitStl> esInitStlListTemp = esInitStlMapper.selectByExample(example);
        EsInitStl esInitStlTemp=esInitStlListTemp.get(0);
        esInitStlTemp.setFlowStatus(ESEnumStatusFlag.STATUS_FLAG1.getCode());
        esInitStlTemp.setFlowStatusReason(ESEnumPreStatusFlag.PRE_STATUS_FLAG6.getCode());
        esInitStlMapper.updateByPrimaryKey(esInitStlTemp);
        //ɾ��es_item_stl_subctt_eng_p���е���Ӧ��¼
        progSubstlItemService.deleteRecordByExample(esInitStlPara.getStlPkid(),esInitStlPara.getPeriodNo());
    }
    public int deleteRecord(String strPkId){
        return esInitStlMapper.deleteByPrimaryKey(strPkId);
    }
    @Transactional
    public String deleteStlMAndItemRecord(ProgInfoShow progInfoShowPara) {
        // ɾ����ϸ����
        int deleteItemsNum =
                progMatqtyItemService.deleteItemsByInitStlSubcttEng(
                        progInfoShowPara.getStlPkid(),
                        progInfoShowPara.getPeriodNo());
        // ɾ���Ǽ�����
        int deleteRecordOfStlMNum = deleteRecord(progInfoShowPara.getPkid());
        if (deleteItemsNum <= 0 && deleteRecordOfStlMNum <= 0) {
            return "�ü�¼��ɾ����";
        }
        return "ɾ��������ɡ�";
    }

    public String deleteStlQAndItemRecord(ProgInfoShow progInfoShowPara) {
        // ɾ����ϸ����
        int deleteItemsNum =
                progWorkqtyItemService.deleteItemsByInitStlSubcttEng(
                        progInfoShowPara.getStlPkid(),
                        progInfoShowPara.getPeriodNo());
        // ɾ���Ǽ�����
        int deleteRecordOfStlQNum = deleteRecord(progInfoShowPara.getPkid());
        if (deleteItemsNum <= 0 && deleteRecordOfStlQNum <= 0) {
            return "�ü�¼��ɾ����";
        }
        return "ɾ��������ɡ�";
    }

    public String getStrMaxStlId(String strCttType){
        return myCttStlMapper.getStrMaxStlId(strCttType) ;
    }
}
