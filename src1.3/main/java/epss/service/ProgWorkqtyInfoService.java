package epss.service;

import epss.common.enums.ESEnum;
import epss.common.enums.EnumFlowStatus;
import epss.common.utils.ToolUtil;
import epss.repository.dao.ProgWorkqtyInfoMapper;
import epss.repository.dao.notMyBits.CommonMapper;
import epss.repository.dao.notMyBits.MyProgMatqtyInfoMapper;
import epss.repository.dao.notMyBits.MyProgWorkqtyInfoMapper;
import epss.repository.model.ProgWorkqtyInfo;
import epss.repository.model.ProgWorkqtyInfoExample;
import epss.repository.model.model_show.ProgWorkqtyInfoShow;
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
public class ProgWorkqtyInfoService {
    @Resource
    private ProgWorkqtyInfoMapper progWorkqtyInfoMapper;
    @Resource
    private MyProgWorkqtyInfoMapper myProgWorkqtyInfoMapper;
    @Resource
    private PlatformService platformService;
    @Resource
    private CommonMapper commonMapper;

    @Resource
    private MyProgMatqtyInfoMapper myProgMatqtyInfoMapper;

    /**
     * �жϼ�¼�Ƿ��Ѵ���
     *
     * @param progWorkqtyInfoShowPara
     * @return
     */
    public List<ProgWorkqtyInfo> getProgWorkqtyInfoListByModel(ProgWorkqtyInfoShow progWorkqtyInfoShowPara) {
        ProgWorkqtyInfoExample example = new ProgWorkqtyInfoExample();
        example.createCriteria().andSubcttInfoPkidEqualTo(progWorkqtyInfoShowPara.getSubcttInfoPkid())
                .andStageNoEqualTo(progWorkqtyInfoShowPara.getStageNo());
        return progWorkqtyInfoMapper.selectByExample(example);
    }

    public List<ProgWorkqtyInfo> getProgWorkqtyInfoListByModel(ProgWorkqtyInfo progWorkqtyInfoPara) {
        ProgWorkqtyInfoExample example = new ProgWorkqtyInfoExample();
        example.createCriteria().andSubcttInfoPkidEqualTo(progWorkqtyInfoPara.getSubcttInfoPkid())
                .andStageNoEqualTo(progWorkqtyInfoPara.getStageNo());
        return progWorkqtyInfoMapper.selectByExample(example);
    }

    public List<ProgWorkqtyInfoShow> getProgWorkqtyInfoListByFlowStatusBegin_End(ProgWorkqtyInfoShow progWorkqtyInfoShowPara) {
        return myProgWorkqtyInfoMapper.getProgWorkqtyInfoListByFlowStatusBegin_End(progWorkqtyInfoShowPara);
    }

    public List<ProgWorkqtyInfoShow> getSgageListByStageBegin_End(String strSubcttInfoPkidPara,
                                                                  String strBeginStageNoPara,
                                                                  String strEndStageNoPara) {
        return myProgWorkqtyInfoMapper.getSgageListByStageBegin_End(
                strSubcttInfoPkidPara,
                strBeginStageNoPara,
                strEndStageNoPara);
    }

    public String getStrMaxProgWorkqtyInfoId() {
        return myProgWorkqtyInfoMapper.getStrMaxProgWorkqtyInfoId();
    }

    public List<ProgWorkqtyInfoShow> getLatestStageProgWorkqtyInfoShowList(
            String strSubcttInfoPkidPara, String strEndStageNoPara, String strFlowStatusPara) {
        return myProgWorkqtyInfoMapper.getLatestStageProgWorkqtyInfoShowList(
                strSubcttInfoPkidPara, strEndStageNoPara, strFlowStatusPara);
    }

    private ProgWorkqtyInfo fromShowModelToModel(ProgWorkqtyInfoShow progWorkqtyInfoShowPara) {
        ProgWorkqtyInfo progWorkqtyInfoTemp = new ProgWorkqtyInfo();
        progWorkqtyInfoTemp.setPkid(progWorkqtyInfoShowPara.getPkid());
        progWorkqtyInfoTemp.setId(progWorkqtyInfoShowPara.getId());
        progWorkqtyInfoTemp.setSubcttInfoPkid(progWorkqtyInfoShowPara.getSubcttInfoPkid());
        progWorkqtyInfoTemp.setStageNo(progWorkqtyInfoShowPara.getStageNo());
        progWorkqtyInfoTemp.setArchivedFlag(progWorkqtyInfoShowPara.getArchivedFlag());
        progWorkqtyInfoTemp.setOriginFlag(progWorkqtyInfoShowPara.getOriginFlag());
        progWorkqtyInfoTemp.setFlowStatus(progWorkqtyInfoShowPara.getFlowStatus());
        progWorkqtyInfoTemp.setFlowStatusRemark(progWorkqtyInfoShowPara.getFlowStatusRemark());
        progWorkqtyInfoTemp.setCreatedBy(progWorkqtyInfoShowPara.getCreatedBy());
        progWorkqtyInfoTemp.setCreatedTime(progWorkqtyInfoShowPara.getCreatedTime());
        progWorkqtyInfoTemp.setUpdatedBy(progWorkqtyInfoShowPara.getUpdatedBy());
        progWorkqtyInfoTemp.setUpdatedTime(progWorkqtyInfoShowPara.getUpdatedTime());
        progWorkqtyInfoTemp.setRecVersion(progWorkqtyInfoShowPara.getRecVersion());
        progWorkqtyInfoTemp.setAttachment(progWorkqtyInfoShowPara.getAttachment());
        progWorkqtyInfoTemp.setRemark(progWorkqtyInfoShowPara.getRemark());
        progWorkqtyInfoTemp.setTid(progWorkqtyInfoShowPara.getTid());
        return progWorkqtyInfoTemp;
    }

    public ProgWorkqtyInfo selectRecordsByPrimaryKey(String strPkId) {
        return progWorkqtyInfoMapper.selectByPrimaryKey(strPkId);
    }

    public void insertRecord(ProgWorkqtyInfoShow progWorkqtyInfoShowPara) {
        progWorkqtyInfoShowPara.setCreatedBy(platformService.getStrLastUpdBy());
        progWorkqtyInfoShowPara.setCreatedByName(commonMapper.selectOpernameByCreatedBy(platformService.getStrLastUpdBy()));
        progWorkqtyInfoShowPara.setCreatedTime(platformService.getStrLastUpdTime());
        progWorkqtyInfoShowPara.setArchivedFlag("0");
        progWorkqtyInfoMapper.insert(fromShowModelToModel(progWorkqtyInfoShowPara));
    }

    public void updateRecord(ProgWorkqtyInfoShow progWorkqtyInfoShowPara) {
        progWorkqtyInfoShowPara.setRecVersion(
                ToolUtil.getIntIgnoreNull(progWorkqtyInfoShowPara.getRecVersion()) + 1);
        progWorkqtyInfoShowPara.setArchivedFlag("0");
        progWorkqtyInfoShowPara.setUpdatedBy(platformService.getStrLastUpdBy());
        progWorkqtyInfoShowPara.setUpdatedByName(commonMapper.selectOpernameByCreatedBy(platformService.getStrLastUpdBy()));
        progWorkqtyInfoShowPara.setUpdatedTime(platformService.getStrLastUpdTime());
        progWorkqtyInfoMapper.updateByPrimaryKey(fromShowModelToModel(progWorkqtyInfoShowPara));
    }

    public void updateRecordByProgSubstlInfo(ProgWorkqtyInfoShow progWorkqtyInfoShowPara) {
        List<ProgWorkqtyInfo> progWorkqtyInfoListTemp = getProgWorkqtyInfoListByModel(progWorkqtyInfoShowPara);
        if (progWorkqtyInfoListTemp.size() > 0) {
            ProgWorkqtyInfo progWorkqtyInfoTemp = progWorkqtyInfoListTemp.get(0);
            progWorkqtyInfoTemp.setRecVersion(
                    ToolUtil.getIntIgnoreNull(progWorkqtyInfoTemp.getRecVersion()) + 1);
            progWorkqtyInfoTemp.setArchivedFlag("0");
            progWorkqtyInfoTemp.setUpdatedBy(platformService.getStrLastUpdBy());
            progWorkqtyInfoTemp.setUpdatedTime(platformService.getStrLastUpdTime());
            progWorkqtyInfoTemp.setFlowStatus(progWorkqtyInfoShowPara.getFlowStatus());
            progWorkqtyInfoTemp.setFlowStatusRemark(progWorkqtyInfoShowPara.getFlowStatusRemark());
            progWorkqtyInfoMapper.updateByPrimaryKey(progWorkqtyInfoTemp);
        }
    }

    public void deleteRecord(ProgWorkqtyInfoShow progWorkqtyInfoShowPara) {
        progWorkqtyInfoMapper.deleteByPrimaryKey(progWorkqtyInfoShowPara.getPkid());
    }

    public int deleteRecord(String strPkId) {
        return progWorkqtyInfoMapper.deleteByPrimaryKey(strPkId);
    }

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


        if (stageNo.compareTo(quantityMaxPeriod) <= 0) { //���Ⱥ�����Ƚ��ںŴ�С�����������С���ߵ�������¼��
            strReturnTemp = "Ӧ¼�����[" + quantityMaxPeriod + "]�ڵķְ�������������!";
            return strReturnTemp;
        } else {
            if (quantityStatus.equals("") && !quantityMaxPeriod.equals("")) {
                strReturnTemp = "�ְ����������[" + quantityMaxPeriod + "]�����ݻ�δ��׼ͨ��������¼��������!";
                return strReturnTemp;
            }
            if (EnumFlowStatus.FLOW_STATUS3.getCode().compareTo(quantityStatus) > 0) { //�ж��Ƿ��з���׼״̬�����ݴ��ڣ�����в���¼��
                if (!quantityStatus.equals("")) {
                    strReturnTemp = "�ְ����������[" + quantityMaxPeriod + "]�����ݻ�δ��׼ͨ��������¼�������ݣ�";
                    return strReturnTemp;
                } else {
                    if (quantityMaxPeriod.compareTo(materialMaxPeriod) < 0 && stageNo.compareTo(materialMaxPeriod) != 0) {
                        strReturnTemp = "��[" + materialMaxPeriod + "]�ڷְ����Ͻ����Ѿ���ʼ����¼��[" + materialMaxPeriod + "]�ڵķְ������������ݣ�";
                        return strReturnTemp;
                    }
                }
            } else {//��>quantityMaxPeriod &&=3�����������������������������£���ʱ˵��������Ƚ�û�����⣬������Ҫ�Ͳ��Ͻ���Ƚ�
                if (quantityMaxPeriod.compareTo(materialMaxPeriod) < 0) {
                    if (stageNo.compareTo(materialMaxPeriod) != 0) {
                        strReturnTemp = "��[" + materialMaxPeriod + "]�ڷְ����Ͻ����Ѿ���ʼ����¼��[" + materialMaxPeriod + "]�ڵķְ������������ݣ�";
                        return strReturnTemp;
                    }
                }
            }
        }
        return "";
    }
}
