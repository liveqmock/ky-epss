package epss.view.scrollInfo;

import epss.common.utils.ToolUtil;
import epss.repository.model.model_show.TaskShow;
import epss.service.ScrollInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.List;

/**
 * Created by XIANGYANG on 2014/8/11.
 */
@ManagedBean
@ViewScoped
public class ScrollInfoAction implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(ScrollInfoAction.class);
    @ManagedProperty(value = "#{scrollInfoService}")
    private ScrollInfoService scrollInfoService;

    private String strCurrentViewMng;
    private String strLastViewMng;

    @PostConstruct
    public void init() {
        getViewMsg();
        if ("".equals(ToolUtil.getStrIgnoreNull(strCurrentViewMng))){
            strCurrentViewMng="欢迎您！！";
        }
        strLastViewMng=strCurrentViewMng;
    }

    public void getViewMsg() {
        getViewMsgFromTaskAction();
    }
    private void getViewMsgFromTaskAction(){
        List<TaskShow> taskShowTempList = scrollInfoService.getViewMsgFromTask();
        if (taskShowTempList != null) {
            String strViewMngTemp="待处理任务：";
            for (TaskShow taskShowItem : taskShowTempList) {
                strViewMngTemp+=taskShowItem.getName()+";";
            }
            if (!(strViewMngTemp.equals(strLastViewMng))){
                strCurrentViewMng=strViewMngTemp;
                strLastViewMng=strCurrentViewMng;
                ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
                HttpSession session = (HttpSession) extContext.getSession(true);
                session.setAttribute("strDefaultMenuUpt",strCurrentViewMng.substring("待处理任务：".length()));
            }
        }
    }

    /*智能字段 Start*/

    public ScrollInfoService getScrollInfoService() {
        return scrollInfoService;
    }

    public void setScrollInfoService(ScrollInfoService scrollInfoService) {
        this.scrollInfoService = scrollInfoService;
    }

    public String getStrCurrentViewMng() {
        return strCurrentViewMng;
    }

    public void setStrCurrentViewMng(String strCurrentViewMng) {
        this.strCurrentViewMng = strCurrentViewMng;
    }
}
