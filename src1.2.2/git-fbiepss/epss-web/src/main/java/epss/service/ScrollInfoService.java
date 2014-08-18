package epss.service;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumStatusFlag;
import epss.common.utils.ToolUtil;
import epss.repository.model.model_show.OperResShow;
import epss.repository.model.model_show.TaskShow;
import org.springframework.stereotype.Service;
import skyline.repository.model.Ptmenu;
import skyline.service.PlatformService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XIANGYANG on 2014/7/27.
 */
@Service
public class ScrollInfoService {
    @Resource
    private PlatformService platformService;
    @Resource
    private TaskService taskService;
    @Resource
    private OperResService operResService;

    private List<TaskShow> taskShowList;

    public List<TaskShow> getViewMsgFromTask(){
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
                            break;
                        }
                    }
                }
            }
        return taskShowList;
    }
}
