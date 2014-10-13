package epss.view.settle;

import epss.common.enums.EnumFlowStatus;
import epss.common.enums.EnumResType;
import epss.repository.model.model_show.CttInfoShow;
import epss.repository.model.model_show.ProgStlInfoShow;
import epss.service.CttInfoService;
import epss.service.ProgStlInfoService;
import epss.service.ProgStlItemSubQService;
import epss.view.flow.EsCommon;
import epss.view.flow.EsFlowControl;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.util.MessageUtil;
import skyline.util.ToolUtil;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-4
 * Time: ����4:53
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class ProgStlInfoSubStlmentAction {
    private static final Logger logger = LoggerFactory.getLogger(ProgStlInfoSubStlmentAction.class);
    @ManagedProperty(value = "#{progStlInfoService}")
    private ProgStlInfoService progStlInfoService;
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;

    private ProgStlInfoShow progStlInfoShowQry;
    private List<ProgStlInfoShow> progStlInfoShowList;

    private List<SelectItem> subcttList;
    private List<SelectItem> subcttPStlFormFlagList;

    private String strCttInfoPkid;
    private String strSubcttPStlFormFlag;

    @PostConstruct
    public void init() {
        progStlInfoShowQry =new ProgStlInfoShow();
        progStlInfoShowList = new ArrayList<>();
        strCttInfoPkid="";
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if (parammap.containsKey("strCttInfoPkid")){
            strCttInfoPkid=parammap.get("strCttInfoPkid").toString();
            subcttPStlFormFlagList = new ArrayList<>();
            subcttPStlFormFlagList.add(new SelectItem("1", "���γ�"));
            subcttPStlFormFlagList.add(new SelectItem("0", "δ�γ�"));
            strSubcttPStlFormFlag="1";
        }

        //��ĳһ�ɱ��ƻ��µķְ���ͬ
        List<CttInfoShow> cttInfoShowList =
                cttInfoService.getCttInfoListByCttType_ParentPkid_Status(
                        EnumResType.RES_TYPE2.getCode()
                        , strCttInfoPkid
                        , EnumFlowStatus.FLOW_STATUS3.getCode());
        subcttList = new ArrayList<>();
        if (cttInfoShowList.size() > 0) {
            SelectItem selectItem = new SelectItem("", "ȫ��");
            subcttList.add(selectItem);
            for (CttInfoShow itemUnit : cttInfoShowList) {
                selectItem = new SelectItem();
                selectItem.setValue(itemUnit.getPkid());
                selectItem.setLabel(itemUnit.getName());
                subcttList.add(selectItem);
            }
        }
    }

    public void queryAction(String strQryFlag) {
        try {
            if(strSubcttPStlFormFlag.equals("0")) {
                progStlInfoShowList.clear();
                if (strQryFlag.equals("Approve")) {
                    List<ProgStlInfoShow> progStlInfoShowListTemp = new ArrayList<>();
                    List<ProgStlInfoShow> itemEsInitNotFormStlListTemp =
                            progStlInfoService.getNotFormSubcttStlP(
                                    strCttInfoPkid,
                                    ToolUtil.getStrIgnoreNull(progStlInfoShowQry.getStlPkid()),
                                    ToolUtil.getStrIgnoreNull(progStlInfoShowQry.getPeriodNo()));
                    List<ProgStlInfoShow> getFormPreSubcttStlP =
                            progStlInfoService.getFormPreSubcttStlP(
                                    strCttInfoPkid,
                                    ToolUtil.getStrIgnoreNull(progStlInfoShowQry.getStlPkid()),
                                    ToolUtil.getStrIgnoreNull(progStlInfoShowQry.getPeriodNo()));
                    for (ProgStlInfoShow itemUnitAll : itemEsInitNotFormStlListTemp) {
                        Boolean isHasSame = false;
                        for (ProgStlInfoShow itemUnitForming : getFormPreSubcttStlP) {
                            if (itemUnitAll.getPkid().equals(itemUnitForming.getPkid())) {
                                isHasSame = true;
                                break;
                            }
                        }
                        if (isHasSame.equals(false)) {
                            progStlInfoShowListTemp.add(itemUnitAll);
                        }
                    }

                    ProgStlInfoShow progStlInfoShow1Temp;
                    for (int i = 0; i < progStlInfoShowListTemp.size(); i++) {
                        if (progStlInfoShowListTemp.get(i).getStlType().equals("3")) {
                            progStlInfoShowListTemp.get(i).setId("�ְ���������(" + progStlInfoShowListTemp.get(i).getId() + ")");
                        } else if (progStlInfoShowListTemp.get(i).getStlType().equals("4")) {
                            progStlInfoShowListTemp.get(i).setId("�ְ����Ͻ���(" + progStlInfoShowListTemp.get(i).getId() + ")");
                        }
                        if (i == 0) {
                            progStlInfoShow1Temp = new ProgStlInfoShow();
                            progStlInfoShow1Temp.setPkid(i + "");
                            progStlInfoShow1Temp.setId("�ְ����㵥");
                            progStlInfoShow1Temp.setStlName(progStlInfoShowListTemp.get(i).getStlName());
                            progStlInfoShow1Temp.setSignPartBName(progStlInfoShowListTemp.get(i).getSignPartBName());
                            progStlInfoShow1Temp.setPeriodNo(progStlInfoShowListTemp.get(i).getPeriodNo());
                            progStlInfoShowList.add(progStlInfoShow1Temp);
                            progStlInfoShowList.add(progStlInfoShowListTemp.get(0));
                        } else {
                            if (progStlInfoShowListTemp.get(i - 1).getStlPkid().equals(
                                    progStlInfoShowListTemp.get(i).getStlPkid()) &&
                                    progStlInfoShowListTemp.get(i - 1).getPeriodNo().equals(
                                            progStlInfoShowListTemp.get(i).getPeriodNo())
                                    ) {
                                progStlInfoShowList.add(progStlInfoShowListTemp.get(i));
                            } else {
                                progStlInfoShow1Temp = new ProgStlInfoShow();
                                progStlInfoShow1Temp.setPkid(i + "");
                                progStlInfoShow1Temp.setId("�ְ����㵥");
                                progStlInfoShow1Temp.setStlName(progStlInfoShowListTemp.get(i).getStlName());
                                progStlInfoShow1Temp.setSignPartBName(progStlInfoShowListTemp.get(i).getSignPartBName());
                                progStlInfoShow1Temp.setPeriodNo(progStlInfoShowListTemp.get(i).getPeriodNo());
                                progStlInfoShowList.add(progStlInfoShow1Temp);
                                progStlInfoShowList.add(progStlInfoShowListTemp.get(i));
                            }
                        }
                    }
                } else if (strQryFlag.equals("Qry") || strQryFlag.equals("Print")) {

                }
            }else if(strSubcttPStlFormFlag.equals("1")){
                progStlInfoShowList.clear();
                if(strQryFlag.equals("Approve")){
                    List<ProgStlInfoShow> progStlInfoShowApprovedListTemp =
                            progStlInfoService.selectFormedEsInitSubcttStlP(
                                    strCttInfoPkid,
                                    ToolUtil.getStrIgnoreNull(progStlInfoShowQry.getStlPkid()),
                                    ToolUtil.getStrIgnoreNull(progStlInfoShowQry.getPeriodNo()));
                    for(int i=0;i< progStlInfoShowApprovedListTemp.size();i++){
                        if(progStlInfoShowApprovedListTemp.get(i).getFlowStatus().equals(EnumFlowStatus.FLOW_STATUS2.getCode())||
                                progStlInfoShowApprovedListTemp.get(i).getFlowStatus().equals(EnumFlowStatus.FLOW_STATUS3.getCode())){
                            progStlInfoShowList.add(progStlInfoShowApprovedListTemp.get(i));
                        }
                    }
                }
                else if(strQryFlag.equals("Qry")){
                    progStlInfoShowList =
                            progStlInfoService.getFormedAfterEsInitSubcttStlPList(
                                    strCttInfoPkid,
                                    ToolUtil.getStrIgnoreNull(progStlInfoShowQry.getStlPkid()),
                                    ToolUtil.getStrIgnoreNull(progStlInfoShowQry.getPeriodNo())
                            );

                }
                else if(strQryFlag.equals("Account")){
                    progStlInfoShowList =
                            progStlInfoService.getFormedAfterEsInitSubcttStlPList(
                                    strCttInfoPkid,
                                    ToolUtil.getStrIgnoreNull(progStlInfoShowQry.getStlPkid()),
                                    ToolUtil.getStrIgnoreNull(progStlInfoShowQry.getPeriodNo())
                            );
                }
            }
            if (progStlInfoShowList.isEmpty()) {
                MessageUtil.addWarn("û�в�ѯ�����ݡ�");
            }
        } catch (Exception e) {
            logger.error("��Ϣ��ѯʧ��", e);
            MessageUtil.addError("��Ϣ��ѯʧ��");
        }
    }

    /*�����ֶ�Start*/

    public List<SelectItem> getSubcttPStlFormFlagList() {
        return subcttPStlFormFlagList;
    }

    public void setSubcttPStlFormFlagList(List<SelectItem> subcttPStlFormFlagList) {
        this.subcttPStlFormFlagList = subcttPStlFormFlagList;
    }

    public List<ProgStlInfoShow> getProgStlInfoShowList() {
        return progStlInfoShowList;
    }

    public void setProgStlInfoShowList(List<ProgStlInfoShow> progStlInfoShowList) {
        this.progStlInfoShowList = progStlInfoShowList;
    }

    public ProgStlInfoService getProgStlInfoService() {
        return progStlInfoService;
    }

    public void setProgStlInfoService(ProgStlInfoService progStlInfoService) {
        this.progStlInfoService = progStlInfoService;
    }

    public EsFlowControl getEsFlowControl() {
        return esFlowControl;
    }

    public void setEsFlowControl(EsFlowControl esFlowControl) {
        this.esFlowControl = esFlowControl;
    }

    public ProgStlInfoShow getProgStlInfoShowQry() {
        return progStlInfoShowQry;
    }

    public void setProgStlInfoShowQry(ProgStlInfoShow progStlInfoShowQry) {
        this.progStlInfoShowQry = progStlInfoShowQry;
    }

    public CttInfoService getCttInfoService() {
        return cttInfoService;
    }

    public void setCttInfoService(CttInfoService cttInfoService) {
        this.cttInfoService = cttInfoService;
    }

    public List<SelectItem> getSubcttList() {
        return subcttList;
    }

    public String getStrSubcttPStlFormFlag() {
        return strSubcttPStlFormFlag;
    }

    public void setStrSubcttPStlFormFlag(String strSubcttPStlFormFlag) {
        this.strSubcttPStlFormFlag = strSubcttPStlFormFlag;
    }
    /*�����ֶ�End*/
}
