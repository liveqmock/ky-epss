package epss.service;

import epss.common.enums.EnumResType;
import epss.common.enums.EnumFlowStatus;
import epss.common.enums.EnumFlowStatusReason;
import epss.repository.dao.FlowCtrlHisMapper;
import epss.repository.model.model_show.ProgStlInfoShow;
import org.springframework.beans.factory.annotation.Autowired;
import skyline.util.ToolUtil;
import epss.repository.dao.not_mybatis.MyProgStlInfoMapper;
import epss.repository.dao.ProgStlInfoMapper;
import epss.repository.model.*;
import epss.repository.model.model_show.ProgStlItemSubStlmentShow;
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
    private ProgStlInfoMapper progStlInfoMapper;
    @Resource
    private MyProgStlInfoMapper myProgStlInfoMapper;
    @Resource
    private FlowCtrlHisMapper flowCtrlHisMapper;
    @Resource
    private ProgSubstlItemService progSubstlItemService;
    @Resource
    private ProgWorkqtyItemService progWorkqtyItemService;
    @Resource
    private ProgMatqtyItemService progMatqtyItemService;

    // �жϼ�¼�Ƿ����
    public List<ProgStlInfo> getInitStlListByModelShow(ProgStlInfoShow progStlInfoShowPara) {
        ProgStlInfo progStlInfoTemp=fromModelShowToModel(progStlInfoShowPara);
        progStlInfoTemp.setFlowStatus(null);
        return getInitStlListByModel(progStlInfoTemp);
    }

    public List<ProgStlInfo> getInitStlListByModel(ProgStlInfo progStlInfoPara) {
        ProgStlInfoExample example = new ProgStlInfoExample();
        ProgStlInfoExample.Criteria criteria = example.createCriteria();
        criteria
                .andStlTypeEqualTo(progStlInfoPara.getStlType())
                .andStlPkidEqualTo(progStlInfoPara.getStlPkid())
                .andPeriodNoEqualTo(progStlInfoPara.getPeriodNo());
        //����ΪNULL����
        if(!ToolUtil.getStrIgnoreNull(progStlInfoPara.getFlowStatus()).equals("")){
            criteria.andFlowStatusEqualTo(progStlInfoPara.getFlowStatus());
        }
        return progStlInfoMapper.selectByExample(example);
    }

    public void accountAction(ProgStlInfo progStlInfoPara) {
        progStlInfoPara.setFlowStatus(EnumFlowStatus.FLOW_STATUS4.getCode());
        progStlInfoPara.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON7.getCode());
        flowCtrlHisMapper.insert(fromProgStlInfoToFlowCtrlHis(progStlInfoPara, "update"));
    }

    public FlowCtrlHis fromProgStlInfoToFlowCtrlHis(ProgStlInfo progStlInfoPara,String strOperType){
        FlowCtrlHis flowCtrlHis =new FlowCtrlHis();
        flowCtrlHis.setInfoType(progStlInfoPara.getStlType());
        flowCtrlHis.setInfoPkid(progStlInfoPara.getStlPkid());
        flowCtrlHis.setPeriodNo(progStlInfoPara.getPeriodNo());
        flowCtrlHis.setFlowStatus(progStlInfoPara.getFlowStatus());
        flowCtrlHis.setFlowStatusReason(progStlInfoPara.getFlowStatusReason());
        flowCtrlHis.setCreatedTime(progStlInfoPara.getCreatedTime());
        flowCtrlHis.setCreatedBy(progStlInfoPara.getCreatedBy());
        flowCtrlHis.setOperType(strOperType);
        return flowCtrlHis;
    }
    public ProgStlInfo fromModelShowToModel(ProgStlInfoShow progStlInfoShowPara){
        ProgStlInfo progStlInfoTemp =new ProgStlInfo();
        progStlInfoTemp.setPkid(progStlInfoShowPara.getPkid());
        progStlInfoTemp.setStlType(progStlInfoShowPara.getStlType());
        progStlInfoTemp.setStlPkid(progStlInfoShowPara.getStlPkid());
        progStlInfoTemp.setId(progStlInfoShowPara.getId());
        progStlInfoTemp.setPeriodNo(progStlInfoShowPara.getPeriodNo());
        progStlInfoTemp.setRemark(progStlInfoShowPara.getRemark());
        progStlInfoTemp.setAttachment(progStlInfoShowPara.getAttachment());
        progStlInfoTemp.setArchivedFlag(progStlInfoShowPara.getArchivedFlag());
        progStlInfoTemp.setCreatedBy(progStlInfoShowPara.getCreatedBy());
        progStlInfoTemp.setCreatedTime(progStlInfoShowPara.getCreatedTime());
        progStlInfoTemp.setLastUpdBy(progStlInfoShowPara.getLastUpdBy());
        progStlInfoTemp.setLastUpdTime(progStlInfoShowPara.getLastUpdTime());
        progStlInfoTemp.setRecversion(progStlInfoShowPara.getRecversion());
        progStlInfoTemp.setAutoLinkAdd(progStlInfoShowPara.getAutoLinkAdd());
        progStlInfoTemp.setFlowStatus(progStlInfoShowPara.getFlowStatus());
        progStlInfoTemp.setFlowStatusReason(progStlInfoShowPara.getFlowStatusReason());
        return progStlInfoTemp;
    }
    public ProgStlInfoShow fromModelToModelShow(ProgStlInfo progStlInfoPara){
        ProgStlInfoShow progStlInfoShowTemp =new ProgStlInfoShow();
        progStlInfoShowTemp.setPkid(progStlInfoPara.getPkid());
        progStlInfoShowTemp.setStlType(progStlInfoPara.getStlType());
        progStlInfoShowTemp.setStlPkid(progStlInfoPara.getStlPkid());
        progStlInfoShowTemp.setId(progStlInfoPara.getId());
        progStlInfoShowTemp.setPeriodNo(progStlInfoPara.getPeriodNo());
        progStlInfoShowTemp.setRemark(progStlInfoPara.getRemark());
        progStlInfoShowTemp.setAttachment(progStlInfoPara.getAttachment());
        progStlInfoShowTemp.setArchivedFlag(progStlInfoPara.getArchivedFlag());
        progStlInfoShowTemp.setCreatedBy(progStlInfoPara.getCreatedBy());
        progStlInfoShowTemp.setCreatedTime(progStlInfoPara.getCreatedTime());
        progStlInfoShowTemp.setLastUpdBy(progStlInfoPara.getLastUpdBy());
        progStlInfoShowTemp.setLastUpdTime(progStlInfoPara.getLastUpdTime());
        progStlInfoShowTemp.setRecversion(progStlInfoPara.getRecversion());
        progStlInfoShowTemp.setAutoLinkAdd(progStlInfoPara.getAutoLinkAdd());
        progStlInfoShowTemp.setFlowStatus(progStlInfoPara.getFlowStatus());
        progStlInfoShowTemp.setFlowStatusReason(progStlInfoPara.getFlowStatusReason());
        return progStlInfoShowTemp;
    }

    public ProgStlInfo selectRecordsByPrimaryKey(String strPkId){
        return progStlInfoMapper.selectByPrimaryKey(strPkId);
    }

    public void insertRecord(ProgStlInfoShow progStlInfoShowPara){
        String strOperatorIdTemp=ToolUtil.getOperatorManager().getOperatorId();
        String strLastUpdTimeTemp=ToolUtil.getStrLastUpdTime();
        progStlInfoShowPara.setCreatedBy(strOperatorIdTemp);
        progStlInfoShowPara.setCreatedTime(strLastUpdTimeTemp);
        progStlInfoShowPara.setArchivedFlag("0");
        progStlInfoShowPara.setLastUpdBy(strOperatorIdTemp);
        progStlInfoShowPara.setLastUpdTime(strLastUpdTimeTemp);
        progStlInfoMapper.insert(fromModelShowToModel(progStlInfoShowPara)) ;
    }
    public void insertRecord(ProgStlInfo progStlInfoPara){
        String strOperatorIdTemp=ToolUtil.getOperatorManager().getOperatorId();
        String strLastUpdTimeTemp=ToolUtil.getStrLastUpdTime();
        progStlInfoPara.setCreatedBy(strOperatorIdTemp);
        progStlInfoPara.setCreatedTime(strLastUpdTimeTemp);
        progStlInfoPara.setArchivedFlag("0");
        progStlInfoPara.setLastUpdBy(strOperatorIdTemp);
        progStlInfoPara.setLastUpdTime(strLastUpdTimeTemp);
        progStlInfoMapper.insert(progStlInfoPara) ;
    }

    @Transactional
    public String insertStlQAndItemRecordAction(ProgStlInfoShow progStlInfoShowPara) {
        insertRecord(progStlInfoShowPara);
        progWorkqtyItemService.setFromLastStageApproveDataToThisStageBeginData(progStlInfoShowPara);
        return "����������ɡ�";
    }
    @Transactional
    public String insertStlMAndItemRecordAction(ProgStlInfoShow progStlInfoShowPara) {
        insertRecord(progStlInfoShowPara);
        progMatqtyItemService.setFromLastStageApproveDataToThisStageBeginData(progStlInfoShowPara);
        return "����������ɡ�";
    }
    public ProgStlInfo initStlData(String strStlTypePara,CttInfo cttInfoPara){
        ProgStlInfo progStlInfo =new ProgStlInfo();
        progStlInfo.setId(getStrMaxStlId(strStlTypePara));
        progStlInfo.setStlType(strStlTypePara);
        progStlInfo.setStlPkid(cttInfoPara.getPkid());
        progStlInfo.setPeriodNo("NULL");
        return progStlInfo;
    }
    @Transactional
    public void insertRecordForOperRes(CttInfo cttInfoPara) {
        //�������������жϲ�������
        if (cttInfoPara.getCttType().equals(EnumResType.RES_TYPE0.getCode())) {
            //�ܰ�����
            insertRecord(initStlData(EnumResType.RES_TYPE6.getCode(), cttInfoPara));
            //�ܰ�ͳ��
            insertRecord(initStlData(EnumResType.RES_TYPE7.getCode(), cttInfoPara));
        } else if (cttInfoPara.getCttType().equals(EnumResType.RES_TYPE2.getCode())) {
            //�ְ���������
            insertRecord(initStlData(EnumResType.RES_TYPE3.getCode(), cttInfoPara));
            //�ְ����Ͻ���
            insertRecord(initStlData(EnumResType.RES_TYPE4.getCode(), cttInfoPara));
            //�ְ��۸����
            insertRecord(initStlData(EnumResType.RES_TYPE5.getCode(), cttInfoPara));
        }
    }

    @Transactional
    public void updateRecordForSubCttPApprovePass(
            ProgStlInfo progStlInfoPara,
            List<ProgStlItemSubStlmentShow> progStlItemSubStlmentShowListForApprovePara){
        //����ǼǱ����
        progStlInfoPara.setRecversion(
                ToolUtil.getIntIgnoreNull(progStlInfoPara.getRecversion())+1);
        progStlInfoPara.setArchivedFlag("0");
        progStlInfoPara.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        progStlInfoPara.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        progStlInfoPara.setFlowStatus(EnumFlowStatus.FLOW_STATUS3.getCode());
        progStlInfoPara.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON5.getCode());
        progStlInfoMapper.updateByPrimaryKey(progStlInfoPara) ;
        //���۸������������ݲ�����PROG_STL_ITEM_SUB_STLMENT��
        for (int i=0;i< progStlItemSubStlmentShowListForApprovePara.size();i++){
            ProgStlItemSubStlmentShow itemUnit= progStlItemSubStlmentShowListForApprovePara.get(i);
            itemUnit.setEngPMng_RowNo(i);
            progSubstlItemService.insertRecordDetail(itemUnit);
        }
    }
    @Transactional
    public void updateRecord(ProgStlInfoShow progStlInfoShowPara){
        progStlInfoShowPara.setRecversion(
                ToolUtil.getIntIgnoreNull(progStlInfoShowPara.getRecversion())+1);
        progStlInfoShowPara.setArchivedFlag("0");
        progStlInfoShowPara.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        progStlInfoShowPara.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        progStlInfoMapper.updateByPrimaryKey(fromModelShowToModel(progStlInfoShowPara));
    }
    @Transactional
    public void updateRecord(ProgStlInfo progStlInfoPara) {
        progStlInfoPara.setRecversion(
                ToolUtil.getIntIgnoreNull(progStlInfoPara.getRecversion()) + 1);
        progStlInfoPara.setArchivedFlag("0");
        progStlInfoPara.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        progStlInfoPara.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        progStlInfoMapper.updateByPrimaryKey(progStlInfoPara);

        // ��������
        if (EnumResType.RES_TYPE3.getCode().equals(progStlInfoPara.getStlType())) {
            // �ȿ����ְ���ͬ�����ڵķְ����㵥
            ProgStlInfo progStlInfoSubStlmentTemp=new ProgStlInfo();
            progStlInfoSubStlmentTemp.setStlType(EnumResType.RES_TYPE5.getCode());
            progStlInfoSubStlmentTemp.setStlPkid(progStlInfoPara.getStlPkid());
            progStlInfoSubStlmentTemp.setPeriodNo(progStlInfoPara.getPeriodNo());
            // ״̬Ϊ���ˣ���״̬ԭ��Ϊ����ͨ��
            if (EnumFlowStatus.FLOW_STATUS2.getCode().equals(progStlInfoPara.getFlowStatus()) &&
                    EnumFlowStatusReason.FLOW_STATUS_REASON3.getCode().equals(progStlInfoPara.getFlowStatusReason())) {
                // ���ְ���ͬ�����ڵķְ����㵥������
                if(getInitStlListByModel(progStlInfoSubStlmentTemp).size()<=0) {
                    ProgStlInfo progStlInfoSubMTemp=new ProgStlInfo();
                    progStlInfoSubMTemp.setStlType(EnumResType.RES_TYPE4.getCode());
                    progStlInfoSubMTemp.setStlPkid(progStlInfoPara.getStlPkid());
                    progStlInfoSubMTemp.setPeriodNo(progStlInfoPara.getPeriodNo());
                    progStlInfoSubMTemp.setFlowStatus(EnumFlowStatus.FLOW_STATUS2.getCode());
                    // ���ְ���ͬ�����ڵķְ����Ͻ���Ҳ������
                    if(getInitStlListByModel(progStlInfoSubMTemp).size()>0) {
                        progStlInfoSubStlmentTemp.setId(getStrMaxStlId(progStlInfoSubStlmentTemp.getStlType()));
                        progStlInfoSubStlmentTemp.setFlowStatus(EnumFlowStatus.FLOW_STATUS2.getCode());
                        progStlInfoSubStlmentTemp.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON3.getCode());
                        insertRecord(progStlInfoSubStlmentTemp);
                    }
                }
            }
            if(EnumFlowStatusReason.FLOW_STATUS_REASON4.getCode().equals(progStlInfoPara.getFlowStatusReason())){
                deleteRecord(progStlInfoSubStlmentTemp);
            }
        }else
        // ���Ͻ���
        if (EnumResType.RES_TYPE4.getCode().equals(progStlInfoPara.getStlType())) {
            // �ȿ����ְ���ͬ�����ڵķְ����㵥
            ProgStlInfo progStlInfoSubStlmentTemp=new ProgStlInfo();
            progStlInfoSubStlmentTemp.setStlType(EnumResType.RES_TYPE5.getCode());
            progStlInfoSubStlmentTemp.setStlPkid(progStlInfoPara.getStlPkid());
            progStlInfoSubStlmentTemp.setPeriodNo(progStlInfoPara.getPeriodNo());
            // ״̬Ϊ���ˣ���״̬ԭ��Ϊ����ͨ��
            if (EnumFlowStatus.FLOW_STATUS2.getCode().equals(progStlInfoPara.getFlowStatus()) &&
                    EnumFlowStatusReason.FLOW_STATUS_REASON3.getCode().equals(progStlInfoPara.getFlowStatusReason())) {
                // ���ְ���ͬ�����ڵķְ����㵥������
                if(getInitStlListByModel(progStlInfoSubStlmentTemp).size()<=0) {
                    ProgStlInfo progStlInfoSubMTemp=new ProgStlInfo();
                    progStlInfoSubMTemp.setStlType(EnumResType.RES_TYPE3.getCode());
                    progStlInfoSubMTemp.setStlPkid(progStlInfoPara.getStlPkid());
                    progStlInfoSubMTemp.setPeriodNo(progStlInfoPara.getPeriodNo());
                    progStlInfoSubMTemp.setFlowStatus(EnumFlowStatus.FLOW_STATUS2.getCode());
                    // ���ְ���ͬ�����ڵķְ����Ͻ���Ҳ������
                    if(getInitStlListByModel(progStlInfoSubMTemp).size()>0) {
                        progStlInfoSubStlmentTemp.setId(getStrMaxStlId(progStlInfoSubStlmentTemp.getStlType()));
                        progStlInfoSubStlmentTemp.setFlowStatus(EnumFlowStatus.FLOW_STATUS2.getCode());
                        progStlInfoSubStlmentTemp.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON3.getCode());
                        insertRecord(progStlInfoSubStlmentTemp);
                    }
                }
            }
            if(EnumFlowStatusReason.FLOW_STATUS_REASON4.getCode().equals(progStlInfoPara.getFlowStatusReason())){
                deleteRecord(progStlInfoSubStlmentTemp);
            }
        }
    }

    public String getMaxPeriodNo(String stlType, String subCttPkid) {
        return myProgStlInfoMapper.getMaxPeriodNo(stlType,subCttPkid);
    }

    public String subCttStlCheckForMng(String stlType,String cttPkid,String periodNo) {
        String strReturnTemp="";
        if(EnumResType.RES_TYPE3.getCode().equals(stlType)|| EnumResType.RES_TYPE4.getCode().equals(stlType)){ //�ְ�����
            String quantityMaxPeriod = ToolUtil.getStrIgnoreNull(
                    getMaxPeriodNo(EnumResType.RES_TYPE3.getCode(),cttPkid));
            String materialMaxPeriod = ToolUtil.getStrIgnoreNull(
                    getMaxPeriodNo(EnumResType.RES_TYPE4.getCode(),cttPkid));
            ProgStlInfo progStlInfoTemp=new ProgStlInfo();
            progStlInfoTemp.setStlType(EnumResType.RES_TYPE3.getCode());
            progStlInfoTemp.setStlPkid(cttPkid);
            progStlInfoTemp.setPeriodNo(quantityMaxPeriod);
            String quantityStatus=ToolUtil.getStrIgnoreNull(
                    getInitStlListByModel(progStlInfoTemp).get(0).getFlowStatus());

            progStlInfoTemp.setStlType(EnumResType.RES_TYPE4.getCode());
            String materialStatus=ToolUtil.getStrIgnoreNull(
                    getInitStlListByModel(progStlInfoTemp).get(0).getFlowStatus());

            System.out.println("\n������������ںţ�"+quantityMaxPeriod+"\n���Ͻ�������ںţ�"+materialMaxPeriod);
            System.out.println("\n������������ںŶ�Ӧ״̬��־��"+quantityStatus+"\n���Ͻ�������ںŶ�Ӧ״̬��־��"+materialStatus);

            if (EnumResType.RES_TYPE3.getCode().equals(stlType)){
                if ("".equals(quantityMaxPeriod)){
                    return strReturnTemp;
                }
                if (periodNo.compareTo(quantityMaxPeriod)<=0){ //���Ⱥ�����Ƚ��ںŴ�С�����������С���ߵ�������¼��
                    strReturnTemp="Ӧ¼�����[" + quantityMaxPeriod + "]�ڵķְ�������������!";
                    return strReturnTemp;
                }else {
                    if (quantityStatus.equals("")&&!quantityMaxPeriod.equals("")){
                        strReturnTemp="�ְ����������["+quantityMaxPeriod+"]�����ݻ�δ��׼ͨ��������¼��������!";
                        return strReturnTemp;
                    }
                    if (EnumFlowStatus.FLOW_STATUS2.getCode().compareTo(quantityStatus)>0){ //�ж��Ƿ��з���׼״̬�����ݴ��ڣ�����в���¼��
                        if (!quantityStatus.equals("")){
                            strReturnTemp="�ְ����������["+quantityMaxPeriod+"]�����ݻ�δ��׼ͨ��������¼�������ݣ�";
                            return strReturnTemp;
                        }else {
                            if (quantityMaxPeriod.compareTo(materialMaxPeriod)<0&&periodNo.compareTo(materialMaxPeriod)!=0){
                                strReturnTemp="��["+materialMaxPeriod+"]�ڷְ����Ͻ����Ѿ���ʼ����¼��["+materialMaxPeriod+"]�ڵķְ������������ݣ�";
                                return strReturnTemp;
                            }
                        }
                    } else{//��>quantityMaxPeriod &&=3�����������������������������£���ʱ˵��������Ƚ�û�����⣬������Ҫ�Ͳ��Ͻ���Ƚ�
                        if (quantityMaxPeriod.compareTo(materialMaxPeriod)<0){
                            if (periodNo.compareTo(materialMaxPeriod)!=0){
                                strReturnTemp="��["+materialMaxPeriod+"]�ڷְ����Ͻ����Ѿ���ʼ����¼��["+materialMaxPeriod+"]�ڵķְ������������ݣ�";
                                return strReturnTemp;
                            }
                        }
                    }
                }
            }
            if (EnumResType.RES_TYPE4.getCode().equals(stlType)){
                if ("".equals(materialMaxPeriod)){
                    return strReturnTemp;
                }
                if (periodNo.compareTo(materialMaxPeriod)<=0){ //���Ⱥ�����Ƚ��ںŴ�С�����������С���ߵ�������¼��
                    strReturnTemp="Ӧ¼�����["+materialMaxPeriod+"]�ڵķְ����Ͻ�������!";
                    return strReturnTemp;
                }else {
                    if (materialStatus.equals("")&&!materialMaxPeriod.equals("")){
                        strReturnTemp="�ְ����Ͻ����["+materialMaxPeriod+"]�����ݻ�δ��׼ͨ��������¼��������!";
                        return strReturnTemp;
                    }

                    if (EnumFlowStatus.FLOW_STATUS2.getCode().compareTo(materialStatus)>0){ //�ж��Ƿ��з���׼״̬�����ݴ��ڣ�����в���¼��
                        if (!materialMaxPeriod.equals("")){
                            strReturnTemp="�ְ����Ͻ����["+materialMaxPeriod+"]�����ݻ�δ��׼ͨ��������¼�������ݣ�";
                            return strReturnTemp;
                        }else {
                            if (materialMaxPeriod.compareTo(quantityMaxPeriod)<0&&periodNo.compareTo(quantityMaxPeriod)!=0){
                                strReturnTemp="��["+quantityMaxPeriod+"]�ڷְ����������Ѿ���ʼ����¼��["+quantityMaxPeriod+"]�ڵķְ����Ͻ������ݣ�";
                                return strReturnTemp;
                            }
                        }
                    } else{//��>materialMaxPeriod &&=3�����������������������������£���ʱ˵��������Ƚ�û�����⣬������Ҫ�Ͳ��Ͻ���Ƚ�
                        if (materialMaxPeriod.compareTo(quantityMaxPeriod)<0){
                            if (periodNo.compareTo(quantityMaxPeriod)!=0){
                                strReturnTemp="��["+quantityMaxPeriod+"]�ڷְ����������Ѿ���ʼ����¼��["+quantityMaxPeriod+"]�ڵķְ����Ͻ������ݣ�";
                                return strReturnTemp;
                            }
                        }
                    }
                }
            }
        }else if(EnumResType.RES_TYPE6.getCode().equals(stlType)|| EnumResType.RES_TYPE7.getCode().equals(stlType)){// �ܰ�����
            if (EnumResType.RES_TYPE6.getCode().equals(stlType)){
                String strStaQtyMaxPeriod = ToolUtil.getStrIgnoreNull(
                        getMaxPeriodNo(EnumResType.RES_TYPE6.getCode(),cttPkid));

                ProgStlInfo progStlInfoTemp=new ProgStlInfo();
                progStlInfoTemp.setStlType(EnumResType.RES_TYPE6.getCode());
                progStlInfoTemp.setStlPkid(cttPkid);
                progStlInfoTemp.setPeriodNo(strStaQtyMaxPeriod);
                String strStaQtyMaxPeriodStatus = ToolUtil.getStrIgnoreNull(
                        getInitStlListByModel(progStlInfoTemp).get(0).getFlowStatus());

                if (periodNo.compareTo(strStaQtyMaxPeriod)<=0){ //���Ⱥ�����Ƚ��ںŴ�С�����������С���ߵ�������¼��
                    strReturnTemp="Ӧ¼�����[" + strStaQtyMaxPeriod + "]�ڵ��ܰ�ͳ������!";
                    return strReturnTemp;
                }else {
                    if (strStaQtyMaxPeriod.equals("")&&!strStaQtyMaxPeriodStatus.equals("")){
                        strReturnTemp="�ܰ�ͳ�ƽ����["+strStaQtyMaxPeriod+"]�����ݻ�δ��׼ͨ��������¼��������!";
                        return strReturnTemp;
                    }
                    if (EnumFlowStatus.FLOW_STATUS3.getCode().compareTo(strStaQtyMaxPeriodStatus)>0){ //�ж��Ƿ��з���׼״̬�����ݴ��ڣ�����в���¼��
                        if (!strStaQtyMaxPeriodStatus.equals("")){
                            strReturnTemp="�ܰ�ͳ�ƽ����["+strStaQtyMaxPeriod+"]�����ݻ�δ��׼ͨ��������¼�������ݣ�";
                            return strReturnTemp;
                        }
                    }
                }
            }else{
                if (EnumResType.RES_TYPE7.getCode().equals(stlType)){
                    String strMeaQtyMaxPeriod = ToolUtil.getStrIgnoreNull(
                            getMaxPeriodNo(EnumResType.RES_TYPE7.getCode(),cttPkid));
                    ProgStlInfo progStlInfoTemp=new ProgStlInfo();
                    progStlInfoTemp.setStlType(EnumResType.RES_TYPE7.getCode());
                    progStlInfoTemp.setStlPkid(cttPkid);
                    progStlInfoTemp.setPeriodNo(strMeaQtyMaxPeriod);
                    String strMeaQtyMaxPeriodStatus = ToolUtil.getStrIgnoreNull(
                            getInitStlListByModel(progStlInfoTemp).get(0).getFlowStatus());

                    if (periodNo.compareTo(strMeaQtyMaxPeriod)<=0){ //���Ⱥ�����Ƚ��ںŴ�С�����������С���ߵ�������¼��
                        strReturnTemp="Ӧ¼�����[" + strMeaQtyMaxPeriod + "]�ڵ��ܰ�ͳ������!";
                        return strReturnTemp;
                    }else {
                        if (strMeaQtyMaxPeriod.equals("")&&!strMeaQtyMaxPeriodStatus.equals("")){
                            strReturnTemp="�ܰ�ͳ�ƽ����["+strMeaQtyMaxPeriod+"]�����ݻ�δ��׼ͨ��������¼��������!";
                            return strReturnTemp;
                        }
                        if (EnumFlowStatus.FLOW_STATUS3.getCode().compareTo(strMeaQtyMaxPeriodStatus)>0){ //�ж��Ƿ��з���׼״̬�����ݴ��ڣ�����в���¼��
                            if (!strMeaQtyMaxPeriodStatus.equals("")){
                                strReturnTemp="�ܰ�ͳ�ƽ����["+strMeaQtyMaxPeriod+"]�����ݻ�δ��׼ͨ��������¼�������ݣ�";
                                return strReturnTemp;
                            }
                        }
                    }
                }
            }

        }
        return strReturnTemp;
    }

    @Transactional
    public void deleteRecord(ProgStlInfoShow progStlInfoShowPara){
        ProgStlInfoExample example = new ProgStlInfoExample();
        example.createCriteria().andStlTypeEqualTo(progStlInfoShowPara.getStlType())
                .andStlPkidEqualTo(progStlInfoShowPara.getStlPkid())
                .andPeriodNoEqualTo(progStlInfoShowPara.getPeriodNo());
        progStlInfoMapper.deleteByExample(example);
    }
    public void deleteRecord(ProgStlInfo progStlInfoPara){
        ProgStlInfoExample example = new ProgStlInfoExample();
        example.createCriteria()
                .andStlTypeEqualTo(progStlInfoPara.getStlType())
                .andStlPkidEqualTo(progStlInfoPara.getStlPkid())
                .andPeriodNoEqualTo(progStlInfoPara.getPeriodNo());
        progStlInfoMapper.deleteByExample(example);
    }
    @Transactional
    public void deleteRecordForSubCttPApprovePass(ProgStlInfo progStlInfoPara,String powerType){
        //ɾ��stl����stl_typeΪ5�ļ�¼
        ProgStlInfoExample example = new ProgStlInfoExample();
        example.createCriteria()
                .andStlTypeEqualTo(progStlInfoPara.getStlType())
                .andStlPkidEqualTo(progStlInfoPara.getStlPkid())
                .andPeriodNoEqualTo(progStlInfoPara.getPeriodNo());
        progStlInfoMapper.deleteByExample(example);

        //����power����power_typeΪ3����4�ļ�¼״̬Ϊ���״̬
        example = new ProgStlInfoExample();
        example.createCriteria()
                .andStlTypeEqualTo(powerType)
                .andStlPkidEqualTo(progStlInfoPara.getStlPkid())
                .andPeriodNoEqualTo(progStlInfoPara.getPeriodNo());
        List<ProgStlInfo> progStlInfoListTemp = progStlInfoMapper.selectByExample(example);
        ProgStlInfo progStlInfoTemp = progStlInfoListTemp.get(0);
        progStlInfoTemp.setFlowStatus(EnumFlowStatus.FLOW_STATUS1.getCode());
        progStlInfoTemp.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON6.getCode());
        progStlInfoMapper.updateByPrimaryKey(progStlInfoTemp);
        //ɾ��PROG_STL_ITEM_SUB_STLMENT���е���Ӧ��¼
        progSubstlItemService.deleteRecordByExample(progStlInfoPara.getStlPkid(), progStlInfoPara.getPeriodNo());
    }
    public int deleteRecord(String strPkId){
        return progStlInfoMapper.deleteByPrimaryKey(strPkId);
    }
    @Transactional
    public String deleteStlMAndItemRecord(ProgStlInfoShow progStlInfoShowPara) {
        // ɾ����ϸ����
        int deleteItemsNum =
                progMatqtyItemService.deleteItemsByInitStlSubcttEng(
                        progStlInfoShowPara.getStlPkid(),
                        progStlInfoShowPara.getPeriodNo());
        // ɾ���Ǽ�����
        int deleteRecordOfStlMNum = deleteRecord(progStlInfoShowPara.getPkid());
        if (deleteItemsNum <= 0 && deleteRecordOfStlMNum <= 0) {
            return "�ü�¼��ɾ����";
        }
        return "ɾ��������ɡ�";
    }

    public String deleteStlQAndItemRecord(ProgStlInfoShow progStlInfoShowPara) {
        // ɾ����ϸ����
        int deleteItemsNum =
                progWorkqtyItemService.deleteItemsByInitStlSubcttEng(
                        progStlInfoShowPara.getStlPkid(),
                        progStlInfoShowPara.getPeriodNo());
        // ɾ���Ǽ�����
        int deleteRecordOfStlQNum = deleteRecord(progStlInfoShowPara.getPkid());
        if (deleteItemsNum <= 0 && deleteRecordOfStlQNum <= 0) {
            return "�ü�¼��ɾ����";
        }
        return "ɾ��������ɡ�";
    }

    public String getStrMaxStlId(String strCttType){
        return myProgStlInfoMapper.getStrMaxStlId(strCttType) ;
    }


    public String getLatestDoubleCkeckedPeriodNo(String strPowerType,String strPowerPkid) {
        return myProgStlInfoMapper.getLatestDoubleCkeckedPeriodNo(strPowerType, strPowerPkid);
    }

    public String getLatestApprovedPeriodNo(String strPowerType,String strPowerPkid) {
        return myProgStlInfoMapper.getLatestApprovedPeriodNo(strPowerType, strPowerPkid);
    }

    public String getLatestApprovedPeriodNoByEndPeriod(String strPowerType,String strPowerPkid,String strEndPeriodNo) {
        return myProgStlInfoMapper.getLatestApprovedPeriodNoByEndPeriod(strPowerType, strPowerPkid,strEndPeriodNo);
    }

    public List<ProgStlInfo> selectIsUsedInQMPBySubcttPkid(String strSubcttPkid){
        return myProgStlInfoMapper.selectIsUsedInQMPBySubcttPkid(strSubcttPkid);
    }

    public List<ProgStlInfoShow> selectSubcttStlQMByStatusFlagBegin_End(ProgStlInfoShow progStlInfoShowPara){
        return myProgStlInfoMapper.selectSubcttStlQMByStatusFlagBegin_End(progStlInfoShowPara);
    }

    public List<ProgStlInfoShow> selectNotFormEsInitSubcttStlP(String strParentPkid,
                                                               String strStlPkid,
                                                               String strPeriodNo){
        return myProgStlInfoMapper.selectNotFormEsInitSubcttStlP(strParentPkid, strStlPkid, strPeriodNo);
    }
    public List<ProgStlInfoShow> selectFormPreEsInitSubcttStlP(String strParentPkid,
                                                               String strStlPkid,
                                                               String strPeriodNo){
        return myProgStlInfoMapper.selectFormPreEsInitSubcttStlP(strParentPkid,strStlPkid,strPeriodNo);
    }
    public List<ProgStlInfoShow> selectFormingEsInitSubcttStlP(String strParentPkid,
                                                               String strStlPkid,
                                                               String strPeriodNo){
        return myProgStlInfoMapper.selectFormingEsInitSubcttStlP(strParentPkid,strStlPkid,strPeriodNo);
    }
    public List<ProgStlInfoShow> selectFormedEsInitSubcttStlP(String strParentPkid,
                                                              String strStlPkid,
                                                              String strPeriodNo){
        return myProgStlInfoMapper.selectFormedEsInitSubcttStlPList(strParentPkid,strStlPkid,strPeriodNo);
    }

    public List<ProgStlInfoShow> getFormedAfterEsInitSubcttStlPList(String strParentPkid,
                                                                    String strStlPkid,
                                                                    String strPeriodNo){
        return myProgStlInfoMapper.getFormedAfterEsInitSubcttStlPList(strParentPkid,strStlPkid,strPeriodNo);
    }

    public List<ProgStlInfoShow> selectTkcttStlSMByStatusFlagBegin_End(ProgStlInfoShow progStlInfoShowPara){
        return myProgStlInfoMapper.selectTkcttStlSMByStatusFlagBegin_End(progStlInfoShowPara);
    }
}
