package epss.view.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.platform.security.OnLineOpersManager;
import skyline.platform.security.OperatorManager;
import skyline.platform.system.manage.dao.PtOperBean;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.*;

;

/**
 * Created by XIANGYANG on 2014/8/11.
 */
@ManagedBean
@ViewScoped
public class OnlineUserAction implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(OnlineUserAction.class);

    private List<PtOperBean> ptOperBeanList;
    private String onlineUserNum;

    @PostConstruct
    public void init() {
        ptOperBeanList=new ArrayList<PtOperBean>();
        HashMap<String,OperatorManager> operMaps = OnLineOpersManager.getAllOperMaps((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext());
        Iterator iter = operMaps.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, OperatorManager> entry = (Map.Entry<String, OperatorManager>) iter.next();
            OperatorManager om = entry.getValue();
            PtOperBean onLineOper = om.getOperator();
            onLineOper.setSessionKey(entry.getKey());
            ptOperBeanList.add(onLineOper);
        }
        onlineUserNum=ptOperBeanList.size()+"";
    }

    public void killLineUser(PtOperBean ptOperBeanPara){
        try {
            ServletContext application=(ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            OnLineOpersManager.removeOperFromServer(ptOperBeanPara.getSessionKey(), application);
            HttpSession session= (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            if(session!=null){
                String strSessionKey=ptOperBeanPara.getSessionKey().substring(0,
                        ptOperBeanPara.getSessionKey().length() - ptOperBeanPara.getOperid().length());
                HashMap<String, HttpSession> sessions=(HashMap<String, HttpSession>)application.getAttribute("sessions");
                if (sessions.containsKey(strSessionKey)){
                    sessions.get(strSessionKey).invalidate();
                    sessions.remove(strSessionKey);
                }
            }
            init();
        } catch ( Exception ex ) {
        }
    }

    public List<PtOperBean> getPtOperBeanList() {
        return ptOperBeanList;
    }

    public void setPtOperBeanList(List<PtOperBean> ptOperBeanList) {
        this.ptOperBeanList = ptOperBeanList;
    }

    public String getOnlineUserNum() {
        return onlineUserNum;
    }

    public void setOnlineUserNum(String onlineUserNum) {
        this.onlineUserNum = onlineUserNum;
    }
}
