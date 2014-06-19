package epss.service;

import epss.common.enums.EnumFlowStatus;
import epss.common.utils.ToolUtil;
import epss.repository.dao.ProgMatqtyInfoMapper;
import epss.repository.dao.notMyBits.CommonMapper;
import epss.repository.dao.notMyBits.MyProgMatqtyInfoMapper;
import epss.repository.dao.notMyBits.MyProgWorkqtyInfoMapper;
import epss.repository.model.ProgMatqtyInfo;
import epss.repository.model.ProgMatqtyInfoExample;
import epss.repository.model.model_show.ProgMatqtyInfoShow;
import org.springframework.stereotype.Service;
import platform.service.PlatformService;

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
public class ProgMatqtyInfoService {
    @Resource
    private ProgMatqtyInfoMapper progMatqtyInfoMapper;
    @Resource
    private MyProgMatqtyInfoMapper myProgMatqtyInfoMapper;
    @Resource
    private MyProgWorkqtyInfoMapper myProgWorkqtyInfoMapper;
    @Resource
    private PlatformService platformService;
    @Resource
    private CommonMapper commonMapper;

    /**
     * �жϼ�¼�Ƿ��Ѵ���
     *
     * @param progMatqtyInfoShowPara
     * @return
     */
    public List<ProgMatqtyInfo> getProgMatqtyInfoListByModel(ProgMatqtyInfoShow progMatqtyInfoShowPara) {
        ProgMatqtyInfoExample example = new ProgMatqtyInfoExample();
        example.createCriteria().andSubcttInfoPkidEqualTo(progMatqtyInfoShowPara.getSubcttInfoPkid())
                                .andStageNoEqualTo(progMatqtyInfoShowPara.getStageNo());
        return progMatqtyInfoMapper.selectByExample(example);
    }

    public List<ProgMatqtyInfo> getProgMatqtyInfoListByModel(ProgMatqtyInfo progMatqtyInfoPara) {
        ProgMatqtyInfoExample example = new ProgMatqtyInfoExample();
        example.createCriteria().andSubcttInfoPkidEqualTo(progMatqtyInfoPara.getSubcttInfoPkid())
                .andStageNoEqualTo(progMatqtyInfoPara.getStageNo());
        return progMatqtyInfoMapper.selectByExample(example);
    }

    public List<ProgMatqtyInfoShow> getProgMatqtyInfoListByFlowStatusBegin_End(ProgMatqtyInfoShow progMatqtyInfoShowPara){
        return myProgMatqtyInfoMapper.getProgMatqtyInfoListByFlowStatusBegin_End(progMatqtyInfoShowPara);
    }

    public String getStrMaxProgMatqtyInfoId(){
        return myProgMatqtyInfoMapper.getStrMaxProgMatqtyInfoId();
    }

    public String getLatestStageNo(String strSubcttInfoPkidPara,String strFlowStatusPara) {
        return myProgMatqtyInfoMapper.getLatestStageNo(strSubcttInfoPkidPara,strFlowStatusPara);
    }

    public String getLatestStageNoByEndStage(String strSubcttInfoPkidPara,
                                              String strEndStageNoPara,
                                              String strFlowStatusPara) {
        return myProgMatqtyInfoMapper.getLatestStageNoByEndStage(
                strSubcttInfoPkidPara,
                strEndStageNoPara,
                strFlowStatusPara);
    }

    private ProgMatqtyInfo fromShowModelToModel(ProgMatqtyInfoShow progMatqtyInfoShowPara){
        ProgMatqtyInfo progMatqtyInfoTemp =new ProgMatqtyInfo();
        progMatqtyInfoTemp.setPkid(progMatqtyInfoShowPara.getPkid());
        progMatqtyInfoTemp.setId(progMatqtyInfoShowPara.getId());
        progMatqtyInfoTemp.setSubcttInfoPkid(progMatqtyInfoShowPara.getSubcttInfoPkid());
        progMatqtyInfoTemp.setStageNo(progMatqtyInfoShowPara.getStageNo());
        progMatqtyInfoTemp.setArchivedFlag(progMatqtyInfoShowPara.getArchivedFlag());
        progMatqtyInfoTemp.setOriginFlag(progMatqtyInfoShowPara.getOriginFlag());
        progMatqtyInfoTemp.setFlowStatus(progMatqtyInfoShowPara.getFlowStatus());
        progMatqtyInfoTemp.setFlowStatusRemark(progMatqtyInfoShowPara.getFlowStatusRemark());
        progMatqtyInfoTemp.setCreatedBy(progMatqtyInfoShowPara.getCreatedBy());
        progMatqtyInfoTemp.setCreatedTime(progMatqtyInfoShowPara.getCreatedTime());
        progMatqtyInfoTemp.setUpdatedBy(progMatqtyInfoShowPara.getUpdatedBy());
        progMatqtyInfoTemp.setUpdatedTime(progMatqtyInfoShowPara.getUpdatedTime());
        progMatqtyInfoTemp.setRecVersion(progMatqtyInfoShowPara.getRecVersion());
        progMatqtyInfoTemp.setAttachment(progMatqtyInfoShowPara.getAttachment());
        progMatqtyInfoTemp.setRemark(progMatqtyInfoShowPara.getRemark());
        progMatqtyInfoTemp.setTid(progMatqtyInfoShowPara.getTid());
        return progMatqtyInfoTemp;
    }

    public ProgMatqtyInfo selectRecordsByPrimaryKey(String strPkId){
        return progMatqtyInfoMapper.selectByPrimaryKey(strPkId);
    }
    public void insertRecord(ProgMatqtyInfoShow progMatqtyInfoShowPara){
        progMatqtyInfoShowPara.setCreatedBy(platformService.getStrLastUpdBy());
        progMatqtyInfoShowPara.setCreatedByName(commonMapper.selectOpernameByCreatedBy(platformService.getStrLastUpdBy()));
        progMatqtyInfoShowPara.setCreatedTime(platformService.getStrLastUpdTime());
        progMatqtyInfoShowPara.setArchivedFlag("0");
        progMatqtyInfoMapper.insert(fromShowModelToModel(progMatqtyInfoShowPara)) ;
    }
    public void updateRecord(ProgMatqtyInfoShow progMatqtyInfoShowPara){
        progMatqtyInfoShowPara.setRecVersion(
                ToolUtil.getIntIgnoreNull(progMatqtyInfoShowPara.getRecVersion()) + 1);
        progMatqtyInfoShowPara.setArchivedFlag("0");
        progMatqtyInfoShowPara.setUpdatedBy(platformService.getStrLastUpdBy());
        progMatqtyInfoShowPara.setUpdatedByName(commonMapper.selectOpernameByCreatedBy(platformService.getStrLastUpdBy()));
        progMatqtyInfoShowPara.setUpdatedTime(platformService.getStrLastUpdTime());
        progMatqtyInfoMapper.updateByPrimaryKey(fromShowModelToModel(progMatqtyInfoShowPara)) ;
    }

    public void updateRecordByProgSubstlInfo(ProgMatqtyInfoShow progMatqtyInfoShowPara){
        List<ProgMatqtyInfo> progMatqtyInfoListTemp=getProgMatqtyInfoListByModel(progMatqtyInfoShowPara);
        if(progMatqtyInfoListTemp.size()>0){
            ProgMatqtyInfo progMatqtyInfoTemp=progMatqtyInfoListTemp.get(0);
            progMatqtyInfoTemp.setRecVersion(
                    ToolUtil.getIntIgnoreNull(progMatqtyInfoTemp.getRecVersion()) + 1);
            progMatqtyInfoTemp.setArchivedFlag("0");
            progMatqtyInfoTemp.setUpdatedBy(platformService.getStrLastUpdBy());
            progMatqtyInfoTemp.setUpdatedTime(platformService.getStrLastUpdTime());
            progMatqtyInfoTemp.setFlowStatus(progMatqtyInfoShowPara.getFlowStatus());
            progMatqtyInfoTemp.setFlowStatusRemark(progMatqtyInfoShowPara.getFlowStatusRemark());
            progMatqtyInfoMapper.updateByPrimaryKey(progMatqtyInfoTemp) ;
        }
    }
    public void deleteRecord(ProgMatqtyInfoShow progMatqtyInfoShowPara){
        progMatqtyInfoMapper.deleteByPrimaryKey(progMatqtyInfoShowPara.getPkid());
    }

    public int deleteRecord(String strPkId){
        return progMatqtyInfoMapper.deleteByPrimaryKey(strPkId);
    }

    /*public String subCttStlCheckForMng(String stlType,String subCttPkid,String periodNo) {
        String strReturnTemp="";
        if(ESEnum.ITEMTYPE3.getCode().equals(stlType)||ESEnum.ITEMTYPE4.getCode().equals(stlType)){ //�ְ�����
            String quantityMaxPeriod = ToolUtil.getStrIgnoreNull(
                    getMaxStageNo(ESEnum.ITEMTYPE3.getCode(),subCttPkid));
            String materialMaxPeriod = ToolUtil.getStrIgnoreNull(
                    getMaxStageNo(ESEnum.ITEMTYPE4.getCode(),subCttPkid));

            String quantityStatus=ToolUtil.getStrIgnoreNull(
                    getFlowStatus(ESEnum.ITEMTYPE3.getCode(),subCttPkid,quantityMaxPeriod));
            String materialStatus=ToolUtil.getStrIgnoreNull(
                    getFlowStatus(ESEnum.ITEMTYPE4.getCode(),subCttPkid,materialMaxPeriod));

            System.out.println("\n������������ںţ�"+quantityMaxPeriod+"\n���Ͻ�������ںţ�"+materialMaxPeriod);
            System.out.println("\n������������ںŶ�Ӧ״̬��־��"+quantityStatus+"\n���Ͻ�������ںŶ�Ӧ״̬��־��"+materialStatus);

            if (ESEnum.ITEMTYPE3.getCode().equals(stlType)){
                if (periodNo.compareTo(quantityMaxPeriod)<=0){ //���Ⱥ�����Ƚ��ںŴ�С�����������С���ߵ�������¼��
                    strReturnTemp="Ӧ¼�����[" + quantityMaxPeriod + "]�ڵķְ�������������!";
                    return strReturnTemp;
                }else {
                    if (quantityStatus.equals("")&&!quantityMaxPeriod.equals("")){
                        strReturnTemp="�ְ����������["+quantityMaxPeriod+"]�����ݻ�δ��׼ͨ��������¼��������!";
                        return strReturnTemp;
                    }
                    if (EnumFlowStatus.FLOW_STATUS3.getCode().compareTo(quantityStatus)>0){ //�ж��Ƿ��з���׼״̬�����ݴ��ڣ�����в���¼��
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
            if (ESEnum.ITEMTYPE4.getCode().equals(stlType)){
                if (periodNo.compareTo(materialMaxPeriod)<=0){ //���Ⱥ�����Ƚ��ںŴ�С�����������С���ߵ�������¼��
                    strReturnTemp="Ӧ¼�����["+materialMaxPeriod+"]�ڵķְ����Ͻ�������!";
                    return strReturnTemp;
                }else {
                    if (materialStatus.equals("")&&!materialMaxPeriod.equals("")){
                        strReturnTemp="�ְ����Ͻ����["+materialMaxPeriod+"]�����ݻ�δ��׼ͨ��������¼��������!";
                        return strReturnTemp;
                    }

                    if (EnumFlowStatus.FLOW_STATUS3.getCode().compareTo(materialStatus)>0){ //�ж��Ƿ��з���׼״̬�����ݴ��ڣ�����в���¼��
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
        }else if(ESEnum.ITEMTYPE6.getCode().equals(stlType)||ESEnum.ITEMTYPE7.getCode().equals(stlType)){// �ܰ�����
            if (ESEnum.ITEMTYPE6.getCode().equals(stlType)){
                String strStaQtyMaxPeriod = ToolUtil.getStrIgnoreNull(
                        getMaxStageNo(ESEnum.ITEMTYPE6.getCode(),subCttPkid));
                String strStaQtyMaxPeriodStatus=ToolUtil.getStrIgnoreNull(
                        getFlowStatus(ESEnum.ITEMTYPE6.getCode(),subCttPkid,strStaQtyMaxPeriod));

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
                if (ESEnum.ITEMTYPE7.getCode().equals(stlType)){
                    String strMeaQtyMaxPeriod = ToolUtil.getStrIgnoreNull(
                            getMaxStageNo(ESEnum.ITEMTYPE7.getCode(),subCttPkid));
                    String strMeaQtyMaxPeriodStatus=ToolUtil.getStrIgnoreNull(
                            getFlowStatus(ESEnum.ITEMTYPE7.getCode(),subCttPkid,strMeaQtyMaxPeriod));

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
    }*/

    public String subCttStlCheckForMng(String subcttInfoPkidPara, String stageNo) {
        String strReturnTemp = "";
        String quantityMaxPeriod = ToolUtil.getStrIgnoreNull(
                myProgWorkqtyInfoMapper.getMaxStageNo(subcttInfoPkidPara));
        String materialMaxPeriod = ToolUtil.getStrIgnoreNull(
                myProgMatqtyInfoMapper.getMaxStageNo(subcttInfoPkidPara));

        String quantityStatus = ToolUtil.getStrIgnoreNull(
                myProgWorkqtyInfoMapper.getFlowStatus(subcttInfoPkidPara, quantityMaxPeriod));
        String materialStatus = ToolUtil.getStrIgnoreNull(
                myProgMatqtyInfoMapper.getFlowStatus(subcttInfoPkidPara, materialMaxPeriod));

        System.out.println("\n������������ںţ�" + quantityMaxPeriod + "\n���Ͻ�������ںţ�" + materialMaxPeriod);
        System.out.println("\n������������ںŶ�Ӧ״̬��־��" + quantityStatus + "\n���Ͻ�������ںŶ�Ӧ״̬��־��" + materialStatus);

        if (stageNo.compareTo(materialMaxPeriod) <= 0) { //���Ⱥ�����Ƚ��ںŴ�С�����������С���ߵ�������¼��
            strReturnTemp = "Ӧ¼�����[" + materialMaxPeriod + "]�ڵķְ����Ͻ�������!";
            return strReturnTemp;
        } else {
            if (materialStatus.equals("") && !materialMaxPeriod.equals("")) {
                strReturnTemp = "�ְ����Ͻ����[" + materialMaxPeriod + "]�����ݻ�δ��׼ͨ��������¼��������!";
                return strReturnTemp;
            }
            if (EnumFlowStatus.FLOW_STATUS3.getCode().compareTo(materialStatus) > 0) { //�ж��Ƿ��з���׼״̬�����ݴ��ڣ�����в���¼��
                if (!materialMaxPeriod.equals("")) {
                    strReturnTemp = "�ְ����Ͻ����[" + materialMaxPeriod + "]�����ݻ�δ��׼ͨ��������¼�������ݣ�";
                    return strReturnTemp;
                } else {
                    if (materialMaxPeriod.compareTo(quantityMaxPeriod) < 0 && stageNo.compareTo(quantityMaxPeriod) != 0) {
                        strReturnTemp = "��[" + quantityMaxPeriod + "]�ڷְ����������Ѿ���ʼ����¼��[" + quantityMaxPeriod + "]�ڵķְ����Ͻ������ݣ�";
                        return strReturnTemp;
                    }
                }
            } else {//��>materialMaxPeriod &&=3�����������������������������£���ʱ˵��������Ƚ�û�����⣬������Ҫ�Ͳ��Ͻ���Ƚ�
                if (materialMaxPeriod.compareTo(quantityMaxPeriod) < 0) {
                    if (stageNo.compareTo(quantityMaxPeriod) != 0) {
                        strReturnTemp = "��[" + quantityMaxPeriod + "]�ڷְ����������Ѿ���ʼ����¼��[" + quantityMaxPeriod + "]�ڵķְ����Ͻ������ݣ�";
                        return strReturnTemp;
                    }
                }
            }
        }
        return "";
    }
}
