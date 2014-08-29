package epss.view.security;

import epss.repository.model.Oper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.platform.security.OnLineOpersManager;
import skyline.platform.security.OperatorManager;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.*;

/**
 * Created by XIANGYANG on 2014/8/11.
 */
@ManagedBean
@ViewScoped
public class OnlineUserAction implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(OnlineUserAction.class);

    private List<Oper> operList;
    private String onlineUserNum;

    @PostConstruct
    public void init() {
        operList=new ArrayList<Oper>();
        ServletContext servletContext=
                (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        HashMap<String,OperatorManager> operMaps = OnLineOpersManager.getAllOperMaps(servletContext);
        Iterator iter = operMaps.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, OperatorManager> entry = (Map.Entry<String, OperatorManager>) iter.next();
            OperatorManager om = entry.getValue();
            Oper onLineOper = om.getOperator();
            onLineOper.setSessionKey(entry.getKey());
            operList.add(onLineOper);
        }
        onlineUserNum=operList.size()+"";
    }

    public void killLineUser(Oper operShowPara){
        try {
            ServletContext application=(ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            OnLineOpersManager.removeOperFromServer(operShowPara.getSessionKey(), application);
            HttpSession session= (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            if(session!=null){
                String strSessionKey=operShowPara.getSessionKey().substring(0,
                        operShowPara.getSessionKey().length() - operShowPara.getId().length());
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

    public List<Oper> getOperList() {
        return operList;
    }

    public void setOperList(List<Oper> operList) {
        this.operList = operList;
    }

    public String getOnlineUserNum() {
        return onlineUserNum;
    }

    public void setOnlineUserNum(String onlineUserNum) {
        this.onlineUserNum = onlineUserNum;
    }
}
