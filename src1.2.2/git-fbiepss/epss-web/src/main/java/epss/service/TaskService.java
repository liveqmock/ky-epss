package epss.service;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumPreStatusFlag;
import epss.common.enums.ESEnumStatusFlag;
import epss.repository.dao.not_mybatis.MyTaskMapper;
import epss.repository.model.model_show.OperResShow;
import epss.repository.model.model_show.TaskShow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import skyline.repository.dao.not_mybatis.PtCommonMapper;
import skyline.repository.model.Ptmenu;
import skyline.util.ToolUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-5-5
 * Time: ����8:50
 * To change this template use File | Settings | File Templates.
 */
@Service
public class TaskService {
    @Autowired
    private MyTaskMapper myTaskMapper;
    @Autowired
    private PtCommonMapper ptCommonMapper;

    public List<TaskShow> getTaskCountsInFlowGroup(String strOperPkidPara) {
        return myTaskMapper.getTaskCountsInFlowGroup(strOperPkidPara);
    }

    public List<TaskShow> getDetailTaskShowList(String strOperPkidPara) {
        return myTaskMapper.getDetailTaskShowList(strOperPkidPara);
    }

    public List<TaskShow> initTaskShowList(String strActionType) {
        //��ͬ����
        String strType;
        //��ͬ״̬
        String strStatusFlag;
        List<TaskShow> taskShowList = new ArrayList<TaskShow>();
        //ͨ��OperatorManager��ȡ��ӦȨ���²˵��б�
        List<Ptmenu> ptmenuList = ptCommonMapper.getPtmenuList(ToolUtil.getOperatorManager().getOperatorId());
        String strOperIdTemp=ToolUtil.getOperatorManager().getOperatorId();
        // �Ժ�ͬ���ͺ�״̬Ϊ����,ȡ�ø��������
        List<TaskShow> taskCountsInFlowGroupListTemp = getTaskCountsInFlowGroup(strOperIdTemp);
        // �����ϸ�����б�
        List<TaskShow> taskShowListTemp = getDetailTaskShowList(strOperIdTemp);
        //�����˵�
        for (Ptmenu itemUnit : ptmenuList) {
            //����˵�Ϊ�ջ��ߵ�ַ�в������ܰ���ͬ�ͳɱ��ƻ��Ļ�������ѭ������
            if (itemUnit.getMenuaction() == null || itemUnit.getTargetmachine().toLowerCase().contains("system")) {
                continue;
            }
            //��ʼ����ͬ����
            strType = "";
            //���ݺ�ͬ�����ͣ�������strType������Ӧ��ֵ
            if (itemUnit.getMenudesc().contains("0")) {
                strType = ESEnum.ITEMTYPE0.getCode();
            } else if (itemUnit.getMenudesc().contains("1")) {
                strType = ESEnum.ITEMTYPE1.getCode();
            } else if (itemUnit.getMenudesc().contains("2")) {
                strType = ESEnum.ITEMTYPE2.getCode();
            } else if (itemUnit.getMenudesc().contains("3")) {
                strType = ESEnum.ITEMTYPE3.getCode();
            } else if (itemUnit.getMenudesc().contains("4")) {
                strType = ESEnum.ITEMTYPE4.getCode();
            } else if (itemUnit.getMenudesc().contains("5")) {
                strType = ESEnum.ITEMTYPE5.getCode();
            } else if (itemUnit.getMenudesc().contains("6")) {
                strType = ESEnum.ITEMTYPE6.getCode();
            } else if (itemUnit.getMenudesc().contains("7")) {
                strType = ESEnum.ITEMTYPE7.getCode();
            }
            //��ʼ����ͬ����
            strStatusFlag = "";
            //���ݺ�ͬ״̬�����ͣ�������strStatusFlag������Ӧ��ֵ
            if (itemUnit.getMenudesc().contains("*A")) {
                strStatusFlag = ESEnumStatusFlag.STATUS_FLAG0.getCode();
            } else if (itemUnit.getMenudesc().contains("*B")) {
                strStatusFlag = ESEnumStatusFlag.STATUS_FLAG1.getCode();
            } else if (itemUnit.getMenudesc().contains("*C")) {
                strStatusFlag = ESEnumStatusFlag.STATUS_FLAG2.getCode();
            } else if (itemUnit.getMenudesc().contains("*D")) {
                strStatusFlag = ESEnumStatusFlag.STATUS_FLAG3.getCode();
            } else if (itemUnit.getMenudesc().contains("*E")) {
                strStatusFlag = ESEnumStatusFlag.STATUS_FLAG4.getCode();
            }
            //ʵ�������� taskShow
            TaskShow taskShow = new TaskShow();
            //������taskShow������Pkid��ֵ
            taskShow.setPkid(itemUnit.getMenuid());
            //��������ʼ���жϺ�ͬ�Ƿ����ض����ͺ�״̬�Ĳ�������
            Boolean isHasTask;

            //������š���ͬ����+״̬+��������Ϣ�ļ���
            for (TaskShow itemUnitCM : taskCountsInFlowGroupListTemp) {
                String strStatusFlagInTaskCountsInFlowGroupList =
                        ToolUtil.getStrIgnoreNull(itemUnitCM.getStatusFlag());
                //�����б�type���ź���ģ����򣩣����ڱȽϵĲ˵�type�Ͳ����ٱȽ���
                if (ToolUtil.getStrIgnoreNull(itemUnitCM.getType()).compareTo(strType) > 0) {
                    break;
                } //���������ͺ�ͬ����һ�µ�ʱ��
                if (itemUnitCM.getType().equals(strType)) {
                    //��֤Խ���Ĳ������
                    isHasTask = false;
                    //����ͬ״̬Ϊ��ʼʱ������"¼��"�еĴ���������
                    if (strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())) {
                        if (strStatusFlagInTaskCountsInFlowGroupList.equals("")) {
                            isHasTask = true;//��������ı�ͷ��ֵ
                        } //����ͬ״̬Ϊ���ʱ����������״̬Ϊ��˵�ʱ�����ǡ����ˡ��еĴ���������
                    } else if (strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG1.getCode())) {
                        if (strStatusFlagInTaskCountsInFlowGroupList.equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())) {
                            isHasTask = true;//��������ı�ͷ��ֵ
                        } //����ͬ״̬Ϊ����ʱ����������״̬Ϊ��˵�ʱ�����ǡ����ˡ��еĴ���������
                    } else if (strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG2.getCode())) {
                        if (strStatusFlagInTaskCountsInFlowGroupList.equals(ESEnumStatusFlag.STATUS_FLAG1.getCode())) {
                            isHasTask = true;
                        }//����ͬ״̬Ϊ��׼ʱ����������״̬Ϊ���˵�ʱ�����ǡ���׼���еĴ���������
                    } else if (strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG3.getCode())) {
                        if (strStatusFlagInTaskCountsInFlowGroupList.equals(ESEnumStatusFlag.STATUS_FLAG2.getCode())) {
                            isHasTask = true;
                        }//����ͬ״̬Ϊ����ʱ����������״̬Ϊ��׼��ʱ�����ǡ����ˡ��еĴ���������
                    } else if (strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG4.getCode())) {
                        if (strStatusFlagInTaskCountsInFlowGroupList.equals(ESEnumStatusFlag.STATUS_FLAG3.getCode())) {
                            isHasTask = true;
                        }
                    } //������Ϊ����������ʱ��ͳ�ƴ����������е��������
                    if (isHasTask.equals(true)) {
                        // ׷�ӱ�����
                        taskShow.setName(itemUnit.getMenulabel() + "(" + itemUnitCM.getRecordsCountInGroup() + ")");
                        taskShow.setPkid("");
                        taskShowList.add(taskShow);
                        //����ͬ״̬Ϊ��������ʱ
                        //���̣��ǡ�¼��δ�ꡯ����Ϣ
                        if ("EsInitTask".equals(strActionType)) {
                            for (TaskShow itemUnitEP : taskShowListTemp) {
                                String strStatusFlagInTaskShowList = ToolUtil.getStrIgnoreNull(itemUnitEP.getStatusFlag());
                                if (itemUnitEP.getType().equals(strType)) {
                                    isHasTask = false;
                                    if (strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())) {
                                        if (strStatusFlagInTaskShowList.equals("")) {
                                            isHasTask = true;
                                        }
                                    } else if (strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG1.getCode())) {
                                        if (strStatusFlagInTaskShowList.equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())) {
                                            isHasTask = true;
                                        }
                                    } else if (strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG2.getCode())) {
                                        if (strStatusFlagInTaskShowList.equals(ESEnumStatusFlag.STATUS_FLAG1.getCode())) {
                                            isHasTask = true;
                                        }
                                    } else if (strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG3.getCode())) {
                                        if (strStatusFlagInTaskShowList.equals(ESEnumStatusFlag.STATUS_FLAG2.getCode())) {
                                            isHasTask = true;
                                        }
                                    } else if (strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG4.getCode())) {
                                        if (strStatusFlagInTaskShowList.equals(ESEnumStatusFlag.STATUS_FLAG3.getCode())) {
                                            isHasTask = true;
                                        }
                                    } else if (strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG4.getCode())) {
                                        if (strStatusFlagInTaskShowList.equals(ESEnumStatusFlag.STATUS_FLAG3.getCode())) {
                                            isHasTask = true;
                                        }
                                    }

                                    //������Ǵ�������������ʾ�б�����������к������еľ�����Ϣ
                                    if (isHasTask.equals(true)) {
                                        taskShow = new TaskShow();
                                        taskShow.setType(itemUnitEP.getType());
                                        taskShow.setPkid(itemUnitEP.getPkid());
                                        taskShow.setId(itemUnitEP.getId());
                                        taskShow.setName(itemUnitEP.getName());
                                        taskShow.setPeriodNo(itemUnitEP.getPeriodNo());
                                        taskShow.setStatusFlag(strStatusFlagInTaskShowList);
                                        if (!strStatusFlagInTaskShowList.equals("")) {
                                            taskShow.setStatusFlagName(
                                                    ESEnumStatusFlag.getValueByKey(strStatusFlagInTaskShowList).getTitle());
                                        }
                                        taskShow.setPreStatusFlag(itemUnitEP.getPreStatusFlag());
                                        if (itemUnitEP.getPreStatusFlag() != null) {
                                            taskShow.setPreStatusFlagName(
                                                    ESEnumPreStatusFlag.getValueByKey(itemUnitEP.getPreStatusFlag()).getTitle());
                                            //��ɫ����
                                            if (taskShow.getStatusFlag() != null) {
                                                if (taskShow.getPreStatusFlag().equals("0")) {
                                                    taskShow.setPreStatusFlagType("1");
                                                } else {
                                                    if ((int) (Integer.parseInt(itemUnitEP.getPreStatusFlag()))
                                                            > 2 * (int) (Integer.parseInt(itemUnitEP.getStatusFlag()))) {
                                                        taskShow.setPreStatusFlagType("2");
                                                    } else if ((int) (Integer.parseInt(itemUnitEP.getStatusFlag()) + (int) Integer.parseInt(itemUnitEP.getStatusFlag())) - 1
                                                            == (int) Integer.parseInt(itemUnitEP.getStatusFlag())) {
                                                        taskShow.setPreStatusFlagType("1");
                                                    }
                                                }
                                            } else {
                                                taskShow.setPreStatusFlagType("2");
                                            }
                                        }
                                        taskShowList.add(taskShow);
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
        return taskShowList;
    }
}
