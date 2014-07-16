package epss.view.task;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumPreStatusFlag;
import epss.common.enums.ESEnumStatusFlag;
import epss.common.utils.ToolUtil;
import epss.repository.model.model_show.TaskShow;
import epss.service.EsCommonService;
import epss.service.FlowCtrlService;
import epss.service.TaskService;
import platform.repository.model.Ptmenu;
import platform.service.PlatformService;
import epss.view.flow.EsFlowControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-4
 * Time: ����4:53
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class EsInitTaskAction {
    private static final Logger logger = LoggerFactory.getLogger(EsInitTaskAction.class);
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    @ManagedProperty(value = "#{platformService}")
    private PlatformService platformService;
    @ManagedProperty(value = "#{taskService}")
    private TaskService taskService;
    @ManagedProperty(value = "#{flowCtrlService}")
    private FlowCtrlService flowCtrlService;

    private List<TaskShow> taskShowList;

    @PostConstruct
    public void init() {
        //���������б�
        this.taskShowList = new ArrayList<TaskShow>();
        //��ͬ����
        String strType;
        //��ͬ״̬
        String strStatusFlag;

        //ͨ��OperatorManager��ȡ��ӦȨ���²˵��б�
        List<Ptmenu> ptmenuList = platformService.getPtmenuList();
        //�����˵�
        for(Ptmenu itemUnit:ptmenuList){
            //����˵�Ϊ�ջ��ߵ�ַ�в������ܰ���ͬ�ͳɱ��ƻ��Ļ�������ѭ������
            if(itemUnit.getMenuaction()==null||
                    (!itemUnit.getMenuaction().toLowerCase().contains("cstpl")&&
                            !itemUnit.getMenuaction().toLowerCase().contains("tkctt"))){
                continue;
            }
            //��ʼ����ͬ����
            strType="";
            //���ݺ�ͬ�����ͣ�������strType������Ӧ��ֵ
            if(itemUnit.getMenuaction().contains("tkctt_Tkctt")) {
                strType= ESEnum.ITEMTYPE0.getCode();
            }else if(itemUnit.getMenuaction().contains("tkctt_Cstpl")) {
                strType= ESEnum.ITEMTYPE1.getCode();
            }else if(itemUnit.getMenuaction().contains("cstpl_SubcttEngStlQ")) {
                strType= ESEnum.ITEMTYPE3.getCode();
            }else if(itemUnit.getMenuaction().contains("cstpl_SubcttEngStlM")) {
                strType= ESEnum.ITEMTYPE4.getCode();
            }else if(itemUnit.getMenuaction().contains("cstpl_SubcttEngStlP")) {
                strType= ESEnum.ITEMTYPE5.getCode();
            }else if(itemUnit.getMenuaction().contains("cstpl_SubcttEngSta")) {
                strType= ESEnum.ITEMTYPE6.getCode();
            }else if(itemUnit.getMenuaction().contains("cstpl_SubcttEngMea")) {
                strType= ESEnum.ITEMTYPE7.getCode();
            }else if(itemUnit.getMenuaction().contains("cstpl_Subctt")) {
                strType= ESEnum.ITEMTYPE2.getCode();
            }
            //��ʼ����ͬ����
            strStatusFlag="";
            //���ݺ�ͬ״̬�����ͣ�������strStatusFlag������Ӧ��ֵ
            if(itemUnit.getMenuaction().contains("Mng")) {
                strStatusFlag= ESEnumStatusFlag.STATUS_FLAG0.getCode();
            }else if(itemUnit.getMenuaction().contains("DoubleCheck")) {
                strStatusFlag= ESEnumStatusFlag.STATUS_FLAG2.getCode();
            }else if(itemUnit.getMenuaction().contains("Check")) {
                strStatusFlag= ESEnumStatusFlag.STATUS_FLAG1.getCode();
            }else if(itemUnit.getMenuaction().contains("Approve")) {
                strStatusFlag= ESEnumStatusFlag.STATUS_FLAG3.getCode();
            }else if(itemUnit.getMenuaction().contains("Print")) {
                strStatusFlag= ESEnumStatusFlag.STATUS_FLAG4.getCode();
            }
            //ʵ�������� taskShow
            TaskShow taskShow =new TaskShow();
            //������taskShow������Pkid��ֵ
            taskShow.setPkid(itemUnit.getMenuid());
            //��������ʼ���жϺ�ͬ�Ƿ����ض����ͺ�״̬�Ĳ�������
            Boolean isHasTask;
            // �Ժ�ͬ���ͺ�״̬Ϊ����,ȡ�ø��������
            List<TaskShow> taskCountsInFlowGroupListTemp =taskService.getTaskCountsInFlowGroup();
            // �����ϸ�����б�
            List<TaskShow> taskShowListTemp=taskService.getTaskShowList();
            //������š���ͬ����+״̬+��������Ϣ�ļ���
            for(TaskShow itemUnitCM: taskCountsInFlowGroupListTemp){
                String strStatusFlagInTaskCountsInFlowGroupList=
                        ToolUtil.getStrIgnoreNull(itemUnitCM.getStatusFlag());
                //�����б�type���ź���ģ����򣩣����ڱȽϵĲ˵�type�Ͳ����ٱȽ���
                if(ToolUtil.getStrIgnoreNull(itemUnitCM.getType()).compareTo(strType)>0){
                    break;
                } //���������ͺ�ͬ����һ�µ�ʱ��
                if(itemUnitCM.getType().equals(strType)){
                    //��֤Խ���Ĳ������
                    isHasTask=false;
                    //����ͬ״̬Ϊ��ʼʱ������"¼��"�еĴ���������
                    if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())){
                        if(strStatusFlagInTaskCountsInFlowGroupList.equals("")){
                            isHasTask=true;//��������ı�ͷ��ֵ
                        } //����ͬ״̬Ϊ���ʱ����������״̬Ϊ��˵�ʱ�����ǡ����ˡ��еĴ���������
                    }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG1.getCode())){
                        if(strStatusFlagInTaskCountsInFlowGroupList.equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())){
                            isHasTask=true;//��������ı�ͷ��ֵ
                        } //����ͬ״̬Ϊ����ʱ����������״̬Ϊ��˵�ʱ�����ǡ����ˡ��еĴ���������
                    }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG2.getCode())){
                        if(strStatusFlagInTaskCountsInFlowGroupList.equals(ESEnumStatusFlag.STATUS_FLAG1.getCode())){
                            isHasTask=true;
                        }//����ͬ״̬Ϊ��׼ʱ����������״̬Ϊ���˵�ʱ�����ǡ���׼���еĴ���������
                    }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG3.getCode())){
                        if(strStatusFlagInTaskCountsInFlowGroupList.equals(ESEnumStatusFlag.STATUS_FLAG2.getCode())){
                            isHasTask=true;
                        }//����ͬ״̬Ϊ����ʱ����������״̬Ϊ��׼��ʱ�����ǡ����ˡ��еĴ���������
                    }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG4.getCode())){
                        if(strStatusFlagInTaskCountsInFlowGroupList.equals(ESEnumStatusFlag.STATUS_FLAG3.getCode())){
                            isHasTask=true;
                        }
                    } //������Ϊ����������ʱ��ͳ�ƴ����������е��������
                    if(isHasTask.equals(true)){
                        // ׷�ӱ�����
                        taskShow.setName(itemUnit.getMenulabel()+"("+itemUnitCM.getRecordsCountInGroup()+")");
                        taskShow.setPkid("");
                        taskShowList.add(taskShow);
                        //����ͬ״̬Ϊ��������ʱ
                        //���̣��ǡ�¼��δ�ꡯ����Ϣ
                        for(TaskShow itemUnitEP:taskShowListTemp){
                            String strStatusFlagInTaskShowList=ToolUtil.getStrIgnoreNull(itemUnitEP.getStatusFlag());
                            if(itemUnitEP.getType().equals(strType)){
                                isHasTask=false;
                                if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())){
                                    if(strStatusFlagInTaskShowList.equals("")){
                                        isHasTask=true;
                                    }
                                }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG1.getCode())){
                                    if(strStatusFlagInTaskShowList.equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())){
                                        isHasTask=true;
                                    }
                                }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG2.getCode())){
                                    if(strStatusFlagInTaskShowList.equals(ESEnumStatusFlag.STATUS_FLAG1.getCode())){
                                        isHasTask=true;
                                    }
                                }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG3.getCode())){
                                    if(strStatusFlagInTaskShowList.equals(ESEnumStatusFlag.STATUS_FLAG2.getCode())){
                                        isHasTask=true;
                                    }
                                }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG4.getCode())){
                                    if(strStatusFlagInTaskShowList.equals(ESEnumStatusFlag.STATUS_FLAG3.getCode())){
                                        isHasTask=true;
                                    }
                                }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG4.getCode())){
                                    if(strStatusFlagInTaskShowList.equals(ESEnumStatusFlag.STATUS_FLAG3.getCode())){
                                        isHasTask=true;
                                    }
                                }

                                //������Ǵ�������������ʾ�б�����������к������еľ�����Ϣ
                                if(isHasTask.equals(true)){
                                    taskShow =new TaskShow();
                                    taskShow.setType(itemUnitEP.getType());
                                    taskShow.setPkid(itemUnitEP.getPkid());
                                    taskShow.setId(itemUnitEP.getId());
                                    taskShow.setName(itemUnitEP.getName());
                                    taskShow.setPeriodNo(itemUnitEP.getPeriodNo());
                                    taskShow.setStatusFlag(strStatusFlagInTaskShowList);
                                    if(!strStatusFlagInTaskShowList.equals("")) {
                                        taskShow.setStatusFlagName(
                                                ESEnumStatusFlag.getValueByKey(strStatusFlagInTaskShowList).getTitle());
                                    }
                                    taskShow.setPreStatusFlag(itemUnitEP.getPreStatusFlag());
                                    if(itemUnitEP.getPreStatusFlag()!=null) {
                                        taskShow.setPreStatusFlagName(
                                                ESEnumPreStatusFlag.getValueByKey(itemUnitEP.getPreStatusFlag()).getTitle());
                                    }
                                    taskShowList.add(taskShow);
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    public EsFlowControl getEsFlowControl() {
        return esFlowControl;
    }

    public PlatformService getPlatformService() {
        return platformService;
    }

    public void setPlatformService(PlatformService platformService) {
        this.platformService = platformService;
    }

    public void setEsFlowControl(EsFlowControl esFlowControl) {
        this.esFlowControl = esFlowControl;
    }

    public List<TaskShow> getTaskShowList() {
        return taskShowList;
    }

    public TaskService getTaskService() {
        return taskService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public FlowCtrlService getFlowCtrlService() {
        return flowCtrlService;
    }

    public void setFlowCtrlService(FlowCtrlService flowCtrlService) {
        this.flowCtrlService = flowCtrlService;
    }
}