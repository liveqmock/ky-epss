package epss.view.settle;

import epss.common.enums.EnumFlowStatus;
import epss.common.enums.EnumResType;
import epss.common.enums.EnumSubcttType;
import epss.repository.model.model_show.CttInfoShow;
import epss.repository.model.model_show.ProgStlInfoShow;
import epss.service.CttInfoService;
import epss.service.ProgStlInfoService;
import epss.view.flow.EsFlowControl;
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
 * Time: 下午4:53
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

    private String strSubcttPStlFormFlag;

    @PostConstruct
    public void init() {
        try {
            progStlInfoShowQry =new ProgStlInfoShow();
            progStlInfoShowList = new ArrayList<>();
            subcttPStlFormFlagList = new ArrayList<>();
            subcttPStlFormFlagList.add(new SelectItem("0", "未形成"));
            subcttPStlFormFlagList.add(new SelectItem("1", "已形成"));
            strSubcttPStlFormFlag="0";

            //在某一成本计划下的分包合同
            CttInfoShow cttInfoShowPara=new CttInfoShow();
            cttInfoShowPara.setCttType(EnumResType.RES_TYPE2.getCode());
            cttInfoShowPara.setFlowStatus(EnumFlowStatus.FLOW_STATUS3.getCode());
            List<CttInfoShow> cttInfoShowList =
                    cttInfoService.getListShowByModelShow(cttInfoShowPara);
            subcttList = new ArrayList<>();
            if (cttInfoShowList.size() > 0) {
                SelectItem selectItem = new SelectItem("", "全部");
                subcttList.add(selectItem);
                for (CttInfoShow itemUnit : cttInfoShowList) {
                    selectItem = new SelectItem();
                    selectItem.setValue(itemUnit.getPkid());
                    selectItem.setLabel(itemUnit.getName());
                    subcttList.add(selectItem);
                }
            }
        }catch (Exception e){
            logger.error("初始化失败", e);
        }
    }

    public void queryAction() {
        try {
            if(strSubcttPStlFormFlag.equals("0")) {
                progStlInfoShowList.clear();
                List<ProgStlInfoShow> progStlInfoShowListTemp = new ArrayList<>();
                List<ProgStlInfoShow> itemEsInitNotFormStlListTemp =
                        progStlInfoService.getNotFormSubcttStlP(
                                ToolUtil.getStrIgnoreNull(progStlInfoShowQry.getStlPkid()),
                                ToolUtil.getStrIgnoreNull(progStlInfoShowQry.getPeriodNo()));
                List<ProgStlInfoShow> getFormPreSubcttStlP =
                        progStlInfoService.getFormPreSubcttStlP(
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
                    ProgStlInfoShow progStlInfoShowTemp=progStlInfoShowListTemp.get(i);
                    progStlInfoShowTemp.setTypeName(EnumSubcttType.getValueByKey(progStlInfoShowTemp.getType()).getTitle());
                    if (progStlInfoShowTemp.getStlType().equals("3")) {
                        progStlInfoShowTemp.setId("分包数量结算(" + progStlInfoShowTemp.getId() + ")");
                    } else if (progStlInfoShowTemp.getStlType().equals("4")) {
                        progStlInfoShowTemp.setId("分包材料结算(" + progStlInfoShowTemp.getId() + ")");
                    }
                    if (i == 0) {
                        progStlInfoShow1Temp = new ProgStlInfoShow();
                        progStlInfoShow1Temp.setPkid(i + "");
                        progStlInfoShow1Temp.setId("分包结算单(合同类型："+progStlInfoShowTemp.getTypeName()+")");
                        progStlInfoShow1Temp.setStlName(progStlInfoShowTemp.getStlName());
                        progStlInfoShow1Temp.setSignPartBName(progStlInfoShowTemp.getSignPartBName());
                        progStlInfoShow1Temp.setPeriodNo(progStlInfoShowTemp.getPeriodNo());
                        progStlInfoShow1Temp.setRowStyle("Group");
                        progStlInfoShowList.add(progStlInfoShow1Temp);
                        progStlInfoShowList.add(progStlInfoShowListTemp.get(0));
                    } else {
                        if (progStlInfoShowListTemp.get(i - 1).getStlPkid().equals(
                                progStlInfoShowTemp.getStlPkid()) &&
                                progStlInfoShowListTemp.get(i - 1).getPeriodNo().equals(
                                        progStlInfoShowTemp.getPeriodNo())
                                ) {
                            progStlInfoShowList.add(progStlInfoShowTemp);
                        } else {
                            progStlInfoShow1Temp = new ProgStlInfoShow();
                            progStlInfoShow1Temp.setPkid(i + "");
                            progStlInfoShow1Temp.setId("分包结算单(合同类型："+progStlInfoShowTemp.getTypeName()+")");
                            progStlInfoShow1Temp.setStlName(progStlInfoShowTemp.getStlName());
                            progStlInfoShow1Temp.setSignPartBName(progStlInfoShowTemp.getSignPartBName());
                            progStlInfoShow1Temp.setPeriodNo(progStlInfoShowTemp.getPeriodNo());
                            progStlInfoShow1Temp.setRowStyle("Group");
                            progStlInfoShowList.add(progStlInfoShow1Temp);
                            progStlInfoShowList.add(progStlInfoShowTemp);
                        }
                    }
                }
            }else if(strSubcttPStlFormFlag.equals("1")){
                progStlInfoShowList.clear();
                progStlInfoShowList =
                progStlInfoService.getFormedAfterEsInitSubcttStlPList(
                        ToolUtil.getStrIgnoreNull(progStlInfoShowQry.getStlPkid()),
                        ToolUtil.getStrIgnoreNull(progStlInfoShowQry.getPeriodNo())
                );
            }
            if (progStlInfoShowList.isEmpty()) {
                MessageUtil.addWarn("没有查询到数据。");
            }
        } catch (Exception e) {
            logger.error("信息查询失败", e);
            MessageUtil.addError("信息查询失败");
        }
    }

    /*智能字段Start*/

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
    /*智能字段End*/
}
