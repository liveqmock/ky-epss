package epss.view.task;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumPreStatusFlag;
import epss.common.enums.ESEnumStatusFlag;
import skyline.util.ToolUtil;
import epss.repository.model.model_show.OperResShow;
import epss.repository.model.model_show.TaskShow;
import epss.service.FlowCtrlService;
import epss.service.OperResService;
import epss.service.TaskService;
import skyline.repository.model.Ptmenu;
import skyline.service.PlatformService;
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
    @ManagedProperty(value = "#{operResService}")
    private OperResService operResService;

    private List<TaskShow> taskShowList;

    @PostConstruct
    public void init() {
        //���������б�
        this.taskShowList = new ArrayList<TaskShow>();
        //��ͬ����
        String strType;
        //��ͬ״̬
        String strStatusFlag;

        OperResShow operResShowTemp=new OperResShow();
        List<OperResShow> operResShowList=
                operResService.selectOperaResRecordsByModelShow(operResShowTemp);

        //ͨ��OperatorManager��ȡ��ӦȨ���²˵��б�
        List<Ptmenu> ptmenuList = platformService.getPtmenuList();
        // �Ժ�ͬ���ͺ�״̬Ϊ����,ȡ�ø��������
        List<TaskShow> taskCountsInFlowGroupListTemp =taskService.getTaskCountsInFlowGroup();
        // �����ϸ�����б�
        List<TaskShow> taskShowListTemp=taskService.getTaskShowList();
        //�����˵�
        for(Ptmenu itemUnit:ptmenuList){
            //����˵�Ϊ�ջ��ߵ�ַ�в������ܰ���ͬ�ͳɱ��ƻ��Ļ�������ѭ������
            if(itemUnit.getMenuaction()==null||itemUnit.getTargetmachine().toLowerCase().contains("system")){
                continue;
            }
            //��ʼ����ͬ����
            strType="";
            //���ݺ�ͬ�����ͣ�������strType������Ӧ��ֵ
            if(itemUnit.getMenudesc().contains("0")) {
                strType= ESEnum.ITEMTYPE0.getCode();
            }else if(itemUnit.getMenudesc().contains("1")) {
                strType= ESEnum.ITEMTYPE1.getCode();
            }else if(itemUnit.getMenudesc().contains("2")) {
                strType= ESEnum.ITEMTYPE2.getCode();
            }else if(itemUnit.getMenudesc().contains("3")) {
                strType= ESEnum.ITEMTYPE3.getCode();
            }else if(itemUnit.getMenudesc().contains("4")) {
                strType= ESEnum.ITEMTYPE4.getCode();
            }else if(itemUnit.getMenudesc().contains("5")) {
                strType= ESEnum.ITEMTYPE5.getCode();
            }else if(itemUnit.getMenudesc().contains("6")) {
                strType= ESEnum.ITEMTYPE6.getCode();
            }else if(itemUnit.getMenudesc().contains("7")) {
                strType= ESEnum.ITEMTYPE7.getCode();
            }
            //��ʼ����ͬ����
            strStatusFlag="";
            //���ݺ�ͬ״̬�����ͣ�������strStatusFlag������Ӧ��ֵ
            if(itemUnit.getMenudesc().contains("*A")) {
                strStatusFlag= ESEnumStatusFlag.STATUS_FLAG0.getCode();
            }else if(itemUnit.getMenudesc().contains("*B")) {
                strStatusFlag= ESEnumStatusFlag.STATUS_FLAG1.getCode();
            }else if(itemUnit.getMenudesc().contains("*C")) {
                strStatusFlag= ESEnumStatusFlag.STATUS_FLAG2.getCode();
            }else if(itemUnit.getMenudesc().contains("*D")) {
                strStatusFlag= ESEnumStatusFlag.STATUS_FLAG3.getCode();
            }else if(itemUnit.getMenudesc().contains("*E")) {
                strStatusFlag= ESEnumStatusFlag.STATUS_FLAG4.getCode();
            }
            //ʵ�������� taskShow
            TaskShow taskShow =new TaskShow();
            //������taskShow������Pkid��ֵ
            taskShow.setPkid(itemUnit.getMenuid());
            //��������ʼ���жϺ�ͬ�Ƿ����ض����ͺ�״̬�Ĳ�������
            Boolean isHasTask;

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
                        //Ȩ��ɸѡ
                        Boolean hasPower=false;
                        /*for(OperResShow operResShowUnit:operResShowList){
                            if(operResShowUnit.getInfoType().equals(strType)){
                                hasPower=true;
                                break;
                            }
                        }*/
                        hasPower=true;//��ʱ��
                        if(hasPower.equals(true)){
                            // ׷�ӱ�����
                            taskShow.setName(itemUnit.getMenulabel()+"("+itemUnitCM.getRecordsCountInGroup()+")");
                            taskShow.setPkid("");
                            taskShowList.add(taskShow);
                        }else{
                            continue;
                        }

                        //����ͬ״̬Ϊ��������ʱ
                        //���̣��ǡ�¼��δ�ꡯ����Ϣ
                        for(TaskShow itemUnitEP:taskShowListTemp){
                            String strStatusFlagInTaskShowList=ToolUtil.getStrIgnoreNull(itemUnitEP.getStatusFlag());
                            if(itemUnitEP.getType().equals(strType)){
                                isHasTask=false;

                                /*//Ȩ��ɸѡ
                                hasPower=false;
                                for(OperResShow operResShowUnit:operResShowList){
                                    if(operResShowUnit.getInfoType().equals(strType)&&
                                            operResShowUnit.getInfoPkid().equals(itemUnitEP.getPkid())){
                                        hasPower=true;
                                        break;
                                    }
                                }
                                if(hasPower.equals(false)){
                                    continue;
                                }*/

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
                                        //��ɫ����
                                        if (taskShow.getStatusFlag()!=null){
                                            if (taskShow.getPreStatusFlag().equals("0")){
                                                taskShow.setPreStatusFlagType("1");
                                            }else {
                                                if ((int)(Integer.parseInt(itemUnitEP.getPreStatusFlag()))
                                                        >2*(int)(Integer.parseInt(itemUnitEP.getStatusFlag()))){
                                                    taskShow.setPreStatusFlagType("2");
                                                }else if ((int)(Integer.parseInt(itemUnitEP.getStatusFlag())+(int)Integer.parseInt(itemUnitEP.getStatusFlag()))-1
                                                        ==(int)Integer.parseInt(itemUnitEP.getStatusFlag())){
                                                    taskShow.setPreStatusFlagType("1");
                                                }
                                            }
                                        }else {
                                            taskShow.setPreStatusFlagType("2");
                                        }
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

    public OperResService getOperResService() {
        return operResService;
    }

    public void setOperResService(OperResService operResService) {
        this.operResService = operResService;
    }
}