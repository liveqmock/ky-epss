package epss.view.task;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumStatusFlag;
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
        //��ȡ�����˵���������ַ���˵������ȵȣ�����ͨ��OperatorManagerʵ�ֲ�ͬȨ���²˵�����ʾ
        List<Ptmenu> ptmenuList = platformService.getPtmenuList();
        //���ݺ�ͬ���ͺ�״̬��ȡ��ͬ������+״̬+������Ϣ
        List<TaskShow> taskShowListTemp =esCommonService.getPowerListByPowerTypeAndStatusFlag();
        //��ȡָ�����ͺ�״̬�º�ͬ�ľ�����Ϣ
        List<TaskShow> esInitPowerList=esCommonService.getTaskModelList();
        //��ͬ����
        String strType;
        //��ͬ״̬
        String strStatusFlag;
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
            //������taskShow������Power_Pkid��ֵ
            taskShow.setPower_Pkid(itemUnit.getMenuid());
            //��������ʼ���ض����ͺ�״̬��ͬ������
            Integer intGroupCounts=0;
            //��������ʼ���жϺ�ͬ�Ƿ����ض����ͺ�״̬�Ĳ�������
            Boolean isHasTask=false;
            //��������ʼ�����һ��״̬��ͬ�ļ���
            List<TaskShow> taskShowListOfStatusFlag =null;
            //����ͬ״̬Ϊ¼��ʱ
            if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())){
                //���ݺ�ͬ�������ܳɷֺ�ͬ�����������͵ĺ�ͬ���в�ѯ����Ϊ�ְ��������ְ����ϵ������ڵ��޶���
                if (strType.equals(ESEnum.ITEMTYPE0.getCode())||strType.equals(ESEnum.ITEMTYPE1.getCode())||strType.equals(ESEnum.ITEMTYPE2.getCode())){
                    taskShowListOfStatusFlag =esCommonService.getTaskModelListOfCtt(strType);
                }else {
                    taskShowListOfStatusFlag =esCommonService.getTaskModelListOfStl(strType);
                }
                //����ź�ͬ�ļ��ϲ�Ϊ��ʱ��������ʾ�б����������еı�ͷ��С��������Ϊ��ͬ����+��ͬ״̬+��ͬ������
                //ͬʱ����Ӧ�������б�ͷ��ֵ
                if (taskShowListOfStatusFlag.size()>0){
                    taskShow.setPower_Type(itemUnit.getMenulabel() + "(" + taskShowListOfStatusFlag.size() + ")");
                    if (strType.equals(ESEnum.ITEMTYPE0.getCode())){
                        taskShow.setCttType("�ܰ���ͬ¼��");
                    }
                    if(strType.equals(ESEnum.ITEMTYPE1.getCode())){
                        taskShow.setCttType("�ɱ��ƻ�¼��");
                    }
                    if (strType.equals(ESEnum.ITEMTYPE2.getCode())){
                        taskShow.setCttType("�ְ���ͬ¼��");
                    }
                    if (strType.equals(ESEnum.ITEMTYPE3.getCode())){
                        taskShow.setCttType("�ְ���������¼��");
                    }
                    if(strType.equals(ESEnum.ITEMTYPE4.getCode())){
                        taskShow.setCttType("�ְ����Ͻ���¼��");
                    }
                    if (strType.equals(ESEnum.ITEMTYPE5.getCode())){
                        taskShow.setCttType("�ְ��۸����¼��");
                    }
                    if (strType.equals(ESEnum.ITEMTYPE6.getCode())){
                        taskShow.setCttType("�ܰ�����ͳ��¼��");
                    }
                    if(strType.equals(ESEnum.ITEMTYPE7.getCode())){
                        taskShow.setCttType("�ܰ���������¼��");
                    }
                    taskShowList.add(taskShow);
                }else {
                    continue;
                }//����ź�ͬ�ļƻ���Ϊ��ʱ���������ض����ͺ�״̬�ĺ�ͬ������¼�����͵ĺ�ͬ�ľ�����Ϣ��ӵ���ʾ�����
                if (taskShowListOfStatusFlag !=null){
                    for(TaskShow itemUnitEP: taskShowListOfStatusFlag){
                        if (strType.equals(ESEnum.ITEMTYPE0.getCode())){
                            itemUnitEP.setCttType("�ܰ���ͬ¼��");
                        }
                        if(strType.equals(ESEnum.ITEMTYPE1.getCode())){
                            itemUnitEP.setCttType("�ɱ��ƻ�¼��");
                        }
                        if (strType.equals(ESEnum.ITEMTYPE2.getCode())){
                            itemUnitEP.setCttType("�ְ���ͬ¼��");
                        }
                        if (strType.equals(ESEnum.ITEMTYPE3.getCode())){
                            itemUnitEP.setCttType("�ְ���������¼��");
                        }
                        if(strType.equals(ESEnum.ITEMTYPE4.getCode())){
                            itemUnitEP.setCttType("�ְ����Ͻ���¼��");
                        }
                        if (strType.equals(ESEnum.ITEMTYPE5.getCode())){
                            itemUnitEP.setCttType("�ְ��۸����¼��");
                        }
                        if (strType.equals(ESEnum.ITEMTYPE6.getCode())){
                            itemUnitEP.setCttType("�ܰ�����ͳ��¼��");
                        }
                        if(strType.equals(ESEnum.ITEMTYPE7.getCode())){
                            itemUnitEP.setCttType("�ܰ���������¼��");
                        }
                        taskShow =new TaskShow();
                        taskShow.setPower_Pkid(itemUnitEP.getPower_Pkid());
                        taskShow.setId(itemUnitEP.getId());
                        taskShow.setName("&#8195;" + itemUnitEP.getName());
                        taskShow.setPower_Type("&#8195;" + itemUnitEP.getParentName());
                        taskShow.setPeriod_No(itemUnitEP.getPeriod_No());
                        taskShow.setName(itemUnitEP.getName());
                        taskShow.setStatus_Flag(itemUnitEP.getStatus_Flag());
                        taskShow.setPre_Status_Flag(itemUnitEP.getPre_Status_Flag());
                        taskShow.setCttType(itemUnitEP.getCttType());
                        taskShowList.add(taskShow);
                    } }
                continue;
            }
            //������š���ͬ����+״̬+��������Ϣ�ļ���
            for(TaskShow itemUnitCM: taskShowListTemp){
                //����������Ϊ�յĻ�����ֵΪ��
                if (itemUnitCM.getPower_Type()==null){
                    itemUnitCM.setPower_Type("");
                }
                //����������ͺ�ͬ���Ͳ����Ļ�����ֹ
                if(itemUnitCM.getPower_Type().compareTo(strType)>0){
                    break;
                } //���������ͺ�ͬ����һ�µ�ʱ��
                if(itemUnitCM.getPower_Type().equals(strType)){
                    //�������״̬Ϊ�գ���ֵΪ��
                    if (itemUnitCM.getStatus_Flag()==null){
                        itemUnitCM.setStatus_Flag("");
                    }
                    //��֤Խ���Ĳ������
                    isHasTask=false;
                    //����ͬ״̬Ϊ��ʼʱ������"¼��"�еĴ���������
                    if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())){
                        isHasTask=true;//����ͬ״̬Ϊ���ʱ����������״̬Ϊ��ʼ��ʱ�����ǡ���ˡ��еĴ���������
                    }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG1.getCode())){
                        if(itemUnitCM.getStatus_Flag().equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())){
                            isHasTask=true;//��������ı�ͷ��ֵ
                            if (strType.equals(ESEnum.ITEMTYPE0.getCode())){
                                taskShow.setCttType("�ܰ���ͬ���");
                            }
                            if(strType.equals(ESEnum.ITEMTYPE1.getCode())){
                                taskShow.setCttType("�ɱ��ƻ����");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE2.getCode())){
                                taskShow.setCttType("�ְ���ͬ���");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE3.getCode())){
                                taskShow.setCttType("�ְ������������");
                            }
                            if(strType.equals(ESEnum.ITEMTYPE4.getCode())){
                                taskShow.setCttType("�ְ����Ͻ������");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE5.getCode())){
                                taskShow.setCttType("�ְ��۸�������");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE6.getCode())){
                                taskShow.setCttType("�ܰ�����ͳ�����");
                            }
                            if(strType.equals(ESEnum.ITEMTYPE7.getCode())){
                                taskShow.setCttType("�ܰ������������");
                            }
                        } //����ͬ״̬Ϊ����ʱ����������״̬Ϊ��˵�ʱ�����ǡ����ˡ��еĴ���������
                    }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG2.getCode())){
                        if(itemUnitCM.getStatus_Flag().equals(ESEnumStatusFlag.STATUS_FLAG1.getCode())){
                            isHasTask=true;
                            if (strType.equals(ESEnum.ITEMTYPE0.getCode())){
                                taskShow.setCttType("�ܰ���ͬ����");
                            }
                            if(strType.equals(ESEnum.ITEMTYPE1.getCode())){
                                taskShow.setCttType("�ɱ��ƻ�����");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE2.getCode())){
                                taskShow.setCttType("�ְ���ͬ����");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE3.getCode())){
                                taskShow.setCttType("�ְ��������㸴��");
                            }
                            if(strType.equals(ESEnum.ITEMTYPE4.getCode())){
                                taskShow.setCttType("�ְ����Ͻ��㸴��");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE5.getCode())){
                                taskShow.setCttType("�ְ��۸���㸴��");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE6.getCode())){
                                taskShow.setCttType("�ܰ�����ͳ�Ƹ���");
                            }
                            if(strType.equals(ESEnum.ITEMTYPE7.getCode())){
                                taskShow.setCttType("�ܰ�������������");
                            }
                        }//����ͬ״̬Ϊ��׼ʱ����������״̬Ϊ���˵�ʱ�����ǡ���׼���еĴ���������
                    }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG3.getCode())){
                        if(itemUnitCM.getStatus_Flag().equals(ESEnumStatusFlag.STATUS_FLAG2.getCode())){
                            isHasTask=true;
                            if (strType.equals(ESEnum.ITEMTYPE0.getCode())){
                                taskShow.setCttType("�ܰ���ͬ��׼");
                            }
                            if(strType.equals(ESEnum.ITEMTYPE1.getCode())){
                                taskShow.setCttType("�ɱ��ƻ���׼");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE2.getCode())){
                                taskShow.setCttType("�ְ���ͬ��׼");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE3.getCode())){
                                taskShow.setCttType("�ְ�����������׼");
                            }
                            if(strType.equals(ESEnum.ITEMTYPE4.getCode())){
                                taskShow.setCttType("�ְ����Ͻ�����׼");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE5.getCode())){
                                taskShow.setCttType("�ְ��۸������׼");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE6.getCode())){
                                taskShow.setCttType("�ܰ�����ͳ����׼");
                            }
                            if(strType.equals(ESEnum.ITEMTYPE7.getCode())){
                                taskShow.setCttType("�ܰ�����������׼");
                            }
                        }//����ͬ״̬Ϊ����ʱ����������״̬Ϊ��׼��ʱ�����ǡ����ˡ��еĴ���������
                    }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG4.getCode())){
                        if(itemUnitCM.getStatus_Flag().equals(ESEnumStatusFlag.STATUS_FLAG3.getCode())){
                            isHasTask=true;
                            if (strType.equals(ESEnum.ITEMTYPE5.getCode())){
                                taskShow.setCttType("�ְ��۸�������");
                            }
                        }
                    } //������Ϊ����������ʱ��ͳ�ƴ����������е��������
                    if(isHasTask.equals(true)){
                        intGroupCounts=intGroupCounts+Integer.parseInt(itemUnitCM.getGroup_Counts());
                    }
                }
            } //������������ĸ�����Ϊ�յ�ʱ�򣬽�ֵ��������������ı�ͷ
            if(intGroupCounts>0){
                taskShow.setPower_Type(itemUnit.getMenulabel()+"("+intGroupCounts+")");
                taskShow.setPower_Pkid("");
            }
            else{
                continue;
            }
            taskShowList.add(taskShow);
            /*������š�ָ�����ͺ�״̬�º�ͬ���ļ���
            ����ͬ״̬Ϊ��ʼʱ������"¼��"�еĴ���������
            ����ͬ״̬Ϊ���ʱ����������״̬Ϊ��ʼ��ʱ�����ǡ���ˡ��еĴ���������
            ����ͬ״̬Ϊ����ʱ����������״̬Ϊ��˵�ʱ�����ǡ����ˡ��еĴ���������
            ����ͬ״̬Ϊ��׼ʱ����������״̬Ϊ���˵�ʱ�����ǡ���׼���еĴ���������
            ����ͬ״̬Ϊ����ʱ����������״̬Ϊ��׼��ʱ�����ǡ����ˡ��еĴ���������*/
            for(TaskShow itemUnitEP:esInitPowerList){
                if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG1.getCode())&&
                        itemUnitEP.getStatus_Flag().equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())){
                }
                if (itemUnitEP.getPower_Type()==null){
                    itemUnitEP.setPower_Type("");
                }
                if(itemUnitEP.getPower_Type().compareTo(strType)>0){
                    break;
                }
                if(itemUnitEP.getPower_Type().equals(strType)){
                    if (itemUnitEP.getStatus_Flag()==null){
                        itemUnitEP.setStatus_Flag("");
                    }
                    isHasTask=false;
                    if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())){
                        if(itemUnitEP.getStatus_Flag().equals("")){
                            isHasTask=true;
                        }
                    }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG1.getCode())){
                        if(itemUnitEP.getStatus_Flag().equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())){
                            isHasTask=true;
                        }
                    }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG2.getCode())){
                        if(itemUnitEP.getStatus_Flag().equals(ESEnumStatusFlag.STATUS_FLAG1.getCode())){
                            isHasTask=true;
                        }
                    }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG3.getCode())){
                        if(itemUnitEP.getStatus_Flag().equals(ESEnumStatusFlag.STATUS_FLAG2.getCode())){
                            isHasTask=true;
                        }
                    }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG4.getCode())){
                        if(itemUnitEP.getStatus_Flag().equals(ESEnumStatusFlag.STATUS_FLAG3.getCode())){
                            isHasTask=true;
                        }
                    }
                    //������Ǵ�������������ʾ�б�����������к������еľ�����Ϣ
                    if(isHasTask.equals(true)){
                        if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG1.getCode())&&
                                itemUnitEP.getStatus_Flag().equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())){
                            if (strType.equals(ESEnum.ITEMTYPE0.getCode())){
                                itemUnitEP.setCttType("�ܰ���ͬ���");
                            }
                            if(strType.equals(ESEnum.ITEMTYPE1.getCode())){
                                itemUnitEP.setCttType("�ɱ��ƻ����");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE2.getCode())){
                                itemUnitEP.setCttType("�ְ���ͬ���");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE3.getCode())){
                                itemUnitEP.setCttType("�ְ������������");
                            }
                            if(strType.equals(ESEnum.ITEMTYPE4.getCode())){
                                itemUnitEP.setCttType("�ְ����Ͻ������");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE5.getCode())){
                                itemUnitEP.setCttType("�ְ��۸�������");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE6.getCode())){
                                itemUnitEP.setCttType("�ܰ�����ͳ�����");
                            }
                            if(strType.equals(ESEnum.ITEMTYPE7.getCode())){
                                itemUnitEP.setCttType("�ܰ������������");
                            }
                        }
                        if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG2.getCode())&&
                                itemUnitEP.getStatus_Flag().equals(ESEnumStatusFlag.STATUS_FLAG1.getCode())){
                            if (strType.equals(ESEnum.ITEMTYPE0.getCode())){
                                itemUnitEP.setCttType("�ܰ���ͬ����");
                            }
                            if(strType.equals(ESEnum.ITEMTYPE1.getCode())){
                                itemUnitEP.setCttType("�ɱ��ƻ�����");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE2.getCode())){
                                itemUnitEP.setCttType("�ְ���ͬ����");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE3.getCode())){
                                itemUnitEP.setCttType("�ְ��������㸴��");
                            }
                            if(strType.equals(ESEnum.ITEMTYPE4.getCode())){
                                itemUnitEP.setCttType("�ְ����Ͻ��㸴��");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE5.getCode())){
                                itemUnitEP.setCttType("�ְ��۸���㸴��");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE6.getCode())){
                                itemUnitEP.setCttType("�ܰ�����ͳ�Ƹ���");
                            }
                            if(strType.equals(ESEnum.ITEMTYPE7.getCode())){
                                itemUnitEP.setCttType("�ܰ�������������");
                            }
                        }
                        if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG3.getCode())&&
                                itemUnitEP.getStatus_Flag().equals(ESEnumStatusFlag.STATUS_FLAG2.getCode())){
                            if (strType.equals(ESEnum.ITEMTYPE0.getCode())){
                                itemUnitEP.setCttType("�ܰ���ͬ��׼");
                            }
                            if(strType.equals(ESEnum.ITEMTYPE1.getCode())){
                                itemUnitEP.setCttType("�ɱ��ƻ���׼");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE2.getCode())){
                                itemUnitEP.setCttType("�ְ���ͬ��׼");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE3.getCode())){
                                itemUnitEP.setCttType("�ְ�����������׼");
                            }
                            if(strType.equals(ESEnum.ITEMTYPE4.getCode())){
                                itemUnitEP.setCttType("�ְ����Ͻ�����׼");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE5.getCode())){
                                itemUnitEP.setCttType("�ְ��۸������׼");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE6.getCode())){
                                itemUnitEP.setCttType("�ܰ�����ͳ����׼");
                            }
                            if(strType.equals(ESEnum.ITEMTYPE7.getCode())){
                                itemUnitEP.setCttType("�ܰ�����������׼");
                            }
                        }
                        if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG4.getCode())&&
                                itemUnitEP.getStatus_Flag().equals(ESEnumStatusFlag.STATUS_FLAG3.getCode())){
                            if (strType.equals(ESEnum.ITEMTYPE5.getCode())){
                                itemUnitEP.setCttType("�ְ��۸�������");
                            }
                        }
                        taskShow =new TaskShow();
                        taskShow.setPower_Pkid(itemUnitEP.getPower_Pkid());
                        taskShow.setId(itemUnitEP.getId());
                        taskShow.setName("&#8195;"+itemUnitEP.getName());
                        taskShow.setPower_Type("&#8195;"+itemUnitEP.getParentName());
                        taskShow.setPeriod_No(itemUnitEP.getPeriod_No());
                        taskShow.setName(itemUnitEP.getName());
                        taskShow.setStatus_Flag(itemUnitEP.getStatus_Flag());
                        taskShow.setPre_Status_Flag(itemUnitEP.getPre_Status_Flag());
                        taskShow.setCttType(itemUnitEP.getCttType());
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

    public void setTaskShowList(List<TaskShow> taskShowList) {
        this.taskShowList = taskShowList;
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