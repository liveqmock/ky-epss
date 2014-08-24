package skyline.security;

import skyline.platform.form.config.SystemAttributeNames;
import skyline.platform.security.OperatorManager;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 11-1-18
 * Time: 下午4:54
 * To change this template use File | Settings | File Templates.
 */
public class SecurityPhaseListener implements PhaseListener {
    public void afterPhase(PhaseEvent event) {
    }
    public void beforePhase(PhaseEvent event) {
        FacesContext fc = event.getFacesContext();
        ExternalContext ec = fc.getExternalContext();
        HttpSession session = (HttpSession) ec.getSession(true);
        HttpServletResponse response = (HttpServletResponse) ec.getResponse();
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        OperatorManager om = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
        if (om == null) {
            try {
                String path = request.getContextPath();
                response.setCharacterEncoding("GBK");
                response.getWriter().write("<script language=\"javascript\">alert ('JSF操作员超时，请重新登录！'); if(top){ top.location.href='" + path + "/pages/security/loginPage.jsp'; } else { location.href = '" + path + "/pages/security/loginPage.jsp';} </script>");
            } catch (IOException e) {
                System.out.println("Response 处理错误!");
            }
            fc.responseComplete();
        }
    }
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }
}
