package epss.view.task;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumPreStatusFlag;
import epss.common.enums.ESEnumStatusFlag;
import epss.common.utils.ToolUtil;
import epss.repository.model.model_show.TaskShow;
import epss.service.EsCommonService;
import epss.service.FlowCtrlService;
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
    @ManagedProperty(value = "#{esCommonService}")
    private EsCommonService esCommonService;
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
            //��������ʼ���ض����ͺ�״̬��ͬ������
            Integer intGroupCounts=0;
            //��������ʼ���жϺ�ͬ�Ƿ����ض����ͺ�״̬�Ĳ�������
            Boolean isHasTask=false;
            //��������ʼ�����һ��״̬��ͬ�ļ���
            List<TaskShow> inputNotFinishTaskShowList;

            //����ͬ״̬Ϊ¼��ʱ
            if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())){
                //���ݺ�ͬ�������ܳɷֺ�ͬ�����������͵ĺ�ͬ���в�ѯ����Ϊ�ְ��������ְ����ϵ������ڵ��޶���
                if (strType.equals(ESEnum.ITEMTYPE0.getCode())||
                    strType.equals(ESEnum.ITEMTYPE1.getCode())||
                    strType.equals(ESEnum.ITEMTYPE2.getCode())){
                    inputNotFinishTaskShowList =esCommonService.getTaskModelListOfCtt(strType);
                }else {
                    inputNotFinishTaskShowList =esCommonService.getTaskModelListOfStl(strType);
                }
                //����ź�ͬ�ļ��ϲ�Ϊ��ʱ��������ʾ�б����������еı�ͷ��С��������Ϊ��ͬ����+��ͬ״̬+��ͬ������
                //ͬʱ����Ӧ�������б�ͷ��ֵ
                if (inputNotFinishTaskShowList.size()>0){
                    taskShow.setName(itemUnit.getMenulabel() + "(" + inputNotFinishTaskShowList.size() + ")");
                    taskShowList.add(taskShow);
                    //����ź�ͬ�ļƻ���Ϊ��ʱ���������ض����ͺ�״̬�ĺ�ͬ������¼�����͵ĺ�ͬ�ľ�����Ϣ��ӵ���ʾ�����
                    for(TaskShow itemUnitEP: inputNotFinishTaskShowList){
                        taskShow =new TaskShow();
                        taskShow.setType(itemUnitEP.getType());
                        taskShow.setPkid(itemUnitEP.getPkid());
                        taskShow.setId(itemUnitEP.getId());
                        taskShow.setName("&#8195;" + itemUnitEP.getName());
                        taskShow.setPeriodNo(itemUnitEP.getPeriodNo());
                        taskShow.setName(itemUnitEP.getName());
                        taskShow.setStatusFlag(itemUnitEP.getStatusFlag());
                        if(itemUnitEP.getStatusFlag()!=null) {
                            taskShow.setStatusFlagName(
                                    ESEnumStatusFlag.getValueByKey(itemUnitEP.getStatusFlag()).getTitle());
                        }
                        taskShow.setPreStatusFlag(itemUnitEP.getPreStatusFlag());
                        if(itemUnitEP.getPreStatusFlag()!=null) {
                            taskShow.setPreStatusFlagName(
                                    ESEnumPreStatusFlag.getValueByKey(itemUnitEP.getPreStatusFlag()).getTitle());
                        }
                        taskShowList.add(taskShow);
                    }
                }else {
                    continue;
                }
            }

            // �Ժ�ͬ���ͺ�״̬Ϊ����,ȡ�ø��������
            List<TaskShow> taskCountsInFlowGroupListTemp =esCommonService.getTaskCountsInFlowGroup();
            //������š���ͬ����+״̬+��������Ϣ�ļ���
            for(TaskShow itemUnitCM: taskCountsInFlowGroupListTemp){
                //�����б�type���ź���ģ����򣩣����ڱȽϵĲ˵�type�Ͳ����ٱȽ���
                if(ToolUtil.getStrIgnoreNull(itemUnitCM.getType()).compareTo(strType)>0){
                    break;
                } //���������ͺ�ͬ����һ�µ�ʱ��
                if(itemUnitCM.getType().equals(strType)){
                    //��֤Խ���Ĳ������
                    isHasTask=false;
                    //����ͬ״̬Ϊ��ʼʱ������"¼��"�еĴ���������
                    /*if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())){
                        isHasTask=true;//����ͬ״̬Ϊ���ʱ����������״̬Ϊ��ʼ��ʱ�����ǡ���ˡ��еĴ���������
                    }else */
                    if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG1.getCode())){
                        if(itemUnitCM.getStatusFlag().equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())){
                            isHasTask=true;//��������ı�ͷ��ֵ
                        } //����ͬ״̬Ϊ����ʱ����������״̬Ϊ��˵�ʱ�����ǡ����ˡ��еĴ���������
                    }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG2.getCode())){
                        if(itemUnitCM.getStatusFlag().equals(ESEnumStatusFlag.STATUS_FLAG1.getCode())){
                            isHasTask=true;
                        }//����ͬ״̬Ϊ��׼ʱ����������״̬Ϊ���˵�ʱ�����ǡ���׼���еĴ���������
                    }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG3.getCode())){
                        if(itemUnitCM.getStatusFlag().equals(ESEnumStatusFlag.STATUS_FLAG2.getCode())){
                            isHasTask=true;
                        }//����ͬ״̬Ϊ����ʱ����������״̬Ϊ��׼��ʱ�����ǡ����ˡ��еĴ���������
                    }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG4.getCode())){
                        if(itemUnitCM.getStatusFlag().equals(ESEnumStatusFlag.STATUS_FLAG3.getCode())){
                            isHasTask=true;
                        }
                    } //������Ϊ����������ʱ��ͳ�ƴ����������е��������
                    if(isHasTask.equals(true)){
                        intGroupCounts=intGroupCounts+Integer.parseInt(itemUnitCM.getRecordsCountInGroup());
                    }
                }
            }

            //������������ĸ�����Ϊ�յ�ʱ�򣬽�ֵ��������������ı�ͷ
            if(intGroupCounts>0){
                taskShow.setName(itemUnit.getMenulabel()+"("+intGroupCounts+")");
                taskShow.setPkid("");
            }
            else{
                continue;
            }
            taskShowList.add(taskShow);

            //����ͬ״̬Ϊ��������ʱ
            //���̣��ǡ�¼��δ�ꡯ����Ϣ
            List<TaskShow> esInitPowerList=esCommonService.getTaskModelList();
            for(TaskShow itemUnitEP:esInitPowerList){
                if(ToolUtil.getStrIgnoreNull(itemUnitEP.getType()).compareTo(strType)>0){
                    break;
                }
                if(itemUnitEP.getType().equals(strType)){
                    isHasTask=false;
                    /*if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())){
                        if(itemUnitEP.getStatus_Flag().equals("")){
                            isHasTask=true;
                        }
                    }else*/ if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG1.getCode())){
                        if(itemUnitEP.getStatusFlag().equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())){
                            isHasTask=true;
                        }
                    }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG2.getCode())){
                        if(itemUnitEP.getStatusFlag().equals(ESEnumStatusFlag.STATUS_FLAG1.getCode())){
                            isHasTask=true;
                        }
                    }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG3.getCode())){
                        if(itemUnitEP.getStatusFlag().equals(ESEnumStatusFlag.STATUS_FLAG2.getCode())){
                            isHasTask=true;
                        }
                    }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG4.getCode())){
                        if(itemUnitEP.getStatusFlag().equals(ESEnumStatusFlag.STATUS_FLAG3.getCode())){
                            isHasTask=true;
                        }
                    }
                    //������Ǵ�������������ʾ�б�����������к������еľ�����Ϣ
                    if(isHasTask.equals(true)){
                        taskShow =new TaskShow();
                        taskShow.setType(itemUnitEP.getType());
                        taskShow.setPkid(itemUnitEP.getPkid());
                        taskShow.setId(itemUnitEP.getId());
                        taskShow.setName("&#8195;" + itemUnitEP.getName());
                        taskShow.setPeriodNo(itemUnitEP.getPeriodNo());
                        taskShow.setName(itemUnitEP.getName());
                        taskShow.setStatusFlag(itemUnitEP.getStatusFlag());
                        if(itemUnitEP.getStatusFlag()!=null) {
                            taskShow.setStatusFlagName(
                                    ESEnumStatusFlag.getValueByKey(itemUnitEP.getStatusFlag()).getTitle());
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

    public EsCommonService getEsCommonService() {
        return esCommonService;
    }

    public void setEsCommonService(EsCommonService esCommonService) {
        this.esCommonService = esCommonService;
    }

    public FlowCtrlService getFlowCtrlService() {
        return flowCtrlService;
    }

    public void setFlowCtrlService(FlowCtrlService flowCtrlService) {
        this.flowCtrlService = flowCtrlService;
    }
}